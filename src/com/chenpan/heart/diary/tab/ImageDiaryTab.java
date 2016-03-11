package com.chenpan.heart.diary.tab;

import com.j256.ormlite.field.DatabaseField;

public class ImageDiaryTab {
	@DatabaseField(generatedId = true)
	private int ID;
	/**
	 * Í¼Æ¬Â·¾¶
	 */
	@DatabaseField
	private String imagePath = "";
	/**
	 * ÎÄ×ÖÃèÊö
	 */
	@DatabaseField
	private String describe = "";
	/**
	 * @param imagePath
	 * @param describe
	 */
	public ImageDiaryTab(String imagePath, String describe) {
		super();
		this.imagePath = imagePath;
		this.describe = describe;
	}
	/**
	 * 
	 */
	public ImageDiaryTab() {
		super();
	}
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}
	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}
	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	/**
	 * @return the describe
	 */
	public String getDescribe() {
		return describe;
	}
	/**
	 * @param describe the describe to set
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
}
