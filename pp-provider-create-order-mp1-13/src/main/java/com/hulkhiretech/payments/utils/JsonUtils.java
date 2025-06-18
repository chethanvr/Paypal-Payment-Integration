package com.hulkhiretech.payments.utils;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor
public class JsonUtils {
	
	private final ObjectMapper objectMapper;
	
	public String toJson(Object obj) {
	
	 String responseBodyASJson=null;
		try {
			responseBodyASJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	return responseBodyASJson;
	}
	
	public <T> T fromJson(String json, Class<T> clazz) {		
		T response = null;
		
		try {
			response = objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return response;
	}

}


