package com.tyiti.easycommerce.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.ConfigKey;
import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.SysConfig;
import com.tyiti.easycommerce.entity.Settings;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.CartService;
import com.tyiti.easycommerce.service.UserService;
import com.tyiti.easycommerce.util.Md5;
import com.tyiti.easycommerce.util.spring.SpringPropertiesUtil;

@Controller
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService cartService;

	private Log log = LogFactory.getLog(UserController.class);
	/**
	 * @Title: getOpenId 获取用户openId
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "openId", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getOpenId(String state,	HttpServletRequest request, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (SysConfig.configMap.get("need_open_id").equals("1")) {
			Object openId = session.getAttribute(Constants.USER_OPENID);
			if (openId == null) {
				// 判断当前用户是否从微信端来的， 如果不是 那么openId 为null 这时去请求获取openId
				String appid = SysConfig.configMap.get(ConfigKey.WX_APPID);
				String origin = SysConfig.configMap.get(ConfigKey.ORIGIN);
				// 要求 Nginx 配置 proxy_set_header X-Origin $scheme://$host/; 且不支持端口
				// String origin = request.getHeader("X-Origin");
				String url;
				try {
					url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
							+ appid
							+ "&redirect_uri="
							+ URLEncoder.encode(origin + "api/weixin/oauth2", "UTF-8") 
							+ "&response_type=code&scope=snsapi_base&state="
							+ state + "&connect_redirect=1#wechat_redirect";
					log.info("获取openId ==="+url);

					map.put("code", 302);
					map.put("url", url);
					return map;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		map.put("code", 200);
		return map;
	}

	/**
	 * 查看用户信息
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> readUser(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Map<String, Object> data = new HashMap<String, Object>();

		String mobile = user.getMobile();
//		data.put("mobile",
//				mobile.substring(0, 3) + "****" + mobile.substring(7, 11));
		data.put("mobile", mobile);

		data.put("real_name", user.getRealName());
		data.put("isCreditCardUser", (user.getThirdPartyType()!=null&&user.getThirdPartyType()==2)?true:false);
		data = userService.getUserScore(user,data);
		
		user=userService.getById(user.getId());
		data.put("nickName", user.getNickName());
		data.put("birthday", user.getBirthday());
		data.put("gender", user.getGender());		
		data.put("headImg", user.getHeadImg());
		
		if(user.getPayPassword()!=null&&!"".equals(user.getPayPassword())){
			data.put("hasPayPassword", true);
		}else{
			data.put("hasPayPassword", false);
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		map.put("data", data);
		return map;
	}

	/**
	 * 发送手机验证码
	 * 
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/verify_code", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> readVerifyCode(
			@RequestParam(value = "mobile", required = true) String mobile,
			@RequestParam(value = "type", required = true) String type) {
		Map<String, Object> map = new HashMap<String, Object>();

		String template = SpringPropertiesUtil.getProperty("sms.content."
				+ type);
		Map<String, Object> res = userService.getVerifyCode(mobile, template);
		if (0 != (int) res.get("state")) {
			map.put("code", 500);
			map.put("messsge", res.get("message"));
			return map;
		}

		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/**
	 * 弱绑定，提供手机号、短信验证码，从 session 获取 openId （这个条件是可选的），即绑定（登录）
	 * 
	 * @param user
	 *            包含手机号、短信验证码的 user 对象
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/connections", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> createConnection(HttpSession session,
			@RequestBody User user) {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> res = userService.createConnection(session, user);

		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}

		map.put("code", 200);
		map.put("messsge", "OK");

		return map;
	}

	/**
	 * 用户注册
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> createUser(HttpSession session,
			@RequestBody User user) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> res = userService.register(session, user);

		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}

		data.put("code", 200);
		data.put("messsge", "OK");
		data.put("data", res.get("data"));
		return data;
	}
	/**
	 * 授信进度
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/creditStep",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCreditStep(HttpSession session) {
		User sessionUser = (User) session.getAttribute(Constants.USER);
		return userService.creditStep(sessionUser);
	}
	
	
	/**
	 * 信用卡用户授信
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/creditSubmit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> creditSubmit(HttpSession session,@RequestBody User user) {
		User sessionUser = (User) session.getAttribute(Constants.USER);
		sessionUser.setRealName(user.getRealName());
		sessionUser.setIdCard(user.getIdCard());
		return userService.creditSubmit(sessionUser);
	}

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> signup(HttpSession session,HttpServletRequest request,
			@RequestBody User user) {
		
		return userService.signup(session,request, user);
	}

	/**
	 * 用户注销
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/signout", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> signout(HttpServletRequest request,HttpServletResponse response,HttpSession session) {
		return userService.signout(request,response,session);
	}

	/**
	 * 修改密码；忘记密码，第二步：修改密码
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/passwords", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> updatePassword(HttpSession session,
			@RequestBody User user) {
		User sessionUser = (User) session.getAttribute(Constants.USER);
		// 修改密码
		if (sessionUser != null) {
			return userService.changePassword(session, user);
		}

		// 忘记密码，第二步：修改密码
		Map<String, Object> result = userService.doAuth(session, user);
		int code = (int) result.get("code");
		if (code != 200) {
			return result;
		}

		try {
			User paramUser = (User) result.get("data");
			user.setThirdPartyId(paramUser.getThirdPartyId());
			user.setPassword(paramUser.getPassword());
			Md5 md5 = new Md5();
			user.setNewPassword(md5.getMD5ofStr(user.getNewPassword()));
		} catch (Exception ex) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", 502);
			map.put("message", "这里报错了：" + ex.getMessage());
			return map;
		}
		return userService.updatePassword(session, user);
	}

	/**
	 * 忘记密码，第一步需要验证身份，请求参数包含手机号、身份证号（天尧分期才有）、短信验证码
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/auths", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> doAuth(HttpSession session,
			@RequestBody User user) {
		Map<String, Object> map = new HashMap<String, Object>();

		User sessionUser = (User) session.getAttribute(Constants.USER);
		if (sessionUser != null) {
			map.put("code", 403);
			map.put("messsge", "已登录");
			return map;
		}

		return userService.doAuth(session, user);
	}

	/**
	 * 修改、设置支付密码，第一步需要验证身份，请求参数包含身份证号、短信验证码
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/pay_auths", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> doPayAuth(HttpSession session,
			@RequestBody User user) {
		return userService.doPayAuth(session, user);
	}

	/**
	 * 修改、设置支付密码，第二步
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/pay_passwords", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> resestPayPassword(HttpSession session,
			@RequestBody User user) {
		Map<String, Object> result = userService.doPayAuth(session, user);
		int code = (int) result.get("code");
		if (code != 200) {
			return result;
		}

		return userService.updatePayPassword(session, user);
	}
	
	
	/**
	 * 个人中心-个人信息 新建了一个PrivateInfoController
	 * @author Black
	 * @date 2016-07-19
	 * @return
	 */
	/*@RequestMapping(value = "/privateInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPrivateInfo(HttpSession session) {

		User user = (User) session.getAttribute(Constants.USER);

		return userService.getPrivateInfo(user);
	}*/

	/**
	 * @description 获取用户的分享码
	 * @author wyy 2016/08/02
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShareCode(HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		return userService.getShareCode(user);
	}

	/**
	 * @description 获取用户的手机号
	 * @author wyy 2016/08/02
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/shareUser", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShareUser(HttpSession session, String code) {
		return userService.getShareUser(code);
	}
	
	
	/**
	 * @description 个人设置
	 * @param session
	 * @param parameter
	 *            参数值
	 * @param type
	 *            类型 photo、头像 2、昵称 3、生日 4、性别
	 * @author wyy 2016/07/15
	 * @return
	 */
	@RequestMapping(value = "/settings", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> mySettings(HttpSession session,
			@RequestBody Settings settings) {
		String parameter = settings.getParameter();
		String type = settings.getType();
		Map<String, Object> map = new HashMap<String, Object>();
		if ("".equals(parameter) || "".equals(type)) {
			map.put("code", 400);
			map.put("messsge", "参数不能为空！");
		}
		// 用户信息
		User user = (User) session.getAttribute(Constants.USER);
		User userParameter = new User();
		userParameter.setId(user.getId());
		switch (type) {
		case "1":
			userParameter.setHeadImg(parameter);
			break;
		case "2":
			userParameter.setNickName(parameter);
			break;
		case "3":
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				userParameter.setBirthday(sdf.parse(parameter));
			} catch (Exception e) {
				map.put("code", 400);
				map.put("messsge", "日期格式不正确！");
				return map;
			}
			break;
		case "4":
			try {
			Integer gender = Integer.parseInt(parameter);
				if (gender == 0 || gender == 1) {
					userParameter.setGender(gender);
				} else {
					map.put("code", 400);
					map.put("messsge", "参数不合法！");
					return map;
				}
			} catch (Exception e) {
				map.put("code", 400);
				map.put("messsge", "性别不正确！");
				return map;
			}
			break;
		}
		return userService.updateByPrimaryKeySelective(userParameter);
	}
	
	/**
	 * @description 上传图片
	 * @author wyy 2016/09/22
	 * @param
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/upload/img", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> imgToReturn(
			@RequestParam(value = "mediaId", required = true) String mediaId,
			@RequestParam(value = "type", required = true) String type,
			HttpSession session) {
		Map<String, Object> data = userService.returnImg(session,
				mediaId,type);
		return data;
	}
	
}
