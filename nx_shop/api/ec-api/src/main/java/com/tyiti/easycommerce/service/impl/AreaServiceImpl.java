package com.tyiti.easycommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Area;
import com.tyiti.easycommerce.repository.AreaDao;
import com.tyiti.easycommerce.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaDao areaDao;

	@Override
	public List<Area> getAreasByParentIdAndLevel(Integer parentId,Integer level) {
		return areaDao.getAreasByParentIdAndLevel(parentId, level);
	}
}
