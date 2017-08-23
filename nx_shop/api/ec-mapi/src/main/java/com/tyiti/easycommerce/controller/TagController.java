package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.Tag;
import com.tyiti.easycommerce.service.TagService;
import com.tyiti.easycommerce.util.TreeModel;

@Scope("prototype")
@Controller
@RequestMapping(value = "", produces = "application/json")
public class TagController {
	
	@Autowired
	private TagService tagService ;
	
	/**
	 * 获取 Tag 本身信息，不包含子节点
	 * @param id tagId
	 * @return
	 */
	@RequestMapping(value = "/tags/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> tagList(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Tag tag = tagService.getById(id);
		
		map.put("code", 200);
		map.put("data", tag);

		return map;
	}

	@RequestMapping(value="/tags",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> tagList(@Param("parentId") int parentId  ){
		Map<String,Object> map = new  HashMap<String,Object>();
		try {
			List<TreeModel> treeList = TreeMenu(tagService.getAllTags(),parentId);
			map.put("code", "200");
			map.put("data",treeList);
		} catch (Exception e) {
			// TODO: handle exception
			
			map.put("code", "400");
			map.put("exception",e.getMessage());
		}
		
		
		 return map ;
	}
	private List<TreeModel> TreeMenu(List<Tag> tagList , int parentId){
		List<TreeModel> listTree = new ArrayList<TreeModel>();
		for (Tag tag : tagList) {
			TreeModel treeModel = new TreeModel();
			Integer id = tag.getId();
			treeModel.setId(id);
			treeModel.setParentId(tag.getParentId());
			treeModel.setText(tag.getName());
			treeModel.setRank(tag.getRank());
			treeModel.setStatus(tag.getStatus());
			treeModel.setCode(tag.getCode());
			treeModel.setTreeDepth(tag.getTreeDepth());
			treeModel.setIcon(tag.getIcon());
			 if(parentId==tag.getParentId()){
				 List<TreeModel> treeNodes = TreeMenu(tagList,tag.getId());
				 treeModel.setNodes(treeNodes);
				 listTree.add(treeModel);
			 }
		}
		return listTree;
	}
	
	/**
	* @Title: tagAdd
	* @Description: TODO(添加)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String,Object> tagAdd(@RequestBody Tag tag){
		tag.setCreateTime(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			 map =	tagService.add(tag);
		} catch (Exception e) {
			map.put("code", "400");
			map.put("exception", e.getMessage());
			return map;
		}
		 
		return map;
	}

	/**
	* @Title: tagEdit
	* @Description: TODO(修改)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> tagEdit(@RequestBody Tag tag) {
		Map<String, Object> map = tagService.edit(tag);
		return map;
	}
	
	@RequestMapping(value = "/tags/{id}/status", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> tagStatusToggle(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			tagService.toggleStatus(id);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			map.put("code", "400");
			map.put("message", "操作失败");
			map.put("exception", e.getMessage());
			return map;
		}

		return map;
	}

	/**
	 * 删除标签
	 * @param id 标签id
	 * @return
	 */
	@RequestMapping(value = "/tags/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> tagDel(@PathVariable("id") Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();

		boolean res = tagService.invalidateById(id);
		map.put("code", res ? 200 : 400);
		return map;
	}

	/**
	* @Title: tagRank
	* @Description: TODO(排序)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/tags/{id}/ranks/{rank}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> tagRank(@PathVariable int id,
			@PathVariable Integer rank) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			tagService.rank(id ,rank);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			map.put("code", "400");
			map.put("message", "排序失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}

	/**
	 * (全量)保存标签关联的SKU列表
	 * @param id 标签ID
	 * @param skuIds SKU ID数组
	 * @return
	 */
	@RequestMapping(value = "/tags/{id}/skus", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String,Object> saveTagSkus(@PathVariable int id, @RequestBody List<Integer> skuIds){
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			tagService.saveTagSkus(id, skuIds);
			map.put("code", 200);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("message", "编辑失败");
			map.put("exception", e.getMessage());
		}

		return map;
	}
	
    @RequestMapping(value = "/tags/{id}/skus", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSkusByTag(@PathVariable("id") int id) {
        Map<String, Object> map = new HashMap<String, Object>();
        //先写出四级分类 如果不够用再加
        List<Sku> skus = tagService.getSkusByTag(id);
        map.put("code", 200);
        map.put("data", skus);
        return map;
    }
    
 }

