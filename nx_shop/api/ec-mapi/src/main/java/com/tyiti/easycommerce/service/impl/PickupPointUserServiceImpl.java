package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.PickupPointUser;
import com.tyiti.easycommerce.repository.PickupPointDao;
import com.tyiti.easycommerce.repository.PickupPointUserDao;
import com.tyiti.easycommerce.service.PickupPointUserService;
import com.tyiti.easycommerce.util.Md5;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service("pickupPointUserService")
public class PickupPointUserServiceImpl implements PickupPointUserService{
	@Autowired
	private PickupPointUserDao pickupPointUserDao ;
	
	@Autowired
	private PickupPointDao pickupPointDao;
	@Override
	public PickupPointUser selectById(Integer id) {
		// TODO Auto-generated method stub
		return pickupPointUserDao.selectByPrimaryKey(id);
	}

	@Override
	public void add(PickupPointUser pickupPointUser) {
		// TODO Auto-generated method stub
		String loginName = pickupPointUser.getLoginName() ; 
		String password = pickupPointUser.getPassword(); 
		String repeat  = pickupPointUser.getRepeat();
		Integer status = pickupPointUser.getStatus();
		String pickupPointId = pickupPointUser.getPickupPointId();
		if(pickupPointUser.getType() == null ||pickupPointUser.getType().equals("") ||(pickupPointUser.getType()!=1 && pickupPointUser.getType()!=2)){
			throw new CommonException("请选择用户类型");
		}
		if(pickupPointUser.getType()== 1){
			if(pickupPointId.contains(",")){
				throw new CommonException("类型选择错误");
			}
		}
		if(pickupPointId == null ||pickupPointId.equals("") ){
			throw new CommonException("营业厅不存在");
		}else{
			String[] pickupPointIds = pickupPointId.split(",");
			Integer num = pickupPointDao.selectByPickupPointList(pickupPointIds);
			if(num != pickupPointIds.length){
				throw new CommonException("营业厅选择错误");
			}
		}
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
		//检查用户名是否存在
		PickupPointUser pickupPointUserDB = pickupPointUserDao.selectByLoginName(loginName);
		if(pickupPointUserDB != null){
			throw new CommonException("用户名已存在");
		}
		Date now = new Date();
		pickupPointUser.setCreateTime(now);
		
		Md5 md5 = new Md5();
		pickupPointUser.setLoginName(loginName);
		pickupPointUser.setPickupPointId(String.valueOf(pickupPointId));
		pickupPointUser.setPassword(md5.getMD5ofStr(md5.getMD5ofStr(password)));
		pickupPointUser.setStatus(status);
		pickupPointUserDao.insertSelective(pickupPointUser);
		
	}

	@Override
	public void edit(PickupPointUser pickupPointUser) {
		 
		String password = pickupPointUser.getPassword(); 
		String repeat  = pickupPointUser.getRepeat();
		String  pickupPointId =  pickupPointUser.getPickupPointId() ;
		Integer status = pickupPointUser.getStatus();
		if(pickupPointId == null ||pickupPointId.equals("") ){
			throw new CommonException("营业厅不存在");
		}else{
			String[] pickupPointIds = pickupPointId.split(",");
			Integer num = pickupPointDao.selectByPickupPointList(pickupPointIds);
			if(num != pickupPointIds.length){
				throw new CommonException("营业厅选择错误");
			}
		}
	    PickupPointUser pickupPointUserDb = pickupPointUserDao.selectByPrimaryKey(pickupPointUser.getId());
	    if(pickupPointUserDb == null){
	    	throw new CommonException("用户不存在");
	    }
	    if(!password.equals("")){
			if(!password.equals(repeat)){
				throw new CommonException("两次密码不一致");
			}
		}else{
			throw new CommonException("密码不能为空");
		}
	    Md5 md5 = new Md5();
	    pickupPointUser.setPickupPointId(pickupPointId);
		pickupPointUser.setPassword(md5.getMD5ofStr(md5.getMD5ofStr(password)));
		pickupPointUser.setStatus(status);
		pickupPointUserDao.updateByPrimaryKeySelective(pickupPointUser);
	}

	@Override
	public void del(Integer id) {
		PickupPointUser pickupPointUser = pickupPointUserDao.selectByPrimaryKey(id);
		if(pickupPointUser ==  null){
			throw new CommonException("用户不存在");
		}
		pickupPointUser.setInvalid(1);
		pickupPointUserDao.updateByPrimaryKeySelective(pickupPointUser);
	}

	@Override
	public List<PickupPointUser> selectListByPointId(Integer pickupPointId) {
		 
		return pickupPointUserDao.selectListByPointId( pickupPointId) ;
	}

	@Override
	public List<PickupPointUser> selectPointUsers() {
		 
		return pickupPointUserDao.selectPointUsers();
	}

	@Override
	public void updateStatus(Integer id) {
		PickupPointUser pickupPointUser = pickupPointUserDao.selectByPrimaryKey(id);
		if(pickupPointUser == null){
			throw new CommonException("用户不存在");
		}
		if(pickupPointUser.getStatus() == 0){
			pickupPointUser.setStatus(1);
		}else{
			pickupPointUser.setStatus(0);
		}
		pickupPointUserDao.updateByPrimaryKeySelective(pickupPointUser);
	}

}
