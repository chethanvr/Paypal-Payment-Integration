package com.hulkhiretech.payments.constants;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
		APM(1, "APM");
	private final int id;
	private final String name;

	PaymentMethodEnum(int id, String name) {
	    this.id = id;
	    this.name = name;
	}

	public static PaymentMethodEnum FromId(int id) {
	    for (PaymentMethodEnum method : values()) {
	        if (method.id == id) return method;
	    }
	    return null;
	}

	public static PaymentMethodEnum FromName(String name) {
	    for (PaymentMethodEnum method : values()) {
	        if (method.name.equalsIgnoreCase(name)) return method;
	    }
	    return null;
	}

}
