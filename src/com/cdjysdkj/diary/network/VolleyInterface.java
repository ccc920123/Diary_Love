package com.cdjysdkj.diary.network;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2016/5/19.
 */
public abstract class VolleyInterface {
    private Context context;
    public static Response.Listener<String> mListener;
    public static Response.Listener<JSONObject> mJsonListener;
    public static Response.ErrorListener mErrorListener;

    public VolleyInterface(Context context, Response.Listener<String> lisener, Response.ErrorListener errorListener) {
        this.context = context;
        mListener = lisener;
        mErrorListener = errorListener;
    }
    public VolleyInterface(Context context) {
        this.context = context;

    }

    public VolleyInterface() {
    }
/**
 * String 
 * @return
 */
    public Response.Listener<String>  loadingListener(){
        mListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onMySuccess(response);
            }
        };
        return mListener;
    }
    /**
     * json
     * @return
     */
    public Response.Listener<JSONObject>  loadingJsonListener(){
    	mJsonListener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onMySuccess(response);
            }
        };
        return mJsonListener;
    }
    public Response.ErrorListener errorListener(){
        mErrorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onMyError(error);
            }
        };
        return mErrorListener;
    }
    public  abstract void onMySuccess(String result);
    public  abstract void onMySuccess(JSONObject result);
    public  abstract void onMyError(VolleyError result);
}
