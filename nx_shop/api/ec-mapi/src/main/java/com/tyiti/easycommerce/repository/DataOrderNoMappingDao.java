package com.tyiti.easycommerce.repository;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.DataOrderNoMapping;

public interface DataOrderNoMappingDao {
	DataOrderNoMapping getBySource(@Param("source")String source);
}
