package com.chenpan.heart.diary.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.chenpan.heart.diary.R;
/**
 * ��ʼ����Բ����ת����ɢ
 * @author Administrator
 *
 */

public class SplshView extends View {
	/**
	 * ��ת������Բ�뾶
	 */
	private float mRotationRadius = 90;
	/**
	 * ��תСԲ�뾶
	 */
	private float mCircleRadius = 18;
	/**
	 * СԲ��ɫ
	 */
	private int[] mCircleColors;
	/**
	 * ��תʱ��
	 */
	private long mRotationDuration = 1200;
	/**
	 * ��ɢ����ʱ��
	 */
	@SuppressWarnings("unused")
	private long mSplshDuration = 1200;
	/**
	 * ������ɫ
	 */
	private int mSplshBackColor = Color.WHITE;
	/**
	 * ����Բ�뾶
	 */
	private float mHoleRadius = 0f;
	/**
	 * ��ǰ��Բ��ת����
	 */
	private float mCurrentRotationAngle = 0f;
	/**
	 * ��ǰ��Բ�뾶
	 */
	private float mCurrentRotationRadius = mRotationRadius;
	/**
	 * ����Բ�Ļ���
	 */
	private Paint mPaint = new Paint();
	/**
	 * ���Ʊ����Ļ���
	 */
	private Paint mPaintBackground = new Paint();
	/**
	 * ��Ļ������X����
	 */
	private float mCenterX;
	/**
	 * ��Ļ������Y����
	 */
	private float mCenterY;
	/**
	 * ��Ļ�Խ��ߵ�һ��
	 */
	private float mDiagonalDist;
	/**
	 * ���浱ǰ����״̬��������������
	 */
	private SplshState mState = null;

	/**
	 * ����ģ��ģʽ����ͬ״ִ̬�в�ͬ�Ķ���
	 * 
	 * @author Administrator
	 * 
	 */
	private abstract class SplshState {
		public abstract void drawState(Canvas canvas);
	}

	public SplshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SplshView(Context context) {
		super(context);
		//
		init(context);
	}

	/**
	 * ��ʼ������
	 */
	private void init(Context context) {
		// ��ɫ��ʼ��
		mCircleColors = context.getResources().getIntArray(
				R.array.splsh_circle_color);
		// ���ʳ�ʼ��
		mPaint.setAntiAlias(true);
		mPaintBackground.setStyle(Style.STROKE);// ������
		mPaintBackground.setColor(mSplshBackColor);

	}

