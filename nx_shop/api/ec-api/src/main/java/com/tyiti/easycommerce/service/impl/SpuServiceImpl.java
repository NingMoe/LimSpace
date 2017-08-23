package com.tyiti.easycommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Spu;
import com.tyiti.easycommerce.entity.SpuAttribute;
import com.tyiti.easycommerce.repository.SpuAttributeDao;
import com.tyiti.easycommerce.repository.SpuDao;
import com.tyiti.easycommerce.service.SpuService;

@Service
public class SpuServiceImpl implements SpuService {
	@Autowired
	private SpuDao spuDao;
	@Autowired
	private SpuAttributeDao spuAttributeDao;

	@Override
	public Spu addSpu(Spu spu) {
		int affectedRows = spuDao.add(spu);
		if (affectedRows < 1) {
			return null;
		}
		List<SpuAttribute> spuAttributeList = spu.getAttrs();
		if (spuAttributeList != null && spuAttributeList.size() > 0) {
			spuAttributeDao.addList(spuAttributeList);
		}

		return spuDao.getById(spu.getId());
	}

	@Override
	public List<Spu> getByCriteria(Spu spu) {
		return spuDao.getByCriteria(spu);
	}

	@Override
	public Spu getById(Integer id) {
		return spuDao.getById(id);
	}

}
