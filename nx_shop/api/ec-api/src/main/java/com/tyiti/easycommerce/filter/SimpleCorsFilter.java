package com.tyiti.easycommerce.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * 解决跨域问题
 * @author Bolor
 *
 * @date 2016年3月16日
 *
 */
@Component
public class SimpleCorsFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) req;
		/*
		String method = httpReq.getMethod();
		if (!method.equalsIgnoreCase("OPTIONS")) {
			return;
		}
		*/

		HttpServletResponse response = (HttpServletResponse) res;
		String originHeader = httpReq.getHeader("Origin");
		// java.net.MalformedURLException
		/*
		URL url = new java.net.URL(originHeader);
		String host = url.getHost();
		int port = url.getPort();
		String origin = (port != 80) ? host + ":" + port : host;
		*/

		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
		response.setHeader("Access-Control-Allow-Origin", originHeader);
		
		chain.doFilter(req, response);
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}