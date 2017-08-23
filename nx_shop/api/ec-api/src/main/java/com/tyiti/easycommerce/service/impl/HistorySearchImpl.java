package com.tyiti.easycommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tyiti.easycommerce.entity.HistorySearch;
import com.tyiti.easycommerce.repository.HistorySearchDao;
import com.tyiti.easycommerce.service.HistorySearchService;

@Service
public class HistorySearchImpl implements HistorySearchService {
	@Autowired
	private HistorySearchDao HistorySearchDao;

	@Override
	public Integer add_user(HistorySearch historySearch){
		return HistorySearchDao.adduser(historySearch);
	}
	@Override
	public Integer update_user(HistorySearch historySearch) {
		return HistorySearchDao.updateuser(historySearch);
	}
	@Override
	public Integer add_null(HistorySearch historySearch){
		return HistorySearchDao.addnull(historySearch);
	}
	@Override
	public Integer update_null(HistorySearch historySearch) {
		return HistorySearchDao.updatenull(historySearch);
	}
}
