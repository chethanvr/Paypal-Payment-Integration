package com.hulkhiretech.payments.interfaces;

import com.hulkhiretech.payments.dto.TransactionDTO;

public interface ProviderHandler {

	 void reconTransaction(TransactionDTO txn);
}
