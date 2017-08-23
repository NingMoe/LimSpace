package com.tyiti.easycommerce.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
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
import com.tyiti.easycommerce.entity.Bill;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.Refund;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.BillRecodeDao;
import com.tyiti.easycommerce.repository.OrderCancellationDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.PayRecodeDao;
import com.tyiti.easycommerce.repository.PickupOrderDao;
import com.tyiti.easycommerce.repository.RefundDao;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.service.OrderCancellationService;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.exception.CommonException;
import com.tyiti.easycommerce.util.exception.OrderStatusException;
import com.tyiti.easycommerce.util.exception.RefundException;

@Service("orderCancellationService")
public class OrderCancellationServiceImpl implements OrderCancellationService {

	private static Log log = LogFactory
			.getLog(OrderCancellationServiceImpl.class);
	@Autowired
	private OrderCancellationDao orderCancellationDao;

	@Autowired
	private RefundDao refundDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private PayRecodeDao payRecodeDao;

	@Autowired
	private PickupOrderDao pickupOrderDao;
	@Autowired
	private BillRecodeDao billRecodeDao;

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

	@Override
	public SearchResult<Map<String, Object>> getOrderCancelList(
			Map<String, Object> param) {
		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit",
					Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset",
					Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		searchResult.setRows(this.orderCancellationDao
				.selectOrderCancelList(param));
		searchResult.setTotal(this.orderCancellationDao
				.selectOrderCancelCount(param));
		return searchResult;
	}

	@Override
	public Map<String, Object> getOrderCancelDetail(int id) {
		// TODO Auto-generated method stub
		return orderCancellationDao.orderCancelDetail(id);
	}

