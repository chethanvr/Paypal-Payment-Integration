package com.hulkhiretech.payments.constants;

import lombok.Getter;

@Getter
public enum TxnStatusEnum {
CREATED(1, "CREATED"),
INITIATED(2, "INITIATED"),
PENDING(3, "PENDING"),
APPROVED(4, "APPROVED"),
SUCCESS(5, "SUCCESS"),
FAILED(6, "FAILED");

private final int id;
private final String name;

TxnStatusEnum(int id, String name) {
    this.id = id;
    this.name = name;
}

public static TxnStatusEnum FromId(int id) {
    for (TxnStatusEnum method : values()) {
        if (method.id == id) return method;
    }
    return null;
}

public static TxnStatusEnum FromName(String name) {
    for (TxnStatusEnum method : values()) {
        if (method.name.equalsIgnoreCase(name)) return method;
    }
    return null;
}
}
