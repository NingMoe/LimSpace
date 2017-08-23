package com.tyiti.easycommerce.service;

import java.util.Map;




import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.User;

public interface UserService {
	
	/**
	 * 查询信用分
	 * 
	 * @param user
	 * @param data
	 * @return
	 */
	Map<String, Object> getUserScore(User user, Map<String, Object> data);

	/**
	 * <p>
	 * 功能描述：。
	 * </p>
	 * 
	 * @param userId
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期:2016年5月18日 下午3:18:47。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	User getById(Integer userId);

	/**
	 * @author wyy 2016/07/15
	 * @description 根据实体修改数据
	 * @param user
	 * @return
	 */
	Map<String, Object> updateByPrimaryKeySelective(User user);


	/**
	 * @author wyy 2016/07/15
	 * @description 获取用户列表
	 * @param user
	 * @return
	 */
	 SearchResult<Map<String,Object>> getUserList(Map<String, Object> param);
	
}
