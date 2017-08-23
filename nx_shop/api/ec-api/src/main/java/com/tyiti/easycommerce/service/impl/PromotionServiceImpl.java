/**  
 * @Title: PromotionServiceImpl222.java
 * @Package com.tyiti.easycommerce.service.impl
 * @Description: TODO
 * @author lmh
 * @date 2016-3-24
 * @version v1.0
 */
package com.tyiti.easycommerce.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Promotion;
import com.tyiti.easycommerce.entity.PromotionPrize;
import com.tyiti.easycommerce.entity.PromotionRaffle;
import com.tyiti.easycommerce.repository.PromotionDao;
import com.tyiti.easycommerce.repository.PromotionPrizeDao;
import com.tyiti.easycommerce.repository.PromotionRaffleDao;
import com.tyiti.easycommerce.service.CouponService;
import com.tyiti.easycommerce.service.PromotionService;

/**
 * @ClassName: PromotionServiceImpl222
 * @Description: TODO
 * @author lmh
 * @date 2016-3-24 上午11:42:33
 */
@Service
public class PromotionServiceImpl implements PromotionService {

	private static Log logger = LogFactory.getLog(PromotionServiceImpl.class);

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private PromotionDao promotionDao;
	@Autowired
	private PromotionPrizeDao promotionPrizeDao;
	@Autowired
	private PromotionRaffleDao promotionRaffleDao;
	@Autowired
	private CouponService couponService;
	
	@Value("${lotteryActivityId}")
	private Integer lotteryActivityId;  //抽奖活动id
	
	@Value("${lotterySkuId}")
	private Integer lotterySkuId;  //抽奖活动sku

