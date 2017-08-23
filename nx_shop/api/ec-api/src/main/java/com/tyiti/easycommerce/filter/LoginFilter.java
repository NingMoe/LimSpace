package com.tyiti.easycommerce.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.User;

public class LoginFilter implements Filter {
	private NotAuthUrl notAuthUrl;

	public LoginFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());
		notAuthUrl = (NotAuthUrl) ctx.getBean("notAuthUrl");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String method = req.getMethod();
		if (method.equalsIgnoreCase("OPTIONS")) {
			return;
		}

		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(true);
		//HttpSession session = req.getSession(false);

		String url = req.getRequestURI().substring(req.getContextPath().length() + 1);

		if (!isNotloginUrl(method, url)) {
			User user = (User) session.getAttribute(Constants.USER);
			boolean isLogin = user != null;

			// 未登录
			if (!isLogin) {
				res.sendError(401);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * 不登录即可访问的页面
	 * 
	 * @param url
	 * @return
	 */
	private boolean isNotloginUrl(String method, String url) {
		Pattern p = null;
		Matcher m = null;
		for (String not : notAuthUrl.getNotAuth()) {
			if (not.indexOf(" ") != -1) {
				url = method + " " + url;
			}
			p = Pattern.compile(not);
			m = p.matcher(url);
			if (m.find())
				return true;
		}
		return false;
	}

	public void destroy() {

	}
}
