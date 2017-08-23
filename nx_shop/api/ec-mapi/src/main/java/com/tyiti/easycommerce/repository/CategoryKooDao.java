package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.CategoryKoo;

public interface CategoryKooDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CategoryKoo record);

    int insertSelective(CategoryKoo record);

    CategoryKoo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CategoryKoo record);

    int updateByPrimaryKey(CategoryKoo record);

	 /**
	  * <p>功能描述：。</p>	
	  * @param kooId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月23日 下午2:00:15。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	CategoryKoo findKooCategoryById(String kooId);

	 /**
	  * <p>功能描述：查询系统当中对应的Category_Id,或者是Tag_id,对应的Koo_Category_id。</p>	
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月24日 上午11:06:31。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<CategoryKoo> findListKooCategoryList();


}