package com.tyiti.easycommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.util.HttpClientUtil;

@Service
public class BillService {
	private Log logger = LogFactory.getLog(this.getClass());
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
	private UserDao userDao;
	/**
	 * 获取账单列表
	 * @param param
	 * @return
	 */
	public Map<String,Object> getBillList(Map<String, Object> param,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		if(connectedToXfb){
			//先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, "", user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					String response = HttpClientUtil.httpGetBillList(xfbInterface, user,obj.getString("userId"), xfbVersion,param);
					JSONObject result = JSONObject.fromObject(response);
					int resultCode = result.getInt("resultCode");
					if (resultCode != 0) {
						data.put("resultCode", resultCode);
						data.put("message", result.getString("resultMessage"));
						map.put("code", 400);
						map.put("messsge", "请求参数有误");
						map.put("data", data);
						return map;
					}else{
						//解析结果集
						data.put("bills", result.getJSONObject("integrationBill"));
						System.out.println("userid:"+obj.getString("userId")+"=======账单查询结果："+result.getJSONObject("integrationBill").toString());
					}
				}else{
					data.put("resultCode", obj.getString("resultCode"));
					data.put("message", obj.getString("resultMessage"));
					map.put("code", 400);
					map.put("messsge", "请求参数有误");
					map.put("data", data);
					return map;
				}
			}
			map.put("code", 200);
			map.put("messsge", "OK");
			map.put("data", data);
		}
		return map;
	}
	/**
	 * 账单详情
	 * @param id
	 * @param session
	 */
	public Map<String,Object> getBillById(Integer id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		if(connectedToXfb){
			//先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, "", user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					String response = HttpClientUtil.httpGetBillById(xfbInterface, user,obj.getString("userId"), xfbVersion,id);
					JSONObject result = JSONObject.fromObject(response);	
					int resultCode = result.getInt("resultCode");
					if (resultCode != 0) {
						data.put("resultCode", resultCode);
						data.put("message", result.getString("resultMessage"));
						map.put("code", 400);
						map.put("messsge", "请求参数有误");
						map.put("data", data);
						return map;
					}else{
						//解析结果集
						data.put("bills", result.getJSONObject("data"));
					}
				}else{
					data.put("resultCode", obj.getString("resultCode"));
					data.put("message", obj.getString("resultMessage"));
					map.put("code", 400);
					map.put("messsge", "请求参数有误");
					map.put("data", data);
					return map;
				}
			}
			map.put("code", 200);
			map.put("messsge", "OK");
			map.put("data", data);
		}
		return map;
	}
	/**
	 * 账单还款
	 * @param param
	 * @param session
	 * @return
	 */
	public Map<String, Object> repayment(Map<String, Object> param,
			HttpSession session,String tradeNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		if(connectedToXfb){
			//先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, "", user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					//还款总金额
					String grossAmount = String.valueOf(param.get("grossAmount"));
					String billArrs = String.valueOf(param.get("billArr"));
					String[] ids = null;
					if(billArrs!=null&&!"".equals(billArrs)){
						ids = billArrs.split(",");
					}else{
						map.put("code", 400);
						map.put("messsge", "请求参数有误");
						return map;
					}
					List<Map<String,String>> list = new ArrayList<Map<String,String>>();
					for(String id : ids){
						Map<String,String> m = new HashMap<String, String>();
						m.put("billId", id);
						list.add(m);
					}
					//封装JsonArray
					JSONArray billArr = JSONArray.fromObject(list);
					
					String response = HttpClientUtil.httpRepayment(xfbInterface, user,obj.getString("userId"), xfbVersion,billArr,grossAmount,tradeNo);
					JSONObject result = JSONObject.fromObject(response);
					int resultCode = result.getInt("resultCode");
					if (resultCode != 0) {
						data.put("resultCode", resultCode);
						data.put("message", result.getString("resultMessage"));
						map.put("code", 400);
						map.put("messsge", "请求参数有误");
						map.put("data", data);
						return map;
					}else{
						data.put("resultCode", resultCode);
						data.put("message", result.getString("resultMessage"));
					}
				}else{
					data.put("resultCode", obj.getString("resultCode"));
					data.put("message", obj.getString("resultMessage"));
					map.put("code", 400);
					map.put("messsge", "请求参数有误");
					map.put("data", data);
					return map;
				}
			}
			map.put("code", 200);
			map.put("messsge", "OK");
			map.put("data", data);
		}
		return map;
	}
	/**
	 * 账单还款，微信调用
	 */
	public boolean repayment(String billArrs,String userId,int grossAmount,String tradeNo){
		//获取用户信息
		User user = userDao.getById(Integer.parseInt(userId));
		if(user==null) return false;
		logger.info("=================进入账单修改，用户："+user.getMobile());
		if(connectedToXfb){
			//先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, "", user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					String[] ids = null;
					if(billArrs!=null&&!"".equals(billArrs)){
						ids = billArrs.split(",");
					}else{
						return false;
					}
					List<Map<String,String>> list = new ArrayList<Map<String,String>>();
					for(String id : ids){
						Map<String,String> m = new HashMap<String, String>();
						m.put("billId", id);
						list.add(m);
					}
					logger.info("=================账单修改参数billArrs："+billArrs+",grossAmount:"+grossAmount);
					String response = HttpClientUtil.httpRepayment(xfbInterface, user,obj.getString("userId"), xfbVersion,JSONArray.fromObject(list),grossAmount+"",tradeNo);
					JSONObject result = JSONObject.fromObject(response);
					int resultCode = result.getInt("resultCode");
					if (resultCode != 0) {
						logger.info("=================账单修改失败，信分宝返回信息：resultCode:"+resultCode+",message:"+result.getString("resultMessage"));
						return false;
					}else{
						logger.info("=================账单修改成功，信分宝返回信息：resultCode:"+resultCode+",message:"+result.getString("resultMessage"));
						return true;
					}
				}else{
					return false;
				}
			}
		}
		return false;
	}
}
