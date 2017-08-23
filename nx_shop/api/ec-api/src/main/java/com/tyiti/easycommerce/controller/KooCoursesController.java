package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.SkuExt;
import com.tyiti.easycommerce.service.KooCoursesService;

/**
 * 新东方课程表
 * @author wangqi
 * @date 2016年5月16日 上午10:20:33
 * @description TODO
 */
@Controller
public class KooCoursesController {
	@Autowired
	private KooCoursesService kooCoursesService;
	
	/**
	 * 根据kooProductId获取课程表详情
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kooCourses/{productId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getListById(@PathVariable("productId") String productId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> res = kooCoursesService.findListById(productId);
		Integer code = (Integer) res.get("code");
        
		if (code != 200) {
			return res;
		}

		data = res;

		return data;
	}
	/***
	  <p>功能描述：根据SkuID查询商品扩展信息。</p>	
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月3日 下午8:00:08。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value = "/skuExt/{skuId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> findSkuExt(@PathVariable("skuId")Integer skuId){
		Map<String, Object> data = new HashMap<String, Object>();
		SkuExt skuExt = kooCoursesService.findSkuExtSkuId(skuId);
		if(skuExt==null){
			data.put("code", "400");
			data.put("message", "查询数据错误");
		}else{
			data.put("code", "200");
			data.put("message", skuExt);
		}
		return data;
	}
}
