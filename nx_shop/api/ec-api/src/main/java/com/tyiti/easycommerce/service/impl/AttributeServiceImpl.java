package com.tyiti.easycommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Attribute;
import com.tyiti.easycommerce.repository.AttributeDao;
import com.tyiti.easycommerce.service.AttributeService;

@Service
public class AttributeServiceImpl implements AttributeService {
	@Autowired
	private AttributeDao attributeDao;

	@Override
	public Attribute addAttribute(Attribute attribute) {
        int affectedRows = attributeDao.add(attribute);
        if (affectedRows < 1) {
            return null;
        }
        return attributeDao.getById(attribute.getId());
	}

	@Override
	public int update(Attribute attribute) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Attribute> getByCriteria(Attribute attribute) {
		return attributeDao.getByCriteria(attribute);
	}

	@Override
	public Attribute getByName(String name) {
		return attributeDao.getByName(name);
	}

}
