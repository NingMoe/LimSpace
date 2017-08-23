 /**
  * 文件名[fileName]：KooCourseServiceImpl.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年6月3日 下午8:19:29
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.KooCourse;
import com.tyiti.easycommerce.entity.SkuExt;
import com.tyiti.easycommerce.repository.KooCourseDao;
import com.tyiti.easycommerce.repository.SkuExtDao;
import com.tyiti.easycommerce.service.KooCoursesService;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年6月3日 下午8:19:29。</p>
  */
@Service
public class KooCourseServiceImpl implements KooCoursesService{
	@Autowired
	private KooCourseDao KooCourseDao;
	@Autowired
	SkuExtDao skuExtDao;
	/***
	  * <p>功能描述:。</p>	
	  * @param skuId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月3日 下午8:20:44。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	public SkuExt findSkuExtSkuId(Integer skuId) {
		int type=1;
		return skuExtDao.findKooProductId(type,skuId);
	}
	/***
	  * <p>功能描述:。</p>	
	  * @param productId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月3日 下午8:21:17。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	public Map<String, Object> findListById(String strProductId) {
		Map<String, Object> res = new HashMap<String, Object>();
		List<KooCourse> treeCourses = new ArrayList<KooCourse>();
		if(strProductId.indexOf("_")!=-1){
			String productId = strProductId.split("_")[0];
			List<KooCourse> courses = KooCourseDao.selectByProductId(productId);
			for (KooCourse course : courses) {
				if(course.getKooParentId() == null || "".equals(course.getKooParentId()) || course.getKooParentId() == 0){
					treeCourses.add(buildTree(courses, course));
				}
			}
			res.put("code", 200);
			res.put("data", treeCourses);
		}else{
			res.put("code", 400);
			res.put("data", treeCourses);
		}
		return res;
	}
	private KooCourse buildTree(List<KooCourse> courses, KooCourse course) {
		List<KooCourse> children = getChildren(courses, course.getKooId());
		if (!children.isEmpty()) {
			for (KooCourse child : children) {
				buildTree(courses, child);
			}
			course.setChildrenCourses(children);
		}
		return course;
	}

	private List<KooCourse> getChildren(List<KooCourse> courses, Integer id) {
		List<KooCourse> children = new ArrayList<KooCourse>();
		for (KooCourse child : courses) {
			if (id.equals(child.getKooParentId())) {
				children.add(child);
			}
		}
		return children;
	}
}
