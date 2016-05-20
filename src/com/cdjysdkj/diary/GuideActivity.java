package com.cdjysdkj.diary;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cdjysdkj.diary.adapter.WelcomeAdapter;
import com.cdjysdkj.diary.anomotion.TraslatePagerFormer;
import com.cdjysdkj.diary.view.SplshView;

/**
 * 引导页
 */
public class GuideActivity extends FragmentActivity implements
		OnPageChangeListener {

	private ViewPager vp;
	private WelcomeAdapter viewAdapter;
	private List<View> views;

	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	private int[] layout = { R.layout.guide_a, R.layout.guide_b,
			R.layout.guide_c };
	private FrameLayout fl;
	private SplshView sp;
	private View contentView;
	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fl = new FrameLayout(this);
		contentView = this.getLayoutInflater().inflate(R.layout.activity_guide,
				null);
		fl.addView(contentView);
		sp = new SplshView(this);
		// sp.setAlpha(0.8f);
		fl.addView(sp);
		setContentView(fl);
		// 模拟加载数据
		loadingData();
		// 初始化页面
		initViews();
		// 初始化底部小点
		initDots();
	}

	/**
	 * 模拟加载数据
	 */
	private void loadingData() {
		// 数据加载完成，旋转动画结束，开启扩散对话
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				//
				sp.splshDisapaer();
				// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
			}
		}, 3000);// 6s以后执行run里面的方法
	}

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.guide_a, null));
		views.add(inflater.inflate(R.layout.guide_b, null));
		views.add(inflater.inflate(R.layout.guide_c, null));

		// 初始化Adapter
		viewAdapter = new WelcomeAdapter(getSupportFragmentManager(), layout);

		vp = (ViewPager) contentView.findViewById(R.id.viewpager);
		vp.setPageTransformer(true, new TraslatePagerFormer());
		vp.setAdapter(viewAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) contentView.findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int position) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int position) {
		// 设置底部小点选中状态
		setCurrentDot(position);
	}

}



