package com.hulkhiretech.payments.service.impl;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.PaypalProviderException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.paypal.Link;
import com.hulkhiretech.payments.paypal.PaypalOrder;
import com.hulkhiretech.payments.pojo.CreateOrderReq;
import com.hulkhiretech.payments.pojo.Order;
import com.hulkhiretech.payments.service.AIChatService;
import com.hulkhiretech.payments.service.TokenService;
import com.hulkhiretech.payments.service.helper.CaptureOrderHelper;
import com.hulkhiretech.payments.service.helper.CreateOrderHelper;
import com.hulkhiretech.payments.service.helper.GetOrderHelper;
import com.hulkhiretech.payments.service.interfaces.PaymentService;
import com.hulkhiretech.payments.utils.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final TokenService tokenService;

	private final HttpServiceEngine httpServiceEngine;

	private final CreateOrderHelper createOrderHelper;

	private final GetOrderHelper getOrderHelper;

	private final CaptureOrderHelper captureOrderHelper;

	private final AIChatService chatService;
	
	private final JsonUtils jsonUtils;

	@Override
	public Order createOrder(CreateOrderReq req) {
		log.info("createOrder called|req:{}", req);

		String accessToken = tokenService.getAccessToken();

		HttpRequest httpRequest = createOrderHelper.prepareHttpRequestForCreateOrder(req, accessToken);

		ResponseEntity<String> createOrderResponse = httpServiceEngine.makeHttpCall(httpRequest);

		Order response = createOrderHelper.processcreateOrderResponse(createOrderResponse);
		log.info("response:" + response);
		return response;
}


	@Override
	public Order getOrder(String orderId) {

		if(orderId.contains("TEMP1")) {
			throw new PaypalProviderException(
					ErrorCodeEnum.TEMP1_ERROR.getCode(), 
					ErrorCodeEnum.TEMP1_ERROR.getMessage(),
					HttpStatus.BAD_REQUEST);
		}

		if(orderId.contains("TEMP3")) {
			throw new PaypalProviderException(
					ErrorCodeEnum.TEMP3_ERROR.getCode(), 
					ErrorCodeEnum.TEMP3_ERROR.getMessage(),
					HttpStatus.BAD_REQUEST);
		}

		/*
		String name = null;
		if(name == null) {
			throw new PaypalProviderException(
					ErrorCodeEnum.NAME_NULL.getCode(), 
					ErrorCodeEnum.NAME_NULL.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
			// 500 INTERNAL SERVER ERROR
		}
		name.length();
		 */

		String accessToken = tokenService.getAccessToken();

		HttpRequest httpRequest = getOrderHelper.prepareHttpRequest(orderId, accessToken);

		ResponseEntity<String> getOrderResponse = httpServiceEngine.makeHttpCall(httpRequest);

		Order orderRes = getOrderHelper.processGetOrderResponse(getOrderResponse);
		log.info("response:" + orderRes);
		return orderRes;
	}

	@Override
	public Order captureOrder(String orderId) {

		String accessToken = tokenService.getAccessToken();

		HttpRequest httpRequest = captureOrderHelper.prepareHttpRequest(orderId, accessToken);

		ResponseEntity<String> captureOrderResponse = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("captureOrderResponse: {}",captureOrderResponse);

		Order orderRes = captureOrderHelper.processcaptureOrderResponse(captureOrderResponse);
		log.info("response:" + orderRes);
		return orderRes;
	}
}
