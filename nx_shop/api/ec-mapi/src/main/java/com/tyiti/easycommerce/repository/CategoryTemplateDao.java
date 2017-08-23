package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.CategoryTemplate;

public interface CategoryTemplateDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CategoryTemplate record);

    int insertSelective(CategoryTemplate record);

    CategoryTemplate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CategoryTemplate record);

    int updateByPrimaryKey(CategoryTemplate record);

	List<CategoryTemplate> getTemplateListByCategoryId(int categoryId);

	void deleteByCategoryId(Integer categoryId);

	void deleteByCategoryType(@Param("categoryId") Integer categoryId,@Param("type") int type);

	void deleteNotInByIds(@Param("ids") int[] ids,@Param("categoryIds") int[] categoryIds,@Param("types") int[] types);

	List<CategoryTemplate> getCheckList(@Param("categoryId") int category,@Param("type") int type,@Param("keyId") int keyId);
}