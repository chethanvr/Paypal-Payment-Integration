package com.hulkhiretech.payments.service.helper;

import java.util.Collections;
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
import com.hulkhiretech.payments.pojo.CreateOrderReq;
import com.hulkhiretech.payments.pojo.Order;
import com.hulkhiretech.payments.req.Amount;
import com.hulkhiretech.payments.req.ExperienceContext;
import com.hulkhiretech.payments.req.PayPal;
import com.hulkhiretech.payments.req.PaymentRequest;
import com.hulkhiretech.payments.req.PaymentSource;
import com.hulkhiretech.payments.req.PurchaseUnit;
import com.hulkhiretech.payments.service.AIChatService;
import com.hulkhiretech.payments.utils.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderHelper {
	

	@Value("${paypal.createOrderUrl}")
	private String creatOrderUrl;

	private final JsonUtils jsonUtils;

	private final AIChatService chatService;

	public HttpRequest prepareHttpRequestForCreateOrder(CreateOrderReq Req, String accesstoken) {
		HttpHeaders headerObj=new HttpHeaders();
		headerObj.setBearerAuth(accesstoken);
		headerObj.setContentType(MediaType.APPLICATION_JSON);
		headerObj.add(Constants.PAY_PAL_REQUEST_ID, Req.getTxnRef());

		Amount amount = new Amount();
		amount.setCurrencyCode(Req.getCurrency());
		amount.setValue(Req.getAmount());

		PurchaseUnit purchaseUnit = new PurchaseUnit();
		purchaseUnit.setAmount(amount);

		ExperienceContext experienceContext = new ExperienceContext();
		experienceContext.setPaymentMethodPreference(Constants.PMP_IMMEDIATE_PAYMENT_REQUIRED);
		experienceContext.setLandingPage(Constants.LANDING_PAGE_LOGIN);
		experienceContext.setShippingPreference(Constants.SP_NO_SHIPPING);
		experienceContext.setUserAction(Constants.UA_PAY_NOW);
		experienceContext.setReturnUrl(Req.getReturnUrl());
		experienceContext.setCancelUrl(Req.getCancelUrl());

		PayPal paypal = new PayPal();
		paypal.setExperienceContext(experienceContext);

		PaymentSource paymentSource = new PaymentSource();
		paymentSource.setPaypal(paypal);

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setIntent(Constants.INTENT_CAPTURE);
		paymentRequest.setPurchaseUnits(Collections.singletonList(purchaseUnit));
		paymentRequest.setPaymentSource(paymentSource);

		String responseBodyASJson=jsonUtils.toJson(paymentRequest);
		if(responseBodyASJson==null) {
			log.error("responseBodyASJson is null");
			throw new RuntimeException("responseBodyASJson is null");
		}

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpMethod(HttpMethod.POST);
		httpRequest.setUrl(creatOrderUrl);
		httpRequest.setHttpheaders(headerObj);
		httpRequest.setRequestbody(responseBodyASJson);
		log.info("HttpRequest called:"+httpRequest);
		return httpRequest;
	}
	public Order processcreateOrderResponse(ResponseEntity<String> createOrderResponse) {
		String responseBody = createOrderResponse.getBody();
		log.info("responseBody:" + responseBody);

		if (createOrderResponse.getStatusCode() == HttpStatus.OK) {
			PaypalOrder resObj = jsonUtils.fromJson(responseBody, PaypalOrder.class);
			log.info("resObj:" + resObj);

			if (resObj != null 
					&& resObj.getId() != null && !resObj.getId().isEmpty() 
					&& resObj.getStatus() != null && !resObj.getStatus().isEmpty()) {

				log.info("SUCCESS 201 with valid id and status");

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

		if (createOrderResponse.getStatusCode().is4xxClientError()
				|| createOrderResponse.getStatusCode().is5xxServerError()) {
			log.error("Paypal error response: {}", responseBody);

			String errorSummary = chatService.getPaypalErrorSummary(responseBody);
			log.info("errorSummary:" + errorSummary);
			
			throw new PaypalProviderException(
					ErrorCodeEnum.PAYPAL_ERROR.getCode(),
					errorSummary, 
					HttpStatus.valueOf(createOrderResponse.getStatusCode().value()));
		} 

		log.error("Got unexpected response from Paypal processing. "
				+ "Returnign GENERIC ERROR: {}", createOrderResponse);

		throw new PaypalProviderException(
				ErrorCodeEnum.GENERIC_ERROR.getCode(),
				ErrorCodeEnum.GENERIC_ERROR.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
