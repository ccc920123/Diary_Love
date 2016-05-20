package com.cdjysdkj.diary.utils;

import android.content.Context;

/**
 * å±å¹•å¤§å°
 * @author CXY
 *
 */
public class DisplayMetricsEN {
  public static int width;
  public static int heigth;
public static int getWidth() {
	return width;
}
public static void setWidth(int width) {
	DisplayMetricsEN.width = width;
}
public static int getHeigth() {
	return heigth;
}
public static void setHeigth(int heigth) {
	DisplayMetricsEN.heigth = heigth;
}
public static int DipToPixels(Context context, int dip) {  
    final float SCALE = context.getResources().getDisplayMetrics().density;  
  
    float valueDips = dip;  
    int valuePixels = (int) (valueDips * SCALE + 0.5f);  
  
    return valuePixels;  
  
}  
  
// ÏñËØ×ªdip  
public static  float PixelsToDip(Context context, int Pixels) {  
    final float SCALE = context.getResources().getDisplayMetrics().density;  
  
    float dips = Pixels / SCALE;  
  
    return dips;  
  
}  
}
