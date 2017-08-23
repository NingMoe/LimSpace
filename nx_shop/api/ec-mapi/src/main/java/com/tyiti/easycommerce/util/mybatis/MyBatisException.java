package com.tyiti.easycommerce.util.mybatis;

import com.tyiti.easycommerce.util.exception.CommonException;


/**
 * MyBatis枚举与对应值之间转换时的异常标识
 * @author rainyhao
 * @since 2015-5-21 下午3:29:19
 */
@SuppressWarnings("serial")
public final class MyBatisException extends CommonException {

	public MyBatisException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyBatisException(String message) {
		super(message);
	}

	public MyBatisException(Throwable cause) {
		super(cause);
	}

}
