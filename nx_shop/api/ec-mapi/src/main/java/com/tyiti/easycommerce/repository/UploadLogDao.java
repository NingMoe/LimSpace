package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.UploadLog;

public interface UploadLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UploadLog record);

    int insertSelective(UploadLog record);

    UploadLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UploadLog record);

    int updateByPrimaryKey(UploadLog record);
}