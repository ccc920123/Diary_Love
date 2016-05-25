package com.cdjysdkj.diary.network;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2016/5/19.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> cache;
    public int max=10*1024*1024;//10M
    @SuppressLint("NewApi")
	public  BitmapCache(){
        cache=new LruCache<String,Bitmap>(max){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getWidth()*value.getHeight();
            }
        };
    }

    @SuppressLint("NewApi")
	@Override
    public Bitmap getBitmap(String url) {
        return cache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
    }
}
