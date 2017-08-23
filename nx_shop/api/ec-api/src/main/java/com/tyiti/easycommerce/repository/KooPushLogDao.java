package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.KooPushLog;

public interface KooPushLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(KooPushLog record);

    int insertSelective(KooPushLog record);

    KooPushLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KooPushLog record);

    int updateByPrimaryKeyWithBLOBs(KooPushLog record);

    int updateByPrimaryKey(KooPushLog record);
	 /**
	  * <p>功能描述：根据订单ID查询推送消息。</p>	
	  * @param id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月12日 上午9:21:51。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
   @Select("select id ,  order_id as orderId,user_id as userId,push_str ,push_time as pushTime,is_success as isSuccess,create_time as createTime ,update_time as updateTime  from t_koo_push_log where t_koo_push_log.order_id=#{order_id}")
	KooPushLog findByOrderId(@Param("order_id")Integer order_id);

	 /**
	  * <p>功能描述：查询所有没有推送从新的。</p>	
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月13日 下午6:30:15。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
   @Select("select id ,  order_id as orderId,user_id as userId,push_str ,push_time as pushTime,is_success as isSuccess,create_time as createTime ,update_time as updateTime  from t_koo_push_log where t_koo_push_log.is_success =0")
	List<KooPushLog> findByKooPushLogList();
}