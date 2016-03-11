package com.chenpan.heart.diary.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.chenpan.heart.diary.R;
import com.chenpan.heart.diary.view.imagefilter.AutoAdjustFilter;
import com.chenpan.heart.diary.view.imagefilter.BannerFilter;
import com.chenpan.heart.diary.view.imagefilter.BigBrotherFilter;
import com.chenpan.heart.diary.view.imagefilter.BlackWhiteFilter;
import com.chenpan.heart.diary.view.imagefilter.BlindFilter;
import com.chenpan.heart.diary.view.imagefilter.BlockPrintFilter;
import com.chenpan.heart.diary.view.imagefilter.BrickFilter;
import com.chenpan.heart.diary.view.imagefilter.BrightContrastFilter;
import com.chenpan.heart.diary.view.imagefilter.BulgeFilter;
import com.chenpan.heart.diary.view.imagefilter.CleanGlassFilter;
import com.chenpan.heart.diary.view.imagefilter.ColorQuantizeFilter;
import com.chenpan.heart.diary.view.imagefilter.ColorToneFilter;
import com.chenpan.heart.diary.view.imagefilter.ComicFilter;
import com.chenpan.heart.diary.view.imagefilter.EdgeFilter;
import com.chenpan.heart.diary.view.imagefilter.FeatherFilter;
import com.chenpan.heart.diary.view.imagefilter.FillPatternFilter;
import com.chenpan.heart.diary.view.imagefilter.FilmFilter;
import com.chenpan.heart.diary.view.imagefilter.FocusFilter;
import com.chenpan.heart.diary.view.imagefilter.GammaFilter;
import com.chenpan.heart.diary.view.imagefilter.GaussianBlurFilter;
import com.chenpan.heart.diary.view.imagefilter.Gradient;
import com.chenpan.heart.diary.view.imagefilter.HslModifyFilter;
import com.chenpan.heart.diary.view.imagefilter.IImageFilter;
import com.chenpan.heart.diary.view.imagefilter.IllusionFilter;
import com.chenpan.heart.diary.view.imagefilter.InvertFilter;
import com.chenpan.heart.diary.view.imagefilter.LensFlareFilter;
import com.chenpan.heart.diary.view.imagefilter.LightFilter;
import com.chenpan.heart.diary.view.imagefilter.LomoFilter;
import com.chenpan.heart.diary.view.imagefilter.MirrorFilter;
import com.chenpan.heart.diary.view.imagefilter.MistFilter;
import com.chenpan.heart.diary.view.imagefilter.MonitorFilter;
import com.chenpan.heart.diary.view.imagefilter.MosaicFilter;
import com.chenpan.heart.diary.view.imagefilter.NeonFilter;
import com.chenpan.heart.diary.view.imagefilter.NightVisionFilter;
import com.chenpan.heart.diary.view.imagefilter.NoiseFilter;
import com.chenpan.heart.diary.view.imagefilter.OilPaintFilter;
import com.chenpan.heart.diary.view.imagefilter.OldPhotoFilter;
import com.chenpan.heart.diary.view.imagefilter.PaintBorderFilter;
import com.chenpan.heart.diary.view.imagefilter.PixelateFilter;
import com.chenpan.heart.diary.view.imagefilter.PosterizeFilter;
import com.chenpan.heart.diary.view.imagefilter.RadialDistortionFilter;
import com.chenpan.heart.diary.view.imagefilter.RainBowFilter;
import com.chenpan.heart.diary.view.imagefilter.RaiseFrameFilter;
import com.chenpan.heart.diary.view.imagefilter.RectMatrixFilter;
import com.chenpan.heart.diary.view.imagefilter.ReflectionFilter;
import com.chenpan.heart.diary.view.imagefilter.ReliefFilter;
import com.chenpan.heart.diary.view.imagefilter.RippleFilter;
import com.chenpan.heart.diary.view.imagefilter.SaturationModifyFilter;
import com.chenpan.heart.diary.view.imagefilter.SceneFilter;
import com.chenpan.heart.diary.view.imagefilter.SepiaFilter;
import com.chenpan.heart.diary.view.imagefilter.SharpFilter;
import com.chenpan.heart.diary.view.imagefilter.ShiftFilter;
import com.chenpan.heart.diary.view.imagefilter.SmashColorFilter;
import com.chenpan.heart.diary.view.imagefilter.SoftGlowFilter;
import com.chenpan.heart.diary.view.imagefilter.SupernovaFilter;
import com.chenpan.heart.diary.view.imagefilter.ThreeDGridFilter;
import com.chenpan.heart.diary.view.imagefilter.ThresholdFilter;
import com.chenpan.heart.diary.view.imagefilter.TileReflectionFilter;
import com.chenpan.heart.diary.view.imagefilter.TintFilter;
import com.chenpan.heart.diary.view.imagefilter.TwistFilter;
import com.chenpan.heart.diary.view.imagefilter.VideoFilter;
import com.chenpan.heart.diary.view.imagefilter.VignetteFilter;
import com.chenpan.heart.diary.view.imagefilter.VintageFilter;
import com.chenpan.heart.diary.view.imagefilter.WaterWaveFilter;
import com.chenpan.heart.diary.view.imagefilter.WaveFilter;
import com.chenpan.heart.diary.view.imagefilter.XRadiationFilter;
import com.chenpan.heart.diary.view.imagefilter.YCBCrLinearFilter;
import com.chenpan.heart.diary.view.imagefilter.ZoomBlurFilter;
import com.chenpan.heart.diary.view.imagefilter.interfaces.CloudsTexture;
import com.chenpan.heart.diary.view.imagefilter.interfaces.LabyrinthTexture;
import com.chenpan.heart.diary.view.imagefilter.interfaces.MarbleTexture;
import com.chenpan.heart.diary.view.imagefilter.interfaces.TextileTexture;
import com.chenpan.heart.diary.view.imagefilter.interfaces.TexturerFilter;
import com.chenpan.heart.diary.view.imagefilter.interfaces.WoodTexture;
import com.chenpan.heart.diary.view.imagefilter.ndk.MylearnFiter;
import com.chenpan.heart.diary.view.imagefilter.ndk.MylearnFiter.MY_TYPE;
/**
 * 图片渲染adapter
 * @author Administrator
 *
 */
