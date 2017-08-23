package com.tyiti.easycommerce.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

@Component
public class AjaxUserFilter extends UserFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request,
        ServletResponse response) throws Exception {
    
		HttpServletResponse res = WebUtils.toHttp(response);
		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		res.setHeader("Content-type", "application/json");
		res.getWriter().write("{\"code\":401,\"message\":\"Unauthorized\"}");
		// res.sendError(401);
		return false;
		// 判断是 Ajax 请求
		/*
        HttpServletRequest req = WebUtils.toHttp(request);
        String xmlHttpRequest = req.getHeader("X-Requested-With");
        if ( xmlHttpRequest != null )
            if ( xmlHttpRequest.equalsIgnoreCase("XMLHttpRequest") )  {
                HttpServletResponse res = WebUtils.toHttp(response);
                res.sendError(401);
                return false;
        }
        */
    
        // return super.onAccessDenied(request, response);
    }
    
    @Override
    public boolean isAccessAllowed(ServletRequest request,
            ServletResponse response,
            Object mappedValue) {
        HttpServletRequest req = WebUtils.toHttp(request);
        if (req.getMethod().equals("OPTIONS")) {
        		return true;
        }
        
        return super.isAccessAllowed(request, response, mappedValue);
    }
    
}
