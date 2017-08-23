package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.CouponRecord;

public interface CouponRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponRecord record);

    int insertSelective(CouponRecord record);

    CouponRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponRecord record);

    int updateByPrimaryKey(CouponRecord record);
    
    /**
     * 修改优惠券记录使用状态
     * @param id
     * @return
     */
    int updateUseStatus(@Param("id") Integer id);
    /**
     * 修改优惠券记录使用状态
     * @param custId
     * @param couId
     * @return
     */
    int updateUseStatusByUserIdAndCouId(@Param("custId") Integer custId,@Param("couId") Integer couId);
    
    /**
     * 优惠券与things关联
     */
    int refCouponAndThings(String refIds);
    /**
     * 通过手机号插入一条记录
     * @param cid
     * @return
     */
	int insertOneRecordByMobile(@Param("couId") Integer couId,@Param("mobile") String mobile); 
	/**
	 * 根据id查询单条记录
	 * @param id
	 * @return
	 */
	Map<String, Object> selectById(Integer id);
	/**
     * 分页查询优惠券列表
     * @param param
     * @return
     */
    List<Map<String, Object>> selectInfoListByPage(Map<String, Object> param);

    /**
     * 分页查询结果行数
     * @param param
     * @return
     */
	long selectInfoListCountByPage(Map<String, Object> param);
	
	/**
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	int deleteByLogic(Integer id);
	
	/**
	 * 查询用户领取优惠券数量
	 * @param couId
	 * @param mobile
	 * @return
	 */
	long selCountByCustId(@Param("couId") Integer couId,@Param("mobile") String mobile);
	/**
	 * 查询手机号是否存在
	 * @param mobile
	 * @return
	 */
	long selCountByMobile(String mobile);

	/**
	 * 根据id查询优惠券详细记录
	 * @param id
	 * @return
	 */
	Map<String, Object> selectDetailById(Integer id);
	/**
	* @Title: insertBatchRecord
	* @Description:  批量添加优惠码
	* @return void    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	void insertBatchRecord( List<CouponRecord> recordList);

}