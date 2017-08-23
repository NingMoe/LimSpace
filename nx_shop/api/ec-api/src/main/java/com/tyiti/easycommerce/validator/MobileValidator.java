package com.tyiti.easycommerce.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MobileValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		String mobile = (String) target;
		/* 
		@see http://baike.so.com/doc/5404570-5642289.html
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
	    联通：130、131、132、152、155、156、185、186 
	    电信：133、153、180、181、189、（1349卫通），4G号段 173、177 
	    总结起来就是第一位必定为1，第二位必定为3或5或7或8，其他位置的可以为0-9 
	    */  
	    String regex = "1[3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。 
		if (mobile.matches(regex)) {
			errors.rejectValue("mobile", "mobile.pattern");
		}
	}

}
