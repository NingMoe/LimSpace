package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;
import com.tyiti.easycommerce.entity.Banner;
import com.tyiti.easycommerce.service.BannerService;

@Scope("prototype")
@Controller
public class BannerController {

	@Autowired
	private BannerService bannerSerive ; 
	
	@RequestMapping(value = "/imgupload", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		SimpleDateFormat formatymd = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat formatHms = new SimpleDateFormat("HHmmSS");
		String path = "image/" + formatymd.format(new Date());
//		String basePath = request.getSession().getServletContext()
//				.getRealPath(path);
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.indexOf("."));
		Long fileSize = file.getSize();
		if (fileSize > 5120000) {
			resultMap.put("code", 400);
			resultMap.put("message", "上传图片不得超过5M");
			return resultMap;
		}
		List<String> allowSuffix = new ArrayList<String>();
		allowSuffix.add(".jpg");
		allowSuffix.add(".png");
		allowSuffix.add(".gif");
		allowSuffix.add(".jpeg");
		if (!allowSuffix.contains(suffix.toLowerCase())) {
			resultMap.put("code", 400);
			resultMap.put("message", "上传图片类型必须是.gif,jpeg,jpg,png中的一种");
			return resultMap;
		}

		String picId = formatHms.format(new Date());
		fileName = picId + suffix;
		 
		try {
			// 保存到本服务器上
			// file.transferTo(targetFile);
			resultMap.put("original", file.getOriginalFilename());
			resultMap.put("size", file.getSize());
			resultMap.put("type", suffix);
			resultMap.put("url", path + "/" + fileName);
			resultMap.put("title", fileName);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("code", 400);
			resultMap.put("message", "图片上传失败,请联系管理员");
		}
		
			
		return resultMap;
	}
	
	@RequestMapping(value = "/banners", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> insertBanners(@RequestBody Banner banner){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			bannerSerive.insertBanner(banner);
			map.put("code", "200");
			map.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "添加不成功");
			map.put("exception", e.getMessage());
		}
		
		 
		return map;
	}
	@RequestMapping(value = "/banners", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> updateBanner(@RequestBody Banner banner){
		Map<String,Object> map = new HashMap<String,Object>();
		int num = bannerSerive.updateBanner(banner);
		if(num !=1){
			map.put("code", "400");
			map.put("message", "修改不成功");
		}else {
			map.put("code", "200");
			map.put("message", "OK");
		}
		return map;
	}
	
	@RequestMapping(value = "/banners/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String,Object> delBanner(@PathVariable("id") Integer id ){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			bannerSerive.delBanner(id);
			map.put("code", 200);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "删除失败");
			map.put("exception", e.getMessage());
		}
		 
		return map;
	}
	@RequestMapping(value = "/banners/sort", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String,Object> sortBanner(@RequestBody Banner banner){
		Map<String,Object> map = new HashMap<String,Object>();
		int result = 0 ; 
		try {
			result=  bannerSerive.sortBanner(banner);
			if(result ==1){
				map.put("code", 200);
				map.put("message", "OK");
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", e.getMessage());
		}
		return map;
	}
	
	 
	@RequestMapping(value = "/banners/ad/{adId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> bannersAd(@PathVariable("adId") Integer adId ){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
				List<Map<String,Object>> bannerList= bannerSerive.bannerList(adId);
				map.put("code", 200);
				map.put("message", "OK");
				map.put("data", bannerList);
			} catch (Exception e) {
				// TODO: handle exception
				map.put("code", 400);
				map.put("message", e.getMessage());
			}
		return map;
	}
	
	@RequestMapping(value = "/banners/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> banners(@PathVariable("id") Integer id ){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
				Banner banner = bannerSerive.selectBanner(id);
				map.put("code", 200);
				map.put("data", banner);
			} catch (Exception e) {
				// TODO: handle exception
				map.put("code", 400);
				map.put("message", e.getMessage());
			}
		return map;
	}
	 
}
