package com.hulkhiretech.payments.constants;

import lombok.Getter;

@Getter
public enum PaypalStatusEnum {
	PAYER_ACTION_REQUIRED("PAYER_ACTION_REQUIRED"),
	APPROVED("APPROVED"),
	COMPLETED("COMPLETED");
	
	private String name;
	
	 PaypalStatusEnum(String name) {
		this.name=name;
	}

	 public static PaypalStatusEnum FromString(String name) {
			for (PaypalStatusEnum status : PaypalStatusEnum.values()) {
				if (status.getName().equalsIgnoreCase(name)) {
                   return status;
			}
			}
			return null;
		}
}
