package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Activity;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.ActivitySkuService;

@Scope("prototype")
@Controller
public class ActivityController {

	Logger logger = Logger.getLogger(ActivityController.class);
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivitySkuService activitySkuService;
	
	/**
	 * 创建活动
	 * @param spu
	 * @param response
	 * @param session
	 * @return
	 */
    @RequestMapping(value = "/activity", method = RequestMethod.POST)
    @ResponseBody
	public Map<String, Object> add(@RequestBody Activity activity) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
        	int num = activityService.insertActivity(activity);
        	if (num == -1) {
        		data.put("code", 401);
    			data.put("message", "活动名称不能为空！");
			}else if (num == -2) {
				data.put("code", 402);
				data.put("message", "活动结束时间、活动开始时间不能为空！");
			} else if (num == -3) {
				data.put("code", 403);
				data.put("message", "活动结束时间不能早于活动开始时间！");
			}else {
				data.put("code", 200);
				data.put("message", "SUCCESS");
				data.put("activityId", activity.getId());
			}
		} catch (Exception e) {
			data.put("code", 400);
			data.put("message", "创建活动失败");
	        data.put("exception", e.getMessage());
		}
        return data;
    }
    
    /**
     * 查询创建的所有活动信息
     * @return
     */
    @RequestMapping(value = "/activity/activityType/{activityType}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAll(@PathVariable("activityType") Integer activityType){
    	Map<String, Object> data = new HashMap<String, Object>();
    	List<Activity> activityList = null;
		try {
			activityList = activityService.getAll(activityType);
			data.put("code", "200");
    		data.put("message", "查询成功");
    		data.put("activityList", activityList);
		} catch (Exception e) {
			data.put("code", "400");
			data.put("message", "查询活动列表失败");
			e.printStackTrace();
		}
    	return data;
    }
    
    /**
     * 修改活动信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/activity", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> updateActivity(@RequestBody Activity activity){
    	Map<String, Object> data = new HashMap<String, Object>();
    	int count = activityService.updateActivity(activity);
    	if (count>0) {
    		data.put("code", "200");
    		data.put("activity", "修改成功");
		}else {
			data.put("code", "400");
    		data.put("activity", "修改失败");
		}
    	return data;
    }
    
    /**
     * 根据活动id查询活动信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/activity/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getActivityById(@PathVariable("id") Integer id){
    	Map<String, Object> data = new HashMap<String, Object>();
    	Activity activity = activityService.getActivityById(id);
    	if (activity!=null) {
    		data.put("code", "200");
        	data.put("activity", activity);
		}else {
			data.put("code", "400");
	    	data.put("message", "查询活动信息失败，活动不存在。");
		}
    	
    	return data;
    }
    
    /**
     * 根据活动id删除活动信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/activity/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteActivityById(@PathVariable("id") Integer id){
    	Map<String, Object> data = new HashMap<String, Object>();
    	int count = activityService.deleteActivityById(id);
    	if (count>0) {
    		data.put("code", "200");
    		data.put("message", "删除成功");
		}else {
			data.put("code", "400");
    		data.put("message", "删除失败");
		}
    	return data;
    }
    
    /**
     * 根据活动id提前结束活动
     * @param id
     * @return
     */
    @RequestMapping(value = "/activity/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> endActivityById(@PathVariable("id") Integer id){
    	Map<String, Object> data = new HashMap<String, Object>();
		try {
			//修改库存
			List<ActivitySku> activitySkuList = activitySkuService.getActivitySkuById(id);
			logger.info("》》》》提前结束活动，该活动下的sku个数为："+activitySkuList.size());
			if (activitySkuList.size()>0) {
				for (int i = 0; i < activitySkuList.size(); i++) {
					activitySkuList.get(i).setInventory(-activitySkuList.get(i).getReservedInventory());
					logger.info("》》》》参与活动的sku的剩余预留库存为：《《《《"+activitySkuList.get(i).getInventory());
				}
				logger.info("》》》》提前结束活动，修改sku库存。《《《《");
				activitySkuService.updateSkuInventory(activitySkuList);
			}
			//修改活动状态
			activityService.endActivityById(id);
			data.put("code", "200");
    		data.put("message", "操作成功");
		} catch (Exception e) {
			data.put("code", "400");
    		data.put("message", "操作失败");
			e.printStackTrace();
		}
    	return data;
    }
    
    /**
     * 根据条件查询活动列表
     * @param param
     * param:1)活动状态 2)活动名称
     * @return
     */
	@RequestMapping(value="/activity/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchByConditions(@RequestParam Map<String, Object> param){
		Map<String, Object> map= new HashMap<String, Object>();
		try {
			List<Activity> activityList = activityService.searchByConditions(param);
			map.put("code", 200);
			map.put("activityList", activityList);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","sku查询失败。");
			e.printStackTrace();
		}
		return map;
	}

}
