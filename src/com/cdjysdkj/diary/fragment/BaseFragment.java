package com.cdjysdkj.diary.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-23
 * TODO
 */

public abstract class BaseFragment extends Fragment{
	public static String TAG;
	protected Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		mContext = getActivity();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
