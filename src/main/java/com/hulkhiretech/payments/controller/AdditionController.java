package com.hulkhiretech.payments.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.service.ReconService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AdditionController {
	
	private Logger logger = LoggerFactory.getLogger(AdditionController.class);
	
	private final ReconService reconservice;
	
    @PostMapping("/add")
    public int add(@RequestParam int num1, @RequestParam int num2) {
    	
        logger.info("num1:{}|num2:{}", num1, num2);
        
        int sumResult = num1 + num2;
        logger.info("sumResult:{}", sumResult);

        return sumResult;
    }
    
    @PostMapping("/recon")
    public String triggerRecon() {
    	log.trace("Addcontroller.triggerRecon(), called");
    	log.debug("Addcontroller.triggerRecon(), called");
    	log.info("Addcontroller.triggerRecon(), called");
    	log.warn("Addcontroller.triggerRecon(), called");
    	log.error("Addcontroller.triggerRecon(), called");
    	
    	
    	//reconservice.reconTransactions();
    	
    	return "Recon Triggered";
    }
}
