package com.tyiti.easycommerce.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.tyiti.easycommerce.entity.Log;
import com.tyiti.easycommerce.repository.LogDao;
import com.tyiti.easycommerce.util.spring.SpringUtil;

import net.sf.json.JSONObject;

public class LogUtil {
	private static LogDao logDao;

	static {
		logDao = (LogDao) SpringUtil.getBean("logDao");
	}
	/**
	 * 日志信息模版方法
	 * @param methodName 方法名
	 * @param model 操作模块 如：订单
	 * @param description 描述 如：下单    订单详情  下单失败，用户可用额度不足
	 * @param userid 当前操作用户id，没有则传null
	 * @param reqParam 请求参数（传入重要参数即可） 如：info("订单","下单","1","id="+id+",name="+name); 无请求参数请传null
	 * @return 
	 */
	public static String info(String model,String description,String userid,String reqParam){
		StringBuffer sb = new StringBuffer("[操作模块：{");
		sb.append(model);
		sb.append("}    描述：{");
		sb.append(description);
		sb.append("}    用户信息：id={");
		sb.append(userid);
		sb.append("}    参数：{");
		sb.append(reqParam);
		sb.append("}]");
		return sb.toString();
	}
	/**
	 * 
	 * @param model
	 * @param description
	 * @param userid
	 * @param reqParam 只允许传入map 或者实体类
	 * @return
	 */
	public static String info(String model,String description,String userid,Object reqParam){
		StringBuffer sb = new StringBuffer("[操作模块：{");
		sb.append(model);
		sb.append("}    描述：{");
		sb.append(description);
		sb.append("}    用户信息：id={");
		sb.append(userid);
		sb.append("}    参数：{");
		if(reqParam!=null){
			try {
				JSONObject object = JSONObject.fromObject(reqParam);
				sb.append(object.toString());
			} catch (Exception e) {
				e.printStackTrace();
				sb.append(e.getMessage());
			}
		}
		sb.append("}]");
		return sb.toString();
	}
	/**
	 * Description:
	 * 
	 * @param operateModel
	 *            操作模块
	 * @param operateType
	 *            操作类型
	 * @param operateResult
	 *            操作结果
	 * @param resInfo
	 *            异常信息
	 * 
	 * @return int
	 * @throws
	 * @Author Create Date: 2015-3-24 下午10:45:55
	 */
	public static int log(int operatorId, String operatorName, int keyId,
			int operateModel, int action, String message, int source,int successStatus) {
		// 用户的id  ，用户名 ，操作表的主键 ，操作model(表)，操作类型，信息，商城或管理端，成功失败
		int num = 0;
		try {
			HttpServletRequest request = (HttpServletRequest) RequestContextHolder
					.currentRequestAttributes().resolveReference(
							RequestAttributes.REFERENCE_REQUEST);
			String ip = getIpAddress(request);
			Log log = new Log(operatorId, operatorName, keyId,
					new Date(), ip, operateModel, action, message, source,successStatus);
			num = logDao.insertSelective(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	/**
	* @Title: logs
	* @Description: TODO(根据keyId 可以为orderId operateModel  订单或者退货 退款等)
	* @return List<Map<String,Object>>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	public static List<Map<String,Object>> logs(Object keyId ,int operateModel){
		Map<String,Object> param  = new HashMap<String, Object>();
		param.put("keyId", Integer.parseInt(String.valueOf(keyId)));
		param.put("operateModel", operateModel);
		List<Map<String,Object>> logsList= new ArrayList<Map<String,Object>>();
		
		logsList = logDao.selectListByOperateModel(param);
		
		return logsList ;
	}
	
	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress = null;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();

			// 这里主要是获取本机的ip,可有可无
			if (ipAddress.equals("127.0.0.1")
					|| ipAddress.endsWith("0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		// 或者这样也行,对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		// return
		// ipAddress!=null&&!"".equals(ipAddress)?ipAddress.split(",")[0]:null;
		return ipAddress;
	}
}
