package com.cdjysdkj.diary;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.cdjysdkj.diary.adapter.CommonAdapter;
import com.cdjysdkj.diary.adapter.ViewHolder;
import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.bean.GridViewItemBean;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.utils.ToastFactory;
import com.cdjysdkj.diary.view.customGridView.DynamicGridView;
import com.cdjysdkj.diary.view.customGridView.util.GridViewBeanData;

@SuppressLint("SetJavaScriptEnabled")
public class CenterActivity extends BaseHomeActivity {
	private DynamicGridView mGridView;
	private CommonAdapter<GridViewItemBean> mAdapter;
	private ArrayList<GridViewItemBean> data;
	private String[] mainTitle;
	private String[] mainImage;
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center);
		sharedPreferences=this.getSharedPreferences(Constant.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		getView();
		init();
	}
	private void getView() {
   
		String title=sharedPreferences.getString("TITLE",Arrays.deepToString(GridViewBeanData.title));
		String image=sharedPreferences.getString("IMAGE",Arrays.deepToString(GridViewBeanData.image));
		
		mainTitle=title.substring(1,title.length()-1).split(",");
		mainImage=image.substring(1,image.length()-1).split(",");
		
		
		
		mGridView = (DynamicGridView) findViewById(R.id.custom_grid);
		
	}

	private void init() {
		data = new ArrayList<GridViewItemBean>();
		GridViewItemBean itemBean = null;
		for (int i = 0; i < mainTitle.length; i++) {
			itemBean = new GridViewItemBean(Integer.parseInt(mainImage[i].trim()),mainTitle[i]);
			data.add(itemBean);
		}
		mGridView.setAdapter(mAdapter = new CommonAdapter<GridViewItemBean>(getApplicationContext(), data,
				R.layout.item_grid,3) {

			@Override
			public void convert(ViewHolder helper, GridViewItemBean item) {
				helper.setText(R.id.item_title, item.getTitle());
				helper.setImageResource(R.id.item_img, item.getImage());

			}
		});
	

	mGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
        @Override
        public void onDragStarted(int position) {
        	ToastFactory.getToast(CenterActivity.this,"可以编辑工具箱了").show();
        }

        @Override
        public void onDragPositionsChanged(int oldPosition, int newPosition) {
        	String temp="";
        	String img="";
//        	ToastFactory.getToast(CenterActivity.this,String.format("drag item position changed from %d to %d", oldPosition, newPosition)).show();
           temp=mainTitle[newPosition];
        	mainTitle[newPosition]=mainTitle[oldPosition];
        	mainTitle[oldPosition]=temp;
        	img=mainImage[newPosition];
            mainImage[newPosition]=mainImage[oldPosition];
            mainImage[oldPosition]=img;
        	Editor editor = sharedPreferences.edit();
        	editor.putString("TITLE", Arrays.toString(mainTitle));
        	editor.putString("IMAGE", Arrays.toString(mainImage));
			editor.commit();
			init();
			mAdapter.notifyDataSetChanged();
        
        }
    });
	mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        	mGridView.startEditMode(position);
            return true;
        }
    });

	mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        	ToastFactory.getToast(CenterActivity.this, parent.getAdapter().getItem(position).toString()).show();
        	TextView textView = (TextView) view.findViewById(R.id.item_title);
        	Intent intent=new Intent();
        	String str=textView.getText().toString().trim();
        if(("号码归属地").equals(str))
        {
        	
        	intent.setClass(CenterActivity.this, MobileNoActivity.class);
        	startActivity(intent);
        	finish();
        	//ToastFactory.getToast(CenterActivity.this, "号码归属地").show();
        }else if("身份证查询".equals(str))
        {
        	intent.setClass(CenterActivity.this, IDCARDActivity.class);
        	startActivity(intent);
        	finish();
//        	ToastFactory.getToast(CenterActivity.this, "身份证查询").show();
        	
        }
        }
    });
	
	}
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "工具箱";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onHomeActionClick() {
		if (mGridView.isEditMode()) {
			mGridView.stopEditMode();
        } else {
		goToActivity(this, HomeActivity.class, null);
		finish();
        }
	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		if (mGridView.isEditMode()) {
			mGridView.stopEditMode();
        } else {
        	goToActivity(this, HomeActivity.class, null);
    		finish();
        }
		
	}

}
