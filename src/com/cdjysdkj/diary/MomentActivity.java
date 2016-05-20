package com.cdjysdkj.diary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.utils.DisplayMetricsEN;
import com.cdjysdkj.diary.utils.SDCardStatu;
import com.cdjysdkj.diary.utils.ToastFactory;

@SuppressLint("SimpleDateFormat")
public class MomentActivity extends BaseHomeActivity {

	private Dialog dialog_choose_img_way;
	private File tempFile;
	private String PHOTO_FILE_NAME;
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 裁剪

	private String PATH = Constant.MYAPP_PATH + "PHOTOS" + "/";
	private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";// temp
																				// file
	private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store
															// the big bitmap

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pull);
	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "精彩瞬间";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onHomeActionClick() {
		goToActivity(this, HomeActivity.class, null);
		finish();

	}

	@Override
	protected void addActions() {
		actionBar.setRightLogo(R.drawable.add);// 设置照相机背景
		actionBar.setRightAction(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 显示底部对话框
				showChooseIMG_WAYDialog();
			}
		});
	}

	/**
	 * 弹出POP样式对话框 选择拍照 选择本地图片
	 */
	protected void showChooseIMG_WAYDialog() {
		dialog_choose_img_way = new Dialog(this, R.style.MyDialogStyle_top);
		dialog_choose_img_way.setContentView(R.layout.dialog_choose_img);
		dialog_choose_img_way.setCanceledOnTouchOutside(true);
		dialog_choose_img_way.findViewById(R.id.other_view).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog_choose_img_way.cancel();
					}
				});
		// 取消
		dialog_choose_img_way.findViewById(R.id.dialog_cancel)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog_choose_img_way.cancel();
					}
				});
		// 拍照上传
		dialog_choose_img_way.findViewById(R.id.choose_by_camera)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog_choose_img_way.cancel();
						camera();

					}
				});
		// 本地上传
		dialog_choose_img_way.findViewById(R.id.choose_by_local)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog_choose_img_way.cancel();
						gallery();

					}
				});
		dialog_choose_img_way.show();
	}

	/**
	 * 从相册获取
	 */
	public void gallery() {
		// 激活系统图库，选择一张图片

		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/**
	 * 从相机获取
	 */
	public void camera() {
		PHOTO_FILE_NAME = getPhotoFileName();
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (SDCardStatu.isSDCardAvailable()) {
			File path = new File(PATH);
			if (!path.exists())
				path.mkdirs();
			File file = new File(PATH + "/" + PHOTO_FILE_NAME);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					// 得到图片的全路径
					Uri uri = data.getData();
					if (SDCardStatu.isSDCardAvailable()) {
						File path = new File(PATH);
						if (!path.exists())
							path.mkdirs();

					}
					PHOTO_FILE_NAME = getPhotoFileName();
					tempFile = new File(PATH, PHOTO_FILE_NAME);
					//crop(uri);
					try {
						Bitmap bitmap = decodeUriAsBitmap(uri);// decode bitmap
						if (tempFile.exists()) {
							tempFile.delete();
						}
						// 图像保存到文件中
						FileOutputStream foutput = null;
						try {
							foutput = new FileOutputStream(tempFile);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
									foutput);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} finally {
							if (null != foutput) {
								try {
									foutput.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					Bundle bundle = new Bundle();
					bundle.putString("OPERATION", "GALLERY");
					bundle.putString("PATH", tempFile.getAbsolutePath());
					goToActivity(this, EditImageActivity.class, bundle);
					finish();

				}
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (resultCode == RESULT_OK) {
				if (SDCardStatu.isSDCardAvailable()) {
					tempFile = new File(PATH, PHOTO_FILE_NAME);
					//crop(Uri.fromFile(tempFile));
					Bundle bundle = new Bundle();
					bundle.putString("OPERATION", "GALLERY");
					bundle.putString("PATH", tempFile.getAbsolutePath());
					goToActivity(this, EditImageActivity.class, bundle);
					finish();
				} else {
					ToastFactory.getToast(this, "未找到存储卡，无法存储照片！").show();
				}
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			if (resultCode == RESULT_OK) {
				try {
					Bitmap bitmap = decodeUriAsBitmap(imageUri);// decode bitmap
					if (tempFile.exists()) {
						tempFile.delete();
					}
					// 图像保存到文件中
					FileOutputStream foutput = null;
					try {
						foutput = new FileOutputStream(tempFile);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
								foutput);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						if (null != foutput) {
							try {
								foutput.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				Bundle bundle = new Bundle();
				bundle.putString("OPERATION", "GALLERY");
				bundle.putString("PATH", tempFile.getAbsolutePath());
				goToActivity(this, EditImageActivity.class, bundle);
				finish();
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		goToActivity(this, HomeActivity.class, null);
		finish();
	}

	private String getPhotoFileName() {

		Date date = new Date(System.currentTimeMillis());

		SimpleDateFormat dateFormat = new SimpleDateFormat(

		"yyyyMMdd_HHmmss");

		return dateFormat.format(date) + ".jpg";

	}

	/**
	 * 剪切图片
	 * 
	 * @function:
	 * @author:Jerry
	 * @date:2013-12-30
	 * @param uri
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 10);
		intent.putExtra("aspectY", 13);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", DisplayMetricsEN.width);
		intent.putExtra("outputY", DisplayMetricsEN.width);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 根据URI获取文件路径
	 * 
	 * @param uri
	 * @return
	 */
	/*
	 * protected String getAbsoluteImagePath(Uri uri) { // can post image
	 * String[] proj = { MediaStore.Images.Media.DATA }; Cursor cursor =
	 * managedQuery(uri, proj, // Which columns to return null, // WHERE clause;
	 * which rows to return (all rows) null, // WHERE clause selection arguments
	 * (none) null); // Order-by clause (ascending by name)
	 * 
	 * int column_index = cursor
	 * .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	 * cursor.moveToFirst();
	 * 
	 * return cursor.getString(column_index); }
	 */

}
