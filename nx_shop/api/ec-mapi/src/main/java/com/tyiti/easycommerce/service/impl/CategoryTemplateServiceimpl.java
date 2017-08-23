package com.tyiti.easycommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.CategoryTemplate;
import com.tyiti.easycommerce.repository.CategoryTemplateDao;
import com.tyiti.easycommerce.service.CategoryTemplateService;
import com.tyiti.easycommerce.util.exception.CommonException;
@Service("categoryTemplateService")
public class CategoryTemplateServiceimpl implements CategoryTemplateService{

	@Autowired
	private CategoryTemplateDao categoryTemplateDao;

	@Override
	public void updateTemplate(CategoryTemplate categoryTemplate) {
		// TODO Auto-generated method stub
		categoryTemplateDao.updateByPrimaryKeySelective(categoryTemplate);
	}

	@Override
	public CategoryTemplate selectTemplate(int id) {
		// TODO Auto-generated method stub
		return categoryTemplateDao.selectByPrimaryKey(id);
	}

	@Override
	public void insertTemplate(List<CategoryTemplate> categoryTemplateList) {
		//1.循环列表 判断是添加还是修改  2.删除不存在的列
		//ｎｏｔ　ｉｎ　删除不需要的列　ｉｄｓ
		int ids[] = new int[categoryTemplateList.size()],categoryIds[] = new int[categoryTemplateList.size()] ,types[] = new int[categoryTemplateList.size()], i = 0 ;
		for (CategoryTemplate categoryTemplate : categoryTemplateList) {
			int categoryId = categoryTemplate.getCategoryId();
			int type = categoryTemplate.getType();
			int keyId = categoryTemplate.getKeyId();
			String value = categoryTemplate.getValue();
			if(value==null ||value.equals("")){
				throw new CommonException("不能填写空值");
			}
			int id = 0 ; 
			List<CategoryTemplate>   checkList = categoryTemplateDao.getCheckList(categoryId, type , keyId);
			if(checkList.size()>1 ){
				throw new CommonException("模板属性存储出错,请管理员解决");
			}
			if(checkList.size()==1){
				//此处执行修改 功能
				CategoryTemplate  categoryWillUpdate= checkList.get(0); 
				 id = categoryWillUpdate.getId() ; 
				categoryTemplate.setId(id);
				categoryTemplateDao.updateByPrimaryKeySelective(categoryTemplate);
			}else{
				//此处执行添加功能
				categoryTemplateDao.insertSelective(categoryTemplate);
				id = categoryTemplate.getId();
			}
			 if(i >19){
				 throw new CommonException("添加数量过多， 最多20 条");
			 }
			ids[i] =  id ; 
			categoryIds[i] = categoryId; 
			types[i] = type ;
			i++;
		}
		categoryTemplateDao.deleteNotInByIds(ids ,categoryIds ,types );
		
	}

	@Override
	public void deleteTemplate(int categoryId) {
		// TODO Auto-generated method stub
		categoryTemplateDao.deleteByCategoryId(categoryId);
	}

	@Override
	public List<CategoryTemplate> getTemplateList(int categoryId) {
		// TODO Auto-generated method stub
		return categoryTemplateDao.getTemplateListByCategoryId(categoryId);
	}

	@Override
	public void deleteByCategoryType(Integer categoryId, int type) {
		// TODO Auto-generated method stub
		  categoryTemplateDao.deleteByCategoryType(categoryId ,type);
	}

}