	/**
	 * <p>
	 * Title: saveRaffle
	 * </p>
	 * <p>
	 * Description:抽奖
	 * </p>
	 * 
	 * @param userId
	 *            用户id
	 * @param promotionId
	 *            活动id
	 * @return map
	 */
	@Override
	public Map<String, Object> saveRaffle(Integer id, Integer userId) {

		logger.info("抽奖活动开始--------service---------->>活动id-->>" + id
				+ "-->用户userId--->>" + userId);

		Map<String, Object> map = new HashMap<String, Object>();

		Promotion promotion = promotionDao.selectByPrimaryKey(id);
		// 没找到活动
		if (promotion == null) {
			map.put("code", 1);
			map.put("message", "无此活动");
			return map;
		}

		Date expireTime = promotion.getExpireTime();
		if (expireTime == null) {
			// 无失效期
		} else {
			Date now = new Date();
			// 活动已过期
			if (expireTime.before(now)) {
				map.put("code", 2);
				map.put("message", "活动已过期");
				return map;
			}
		}

		// 抽奖记录
		PromotionRaffle promotionRaffle = new PromotionRaffle();
		promotionRaffle.setPrizeId(null);
		promotionRaffle.setPromotionId(id);
		promotionRaffle.setUserId(userId);
		int rand = (int) (Math.random() * 100) + 1;
		// 计算剩余抽检次数 中奖 次数 必中次数
		// 中奖次数
		Integer prizeCount = promotionRaffleDao.getPrizeCount(id, userId,
				promotion.getType()).getPrizeCount();
		// 剩余次数
		PromotionRaffle promotionraffle = promotionRaffleDao.getLotteryCount(
				id, userId, promotion.getType());
		// 可抽奖次数
		Integer times = promotionraffle.getTimes();
		// 已经抽奖次数
		Integer raffleCount = promotionraffle.getRaffleCount();
		// 必中次数-----------------------------------------
		// 最少中奖次数 ------------------------
		Integer minHit = promotion.getMinHit();
		Integer maxHit = promotion.getMaxHit();

		if (minHit != null) {
			if ((times - raffleCount) <= (minHit - prizeCount)) {
				// 必须中奖
			} else {
				if (promotion.getRate().compareTo(rand) == -1) {
					promotionRaffle.setCode(3);
					promotionRaffleDao.add(promotionRaffle);
					map.put("code", 3);
					map.put("message", "未中奖");
					return map;
				}
			}
		}

		if (maxHit != null) {
			if (maxHit <= prizeCount) {
				// 必须不中奖
				promotionRaffle.setCode(3);
				promotionRaffleDao.add(promotionRaffle);
				map.put("code", 3);
				map.put("message", "未中奖");
				return map;
			} else {
				// 中奖
			}
		}
		if (minHit == null && maxHit == null) {
			if (promotion.getRate().compareTo(rand) == -1) {
				promotionRaffle.setCode(3);
				promotionRaffleDao.add(promotionRaffle);
				map.put("code", 3);
				map.put("message", "未中奖");
				return map;
			}
		}
		// 获取奖品列表
		List<PromotionPrize> promotionPrizeList = promotionPrizeDao
				.getPrizeList(id);

		if (null == promotionPrizeList || promotionPrizeList.size() <= 0) {
			promotionRaffle.setCode(4);
			promotionRaffleDao.add(promotionRaffle);
			map.put("code", 4);
			map.put("message", "奖品已抽完,未中奖");
			return map;
		}
		// 计算总中奖率
		Integer sumRate = 0;
		for (PromotionPrize promotionPrize : promotionPrizeList) {
			sumRate += promotionPrize.getRate();
		}
		// 没有可以抽的奖品
		if (sumRate <= 0) {
			promotionRaffle.setCode(4);
			promotionRaffleDao.add(promotionRaffle);
			map.put("code", 4);
			map.put("message", "奖品已抽完,未中奖");
			return map;
		}

		// 抽奖方法
		PromotionPrize awardedPromotionPrize = null;
		rand = (int) (Math.random() * sumRate);
		for (PromotionPrize promotionPrize : promotionPrizeList) {
			Integer rate = promotionPrize.getRate();
			if (rand - rate >= 0) {
				rand = rand - rate;
			} else {
				// 确认奖品
				awardedPromotionPrize = promotionPrize;
				break;
			}
		}

		// 异常处理，到上面分支是必中的，如果未中奖
		if (null == awardedPromotionPrize) {
			logger.error(String.format(
					"[抽奖] 必须中奖，但是未中奖, userId = %s, rand = %s", userId, rand));
			promotionRaffle.setCode(5);
			promotionRaffleDao.add(promotionRaffle);
			map.put("code", 5);
			return map;
		}

		// 抽中奖品
		// 已获得奖品数量 +1，如果有库存上限，则奖品数量-1
		promotionPrizeDao.decInventory(awardedPromotionPrize.getId());

		// 如果是本站虚拟物品，则立即发放虚拟物品
		if (awardedPromotionPrize.getVirtualType() != null) {
			awardVirtualPrize(promotion, awardedPromotionPrize, userId);
		}
		// 记录中奖
		promotionRaffle.setCode(0);
		promotionRaffle.setPrizeId(awardedPromotionPrize.getId());

		/*
		 * if(null!=awardedPromotionPrize.getExpire_time()){
		 * promotionRaffle.setExpire_time
		 * (awardedPromotionPrize.getExpire_time()); }else{
		 * if(null!=awardedPromotionPrize.getExpire_seconds()){ Date expire_time
		 * = new Date(); Calendar c = new GregorianCalendar();
		 * c.setTime(expire_time);//设置参数时间
		 * c.add(Calendar.SECOND,awardedPromotionPrize
		 * .getExpire_seconds());//把日期往后增加SECOND 秒.整数往后推,负数往前移动
		 * expire_time=c.getTime(); //这个时间就是日期往后推XX秒后的结果
		 * promotionRaffle.setExpire_time(expire_time); } }
		 */
		promotionRaffleDao.add(promotionRaffle);

		awardedPromotionPrize.setRaffleId(promotionRaffle.getId());

		map.put("code", 200);
		map.put("data", awardedPromotionPrize);

		return map;
	}

	/**
	 * @Title: awardVirtualPrize
	 * @Description: 虚拟礼品直接发放
	 * @param promotion
	 * @param awardedPromotionPrize
	 * @param userId
	 */
	private void awardVirtualPrize(Promotion promotion,
			PromotionPrize awardedPromotionPrize, Integer userId) {
			logger.info("保存用户中奖的虚拟礼品，该用户中的奖项为："+awardedPromotionPrize.getName());
			couponService.recevieCouponRecord(userId, awardedPromotionPrize.getVirtualExt1());
			logger.info("用户"+userId+"领取优惠券成功。");
	}

