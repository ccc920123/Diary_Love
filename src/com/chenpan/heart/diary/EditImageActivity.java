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
	// private static final int PHOTO_REQUEST_CUT = 3;// �ü�
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
	 * �Ƿ�༭״̬
	 */
	private boolean isEdit = false;
	/**
	 * �Ƿ�������
	 */
	private boolean isNew = false;
	/**
	 * �Ƿ��Ѿ�����
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
		// ��2������߽���
		if ("LOOK".equals(OPERATION)) {// �鿴
			isSave = true;
			actionBar.setTitle("��    ��");
			List<ImageDiaryTab> imageDiaryTabs = imageDiaryTabHelper
					.queryByimagePath(imagePath);
			if (imageDiaryTabs != null && imageDiaryTabs.size() > 0) {
				imageDiaryTab = imageDiaryTabs.get(0);
				mEditText.setText(imageDiaryTab.getDescribe());
			}
			// mEditText.setEnabled(false);
		} else {// �༭
			isEdit = true;
			actionBar.hideRightBtn(true);
			actionBar.setTitle("��    ��");

			List<ImageDiaryTab> imageDiaryTabs = imageDiaryTabHelper
					.queryByimagePath(imagePath);
			if (imageDiaryTabs != null && imageDiaryTabs.size() > 0) {// ����У�˵������ԭ�еĻ����ϱ༭
				imageDiaryTab = imageDiaryTabs.get(0);
				mEditText.setText(imageDiaryTab.getDescribe());
			} else {// �½��༭ͼƬ������
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
					actionBar.setTitle("��    ��");
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
				actionBar.setTitle("��    ��");
				mGlayout.setVisibility(View.VISIBLE);
				return false;
			}
		});

	}

	/**
	 * ����ͼƬfilter
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
							EditImageActivity.this, "ͼƬ������Ⱦ�С���");

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
							System.gc(); // ����ϵͳ��ʱ����
						}
					} finally {
						if (img != null && img.image.isRecycled()) {
							img.image.recycle();
							img.image = null;
							System.gc(); // ����ϵͳ��ʱ����
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
		return "����˲��";
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
					if (isEdit) {// ����Ǳ༭״̬��ִ�б������
						saveImage();
						isSave = true;
					} else {// ����༭״̬
						actionBar.hideRightBtn(true);
						mEditText.setEnabled(true);
						actionBar.setTitle("��    ��");
						mGlayout.setVisibility(View.VISIBLE);

					}
				}
			}
		});
	}

	/**
	 * ����
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
		} else {// ��ʾ�Ի���
			if (isNew) {
				saveImage();
				Afinish();
			} else {
				MyAlertDialog alertDialog = new MyAlertDialog(this);
				alertDialog.builder().setTitle("����").setMsg("�Ƿ񱣴��ͼƬ��Ϣ��")
						.setNegativeButton("ȡ��", new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Afinish();
							}
						}).setPositiveButton("����", new OnClickListener() {

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
					System.gc(); // ����ϵͳ��ʱ����
				}
			} finally {
				if (img != null && img.image.isRecycled()) {
					img.image.recycle();
					img.image = null;
					System.gc(); // ����ϵͳ��ʱ����
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

		// �ǵý�Bitmap������յ�
		if (mbitmap != null) {
			mbitmap.recycle();
			System.gc();
		}
		super.onDestroy();
	}
}
