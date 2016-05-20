package com.cdjysdkj.diary.application;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cdjysdkj.diary.R;
import com.cdjysdkj.diary.utils.DisplayMetricsEN;
import com.cdjysdkj.diary.widget.ActionBar;

public abstract class BaseHomeActivity extends BaseActivity {

	protected ActionBar actionBar;
	private RelativeLayout layout;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.activity_base_home);
		WindowManager wm = this.getWindowManager();
	     DisplayMetricsEN.width=wm.getDefaultDisplay().getWidth();
	     DisplayMetricsEN.heigth=wm.getDefaultDisplay().getHeight();
		initActionBar();

//		initFragment();

	}

	private void initActionBar() {
		actionBar = (ActionBar) findViewById(R.id.actionbar_base);
		actionBar.setTitle(getActionBarTitle());
		actionBar.setDisplayHomeAsUpEnabled(isHomeAsUpEnabled() == true ? true
				: false);
		actionBar.setHomeAction(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onHomeActionClick();
			}
		});
		addActions();
		layout = (RelativeLayout) findViewById(R.id.content_frame_base);
	}

	
	public void setContentView(int layoutResID) {
	View.inflate(this, layoutResID, layout);
}

	/**
	 * 设置标题
	 * 
	 * @return
	 */
	protected abstract String getActionBarTitle();

	/**
	 * 是否右边按键
	 * 
	 * @return
	 */
	protected abstract boolean isHomeAsUpEnabled();

	/**
	 * 返回键监听
	 */
	protected abstract void onHomeActionClick();

	/**
	 * 设置actionbar的功能
	 */
	protected abstract void addActions();


}
