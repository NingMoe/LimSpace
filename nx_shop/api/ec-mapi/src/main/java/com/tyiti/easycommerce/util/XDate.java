package com.tyiti.easycommerce.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期常用的操作方法
 * @author rainyhao
 * @since 2015-5-21 下午3:58:51
 */
public final class XDate {
	
	/**
	 * 默认日期格式
	 */
	private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 当前时间, java.util.Date
	 * @author rainyhao
	 * @since 2015-5-21 下午4:01:08
	 * @return
	 */
	public static final Date now() {
		return new Date();
	}
	
	/**
	 * 当前时间, java.sql.Timestamp
	 * @author rainyhao
	 * @since 2015-5-21 下午4:12:15
	 * @return
	 */
	public static final Timestamp nowTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 使用yyyy-MM-dd HH:mm:ss将日期格式化为字符串
	 * @author rainyhao
	 * @since 2015-5-21 下午4:07:42
	 * @param date java.util.Date或者java.sql.Timestamp
	 * @return
	 */
	public static final String format(Object date) {
		return format(date, DEFAULT_PATTERN);
	}
	
	/**
	 * 用指定的格式将日期格式化为字符串
	 * @author rainyhao
	 * @since 2015-5-21 下午4:14:00
	 * @param date java.util.Date或者java.sql.Timestamp
	 * @param pattern 日期格式
	 * @return
	 */
	public static final String format(Object date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	/**
	 * 用指yyyy-MM-dd HH:mm:ss格式化当前时间
	 * @author rainyhao
	 * @since 2015-5-21 下午4:15:38
	 * @return
	 */
	public static final String formatNow() {
		return format(now(), DEFAULT_PATTERN);
	}
	
	/**
	 * 把当前时间格式化为指定格式的字符串
	 * @author rainyhao
	 * @since 2015-5-21 下午4:17:43
	 * @param pattern
	 * @return
	 */
	public static final String formatNow(String pattern) {
		return format(now(), pattern);
	}
	
	/**
	 * 获取指定日期相差天数的日期
	 * @author rainyhao 
	 * @since 2015-3-9 下午6:52:35
	 * @param date 指定对比的日期
	 * @param days 天数, 负数表示之前多少天, 正数表示之后多少天 
	 * @return
	 */
	public static final Date different(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
	/**
	 * 获取当前日期相差天数的日期
	 * @author rainyhao 
	 * @since 2015-3-9 下午6:54:23
	 * @param days 相关的天数, 负数表示之前多少天, 正数表示之后多少天 
	 * @return
	 */
	public static final Date different(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
	/**
	 * 比较两个日期相差多少天
	 * 参数to比from晚多少天
	 * @author rainyhao
	 * @since 2015-7-8 下午4:22:19
	 * @param from 较小的日期
	 * @param to 较大的日期
	 * @return
	 */
	public static final int differentDays(Date from, Date to) {
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(from);
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(to);
		int fromDay = calFrom.get(Calendar.DAY_OF_YEAR);
		int toDay = calTo.get(Calendar.DAY_OF_YEAR);
		return toDay - fromDay;
	}
	
	/**
	 * 比较两个日期相差多少天
	 * 参数to比from晚多少天
	 * @author rainyhao
	 * @since 2015-7-8 下午4:26:25
	 * @param from 较小的日期
	 * @param to 较大的日期
	 * @return
	 */
	public static final int differentDays(String from, String to) {
		String pattern = "yyyy-MM-dd";
		return differentDays(parse(from, pattern), parse(to, pattern));
	}
	
	/**
	 * 获取当时日期与一着想天数的一段日期列表(包括今天在内)
	 * @author rainyhao
	 * @since 2015-6-17 下午6:13:33
	 * @param days
	 * @return
	 */
	public static final List<Date> differentList(int days) {
		List<Date> lstDate = new ArrayList<Date>();
		if (days > 0) {
			for (int i = 0; i < days; i++) {
				lstDate.add(different(i));
			}
		} else {
			for (int i = days + 1; i <= 0; i++) {
				lstDate.add(different(i));
			}
		}
		return lstDate;
	}
	
	/**
	 * 获取指定开始结束日期之间的所有日期对象
	 * @author rainyhao
	 * @since 2015-7-8 下午4:37:40
	 * @param from 开始日期 yyyy-MM-dd
	 * @param to 结束日期 yyyy-MM-dd
	 * @return
	 */
	public static final List<Date> differentList(String from, String to) {
		String pattern = "yyyy-MM-dd";
		return differentList(parse(from, pattern), parse(to, pattern));
	}
	
	public static final List<Date> differentList(Date from, Date to) {
		int days = differentDays(from, to);
		List<Date> lstDate = new ArrayList<Date>();
		for (int i = 0; i < days + 1; i++) {
			lstDate.add(different(from, i));
		}
		return lstDate;
	}
	
	/**
	 * 将指定的日期字符串按指定的格式转换成日期类型
	 * 如果转换错误, 返回空
	 * @author rainyhao 
	 * @since 2015-3-9 下午6:55:38
	 * @param date 日期字符串
	 * @param pattern 格式
	 * @return
	 */
	public static final Date parse(String date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date d = format.parse(date);
			return d;
		} catch (ParseException ignore) {
			return null;
		}
	}
	
	/**
	 * 指定形式的日期字符串转成java.sql.Timestamp
	 * @author rainyhao 
	 * @since 2015-3-9 下午7:46:37
	 * @param date 日期字符串
	 * @param pattern 格式
	 * @return
	 */
	public static final Timestamp toTimestamp(String date, String pattern) {
		Date d = parse(date, pattern);
		return null == d ? null : new Timestamp(d.getTime());
	}
	
	/**
	 * 两个时间比较
	 * @author :xulihui
	 * @since :2016年4月15日 下午3:24:52
	 * @param date1
	 * @return
	 */
	public static int compareDateWithNow(Date date1) {
		Date date2 = new Date();
		int rnum = date1.compareTo(date2);
		return rnum;
	}

	/**
	 * 两个时间比较(时间戳比较)
	 * @author :xulihui
	 * @since :2016年4月15日 下午3:25:02
	 * @param date1
	 * @return
	 */
	public static int compareDateWithNow(long date1) {
		long date2 = dateToUnixTimestamp();
		if (date1 > date2) {
			return 1;
		} else if (date1 < date2) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 将当前日期转换成Unix时间戳
	 * @author :xulihui
	 * @since :2016年4月15日 下午3:25:13
	 * @return
	 */
	public static long dateToUnixTimestamp() {
		long timestamp = new Date().getTime();
		return timestamp;
	}

}
