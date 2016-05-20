package com.cdjysdkj.diary.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class Images {
	 /**
     * 获取SD卡某路径下的所有图片
     * @param strPath
     * @return
     */
    @SuppressLint("DefaultLocale")
	public   List<String> getPictures(final String strPath) { 
        List<String> list = new ArrayList<String>(); 
        File file = new File(strPath); 
        File[] allfiles = file.listFiles(); 
        if (allfiles == null) { 
          return null; 
        } 
        for(int k = 0; k < allfiles.length; k++) { 
          final File fi = allfiles[k]; 
          if(fi.isFile()) { 
                  int idx = fi.getPath().lastIndexOf("."); 
                  if (idx <= 0) { 
                      continue; 
                  } 
                  String suffix = fi.getPath().substring(idx); 
                  if (suffix.toLowerCase().equals(".jpg") || 
                      suffix.toLowerCase().equals(".jpeg") || 
                      suffix.toLowerCase().equals(".bmp") || 
                      suffix.toLowerCase().equals(".png") || 
                      suffix.toLowerCase().equals(".gif") ) { 
                      list.add(fi.getPath()); 
                  } 
          } 
       } 
       return list; 
     }
}
