package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.common.Solr;
import com.tyiti.easycommerce.entity.Config;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.OrderSkuActivity;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.SkuAttribute;
import com.tyiti.easycommerce.entity.SpuAttribute;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.entity.WarningItem;
import com.tyiti.easycommerce.entity.WarningUser;
import com.tyiti.easycommerce.repository.ConfigDao;
import com.tyiti.easycommerce.repository.FavoriteDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.SkuAttributeDao;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.repository.SpuAttributeDao;
import com.tyiti.easycommerce.repository.WarningItemDao;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.SkuService;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.xml.XmlUtil;
import com.tyiti.easycommerce.util.xml.entity.CSubmitState;

@Service
public class SkuServiceImpl implements SkuService {
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
	private FavoriteDao favoriteDao;
	@Autowired
	ActivityService activityService;

	@Autowired
	private ConfigDao configDao;

	@Autowired
	private WarningItemDao warningItemDao;

	@Value("${SolrHost}")
	private String SolrHost;
	@Value("${sys}")
	private String sys;
	@Value("${SolrPort}")
	private String SolrPort;

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

	public Sku addSku(Sku sku) {
		int affectedRows = skuDao.add(sku);
		if (affectedRows < 1) {
			return null;
		}
		Integer skuId = sku.getId();
		List<SkuAttribute> skuAttributeList = sku.getAttrs();
		if (skuAttributeList != null && skuAttributeList.size() > 0) {
			for (SkuAttribute skuAttribute : skuAttributeList) {
				skuAttribute.setSkuId(skuId);
			}
			skuAttributeDao.addList(skuAttributeList);
		}
		return skuDao.getById(skuId);
	}

	/**
	 * 按多种条件查询列表
	 */
	public List<Sku> getByCriteria(Sku sku) {
		return skuDao.getByCriteria(sku);
	}

	/**
	 * 商品详情
	 */
	public Sku getById(Integer id) {
		return skuDao.getById(id);
	}

	/**
	 * 商品详情 包含商品属性 等多种数据
	 */
	public Map<String, Object> findDetailById(HttpSession session, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Sku sku = skuDao.getById(id);
		if (sku == null) {
			map.put("code", 404);
			map.put("messsge", "商品未找到");
			return map;
		}
		// 判断是否登录，从而判断此商品是否被收藏
		User user = (User) session.getAttribute(Constants.USER);
		if (user != null) {
			List<Integer> SkuIds = favoriteDao.getSkuIdsByUserId(user.getId());
			if (SkuIds.contains(id)) {
				sku.setIsFavorite(true);
			}
		} else {
			sku.setIsFavorite(false);
		}

		map.put("sku", sku);

		List<SpuAttribute> spuAttrs = spuAttributeDao.getListBySpuId(sku
				.getSpuId());
		map.put("spuAttrs", spuAttrs);

		List<SkuAttribute> otherSkuAttrs = skuAttributeDao
				.getListBySpuIdAndNotSkuAttrId(sku.getId(), sku.getSpuId());
		map.put("otherSkuAttrs", otherSkuAttrs);

		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/**
	 * <p>
	 * 功能描述:减库存。
	 * </p>
	 * 
	 * @param orderId
	 * @return <p>
	 *         创建日期2016年7月29日 上午10:37:21。
	 *         </p>
	 *         <p>
	 *         更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *         </p>
	 */
	@Override
	public int subtractInventory(Integer orderId) {
		logger.info(">>>>>>>>>>>>>>>>进入减库存信息>>>>>>>>>>>>>>>>>>>>>>>>>");
		List<OrderSku> lists = orderSkuDao.getByOrderId(orderId);
		if (lists == null || lists.size() == 0) {
			return 0;
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
				activitySku.setSoldNum(Integer.parseInt(activitySkuList.get(0)
						.get("soldNum").toString())
						+ os.getSkuCount());
				activityService.updateActivitySku(activitySku);
				// 库存预警
				WarningSkuInventory(activitySku.getId(), os.getSkuCount());
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
				activitySku.setSoldNum(Integer.parseInt(activitySkuList.get(0)
						.get("soldNum").toString())
						+ os.getSkuCount());
				Integer skuCount = os.getSkuCount();
				Integer r_Inventory = Integer.parseInt(activitySkuList.get(0)
						.get("reservedInventory").toString());
				Integer count = r_Inventory - skuCount;
				if (count >= 0) {// 如果
					activitySku.setReservedInventory(count);
					activityService.updateActivitySku(activitySku);
					// 库存预警
					WarningSkuInventory(activitySku.getId(), os.getSkuCount());
				} else {
					activitySku.setReservedInventory(0);
					Sku sku = skuDao.getById(os.getSkuId());
					sku.setInventory(sku.getInventory() - Math.abs(count));
					activityService.updateActivitySku(activitySku);
					skuDao.updateByPrimaryKeySelective(sku);
					// 库存预警
					WarningSkuInventory(sku.getId(), os.getSkuCount());
				}
			} else {
				logger.info(">>>>>>>>>>>>>>>>进入减库存信息--普通商品减库存>>>>>>>>>>>>>>>>>>>>>>>>>");
				Sku sku = skuDao.getById(os.getSkuId());
				sku.setInventory(sku.getInventory() - os.getSkuCount());
				skuDao.updateByPrimaryKeySelective(sku);
				// 库存预警
				WarningSkuInventory(sku.getId(), os.getSkuCount());
			}
		}
		return 1;
	}

