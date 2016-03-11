package com.chenpan.heart.diary.view.switchbutton;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.CheckBox;

import com.chenpan.heart.diary.R;

/**
 * 开关按钮组件。大小完全由图片决定；因为按钮图片移动过程动画的实现，实际上不在不断改变图片的绘制起点实现的
 * canvas.drawBitmap(mBottom, mRealPos, mExtendOffsetY, mPaint)mRealPos在不断改变；
 * 按钮图片是全部画完了的，但是画布宽度RectF是图片按钮的1/2，所以显示视图窗口显示图片的1/2。
 * 暂时无法实现将图片canvas.drawBitmap(mBottom, null, RectF, mPaint)画到指定大小的矩阵中RectF；
 * 可以考虑改变得到bitmap的尺寸来自适应
 * 
 * @author Administrator
 * 
 */

public class SwitchButton extends CheckBox {
	private Paint mPaint;

	private ViewParent mParent;
	/** 开关样式图片 **/
	private Bitmap mBottom;
	/** 当前开关按钮显示图片 **/
	private Bitmap mCurBtnPic;
	/** 开关按钮按下图片 **/
	private Bitmap mBtnPressed;
	/** 开关按钮正常图片 **/
	private Bitmap mBtnNormal;
	/** 开关边框图片 **/
	private Bitmap mFrame;
	/** 开关底图 **/
	private Bitmap mMask;
	/** 开关画在这个矩阵里面控制开关大小 **/
	private RectF mSaveLayerRectF;
	/** 图片绘制相交模式 **/
	private PorterDuffXfermode mXfermode;

	private float mFirstDownY; // 首次按下的Y

	private float mFirstDownX; // 首次按下的X
	/** 开关按钮绘制实际位置 **/
	private float mRealPos; // 图片的绘制位置
	/** 开关按钮位置 **/
	private float mBtnPos; // 按钮的位置

	private float mBtnOnPos; // 开关打开的位置

	private float mBtnOffPos; // 开关关闭的位置
	/** 开关底图宽度 **/
	private float mMaskWidth;
	/** 开关底图高度 **/
	private float mMaskHeight;
	/** 开关按钮的宽度=开关样式图片宽度 **/
	private float mBtnWidth;

	private float mBtnInitPos;
	/** 点击事件触发所需最小时间 **/
	private int mClickTimeout;
	/** 滑动事件触发所需最小距离 **/
	private int mTouchSlop;
	/** 画布的透明度 **/
	private final int MAX_ALPHA = 255;

	private int mAlpha = MAX_ALPHA;
	/** 点击状态 **/
	private boolean mChecked = true;
	private boolean mBroadcasting;
	private boolean mTurningOn;
	/** 继承Runnable的类；开启一个线程执行一次点击事件 **/
	private PerformClick mPerformClick;

	private OnCheckedChangeListener mOnCheckedChangeListener;

	private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

	private boolean mAnimating;
	/** 动画速率 **/
	private final float VELOCITY = 350;
	/** 当前动画速率 **/
	private float mVelocity;
	/** y轴方向上的偏移量 **/
	private final float EXTENDED_OFFSET_Y = 0;// 偏移15扩大点击范围，便于点击
	/** 当前y轴方向上的偏移量 **/
	private float mExtendOffsetY; // Y轴方向扩大的区域,增大点击区域

	private float mAnimationPosition;

	private float mAnimatedVelocity;

	private int backImage;

