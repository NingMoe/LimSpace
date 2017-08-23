package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Activity;

public interface ActivityDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Activity record);

    int insertSelective(Activity record);

    Activity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);

	 /**
	  * <p>功能描述：查找该类型的所有活动。</p>	
	  * @param activityType
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月20日 上午11:52:37。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findActivityList(@Param("activityType")Integer activityType);
}