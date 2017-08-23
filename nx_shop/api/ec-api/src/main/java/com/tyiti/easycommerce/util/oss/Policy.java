package com.tyiti.easycommerce.util.oss;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;

public class Policy {
	public void policy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		//读配置文件
		 Properties props = new Properties();
		 InputStream inputStream;        
		 ClassLoader cl = Policy. class .getClassLoader();   
		 inputStream = cl.getResourceAsStream( "ossconfig.properties" );  
		 props.load(inputStream);
		//配置文件参数赋值 
	    String endpoint = props.getProperty("endpoint");//阿里云服务器地址
        String accessId = props.getProperty("accessId");//key id  
        String accessKey = props.getProperty("accessKey");//秘钥
        String bucket = props.getProperty("bucket");//bucket 
        String dir = props.getProperty("dir");//文件夹
        String host = "http://" + bucket + "." + endpoint.substring(7); //文件地址
        String timeout = props.getProperty("timeout");//文件夹
        String callbackUrl = props.getProperty("callbackUrl");
        String callbackHost =  props.getProperty("callbackHost");
        
        String ipHost = getIpAddress(request);
        OSSClient client = new OSSClient(endpoint, accessId, accessKey); // 连接服务器
        try { 	
        	long expireTime = Long.valueOf(timeout); //设置失效时间(秒)
        	long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);
            
            Map<String, Object> respMap = new LinkedHashMap<String, Object>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            //respMap.put("expire", formatISO8601Date(expiration));
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            //callback　设置　　
            String user = request.getSession().getAttribute("userId")+"";
            Map<String, Object> callbackMap = new LinkedHashMap<String, Object>();
            callbackMap.put("callbackUrl", callbackUrl); 
            callbackMap.put("callbackHost", callbackHost);
            callbackMap.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}&userid="+user+"&ipHost="+ipHost);
            callbackMap.put("callbackBodyType", "application/json");
            String callback_string = String.valueOf(JSONObject.fromObject(callbackMap));
            String base64_callback_body = BinaryUtil.toBase64String(callback_string.getBytes("utf-8"));
            
            //自定义参数
                 
            respMap.put("callback", base64_callback_body);
            JSONObject ja1 = JSONObject.fromObject(respMap);
//            System.out.println(ja1.toString());
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            response(request, response, ja1.toString());
            
        } catch (Exception e) {
        	System.err.println(e.getMessage());
        }
    }
	
	private void response(HttpServletRequest request, HttpServletResponse response, String results) throws IOException {
 		String callbackFunName = request.getParameter("callback");
		if (callbackFunName==null || callbackFunName.equalsIgnoreCase(""))
			response.getWriter().println(results);
		else
			response.getWriter().println(callbackFunName + "( "+results+" )");
//		System.out.println(callbackFunName + "( "+results+" )");
		response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
	}
	public static void main(String[] args) throws ServletException, IOException {
		HttpServletRequest request = null  ; 
		HttpServletResponse response = null ; 
		//测试properties参数 是否有问题
		new Policy().policy(request, response);
	}
	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress = null;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();

			// 这里主要是获取本机的ip,可有可无
			if (ipAddress.equals("127.0.0.1")
					|| ipAddress.endsWith("0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		// 或者这样也行,对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		// return
		// ipAddress!=null&&!"".equals(ipAddress)?ipAddress.split(",")[0]:null;
		return ipAddress;
	}
}
