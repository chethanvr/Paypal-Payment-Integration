package com.hulkhiretech.payments.http;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.PaymentServiceException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

/**
 * Here we lookup destination service using LoadBalancerClient 
 * and make the RestClient API Call
 * 
 * Commenting this, since we want to use HttpServiceEngine instead, 
 * which directly makes call without SR.
 */
//@Component
@Slf4j
public class HttpServiceEngineSR {

	private RestClient restClient;
	
	private LoadBalancerClient loadBalancerClient;

	public HttpServiceEngineSR(RestClient restClient, 
			LoadBalancerClient loadBalancerClient) {
		this.restClient = restClient;
		this.loadBalancerClient = loadBalancerClient;
	}

	@CircuitBreaker(name = "payment-processing-service", 
			fallbackMethod = "fallbackProcessPayment")
	public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
		log.info("makeHttpCall called|httpRequest:" + httpRequest);

		try {
			
			// if destinationServiceName is null, then throw exception
			if (httpRequest.getDestinationServiceName() == null) {
				log.error("No destination service name provided");
				throw new PaymentServiceException(
						ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getCode(),
						ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getMessage(), 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			ServiceInstance destinationService = loadBalancerClient.choose(
					httpRequest.getDestinationServiceName());
		
			if (destinationService == null) {
				log.error("No available service instance found for paypal-provider-service");
				throw new PaymentServiceException(
						ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getCode(),
						ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getMessage(), 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			// Construct the URL using the service instance's URI
			String url = destinationService.getUri() + httpRequest.getUrl();
			log.info("url:" + url);
			
			ResponseEntity<String> responseEntity = restClient.method(httpRequest.getHttpMethod())
					.uri(url)
					.headers(header -> header.addAll(httpRequest.getHttpheaders()))
					.body(httpRequest.getRequestbody())
					.retrieve()
					.toEntity(String.class);
			log.info("responseEntity:" + responseEntity);
			return responseEntity;

		} catch(HttpClientErrorException | HttpServerErrorException e) {
			log.error("HTTP error occurred: {}", e.getStatusCode(), e);

		    HttpStatusCode status = e.getStatusCode();

		    // Check for Service Unavailable (503) or Gateway Timeout (504)
		    if (status == HttpStatus.SERVICE_UNAVAILABLE || status == HttpStatus.GATEWAY_TIMEOUT) {

		    	throw new PaymentServiceException(
						ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getCode(),
						ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getMessage(), 
						HttpStatus.valueOf(e.getStatusCode().value()));
		    }

		    // For other client/server errors, return the error response body as-is
		    String errorJson = e.getResponseBodyAsString(); // PayPal may send structured error JSON
		    return ResponseEntity.status(status).body(errorJson);
			
		} catch (Exception e) {// timeout case - No response from paypal
			log.error("Exception occurred while making HTTP call: {}", e.getMessage(), e);
			
			throw new PaymentServiceException(
					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getCode(),
					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}

	public ResponseEntity<String> fallbackProcessPayment(
			HttpRequest httpRequest, Throwable t) {
		// Handle fallback logic here
		log.error("Fallback method called due to: {}", t.getMessage(), t);
		throw new PaymentServiceException(
				ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getCode(),
				ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL_PROVIDER.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
