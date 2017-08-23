package com.tyiti.easycommerce.service;

import com.tyiti.easycommerce.entity.HistorySearch;

public interface HistorySearchService {
	
	Integer add_user(HistorySearch historySearch);
	
	Integer update_user(HistorySearch historySearch);
	
	Integer add_null(HistorySearch historySearch);
	
	Integer update_null(HistorySearch historySearch);
}