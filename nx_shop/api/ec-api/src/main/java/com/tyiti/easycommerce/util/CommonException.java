package com.tyiti.easycommerce.util;
/**
 * 自定义输出异常信息
 * @author Administrator
 *
 */
public class CommonException extends RuntimeException{

	/**
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	* @date 2016-4-9 上午11:31:15
	* @author Yan Zuoyu
	*/
	private static final long serialVersionUID = 2777472210569723252L;

	/**
	 * 构造方法
	 * @param arg0 信息
	 * @param arg1 原因
	 */
	public CommonException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * 构造方法
	 * @param arg0 信息
	 */
	public CommonException(String arg0) {
		super(arg0);
	}

	/**
	 * 构造方法
	 * @param arg0 原因
	 */
	public CommonException(Throwable arg0) {
		super(arg0);
	}
}
