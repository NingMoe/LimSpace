package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.Headline;

public interface HeadlineDao {

	Integer insert(Headline headline);

	List<Headline> getHeadlineList(Headline headline);
	
	List<Headline> getInvalidList();
	
	void updateHeadlineByPrimaryKey(Headline headline);
	
	Integer deleteByPrimaryKey(Integer id);
	
	void updateInvalid();
}