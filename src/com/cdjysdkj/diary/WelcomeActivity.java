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

	// 跳转延时
	private static final long TIME = 2000;

	private ViewGroup container;
	private SplashAD splashAD;

	public boolean canJump = false;
	/**
	 * Handler:跳转到不同界面
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case GO_GUIDE: // 跳至引导页
				goGuide();
				break;

			case GO_HOME: // 跳至主页
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
		 * 开屏广告现已增加新的接口，可以由开发者在代码中设置开屏的超时时长 SplashAD(Activity activity,
		 * ViewGroup container, String appId, String posId, SplashADListener
		 * adListener, int fetchDelay) fetchDelay参数表示开屏的超时时间，单位为ms，取值范围[3000,
		 * 5000]。设置为0时表示使用广点通的默认开屏超时配置
		 * 
		 * splashAD = new SplashAD(this, container, Constants.APPID,
		 * Constants.SplashPosID, this, 3000);可以设置超时时长为3000ms
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
	 * 模拟加载数据
	 */
	private void loadingData(final int i) {
		// 数据加载完成，旋转动画结束，开启扩散对话
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				//
				// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
				mHandler.sendEmptyMessageDelayed(i, TIME);
			}
		}, 2000);// 6s以后执行run里面的方法
	}

	private void skips() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				Constant.SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
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
		// 模拟加载数据
		loadingData(GO_HOME);
	}

	private void goGuide() { // 跳至引导页
		Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
		startActivity(intent);
		finish();
	}

	private void goHome() { // 跳至主页
		SharedPreferences preferences = getSharedPreferences(
				Constant.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		boolean islock = preferences.getBoolean("isLock", false);
		if (islock) {
			Intent intent = new Intent(getApplicationContext(),
					PasswordActivity.class);
			// intent.putExtra("str", "MainActivity");// 传递数据
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					HomeActivity.class);
			// intent.putExtra("str", "MainActivity");// 传递数据
			startActivity(intent);
			finish();
		}
	}

	/** 开屏页最好禁止用户对返回按钮的控制 */
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
		/** 如果加载广告失败，则直接跳转 */
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
