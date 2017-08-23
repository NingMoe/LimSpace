package com.tyiti.easycommerce.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.OrderReturn;
import com.tyiti.easycommerce.entity.Refund;
import com.tyiti.easycommerce.repository.OrderCancellationDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderReturnDao;
import com.tyiti.easycommerce.repository.PickupOrderDao;
import com.tyiti.easycommerce.repository.RefundDao;
import com.tyiti.easycommerce.service.RefundService;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.exception.OrderStatusException;

@Service("refundService")
public class RefundServiceImpl  implements RefundService{

	@Autowired
	private RefundDao  refundDao ; 
	
	@Autowired
	private OrderDao orderDao ; 
	
	@Autowired
	private OrderCancellationDao orderCancellationDao ; 
	
	@Autowired
	private PickupOrderDao pickupOrderDao;
	
	@Autowired
	private OrderReturnDao orderReturnDao ; 
	
	@Override
	public SearchResult<Map<String, Object>> getRefundList(
			Map<String, Object> param) {
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		searchResult.setRows(this.refundDao.selectRefundList(param));
		searchResult.setTotal(this.refundDao.selectRefundCount(param));
		 return searchResult;
	}
	@Override
	public Map<String, Object> getRefundDetail(Integer id) {
		return this.refundDao.getRefundDetail(id);
	}
	
	@Transactional
	public void refundMakeSure(int id) {
		//1.判断是否具备退款条件
		Map<String,Object> refundMap= refundDao.selectRefundWithAllStatus(id);
		if(!refundMap.get("status").equals(0)){
			throw new OrderStatusException("订单号："+refundMap.get("orderNo")+";退款状态为："+refundMap.get("statusText")+",无法执行退款确定操作");
		}
		int orderId = Integer.parseInt(String.valueOf(refundMap.get("orderId")));
		int cancellationId = Integer.parseInt(String.valueOf(refundMap.get("cancellationId")));
		String orderNo = String.valueOf(refundMap.get("orderNo"));
		if((Integer.parseInt(String.valueOf(refundMap.get("cancelType")))==1 || Integer.parseInt(String.valueOf(refundMap.get("cancelType")))==3) && Integer.parseInt(String.valueOf(refundMap.get("cancelStatus")))==1){
			//退货表 ：1) type=1 申请取消 ; status =1 同意  2) 此时更改状态为 status= 5 完成
			int orderNum = orderDao.orderCancel(orderId);
			if(orderNum !=1 ){
				//修改订单失败
				throw new OrderStatusException("退款失败，订单取消失败，订单号："+orderNo);
			}
			LogUtil.log(orderId, LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.QUXIAO.getKey(), LogEnum.Action.QUXIAO.getValue(), LogEnum.Source.PLAT.getKey(), 1);
			OrderCancellation orderCancellation = new OrderCancellation();
			orderCancellation.setId(cancellationId);
			orderCancellation.setStatus(5);
			int orderCancellationNum = orderCancellationDao.updateByPrimaryKeySelective(orderCancellation);
			if(orderCancellationNum!=1){
				//修改退货表失败
				throw new OrderStatusException("退货完成失败：订单号为："+orderNo);
			}
			
		}else if(Integer.parseInt(String.valueOf(refundMap.get("cancelType")))==2&&Integer.parseInt(String.valueOf(refundMap.get("cancelStatus")))==4){
			//1)退货表 type=2 申请退货 status =4 已收货  2)此时更改状态为 status= 5 完成 3) 修改order 状态status=8
			OrderCancellation orderCancellation = new OrderCancellation();
			orderCancellation.setId(cancellationId);
			orderCancellation.setStatus(5);
			int orderCancellationNum = orderCancellationDao.updateByPrimaryKeySelective(orderCancellation);
			if(orderCancellationNum!=1){
				throw new OrderStatusException("退货完成失败：订单号为："+orderNo);
			}
			orderDao.orderFinish(orderId);
			LogUtil.log(orderId, LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.ORDERRETURN.getKey(),LogEnum.Action.ORDERRETURN.getValue() , LogEnum.Source.PLAT.getKey(), 1);
		}else{
			throw new OrderStatusException("退款失败：订单号为："+orderNo);
		}
		this.refundDao.refundMakeSure(id);
//		PickupOrder pickupOrder = pickupOrderDao.selectByOrderId(orderId);
//		if( pickupOrder != null){
//			//将自提单状态改为退款完成
//			pickupOrder.setStatus(8);
//			pickupOrderDao.updateByPrimaryKeySelective(pickupOrder);
//		}
		LogUtil.log(id, LogEnum.OperateModel.REFUND.getKey(), LogEnum.Action.REFUND.getKey(),LogEnum.Action.REFUND.getValue(), LogEnum.Source.PLAT.getKey(), 1);

	}
	@Override
	public SearchResult<Map<String, Object>> selectRefundsSkusList(
			Map<String, Object> param) {

		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		searchResult.setRows(this.refundDao.selectRefundsSkusList(param));
		searchResult.setTotal(this.refundDao.selectRefundsSkusCount(param));
		 return searchResult;
	}
	@Transactional
	public Map<String, Object> refundMake(int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Refund refund = refundDao.selectByPrimaryKey(id);
		if(null!=refund){
			if(refund.getStatus() !=0){
				map.put("code", 400);
				map.put("message", "该单已经退款");
			}else{
				refund.setStatus(1);//退款成功
				refundDao.updateByPrimaryKeySelective(refund);
				if(refund.getRefundType() == 1){//取消订单
					orderDao.orderCancel(refund.getOrderId());//订单状态改为9
					OrderCancellation orderCancellation = new OrderCancellation();
					//取消订单状态改为5  完成
					orderCancellation.setId(refund.getCancellationId());
					orderCancellation.setStatus(5);
					orderCancellationDao.updateByPrimaryKeySelective(orderCancellation);
				}else if(refund.getRefundType() ==2){//退货
					OrderReturn orderReturn = new OrderReturn();
					orderReturn.setId(refund.getCancellationId());
					orderReturn.setStatus(5);//完成
					orderReturnDao.updateByPrimaryKeySelective(orderReturn);
					//如果可退货数量为0  那么订单状态 改为 8  
					Integer orderId = refund.getOrderId() ; 
					Integer returnCount = orderReturnDao.selectReturnCount(orderId );
					Integer buyCount = orderReturnDao.selectBuyCount(orderId );
					if(returnCount.equals(buyCount) && returnCount!=0 ){
						Order order = new Order();
						order.setId(orderId);
						order.setStatus(8);
						orderDao.updateStatus(order);
					}
				}
			}
			map.put("code", 200);
			map.put("messsge", "OK");
		}else{
			map.put("code", 400);
			map.put("message", "退款不存在");
		}
		
		return map ; 
	}


}
