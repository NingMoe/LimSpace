package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Promotion;
import com.tyiti.easycommerce.entity.PromotionPrize;
import com.tyiti.easycommerce.repository.PromotionDao;
import com.tyiti.easycommerce.repository.PromotionPrizeDao;
import com.tyiti.easycommerce.repository.PromotionRaffleDao;
import com.tyiti.easycommerce.service.PromotionService;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service("promotionService")
public class PromotionServiceImpl implements PromotionService{

	@Autowired
	private PromotionDao promotionDao;
	
	@Autowired
	private PromotionPrizeDao promotionPrizeDao;
	
	@Autowired
	private PromotionRaffleDao promotionRaffleDao;
	@Override
	public void addPromotion(Promotion promotion) {
		if(promotion.getName()==null ||promotion.getName().equals(""))
		throw new CommonException("活动名称为空");
		if(promotion.getMinHit()!=null && promotion.getMaxHit()!=null)
		if(promotion.getMinHit()>promotion.getMaxHit()){
			throw new CommonException("最少中奖次数大于最大中奖次数");
		}
		if(promotion.getType() ==null ||promotion.getType().equals("")){
			promotion.setType(0);
		}
		promotion.setCreateTime(new Date());
		promotion.setInvalid(0);
		promotionDao.insertSelective(promotion);
	}
	@Override
	public void editPromotion(Promotion promotion) {
		// TODO Auto-generated method stub
		Promotion promotionDb = promotionDao.selectByPrimaryKey(promotion.getId());
		if(promotionDb==null){
			throw new CommonException("id 不存在");
		}
		promotion.setInvalid(0);
		promotion.setUpdateTime(new Date());
		promotionDao.updateByPrimaryKeySelective(promotion);
	}
	@Override
	public void editStatus(Integer id) {
		// TODO Auto-generated method stub
		Promotion promotionDb = promotionDao.selectByPrimaryKey(id);
		if(promotionDb.getStatus()==0){
			promotionDb.setStatus(1);
		}else{
			promotionDb.setStatus(0);
		}
		promotionDao.updateByPrimaryKeySelective(promotionDb);
	}
	@Override
	public void delPromotion(Integer id) {
		// TODO Auto-generated method stub
		Promotion promotionDb = promotionDao.selectByPrimaryKey(id);
		promotionDb.setInvalid(1);
		promotionDao.updateByPrimaryKeySelective(promotionDb);
	}
	@Override
	public SearchResult<Promotion> selectPromotionList(
			Map<String, Object> param) {
		
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Promotion> result =  new SearchResult<Promotion>();
		result.setRows(promotionDao.selectPromotionList(param));
		result.setTotal(promotionDao.selectPromotionCount(param));
		return result;
	}
	@Override
	public void addPromotionPrize(PromotionPrize promotionPrize) {
		// TODO Auto-generated method stub
		Promotion promotionDb = promotionDao.selectByPrimaryKey(promotionPrize.getPromotionId());
		if(promotionDb == null){
			throw new CommonException("活动id 不存在");
		}
		promotionPrize.setInvalid(0);
		promotionPrize.setCreateTime(new Date());
		promotionPrizeDao.insertSelective(promotionPrize);
	}
	@Override
	public void editPromotionPrize(PromotionPrize promotionPrize) {
		// TODO Auto-generated method stub
		if(promotionPrizeDao.selectByPrimaryKey(promotionPrize.getId())==null){
			throw new CommonException("奖品id 不存在");
		}
		Promotion promotionDb = promotionDao.selectByPrimaryKey(promotionPrize.getPromotionId());
		if(promotionDb == null){
			throw new CommonException("活动id 不存在");
		}
		promotionPrize.setModifyTime(new Date());
		promotionPrizeDao.updateByPrimaryKeySelective(promotionPrize);
	}
	@Override
	public List<PromotionPrize> selectPromotionPrizeList(Integer promotionId) {
		if(promotionDao.selectByPrimaryKey(promotionId) == null){
			throw new CommonException("活动id 不存在");
		}
		return promotionPrizeDao.selectPromotionPrizeList(promotionId);
	}
	@Override
	public SearchResult<Map<String,Object>> selectPromotionRaffleList(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> result =  new SearchResult<Map<String,Object>>();
		result.setRows(promotionRaffleDao.selectPromotionRaffleList(param));
		result.setTotal(promotionRaffleDao.selectPromotionRaffleCount(param));
		return result;
	}
	@Override
	public Promotion getPromotionById(Integer id) {
		// TODO Auto-generated method stub
		return promotionDao.selectByPrimaryKey(id);
	}
	@Override
	public PromotionPrize getPrizeById(Integer id) {
		// TODO Auto-generated method stub
		return promotionPrizeDao.selectByPrimaryKey(id);
	}
	@Override
	public void delPrize(Integer id) {
		// TODO Auto-generated method stub
		PromotionPrize promotionPrize =promotionPrizeDao.selectByPrimaryKey(id);
		promotionPrize.setInvalid(1);
		promotionPrizeDao.updateByPrimaryKeySelective(promotionPrize);
	}

}
