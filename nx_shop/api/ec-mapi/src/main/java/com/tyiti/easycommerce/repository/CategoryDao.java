package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tyiti.easycommerce.entity.Category;
import com.tyiti.easycommerce.entity.Sku;

public interface CategoryDao {
    int deleteByPrimaryKey(Integer id);


    void insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);


	List<Category> getAllCategorys();


	List<Category> getListByParentId(int parentId);


	void updateOtherRank(Category category);


	int getMaxRank(Integer parentId);




	int updateSortOwn(Category categoryOwn);



	int updateSortOtherDown(@Param("rank") int rank,@Param("parentId") Integer parentId,@Param("toRank") int toRank);


	int updateSortOtherUp(@Param("rank") int rank,@Param("parentId") Integer parentId,@Param("toRank") int toRank);

 

	@Update("UPDATE t_category SET status = !status WHERE id = #{id}")
	void toggleStatus(Integer id);

	//拼接子节点id
	String getIdsByCategoryId(int categoryId);

	@Select("SELECT k.* FROM t_sku k LEFT JOIN t_spu p ON k.spu_id = p.id LEFT JOIN t_category c ON p.category_id = c.id WHERE k.invalid = 0 AND c.id = #{id}")
	List<Sku> getSkusByCategory(@Param("id") int id);

	 /**
	  * <p>功能描述：。</p>	
	  * @param name
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月25日 下午4:46:25。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Category findKooCategoryCode(@Param("name")String name);


	 /**
	  * <p>功能描述：。</p>	
	  * @return
	  * <p>创建日期:2016年8月29日 下午7:31:48。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Category> getKooAllCategorys();
}
