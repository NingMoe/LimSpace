package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.Tag;
import com.tyiti.easycommerce.entity.TagSku;

public interface TagDao {

	@Select("select * from t_tag where id = #{id}")
	@ResultMap("BaseResultMap")
	Tag getById(@Param("id") Integer id);

	List<Tag> getListByParentId(@Param("parentId") Integer parentId, @Param("level") Integer level);

	List<Tag> getSkuListByParentId(@Param("parentId") Integer parentId, @Param("level") Integer level, @Param("limit") Integer limit);
	
    /**
     * 获取商品的分类Id 列表
     * @author :xulihui
     * @since :2016年4月15日 下午6:30:48
     * @param param
     * @return
     */
	List<Map<String, Object>> selectTagLists(Map<String, Object> param);
    /**
     * 获取查询分页的总数
     * @author :xulihui
     * @since :2016年4月15日 下午6:31:14
     * @return
     */
	long selectTagCount(Map<String, Object> param);
	/**
	* @Title: getTagIdByCode
	* @Description: TODO(根据code 获取tagId)
	* @return int    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	int getTagIdByCode(String code);
	/**
	* @Title: getByParentId
	* 根据parentId 获取标签
	* @return List<Tag>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	List<Tag> getByParentId(Integer parentId);

	Integer getTagSkus(Map<String, Object> param);

	 /**
     * 通过SKUID  获取Tag实体
     * @author :wyy
     * @since :2016年7月5日 
     * @param param
     * @return
     */
	@Select("select * from t_tag where sku_id= #{skuId}")
	@ResultMap("BaseResultMap")
	Tag getBySkuId(@Param("skuId") Integer skuId);
	
	/**
	 * 
	 * @Title: getTagSkuBySkuId 
	 * @Description: 查询TagSku
	 * @param skuId
	 * @return  
	 * @return List<TagSku>  
	 * @throws
	 * @author hcy
	 * @date 2016年8月11日 上午9:36:49
	 */
	List<TagSku> getTagSkuBySkuId(Integer skuId);
	
}