	@Override
	// ͼ�񻭳�����ø÷��� View��ʾ����
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mCenterX = w / 2f;
		mCenterY = h / 2f;
		mDiagonalDist = (float) Math.sqrt(w * w + h * h);
	}

	/**
	 * ��ת������ʧ
	 */
	public void splshDisapaer() {
		mState = new MergingState();
		//invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// �жϵ�ǰ�Ƿ��ǵ�һ������
		if (mState == null) {
			mState = new RotationState();// ִ�е�һ������

		}
		mState.drawState(canvas);
	}

	/**
	 * 
	 * @author Administrator ��ת����
	 */
	private class RotationState extends SplshState {
		private ValueAnimator animator;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@SuppressLint("NewApi")
		private RotationState() {
			// ��1200msת��2��
			// ��ֵ��
			animator = ValueAnimator.ofFloat(0, (float) Math.PI * 2);
			animator.setDuration(mRotationDuration);
			//animator.setInterpolator(new OvershootInterpolator(50f));
			animator.setInterpolator(new LinearInterpolator());
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// �õ�ĳ��ʱ��� ת���ٶ�
					mCurrentRotationAngle = (Float) animation
							.getAnimatedValue();
					invalidate();
				}
			});
			animator.setRepeatCount(ValueAnimator.INFINITE);// ���޴�������ת
			animator.start();
		}

		@Override
		public void drawState(Canvas canvas) {
			// ִ��3����
			// 1.��ջ��� 2.����СԲ 3.��תСԲ
			drawBackGround(canvas);
			drawCircle(canvas);

		}

	}

	/**
	 * ��ɢ����
	 * 
	 * @author Administrator
	 * 
	 */
	private class ExpandingState extends SplshState {
		private ValueAnimator animator;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@SuppressLint("NewApi")
		private ExpandingState() {
			// ����Բ��ɢ���� ���뾶Ϊ��Ļ�Խ��ߵ�һ��
			// �������� ��Բ�뾶��0�����Խ��ߵ�һ��
			animator = ValueAnimator.ofFloat(0, mDiagonalDist);
			animator.setDuration(mRotationDuration);
			// animator.setInterpolator(new OvershootInterpolator(10f));// ����Ч��
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// �õ�ĳ��ʱ��� ��Բ�İ뾶
					mHoleRadius = (Float) animation.getAnimatedValue();
					invalidate();
				}
			});
			// animator.setRepeatCount(ValueAnimator.INFINITE);//���޴�������ת
			// animator.addListener(new AnimatorListenerAdapter() {
			// @Override//�����ö�������
			// public void onAnimationEnd(Animator animation) {
			// super.onAnimationEnd(animation);
			// mState=new ExpandingState();
			// invalidate();
			// }
			// });
			animator.start();
		}

		@Override
		public void drawState(Canvas canvas) {
			// ���Ʊ�������Բ
			drawBackGround(canvas);
		}

	}

	/**
	 * 
	 * @author Administrator �ۺ϶���
	 */
	private class MergingState extends SplshState {
		private ValueAnimator animator;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private MergingState() {
			// �������� �뾶R-��0
			animator = ValueAnimator.ofFloat(mRotationRadius, 0);
			animator.setDuration(mRotationDuration );
			animator.setInterpolator(new OvershootInterpolator(50f));// ����Ч��
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@SuppressLint("NewApi")
				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// �õ�ĳ��ʱ��� ��Բ�İ뾶
					mCurrentRotationRadius = (Float) animation
							.getAnimatedValue();
					invalidate();
				}
			});
			// animator.setRepeatCount(ValueAnimator.INFINITE);//���޴�������ת
			animator.addListener(new AnimatorListenerAdapter() {
				@Override
				// �����ö�������
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					mState = new ExpandingState();
					invalidate();
				}
			});
			animator.start();
		}

		@Override
		public void drawState(Canvas canvas) {
			drawBackGround(canvas);
			drawCircle(canvas);
		}

	}

	/**
	 * ��ջ���
	 * 
	 * @param canvas
	 */
	public void drawBackGround(Canvas canvas) {
		if (mHoleRadius > 0f) {
			/**
			 * ���Ʊ�������Բ
			 * 
			 * @param canvas
			 */
			// ���ʿ��=�Խ���һ��-����Բ�İ뾶
			float strokewidth = mDiagonalDist / 2 - mHoleRadius;
			// Բ�İ뾶=�Խ��ߵ�һ��+���ʿ�ȵ�һ��
			float radius =mHoleRadius + strokewidth / 2;
			mPaintBackground.setStrokeWidth(strokewidth);
			canvas.drawCircle(mCenterX, mCenterY, radius, mPaintBackground);
		}else{
			canvas.drawColor(mSplshBackColor);
		}
		
	}

	

	/**
	 * ����СԲ
	 * 
	 * @param canvas
	 */
	public void drawCircle(Canvas canvas) {
		// СԲ������ٶ�
		float rotationAngle = (float) (Math.PI * 2 / mCircleColors.length);
		for (int i = 0; i < mCircleColors.length; i++) {
			/**
			 * x=R*cos(a)+mcx; y=R*sin(a)+mcy;
			 */
			// �Ƕ�=��ת֮ǰ��+��ת֮��ģ��ڼ�����Ҫ���Լ�
			double angle = i * rotationAngle + mCurrentRotationAngle;
			float cx = (float) (mCurrentRotationRadius * Math.cos(angle) + mCenterX);
			//
			// float cy= (float)
			// (mCurrentRotationAngle*Math.sin(angle)+mCenterY);
			float cy = (float) (mCurrentRotationRadius * Math.sin(angle) + mCenterY);
			mPaint.setColor(mCircleColors[i]);
			canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
		}
	}
}
