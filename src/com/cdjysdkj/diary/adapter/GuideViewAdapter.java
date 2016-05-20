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

	// �����б�
	private List<View> views;
	private Activity activity;

	

	public GuideViewAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
	}

	// ����viewλ�õĽ���
	@Override
	public void destroyItem(View view, int position, Object object) {
		((ViewPager) view).removeView(views.get(position));
	}

	@Override
	public void finishUpdate(View view) {
	}

	// ��õ�ǰ������
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// ��ʼ��viewλ�õĽ���
	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(views.get(position), 0);
		if (position == views.size() - 1) {
			ImageView mStartWeiboImageButton = (ImageView) view
					.findViewById(R.id.iv_start_weibo);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// ��������ֵ
					setGuided();

					// ��ת
					Intent intent = new Intent(activity, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					activity.startActivity(intent);
					activity.finish();
					activity.overridePendingTransition(0, 0);// ���finish()��SetFlagsʧЧ

				}

			});
		}
		return views.get(position);
	}

	/**
	 * method desc: �������ֵ ʹ�´�������������
	 */
	private void setGuided() {
		SharedPreferences preferences = activity.getSharedPreferences(
				Constant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// ��������
		editor.putBoolean("isFirstIn", false);
		// �ύ�޸�
		editor.commit();
	}

	// �ж��Ƿ��ɶ������ɽ���
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