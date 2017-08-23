package com.tyiti.easycommerce.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.RushBuy;
import com.tyiti.easycommerce.entity.RushBuyAppoint;
import com.tyiti.easycommerce.entity.RushBuySku;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.RushBuyAppointDao;
import com.tyiti.easycommerce.repository.RushBuyDao;
import com.tyiti.easycommerce.repository.RushBuySkuDao;

/**
 * 秒杀相关的业务处理程序
 * @author rainyhao
 * @since 2016-3-30 下午6:56:50
 */
@Service
public class RushBuyService {
	
	// 秒杀活动信息的数据访问程序
	@Autowired
	private RushBuyDao rushBuyDao;
	
	// 秒杀活动所关联的商品信息的数据访问程序
	@Autowired
	private RushBuySkuDao rushBuySkuDao;
	
	// 秒杀预约信息的数据访问程序
	@Autowired
	private RushBuyAppointDao rushBuyAppointDao;
	
	// 订单所买到的商品相关的数据访问程序
	@Autowired
	private OrderSkuDao orderSkuDao;

	/**
	 * 获取当前可见的秒杀活动列表
	 * @authro rainyhao
	 * @since 2016-3-30 下午7:00:22
	 * @return
	 */
	public List<RushBuy> getVisibleList() {
		RushBuy entity = new RushBuy();
		// 取当前时间之前1小时
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		// 活动结束后1小时仍然可以看到这个秒杀活动
		// 所以就以库中的结束时间大于当前时间减1小时来判定
		// entity.setEndTimeGT(cal.getTime());
		entity.setInvalid(false);
		return rushBuyDao.selectForList(entity);
	}
	
	/**
	 * 获取指定秒杀活动的所有商品
	 * @authro rainyhao
	 * @since 2016-4-5 上午10:33:05
	 * @param rushBuyId 指定的秒杀活动
	 * @return
	 */
	public List<RushBuySku> getSkuListByRushBuy(Integer rushBuyId) {
		RushBuySku entity = new RushBuySku();
		entity.setRushBuyId(rushBuyId);
		return rushBuySkuDao.selectForList(entity);
	}
	
