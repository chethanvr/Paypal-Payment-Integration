package com.hulkhiretech.payments.paypal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaypalOrder {
	  private String id;
	    private String status;

	    @JsonProperty("payment_source")
	    private PaymentSource paymentSource;

	    private List<Link> links;

	    // getters and setters
	}


