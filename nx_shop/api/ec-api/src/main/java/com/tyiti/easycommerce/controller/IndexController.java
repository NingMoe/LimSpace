package com.tyiti.easycommerce.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tyiti.easycommerce.common.ConfigKey;
import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.SysConfig;

@Controller
@RequestMapping(value = "/index")
public class IndexController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String ceb(@RequestParam(value = "id", required = false) String thirdPartyId, HttpSession session) {
		String sessionThirdPartyId = (String) session.getAttribute(Constants.THIRD_PARTY_ID);
		
		// 不存在
		if (sessionThirdPartyId == null) {
			session.setAttribute(Constants.THIRD_PARTY_ID, thirdPartyId);
		} else {
			if (!sessionThirdPartyId.equals(thirdPartyId)) {
				session.setAttribute(Constants.THIRD_PARTY_ID, thirdPartyId);
			}
		}
		
		return "redirect:" + SysConfig.configMap.get(ConfigKey.WX_REDIRECT_URL_PREFIX) + "#home";
	}

	// 重定向，中转页
	@RequestMapping(method = RequestMethod.POST)
	public String entrance(@RequestParam String openId,
			@RequestParam String openUrl, HttpSession session) {
		// 中转，将获取到的参数通过 Set-Cookie 返回客户端，同时 302 到目标 URL
		openId = Base64Utils.encodeToString(openId.getBytes());
		String sessionThirdPartyId = (String) session.getAttribute(Constants.THIRD_PARTY_ID);

		// 不存在
		if (sessionThirdPartyId == null) {
			session.setAttribute(Constants.THIRD_PARTY_ID, openId);
		} else {
			if (!sessionThirdPartyId.equals(openId)) {
				session.setAttribute(Constants.THIRD_PARTY_ID, openId);
			}
		}

		return "redirect:" + openUrl;
	}
}
