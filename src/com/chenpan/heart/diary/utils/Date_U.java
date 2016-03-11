package com.chenpan.heart.diary.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

public class Date_U {
	
	public static final int WEEKDAYS = 7;
	public static String[] WEEK = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
			"星期六" };
	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String data(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 根据传递的类型格式化时间
	 * 
	 * @param str
	 * @param type
	 *            例如：yy-MM-dd
	 * @return
	 */
	public static String getDateTimeByMillisecond() {

		Date date = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

		String time = format.format(date);

		return time;
	}

	/**
	 * 根据传递的类型格式化时间
	 * 
	 * @param str
	 * @param type
	 *            例如：yy-MM-dd
	 * @return
	 */
	public static String getDateTimeByMilli() {

		Date date = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String time = format.format(date);

		return time;
	}

	/**
	 * 将字符串转为时间戳
	 * 
	 * @param user_time
	 * @return
	 */
	public static Long dateToTimestamp(String user_time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(user_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	/**
	 * 
	 * @功能描述：将传入的时间字符串转化为固定格式的字符串
	 * @param
	 * @return yyyy-MM-dd转换为yyyy-MM
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static String TimestampToTimestamp(String user_time) {
		SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM");
		if (!StringUtils.isEmpty(user_time)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// SimpleDateFormat sdfdata = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = sdf.parse(user_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return sdfdata.format(date);
		} else {
			return sdfdata.format(new Date().getTime());
		}
	}

	/**
	 * 获取当前时间
	 * yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new java.util.Date());
	}

	/**
	 * 获取当前时间
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new java.util.Date());
	}
	/**
	 * 
	 * @功能描述：
	 * <p>得到當前系統的hms</P>
	 * @param 
	 * @return 
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static String getCurrentTimeHuors() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return " "+sdf.format(new java.util.Date());
	}
	/**
	 * 获取星期几
	 * @param date
	 * @return
	 */
	public static String DateToWeek(Date date){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) {  
	        return null;  
	    }  
	    return WEEK[dayIndex - 1];  
	}
}