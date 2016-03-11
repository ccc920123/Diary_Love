package com.chenpan.heart.diary;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.chenpan.heart.diary.adapter.TextAdapter;
import com.chenpan.heart.diary.anomotion.Dynamics;
import com.chenpan.heart.diary.application.BaseHomeActivity;
import com.chenpan.heart.diary.tab.TextDiaryTab;
import com.chenpan.heart.diary.tabhelper.TextDiaryTabHelper;
import com.chenpan.heart.diary.view.ListView3D;
import com.chenpan.heart.diary.widget.dialog.UpDialog;
import com.chenpan.heart.diary.widget.dialog.UpDialog.IDialogOnclickInterface;

public class TextDiaryActivity extends BaseHomeActivity implements
		IDialogOnclickInterface {
	private ListView3D listView;
	private TextAdapter adapter;
	private UpDialog myDialog;
	private int longClickPosition;
	private View currentItemView;
	private List<TextDiaryTab> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_diary);
		listView = (ListView3D) findViewById(R.id.my_listview);
		myDialog = new UpDialog(this, R.style.MyDialogStyle);
		adapter = new TextAdapter(this, getDatafromDatabase());
		listView.setAdapter(adapter);
		listView.setDynamics(new SimpleDynamics(0.9f, 0.6f));
		listView.setOnItemClickListener(onItemClickListener);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int[] location = new int[2];
				// 获取当前view在屏幕中的绝对位置
				// ,location[0]表示view的x坐标值,location[1]表示view的坐标值
				view.getLocationOnScreen(location);
				view.setBackgroundColor(getResources().getColor(R.color.transparent));
				currentItemView = view;
				longClickPosition = position;
				DisplayMetrics displayMetrics = new DisplayMetrics();
				Display display = TextDiaryActivity.this.getWindowManager()
						.getDefaultDisplay();
				display.getMetrics(displayMetrics);
				WindowManager.LayoutParams params = myDialog.getWindow()
						.getAttributes();
				params.gravity = Gravity.BOTTOM;
				params.y = display.getHeight() - location[1];
				myDialog.getWindow().setAttributes(params);
				myDialog.setCanceledOnTouchOutside(true);
				myDialog.show();
				listView.setOnItemClickListener(null);
				return false;
			}
		});
		myDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				listView.setOnItemClickListener(onItemClickListener);
			}
		});
	}
	private OnItemClickListener onItemClickListener=	new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Bundle bundle = new Bundle();
			bundle.putString("op", "look");
			TextDiaryTab diary = (TextDiaryTab) adapter.getItem(arg2);
			bundle.putSerializable("DIARY", diary);
			goToActivity(TextDiaryActivity.this, WriteDiaryActivity.class,
					bundle);
			finish();

		}
	};
	/**
	 * 从数据库中获取日记信息
	 * 
	 * @return
	 */
	private List<TextDiaryTab> getDatafromDatabase() {
		TextDiaryTabHelper diaryTabHelper = TextDiaryTabHelper.getInstance();
		list = diaryTabHelper.queryAll();
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	@Override
	protected String getActionBarTitle() {
		return "我的日记";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		return true;
	}

	@Override
	protected void onHomeActionClick() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}

	class SimpleDynamics extends Dynamics {

		private float mFrictionFactor;
		private float mSnapToFactor;

		public SimpleDynamics(final float frictionFactor,
				final float snapToFactor) {
			mFrictionFactor = frictionFactor;
			mSnapToFactor = snapToFactor;
		}

		@Override
		protected void onUpdate(final int dt) {
			mVelocity += getDistanceToLimit() * mSnapToFactor;
			// 速度 * 时间间隔 = 间隔时间内位移
			mPosition += mVelocity * dt / 1000;
			// 减速， 供下次onUpdate使用
			mVelocity *= mFrictionFactor;
		}
	}

	@Override
	protected void addActions() {
		actionBar.setRightAction(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("op", "new");
				goToActivity(TextDiaryActivity.this, WriteDiaryActivity.class,
						bundle);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}

	/**
	 * 左边
	 */
	@Override
	public void leftOnclick() {
		// TODO Auto-generated method stub
		myDialog.dismiss();
		listView.setOnItemClickListener(onItemClickListener);
		currentItemView.setBackgroundColor(getResources().getColor(
				android.R.color.white));
		Bundle bundle = new Bundle();
		bundle.putString("op", "look");
		TextDiaryTab diary = list.get(longClickPosition);
		bundle.putSerializable("DIARY", diary);
		goToActivity(TextDiaryActivity.this, WriteDiaryActivity.class, bundle);
		finish();
	}

	/**
	 * 右边
	 */
	@Override
	public void rightOnclick() {
		// TODO Auto-generated method stub
		myDialog.dismiss();
		listView.setOnItemClickListener(onItemClickListener);
		currentItemView.setBackgroundColor(getResources().getColor(
				android.R.color.white));
		TextDiaryTab diary = list.get(longClickPosition);
		list.remove(longClickPosition);
		TextDiaryTabHelper diaryTabHelper = TextDiaryTabHelper.getInstance();
		diaryTabHelper.delete(diary);
		
		adapter=new TextAdapter(this, list);
		listView.setAdapter(adapter);
	}
}