	/**
	 * <p>
	 * Title: getPromotionPrizeList
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public List<PromotionPrize> getPromotionPrizeList(Integer id) {
		List<PromotionPrize> pplistList = promotionRaffleDao
				.getPromotionPrizeList(id);
		return pplistList;
	}

	/**
	 * <p>
	 * Title: getMyPrizeList
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@Override
	public List<PromotionRaffle> getMyPrizeList(Integer id, Integer userId) {
		List<PromotionRaffle> pplistList = promotionRaffleDao.getMyPrizeList(
				id, userId);
		return pplistList;
	}

	/**
	 * <p>
	 * Title: getPrizeList
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public List<PromotionRaffle> getPrizeList(Integer id) {
		List<PromotionRaffle> pplistList = promotionRaffleDao.getPrizeList(id);
		return pplistList;
	}

	/**
	 * <p>
	 * Title: acceptPrize
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@Override
	public Map<String, Object> updateAcceptPrize(PromotionRaffle promotionraffle) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		PromotionRaffle raffle = promotionRaffleDao
				.selectByPrimaryKey(promotionraffle.getId());
		if (raffle != null) {
			if (raffle.getUserId().compareTo(promotionraffle.getUserId())!=0) {
				resMap.put("code", 400);
				resMap.put("message", "中奖用户 不存在");
				return resMap;
			} else if (raffle.isReceive()) {
				resMap.put("code", 400);
				resMap.put("message", "已领取过奖品");
				return resMap;
			}
		} else {
			resMap.put("code", 400);
			resMap.put("message", "中奖纪录不存在");
			return resMap;
		}
		int num = promotionRaffleDao.updateAcceptPrize(promotionraffle);
		if(num == 1 ){
			resMap.put("code", 200);
			resMap.put("message", "领取成功");
		}else{
			resMap.put("code", 400);
			resMap.put("message", "未知错误");
		}
		return resMap;
	}

	/**
	 * <p>
	 * Title: addPromotion
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param promotion
	 * @return
	 */
	@Override
	public Map<String, Object> addPromotion(Promotion promotion) {
		return null;
	}

	/**
	 * <p>
	 * Title: deletePromotion
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> deletePromotion(Integer id) {
		return null;
	}

	/**
	 * <p>
	 * Title: addPromotionPrize
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param promotionprize
	 * @return
	 */
	@Override
	public Map<String, Object> addPromotionPrize(PromotionPrize promotionprize) {
		return null;
	}

	/**
	 * <p>
	 * Title: deletePromotionPrize
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param promotionprize
	 * @return
	 */
	@Override
	public Map<String, Object> deletePromotionPrize(
			PromotionPrize promotionprize) {
		return null;
	}

	/**
	 * <p>
	 * Title: getRaffleCount
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@Override
	public PromotionRaffle getRaffleCount(Integer id, Integer userId,
			Integer type) {
		PromotionRaffle promotionraffle = promotionRaffleDao.getLotteryCount(
				id, userId, type);
		return promotionraffle;

	}

	/**
	 * <p>
	 * Title: getPromotion
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Promotion getPromotion(Integer id) {
		Promotion promotion = promotionDao.selectByPrimaryKey(id);
		return promotion;
	}

	@Override
	public Integer getIdByCode(String code) {
		// TODO Auto-generated method stub
		return promotionDao.getIdByCode(code);
	}

	@Override
	public Map<String, Object> lottery(Integer userId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Integer promotionId =lotteryActivityId; // 活动Id
		Integer skuId = lotterySkuId; // skuId
		logger.info("参与此次抽奖活动的活动id为：" + promotionId + ",参与此次抽奖活动的skuId为："+ skuId);
		// 获取活动信息
		Promotion promotion = getPromotion(promotionId);
		Date promotionCreateTime = promotion.getBeginTime();
		Date now = new Date();
		if (promotionCreateTime != null)
			if (promotionCreateTime.getTime() > now.getTime()) {
				data.put("code", 6);
				data.put("messsge", "活动未开始");
				return data;
			}
		
		Integer promotionPrizeCount = promotionDao.getPromotionPrizeCount(promotionId);
		if (promotionPrizeCount==0) {
			data.put("code", 7);
			data.put("messsge", "活动已结束");
			return data;
		}
		
		//判断用户时候已经购买一元商品
		Integer count = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("custId", userId);
		map.put("skuId", skuId);
		count = promotionDao.getOrderCount(map);
		if (count>0) {//表示用户已经购买一元购抽奖资格
			//判断用户是否已经抽奖
			PromotionRaffle promotionraffle = promotionRaffleDao.getLotteryCount(promotionId,
					userId, promotion.getType());
			if (null != promotionraffle) {
				int lastCount = promotionraffle.getTimes()
						- promotionraffle.getRaffleCount();
				if (lastCount <= 0) {
					data.put("code", 9);
					data.put("messsge", "抽奖机会已用完");
					return data;
				}
				
				data = saveRaffle(promotionId, userId);

				Integer codeValue = (Integer) data.get("code");

				if (codeValue != 200) {
					return data;
				}
			} else {
				data.put("code", 400);
				data.put("message", "操作失败");
				return data;
			}
		}else {
			data.put("code", 8);
			data.put("messsge", "用户还没有购买1元购商品。");
			return data;
		}
		return data;
	}
}
