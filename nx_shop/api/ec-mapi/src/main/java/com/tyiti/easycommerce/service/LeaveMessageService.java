package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;

/**
 * @author wangqi
 * @date 2016-4-22 上午10:38:53
 * @description 用户留言
 */
public interface LeaveMessageService {
	/**
	 * 用户留言分页
	 * @param param
	 * @return
	 */
	SearchResult<Map<String, Object>> queryLeMsgByPage(Map<String, Object> param);
	/**
	 * 管理员修改留言状态
	 * @param id
	 * @return
	 */
	int updateStatus(Integer id);

}
