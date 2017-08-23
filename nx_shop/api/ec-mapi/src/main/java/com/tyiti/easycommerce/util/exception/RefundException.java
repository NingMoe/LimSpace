package com.tyiti.easycommerce.util.exception;
/**
 * 自定义输出异常信息
 * @author Administrator
 *
 */
public class RefundException extends RuntimeException{

	/**
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	* @date 2016-4-9 上午11:32:16
	* @author Yan Zuoyu
	*/
	private static final long serialVersionUID = 6011963844962502141L;

	/**
	 * 构造方法
	 * @param arg0 信息
	 * @param arg1 原因
	 */
	public RefundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * 构造方法
	 * @param arg0 信息
	 */
	public RefundException(String arg0) {
		super(arg0);
	}

	/**
	 * 构造方法
	 * @param arg0 原因
	 */
	public RefundException(Throwable arg0) {
		super(arg0);
	}
}
