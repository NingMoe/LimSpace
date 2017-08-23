package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Config;
import com.tyiti.easycommerce.entity.Headline;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.SkuShelvesSchedule;
import com.tyiti.easycommerce.entity.Warning;
import com.tyiti.easycommerce.entity.WarningGroup;
import com.tyiti.easycommerce.entity.WarningUser;
import com.tyiti.easycommerce.entity.WarningUserGroup;
import com.tyiti.easycommerce.repository.HeadlineDao;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.repository.SkuShelvesScheduleDao;
import com.tyiti.easycommerce.repository.WarningGroupDao;
import com.tyiti.easycommerce.repository.WarningUserDao;
import com.tyiti.easycommerce.repository.WarningUserGroupDao;
import com.tyiti.easycommerce.service.WarningService;

@Service("WarningService")
public class WarningServiceImpl implements WarningService {

	@Autowired
	private WarningGroupDao warningGroupDao;
	@Autowired
	private WarningUserDao warningUserDao;
	@Autowired
	private WarningUserGroupDao warningUserGroupDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private SkuShelvesScheduleDao sssDao;
	@Autowired
	private HeadlineDao headlineDao;

	Logger logger = Logger.getLogger(WarningServiceImpl.class);

	/**
	 * 添加预警
	 */
	public Map<String, Object> addWarning(Warning warning,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 预警类型
			String warningType = warning.getWarningType();
			/*********************** userList 预警用户 SATRT ****************************/
			if (null != warning.getUserList()
					&& warning.getUserList().size() > 0) {
				// 预警用户
				JSONArray jsonArray = JSONArray.fromObject(warning
						.getUserList());
				List<WarningUser> userList = new ArrayList<WarningUser>();
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					userList.add((WarningUser) JSONObject.toBean(jsonObject,
							WarningUser.class));
				}
				// 添加预警用户
				if (userList.size() > 0) {
					addWarningUser(userList, warningType);
				}
			}
			/*********************** userList 预警用户 END ****************************/
			/***************************** 库存预警值 START **************************/
			Integer inventory = warning.getInventory();
			if (null != inventory && inventory > 0) {
				if ("sku_warning_all".equals(warningType)) {
					// 设置全部库存预警值
					setAllWarning(inventory.toString());
				} else {
					if (warning.getSkuIdList() != null
							&& warning.getSkuIdList().size() > 0) {
						Integer[] skuIdArray = new Integer[warning
								.getSkuIdList().size()];
						setPartWarning(
								warning.getSkuIdList().toArray(skuIdArray),
								inventory);
					}
				}
			}
			/***************************** 库存预警值 END **************************/
			map.put("code", "200");
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("添加预警有报异常了，参数：" + warning, e);
			map.put("code", "400");
			map.put("message", "出错了，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 删除预警用户
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	public Map<String, Object> delWarningUser(Warning warning,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 预警用户
			JSONArray jsonArray = JSONArray.fromObject(warning.getUserList());
			List<WarningUser> userList = new ArrayList<WarningUser>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				userList.add((WarningUser) JSONObject.toBean(jsonObject,
						WarningUser.class));
			}
			//
			if (userList.size() <= 0) {
				map.put("code", 400);
				map.put("message", "参数有误！");
				return map;
			}
			// 预警类型
			String warningType = warning.getWarningType();
			// 通过预警组名称 获取预警组Id
			WarningGroup warningGroup = new WarningGroup();
			warningGroup.setName(warningType);
			warningGroup.setInvalid(0);
			warningGroup = warningGroupDao.getWarningGroup(warningGroup);
			for (WarningUser user : userList) {
				// userId与预警用户
				Integer userId = user.getId();
				// 报警用户与报警组之间的关系
				WarningUserGroup warningUserGroup = new WarningUserGroup();
				warningUserGroup.setWarningGroupId(warningGroup.getId());
				warningUserGroup.setInvalid(1);
				warningUserGroup.setWarningUserId(userId);
				warningUserGroupDao
						.updateWarningUserGroupByPrimaryKey(warningUserGroup);
			}
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("删除报警用户抛出异常了：" + e);
			map.put("code", 400);
			map.put("message", "删除报警用户异常，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 获取报警用户
	 */
	public Map<String, Object> getWarningUser(Map<String, Object> param,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<WarningUser> userList = warningUserDao
					.getWarningUserByGroupName(param.get("warningType")
							.toString());
			map.put("code", 200);
			map.put("message", "ok");
			if ("sku_warning_all".equals(param.get("warningType"))) {
				Config config = new Config();
				config.setKey("sku_warning_all");
				config = warningGroupDao.getConfig(config);
				if (config != null) {
					map.put("warningInventory", config.getValue());
				}
			}
			map.put("data", userList);
		} catch (Exception e) {
			logger.error("获取报警用户抛出异常了：" + e);
			map.put("code", 400);
			map.put("message", "获取报警用户异常，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 获取部分预警sku
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	public Map<String, Object> getWarningSku(HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Sku> skuList = skuDao.getWarningSku();
			map.put("code", 200);
			map.put("message", "ok");
			map.put("data", skuList);
		} catch (Exception e) {
			logger.error("删除报警用户抛出异常了：" + e);
			map.put("code", 400);
			map.put("message", "删除报警用户异常，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 删除预警sku
	 * 
	 * @param param
	 *            skuIdList
	 * @param response
	 * @return
	 */
	public Map<String, Object> delWarningSku(Warning warning,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Integer[] skuIdArray = new Integer[warning.getSkuIdList().size()];
			setPartWarning(warning.getSkuIdList().toArray(skuIdArray), 0);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("删除报警用户抛出异常了：" + e);
			map.put("code", 400);
			map.put("message", "删除报警用户异常，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 设置全部库存
	 * 
	 * @param inventory
	 *            预警值
	 * @return
	 */
	public void setAllWarning(String inventory) {
		try {
			// 查询 t_config 是否存在 key
			Config config = new Config();
			config.setKey("sku_warning_all");
			config = warningGroupDao.getConfig(config);
			config.setValue(inventory);
			warningGroupDao.updateConfigByPrimaryKey(config);

		} catch (Exception e) {
			logger.error("设置全部库存报异常了--", e);
			throw e;
		}
	}

	/**
	 * 设置部分库存
	 * 
	 * @param inventory
	 *            预警值
	 * @return
	 */
	public void setPartWarning(Integer[] skuIdArray, Integer inventory) {
		try {
			skuDao.setWarningInventoryByIds(skuIdArray, inventory);
		} catch (Exception e) {
			logger.error("设置部分库存预警有报异常了，参数：" + skuIdArray + "--" + inventory, e);
			throw e;
		}
	}

	/**
	 * @description 添加预警用户,根据报警组名称获取组Id关联用户 wyy 2016/09/05
	 * @param userList
	 *            报警用户列表数组
	 * @param warningType
	 *            报警组用名称
	 */
	public void addWarningUser(List<WarningUser> userList, String warningType) {
		try {
			// 通过预警组名称 获取预警组Id
			WarningGroup warningGroup = new WarningGroup();
			warningGroup.setName(warningType);
			warningGroup.setInvalid(0);
			warningGroup = warningGroupDao.getWarningGroup(warningGroup);
			// 报警用户与报警组之间的关系
			WarningUserGroup warningUserGroup = new WarningUserGroup();
			warningUserGroup.setWarningGroupId(warningGroup.getId());
			Integer userId = 0;
			for (WarningUser user : userList) {
				// 添加报警用户
				//循环添加时存在
				user.setId(null);
				WarningUser warningUser = warningUserDao.getWarningUser(user);
				if (warningUser != null) {
					userId = warningUser.getId();
					warningUser.setInvalid(0);
					warningUserDao.updateWarningUserByPrimaryKey(warningUser);
				} else {
					if (warningUser == null) {
						warningUserDao.insert(user);
						userId = user.getId();
					}
				}
				// 报警用户和组的关联表
				warningUserGroup.setWarningUserId(userId);
				if (warningUserGroupDao.getWarningUserGroup(warningUserGroup).size()<1) {
					warningUserGroup.setInvalid(0);
					warningUserGroupDao.insert(warningUserGroup);
				} else {
					warningUserGroup.setInvalid(0);
					warningUserGroupDao
							.updateWarningUserGroupByPrimaryKey(warningUserGroup);
				}
			}
		} catch (Exception e) {
			logger.error("设置报警用户异常捕获：" + e);
			throw e;
		}
	}

	/**
	 * 定时上下架
	 * 
	 * @param param
	 *            startDate 开始时间 endDate 结束时间 skuList skuList 只存在 startDate
	 *            type=上架 只存在 endDate type=下架 两个时间都存在是 type=上下架 skuIdList
	 * @return
	 */
	public Map<String, Object> addTimingShelves(SkuShelvesSchedule sssm,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Integer[] skuIdArray = new Integer[sssm.getSkuIdList().size()];
			skuIdArray = sssm.getSkuIdList().toArray(skuIdArray);
			List<SkuShelvesSchedule> sssList = new ArrayList<SkuShelvesSchedule>();
			// SkuShelvesSchedule[] sssArray = new
			// SkuShelvesSchedule[sssm.getSkuIdList().size()];
			Integer index = 0;
			SkuShelvesSchedule sss;
			for (Integer skuId : skuIdArray) {
				sss = new SkuShelvesSchedule();
				if (null != sssm.getOnTime()) {
					// 上架时间
					sss.setOnTime(sssm.getOnTime());
				}
				if (null != sssm.getOffTime()) {
					// 下架时间
					sss.setOffTime(sssm.getOffTime());
				}
				sss.setCreateTime(new Date());
				sss.setUpdateTime(new Date());
				sss.setInvalid(0);
				sss.setSkuId(skuId);
				sssList.add(index, sss);
				index++;
			}
			sssDao.insertRows(sssList);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("添加上下架异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "添加上下架异常，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 获取定时上下架
	 * 
	 * @param param
	 *            type =上架、 下架 、上下架
	 * @return
	 */
	public Map<String, Object> getTimingShelves(HttpServletResponse response,SkuShelvesSchedule sssm) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//结束状态
			sssDao.timeShelvesInvalid();
			
			//SkuShelvesSchedule sssm = new SkuShelvesSchedule();
			List<SkuShelvesSchedule> sssList = sssDao.getskuSSSList(sssm);
			Integer total=sssDao.getskuSSSListCount(sssm);
			for (SkuShelvesSchedule sss : sssList) {
				// 判断类型
				if (null != sss.getOnTime() && null != sss.getOffTime()) {
					sss.setType("上下架");
				} else {
					if (null != sss.getOnTime()) {
						sss.setType("上 架");
					} else {
						sss.setType("下 架");
					}
				}
				// 判断状态
				if (sss.getInvalid() == 1) {
					sss.setStatus("已结束");
				} else {
					Date date = new Date();
					if ("上下架".equals(sss.getType())) {
						if (sss.getOnTime().getTime() > date.getTime()) {
							sss.setStatus("未开始");
						} else {
							if (sss.getOffTime().getTime() < date.getTime()) {
								sss.setStatus("已结束");
							} else {
								sss.setStatus("进行中");
							}
						}
					}
					if ("上 架".equals(sss.getType())) {
						if (sss.getOnTime().getTime() > date.getTime()) {
							sss.setStatus("未开始");
						} else {
							sss.setStatus("已结束");
						}
					}
					if ("下 架".equals(sss.getType())) {
						if (sss.getOffTime().getTime() < date.getTime()) {
							sss.setStatus("已结束");
						} else {
							sss.setStatus("未开始");
						}
					}
				}
			}
			map.put("code", 200);
			map.put("message", "ok");
			map.put("data", sssList);
			map.put("total", total);
		} catch (Exception e) {
			logger.error("获取定时上下架列表异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "报异常了，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 结束指定 定时上下架
	 * 
	 * @param param
	 *            skuId 要结束的skuid
	 * @return
	 */
	public Map<String, Object> endTimingShelves(Integer id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SkuShelvesSchedule sssm = new SkuShelvesSchedule();
			sssm.setId(id);
			sssm = sssDao.getSSS(sssm);
			if (sssm.getInvalid() == 1) {
				sssDao.deleteByPrimaryKey(sssm);
			} else {
				sssDao.endSSSById(id);
			}
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("结束单个sku上下架异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "报异常了，请于管理员联系!");
		}
		return map;
	}

	/**
	 * 结束指定 定时上下架
	 * 
	 * @param param
	 *            Id 要结束的id
	 * @return
	 */
	public Map<String, Object> delTimingShelves(Integer id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {		
			
			SkuShelvesSchedule sssm = new SkuShelvesSchedule();
			sssm.setId(id);
			sssDao.deleteByPrimaryKey(sssm);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("结束单个sku上下架异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "报异常了，请于管理员联系!");
		}
		return map;
	}

	public void addtimingSku() {
		try {
			// 先查询上架
			List<SkuShelvesSchedule> skuIdList = sssDao.getOnTimeList();
			if (skuIdList != null && skuIdList.size() > 0) {
				// 上架
				sssDao.timeOnShelves(skuIdList);
			}
			// 在查询下架
			skuIdList = sssDao.getOffTimeList();
			if (skuIdList != null && skuIdList.size() > 0) {
				// 下架
				sssDao.timeOffShelves(skuIdList);
			}
			// 定时上下架表的状态修改
			sssDao.timeShelvesInvalid();
		} catch (Exception e) {
			logger.error("定时上下架异常捕获：" + e);
		}
	}

	/**
	 * 添加头条
	 */
	public Map<String, Object> addHeadline(Headline headline,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			headline.setInvalid(0);
			headlineDao.insert(headline);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("添加头条异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "添加头条报异常了，请于管理员联系!");
		}
		return map;
	}

	/**
	 * 获取头条
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getHeadline(Integer id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("code", 200);
			map.put("message", "ok");
			map.put("data", headlineDao.getHeadline(id));
		} catch (Exception e) {
			logger.error("结束或删除头条异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "出现异常，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 修改头条
	 * 
	 * @param headline
	 * @return
	 */
	public Map<String, Object> updateHeadline(Headline headline,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			headlineDao.updateHeadlineByPrimaryKey(headline);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("结束或删除头条异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "出现异常，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 获取头条列表
	 */
	public Map<String, Object> getHeadlineList(HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 结束过期的
			headlineDao.updateInvalid();
			Headline headline = new Headline();
			List<Headline> hlList = headlineDao.getHeadlineList(headline);
			for (Headline hl : hlList) {
				if (hl.getInvalid() == 1) {
					hl.setStatus("已结束");
				} else {
					Date date = new Date();
					if (hl.getStartTime() != null
							&& date.getTime() < hl.getStartTime().getTime()) {
						hl.setStatus("未开始");
					} else {
						if (hl.getEndTime() != null
								&& date.getTime() > hl.getEndTime().getTime()) {
							hl.setStatus("已结束");
						} else {
							hl.setStatus("进行中");
						}
					}
				}
			}
			map.put("code", 200);
			map.put("message", "ok");
			map.put("data", hlList);
		} catch (Exception e) {
			logger.error("获取头条异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "出现异常，请于管理员联系！");
		}
		return map;
	}

	/**
	 * 删除头条
	 */
	public Map<String, Object> delHeadline(Integer id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Headline hl = headlineDao.getHeadline(id);
			if (hl.getInvalid() == 1) {
				headlineDao.deleteByPrimaryKey(id);
			} else {
				Headline headline = new Headline();
				headline.setId(id);
				headline.setInvalid(1);
				headlineDao.updateHeadlineByPrimaryKey(headline);
			}
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.error("结束或删除头条异常捕获：" + e);
			map.put("code", 400);
			map.put("message", "出现异常，请于管理员联系！");
		}
		return map;
	}

	public List<Map<String, Object>> selectTagLists(Map<String, Object> param) {
		if ("TimeSku".equals(param.get("listType"))) {
			return sssDao.selectTagLists(param);
		} else {
			return warningGroupDao.selectTagLists(param);
		}
	}
}