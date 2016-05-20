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
			actionBar.setTitle("��������");
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
						 * "�ٴ�ȷ����������") .show();
						 */
						mTip.setText("�ٴ�ȷ����������");
					} else if (isfirst && count >= 2) {// ��ʾ�����趨�ɹ�
						if (setfirst.equals(password)) {// �������õ�������ͬ
							editor.putBoolean("isFirstLock", false);
							editor.putString("password", setfirst);
							editor.putBoolean("isLock", true);
							// �ύ�޸�
							editor.commit();
							goToActivity(PasswordActivity.this,
									HomeActivity.class, null);
							finish();
						} else {
							count--;
							mTip.setTextColor(Color.RED);
							mTip.setText("������������벻ͬ������������");
							mTip.startAnimation(ButtonTools.shakeAnimation(3));

						}
					} else {// ���ǽ�����������ģ�����,�ȶ��������У�飬У��ɹ�����������
						if (password.equals(Opassword)) {
							goToActivity(PasswordActivity.this,
									HomeActivity.class, null);
							finish();
						} else {
							mTip.setTextColor(Color.RED);
							mTip.setText("�������");
							mTip.startAnimation(ButtonTools.shakeAnimation(3));
						}
					}
				} else {
					mTip.setTextColor(Color.RED);
					mTip.setText("�������벻�������ĸ���");
					mTip.startAnimation(ButtonTools.shakeAnimation(3));
				}

			}

		});
	}

	@Override
	protected String getActionBarTitle() {
		return "��   ¼";
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
