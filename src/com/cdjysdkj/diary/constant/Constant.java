package com.cdjysdkj.diary.constant;

import java.io.File;

import android.os.Environment;

public class Constant {
	/**
	 * sp引用
	 */
  public static String SHAREDPREFERENCES_NAME="DIARY_SP";
  /**
   * sd卡存储路径
   * 格式：mnt/storage0/DIARY/+文件夹名
   */
  public static String MYAPP_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator+"DIARY"+ File.separator;
public  static int  tableindex;
public static int HOMETIMES=0;
public static String MYAPP_PATH_ICON=Environment.getExternalStorageDirectory().getAbsolutePath()
+ File.separator+"DIARY"+ File.separator+"test.jpg";
}
