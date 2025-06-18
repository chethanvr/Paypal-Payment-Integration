package com.hulkhiretech.payments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.pojo.CreateOrderReq;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/paypal/order")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
	
	private  final PaymentService paymentservice;
	
	 
    @PostMapping
	public String createOrder(@RequestBody CreateOrderReq createOrderReq) {
    	
    	
    	
    	log.info("createOrderReq:  {}",createOrderReq);
    	
    	String Response=paymentservice.createOrder(createOrderReq);

    	createOrderReq.getAmount();
		return "createOrder returning from"
				+ "CreateOrder"+createOrderReq
				+"\nResponse:"+ Response;	
		}

}
