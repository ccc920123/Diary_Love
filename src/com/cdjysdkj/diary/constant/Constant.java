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

public static String KEY="8ccb360438f1eabae3afad356b734a6f";
public static String IDCARDKEY="f8c4fd868695065591d0265f6d8f9acd";//身份证查询
public static String IPKEY="25af939f2b9b1d97d3d0a5502be397a6";//IP查询
public static String POSTCODEKEY="521daf38c907561d68107f40e93b964c";//邮编查询
}
