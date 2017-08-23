package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Fqa;
import com.tyiti.easycommerce.entity.FqaWithBLOBs;

public interface FaqDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FqaWithBLOBs record);

    int insertSelective(FqaWithBLOBs record);

    FqaWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FqaWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(FqaWithBLOBs record);

    int updateByPrimaryKey(Fqa record);
    
	List<Map<String, Object>> selectFqaByPage(Map<String, Object> param);

	long selectFqaCountByPage(Map<String, Object> param);
}