package com.cdjysdkj.diary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.utils.SDCardStatu;
import com.cdjysdkj.diary.view.SplshView;

public class WelcomeActivity extends Activity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;

	// ��ת��ʱ
	private static final long TIME = 2000;

	private FrameLayout fl;
	private SplshView sp;
	/**
	 * Handler:��ת����ͬ����
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case GO_GUIDE: // ��������ҳ
				goGuide();
				break;

			case GO_HOME: // ������ҳ
				goHome();
				break;

			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (SDCardStatu.isSDCardAvailable()) {
			File path = new File(Constant.MYAPP_PATH);
			if (!path.exists())
				path.mkdirs();
			File file = new File(Constant.MYAPP_PATH,"test.jpg");
			 Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
			 FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(file);
				 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally{
				bitmap.recycle();
				
			}
			
		}
		skips();
	}

	/**
	 * ģ���������
	 */
	private void loadingData(final int i) {
		// ���ݼ�����ɣ���ת����������������ɢ�Ի�
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				//
				sp.splshDisapaer();
				// ʹ��Handler��postDelayed������3���ִ����ת��MainActivity
				mHandler.sendEmptyMessageDelayed(i, TIME);
			}
		}, 2000);// 6s�Ժ�ִ��run����ķ���
	}

	private void skips() {
		// ��ȡSharedPreferences����Ҫ������
		// ʹ��SharedPreferences����¼�����ʹ�ô���
		SharedPreferences preferences = getSharedPreferences(
				Constant.SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// ȡ����Ӧ��ֵ�����û�и�ֵ��˵����δд�룬��true��ΪĬ��ֵ
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// �жϳ�����ڼ������У�����ǵ�һ����������ת���������棬������ת��������
		if (!isFirstIn) {
			fl = new FrameLayout(this);
			ImageView contentView = new ImageView(this);
			contentView.setBackgroundResource(R.drawable.guide_03);
			fl.addView(contentView);
			sp = new SplshView(this);
			// sp.setAlpha(0.8f);
			fl.addView(sp);
			setContentView(fl);
			// ģ���������
			loadingData(GO_HOME);

		} else {
			goGuide();
			// loadingData(GO_GUIDE);
		}

	}

	private void goGuide() { // ��������ҳ
		Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
		startActivity(intent);
		finish();
	}

	private void goHome() { // ������ҳ
		SharedPreferences preferences = getSharedPreferences(
				Constant.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		boolean islock = preferences.getBoolean("isLock", false);
		if (islock) {
			Intent intent = new Intent(getApplicationContext(),
					PasswordActivity.class);
			// intent.putExtra("str", "MainActivity");// ��������
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					HomeActivity.class);
			// intent.putExtra("str", "MainActivity");// ��������
			startActivity(intent);
			finish();
		}
	}
	@Override
	public void onBackPressed() {
		System.exit(0);
		super.onBackPressed();
	}
}
