package com.tyiti.easycommerce.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.ShareCodeUtil;
import com.tyiti.easycommerce.common.SysConfig;
import com.tyiti.easycommerce.common.WxAccessToken;
import com.tyiti.easycommerce.entity.Cart;
import com.tyiti.easycommerce.entity.Coupon;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.Share;
import com.tyiti.easycommerce.entity.ShareReward;
import com.tyiti.easycommerce.entity.ShareRule;
import com.tyiti.easycommerce.entity.ShareUser;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.entity.VerifyCode;
import com.tyiti.easycommerce.repository.CartDao;
import com.tyiti.easycommerce.repository.CouponDao;
import com.tyiti.easycommerce.repository.CouponRecordDao;
import com.tyiti.easycommerce.repository.ShareDao;
import com.tyiti.easycommerce.repository.ShareRewardDao;
import com.tyiti.easycommerce.repository.ShareRuleDao;
import com.tyiti.easycommerce.repository.ShareUserDao;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.repository.VerifyCodeDao;
import com.tyiti.easycommerce.service.CartService;
import com.tyiti.easycommerce.service.CouponService;
import com.tyiti.easycommerce.service.UserService;
import com.tyiti.easycommerce.util.CookieUtils;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.JsonUtils;
import com.tyiti.easycommerce.util.Md5;
import com.tyiti.easycommerce.util.ValidateUtils;
import com.tyiti.easycommerce.util.xml.XmlUtil;
import com.tyiti.easycommerce.util.xml.entity.CSubmitState;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private VerifyCodeDao verifyCodeDao;

	@Autowired
	private ShareDao shareDao;
	@Autowired
	private ShareUserDao shareUserDao;
	@Autowired
	private ShareRuleDao shareRuleDao;
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponRecordDao couponRecordDao;
	@Autowired
	ShareRewardDao shareRewardDao;

	@Value("${sms.url}")
	private String smsUrl;
	@Value("${sms.sname}")
	private String sname;
	@Value("${sms.spwd}")
	private String spwd;
	@Value("${sms.scorpid}")
	private String scorpid;
	@Value("${sms.sprdid}")
	private String sprdid;
	@Value("${sms.tail}")
	private String tail;

	// 信分宝接口地址
	@Value("${xfbInterface}")
	private String xfbInterface;
	// 信分宝接口地址版本号
	@Value("${xfbVersion}")
	private String xfbVersion;
	// 信分宝需要推荐人
	@Value("${recomCode}")
	private String recomCode;
	// 用户是否和信分宝打通
	@Value("${connectedToXfb}")
	private boolean connectedToXfb;
	@Value("${needIdCardWhenResetPassword}")
	private boolean needIdCardWhenResetPassword;
	// sys
	@Value("${sys}")
	private String sys;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartDao cartDao;

	// 微信下载图片保存地址
	@Value("${wxDownPicUrl}")
	private String wxDownPicUrl;

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 按手机号码和密码登录
	 * 
	 * @param session
	 * @param mobile
	 * @param password
	 * @return
	 */
	public Map<String, Object> signup(HttpSession session,HttpServletRequest request, User user) {
		Map<String, Object> map = new HashMap<String, Object>();

		String mobile = user.getMobile();
		Md5 md5 = new Md5();
		String password = md5.getMD5ofStr(user.getPassword());
		user = userDao.findByMobileAndPassword(mobile, password);
		if (user != null) {
			if (SysConfig.configMap.get("need_open_id") != null)
				if (SysConfig.configMap.get("need_open_id").equals("1")) {
					Object openId = session.getAttribute(Constants.USER_OPENID);
					if (openId == null) {
						// 判断当前用户是否从微信端来的， 如果不是 那么openId 为null 这时去请求获取openId
						map.put("code", 302);
						map.put("messsge", "没有OpenId");
						return map;

					}
					user.setOpenId(String.valueOf(openId));
				}

			// session 添加到 redis 中
			session.setAttribute(Constants.USER, user);
			userDao.updateOpenId(user);
			map.put("code", 200);
			map.put("message", "OK");
			map.put("data", user);
			try {
				// add by hcy 增加登录的时候 实体化cookie
				MergeCart(request.getSession(),user.getId());
			} catch (Exception e) {
				e.getStackTrace();
			}
			
		} else {
			map.put("code", 400);
			map.put("message", "用户名或密码错误");
		}

		return map;
	}
	
	private void MergeCart(HttpSession session,Integer userId) {
		// TODO Auto-generated method stub
		String cartJson  = null ; 
		if (session.getAttribute(Constants.CART_INFO) != null) {
			  cartJson = String.valueOf(session.getAttribute(
					  Constants.CART_INFO));
			}
		if (cartJson != null) {
			// 把json转换成商品列表(cookie 里面的商品)
			List<Cart> list = JsonUtils.jsonToList(cartJson, Cart.class);
			// 数据库里面的商品
			for(Cart cart:list){ 
				Cart cartDB = cartDao.selectCartBySku(cart.getSkuId(), userId);
				if(cartDB != null){
					cartDB.setCount(cartDB.getCount()+cart.getCount());
					cartDB.setInstallmentMonths(cart.getInstallmentMonths());
					cartDao.updateByPrimaryKeySelective(cartDB);
				}else{
					Cart addCart = new Cart();
					addCart.setCount(cart.getCount());
					addCart.setUserId(userId);
					addCart.setSkuId(cart.getSkuId());
					addCart.setInstallmentMonths(cart.getInstallmentMonths());
					addCart.setStatus(cart.getStatus());
					cartDao.insertSelective(addCart);
				}
			}
		}
	}

	/**
	 * 用户注销
	 * 
	 * @param session
	 * @return
	 */
	public Map<String, Object> signout(HttpServletRequest request,HttpServletResponse response,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		session.removeAttribute(Constants.USER);
		// 清除cookie中购物车
		CookieUtils.deleteCookie(request, response, Constants.CART_INFO);
		map.put("code", 200);
		map.put("message", "OK");

		return map;
	}

	/**
	 * 修改密码
	 * 
	 * @param session
	 * @param paramUser
	 * @return
	 */
	public Map<String, Object> changePassword(HttpSession session,
			User paramUser) {
		Md5 md5 = new Md5();
		paramUser.setPassword(md5.getMD5ofStr(paramUser.getPassword()));
		paramUser.setNewPassword(md5.getMD5ofStr(paramUser.getNewPassword()));

		String password = paramUser.getPassword();

		Map<String, Object> map = new HashMap<String, Object>();

		User user = (User) session.getAttribute(Constants.USER);
		user.setNewPassword(paramUser.getNewPassword());
		if (!user.getPassword().equalsIgnoreCase(password)) {
			map.put("code", 400);
			map.put("message", "原密码错误");
		} else {
			if (user.getPassword().equalsIgnoreCase(paramUser.getNewPassword())) {
				map.put("code", 400);
				map.put("message", "新密码和旧密码不能相同");
			} else {
				map = updatePassword(session, user);
			}
		}

		return map;
	}

	@Override
	public Map<String, Object> updatePassword(HttpSession session, User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 200);
		map.put("message", "修改成功！");

		boolean needSaveSession = false;
		int affectedRows = 0;
		// 找回密码时，传进来的参数只有 mobile 没有 id
		if (user.getId() == null) {
			affectedRows = userDao.updatePasswordByMobile(user);
		} else {
			needSaveSession = true;// 只有在改密码时更新session
			affectedRows = userDao.updatePassword(user);
		}

		if (affectedRows > 0) {
			if (connectedToXfb) {
				// 修改信分宝用户密码
				String response = HttpClientUtil.httpUpdatePassword(
						xfbInterface, user, xfbVersion);
				// 判断信分宝修改密码是否成功
				JSONObject result = JSONObject.fromObject(response);
				int resultCode = result.getInt("resultCode");
				if (resultCode != 0) {
					map.put("code", 500);
					map.put("resultCode", resultCode);
					map.put("message", result.getString("resultMessage"));
				} else {
					// 修改成功之后需要重新登录才可以
					HttpClientUtil.httpLogin(xfbInterface,
							user.getThirdPartyId(), user.getMobile(),
							user.getNewPassword(), xfbVersion);
				}
			}
		} else {
			map.put("code", 500);
			map.put("message", "更新数据库失败");
		}

		int code = (int) map.get("code");
		if (needSaveSession && code == 200) {
			session.setAttribute(Constants.USER,
					userDao.findByMobile(user.getMobile()));
		}

		return map;
	}

	@Override
	public Map<String, Object> updatePayPassword(HttpSession session, User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		User sessionUser = (User) session.getAttribute(Constants.USER);

		Md5 md5 = new Md5();

		int affectedRows = userDao.updatePayPassword(sessionUser.getId(),
				md5.getMD5ofStr(user.getPayPassword()));
		if (affectedRows > 0) {
			map.put("code", 200);
			map.put("message", "OK");

			/* 更新session中支付密码 */
			sessionUser.setPayPassword(md5.getMD5ofStr(user.getPayPassword()));
			session.setAttribute(Constants.USER, sessionUser);
		} else {
			map.put("code", 500);
			map.put("message", "更新数据库失败");
		}

		return map;
	}

	@Override
	public Map<String, Object> doAuth(HttpSession session, User user) {
		String mobile = user.getMobile();
		String verifyCode = user.getVerifyCode();
		// String idCard = user.getIdCard();
		Map<String, Object> map = new HashMap<String, Object>();

		// 检查短信验证码是否正确
		boolean verifyCodeValid = checkVerifyCode(mobile, verifyCode);
		if (!verifyCodeValid) {
			map.put("code", 400);
			map.put("message", "短信验证码错误");
			return map;
		}

		User userRecord = userDao.findByMobile(mobile);
		if (userRecord == null) {
			map.put("code", 400);
			map.put("message", "该手机号未注册");
			return map;
		}

		/*
		 * // 忘记密码时，天尧分期还需要身份证 if (needIdCardWhenResetPassword &&
		 * userRecord.getIdCard() != null &&
		 * !userRecord.getIdCard().equalsIgnoreCase(idCard)) { map.put("code",
		 * 400); map.put("message", "手机号或身份证号有误"); return map; }
		 */

		map.put("code", 200);
		map.put("message", "OK");
		map.put("data", userRecord);
		return map;
	}

	@Override
	public Map<String, Object> doPayAuth(HttpSession session, User user) {
		User sessionUser = (User) session.getAttribute(Constants.USER);

		String mobile = sessionUser.getMobile();
		String verifyCode = user.getVerifyCode();
		String idCard = user.getIdCard();
		Map<String, Object> map = new HashMap<String, Object>();

		// 检查短信验证码是否正确
		boolean verifyCodeValid = checkVerifyCode(mobile, verifyCode);
		if (!verifyCodeValid) {
			map.put("code", 400);
			map.put("message", "短信验证码错误");
			return map;
		}

		// 忘记密码时，天尧分期还需要身份证
		if (!sessionUser.getIdCard().equalsIgnoreCase(idCard)) {
			map.put("code", 400);
			map.put("message", "身份证号有误");
			return map;
		}

		map.put("code", 200);
		map.put("message", "OK");
		return map;
	}

	/**
	 * 发送短信验证码
	 */
	public Map<String, Object> sendSmsForVerifyCode(String mobile,
			String verifyCode) {
		Map<String, String> requestParams = new HashMap<String, String>();
		Map<String, Object> ret = new HashMap<String, Object>();

		requestParams.put("sname", sname);
		requestParams.put("spwd", spwd);
		requestParams.put("scorpid", scorpid);
		requestParams.put("sprdid", sprdid);
		requestParams.put("sdst", mobile);
		requestParams.put("smsg", verifyCode + tail);

		// 解析返回的 XML
		String responseXml = HttpClientUtil
				.httpGet(smsUrl, null, requestParams);
		CSubmitState responseData = (CSubmitState) XmlUtil.fromXml(responseXml,
				CSubmitState.class);
		// 解析 XML 失败
		if (responseData == null) {
			ret.put("state", 1);
			ret.put("message", "解析 XML 失败");
			return ret;
		}

		ret.put("state", (int) responseData.getState());
		ret.put("message",
				responseData.getState() + ": " + responseData.getMsgState());
		return ret;
	}

	/**
	 * 生成短信验证码并存入数据库
	 */
	public Map<String, Object> getVerifyCode(String mobile, String template) {
		Integer rand = (int) (Math.random() * 900000 + 100000);
		String randStr = rand.toString();

		// 插入数据库
		VerifyCode verifyCode = new VerifyCode();
		verifyCode.setMobile(mobile);
		verifyCode.setVerifyCode(randStr);
		verifyCodeDao.add(verifyCode);

		// 发送短信
		String smsContent = template.replaceAll("%verify_code%", randStr);
		return sendSmsForVerifyCode(mobile, smsContent);
	}

	/**
	 * 验证短信验证码
	 */
	public boolean checkVerifyCode(String mobile, String verifyCode) {
		VerifyCode verifyCodeEntity = verifyCodeDao.findLastByMobile(mobile);
		if (verifyCodeEntity == null) {
			return false;
		}

		return verifyCodeEntity.getVerifyCode().equals(verifyCode);
	}

	/**
	 * 用户注册
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	public Map<String, Object> register(HttpSession session, User user) {
		// 判断推荐码是否存在，不存在从配置文件中获取 wyy 2016/05/23
		if (user.getRecByCode() == null && user.getRecByCode() == "") {
			user.setRecByCode(recomCode);
		}

		// 分享码 wyy 2016/08/02
		String shareCode = null;
		if (user.getShareCode() != null && !"".equals(user.getShareCode())) {
			shareCode = user.getShareCode();
		}

		String mobile = user.getMobile();
		String verifyCode = user.getVerifyCode();

		log.info("用户注册：Mobile " + mobile);
		Map<String, Object> map = new HashMap<String, Object>();

		// 判断是否已登录
		if (session.getAttribute(Constants.USER) != null) {
			map.put("code", 400);
			map.put("message", "已登录，不允许注册");
			log.info("用户注册：已登录，不允许注册");
			return map;
		}

		// 检查短信验证码是否正确
		boolean verifyCodeValid = checkVerifyCode(mobile, verifyCode);
		if (!verifyCodeValid) {
			map.put("code", 400);
			map.put("message", "短信验证码错误");
			log.info("用户注册：短信验证码错误");
			return map;
		}
		// 检查手机号是否已注册
		if (userDao.findByMobile(mobile) != null) {
			map.put("code", 400);
			// 查看是否存在分享码,老用户已注册 添加分享记录 wyy 2016/08/01
			if (shareCode != null && !"".equals(shareCode)) {
				map.put("message", "手机号已被注册，无法参加此活动");
			} else {
				map.put("message", "该手机号已注册");
			}
			log.info("用户注册：该手机号已注册");
			return map;
		}

		// 把传过来的密码加密保存 wyy 2016/05/17 解决多个系统的问题
		Md5 md5 = new Md5();
		user.setPassword(md5.getMD5ofStr(user.getPassword()));

		// 注册用户
		if (SysConfig.configMap.get("need_open_id") != null)
			if (SysConfig.configMap.get("need_open_id").equals("1")) {
				Object openId = session.getAttribute(Constants.USER_OPENID);
				if (openId == null) {
					// 判断当前用户是否从微信端来的， 如果不是 那么openId 为null 这时去请求获取openId
					map.put("code", 302);
					map.put("message", "没有OpenId");
					log.info("用户注册：302 没有OpenId");
					return map;
				}
				user.setOpenId(String.valueOf(openId));
			}

		// 需要和信分宝用户打通
		if (connectedToXfb) {
			Map<String, Object> result = xfbRegister(user);
			int code = (int) result.get("code");
			if (code != 200) {
				map.put("code", result.get("resultCode"));
				map.put("message", result.get("message"));
				log.info("用户注册：信分宝注册失败 Code " + result.get("resultCode")
						+ " | Message " + result.get("message"));
				return map;
			}
			user = (User) result.get("user");
		}

		// 如果第三方类型为空，赋值为0
		if (user.getThirdPartyType() == null) {
			user.setThirdPartyType(0);
		}

		// 注册 插入数据
		userDao.register(user);

		user = userDao.getById(user.getId());

		session.setAttribute(Constants.USER, user);

		Map<String, Object> data = new HashMap<String, Object>();
		if (connectedToXfb) {
			data.put("userId", user.getThirdPartyId());
		}
		log.info("用户注册：注册成功！xfbUserId " + user.getThirdPartyId());

		// 查看是否存在分享码 wyy 2016/08/01
		if (shareCode != null && !"".equals(shareCode)) {
			user.setShareCode(shareCode);
			Map<String, Object> shareMap = shareGift(user);
			Integer resultCode = Integer.parseInt(shareMap.get("code")
					.toString());
			if (resultCode == 200) {
				data.put("couponName", shareMap.get("couponName"));
				data.put("couponId", shareMap.get("couponId"));
				data.put("couponNum", 1);
			} else {
				log.info("分享有礼赠送优惠券返回：" + shareMap);
			}
		}
		map.put("code", 200);
		map.put("message", "OK");
		map.put("data", data);

		return map;
	}

	public Map<String, Object> createConnection(HttpSession session, User user) {
		String mobile = user.getMobile();
		String verifyCode = user.getVerifyCode();
		String thirdPartyId = (String) session
				.getAttribute(Constants.THIRD_PARTY_ID);
		Map<String, Object> map = new HashMap<String, Object>();
		/*
		 * if (thirdPartyId == null) { map.put("code", 400); map.put("message",
		 * "第三方Id不存在"); return map; }
		 */

		// 检查短信验证码是否正确
		boolean verifyCodeValid = checkVerifyCode(mobile, verifyCode);
		if (!verifyCodeValid) {
			map.put("code", 400);
			map.put("message", "短信验证码错误");
			return map;
		}

		User userRecord;
		if (thirdPartyId != null) {
			thirdPartyId = new String(
					Base64Utils.decodeFromString(thirdPartyId));
			userRecord = userDao.findByThirdParty(2, thirdPartyId);
			if (userRecord != null) {
				// 已经绑定，重复绑定下
				if (userRecord.getMobile().equals(mobile)) {
					session.setAttribute(Constants.USER, userRecord);
					try {
						//合并购物车
						MergeCart(session, userRecord.getId());
					} catch (Exception e) {
						// TODO: handle exception
						e.getStackTrace();
					}
					map.put("code", 200);
					map.put("message", "ok");
					return map;
					// 此ID已绑定另外一个手机号，不能再次绑定
				} else {
					map.put("code", 400);
					map.put("message", "此ID已绑定另外一个手机，不能再次绑定");
					return map;
				}
			}
		}

		userRecord = userDao.findByMobile(mobile);
		// 如果这个手机号已经绑定，则更新 id （重新绑定到新的 id）即可
		if (userRecord != null) {
			if (thirdPartyId != null) {
				userRecord.setThirdPartyType(2);
				userRecord.setThirdPartyId(thirdPartyId);
				userDao.updateThirdPartyId(userRecord);
			}
			session.setAttribute(Constants.USER, userRecord);
			try {
				//合并购物车
				MergeCart(session, userRecord.getId());
			} catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
			}
			map.put("code", 200);
			map.put("message", "ok");
			return map;
		}

		if (userDao.addConnectedUser(user) <= 0) {
			map.put("code", 400);
			map.put("message", "绑定账号错误！");
			return map;
		}

		// 正确绑定，需要更新 session
		user = userDao.getById(user.getId());
		session.setAttribute(Constants.USER, user);
		
		map.put("code", 200);
		map.put("message", "ok");
		return map;
	}

	/**
	 * 去信分宝注册用户
	 * 
	 * @param user
	 * @return
	 */
	private Map<String, Object> xfbRegister(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 此密码为 商城存在的密码 与信分宝存的密码相同
		String response = HttpClientUtil
				.httpReg(xfbInterface, user, xfbVersion);
		log.info("用户注册 注册信分宝：response " + response);
		// 先判断信分宝注册是否成功
		JSONObject result = JSONObject.fromObject(response);
		int resultCode = result.getInt("resultCode");
		if (resultCode != 0) {
			map.put("code", 500);
			map.put("resultCode", resultCode);
			map.put("message", result.getString("resultMessage"));
			return map;
		}
		// 判断是否为信用卡用户，如果是，则ThirdPartyType=2；否，则ThirdPartyType= 1. Edited by 黄义
		String isCreditCardUser = result.getString("isCreditCardUser");
		if ("true".equals(isCreditCardUser)) {
			user.setThirdPartyType(2);
		} else {
			user.setThirdPartyType(1);
		}
		// 登录信分宝获取用户Id
		response = HttpClientUtil.httpLogin(xfbInterface, "", user.getMobile(),
				user.getPassword(), xfbVersion);
		log.info("用户注册 登录信分宝：response " + response);

		result = JSONObject.fromObject(response);
		resultCode = result.getInt("resultCode");
		if (resultCode != 0) {
			map.put("code", 500);
			map.put("resultCode", resultCode);
			map.put("message", result.getString("resultMessage"));
			return map;
		}
		String xfbUserId = result.getString("userId");
		user.setThirdPartyId(xfbUserId);

		map.put("code", 200);
		map.put("user", user);
		return map;
	}

	@Override
	public Map<String, Object> getUserScore(User user, Map<String, Object> data) {
		if (connectedToXfb) {
			// 先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, "",
					user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					String response = HttpClientUtil.httpGetUserScore(
							xfbInterface, user, obj.getString("userId"),
							xfbVersion);
					JSONObject result = JSONObject.fromObject(response);
					int resultCode = result.getInt("resultCode");
					if (resultCode != 0) {
						data.put("code", 500);
						data.put("resultCode", resultCode);
						// 授信状态，0：未授信 1：授信中 2：授信成功 3：授信失败
						data.put("creditState", "");
						// 授信步骤
						data.put("creditStep", "");
						data.put("message", result.getString("resultMessage"));
						return data;
					} else {
						// 总额度
						data.put("creditSum", result.getString("creditSum"));
						// 可使用额度
						data.put("useSum", result.getString("useSum"));
						// 授信状态，0：未授信 1：授信中 2：授信成功 3：授信失败
						data.put("creditState", result.getString("creditState"));
						// 授信步骤
						data.put("creditStep", result.getString("creditStep"));
					}
				} else {
					data.put("resultCode", obj.getString("resultCode"));
					data.put("message", obj.getString("resultMessage"));
				}
			}
		}
		return data;
	}

	/**
	 * <p>
	 * 功能描述:。
	 * </p>
	 * 
	 * @param userId
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年5月18日 下午3:56:31。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public User getById(Integer userId) {
		return userDao.getById(userId);
	}


	@Override
	public Map<String, Object> creditSubmit(User user) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(user.getRealName())
				&& ValidateUtils.chekIdCard(user.getIdCard(), -1)) {
			userDao.updateUserRealNameAndIdcard(user);
			// 先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, "",
					user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if ("0".equals(obj.getString("resultCode"))) {
				String response = HttpClientUtil
						.httpGetcreditSubmit(xfbInterface, user,
								obj.getString("userId"), xfbVersion);
				JSONObject result = JSONObject.fromObject(response);
				int resultCode = result.getInt("resultCode");
				if (resultCode != 0) {
					resMap.put("code", 500);
					resMap.put("resultCode", resultCode);
					resMap.put("messsge", result.getString("resultMessage"));
					return resMap;
				} else {
					resMap.put("code", 200);
					resMap.put("data", result);
					resMap.put("messsge", "ok");
				}
			} else {
				resMap.put("resultCode", obj.getString("resultCode"));
				resMap.put("messsge", obj.getString("resultMessage"));
			}
		} else {
			resMap.put("code", 400);
			resMap.put("messsge", "姓名或者身份证不合法");
		}
		return resMap;
	}

	@Override
	public Map<String, Object> creditStep(User user) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 先登录信分宝
		String json = HttpClientUtil.httpLogin(xfbInterface, "",
				user.getMobile(), user.getPassword(), xfbVersion);
		JSONObject obj = JSONObject.fromObject(json);
		if ("0".equals(obj.getString("resultCode"))) {
			String response = HttpClientUtil.httpGetCreditStep(xfbInterface,
					user, obj.getString("userId"), xfbVersion);
			JSONObject result = JSONObject.fromObject(response);

			int resultCode = result.getInt("resultCode");
			if (resultCode != 0) {
				resMap.put("code", 500);
				resMap.put("resultCode", resultCode);
				resMap.put("message", result.getString("resultMessage"));
			} else {
				resMap.put("code", 200);
				resMap.put("data", result);
				resMap.put("messsge", "OK");
			}
		} else {
			resMap.put("code", obj.getString("resultCode"));
			resMap.put("message", obj.getString("resultMessage"));
		}
		return resMap;
	}

	public Map<String, Object> getPrivateInfo(User user) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 先登录信分宝
		String json = HttpClientUtil.httpLogin(xfbInterface, "",
				user.getMobile(), user.getPassword(), xfbVersion);
		JSONObject obj = JSONObject.fromObject(json);
		if (obj != null && !obj.isEmpty()) {
			if ("0".equals(obj.getString("resultCode"))) {
				String response = HttpClientUtil
						.httpGetPrivateInfo(xfbInterface, user,
								obj.getString("userId"), xfbVersion);
				JSONObject result = JSONObject.fromObject(response);
				int resultCode = result.getInt("resultCode");
				if (resultCode != 0) {
					data.put("code", resultCode);
					data.put("message", result.getString("resultMessage"));
				} else {
					data.put("code", 200);
					data.put("data", result);
					data.put("messsge", "OK");
				}
			} else {
				data.put("code", obj.getString("resultCode"));
				data.put("message", obj.getString("resultMessage"));
			}
		}
		return data;
	}

	/**
	 * @description 获取用户的分享码
	 * @author wyy 2016/08/02
	 */
	public Map<String, Object> getShareCode(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Share share = new Share();
			share.setUserId(user.getId());
			Share shareEntity = shareDao.selectByShare(share);
			if (shareEntity == null) {
				// 生成分享码 6+4=10位分享码
				share.setShareCode(ShareCodeUtil.toSerialCode(user.getId())
						+ GetCode().substring(1, 4));
				share.setUserId(user.getId());
				share.setCreateTime(new Date());
				shareDao.addShare(share);
			} else {
				share = shareEntity;
			}
			map.put("code", 200);
			map.put("mobile", user.getMobile());
			map.put("shareCode", share.getShareCode());
		} catch (Exception e) {
			map.put("code", 400);
			map.put("message", e.getMessage());
			log.info("获取分享码的时候报异常了！---" + e.getMessage());
		}
		return map;

	}

	/**
	 * @description 生成八位分享码
	 * @author wyy 2016/08/02
	 * @return 分享码
	 */
	public String GetCode() {
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
				"u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z" };
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

	/**
	 * @description 分享有礼赠送优惠券
	 * @param user
	 *            注册信息
	 * @author wyy 2016/08/18
	 * @return
	 */
	public Map<String, Object> shareGift(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 获取分享码实体
			Share share = new Share();
			share.setShareCode(user.getShareCode());
			Share shareEntity = shareDao.selectByShare(share);
			if (shareEntity == null) {
				map.put("code", "400");
				map.put("message", "推荐码不存在");
				return map;
			}
			// 1、 做一下注册记录 t_share_user
			ShareUser shareUser = new ShareUser();
			shareUser.setCreateTime(new Date());
			shareUser.setShareId(shareEntity.getId());
			shareUser.setUserId(user.getId());
			shareUserDao.addShareUser(shareUser);

			// 获取奖励规则（注册者）
			ShareRule shareRule = new ShareRule();
			shareRule.setType(1);// 注册者
			shareRule.setStatus(1);// 状态
			ShareRule shareRuleObj = shareRuleDao.selectByShareRule(shareRule);
			// 2.1、向注册者发送优惠券
			Integer couponRecordId = couponService.sendCouponRecord(
					user.getId(), shareRuleObj.getCouponId());
			CouponRecord couponRecord = couponRecordDao
					.selectByPrimaryKey(couponRecordId);
			Coupon coupon = couponDao.selectByPrimaryKey(couponRecord
					.getCouponId());
			// 2.2、奖励记录表
			ShareReward shareReward = new ShareReward();
			shareReward.setCouponRecordId(couponRecordId);
			shareReward.setCreateTime(new Date());
			shareReward.setShareId(shareEntity.getId());
			shareReward.setShareRuleId(shareRuleObj.getId());
			shareReward.setUserId(user.getId());
			shareRewardDao.add(shareReward);

			// 3.1、向分享者发放第一张优惠券
			shareRule.setType(2);// 分享者
			shareRule.setStatus(1);// 状态
			shareRuleObj = shareRuleDao.selectByShareRule(shareRule);
			couponRecordId = couponService.sendCouponRecord(
					shareEntity.getUserId(), shareRuleObj.getCouponId());
			// 3.2、奖励记录
			shareReward.setCouponRecordId(couponRecordId);
			shareReward.setCreateTime(new Date());
			shareReward.setShareId(shareEntity.getId());
			shareReward.setShareRuleId(shareRuleObj.getId());
			shareReward.setUserId(shareEntity.getUserId());
			shareRewardDao.add(shareReward);

			// 4.1查询通过该注册码成功注册的人数
			shareUser = new ShareUser();
			shareUser.setShareId(shareEntity.getId());
			Integer extraThreshold = shareUserDao
					.getCountByShareUser(shareUser);
			// 4.2向分享者发放第二张优惠券
			shareRule.setType(3);// 分享者
			shareRule.setStatus(1);// 状态
			shareRule.setExtraThreshold(extraThreshold);
			shareRuleObj = shareRuleDao.selectByShareRule(shareRule);
			if (shareRuleObj != null) {
				couponRecordId = couponService.sendCouponRecord(
						shareEntity.getUserId(), shareRuleObj.getCouponId());
				// 4.3、添加奖励记录
				shareReward.setCouponRecordId(couponRecordId);
				shareReward.setCreateTime(new Date());
				shareReward.setShareId(shareEntity.getId());
				shareReward.setShareRuleId(shareRuleObj.getId());
				shareReward.setUserId(shareEntity.getUserId());
				shareRewardDao.add(shareReward);
			}
			// 返回消息
			map.put("code", 200);
			map.put("couponId", coupon.getId());
			map.put("couponName", coupon.getName());
		} catch (Exception e) {
			map.put("code", 400);
			map.put("message", e.getMessage());
			log.info("分享注册，发放优惠券时报异常信息：" + e.getMessage() + "---"
					+ e.getLocalizedMessage());
		}
		return map;
	}

	public Map<String, Object> getShareUser(String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Share share = new Share();
			share.setShareCode(code);
			Share shareObj = shareDao.selectByShare(share);
			map.put("code", 200);
			map.put("data", shareObj.getMobile().substring(0, 3) + "****"
					+ shareObj.getMobile().substring(7));
		} catch (Exception e) {
			map.put("code", 400);
			map.put("data", e.getMessage());
		}
		return map;
	}

	/**
	 * @author wyy 2016/07/15
	 * @description 根据实体修改数据
	 * @param user
	 * @return
	 */
	public Map<String, Object> updateByPrimaryKeySelective(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (userDao.updateByPrimaryKeySelective(user) > 0) {
				map.put("code", 200);
				map.put("messsge", "ok!");
			} else {
				map.put("code", 400);
				map.put("messsge", "修改失败！");
			}

		} catch (Exception e) {
			map.put("code", 400);
			map.put("messsge", e.getMessage());
		}
		return map;
	}

	/**
	 * @author wyy 2016/09/22
	 * @description 提交图片
	 * @param session
	 * @param imgUrl
	 * @return
	 */
	@Override
	public Map<String, Object> returnImg(HttpSession session, String mediaId,
			String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User user = (User) session.getAttribute(Constants.USER);
			log.info("退货上传图片，微信mediaId=" + mediaId + ",用户信息：" + user);
			// 微信的全局access_token
			String accessToken = WxAccessToken.getAccessToken();
			if (accessToken == null || accessToken == ""
					|| accessToken == "error") {
				map.put("code", 403);
				map.put("message", "获取微信accessToken出错！");
				return map;
			}
			// 从微信下载素材文件
			String wxAccessTokenUrl = "http://api.weixin.qq.com/cgi-bin/media/get?access_token="
					+ accessToken + "&media_id=" + mediaId;
			log.info("微信下载图片链接：" + wxAccessTokenUrl);
			// 获取文件流数组
			byte[] inputStreamArry = getImageFromNetByUrl(wxAccessTokenUrl,
					"GET");
			if (inputStreamArry.length <= 0) {
				map.put("code", 4001);
				map.put("message", "获取的字节流数组为空！");
				map.put("data", "");
				return map;
			}
			String wxInfo = new String(inputStreamArry);
			if (wxInfo.contains("errmsg")) {
				map.put("code", 4001);
				map.put("message", wxInfo);
				map.put("data", "");
				return map;
			}
			// 下载微信图片保存到本地的地址
			String date = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
			String relativePath = user.getMobile() + "_" + type + "_" + date
					+ ".jpg";
			String fileUrl = wxDownPicUrl + relativePath;
			// 保存到本地
			if (!HttpClientUtil.writeImageToDisk(inputStreamArry, fileUrl)) {
				map.put("code", "4002");
				map.put("message", "微信保存本地图片出错," + "微信地址:" + wxAccessTokenUrl);
				return map;
			}
			map.put("code", "200");
			map.put("message", "ok");
			map.put("imgUrl", relativePath);
		} catch (Exception e) {
			map.put("code", "400");
			map.put("message", e.getMessage());
			log.error("退货上传图片(异常捕获)：" + e);
		}
		return map;
	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 */
	public static byte[] getImageFromNetByUrl(String strUrl,
			String RequestMethod) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(RequestMethod);
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			// log.error("根据地址获得数据的字节流(异常捕获):" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			inStream.close();
		} catch (Exception e) {
			// log.error("从输入流中获取数据(异常捕获):" + e);
		}
		return outStream.toByteArray();
	}

}
