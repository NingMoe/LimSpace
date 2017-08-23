package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.PickupPointUser;
import com.tyiti.easycommerce.service.PickupPointService;
import com.tyiti.easycommerce.service.PickupPointUserService;
import com.tyiti.easycommerce.util.Md5;

@Controller
@RequestMapping("/pickupPointUser")
@Scope("prototype")
public class PickupPointUserController {
	@Autowired
	private PickupPointUserService pickupPointUserService;

	@Autowired
	private PickupPointService pickupPointService;
	

	/**
	 * 
	 * @Title: login
	 * @Description: 自提登录
	 * @param user
	 * @param session
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @author hcy
	 * @date 2016年6月29日 下午5:14:54
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> login(@RequestBody PickupPointUser user,
			HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if (user.getLoginName() == null
					|| "".equals(user.getLoginName().trim())) {
				throw new Exception("登录名不能为空!");
			}
			if (user.getPassword() == null
					|| "".equals(user.getPassword().trim())) {
				throw new Exception("密码不能为空!");
			}
			PickupPointUser pickupPointUser = new PickupPointUser();
			pickupPointUser.setLoginName(user.getLoginName());
			pickupPointUser.setPassword(new Md5().getMD5ofStr(user
					.getPassword()));
			PickupPointUser pickupPoint = pickupPointUserService
					.selectForObject(pickupPointUser);
			if (pickupPoint == null) {
				throw new Exception("用户名或密码错误");
			}
			List<PickupPoint> points=new ArrayList<PickupPoint>();
			List<String> list = new ArrayList<String>();
			if (pickupPoint.getType() == 1) {
				PickupPoint point = pickupPointService
						.selectPickupPointById(Integer.parseInt(pickupPoint
								.getPickupPointId()));
				//禁用自提点
				if(point==null){
					data.put("code", "400");
					data.put("exception", "自提点已被禁用");
					return data;
				} else {
					list.add(point.getName());
				}
			} else {
				String[] ids = pickupPoint.getPickupPointId().split(",");
				
				for (String id : ids) {
					PickupPoint point = pickupPointService
							.selectPickupPointById(Integer.parseInt(id));
					if(point==null){
						data.put("code", "400");
						data.put("exception", "自提点已被禁用");
						return data;
					} else {
						list.add(point.getName());
						points.add(point);
					}
				}
			}
			data.put("pickupPointNames", list);
			data.put("pickupPointUser", pickupPoint);
			data.put("pickupPoins", points);
			Constants.pointUserMap.put(request.getSession().getId(),pickupPoint) ; 
			//GetSession.getSession().setAttribute(Constants.POINT_USER, pickupPoint);
			data.put("code", "200");
			data.put("message", "登录成功");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", "400");
			data.put("exception", e.getMessage());
		}
		return data;

	}

	/**
	 * 
	 * @Title: exit
	 * @Description: 退出登录
	 * @param session
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @author hcy
	 * @date 2016年7月4日 下午1:41:31
	 */
	@RequestMapping(value = "/exit")
	@ResponseBody
	public Map<String, Object> exit(HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			//GetSession.getSession().removeAttribute(Constants.POINT_USER);
			//session.invalidate();
			Constants.pointUserMap.remove(request.getSession().getId());
			data.put("code", "200");
			data.put("message", "退出成功！");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", "400");
			data.put("message", "退出失败");
			data.put("exception", e.getMessage());
		}
		return data;
	}

}
