package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.Attribute;

public interface AttributeService {
	void addAttribute(Attribute attribute);

    void  update(Attribute attribute);

	List<Attribute> attributeList();

	int selectCountByName(String name);
}
