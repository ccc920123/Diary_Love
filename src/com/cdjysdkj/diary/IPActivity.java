package com.cdjysdkj.diary;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.application.MyApplication;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.network.VolleyInterface;
import com.cdjysdkj.diary.network.VolleyRequest;
import com.cdjysdkj.diary.widget.dialog.CustomProgressDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IPActivity extends BaseHomeActivity {
	private CustomProgressDialog progressDialog;
	private EditText ipAdress;
	private Button ipQuery;
	private LinearLayout ipShowError;
	private TextView ipShowResult;
	private LinearLayout ipShowdata;
	private TextView ipadresscCity;
	private TextView ipLocation;
	String url = "http://apis.juhe.cn/ip/ip2addr";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip);
	init();
	
}
private void init() {
	ipAdress=(EditText) findViewById(R.id.ipadress);
	ipQuery=(Button) findViewById(R.id.ipquery);
	ipShowError=(LinearLayout) findViewById(R.id.ipshowerror);
	ipShowResult=(TextView) findViewById(R.id.ipshowresult);
	ipShowdata=(LinearLayout) findViewById(R.id.ipshowdatas);
	ipadresscCity=(TextView) findViewById(R.id.ipadresscity);
	ipLocation=(TextView) findViewById(R.id.iplocation);
	ipQuery.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			progressDialog = CustomProgressDialog.createDialog(
					IPActivity.this, "查询中……");

			progressDialog.show();
			queryTelNO(ipAdress.getText().toString());
			
		}
	});
	
}

protected void queryTelNO(String IP) {
//	Map<String, String> map = new HashMap<String, String>();
//	map.put("cardno", tel);
//	map.put("key", Constant.IDCARDKEY);
	String urls=url+"?ip="+IP+"&key="+Constant.IPKEY;
	VolleyRequest.RequestGetString(urls, "postip",
			new VolleyInterface() {
				@Override
				public void onMySuccess(String result) {
					progressDialog.dismiss();
					ipShowError.setVisibility(View.GONE);
					ipShowdata.setVisibility(View.VISIBLE);
					ipShowdata.setLayoutAnimation(getAnimation());
					try {
						JSONObject jsonObject = new JSONObject(result);
						showData(jsonObject);
						

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ipShowError.setVisibility(View.VISIBLE);
						ipShowdata.setVisibility(View.GONE);
						ipShowResult.setText("解析数据出错");
					}
				}

				@Override
				public void onMyError(VolleyError result) {
					progressDialog.dismiss();
					ipShowError.setVisibility(View.VISIBLE);
					ipShowdata.setVisibility(View.GONE);
					ipShowResult.setText("网络请求失败，请重试!");
				}

				@Override
				public void onMySuccess(JSONObject result) {
					// TODO Auto-generated method stub

				}
			});

}
protected void showData(JSONObject result) throws JSONException {
	int code = result.getInt("resultcode");
	if (code == 200) {
		JSONObject object=new JSONObject(result.getString("result"));
		ipadresscCity.setText(object.getString("area"));
		ipLocation.setText(object.getString("location"));
		
	} else {
		ipShowError.setVisibility(View.VISIBLE);
		ipShowdata.setVisibility(View.GONE);
		ipShowResult.setText(result.getString("reason"));
	}
}
public LayoutAnimationController getAnimation() {
	Animation animation = AnimationUtils.loadAnimation(this,
			R.anim.anim_item);

	// 得到一个LayoutAnimationController对象；

	LayoutAnimationController lac = new LayoutAnimationController(animation);
	lac.setOrder(LayoutAnimationController.ORDER_NORMAL);

	// 设置控件显示间隔时间；

	lac.setDelay(0.5f);
	return lac;
}

@Override
protected String getActionBarTitle() {
	// TODO Auto-generated method stub
	return "IP查询";
}

@Override
protected boolean isHomeAsUpEnabled() {
	return false;
}

@Override
protected void onHomeActionClick() {
	quitOrBack();

}

private void quitOrBack() {
	Intent intent = new Intent(IPActivity.this, CenterActivity.class);
	startActivity(intent);

	finish();
}

@Override
protected void addActions() {

}

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	MyApplication.getQueues().cancelAll("postip");
}

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	quitOrBack();
}




}
