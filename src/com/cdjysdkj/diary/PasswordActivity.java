package com.cdjysdkj.diary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.utils.ButtonTools;
import com.cdjysdkj.diary.view.Lock9View;

public class PasswordActivity extends BaseHomeActivity {
	private Lock9View lock9View;

	private TextView mTip;
	private boolean isfirst;
	private int count = 0;
	private String setfirst = "";
	private SharedPreferences preferences;
	private Editor editor;
	private String Opassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		getview();
		init();
	}

	private void init() {
		Intent intent = getIntent();
		isfirst = intent.getBooleanExtra("first", false);
		if (isfirst) {
			actionBar.setTitle("设置密码");
		}
		preferences = getSharedPreferences(Constant.SHAREDPREFERENCES_NAME,
				MODE_PRIVATE);
		editor = preferences.edit();
		Opassword = preferences.getString("password", "");
	}

	private void getview() {
		lock9View = (Lock9View) findViewById(R.id.lock_9_view);
		mTip = (TextView) findViewById(R.id.tip);
		lock9View.setCallBack(new Lock9View.CallBack() {

			@Override
			public void onFinish(String password) {
				if (password.length() >= 4) {
					count++;
					if (isfirst && count < 2) {
						setfirst = password;
						/*
						 * ToastFactory .getToast(PasswordActivity.this,
						 * "再次确定手势密码") .show();
						 */
						mTip.setText("再次确定手势密码");
					} else if (isfirst && count >= 2) {// 提示密码设定成功
						if (setfirst.equals(password)) {// 两次设置的密码相同
							editor.putBoolean("isFirstLock", false);
							editor.putString("password", setfirst);
							editor.putBoolean("isLock", true);
							// 提交修改
							editor.commit();
							goToActivity(PasswordActivity.this,
									HomeActivity.class, null);
							finish();
						} else {
							count--;
							mTip.setTextColor(Color.RED);
							mTip.setText("两次输入的密码不同，请重新输入");
							mTip.startAnimation(ButtonTools.shakeAnimation(3));

						}
					} else {// 不是进来设置密码的，解锁,比对密码进行校验，校验成功进入主界面
						if (password.equals(Opassword)) {
							goToActivity(PasswordActivity.this,
									HomeActivity.class, null);
							finish();
						} else {
							mTip.setTextColor(Color.RED);
							mTip.setText("密码错误");
							mTip.startAnimation(ButtonTools.shakeAnimation(3));
						}
					}
				} else {
					mTip.setTextColor(Color.RED);
					mTip.setText("手势密码不能少于四个点");
					mTip.startAnimation(ButtonTools.shakeAnimation(3));
				}

			}

		});
	}

	@Override
	protected String getActionBarTitle() {
		return "登   录";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onHomeActionClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub
		actionBar.hideLeftBtn(true);
	}

	@Override
	public void onBackPressed() {
		if (isfirst) {
			goToActivity(this, HomeActivity.class, null);
			finish();
		} else {
			System.exit(0);
		}
		super.onBackPressed();
	}

}
