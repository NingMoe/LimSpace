package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.LeaveMessage;

/**
 * @author wangqi
 * @date 2016-4-22 上午10:40:09
 * @description 用户留言
 */
public interface LeaveMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LeaveMessage record);

    int insertSelective(LeaveMessage record);

    LeaveMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LeaveMessage record);

    int updateByPrimaryKeyWithBLOBs(LeaveMessage record);

    int updateByPrimaryKey(LeaveMessage record);

	List<Map<String, Object>> selectLeMsgByPage(Map<String, Object> param);

	long selectLeMsgCountByPage(Map<String, Object> param);

	int updateStautsByPrimaryKey(@Param("id")Integer id,@Param("managerId")Integer managerId);
}