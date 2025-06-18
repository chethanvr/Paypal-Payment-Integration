package com.hulkhiretech.payments.constants;

import lombok.Getter;

@Getter
public enum ProviderEnum {
	PAYPAL(1, "PAYPAL");

	private final int id;
	private final String name;

	ProviderEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static ProviderEnum FromId(int id) {
		for (ProviderEnum method : values()) {
			if (method.id == id) return method;
		}
		return null;
	}

	public static ProviderEnum FromName(String name) {
		for (ProviderEnum method : values()) {
			if (method.name.equalsIgnoreCase(name)) return method;
		}
		return null;
	}
}