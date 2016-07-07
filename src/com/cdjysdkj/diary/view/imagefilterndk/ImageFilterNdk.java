package com.cdjysdkj.diary.view.imagefilterndk;

public class ImageFilterNdk {

	/**
	 * 美白效果
	 */
	public native static int[] whitening(int[] buff,int width,int height);
	/**
	 * 冰冻效果
	 */
	public native static int[] ice(int[] buff,int width,int height);
	/**
	 * 熔铸效果fire
	 */
	public native static int[] fire(int[] buff,int width,int height);
	/**
	 * 连环画comic strip
	 */
	public native static int[] comicStrip(int[] buff,int width,int height);
	/**
	 * 照亮边缘
	 * @param buff
	 * @param width
	 * @param height
	 * @return
	 */
	public native static int[] light(int[] buff,int width,int height);
	/**
	 * 羽化
	 * @param buff
	 * @param width
	 * @param height
	 * @return
	 */
	public native static int[] eclosion(int[] buff,int width,int height);
	/**
	 * 羽化加美白
	 * @param buff
	 * @param width
	 * @param height
	 * @return
	 */
	public native static int[] eclosionAndWhite(int[] buff,int width,int height);
	/**
	 * 模糊
	 * @param buff
	 * @param width
	 * @param height
	 * @return
	 */
	public native static int[] dim(int[] buff,int width,int height);
	/**
	 * lomo
	 * @param buff
	 * @param width
	 * @param height
	 * @return
	 */
	public native static int[] lomo(int[] buff,int width,int height,int type);
	
	public native static int[] filterfiveAndsix(int[] buff,int width,int height,int type);
	
}
