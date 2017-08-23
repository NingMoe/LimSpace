package com.tyiti.easycommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tyiti.easycommerce.entity.PopularSearch;
import com.tyiti.easycommerce.repository.PopularSearchDao;
import com.tyiti.easycommerce.service.PopularSearchService;

@Service
public class PopularSearchImpl implements PopularSearchService {
	@Autowired
	private PopularSearchDao popularSearchDao;

	@Override
	public List<PopularSearch> getAll() {
		return popularSearchDao.getAll();
	}

}
