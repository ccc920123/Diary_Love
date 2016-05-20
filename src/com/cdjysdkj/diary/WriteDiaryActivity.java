package com.cdjysdkj.diary;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.tab.TextDiaryTab;
import com.cdjysdkj.diary.tabhelper.TextDiaryTabHelper;
import com.cdjysdkj.diary.utils.Date_U;
import com.cdjysdkj.diary.utils.StringUtils;
import com.cdjysdkj.diary.view.BiuEditText;
import com.cdjysdkj.diary.view.MoreWindow;

public class WriteDiaryActivity extends BaseHomeActivity {
	/**
	 * 时间
	 */
	private TextView mTime;
	/**
	 * 星期
	 */
	private TextView mWeek;
	/**
	 * 天气
	 */
	private TextView mWeather;
	/**
	 * 标题
	 */
	private EditText mTitle;
	/**
	 * 日记内容
	 */
	private BiuEditText mContent;
	private Calendar cal = Calendar.getInstance();
	private MoreWindow mMoreWindow;
	private TextDiaryTab diary;
	private boolean isNew = false;
	private boolean ischange = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_diary);
		getView();
		init();
		
	}

	private void init() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String option = bundle.getString("op");
		if ("new".equals(option)) {// 如果是创建新的
			isNew = true;
			ischange = true;
			mContent.setlistener();
			diary = new TextDiaryTab();
			mTime.setText(Date_U.getCurrentTime());
			diary.setCreatTime(Date_U.getCurrentTime());
			mWeek.setText(Date_U.DateToWeek(cal.getTime()));
			diary.setWeek(Date_U.DateToWeek(cal.getTime()));
			mWeather.setText("晴");
			diary.setWeather("晴");
		} else {// 查看
			diary = (TextDiaryTab) bundle.getSerializable("DIARY");
			if (diary != null) {
				mTime.setText(diary.getCreatTime());
				mWeek.setText(diary.getWeek());
				mWeather.setText(diary.getWeather());
				mTitle.setText(diary.getTitle());
				mContent.setText(diary.getText());
			} else {
				diary = new TextDiaryTab();
				mTime.setText(Date_U.getCurrentTime());
				diary.setCreatTime(Date_U.getCurrentTime());
				mWeek.setText(Date_U.DateToWeek(cal.getTime()));
				diary.setWeek(Date_U.DateToWeek(cal.getTime()));
				mWeather.setText("晴");
				diary.setWeather("晴");
			}
		}
		mWeather.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNew) {
					showMoreWindow(v);
				}
			}
		});

		mTitle.addTextChangedListener(watcher);
		mContent.addTextChangedListener(watcher);
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			ischange = true;
			actionBar.hideRightBtn(false);
			addActions();
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}
	};

	private void getView() {
		mTime = (TextView) findViewById(R.id.time);
		mWeek = (TextView) findViewById(R.id.week);
		mWeather = (TextView) findViewById(R.id.weather);
		mTitle = (EditText) findViewById(R.id.title_txt);
		mContent = (BiuEditText) findViewById(R.id.edit_diary_info);

	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "写日记";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onHomeActionClick() {
		saveDiary();
		goToActivity(this, TextDiaryActivity.class, null);
		finish();
	}

	private void saveDiary() {
		String title = mTitle.getEditableText().toString();
		String content = mContent.getEditableText().toString();
		diary.setWeather(mWeather.getText().toString());
		diary.setTitle(title);
		diary.setText(content);
		if (!StringUtils.isEmpty(title) || !StringUtils.isEmpty(content)) {
			TextDiaryTabHelper diaryTabHelper = TextDiaryTabHelper
					.getInstance();
			if (isNew) {
				diaryTabHelper.insert(diary);

			} else {
				int i = diaryTabHelper.update(diary);
				if (i > 0) {

				} else {
					diaryTabHelper.insert(diary);
				}
			}
		}
	}

	@Override
	protected void addActions() {

		if (ischange) {
			actionBar.setRightLogo(R.drawable.save_dairy);
			actionBar.setRightAction(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					saveDiary();
					actionBar.hideRightBtn(true);
					ischange = false;
				}
			});
		} else {
			actionBar.hideRightBtn(true);
		}
	}

	private void showMoreWindow(View view) {
		if (null == mMoreWindow) {
			mMoreWindow = new MoreWindow(this);
			mMoreWindow.init();
		}

		mMoreWindow.showMoreWindow(view, 100);
	}

	@Override
	public void onBackPressed() {
		saveDiary();
		goToActivity(this, TextDiaryActivity.class, null);
		finish();
	}
}
