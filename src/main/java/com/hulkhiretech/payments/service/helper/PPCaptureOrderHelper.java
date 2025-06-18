package com.hulkhiretech.payments.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constants.Constant;
import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.exception.PaymentServiceException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.paypalprovider.PPErrorResponse;
import com.hulkhiretech.payments.paypalprovider.PPOrder;
import com.hulkhiretech.payments.util.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PPCaptureOrderHelper {
	
	private final JsonUtils jsonUtils;
	
	@Value("${paypalprovider.captureOrderurl}")
	 private String ppCaptureOrderurl;
	
	public HttpRequest prepareHttpRequest(TransactionDTO txn) {
		HttpHeaders httHeaders=new HttpHeaders();
		httHeaders.setContentType(MediaType.APPLICATION_JSON);
		

		String url = ppCaptureOrderurl;
		url = url.replace(Constant.ORDER_ID,txn.getProviderReference());
		
		HttpRequest httpRequest=new HttpRequest();
		httpRequest.setUrl(url);
		httpRequest.setHttpMethod(HttpMethod.POST);
		httpRequest.setRequestbody(Constant.EMPTY_STRING);
		httpRequest.setHttpheaders(httHeaders);
		httpRequest.setDestinationServiceName(Constant.PAYPAL_PROVIDER_SERVICE);
		return httpRequest;
}
	public PPOrder processResponse(ResponseEntity<String> captureOrderResponse) {
		String responseBody = captureOrderResponse.getBody();
		log.info("responseBody:" + responseBody);

		if (captureOrderResponse.getStatusCode() == HttpStatus.OK) {
			PPOrder resObj = jsonUtils.fromJson(responseBody, PPOrder.class);
			log.info("resObj:" + resObj);

			if (resObj != null 
					&& resObj.getOrderId() != null && !resObj.getOrderId().isEmpty() 
					&& resObj.getPaypalStatus() != null && !resObj.getPaypalStatus().isEmpty()) {

				log.info("SUCCESS 200 with valid id and status");


				log.info("resObj:{}", resObj);

				return resObj;

			} 
			log.error("SUCCESS but invalid id or status");
		}

		if (captureOrderResponse.getStatusCode().is4xxClientError()
				|| captureOrderResponse.getStatusCode().is5xxServerError()) {
			log.error("Paypal error response: {}", responseBody);
			
			PPErrorResponse errorRes = jsonUtils.fromJson(responseBody, PPErrorResponse.class);
			log.error("errorRes: {}", errorRes);

			throw new PaymentServiceException(
					errorRes.getErrorCode(),
					errorRes.getErrorMessage(), 
					HttpStatus.valueOf(captureOrderResponse.getStatusCode().value()));
					
		} 

		log.error("Got unexpected response from Paypal processing. "
				+ "Returnign GENERIC ERROR: {}", captureOrderResponse);
		
		throw new PaymentServiceException(
				ErrorCodeEnum.GENERIC_ERROR.getCode(),
				ErrorCodeEnum.GENERIC_ERROR.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

}