	public Map<String, Object> getSkusByQueryText(String querytext,
			String sortfield, String sorttype) {

		List<Integer> ids = new Solr("http://" + SolrHost + ":" + SolrPort
				+ "/solr/collection_" + sys).GetSkuIdByInput(querytext,
				sortfield, sorttype);
		Map<String, Object> map = new HashMap<String, Object>();
		if (ids.size() == 0) {
			map.put("code", 404);
			map.put("messsge", "商品未找到");
			return map;
		}
		List<Sku> li = new ArrayList<Sku>();
		for (int i = 0; i < ids.size(); i++) {
			Sku sku = skuDao.getById(ids.get(i));
			if (sku != null) {
				li.add(sku);
			}
		}
		map.put("sku", li);
		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/**
	 * <p>
	 * 功能描述:。
	 * </p>
	 * 
	 * @param sku
	 *            <p>
	 *            创建日期2016年9月14日 下午5:23:01。
	 *            </p>
	 *            <p>
	 *            更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *            </p>
	 */
	@Override
	public void updateSku(Sku sku) {
		skuDao.updateByPrimaryKeySelective(sku);
	}

	/**
	 * 功能描述:库存预警。
	 * 
	 * @param skuId
	 *            商品Id
	 * @param count
	 *            购买商品数量
	 * @return 创建日期2016年9月07日 wyy 更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 */
	public void WarningSkuInventory(Integer skuId, Integer count) {
		try {
			Sku sku = skuDao.getById(skuId);
			// 查看是否存在预警值
			if (sku.getWarningInventory() == null
					|| 0 == sku.getWarningInventory()) {
				// 不存在预警值，查看配置中是否存在全部预警值
				Config config = configDao.selectByKey("sku_warning_all");
				// 存在全局预警值
				if (config != null) {
					Integer WarningInventory = Integer.parseInt(config
							.getValue());
					// 商品库存等于全局预警值触发预警
					if (WarningInventory == sku.getInventory()
							|| WarningInventory == sku.getInventory() + count
									- 1) {
						WarningUser(sku, "sku_warning_all");
					}
				}
			} else {
				// 存在预警值并且等于库存值，触发报警
				if (sku.getWarningInventory() == sku.getInventory()
						|| sku.getWarningInventory() == sku.getInventory()
								+ count - 1) {
					WarningUser(sku, "sku_warning_part");
				}
			}
		} catch (Exception e) {
			logger.error("查询库存预警异常捕获：" + e);
		}
	}

	/**
	 * @description 向报警用户，报警
	 * @author wyy 2016/09/07
	 * @param sku
	 *            报警的商品
	 * @param warningType
	 *            报警组
	 */
	public void WarningUser(Sku sku, String warningType) {
		try {
			// 1、获取报警模板
			WarningItem item = warningItemDao
					.getWarningItemByName("global_inventory");
			if (item != null) {
				List<WarningUser> userList = warningItemDao
						.getWarningUserByName("global_inventory", warningType);
				String smsContent = "";
				if (item.getSmsTpl() != null && !"".equals(item.getSmsTpl())) {
					// 发送短信
					smsContent = item.getSmsTpl().replaceAll("%skuName%",
							sku.getName());
					smsContent = smsContent.replaceAll("%skuId%", sku.getId()
							.toString());
					smsContent = smsContent.replaceAll("%warning_inventory%",
							sku.getInventory().toString());
					smsContent = smsContent.replaceAll(" ", "");
				}
				for (WarningUser user : userList) {
					// 发送短信
					if (item.getSendSms() == 1) {
						if (user.getMobile() != null) {
							sendSms(user.getMobile(), smsContent);
						}
					}
					// 发送邮件
					if (item.getSendEmail() == 1) {
						if (user.getEmail() != null) {
							sendEmail();
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("查询报警项或者发送报警异常捕获：" + e);
		}
	}

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 *            接受手机号
	 * @param smsg
	 *            短信内容
	 */
	public void sendSms(String mobile, String smsg) {
		try {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("sname", sname);
			requestParams.put("spwd", spwd);
			requestParams.put("scorpid", scorpid);
			requestParams.put("sprdid", sprdid);
			requestParams.put("sdst", mobile);
			requestParams.put("smsg", smsg + tail);			
			// 解析返回的 XML
			String responseXml = HttpClientUtil.httpGet(smsUrl, null,
					requestParams);
			logger.info(">>>>>>>>>>>>库存预警发送短信：短信地址："+smsUrl+",参数："+requestParams);
			CSubmitState responseData = (CSubmitState) XmlUtil.fromXml(
					responseXml, CSubmitState.class);
			logger.info(">>>>>>>>>>>发送预警短信返回信息："+responseData);
			// 解析 XML 失败
			if (responseData == null) {
				logger.error("解析 XML 失败," + mobile + "--" + smsg);
			}
		} catch (Exception e) {
			logger.error("发送库存报警短信异常捕获：" + e);
		}
	}

	/**
	 * 发送邮件
	 */
	public void sendEmail() {

	}

}
