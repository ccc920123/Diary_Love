package com.cdjysdkj.diary;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdjysdkj.diary.adapter.GridViewAdapter;
import com.cdjysdkj.diary.bean.GridViewEntity;
import com.cdjysdkj.diary.tab.DillTab;
import com.cdjysdkj.diary.tabhelper.DillTabHelper;
import com.cdjysdkj.diary.utils.Date_U;
import com.cdjysdkj.diary.utils.StringUtils;
import com.cdjysdkj.diary.utils.ToastFactory;
import com.cdjysdkj.diary.view.timechose.JudgeDate;
import com.cdjysdkj.diary.view.timechose.ScreenInfo;
import com.cdjysdkj.diary.view.timechose.WheelMain;
import com.cdjysdkj.diary.widget.dialog.ActionSheetDialog;
import com.cdjysdkj.diary.widget.dialog.MyAlertDialog;
import com.cdjysdkj.diary.widget.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.cdjysdkj.diary.widget.dialog.ActionSheetDialog.SheetItemColor;

public class AddBillActivity extends Activity {
	private GridViewEntity ent;
	private GridViewAdapter adapter;
	private Integer[] img;
	private String[] st;
	private List<GridViewEntity> list;

	private ImageView mBack;
	private ImageView mSave;
	private Button mPay;
	private Button mIncome;
	private TextView mTime;
	private TextView mCashType;
	private ImageView changeImage;
	private TextView changeText;
	private EditText mMoney;
	private GridView gridview;

