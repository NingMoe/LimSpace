package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.Spec;

public interface SpecService {

	void update(Spec spec);

	void addSpec(Spec spec);

	List<Spec> specList();

	int selectCountByName(String name);

}
