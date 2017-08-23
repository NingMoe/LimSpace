package com.tyiti.easycommerce.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

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
import com.tyiti.easycommerce.entity.User;

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
		str = HttpClientUtil.httpPost(httpUrl + "customer/registration.do",
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
	 * @Dception 账单详情
	 * @param httpUrl
	 * @param userId
	 * @param phone
	 * @param password
	 * @param sysVersion
	 * @param orderId
	 * @return
	 */
	public static String httpBillDetail(String httpUrl, String userId,
			String phone, String password, String sysVersion, String billId) {
		String str = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("billId", billId);

		Map<String, String> headerParams = getHeaders(userId, phone, password,
				sysVersion);
		
		str = HttpClientUtil.httpPost(httpUrl + "/bill/billDetail.do",
				headerParams, requestParams, HTTP.UTF_8);
		return str;
	}
		
	 
		/**
		 * @Dception 账单详情
		 * @param httpUrl
		 * @param userId
		 * @param phone
		 * @param password
		 * @param sysVersion
		 * @param orderId
		 * @return
		 */
		public static String httpOrderAndBillDetail(String httpUrl, String userId,
				String phone, String password, String sysVersion, String orderId) {
			String str = null;
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("orderId", orderId);

			Map<String, String> headerParams = getHeaders(userId, phone, password,
					sysVersion);
			
			str = HttpClientUtil.httpPost(httpUrl + "/order/orderAndBillDetail.do",
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
	 * 发送订单分期成功短信
	 * @param httpUrl
	 * @param userId
	 * @param phone
	 * @param password
	 * @param sysVersion
	 * @param orderId
	 * @return
	 */
	public static String httpSendOrderConfirmSms(String httpUrl, String userId,
			String phone, String password, String sysVersion, String orderId) {
		String str = null;

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("id", orderId);

		Map<String, String> headerParams = getHeaders(userId, phone, password,sysVersion);

		str = HttpClientUtil.httpPost(httpUrl + "order/sendOrderConfirmSms.do",
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
}
