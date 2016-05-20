package com.cdjysdkj.diary.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cdjysdkj.diary.HomeActivity;
import com.cdjysdkj.diary.R;
import com.cdjysdkj.diary.constant.Constant;

public class TraslateFrament extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		View view = inflater.inflate(bundle.getInt("layout"), null);
		int i=(Integer) bundle.get("key");
		if (i == 2) {
			ImageView mStartWeiboImageButton = (ImageView) view
					.findViewById(R.id.iv_start_weibo);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 设置引导值
					setGuided();

					// 跳转
					Intent intent = new Intent(getActivity(),
							HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					getActivity().startActivity(intent);
					getActivity().finish();
					getActivity().overridePendingTransition(0, 0);// 解决finish()后SetFlags失效

				}

			});
		}
		return view;
	}

	/**
	 * method desc: 变更引导值 使下次启动不再引导
	 */
	private void setGuided() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				Constant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isFirstIn", false);
		// 提交修改
		editor.commit();
	}
}