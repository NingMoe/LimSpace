package com.tyiti.easycommerce.util.exception;

/**
 * 业务异常
 * @author rainyhao
 * @since 2016-4-12 下午3:08:19
 */
public class ServiceException extends CommonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServiceException(String arg0) {
		super(arg0);
	}

	public ServiceException(Throwable arg0) {
		super(arg0);
	}

}
