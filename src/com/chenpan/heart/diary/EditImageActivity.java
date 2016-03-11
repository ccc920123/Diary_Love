package com.chenpan.heart.diary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chenpan.heart.diary.adapter.ImageFilterAdapter;
import com.chenpan.heart.diary.application.BaseHomeActivity;
import com.chenpan.heart.diary.tab.ImageDiaryTab;
import com.chenpan.heart.diary.tabhelper.ImageDiaryTabHelper;
import com.chenpan.heart.diary.utils.ButtonTools;
import com.chenpan.heart.diary.utils.DisplayMetricsEN;
import com.chenpan.heart.diary.utils.FileUtil;
import com.chenpan.heart.diary.utils.ImageLoader;
import com.chenpan.heart.diary.utils.StringUtils;
import com.chenpan.heart.diary.view.imagefilter.IImageFilter;
import com.chenpan.heart.diary.view.imagefilter.Image;
import com.chenpan.heart.diary.widget.dialog.CustomProgressDialog;
import com.chenpan.heart.diary.widget.dialog.MyAlertDialog;

@SuppressWarnings("deprecation")
@SuppressLint("SimpleDateFormat")
public class EditImageActivity extends BaseHomeActivity {
	private LinearLayout mGlayout;
	private Gallery mGallery;
	private ImageView mImageView;
	private EditText mEditText;
	private String OPERATION;
	private String imagePath = "";
	// private static final int PHOTO_REQUEST_CUT = 3;// 裁剪
	private Bitmap mbitmap;
	private ImageDiaryTabHelper imageDiaryTabHelper;
	private ImageDiaryTab imageDiaryTab;
	private CustomProgressDialog progressDialog;
	// private String PATH = Constant.MYAPP_PATH + "PHOTOS" + "/";
	private static final String IMAGE_FILE_LOCATION = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "temp.jpg";// temp
	// file
	// the big bitmap
	private long firstTime;
	/**
	 * 是否编辑状态
	 */
	private boolean isEdit = false;
	/**
	 * 是否是新增
	 */
	private boolean isNew = false;
	/**
	 * 是否已经保存
	 */
	private boolean isSave = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_image);
		getview();
		init();

		LoadImageFilter();
	}

	static {
		System.loadLibrary("image_jni");
	}

	private void init() {

		imageDiaryTabHelper = ImageDiaryTabHelper.getInstance();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		OPERATION = bundle.getString("OPERATION");
		imagePath = bundle.getString("PATH");
		if (!StringUtils.isEmpty(imagePath)) {

			mbitmap = ImageLoader.decodeSampledBitmapFromResource(imagePath,
					DisplayMetricsEN.width);
			// mbitmap=BitmapFactory.decodeFile(imagePath);
			FileUtil.copyFile(imagePath, IMAGE_FILE_LOCATION);
			mImageView.setImageBitmap(mbitmap);
		}
		// 有2种情况走进来
		if ("LOOK".equals(OPERATION)) {// 查看
			isSave = true;
			actionBar.setTitle("查    看");
			List<ImageDiaryTab> imageDiaryTabs = imageDiaryTabHelper
					.queryByimagePath(imagePath);
			if (imageDiaryTabs != null && imageDiaryTabs.size() > 0) {
				imageDiaryTab = imageDiaryTabs.get(0);
				mEditText.setText(imageDiaryTab.getDescribe());
			}
			// mEditText.setEnabled(false);
		} else {// 编辑
			isEdit = true;
			actionBar.hideRightBtn(true);
			actionBar.setTitle("编    辑");

			List<ImageDiaryTab> imageDiaryTabs = imageDiaryTabHelper
					.queryByimagePath(imagePath);
			if (imageDiaryTabs != null && imageDiaryTabs.size() > 0) {// 如果有，说明是在原有的基础上编辑
				imageDiaryTab = imageDiaryTabs.get(0);
				mEditText.setText(imageDiaryTab.getDescribe());
			} else {// 新建编辑图片，文字
				isNew = true;
				imageDiaryTab = new ImageDiaryTab();
				// imageDiaryTab.setImagePath(imagePath);
			}
			mGlayout.setVisibility(View.VISIBLE);
		}
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!ButtonTools.isFastDoubleClick()) {
					Intent intent = new Intent(EditImageActivity.this,
							ImageDetailsActivity.class);
					intent.putExtra("image_path", IMAGE_FILE_LOCATION);
					startActivity(intent);
					overridePendingTransition(0, 0);
				}
			}
		});
		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				isSave = false;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	private void getview() {
		mEditText = (EditText) findViewById(R.id.edit_btn);
		mGallery = (Gallery) findViewById(R.id.galleryFilter);
		mGlayout = (LinearLayout) findViewById(R.id.gallery_layout);
		mImageView = (ImageView) findViewById(R.id.edit_img);
		mEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				long time = System.currentTimeMillis();
				if (time - firstTime < 2000) {
					actionBar.hideRightBtn(true);
					mEditText.setEnabled(true);
					actionBar.setTitle("编    辑");
					mGlayout.setVisibility(View.VISIBLE);

				}
				firstTime = time;
			}
		});
		mEditText.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				actionBar.hideRightBtn(true);
				mEditText.setEnabled(true);
				actionBar.setTitle("编    辑");
				mGlayout.setVisibility(View.VISIBLE);
				return false;
			}
		});

	}

	/**
	 * 加载图片filter
	 * 
	 */
	private void LoadImageFilter() {
		final ImageFilterAdapter filterAdapter = new ImageFilterAdapter(this);
		mGallery.setAdapter(filterAdapter);
		mGallery.setSelection(0);
		mGallery.setAnimationDuration(3000);
		mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				isSave = false;
				IImageFilter filter = (IImageFilter) filterAdapter
						.getItem(position);
				boolean isndk = filter.getisNDK();
				Image img = null;
				if (!isndk) {
					new processImageTask(EditImageActivity.this, filter)
							.execute();
					progressDialog = CustomProgressDialog.createDialog(
							EditImageActivity.this, "图片正在渲染中……");

					progressDialog.show();
				} else {
					try {
						Bitmap bitmap = mbitmap.copy(Config.ARGB_8888, true);
						img = new Image(bitmap, isndk);

						if (filter != null) {
							img = filter.process(img);
							img.copyPixelsFromBuffer();
						}

						// return img.getImage();
					} catch (Exception e) {
						if (img != null && img.destImage.isRecycled()) {
							img.destImage.recycle();
							img.destImage = null;
							System.gc(); // 提醒系统及时回收
						}
					} finally {
						if (img != null && img.image.isRecycled()) {
							img.image.recycle();
							img.image = null;
							System.gc(); // 提醒系统及时回收
						}
					}

					mImageView.setImageBitmap(img.destImage);
					File file = new File(IMAGE_FILE_LOCATION);
					BufferedOutputStream bos = null;
					try {
						bos = new BufferedOutputStream(new FileOutputStream(
								file));
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					img.destImage
							.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					try {
						bos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		});
	}

	@Override
	protected String getActionBarTitle() {
		return "精彩瞬间";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		return true;
	}

	@Override
	protected void onHomeActionClick() {
		saveAndFinish();
	}

	@Override
	protected void addActions() {

		actionBar.setRightAction(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!ButtonTools.isFastDoubleClick()) {
					if (isEdit) {// 如果是编辑状态，执行保存操作
						saveImage();
						isSave = true;
					} else {// 进入编辑状态
						actionBar.hideRightBtn(true);
						mEditText.setEnabled(true);
						actionBar.setTitle("编    辑");
						mGlayout.setVisibility(View.VISIBLE);

					}
				}
			}
		});
	}

	/**
	 * 保存
	 */
	protected void saveImage() {
		FileUtil.copyFile(IMAGE_FILE_LOCATION, imagePath);
		if (imageDiaryTab == null) {
			imageDiaryTab = new ImageDiaryTab();
			isNew = true;
		}
		imageDiaryTab.setDescribe(mEditText.getText().toString());
		imageDiaryTab.setImagePath(imagePath);
		if (isNew) {
			imageDiaryTabHelper.insert(imageDiaryTab);
		} else {
			imageDiaryTabHelper.update(imageDiaryTab);
		}
	}

	@Override
	public void onBackPressed() {
		saveAndFinish();
	}

	private void saveAndFinish() {
		if (isSave) {
			Afinish();
		} else {// 提示对话框
			if (isNew) {
				saveImage();
				Afinish();
			} else {
				MyAlertDialog alertDialog = new MyAlertDialog(this);
				alertDialog.builder().setTitle("保存").setMsg("是否保存该图片信息？")
						.setNegativeButton("取消", new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Afinish();
							}
						}).setPositiveButton("保存", new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								saveImage();
								Afinish();
							}
						});
				alertDialog.show();
			}
		}
	}

	private void Afinish() {
		goToActivity(this, MomentActivity.class, null);
		finish();
	};

	public class processImageTask extends AsyncTask<Bitmap, Void, Bitmap> {
		private IImageFilter filter;

		// private Activity activity = null;

		public processImageTask(Activity activity, IImageFilter imageFilter) {
			this.filter = imageFilter;
			// this.activity = activity;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		public Bitmap doInBackground(Bitmap... params) {
			Image img = null;
			isSave = false;

			try {
				 Bitmap bitmap = mbitmap.copy(Config.ARGB_8888, true);
				img = new Image(bitmap, false);
				if (filter != null) {
					img = filter.process(img);
					img.copyPixelsFromBuffer();
				}
				return img.getImage();
			} catch (Exception e) {
				if (img != null && img.destImage.isRecycled()) {
					img.destImage.recycle();
					img.destImage = null;
					System.gc(); // 提醒系统及时回收
				}
			} finally {
				if (img != null && img.image.isRecycled()) {
					img.image.recycle();
					img.image = null;
					System.gc(); // 提醒系统及时回收
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				super.onPostExecute(result);
				progressDialog.dismiss();
				mImageView.setImageBitmap(result);
				File file = new File(IMAGE_FILE_LOCATION);
				BufferedOutputStream bos = null;
				try {
					bos = new BufferedOutputStream(new FileOutputStream(file));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				result.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				try {
					bos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void onDestroy() {

		// 记得将Bitmap对象回收掉
		if (mbitmap != null) {
			mbitmap.recycle();
			System.gc();
		}
		super.onDestroy();
	}
}
