package com.tyiti.easycommerce.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.OrderEnum;
import com.tyiti.easycommerce.common.WxAccessToken;
import com.tyiti.easycommerce.common.service.NoService;
import com.tyiti.easycommerce.entity.Coupon;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.entity.OrderReturn;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.CouponDao;
import com.tyiti.easycommerce.repository.CouponRecordDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderOperationDao;
import com.tyiti.easycommerce.repository.OrderPaymentDao;
import com.tyiti.easycommerce.repository.OrderReturnDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.PickupOrderDao;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.service.OrderOperationService;
import com.tyiti.easycommerce.util.CommonException;
import com.tyiti.easycommerce.util.HttpClientUtil;

@Service
public class OrderOperationServiceImpl implements OrderOperationService {

	private static Log log = LogFactory.getLog(OrderOperationServiceImpl.class);
	@Autowired
	private OrderOperationDao orderOperationDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private NoService noService;
	@Autowired
	private CouponRecordDao couponRecordDao;
	@Autowired
	private OrderPaymentDao orderPaymentDao;
	@Autowired
	private OrderSkuDao orderSkuDao ;
	@Autowired
	private OrderReturnDao orderReturnDao ; 
	


	// 用户是否和信分宝打通
	@Value("${connectedToXfb}")
	private boolean connectedToXfb;
	@Value("${sys}")
	private String sys;
	// 信分宝接口地址
	@Value("${xfbInterface}")
	private String xfbInterface;
	// 信分宝接口地址版本号
	@Value("${xfbVersion}")
	private String xfbVersion;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PickupOrderDao pickupOrderDao;
	@Autowired
	private CouponDao couponDao;
	// 微信下载图片保存地址
	@Value("${wxDownPicUrl}")
	private String wxDownPicUrl;

	@Override
	@Transactional
	public Map<String, Object> applyToCancel(HttpSession session,
			OrderCancellation orderCancellation) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		Integer orderId = orderCancellation.getOrderId();
		Order order = orderDao.getById(orderId);
		List<OrderCancellation> listOrderCancellation = orderOperationDao
				.getByOrderIdAndType(orderId, 1);

		if (listOrderCancellation != null) {
			for (OrderCancellation a : listOrderCancellation) {
				if (a.getStatus() != 2) {
					map.put("code", 400);
					map.put("message", "申请中，无法再次申请");
					return map;
				}
			}
		}
		List<OrderCancellation> CancellationList = orderOperationDao
				.getByOrderIdAndType(orderId, 2);
		for (OrderCancellation orderCancellation2 : CancellationList) {
			if (orderCancellation2.getStatus() != 2) {
				map.put("code", 400);
				map.put("message", "申请中，无法再次申请");
				return map;
			}
		}
		if (order == null) {
			map.put("code", 404);
			map.put("message", "订单不存在");
			return map;
		}
		if (!order.getCustId().equals(custId)) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		Integer status = order.getStatus();
		PickupOrder pickupOrder = pickupOrderDao.selectByOrderId(orderId);
		if (pickupOrder != null) {
			if (status < 2 || status > 5) {
				map.put("code", 400);
				map.put("message", "订单不允许取消");
				return map;
			}
		} else if (status < 2 || status > 4) {
			map.put("code", 400);
			map.put("message", "订单不允许取消");
			return map;
		}

		String no = noService.getOrderNo();
		String reason = orderCancellation.getReason();
		Integer count = orderOperationDao.applyToCancel(orderId, custId, no,
				reason);
		if (count <= 0) {
			map.put("code", 400);
			map.put("message", "服务器异常，请稍候重试");
			return map;
		}
		map.put("code", 200);
		map.put("message", "成功");
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> cancel(HttpSession session, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		Order order = orderDao.getById(id);

		if (order == null) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}

