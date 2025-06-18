package com.hulkhiretech.payments.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.dao.interfaces.TransactionDAO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.entity.TransactionEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReconService {

	private final TransactionDAO transactionDAO;
	
	private final ReconAsync reconAsync;
	
	private final ModelMapper modelMapper;

	//@Scheduled(cron="0 0/10 * * * ?")
	public void reconTransactions() {
		log.info("Reconservice.recon() called");
		
		List<TransactionEntity> txnForRecon=transactionDAO.loadTransactionsForRecon();
		
		log.info("ReconService.recon() - txnForRecon.size(): {}", txnForRecon.size());
		
		List<TransactionDTO> txnDTOList=convertToDTOList(txnForRecon);
		log.info("ReconService.recon() - txnDTOList.size(): {}", txnDTOList.size());
		
		txnDTOList.forEach(txn->{
			log.info("ReconService.recon()- txn: {}",txn);
			reconAsync.reconTransactionAsync(txn);
			});
		
	}
		
	private List<TransactionDTO> convertToDTOList(List<TransactionEntity> entities) {
	    return entities.stream()
	            .map(entity -> modelMapper.map(entity, TransactionDTO.class))
	            .toList(); // Java 16+ style, returns an unmodifiable list
	}
}
