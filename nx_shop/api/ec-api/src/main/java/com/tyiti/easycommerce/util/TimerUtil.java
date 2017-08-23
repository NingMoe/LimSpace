package com.tyiti.easycommerce.util;

import java.util.TimerTask;

import com.tyiti.easycommerce.common.SysConfig;


public class TimerUtil extends TimerTask{
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SysConfig.init();
	}
}