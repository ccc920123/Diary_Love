package com.cdjysdkj.diary.anomotion;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.view.ViewGroup;

import com.cdjysdkj.diary.R;

public class TraslatePagerFormer implements PageTransformer{
/**
 * 当viewpager滑动时候，都会回调该方法
 * view：当前页面
 * postion：滑动位置
 * 当-1到1时  在屏幕上
 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void transformPage(View view, float position) {
		ViewGroup group = (ViewGroup) view.findViewById(R.id.rl);
		
		
		/*if (position>-1&&position<1) {
			//父控件名字相同，几个页面才会实现同样的效果
			//如果单一的判断
		//	ViewGroup group = (ViewGroup) view.findViewById(R.id.rl);
			for (int i = 0; i < group.getChildCount(); i++) {
				View child=group.getChildAt(i);//子控件
				//各个空间不同的移动加速度
				float foctor=(float) Math.random();
				if(child.getTag()==null){
					child.setTag(foctor);
				}else{
					foctor=(Float) child.getTag();//让每个控件保持相同的加速度
				}
				//position:0~-1
//				child.setTranslationX(postion*foctor*child.getWidth());
				//child.setTranslationX(postion*i*100);
				child.setTranslationX(-position*i*100);
			}
		}*/
		/*//缩放范围0~1  效果页面从小到大显示在屏幕上
		group.setScaleX(1-Math.abs(position));//页面缩放
		group.setScaleY(1-Math.abs(position));*/
		
		//效果 ：界面缩放到一定大小——平移  
		group.setScaleX(Math.max(0.8f, 1-Math.abs(position)));//页面缩放
		group.setScaleY(Math.max(0.8f, 1-Math.abs(position)));
		
		//3D外翻转
		group.setPivotX(position<0f?group.getWidth():0f);
		group.setPivotY(group.getHeight()*0.5f);
		group.setRotationY(position*90);//度数越大，3D尖角越小
		
		/*//3D内翻转
				group.setPivotX(position<0f?group.getWidth():0f);
				group.setPivotY(group.getHeight()*0.5f);
				group.setRotationY(-position*90);//度数越大，3D尖角越小
		*/
		/*//效果：各个界面绕着自己中心旋转
		group.setPivotX(group.getWidth()*0.5f);
		group.setPivotY(group.getHeight()*0.5f);
		group.setRotationY(-position*90);*/
	}
	
}