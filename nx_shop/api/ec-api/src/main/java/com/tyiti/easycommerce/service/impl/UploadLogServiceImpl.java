package com.tyiti.easycommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.UploadLog;
import com.tyiti.easycommerce.repository.UploadLogDao;
import com.tyiti.easycommerce.service.UploadLogService;
@Service("uploadLogService")
public class UploadLogServiceImpl implements UploadLogService{

	@Autowired
	private UploadLogDao uploadLogDao ;

	@Override
	public void insertUploadLog(UploadLog uploadLog) {
		// TODO Auto-generated method stub
		uploadLogDao.insertSelective(uploadLog);
	}
}
