/**
 * 文件名[fileName]：KooPayOrderServiceImpl.java
 * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
 * @version: v1.0.0.1
 * 日期：2016年5月17日 下午1:38:19
 * Copyright 【北京天尧信息有限公司所有】 2016 
 */

package com.tyiti.easycommerce.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.ConfigKey;
import com.tyiti.easycommerce.common.SysConfig;
import com.tyiti.easycommerce.entity.KooPushLog;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.SkuExt;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.KooPushLogDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.SkuExtDao;
import com.tyiti.easycommerce.service.KooPayOrderService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.service.SkuService;
import com.tyiti.easycommerce.service.UserService;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.SignUtil;

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
 *        创建日期：2016年5月17日 下午1:38:19。
 *        </p>
 */
@Service
public class KooPayOrderServiceImpl implements KooPayOrderService {

	private Log logger = LogFactory.getLog(KooPayOrderServiceImpl.class);
	@Autowired
	SkuService skuService;
	@Autowired
	OrderSkuDao orderSkuDao;
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	KooPushLogDao kooPushLogDao;
	@Autowired
	SkuExtDao skuExtDao;

	/**
	 * <p>
	 * 功能描述:。
	 * </p>
	 * 
	 * @param orderEntity
	 * @param user
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年5月17日 下午1:47:43。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public String getPushStringJson(Order orderEntity, User user) {
		int orderId = orderEntity.getId();
		String orderNo = orderEntity.getNo();
		int payType = orderEntity.getPayMethod();
		BigDecimal orderTotalPrice = orderEntity.getThirdPartyAmount();
		// BigDecimal orderPayMent = orderEntity.getDownPayment();
		BigDecimal orderPayMent = orderEntity.getThirdPartyAmount();
		Double sellerDiscount = orderTotalPrice.subtract(orderPayMent).doubleValue();
		Integer orderState = orderEntity.getStatus();
		String orderRemark = "";
		Date orderTime = orderEntity.getCreateTime();
		Date payTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String strOrderTime = formatter.format(orderTime);
		if (orderEntity.getPayMethod().equals("1")) {// 全款
			payTime = orderEntity.getDownPaymentTime();
		} else if (orderEntity.getPayMethod().equals("2")) {// 全分期
			payTime = orderEntity.getInstallmentTime();
		} else if (orderEntity.getPayMethod().equals("3")) {// 首付+分期
			Date paymentTime = orderEntity.getDownPaymentTime();
			Date installmentTime = orderEntity.getInstallmentTime();
			boolean flag = paymentTime.before(installmentTime);
			if (flag) {
				payTime = paymentTime;
			} else {
				payTime = installmentTime;
			}
		}
		String strPayTime = formatter.format(payTime);
		String userName = (String) user.getRealName();

		String mobile = (String) user.getMobile();
		if (orderEntity != null && orderEntity.getAddressMobile() != null && orderEntity.getAddressName() != "") {
			mobile = orderEntity.getAddressMobile();
		}
		String email = "";
		// String email = (String) user.getAttribute("email");// 没有邮箱
		String address = orderEntity.getAddressName();
		if (address == null) {
			address = "";
		}
		List<OrderSku> orderskuList = orderSkuDao.getByOrderId(orderId);
		StringBuffer pushStr = new StringBuffer();
		pushStr.append("{\"orders_response\":");
		pushStr.append("{\"total_results\":1,");
		pushStr.append("\"orders\":{");
		pushStr.append("\"order\":[");
		pushStr.append("{ ");
		pushStr.append("\"orderId\":\"" + orderNo + "\",  ");
		pushStr.append("\"payType\":\"" + payType + "\",  ");
		pushStr.append("\"orderTotalPrice\":\"" + orderTotalPrice + "\",  ");
		pushStr.append("\"orderPayment\":\"" + orderPayMent + "\",  ");
		pushStr.append("\"sellerDiscount\":\"" + sellerDiscount + "\",  ");
		pushStr.append("\"orderState\":\"" + orderState + "\",  ");
		pushStr.append("\"orderRemark\":\"" + orderRemark + "\",  ");
		pushStr.append("\"orderTime\":\"" + strOrderTime + "\",  ");
		pushStr.append("\"payTime\":\"" + strPayTime + "\",  ");
		pushStr.append("\"userName\":\"" + userName + "\",  ");
		pushStr.append("\"mobile\":\"" + mobile + "\",  ");
		pushStr.append("\"email\":\"" + email + "\",  ");
		pushStr.append("\"address\":\"" + address + "\",  ");
		int itemNum = 0;
		if (orderskuList != null) {
			itemNum = orderskuList.size();
		}
		pushStr.append("\"itemNum\":\"" + itemNum + "\",  ");
		pushStr.append("\"orderitems\": {");
		pushStr.append("\"orderitem\":[");
		for (int i = 0; i < itemNum; i++) {
			OrderSku orderSku = orderskuList.get(i);
			Sku sku = skuService.getById(orderSku.getSkuId());
			if (sku != null) {
				SkuExt skuExt = skuExtDao.findKooProductId(1, sku.getId());
				pushStr.append("{");
				pushStr.append("\"itemId\":\"" + orderSku.getId() + "\" ,");
				String strproductId = skuExt.getField1();
				String productVersion = "";
				String productId = "";
				if (strproductId != null && strproductId != "") {
					productId = strproductId.split("_")[0];
					if(strproductId.split("_").length>1){					
						productVersion = strproductId.split("_")[1];
					}
				}
				pushStr.append("\"productId\":\"" + productId + "\" ,");
				pushStr.append("\"productName\":\"" + sku.getName() + "\" ,");
				pushStr.append("\"productVersion\":\"" + productVersion + "\" ,");
				pushStr.append("\"productPrice\":\"" + skuExt.getField9() + "\" ");
				if (i != itemNum - 1) {
					pushStr.append("},");
				} else {
					pushStr.append("}");
				}
			}
		}
		pushStr.append("]");
		pushStr.append("}");
		pushStr.append("}");
		pushStr.append("]");
		pushStr.append("}");
		pushStr.append("}");
		pushStr.append("}");
		System.out.println(pushStr.toString());
		return pushStr.toString();
	}

	/**
	 * <p>
	 * 功能描述:。
	 * </p>
	 * 
	 * @param order_id
	 * @param session
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年5月18日 下午2:15:29。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public String getHttpClients(String no, Integer userId) {
		String url = SysConfig.configMap.get(ConfigKey.KOO_REQUEST_URL);
		String key = SysConfig.configMap.get(ConfigKey.KOO_REQUEST_KEY);
		logger.info(url + "===============" + key);
		Order orderEntity = orderService.getByNo(no);
		if (orderEntity == null) {
			return null;
		}
		User user = userService.getById(userId);
		// 拼写JSON字符串
		String pushStr = getPushStringJson(orderEntity, user);
		int i = pushStr.indexOf("orderId");
		String readySignStr = pushStr.substring(i, i + 40);
		// 加密
		String signStr = SignUtil.sign(readySignStr, key, "utf-8");
		Map<String, String> props = new HashMap<String, String>();
		props.put("pushStr", pushStr);
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		props.put("pushTime", dateString);
		props.put("signStr", signStr);
		String message = "";
		try {
			KooPushLog pushLog = null;
			pushLog = kooPushLogDao.findByOrderId(orderEntity.getId());// 确保只发送一次
			Integer push_id = 0;
			if (pushLog == null) {
				pushLog = new KooPushLog();
				pushLog.setOrderId(orderEntity.getId());
				pushLog.setUserId(userId);
				pushLog.setPushStr(pushStr);
				pushLog.setPushTime(currentTime);
				pushLog.setCreateTime(new Date());
				pushLog.setIsSuccess(false);
				kooPushLogDao.insert(pushLog);
				push_id = pushLog.getId();
				logger.info(push_id + "=========================第一次插入==========================================" + url);
				message = HttpClientUtil.httpPost(url, null, props, "utf-8");
			} else {
				push_id = pushLog.getId();
				if (!pushLog.getIsSuccess()) {// 如果信息没有发送成功则从新发送
					message = HttpClientUtil.httpPost(url, null, props, "utf-8");
				}
			}
			logger.info(push_id + "=============================推送ID======================================" + url);
			if (message.equals("00")) {
				logger.info(message + "==============================返回信息=====================================" + push_id);
				KooPushLog pushLogEntity = kooPushLogDao.selectByPrimaryKey(push_id);
				pushLogEntity.setIsSuccess(true);
				pushLogEntity.setUpdateTime(new Date());
				kooPushLogDao.updateByPrimaryKeySelective(pushLogEntity);
			}
			logger.info("接口返回字符串:" + message);
		} catch (Exception e) {
			logger.info("调用接口失败" + e);
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * <p>
	 * 功能描述:。推送日志接口
	 * </p>
	 * 
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年6月12日 下午3:48:14。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public Map<String, Object> pushOrderMessage(String no, Integer user_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String httpMessage = getHttpClients(no, user_id);
		if(httpMessage.equals("00")){
			map.put("code", "200");
			map.put("Message", httpMessage);
		}else{
			map.put("code", "200");
			map.put("Message", "推送失败！");
		}

		return map;

	}
}
