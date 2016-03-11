package com.chenpan.heart.diary.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chenpan.heart.diary.R;
import com.chenpan.heart.diary.tab.TextDiaryTab;

public class TextAdapter extends BaseAdapter {
	private Activity activity;
	private List<TextDiaryTab> list;
	
	/**
	 * @param activity
	 * @param list
	 */
	public TextAdapter(Activity activity, List<TextDiaryTab> list) {
		super();
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list==null?0:list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView=activity.getLayoutInflater().inflate(
					R.layout.text_list_item, null);
			holder=new ViewHolder();
			holder.diaryTitle=(TextView) convertView.findViewById(R.id.view_title);
			holder.diaryInfo=(TextView) convertView.findViewById(R.id.view_info);
			holder.date=(TextView) convertView.findViewById(R.id.view_date);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.diaryTitle.setText(list.get(position).getTitle());
		holder.diaryInfo.setText(list.get(position).getText());
		holder.date.setText(list.get(position).getCreatTime());
		
		return convertView;
	}
	class ViewHolder{
		TextView diaryTitle;
		TextView diaryInfo;
		TextView date;
	}
}
