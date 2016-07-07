package com.cdjysdkj.diary;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cdjysdkj.diary.adapter.slidingadapter.Item;
import com.cdjysdkj.diary.adapter.slidingadapter.MyFancyCoverFlowAdapter;
import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.application.MyApplication;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.network.VolleyInterface;
import com.cdjysdkj.diary.network.VolleyRequest;
import com.cdjysdkj.diary.sliding.customsiliding.FancyCoverFlow;
import com.cdjysdkj.diary.view.NumberProgressBar;

/**
 * 星座运势 ConstellationActivity.java
 * 
 * @author 陈渝金
 * @Description
 * @Create Time 2016-6-12
 */
public class ConstellationActivity extends BaseHomeActivity {

	private FancyCoverFlow mfancyCoverFlow;
	private MyFancyCoverFlowAdapter mMyFancyCoverFlowAdapter;
	private String[] title = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座",
			"处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
	private String[] dateName = { "1/20-2/18", "2/19-3/20", "3/21-4/19",
			"4/20-5/20", "5/21-6/21", "6/22-7/22", "7/23-8/22", "8/23-9/22",
			"9/23-10/23", "10/24-11/22", "11/23-12/21", "12/22-1/19" };
    private int[] image={R.drawable.shuiping,R.drawable.shuangyu,R.drawable.baiyang,
    		R.drawable.jinniu,R.drawable.shuangzi,R.drawable.juxie,R.drawable.shizi,
    		R.drawable.chunv,R.drawable.tianping,R.drawable.tianxie,R.drawable.sheshou,R.drawable.mojie};
	String url = "http://web.juhe.cn:8080/constellation/getAll";
	private TextView summaryText;
	private TextView health, color, number, qfriend;
	private NumberProgressBar all_prgressbar, love_prgressbar, work_prgressbar,
			money_prgressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_main);
		List<Item> mFancyCoverFlows = new ArrayList<Item>();
		for (int i = 0; i < 12; i++) {
			Item item = new Item();
			item.setName(title[i]);
			item.setDateName(dateName[i]);
			item.setImage(image[i]);
			item.setSelected(false);
			mFancyCoverFlows.add(item);
		}
		findView();
		mMyFancyCoverFlowAdapter = new MyFancyCoverFlowAdapter(this,
				mFancyCoverFlows);
		mfancyCoverFlow.setAdapter(mMyFancyCoverFlowAdapter);
		mMyFancyCoverFlowAdapter.notifyDataSetChanged();
		mfancyCoverFlow.setUnselectedAlpha(0.5f);// 通明度
		mfancyCoverFlow.setUnselectedSaturation(0.5f);// 设置选中的饱和度
		mfancyCoverFlow.setUnselectedScale(0.3f);// 设置选中的规模
		mfancyCoverFlow.setSpacing(0);// 设置间距
		mfancyCoverFlow.setMaxRotation(0);// 设置最大旋转
		mfancyCoverFlow.setScaleDownGravity(0.5f);
		mfancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
		int num = Integer.MAX_VALUE / 2 % mFancyCoverFlows.size();
		int selectPosition = Integer.MAX_VALUE / 2 - num;
		mfancyCoverFlow.setSelection(selectPosition);
		mfancyCoverFlow
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						Item homeFancyCoverFlow = (Item) mfancyCoverFlow
								.getSelectedItem();
						if (homeFancyCoverFlow != null) {
							// Toast.makeText(ConstellationActivity.this,homeFancyCoverFlow.getName(),Toast.LENGTH_SHORT).show();
							queryConstelltion(homeFancyCoverFlow.getName());
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

	}

	private void findView() {
		mfancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
		summaryText = (TextView) findViewById(R.id.summary);
		health = (TextView) findViewById(R.id.health);
		color = (TextView) findViewById(R.id.color);
		number = (TextView) findViewById(R.id.number);
		qfriend = (TextView) findViewById(R.id.qfriend);
		all_prgressbar = (NumberProgressBar) findViewById(R.id.all_prgressbar);
		love_prgressbar = (NumberProgressBar) findViewById(R.id.love_prgressbar);
		work_prgressbar = (NumberProgressBar) findViewById(R.id.work_prgressbar);
		money_prgressbar = (NumberProgressBar) findViewById(R.id.money_prgressbar);

	}

	private void queryConstelltion(String name) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String urls = url + "?consName=" + name + "&type=today&key="
				+ Constant.CONSTELLATIONKEY;
		VolleyRequest.RequestGetString(urls, "costip", new VolleyInterface() {
			@Override
			public void onMySuccess(String result) {
				// ipShowdata.setLayoutAnimation(getAnimation());
				try {
					JSONObject jsonObject = new JSONObject(result);
					showData(jsonObject);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onMyError(VolleyError result) {
			}

			@Override
			public void onMySuccess(JSONObject result) {
				// TODO Auto-generated method stub

			}
		});

	}

	protected void showData(JSONObject jsonObject) throws JSONException {
		int code = jsonObject.getInt("resultcode");
		if (code == 200) {
			// JSONObject object=new JSONObject(jsonObject.getString("result"));
			summaryText.setText(jsonObject.getString("summary"));
			health.setText(jsonObject.getString("health"));
			color.setText(jsonObject.getString("color"));
			number.setText(jsonObject.getString("number"));
			qfriend.setText(jsonObject.getString("QFriend"));
			String allstr=jsonObject.getString("all");
			allstr=allstr.substring(0, allstr.length()-1);
			all_prgressbar.setProgress(Integer.parseInt(allstr));
			String lovestr=jsonObject.getString("love");
			lovestr=lovestr.substring(0,lovestr.length()-1);
			love_prgressbar.setProgress(Integer.parseInt(lovestr));
			String workstr=jsonObject.getString("work");
			workstr=workstr.substring(0,workstr.length()-1);
			work_prgressbar.setProgress(Integer.parseInt(workstr));
			String moneystr=jsonObject.getString("money");
			moneystr=moneystr.substring(0,moneystr.length()-1);
			money_prgressbar.setProgress(Integer.parseInt(moneystr));
		}

	}
@Override
public void onBackPressed() {
	quitOrBack();
}
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "星座运势";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onHomeActionClick() {
		quitOrBack();
	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub

	}
@Override
protected void onDestroy() {
	super.onDestroy();
	MyApplication.getQueues().cancelAll("costip");
}
	private void quitOrBack() {
		Intent intent = new Intent(ConstellationActivity.this,
				CenterActivity.class);
		startActivity(intent);

		finish();
	}

}
