package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.Attribute;

public interface AttributeService {
	Attribute addAttribute(Attribute attribute);

    int update(Attribute attribute);

    List<Attribute> getByCriteria(Attribute attribute);

	Attribute getByName(String name);
}