	public SwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.checkboxStyle);
	}

	public SwitchButton(Context context) {
		this(context, null);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.switch_button);
		backImage = a.getResourceId(R.styleable.switch_button_back_image, R.drawable.bottom);
		initView(context);
		a.recycle();
	}

	private void initView(Context context) {
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		Resources resources = context.getResources();

		// get viewConfiguration
		mClickTimeout = ViewConfiguration.getPressedStateDuration()
				+ ViewConfiguration.getTapTimeout();// 接触屏幕的最小时间
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();// 滑动是要大于这个距离才移动

		// 改变位图的宽度实现图片按钮自适应
		// Bitmap bitMap = BitmapFactory.decodeFile(path);
		//
		// 02 int width = bitMap.getWidth();
		//
		// 03 int height = bitMap.getHeight();
		//
		// 04 // 设置想要的大小
		//
		// 05 int newWidth = 500;
		//
		// 06 int newHeight = 400;
		//
		// 07 // 计算缩放比例
		//
		// 08 float scaleWidth = ((float) newWidth) / width;
		//
		// 09 float scaleHeight = ((float) newHeight) / height;
		//
		// 10 // 取得想要缩放的matrix参数
		//
		// 11 Matrix matrix = new Matrix();
		//
		// 12 matrix.postScale(scaleWidth, scaleHeight);
		//
		// 13 // 得到新的图片
		//
		// 14 bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix,
		// true);

		// get Bitmap
		mBottom = BitmapFactory.decodeResource(resources, backImage);
		mBtnPressed = BitmapFactory.decodeResource(resources,
				R.drawable.btn_pressed);
		mBtnNormal = BitmapFactory.decodeResource(resources,
				R.drawable.btn_unpressed);
		mFrame = BitmapFactory.decodeResource(resources, R.drawable.frame);
		mMask = BitmapFactory.decodeResource(resources, R.drawable.mask);
		mCurBtnPic = mBtnNormal;
		mBtnWidth = mBtnPressed.getWidth();
		mMaskWidth = mMask.getWidth();
		mMaskHeight = mMask.getHeight();
		mBtnOffPos = mBtnWidth / 2;// 按钮的半径
		mBtnOnPos = mMaskWidth - mBtnWidth / 2;
		mBtnPos = mChecked ? mBtnOnPos : mBtnOffPos;
		mRealPos = getRealPos(mBtnPos);// 得到botton的实际位置
		final float density = getResources().getDisplayMetrics().density;// 屏幕的密度
		mVelocity = (int) (VELOCITY * density + 0.5f);// 将VELOCITY（单位dip）转化为px
		mExtendOffsetY = (int) (EXTENDED_OFFSET_Y * density + 0.5f);
		// 如果是除{VELOCITY / density + 0.5f}得到的结果单位是dip；原VELOCITY的单位就是px
		mSaveLayerRectF = new RectF(0, mExtendOffsetY, mMask.getWidth(),
				mMask.getHeight() + mExtendOffsetY);// （x,y,x,y）矩阵的起点和终点x轴方向没有便宜一周方向便宜了mExtendOffsetY；这个地方决定CheckBox的大小
		mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
	}

	@Override
	public void setEnabled(boolean enabled) {
		mAlpha = enabled ? MAX_ALPHA : MAX_ALPHA / 2;
		super.setEnabled(enabled);
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void toggle() {
		setChecked(!mChecked);
	}

	/**
	 * 内部调用此方法设置checked状态，此方法会延迟执行各种回调函数，保证动画的流畅度
	 * 
	 * @param checked
	 */
	private void setCheckedDelayed(final boolean checked) {
		this.postDelayed(new Runnable() {

			@Override
			public void run() {
				setChecked(checked);
			}
		}, 10);
	}

	/**
	 * <p>
	 * Changes the checked state of this button.
	 * </p>
	 * 
	 * @param checked
	 *            true to check the button, false to uncheck it
	 */
	public void setChecked(boolean checked) {

		if (mChecked != checked) {
			mChecked = checked;

			mBtnPos = checked ? mBtnOnPos : mBtnOffPos;
			mRealPos = getRealPos(mBtnPos);
			invalidate();

			// Avoid infinite recursions if setChecked() is called from a
			// listener
			if (mBroadcasting) {
				return;
			}

			mBroadcasting = true;
			if (mOnCheckedChangeListener != null) {
				mOnCheckedChangeListener.onCheckedChanged(SwitchButton.this,
						mChecked);
			}
			if (mOnCheckedChangeWidgetListener != null) {
				mOnCheckedChangeWidgetListener.onCheckedChanged(
						SwitchButton.this, mChecked);
			}

			mBroadcasting = false;
		}
	}

	public void setCheckedforString(String istrue) {
		boolean checked = true;
		if ("true".equals(istrue)) {
			checked = false;//false显示合格
		} else if ("1".equals(istrue)) {
			checked = false;
		} else {
			checked = true;//true显示不合格
		}
		setChecked(checked);
	}

	/**
	 * Register a callback to be invoked when the checked state of this button
	 * changes.
	 * 
	 * @param listener
	 *            the callback to call on checked state change
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	/**
	 * Register a callback to be invoked when the checked state of this button
	 * changes. This callback is used for internal purpose only.
	 * 
	 * @param listener
	 *            the callback to call on checked state change
	 * @hide
	 */
	void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeWidgetListener = listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		float deltaX = Math.abs(x - mFirstDownX);
		float deltaY = Math.abs(y - mFirstDownY);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			attemptClaimDrag();
			mFirstDownX = x;
			mFirstDownY = y;
			mCurBtnPic = mBtnPressed;
			mBtnInitPos = mChecked ? mBtnOnPos : mBtnOffPos;
			break;
		case MotionEvent.ACTION_MOVE:
			float time = event.getEventTime() - event.getDownTime();
			mBtnPos = mBtnInitPos + event.getX() - mFirstDownX;
			if (mBtnPos >= mBtnOffPos) {
				mBtnPos = mBtnOffPos;
			}
			if (mBtnPos <= mBtnOnPos) {
				mBtnPos = mBtnOnPos;
			}
			mTurningOn = mBtnPos > (mBtnOffPos - mBtnOnPos) / 2 + mBtnOnPos;

			mRealPos = getRealPos(mBtnPos);
			break;
		case MotionEvent.ACTION_UP:
			mCurBtnPic = mBtnNormal;
			time = event.getEventTime() - event.getDownTime();
			if (deltaY < mTouchSlop && deltaX < mTouchSlop
					&& time < mClickTimeout) {
				if (mPerformClick == null) {
					mPerformClick = new PerformClick();
				}
				if (!post(mPerformClick)) {
					performClick();
				}
			} else {
				startAnimation(!mTurningOn);
			}
			break;
		}

		invalidate();
		return isEnabled();
	}

	private final class PerformClick implements Runnable {
		public void run() {
			performClick();
		}
	}

	@Override
	public boolean performClick() {
		startAnimation(!mChecked);
		return true;
	}

	/**
	 * Tries to claim the user's drag motion, and requests disallowing any
	 * ancestors from stealing events in the drag.
	 */
	private void attemptClaimDrag() {
		mParent = getParent();
		if (mParent != null) {
			mParent.requestDisallowInterceptTouchEvent(true);
		}
	}

	/**
	 * 将btnPos转换成RealPos
	 * 
	 * @param btnPos
	 * @return
	 */
	private float getRealPos(float btnPos) {
		return btnPos - mBtnWidth / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 新建一个图层入栈后面都会画在这个图层上
		canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha, Canvas.MATRIX_SAVE_FLAG
				| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
				| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		// 绘制蒙板
		canvas.drawBitmap(mMask, 0, mExtendOffsetY, mPaint);// 中间两个参数为绘制的起点
		mPaint.setXfermode(mXfermode);// 设置图片的相交模式

		// 绘制底部图片
		canvas.drawBitmap(mBottom, mRealPos, mExtendOffsetY, mPaint);
		mPaint.setXfermode(null);
		// 绘制边框
		canvas.drawBitmap(mFrame, 0, mExtendOffsetY, mPaint);

		// 绘制按钮
		canvas.drawBitmap(mCurBtnPic, mRealPos, mExtendOffsetY, mPaint);
		// 在图层上画好后将图层推出栈显示
		canvas.restore();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) mMaskWidth,
				(int) (mMaskHeight + 2 * mExtendOffsetY));
	}

	private void startAnimation(boolean turnOn) {
		mAnimating = true;
		mAnimatedVelocity = turnOn ? -mVelocity : mVelocity;
		mAnimationPosition = mBtnPos;

		new SwitchAnimation().run();
	}

	private void stopAnimation() {
		mAnimating = false;
	}

	private final class SwitchAnimation implements Runnable {

		@Override
		public void run() {
			if (!mAnimating) {
				return;
			}
			doAnimation();
			FrameAnimationController.requestAnimationFrame(this);
		}
	}

	private void doAnimation() {
		mAnimationPosition += mAnimatedVelocity
				* FrameAnimationController.ANIMATION_FRAME_DURATION / 1000;
		if (mAnimationPosition <= mBtnOnPos) {
			stopAnimation();
			mAnimationPosition = mBtnOnPos;
			setCheckedDelayed(true);
		} else if (mAnimationPosition >= mBtnOffPos) {
			stopAnimation();
			mAnimationPosition = mBtnOffPos;
			setCheckedDelayed(false);
		}
		moveView(mAnimationPosition);
	}

	private void moveView(float position) {
		mBtnPos = position;
		mRealPos = getRealPos(mBtnPos);
		invalidate();
	}
}
