package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.Category;
import com.tyiti.easycommerce.entity.Sku;

public interface CategoryService {

	List<Category> getAllCategorys();

	void add(Category category);

	void edit(Category category);

	void del(Integer id);

	List<Category> getListByParentId(int parentId);

	Category getCategoryById(int id);

	void toggleStatus(Integer id);

	void rank(int id, int rank);

	List<Sku> getSkusByCategory(int id, boolean recursive);

	 /**
	  * <p>功能描述：根据名字查询栏目。</p>	
	  * @param string
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月25日 下午4:26:22。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Category findKooCategoryCode(String name);

	 /**
	  * <p>功能描述：。</p>	
	  * @param categoryId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月25日 下午5:04:05。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Category selectByPrimaryKey(Integer categoryId);

	 /**
	  * <p>功能描述：。</p>	
	  * @return
	  * <p>创建日期:2016年8月29日 下午7:31:16。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Category> getKooAllCategorys();


}
