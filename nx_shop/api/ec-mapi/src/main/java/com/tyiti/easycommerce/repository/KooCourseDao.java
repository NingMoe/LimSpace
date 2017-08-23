package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.KooCourse;

public interface KooCourseDao {
	int deleteByPrimaryKey(Integer id);

	int insert(KooCourse record);
	
	int insertSelective(KooCourse record);
	
	KooCourse selectByPrimaryKey(Integer id);
	
	int updateByPrimaryKeySelective(KooCourse record);
	
	int updateByPrimaryKey(KooCourse record);

	 /**
	  * <p>功能描述：新东方根据产品ID，确定课程是否已经插入。</p>	
	  * @param product_id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月20日 下午2:45:16。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	KooCourse findCoursesProductId(String product_id);

	 /**
	  * <p>功能描述：。</p>	
	  * @param coursesList
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月26日 上午10:37:47。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	void insertRows(@Param("coursesList") List<KooCourse> coursesList);

	 /**
	  * <p>功能描述：。</p>	
	  * @param kooProductId
	  * @param kooId
	 * @param kooParentId 
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月26日 下午2:53:33。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
//	KooCourse findCourses(@Param("kooProductId")String kooProductId,@Param("kooId")Integer kooId);
	List<KooCourse> findCourses(@Param("kooProductId")String kooProductId,@Param("kooId")Integer kooId);
}