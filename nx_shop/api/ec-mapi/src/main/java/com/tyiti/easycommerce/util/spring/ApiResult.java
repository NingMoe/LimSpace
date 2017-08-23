package com.tyiti.easycommerce.util.spring;

import java.util.HashMap;
import java.util.Map;

import com.tyiti.easycommerce.util.XString;
import com.tyiti.easycommerce.util.exception.CommonException;

/**
 * 接口返回统一数据格式
 * @author rainyhao
 * @since 2016-4-12 上午9:30:14
 */
public final class ApiResult {
	
	/**
	 * 创建统一格式 {code: 操作响应码, message: 返回消息, data: 返回数据}
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:44:13
	 * @param code 响应码
	 * @param message 返回消息
	 * @param data 返回数据
	 * @return
	 */
	public static final Map<String, Object> create(int code, String message, Object data) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", code);
		if (!XString.isNullOrEmpty(message)) {
			res.put("message", message);
		}
		if (null != data) {
			res.put("data", data);
		}
		return res;
	}
	
	public static final Map<String, Object> create(int code, String message) {
		return create(code, message, null);
	}
	
	/**
	 * 操作成功并携带数据返回
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:39:55
	 * @param data 要输出的数据
	 * @return
	 */
	public static final Map<String, Object> success(Object data) {
		return create(200, "OK", data);
	}
	
	/**
	 * 操作成功
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:39:45
	 * @return
	 */
	public static final Map<String, Object> success() {
		return create(200, "OK");
	}
	
	/**
	 * 输入某请求参数不能为空的提示消息
	 * @authro rainyhao
	 * @since 2016-4-12 下午4:09:10
	 * @param paramName
	 * @return
	 */
	public static final Map<String, Object> paramNull(String paramName) {
		return paramInvalid(paramName + "不能为空");
	}
	
	/**
	 * 参数错误
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:41:43
	 * @param message 具体错误消息
	 * @return
	 */
	public static final Map<String, Object> paramInvalid(String message) {
		return create(400, message);
	}
	
	/**
	 * 参数错误
	 * @authro rainyhao
	 * @since 2016-4-12 下午4:23:44
	 * @param e 项目自定义异常
	 * @return
	 */
	public static final Map<String, Object> paramInvalid(CommonException e) {
		return paramInvalid(e.getMessage());
	}
	
	/**
	 * 参数错误, 无具体错误消息
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:46:51
	 * @return
	 */
	public static final Map<String, Object> paramInvalid() {
		return paramInvalid("参数错误");
	}
	
	/**
	 * 禁止访问
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:43:58
	 * @param message, 具体原因
	 * @return
	 */
	public static final Map<String, Object> forbidden(String message) {
		return create(403, message);
	}
	
	/**
	 * 禁止访问, 无具体错误消息
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:48:08
	 * @return
	 */
	public static final Map<String, Object> forbidden() {
		return create(403, "您无权做此操作");
	}
	
	/**
	 * 所访问的资源不存在
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:42:33
	 * @param message 具体错误消息
	 * @return
	 */
	public static final Map<String, Object> notFound(String message) {
		return create(404, message);
	}
	
	/**
	 * 所访问的资源不存在, 无具体错误消息
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:49:01
	 * @return
	 */
	public static final Map<String , Object> notFound() {
		return notFound("资源未找到");
	}
	
	/**
	 * 系统内部错误, 500
	 * @authro rainyhao
	 * @since 2016-4-12 上午9:40:28
	 * @return
	 */
	public static final Map<String, Object> sysError() {
		return create(500, "系统错误");
	}
	/**
	 * 
	 * @Title: sysError 
	 * @Description: 
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月27日 下午5:58:19
	 */
	public static final Map<String, Object> getWps(String message) {
		return create(500, message);
	}
}
