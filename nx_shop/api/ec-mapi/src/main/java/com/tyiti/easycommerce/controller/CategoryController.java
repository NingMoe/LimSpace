package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Category;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.service.CategoryService;
import com.tyiti.easycommerce.util.TreeModel;

@Scope("prototype")
@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * @Title: categoryList
	 * @Description: TODO(树结构显示)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/categorys/tree", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> categoryList(@Param("parentId") int parentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<TreeModel> treeList = TreeMenu(
					categoryService.getAllCategorys(), parentId);
			map.put("code", "200");
			map.put("data", treeList);
		} catch (Exception e) {
			// TODO: handle exception

			map.put("code", "400");
			map.put("exception", e.getMessage());
		}

		return map;
	}

	private List<TreeModel> TreeMenu(List<Category> categoryList, int parentId) {
		List<TreeModel> listTree = new ArrayList<TreeModel>();
		for (Category category : categoryList) {
			TreeModel treeModel = new TreeModel();
			Integer id = category.getId();
			treeModel.setId(id);
			treeModel.setParentId(category.getParentId());
			treeModel.setText(category.getName());
			treeModel.setRank(category.getRank());
			treeModel.setIcon(category.getIcon());
			treeModel.setHasTemplate(category.getHasTemplate());
			if (parentId == category.getParentId()) {
				List<TreeModel> treeNodes = TreeMenu(categoryList,
						category.getId());
				treeModel.setStatus(category.getStatus());
				treeModel.setNodes(treeNodes);
				listTree.add(treeModel);
			}
		}
		return listTree;
	}

	/**
	 * @Title: categoryByParentId
	 * @Description: TODO(菜单联动)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/categories/{id}/nodes", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> categoryByParentId(@PathVariable int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Category> list = categoryService.getListByParentId(id);
			map.put("code", "200");
			map.put("data", list);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("exception", e.getMessage());
		}

		return map;
	}

	/**
	 * @Title: categoryAdd
	 * @Description: TODO(添加)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/categorys", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> categoryAdd(@RequestBody Category category,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			categoryService.add(category);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "菜单添加失败");
			map.put("exception", e.getMessage());
		}

		return map;
	}

	/**
	 * @Title: category
	 * @Description: TODO(获取category详细信息)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> category(@PathVariable int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Category category = categoryService.getCategoryById(id);
			map.put("code", 200);
			map.put("data", category);
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "获取详情失败");
			map.put("exception", e.getMessage());
		}

		return map;
	}

	/**
	 * @Title: categoryEdit
	 * @Description: TODO(修改)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/categorys", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> categoryEdit(@RequestBody Category category) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			categoryService.edit(category);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "修改失败");
			map.put("exception", e.getMessage());
		}

		return map;
	}

	/**
	 * @Title: categoryDel
	 * @Description: TODO(删除 修改为不可用)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> categoryDel(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			categoryService.del(id);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "删除失败");
			map.put("exception", e.getMessage());
		}

		return map;
	}

	/**
	 * @Title: categoryRank
	 * @Description: TODO(置顶)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/categorys/{id}/ranks/{rank}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> categoryRank(@PathVariable int id ,@PathVariable Integer rank) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			categoryService.rank(id ,rank);
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
	 * @Title: categoryUse
	 * @Description: TODO(菜单启用 不启用 )
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/categories/{id}/status", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> categoryUse(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			categoryService.toggleStatus(id);
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

	@RequestMapping(value = "/categories/{id}/skus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSkusByCategory(@PathVariable("id") int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Sku> skus = categoryService.getSkusByCategory(id, true);
		map.put("code", 200);
		map.put("data", skus);
		return map;
	}
}
