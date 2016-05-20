package com.cdjysdkj.diary.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cdjysdkj.diary.fragment.TraslateFrament;


public class WelcomeAdapter extends FragmentStatePagerAdapter {
	private int[] layout;

	public WelcomeAdapter(FragmentManager fragmentManager, int[] a ) {
		super(fragmentManager);
		layout = a;
	}

	@Override
	public android.support.v4.app.Fragment getItem(int arg0) {
		TraslateFrament fragment = new TraslateFrament();
		Bundle bundle = new Bundle();
		bundle.putInt("layout", layout[arg0]);
		bundle.putInt("key", arg0);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCount() {// ¼¸¸öÒ³Ãæ
		return 3;
	}

}