package com.cdjysdkj.diary;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.application.MyApplication;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.network.VolleyInterface;
import com.cdjysdkj.diary.network.VolleyRequest;
import com.cdjysdkj.diary.widget.dialog.CustomProgressDialog;
/**
 * 
 *IDCARDActivity.java
 * @author 陈渝金
 * @Description area	string	地区
 	　　sex	string	性别
 	　　birthday	string	出生日期
 * @Create Time 2016-5-25
 */
public class IDCARDActivity extends BaseHomeActivity {

	private CustomProgressDialog progressDialog;
	private EditText idCardPhone;
	private Button idCardQuery;
	private LinearLayout idCardShowError;
	private TextView showResult;
	private LinearLayout idCardShowdata;
	private TextView adresscCity;
	private TextView mSex;
	private TextView mTime;
	String url = "http://apis.juhe.cn/idcard/index";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idcard);
		init();
		
	}
	private void init() {
		idCardPhone=(EditText) findViewById(R.id.idcardphone);
		idCardQuery=(Button) findViewById(R.id.idcardquery);
		idCardShowError=(LinearLayout) findViewById(R.id.idcardshowerror);
		showResult=(TextView) findViewById(R.id.showresult);
		idCardShowdata=(LinearLayout) findViewById(R.id.idcardshowdatas);
		adresscCity=(TextView) findViewById(R.id.adresscity);
		mSex=(TextView) findViewById(R.id.sex);
		mTime=(TextView) findViewById(R.id.time);
		idCardQuery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progressDialog = CustomProgressDialog.createDialog(
						IDCARDActivity.this, "查询中……");

				progressDialog.show();
				queryTelNO(idCardPhone.getText().toString());
				
			}
		});
		
	}

	protected void queryTelNO(String tel) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("cardno", tel);
//		map.put("key", Constant.IDCARDKEY);
		String urls=url+"?key="+Constant.IDCARDKEY+"&cardno="+tel;
		VolleyRequest.RequestGetString(urls, "postIdCard",
				new VolleyInterface() {
					@Override
					public void onMySuccess(String result) {
						progressDialog.dismiss();
						idCardShowError.setVisibility(View.GONE);
						idCardShowdata.setVisibility(View.VISIBLE);
						idCardShowdata.setLayoutAnimation(getAnimation());
						try {
							JSONObject jsonObject = new JSONObject(result);
							showData(jsonObject);
							

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							idCardShowError.setVisibility(View.VISIBLE);
							idCardShowdata.setVisibility(View.GONE);
							showResult.setText("解析数据出错");
						}
					}

					@Override
					public void onMyError(VolleyError result) {
						progressDialog.dismiss();
						idCardShowError.setVisibility(View.VISIBLE);
						idCardShowdata.setVisibility(View.GONE);
						showResult.setText("网络请求失败，请重试!");
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
			adresscCity.setText(object.getString("area"));
			mSex.setText(object.getString("sex"));
			mTime.setText(object.getString("birthday"));
			
		} else {
			idCardShowError.setVisibility(View.VISIBLE);
			idCardShowdata.setVisibility(View.GONE);
			showResult.setText(result.getString("reason"));
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
		return "身份证查询";
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
		Intent intent = new Intent(IDCARDActivity.this, CenterActivity.class);
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
		MyApplication.getQueues().cancelAll("postIdCard");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		quitOrBack();
	}
	
	
}
