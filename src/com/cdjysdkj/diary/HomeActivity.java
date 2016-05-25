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
 * ������ɺ� ��ת����ҳ
 */
public class HomeActivity extends BaseHomeActivity implements
		OnItemSelectedListener, OnItemClickListener {
	private DragLayout mDragLayout;
	/**
	 * �м�Բ�ΰ�ť
	 */
	private RelativeLayout relbtn;
	private CircleLayout circleMenu;
	private boolean isopen = false;

	/**
	 * ������
	 */
	private SwitchButton switchButton;
	/**
	 * ����
	 */
	private LinearLayout shareLayout;
	/**
	 * ����
	 */
	private LinearLayout aboutLayout;
	/**
	 * ����
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
		mDragLayout.setDragListener(new DragListener() {// �����������б����С��Ч��
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

				// ȡ����Ӧ��ֵ�����û�и�ֵ��˵����δд�룬��true��ΪĬ��ֵ
				boolean isFirstlock = preferences.getBoolean("isFirstLock",
						true);
				// ToastFactory.getToast(HomeActivity.this, ""+islock).show();
				if (isFirstlock) {// ����ǵ�һ������������ת��������
					Bundle bundle = new Bundle();
					bundle.putBoolean("first", true);

					goToActivity(HomeActivity.this, PasswordActivity.class,
							bundle);
					finish();
				} else {// �޸�״̬
					Editor editor = preferences.edit();
					// ��������
					editor.putBoolean("isLock", !islock);
					// �ύ�޸�
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
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle("I����");
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.chenpan.heart.diary");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("ȥ���ģ���ȥ�ģ��������õĻ��䣡");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		oks.setImagePath(Constant.MYAPP_PATH_ICON);// ȷ��SDcard������ڴ���ͼƬ
		//oks.setImageUrl();
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.chenpan.heart.diary");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");
		
		// ��������GUI
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
			if ("jsb".equals(name)) {// �ռǱ�
				goToActivity(this, TextDiaryActivity.class, null);
				finish();
			} else if ("shdd".equals(name)) {// �����˵�
				goToActivity(this, TabBillActivity.class, null);
				finish();
			} else if ("jcsj".equals(name)) {// ����˲��
				goToActivity(this, MomentActivity.class, null);
				finish();
			} else if ("qqh".equals(name)) {// �����ǩ
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

		// �õ�һ��LayoutAnimationController����

		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setOrder(LayoutAnimationController.ORDER_RANDOM);

		// ���ÿؼ���ʾ���ʱ�䣻

		lac.setDelay(0.5f);
		return lac;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}

}
