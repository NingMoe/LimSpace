package com.tyiti.easycommerce.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.ConfigKey;
import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.SysConfig;
import com.tyiti.easycommerce.common.WxAccessToken;
import com.tyiti.easycommerce.entity.BillRecode;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.PayRecode;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.entity.WePayChatEntity;
import com.tyiti.easycommerce.entity.WeixinSignature;
import com.tyiti.easycommerce.repository.BillRecodeDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.PayRecodeDao;
import com.tyiti.easycommerce.repository.PickupOrderDao;
import com.tyiti.easycommerce.repository.UnifiedorderDao;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.service.BillService;
import com.tyiti.easycommerce.service.KooPayOrderService;
import com.tyiti.easycommerce.service.OrderPaymentService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.service.SkuService;
import com.tyiti.easycommerce.service.WeixinMessageService;
import com.tyiti.easycommerce.service.WeixinService;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.TimeUtils;
import com.tyiti.easycommerce.util.xml.XmlUtil;
import com.tyiti.easycommerce.util.xml.entity.weixin.PayNotifyReq;
import com.tyiti.easycommerce.util.xml.entity.weixin.PayNotifyRes;
import com.tyiti.easycommerce.util.xml.entity.weixin.PayOrderSyncReq;
import com.tyiti.easycommerce.util.xml.entity.weixin.PayOrderSyncRes;
import com.tyiti.easycommerce.util.xml.entity.weixin.Unifiedorder;
import com.tyiti.easycommerce.util.xml.entity.weixin.UnifiedorderResponse;
import com.tyiti.easycommerce.vo.UnifiedorderVO;

/**
 * <p>
 * 类描述：。
 * </p>
 * 
 * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
 * @version: v1.0.0.1
 * @version: v1.0.0.1。
 * @since JDK1.6。
 *        <p>
 *        创建日期：2016年4月28日 下午2:25:44。
 *        </p>
 */
@Service
public class WeixinServiceImpl implements WeixinService {
	public final static String WX_ACCESS_TOKEN_API = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public final static String EVENT_TYPE_CLICK = "CLICK";
	private static String jsapiTicket = null;
	// 过期时间
	private static Date expiresTime = null;
	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private WeixinMessageService weixinMessageService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderSkuDao orderSkuDao;
	@Autowired
	private UnifiedorderDao unifiedorderDao;
	@Autowired
	private PayRecodeDao payRecodeDao;
	@Autowired
	private BillService billService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private OrderPaymentService orderPaymentService;
	@Autowired
	private OrderService orderService;
	@Autowired
	KooPayOrderService kooPayOrderService;
	@Autowired
	BillRecodeDao billRecodeDao;

	// 信分宝接口地址
	@Value("${xfbInterface}")
	private String xfbInterface;
	// 信分宝接口地址版本号
	@Value("${xfbVersion}")
	private String xfbVersion;

	// 用户是否和信分宝打通
	@Value("${connectedToXfb}")
	private boolean connectedToXfb;
	// sys
	@Value("${sys}")
	private String sys;

	@Autowired
	private PickupOrderDao pickupOrderDao;
	@Autowired
	private UserDao userDao;

