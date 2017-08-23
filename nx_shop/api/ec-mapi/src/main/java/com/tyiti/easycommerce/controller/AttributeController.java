package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Attribute;
import com.tyiti.easycommerce.service.AttributeService;

/**
* @ClassName: AttributeController
* @Description: TODO(属性控制器)
* @author Yan Zuoyu
* @date 2016-4-19 下午1:46:08
*
 */
@Controller
public class AttributeController {
	@Autowired
	private AttributeService attributeService;
	
    @RequestMapping(value = "/attributes", method = RequestMethod.POST, headers = { "Content-type=application/json" })
    @ResponseBody
    public Map<String, Object> add(@RequestBody Attribute attribute  ) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	int num =  attributeService.selectCountByName(attribute.getName());
        	if(num==0){
        		attributeService.addAttribute(attribute);
        		map.put("code", "200");
            	map.put("data", attribute);
        	} else{
        		map.put("code", "400");
            	map.put("message", "重复添加");
        	}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "添加失败");
            map.put("exception", e.getMessage());
		}
        return map;
    }
    
    
    @RequestMapping(value = "/attributes", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> edit(@RequestBody Attribute attribute ) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	attributeService.update(attribute);
        	map.put("code", "200");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "修改失败");
            map.put("exception", e.getMessage());
		}
        return map;
    }
    
    /**
     * @Title: attributeList
     * @Description: TODO(获取列表)
     * @return Map<String,Object>    返回类型
     * @author Yan Zuoyu
     * @throws
      */
     @RequestMapping(value = "/attributes/list", method = RequestMethod.GET)
     @ResponseBody
     public Map<String, Object> attributeList() {
         Map<String, Object> map = new HashMap<String, Object>();
         try {
         	List<Attribute> attributeList = 	attributeService.attributeList();
         	map.put("code", "200");
         	map.put("data", attributeList);
         	map.put("message", "ok");
 		} catch (Exception e) {
 			// TODO: handle exception
 			map.put("code", "400");
 			map.put("message", "查询列表失败");
             map.put("exception", e.getMessage());
 		}
         return map;
     }
}
