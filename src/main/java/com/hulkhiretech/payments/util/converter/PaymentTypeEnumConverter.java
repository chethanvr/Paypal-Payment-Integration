package com.hulkhiretech.payments.util.converter;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.PaymentTypeEnum;

public class PaymentTypeEnumConverter extends AbstractConverter<Integer, String> {

	@Override
	protected String convert(Integer source) {
		return PaymentTypeEnum.FromId(source).getName();
	}

}
