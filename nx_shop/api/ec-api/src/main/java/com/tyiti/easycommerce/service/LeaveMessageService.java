package com.tyiti.easycommerce.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.entity.LeaveMessage;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.LeaveMessageDao;
import com.tyiti.easycommerce.util.LogUtil;

/**
 * @author wangqi
 * @date 2016-4-22 上午9:33:13
 * @description 用户留言
 */
@Service
public class LeaveMessageService {

	@Autowired
	private LeaveMessageDao leaveMessageDao;
	
	public boolean addOneLeMessage(LeaveMessage leaveMessage,
			HttpSession session,HttpServletRequest request) {
		User user = (User) session.getAttribute(Constants.USER);
		leaveMessage.setCustId(user.getId());
		leaveMessage.setLeaveTime(new Date());
		leaveMessage.setStatus(1);
		leaveMessage.setLeaveIp(LogUtil.getIpAddress(request));
		int i = leaveMessageDao.insertSelective(leaveMessage);
		if (i != 1) {
			return false;
		}
		LogUtil.log(user.getId(),user.getRealName(),
				leaveMessage.getId(), LogEnum.OperateModel.LEVMSG.getKey(),
				LogEnum.Action.ADD.getKey(), "用户留言",
				LogEnum.Source.SHOP.getKey(), 1);
		return true;
	}

}
