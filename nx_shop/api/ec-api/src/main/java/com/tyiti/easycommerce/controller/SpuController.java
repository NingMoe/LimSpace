package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Spu;
import com.tyiti.easycommerce.entity.SpuAttribute;
import com.tyiti.easycommerce.service.SpuService;

@Controller
public class SpuController {
	@Autowired
	private SpuService spuService;

	@RequestMapping(value = "/spu", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> add(@RequestBody Spu spu,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		/*
		 * List<SpuAttribute> spuAttributeList = null; if (attrs != null) {
		 * spuAttributeList = attrs.getSpuAttributeList(); }
		 */
		spu = spuService.addSpu(spu);
		data.put("data", spu);
		return data;
	}

	@RequestMapping(value = "/spu/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		Spu spu = spuService.getById(id);
		List<SpuAttribute> makeAttrs = new ArrayList<SpuAttribute>();
		List<SpuAttribute> attrs = spu.getAttrs();
		for (SpuAttribute spuAttribute : attrs) {
			String attrValues = spuAttribute.getAttrValues();
			if (attrValues != null) {
				boolean flag = true;
				try {
					JSONObject.fromObject(attrValues);
				} catch (JSONException e) {
					flag = false;
				}
				if (flag) {
					JSONObject attrJson = JSONObject.fromObject(attrValues);
					if (attrJson != null) {
						String convertAttr = attrJson.getString("values");
						spuAttribute.setAttrValues(convertAttr);
						makeAttrs.add(spuAttribute);
					}
				}
			}
		}
		if (makeAttrs.size() > 0) {
			spu.setAttrs(makeAttrs);
		}
		data.put("data", spu);
		return data;
	}

	@RequestMapping(value = "/spu", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getByCriteria(Spu spu,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Spu> spuList = spuService.getByCriteria(spu);
		data.put("data", spuList);
		return data;
	}
}
