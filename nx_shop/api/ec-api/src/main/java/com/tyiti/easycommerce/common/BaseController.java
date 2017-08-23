package com.tyiti.easycommerce.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tyiti.easycommerce.entity.PickupPointUser;
/**
 * 
* @ClassName: BaseController 
* @Description:  掉接口前先检查一下是否 pickupPointUser 是否登录
* @author hcy 
* @date 2016年6月29日 下午5:12:33
 */
public class BaseController {

	public static HttpSession getSession() {
		HttpSession session = null;
		try {
			session = getRequest().getSession();
		} catch (Exception e) {
		}
		return session;
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return attrs.getRequest();
	}
	
	public static boolean judeIfLogin(){
		HttpSession session=getSession();
		PickupPointUser pickupPointUser=(PickupPointUser)session.getAttribute("pickupPointUser");
		if(pickupPointUser!=null){
			return true;
		}
		return false;
	}
}