	private WheelMain wheelMain;
	private String YearMoth;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 支出还是收入 0是支出 1是收入
	 */
	private int instate = 0;
	/**
	 * img的下标
	 */
	private int indeximg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_bill);
		getView();
		initView();
		initSelectColor();
	}

	private void initSelectColor() {
		mPay.setBackgroundColor(mPay.getResources().getColor(R.color.blue2));
		mIncome.setBackgroundColor(mPay.getResources().getColor(R.color.white));
		mPay.setTextColor(mPay.getResources().getColor(R.color.white));
		mIncome.setTextColor(mIncome.getResources().getColor(R.color.black));

	}

	/**
	 * 此方法可展示的gridview单击结果
	 */
	private void changedShow(GridViewEntity ent, int position) {

		Integer[] img = ent.getImgs();
		int path = img[position];
		changeImage.setImageResource(path);
		String[] st = ent.getTypes();
		String text = st[position];
		changeText.setText(text);

	}

	/**
	 * 支出是加载的
	 */
	private void initView() {
		img = new Integer[] { R.drawable.type_big_1, R.drawable.type_big_2,
				R.drawable.type_big_3, R.drawable.type_big_4,
				R.drawable.type_big_5, R.drawable.type_big_6,
				R.drawable.type_big_7, R.drawable.type_big_8,
				R.drawable.type_big_9, R.drawable.type_big_10,
				R.drawable.type_big_11, R.drawable.type_big_12 };
		st = new String[] { "一般", "用餐", "零食", "交通", "充值", "购物", "娱乐", "住房",
				"约会", "网购", "日用品", "香烟" };
		ent = new GridViewEntity(img, st);
		list = new ArrayList<GridViewEntity>();
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		adapter = new GridViewAdapter(list, this);
		gridview.setAdapter(adapter);
	}

	/**
	 * 收入时加载
	 */
	private void incomeIsClick() {
		img = new Integer[] { R.drawable.type_big_13, R.drawable.type_big_14,
				R.drawable.type_big_15, R.drawable.type_big_16,
				R.drawable.type_big_17, R.drawable.type_big_18,
				R.drawable.type_big_19 };
		st = new String[] { "工资", "外快兼职", "奖金", "借入", "零花钱", "投资收入", "礼物红包" };
		ent = new GridViewEntity(img, st);
		list = new ArrayList<GridViewEntity>();
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		list.add(ent);
		adapter = new GridViewAdapter(list, this);
		adapter.notifyDataSetChanged();
		gridview.setAdapter(adapter);
	}

	private void getView() {
		mBack = (ImageView) findViewById(R.id.topbar_menu_left);
		mBack.setOnClickListener(clicklistener);
		mSave = (ImageView) findViewById(R.id.topbar_menu_right);
		mSave.setOnClickListener(clicklistener);
		mPay = (Button) findViewById(R.id.pay_btn);
		mPay.setOnClickListener(clicklistener);
		mIncome = (Button) findViewById(R.id.income_btn);
		mIncome.setOnClickListener(clicklistener);
		mTime = (TextView) findViewById(R.id.time_text);
		mTime.setText(Date_U.getCurrentTime());
		mTime.setOnClickListener(clicklistener);
		mCashType = (TextView) findViewById(R.id.take_text);
		mCashType.setOnClickListener(clicklistener);
		changeImage = (ImageView) findViewById(R.id.changedshow_jpg);
		changeText = (TextView) findViewById(R.id.changedshow_text);
		mMoney = (EditText) findViewById(R.id.changedshow_number);
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ent = list.get(position);
				changedShow(ent, position);
				indeximg = position;
			}
		});

	}

	/**
	 * @功能描述： 显示时间对话框
	 * @param
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 */
	protected void showTimeDialog(final TextView edv, String chosetimeBegin) {
		LayoutInflater inflater = LayoutInflater.from(AddBillActivity.this);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(AddBillActivity.this);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		String time = edv.getText().toString();
		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month, day);
		MyAlertDialog alertDialog = new MyAlertDialog(this);
		alertDialog.builder().setTitle(chosetimeBegin).setView(timepickerview)
				.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(View arg0) {

					}
				}).setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						edv.setText(wheelMain.getTime());
					}
				});
		alertDialog.show();
	}

	private OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.topbar_menu_left:// 返回键
				Afinish();
				break;
			case R.id.topbar_menu_right:// 保存
				save();
				break;
			case R.id.pay_btn:// 支出
				initView();
				initSelectColor();
				ent = list.get(0);
				changedShow(ent, 0);
				indeximg = 0;
				instate = 0;
				break;
			case R.id.income_btn:// 收入
				incomeIsClick();
				mIncome.setBackgroundColor(mIncome.getResources().getColor(
						R.color.blue2));
				mPay.setBackgroundColor(mPay.getResources().getColor(
						R.color.white));
				mIncome.setTextColor(mIncome.getResources().getColor(
						R.color.white));
				mPay.setTextColor(mPay.getResources().getColor(R.color.black));
				ent = list.get(0);
				changedShow(ent, 0);
				indeximg = 0;
				instate = 1;
				break;
			case R.id.time_text:// 时间
				showTimeDialog(mTime, "选择时间");
				break;
			case R.id.take_text:// 账号类型
				new ActionSheetDialog(AddBillActivity.this)
						.builder()
						.setTitle("请选择账户类型")
						.setCancelable(true)
						.setCanceledOnTouchOutside(true)
						.addSheetItem("现  金", getSheetColor("现  金"),
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {
										mCashType.setText("现  金");
									}
								})
						.addSheetItem("储蓄卡", getSheetColor("储蓄卡"),
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {
										mCashType.setText("储蓄卡");
									}
								})
						.addSheetItem("信用卡", getSheetColor("信用卡"),
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {
										mCashType.setText("信用卡");
									}
								})
						.addSheetItem("支付宝", getSheetColor("支付宝"),
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {
										mCashType.setText("支付宝");
									}
								}).show();
				break;

			}
		}
	};

	protected void Afinish() {
		Intent intent = new Intent(AddBillActivity.this, TabBillActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 保存账单到数据库，如果当天的相同消费类型累加
	 */
	protected void save() {
		String qian = mMoney.getText().toString();
		if (!StringUtils.isEmpty(qian)) {
			String time = mTime.getText().toString();
			YearMoth = Date_U.TimestampToTimestamp(time);
			String payType=mCashType.getText().toString();
			DillTabHelper dillTabHelper = DillTabHelper.getInstance();
			List<DillTab> dillTabs = dillTabHelper.queryBytimeAndcashtype(time,
					String.valueOf(instate), String.valueOf(indeximg),payType);
			if (dillTabs != null && dillTabs.size() > 0) {// 追加在后面，累加
				DillTab dillTab = dillTabs.get(0);
				int money = Integer.valueOf(dillTab.getMoney())
						+ Integer.valueOf(qian);
				dillTab.setMoney(String.valueOf(money));
				dillTab.setDeclare(dillTab.getDeclare() + "\n" + "");// 这里添加描述，以转行的方式显示
				dillTabHelper.update(dillTab);
			} else {// 新增，添加一条数据
				DillTab dillTab = new DillTab();
				dillTab.setTime(YearMoth);
				dillTab.setDetailtime(time);
				dillTab.setMoney(qian);
				dillTab.setAccountType(payType);
				dillTab.setDeclare("");// 这里以后添加描述
				dillTab.setIndexImg(String.valueOf(indeximg));
				dillTab.setType(changeText.getText().toString());
				dillTab.setTakeType(String.valueOf(instate));
				dillTabHelper.insert(dillTab);

			}
			Afinish();
		} else {
			ToastFactory.getToast(this, "请输入金额……").show();
		}
	}

	/**
	 * 如果是当前的显示红色
	 * 
	 * @param item
	 * @return
	 */
	protected SheetItemColor getSheetColor(String item) {
		String text = mCashType.getText().toString();
		if (item.equals(text)) {
			return SheetItemColor.Red;
		} else {
			return SheetItemColor.Blue;
		}

	}

	@Override
	public void onBackPressed() {
		Afinish();
	}

}
