package com.tyiti.easycommerce.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.JdAccessToken;
import com.tyiti.easycommerce.service.JdOrderService;
import com.tyiti.easycommerce.util.HttpClientUtil;

@Service
public class JdOrderServiceImpl implements JdOrderService {

	// 分配给应用的AppKey，用于标记一个唯一的接入方
	@Value("${jdAPPKey}")
	private String jdAPPKey;
	// Oauth2颁发的动态令牌
	@Value("${jdAPPSecret}")
	private String jdAPPSecret;
	// 京东分配的接入账号，用户获取token以及生成订单时打标记
	@Value("${jdName}")
	private String jdName;
	// 京东分配的接入密码
	@Value("${jdPassWord}")
	private String jdPassWord;
	
	
	public Map<String, Object> addOrder(String method, Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		
		return data;
	}
	

	/**
	 * 获取京东AccessToken
	 *
	 * */
	@Override
	public String getAccessToken() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jdAPPKey", jdAPPKey);
		map.put("jdAPPSecret", jdAPPSecret);
		map.put("jdName", jdName);
		map.put("jdPassWord", jdPassWord);
		// 获取京东的AccessToken
		return JdAccessToken.getAccessToken(map);
	}

	/**
	 * 公用方法
	 * @param method api 名称
	 * @param map 参数
	 * @return
	 */
	public String httpsRequest(String method, Map<String, Object> map) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		// 京东地址
		String jdUrl = "https://router.jd.com/api?method=" + method
				+ "&app_key=" + jdAPPKey + "&access_token=" + getAccessToken()
				+ "&timestamp=" + df.format(new Date())
				+ "&v=1.0&format=json&param_json=" + map;
		// 请求京东服务器
		StringBuffer buffer = HttpClientUtil.httpsRequest(jdUrl, "GET", "");
		// 返回京东消息
		return buffer.toString();
	}
}