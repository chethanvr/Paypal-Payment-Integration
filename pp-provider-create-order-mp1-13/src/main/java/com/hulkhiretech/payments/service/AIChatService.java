package com.hulkhiretech.payments.service;


import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIChatService {

	//private final ChatModel chatModel;

	private String paypalErrorSummaryPromptTemplate = """
				You are an technical analyst.
				Given the following json message from a third-party API, read the entire JSON, and summarize in 1 line:

				Instructions:
				1. Put a short, simple summary. Which exactly represents what error happened.
				2. Max length of summary less than 200 characters.
				3. Keep the output clear and concise.
				4. Summarize as message that we can sent in API response to the client.
				5. Dont point any info to read external documentation or link.

				{error_json}

			""";

	public String getPaypalErrorSummary(String jsonError) {
		
		return jsonError;//TODO, we are commenting AI chat service call for now.

		/*
		Map<String, Object> vars = Map.of("error_json", jsonError);

		Prompt prompt = new PromptTemplate(paypalErrorSummaryPromptTemplate).create(vars);
		log.info("prompt:" + prompt);

		String errorSummary = chatModel.call(prompt).getResult().getOutput().getText();
		log.info("errorSummary:" + errorSummary);

		return errorSummary;
		*/
	}
	
	@PostConstruct
	public void init() {
		
		/*
		if (chatModel instanceof OpenAiChatModel openAiModel) {
			System.out.println("openAiModel:" + openAiModel);
			System.out.println("getModel:" + openAiModel.getDefaultOptions().getModel());
		}
		*/

	}
}

