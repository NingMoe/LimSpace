package com.tyiti.easycommerce.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.entity.WePayChatEntity;
import com.tyiti.easycommerce.entity.WeixinSignature;
import com.tyiti.easycommerce.service.CallbackService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.service.WeixinService;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.xml.XmlUtil;
import com.tyiti.easycommerce.util.xml.entity.weixin.Unifiedorder;
import com.tyiti.easycommerce.util.xml.entity.weixin.UnifiedorderResponse;
import com.tyiti.easycommerce.vo.UnifiedorderVO;
@Controller
public class WeixinController {
	@Autowired
	private WeixinService weixinService;
	@Autowired
	private OrderService orderService;
    @Autowired
    private CallbackService  callbackService;

	private Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 验证服务器地址的有效性
	 */
	@RequestMapping(value = "/weixin/signature/check", produces = "text/html")
	@ResponseBody
	public String checkWeixinSignature(WeixinSignature weixinSignature) {
		if (weixinService.checkSignature(weixinSignature)) {
			return weixinSignature.getEchostr();
		} else {
			return "check Error";
		}
	}

	@RequestMapping(value = "/weixin/signature/check", method = RequestMethod.POST, produces = "text/html")
	@ResponseBody
	public String firstRequest(WeixinSignature weixinSignature,
			HttpServletRequest request, HttpServletResponse response) {
		if (!weixinService.checkSignature(weixinSignature)) {
			return "check Error";
		}

		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");

		String responseMessage = "";
		try {
			responseMessage = weixinService.processRequest(request
					.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseMessage;
	}

	@RequestMapping(value = "/weixin/oauth2")
	@ResponseBody
	public void oauth2(@RequestParam("code") String code,
			@RequestParam("state") String state, HttpServletResponse response,
			HttpSession session) throws IOException {
		String openId = weixinService.getOpenId(code);
		logger.info("openId======" + openId);
		session.setAttribute(Constants.USER_OPENID, openId);
		// 把 openId 存入 session
		User user = (User) session.getAttribute(Constants.USER);
		if (user != null) {
			user.setOpenId(openId);
			session.setAttribute(Constants.USER, user);
		}
		String redirectUrl = state.replaceAll("-", "/");
		logger.info("oauth2获取openId+redirectUrl====" + redirectUrl);
		/*
		 * String[] codeList = { "home", "category", "order", "user", "user-reg"
		 * }; Arrays.sort(codeList); if (Arrays.binarySearch(codeList, 0,
		 * codeList.length, code) != -1) { redirectUrl = code.replaceAll("-",
		 * "/"); }
		 */
		// response.sendRedirect("http://dev.cebshop.tyiti.com/pay.html");
		String urlPrefix = SysConfig.configMap
				.get(ConfigKey.WX_REDIRECT_URL_PREFIX);
		logger.info("oauth2获取openId+urlPrefix" + urlPrefix);
		response.sendRedirect(urlPrefix + "#" + redirectUrl);
	}

	// 通过config接口注入权限验证配置
	@RequestMapping(value = "/weixin/wxConfig", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> wxConfig(@RequestParam("url") String url,
			HttpServletResponse response, HttpServletRequest request,
			HttpSession session) throws IOException {
		return weixinService.getSignature(request, url);
	}

	/***
	  * <p>功能描述：微信未支付之间验证商品的活动信息。</p>	
	  * @param unifiedorderVO
	  * @param request
	  * @param response
	  * @param session
	  * @return
	  * @throws ParseException
	  * <p>创建日期:2016年8月12日 上午9:39:59。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value = "/weixin/unifiedorder", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> unifiedorder(@RequestBody UnifiedorderVO unifiedorderVO,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (unifiedorderVO == null) {
			map.put("code", "400");
			map.put("message", "订单不存在");
			return map;
		}
		String orderId = unifiedorderVO.getOrderId();
		Order order = orderService.getById(Integer.parseInt(orderId));
		User user = (User) session.getAttribute(Constants.USER);
			/******下订单之前订单是否是有效订单结束***/
		if ("0".equals(unifiedorderVO.getType())) {
			if (StringUtils.isEmpty(orderId) || !StringUtils.isNumeric(orderId)) {
				map.put("code", "400");
				map.put("message", "订单不存在");
				return map;
			}
			/******下订单之前订单是否是有效订单 开始 author:kongchunlong***/
			map = orderService.getOrderIsSuccessOrder(order,user);
			Integer code = (Integer) map.get("code");
			if(code!=200){
				return map;
			}
			/******下订单之前订单是否是有效订单结束***/
			
			if (order == null || order.getInvalid().equals("1")|| order.getStatus() >= 2) {
				map.put("code", "400");
				map.put("message", "订单不存在,或者订单已经支付！");
				return map;
			}
		} else if ("1".equals(unifiedorderVO.getType())) {
			// 账单还款
			if (StringUtils.isEmpty(orderId) || orderId.length() == 0) {
				map.put("code", "400");
				map.put("message", "请求参数有误");
				return map;
			}
			if (orderId.length() > 110) {
				map.put("code", "400");
				map.put("message", "数据包过长");
				return map;
			}
			if (unifiedorderVO.getGrossAmount().compareTo(new BigDecimal(0)) == 0) {
				map.put("code", "400");
				map.put("message", "请求参数有误");
				return map;
			}
		} else {
			map.put("code", "400");
			map.put("message", "请求参数有误");
			return map;
		}
		String ip = request.getRemoteAddr();// 获取Ip
		String openId = (String) session.getAttribute(Constants.USER_OPENID);
		unifiedorderVO.setIp(ip);
		unifiedorderVO.setOpenId(openId);

		Unifiedorder unifiedorder = weixinService.getUnifiedorder(unifiedorderVO, session);// 获取用户信息
		// 组成请求前的xml
		String xml = XmlUtil.toXml(unifiedorder, Unifiedorder.class);
		logger.info(LogUtil.info("微信预支付", "组成请求前的xml", null, "xml:" + xml));
		StringBuffer sb = HttpClientUtil.httpsRequest(Constants.WX_UNIFIEDORDER_URL, "POST", xml);
		logger.info(LogUtil.info("微信预支付", "请求微信"+ Constants.WX_UNIFIEDORDER_URL + ",微信返回结果：" + sb.toString(),null, "xml:" + xml));
		UnifiedorderResponse unifiedorderResponse = (UnifiedorderResponse) XmlUtil.fromXml(sb.toString(), UnifiedorderResponse.class);
		if (unifiedorderResponse != null&& unifiedorderResponse.getReturnMsg() != null) {
			String message = unifiedorderResponse.getReturnMsg();
			if (!message.equals("OK")) {// 正确返回为 Oke
				map.put("code", "200");
				map.put("message", message);
				return map;
			}
			logger.info(message+">>>>>>>>>>>>>>>>>>>>>>微信返回信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			WePayChatEntity wePayChatEntity = weixinService.getSignature(unifiedorderResponse, unifiedorder);
			logger.info(wePayChatEntity+">>>>>>>>>>>>>>>>>>>>>>微信返回信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			map.put("wePayChatEntity", wePayChatEntity);
		} else {
			map.put("code", "400");
			map.put("message", "请求支付错误!");
		}
		return map;
	}

	@RequestMapping(value = "/weixin/paynotify")
	@ResponseBody
	public Map<String, Object>  paynotify(HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info(">>>>>>>>>>>>>>>>>>>>>>进入微信支付回调函数");
		map = callbackService.callback(map, "weCatch", request, response);
//		map = weixinService.paynotify(request,response);
		return map;
	}

	/**
	 * 微信支付主动查询支付结果
	 * @param request
	 * @param response
	 * @param session
	 * author:Black
	 * date:2016-08-23
	 * @return
	 */
	@RequestMapping(value = "/weixin/payordersync")
	@ResponseBody
	public void payordersync(HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		
		weixinService.payordersync(request,response);
		
	}

	/***
	 * <p>
	 * 功能描述：。
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期:2016年4月28日 下午9:30:39。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@RequestMapping(value = "/indexMMM")
	@ResponseBody
	public String indexMMM(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		System.out.println("paynotify");
		return "redirect:/index.jsp";
		// return new ModelAndView("/webapp/index.jsp");
	}
}
