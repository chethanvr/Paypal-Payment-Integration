package com.hulkhiretech.payments.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OAuthToken {

	private String scope;

	@JsonProperty("access_token")
	private String accesstoken;

	@JsonProperty("token_type")
	private String tokentype;

	@JsonProperty("app_id")
	private String appid;

	@JsonProperty("expires_in")
	private String expiresin;

	private String nonce;



}
