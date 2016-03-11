package com.chenpan.heart.diary.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

public class Date_U {
	
	public static final int WEEKDAYS = 7;
	public static String[] WEEK = { "������", "����һ", "���ڶ�", "������", "������", "������",
			"������" };
	/**
	 * ���˷���������Ҫת����ʱ���������磨"2014��06��14��16ʱ09��00��"������ʱ���
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String data(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy��MM��dd��HHʱmm��ss��");
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
	 * ���ݴ��ݵ����͸�ʽ��ʱ��
	 * 
	 * @param str
	 * @param type
	 *            ���磺yy-MM-dd
	 * @return
	 */
	public static String getDateTimeByMillisecond() {

		Date date = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��");

		String time = format.format(date);

		return time;
	}

	/**
	 * ���ݴ��ݵ����͸�ʽ��ʱ��
	 * 
	 * @param str
	 * @param type
	 *            ���磺yy-MM-dd
	 * @return
	 */
	public static String getDateTimeByMilli() {

		Date date = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String time = format.format(date);

		return time;
	}

	/**
	 * ���ַ���תΪʱ���
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
	 * @�����������������ʱ���ַ���ת��Ϊ�̶���ʽ���ַ���
	 * @param
	 * @return yyyy-MM-ddת��Ϊyyyy-MM
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
	 * ��ȡ��ǰʱ��
	 * yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new java.util.Date());
	}

	/**
	 * ��ȡ��ǰʱ��
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new java.util.Date());
	}
	/**
	 * 
	 * @����������
	 * <p>�õ���ǰϵ�y��hms</P>
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
	 * ��ȡ���ڼ�
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