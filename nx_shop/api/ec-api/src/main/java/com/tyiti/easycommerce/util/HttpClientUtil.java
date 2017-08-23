package com.tyiti.easycommerce.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.itextpdf.xmp.impl.Base64;
import com.tyiti.easycommerce.common.SysConfig;
import com.tyiti.easycommerce.entity.Contac;
import com.tyiti.easycommerce.entity.JobInfo;
import com.tyiti.easycommerce.entity.MemberInfo;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HttpClientUtil {

	private static Log log = LogFactory.getLog(HttpClientUtil.class);

	/**
	 * 获得日志服务器连接头信息
	 * 
	 * @param code
	 * @return
	 */
	public static Map<String, String> getLogHeaders(String code) {
		Md5 md5 = new Md5();
		String secret = "842A381C91CE43A98720825601C22A56";

		Map<String, String> headerParams = new HashMap<String, String>();
		String mm = md5.getMD5ofStr(code + secret).toUpperCase();
		mm = Base64.encode("\"" + mm + "\"");
		headerParams.put("Authorization", mm);
		headerParams
				.put("Accept",
						"application/json; version=1000; client=easycommerceManagement");
		headerParams.put("ContentType", "application/json");
		return headerParams;
	}

	/**
	 * 往日志服务器添加日志
	 * 
	 * @param httpUrl
	 * @param code
	 * @param message
	 * @param userid
	 * @param key
	 * @return
	 */
	public static String httpLogServiceInsert(String httpUrl, String code,
			String message, String userid, String key) {
		String str = null;

		Map<String, String> headerParams = getLogHeaders(code);

		JSONObject requestParams = new JSONObject();
		requestParams.put("code", code);
		requestParams.put("message", "\"" + message + "\"");
		requestParams.put("userid", userid);
		requestParams.put("key", key);

		str = httpPostBody(httpUrl + "/logs", headerParams, requestParams,
				HTTP.UTF_8);

		return str;
	}

	/**
	 * 获得信分宝接口中添加的头信息
	 * 
	 * @param userId
	 * @param phone
	 * @param password
	 * @param sysVersion
	 * @return
	 */
	public static Map<String, String> getHeaders(String userId, String phone,
			String password, String sysVersion) {
		Md5 md5 = new Md5();
		String tick = String.valueOf(System.currentTimeMillis() / 1000L);
		// 加密
		String passWord = md5.getMD5ofStr(phone + password);

		Map<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("userId", userId);
		headerParams.put("password", passWord);
		headerParams.put("authentication", "timestamp=" + tick + ";secretKey="
				+ md5.getMD5ofStr(tick + "passw0rd!" + userId).toUpperCase());
		headerParams.put("sysType", "IOS");
		headerParams.put("sysVersion", sysVersion);
		headerParams.put("uuid", "A6872607-3614-4361-8B10-319D66114309");
		headerParams.put("OSversion", "8.4.1");
		headerParams.put("phoneModel", "iPhone 6 (A1549/A1586)");
		headerParams.put("carrierName", "CU");
		return headerParams;
	}

	/**
	 * 登录信分宝
	 * 
	 * @param httpUrl
	 * @param userId
	 * @param phone
	 * @param password
	 * @param sysVersion
	 * @return
	 */
	public static String httpLogin(String httpUrl, String userId, String phone,
			String password, String sysVersion) {
		String str = null;

		Md5 md5 = new Md5();
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("loginName", phone);
		requestParams.put("passWord", md5.getMD5ofStr(phone + password));

		Map<String, String> headerParams = getHeaders(userId, phone, password,
				sysVersion);

		str = HttpClientUtil.httpPost(httpUrl + "customer/sslogin.do",
				headerParams, requestParams, HTTP.UTF_8);

		return str;
	}

	/**
	 * 登录信分宝
	 * 
	 * @param httpUrl
	 * @param userId
	 * @param phone
	 * @param Signature
	 *            =MD5(LoginName+md5(password))
	 * @param sysVersion
	 * @return
	 */
	public static String httpSignatureLogin(String httpUrl, String userId,
			String phone, String signature, String sysVersion) {
		String str = null;

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("loginName", phone);
		requestParams.put("passWord", signature);

		Map<String, String> headerParams = getHeaders(userId, phone, signature,
				sysVersion);

		str = HttpClientUtil.httpPost(httpUrl + "customer/sslogin.do",
				headerParams, requestParams, HTTP.UTF_8);

		return str;
	}

	/**
	 * 注册信分宝
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            注册用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpReg(String httpUrl, User user, String sysVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("loginName", user.getMobile());
		// 和天尧分期生成存在的密码一样
		requestParams.put("passWord", user.getPassword());
		requestParams.put("cardId", user.getIdCard());
		requestParams.put("recomCode", user.getRecByCode());
		requestParams.put("realName", user.getRealName());
		Map<String, String> headerParams = getHeaders("", user.getMobile(),
				user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(httpUrl + "customer/tyfqregistration.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 修改信分宝用戶密碼
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            注册用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpUpdatePassword(String httpUrl, User user,
			String sysVersion) {
		String str = null;
		Md5 md5 = new Md5();
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("loginName", user.getMobile());
		requestParams.put("newPassWord", user.getNewPassword());
		// 用户名+密码的MD5
		String passWord = md5
				.getMD5ofStr(user.getMobile() + user.getPassword());
		requestParams.put("oldPassword", passWord);

		Map<String, String> headerParams = getHeaders(user.getThirdPartyTd(),
				user.getMobile(), user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(httpUrl + "customer/changeInfo.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 订单取消或退货
	 * 
	 * @param httpUrl
	 * @param userId
	 * @param phone
	 * @param password
	 * @param sysVersion
	 * @param orderId
	 * @param cause
	 * @param flag
	 * @return
	 */
	public static String httpCancelOrReturnOrder(String httpUrl, String userId,
			String phone, String password, String sysVersion, String orderId,
			String cause, String flag) {
		String str = null;

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("orderId", orderId);
		requestParams.put("cause", cause);
		requestParams.put("flag", flag);

		Map<String, String> headerParams = getHeaders(userId, phone, password,
				sysVersion);

		str = HttpClientUtil.httpPost(httpUrl + "order/cancelOrReturn.do",
				headerParams, requestParams, HTTP.UTF_8);

		return str;
	}

	/**
	 * 获取授信的状态
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpCreditStatus(String httpUrl, User user,
			String sysVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();

		Map<String, String> headerParams = getHeaders(user.getThirdPartyId(),
				user.getMobile(), user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(httpUrl + "amount/getStateInfo.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 获取授信的个人信息
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpCreditGetMemerInfo(String httpUrl, User user,
			String id, String sysVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("id", id);

		Map<String, String> headerParams = getHeaders(user.getThirdPartyId(),
				user.getMobile(), user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(httpUrl + "memberInfo/getMemberInfo.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 提交授信的个人信息
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpCreditMemerInfo(String httpUrl, User user,
			MemberInfo memberInfo, String sysVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("id", memberInfo.getId());
		requestParams.put("name", memberInfo.getName());
		requestParams.put("cardNumber", memberInfo.getCardNumber());
		requestParams.put("province", memberInfo.getProvince());
		requestParams.put("city", memberInfo.getCity());
		requestParams.put("county", memberInfo.getCounty());
		requestParams.put("address", memberInfo.getAddress());
		requestParams.put("verifyState", memberInfo.getVerifyState());
		requestParams.put("geolocation", memberInfo.getGeolocation());

		Map<String, String> headerParams = getHeaders(user.getThirdPartyId(),
				user.getMobile(), user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(httpUrl + "memberInfo/saveMemberInfo.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 获取授信的工作信息
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpCreditGetJobInfo(String httpUrl, User user,
			String id, String sysVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("id", id);

		Map<String, String> headerParams = getHeaders(user.getThirdPartyId(),
				user.getMobile(), user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(httpUrl + "professionInfo/getJobInfo.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 提交授信的工作信息
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpCreditJobInfo(String httpUrl, User user,
			JobInfo jobInfo, String sysVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("id", jobInfo.getId());
		requestParams.put("companyName", jobInfo.getCompanyName());
		requestParams.put("companyNature", jobInfo.getCompanyNature());
		requestParams.put("department", jobInfo.getDepartment());
		requestParams.put("post", jobInfo.getPost());
		requestParams.put("telNumber", jobInfo.getTelNumber());
		requestParams.put("province", jobInfo.getProvince());
		requestParams.put("city", jobInfo.getCity());
		requestParams.put("county", jobInfo.getCounty());
		requestParams.put("companyAddress", jobInfo.getCompanyAddress());
		requestParams.put("annualIncome", jobInfo.getAnnualIncome());
		requestParams.put("verifyState", jobInfo.getVerifyState());

		Map<String, String> headerParams = getHeaders(user.getThirdPartyId(),
				user.getMobile(), user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(
				httpUrl + "professionInfo/saveJobInfo.do", headerParams,
				requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 获取授信的联系人信息
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpCreditGetContacInfo(String httpUrl, User user,
			String id, String sysVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("id", id);

		Map<String, String> headerParams = getHeaders(user.getThirdPartyId(),
				user.getMobile(), user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(
				httpUrl + "contactInfo/getContactInfo.do", headerParams,
				requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 提交授信的联系人信息
	 *
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            用户信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpCreditContacInfo(String httpUrl, User user,
			Contac[] contac, String sysVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		// requestParams.put("contactInfo",
		// JSONObject.fromObject(contac).toString());
		requestParams.put("contactInfo", JSONArray.fromObject(contac)
				.toString());

		Map<String, String> headerParams = getHeaders(user.getThirdPartyId(),
				user.getMobile(), user.getPassword(), sysVersion);
		str = HttpClientUtil.httpPost(httpUrl
				+ "contactInfo/saveContactInfo.do", headerParams,
				requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 保存授信的图片
	 * 
	 * @param httpUrl
	 *            信分宝接口地址
	 * 
	 * @param user
	 *            用户信息
	 * 
	 * @param type
	 *            A1:身份证（正面） A2:职业证 A3:学生证（封面） A4:储蓄卡 A5:信用卡 A6:驾照（封面） A7:结婚证
	 *            B1:身份证（反面） C1:身份证（手持） B3:学生证信息页 C3:学生证注册页
	 * @param imageFile
	 * 
	 * @param sysVersion
	 *            接口版本号
	 * 
	 * @return
	 */
	public static String httpSaveImageInfo(String httpUrl, User user,
			String type, String fileUrl, String sysVersion) {
		String str = null;

		Md5 md5 = new Md5();
		String tick = String.valueOf(System.currentTimeMillis() / 1000L);
		Map<String, String> headerParams = new HashMap<String, String>();
		// 此处的头信息和其他的加密方式不同
		headerParams.put("userId", user.getThirdPartyId());
		headerParams.put("password", user.getPassword());
		headerParams.put("authentication", "timestamp="
				+ tick
				+ ";secretKey="
				+ md5.getMD5ofStr(tick + "passw0rd!" + user.getThirdPartyId())
						.toUpperCase());
		headerParams.put("sysType", "IOS");
		headerParams.put("sysVersion", sysVersion);
		headerParams.put("uuid", "A6872607-3614-4361-8B10-319D66114309");
		headerParams.put("OSversion", "8.4.1");
		headerParams.put("phoneModel", "iPhone 6 (A1549/A1586)");
		headerParams.put("carrierName", "CU");

		String Url = httpUrl + "imageInfo/saveImageInfo.do";
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("type", type);

		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("imageFile", fileUrl);

		str = formUpload(Url, headerParams, textMap, fileMap, ".jpg");
		return str;
	}

	/**
	 * 上传图片
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @param contentType
	 *            没有传入文件类型默认采用application/octet-stream
	 *            contentType非空采用filename匹配默认的图片类型
	 * @return 返回response数据
	 */
	@SuppressWarnings("rawtypes")
	public static String formUpload(String urlStr,
			Map<String, String> headerParams, Map<String, String> textMap,
			Map<String, String> fileMap, String contentType) {
		String res = "";
		HttpURLConnection conn = null;
		// boundary就是request头和上传文件内容的分隔符
		String BOUNDARY = "---------------------------123821742118716";
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			// 添加头信息
			if (headerParams != null) {
				for (Map.Entry<String, String> entry : headerParams.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					// 没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
					contentType = new MimetypesFileTypeMap()
							.getContentType(file);
					// contentType非空采用filename匹配默认的图片类型
					if (!"".equals(contentType)) {
						if (filename.endsWith(".png")) {
							contentType = "image/png";
						} else if (filename.endsWith(".jpg")
								|| filename.endsWith(".jpeg")
								|| filename.endsWith(".jpe")) {
							contentType = "image/jpeg";
						} else if (filename.endsWith(".gif")) {
							contentType = "image/gif";
						} else if (filename.endsWith(".ico")) {
							contentType = "image/image/x-icon";
						}
					}
					if (contentType == null || "".equals(contentType)) {
						contentType = "application/octet-stream";
					}
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			res = "{'resultCode':40001,'resultMessage':'提交信分宝发送POST请求出错。'}";
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/**
	 * 将图片写入到磁盘
	 * 
	 * @param img
	 *            图片数据流
	 * @param fileName
	 *            文件保存时：路径+名称
	 */
	public static boolean writeImageToDisk(byte[] img, String fileUrl) {
		try {
			File file = new File(fileUrl);
			FileOutputStream fops = new FileOutputStream(file);
			fops.write(img);
			fops.flush();
			fops.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("将图片写入到磁盘(异常捕获):" + e);
			return false;
		}
	}

	/**
	 * 订单详情
	 * 
	 * @param httpUrl
	 * @param userId
	 * @param phone
	 * @param password
	 * @param sysVersion
	 * @param orderId
	 * @return
	 */
	public static String httpOrderDetail(String httpUrl, String userId,
			String phone, String password, String sysVersion, String orderId) {
		String str = null;

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("orderId", orderId);

		Map<String, String> headerParams = getHeaders(userId, phone, password,
				sysVersion);

		str = HttpClientUtil.httpPost(httpUrl + "/order/orderDetail.do",
				headerParams, requestParams, HTTP.UTF_8);

		return str;
	}

	/**
	 * 创建订单+分期
	 * 
	 * @param httpUrl
	 *            信分宝接口地址
	 * @param user
	 *            用户信息
	 * @param order
	 *            订单信息
	 * @param sku
	 *            订单商品信息
	 * @param sysVersion
	 *            接口版本号
	 * @return
	 */
	public static String httpCreateOrderAndStage(String httpUrl, User user,
			String xfbUserId, Order order, OrderSku orderSku, String xfbVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		// 订单号
		requestParams.put("orderName", order.getNo());
		// 商品名称
		requestParams.put("productName", orderSku.getSkuName());
		// 商品价格
		requestParams.put(
				"productPrice",
				String.valueOf(orderSku.getSkuPrice().multiply(
						new BigDecimal(orderSku.getSkuCount()))));
		// 首付金额
		requestParams.put("firstPay", String.valueOf(order.getDownPayment()));
		// 分期方案 >>分期数 installment_months
		requestParams.put("stagePlan",
				String.valueOf(order.getInstallmentMonths()));
		// 分期费率
		requestParams.put("stageRate", String.valueOf(SysConfig.rateConfigMap
				.get(String.valueOf(order.getInstallmentMonths()))));

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);
		str = HttpClientUtil.httpPost(httpUrl + "order/createOrderAndStage.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 创建订单，只扣额度，不生成账单
	 * 
	 * @author Jeffrey
	 * @since 2016年8月4日 下午5:13:23
	 * @param httpUrl
	 * @param user
	 * @param xfbUserId
	 * @param order
	 * @param orderSku
	 * @param xfbVersion
	 * @return
	 */
	public static String httpCreateOrder(String httpUrl, User user,
			String xfbUserId, Order order, OrderSku orderSku, String xfbVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		// 订单号
		requestParams.put("orderName", order.getNo());
		// 商品名称
		requestParams.put("productName", orderSku.getSkuName());
		// 商品价格
		requestParams.put(
				"productPrice",
				String.valueOf(orderSku.getSkuPrice().multiply(
						new BigDecimal(orderSku.getSkuCount()))));
		// 首付金额
		requestParams.put("firstPay", String.valueOf(order.getDownPayment()));
		// 分期方案 >>分期数 installment_months
		requestParams.put("stagePlan",
				String.valueOf(order.getInstallmentMonths()));
		// 分期费率
		requestParams.put("stageRate", String.valueOf(SysConfig.rateConfigMap
				.get(String.valueOf(order.getInstallmentMonths()))));

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);
		str = HttpClientUtil.httpPost(httpUrl + "order/createOrder.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 订单分期接口，创建订单
	 * 
	 * @author Jeffrey
	 * @since 2016年8月4日 下午5:31:22
	 * @param stageId
	 *            信分宝订单id
	 * @param stageRate
	 *            分期费率
	 * @param httpUrl
	 * @param user
	 * @param xfbUserId
	 * @param order
	 *            商城订单
	 * @param xfbVersion
	 * @return
	 */
	public static String httpOrderStage(String stageId, Integer stageMonth,
			String httpUrl, User user, String xfbUserId, String xfbVersion) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		// 信分宝订单id
		requestParams.put("id", stageId);
		// 分期费率
		requestParams.put("stageRate", String.valueOf(SysConfig.rateConfigMap
				.get(String.valueOf(stageMonth))));

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);
		str = HttpClientUtil.httpPost(httpUrl + "order/orderStage.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}

	/**
	 * 查询信用分
	 * 
	 * @param xfbInterface
	 * @param user
	 * @param string
	 * @param xfbVersion
	 * @return
	 */
	public static String httpGetUserScore(String xfbInterface, User user,
			String xfbUserId, String xfbVersion) {
		String str = null;

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("userID", xfbUserId);

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);

		str = HttpClientUtil.httpPost(xfbInterface + "customer/score.do",
				headerParams, requestParams, HTTP.UTF_8);

		return str;
	}

	/**
	 * 信用卡用户授信
	 * 
	 * @param xfbInterface
	 * @param user
	 * @param xfbUserId
	 * @param xfbVersion
	 * @return
	 */
	public static String httpGetcreditSubmit(String xfbInterface, User user,
			String xfbUserId, String xfbVersion) {
		String str = null;

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("userId", xfbUserId);
		requestParams.put("idCardNo", user.getIdCard());
		requestParams.put("name", user.getRealName());

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);

		str = HttpClientUtil.httpPost(xfbInterface
				+ "infoVerify/idCardVerify.do", headerParams, requestParams,
				HTTP.UTF_8);

		return str;
	}

	public static String httpGetCreditStep(String xfbInterface, User user,
			String xfbUserId, String xfbVersion) {
		String str = null;

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("userId", xfbUserId);
		requestParams
				.put("isCreditCardUser",
						(user.getThirdPartyType() != null && user
								.getThirdPartyType() == 2) ? "0" : "1");

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);

		str = HttpClientUtil.httpPost(xfbInterface
				+ "memberInfo/getCreditStep.do", headerParams, requestParams,
				HTTP.UTF_8);

		return str;
	}

	/**
	 * 个人中心-个人信息
	 * 
	 * @param xfbInterface
	 * @param user
	 * @param string
	 * @param xfbVersion
	 * @return
	 */
	public static String httpGetPrivateInfo(String xfbInterface, User user,
			String xfbUserId, String xfbVersion) {
		String str = null;

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("userID", xfbUserId);

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);

		str = HttpClientUtil.httpPost(xfbInterface
				+ "memberInfo/getPrivateInfo.do", headerParams, requestParams,
				HTTP.UTF_8);

		return str;
	}

	/**
	 * 账单列表
	 * 
	 * @param xfbInterface
	 * @param user
	 * @param string
	 * @param xfbVersion
	 * @param param
	 * @return
	 */
	public static String httpGetBillList(String xfbInterface, User user,
			String xfbUserId, String xfbVersion, Map<String, Object> param) {
		String str = null;
		/**
		 * ownerUserid String 用户id page String 页码 pageSize String 条数 flag String
		 * null:全部账单 A1:全部账单 A2:近7日待还账单
		 */
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("ownerUserid", xfbUserId);
		requestParams.put("page", String.valueOf(param.get("page")));
		requestParams.put("pageSize", String.valueOf(param.get("pageSize")));
		requestParams.put("flag", String.valueOf(param.get("flag")));

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);

		str = HttpClientUtil.httpPost(xfbInterface + "bill/integrationBill.do",
				headerParams, requestParams, HTTP.UTF_8);

		return str;
	}

	/**
	 * 账单详情
	 * 
	 * @param xfbInterface
	 * @param user
	 * @param string
	 * @param xfbVersion
	 * @param id
	 * @return
	 */
	public static String httpGetBillById(String xfbInterface, User user,
			String xfbUserId, String xfbVersion, Integer billId) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("billId", String.valueOf(billId));

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);

		str = HttpClientUtil.httpPost(xfbInterface + "bill/billDetail.do",
				headerParams, requestParams, HTTP.UTF_8);

		return str;
	}

	/**
	 * 账单还款
	 * 
	 * @param xfbInterface
	 * @param user
	 * @param string
	 * @param xfbVersion
	 * @param billArr
	 * @param grossAmount
	 * @return
	 */
	public static String httpRepayment(String xfbInterface, User user,
			String xfbUserId, String xfbVersion, JSONArray billArr,
			String grossAmount, String tradeNo) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("billArr", billArr.toString());
		requestParams.put("grossAmount", String.valueOf(new BigDecimal(
				grossAmount).divide(new BigDecimal(100))));
		requestParams.put("tradeNo", tradeNo);

		Map<String, String> headerParams = getHeaders(xfbUserId,
				user.getMobile(), user.getPassword(), xfbVersion);

		str = HttpClientUtil.httpPost(xfbInterface + "bill/repayment.do",
				headerParams, requestParams, HTTP.UTF_8);

		return str;
	}

	/**
	 * httpGet
	 * 
	 * @param url
	 * @param headerParams
	 * @param requestParams
	 * @return
	 */
	public static String httpGet(String url, Map<String, String> headerParams,
			Map<String, String> requestParams) {

		String str = null;
		HttpGet httpGet = null;
		HttpClient httpClient = new DefaultHttpClient();

		try {
			// 参数设置
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");
			for (Map.Entry<String, String> entry : requestParams.entrySet()) {
				builder.append((String) entry.getKey());
				builder.append("=");
				builder.append((String) entry.getValue());
				builder.append("&");
			}

			String tmpUrl = builder.toString();
			tmpUrl = tmpUrl.substring(0, tmpUrl.length() - 1);

			httpGet = new HttpGet(tmpUrl);

			if (headerParams != null) {
				for (Map.Entry<String, String> entry : headerParams.entrySet()) {
					httpGet.setHeader(entry.getKey(), entry.getValue());
				}
			}

			HttpResponse response = httpClient.execute(httpGet);

			// reponse header
			log.info(response.getStatusLine().getStatusCode());

			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				log.info(header.getName() + ": " + header.getValue());
			}

			// 网页内容
			HttpEntity httpEntity = response.getEntity();
			str = EntityUtils.toString(httpEntity);
			log.info(str);
		} catch (ClientProtocolException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if (httpGet != null) {
				httpGet.abort();
			}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return str;
	}

	/**
	 * 发送 https 请求， Response 作为 StringBuffer 返回，不按 Content-Type 解析
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param requestBody
	 *            提交的数据
	 * @return StringBuffer Response Body
	 */
	public static StringBuffer httpsRequest(String requestUrl,
			String requestMethod, String requestBody) {
		StringBuffer buffer = new StringBuffer();

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new X509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);

			// 当outputStr不为null时向输出流写数据
			if (null != requestBody) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(requestBody.getBytes("UTF-8"));
				outputStream.close();
			}

			// 检查返回码
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				return null;
			}

			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
		} catch (ConnectException ce) {
			log.info("Out Of Time：{}", ce);
		} catch (Exception e) {
			log.info("https Error：{}", e);
		}

		return buffer;
	}

	/**
	 * httpPost
	 * 
	 * @param url
	 * @param headerParams
	 * @param requestParams
	 * @param urlEncode
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> headerParams,
			Map<String, String> requestParams, String urlEncode) {

		String str = null;
		HttpPost httpPost = null;
		HttpClient httpClient = new DefaultHttpClient();

		try {
			// 参数设置
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : requestParams.entrySet()) {
				params.add(new BasicNameValuePair((String) entry.getKey(),
						(String) entry.getValue()));
			}

			httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, urlEncode));

			if (headerParams != null) {
				for (Map.Entry<String, String> entry : headerParams.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}

			// reponse header
			HttpResponse response = httpClient.execute(httpPost);
			log.info(response.getStatusLine().getStatusCode());

			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				log.info(header.getName() + ": " + header.getValue());
			}

			// 网页内容
			HttpEntity httpEntity = response.getEntity();
			str = EntityUtils.toString(httpEntity);
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		} catch (ClientProtocolException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if (httpPost != null) {
				httpPost.abort();
			}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return str;
	}

	/**
	 * httpPostBody
	 * 
	 * @param url
	 * @param headerParams
	 * @param requestParams
	 * @param urlEncode
	 * @return
	 */
	public static String httpPostBody(String url,
			Map<String, String> headerParams, JSONObject requestParams,
			String urlEncode) {

		String str = null;
		HttpPost httpPost = null;
		HttpClient httpClient = new DefaultHttpClient();

		try {
			StringEntity entity = new StringEntity(requestParams.toString(),
					urlEncode);
			entity.setContentEncoding(urlEncode);
			entity.setContentType("application/json");

			log.info(requestParams.toString());

			httpPost = new HttpPost(url);
			httpPost.setEntity(entity);

			if (headerParams != null) {
				for (Map.Entry<String, String> entry : headerParams.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}

			// reponse header
			HttpResponse response = httpClient.execute(httpPost);
			log.info(response.getStatusLine().getStatusCode());

			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				log.info(header.getName() + ": " + header.getValue());
			}

			// 网页内容
			HttpEntity httpEntity = response.getEntity();
			str = EntityUtils.toString(httpEntity);
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		} catch (ClientProtocolException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if (httpPost != null) {
				httpPost.abort();
			}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return str;
	}
}
