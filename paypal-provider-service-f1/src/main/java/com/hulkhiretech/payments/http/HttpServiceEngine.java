package com.hulkhiretech.payments.http;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpServiceEngine {

	private RestClient restClient;

	public HttpServiceEngine(RestClient.Builder restClientBuilder) {
		restClient=restClientBuilder.build();
		log.info("restClient created:"+restClient);
	}

	public  ResponseEntity<String>  makeHttpCall(HttpRequest httpRequest) {

		log.info("MakehttpCall called|HttpRequest"+httpRequest);

		ResponseEntity<String> responseEntity=restClient.method(httpRequest.getHttpMethod())
				.uri(httpRequest.getUrl())
				.headers( header-> header.addAll(httpRequest.getHttpheaders()))
				.body(httpRequest.getRequestbody())
				.retrieve()
				.toEntity(String.class);

		log.info("responseEntity:"+responseEntity);
		return responseEntity;
	}



}
