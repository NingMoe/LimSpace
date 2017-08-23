package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Spu;

public interface SpuDao {
	//根据主键查询
	Spu selectByPrimaryKey(Integer id);
	//增加所有字段
	int insert(Spu spu);
	//增加部分字段
	int insertSelective(Spu spu);
	//修改过
	int updateByPrimaryKeySelective(Spu spu);
	//修改
	int updateByPrimaryKey(Spu spu);
	//删除
	int deleteByPrimaryKey(Integer id);
	//查询列表
	List<Map<String, Object>> selectListByParams(Map<String, Object> params);
	//对应查询列表 总数
	long selectCountByParams(Map<String, Object> params);
	//获取排序
	int getMaxRankByCategoryId(@Param("categoryId")	Integer categoryId);
	
	int getMaxId();
	//分类下是否有商品
	int selectCountByCategory(@Param("categoryId")	Integer categoryId);
	//商品条件查询分页列表
	List<Map<String, Object>> selectSpuList(Map<String, Object> param);
	//商品条件查询总数
	long selectSpuCount(Map<String, Object> param);
	//以下三个方法为排序操作 
	int updateSortOtherUp(@Param("rank") int rank,@Param("categoryId") Integer categoryId,@Param("toRank") int toRank);
	int updateSortOwn(Spu spuOwn);
	int updateSortOtherDown(@Param("rank") int rank,@Param("categoryId") Integer categoryId,@Param("toRank") int toRank);
	 /**
	  * <p>功能描述：根据条件查询SKU信息。</p>	
	  * @param category_id
	  * @param spu_id
	  * @param supplier_id
	  * @param isstatus
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月15日 下午3:23:15。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Spu> findSpus(@Param("categoryId")Integer category_id, @Param("spuId")Integer spu_id, @Param("supplierId")Integer suppler_id, @Param("status")Integer status);
 
}