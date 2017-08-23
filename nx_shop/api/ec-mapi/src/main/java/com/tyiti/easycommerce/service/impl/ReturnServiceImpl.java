package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.OrderReturn;
import com.tyiti.easycommerce.entity.Refund;
import com.tyiti.easycommerce.repository.OrderReturnDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.RefundDao;
import com.tyiti.easycommerce.service.ReturnService;

@Service("orderReturnService")
public class ReturnServiceImpl implements ReturnService {

	@Autowired
	private OrderReturnDao orderReturnDao;
	
	@Autowired
	private RefundDao refundDao;
	
	@Autowired
	private OrderSkuDao orderSkuDao ; 
	
	@Override
	public SearchResult<Map<String, Object>> selectListByParmas(
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

	@Override
	public OrderReturn selectReturnDetail(Integer id) {
		// TODO Auto-generated method stub
		OrderReturn orderReturn = orderReturnDao.selectByPrimaryKey(id);
		return orderReturn;
	}

	@Override
	@Transactional
	public Map<String, Object> updateStatus(Integer id, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		OrderReturn orderReturn = orderReturnDao.selectByPrimaryKey(id);
		if (orderReturn == null) {
			map.put("code", 400);
			map.put("message", "id 不存在");
			return map;
		}
		if (status == 2) {
			// 拒绝操作 必须 退货单状态是0
			if (orderReturn.getStatus() != 0) {
				map.put("code", 400);
				map.put("message", "无权操作");
				map.put("exception", "状态为" + orderReturn.getStatus()+",不能执行取消操作");
				return map;
			}
			//修改退货数量
			orderSkuDao.updateReturnCount(orderReturn.getOrderSkuId() ,-orderReturn.getCount());
		}
		if (status == 3) // 确认操作 必须 退货单状态是0
			if (orderReturn.getStatus() != 0) {
				map.put("code", 400);
				map.put("message", "无权操作");
				map.put("exception", "status=" + orderReturn.getStatus());
				return map;
			}
		if (status == 4) // 收货操作 必须 退货单状态是3
			if (orderReturn.getStatus() != 3) {
				map.put("code", 400);
				map.put("message", "无权操作");
				map.put("exception", "status=" + orderReturn.getStatus());
				return map;
			} else {
//				// 退货数量改回去
//				orderSkuDao.updateReturnCount(orderReturn.getOrderSkuId() ,-orderReturn.getCount());
				// 生成退款记录 付款后 取消 或者 发货后 退货 加入一个return_type
				Refund refund = new Refund();
				refund.setOrderId(orderReturn.getOrderId());
				refund.setCreateTime(new Date());
				refund.setCancellationId(id);
				refund.setAmount(orderReturn.getReturnAmount());
				refund.setRefundType(2);// 退货
				refund.setStatus(0);
				refund.setType(1);
				refundDao.insertSelective(refund);
			}
		orderReturn.setStatus(status);
		orderReturnDao.updateByPrimaryKeySelective(orderReturn);
		map.put("code", 200);
		return map;
	}

	@Override
	public Integer selectReturnCountByOrderSkuId(Integer orderSkuId) {
		// TODO Auto-generated method stub
		return orderReturnDao.selectReturnCountByOrderSkuId(orderSkuId);
	}

}
