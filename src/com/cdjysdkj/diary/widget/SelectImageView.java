package com.cdjysdkj.diary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cdjysdkj.diary.R;
/**
 * 选择图片，实现删除多张图片的控件
 * @author Administrator
 *
 */
public class SelectImageView extends RelativeLayout {
	public ImageView mImageView;
	public ImageView mSelectimg;
	public SelectImageView(Context context) {
		super(context);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.select_img_layout, this);
		initWidget();
	}

	private void initWidget() {
		mImageView=(ImageView) findViewById(R.id.img);
		mSelectimg=(ImageView) findViewById(R.id.isselect);
	}

	public void setImageBitmap(Bitmap bitmap) {
		mImageView.setImageBitmap(bitmap);
	}

}
