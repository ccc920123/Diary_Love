package com.chenpan.heart.diary.application;

import android.app.Activity;
import android.app.Application;

import com.chenpan.heart.diary.utils.ActivityManagerUtils;

public class MyApplication extends Application{

	public static String TAG;
	
	private static MyApplication myApplication = null;
	
	
	public static MyApplication getInstance(){
		return myApplication;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		TAG = this.getClass().getSimpleName();
		//由于Application类本身已经单例，所以直接按以下处理即可。
		myApplication = this;
	}

	
	
	public static MyApplication getAppContext() {
		return myApplication;
	}

	public void addActivity(Activity ac){
		ActivityManagerUtils.getInstance().addActivity(ac);
	}
	
	public void exit(){
		ActivityManagerUtils.getInstance().removeAllActivity();
	}
	
	public Activity getTopActivity(){
		return ActivityManagerUtils.getInstance().getTopActivity();
	}
	

}
