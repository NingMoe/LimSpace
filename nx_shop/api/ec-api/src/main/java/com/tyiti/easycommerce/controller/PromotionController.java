/**  
 * @Title: PromotionController.java
 * @Package com.tyiti.easycommerce.controller
 * @Description: TODO
 * @author lmh
 * @date 2016-3-23
 * @version v1.0
 */
package com.tyiti.easycommerce.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.Promotion;
import com.tyiti.easycommerce.entity.PromotionPrize;
import com.tyiti.easycommerce.entity.PromotionRaffle;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.PromotionService;
import com.tyiti.easycommerce.service.impl.PromotionServiceImpl;

/**
 * @ClassName: PromotionController
 * @Description: TODO
 * @author lmh
 * @date 2016-3-23 下午4:38:42
 */
@Controller
@RequestMapping("/reward")
public class PromotionController {

	private static Log logger = LogFactory.getLog(PromotionServiceImpl.class);

	@Autowired
	private PromotionService promotionService;

	/**
	 * @Title: lottery
	 * @Description: TODO
	 * @param id
	 *            活动id
	 * @param response
	 * @param session
	 *            userId
	 * @return
	 */
	@RequestMapping(value = "/lottery/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> lottery(@PathVariable("code") String code,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		if (null == user ) {
			data.put("code", 401);
			data.put("messsge", "用户未登录");
			return data;
		}
		Integer userId = user.getId();
		Integer id = 0;
		try {
			id = promotionService.getIdByCode(code);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 1);
			data.put("messsge", "无此活动");
			data.put("exception", e.getMessage());
			return data;
		}

		logger.info("抽奖开始------通过 session获取的userId是-->" + userId
				+ "--->>>活动的id为：-->>" + id);
		
		Promotion promotion = promotionService.getPromotion(id);
		Date promotionCreateTime = promotion.getBeginTime();
		Date promotionExpireTime = promotion.getExpireTime();
		Date now = new Date();
		if (promotionCreateTime != null)
			if (promotionCreateTime.getTime() > now.getTime()) {
				data.put("code", 9);
				data.put("messsge", "活动未开始");
				return data;
			}
		if (promotionExpireTime != null)
			if (promotionExpireTime.getTime() < now.getTime()) {
				data.put("code", 10);
				data.put("messsge", "活动已结束");
				return data;
			}
		Integer newComer = promotion.getNewcomer();
		Date userCreateTime = user.getCreateTime();
		// 判断新老用户抽奖权限 -------------------------------------------------------
		if (newComer == 1) {
			// 老用户
			if (userCreateTime.compareTo(promotionCreateTime) > 0) {
				// 新用户不能抽奖
				data.put("code", 11);
				data.put("messsge", "新用户不能抽奖");
				return data;
			}
		} else if (newComer == 2) {
			// 新用户
			if (userCreateTime.compareTo(promotionCreateTime) <= 0) {
				// 新用户不能抽奖
				data.put("code", 6);
				data.put("messsge", "老用户不能抽奖");
				return data;
			}
		}
		// 1.获取活动详情 2.判断新用户还是老用户 还是 都可参加 3.判断是按天抽奖 还是按活动

		// 判断用户是否有抽奖次数
		PromotionRaffle promotionraffle = promotionService.getRaffleCount(id,
				userId, promotion.getType());

		if (null != promotionraffle) {
			int lastCount = promotionraffle.getTimes()
					- promotionraffle.getRaffleCount();
			if (lastCount <= 0) {
				data.put("code", 7);
				data.put("messsge", "抽奖机会已用完");
				return data;
			}

			Map<String, Object> res = promotionService.saveRaffle(id, userId);

			Integer codeValue = (Integer) res.get("code");

			if (codeValue != 200) {
				return res;
			}
			data.put("code", 200);
			data.put("messsge", "OK");
			data.put("lastCount", lastCount);
			data.put("data", res.get("data"));
			return data;
		} else {
			data.put("code", 400);
			data.put("message", "操作失败");
			return data;
		}
	}

	/**
	 * @Title: lotteryNum
	 * @Description: 获取当前用户可抽奖次数
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/lottery/{code}/num", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> lotteryNum(@PathVariable("code") String code,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		if (null == user) {
			data.put("code", 401);
			data.put("messsge", "用户未登录");
			data.put("lastCount", 0);
			return data;
		}
		Integer userId = user.getId();
		logger.info("抽奖开始------通过 session获取的userId是-->" + userId
				+ "--->>>活动的id为：-->>" + code);
		
		Integer id = 0;
		try {
			id = promotionService.getIdByCode(code);
			if(id == null){
				data.put("code", 1);
				data.put("messsge", "无此活动");	
				return data;
			}
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 1);
			data.put("messsge", "无此活动");
			data.put("lastCount", 0);
			data.put("exception", e.getMessage());
			return data;
		}
		Promotion promotion = promotionService.getPromotion(id);
		Date promotionCreateTime = promotion.getBeginTime();
		Date promotionExpireTime = promotion.getExpireTime();
		Date now = new Date();
		if (promotionCreateTime != null)
			if (promotionCreateTime.getTime() > now.getTime()) {
				data.put("code", 9);
				data.put("messsge", "活动未开始");
				data.put("lastCount", 0);
				return data;
			}
		if (promotionExpireTime != null)
			if (promotionExpireTime.getTime() < now.getTime()) {
				data.put("code", 10);
				data.put("messsge", "活动已结束");
				data.put("lastCount", 0);
				return data;
			}
		Integer newComer = promotion.getNewcomer();
		Date userCreateTime = user.getCreateTime();
		// 判断新老用户抽奖权限 -------------------------------------------------------
		if (newComer == 1) {
			// 老用户
			if (userCreateTime.compareTo(promotionCreateTime) > 0) {
				// 新用户不能抽奖
				data.put("code", 11);
				data.put("messsge", "新用户不能抽奖");
				data.put("lastCount", 0);
				return data;
			}
		} else if (newComer == 2) {
			// 新用户
			if (userCreateTime.compareTo(promotionCreateTime) <= 0) {
				// 新用户不能抽奖
				data.put("code", 6);
				data.put("messsge", "老用户不能抽奖");
				data.put("lastCount", 0);
				return data;
			}
		}
		// 1.获取活动详情 2.判断新用户还是老用户 还是 都可参加 3.判断是按天抽奖 还是按活动

		// 判断用户是否有抽奖次数
		PromotionRaffle promotionraffle = promotionService.getRaffleCount(id,
				userId, promotion.getType());

		if (null != promotionraffle) {
			int lastCount = promotionraffle.getTimes()
					- promotionraffle.getRaffleCount();
			if (lastCount <= 0) {
				data.put("code", 7);
				data.put("messsge", "抽奖机会已用完");
				data.put("lastCount", 0);
				return data;
			}
			data.put("code", 200);
			data.put("messsge", "OK");
			data.put("lastCount", lastCount);
			return data;
		} else {
			data.put("code", 400);
			data.put("message", "操作失败");
			return data;
		}
	}

	/**
	 * @Title: getPromotionPrizeList
	 * @Description: 活动奖品列表
	 * @param PromotionPrize
	 *            活动id
	 * @param session
	 *            用户userId
	 * @return
	 */
	@RequestMapping(value = "/promotionPrizeList/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPromotionPrizeList(
			@PathVariable("code") String code, HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		Integer id = 0;
		try {
			id = promotionService.getIdByCode(code);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 1);
			data.put("messsge", "无此活动");
			data.put("exception", e.getMessage());
			return data;
		}
		try {
			Promotion promotion = promotionService.getPromotion(id);
			// 没找到活动
			if (null == promotion) {
				data.put("code", 400);
				data.put("messsge", "没有此活动");
				return data;
			}

			List<PromotionPrize> promotionprizeList = promotionService
					.getPromotionPrizeList(id);
			data.put("code", 200);
			data.put("messsge", "OK");
			data.put("data", promotionprizeList);
		} catch (Exception e) {
			data.put("code", "400");
			logger.error("活动奖品列表获取异常", e);
		}
		return data;
	}

	/**
	 * @Title: getMyPrizeList
	 * @Description: 获取我的中奖记录
	 * @param PromotionPrize
	 *            活动id
	 * @param session
	 *            用户id
	 * @return
	 */
	@RequestMapping(value = "/getMyPrizeList/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getMyPrizeList(
			@PathVariable("code") String code, HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		if (null == user) {
			data.put("code", 401);
			data.put("messsge", "用户未登录");
			return data;
		}
		Integer userId = user.getId();
		Integer id = 0;
		try {
			id = promotionService.getIdByCode(code);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 1);
			data.put("messsge", "无此活动");
			data.put("exception", e.getMessage());
			return data;
		}
		logger.info("我的中奖记录-->>userId-->>" + userId + "----活动id-->>" + id);
		
		try {
			List<PromotionRaffle> promotionPrizeList = promotionService
					.getMyPrizeList(id, userId);
			data.put("code", 200);
			data.put("messsge", "OK");
			data.put("data", promotionPrizeList);
		} catch (Exception e) {
			data.put("code", "400");
			logger.error("获取我的中奖记录", e);
		}
		return data;
	}

	/**
	 * @Title: getPrizeList
	 * @Description: 获奖名单及奖品列表
	 * @param PromotionPrize
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getPrizeList/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPrizeList(@PathVariable("code") String code,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		Integer id = 0;
		try {
			id = promotionService.getIdByCode(code);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 1);
			data.put("messsge", "无此活动");
			data.put("exception", e.getMessage());
			return data;
		}
		Promotion promotion = promotionService.getPromotion(id);
		Date promotionCreateTime = promotion.getBeginTime();
		Date promotionExpireTime = promotion.getExpireTime();
		Date now = new Date();
		if (promotionCreateTime != null)
			if (promotionCreateTime.getTime() > now.getTime()) {
				data.put("status", 1);
				data.put("description", "活动未开始");
			}
		if (promotionExpireTime != null)
			if (promotionExpireTime.getTime() < now.getTime()) {
				data.put("status", 2);
				data.put("description", "活动已结束");
			}
		List<PromotionRaffle> res = promotionService.getPrizeList(id);

		data.put("code", 200);
		data.put("messsge", "OK");
		data.put("data", res);
		return data;
	}

	/**
	 * @Title: acceptPrize
	 * @Description: 领奖
	 * @param promotion
	 * @param session
	 * @return
	 */

	@RequestMapping(value = "/acceptPrize", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> acceptPrize(
			@RequestBody PromotionRaffle promotionraffle,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		if (null == user) {
			data.put("code", 401);
			data.put("messsge", "用户未登录");
			return data;
		}
		Integer userId = user.getId();
		if (null != promotionraffle) {
			logger.info("领取奖品开始--领取人user_id+" + userId + "-中奖记录id->>"
					+ promotionraffle.getId() + "---领取人姓名-->>"
					+ promotionraffle.getRealName() + "领取人电话-->>"
					+ promotionraffle.getMobile() + "--地址-->>"
					+ promotionraffle.getAddress());
			promotionraffle.setUserId(userId);
			data = promotionService.updateAcceptPrize(promotionraffle);
		} else {
			data.put("code", 400);
			data.put("messsge", "领取失败");
		}
		return data;
	}

	/**
	 * @Title: addPromotion
	 * @Description: 添加活动
	 * @param Promotion
	 *            活动信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addPromotion", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addPromotion(Promotion promotion,
			HttpSession session) {

		Map<String, Object> data = new HashMap<String, Object>();

		Map<String, Object> res = promotionService.addPromotion(promotion);

		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}
		data.put("code", 200);
		data.put("messsge", "OK");
		data.put("data", res.get("data"));
		return data;
	}

	/**
	 * @Title: deletePromotion
	 * @Description: 删除活动
	 * @param Promotion
	 *            活动id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deletePromotion", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deletePromotion(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {

		Map<String, Object> data = new HashMap<String, Object>();

		Map<String, Object> res = promotionService.deletePromotion(id);

		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}
		data.put("code", 200);
		data.put("messsge", "OK");
		data.put("data", res.get("data"));
		return data;

	}

	/**
	 * @Title: deletePromotion
	 * @Description: 添加奖品
	 * @param Promotion
	 *            奖品信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addPromotionPrize", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addPromotionPrize(PromotionPrize promotionprize,
			HttpSession session) {

		Map<String, Object> data = new HashMap<String, Object>();

		Map<String, Object> res = promotionService
				.addPromotionPrize(promotionprize);

		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}
		data.put("code", 200);
		data.put("messsge", "OK");
		data.put("data", res.get("data"));
		return data;
	}

	/**
	 * @Title: deletePromotionPrize
	 * @Description: 删除活动奖品
	 * @param PromotionPrize
	 *            奖品id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deletePromotionPrize", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deletePromotionPrize(
			PromotionPrize promotionprize, HttpSession session) {

		Map<String, Object> data = new HashMap<String, Object>();

		Map<String, Object> res = promotionService
				.deletePromotionPrize(promotionprize);

		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}
		data.put("code", 200);
		data.put("messsge", "OK");
		data.put("data", res.get("data"));
		return data;
	}
	
	/**
	 * 1元抽奖活动
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/lottery", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> lottery(HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		if (null == user ) {
			data.put("code", 401);
			data.put("messsge", "用户未登录");
			return data;
		}else {
			data = promotionService.lottery(user.getId());
			return data;
		}
		
	}
	
}