	/***
	 * <p>
	 * 功能描述:。
	 * </p>
	 * 
	 * @param weixinSignature
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年4月28日 下午2:30:30。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	public boolean checkSignature(WeixinSignature weixinSignature) {
		String token = SysConfig.configMap.get(ConfigKey.WX_TOKEN);
		String timestamp = weixinSignature.getTimestamp();
		String nonce = weixinSignature.getNonce();
		if (token == null || timestamp == null || nonce == null) {
			return false;
		}
		// 1. 将token、timestamp、nonce三个参数进行字典序排序
		String[] paramArr = new String[] { token, timestamp, nonce };
		Arrays.sort(paramArr);

		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
		String cipherText = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());
			for (int i = 0; i < digest.length; i++) {
				cipherText += byteToHexStr(digest[i]);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		return cipherText != null ? cipherText.equals(weixinSignature
				.getSignature().toUpperCase()) : false;
	}

	/**
	 * 获取JS-SDK使用权限签名
	 * 
	 * @return
	 */
	public Map<String, Object> getSignature(HttpServletRequest request,
			String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String nonceStr = UUID.randomUUID().toString();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		/*
		 * String reqUrl = request.getContextPath(); String reqParam =
		 * request.getQueryString(); if(reqParam!=null&&!"".equals(reqParam))
		 * reqUrl = reqUrl +"?"+reqParam;
		 */
		if (url != null && url.length() != 0) {
			if (-1 != url.indexOf("#")) {
				url = url.substring(0, url.indexOf("#"));
			}
		} else {
			map.put("code", "400");
			map.put("message", "url不能为空");
			return map;
		}
		String sig = "";
		logger.info("reqUrl:" + url);
		// 获取 access_token
		String accessToken = WxAccessToken.getAccessToken();
		logger.info("accessToken:" + accessToken);
		if (accessToken == null || "".equals(accessToken)) {
			map.put("code", "400");
			map.put("message", "获取accessToken失败");
			return map;
		}
		// 获取JsapiTicket
		if (jsapiTicket == null) {
			jsapiTicket = getJsapiTicket(accessToken);
		} else {
			if (expiresTime.before(new Date()))
				jsapiTicket = getJsapiTicket(accessToken);
		}
		logger.info("jsapiTicket:" + jsapiTicket);
		if (jsapiTicket == null || "".equals(jsapiTicket)) {
			map.put("code", "400");
			map.put("message", "获取jsapiTicket失败");
			return map;
		}
		// 注意这里参数名必须全部小写，且必须有序
		String str = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr
				+ "&timestamp=" + timestamp + "&url=" + url;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(str.toString().getBytes());
			for (int i = 0; i < digest.length; i++) {
				sig += byteToHexStr(digest[i]);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		data.put("appId", SysConfig.configMap.get(ConfigKey.WX_APPID));
		data.put("timestamp", timestamp);
		data.put("nonceStr", nonceStr);
		data.put("signature", sig.toLowerCase());

		map.put("code", "200");
		map.put("message", "Ok");
		map.put("data", data);
		return map;
	}

	/**
	 * 调用微信JS接口的临时票据 jsapi_ticket的有效期为7200秒
	 * 
	 * @return
	 */
	private String getJsapiTicket(String accessToken) {
		Date date = new Date();
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
				+ accessToken + "&type=jsapi";
		// String requestUrl = url.replace("ACCESS_TOKEN", accessToken);
		// 发起GET请求获取凭证
		StringBuffer sb = HttpClientUtil.httpsRequest(url, "GET", null);
		JSONObject jsonObject = JSONObject.fromObject(sb.toString());
		logger.info("getJsapiTicket-JSON:" + jsonObject);
		if (null != jsonObject && "0".equals(jsonObject.getString("errcode"))) {
			try {
				jsapiTicket = jsonObject.getString("ticket");
				// 微信的凭证有效时间，单位：秒
				Integer wxExpiresIn = Integer.parseInt(jsonObject
						.getString("expires_in"));
				Calendar c = new GregorianCalendar();
				c.setTime(date);
				c.add(Calendar.SECOND, wxExpiresIn - 1);
				date = c.getTime();
				// 过期时间
				expiresTime = date;
			} catch (JSONException e) {
				// 获取jsapiTicket失败
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return jsapiTicket;
	}

	/**
	 * <p>
	 * 功能描述:第一次校验签名。
	 * </p>
	 * 
	 * @param unifiedorder
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年4月28日 下午2:28:08。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	public String getSignature(Unifiedorder unifiedorder) {
		// Md5 md5 = new Md5();
		return DigestUtils.md5Hex(
				unifiedorder.toUrlParamsSortedByKey() + "&key="
						+ SysConfig.configMap.get(ConfigKey.WX_PARTNER_KEY))
				.toUpperCase();
	}

	/**
	 * <p>
	 * 功能描述:第一次校验签名。
	 * </p>
	 * 
	 * @param payOrderSyncReq
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年4月28日 下午2:28:08。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	public String getSignature(PayOrderSyncReq payOrderSyncReq) {
		// Md5 md5 = new Md5();
		return DigestUtils.md5Hex(
				payOrderSyncReq.toUrlParamsSortedByKey() + "&key="
						+ SysConfig.configMap.get(ConfigKey.WX_PARTNER_KEY))
				.toUpperCase();
	}

	/***
	 * <p>
	 * 功能描述：。
	 * </p>
	 * 
	 * @param inputByte
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期:2016年4月28日 下午2:30:22。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	public static String byteToHexStr(byte inputByte) {
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hex = new char[2];
		hex[0] = digits[(inputByte >>> 4) & 0X0F];
		hex[1] = digits[inputByte & 0X0F];

		return new String(hex);
	}

	public String getOpenId(String code) {
		String appid = SysConfig.configMap.get(ConfigKey.WX_APPID);
		String appsecret = SysConfig.configMap.get(ConfigKey.WX_APPSECRET);

		String url = WX_ACCESS_TOKEN_API
				+ "?grant_type=authorization_code&appid=" + appid + "&code="
				+ code + "&secret=" + appsecret;

		StringBuffer result = HttpClientUtil.httpsRequest(url, "POST", null);
		System.out.println(result.toString());
		JSONObject resp = JSONObject.fromObject(result.toString());
		return resp.getString("openid");
	}

	public String processRequest(InputStream inputStream) {
		Map<String, String> requestMap = null;
		try {
			requestMap = weixinMessageService.parseXml(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (requestMap == null) {
			return null;
		}

		// 消息类型
		String msgType = requestMap.get("MsgType");
		if (msgType.equals(EVENT_TYPE_CLICK)) {
			String contentText = "测试";
			return weixinMessageService.getTextMessage(requestMap, contentText);
		}

		return null;
	}

	/**
	 * <p>
	 * 功能描述:。
	 * </p>
	 * 
	 * @param inputStream
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年4月28日 下午2:30:39。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public String processRequest(ServletInputStream inputStream) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * 功能描述:第二次校验签名。
	 * </p>
	 * 
	 * @param unifiedorderResponse
	 * @param unifiedorder
	 * @throws ParseException
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年4月28日 下午2:30:39。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public WePayChatEntity getSignature(
			UnifiedorderResponse unifiedorderResponse, Unifiedorder unifiedorder)
			throws ParseException {
		String partnerKey = SysConfig.configMap.get(ConfigKey.WX_PARTNER_KEY);
		Integer rand = (int) (Math.random() * 1000000);
		String appId = unifiedorder.getAppid();// AppId
		String partner_id = unifiedorder.getMchId();// 商户Id
		String prepay_id = unifiedorderResponse.getPrepayId();// 预支付ID
		String timeStamp = TimeUtils.toStrDateFromUtilDateByFormat(new Date(),
				"yyyyMMddHHmmss");
		String strpackage = "prepay_id=" + prepay_id;
		// String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
		WePayChatEntity WePayChatEntity = new WePayChatEntity();
		WePayChatEntity.setAppId(appId);
		WePayChatEntity.setPartnerid(partner_id);
		WePayChatEntity.setPrepayId(prepay_id);
		WePayChatEntity.setStrpackage(strpackage);
		WePayChatEntity.setTimestamp(timeStamp);
		WePayChatEntity.setNocestr(rand.toString());
		String sign = DigestUtils
				.md5Hex(WePayChatEntity.toUrlParamsSortedByKey() + "&key="
						+ partnerKey).toUpperCase();
		WePayChatEntity.setSign(sign);
		saveWePayChatEntity(WePayChatEntity);
		return WePayChatEntity;

	}

	/**
	 * <p>
	 * 功能描述：保存数据信息。
	 * </p>
	 * 
	 * @param wePayChatEntity
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期:2016年4月29日 下午7:22:09。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	private void saveWePayChatEntity(WePayChatEntity wePayChatEntity) {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>
	 * 功能描述:根据用户获取用户当前的订单信息。
	 * </p>
	 * 
	 * @param session
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年4月28日 下午4:32:22。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public Unifiedorder getUnifiedorder(UnifiedorderVO vo, HttpSession session) {
		String orderId = "";
		String body = "";
		String attach = "";// 附件数据包
		int totalFee = 0;
		String outTradeNo = "";
		User user = (User) session.getAttribute(Constants.USER);
		if ("0".equals(vo.getType())) {
			orderId = vo.getOrderId();
			List<OrderSku> orderSkuList = orderSkuDao.getByOrderId(Integer
					.parseInt(orderId));
			OrderSku orderSku = orderSkuList.get(0);
			body = orderSku.getSkuName();
			if (body.length() > 40) {
				body = body.substring(0, 30) + "...";
			}
			logger.info(LogUtil
					.info("微信预支付", "微信预支付body", null, "body:" + body));
			Order order = orderDao.getById(Integer.parseInt(orderId));
			outTradeNo = order.getNo();
			totalFee = order.getDownPayment().multiply(new BigDecimal(100))
					.intValue();
			attach = vo.getType() + ";" + user.getId() + ";" + sys + ";"
					+ xfbInterface + ";" + xfbVersion;
		} else if ("1".equals(vo.getType())) {
			orderId = "B" + System.currentTimeMillis()
					+ new Random().nextInt(10000);
			outTradeNo = orderId;
			body = "账单还款：" + vo.getOrderId();
			totalFee = vo.getGrossAmount().multiply(new BigDecimal(100))
					.intValue();
			attach = vo.getType() + ";" + vo.getOrderId() + ";" + user.getId();
		}
		logger.debug("预支付数据包：attach:" + attach + " orderId:" + orderId
				+ " totalFee:" + totalFee + " body:" + body);
		Integer rand = (int) (Math.random() * 1000000);
		Unifiedorder unifiedorder = new Unifiedorder();
		unifiedorder.setAppid(SysConfig.configMap.get(ConfigKey.WX_APPID));
		unifiedorder.setMchId(SysConfig.configMap.get(ConfigKey.WX_MCHID));
		unifiedorder.setNonceStr(rand.toString());// 随机字符串 需要有含义后期支付
		unifiedorder.setBody(body);// 商品描述
		unifiedorder.setOutTradeNo(outTradeNo);// 订单号
		unifiedorder.setTotalFee(totalFee); // 支付金额 （分）
		unifiedorder.setAttach(attach);// 附加数据包

		unifiedorder.setSpbillCreateIp(vo.getIp());
		unifiedorder.setNotifyUrl(SysConfig.configMap
				.get(ConfigKey.WX_NOTIFY_URL));
		unifiedorder.setOpenid(vo.getOpenId());
		unifiedorder.setSign(getSignature(unifiedorder));
		saveUnifiedorder(unifiedorder);
		return unifiedorder;
	}

	/**
	 * <p>
	 * 功能描述：保存预支付信息。
	 * </p>
	 * 
	 * @param unifiedorder
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期:2016年4月29日 下午7:19:25。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	public void saveUnifiedorder(Unifiedorder unifiedorder) {
		com.tyiti.easycommerce.entity.Unifiedorder entity = new com.tyiti.easycommerce.entity.Unifiedorder();
		entity.setAppid(unifiedorder.getAppid());
		entity.setAttach(unifiedorder.getAttach());
		entity.setBody(unifiedorder.getBody());
		entity.setDeviceInfo(unifiedorder.getDeviceInfo());
		entity.setFeeType(unifiedorder.getFeeType());
		entity.setMchId(unifiedorder.getMchId());
		entity.setNonceStr(unifiedorder.getNonceStr());
		entity.setNotifyUrl(unifiedorder.getNotifyUrl());
		entity.setOpenid(unifiedorder.getOpenid());
		entity.setOutTradeNo(unifiedorder.getOutTradeNo());
		entity.setSign(unifiedorder.getSign());
		entity.setSpbillCreateIp(unifiedorder.getSpbillCreateIp());
		entity.setTotalFee(unifiedorder.getTotalFee());
		entity.setTradeType(unifiedorder.getTradeType());
		unifiedorderDao.insertSelective(entity);
	}

	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("#.00");
		df.format(12);
		System.out.println(df.format(12));
	}

	/**
	 * <p>
	 * 功能描述:。
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年7月7日 下午6:04:48。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public Map<String, Object> paynotify(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		BufferedReader reader = null;
		StringBuilder sb = null;
		PayNotifyRes res = new PayNotifyRes();
		PrintWriter out = null;
		synchronized (this) {
			try {
				reader = new BufferedReader(new InputStreamReader(
						request.getInputStream()));
				out = response.getWriter();
				sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				PayNotifyReq payNotifyReq = (PayNotifyReq) XmlUtil.fromXml(
						sb.toString(), PayNotifyReq.class);
				logger.info(payNotifyReq
						+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>payNotifyReq>>>>>>>>>>>>>");

				if (payNotifyReq == null) {
					res.setReturnCode("FAIL");
					res.setReturnMsg("请求参数有误");
					logger.info(LogUtil.info("微信支付回调", "微信请求参数为空", null,
							"payNotifyReq=" + sb.toString()));
					out.write(XmlUtil.toXml(res, PayNotifyRes.class));
					map.put("code", "400");
					map.put("message", "请求参数有误");
					return map;
				}

				logger.info(payNotifyReq.getReturnCode()
						+ ">>>>>>>>>>>>>>>>通知标识>>>>>>>>>>>>>>>>>>>>>>>>>");

				// 通信标识
				if (!"SUCCESS".equals(payNotifyReq.getReturnCode())) {
					res.setReturnCode("FAIL");
					res.setReturnMsg(payNotifyReq.getReturnMsg());
					logger.info(LogUtil.info("微信支付回调", "ReturnCode不为SUCCESS",
							null, payNotifyReq));
					out.write(XmlUtil.toXml(res, PayNotifyRes.class));
					map.put("code", "400");
					map.put("message", "ReturnCode不为SUCCESS");
					return map;
				}
				// 验证签名
				// 。。。。。
				logger.info(payNotifyReq.getReturnCode()
						+ ">>>>>>>>>>>>>>>>验证交易是否成功>>>>>>>>>>>>>>>>>>>>>>>>>");

				// 验证交易是否成功
				if (!"SUCCESS".equals(payNotifyReq.getResultCode())) {
					res.setReturnCode("FAIL");
					res.setReturnMsg(payNotifyReq.getReturnMsg() + ","
							+ payNotifyReq.getErrCode() + ","
							+ payNotifyReq.getErrCodeDes());
					logger.info(LogUtil.info("微信支付回调", "ResultCode不为SUCCESS",
							null, payNotifyReq));
					out.write(XmlUtil.toXml(res, PayNotifyRes.class));
					map.put("code", "400");
					map.put("message", "ReturnCode不为SUCCESS");
					return map;
				}

				logger.info(payNotifyReq.getReturnCode()
						+ ">>>>>>>>>>>>>>>>验证交易是否成功>>>>>>>>>>>>>>>>>>>>>>>>>");

				// >>>业务逻辑
				// 解析附加数据包
				String attach = payNotifyReq.getAttach();
				String[] attachArr;
				String type = "";
				if (attach != null && attach.length() > 0) {
					attachArr = attach.split(";", -1);
					type = attachArr[0];
				}
				logger.info(LogUtil.info("微信支付回调", "请求数据包", null, "attach="
						+ attach));
				if ("0".equals(type)) {
					// 用户id
					attachArr = attach.split(";", -1);
					String userId = attachArr[1];
					// 所在平台
					String xfbInterface = attachArr[3];
					String xfbVersion = attachArr[4];
					String no = payNotifyReq.getOutTradeNo();
					logger.info(LogUtil.info("微信支付回调", "支付方式为：首付", userId,
							"订单no:" + no));
					if (StringUtils.isEmpty(no)) {
						res.setReturnCode("FAIL");
						res.setReturnMsg("订单不存在");
						logger.info(LogUtil.info("微信支付回调", "订单不存在", userId,
								"订单no:" + no));
						out.write(XmlUtil.toXml(res, PayNotifyRes.class));
						map.put("code", "400");
						map.put("message", "订单不存在");
						return map;
					}
					Order order = orderService.getByNo(no);
					/**************** 保存支付流水号t_pay_record ***************************/
					try {
						logger.info(LogUtil.info("微信支付回调", "payNotifyReq",
								order.getCustId().toString(), payNotifyReq));
						// 保存支付流水号
						PayRecode payRecode = new PayRecode();
						payRecode.setOrderId(order.getId().toString());
						// 商户内部的订单号
						payRecode.setTradeNo(no);
						// 付款银行
						payRecode.setAccNo(payNotifyReq.getBankType());
						payRecode.setPayState("A2"); // 已支付
						// 是否关注公众号Y N
						if ("Y".equals(payNotifyReq.getIsSubscribe())) {
							payRecode.setIsSubscribe(1);
						} else {
							payRecode.setIsSubscribe(0);
						}
						payRecode.setOpenId(payNotifyReq.getOpenId());
						// 添加记录微信的支付流水号 wyy 2016/07/04 add
						payRecode.setPayNo(payNotifyReq.getTransactionId());
						// 微信返回的支付完成时间
						String date = payNotifyReq.getTimeEnd();
						String info = "订单no:" + no + " -- 微信支付流水号："
								+ payNotifyReq.getTransactionId()
								+ " -- 微信返回支付时间：" + date + " --支付银行类型："
								+ payNotifyReq.getBankType() + " --订单Id: "
								+ order.getId();
						logger.info(LogUtil.info("微信支付回调", "微信流水号记录号", order
								.getCustId().toString(), info));
						String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
						date = date.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date dateTime = sdf.parse(date);
						payRecode.setPayTime(dateTime);
						// 查看是否已做记录 wyy 2016/09/12
						if (payRecodeDao.getCount(payRecode.getOrderId(),
								payRecode.getTradeNo()) != null
								&& payRecodeDao.getCount(
										payRecode.getOrderId(),
										payRecode.getTradeNo()) > 0) {
							// 已存在该记录
						} else {
							payRecodeDao.insertPayRecode(payRecode);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info(LogUtil.info("微信支付回调",
								"微信流水号记录号异常-line753", order.getCustId()
										.toString(), e.getMessage()));
					}
					/**************** 保存支付流水号t_pay_record END ***************************/

					if (order == null || order.getInvalid().equals("1")) {
						res.setReturnCode("FAIL");
						res.setReturnMsg("订单不存在");
						logger.info(LogUtil.info("微信支付回调", "订单不存在", userId,
								"订单no:" + no));
						out.write(XmlUtil.toXml(res, PayNotifyRes.class));
						map.put("code", "400");
						map.put("message", "微信支付回调");
						return map;
					}
					if ((order.getStatus() >= 2 && order.getStatus() != 7)
							|| order.getDownPaymentPayed()) {
						// 发送成功通知
						res.setReturnCode("SUCCESS");
						res.setReturnMsg("订单已处理");
						logger.info(LogUtil.info("微信支付回调", "订单已处理", userId,
								"订单no:" + no));
						out.write(XmlUtil.toXml(res, PayNotifyRes.class));
						map.put("code", "400");
						map.put("message", "订单已处理");
						return map;
					}
					order.setDownPaymentPayed(true);
					order.setDownPaymentTime(new Date());
					// 如果是天尧分期，先分期-后首付的情况，需要单独创建账单
					if (connectedToXfb) {
						logger.info("==========进入天尧分期=========");
						// 判断分期是否有分期，并且分期已支付，如果是该情况，则调用信分宝创建账单（订单分期）接口
						if (order.getInstallmentAmount().compareTo(
								new BigDecimal(0)) > 0
								&& order.getInstallmentPayed()) {
							logger.info(LogUtil.info(
									"微信支付回调",
									"天尧分期：分期已支付，首付也完成，创建账单",
									userId,
									"订单no:" + no + "信分宝订单id:"
											+ order.getStageId()));
							// 获取用户信息
							User user = userDao.getById(Integer
									.parseInt(userId));
							// 先登录信分宝
							logger.info("信分宝参数xfbInterface：" + xfbInterface
									+ "xfbVersion:" + xfbVersion);
							String json = HttpClientUtil.httpLogin(
									xfbInterface, "", user.getMobile(),
									user.getPassword(), xfbVersion);
							logger.info("登录信分宝返回结果json：" + json);
							JSONObject obj = JSONObject.fromObject(json);
							if (obj != null && !obj.isEmpty()) {
								if ("0".equals(obj.getString("resultCode"))) {
									String resString = HttpClientUtil
											.httpOrderStage(
													order.getStageId(),
													order.getInstallmentMonths(),
													xfbInterface, user,
													obj.getString("userId"),
													xfbVersion);
									logger.info("调用信分宝创建账单返回结果json："
											+ resString);
									JSONObject result = JSONObject
											.fromObject(resString);
									int resultCode = result
											.getInt("resultCode");
									if (resultCode != 0) {
										res.setReturnCode("FAIL");
										res.setReturnMsg(result
												.getString("resultMessage"));
										out.write(XmlUtil.toXml(res,
												PayNotifyRes.class));
										// 终止程序，不让用户支付成功
										map.put("code", "400");
										map.put("message", result
												.getString("resultMessage"));
										return map;
									}
									logger.info(LogUtil.info(
											"微信支付回调",
											"天尧分期：先分期后首付，信分宝创建账单，响应码："
													+ resultCode,
											userId,
											"信分宝接口返回结果:"
													+ result.getString("resultMessage")));
								} else {
									res.setReturnCode("FAIL");
									res.setReturnMsg(obj
											.getString("resultMessage"));
									logger.info(LogUtil
											.info("微信支付回调",
													"天尧分期：先分期后首付，登录信分宝接口失败，响应码："
															+ obj.getString("resultCode"),
													userId,
													"信分宝接口返回结果:"
															+ obj.getString("resultMessage")));
									out.write(XmlUtil.toXml(res,
											PayNotifyRes.class));
									// 终止程序，不让用户支付成功
									map.put("code", "400");
									map.put("message",
											obj.getString("resultMessage"));
									return map;
								}
							}
						}
					}
					if ((order.getDownPayment().compareTo(new BigDecimal(0)) > 0 && !order
							.getDownPaymentPayed())
							|| (order.getInstallmentAmount().compareTo(
									new BigDecimal(0)) > 0 && !order
									.getInstallmentPayed())) {
						// 说明至少有一种未支付，什么都不用做
						logger.info(LogUtil.info("微信支付回调", "微信支付完成，首付未完成",
								null, "订单no:" + no));
					} else {
						// 支付完成，修改订单状态，减库存
						logger.info(payNotifyReq.getReturnCode()+">>>>>>>>>>>>>>>>支付完成，修改订单状态，减库存>>>>>>>>>>>>>>>>>>>>>>>>>");
						List<OrderSku> lists = orderSkuDao.getByOrderId(order.getId());
						if(lists==null||lists.size()<=0){
							map.put("code", "400");
							map.put("message", "没有找到该订单商品！");
							return map;
						}
						if ("koo".equals(lists.get(0).getSkuErpCode())) {
							order.setStatus(6); // 0: 草稿 1: 未付款 2: 已付款 3: 已确认
												// 4:已制单 5: 已发货 6: 已签收 9: 已取消',
						} else {
							order.setStatus(2); // 0: 草稿 1: 未付款 2: 已付款 3: 已确认
						}
						// 减库存
						skuService.subtractInventory(order.getId());
						logger.info(LogUtil.info("微信支付回调", "微信支付完成，订单修改为已付款状态",
								null, "订单no:" + no));
						map.put("orderId", order.getId());
					}
					if (orderService.updateOrder(order) != 1) {
						res.setReturnCode("FAIL");
						res.setReturnMsg("修改订单失败");
						logger.info(LogUtil.info("微信支付回调", "修改订单失败", null,
								"订单no:" + no));
						out.write(XmlUtil.toXml(res, PayNotifyRes.class));
						map.put("code", "400");
						map.put("message", "修改订单失败");
						return map;
					}
					/**************** 记录t_order_payment ***************************/
					try {
						Integer num = orderPaymentService.updateStatus(
								order.getId(), 1);
						/***************** 自提订单 ***************************/
						PickupOrder pickupOrder = pickupOrderDao
								.selectByOrderId(order.getId());
						if (pickupOrder != null) {
							pickupOrder.setStatus(1);
							pickupOrderDao
									.updateByPrimaryKeySelective(pickupOrder);
						}
						logger.info("updateStatus" + num + "条");
					} catch (Exception e) {
						e.printStackTrace();
						logger.info(LogUtil.info("微信支付回调",
								"修改t_order_payment异常", order.getCustId()
										.toString(), e.getMessage()));
					}
					// 账单还款
				} else if ("1".equals(type)) {
					// 账单还款
					String userId = attach.split(";")[2];// 所属账单用户Id
					String billArr = attach.split(";")[1];// 账单Id数组
					logger.info(LogUtil.info("微信支付回调", "支付方式为：账单还款", userId,
							"账单id:" + billArr));
					/************** 添加支付记录流水 wyy 2016/08/03 ******************/
					try {
						BillRecode billRecord = new BillRecode();
						String[] ids = null;
						if (billArr != null && !"".equals(billArr)) {
							ids = billArr.split(",");
						} else {
							map.put("code", "400");
							map.put("message", "支付方式为：账单id不能为空！");
							return map;
						}
						// 微信返回的支付完成时间
						String date = payNotifyReq.getTimeEnd();
						String info = "订单no:" + payNotifyReq.getOutTradeNo()
								+ " -- 微信支付流水号："
								+ payNotifyReq.getTransactionId()
								+ " -- 微信返回支付时间：" + date + " --支付银行类型："
								+ payNotifyReq.getBankType();
						logger.info(LogUtil.info("微信支付回调", "微信流水号记录号", null,
								info));
						String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
						date = date.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date dateTime = sdf.parse(date);
						billRecord.setPayTime(dateTime);
						// 付款银行
						billRecord.setAccNo(payNotifyReq.getBankType());
						billRecord.setPayState("A2"); // 已支付
						// 添加记录微信的支付流水号 wyy 2016/07/04 add
						billRecord.setPayNo(payNotifyReq.getTransactionId());
						// 商户内部的订单号
						String no = payNotifyReq.getOutTradeNo();
						billRecord.setTradeNo(no);
						// 是否关注公众号
						if ("Y".equals(payNotifyReq.getIsSubscribe())) {
							billRecord.setIsSubscribe(1);
						} else {
							billRecord.setIsSubscribe(0);
						}
						billRecord.setOpenId(payNotifyReq.getOpenId());
						for (String id : ids) {
							billRecord.setBillId(id);
							// 查看是否已做记录 wyy 2016/08/16
							if (billRecodeDao.getCount(billRecord) != null
									&& billRecodeDao.getCount(billRecord) != 0) {
								// 已存在该记录
							} else {
								billRecodeDao.insertBillRecode(billRecord);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info(LogUtil.info("微信支付回调", "修改t_bill_record异常",
								userId + "--" + billArr, e.getMessage()));
					}
					/************** 添加支付记录流水 END ******************/
					if (!billService.repayment(billArr, userId,
							payNotifyReq.getTotalFee(),
							payNotifyReq.getTransactionId())) {
						res.setReturnCode("FAIL");
						res.setReturnMsg("账单修改失败");
						logger.info(LogUtil.info("微信支付回调", "支付方式为：账单修改失败",
								userId, "账单id:" + billArr));
						out.write(XmlUtil.toXml(res, PayNotifyRes.class));
						map.put("code", "400");
						map.put("message", "支付方式为：微信支付，账单修改失败");
						return map;
					}

				} else {
					res.setReturnCode("FAIL");
					res.setReturnMsg("支付类型错误");
					logger.info(LogUtil.info("微信支付回调", "支付类型错误", null,
							"attach=" + attach));
					out.write(XmlUtil.toXml(res, PayNotifyRes.class));
					map.put("code", "400");
					map.put("message", "支付类型错误");
					return map;
				}

				/*
				 * 更新微信预支付信息表 只更新支付状态为成功 author:Black date:2016-08-24
				 */
				com.tyiti.easycommerce.entity.Unifiedorder tempunifiedorder = new com.tyiti.easycommerce.entity.Unifiedorder();
				// tempunifiedorder.setId(unifiedorder.getId());
				tempunifiedorder.setOutTradeNo(payNotifyReq.getOutTradeNo());
				// 0:未支付 1：已关闭 2：支付成功 3：支付失败（其他原因）
				tempunifiedorder.setPayState(2);
				unifiedorderDao.updateByPrimaryKeySelective(tempunifiedorder);

				// 发送成功通知
				res.setReturnCode("SUCCESS");
				res.setReturnMsg("OK");
				logger.info(LogUtil.info("微信支付回调", "支付成功", null, "attach="
						+ attach));
				out.write(XmlUtil.toXml(res, PayNotifyRes.class));
			} catch (Exception e) {
				e.printStackTrace();
				res.setReturnCode("FAIL");
				res.setReturnMsg(e.getMessage());
				logger.error(LogUtil.info("微信支付回调", "系统异常", null,
						e.getMessage()));
				out.write(XmlUtil.toXml(res, PayNotifyRes.class));
			} finally {
				try {
					if (reader != null) {
						reader.close();
						reader = null;
					}
					if (out != null) {
						out.flush();
						out.close();
						out = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		map.put("code", "200");
		map.put("message", "支付成功");
		return map;
	}

	/**
	 * 微信支付主动查询支付结果 author:Black date:2016-08-23
	 */
	@Override
	public void payordersync(HttpServletRequest request,
			HttpServletResponse response) {
		String requestKey = request.getParameter("key");
		if (StringUtils.isBlank(requestKey)
				|| !requestKey.equals(SysConfig.configMap
						.get(ConfigKey.WX_PARTNER_KEY))) {
			logger.info(">>>>>>>>>>>>>>>>微信支付主动查询支付结果,请求的密钥不正确：" + requestKey
					+ ">>>>>>>>>>>>>>>>>>>>>>>>>");
			return;
		}
		List<com.tyiti.easycommerce.entity.Unifiedorder> unifiedorderList = unifiedorderDao
				.selectPayOrderSync();
		logger.info(">>>>>>>>>>>>>>>>微信支付主动查询支付结果,需要处理的预支付条数："
				+ unifiedorderList.size() + ">>>>>>>>>>>>>>>>>>>>>>>>>");
		for (com.tyiti.easycommerce.entity.Unifiedorder unifiedorder : unifiedorderList) {
			synchronized (this) {
				try {

					PayOrderSyncReq payOrderSyncReq = new PayOrderSyncReq();
					payOrderSyncReq.setAppid(SysConfig.configMap
							.get(ConfigKey.WX_APPID));
					payOrderSyncReq.setMchId(SysConfig.configMap
							.get(ConfigKey.WX_MCHID));
					payOrderSyncReq.setNonceStr(unifiedorder.getNonceStr());// 随机字符串
																			// 需要有含义后期支付
					payOrderSyncReq.setOutTradeNo(unifiedorder.getOutTradeNo());
					payOrderSyncReq.setSign(getSignature(payOrderSyncReq));

					// 组成请求前的xml
					String xml = XmlUtil.toXml(payOrderSyncReq,
							PayOrderSyncReq.class);
					logger.info(LogUtil.info("微信支付主动查询支付结果", "组成请求前的xml", null,
							"xml:" + xml));
					StringBuffer sb = HttpClientUtil.httpsRequest(
							Constants.WX_PAY_ORDER_SYNC_URL, "POST", xml);
					logger.info(LogUtil.info("微信支付主动查询支付结果",
							"请求微信" + Constants.WX_PAY_ORDER_SYNC_URL
									+ ",微信返回结果：" + sb.toString(), null, "xml:"
									+ xml));
					PayOrderSyncRes payOrderSyncRes = (PayOrderSyncRes) XmlUtil
							.fromXml(sb.toString(), PayOrderSyncRes.class);

					String returnCode = payOrderSyncRes.getReturnCode();
					logger.info(returnCode
							+ ">>>>>>>>>>>>>>>>验证交易是否成功>>>>>>>>>>>>>>>>>>>>>>>>>");

					com.tyiti.easycommerce.entity.Unifiedorder tempunifiedorder = new com.tyiti.easycommerce.entity.Unifiedorder();
					tempunifiedorder.setId(unifiedorder.getId());
					// 通信标识
					if (!"SUCCESS".equals(returnCode)) {
						logger.info(LogUtil.info("微信支付主动查询支付结果",
								"ReturnCode不为SUCCESS", null, payOrderSyncRes));
						/*
						 * 更新微信预支付信息表 只更新微信订单查询次数 author:Black date:2016-08-24
						 */
						int paySyncCount = unifiedorder.getPaySyncCount();
						paySyncCount++;
						tempunifiedorder.setPaySyncCount(paySyncCount);
						unifiedorderDao
								.updateByPrimaryKeySelective(tempunifiedorder);
						continue;
					} else if (!"SUCCESS".equals(payOrderSyncRes
							.getTradeState())) {
						logger.info(LogUtil.info("微信支付主动查询支付结果",
								"TradeState不为SUCCESS", null, payOrderSyncRes));
						/*
						 * 更新微信预支付信息表 微信订单查询次数 支付状态 author:Black date:2016-08-24
						 */
						// 0:未支付 1：已关闭 2：支付成功 3：支付失败（其他原因）
						int payState = "NOTPAY".equals(payOrderSyncRes
								.getTradeState()) ? 0 : "CLOSED"
								.equals(payOrderSyncRes.getTradeState()) ? 1
								: 3;
						tempunifiedorder.setPayState(payState);
						int paySyncCount = unifiedorder.getPaySyncCount();
						paySyncCount++;
						tempunifiedorder.setPaySyncCount(paySyncCount);
						unifiedorderDao
								.updateByPrimaryKeySelective(tempunifiedorder);
						continue;
					}

					// >>>业务逻辑
					// 解析附加数据包
					String attach = payOrderSyncRes.getAttach();
					String[] attachArr;
					String type = "";
					if (attach != null && attach.length() > 0) {
						attachArr = attach.split(";", -1);
						type = attachArr[0];
					}
					logger.info(LogUtil.info("微信支付主动查询支付结果", "请求数据包", null,
							"attach=" + attach));
					if ("0".equals(type)) {
						// 用户id
						attachArr = attach.split(";", -1);
						String userId = attachArr[1];
						String xfbInterface = attachArr[3];
						String xfbVersion = attachArr[4];
						String no = payOrderSyncRes.getOutTradeNo();
						logger.info(LogUtil.info("微信支付主动查询支付结果", "支付方式为：首付",
								userId, "订单no:" + no));
						if (StringUtils.isEmpty(no)) {
							logger.info(LogUtil.info("微信支付主动查询支付结果", "订单不存在",
									userId, "订单no:" + no));
							continue;
						}
						Order order = orderService.getByNo(no);
						/**************** 保存支付流水号t_pay_record ***************************/
						try {
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"payOrderSyncRes", order.getCustId()
											.toString(), payOrderSyncRes));
							// 保存支付流水号
							PayRecode payRecode = new PayRecode();
							payRecode.setOrderId(order.getId().toString());
							// 商户内部的订单号
							payRecode.setTradeNo(no);
							// 付款银行
							payRecode.setAccNo(payOrderSyncRes.getBankType());
							payRecode.setPayState("A2"); // 已支付
							// 是否关注公众号Y N
							if ("Y".equals(payOrderSyncRes.getIsSubscribe())) {
								payRecode.setIsSubscribe(1);
							} else {
								payRecode.setIsSubscribe(0);
							}
							payRecode.setOpenId(payOrderSyncRes.getOpenId());
							// 添加记录微信的支付流水号 wyy 2016/07/04 add
							payRecode.setPayNo(payOrderSyncRes
									.getTransactionId());
							// 微信返回的支付完成时间
							String date = payOrderSyncRes.getTimeEnd();
							String info = "订单no:" + no + " -- 微信支付流水号："
									+ payOrderSyncRes.getTransactionId()
									+ " -- 微信返回支付时间：" + date + " --支付银行类型："
									+ payOrderSyncRes.getBankType()
									+ " --订单Id: " + order.getId();
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"微信流水号记录号", order.getCustId().toString(),
									info));
							String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
							date = date.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date dateTime = sdf.parse(date);
							payRecode.setPayTime(dateTime);
							// 查看是否已做记录 wyy 2016/09/12
							if (payRecodeDao.getCount(payRecode.getOrderId(),
									payRecode.getTradeNo()) != null
									&& payRecodeDao.getCount(
											payRecode.getOrderId(),
											payRecode.getTradeNo()) > 0) {
								// 已存在该记录
							} else {
								payRecodeDao.insertPayRecode(payRecode);
							}
						} catch (Exception e) {
							e.printStackTrace();
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"微信流水号记录号异常", order.getCustId().toString(),
									e.getMessage()));
						}
						/**************** 保存支付流水号t_pay_record END ***************************/

						if (order == null || order.getInvalid().equals("1")) {
							logger.info(LogUtil.info("微信支付主动查询支付结果", "订单不存在",
									userId, "订单no:" + no));
							continue;
						}
						if (order.getStatus() >= 2
								|| order.getDownPaymentPayed()) {
							logger.info(LogUtil.info("微信支付主动查询支付结果", "订单已处理",
									userId, "订单no:" + no));
							continue;
						}
						order.setDownPaymentPayed(true);
						order.setDownPaymentTime(new Date());
						// 如果是天尧分期，先分期-后首付的情况，需要单独创建账单
						if (connectedToXfb) {
							logger.info("==========进入天尧分期=========");
							// 判断分期是否有分期，并且分期已支付，如果是该情况，则调用信分宝创建账单（订单分期）接口
							if (order.getInstallmentAmount().compareTo(
									new BigDecimal(0)) > 0
									&& order.getInstallmentPayed()) {
								logger.info(LogUtil.info(
										"微信支付主动查询支付结果",
										"天尧分期：分期已支付，首付也完成，创建账单",
										userId,
										"订单no:" + no + "信分宝订单id:"
												+ order.getStageId()));
								// 获取用户信息
								User user = userDao.getById(Integer
										.parseInt(userId));
								// 先登录信分宝
								logger.info("信分宝参数xfbInterface：" + xfbInterface
										+ "xfbVersion:" + xfbVersion);
								String json = HttpClientUtil.httpLogin(
										xfbInterface, "", user.getMobile(),
										user.getPassword(), xfbVersion);
								logger.info("登录信分宝返回结果json：" + json);
								JSONObject obj = JSONObject.fromObject(json);
								if (obj != null && !obj.isEmpty()) {
									if ("0".equals(obj.getString("resultCode"))) {
										String resString = HttpClientUtil
												.httpOrderStage(
														order.getStageId(),
														order.getInstallmentMonths(),
														xfbInterface,
														user,
														obj.getString("userId"),
														xfbVersion);
										logger.info("调用信分宝创建账单返回结果json："
												+ resString);
										JSONObject result = JSONObject
												.fromObject(resString);
										int resultCode = result
												.getInt("resultCode");
										if (resultCode != 0) {
											logger.info("resultCode != 0");
											continue;
										}
										logger.info(LogUtil.info(
												"微信支付主动查询支付结果",
												"天尧分期：先分期后首付，信分宝创建账单，响应码："
														+ resultCode,
												userId,
												"信分宝接口返回结果:"
														+ result.getString("resultMessage")));
									} else {

										logger.info(LogUtil
												.info("微信支付主动查询支付结果",
														"天尧分期：先分期后首付，登录信分宝接口失败，响应码："
																+ obj.getString("resultCode"),
														userId,
														"信分宝接口返回结果:"
																+ obj.getString("resultMessage")));
										continue;
									}
								}
							}
						}
						if ((order.getDownPayment()
								.compareTo(new BigDecimal(0)) > 0 && !order
								.getDownPaymentPayed())
								|| (order.getInstallmentAmount().compareTo(
										new BigDecimal(0)) > 0 && !order
										.getInstallmentPayed())) {
							// 说明至少有一种未支付，什么都不用做
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"微信支付完成，首付未完成", null, "订单no:" + no));
						} else {
							// 支付完成，修改订单状态，减库存
							logger.info(returnCode
									+ ">>>>>>>>>>>>>>>>支付完成，修改订单状态，减库存>>>>>>>>>>>>>>>>>>>>>>>>>");
							List<OrderSku> lists = orderSkuDao
									.getByOrderId(order.getId());
							if (lists == null || lists.size() <= 0) {
								logger.info("没有找到该订单的商品！：" + requestKey
										+ ">>>>>>>>>>>>>>>>>>>>>>>>>");
								return;
							}
							if (lists.get(0).getSkuErpCode().equals("koo")) {
								order.setStatus(6); // 0: 草稿 1: 未付款 2: 已付款 3:
													// 已确认
													// 4:已制单 5: 已发货 6: 已签收 9:
													// 已取消',
							} else {
								order.setStatus(2); // 0: 草稿 1: 未付款 2: 已付款 3:
													// 已确认
							}
							// 减库存
							skuService.subtractInventory(order.getId());
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"微信支付完成，订单修改为已付款状态", null, "订单no:" + no));
						}
						if (orderService.updateOrder(order) != 1) {

							logger.info(LogUtil.info("微信支付主动查询支付结果", "修改订单失败",
									null, "订单no:" + no));
							continue;
						}
						/**************** 记录t_order_payment ***************************/
						try {
							Integer num = orderPaymentService.updateStatus(
									order.getId(), 1);
							/***************** 自提订单 ***************************/
							PickupOrder pickupOrder = pickupOrderDao
									.selectByOrderId(order.getId());
							if (pickupOrder != null) {
								pickupOrder.setStatus(1);
								pickupOrderDao
										.updateByPrimaryKeySelective(pickupOrder);
							}
							logger.info("updateStatus" + num + "条");
						} catch (Exception e) {
							e.printStackTrace();
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"修改t_order_payment异常", order.getCustId()
											.toString(), e.getMessage()));
						}
						// 账单还款
					} else if ("1".equals(type)) {
						// 账单还款
						String userId = attach.split(";")[2];// 所属账单用户Id
						String billArr = attach.split(";")[1];// 账单Id数组
						logger.info(LogUtil.info("微信支付主动查询支付结果", "支付方式为：账单还款",
								userId, "账单id:" + billArr));
						/************** 添加支付记录流水 wyy 2016/08/03 ******************/
						try {
							BillRecode billRecord = new BillRecode();
							String[] ids = null;
							if (billArr != null && !"".equals(billArr)) {
								ids = billArr.split(",");
							} else {
								logger.info(LogUtil.info("微信支付主动查询支付结果",
										"添加支付记录流水", null, "账单id不能为空！"));
								continue;
							}
							// 微信返回的支付完成时间
							String date = payOrderSyncRes.getTimeEnd();
							String info = "订单no:"
									+ payOrderSyncRes.getOutTradeNo()
									+ " -- 微信支付流水号："
									+ payOrderSyncRes.getTransactionId()
									+ " -- 微信返回支付时间：" + date + " --支付银行类型："
									+ payOrderSyncRes.getBankType();
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"微信流水号记录号", null, info));
							String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
							date = date.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date dateTime = sdf.parse(date);
							billRecord.setPayTime(dateTime);
							// 付款银行
							billRecord.setAccNo(payOrderSyncRes.getBankType());
							billRecord.setPayState("A2"); // 已支付
							// 添加记录微信的支付流水号 wyy 2016/07/04 add
							billRecord.setPayNo(payOrderSyncRes
									.getTransactionId());
							// 商户内部的订单号
							String no = payOrderSyncRes.getOutTradeNo();
							billRecord.setTradeNo(no);
							// 是否关注公众号
							if ("Y".equals(payOrderSyncRes.getIsSubscribe())) {
								billRecord.setIsSubscribe(1);
							} else {
								billRecord.setIsSubscribe(0);
							}
							billRecord.setOpenId(payOrderSyncRes.getOpenId());
							for (String id : ids) {
								billRecord.setBillId(id);
								// 查看是否已做记录 wyy 2016/08/16
								if (billRecodeDao.getCount(billRecord) != null
										&& billRecodeDao.getCount(billRecord) != 0) {
									// 已存在该记录
									logger.info(LogUtil.info("微信支付主动查询支付结果",
											"添加支付记录流水",
											userId + "--" + billArr, "已存在该记录"));
								} else {
									billRecodeDao.insertBillRecode(billRecord);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"修改t_bill_record异常", userId + "--"
											+ billArr, e.getMessage()));
						}
						/************** 添加支付记录流水 END ******************/
						if (!billService.repayment(billArr, userId,
								payOrderSyncRes.getTotalFee(),
								payOrderSyncRes.getTransactionId())) {
							logger.info(LogUtil.info("微信支付主动查询支付结果",
									"支付方式为：账单修改失败", userId, "账单id:" + billArr));
							continue;
						}

					} else {
						logger.info(LogUtil.info("微信支付主动查询支付结果", "支付类型错误",
								null, "attach=" + attach));
						continue;
					}

					/*
					 * 微信查询处理成功，最后更新微信预支付信息表 更新支付状态为成功，请求次数+1 author:Black
					 * date:2016-08-24
					 */
					// 0:未支付 1：已关闭 2：支付成功 3：支付失败（其他原因）
					tempunifiedorder.setPayState(2);
					int paySyncCount = unifiedorder.getPaySyncCount();
					paySyncCount++;
					tempunifiedorder.setPaySyncCount(paySyncCount);
					unifiedorderDao
							.updateByPrimaryKeySelective(tempunifiedorder);

					logger.info(LogUtil.info("微信支付主动查询支付结果", "支付成功", null,
							"attach=" + attach));
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(LogUtil.info("微信支付主动查询支付结果", "系统异常", null,
							e.getMessage()));
					continue;
				} finally {

				}
			}
		}
	}
}
