package com.tyiti.easycommerce.service.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.UserDao;
import com.tyiti.easycommerce.service.UserService;
import com.tyiti.easycommerce.util.HttpClientUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	// 信分宝接口地址
	@Value("${xfbInterface}")
	private String xfbInterface;
	// 信分宝接口地址版本号
	@Value("${xfbVersion}")
	private String xfbVersion;
	// 信分宝需要推荐人
	@Value("${recomCode}")
	private String recomCode;

	// sys
	@Value("${sys}")
	private String sys;

	private Log log = LogFactory.getLog(this.getClass());

	@Override
	public Map<String, Object> getUserScore(User user, Map<String, Object> data) {
		if ("tyfq".equals(sys)) {
			// 先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, "",
					user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					String response = HttpClientUtil.httpGetUserScore(
							xfbInterface, user, obj.getString("userId"),
							xfbVersion);
					JSONObject result = JSONObject.fromObject(response);
					int resultCode = result.getInt("resultCode");
					if (resultCode != 0) {
						data.put("code", 500);
						data.put("resultCode", resultCode);
						data.put("message", result.getString("resultMessage"));
						return data;
					} else {
						// 总额度
						data.put("creditSum", result.getString("creditSum"));
						// 可使用额度
						data.put("useSum", result.getString("useSum"));
					}
				} else {
					data.put("resultCode", obj.getString("resultCode"));
					data.put("message", obj.getString("resultMessage"));
				}
			}
		}
		return data;
	}

	/**
	 * <p>
	 * 功能描述:。
	 * </p>
	 * 
	 * @param userId
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期2016年5月18日 下午3:56:31。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@Override
	public User getById(Integer userId) {
		return userDao.selectByPrimaryKey(userId);
	}

	/**
	 * @author wyy 2016/07/15
	 * @description 根据实体修改数据
	 * @param user
	 * @return
	 */
	public Map<String, Object> updateByPrimaryKeySelective(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (userDao.updateByPrimaryKeySelective(user) > 0) {
				map.put("code", 200);
				map.put("messsge", "ok!");
			} else {
				map.put("code", 400);
				map.put("messsge", "修改失败！");
			}

		} catch (Exception e) {
			map.put("code", 400);
			map.put("messsge", e.getMessage());
		}
		return map;
	}

	/**
	 * @author wyy 2016/07/15
	 * @description 获取用户列表
	 * @param user
	 * @return
	 */
	public SearchResult<Map<String, Object>> getUserList(
			Map<String, Object> param) {

		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit",
					Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset",
					Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		searchResult.setRows(userDao.selectUserList(param));
		searchResult.setTotal(userDao.selectUserCount(param));
		log.info("获取用户列表信息：" + searchResult);
		return searchResult;

	}

}
