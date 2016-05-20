package com.cdjysdkj.diary.view.imagefilter.ndk;

import com.cdjysdkj.diary.view.imagefilter.IImageFilter;
import com.cdjysdkj.diary.view.imagefilter.Image;
import com.cdjysdkj.diary.view.imagefilterndk.ImageFilterNdkUtil;

public class MylearnFiter implements IImageFilter {
	public static class  MY_TYPE { 
		public static int MY_ZERO = 0;
		public static int MY_ONE = 1;
		public static int MY_TWO= 2;
		public static int MY_THREE = 3;
		public static int MY_FOUR = 4;
		public static int MY_FIVE = 5;
		public static int MY_SIX = 6;
		public static int MY_SEVEN = 7;
		public static int MY_EIGHT = 8;
		public static int MY_NINE = 9;
		
		
	} 
	private int IMG_Type;
	public boolean isNDK=true;

	

	@Override
	public Image process(Image imageIn) {
		switch (IMG_Type) {
		case 0:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKwhite(imageIn.getSrouseImage()));
			break;

		case 1:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKice(imageIn.getSrouseImage()));
			break;
		case 2:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKfire(imageIn.getSrouseImage()));
			break;

		case 3:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKcomicStrip(imageIn.getSrouseImage()));
			break;
		case 4:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKlight(imageIn.getSrouseImage()));
			break;
		case 5:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKeclosion(imageIn.getSrouseImage()));
			break;
		case 6:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKeclosionAndWhite(imageIn.getSrouseImage()));
			break;
		case 7:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKdim(imageIn.getSrouseImage()));
			break;
		case 8:
			imageIn.setDestImage(ImageFilterNdkUtil.getNDKlomo(imageIn.getSrouseImage()));
			break;
		case 9:
			imageIn.setDestImage(ImageFilterNdkUtil.toGrayscale(imageIn.getSrouseImage()));
			break;
		}
		return imageIn;
	}

	/**
	 * @param iMG_Type
	 */
	public MylearnFiter(int iMG_Type,boolean isndk) {
		super();
		IMG_Type = iMG_Type;
		isNDK=isndk;
		
	}

	@Override
	public boolean getisNDK() {
		// TODO Auto-generated method stub
		return isNDK;
	}

}
