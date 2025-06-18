package com.hulkhiretech.payments.service.impl;

import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.pojo.CreateOrderReq;
import com.hulkhiretech.payments.service.TokenService;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	
	 private final TokenService tokenservice;
	@Override
	public String createOrder(CreateOrderReq Req){
		
		log.info("impl object Req:  {}",Req);
		
		
	String accesstoken=tokenservice.getAccessToken();
			
		return accesstoken;
	}
}
