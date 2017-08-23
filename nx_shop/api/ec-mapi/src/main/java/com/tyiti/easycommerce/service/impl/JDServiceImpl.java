package com.tyiti.easycommerce.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.JdAccessToken;
import com.tyiti.easycommerce.service.JDService;
import com.tyiti.easycommerce.util.HttpClientUtil;

import net.sf.json.JSONObject;

@Service
public class JDServiceImpl implements JDService {

	Logger logger = Logger.getLogger(JDServiceImpl.class);
	
	@Value("${jd_app_key}")
	private String appKey;
	
	@Value("${jd_app_secret}")
	private String appSecret;
	
	@Value("${jd_username}")
	private String userName;
	
	@Value("${jd_password}")
	private String password;
	
	private final static String jdUrl = "https://router.jd.com/api?";
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	
	/**
	 * 查询商品池编号
	 */
	@Override
	public void queryPoolNum() {
		String accessToken = JdAccessToken.getAccessToken();
		String timestamp = df.format(new Date());
		String requestBody = "method=biz.product.PageNum.query&app_key="+appKey+
				"&access_token="+accessToken+"&timestamp="+timestamp+"&v=1.0&format=json&param_json={}";
		StringBuffer buffer = HttpClientUtil.httpsRequest(jdUrl,
				"POST", requestBody);
		JSONObject msgStr = JSONObject.fromObject(buffer.toString());
		logger.info("查询商品池编号："+msgStr);
		String resultMessage = msgStr.getString("biz_product_PageNum_query_response");
		JSONObject response = JSONObject.fromObject(resultMessage);
		String success = response.getString("success");
		if (success!=null && success.equals("true")) {
//			String result = response.getString("result");
			/*if (result!=null) {
				
			}else {
				logger.info("商品池为空！");
			}*/
		}else {
			logger.info("查询商品池编号失败，错误编码为："+response.getString("resultCode")+",失败原因为："+response.getString("resultMessage"));
		}
	}
	

}
