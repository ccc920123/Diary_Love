package com.cdjysdkj.diary.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.cdjysdkj.diary.EditImageActivity;
import com.cdjysdkj.diary.MomentActivity;
import com.cdjysdkj.diary.R;
import com.cdjysdkj.diary.bean.Images;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.tab.ImageDiaryTab;
import com.cdjysdkj.diary.tabhelper.ImageDiaryTabHelper;
import com.cdjysdkj.diary.utils.FileUtil;
import com.cdjysdkj.diary.utils.ImageLoader;
import com.cdjysdkj.diary.utils.ToastFactory;

/**
 * �Զ����ScrollView�������ж�̬�ض�ͼƬ������ӡ�
 * 
 * @author guolin
 */
public class MyScrollView extends ScrollView implements OnTouchListener {
	private AlertDialog dialog;
	/**
	 * ÿҳҪ���ص�ͼƬ����
	 */
	public static final int PAGE_SIZE = 15;

	/**
	 * ��¼��ǰ�Ѽ��ص��ڼ�ҳ
	 */
	private int page;

	/**
	 * ÿһ�еĿ��
	 */
	private int columnWidth;

	/**
	 * ��ǰ��һ�еĸ߶�
	 */
	private int firstColumnHeight;

	/**
	 * ��ǰ�ڶ��еĸ߶�
	 */
	private int secondColumnHeight;

	/**
	 * ��ǰ�����еĸ߶�
	 */
	private int thirdColumnHeight;

	/**
	 * �Ƿ��Ѽ��ع�һ��layout������onLayout�еĳ�ʼ��ֻ�����һ��
	 */
	private boolean loadOnce;

	/**
	 * ��ͼƬ���й���Ĺ�����
	 */
	private ImageLoader imageLoader;

	/**
	 * ��һ�еĲ���
	 */
	private LinearLayout firstColumn;

	/**
	 * �ڶ��еĲ���
	 */
	private LinearLayout secondColumn;

	/**
	 * �����еĲ���
	 */
	private LinearLayout thirdColumn;

	/**
	 * ��¼�����������ػ�ȴ����ص�����
	 */
	private static Set<LoadImageTask> taskCollection;

	/**
	 * MyScrollView�µ�ֱ���Ӳ��֡�
	 */
	private static View scrollLayout;

	/**
	 * MyScrollView���ֵĸ߶ȡ�
	 */
	private static int scrollViewHeight;

	/**
	 * ��¼�ϴ�ֱ����Ĺ������롣
	 */
	private static int lastScrollY = -1;

	/**
	 * ��¼���н����ϵ�ͼƬ�����Կ�����ʱ���ƶ�ͼƬ���ͷš�
	 */
	private List<ImageView> imageViewList = new ArrayList<ImageView>();

	public List<String> allImagePath = null;

	public Button lookBtn;
	public Button editBtn;
	public Button delBtn;
	/**
	 * �����õ���URL
	 */
	private String mUrl;
	
	private Activity context;

