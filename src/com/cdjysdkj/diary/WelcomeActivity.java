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
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.utils.SDCardStatu;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

public class WelcomeActivity extends Activity implements SplashADListener {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;

	// ��ת��ʱ
	private static final long TIME = 2000;

	private ViewGroup container;
	private SplashAD splashAD;

	public boolean canJump = false;
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
		container = (ViewGroup) this.findViewById(R.id.splash_container);
		splashAD = new SplashAD(this, container, "1105647100",
				"6040218457195678", this, 3000);
		/**
		 * ����������������µĽӿڣ������ɿ������ڴ��������ÿ����ĳ�ʱʱ�� SplashAD(Activity activity,
		 * ViewGroup container, String appId, String posId, SplashADListener
		 * adListener, int fetchDelay) fetchDelay������ʾ�����ĳ�ʱʱ�䣬��λΪms��ȡֵ��Χ[3000,
		 * 5000]������Ϊ0ʱ��ʾʹ�ù��ͨ��Ĭ�Ͽ�����ʱ����
		 * 
		 * splashAD = new SplashAD(this, container, Constants.APPID,
		 * Constants.SplashPosID, this, 3000);�������ó�ʱʱ��Ϊ3000ms
		 */

		if (SDCardStatu.isSDCardAvailable()) {
			File path = new File(Constant.MYAPP_PATH);
			if (!path.exists())
				path.mkdirs();
			File file = new File(Constant.MYAPP_PATH, "test.jpg");
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.app_logo);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
						fileOutputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				bitmap.recycle();

			}

		}

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
			jump();

		} else {
			goGuide();
			// loadingData(GO_GUIDE);
		}

	}

	private void jump() {
		ImageView contentView = new ImageView(this);
		contentView.setBackgroundResource(R.drawable.guide_03);
		// ģ���������
		loadingData(GO_HOME);
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

	/** ����ҳ��ý�ֹ�û��Է��ذ�ť�Ŀ��� */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onADClicked() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onADDismissed() {

		if (canJump) {
			skips();
		} else {
			canJump = true;
		}

	}

	@Override
	public void onADPresent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNoAD(int arg0) {
		/** ������ع��ʧ�ܣ���ֱ����ת */
		if (canJump) {
			skips();
		} else {
			canJump = true;
		}

	}

	

	@Override
	protected void onPause() {
		super.onPause();
		canJump = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (canJump) {
			skips();
		}
		canJump = true;
	}
}
