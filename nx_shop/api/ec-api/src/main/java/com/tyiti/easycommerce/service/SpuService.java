package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.Spu;

public interface SpuService {
	Spu addSpu(Spu spu);

	List<Spu> getByCriteria(Spu spu);

	Spu getById(Integer id);

}
