package com.cdjysdkj.diary.utils;

import java.io.File;

import android.os.Environment;

/**
 * @className SDCardStatu
 * @author goubaihu
 * @function SDCard״̬
 * @createTime 2014��11��28��
 */
public class SDCardStatu {

	public static boolean isSDCardAvailable() {
		// ���sd����״̬
		String sdState = Environment.getExternalStorageState();
		// �ж�SD���Ƿ����
		return sdState.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * �õ��ֻ��ڴ�·��
	 * 
	 * @return
	 */
	public static String get_SdCard_Categories() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	

}