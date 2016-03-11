package com.chenpan.heart.diary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenpan.heart.diary.R;

/**
 * ╪сть╣х╢Щ©Р
 * @author Administrator
 *
 */

 
public class CustomProgressDialog extends Dialog {
	private static TextView loadingmsg;
	private Context context = null;
		    private static CustomProgressDialog customProgressDialog = null;
		     
		    public CustomProgressDialog(Context context){
		        super(context);
		        this.context = context;
		    }
		      
		    public CustomProgressDialog(Context context, int theme) {
		        super(context, theme);
		    }
		     
		    public static CustomProgressDialog createDialog(Context context,String msg){
		    	View v=LinearLayout.inflate(context, R.layout.customprogressdialog, null);
		    	loadingmsg=(TextView) v.findViewById(R.id.id_tv_loadingmsg);
		    	loadingmsg.setText(msg);
		        customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
		        customProgressDialog.setCancelable(false);
		        customProgressDialog.setContentView(v);
		        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
		        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.load);  
		        LinearInterpolator lin = new LinearInterpolator();  
		        operatingAnim.setInterpolator(lin);  
		        if (operatingAnim != null) {  
		        	imageView.startAnimation(operatingAnim);  
		        } 
		        return customProgressDialog;
		    } 
		  
		    public void onWindowFocusChanged(boolean hasFocus){
		        
		    }
		    
		    /**
		     *
		     * [Summary]
		     *       setTitile О©╫О©╫О©╫О©╫
		     * @param strTitle
		     * @return
		     *
		     */
		    public CustomProgressDialog setTitile(String strTitle){
		        return customProgressDialog;
		    }
		     
		    /**
		     *
		     * [Summary]
		     *       setMessage О©╫О©╫й╬О©╫О©╫О©╫О©╫
		     * @param strMessage
		     * @return
		     *
		     */
		    public CustomProgressDialog setMessage(String strMessage){
		        TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
		         
		        if (tvMsg != null){
		            tvMsg.setText(strMessage);
		        }
		         
		        return customProgressDialog;
	    }
}
