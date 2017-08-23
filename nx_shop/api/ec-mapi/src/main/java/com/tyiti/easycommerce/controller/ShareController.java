package com.tyiti.easycommerce.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.text.SimpleDateFormat;
import com.mysql.jdbc.log.Log;
import com.tyiti.easycommerce.entity.Share;
import com.tyiti.easycommerce.service.ShareService;
import com.tyiti.easycommerce.service.impl.ShareServiceImpl;

@Controller
@RequestMapping("/share")
public class ShareController {

	@Autowired
	ShareService shareService;
	
	Logger logger = Logger.getLogger(ShareController.class);
	
	/**
	 * @Title: 获取分享数据
	 * @Description: 查询
	 * @return Map<String,Object> 返回类型
	 * @RequestParam(value = "share", required = false)
	 * @author wyy 2016/08/05
	 * @throws
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShareList(
			@RequestParam Map<String, Object> map) {
		Share share = mapToShare(map);
		return shareService.getShareList(share);
	}

	/**
	 * @Title: 获取分享数据
	 * @Description: 查询
	 * @return Map<String,Object> 返回类型
	 * @RequestParam(value = "share", required = false)
	 * @author wyy 2016/08/08
	 * @throws
	 */
	@RequestMapping(value = "/execl", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> toExecl(HttpServletResponse response,
			HttpServletRequest request, @RequestParam Map<String, Object> map) {
		Share share = mapToShare(map);
		return shareService.shareToExecl(response, request, share);
	}

	public Share mapToShare(Map<String, Object> map) {
		Share share = new Share();
		try {
			if (map.get("dateType") != null && !"".equals(map.get("dateType"))) {
				share.setDateType(map.get("dateType").toString());
			}
			if (map.get("mobile") != null && !"".equals(map.get("mobile"))) {
				share.setMobile(map.get("mobile").toString());
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if (map.get("stratTime") != null
					&& !"".equals(map.get("stratTime"))) {
				share.setStratTime(format
						.parse(map.get("stratTime").toString()));
			}
			if (map.get("endTime") != null && !"".equals(map.get("endTime"))) {
				share.setEndTime(format.parse(map.get("endTime").toString()));
			}
			
			if(share.getStratTime()==null && share.getEndTime()!=null){
				share.setStratTime(format.parse("2010-09-01"));
			}			
			if(share.getStratTime()!=null && share.getEndTime()==null){
				share.setEndTime(new Date());
			}		

			if (map.get("limit") != null && !"".equals(map.get("limit"))) {
				share.setLimit(Integer.parseInt(map.get("limit").toString()));
			}
			if (map.get("offset") != null && !"".equals(map.get("offset"))) {
				share.setOffset(Integer.parseInt(map.get("offset").toString()));
			}
		} catch (Exception e) {
			logger.error("分享注册获取列表异常捕获：",e);			
		}
		return share;
	}

}