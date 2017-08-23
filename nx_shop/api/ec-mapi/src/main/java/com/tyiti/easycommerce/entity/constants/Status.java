package com.tyiti.easycommerce.entity.constants;

import java.util.HashMap;
import java.util.Map;

import com.tyiti.easycommerce.util.mybatis.MyBatisEnum;

/**
 * 通用表示 '禁用/启用' 的状态
 * @author rainyhao
 * @since 2016-4-11 下午2:24:32
 */
public enum Status implements MyBatisEnum {
	ENABLED(0),
	DISABLED(1);
	
	Status(int value) {
		this.value = value;
	}

	private int value;
	@Override
	public int getValue() {
		return value;
	}

	private static final Map<Integer, Status> valueLookup = new HashMap<Integer, Status>();
	
	static {
		for (Status type : values()) {
			valueLookup.put(type.getValue(), type);
		}
	}
	
	public static final Status forValue(int value) {
		return valueLookup.get(value);
	}
}
