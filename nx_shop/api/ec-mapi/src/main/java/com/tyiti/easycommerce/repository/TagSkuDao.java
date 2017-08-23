package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tyiti.easycommerce.entity.TagSku;

public interface TagSkuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TagSku record);

    int insertSelective(TagSku record);

    TagSku selectByPrimaryKey(Integer id);
    
    @Select("SELECT id AS id, tag_id AS tagId, sku_id AS skuId, invalid AS invalid, rank AS rank, create_time AS createTime, update_time AS updateTime FROM t_tag_sku WHERE tag_id = #{tagId}")
    List<TagSku> getByTagId(@Param("tagId") Integer tagId);

    int updateByPrimaryKeySelective(TagSku record);

    int updateByPrimaryKey(TagSku record);

	void deleteByTagId(Map<String, Object> params);

	/**
	 * 逻辑删除标签到SKU的关联
	 * @param tagId 标签ID
	 */
	@Update("UPDATE t_tag_sku SET invalid = 1, rank = NULL WHERE tag_id = #{tagId}")
	void invalidateByTagId(@Param("tagId") Integer tagId);
	
	/**
	 * 新增标签到SKU的关联，包括维护 rank
	 * @param tagSkus TagSku 列表
	 */
	void insertRows(@Param("tagSkus") List<TagSku> tagSkus);
	
	/**
	 * 更新标签到SKU的关联，包括维护 rank
	 * @param tagSkus TagSku 列表
	 */
	void updateRows(@Param("tagSkus") List<TagSku> tagSkus);
}