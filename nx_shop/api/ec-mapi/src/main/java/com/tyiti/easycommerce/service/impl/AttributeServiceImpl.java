package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Attribute;
import com.tyiti.easycommerce.repository.AttributeDao;
import com.tyiti.easycommerce.service.AttributeService;

@Service("attributeService")
public class AttributeServiceImpl implements AttributeService {
	@Autowired
	private AttributeDao attributeDao;

	@Override
	public void addAttribute(Attribute attribute) {
		attribute.setCreateTime(new Date());
		attributeDao.insertSelective(attribute);
	}

	@Override
	public void update(Attribute attribute) {
		// TODO Auto-generated method stub
		attribute.setUpdateTime(new Date());
		attributeDao.updateByPrimaryKeySelective(attribute);
	}

	@Override
	public List<Attribute> attributeList() {
		// TODO Auto-generated method stub
		return attributeDao.getAttributeList();
	}

	@Override
	public int selectCountByName(String name) {
		// TODO Auto-generated method stub
		return attributeDao.selectCountByName(name);
	}
 
}
