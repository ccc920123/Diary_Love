package com.cdjysdkj.diary;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.cdjysdkj.diary.constant.Constant;

@SuppressWarnings("deprecation")
public class TabBillActivity extends TabActivity implements OnTabChangeListener {
	/**
	 * 当前选中的Tab标号
	 */
	private int mCurSelectTabIndex = 0;
	/**
	 * 默认选中第一个tab页 移动标志操作
	 */
	private final int INIT_SELECT = 0;
	/**
	 * 滑动动画执行时间
	 */
	private final int DELAY_TIME = 500;
	int statusBarHeight;
	private TabHost mTabHost;
	/**
	 * 存放Tab页中ImageView信息
	 */
	public List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_bill);
		init(savedInstanceState);
	}

	/**
	 * 初始化界面组件
	 */
	private void init(Bundle savedInstanceState) {
		// mTabHost = (TabHost) findViewById(R.id.tabhost);
		mTabHost = getTabHost(); // 因为集成了TabActivity
		mTabHost.setup();
		// 车辆信息
		Intent billIntent = new Intent(TabBillActivity.this, BillActivity.class);
		mTabHost.addTab(mTabHost
				.newTabSpec("zd")
				.setIndicator(
						composeLayout(R.drawable.bill, "我的账单"))
				.setContent(billIntent));
		// 外出检验
		Intent homeIntent = new Intent(TabBillActivity.this,
				AccountActivity.class);
		mTabHost.addTab(mTabHost
				.newTabSpec("zh")
				.setIndicator(
						composeLayout(R.drawable.account, "我的账户"))
				.setContent(homeIntent));

		// getMiddle();
		// 设置TabHost的背景颜色
		// tabHost.setBackgroundColor(Color.argb(20, 22, 70, 150));
		mTabHost.setBackgroundResource(R.drawable.tablebar_bg);

		// 设置当前选中的Tab页
		mTabHost.setCurrentTab(mCurSelectTabIndex);
		linearLayouts.get(0).setBackgroundDrawable(
				getResources().getDrawable(R.drawable.tab_widget2));
		// TabHost添加事件
		mTabHost.setOnTabChangedListener(this);
		// 初始化移动标识这里移到第一个tab页
		initCurSelectTab();

	}

	/**
	 * 初始化选中Tab覆盖图片
	 */
	public void initCurSelectTab() {
		// 默认选中移动图片位置
		Message msg = new Message();
		msg.what = INIT_SELECT;
		initSelectTabHandle.sendMessageDelayed(msg, DELAY_TIME);
	}

	/**
	 * 初始化选中Tab覆盖图片的Handler
	 */
	@SuppressLint("HandlerLeak")
	private Handler initSelectTabHandle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INIT_SELECT:
				moveTopSelect(Constant.tableindex);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 移动tab选中标识图片
	 * 
	 * @param selectIndex
	 * @param curIndex
	 */
	public void moveTopSelect(int selectIndex) {

		Constant.tableindex = selectIndex;
		// 改变当前选中按钮索引
		mCurSelectTabIndex = selectIndex;
	}

	/**
	 * 拼装成view
	 * 
	 * @param 图片
	 * @param 文字
	 * @return
	 */
	public View composeLayout(int i, String t) {
		View view;
		LayoutInflater inflater = LayoutInflater.from(TabBillActivity.this);
		view = inflater.inflate(R.layout.tab_bottom, null);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.tab_layout);
		ImageView iv = (ImageView) view.findViewById(R.id.tab_img);
		TextView text = (TextView) view.findViewById(R.id.tab_name);
		linearLayouts.add(layout);
		layout.setBackgroundResource(R.drawable.tab_widget);
		iv.setImageResource(i);
		text.setText(t);
		return view;
	}

	@Override
	public void onTabChanged(String tabId) {
		linearLayouts.get(0).setBackgroundDrawable(
				getResources().getDrawable(R.drawable.tab_widget));
		linearLayouts.get(1).setBackgroundDrawable(
				getResources().getDrawable(R.drawable.tab_widget));
		if (tabId.equalsIgnoreCase("zd")) {
			linearLayouts.get(0).setBackgroundDrawable(
					getResources().getDrawable(R.drawable.tab_widget2));
			// 移动底部背景图片
			moveTopSelect(0);
		} else if (tabId.equalsIgnoreCase("zh")) {
			linearLayouts.get(1).setBackgroundDrawable(
					getResources().getDrawable(R.drawable.tab_widget2));
			// 移动底部背景图片
			moveTopSelect(1);
		}

	}
}
