package com.chenpan.heart.diary.utils;


public class StringUtils {

	public static boolean isEmpty(CharSequence str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	
	public static String[] splitString(String str,String label){
		String[] subStr = str.split(label);
		return subStr;
	}
}
