package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.PushLog;

public interface PushLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PushLog record);

    int insertSelective(PushLog record);

    PushLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PushLog record);

    int updateByPrimaryKeyWithBLOBs(PushLog record);

    int updateByPrimaryKey(PushLog record);
}