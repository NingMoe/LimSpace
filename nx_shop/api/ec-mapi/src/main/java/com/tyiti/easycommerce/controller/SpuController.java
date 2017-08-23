package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Spu;
import com.tyiti.easycommerce.service.SpuService;

/**
* @ClassName: SpuController
* @Description: TODO(品牌管理)
* @author Yan Zuoyu
* @date  
*
 */
@Scope("prototype")
@Controller
public class SpuController {
	@Autowired
	private SpuService spuService;
	
	/**
	* @Title: search
	* @Description: TODO(查询列表)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	* 
	 */
    @RequestMapping(value = "/spus", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> list(@RequestParam Map<String, Object> param,
    		HttpServletResponse response, HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        try {
        	SearchResult<Map<String, Object>> spusList =   spuService.search(param);
        	data.put("code", 200);
            data.put("data", spusList);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 400);
			data.put("message", "查询列表失败");
	        data.put("exception", e.getMessage());
		}
        return data;
    }
	/**
	* @Title: add
	* @Description: TODO(增加一条记录)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	* 
	 */
    @RequestMapping(value = "/spus", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    @ResponseBody
	public Map<String, Object> add(@RequestBody Spu spu,
    		HttpServletResponse response, HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
        	spuService.add(spu);
        	data.put("code", 200);
            data.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 400);
			data.put("message", "添加失败");
	        data.put("exception", e.getMessage());
		}
        return data;
    }
    /**
    * @Title: getById
    * @Description: TODO(根据主键查询)
    * @return Map<String,Object>    返回类型
    * @author Yan Zuoyu
    * @throws
     */
    @RequestMapping(value = "/spus/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getById(@PathVariable("id") Integer id, HttpServletResponse response,
            HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
        	Spu spu = spuService.getById(id);
        	data.put("data", spu);
        	data.put("code", 200);
        	data.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("exception", e.getMessage());
        	data.put("code", 400);
        	data.put("message", "查询失败，请联系管理员");
		}
        return data;
    }
    /**
    * @Title: getByCategory
    * @Description: TODO(根据所属分类查询)
    * @return Map<String,Object>    返回类型
    * @author Yan Zuoyu
    * @throws
     */
    @RequestMapping(value = "/spus/category", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getByCriteria(@RequestParam Map<String,Object> params, HttpServletResponse response,
            HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
        	SearchResult<Map<String,Object> >  result = spuService.getByParmas(params);
        	data.put("data", result);
        	data.put("code", 200);
        	data.put("message", "OK");
		} catch (Exception e) {
			data.put("exception", e.getMessage());
        	data.put("code", 400);
        	data.put("message", "查询失败，请联系管理员");
		}
        return data;    	
    }
    /**
    * @Title: edit
    * @Description: TODO(修改)
    * @return Map<String,Object>    返回类型
    * @author Yan Zuoyu
    * @throws
     */
    @RequestMapping(value = "/spus", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> edit(@RequestBody Spu spu, HttpServletResponse response,
            HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
        	spuService.update(spu);
        	data.put("code", 200);
        	data.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("exception", e.getMessage());
        	data.put("code", 400);
        	data.put("message", "修改失败，请联系管理员");
		}
        return data;    	
    }
    
    /**
    * @Title: del
    * @Description: TODO(删除)
    * @return Map<String,Object>    返回类型
    * @author Yan Zuoyu
    * @throws
     */
    @RequestMapping(value = "/spus/del", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> del(@RequestBody int id , HttpServletResponse response,
            HttpSession session) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
        	spuService.del(id);
        	data.put("code", 200);
        	data.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("exception", e.getMessage());
        	data.put("code", 400);
        	data.put("message", "删除失败，请联系管理员");
		}
        return data;    	
    }
    /**
    * @Title: spusRank
    * @Description: TODO(spu排序)
    * @return Map<String,Object>    返回类型
    * @author Yan Zuoyu
    * @throws
     */
    @RequestMapping(value = "/spus/{id}/ranks/{rank}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> spusRank(@PathVariable int id ,@PathVariable Integer rank) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			spuService.rank(id ,rank);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "排序失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}
}
