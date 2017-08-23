package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.Tag;

public interface TagDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    Tag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

	int updateByPrimaryKeyDel(Tag tag);

	List<Tag> getTagByParentId(@Param("parentId") int parentId);

	List<Tag> getAllTags();

	int getMaxRank(Integer parentId);

	int updateSortOtherUp(@Param("rank") int rank,
			@Param("parentId") Integer parentId, @Param("toRank") int toRank);

	int updateSortOwn(Tag tag);

	int updateSortOtherDown(@Param("rank") int rank,
			@Param("parentId") Integer parentId, @Param("toRank") int toRank);
	
	@Update("UPDATE t_tag SET status = !status WHERE id = #{id}")
	void toggleStatus(Integer id);

	@Select("SELECT k.* FROM t_sku k LEFT JOIN t_tag_sku tk ON k.id = tk.sku_id WHERE tk.invalid = 0 AND tk.tag_id = #{id} ORDER BY tk.rank ASC")
	List<Sku> getSkusByTag(@Param("id") int id);
	
	/**
	 * 获取 Tag 本身信息，不包含子节点
	 * @param id tagId
	 * @return Tag
	 */
	@Select("SELECT * FROM t_tag WHERE invalid = 0 AND id = #{id}")
	Tag getById(@Param("id") int id);
	/**
	 * 获取 Tag 本身信息，不包含子节点
	 * @param id tagId
	 * @return Tag
	 */
	@Select("SELECT * FROM t_tag WHERE invalid = 0 AND code = #{code}")
	Tag findKooCode(@Param("code") String code);
	/**
	* @Title: selectCodeCount
	* @Description: TODO(增加 code 不能重复)
	* @return int    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@Select("SELECT count(*) FROM t_tag WHERE invalid = 0 AND code = #{code}")
	int selectCodeCount(@Param("code") String code);
	
	/**
	* @Title: selectCodeNum
	* @Description: TODO(修改code 不能重复)
	* @return int    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@Select("SELECT count(*) FROM t_tag WHERE invalid = 0 AND code = #{code} and id <> #{id}")
	int selectCodeNum(@Param("id") Integer id,@Param("code") String code);

	/**
	* @Title: getTagIdByCode
	* @Description: TODO(根据code 获取tagId)
	* @return int    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	Integer getTagIdByCode(String code);
	
	List<Tag> getSkuListByParentId(@Param("parentId") Integer parentId, @Param("level") Integer level, @Param("limit") Integer limit);
	
}