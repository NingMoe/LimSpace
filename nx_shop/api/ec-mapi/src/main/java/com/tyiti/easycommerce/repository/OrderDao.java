package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.util.ordersystem.Orders;

public interface OrderDao {

	Map<String, Object> getOrderStatus(@Param("id") Integer id);

	Map<String, Object> orderDetail(@Param("id") Integer id);

	/**
	 * @Title: selectOrderList
	 * @Description: TODO(获取订单条件查询列表)
	 * @return List<Map<String,Object>> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	List<Map<String, Object>> selectOrderList(Map<String, Object> param);

	/**
	 * 
	 * @Title: selectOrderCount
	 * @Description: TODO(获取条件查询总数)
	 * @return long 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	long selectOrderCount(Map<String, Object> param);

	/**
	 * @Title: selectOrderListByIds
	 * @Description: TODO(订单批量导出列表)
	 * @return List<Map<String,Object>> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	List<Map<String, Object>> selectOrderListByIds(Map<String, Object> param);
	
	/**
	 * @Title: selectOperateInfoOrderListByIds
	 * @Description: TODO(根据订单Id集合查询订单操作日志信息列表)
	 * @return List<Map<String,Object>> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	List<Map<String, Object>> selectOrderListByIdsForOperateLog(Map<String, Object> param);

	/**
	 * 
	 * @Title: selectOrderCount
	 * @Description: TODO(获取条件查询总数)
	 * @return long 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	long selectOrderCountByIds(Map<String, Object> param);

	/**
	 * @Title: updateByIds
	 * @Description: TODO(制单导出多条修改状态)
	 * @return void 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	void updateByIds(Map<String, Object> param);
	
	@Update("UPDATE t_order SET status = #{status} WHERE id = #{id}")
	int updateStatus(Order order);

	int orderCancel(Integer id);

	void orderFinish(int orderId);

	Map<String, Object> getOrderSkuCount(Integer id);

	void updateSkuCount(@Param("skuId") int skuId,
			@Param("skuCount") int skuCount);

	/**
	 * @author wyy 2016/7/7
	 * @Description:通过订单的id或者no 获取订单的部分数据
	 * @param id
	 * @param no
	 * @return
	 */
	Map<String, Object> OrderByIdOrNo(@Param("id") Integer id,
			@Param("no") String no);

	/**
	 * @author wyy 2016/7/13
	 * @Description:通过 退货订单id或者no 获取订单的部分数据
	 * @param id
	 * @param no
	 * @return
	 */
	Map<String, Object> OrderByRefundIdOrNo(@Param("id") Integer id);

	//@Select("select id,no,cust_id,stage_id,amount,down_payment,installment_amount,installment_months,installment_rate,down_payment_payed,installment_payed,pay_method,`status`,invalid,create_time from t_order t where	t.id=(select order_id from t_refund r where	r.id=#{id,jdbcType=INTEGER} LIMIT 1)")
	Order getByRefundId(Integer id);


	 /**
	  * <p>功能描述：查找全部新东订单信息。。</p>	
	 * @param erpCode 
	 * @param order 
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月8日 上午10:02:54。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findKooOrders(Map<String, Object> param);
	
	Order getById(@Param("id") Integer id);
	
	int logisticsImport(Order order);

	 /**
	  * <p>功能描述：。</p>	
	  * @param skuErpCode
	  * @param startCloseTime
	  * @param endCloseTime
	  * @param startReturnTime
	  * @param endReturnTime
	  * @param offset
	  * @param limit
	  * @return
	  * <p>创建日期:2016年9月5日 下午5:53:14。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findBillList(@Param("skuErpCode")String skuErpCode, @Param("startCloseTime")String startCloseTime,  @Param("endCloseTime")String endCloseTime, 
			@Param("startReturnTime")String startReturnTime,@Param("endReturnTime")String endReturnTime, @Param("offset")Integer offset, @Param("limit")Integer limit);

	 /**
	  * <p>功能描述：查询订单总数。</p>	
	  * @param skuErpCode
	  * @param startCloseTime
	  * @param endCloseTime
	  * @param startReturnTime
	  * @param endReturnTime
	  * @param offset
	  * @param limit
	  * @return
	  * <p>创建日期:2016年9月6日 上午9:11:09。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	long findBillTotal(@Param("skuErpCode")String skuErpCode, @Param("startCloseTime")String startCloseTime,  @Param("endCloseTime")String endCloseTime, 
			@Param("startReturnTime")String startReturnTime,@Param("endReturnTime")String endReturnTime, @Param("offset")Integer offset, @Param("limit")Integer limit);

	List<Map<String, Object>> selectOrderSkusList(Map<String, Object> param);

	long selectOrderSkusCount(Map<String, Object> param);

	 /**
	  * <p>功能描述：查找对账信息管理。</p>	
	  * @param param
	  * @return
	  * <p>创建日期:2016年9月6日 下午8:06:12。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	long findKooTotalOrders(Map<String, Object> param);
	
	/********制单导出 **************/
	List<Map<String, Object>> selectOrdersMaking(Map<String, Object> param);

	/**
	* @Title: selectOrdersToOrderSystem
	* @Description: 向订单系统中导入订单
	* @return List<Orders>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	List<Orders> selectOrdersToOrderSystem(Map<String, Object> param);
}
