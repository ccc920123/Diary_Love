package com.cdjysdkj.diary.application;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

	protected static String TAG;

	protected MyApplication mMyApplication;
	protected Resources mResources;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		TAG = this.getClass().getSimpleName();
		initConfigure();
	}

	private void initConfigure() {
		mContext = this;
		if (null == mMyApplication) {
			mMyApplication = MyApplication.getInstance();
		}
		mMyApplication.addActivity(this);

		mResources = getResources();
	}

	/**
	 * ActivityÌø×ª
	 * 
	 * @param context
	 * @param targetActivity
	 * @param bundle
	 */
	public void goToActivity(Context context, Class<?> targetActivity,
			Bundle bundle) {
		Intent intent = new Intent(context, targetActivity);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
