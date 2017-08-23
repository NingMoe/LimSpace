package com.tyiti.easycommerce.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.WxAccessToken;
import com.tyiti.easycommerce.entity.Contac;
import com.tyiti.easycommerce.entity.Credit;
import com.tyiti.easycommerce.entity.MemberInfo;
import com.tyiti.easycommerce.entity.JobInfo;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.service.CreditService;
import com.tyiti.easycommerce.util.HttpClientUtil;

@Service
public class CreditServiceImpl implements CreditService {

	// 信分宝接口地址
	@Value("${xfbInterface}")
	private String xfbInterface;
	// 信分宝接口地址版本号
	@Value("${xfbVersion}")
	private String xfbVersion;
	// 微信下载图片保存地址
	@Value("${wxDownPicUrl}")
	private String wxDownPicUrl;
	@Autowired
	private UserDao userDao;

	/**
	 * 获取用户授信获取用户的授信状态
	 * 
	 * @return
	 */
	public Map<String, Object> Status(HttpSession session) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}
		User sessionUser = (User) session.getAttribute(Constants.USER);
		if (sessionUser == null || sessionUser.getThirdPartyId() == null
				|| sessionUser.getThirdPartyId() == "") {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		// 获取用户的授信状态
		String statusJson = HttpClientUtil.httpCreditStatus(xfbInterface,
				sessionUser, xfbVersion);
		JSONObject statusStr = JSONObject.fromObject(statusJson);
		Integer resultCode = Integer
				.parseInt(statusStr.getString("resultCode"));
		if (resultCode != 0) {
			if (resultCode == 6777) {
				// 修改成功之后需要重新登录才可以
				Map<String, Object> result = XfbLogin(sessionUser);
				Integer code = (Integer) result.get("code");
				if (code != 0) {
					map.put("code", code);
					map.put("message", "登录" + result.get("message"));
					map.put("data", "");
					return map;
				}
				statusJson = HttpClientUtil.httpCreditStatus(xfbInterface,
						sessionUser, xfbVersion);
				statusStr = JSONObject.fromObject(statusJson);
				resultCode = Integer
						.parseInt(statusStr.getString("resultCode"));
			}
			if (resultCode != 0) {
				map.put("code", resultCode);
				map.put("message", statusStr.getString("resultMessage"));
				return map;
			}
		}
		// 获取授信状态实体
		Credit credit = new Credit();
		credit.setType(statusStr.getString("type"));
		credit.setUserRecomNum(statusStr.getString("userRecomNum"));
		credit.setisDisplayNopass(statusStr.getString("userRecomNum"));

		// 个人信息
		JSONObject memberStr = JSONObject.fromObject(statusStr
				.getString("memberInfo"));
		credit.setMemberInfoId(memberStr.getString("id"));
		credit.setMemberInfoState(memberStr.getString("state"));
		// 工作信息
		JSONObject jobStr = JSONObject.fromObject(statusStr
				.getString("jobInfo"));
		credit.setJobInfoId(jobStr.getString("id"));
		credit.setJobInfoState(jobStr.getString("state"));
		// 联系人信息
		JSONObject contactsStr = JSONObject.fromObject(statusStr
				.getString("contactsInfo"));
		credit.setContactsInfoId(contactsStr.getString("id"));
		credit.setContactsInfoState(contactsStr.getString("state"));

		// session添加到redis中 信分宝授信时的用户信息
		session.setAttribute(Constants.XFB_Credit, credit);

		map.put("code", 200);
		map.put("messsge", "OK");
		map.put("data", credit);

		return map;
	}

	/**
	 * 获取用户授信获取用户的授信
	 * 
	 * @return
	 */
	public Map<String, Object> MemberInfo(HttpSession session, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}
		User sessionUser = (User) session.getAttribute(Constants.USER);
		if (sessionUser == null || sessionUser.getThirdPartyId() == null
				|| sessionUser.getThirdPartyId() == "") {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		// 获取用户的授信信息
		String msgJson = HttpClientUtil.httpCreditGetMemerInfo(xfbInterface,
				sessionUser, id, xfbVersion);
		JSONObject msgStr = JSONObject.fromObject(msgJson);
		Integer resultCode = Integer.parseInt(msgStr.getString("resultCode"));
		if (resultCode != 0) {
			map.put("code", resultCode);
			map.put("message", msgStr.getString("resultMessage"));
			return map;
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", msgStr.getString("id"));
		data.put("name", msgStr.getString("name"));
		data.put("cardNumber", msgStr.getString("cardNumber"));
		data.put("province", msgStr.getString("province"));
		data.put("city", msgStr.getString("city"));
		data.put("county", msgStr.getString("county"));
		data.put("address", msgStr.getString("address"));
		data.put("verifyState", msgStr.getString("verifyState"));

		map.put("code", 200);
		map.put("message", "ok!");
		map.put("data", data);
		return map;

	}

	/**
	 * 提交用户授信信息
	 * 
	 * @return
	 */
	public Map<String, Object> SubmitMemberInfo(HttpSession session,
			MemberInfo memberInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}
		User sessionUser = (User) session.getAttribute(Constants.USER);

		if (sessionUser == null || sessionUser.getThirdPartyId() == null
				|| sessionUser.getThirdPartyId() == "") {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		// 提交用户的授信信息
		String msgJson = HttpClientUtil.httpCreditMemerInfo(xfbInterface,
				sessionUser, memberInfo, xfbVersion);

		JSONObject msgStr = JSONObject.fromObject(msgJson);
		Integer resultCode = Integer.parseInt(msgStr.getString("resultCode"));

		if (resultCode != 0) {
			map.put("code", resultCode);
			map.put("message", msgStr.getString("resultMessage"));
			return map;
		}
		
		/*
		 * 调用信分宝成功后更新用户的姓名和身份证号
		 * aurhor：Black
		 * date:2016-7-21
		 */
//		User user = new User();
		sessionUser.setRealName(memberInfo.getName());
		sessionUser.setIdCard(memberInfo.getCardNumber());
		userDao.updateUserRealNameAndIdcard(sessionUser);
		
		// session 添加到 redis 中
		session.setAttribute(Constants.USER, sessionUser);
		
		map.put("code", 200);
		map.put("message", "ok!");
		map.put("data", "");
		return map;
	}

	/**
	 * 获取用户授信工作信息
	 * 
	 * @return
	 */
	public Map<String, Object> JobInfo(HttpSession session, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		User sessionUser = (User) session.getAttribute(Constants.USER);

		if (sessionUser == null || sessionUser.getThirdPartyId() == null
				|| sessionUser.getThirdPartyId() == "") {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		// 获取用户的工作信息
		String msgJson = HttpClientUtil.httpCreditGetJobInfo(xfbInterface,
				sessionUser, id, xfbVersion);

		JSONObject msgStr = JSONObject.fromObject(msgJson);
		Integer resultCode = Integer.parseInt(msgStr.getString("resultCode"));
		if (resultCode != 0) {
			map.put("code", resultCode);
			map.put("message", msgStr.getString("resultMessage"));
			return map;
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", msgStr.getString("id"));
		data.put("companyName", msgStr.getString("companyName"));
		data.put("companyNature", msgStr.getString("companyNature"));
		data.put("department", msgStr.getString("department"));
		data.put("post", msgStr.getString("post"));
		data.put("telNumber", msgStr.getString("telNumber"));
		data.put("province", msgStr.getString("province"));
		data.put("city", msgStr.getString("city"));
		data.put("county", msgStr.getString("county"));
		data.put("companyAddress", msgStr.getString("companyAddress"));
		data.put("annualIncome", msgStr.getString("annualIncome"));
		data.put("verifyState", msgStr.getString("verifyState"));

		map.put("code", 200);
		map.put("message", "ok!");
		map.put("data", data);
		return map;

	}

	/**
	 * 提交用户工作信息
	 * 
	 * @return
	 */
	public Map<String, Object> SubmitJobInfo(HttpSession session,
			JobInfo jobInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}
		User sessionUser = (User) session.getAttribute(Constants.USER);

		if (sessionUser == null || sessionUser.getThirdPartyId() == null
				|| sessionUser.getThirdPartyId() == "") {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		// 获取用户的授信信息
		String msgJson = HttpClientUtil.httpCreditJobInfo(xfbInterface,
				sessionUser, jobInfo, xfbVersion);

		JSONObject msgStr = JSONObject.fromObject(msgJson);
		Integer resultCode = Integer.parseInt(msgStr.getString("resultCode"));

		if (resultCode != 0) {
			map.put("code", resultCode);
			map.put("message", msgStr.getString("resultMessage"));
			return map;
		}

		map.put("code", 200);
		map.put("message", "ok!");
		map.put("data", "");
		return map;
	}

	/**
	 * 获取用户授信联系人信息
	 * 
	 * @return
	 */
	public Map<String, Object> ContacInfo(HttpSession session, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		User sessionUser = (User) session.getAttribute(Constants.USER);

		if (sessionUser == null || sessionUser.getThirdPartyId() == null
				|| sessionUser.getThirdPartyId() == "") {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		// 获取用户的工作信息
		String msgJson = HttpClientUtil.httpCreditGetContacInfo(xfbInterface,
				sessionUser, id, xfbVersion);

		JSONObject msgStr = JSONObject.fromObject(msgJson);
		Integer resultCode = Integer.parseInt(msgStr.getString("resultCode"));
		if (resultCode != 0) {
			map.put("code", resultCode);
			map.put("message", msgStr.getString("resultMessage"));
			return map;
		} // 联系人数组
			// JSONArray jsonArray = new JSONArray();
		map.put("code", 200);
		map.put("message", "ok!");
		map.put("data", msgStr.getString("contactInfo"));
		return map;

	}

	/**
	 * 提交用户联系人信息
	 * 
	 * @return
	 */
	public Map<String, Object> SubmitContacInfo(HttpSession session,
			Contac[] contacList) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}
		User sessionUser = (User) session.getAttribute(Constants.USER);

		if (sessionUser == null || sessionUser.getThirdPartyId() == null
				|| sessionUser.getThirdPartyId() == "") {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		String msgJson = HttpClientUtil.httpCreditContacInfo(xfbInterface,
				sessionUser, contacList, xfbVersion);
		JSONObject msgStr = JSONObject.fromObject(msgJson);
		Integer resultCode = Integer.parseInt(msgStr.getString("resultCode"));

		if (resultCode != 0) {
			map.put("code", resultCode);
			map.put("message", msgStr.getString("resultMessage"));
			return map;
		}

		map.put("code", 200);
		map.put("message", "ok!");
		map.put("data", "");
		return map;
	}

	/**
	 * 提交用户图片信息
	 * 
	 * @param type
	 *            A1:身份证（正面） A2:职业证 A3:学生证（封面） A4:储蓄卡 A5:信用卡 A6:驾照（封面） A7:结婚证
	 *            B1:身份证（反面） C1:身份证（手持） B3:学生证信息页 C3:学生证注册页
	 * @param imgUrl
	 *            微信服务器图片地址
	 * @return
	 */
	public Map<String, Object> SubmitImg(HttpSession session, String type,
			String imgUrl) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session == null) {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}
		User sessionUser = (User) session.getAttribute(Constants.USER);
		if (sessionUser == null || sessionUser.getThirdPartyId() == null
				|| sessionUser.getThirdPartyId() == "") {
			map.put("code", 401);
			map.put("message", "未登录");
			map.put("data", "");
			return map;
		}

		// 微信的全局access_token
		String accessToken = WxAccessToken.getAccessToken();
		if (accessToken == null || accessToken == "" || accessToken == "error") {
			map.put("code", 403);
			map.put("message", "获取微信accessToken出错！");
			return map;
		}

		// 从微信下载素材文件
		String wxAccessTokenUrl = "http://api.weixin.qq.com/cgi-bin/media/get?access_token="
				+ accessToken + "&media_id=" + imgUrl;

		// 获取文件流数组
		byte[] inputStreamArry = getImageFromNetByUrl(wxAccessTokenUrl, "GET");

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
		String fileUrl = wxDownPicUrl + sessionUser.getMobile() + type + date
				+ ".jpg";
		// 保存到本地
		if (!HttpClientUtil.writeImageToDisk(inputStreamArry, fileUrl)) {
			map.put("code", "4002");
			map.put("message", "微信保存本地图片出错," + "微信地址:" + wxAccessTokenUrl);
			return map;
		}
		
		// 修改成功之后需要重新登录才可以
		Map<String, Object> result = XfbLogin(sessionUser);
		Integer code = (Integer) result.get("code");
		if (code != 0) {
			map.put("code", code);
			map.put("message", "登录" + result.get("message"));
			map.put("data", "");
			return map;
		}
		
		String msgJson = HttpClientUtil.httpSaveImageInfo(xfbInterface,
				sessionUser, type, fileUrl, xfbVersion);

		JSONObject msgStr = JSONObject.fromObject(msgJson);
		Integer resultCode = Integer.parseInt(msgStr.getString("resultCode"));
		if (resultCode != 0) {
			map.put("code", resultCode);
			map.put("message", "保存图片出错：" + msgStr.getString("resultMessage"));
			return map;
		}

		map.put("code", 200);
		map.put("message", "ok!");
		map.put("data", "");
		return map;
	}

	/**
	 * 登录信分宝，解决6777 在另一台登录问题
	 * 
	 * @param user
	 * @return
	 */
	public Map<String, Object> XfbLogin(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		String statusJson = HttpClientUtil.httpLogin(xfbInterface,
				user.getThirdPartyId(), user.getMobile(), user.getPassword(),
				xfbVersion);
		JSONObject statusStr = JSONObject.fromObject(statusJson);
		Integer resultCode = Integer
				.parseInt(statusStr.getString("resultCode"));
		map.put("code", resultCode);
		map.put("message", statusStr.getString("resultMessage"));
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
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

}