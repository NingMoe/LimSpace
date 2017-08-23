package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.OrderSkuActivity;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.ConfigDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.SkuAttributeDao;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.repository.SpuAttributeDao;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.JdOrderService;
import com.tyiti.easycommerce.service.OrderOperationService;
import com.tyiti.easycommerce.service.SkuService;

@Controller
public class TestController {
	@Autowired
	UserDao userdao;

	@Autowired
	private JdOrderService jdOrderService;

	@Autowired
	private OrderOperationService orderOperationService;

	@Autowired
	SkuService skuService;

	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private SkuAttributeDao skuAttributeDao;
	@Autowired
	private SpuAttributeDao spuAttributeDao;
	@Autowired
	private OrderSkuDao orderSkuDao;
	@Autowired
	ActivityService activityService;
	@Autowired
	private ConfigDao configDao;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = userdao.findByMobile("18500682804");
		if (user != null) {
			System.out.print(user.getRealName());
			System.out.print("");
		}
		data.put("data", user);
		return data;
	}

	@RequestMapping(value = "/test/tag", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTag(HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = userdao.findByMobile("18500682804");
		data.put("data", user);
		return data;

	}

	@RequestMapping(value = "/test/jdtoken", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> readJdToken() {
		Map<String, Object> data = new HashMap<String, Object>();
		// data.put("AccessToken", jdOrderService.getAccessToken());
		return data;
	}

	@RequestMapping(value = "/test/jdorder", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> addJdToken() {
		Map<String, Object> data = new HashMap<String, Object>();
		// data.put("AccessToken", jdOrderService.getAccessToken());
		return data;
	}

	/**
	 * @description 申请退货上传图片
	 * @author wyy 2016/07/08
	 * @param
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/test/orders/img", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> imgToReturn(
			@RequestParam(value = "mediaId", required = true) String mediaId,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.returnImg(session,
				mediaId);
		return data;
	}

	/**
	 * @description 库存预警
	 * @author wyy 2016/09/09
	 * @param
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/test/skuWarning", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> warningSku(
			@RequestParam(value = "skuId", required = true) Integer skuId,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			skuService.WarningSkuInventory(skuId, 1);
			data.put("code", 200);
			data.put("message", "ok");
		} catch (Exception e) {
			data.put("code", 400);
			data.put("message", e);
		}
		return data;
	}

	/**
	 * @description 库存预警
	 * @author wyy 2016/09/29
	 * @param
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/test/Warning", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> subtractInventory(
			@RequestParam(value = "orderId", required = true) Integer orderId) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<OrderSku> lists = orderSkuDao.getByOrderId(orderId);
			if (lists == null || lists.size() == 0) {
				data.put("code", 400);
				data.put("message", "不存在该订单");
				return data;
			}
			ActivitySku activitySku = null;
			for (OrderSku os : lists) {
				OrderSkuActivity orderSkuActivity = activityService
						.findOrderSkuActivity(orderId, os.getSkuId());
				List<Map<String, Object>> activitySkuList = null;
				if (orderSkuActivity != null
						&& Constants.ACTIVITY_TYPE_SECKILL == (orderSkuActivity
								.getActivityType())) {
					// 秒杀
					logger.info(">>>>>>>>>>>>>>>>进入减库存信息--秒杀>>>>>>>>>>>>>>>>>>>>>>>>>");
					activitySkuList = activityService.findSpikeActivitySkuList(
							orderSkuActivity.getActivityId(), os.getSkuId());
					activitySku = new ActivitySku();
					activitySku.setId(Integer.parseInt(activitySkuList.get(0)
							.get("activitySkuId").toString()));
					activitySku.setReservedInventory(Integer
							.parseInt(activitySkuList.get(0)
									.get("reservedInventory").toString())
							- os.getSkuCount());
					activitySku.setSoldNum(Integer.parseInt(activitySkuList
							.get(0).get("soldNum").toString())
							+ os.getSkuCount());
					// activityService.updateActivitySku(activitySku);
					// 库存预警
					skuService.WarningSkuInventory(activitySku.getId(),
							os.getSkuCount());
				} else if (orderSkuActivity != null
						&& Constants.ACTIVITY_TYPE_PURCHASE == (orderSkuActivity
								.getActivityType())) {
					// 限购
					logger.info(">>>>>>>>>>>>>>>>进入减库存信息--限购>>>>>>>>>>>>>>>>>>>>>>>>>");
					activitySkuList = activityService.findSpikeActivitySkuList(
							orderSkuActivity.getActivityId(), os.getSkuId());
					// ActivitySku activitySku = activitySkuList.get(0);
					activitySku = new ActivitySku();
					activitySku.setId(Integer.parseInt(activitySkuList.get(0)
							.get("activitySkuId").toString()));
					activitySku.setSoldNum(Integer.parseInt(activitySkuList
							.get(0).get("soldNum").toString())
							+ os.getSkuCount());
					Integer skuCount = os.getSkuCount();
					Integer r_Inventory = Integer.parseInt(activitySkuList
							.get(0).get("reservedInventory").toString());
					Integer count = r_Inventory - skuCount;
					if (count >= 0) {// 如果
						activitySku.setReservedInventory(count);
						activityService.updateActivitySku(activitySku);
						// 库存预警
						skuService.WarningSkuInventory(activitySku.getId(),
								os.getSkuCount());
					} else {
						activitySku.setReservedInventory(0);
						Sku sku = skuDao.getById(os.getSkuId());
						sku.setInventory(sku.getInventory() - Math.abs(count));
						activityService.updateActivitySku(activitySku);
						// skuDao.updateByPrimaryKeySelective(sku);
						// 库存预警
						skuService.WarningSkuInventory(sku.getId(),
								os.getSkuCount());
					}
				} else {
					logger.info(">>>>>>>>>>>>>>>>进入减库存信息--普通商品减库存>>>>>>>>>>>>>>>>>>>>>>>>>");
					Sku sku = skuDao.getById(os.getSkuId());
					sku.setInventory(sku.getInventory() - os.getSkuCount());
					// skuDao.updateByPrimaryKeySelective(sku);
					// 库存预警
					skuService.WarningSkuInventory(sku.getId(),
							os.getSkuCount());
				}
			}
			data.put("code", 200);
			data.put("message", "ok");
		} catch (Exception e) {
			data.put("code", 400);
			data.put("message", e);
		}
		return data;
	}

}
