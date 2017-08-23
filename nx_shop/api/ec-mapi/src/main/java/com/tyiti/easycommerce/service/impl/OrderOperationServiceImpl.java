package com.tyiti.easycommerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.service.NoService;
import com.tyiti.easycommerce.entity.Coupon;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.CouponDao;
import com.tyiti.easycommerce.repository.CouponRecordDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderOperationDao;
import com.tyiti.easycommerce.repository.OrderPaymentDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.service.OrderOperationService;

@Service
public class OrderOperationServiceImpl implements OrderOperationService {

	@Autowired
	private OrderOperationDao orderOperationDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private NoService noService;
	@Autowired
	private CouponRecordDao couponRecordDao;
	@Autowired
	private OrderPaymentDao  orderPaymentDao;
	@Autowired
	OrderSkuDao orderSkuDao;
	@Autowired
	private CouponDao couponDao ;
	@Override
	@Transactional
	public Map<String, Object> applyToCancel(OrderCancellation orderCancellation) {
		Map<String, Object> map = new HashMap<String, Object>();
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
		
		List<OrderCancellation>  CancellationList = orderOperationDao
				.getByOrderIdAndType(orderId, 2);
		for (OrderCancellation orderCancellation2 : CancellationList) {
			if(orderCancellation2.getStatus()!=2){
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
		List<OrderSku> orderSkuList = orderSkuDao.getByOrderId(order.getId());
		Integer status = order.getStatus();
		for(OrderSku orderSku:orderSkuList){
			if(!orderSku.getSkuErpCode().equals("koo")){
				if (status < 2 || status > 5) {
					map.put("code", 400);
					map.put("message", "订单不允许取消");
					return map;
				}
			}
		}
		
		

		String no = noService.getOrderNo();
		String reason = orderCancellation.getReason();
		Integer count = orderOperationDao.applyToCancel(orderId, no,reason);
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
		if (!order.getCustId().equals(custId)) {
			map.put("code", 404);
			map.put("message", "无效的订单");
			return map;
		}
		Integer status = order.getStatus();
		if (status >= 2) {
			map.put("code", 400);
			map.put("message", "订单不允许取消");
			return map;
		}

		Integer count = orderOperationDao.cancel(id, custId);

		if (count <= 0) {
			map.put("code", 400);
			map.put("message", "服务器异常，请稍候重试");
			return map;
		} else {
			List<OrderPayment> orderPaymentList = orderPaymentDao.getByOrderId(order.getId());
			if(orderPaymentList!=null){
				for (OrderPayment orderPayment : orderPaymentList) {
					if(orderPayment.getCategory() == 3 && orderPayment.getType()==11){
						
						CouponRecord couponRecord = couponRecordDao
								.selectByPrimaryKey(orderPayment.getSource());
						if (couponRecord == null) {
							map.put("code", 400);
							map.put("message", "优惠券信息错误， 请联系管理员");
							return map;
						}else{
							if(couponRecord.getCustId().compareTo(order.getCustId())!=0){
								map.put("code", 400);
								map.put("message", "优惠券信息错误， 请联系管理员");
								return map;
							}
							couponRecord.setIsUsed(0);
							couponRecord.setUseTime(null);
							couponRecordDao.updateByPrimaryKeySelective(couponRecord);
							Coupon coupon = couponDao.selectByPrimaryKey(couponRecord.getCouponId());
							coupon.setUsedNum(coupon.getUsedNum()-1);
							couponDao.updateByPrimaryKeySelective(coupon);
						}
					}
				}
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

		return map;
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
		if(order.getInvalid() == true){
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
		String no = noService.getReturnNo();
		String reason = orderCancellation.getReason();
		Integer count = orderOperationDao.applyToReturn(orderId, custId, no,
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


}