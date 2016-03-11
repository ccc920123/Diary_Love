package com.chenpan.heart.diary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;

import com.chenpan.heart.diary.view.ZoomImageView;

/**
 * �鿴��ͼ��Activity���档
 * 
 * @author guolin
 */
public class ImageDetailsActivity extends Activity {

	/**
	 * �Զ����ImageView���ƣ��ɶ�ͼƬ���ж�㴥�����ź��϶�
	 */
	private ZoomImageView zoomImageView;

	/**
	 * ��չʾ��ͼƬ
	 */
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_details);
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		// ȡ��ͼƬ·������������Bitmap����Ȼ����ZoomImageView����ʾ
		String imagePath = getIntent().getStringExtra("image_path");
		bitmap = BitmapFactory.decodeFile(imagePath);
		zoomImageView.setImageBitmap(bitmap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �ǵý�Bitmap������յ�
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

}