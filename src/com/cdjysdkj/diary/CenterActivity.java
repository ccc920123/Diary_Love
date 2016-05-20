package com.cdjysdkj.diary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cdjysdkj.diary.application.BaseHomeActivity;

@SuppressLint("SetJavaScriptEnabled")
public class CenterActivity extends BaseHomeActivity {
	private WebView mWebview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center);
		getView();
		init();
	}
	private void getView() {
		mWebview = (WebView) findViewById(R.id.pro_web);
	}

	private void init() {
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.loadUrl("file:///android_asset/test.html");
		mWebview.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;

			}
		});	
	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "≤‚ ‘";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onHomeActionClick() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}

}
