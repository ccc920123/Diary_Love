package com.cdjysdkj.diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.utils.StringUtils;
import com.cdjysdkj.diary.utils.ToastFactory;

public class AdviceActivity extends BaseHomeActivity {
	private EditText editText;
	private Button button;
	private boolean isabout = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice);
		getView();
		init();
	}

	private void init() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			isabout = getabout(bundle);
		}
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String email = editText.getEditableText().toString();
				if (!StringUtils.isEmpty(email)) {
					mail(email);
				} else {
					ToastFactory.getToast(AdviceActivity.this, "请输入您宝贵的意见或建议")
							.show();
				}
			}
		});
	}

	private void mail(String strjh) {

		// 必须明确使用mailto前缀来修饰邮件地址,如果使用
		// intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
		Uri uri = Uri.parse("mailto:" + "645503254@qq.com"); // 616707902@qq.com
																// mailto:
		// String[] email = {"3802**92@qq.com"};
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		// intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
		intent.putExtra(Intent.EXTRA_SUBJECT, "意见或建议"); // 主题
		intent.putExtra(Intent.EXTRA_TEXT, strjh); // 正文
		startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
		showDialog();

	}

	private void showDialog() {
		
	}

	private boolean getabout(Bundle bundle) {
		String str = bundle.getString("from");
		if ("about".equals(str)) {
			return true;
		} else {
			return false;
		}

	}

	private void getView() {
		editText = (EditText) findViewById(R.id.advice);
		button = (Button) findViewById(R.id.submit);
	}

	@Override
	protected String getActionBarTitle() {
		return "建议/意见";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		return false;
	}

	@Override
	protected void onHomeActionClick() {
		Afinish();
	}

	@Override
	protected void addActions() {

	}

	@Override
	public void onBackPressed() {
		Afinish();

	}

	private void Afinish() {
		if (isabout) {
			goToActivity(this, AboutActivity.class, null);
		} else {
			goToActivity(this, HomeActivity.class, null);
		}

		finish();
	}
}
