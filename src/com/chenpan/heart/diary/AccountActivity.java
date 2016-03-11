package com.chenpan.heart.diary;

import java.util.List;

import android.os.Bundle;
import android.widget.TextView;

import com.chenpan.heart.diary.application.BaseHomeActivity;
import com.chenpan.heart.diary.tab.DillTab;
import com.chenpan.heart.diary.tabhelper.DillTabHelper;

public class AccountActivity extends BaseHomeActivity {
	private TextView zongzican_text,xianjin_text,chuxu_text,xinyongka_text,web_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		getView();
		init();
	}
/**
 * 访问数据库 从数据库中获取数据
 */
	private void init() {
		DillTabHelper dillTabHelper=DillTabHelper.getInstance();
		double xianjin=getMoney(dillTabHelper,"现  金");
		double chuxu=getMoney(dillTabHelper,"储蓄卡");
		double xinyongka=getMoney(dillTabHelper,"信用卡");
		double zhifub=getMoney(dillTabHelper,"支付宝");
		xianjin_text.setText(String.valueOf(xianjin));
		chuxu_text.setText(String.valueOf(chuxu));
		xinyongka_text.setText(String.valueOf(xinyongka));
		web_text.setText(String.valueOf(zhifub));
		zongzican_text.setText(String.valueOf(zhifub+chuxu+xianjin+xinyongka));
		
		
	}

	private double getMoney(DillTabHelper dillTabHelper,String account) {
		double money=0.0;
		List<DillTab> dillTabs = dillTabHelper.queryByAccount(account);
		if(dillTabs!=null&&dillTabs.size()>0){
			for (int i = 0; i < dillTabs.size(); i++) {
				DillTab dillTab = dillTabs.get(i);
				int state = Integer.parseInt(dillTab.getTakeType());
				if (state == 0) {// 支出
					money -= Double.parseDouble(dillTab.getMoney());
				}
				if (state == 1) {// 收入
					money += Double.parseDouble(dillTab.getMoney());
				}
			}
		}else{
			
		}
		
		return money;
}
	private void getView() {
		web_text=(TextView) findViewById(R.id.web_text);
		xinyongka_text=(TextView) findViewById(R.id.xinyongka_text);
		chuxu_text=(TextView) findViewById(R.id.chuxu_text);
		xianjin_text=(TextView)findViewById(R.id.xianjin_text);
		zongzican_text=(TextView) findViewById(R.id.zongzican_text);
	}

	@Override
	protected String getActionBarTitle() {
		return "我的账户";
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
		/*actionBar.setRightLogo(R.drawable.ic_action_tran);
		actionBar.setRightAction(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			}
		});*/
		actionBar.hideRightBtn(true);
	}

	@Override
	public void onBackPressed() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}

}
