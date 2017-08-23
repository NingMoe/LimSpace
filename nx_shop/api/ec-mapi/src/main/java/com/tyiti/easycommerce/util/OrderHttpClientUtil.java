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

public class OrderHttpClientUtil {

	private static Log log = LogFactory.getLog(OrderHttpClientUtil.class);
	 
	 
	
	 /**
	 * @param no 
	 * @Title: getHeaders
	 * @Description: 添加头部信息
	 * @return Map<String,String>    返回类型
	 * @author Yan Zuoyu
	 * @throws
	  */
	public static Map<String, String> getHeaders(String no ,String secretKey) {
		Md5 md5 = new Md5();
		// 加密
		Map<String, String> headerParams = new HashMap<String, String>();
 		headerParams.put("authentication", md5.getMD5ofStr(no +secretKey));
		headerParams.put("sysType", "IOS");
		headerParams.put("uuid", "A6872607-3614-4361-8B10-319D66114309");
		headerParams.put("OSversion", "8.4.1");
		headerParams.put("phoneModel", "iPhone 6 (A1549/A1586)");
		headerParams.put("carrierName", "CU");
		return headerParams;
	}

	 /**
	 * @param no 订单号  +  秘钥  MD5
	 * @Title: addDataToOrderSys
	 * @Description: 向订单系统中加入数据
	 * @return String    返回类型
	 * @author Yan Zuoyu
	 * @throws
	  */
	public static String addDataToOrderSys(String httpUrl , JSONObject requestParams, String no,String secretKey) {
		String str = null;
		Map<String, String> headerParams = getHeaders(no ,secretKey);
		str = OrderHttpClientUtil.httpPostBody(httpUrl,
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
	
}
