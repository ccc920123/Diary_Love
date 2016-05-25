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

public class MobileNoActivity extends BaseHomeActivity {

	private EditText mTelPhone;
	private Button mQuery;
	private TextView mShow;
	private LinearLayout mShowError;
	private LinearLayout mShowData;
	private TextView mCity;
	private TextView mCompany;
	private CustomProgressDialog progressDialog;
	private TextView mCard;
	String url = "http://apis.juhe.cn/mobile/get";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_no);
		getiView();
	}

	private void getiView() {
		mTelPhone = (EditText) findViewById(R.id.telphone);
		mQuery = (Button) findViewById(R.id.query);
		mShow = (TextView) findViewById(R.id.showresult);
		mShowError = (LinearLayout) findViewById(R.id.showerror);
		mShowData = (LinearLayout) findViewById(R.id.showdata);
		mCity = (TextView) findViewById(R.id.city);
		mCompany = (TextView) findViewById(R.id.company);
		mCard = (TextView) findViewById(R.id.card);
		mQuery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressDialog = CustomProgressDialog.createDialog(
						MobileNoActivity.this, "查询中……");

				progressDialog.show();
				queryTelNO(mTelPhone.getText().toString());
			}
		});
	}

	protected void queryTelNO(String tel) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", tel);
		map.put("key", Constant.KEY);
		VolleyRequest.RequestPostString(url, "postTel", map,
				new VolleyInterface() {
					@Override
					public void onMySuccess(String result) {
						progressDialog.dismiss();
						mShowError.setVisibility(View.GONE);
						mShowData.setVisibility(View.VISIBLE);
						mShowData.setLayoutAnimation(getAnimation());
						try {
							JSONObject jsonObject = new JSONObject(result);
							showData(jsonObject);
							

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							mShowError.setVisibility(View.VISIBLE);
							mShowData.setVisibility(View.GONE);
							mShow.setText("解析数据出错");
						}
					}

					@Override
					public void onMyError(VolleyError result) {
						progressDialog.dismiss();
						mShowError.setVisibility(View.VISIBLE);
						mShowData.setVisibility(View.GONE);
						mShow.setText("网络请求失败，请重试!");
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
			mCity.setText(object.getString("province") + "·"
					+ object.getString("city"));
			mCompany.setText(object.getString("company"));
			mCard.setText(object.getString("card"));
			
		} else {
			mShowError.setVisibility(View.VISIBLE);
			mShowData.setVisibility(View.GONE);
			mShow.setText(result.getString("reason"));
		}
	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "号码归属地查询";
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
		Intent intent = new Intent(MobileNoActivity.this, CenterActivity.class);
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
		MyApplication.getQueues().cancelAll("postTel");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		quitOrBack();
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

}
