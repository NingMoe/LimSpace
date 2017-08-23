package com.tyiti.easycommerce.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Config;
import com.tyiti.easycommerce.repository.ConfigDao;
import com.tyiti.easycommerce.util.spring.SpringUtil;

public abstract  class SysConfig {
	
	public static Map<String,String> configMap = new HashMap<String,String>();
	//分期利率配置
	public static Map<String,String> rateConfigMap = new HashMap<String,String>();
	

	 public static void init(){
		 ConfigDao configDao = (ConfigDao) SpringUtil.getBean("configDao");
		 List<Config>  config =  configDao.selectList();
		 List<Map<String, Object>>  rateConfig =  configDao.selectRateList();
		for (Config config2 : config) {
			configMap.put(config2.getKey(), config2.getValue());
		}
		for (Map<String, Object> rate : rateConfig) {
			rateConfigMap.put(String.valueOf(rate.get("months")), String.valueOf(rate.get("rate")));
		}
	 }
}