	/**
	 * ��Handler�н���ͼƬ�ɼ��Լ����жϣ��Լ����ظ���ͼƬ�Ĳ�����
	 */
	private static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			MyScrollView myScrollView = (MyScrollView) msg.obj;
			int scrollY = myScrollView.getScrollY();
			// �����ǰ�Ĺ���λ�ú��ϴ���ͬ����ʾ��ֹͣ����
			if (scrollY == lastScrollY) {
				// ����������ײ������ҵ�ǰû���������ص�����ʱ����ʼ������һҳ��ͼƬ
				if (scrollViewHeight + scrollY >= scrollLayout.getHeight()
						&& taskCollection.isEmpty()) {
					myScrollView.loadMoreImages();
				}
				myScrollView.checkVisibility();
			} else {
				lastScrollY = scrollY;
				Message message = new Message();
				message.obj = myScrollView;
				// 5������ٴζԹ���λ�ý����ж�
				handler.sendMessageDelayed(message, 5);
			}
		};

	};

	/**
	 * MyScrollView�Ĺ��캯����
	 * 
	 * @param context
	 * @param attrs
	 */
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 this.context=(Activity) context;
		imageLoader = ImageLoader.getInstance();
		taskCollection = new HashSet<LoadImageTask>();
		setOnTouchListener(this);
		allImagePath = new Images().getPictures(Constant.MYAPP_PATH + "PHOTOS"
				+ "/");

	}

	/**
	 * ����һЩ�ؼ��Եĳ�ʼ����������ȡMyScrollView�ĸ߶ȣ��Լ��õ���һ�еĿ��ֵ���������￪ʼ���ص�һҳ��ͼƬ��
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			scrollViewHeight = getHeight();
			scrollLayout = getChildAt(0);
			firstColumn = (LinearLayout) findViewById(R.id.first_column);
			secondColumn = (LinearLayout) findViewById(R.id.second_column);
			thirdColumn = (LinearLayout) findViewById(R.id.third_column);
			columnWidth = firstColumn.getWidth();
			loadOnce = true;
			loadMoreImages();
		}
	}

	/**
	 * �����û��Ĵ����¼�������û���ָ�뿪��Ļ��ʼ���й�����⡣
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Message message = new Message();
			message.obj = this;
			handler.sendMessageDelayed(message, 5);
		}
		return false;
	}

	/**
	 * ��ʼ������һҳ��ͼƬ��ÿ��ͼƬ���Ὺ��һ���첽�߳�ȥ���ء�
	 */
	public void loadMoreImages() {
		if (hasSDCard()) {
			int startIndex = page * PAGE_SIZE;
			int endIndex = page * PAGE_SIZE + PAGE_SIZE;
			if (allImagePath != null) {
				if (startIndex < allImagePath.size()) {
					// Toast.makeText(getContext(), "���ڼ���...",
					// Toast.LENGTH_SHORT)
					// .show();
					ToastFactory.getToast(getContext(), "���ڼ���...").show();
					if (endIndex > allImagePath.size()) {
						endIndex = allImagePath.size();
					}
					for (int i = startIndex; i < endIndex; i++) {
						LoadImageTask task = new LoadImageTask();
						taskCollection.add(task);
						task.execute(allImagePath.get(i));
					}
					page++;
				} else {
					/*
					 * Toast.makeText(getContext(), "��û�и���ͼƬ",
					 * Toast.LENGTH_SHORT) .show();
					 */
					ToastFactory.getToast(getContext(), "��û�и���ͼƬ").show();
				}
			} else {
				ToastFactory.getToast(getContext(), "��û��ͼƬ").show();
			}
		} else {
			// Toast.makeText(getContext(), "δ����SD��",
			// Toast.LENGTH_SHORT).show();
			ToastFactory.getToast(getContext(), "δ����SD��").show();
		}
	}

	/**
	 * ����imageViewList�е�ÿ��ͼƬ����ͼƬ�Ŀɼ��Խ��м�飬���ͼƬ�Ѿ��뿪��Ļ�ɼ���Χ����ͼƬ�滻��һ�ſ�ͼ��
	 */
	public void checkVisibility() {
		for (int i = 0; i < imageViewList.size(); i++) {
			ImageView imageView = imageViewList.get(i);
			int borderTop = (Integer) imageView.getTag(R.string.border_top);
			int borderBottom = (Integer) imageView
					.getTag(R.string.border_bottom);
			if (borderBottom > getScrollY()
					&& borderTop < getScrollY() + scrollViewHeight) {
				String imageUrl = (String) imageView.getTag(R.string.image_url);
				Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				} else {
					LoadImageTask task = new LoadImageTask(imageView);
					task.execute(imageUrl);
				}
			} else {
				imageView.setImageResource(R.drawable.empty_photo);
			}
		}
	}

	/**
	 * �ж��ֻ��Ƿ���SD����
	 * 
	 * @return ��SD������true��û�з���false��
	 */
	private boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * �첽����ͼƬ������
	 * 
	 * @author guolin
	 */
	class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * ͼƬ��URL��ַ
		 */
		private String mImageUrl;

		/**
		 * ���ظ�ʹ�õ�ImageView
		 */
		private ImageView mImageView;

		public LoadImageTask() {
		}

		/**
		 * �����ظ�ʹ�õ�ImageView����
		 * 
		 * @param imageView
		 */
		public LoadImageTask(ImageView imageView) {
			mImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			mImageUrl = params[0];
			Bitmap imageBitmap = imageLoader
					.getBitmapFromMemoryCache(mImageUrl);
			if (imageBitmap == null) {
				imageBitmap = loadImage(mImageUrl);
			}
			return imageBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				double ratio = bitmap.getWidth() / (columnWidth * 1.0);
				int scaledHeight = (int) (bitmap.getHeight() / ratio);
				addImage(bitmap, columnWidth, scaledHeight);
			}
			taskCollection.remove(this);
		}

		/**
		 * ���ݴ����URL����ͼƬ���м��ء��������ͼƬ�Ѿ�������SD���У���ֱ�Ӵ�SD�����ȡ������ʹ����������ء�
		 * 
		 * @param imageUrl
		 *            ͼƬ��URL��ַ
		 * @return ���ص��ڴ��ͼƬ��
		 */
		private Bitmap loadImage(String imageUrl) {
			File imageFile = new File(getImagePath(imageUrl));
			if (!imageFile.exists()) {
				// downloadImage(imageUrl);�ļ������ڣ���ʲô��

			}
			if (imageUrl != null) {
				Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(
						imageFile.getPath(), columnWidth);
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
					return bitmap;
				}
			}
			return null;
		}

		/**
		 * ��ImageView�����һ��ͼƬ
		 * 
		 * @param bitmap
		 *            ����ӵ�ͼƬ
		 * @param imageWidth
		 *            ͼƬ�Ŀ��
		 * @param imageHeight
		 *            ͼƬ�ĸ߶�
		 */
		private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					imageWidth, imageHeight);
			if (mImageView != null) {
				mImageView.setImageBitmap(bitmap);
			} else {
				ImageView imageView = new ImageView(getContext());
				imageView.setLayoutParams(params);
				imageView.setImageBitmap(bitmap);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setPadding(5, 5, 5, 5);
				imageView.setTag(R.string.image_url, mImageUrl);
				Animation animation = AnimationUtils.loadAnimation(getContext(),
						R.anim.image_in);

				Interpolator interpolator = new OvershootInterpolator(0.8f);
				animation.setInterpolator(interpolator);
				imageView.startAnimation(animation);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent = new Intent(getContext(),
								EditImageActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("OPERATION", "LOOK");
						bundle.putString("PATH", mImageUrl);
						intent.putExtras(bundle);
						getContext().startActivity(intent);
						imageLoader.deleteBitmapToMemoryCache(mImageUrl);
						((Activity)getContext()).finish();
					}
				});
				imageView.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View arg0) {
						 mUrl = mImageUrl;
						showDialog();
						return false;
					}
				});
				findColumnToAdd(imageView, imageHeight).addView(imageView);
				imageViewList.add(imageView);
			}
		}

		/**
		 * �ҵ���ʱӦ�����ͼƬ��һ�С�ԭ����Ƕ����еĸ߶Ƚ����жϣ���ǰ�߶���С��һ�о���Ӧ����ӵ�һ�С�
		 * 
		 * @param imageView
		 * @param imageHeight
		 * @return Ӧ�����ͼƬ��һ��
		 */
		private LinearLayout findColumnToAdd(ImageView imageView,
				int imageHeight) {
			if (firstColumnHeight <= secondColumnHeight) {
				if (firstColumnHeight <= thirdColumnHeight) {
					imageView.setTag(R.string.border_top, firstColumnHeight);
					firstColumnHeight += imageHeight;
					imageView.setTag(R.string.border_bottom, firstColumnHeight);
					return firstColumn;
				}
				imageView.setTag(R.string.border_top, thirdColumnHeight);
				thirdColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, thirdColumnHeight);
				return thirdColumn;
			} else {
				if (secondColumnHeight <= thirdColumnHeight) {
					imageView.setTag(R.string.border_top, secondColumnHeight);
					secondColumnHeight += imageHeight;
					imageView
							.setTag(R.string.border_bottom, secondColumnHeight);
					return secondColumn;
				}
				imageView.setTag(R.string.border_top, thirdColumnHeight);
				thirdColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, thirdColumnHeight);
				return thirdColumn;
			}
		}

		/**
		 * ��ͼƬ���ص�SD������������
		 * 
		 * @param imageUrl
		 *            ͼƬ��URL��ַ��
		 */
		/*
		 * private void downloadImage(String imageUrl) { if
		 * (Environment.getExternalStorageState
		 * ().equals(Environment.MEDIA_MOUNTED)) { Log.d("TAG",
		 * "monted sdcard"); } else { Log.d("TAG", "has no sdcard"); }
		 * HttpURLConnection con = null; FileOutputStream fos = null;
		 * BufferedOutputStream bos = null; BufferedInputStream bis = null; File
		 * imageFile = null; try { URL url = new URL(imageUrl); con =
		 * (HttpURLConnection) url.openConnection(); con.setConnectTimeout(5 *
		 * 1000); con.setReadTimeout(15 * 1000); con.setDoInput(true);
		 * con.setDoOutput(true); bis = new
		 * BufferedInputStream(con.getInputStream()); imageFile = new
		 * File(getImagePath(imageUrl)); fos = new FileOutputStream(imageFile);
		 * bos = new BufferedOutputStream(fos); byte[] b = new byte[1024]; int
		 * length; while ((length = bis.read(b)) != -1) { bos.write(b, 0,
		 * length); bos.flush(); } } catch (Exception e) { e.printStackTrace();
		 * } finally { try { if (bis != null) { bis.close(); } if (bos != null)
		 * { bos.close(); } if (con != null) { con.disconnect(); } } catch
		 * (IOException e) { e.printStackTrace(); } } if (imageFile != null) {
		 * Bitmap bitmap =
		 * ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
		 * columnWidth); if (bitmap != null) {
		 * imageLoader.addBitmapToMemoryCache(imageUrl, bitmap); } } }
		 */

		
	}
	/**
	 * ��ȡͼƬ�ı��ش洢·����
	 * 
	 * @param imageUrl
	 *            ͼƬ��URL��ַ��
	 * @return ͼƬ�ı��ش洢·����
	 */
	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Constant.MYAPP_PATH + "PHOTOS" + "/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}
	private void showDialog() {
		dialog = new AlertDialog.Builder(getContext()).create();
		View view = ((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.deal_photo_dialog, null);// �Զ��岼��
		lookBtn = (Button) view.findViewById(R.id.button1);
		lookBtn.setOnClickListener(clicklistener);
		editBtn = (Button) view.findViewById(R.id.button2);
		editBtn.setOnClickListener(clicklistener);
		delBtn = (Button) view.findViewById(R.id.button3);
		delBtn.setOnClickListener(clicklistener);
		dialog.setView(view, 0, 0, 0, 0);// ���Զ���Ĳ������õ�dialog�У�ע�⣬��������һ��Ҫ��show֮ǰ���ӵڶ��������ֱ����������߿�֮�����ϡ��ҡ��¡�������
		dialog.show();// һ��Ҫ��show����������dialog�Ĳ�������Ȼ�Ͳ���ı�dialog�Ĵ�С��
		@SuppressWarnings("deprecation")
		int width = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getWidth();// �õ���ǰ��ʾ�豸�Ŀ�ȣ���λ������
		android.view.WindowManager.LayoutParams params = dialog.getWindow()
				.getAttributes();// �õ����dialog����Ĳ�������
		params.width = width - (width / 6);// ����dialog�Ľ�����
		params.height = LayoutParams.WRAP_CONTENT;// ����dialog�߶�Ϊ��������
		params.gravity = Gravity.CENTER;// ����dialog������
		// dialog.getWindow().setLayout(width-(width/6),
		// LayoutParams.WRAP_CONTENT);//�������������dialog��СҲ���ԣ������������������������֮��Ĳ������Ƽ���Attributes����
		dialog.getWindow().setAttributes(params);// ������������������ý�ȥ������dialog��

	}

	private OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(getContext(),
					EditImageActivity.class);
			Bundle bundle=new Bundle();
			switch (view.getId()) {
			case R.id.button1:
				bundle.putString("OPERATION", "LOOK");
				bundle.putString("PATH", getImagePath(mUrl));
				intent.putExtras(bundle);
				getContext().startActivity(intent);
				((Activity)getContext()).finish();
				break;
			case R.id.button2:
				bundle.putString("OPERATION", "EDIT");
				bundle.putString("PATH", getImagePath(mUrl));
				intent.putExtras(bundle);
				getContext().startActivity(intent);
				((Activity)getContext()).finish();
				break;
			case R.id.button3:
				ImageDiaryTab imageDiaryTab=null ;
				ImageDiaryTabHelper	imageDiaryTabHelper = ImageDiaryTabHelper.getInstance();
				List<ImageDiaryTab> imageDiaryTabs = imageDiaryTabHelper
						.queryByimagePath(mUrl);
				if (imageDiaryTabs != null && imageDiaryTabs.size() > 0) {
					 imageDiaryTab = imageDiaryTabs.get(0);
				}
				if(imageDiaryTab!=null){//ɾ�����ݿ������
					imageDiaryTabHelper.delete(imageDiaryTab);
				}
				FileUtil.deleteFileByPath(mUrl);//ɾ���ļ�
				Intent intent2 = new Intent(getContext(),
						MomentActivity.class);
				context.startActivity(intent2);
				context.finish();
				context.overridePendingTransition(0, 0);
				/*imageViewList.remove(view);
				MyScrollView.this.postInvalidate();*/
				break;
				
			}
			imageLoader.deleteBitmapToMemoryCache(mUrl);//ɾ������
			dialog.dismiss();
		}
	};

	

}