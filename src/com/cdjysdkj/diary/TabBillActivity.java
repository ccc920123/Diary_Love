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
	 * ��ǰѡ�е�Tab���
	 */
	private int mCurSelectTabIndex = 0;
	/**
	 * Ĭ��ѡ�е�һ��tabҳ �ƶ���־����
	 */
	private final int INIT_SELECT = 0;
	/**
	 * ��������ִ��ʱ��
	 */
	private final int DELAY_TIME = 500;
	int statusBarHeight;
	private TabHost mTabHost;
	/**
	 * ���Tabҳ��ImageView��Ϣ
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
	 * ��ʼ���������
	 */
	private void init(Bundle savedInstanceState) {
		// mTabHost = (TabHost) findViewById(R.id.tabhost);
		mTabHost = getTabHost(); // ��Ϊ������TabActivity
		mTabHost.setup();
		// ������Ϣ
		Intent billIntent = new Intent(TabBillActivity.this, BillActivity.class);
		mTabHost.addTab(mTabHost
				.newTabSpec("zd")
				.setIndicator(
						composeLayout(R.drawable.bill, "�ҵ��˵�"))
				.setContent(billIntent));
		// �������
		Intent homeIntent = new Intent(TabBillActivity.this,
				AccountActivity.class);
		mTabHost.addTab(mTabHost
				.newTabSpec("zh")
				.setIndicator(
						composeLayout(R.drawable.account, "�ҵ��˻�"))
				.setContent(homeIntent));

		// getMiddle();
		// ����TabHost�ı�����ɫ
		// tabHost.setBackgroundColor(Color.argb(20, 22, 70, 150));
		mTabHost.setBackgroundResource(R.drawable.tablebar_bg);

		// ���õ�ǰѡ�е�Tabҳ
		mTabHost.setCurrentTab(mCurSelectTabIndex);
		linearLayouts.get(0).setBackgroundDrawable(
				getResources().getDrawable(R.drawable.tab_widget2));
		// TabHost����¼�
		mTabHost.setOnTabChangedListener(this);
		// ��ʼ���ƶ���ʶ�����Ƶ���һ��tabҳ
		initCurSelectTab();

	}

	/**
	 * ��ʼ��ѡ��Tab����ͼƬ
	 */
	public void initCurSelectTab() {
		// Ĭ��ѡ���ƶ�ͼƬλ��
		Message msg = new Message();
		msg.what = INIT_SELECT;
		initSelectTabHandle.sendMessageDelayed(msg, DELAY_TIME);
	}

	/**
	 * ��ʼ��ѡ��Tab����ͼƬ��Handler
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
	 * �ƶ�tabѡ�б�ʶͼƬ
	 * 
	 * @param selectIndex
	 * @param curIndex
	 */
	public void moveTopSelect(int selectIndex) {

		Constant.tableindex = selectIndex;
		// �ı䵱ǰѡ�а�ť����
		mCurSelectTabIndex = selectIndex;
	}

	/**
	 * ƴװ��view
	 * 
	 * @param ͼƬ
	 * @param ����
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
			// �ƶ��ײ�����ͼƬ
			moveTopSelect(0);
		} else if (tabId.equalsIgnoreCase("zh")) {
			linearLayouts.get(1).setBackgroundDrawable(
					getResources().getDrawable(R.drawable.tab_widget2));
			// �ƶ��ײ�����ͼƬ
			moveTopSelect(1);
		}

	}
}
