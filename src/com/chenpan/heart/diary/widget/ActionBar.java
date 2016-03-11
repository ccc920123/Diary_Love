/*
 * Copyright (C) 2010 Johan Nilsson <http://markupartist.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chenpan.heart.diary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenpan.heart.diary.R;

public class ActionBar extends RelativeLayout {

	private LayoutInflater mInflater;
	private RelativeLayout mBarView;
	private ImageView mLeftBtn;
	private ImageView mRightBtn;
	// private View mHomeView;
	private TextView mTitleView;

	public ActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mBarView = (RelativeLayout) mInflater.inflate(R.layout.actionbar, null);
		addView(mBarView);
		mLeftBtn = (ImageView) mBarView.findViewById(R.id.topbar_menu_left);
		mRightBtn = (ImageView) mBarView.findViewById(R.id.topbar_menu_right);
		mTitleView = (TextView) mBarView.findViewById(R.id.topbar_title);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ActionBar);
		CharSequence title = a.getString(R.styleable.ActionBar_title);
		if (title != null) {
			setTitle(title);
		}
		a.recycle();
	}

	/**
	 * œ‘ æ◊Û±ﬂÕº∞∏
	 * 
	 * @param resId
	 */
	public void setHomeLogo(int resId) {
		// TODO: Add possibility to add an IntentAction as well.
		mLeftBtn.setImageResource(resId);
		mLeftBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * œ‘ æ”“±ﬂÕº∞∏£¨false≤ªœ‘ æ
	 * 
	 * @param show
	 */
	public void setDisplayHomeAsUpEnabled(boolean show) {
		mRightBtn.setVisibility(show ? View.VISIBLE : View.GONE);

	}

	public void setTitle(CharSequence title) {
		mTitleView.setText(title);
	}

	public void setTitle(int resid) {
		mTitleView.setText(resid);
	}

	public void setHomeAction(OnClickListener onHomeActionClick) {
		mLeftBtn.setOnClickListener(onHomeActionClick);
	}

	/**
	 * œ‘ æ”“±ﬂÕº∞∏
	 * 
	 * @param resId
	 */
	public void setRightLogo(int resId) {
		// TODO: Add possibility to add an IntentAction as well.
		mRightBtn.setImageResource(resId);
		mRightBtn.setVisibility(View.VISIBLE);
	}
	/**
	 * ”“±ﬂº‡Ã˝
	 * @param onHomeActionClick
	 */
	public void setRightAction(OnClickListener onHomeActionClick){
		mRightBtn.setOnClickListener(onHomeActionClick);
	}
	/**
	 * “˛≤ÿ”“±ﬂ
	 * @param ishide
	 */
	public void hideRightBtn(boolean ishide){
		mRightBtn.setVisibility(ishide==true?View.GONE:View.VISIBLE);
	}
	/**
	 * “˛≤ÿ◊Û±ﬂ
	 * @param ishide
	 */
	public void hideLeftBtn(boolean ishide){
		mLeftBtn.setVisibility(ishide==true?View.GONE:View.VISIBLE);
	}
}
