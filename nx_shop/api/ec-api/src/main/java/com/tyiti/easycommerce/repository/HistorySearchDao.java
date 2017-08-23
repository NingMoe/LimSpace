package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.HistorySearch;


public interface HistorySearchDao {
	
	Integer adduser(HistorySearch historySearch);
	
	Integer updateuser(HistorySearch historySearch);
	
	Integer addnull(HistorySearch historySearch);
	
	Integer updatenull(HistorySearch historySearch);
	
	
}