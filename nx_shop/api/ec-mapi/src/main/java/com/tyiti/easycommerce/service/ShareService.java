package com.tyiti.easycommerce.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tyiti.easycommerce.entity.Share;

public interface ShareService {

	Map<String, Object> getShareList(Share share);
	
	Map<String, Object> shareToExecl(HttpServletResponse response,
			HttpServletRequest request, Share share);
}