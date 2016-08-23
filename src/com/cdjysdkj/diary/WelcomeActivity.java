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
import android.widget.RelativeLayout;

import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.utils.SDCardStatu;
import com.cdjysdkj.diary.view.SplshView;

public class WelcomeActivity extends Activity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;

	// 跳转延时
	private static final long TIME = 2000;

	private FrameLayout fl;
	private SplshView sp;
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
		RelativeLayout adsParent = (RelativeLayout) this.findViewById(R.id.adsRl); 	
		SplashAdListener listener=new SplashAdListener() 
		{   
			@Override    
			public void onAdDismissed() 
			{    
//				Log.i("RSplashActivity", "onAdDismissed");
				jumpWhenCanClick();// 跳转至您的应用主界面  
		     }
		   @Override    
		   public void onAdFailed(String arg0) 
		   {    
			   skips();
		   }
		   @Override  
		   public void onAdPresent() { 
//			   Log.i("RSplashActivity", "onAdPresent"); 
		   }
		   @Override   
		   public void onAdClick() { 
//			   Log.i("RSplashActivity", "onAdClick"); 
			   //设置开屏可接受点击时，该回调可用     
		   }
		};
		/**
		 * 构造函数：
		 * new SplashAd(Context context, ViewGroup adsParent,
		 * 				SplashAdListener listener,String adPlaceId, boolean canClick, SplashType splashType);
		 */
		String adPlaceId ="2723339";//重要：请填上您的广告位ID
		new SplashAd(this, adsParent, listener, adPlaceId, true);
		
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
		
	}
	/**
	 * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加waitingOnRestart判断。
	 * 另外，点击开屏还需要在onRestart中调用jumpWhenCanClick接口。
	 */
	public boolean waitingOnRestart=false;
	private void jumpWhenCanClick() {
		if(this.hasWindowFocus()||waitingOnRestart){
			skips();
		}else{
			waitingOnRestart=true;
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
				sp.splshDisapaer();
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
		fl = new FrameLayout(this);
		ImageView contentView = new ImageView(this);
		contentView.setBackgroundResource(R.drawable.guide_03);
		fl.addView(contentView);
		sp = new SplshView(this);
		sp.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.guide_04));
		// sp.setAlpha(0.8f);
		fl.addView(sp);
		setContentView(fl);
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
	@Override
	public void onBackPressed() {
		System.exit(0);
		super.onBackPressed();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		if(waitingOnRestart){
			jumpWhenCanClick();
		}
	}
}
