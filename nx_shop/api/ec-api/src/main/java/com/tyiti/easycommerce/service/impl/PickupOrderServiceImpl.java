package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderOperationDao;
import com.tyiti.easycommerce.repository.PickupOrderDao;
import com.tyiti.easycommerce.repository.PickupPointDao;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.service.PickupOrderService;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.xml.XmlUtil;
import com.tyiti.easycommerce.util.xml.entity.CSubmitState;
@Service
public class PickupOrderServiceImpl implements PickupOrderService {
	@Autowired
	private PickupOrderDao pickupOrderDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderOperationDao orderOperationDao;
	
	@Autowired
	private UserDao userDao;
	
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
	
	

	@Override
	public Integer selectCodeNum(String code) {
		// TODO Auto-generated method stub
		return pickupOrderDao.selectCodeNum(code);
	}

	@Override
	public PickupOrder selectByOrderId(Integer id) {
		// TODO Auto-generated method stub
		return pickupOrderDao.selectByOrderId(id);
	}

	@Override
	public List<PickupOrder> selectByPickupPointId(Integer pickupPointId) {
		// TODO Auto-generated method stub
		List<PickupOrder> list=pickupOrderDao.selectByPickupPointId(pickupPointId);
		return list;
	}

	@Override
	public List<PickupOrder> findbyStatus(PickupOrder pickupOrder) {
		List<PickupOrder> list=pickupOrderDao.findByPickupOrderStatus(pickupOrder);
		return list;
	}

	

	@Override
	public SearchResult<Map<String, Object>> pickupOrderLists(
			Map<String, Object> param) {
		//Integer status=10;
		
//		if(String.valueOf(param.get("code")).length()>6){
//			param.remove("code");
//			param.put("orderNo", param.get("code"));
//		}
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		
		searchResult.setRows(pickupOrderDao.getListPickupOrder(param));
		searchResult.setTotal(pickupOrderDao.selectAllPickupOrder(param));
		
		return searchResult;
	}

	@Override
	public PickupOrder selectPickupOrderByCode(String code) {
		// TODO Auto-generated method stub
		return pickupOrderDao.selectByCode(code);
	}

	@Override
	public PickupOrder getOrderDetail(Integer id, Integer sessionPickUpId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PickupOrder selectPickupOrderById(Integer id) {
		// TODO Auto-generated method stub
		return pickupOrderDao.selectByPrimaryKey(id);
	}

	@Override
	public PickupOrder getLastPickupOrderByCustId(Integer custId) {
		// TODO Auto-generated method stub
		return pickupOrderDao.getListPickupOrderByCustId(custId);
	}

	@Override
	public void updateStus(PickupOrder record) {
		// TODO Auto-generated method stub
		pickupOrderDao.updateByPrimaryKeySelective(record);
		
	}
	@Transactional
	public void changeStatus(Integer id,Integer status,String code) throws Exception {
		
		PickupOrder pickupOrder = pickupOrderDao.selectByPrimaryKey(id);
		Order order = orderDao.getById(pickupOrder.getOrderId());
		OrderCancellation orderCancellation=orderOperationDao.getOrderCancellationByOrderId(pickupOrder.getOrderId());
		User user=userDao.getById(order.getCustId());
		if(status != 2 && status != 3 && status != 5 && status != 6){
			throw new Exception("状态异常！"); // 不在可操作状态范围内
		}
		if(status==2){
			if(pickupOrder.getStatus()!=1){
				throw new Exception("未付款不能收货");
			}
			if(order.getStatus()!=5){
				throw new Exception("订单未发货不能收货");
			}
				pickupOrder.setStatus(status);
				pickupOrder.setTaketime(new Date());
				pickupOrder.setArrivetime(new Date());
				pickupOrderDao.updateByPrimaryKeySelective(pickupOrder);	

				
				//String template = SpringPropertiesUtil.getProperty("sms.content."+ type);
				if(orderCancellation!=null){
					if(orderCancellation.getType()==1 && (orderCancellation.getStatus()==0||orderCancellation.getStatus()==1)){
					}else{
						String randStr=pickupOrder.getCode();
						PickupPoint pickupPoint=pickupPointDao.selectByPrimaryKey(pickupOrder.getPickupPointId());
						String message=("主人，您的提货码为"+randStr+"的商品已到达"+pickupPoint.getName()+"自提点,"+pickupPoint.getOpenTime()+"时间凭提货码即可领取。记得把我领回家哟，么么哒~").replaceAll(" ", "");
						sendSmsForVerifyCode(user.getMobile(), message);
					}	
				}else{
				String randStr = pickupOrder.getCode();
				PickupPoint pickupPoint = pickupPointDao
						.selectByPrimaryKey(pickupOrder.getPickupPointId());
				String message = ("主人，您的提货码为" + randStr + "的商品已到达"
						+ pickupPoint.getName() + "自提点,"
						+ pickupPoint.getOpenTime() + "时间凭提货码即可领取。记得把我领回家哟，么么哒~")
						.replaceAll(" ", "");
				sendSmsForVerifyCode(user.getMobile(), message);
				}
				return;
				
		}
		//前端传的是待自提  3 后端改成 已自提  t_order 改成已经签收  （点提货按钮）
		if(status==3){
			if(pickupOrder.getStatus()!=2){
				throw new Exception("货物未到自提点或货物还未发货不能签收");
			}if(orderCancellation!=null){
				if(orderCancellation.getType()==1 && (orderCancellation.getStatus()==0||orderCancellation.getStatus()==1)){
					throw new Exception("该订单正在取消中！");
				}
			}
			if(code==null||"".equals(code)){
				throw new Exception("提货码不能为空");
			}
			if(!pickupOrder.getCode().equals(code)){
				throw new Exception("提货码不匹配！");
			}
			
			pickupOrder.setStatus(status);
			pickupOrder.setTaketime(new Date());
			pickupOrder.setArrivetime(new Date());
			order.setStatus(6);
		}
		if(status==5){
			if(pickupOrder.getStatus()!=4){
				throw new Exception("不是退货中 不能拒绝退货！");	
			}
			pickupOrder.setStatus(3);
			orderCancellation.setStatus(2);//已经拒绝
		}
		if(status==6){
			if(pickupOrder.getStatus()!=4){
				throw new Exception("不是退货状态 不能退货！");
			}
			pickupOrder.setStatus(status);
		}
		pickupOrderDao.updateByPrimaryKeySelective(pickupOrder);
		orderDao.updateOrder(order);
		if(orderCancellation!=null){
			orderOperationDao.updateByPrimaryKeySelective(orderCancellation);
		}
		
	}
	
	private Map<String, Object> sendSmsForVerifyCode(String mobile, String verifyCode) {
		Map<String, String> requestParams = new HashMap<String, String>();
		Map<String, Object> ret = new HashMap<String, Object>();
		requestParams.put("sname", sname);
		requestParams.put("spwd", spwd);
		requestParams.put("scorpid", scorpid);
		requestParams.put("sprdid", sprdid);
		requestParams.put("sdst", mobile);
		requestParams.put("smsg", tail+verifyCode );

		// 解析返回的 XML
		String responseXml = HttpClientUtil
				.httpGet(smsUrl, null, requestParams);
		CSubmitState responseData = (CSubmitState) XmlUtil.fromXml(responseXml,
				CSubmitState.class);
		// 解析 XML 失败
		if (responseData == null) {
			ret.put("state", 1);
			ret.put("message", "解析 XML 失败");
			return ret;
		}

		ret.put("state", (int) responseData.getState());
		ret.put("message",
				responseData.getState() + ": " + responseData.getMsgState());
		return ret;
		
	}

	

}
