package com.hulkhiretech.payments.constants;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
SALE(1, "SALE");


private final int id;
private final String name;

PaymentTypeEnum(int id, String name) {
    this.id = id;
    this.name = name;
}

public static PaymentTypeEnum FromId(int id) {
    for (PaymentTypeEnum method : values()) {
        if (method.id == id) 
        	return method;
    }
    return null;
}

public static PaymentTypeEnum FromName(String name) {
    for (PaymentTypeEnum method : values()) {
        if (method.name.equalsIgnoreCase(name)) 
        	return method;
    }
    return null;
}
}
