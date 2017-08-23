package com.tyiti.easycommerce.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 不需要认证的请求
 * 
 * @author Bolor
 *
 * @date 2016年3月24日
 *
 */
public class NotAuthUrl {

	private List<String> notAuth = new ArrayList<String>();

	public List<String> getNotAuth() {
		return notAuth;
	}

	public void setNotAuth(List<String> notAuth) {
		this.notAuth = notAuth;
	}
}
