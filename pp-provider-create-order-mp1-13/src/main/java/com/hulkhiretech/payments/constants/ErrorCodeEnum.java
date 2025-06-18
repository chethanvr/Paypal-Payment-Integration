package com.hulkhiretech.payments.constants;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
	GENERIC_ERROR("40000", "Unable to process your request, Try again later"),
    TEMP1_ERROR("40001", "Temp1 error."),
    TEMP2_ERROR("40002", "Temp2 error."),
    TEMP3_ERROR("40003", "Temp3 error."),
	NAME_NULL("40004","Name is empty please check"),
	UNABLE_TO_CONNECT_TO_PAYPAL("40005", "Unable to connect to PayPal, please try again later"),
	PAYPAL_ERROR("40006", "<PREPARE DYNAMIC MESSAGE FROM PAYPAL ERROR RESPONSE>");


    private final String code;
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}

