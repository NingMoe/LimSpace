package com.tyiti.easycommerce.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tyiti.easycommerce.entity.UploadLog;
import com.tyiti.easycommerce.service.UploadLogService;
import com.tyiti.easycommerce.util.oss.Callback;
import com.tyiti.easycommerce.util.oss.Policy;

@Controller
@RequestMapping("/oss")
public class OSSController {

	@Autowired
	private UploadLogService uploadLogService ; 
	@RequestMapping("/policy")
	public void policy(HttpServletRequest request, HttpServletResponse response) {

			try {
				new  Policy().policy(request, response);
				//System.out.println(new Date());
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}

	@RequestMapping("/callback")
	public void callback(HttpServletRequest request,
			HttpServletResponse response) throws NoSuchMethodException, SecurityException {

			try {
				//单独把callBack的参数拿出来 记日志
				String ossCallbackBody = GetPostBody(request.getInputStream(), Integer.parseInt(request.getHeader("content-length")));
				String a[] = ossCallbackBody.split("&");
				 UploadLog uploadLog = new UploadLog();
				 uploadLog.setCreateTime(new Date());
				for (int i = 0; i < a.length; i++) {
					String values[] = a[i].split("=");
					 if(values[0].equals("filename")){
						 uploadLog.setFilename(values[1].replaceAll("\"", ""));
					 }else if(values[0].equals("userId")){
						 uploadLog.setUserId(Integer.parseInt(values[1]));
					 }else if(values[0].equals("ipHost")){
						 uploadLog.setIpHost(values[1]);
					 }else if(values[0].equals("size")){
						 uploadLog.setSize(values[1]);
					 }else if(values[0].equals("mimeType")){
						 uploadLog.setMimetype(values[1].replaceAll("\"", ""));
					 }else if(values[0].equals("height")){
						 uploadLog.setHeight(Integer.parseInt(values[1]));
					 }else if(values[0].equals("width")){
						 uploadLog.setWidth(Integer.parseInt(values[1]));
					 } 
				}
				uploadLogService.insertUploadLog(uploadLog)	;
				new Callback().callback(request, response,ossCallbackBody);
				
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
	}
	
	public String GetPostBody(InputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					if (readLengthThisTime == -1) {// Should not happen.
						break;
					}
					readLen += readLengthThisTime;
				}
				return new String(message);
			} catch (IOException e) {
			}
		}
		return "";
	}
	
}
