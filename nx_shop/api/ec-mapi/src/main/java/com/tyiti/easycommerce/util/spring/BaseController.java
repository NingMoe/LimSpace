package com.tyiti.easycommerce.util.spring;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * spring所有controller从此继承
 * @author rainyhao
 * @since 2016-4-11 下午5:53:19
 */
@SuppressWarnings({"unchecked"})
public abstract class BaseController {
	
	/**
	 * 日志记录器
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
	/**
     * 获取请求参数的查询字符串形式
     * @author rainyhao
     * @since 2015-6-8 下午12:17:04
     * @param request
     * @return
     */
    protected String getQueryString(HttpServletRequest request) {
        StringBuffer queryString = new StringBuffer();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            queryString.append(name.toString()).append("=").append(value).append("&");
        }
        return 0 == queryString.length() ? "" : queryString.subSequence(0, queryString.length() - 1).toString();
    }
    
	/**
	 * 捕获所有api中通过shiro过滤发生的无权限访问异常并输出没权限的api结果
	 * 所有需要权限过滤的接口使用@RequiresPermissions,@RequiresRoles 等注解进行声明
	 * @authro rainyhao
	 * @since 2016-4-11 下午6:06:14
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public Map<String, Object> toUnauthorized(NativeWebRequest request, UnauthorizedException e) {
		return ApiResult.forbidden();
	}
}
