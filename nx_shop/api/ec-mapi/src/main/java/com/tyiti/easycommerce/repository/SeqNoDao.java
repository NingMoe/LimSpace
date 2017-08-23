package com.tyiti.easycommerce.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.SeqNo;

public interface SeqNoDao {
	Integer addSeqNo(SeqNo seqNo);
	
	@Select("SELECT * FROM t_seq_no WHERE id = #{id}")
	@ResultMap(value = "BaseResultMap")
	SeqNo getById(@Param("id")Integer id);
	
	@Select("SELECT MAX(seq_no) FROM t_seq_no WHERE `key` = #{key}")
	Integer getMaxSeqNo(@Param("key")String key);
}
