package com.hulkhiretech.payments.service.helper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hulkhiretech.payments.constants.Constants;
import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.PaypalProviderException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.paypal.Link;
import com.hulkhiretech.payments.paypal.PaypalOrder;
import com.hulkhiretech.payments.pojo.Order;
import com.hulkhiretech.payments.service.AIChatService;
import com.hulkhiretech.payments.utils.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetOrderHelper {

	@Value("${paypal.getOrderUrl}")
	private String getOrderUrl;
	
	private final JsonUtils jsonUtils;

	private final AIChatService chatService;

	public HttpRequest prepareHttpRequest(String orderId, String accessToken) {
		HttpHeaders headerObj = new HttpHeaders();
		headerObj.setBearerAuth(accessToken);
		headerObj.setContentType(MediaType.APPLICATION_JSON);

		String url = getOrderUrl;
		url = url.replace("{orderId}", orderId);
		
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpMethod(HttpMethod.GET);

		httpRequest.setUrl(url);
		httpRequest.setHttpheaders(headerObj);
		httpRequest.setRequestbody(Constants.EMPTY_STRING);
		log.info("httpRequest:" + httpRequest);
		return httpRequest;
	}
	
	public Order processGetOrderResponse(ResponseEntity<String> getOrderResponse) {
		String responseBody = getOrderResponse.getBody();
		log.info("responseBody:" + responseBody);

		if (getOrderResponse.getStatusCode() == HttpStatus.OK) {
			PaypalOrder resObj = jsonUtils.fromJson(responseBody, PaypalOrder.class);
			log.info("resObj:" + resObj);

			if (resObj != null 
					&& resObj.getId() != null && !resObj.getId().isEmpty() 
					&& resObj.getStatus() != null && !resObj.getStatus().isEmpty()) {

				log.info("SUCCESS 200 with valid id and status");

				Order orderRes = new Order();
				orderRes.setOrderId(resObj.getId());
				orderRes.setPaypalStatus(resObj.getStatus());

				Optional<String> opRedirectUrl = resObj.getLinks().stream()
						.filter(link -> "payer-action".equalsIgnoreCase(link.getRel()))
						.map(Link::getHref)
						.findFirst();

				orderRes.setRedirectUrl(opRedirectUrl.orElse(null));

				log.info("orderRes:{}", orderRes);

				return orderRes;

			} 
			log.error("SUCCESS 200 but invalid id or status");
		}

		if (getOrderResponse.getStatusCode().is4xxClientError()
				|| getOrderResponse.getStatusCode().is5xxServerError()) {
			log.error("Paypal error response: {}", responseBody);

			String errorSummary = chatService.getPaypalErrorSummary(responseBody);
			log.info("errorSummary:" + errorSummary);
			
			throw new PaypalProviderException(
					ErrorCodeEnum.PAYPAL_ERROR.getCode(),
					errorSummary, 
					HttpStatus.valueOf(getOrderResponse.getStatusCode().value()));
		} 

		log.error("Got unexpected response from Paypal processing. "
				+ "Returnign GENERIC ERROR: {}", getOrderResponse);

		throw new PaypalProviderException(
				ErrorCodeEnum.GENERIC_ERROR.getCode(),
				ErrorCodeEnum.GENERIC_ERROR.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
