package com.cdjysdkj.diary.bean;

public class GridViewItemBean {

	private int image;
	private String title;
	public GridViewItemBean(int image, String title) {
		super();
		this.image = image;
		this.title = title;
	}
	public GridViewItemBean() {
		super();
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
