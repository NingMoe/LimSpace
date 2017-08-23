package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.Refund;
import com.tyiti.easycommerce.repository.OrderCancellationDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.RefundDao;
import com.tyiti.easycommerce.service.CancellationService;
import com.tyiti.easycommerce.util.LogUtil;

@Service("cancellationService")
public class CancellationServiceImpl implements CancellationService {

	private static Log log = LogFactory.getLog(CancellationServiceImpl.class);
	@Autowired
	private OrderCancellationDao orderCancellationDao;

	@Autowired
	private RefundDao refundDao;

	@Autowired
	private OrderDao orderDao;


	// 信分宝接口地址
	@Value("${xfbInterface}")
	private String xfbInterface;
	// 信分宝接口地址版本号
	@Value("${xfbVersion}")
	private String xfbVersion;
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

 

	@Override
	@Transactional
	public Map<String, Object> orderCancel(Integer id, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		OrderCancellation orderCancellation = orderCancellationDao
				.selectByPrimaryKey(id);
		if (orderCancellation == null) {
			map.put("code", 400);
			map.put("message", "取消单不存在");
			return map;
		}
		if (orderCancellation.getStatus() != 0) {
			map.put("code", 400);
			map.put("message", "当前状态不允许进行此操作");
			return map;
		}
		if (status == 1) {// 取消订单同意操作
			Map<String,Object> param =  new HashMap<String, Object>();
			param.put("id", orderCancellation.getOrderId());
			// 查询一下库存，然后将库存恢复 多条记录 需要退回
			List<Map<String, Object>> orderSkusList = orderDao.selectOrderSkusList(param); 
			Order order = (Order) orderSkusList.get(0);
			List<OrderSku> orderSkus = order.getOrderSku() ; 
			for (OrderSku orderSku : orderSkus) {
				orderDao.updateSkuCount(orderSku.getSkuId(), orderSku.getSkuCount());
			}
			Integer refundType = 1 ; // 付款后取消订单
			createSkusRefund(id, order,refundType);
			LogUtil.log(id, LogEnum.OperateModel.CANCEL.getKey(),
					LogEnum.Action.TONGYIQUXIAO.getKey(),
					LogEnum.Action.TONGYIQUXIAO.getValue(),
					LogEnum.Source.PLAT.getKey(), 1);
		}
		if (status == 2) {// 取消订单拒绝
			LogUtil.log(id, LogEnum.OperateModel.CANCEL.getKey(),
					LogEnum.Action.JUJUETUIHUO.getKey(),
					LogEnum.Action.JUJUETUIHUO.getValue(),
					LogEnum.Source.PLAT.getKey(), 1);
		}
		orderCancellation.setStatus(status);
		this.orderCancellationDao
				.updateByPrimaryKeySelective(orderCancellation);
		map.put("code", 200);
		return map;
	}

	/**
	 * @Title: createSkusRefund
	 * @Description: 生成退款记录
	 * @return void 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	private void createSkusRefund(int id, Order order ,Integer  refundType) {
		/**
		 * update 2016-05-18 shenzhiqiang 生成退款记录之前修改t_order表的cancel_time字段
		 */
		this.orderCancellationDao.cancel(id);
		 
		 
		Refund refund = new Refund();
		refund.setCreateTime(new Date());
		// 分期金额
		refund.setAmount(order.getInstallmentAmount());
		// 全分期
		refund.setType(3);
		// 订单id
		refund.setOrderId(order.getId());
		refund.setCancellationId(id);
		refund.setRefundType(refundType);
		// 状态 未退款
		refund.setStatus(0);
		refundDao.insertSelective(refund);
	}
}
