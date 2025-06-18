package com.hulkhiretech.payments.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Amount {
	
	  @JsonProperty("currency_code")
	    private String currencyCode;

	    private String value;


}
