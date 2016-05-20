package com.cdjysdkj.diary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cdjysdkj.diary.application.BaseHomeActivity;

@SuppressLint("SetJavaScriptEnabled")
public class AboutActivity extends BaseHomeActivity {
	private WebView mWebview;
	private TextView mEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		getView();
		init();
	}

	private void getView() {
		mWebview = (WebView) findViewById(R.id.pro_web);
		mEmail=(TextView) findViewById(R.id.email);
	}

	private void init() {
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.loadUrl("file:///android_asset/about.html");
		mWebview.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;

			}
		});	
		mEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle=new Bundle();
				bundle.putString("from", "about");
				goToActivity(AboutActivity.this, AdviceActivity.class, bundle);
				finish();
			}
		});
	}

	@Override
	protected String getActionBarTitle() {
		return "นุ    ำฺ";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		return false;
	}

	@Override
	protected void onHomeActionClick() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}

	@Override
	protected void addActions() {
		
	}

	@Override
	public void onBackPressed() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}

}
