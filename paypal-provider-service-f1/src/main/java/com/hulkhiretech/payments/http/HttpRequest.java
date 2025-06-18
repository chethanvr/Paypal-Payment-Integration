package com.hulkhiretech.payments.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import lombok.Data;

@Data
public class HttpRequest {
	
	private HttpMethod httpMethod;
	private String Url;
	private HttpHeaders httpheaders;
	private Object requestbody;

}
