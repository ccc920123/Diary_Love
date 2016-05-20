package com.cdjysdkj.diary.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cdjysdkj.diary.HomeActivity;
import com.cdjysdkj.diary.R;
import com.cdjysdkj.diary.constant.Constant;

class GuideViewAdapter extends PagerAdapter {

	// 界面列表
	private List<View> views;
	private Activity activity;

	

	public GuideViewAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
	}

	// 销毁view位置的界面
	@Override
	public void destroyItem(View view, int position, Object object) {
		((ViewPager) view).removeView(views.get(position));
	}

	@Override
	public void finishUpdate(View view) {
	}

	// 获得当前界面数
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// 初始化view位置的界面
	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(views.get(position), 0);
		if (position == views.size() - 1) {
			ImageView mStartWeiboImageButton = (ImageView) view
					.findViewById(R.id.iv_start_weibo);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 设置引导值
					setGuided();

					// 跳转
					Intent intent = new Intent(activity, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					activity.startActivity(intent);
					activity.finish();
					activity.overridePendingTransition(0, 0);// 解决finish()后SetFlags失效

				}

			});
		}
		return views.get(position);
	}

	/**
	 * method desc: 变更引导值 使下次启动不再引导
	 */
	private void setGuided() {
		SharedPreferences preferences = activity.getSharedPreferences(
				Constant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isFirstIn", false);
		// 提交修改
		editor.commit();
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}