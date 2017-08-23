package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Attribute;
import com.tyiti.easycommerce.service.AttributeService;

@Controller
public class AttributeController {
	@Autowired
	private AttributeService attributeService;
	
    @RequestMapping(value = "/attribute", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(Attribute attribute, HttpServletResponse response,
            HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        attribute = attributeService.addAttribute(attribute);
        data.put("data", attribute);
        return data;
    }
    
    @RequestMapping(value = "/attribute/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getByName(@PathVariable("name") String name, HttpServletResponse response,
            HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        Attribute attribute = attributeService.getByName(name);
        data.put("data", attribute);
        return data;
    }
    
    @RequestMapping(value = "/attribute", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getByCriteria(Attribute attribute, HttpServletResponse response,
            HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Attribute> attributeList = attributeService.getByCriteria(attribute);
        data.put("data", attributeList);
        return data;        
    }
}
