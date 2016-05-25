package com.cdjysdkj.diary;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.cdjysdkj.diary.application.BaseHomeActivity;
import com.cdjysdkj.diary.constant.Constant;
import com.cdjysdkj.diary.sliding.DragLayout;
import com.cdjysdkj.diary.sliding.DragLayout.DragListener;
import com.cdjysdkj.diary.utils.ButtonTools;
import com.cdjysdkj.diary.utils.ToastFactory;
import com.cdjysdkj.diary.view.CircleLayout;
import com.cdjysdkj.diary.view.ClickContinue;
import com.cdjysdkj.diary.view.CircleLayout.OnItemClickListener;
import com.cdjysdkj.diary.view.CircleLayout.OnItemSelectedListener;
import com.cdjysdkj.diary.view.switchbutton.SwitchButton;

/**
 * 引导完成后 跳转的主页
 */
public class HomeActivity extends BaseHomeActivity implements
		OnItemSelectedListener, OnItemClickListener {
	private DragLayout mDragLayout;
	/**
	 * 中间圆形按钮
	 */
	private RelativeLayout relbtn;
	private CircleLayout circleMenu;
	private boolean isopen = false;

	/**
	 * 密码锁
	 */
	private SwitchButton switchButton;
	/**
	 * 分享
	 */
	private LinearLayout shareLayout;
	/**
	 * 关于
	 */
	private LinearLayout aboutLayout;
	/**
	 * 建议
	 */
	private LinearLayout adviceLayout;

	private SharedPreferences preferences;
	private ClickContinue mlighttext;
	private LinearLayout textlLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainhome);
		getview();
		init();
	}

	private void init() {
		mlighttext.upDateText();
		if (Constant.HOMETIMES < 1) {
			textlLayout.setLayoutAnimation(getAnimation());
			circleMenu.setLayoutAnimation(getAnimation());
		}
		preferences = getSharedPreferences(Constant.SHAREDPREFERENCES_NAME,
				MODE_PRIVATE);
		switchButton.setChecked(!preferences.getBoolean("isLock", false));
		mDragLayout.setDragListener(new DragListener() {// 动作监听，有变大缩小的效果
					@Override
					public void onOpen() {
						isopen = true;
					}

					@Override
					public void onClose() {
						isopen = false;
					}

					@Override
					public void onDrag(float percent) {

					}
				});
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);

		relbtn.setOnClickListener(onclicklister);
		shareLayout.setOnClickListener(onclicklister);
		aboutLayout.setOnClickListener(onclicklister);
		adviceLayout.setOnClickListener(onclicklister);
		switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean islock) {

				// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
				boolean isFirstlock = preferences.getBoolean("isFirstLock",
						true);
				// ToastFactory.getToast(HomeActivity.this, ""+islock).show();
				if (isFirstlock) {// 如果是第一次设置锁，跳转到锁界面
					Bundle bundle = new Bundle();
					bundle.putBoolean("first", true);

					goToActivity(HomeActivity.this, PasswordActivity.class,
							bundle);
					finish();
				} else {// 修改状态
					Editor editor = preferences.edit();
					// 存入数据
					editor.putBoolean("isLock", !islock);
					// 提交修改
					editor.commit();
				}
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		Constant.HOMETIMES++;
	}

	private OnClickListener onclicklister = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (!ButtonTools.isFastDoubleClick()) {
				switch (view.getId()) {
				case R.id.relbtn:
					goToActivity(HomeActivity.this, CenterActivity.class, null);
					finish();
					break;
				case R.id.share:
					showShare();
					break;
				case R.id.about:
					goToActivity(HomeActivity.this, AboutActivity.class, null);
					finish();
					break;
				case R.id.advice:
					goToActivity(HomeActivity.this, AdviceActivity.class, null);
					finish();
					break;

				}
			}
		}
	};

	private void getview() {
		circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
		relbtn = (RelativeLayout) findViewById(R.id.relbtn);
		mDragLayout = (DragLayout) findViewById(R.id.side_layout);
		switchButton = (SwitchButton) findViewById(R.id.lock);
		shareLayout = (LinearLayout) findViewById(R.id.share);
		aboutLayout = (LinearLayout) findViewById(R.id.about);
		adviceLayout = (LinearLayout) findViewById(R.id.advice);
		mlighttext = (ClickContinue) findViewById(R.id.lighttext);
		textlLayout = (LinearLayout) findViewById(R.id.text_layout);
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("I·记");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.chenpan.heart.diary");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("去过的，过去的，都是美好的回忆！");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(Constant.MYAPP_PATH_ICON);// 确保SDcard下面存在此张图片
		//oks.setImageUrl();
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.chenpan.heart.diary");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");
		
		// 启动分享GUI
		oks.show(this);
	}

	@Override
	protected String getActionBarTitle() {
		return getResources().getString(R.string.app_name);
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		return false;
	}

	@Override
	protected void onHomeActionClick() {
		if (isopen) {
			mDragLayout.close();
			isopen = false;
		} else {
			mDragLayout.open();
			isopen = true;
		}

	}

	@Override
	public void onItemClick(View view, int position, long id, String name) {
		if (!ButtonTools.isFastDoubleClick()) {
			if ("jsb".equals(name)) {// 日记本
				goToActivity(this, TextDiaryActivity.class, null);
				finish();
			} else if ("shdd".equals(name)) {// 生活账单
				goToActivity(this, TabBillActivity.class, null);
				finish();
			} else if ("jcsj".equals(name)) {// 精彩瞬间
				goToActivity(this, MomentActivity.class, null);
				finish();
			} else if ("qqh".equals(name)) {// 生活便签
				goToActivity(this, NotesListActivity.class, null);
				finish();
			}
		}
	}

	@Override
	public void onItemSelected(View view, int position, long id, String name) {

	}

	@Override
	protected void addActions() {
		actionBar.setHomeLogo(R.drawable.topbar_menu_left);
	}

	public LayoutAnimationController getAnimation() {
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.anim_item);

		// 得到一个LayoutAnimationController对象；

		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setOrder(LayoutAnimationController.ORDER_RANDOM);

		// 设置控件显示间隔时间；

		lac.setDelay(0.5f);
		return lac;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}

}
