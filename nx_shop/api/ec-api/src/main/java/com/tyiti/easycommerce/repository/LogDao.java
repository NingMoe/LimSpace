package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tyiti.easycommerce.entity.Log;

@Repository("logDao")
public interface LogDao {

    int insertSelective(Log log);

	List<Map<String, Object>> selectListByOperateModel(Map<String, Object> param);

}