package com.hulkhiretech.payments.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payments.constants.Constants;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.paypal.OAuthToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

	private  final HttpServiceEngine httpServiceEngine;

	@Value("${paypal.clientId}")
	private String clientId;

	@Value("${paypal.clientSecret}")
	private String ClientSecret;

	@Value("${paypal.oAuthUrl}")
	private String oAuthUrl;

	private final ObjectMapper objectMapper;

	private static String accessToken;

	public String getAccessToken() {
		log.info("getAccesstoken Called");

		if(accessToken != null)
		{
			log.info("getAccessToken returning | AccessToken {}",accessToken );
			return accessToken;
		}

		log.info("Calling paypal for generating new AccessToken");
		HttpHeaders headerObj=new HttpHeaders();
		headerObj.setBasicAuth(clientId,ClientSecret);
		headerObj.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(Constants.GRANT_TYPE, Constants.CLIENT_CREDENTIALS);

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpMethod(HttpMethod.POST);
		httpRequest.setUrl(oAuthUrl);
		httpRequest.setHttpheaders(headerObj);
		httpRequest.setRequestbody(params);

		log.info("HttpRequest called:"+httpRequest);
		ResponseEntity<String>  oAuthResponse = httpServiceEngine.makeHttpCall(httpRequest);

		String ResponseBody=oAuthResponse.getBody();
		log.info("ResponseBody :"+ResponseBody);

		try {
			OAuthToken oAuthObj=objectMapper.readValue(ResponseBody,OAuthToken.class);
			accessToken=oAuthObj.getAccesstoken();
			log.info("AccessToken: {}",accessToken);
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("getAccessToken returning accesstoken"+accessToken);
		return accessToken;

	}

}
