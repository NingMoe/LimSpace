package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tyiti.easycommerce.entity.Config;
@Component("configDao")
public interface ConfigDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Config record);

    int insertSelective(Config record);

    Config selectByPrimaryKey(Integer id);
    
    Config selectByKey(String key);

    int updateByPrimaryKeySelective(Config record);

    int updateByPrimaryKey(Config record);

	List<Config> selectList();
	List<Map<String, Object>> selectRateList();
}