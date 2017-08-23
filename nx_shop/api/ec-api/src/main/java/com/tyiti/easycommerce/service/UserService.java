package com.tyiti.easycommerce.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.User;

public interface UserService {
	/**
	 * 注册用户
	 * 
	 * @param session
	 * @param userRegForm
	 * @return
	 */
	Map<String, Object> register(HttpSession session, User user);

	/**
	 * 弱绑定
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	Map<String, Object> createConnection(HttpSession session, User user);

	/**
	 * 按手机号码和密码登录
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	Map<String, Object> signup(HttpSession session,HttpServletRequest request,User user);

	/**
	 * 用户注销
	 * 
	 * @param session
	 * @return
	 */
	Map<String, Object> signout(HttpServletRequest request,HttpServletResponse response,HttpSession session);

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	Map<String, Object> changePassword(HttpSession session, User user);

	Map<String, Object> updatePassword(HttpSession session, User user);

	/**
	 * 发送短信验证码到用户手机
	 * 
	 * @param mobile
	 * @param verifyCode
	 * @return
	 */
	Map<String, Object> sendSmsForVerifyCode(String mobile, String verifyCode);

	/**
	 * 生成短信验证码，发送给用户，并存入数据库
	 * 
	 * @return 发送失败返回 null
	 */
	Map<String, Object> getVerifyCode(String mobile, String template);

	/**
	 * 检查短信验证码是否一致
	 * 
	 * @param mobile
	 * @param verifyCode
	 * @return
	 */
	boolean checkVerifyCode(String mobile, String verifyCode);

	Map<String, Object> doAuth(HttpSession session, User user);

	Map<String, Object> doPayAuth(HttpSession session, User user);

	Map<String, Object> updatePayPassword(HttpSession session, User user);

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
	 * 信用卡授信用户
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	Map<String, Object> creditSubmit(User user);

	Map<String, Object> creditStep(User user);

	/**
	 * 个人中心-个人信息
	 * 
	 * @author Black
	 * @date 2016-07-19
	 * @return
	 */
	Map<String, Object> getPrivateInfo(User user);

	/**
	 * 获取分享码
	 * 
	 * @param user
	 * @return
	 */
	Map<String, Object> getShareCode(User user);

	/**
	 * 通过分享码获取用户信息
	 * 
	 * @param user
	 * @return
	 */
	Map<String, Object> getShareUser(String code);

	/**
	 * @author wyy 2016/07/15
	 * @description 根据实体修改数据
	 * @param user
	 * @return
	 */
	Map<String, Object> updateByPrimaryKeySelective(User user);
	
	
	/**
	 * @author wyy 2016/09/22
	 * @description 上传图片
	 * @param session
	 * @param imgUrl
	 * @return
	 */
	Map<String, Object> returnImg(HttpSession session, String mediaId,String type);
}
