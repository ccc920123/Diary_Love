package com.chenpan.heart.diary.view.imagefilterndk;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class ImageFilterNdkUtil {
	/**
	 * 调用美白jni
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKwhite(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.whitening(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		
		return rBitmap;
	}
	/**
	 * 冰冻
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKice(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.ice(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		return rBitmap;
	}
	/**
	 * 熔铸
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKfire(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.fire(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		
		return rBitmap;
	}
	/**
	 * 连环画comic strip
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKcomicStrip(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.comicStrip(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		
		return rBitmap;
	}
	/**
	 * 照亮边缘
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKlight(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.light(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		
		return rBitmap;
	}
	/**
	 * 羽化
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKeclosion(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.eclosion(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		
		return rBitmap;
	}
	/**
	 * 羽化加美白
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKeclosionAndWhite(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.eclosionAndWhite(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		
		return rBitmap;
	}
	/**
	 * 模糊
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKdim(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.dim(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		
		return rBitmap;
	}
	/**
	 * lomo
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getNDKlomo(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] buff=new int[width*height];
		bitmap.getPixels(buff, 0, width, 1, 1, width-1, height-1);
		int[] result=ImageFilterNdk.lomo(buff, width, height);
		Bitmap rBitmap=Bitmap.createBitmap(result, width, height, Bitmap.Config.RGB_565);
		
		return rBitmap;
	}
	/**
	 * 图片灰度化
	 * @param bmpOriginal
	 * @return
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0); // 灰色
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}
}
