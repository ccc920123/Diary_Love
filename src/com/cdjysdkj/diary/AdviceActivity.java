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
					ToastFactory.getToast(AdviceActivity.this, "��������������������")
							.show();
				}
			}
		});
	}

	private void mail(String strjh) {

		// ������ȷʹ��mailtoǰ׺�������ʼ���ַ,���ʹ��
		// intent.putExtra(Intent.EXTRA_EMAIL, email)�������ƥ�䲻���κ�Ӧ��
		Uri uri = Uri.parse("mailto:" + "645503254@qq.com"); // 616707902@qq.com
																// mailto:
		// String[] email = {"3802**92@qq.com"};
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		// intent.putExtra(Intent.EXTRA_CC, email); // ������
		intent.putExtra(Intent.EXTRA_SUBJECT, "�������"); // ����
		intent.putExtra(Intent.EXTRA_TEXT, strjh); // ����
		startActivity(Intent.createChooser(intent, "��ѡ���ʼ���Ӧ��"));
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
		return "����/���";
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
