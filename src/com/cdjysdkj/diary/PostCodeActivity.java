package com.cdjysdkj.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cdjysdkj.diary.application.PostBaseActivity;
import com.cdjysdkj.diary.view.postcode.adapter.ArrayWheelAdapter;
import com.cdjysdkj.diary.view.postcode.widget.OnWheelChangedListener;
import com.cdjysdkj.diary.view.postcode.widget.WheelView;
/**
 * �ʱ�
 *PostCodeActivity.java
 * @author �����
 * @Description
 * @Create Time 2016-6-12
 */
public class PostCodeActivity extends PostBaseActivity {

	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnConfirm;
	private TextView postCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_code);
		setUpViews();
		setUpListener();
		setUpData();
	}
	
	private void setUpViews() {
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		postCode=(TextView) findViewById(R.id.postcodetext);
	}
	
	private void setUpListener() {
    	// ���change�¼�
    	mViewProvince.addChangingListener(ChangCliclk);
    	// ���change�¼�
    	mViewCity.addChangingListener(ChangCliclk);
    	// ���change�¼�
    	mViewDistrict.addChangingListener(ChangCliclk);
    	// ���onclick�¼�
    	mBtnConfirm.setOnClickListener(Cliclk);
    }
	
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(PostCodeActivity.this, mProvinceDatas));
		// ���ÿɼ���Ŀ����
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

 
	OnWheelChangedListener ChangCliclk=new OnWheelChangedListener() {
		
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (wheel == mViewProvince) {
				updateCities();
			} else if (wheel == mViewCity) {
				updateAreas();
			} else if (wheel == mViewDistrict) {
				mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
				mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			}
			
		}
	};
	


	/**
	 * ���ݵ�ǰ���У�������WheelView����Ϣ
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * ���ݵ�ǰ��ʡ��������WheelView����Ϣ
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	private View.OnClickListener Cliclk=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_confirm:
				showSelectedResult();
				break;
			default:
				break;
			}
		}
	};
	
	

	private void showSelectedResult() {
//		Toast.makeText(PostCodeActivity.this, "��ǰѡ��:"+mCurrentProviceName+","+mCurrentCityName+","
//				+mCurrentDistrictName+","+mCurrentZipCode, Toast.LENGTH_SHORT).show();
	
	postCode.setText(mCurrentZipCode);
	}
	

}
