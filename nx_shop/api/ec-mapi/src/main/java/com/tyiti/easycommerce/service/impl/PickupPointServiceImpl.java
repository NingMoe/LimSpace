package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.PickupPointUser;
import com.tyiti.easycommerce.repository.PickupPointDao;
import com.tyiti.easycommerce.repository.PickupPointUserDao;
import com.tyiti.easycommerce.service.PickupPointService;
import com.tyiti.easycommerce.util.Md5;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service("pickupPointService")
public class PickupPointServiceImpl implements PickupPointService{

	@Autowired
	private PickupPointDao pickupPointDao ;

	@Autowired
	private PickupPointUserDao pickupPointUserDao;
	
	@Transactional
	public void add(PickupPoint pickupPoint) {
		if(pickupPoint.getName() == null || pickupPoint.getName().equals("")){
			throw new CommonException("请输入营业厅名");
		}
		if(pickupPoint.getOpenTime() == null || pickupPoint.getOpenTime().equals("")){
			throw new CommonException("请输入营业时间");
		}
		String loginName = pickupPoint.getLoginName() ; 
		String password = pickupPoint.getPassword(); 
		String repeat  = pickupPoint.getRepeat();
		if(loginName == null || loginName.equals("")){
			throw new CommonException("请输入用户名");
		}
		if(password == null || password.equals("")){
			throw new CommonException("请输入密码");
		}
		if(repeat == null || repeat.equals("")){
			throw new CommonException("请输入确认密码");
		}
		if(!password.equals(password)){
			throw new CommonException("密码输入不一致");
		}
		//检查name 是否存在
		PickupPoint pickupPointDB= pickupPointDao.checkName(pickupPoint.getName());
		if(pickupPointDB !=null){
			throw new CommonException("营业厅名称已存在");
		}
		//检查用户名是否存在
		PickupPointUser pickupPointUserDB = pickupPointUserDao.selectByLoginName(loginName);
		if(pickupPointUserDB != null){
			throw new CommonException("用户名已存在");
		}
		Date now = new Date();
		pickupPoint.setCreateTime(now);
		pickupPointDao.insertSelective(pickupPoint);
		Integer pickupPointId =  pickupPoint.getId() ; 
		
		PickupPointUser pickupPointUser = new PickupPointUser();
		Md5 md5 = new Md5();
		pickupPointUser.setType(1);
		pickupPointUser.setStatus(pickupPoint.getStatus());
		pickupPointUser.setLoginName(loginName);
		pickupPointUser.setPickupPointId(String.valueOf(pickupPointId));
		pickupPointUser.setPassword(md5.getMD5ofStr(md5.getMD5ofStr(password)));
		pickupPointUserDao.insertSelective(pickupPointUser);
	}

	@Transactional
	public void edit(PickupPoint pickupPoint) {
		
		PickupPoint pickupPointDB = pickupPointDao.selectByPrimaryKey(pickupPoint.getId());
		if(pickupPointDB == null){
			throw new CommonException("不存在该营业厅");
		}
		if(pickupPoint.getName() !=null && !pickupPoint.getName().equals("")){
			PickupPoint pickupPointCheckName= pickupPointDao.checkName(pickupPoint.getName());
			if(pickupPointCheckName!=null)
			if(pickupPointCheckName.getId() != pickupPoint.getId()){
				throw new CommonException("营业厅名称已存在");
			}
		}
		
		
		if(pickupPoint.getLoginName() == null || pickupPoint.getLoginName().equals("")){
			throw new CommonException("用户名为空");
		}
		PickupPointUser pickupPointUser = pickupPointUserDao.selectByLoginName(pickupPoint.getLoginName());
		if(pickupPointUser == null){
			throw new CommonException("不存在该用户");
		}
		pickupPoint.setUpdateTime(new Date());
		pickupPointDao.updateByPrimaryKeySelective(pickupPoint);
		if(pickupPoint.getPassword() == null || pickupPoint.getPassword().equals("")|| pickupPoint.getPassword().equals("******")){
		}else{
			if(!pickupPoint.getPassword().equals(pickupPoint.getRepeat())){
				throw new CommonException("两次密码输入不一致");
			}
			Md5 md5 = new Md5();
			pickupPointUser.setPassword(md5.getMD5ofStr(md5.getMD5ofStr(pickupPoint.getPassword())));
			pickupPointUserDao.updateByPrimaryKeySelective(pickupPointUser);
		}
	}

	@Override
	public PickupPoint selectById(Integer id) {
		// TODO Auto-generated method stub
		return pickupPointDao.selectByPrimaryKey(id);
	}

	@Override
	public void del(Integer id) {
		// TODO Auto-generated method stub
		PickupPoint pickupPoint = pickupPointDao.selectByPrimaryKey(id);
		if(pickupPoint == null){
			throw new CommonException("不存在该营业厅");
		}
		pickupPoint.setInvalid(1);
		pickupPointDao.updateByPrimaryKeySelective(pickupPoint);
	}

	@Override
	public List<PickupPoint> selectList() {
		// TODO Auto-generated method stub
		return pickupPointDao.selectList();
	}

	@Override
	public void updateStatus(Integer id) {
		// TODO Auto-generated method stub
		PickupPoint pickupPoint = pickupPointDao.selectByPrimaryKey(id);
		if(pickupPoint == null){
			throw new CommonException("不存在该营业厅");
		}
		if(pickupPoint.getStatus()==0){
			pickupPoint.setStatus(1);
		}else{
			pickupPoint.setStatus(0);
		}
		pickupPointDao.updateByPrimaryKeySelective(pickupPoint);
	}

	@Override
	public void updateRank(Integer id, Integer rank) {
		// TODO Auto-generated method stub
		PickupPoint pickupPoint = pickupPointDao.selectByPrimaryKey(id);
		if(pickupPoint == null){
			throw new CommonException("不存在该营业厅");
		}
		pickupPoint.setRank(rank);
		pickupPointDao.updateByPrimaryKeySelective(pickupPoint);
	} 
}
