package com.cdjysdkj.diary;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;

import com.cdjysdkj.diary.adapter.BillExpandableAdapter;
import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.tab.DillTab;
import com.cdjysdkj.diary.tabhelper.DillTabHelper;

public class BillActivity extends BaseHomeActivity {
	private ExpandableListView expandableListView;
	private BillExpandableAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);
		getView();
		init();
	}

	/**
	 * 查询数据库
	 */
	private void init() {
		DillTabHelper dillTabHelper = DillTabHelper.getInstance();
		String[] groupItem = dillTabHelper.queryByGroup();
		DillTab child[][] = new DillTab[100][100] ;
		if (groupItem != null) {
			//child[][]=new DillTab[groupItem.length];
			for (int i = 0; i < groupItem.length; i++) {
				String timetom = groupItem[i];
				List<DillTab> dillTabs = dillTabHelper.queryBytime(timetom);
				child[i] = (DillTab[]) dillTabs.toArray(new DillTab[dillTabs.size()]);
			}
		}
		adapter = new BillExpandableAdapter(groupItem, child, this);
		expandableListView.setGroupIndicator(null);
		expandableListView.setAdapter(adapter);

	}

	private void getView() {
		expandableListView = (ExpandableListView) findViewById(R.id.list_expandaable);
	}

	@Override
	protected String getActionBarTitle() {
		return "我的账单";
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

	@Override
	protected void addActions() {
		actionBar.setRightLogo(R.drawable.ic_action_add);
		actionBar.setRightAction(new OnClickListener() {

			@Override
			public void onClick(View arg0) {// 跳转到新增账单界面
				goToActivity(BillActivity.this, AddBillActivity.class, null);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}
}
