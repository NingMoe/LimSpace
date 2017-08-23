package com.tyiti.easycommerce.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.repository.LeaveMessageDao;
import com.tyiti.easycommerce.service.LeaveMessageService;
import com.tyiti.easycommerce.util.LogUtil;

@Service("leaveMessageService")
public class LeaveMessageServiceImpl implements LeaveMessageService{

	@Autowired
	private LeaveMessageDao leaveMessageDao;
	
	@Override
	public SearchResult<Map<String, Object>> queryLeMsgByPage(
			Map<String, Object> param) {
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		List<Map<String, Object>> rows = this.leaveMessageDao.selectLeMsgByPage(param);
		searchResult.setRows(rows);
		searchResult.setTotal(leaveMessageDao.selectLeMsgCountByPage(param));
		return searchResult;
	}

	@Override
	public int updateStatus(Integer id) {
		int managerId = 1;
		int num = leaveMessageDao.updateStautsByPrimaryKey(id,managerId);
		if(num==1){
			LogUtil.log(id,
					LogEnum.OperateModel.LEVMSG.getKey(),
					LogEnum.Action.UPDATE.getKey(), "回复用户留言",
					LogEnum.Source.PLAT.getKey(), 1);
		}
		return num;
	}

}
