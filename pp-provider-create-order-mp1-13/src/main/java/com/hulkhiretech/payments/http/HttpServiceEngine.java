package com.hulkhiretech.payments.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.PaypalProviderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpServiceEngine {

	private RestClient restClientInstance;

	public HttpServiceEngine(RestClient restClient) {
		this.restClientInstance = restClient;
	}
	

	public  ResponseEntity<String>  makeHttpCall(HttpRequest httpRequest) {

		log.info("MakehttpCall called|HttpRequest"+httpRequest);
		try {
			ResponseEntity<String> responseEntity=restClientInstance.method(httpRequest.getHttpMethod())
					.uri(httpRequest.getUrl())
					.headers( header-> header.addAll(httpRequest.getHttpheaders()))
					.body(httpRequest.getRequestbody())
					.retrieve()
					.toEntity(String.class);

			log.info("responseEntity:"+responseEntity);
			return responseEntity;

		}catch(HttpClientErrorException | HttpServerErrorException e){
			log.info("Http Error Occurred {}", e.getStatusCode(), e);

			HttpStatusCode status = e.getStatusCode();

			if (status == HttpStatus.SERVICE_UNAVAILABLE || status == HttpStatus.GATEWAY_TIMEOUT) {
				throw new  PaypalProviderException(ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL.getCode(),
						ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL.getMessage(),
						HttpStatus.valueOf(e.getStatusCode().value()));
			} 
			String errorJson =e.getResponseBodyAsString();
			return ResponseEntity.status(status).body(errorJson);

	    	}catch(Exception e) {
			log.info("Error occured on making httpcall {}",e.getMessage(),e);
			throw new  PaypalProviderException(ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL.getCode(),
					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_PAYPAL.getMessage(),
					HttpStatus.SERVICE_UNAVAILABLE);
		}

	}

}
