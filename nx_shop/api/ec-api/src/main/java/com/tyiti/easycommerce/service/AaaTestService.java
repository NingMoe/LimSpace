package com.tyiti.easycommerce.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.tyiti.easycommerce.entity.Address;
import com.tyiti.easycommerce.repository.SkuDao;

@Service
public class AaaTestService {
	@Autowired
	private SkuDao skuDao;
	public int updateTest(Integer count,Integer id){
		return skuDao.subtractInventory(count, id);
	}
}