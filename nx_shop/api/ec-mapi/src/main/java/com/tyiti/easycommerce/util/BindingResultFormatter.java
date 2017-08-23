package com.tyiti.easycommerce.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingResultFormatter {
	public static Map<String, Object> getMap(BindingResult bindingResult, boolean onlyFirst) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<FieldError> errors = bindingResult.getFieldErrors();
		for (FieldError error : errors ) {
			
			map.put(error.getField(), error.getDefaultMessage());
	        System.out.println (error.getObjectName() + " - " + error.getDefaultMessage());
	        if (onlyFirst) {
	        		break;
	        }
		}
		
		return map;
	}
	
	public static String getFirstErrorMessage(BindingResult bindingResult) {
		List<FieldError> errors = bindingResult.getFieldErrors();
		for (FieldError error : errors ) {
			return error.getDefaultMessage();
		}
		return "";
	}
}