	/**
	 * 取消申请
	 */
	@Transactional
	public Map<String, Object> orderCancelMakeSure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> statusMap = orderCancellationDao.getOrderCancelStatus(param);
		for (Map<String, Object> sMap : statusMap) {
			int id = Integer.parseInt(String.valueOf(sMap.get("id")));
			if (!sMap.get("status").equals(0)) {
				throw new OrderStatusException("当前退货类型为："+ sMap.get("typeText") + ";退货状态为："+ sMap.get("statusText"));
			}
			// 付款后取消订单 同意
			updateCancellation(id, 1);
			// 取消自提单
			pickupOrderDao.updatePickupOrderStatus(
					Integer.parseInt(String.valueOf(sMap.get("orderId"))), 7);
			LogUtil.log(id,
					LogEnum.OperateModel.CANCELLATION.getKey(),
					LogEnum.Action.QUEDINGTUIHUO.getKey(),
					LogEnum.Action.QUEDINGTUIHUO.getValue(),
					LogEnum.Source.PLAT.getKey(), 1);

			BigDecimal aRefundBillAmount = new BigDecimal(0.00);
			if (sMap.get("custId") != null && sMap.get("stageId") != null) {
				int custId = Integer
						.parseInt(String.valueOf(sMap.get("custId")));
				String stageId = String.valueOf(sMap.get("stageId"));
				String xfbResult = orderDetail(custId, stageId);
				try {
					JSONObject.fromObject(xfbResult);
				} catch (Exception e) {
					// TODO: handle exception
					throw new CommonException("连接信分宝失败");
				}
				JSONObject obj = JSONObject.fromObject(xfbResult);
				if (obj.get("resultCode").equals("0")) {
					aRefundBillAmount = new BigDecimal(String.valueOf(obj
							.get("aRefundBillAmount")));// 已还
					BigDecimal sRefundBillAmount = new BigDecimal(
							String.valueOf(obj.get("sRefundBillAmount")));// 分期
					log.info("aRefundBillAmount:" + aRefundBillAmount + ";"
							+ "sRefundBillAmount" + sRefundBillAmount);
				} else {
					throw new CommonException("查询信分宝订单信息异常");
				}
			}
			if (Integer.parseInt(String.valueOf(sMap.get("type"))) == 2) {
				// 退货 同意
				updateCancellation(id,3);	
				// 自提退货
				pickupOrderDao.updatePickupOrderStatus(Integer.parseInt(String.valueOf(sMap.get("orderId"))),4);
				LogUtil.log(id, LogEnum.OperateModel.CANCELLATION.getKey(), LogEnum.Action.QUEDINGTUIHUO.getKey(), LogEnum.Action.QUEDINGTUIHUO.getValue(), LogEnum.Source.PLAT.getKey(), 1);
			} else if (Integer.parseInt(String.valueOf(sMap.get("type"))) == 1 ) {
				// 付款后取消订单 同意
				//取消自提单
				pickupOrderDao.updatePickupOrderStatus(Integer.parseInt(String.valueOf(sMap.get("orderId"))),7);
				LogUtil.log(id, LogEnum.OperateModel.CANCELLATION.getKey(), LogEnum.Action.QUEDINGTUIHUO.getKey(), LogEnum.Action.QUEDINGTUIHUO.getValue(), LogEnum.Source.PLAT.getKey(), 1);
				if (sMap.get("custId") != null && sMap.get("stageId") != null) {
					int custId = Integer.parseInt(String.valueOf(sMap.get("custId")));
					String stageId = String.valueOf(sMap.get("stageId"));
					String xfbResult = orderDetail(custId, stageId);
					try {
						JSONObject.fromObject(xfbResult);
					} catch (Exception e) {
						throw new CommonException("连接信分宝失败");
					}
					JSONObject obj = JSONObject.fromObject(xfbResult);
					if (obj.get("resultCode").equals("0")) {
						aRefundBillAmount = new BigDecimal(String.valueOf(obj.get("aRefundBillAmount")));// 已还
						BigDecimal sRefundBillAmount = new BigDecimal(String.valueOf(obj.get("sRefundBillAmount")));// 分期
						log.info("aRefundBillAmount:" + aRefundBillAmount + ";"+ "sRefundBillAmount" + sRefundBillAmount);
					} else {
						throw new CommonException("查询信分宝订单信息异常");
					}
				}else if(sys.equals("ceb")){
					Order order = orderDao.getById(Integer.parseInt(String.valueOf(sMap.get("orderId"))));
					aRefundBillAmount =order.getInstallmentAmount();
				}
				// 查询一下库存，然后将库存恢复
				Map<String, Object> orderSku = orderDao.getOrderSkuCount(Integer.parseInt(String.valueOf(sMap.get("orderId"))));
				int skuCount = Integer.parseInt(String.valueOf(orderSku.get("skuCount")));
				int skuId = Integer.parseInt(String.valueOf(orderSku.get("skuId")));
				orderDao.updateSkuCount(skuId, skuCount);
				 Order order = orderDao.getById(Integer.parseInt(String.valueOf(sMap.get("orderId"))));
				 if((order.getDownPayment().add(aRefundBillAmount).compareTo(new BigDecimal(0.00))>0)){
					 updateCancellation(id,1);
					 createRefund(id, aRefundBillAmount);
				 }else{
					 updateCancellation(id,5);
					 order.setStatus(9);
					 orderDao.updateStatus(order);
				 }
				if (sMap.get("custId") != null && sMap.get("stageId") != null) {
					int custId = Integer.parseInt(String.valueOf(sMap.get("custId")));
					String stageId = String.valueOf(sMap.get("stageId"));
					returnCredit(custId, stageId, "0");
				}
			}
		}
		return map;
	}

	/**
	 * 拒绝退货退款申请
	 */
	public Map<String, Object> orderCancelRefuse(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> statusMap = orderCancellationDao
				.getOrderCancelStatus(param);
		for (Map<String, Object> sMap : statusMap) {
			// 退货单id
			int id = Integer.parseInt(String.valueOf(sMap.get("id")));
			if (Integer.parseInt(String.valueOf(sMap.get("status"))) == 0
					|| Integer.parseInt(String.valueOf(sMap.get("status"))) == 1) {
				OrderCancellation orderCancellation = new OrderCancellation();
				orderCancellation.setId(id);
				orderCancellation.setStatus(2);
				int num = this.orderCancellationDao
						.updateByPrimaryKeySelective(orderCancellation);
				LogUtil.log(id,
						LogEnum.OperateModel.CANCELLATION.getKey(),
						LogEnum.Action.JUJUETUIHUO.getKey(),
						LogEnum.Action.JUJUETUIHUO.getValue(),
						LogEnum.Source.PLAT.getKey(), 1);
				map.put("num", num);
				if (num == 0) {
					map.put("message", "未知原因导致操作失败,请重试或联系管理员");
				}

			} else {
				map.put("num", 0);
				map.put("message", "当前退货状态为" + sMap.get("statusText")
						+ ",不能执行确定操作");
			}
		}
		// 判断当前是否可以拒绝退货 status = 0 , 1 可以其他不可以

		return map;
	}

	/**
	 * 收货后 进行 生成退款单
	 */
	@Transactional
	public Map<String, Object> orderCancelDelivery(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取订单状态
		List<Map<String, Object>> statusMap = orderCancellationDao
				.getOrderCancelStatus(param);
		for (Map<String, Object> sMap : statusMap) {
			int id = Integer.parseInt(String.valueOf(sMap.get("id")));
			// 用户发货后才能 确定收货 status=3 已发货
			if (Integer.parseInt(String.valueOf(sMap.get("status"))) == 3) {
				// 查询是否为自提商品 ，判断自提商品是否为 已退货
				PickupOrder pickupOrder = pickupOrderDao
						.selectByOrderId(Integer.parseInt(String.valueOf(sMap
								.get("orderId"))));
				if (pickupOrder != null) {
					if (pickupOrder.getStatus() != 6) {
						throw new CommonException("自提订单商家未完成退货，不能执行收货操作");
					}
				}
				updateCancellation(id, 4);
				LogUtil.log(id,
						LogEnum.OperateModel.CANCELLATION.getKey(),
						LogEnum.Action.SHOUHUO.getKey(), "收货",
						LogEnum.Source.PLAT.getKey(), 1);
				LogUtil.log(id, LogEnum.OperateModel.CANCELLATION.getKey(), LogEnum.Action.SHOUHUO.getKey(), "收货", LogEnum.Source.PLAT.getKey(), 1);
				BigDecimal aRefundBillAmount = new BigDecimal(0.00);
				if (sMap.get("custId") != null && sMap.get("stageId") != null) {
					int custId = Integer.parseInt(String.valueOf(sMap.get("custId")));
					String stageId = String.valueOf(sMap.get("stageId"));
					String xfbResult = orderDetail(custId, stageId);
					JSONObject obj = JSONObject.fromObject(xfbResult);
					if (obj.get("resultCode").equals("0")) {
						aRefundBillAmount = new BigDecimal(String.valueOf(obj
								.get("aRefundBillAmount")));// 已还
						BigDecimal sRefundBillAmount = new BigDecimal(
								String.valueOf(obj.get("sRefundBillAmount")));// 分期
						log.info("aRefundBillAmount:" + aRefundBillAmount + ";"
								+ "sRefundBillAmount" + sRefundBillAmount);
					} else {
						throw new CommonException("查询信分宝订单信息异常");
					}
				}else if(sys.equals("ceb")){
					Order order = orderDao.getById(Integer.parseInt(String.valueOf(sMap.get("orderId"))));
					aRefundBillAmount =order.getInstallmentAmount();
				}
				/**
				 * update 2016-05-18 shenzhiqiang 生成退款记录之前修改t_order表的cancel_time字段
				 */		
				this.orderCancellationDao.cancel(id);
				 Order order = orderDao.getById(Integer.parseInt(String.valueOf(sMap.get("orderId"))));
				 if(order.getDownPayment().add(aRefundBillAmount).compareTo(new BigDecimal(0.00))>0){
					 updateCancellation(id,4);
					 createRefund(id, aRefundBillAmount);
				 }else{
					 //退货完成不生成退款单
					 order.setStatus(8);
					 orderDao.updateStatus(order);
					 updateCancellation(id,5);
				 }
				
				if (sMap.get("custId") != null && sMap.get("stageId") != null) {
					int custId = Integer.parseInt(String.valueOf(sMap.get("custId")));
					String stageId = String.valueOf(sMap.get("stageId"));
					returnCredit(custId, stageId, "1");
				}
			} else {
				map.put("num", 0);
				map.put("message", "当前退货状态为" + sMap.get("statusText")
						+ ",不能执行确定操作");
			}
		}

		return map;
	}

	/**
	 * @Title: updateCancellation
	 * @Description: TODO(修改状态)
	 * @return void 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	private void updateCancellation(int id, int status) {
		OrderCancellation orderCancellation = new OrderCancellation();
		orderCancellation.setId(id);
		orderCancellation.setStatus(status);
		this.orderCancellationDao
				.updateByPrimaryKeySelective(orderCancellation);
	}

	/**
	 * 返还授信额度
	 * 
	 * @param custId
	 * @param stageId
	 * @param type
	 */
	private void returnCredit(Integer custId, String stageId, String type) {
		log.info("#########################################################");
		if (connectedToXfb) {
			if (stageId == null) {
				throw new CommonException("授信账单不存在");
			}
			User user = userDao.selectByCustId(custId);
			log.info("xfbInterface:" + xfbInterface + "getThirdPartyId :"
					+ user.getThirdPartyId() + ";user.getMobile():"
					+ user.getMobile() + "user.getPassword()："
					+ user.getPassword() + "xfbVersion" + xfbVersion);
			// 先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface,
					user.getThirdPartyId(), user.getMobile(),
					user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			log.info(obj);
			if (obj != null && !obj.isEmpty()) {
				String returnObj = HttpClientUtil.httpCancelOrReturnOrder(
						xfbInterface, user.getThirdPartyId(), user.getMobile(),
						user.getPassword(), xfbVersion, stageId,
						"用户申请退款,退回授信额度", type);
				JSONObject result = JSONObject.fromObject(returnObj);
				if (result.get("resultCode").equals("0")) {
				} else {
					throw new CommonException("返还授信额度失败："
							+ result.get("resultCode") + ",错误信息："
							+ result.get("resultMessage"));
				}
			}

		}
	}

	/**
	 * @param aRefundBillAmount
	 * @Title: createRefund
	 * @Description: TODO(生成退款记录，用于收货和申请取消订单同意（确定）)
	 * @return void 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	private void createRefund(int id, BigDecimal aRefundBillAmount) {
		
		Map<String, Object> orderForRefund = this.orderCancellationDao
				.orderCancelDetail(id);
		int orderId = Integer.parseInt(String.valueOf(orderForRefund
				.get("orderId")));
		Order order = orderDao.getById(orderId);
		int cancellationId = Integer.parseInt(String.valueOf(orderForRefund
				.get("id")));
		int refundByOrderNum = refundDao.selectRefundCountByOrderId(orderId,
				cancellationId);
		if (refundByOrderNum != 0) {
			throw new RefundException("该订单已生成过退款记录，请联系管理员");
		}
		Refund refund = new Refund();
		refund.setCreateTime(new Date());
		// 光大是记录在分期里面
		if (order.getStageId() == null && order.getInstallmentPayed()) {
			refund.setAmount(order.getInstallmentAmount());
		// 首付 + 分期	
		} else {
			refund.setAmount(new BigDecimal(String.valueOf(orderForRefund
					.get("downPayment"))).add(aRefundBillAmount));
		}
		
		// 全分期
		refund.setType(3);
		// 订单id
		refund.setOrderId(orderId);
		refund.setCancellationId(cancellationId);
		// 状态 未退款
		refund.setStatus(0);
		int refundNum = refundDao.insert(refund);
		if (refundNum != 1) {
			throw new RefundException("同意退款生成退款单出错");
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
		String json = HttpClientUtil.httpLogin(xfbInterface,
				user.getThirdPartyId(), user.getMobile(), user.getPassword(),
				xfbVersion);
		log.info(json);
		boolean flag = true;
		try {
			JSONObject.fromObject(json);
		} catch (JSONException e) {
			flag = false;
		}
		if (!flag) {
			throw new CommonException("信分宝连接失败");
		}
		if (flag) {
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					str = HttpClientUtil.httpOrderDetail(xfbInterface,
							user.getThirdPartyId(), user.getMobile(),
							user.getPassword(), xfbVersion, stageId);
					log.info(str);
				}
			}
		}
		return str;
	}

	/**
	 * @param aRefundBillAmount
	 * @Title: createRefund
	 * @Description: TODO(信分宝添加通过账单Id(stage_id分期Id)查询账单详情的接口 )
	 * @return Map<String, Object> 返回类型
	 * @author wyy
	 * @throws
	 */
	@Override
	public Map<String, Object> getPayStatus(Integer id,
			HttpServletResponse response, HttpSession session) {

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> downPayment = new HashMap<String, Object>();
		Order order = new Order();
		try {
			order = orderDao.getByRefundId(id);
			if (order == null) {
				map.put("code", 400);
				map.put("messsge", "不存在该订单！");
				return map;
			}
		} catch (Exception e) {
			map.put("code", 400);
			map.put("messsge", "异常：查询订单" + e.getMessage());
			return map;
		}

		/************** 判断是否为天尧分期，为天尧时返回已还账单数据 START ******************/
		try {
			if (connectedToXfb) {
				if (order.getStageId() != null
						&& !"".equals(order.getStageId())) {
					// 1、查询已还账单的微信支付流水号
					User user = userDao.selectByCustId(order.getCustId());
					// 先登录信分宝
					String json = HttpClientUtil.httpLogin(xfbInterface,
							user.getThirdPartyId(), user.getMobile(),
							user.getPassword(), xfbVersion);
					JSONObject obj = JSONObject.fromObject(json);
					if (obj != null && !obj.isEmpty()
							&& obj.get("resultCode").equals("0")) {
						// 查看账单详情
						String returnObj = HttpClientUtil
								.httpOrderAndBillDetail(xfbInterface, user
										.getThirdPartyId(), user.getMobile(),
										user.getPassword(), xfbVersion, order
												.getStageId().toString());
						log.info("获取信分宝订单详情的数据：" + returnObj);
						JSONObject result = JSONObject.fromObject(returnObj);
						if (result.get("resultCode").equals("0")) {

							JSONObject resultData = JSONObject
									.fromObject(result.get("data"));
							log.info("获取信分宝订单及账单详情的数据：" + resultData);
							map.put("orderList", resultData);

							List<Bill> billList = new ArrayList<Bill>();
							// 全部账单数组
							JSONArray jsonArray = JSONArray
									.fromObject(resultData.get("billList"));
							List<Bill> collection = new ArrayList<Bill>();
							for (int i = 0; i < jsonArray.size(); i++) {
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);
								collection.add((Bill) JSONObject.toBean(
										jsonObject, Bill.class));
							}
							if (collection.size() > 0) {
								Map<String, Object> billMap = new HashMap<String, Object>();
								for (Bill bill : collection) {
									// 查询已还账单的微信支付流水号
									billMap = billRecodeDao
											.billRecodeByBillIdOrPayNo(
													bill.getBillId(), null);
									if (billMap != null) {
										// 微信支付流水号
										bill.setPayNo(billMap.get("pay_no")
												.toString());
										bill.setPayState(billMap.get(
												"pay_state").toString());
										bill.setTradeNo(billMap.get("trade_no")
												.toString());
										bill.setBillId(bill.getBillId());

										bill.setCreateTime(bill.getCreateTime());
										billList.add(bill);
									}
								}
							}
							// 已还款账单
							map.put("billList", billList);
						} else {
							map.put("billList",
									"获取已还账单失败,错误信息:"
											+ result.get("resultMessage"));
							log.info("分期异常：" + result.get("resultMessage"));
						}
					}
				} else {
					map.put("billList", "不存在分期账单！");
				}
			}
		} catch (Exception e) {
			log.info("分期异常(Exception)：" + e.getMessage());
			map.put("billList", e.getMessage() + e);
		}
		/************** 判断是否为天尧分期，为天尧时返回已还账单数据 END ******************/

		Map<String, Object> payMap = new HashMap<String, Object>();
		try {
			// 2、查询订单的首付微信支付流水号
			payMap = payRecodeDao.payRecodeByOrderIdOrPayNo(order.getId()
					.toString(), "");
			downPayment.put("Amount", order.getDownPayment());
			if (payMap != null) {
				downPayment.put("PayNo", payMap.get("pay_no"));
				downPayment.put("PayState", payMap.get("pay_state"));
			}
		} catch (Exception e) {
			log.info("首付异常(Exception)：" + e.getMessage() + " ---- " + order);
			map.put("downPayment", e.getMessage());
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		// 首付款
		map.put("downPayment", downPayment);
		return map;
	}
}
