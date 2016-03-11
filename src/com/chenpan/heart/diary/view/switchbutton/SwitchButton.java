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
 * ���ذ�ť�������С��ȫ��ͼƬ��������Ϊ��ťͼƬ�ƶ����̶�����ʵ�֣�ʵ���ϲ��ڲ��ϸı�ͼƬ�Ļ������ʵ�ֵ�
 * canvas.drawBitmap(mBottom, mRealPos, mExtendOffsetY, mPaint)mRealPos�ڲ��ϸı䣻
 * ��ťͼƬ��ȫ�������˵ģ����ǻ������RectF��ͼƬ��ť��1/2��������ʾ��ͼ������ʾͼƬ��1/2��
 * ��ʱ�޷�ʵ�ֽ�ͼƬcanvas.drawBitmap(mBottom, null, RectF, mPaint)����ָ����С�ľ�����RectF��
 * ���Կ��Ǹı�õ�bitmap�ĳߴ�������Ӧ
 * 
 * @author Administrator
 * 
 */

public class SwitchButton extends CheckBox {
	private Paint mPaint;

	private ViewParent mParent;
	/** ������ʽͼƬ **/
	private Bitmap mBottom;
	/** ��ǰ���ذ�ť��ʾͼƬ **/
	private Bitmap mCurBtnPic;
	/** ���ذ�ť����ͼƬ **/
	private Bitmap mBtnPressed;
	/** ���ذ�ť����ͼƬ **/
	private Bitmap mBtnNormal;
	/** ���ر߿�ͼƬ **/
	private Bitmap mFrame;
	/** ���ص�ͼ **/
	private Bitmap mMask;
	/** ���ػ����������������ƿ��ش�С **/
	private RectF mSaveLayerRectF;
	/** ͼƬ�����ཻģʽ **/
	private PorterDuffXfermode mXfermode;

	private float mFirstDownY; // �״ΰ��µ�Y

	private float mFirstDownX; // �״ΰ��µ�X
	/** ���ذ�ť����ʵ��λ�� **/
	private float mRealPos; // ͼƬ�Ļ���λ��
	/** ���ذ�ťλ�� **/
	private float mBtnPos; // ��ť��λ��

	private float mBtnOnPos; // ���ش򿪵�λ��

	private float mBtnOffPos; // ���عرյ�λ��
	/** ���ص�ͼ��� **/
	private float mMaskWidth;
	/** ���ص�ͼ�߶� **/
	private float mMaskHeight;
	/** ���ذ�ť�Ŀ��=������ʽͼƬ��� **/
	private float mBtnWidth;

	private float mBtnInitPos;
	/** ����¼�����������Сʱ�� **/
	private int mClickTimeout;
	/** �����¼�����������С���� **/
	private int mTouchSlop;
	/** ������͸���� **/
	private final int MAX_ALPHA = 255;

	private int mAlpha = MAX_ALPHA;
	/** ���״̬ **/
	private boolean mChecked = true;
	private boolean mBroadcasting;
	private boolean mTurningOn;
	/** �̳�Runnable���ࣻ����һ���߳�ִ��һ�ε���¼� **/
	private PerformClick mPerformClick;

	private OnCheckedChangeListener mOnCheckedChangeListener;

	private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

	private boolean mAnimating;
	/** �������� **/
	private final float VELOCITY = 350;
	/** ��ǰ�������� **/
	private float mVelocity;
	/** y�᷽���ϵ�ƫ���� **/
	private final float EXTENDED_OFFSET_Y = 0;// ƫ��15��������Χ�����ڵ��
	/** ��ǰy�᷽���ϵ�ƫ���� **/
	private float mExtendOffsetY; // Y�᷽�����������,����������

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
				+ ViewConfiguration.getTapTimeout();// �Ӵ���Ļ����Сʱ��
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();// ������Ҫ�������������ƶ�

		// �ı�λͼ�Ŀ��ʵ��ͼƬ��ť����Ӧ
		// Bitmap bitMap = BitmapFactory.decodeFile(path);
		//
		// 02 int width = bitMap.getWidth();
		//
		// 03 int height = bitMap.getHeight();
		//
		// 04 // ������Ҫ�Ĵ�С
		//
		// 05 int newWidth = 500;
		//
		// 06 int newHeight = 400;
		//
		// 07 // �������ű���
		//
		// 08 float scaleWidth = ((float) newWidth) / width;
		//
		// 09 float scaleHeight = ((float) newHeight) / height;
		//
		// 10 // ȡ����Ҫ���ŵ�matrix����
		//
		// 11 Matrix matrix = new Matrix();
		//
		// 12 matrix.postScale(scaleWidth, scaleHeight);
		//
		// 13 // �õ��µ�ͼƬ
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
		mBtnOffPos = mBtnWidth / 2;// ��ť�İ뾶
		mBtnOnPos = mMaskWidth - mBtnWidth / 2;
		mBtnPos = mChecked ? mBtnOnPos : mBtnOffPos;
		mRealPos = getRealPos(mBtnPos);// �õ�botton��ʵ��λ��
		final float density = getResources().getDisplayMetrics().density;// ��Ļ���ܶ�
		mVelocity = (int) (VELOCITY * density + 0.5f);// ��VELOCITY����λdip��ת��Ϊpx
		mExtendOffsetY = (int) (EXTENDED_OFFSET_Y * density + 0.5f);
		// ����ǳ�{VELOCITY / density + 0.5f}�õ��Ľ����λ��dip��ԭVELOCITY�ĵ�λ����px
		mSaveLayerRectF = new RectF(0, mExtendOffsetY, mMask.getWidth(),
				mMask.getHeight() + mExtendOffsetY);// ��x,y,x,y������������յ�x�᷽��û�б���һ�ܷ��������mExtendOffsetY������ط�����CheckBox�Ĵ�С
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
	 * �ڲ����ô˷�������checked״̬���˷������ӳ�ִ�и��ֻص���������֤������������
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
			checked = false;//false��ʾ�ϸ�
		} else if ("1".equals(istrue)) {
			checked = false;
		} else {
			checked = true;//true��ʾ���ϸ�
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
	 * ��btnPosת����RealPos
	 * 
	 * @param btnPos
	 * @return
	 */
	private float getRealPos(float btnPos) {
		return btnPos - mBtnWidth / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// �½�һ��ͼ����ջ���涼�ử�����ͼ����
		canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha, Canvas.MATRIX_SAVE_FLAG
				| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
				| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		// �����ɰ�
		canvas.drawBitmap(mMask, 0, mExtendOffsetY, mPaint);// �м���������Ϊ���Ƶ����
		mPaint.setXfermode(mXfermode);// ����ͼƬ���ཻģʽ

		// ���Ƶײ�ͼƬ
		canvas.drawBitmap(mBottom, mRealPos, mExtendOffsetY, mPaint);
		mPaint.setXfermode(null);
		// ���Ʊ߿�
		canvas.drawBitmap(mFrame, 0, mExtendOffsetY, mPaint);

		// ���ư�ť
		canvas.drawBitmap(mCurBtnPic, mRealPos, mExtendOffsetY, mPaint);
		// ��ͼ���ϻ��ú�ͼ���Ƴ�ջ��ʾ
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
