package com.cdjysdkj.diary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cdjysdkj.diary.R;

public class UpDialog extends Dialog implements OnClickListener {
	private TextView leftTextView, rightTextView;
	private IDialogOnclickInterface dialogOnclickInterface;
	private Context context;

	public UpDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.up_dialog_layout);
	
		leftTextView = (TextView) findViewById(R.id.textview_one);
		rightTextView = (TextView) findViewById(R.id.textview_two);
		leftTextView.setOnClickListener(this);
		rightTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dialogOnclickInterface = (IDialogOnclickInterface) context;
		switch (v.getId()) {
		case R.id.textview_one:
			dialogOnclickInterface.leftOnclick();
			break;
		case R.id.textview_two:
			dialogOnclickInterface.rightOnclick();
			break;
		default:
			break;
		}
	}

	public interface IDialogOnclickInterface {
		void leftOnclick();

		void rightOnclick();
	}
}
