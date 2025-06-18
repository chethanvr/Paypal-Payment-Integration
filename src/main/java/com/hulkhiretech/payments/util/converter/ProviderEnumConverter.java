package com.hulkhiretech.payments.util.converter;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.ProviderEnum;

public class ProviderEnumConverter extends AbstractConverter<Integer, String> {

	@Override
	protected String convert(Integer source) {
		return ProviderEnum.FromId(source).getName();
	}

}
