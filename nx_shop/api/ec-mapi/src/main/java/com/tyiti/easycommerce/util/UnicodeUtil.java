package com.tyiti.easycommerce.util;


public class UnicodeUtil {
	/**
	 * unicode转汉字
	 * @param utfString
	 * @return
	 */
	public static String convert(String utfString){
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		
		while((i=utfString.indexOf("\\U", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if(i+5 < utfString.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
			}
		}
		
		sb.append(utfString.substring(pos, utfString.length()));
		return sb.toString();
	}
}
