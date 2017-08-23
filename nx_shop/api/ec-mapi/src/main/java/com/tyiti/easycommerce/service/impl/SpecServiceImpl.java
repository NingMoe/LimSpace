package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Spec;
import com.tyiti.easycommerce.repository.SpecDao;
import com.tyiti.easycommerce.service.SpecService;

@Service("specService")
public class SpecServiceImpl implements SpecService{
	
	@Autowired
	private SpecDao specDao ;

	@Override
	public void update(Spec spec) {
		// TODO Auto-generated method stub
		spec.setUpdateTime(new Date());
		specDao.updateByPrimaryKeySelective(spec);
		
	}

	@Override
	public void addSpec(Spec spec) {
		// TODO Auto-generated method stub
		spec.setCreateTime(new Date());
		specDao.insertSelective(spec);
	}

	@Override
	public List<Spec> specList() {
		// TODO Auto-generated method stub
		return specDao.getSpecList();
	}

	@Override
	public int selectCountByName(String name) {
		// TODO Auto-generated method stub
		return specDao.selectCountByName(name);
	}
	
	
}
