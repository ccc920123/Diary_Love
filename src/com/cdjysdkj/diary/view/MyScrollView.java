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
 * 自定义的ScrollView，在其中动态地对图片进行添加。
 * 
 * @author guolin
 */
public class MyScrollView extends ScrollView implements OnTouchListener {
	private AlertDialog dialog;
	/**
	 * 每页要加载的图片数量
	 */
	public static final int PAGE_SIZE = 15;

	/**
	 * 记录当前已加载到第几页
	 */
	private int page;

	/**
	 * 每一列的宽度
	 */
	private int columnWidth;

	/**
	 * 当前第一列的高度
	 */
	private int firstColumnHeight;

	/**
	 * 当前第二列的高度
	 */
	private int secondColumnHeight;

	/**
	 * 当前第三列的高度
	 */
	private int thirdColumnHeight;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;

	/**
	 * 对图片进行管理的工具类
	 */
	private ImageLoader imageLoader;

	/**
	 * 第一列的布局
	 */
	private LinearLayout firstColumn;

	/**
	 * 第二列的布局
	 */
	private LinearLayout secondColumn;

	/**
	 * 第三列的布局
	 */
	private LinearLayout thirdColumn;

	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private static Set<LoadImageTask> taskCollection;

	/**
	 * MyScrollView下的直接子布局。
	 */
	private static View scrollLayout;

	/**
	 * MyScrollView布局的高度。
	 */
	private static int scrollViewHeight;

	/**
	 * 记录上垂直方向的滚动距离。
	 */
	private static int lastScrollY = -1;

	/**
	 * 记录所有界面上的图片，用以可以随时控制对图片的释放。
	 */
	private List<ImageView> imageViewList = new ArrayList<ImageView>();

	public List<String> allImagePath = null;

	public Button lookBtn;
	public Button editBtn;
	public Button delBtn;
	/**
	 * 长按得到的URL
	 */
	private String mUrl;
	
	private Activity context;

	/**
	 * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
	 */
	private static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			MyScrollView myScrollView = (MyScrollView) msg.obj;
			int scrollY = myScrollView.getScrollY();
			// 如果当前的滚动位置和上次相同，表示已停止滚动
			if (scrollY == lastScrollY) {
				// 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
				if (scrollViewHeight + scrollY >= scrollLayout.getHeight()
						&& taskCollection.isEmpty()) {
					myScrollView.loadMoreImages();
				}
				myScrollView.checkVisibility();
			} else {
				lastScrollY = scrollY;
				Message message = new Message();
				message.obj = myScrollView;
				// 5毫秒后再次对滚动位置进行判断
				handler.sendMessageDelayed(message, 5);
			}
		};

	};

	/**
	 * MyScrollView的构造函数。
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
	 * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
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
	 * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
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
	 * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
	 */
	public void loadMoreImages() {
		if (hasSDCard()) {
			int startIndex = page * PAGE_SIZE;
			int endIndex = page * PAGE_SIZE + PAGE_SIZE;
			if (allImagePath != null) {
				if (startIndex < allImagePath.size()) {
					// Toast.makeText(getContext(), "正在加载...",
					// Toast.LENGTH_SHORT)
					// .show();
					ToastFactory.getToast(getContext(), "正在加载...").show();
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
					 * Toast.makeText(getContext(), "已没有更多图片",
					 * Toast.LENGTH_SHORT) .show();
					 */
					ToastFactory.getToast(getContext(), "已没有更多图片").show();
				}
			} else {
				ToastFactory.getToast(getContext(), "还没有图片").show();
			}
		} else {
			// Toast.makeText(getContext(), "未发现SD卡",
			// Toast.LENGTH_SHORT).show();
			ToastFactory.getToast(getContext(), "未发现SD卡").show();
		}
	}

	/**
	 * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
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
	 * 判断手机是否有SD卡。
	 * 
	 * @return 有SD卡返回true，没有返回false。
	 */
	private boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author guolin
	 */
	class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		private String mImageUrl;

		/**
		 * 可重复使用的ImageView
		 */
		private ImageView mImageView;

		public LoadImageTask() {
		}

		/**
		 * 将可重复使用的ImageView传入
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
		 * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 加载到内存的图片。
		 */
		private Bitmap loadImage(String imageUrl) {
			File imageFile = new File(getImagePath(imageUrl));
			if (!imageFile.exists()) {
				// downloadImage(imageUrl);文件不存在，做什么呢

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
		 * 向ImageView中添加一张图片
		 * 
		 * @param bitmap
		 *            待添加的图片
		 * @param imageWidth
		 *            图片的宽度
		 * @param imageHeight
		 *            图片的高度
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
		 * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
		 * 
		 * @param imageView
		 * @param imageHeight
		 * @return 应该添加图片的一列
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
		 * 将图片下载到SD卡缓存起来。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
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
	 * 获取图片的本地存储路径。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return 图片的本地存储路径。
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
				R.layout.deal_photo_dialog, null);// 自定义布局
		lookBtn = (Button) view.findViewById(R.id.button1);
		lookBtn.setOnClickListener(clicklistener);
		editBtn = (Button) view.findViewById(R.id.button2);
		editBtn.setOnClickListener(clicklistener);
		delBtn = (Button) view.findViewById(R.id.button3);
		delBtn.setOnClickListener(clicklistener);
		dialog.setView(view, 0, 0, 0, 0);// 把自定义的布局设置到dialog中，注意，布局设置一定要在show之前。从第二个参数分别填充内容与边框之间左、上、右、下、的像素
		dialog.show();// 一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了
		@SuppressWarnings("deprecation")
		int width = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getWidth();// 得到当前显示设备的宽度，单位是像素
		android.view.WindowManager.LayoutParams params = dialog.getWindow()
				.getAttributes();// 得到这个dialog界面的参数对象
		params.width = width - (width / 6);// 设置dialog的界面宽度
		params.height = LayoutParams.WRAP_CONTENT;// 设置dialog高度为包裹内容
		params.gravity = Gravity.CENTER;// 设置dialog的重心
		// dialog.getWindow().setLayout(width-(width/6),
		// LayoutParams.WRAP_CONTENT);//用这个方法设置dialog大小也可以，但是这个方法不能设置重心之类的参数，推荐用Attributes设置
		dialog.getWindow().setAttributes(params);// 最后把这个参数对象设置进去，即与dialog绑定

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
				if(imageDiaryTab!=null){//删除数据库的内容
					imageDiaryTabHelper.delete(imageDiaryTab);
				}
				FileUtil.deleteFileByPath(mUrl);//删除文件
				Intent intent2 = new Intent(getContext(),
						MomentActivity.class);
				context.startActivity(intent2);
				context.finish();
				context.overridePendingTransition(0, 0);
				/*imageViewList.remove(view);
				MyScrollView.this.postInvalidate();*/
				break;
				
			}
			imageLoader.deleteBitmapToMemoryCache(mUrl);//删除缓存
			dialog.dismiss();
		}
	};

	

}