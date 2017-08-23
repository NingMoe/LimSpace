package com.tyiti.easycommerce.service.impl;

import java.util.List;
import java.util.Map;

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
	public List<Map<String, Object>> getAll() {
		return popularSearchDao.getAll();
	}
	@Override
	public PopularSearch getById(Integer id) {
		Integer rankMax = popularSearchDao.getMaxRank();
		PopularSearch ps = popularSearchDao.getById(id);
		ps.setRankMax(rankMax);
		return ps;
	}
	@Override
	public Integer add(PopularSearch popularSearch) {
		Integer rank = popularSearchDao.getMaxRank();
		popularSearch.setRank(rank+1);
		return popularSearchDao.add(popularSearch);
	}
	@Override
	public Integer delete(Integer id) {
		return popularSearchDao.delete(id);
	}
	@Override
	public Integer updateStatus(Integer id) {
		return popularSearchDao.updateStatus(id);
	}
	@Override
	public Integer updateRank(PopularSearch popularSearch) {
		Integer rankOld = popularSearchDao.getRankById(popularSearch.getId());
		popularSearch.setRankOld(rankOld);
		
		Integer maxrank = popularSearchDao.getMaxRank();
		if(popularSearch.getRankNew()>maxrank){
			popularSearch.setRankNew(maxrank);
		}
		if(popularSearch.getRankNew()<1){
			popularSearch.setRankNew(1);
		}
		
		if (popularSearch.getRankNew() < rankOld){
			popularSearchDao.rankBottonUp(popularSearch);
		}
		else if (popularSearch.getRankNew() > rankOld){
			popularSearchDao.rankTopDown(popularSearch);
		}
		return popularSearchDao.updateRank(popularSearch);
	}
}