		Integer num = orderOperationDao.selectRefundByOrderId(id);
		if (num != 0) {
			map.put("code", 400);
			map.put("message", "请勿重复操作");
			return map;
		}
		if (!order.getCustId().equals(custId)) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		
		Integer status = order.getStatus();
		if(connectedToXfb) {
			if (status >= OrderEnum.status.YZD.getKey()) {
				map.put("code", 400);
				map.put("message", "订单不允许取消");
				return map;
			}
			return fxbCancelOrder(order, map);
		} else {
			if (status >= OrderEnum.status.YFK.getKey()) {
				map.put("code", 400);
				map.put("message", "订单不允许取消");
				return map;
			}
			Integer count = orderOperationDao.cancel(id, custId);
			if (count <= 0) {
				throw new CommonException("服务器异常，请稍后重试");
			}else {
				List<OrderPayment> orderPaymentList = orderPaymentDao
						.getByOrderId(order.getId());
				if (orderPaymentList != null) {
					for (OrderPayment orderPayment : orderPaymentList) {
						if (orderPayment.getCategory() == 3
								&& orderPayment.getType() == 11) {

							CouponRecord couponRecord = couponRecordDao
									.selectByPrimaryKey(orderPayment.getSource());
							if (couponRecord == null) {
								map.put("code", 400);
								map.put("message", "优惠券信息错误， 请联系管理员");
								return map;
							} else {
								if (couponRecord.getCustId().compareTo(
										order.getCustId()) != 0) {
									map.put("code", 400);
									map.put("message", "优惠券信息错误， 请联系管理员");
									return map;
								}
								couponRecord.setIsUsed(0);
								couponRecord.setUseTime(null);
								couponRecordDao
										.updateByPrimaryKeySelective(couponRecord);
								Coupon coupon = couponDao
										.selectByPrimaryKey(couponRecord
												.getCouponId());
								coupon.setUsedNum(coupon.getUsedNum() - 1);
								couponDao.updateByPrimaryKeySelective(coupon);
							}
						}
					}
					if(order.getDownPaymentPayed() == true && order.getInstallmentPayed() == true){
						 throw new CommonException("您无权进行此操作"); 
					}
					if (order.getDownPaymentPayed() == true) {
						OrderCancellation orderCancellation = new OrderCancellation();
						orderCancellation.setOrderId(order.getId());
						orderCancellation.setNo(noService.getCancellationNo());
						orderCancellation.setCreateTime(new Date());
						orderCancellation.setType(1);
						orderCancellation.setStatus(1);
						orderCancellation.setReason("用户付款后取消订单");
						orderOperationDao.insertCancellation(orderCancellation);
						orderOperationDao.insertRefund(order.getId(),order.getDownPayment(),orderCancellation.getId());
					} 
					if (order.getInstallmentPayed() == true) {
						returnCredit(order.getCustId(), order.getStageId(), "0");
					} 
				}
				// 删除自提单
				PickupOrder pickupOrder = pickupOrderDao.selectByOrderId(order.getId());
				if (pickupOrder != null) {
					//pickupOrderDao.deleteByPrimaryKey(pickupOrder.getId());
					pickupOrder.setInvalid(1);
					pickupOrderDao.updateByPrimaryKeySelective(pickupOrder);
				}
				map.put("code", 200);
				
				if (order.getDownPaymentPayed() == true) {
					orderOperationDao.insertRefund(order.getId(),
							order.getDownPayment());
					map.put("message", "成功，退款稍候将退还");
				} else {
					map.put("message", "成功");
				}
			}
		}