public class ImageFilterAdapter extends BaseAdapter {
		private class FilterInfo {
			public int filterID;
			public IImageFilter filter;

			public FilterInfo(int filterID, IImageFilter filter) {
				this.filterID = filterID;
				this.filter = filter;
			}
		}

		private Activity mContext;
		private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();

		public ImageFilterAdapter(Context c) {
			mContext = (Activity) c;
			
			//99种效果
			filterArray.add(new FilterInfo(R.drawable.f_white4, new MylearnFiter(MY_TYPE.MY_ZERO,true)));
			filterArray.add(new FilterInfo(R.drawable.f_ice1, new MylearnFiter(MY_TYPE.MY_ONE,true)));
			filterArray.add(new FilterInfo(R.drawable.f_fire2, new MylearnFiter(MY_TYPE.MY_TWO,true)));
			filterArray.add(new FilterInfo(R.drawable.f_com3, new MylearnFiter(MY_TYPE.MY_THREE,true)));
			//羽化美白
		//	filterArray.add(new FilterInfo(R.drawable.f_white4, new MylearnFiter(MY_TYPE.MY_FOUR,true)));
			filterArray.add(new FilterInfo(R.drawable.f_diaok5, new MylearnFiter(MY_TYPE.MY_FOUR,true)));
			filterArray.add(new FilterInfo(R.drawable.f_yuhua6, new MylearnFiter(MY_TYPE.MY_FIVE,true)));
			//filterArray.add(new FilterInfo(R.drawable.f_mohu7, new MylearnFiter(MY_TYPE.MY_SIX,true)));
			filterArray.add(new FilterInfo(R.drawable.f_huihua10, new MylearnFiter(MY_TYPE.MY_NINE,true)));
			//filterArray.add(new FilterInfo(R.drawable.f_lomo8, new MylearnFiter(MY_TYPE.MY_SEVEN,true)));
			//filterArray.add(new FilterInfo(R.drawable.f_movie9, new MylearnFiter(MY_TYPE.MY_EIGHT,true)));
			//v0.4 
			filterArray.add(new FilterInfo(R.drawable.video_filter1, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_STAGGERED)));
			filterArray.add(new FilterInfo(R.drawable.video_filter2, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_TRIPED)));
			filterArray.add(new FilterInfo(R.drawable.video_filter3, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_3X3)));
			filterArray.add(new FilterInfo(R.drawable.video_filter4, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_DOTS)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter1, new TileReflectionFilter(20, 8, 45, (byte)1)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter2, new TileReflectionFilter(20, 8, 45, (byte)2)));
			filterArray.add(new FilterInfo(R.drawable.fillpattern_filter, new FillPatternFilter(mContext, R.drawable.texture1)));
			filterArray.add(new FilterInfo(R.drawable.fillpattern_filter1, new FillPatternFilter(mContext, R.drawable.texture2)));
			filterArray.add(new FilterInfo(R.drawable.mirror_filter1, new MirrorFilter(true)));
			filterArray.add(new FilterInfo(R.drawable.mirror_filter2, new MirrorFilter(false)));
			filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.3f, 0.3f))));
			filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter2, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.276f, 0.163f), new YCBCrLinearFilter.Range(-0.202f, 0.5f))));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter, new TexturerFilter(new CloudsTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter1, new TexturerFilter(new LabyrinthTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter2, new TexturerFilter(new MarbleTexture(), 1.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter3, new TexturerFilter(new WoodTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter4, new TexturerFilter(new TextileTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter, new HslModifyFilter(20f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter0, new HslModifyFilter(40f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter1, new HslModifyFilter(60f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter2, new HslModifyFilter(80f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter3, new HslModifyFilter(100f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter4, new HslModifyFilter(150f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter5, new HslModifyFilter(200f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter6, new HslModifyFilter(250f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter7, new HslModifyFilter(300f)));
			
			//v0.3  
			filterArray.add(new FilterInfo(R.drawable.zoomblur_filter, new ZoomBlurFilter(30)));
			filterArray.add(new FilterInfo(R.drawable.threedgrid_filter, new ThreeDGridFilter(16, 100)));
			filterArray.add(new FilterInfo(R.drawable.colortone_filter, new ColorToneFilter(Color.rgb(33, 168, 254), 192)));
			filterArray.add(new FilterInfo(R.drawable.colortone_filter2, new ColorToneFilter(0x00FF00, 192)));//green
			filterArray.add(new FilterInfo(R.drawable.colortone_filter3, new ColorToneFilter(0xFF0000, 192)));//blue
			filterArray.add(new FilterInfo(R.drawable.colortone_filter4, new ColorToneFilter(0x00FFFF, 192)));//yellow
			filterArray.add(new FilterInfo(R.drawable.softglow_filter, new SoftGlowFilter(10, 0.1f, 0.1f)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter, new TileReflectionFilter(20, 8)));
			filterArray.add(new FilterInfo(R.drawable.blind_filter1, new BlindFilter(true, 96, 100, 0xffffff)));
			filterArray.add(new FilterInfo(R.drawable.blind_filter2, new BlindFilter(false, 96, 100, 0x000000)));
			filterArray.add(new FilterInfo(R.drawable.raiseframe_filter, new RaiseFrameFilter(20)));
			filterArray.add(new FilterInfo(R.drawable.shift_filter, new ShiftFilter(10)));
			filterArray.add(new FilterInfo(R.drawable.wave_filter, new WaveFilter(25, 10)));
			filterArray.add(new FilterInfo(R.drawable.bulge_filter, new BulgeFilter(-97)));
			filterArray.add(new FilterInfo(R.drawable.twist_filter, new TwistFilter(27, 106)));
			filterArray.add(new FilterInfo(R.drawable.ripple_filter, new RippleFilter(38, 15, true)));
			filterArray.add(new FilterInfo(R.drawable.illusion_filter, new IllusionFilter(3)));
			filterArray.add(new FilterInfo(R.drawable.supernova_filter, new SupernovaFilter(0x00FFFF,20,100)));
			filterArray.add(new FilterInfo(R.drawable.lensflare_filter, new LensFlareFilter()));
			filterArray.add(new FilterInfo(R.drawable.posterize_filter, new PosterizeFilter(2)));
			filterArray.add(new FilterInfo(R.drawable.gamma_filter, new GammaFilter(50)));
			filterArray.add(new FilterInfo(R.drawable.sharp_filter, new SharpFilter()));
			
			//v0.2
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new ComicFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene())));//green
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene1())));//purple
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene2())));//blue
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene3())));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new FilmFilter(80f)));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new FocusFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new CleanGlassFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FF00)));//green
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FFFF)));//yellow
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0xFF0000)));//blue
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new LomoFilter()));
			
			//v0.1
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new InvertFilter()));
			filterArray.add(new FilterInfo(R.drawable.blackwhite_filter, new BlackWhiteFilter()));
			filterArray.add(new FilterInfo(R.drawable.edge_filter, new EdgeFilter()));
			filterArray.add(new FilterInfo(R.drawable.pixelate_filter, new PixelateFilter()));
			filterArray.add(new FilterInfo(R.drawable.neon_filter, new NeonFilter()));
			filterArray.add(new FilterInfo(R.drawable.bigbrother_filter, new BigBrotherFilter()));
			filterArray.add(new FilterInfo(R.drawable.monitor_filter, new MonitorFilter()));
			filterArray.add(new FilterInfo(R.drawable.relief_filter, new ReliefFilter()));
			filterArray.add(new FilterInfo(R.drawable.brightcontrast_filter,new BrightContrastFilter()));
			filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,	new SaturationModifyFilter()));
			filterArray.add(new FilterInfo(R.drawable.threshold_filter,	new ThresholdFilter()));
			filterArray.add(new FilterInfo(R.drawable.noisefilter,	new NoiseFilter()));
			filterArray.add(new FilterInfo(R.drawable.banner_filter1, new BannerFilter(10, true)));
			filterArray.add(new FilterInfo(R.drawable.banner_filter2, new BannerFilter(10, false)));
			filterArray.add(new FilterInfo(R.drawable.rectmatrix_filter, new RectMatrixFilter()));
			filterArray.add(new FilterInfo(R.drawable.blockprint_filter, new BlockPrintFilter()));
			filterArray.add(new FilterInfo(R.drawable.brick_filter,	new BrickFilter()));
			filterArray.add(new FilterInfo(R.drawable.gaussianblur_filter,	new GaussianBlurFilter()));
			filterArray.add(new FilterInfo(R.drawable.light_filter,	new LightFilter()));
			filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MistFilter()));
			filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MosaicFilter()));
			filterArray.add(new FilterInfo(R.drawable.oilpaint_filter,	new OilPaintFilter()));
			filterArray.add(new FilterInfo(R.drawable.radialdistortion_filter,new RadialDistortionFilter()));
			filterArray.add(new FilterInfo(R.drawable.reflection1_filter,new ReflectionFilter(true)));
			filterArray.add(new FilterInfo(R.drawable.reflection2_filter,new ReflectionFilter(false)));
			filterArray.add(new FilterInfo(R.drawable.saturationmodify_filter,	new SaturationModifyFilter()));
			filterArray.add(new FilterInfo(R.drawable.smashcolor_filter,new SmashColorFilter()));
			filterArray.add(new FilterInfo(R.drawable.tint_filter,	new TintFilter()));
			filterArray.add(new FilterInfo(R.drawable.vignette_filter,	new VignetteFilter()));
			filterArray.add(new FilterInfo(R.drawable.autoadjust_filter,new AutoAdjustFilter()));
			filterArray.add(new FilterInfo(R.drawable.colorquantize_filter,	new ColorQuantizeFilter()));
			filterArray.add(new FilterInfo(R.drawable.waterwave_filter,	new WaterWaveFilter()));
			filterArray.add(new FilterInfo(R.drawable.vintage_filter,new VintageFilter()));
			filterArray.add(new FilterInfo(R.drawable.oldphoto_filter,new OldPhotoFilter()));
			filterArray.add(new FilterInfo(R.drawable.sepia_filter,	new SepiaFilter()));
			filterArray.add(new FilterInfo(R.drawable.rainbow_filter,new RainBowFilter()));
			filterArray.add(new FilterInfo(R.drawable.feather_filter,new FeatherFilter()));
			filterArray.add(new FilterInfo(R.drawable.xradiation_filter,new XRadiationFilter()));
			filterArray.add(new FilterInfo(R.drawable.nightvision_filter,new NightVisionFilter()));

			//filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,null/* 此处会生成原图效果 */));
		}

		public int getCount() {
			return filterArray.size();
		}

		public Object getItem(int position) {
			return position < filterArray.size() ? filterArray.get(position).filter
					: null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Bitmap bmImg = BitmapFactory
					.decodeResource(mContext.getResources(),
							filterArray.get(position).filterID);
		//	int width = 100;// bmImg.getWidth();
		//	int height = 100;// bmImg.getHeight();
			bmImg.recycle();
			final ViewHolder holder ;
			if(convertView == null){
    			holder = new ViewHolder();
    			convertView = View.inflate(mContext, R.layout.image_item, null);
    			holder.image = (ImageView) convertView.findViewById(R.id.image);
    			convertView.setTag(holder);
    		}else {
    			holder = (ViewHolder) convertView.getTag();
    		}
			holder.image.setImageResource(filterArray.get(position).filterID);
		//	holder.image.setLayoutParams(new Gallery.LayoutParams(width, height));
			holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置显示比例类型
			return convertView;
		}
	};
	class ViewHolder{
    	ImageView image;
    }
