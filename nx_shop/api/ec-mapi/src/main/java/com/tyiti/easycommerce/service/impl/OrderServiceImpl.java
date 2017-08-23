package com.tyiti.easycommerce.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.common.service.NoService;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.Refund;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.OrderCancellationDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.PickupOrderDao;
import com.tyiti.easycommerce.repository.PickupPointDao;
import com.tyiti.easycommerce.repository.RefundDao;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.OrderHttpClientUtil;
import com.tyiti.easycommerce.util.exception.CommonException;
import com.tyiti.easycommerce.util.exception.OrderStatusException;
import com.tyiti.easycommerce.util.exception.ServiceException;
import com.tyiti.easycommerce.util.ordersystem.Orders;
import com.tyiti.easycommerce.util.ordersystem.Sku;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private static Log log = LogFactory.getLog(OrderServiceImpl.class);
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderCancellationDao orderCancellationDao;

	@Autowired
	private RefundDao refundDao;

	@Autowired
	private NoService noService;

	@Autowired
	private PickupOrderDao pickupOrderDao;

	@Autowired
	private PickupPointDao pickupPointDao;

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
	// 用户是否和信分宝打通
	@Value("${connectedToXfb}")
	private boolean connectedToXfb;
	// sys
	@Value("${sys}")
	private String sys;
	//订单系统 连接 秘钥
	@Value("${orderSysUrl}")
	private String orderSysUrl; // http://192.168.50.143:8080/order-api/orders/add/shangcheng" 
	@Value("${OrderSystemSecretKey}")
	private String OrderSystemSecretKey ; 
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OrderSkuDao orderSkuDao ; 
	/**
	 * 订单详情
	 * */
	public Map<String, Object> getOrderDetail(Integer id) {
		return orderDao.orderDetail(id);
	}

	/**
	 * 获取订单条件查询分页结果集
	 */
	@Override
	public SearchResult<Map<String, Object>> getOrderList(Map<String, Object> param) {
		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		searchResult.setRows(this.orderDao.selectOrderList(param));
		searchResult.setTotal(this.orderDao.selectOrderCount(param));
		return searchResult;
	}

	@Transactional
	public SearchResult<Map<String, Object>> getOrderByIds(Map<String, Object> param) {
		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		param.put("sys", sys);
		List<Map<String, Object>> rows = this.orderDao.selectOrderListByIds(param);
		List<Map<String, Object>> rowsLogs = this.orderDao.selectOrderListByIdsForOperateLog(param);
		searchResult.setRows(rows);
		searchResult.setTotal(this.orderDao.selectOrderCountByIds(param));
		for (Map<String, Object> map : rowsLogs) {
			if (!map.get("status").equals(3)) {
				throw new OrderStatusException("订单号：" + map.get("no") + ";订单状态为：" + map.get("statusText") + ",无法执行制单操作");
			}
			if (map.containsKey("cancelStatus")) {
				if (!map.get("cancelStatus").equals(2))
					throw new OrderStatusException("订单号：" + map.get("no") + ";申请类型为：" + map.get("cancelTypeText") + ",无法执行确定操作");
			}
			if (param.containsKey("download") && param.get("download").equals(0)) {
			} else {
				LogUtil.log( Integer.parseInt(String.valueOf(map.get("id"))), LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.ZHIDAN.getKey(),
						LogEnum.Action.ZHIDAN.getValue(), LogEnum.Source.PLAT.getKey(), 1);
			}
		}
		// download =1 表示下载 excel 执行制单操作 download =0 表示查看状态 不执行制单操作 所以不更改状态

		if (param.containsKey("download") && param.get("download").equals(0)) {
		} else {
			param.put("status", "4");
			this.orderDao.updateByIds(param);
		}
		return searchResult;
	}

	@Override
	public void orderMakeSure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> rows = this.orderDao.selectOrderListByIdsForOperateLog(param);
		for (Map<String, Object> map : rows) {
			if (!map.get("status").equals(2)) {
				throw new OrderStatusException("订单号：" + map.get("no") + ";订单状态为：" + map.get("statusText") + ",无法执行确定操作");
			}

			if (map.containsKey("cancelStatus")) {
				if (!map.get("cancelStatus").equals(2))
					throw new OrderStatusException("订单号：" + map.get("no") + ";申请类型为：" + map.get("cancelTypeText") + ",无法执行确定操作");
			}
			LogUtil.log( Integer.parseInt(String.valueOf(map.get("id"))), LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.QUEREN.getKey(),
					LogEnum.Action.QUEREN.getValue(), LogEnum.Source.PLAT.getKey(), 1);
			
			/*
			 * 送订单分期成功短信start
			 * author:Black
			 * date:2016-08-23
			 */
			String mobile = (String) map.get("mobile");
			try{
				String thirdPartyId = (String) map.get("thirdPartyId");
				String password = (String) map.get("password");
				String stageId = (String) map.get("stageId");
				// 先登录信分宝
				String json = HttpClientUtil.httpLogin(xfbInterface, thirdPartyId, mobile, thirdPartyId, xfbVersion);
				JSONObject obj = JSONObject.fromObject(json);
				if (obj != null && !obj.isEmpty()) {
					String returnObj = HttpClientUtil.httpSendOrderConfirmSms(xfbInterface, thirdPartyId, mobile, password, xfbVersion, stageId);
					JSONObject result = JSONObject.fromObject(returnObj);
					if (result.get("resultCode").equals("0")) {
						log.info("mobile:"+mobile+"发送订单分期成功短信成功");
					} else {
						log.error("mobile:"+mobile+"发送订单分期成功短信失败");
					}
				}
			} catch(Exception ex) {
				log.error("mobile:"+mobile+"发送订单分期成功短信失败");
			}
			/*
			 * 送订单分期成功短信end
			 */
		}
		param.put("status", "3");
		this.orderDao.updateByIds(param);
		
	}

	@Override
	public void sendSku(Map<String, Object> param) {
		List<Map<String, Object>> rows = this.orderDao.selectOrderListByIdsForOperateLog(param);
		for (Map<String, Object> map : rows) {

			if (!map.get("status").equals(4)) {
				throw new OrderStatusException("订单号：" + map.get("no") + ";订单状态为：" + map.get("statusText") + ",无法发货执行操作");
			}
			if (map.containsKey("cancelStatus")) {
				if (!map.get("cancelStatus").equals(2))
					throw new OrderStatusException("订单号：" + map.get("no") + ";申请类型为：" + map.get("cancelTypeText") + ",无法执行确定操作");
			}
			LogUtil.log( Integer.parseInt(String.valueOf(map.get("id"))), LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.FAHUO.getKey(),
					LogEnum.Action.FAHUO.getValue(), LogEnum.Source.PLAT.getKey(), 1);
			// add by hcy 判断是否是自提订单 如果是自提订单的话 发短信通知客户
			// 自提单id
			if (map.containsKey("pickupId")) {
				Long id = (Long) map.get("pickupId");
				PickupOrder pickupOrder = pickupOrderDao.selectByPrimaryKey(Integer.parseInt(String.valueOf(id)));
				PickupPoint pickupPoint = pickupPointDao.selectByPrimaryKey(pickupOrder.getPickupPointId());
				// Integer userId=(Integer)map.get("userId");
				if (id != null) {
					String mobile = (String) map.get("mobile");
					sendSmsForVerifyCode(mobile, "【淘惠帮】" + "您的提货码为" + pickupOrder.getCode() + "的订单已经发货，" + pickupPoint.getName()
							+ "自提点提货时间"+pickupPoint.getOpenTime()+"。好想快点见到主人呀~电话4006603813");
				}
			}
		}
		param.put("status", "5");
		this.orderDao.updateByIds(param);

	}

	/**
	 * 取消订单增加同步锁
	 * author:Black
	 * date:2016-08-26
	 */
	@Override
	@Transactional
	public synchronized void orderCancel(Integer id) {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		Order order = orderDao.getById(id);
		statusMap = orderDao.getOrderStatus(id);
		Map<String, Object> cancelStatusMap = orderCancellationDao.getOrderCancelStatusByOrderId(id);
		if (cancelStatusMap != null && cancelStatusMap.containsKey("cancelStatus")) {
			if (!cancelStatusMap.get("cancelStatus").equals(2)) {
				throw new OrderStatusException("订单当前状态不允许取消操作,请优先处理售后");
			}
		}

		int status = Integer.parseInt(String.valueOf(statusMap.get("status")));

		// 点击取消的时候把 t_pickup_order.status 置为 7 已取消
		PickupOrder pickupOrder = pickupOrderDao.selectByOrderId(id);
		if (pickupOrder != null) {
			pickupOrder.setStatus(7);
			pickupOrderDao.updateByPrimaryKey(pickupOrder);
		}

		// 状态为未付款(未同时完成首付和分期)、且未首付、未分期
		if (status == 1 && !order.getDownPaymentPayed() && !order.getInstallmentPayed()) {
			orderDao.orderCancel(id);
			// 订单状态，0: 草稿 1: 未付款 2: 已付款 3: 已确认 4: 已制单 5: 已发货 6: 已签收 9: 已取消
			// 注意：status == 1 可能是已部分付款
		} else if (status == 1 || status == 2 || status == 3 || status == 4) {
			// 如果有分期，则实时返回分期额度、取消账单
			BigDecimal aRefundBillAmount = BigDecimal.ZERO; // 已还账单金额
			if (statusMap.get("custId") != null && statusMap.get("stageId") != null) {
				int custId = Integer.parseInt(String.valueOf(statusMap.get("custId")));
				String stageId = String.valueOf(statusMap.get("stageId"));
				String xfbResult = orderDetail(custId, stageId);
				JSONObject obj = JSONObject.fromObject(xfbResult);
				if (obj.get("resultCode").equals("0")) {
					aRefundBillAmount = new BigDecimal(String.valueOf(obj.get("aRefundBillAmount")));
					// 分期
					BigDecimal sRefundBillAmount = new BigDecimal(String.valueOf(obj.get("sRefundBillAmount")));
					log.info("aRefundBillAmount:" + aRefundBillAmount + ";" + "sRefundBillAmount" + sRefundBillAmount);
					// 返还授信额度
					returnCredit(custId, stageId, "0");
				} else {
					throw new CommonException("查询信分宝订单信息异常");
				}
			}

			// 生成申请取消订单记录
			int cancelId = orderCancellationDao.getCancelMaxId() + 1;
			OrderCancellation orderCancellation = new OrderCancellation();
			orderCancellation.setId(cancelId);
			orderCancellation.setCreateTime(new Date());
			orderCancellation.setUpdateTime(new Date());
			orderCancellation.setNo(noService.getCancellationNo());
			// type=1: 申请取消
			orderCancellation.setType(1);
			boolean hasRefund = true;
			// 如果有授信分期，则还要检查是否有其他需要退款的条件
			if (statusMap.get("stageId") != null) {
				// 退款条件，1: 有首付 2: 已还 > 0
				hasRefund = order.getDownPaymentPayed() || aRefundBillAmount.compareTo(BigDecimal.ZERO) > 0;
			}
			// status=1: 已确认 5: 完成
			orderCancellation.setStatus(hasRefund ? 1 : 5);
			orderCancellation.setOrderId(id);
			orderCancellationDao.insertSelective(orderCancellation);

			// 如果不需要退还现金，则订单直接更改为已取消
			if (!hasRefund) {
				order.setStatus(9);
				orderDao.updateStatus(order);
			}
			// 查询一下库存，然后将库存恢复
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("id", id);
			List<Map<String, Object>> orderSkus = orderDao.selectOrderSkusList(param);
			Order orderThis = (Order) orderSkus.get(0);
			List<OrderSku> skuList = orderThis.getOrderSku();
			for(OrderSku sku:skuList){
				// 增加 skuCount 个库存
				orderDao.updateSkuCount(sku.getId(), sku.getSkuCount());
			}
			// 生成退款记录，备注：现在只记录了需要退现金的情况，不包括分期（注意：光大是记录为分期的）
			if (hasRefund) {
				int orderId = orderThis.getId();
				int refundByOrderNum = refundDao.selectRefundCountByOrderId(orderId, cancelId);
				if (refundByOrderNum != 0) {
					throw new OrderStatusException("该订单已生成过退款记录，请联系管理员");
				}
				Refund refund = new Refund();
				refund.setCreateTime(new Date());
				// 退款金额 = 已还账单金额
				BigDecimal amount = aRefundBillAmount;
				// 如果支付了首付：退款金额 = 首付金额 + 已还账单金额
				if (order.getDownPaymentPayed()) {
					amount = amount.add(orderThis.getDownPayment());
				}
				// 光大的情况
				if (statusMap.get("stageId") == null && order.getInstallmentPayed()) {
					amount = amount.add(new BigDecimal(String.valueOf(order.getInstallmentAmount())));
				}
				refund.setAmount(amount);
				// 1: 首付款 2: 已还分期金额 3: 分期额度
				refund.setType(3);
				// 订单id
				refund.setOrderId(orderId);
				refund.setRefundType(1);
				refund.setCancellationId(cancelId);
				// 状态 未退款
				refund.setStatus(0);
				try {
					refundDao.insert(refund);
					LogUtil.log(orderId, LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.QUXIAO.getKey(), LogEnum.Action.QUXIAO.getValue(),
							LogEnum.Source.PLAT.getKey(), 1);
					LogUtil.log(orderId, LogEnum.OperateModel.CANCELLATION.getKey(), LogEnum.Action.TONGYIQUXIAO.getKey(),
							LogEnum.Action.TONGYIQUXIAO.getValue(), LogEnum.Source.PLAT.getKey(), 1);

				} catch (Exception e) {
					throw new OrderStatusException("生成退款单出错");
				}
			}
		} else {
			throw new OrderStatusException("当前订单状态为" + statusMap.get("statusText") + ",不能执行取消操作");
		}
	}

	/**
	 * 返还授信额度
	 * 
	 * @param custId
	 * @param stageId
	 * @param type
	 */
	private void returnCredit(Integer custId, String stageId, String type) {
		if (connectedToXfb) {
			if (stageId == null) {
				throw new CommonException("授信账单不存在");
			}
			User user = userDao.selectByCustId(custId);
			// 先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, user.getThirdPartyId(), user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				String returnObj = HttpClientUtil.httpCancelOrReturnOrder(xfbInterface, user.getThirdPartyId(), user.getMobile(), user.getPassword(),
						xfbVersion, stageId, "用户申请退款,退回授信额度", type);
				JSONObject result = JSONObject.fromObject(returnObj);
				if (result.get("resultCode").equals("0")) {
				} else {
					throw new CommonException("返还授信额度失败：" + result.get("resultCode") + ",错误信息：" + result.get("resultMessage"));
				}
			}

		}
	}

	/**
	 * 信分宝订单详情
	 * 
	 * @param userId
	 * @param stageId
	 */
	public String orderDetail(int custId, String stageId) {
		String str = null;
		User user = userDao.selectByCustId(custId);
		String json = HttpClientUtil.httpLogin(xfbInterface, user.getThirdPartyId(), user.getMobile(), user.getPassword(), xfbVersion);
		log.info(json);
		boolean flag = true;
		try {
			JSONObject.fromObject(json);
		} catch (JSONException e) {
			flag = false;
		}
		if (flag) {
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					str = HttpClientUtil.httpOrderDetail(xfbInterface, user.getThirdPartyId(), user.getMobile(), user.getPassword(), xfbVersion, stageId);
					log.info(str);
				}
			}
		}
		return str;
	}

	public int sendSmsForVerifyCode(String mobile, String verifyCode) {
		// String url = "http://cf.lmobile.cn/submitdata/service.asmx/g_Submit";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("sname", sname);
		requestParams.put("spwd", spwd);
		requestParams.put("scorpid", scorpid);
		requestParams.put("sprdid", sprdid);
		requestParams.put("sdst", mobile);
		requestParams.put("smsg", verifyCode);
		System.out.println("verifyCode=" + verifyCode);
		// TODO 解析返回的 XML
		HttpClientUtil.httpGet(smsUrl, null, requestParams);
		return 0;
	}

	/**
	 * <p>
	 * 功能描述:查找新东订单信息。
	 * </p>
	 * 
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年7月8日 上午9:57:04。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public SearchResult<Map<String, Object>> findKooOrders(Map<String, Object> param) {
		SearchResult<Map<String, Object>> searchResult =new SearchResult<Map<String, Object>>();
		searchResult.setRows(orderDao.findKooOrders(param));
		searchResult.setTotal(orderDao.findKooTotalOrders(param));
		return searchResult;
	}
	
	@Override
	public Boolean logisticsImport(Order order) {
		Boolean b = false ;
		try{
			if(orderDao.logisticsImport(order)==1){
				b = true;
			}
		}
		catch(ServiceException e){
			log.error("导入物流单失败，order.no="+order.getNo()+"。原因："+e.getMessage());
		}
		return b;
	}
	/**
	* @Title: kooOrdersExport
	* @Description: TODO(新东方订单导出)
	* @return SearchResult<Map<String,Object>>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	public SearchResult<Map<String, Object>> kooOrdersExport(Map<String, Object> param) {
		SearchResult<Map<String, Object>> searchResult =new SearchResult<Map<String, Object>>();
		searchResult.setRows(orderDao.findKooOrders(param));
		searchResult.setTotal(orderDao.findKooTotalOrders(param));
		return searchResult;
	}
	@Override
	public SearchResult<Map<String,Object>> getOrdersSkus(Map<String,Object> param) {
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		searchResult.setRows(this.orderDao.selectOrderSkusList(param));
		searchResult.setTotal(this.orderDao.selectOrderSkusCount(param));
		 return searchResult;
	}
	@Transactional
	@Override
	public synchronized SearchResult<Map<String, Object>> promoteOrdersExport(Map<String, Object> param) {
		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		param.put("sys", sys);
		List<Map<String, Object>> rows = this.orderDao.selectOrdersMaking(param);
		List<Map<String, Object>> rowsLog = this.orderDao.selectOrderListByIdsForOperateLog(param);
		searchResult.setRows(rows);
		searchResult.setTotal(this.orderDao.selectOrderCountByIds(param));
		for (Map<String, Object> map : rowsLog) {
			if (!map.get("status").equals(3)) {
				throw new OrderStatusException("订单号：" + map.get("no") + ";订单状态为：" + map.get("statusText") + ",无法执行制单操作");
			}
			if (map.containsKey("cancelStatus")) {
				if (!map.get("cancelStatus").equals(2))
					throw new OrderStatusException("订单号：" + map.get("no") + ";申请类型为：" + map.get("cancelTypeText") + ",无法执行确定操作");
			}
			if (param.containsKey("download") && param.get("download").equals(0)) {
			} else {
				LogUtil.log(Integer.parseInt(String.valueOf(map.get("id"))), LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.ZHIDAN.getKey(),
						LogEnum.Action.ZHIDAN.getValue(), LogEnum.Source.PLAT.getKey(), 1);
			}
		}
		// download =1 表示下载 excel 执行制单操作 download =0 表示查看状态 不执行制单操作 所以不更改状态
		if (param.containsKey("download") && param.get("download").equals(0)) {
		} else {
			param.put("status", "4");
			this.orderDao.updateByIds(param);
		}
		return searchResult;
	}
	@Transactional
	@Override
	public synchronized Map<String, Object> toOrderSys(Map<String, Object> param) {
		 Map<String, Object> data = new HashMap<String, Object>();
 		param.put("sys", sys);
 		List<Orders> rows = this.orderDao.selectOrdersToOrderSystem(param);
 		for (Orders orders : rows) {
			Integer status = orders.getShopOrderStatus() ; 
			Integer cancelStatus = orders.getCancelStatus();
			if(!status.equals(3)){
				data.put("code",400);
				data.put("message" ,"订单号：" + orders.getNo() + ";订单状态为：" + orders.getShopOrderStatus() + ",无法执行制单操作");
				return data ; 
			}
			if(cancelStatus != null)
			if(!cancelStatus.equals(2)){
				data.put("code",400);
				data.put("message" ,"订单号：" + orders.getNo() + ";订单状态为：" + orders.getShopOrderStatus() + ",无法执行制单操作");
				return data ; 
			}
		}
 		String returenMessage = "" ; 
 		String httpUrl = orderSysUrl ; // "http://192.168.50.143:8080/order-api/orders/add/shangcheng"; 
 		for (Orders ordersToSys : rows) {
 			//处理 商品信息 
 			List<OrderSku> orderSkus = orderSkuDao.getByOrderId(ordersToSys.getShopOrderId()) ; 
 			Integer maxPriceOrderSkuId  = null ; // 将价钱最多的商品处理为主商品
 			BigDecimal price = new BigDecimal(0.00);
 			BigDecimal skusPrice  = new BigDecimal(0.00);
 			for (OrderSku orderSku : orderSkus) {
 				if(orderSku.getAvgPrice() != null){
 					  skusPrice = orderSku.getAvgPrice().multiply(new BigDecimal(orderSku.getSkuCount())) ; 
 				}else{
 					 skusPrice = orderSku.getSkuPrice().multiply(new BigDecimal(orderSku.getSkuCount())) ;
 				}
 				
				 if(skusPrice.compareTo(price)>-1){
					 maxPriceOrderSkuId = orderSku.getId();
					 price = skusPrice ; 
				 }
			}
 			//组装订单系统需要的sku 信息
 			List<Sku> skuList = new ArrayList<Sku>();
 			for (OrderSku orderSku : orderSkus) {
 				Sku sku = new Sku();
				if(orderSku.getId().equals(maxPriceOrderSkuId)){
					sku.setType(1);
					ordersToSys.setSkuNo(orderSku.getSkuErpCode());
					ordersToSys.setSkuName(orderSku.getSkuName());
					ordersToSys.setSpecification(orderSku.getSkuAttribute());
					BigDecimal toSysPrice = new BigDecimal(0.00); //插入到订单系统的价格
					if(orderSku.getAvgPrice() != null){
						toSysPrice = orderSku.getAvgPrice().multiply(new BigDecimal(orderSku.getSkuCount())) ; 
	 				}else{
	 					toSysPrice = orderSku.getSkuPrice().multiply(new BigDecimal(orderSku.getSkuCount())) ;
	 				}
					ordersToSys.setSkuPrice(toSysPrice);
				}else{
					sku.setType(2);
				}
				sku.setCount(orderSku.getSkuCount());
				sku.setPrice(orderSku.getAvgPrice());
				sku.setNo(orderSku.getSkuErpCode());
				skuList.add(sku);
			}
 			ordersToSys.setSkus(skuList);
 			ordersToSys.setStatusVal(1); // 订单系统状态
			 // 将map 转换成json 数据 
			JSONObject requestParams = JSONObject.fromObject(ordersToSys);
			System.out.println("requestParams"+requestParams);
			System.out.println("orderSysUrl"+orderSysUrl);
			String message = OrderHttpClientUtil.addDataToOrderSys(httpUrl, requestParams,ordersToSys.getNo(),OrderSystemSecretKey);
			JSONObject resultJson = JSONObject.fromObject(message);	
			System.out.println(resultJson);
			if(resultJson !=null){
				Integer code = resultJson.getInt("code");
				if(code.equals(400)){
					data.put("code", 400);
					returenMessage = returenMessage + resultJson.getString("message");
				}else{
					Order order  = new Order();
					order.setId(ordersToSys.getShopOrderId());
					order.setStatus(4); // 已制单
					orderDao.updateStatus(order) ; 
					LogUtil.log(ordersToSys.getShopOrderId(), LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.ZHIDAN.getKey(),
						LogEnum.Action.ZHIDAN.getValue(), LogEnum.Source.PLAT.getKey(), 1);
				}
			}
 		}
 		if(data.containsKey("code")&&data.get("code").equals(400)){
 			data.put("message", returenMessage); 
 		}else{
 			data.put("code", 200);
 		}
 		
		return data;
	}
}
