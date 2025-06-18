package com.hulkhiretech.payments.util.converter;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.TxnStatusEnum;

public class TxnStatusEnumConverter extends AbstractConverter<Integer, String> {

	@Override
	protected String convert(Integer source) {
		return TxnStatusEnum.FromId(source).getName();
	}

}