	/**
	 * 检查可参与秒杀的先决条件
	 * @authro rainyhao
	 * @since 2016-3-31 上午11:32:55
	 * @param id 秒杀活动id
	 * @param skuId 秒此次活动的哪个商品
	 * @param custId 请求用户id
	 * @param skuCount 此次要秒杀的数量
	 * @return
	 */
	public Map<String, Object> checkPrerequisite(Integer id, Integer skuId, Integer custId, Integer skuCount) {
		Map<String, Object> res = new HashMap<String, Object>(); // 操作结果
		// 校验当前活动, (是否存在, 是否已开始/结束, 是否需要预约) 
		RushBuy rushBuy = rushBuyDao.getById(id);
		if (null == rushBuy) { // 检查活动是否存在
			res.put("code", 404);
			res.put("message", "活动不存在");
			return res;
		}
		if (rushBuy.getInvalid()) { // 标记为已删除
			res.put("code", 404);
			res.put("message", "活动不存在");
			return res;
		}
		Date now = new Date(); // 取当前时间作校验
		if (rushBuy.getStartTime().getTime() > now.getTime()) { // 活动还没开始
			res.put("code", 403); // 不允许
			res.put("message", "秒杀活动还未开始,不要太心急哦");
			return res;
		}
		if (now.getTime() > rushBuy.getEndTime().getTime()) { // 活动已结束
			res.put("code", 403); // 不允许
			res.put("message", "活动结束啦,请期待下次吧");
			return res;
		}
		// 检查要秒的商品是否存在
		RushBuySku skuEntity = new RushBuySku();
		skuEntity.setRushBuyId(id); // 此次秒杀活动
		skuEntity.setSkuId(skuId); // 要秒些次活动的商品
		RushBuySku sku = rushBuySkuDao.selectForObject(skuEntity); // 加载商品
		if (null == sku) { // 商品不存在
			res.put("code", 404); // 还秒个啥
			res.put("message", "商品不存在");
			return res;
		}
		if (sku.getLeftSku().intValue() == 0) { // 优先校验一下库存,如果没了,其他的也没必要校验了
			res.put("code", 404);
			res.put("message", "商品已被秒完");
			return res;
		}
		if (rushBuy.getNeedAppoint()) { // 如果这个活动是需要预约的, 检查是否预约过
			RushBuyAppoint entity = new RushBuyAppoint();
			entity.setCustId(custId); // 当前此用户
			entity.setRushBuyId(id); // 预约的此活动
			// 查是否预约过
			RushBuyAppoint appointed = rushBuyAppointDao.selectForObject(entity);
			if (null == appointed) {
				res.put("code", 403); // 不允许
				res.put("message", "很抱歉,您没有预约此活动,不能参与秒杀");
				return res;
			}
		}
		// 检查是否秒过
		OrderSku orderSku = new OrderSku();
		orderSku.setCustId(custId); // 此用户
		orderSku.setActivityType(1); // 1表示秒杀
		orderSku.setActivityId(id); // 参与此次秒杀
		orderSku.setSkuId(skuId); // 所秒到的商品
		Integer rushedSku = orderSkuDao.count(orderSku);
		if (null != rushedSku && rushedSku.intValue() > 0) { // 之前秒过
			if (sku.getAvailableTimes().intValue() <= rushedSku.intValue()) { // 有过多少订单就证明参与过多少次
				res.put("code", 400);
				res.put("message", "您已达到此次活动的参与次数上限,留一些机会给其他人吧");
				return res;
			}
			// 检查此次活动秒到的总数量是达到活动规定上限
			Integer boughtTotal = orderSkuDao.sumOrderSkuTotal(orderSku);
			if (null != boughtTotal && boughtTotal.intValue() > 0) { // 总数大于0的前提下继续检查
				res.put("code", 400);
				if (boughtTotal.intValue() >= sku.getAvailableCount().intValue()) { // 已买到的数量达到活动上限
					res.put("message", "您已达到此次活动购买数量上限,留一些机会给其他人吧");
					return res;
				}
				if (boughtTotal.intValue() + skuCount.intValue() > sku.getAvailableCount().intValue()) { // 已买到的数量未达到活动上限但此次要秒的数量与之前已秒到的加起来数量超过活动上限
					res.put("message", "您只能购买" + (sku.getAvailableCount().intValue() - boughtTotal.intValue()) + "个了");
					return res;
				}
			}
		}
		res.put("code", 200);
		res.put("message", "OK");
		return res;
	}
	
	/**
	 * 给指定的秒杀活动减库存
	 * @authro rainyhao
	 * @since 2016-3-31 下午6:33:59
	 * @param id 秒杀活动id
	 * @param skuId 些次要做减库存的商品
	 * @param toDecrease 要减的数量
	 */ 
	public Map<String, Object> decreaseLeftSku(Integer id, Integer skuId, Integer toDecrease) {
		// 操作结果
		Map<String, Object> res = new HashMap<String, Object>();
		// 检查要秒的商品是否存在
		RushBuySku skuEntity = new RushBuySku();
		skuEntity.setRushBuyId(id); // 此次秒杀活动
		skuEntity.setSkuId(skuId); // 要秒些次活动的商品
		RushBuySku sku = rushBuySkuDao.selectForObject(skuEntity); // 加载商品
		if (null == sku) {
			res.put("code", 404);
			res.put("message", "商品不存在");
			return res;
		}
		// 检查一下库存是否够
		if (sku.getLeftSku().intValue() < toDecrease.intValue()) {
			res.put("code", 400);
			res.put("message", "库存不足");
			return res;
		}
		// 执行更新
		int rowsAffected = rushBuySkuDao.updateLeftSkuAsDecrease(id, skuId, toDecrease);
		if (0 == rowsAffected) {
			res.put("code", 400);
			res.put("message", "库存不足");
			return res;
		}
		res.put("code", 200);
		res.put("message", "OK");
		return res;
	}

	 /**
	  * <p>功能描述：根据活动类型查找活动ID。</p>	
	  * @param activityId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月19日 下午5:29:17。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	public List<ActivitySku> findByActivityList(Integer activityId) {
		// TODO Auto-generated method stub
		return null;
	}
}