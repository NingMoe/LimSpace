package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Banner;
import com.tyiti.easycommerce.entity.Headline;
import com.tyiti.easycommerce.repository.HeadlineDao;
import com.tyiti.easycommerce.service.AdvertService;

@Controller
public class AdvertController {

	@Autowired
	private AdvertService advertService;

	@Autowired
	private HeadlineDao headlineDao;

	/**
	 * @Title: Advert
	 * @Description: 查询列表
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/adverts/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> Advert(@PathVariable("code") String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Banner> bannerList = advertService.getBannerListByCode(code);
			map.put("code", "200");
			map.put("data", bannerList);
		} catch (Exception e) {
			map.put("code", "400");
			map.put("message", "banner 查询失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}

	/**
	 * @Title: Advert
	 * @Description: 查询列表
	 * @return Map<String,Object> 返回类型
	 * @author wyy 2016/06/29
	 * @since
	 * @throws
	 */
	@RequestMapping(value = "/adverts", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> AdvertList(
			@RequestParam(value = "group", required = false) String group,
			@RequestParam(value = "code", required = false) String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> data = advertService
					.getBannerListByGroupOrCode(group, code);
			map.put("code", "200");
			map.put("data", data);
		} catch (Exception e) {
			map.put("code", "400");
			map.put("message", "banner 查询失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}

	/**
	 * @Title: headline
	 * @Description: 查询头条列表
	 * @return Map<String,Object> 返回类型
	 * @author wyy 2016/09/07
	 * @since
	 * @throws
	 */
	@RequestMapping(value = "/headline", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> headlineList() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			headlineDao.updateInvalid();
			map.put("code", "200");
			map.put("data", headlineDao.getInvalidList());
		} catch (Exception e) {
			map.put("code", "400");
			map.put("message", "banner 查询失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}

}
