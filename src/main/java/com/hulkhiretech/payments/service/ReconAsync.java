package com.hulkhiretech.payments.service;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constants.ProviderEnum;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.impl.PaypalProviderHandler;
import com.hulkhiretech.payments.interfaces.ProviderHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReconAsync {
	
	private final ApplicationContext applicationContext;
	
	@Async
	public void reconTransactionAsync(TransactionDTO txn) {
		log.info("ReconAsync.reconTransactionAsync() called- txn: {}",txn);
		
		ProviderHandler providerHandler=null;
		if(txn.getProvider().equals(ProviderEnum.PAYPAL.getName())) {
			providerHandler=applicationContext.getBean(PaypalProviderHandler.class);
		}
		
		if(providerHandler==null) {
			log.error("ReconAsync.reconTransactionAsync-ProviderHandler is null for txn: {}",txn);
			return;
		}
		
		providerHandler.reconTransaction(txn);
		
	}

}