		map.put("code", 200);
		map.put("message", "成功");
		return map;
	}

	/**
	 * 分信宝取消订单
	 * author:Black
	 * date:2016-08-30
	 * @param order
	 * @param map
	 * @return
	 */
	private synchronized Map<String, Object> fxbCancelOrder(Order order, Map<String, Object> map) {

		List<OrderPayment> orderPaymentList = orderPaymentDao.getByOrderId(order.getId());
		if (orderPaymentList != null) {
			for (OrderPayment orderPayment : orderPaymentList) {
				//orderPayment.getCategory() == 3 优惠
				//orderPayment.getType() == 11 代金券
				if (orderPayment.getCategory() == 3
						&& orderPayment.getType() == 11) {

					CouponRecord couponRecord = couponRecordDao
							.selectByPrimaryKey(orderPayment.getSource());
					if (couponRecord == null) {
						map.put("code", 400);
						map.put("message", "优惠券信息错误， 请联系管理员");
						return map;
					} else {
						if (couponRecord.getCustId().compareTo(
								order.getCustId()) != 0) {
							map.put("code", 400);
							map.put("message", "优惠券信息错误， 请联系管理员");
							return map;
						}
						couponRecord.setIsUsed(0);
						couponRecord.setUseTime(null);
						couponRecordDao
								.updateByPrimaryKeySelective(couponRecord);
						Coupon coupon = couponDao
								.selectByPrimaryKey(couponRecord
										.getCouponId());
						coupon.setUsedNum(coupon.getUsedNum() - 1);
						couponDao.updateByPrimaryKeySelective(coupon);
					}
				}
			}
			
			/*
			 * order.getDownPaymentPayed() 是否已支付首付，如果无首付，为 false
			 * order.getInstallmentPayed() 是否已支付分期，如果无分期，为 false
			 * author:Black
			 * date:2016-08-30
			*/
			//是否需要生成退款记录
			boolean hasRefund = true;
			BigDecimal aRefundBillAmount = BigDecimal.ZERO; // 已还账单金额
			if (order.getInstallmentPayed() == true) {//是否有分期
				String result = returnCredit(order.getCustId(), order.getStageId(), "0");
				aRefundBillAmount = new BigDecimal(result);
			}
			// 退款条件，1: 有首付 2: 已还 > 0
			hasRefund = order.getDownPaymentPayed() || aRefundBillAmount.compareTo(BigDecimal.ZERO) > 0;
			OrderCancellation orderCancellation = new OrderCancellation();
			orderCancellation.setOrderId(order.getId());
			orderCancellation.setNo(noService.getCancellationNo());
			orderCancellation.setCreateTime(new Date());
			orderCancellation.setType(3);
			// status=1: 已确认 5: 完成
			orderCancellation.setStatus(hasRefund ? 1 : 5);
			orderCancellation.setReason("用户取消订单");
			orderOperationDao.insertCancellation(orderCancellation);

			// 如果不需要退还现金，则订单直接更改为已取消
			if (!hasRefund) {
				Order tempOrder = new Order();
				tempOrder.setStatus(9);
				tempOrder.setId(order.getId());
				orderDao.updateOrder(tempOrder);
			}
			
			// 查询一下库存，然后将库存恢复
			Map<String, Object> orderSku = orderDao.getOrderSkuCount(order.getId());
			int skuCount = Integer.parseInt(String.valueOf(orderSku.get("skuCount")));
			int skuId = Integer.parseInt(String.valueOf(orderSku.get("skuId")));
			// 增加 skuCount 个库存
			orderDao.updateSkuCount(skuId, skuCount);
			// 生成退款记录，备注：现在只记录了需要退现金的情况，不包括分期（注意：光大是记录为分期的）
			if (hasRefund) {
				// 退款金额 = 已还账单金额
				BigDecimal amount = aRefundBillAmount;
				// 如果支付了首付：退款金额 = 首付金额 + 已还账单金额
				if (order.getDownPaymentPayed()) {
					amount = amount.add(new BigDecimal(String.valueOf(order.getDownPayment())));
				}
				orderOperationDao.insertRefund(order.getId(), amount, orderCancellation.getId());
				map.put("message", "成功，退款稍候将退还");
			} else {
				map.put("message", "成功");
			}
		}
		// 删除自提单
		PickupOrder pickupOrder = pickupOrderDao.selectByOrderId(order.getId());
		if (pickupOrder != null) {
			pickupOrder.setInvalid(1);
			pickupOrderDao.updateByPrimaryKeySelective(pickupOrder);
		}
		map.put("code", 200);
		return map;
	}

	/**
	 * 返还授信额度
	 * 
	 * @param custId
	 * @param stageId
	 * @param type
	 */
	private String returnCredit(Integer custId, String stageId, String type) {
		log.info("#########################################################");
		String aRefundBillAmount="";//已还账单金额
		if (connectedToXfb) {
			if (stageId == null) {
				throw new CommonException("授信账单不存在");
			}
			User user = userDao.getById(custId);
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
					aRefundBillAmount = String.valueOf(result.get("aRefundBillAmount"));
				} else {
					throw new CommonException("返还授信额度失败："
							+ result.get("resultCode") + ",错误信息："
							+ result.get("resultMessage"));
				}
			}

		}
		return aRefundBillAmount;
	}

	@Override
	public Map<String, Object> delete(HttpSession session, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		Order order = orderDao.getById(id);

		if (order == null) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		if (order.getInvalid() == true) {
			map.put("code", 404);
			map.put("message", "订单已删除");
			return map;
		}
		if (!order.getCustId().equals(custId)) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		Integer status = order.getStatus();
		if (status < 6) {
			map.put("code", 400);
			map.put("message", "订单不允许删除");
			return map;
		}

		Integer count = orderOperationDao.delete(id, custId);
		if (count <= 0) {
			map.put("code", 400);
			map.put("message", "服务器异常，请稍候重试");
			return map;
		}
		// 删除自提订单
		PickupOrder pickupOrder = pickupOrderDao.selectByOrderId(order.getId());
		if (pickupOrder != null) {
			// pickupOrderDao.deleteByPrimaryKey(pickupOrder.getId());
			pickupOrder.setInvalid(1);
			pickupOrderDao.updateByPrimaryKeySelective(pickupOrder);
		}

		map.put("code", 200);
		map.put("message", "成功");
		return map;
	}

	@Override
	public Map<String, Object> sign(HttpSession session, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		Order order = orderDao.getById(id);

		if (order == null) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		if (!order.getCustId().equals(custId)) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		Integer status = order.getStatus();
		if (status != 5) {
			map.put("code", 400);
			map.put("message", "订单不允许被签收");
			return map;
		}

		Integer count = orderOperationDao.sign(id, custId);
		if (count <= 0) {
			map.put("code", 400);
			map.put("message", "服务器异常，请稍候重试");
			return map;
		}
		map.put("code", 200);
		map.put("message", "成功");
		return map;
	}

	@Override
	public Map<String, Object> applyToReturn(HttpSession session,
			OrderCancellation orderCancellation) {

		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();

		Integer orderId = orderCancellation.getOrderId();

		Order order = orderDao.getById(orderId);
		List<OrderCancellation> listOrderCancellation = orderOperationDao
				.getByOrderIdAndType(orderId, 2);

		if (listOrderCancellation != null) {
			for (OrderCancellation a : listOrderCancellation) {
				if (a.getStatus() < 2) {
					map.put("code", 400);
					map.put("message", "申请中，无法再次申请");
					return map;
				}
			}
		}
		if (order == null) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		if (!order.getCustId().equals(custId)) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		Integer status = order.getStatus();
		if (status != 6) {
			map.put("code", 400);
			map.put("message", "没签收无法退货");
			return map;
		}
		// 增加 退货原因类型 图片集
		String no = noService.getReturnNo();
		String reason = orderCancellation.getReason();
		String pics = orderCancellation.getPics();
		Integer reasonType = orderCancellation.getReasonType();
		Integer count = orderOperationDao.applyToReturn(orderId, custId, no,
				reason, reasonType, pics);
		if (count <= 0) {
			map.put("code", 400);
			map.put("message", "服务器异常，请稍候重试");
			return map;
		}

		map.put("code", 200);
		map.put("message", "成功");
		return map;
	}

	
	/**
	 * @author wyy 2016/07/08
	 * @description 退货提交图片
	 * @param session
	 * @param imgUrl
	 * @return
	 */
	@Override
	public Map<String, Object> returnImg(HttpSession session, String mediaId) {
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
			// String wxAccessTokenUrl =
			// "http://api.weixin.qq.com/cgi-bin/media/get?access_token=DeRCoSdikXE3d--eS2q_yxHFd4HfJJ7DQBOlsocF-rpCa8kEldFpaqTHC-5rpxnmxJgSlmepUqjoAjYeMFBbFIVtlwD0VzkI-qiVUkeahqzzgCqttU-J5BwVzYAU4DSHEFPdAIALMW&media_id=TIG_f_6ZI5p4tCRteC6IpDMVBw2yfcuqenvZKXI858tBhuoPJdrv_5rkoWQjFOtJ";
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
			String relativePath = user.getMobile() + "_return_" + date + ".jpg";
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
			log.error("根据地址获得数据的字节流(异常捕获):" + e);
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
			log.error("从输入流中获取数据(异常捕获):" + e);
		}
		return outStream.toByteArray();
	}

	public List<OrderCancellation> getReturnList(HttpSession session,
			Integer offset, Integer limit) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		List<OrderCancellation> list = orderOperationDao.getReturnList(custId,
				offset, limit);
		return list;
	}

	public int getReturnListCount(HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		int pageCount = orderOperationDao.getReturnListCount(custId);
		return pageCount;
	}

	public OrderCancellation getReturnDetail(HttpSession session, Integer id) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		OrderCancellation detail = orderOperationDao
				.getReturnDetail(id, custId);
		return detail;
	}

	@Override
	public OrderCancellation selectorderOperationByOrderId(Integer orderId) {
		// TODO Auto-generated method stub
		return orderOperationDao.getOrderCancellationByOrderId(orderId);
	}

	@Override
	public void updateByprimary(OrderCancellation orderCancellation) {
		// TODO Auto-generated method stub
		orderOperationDao.updateByPrimaryKeySelective(orderCancellation);
	}
	@Transactional
	public Map<String,Object> orderReturn(HttpSession session , OrderReturn orderReturn ){
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		OrderSku orderSku = orderSkuDao.getById(orderReturn.getOrderSkuId());
		Order order = orderDao.getById(orderSku.getOrderId());
		if(order.getStatus() != 6){
			map.put("code", 400);
			map.put("message", "订单不能退货");
			return  map; 
		}
		if(orderSku ==null || order == null || !order.getCustId().equals(custId) ){
			map.put("code", 400);
			map.put("message", "订单不存在");
			return  map; 
		}
		Integer returnCount ; 
		
		if(orderSku.getReturnCount() == null){
			returnCount = 0 ; 
		}else{
			returnCount = orderSku.getReturnCount() ; 
		}
		if(orderReturn.getCount()<=0 || (returnCount+orderReturn.getCount())>orderSku.getSkuCount()){
			map.put("code", 400);
			map.put("message", "退货数量错误");
			return  map; 
		}
		//退款金额
		BigDecimal price  ; 
		
		if(orderSku.getAvgPrice() ==null){
			price = orderSku.getSkuPrice() ; 
		}else{
			price = orderSku.getAvgPrice() ; 
		}
		BigDecimal returnAmount = price.multiply(new BigDecimal(orderReturn.getCount()));
		//退货单号
		String no = noService.getReturnNo(); 
		orderReturn.setNo(no);
		orderReturn.setAvgPrice(price);
		orderReturn.setReturnAmount(returnAmount);
		orderReturn.setPics(orderReturn.getPics());
		orderReturn.setStatus(0);
		orderReturnDao.insertSelective(orderReturn);
		//添加退货数量
		orderSkuDao.updateReturnCount(orderReturn.getOrderSkuId() ,orderReturn.getCount());
		map.put("code", 200);
		map.put("message", "成功");
		return map;
	}
	
	public SearchResult<Map<String, Object>> selectReturnsListByParmas(
			Map<String, Object> params) {
		if (params.get("limit") != null && params.get("limit") != "") {
			params.put("limit",
					Integer.parseInt(String.valueOf(params.get("limit"))));
		}
		if (params.get("offset") != null && params.get("offset") != "") {
			params.put("offset",
					Integer.parseInt(String.valueOf(params.get("offset"))));
		}
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		searchResult.setRows(orderReturnDao.selectList(params));
		searchResult.setTotal(orderReturnDao.selectCount(params));
		return searchResult;
	}

	@Transactional
	public Map<String, Object> cancelReturns(Integer custId, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		OrderReturn orderReturn = orderReturnDao.selectByPrimaryKey(id);
		if(orderReturn == null){
			map.put("code", 400);
			map.put("message", "退货订单不存在");
			return map ; 
		}else{
			if(!orderReturn.getStatus().equals(0)){
				//不是申请 中 不能取消申请
				map.put("code", 400);
				map.put("message", "不能取消申请");
				return map ; 
			}
			OrderSku orderSku =  orderSkuDao.getById(orderReturn.getOrderSkuId());
			Order order = orderDao.getById(orderSku.getOrderId());
			if(!order.getCustId().equals(custId)){
				map.put("code", 400);
				map.put("message", "退货订单不存在");
				return map ; 
			}
			orderReturn.setStatus(1);
			orderReturnDao.updateByPrimaryKey(orderReturn);
			orderSkuDao.updateReturnCount(orderReturn.getOrderSkuId() ,-orderReturn.getCount());
			map.put("code", 200);
		}
		return map;
	}
}