package com.chenpan.heart.diary.tab;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class TextDiaryTab implements Serializable {
	@DatabaseField(generatedId = true)
	private int ID;
	/**
	 * 标题
	 */
	@DatabaseField
	private String title = "";
	/**
	 * 创建或者修改时间
	 */
	@DatabaseField
	private String creatTime = "";
	/**
	 * 文本内容
	 */
	@DatabaseField
	private String text = "";

	/**
	 * 星期
	 */
	@DatabaseField
	private String week = "";
	/**
	 * 天气
	 */
	@DatabaseField
	private String weather = "";

	/**
	 * 
	 */
	public TextDiaryTab() {
		super();
	}

	/**
	 * @param title
	 * @param creatTime
	 * @param text
	 * @param week
	 * @param weather
	 */
	public TextDiaryTab(String title, String creatTime, String text,
			String week, String weather) {
		super();
		this.title = title;
		this.creatTime = creatTime;
		this.text = text;
		this.week = week;
		this.weather = weather;
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
	 * @return the week
	 */
	public String getWeek() {
		return week;
	}

	/**
	 * @param week
	 *            the week to set
	 */
	public void setWeek(String week) {
		this.week = week;
	}

	/**
	 * @return the weather
	 */
	public String getWeather() {
		return weather;
	}

	/**
	 * @param weather
	 *            the weather to set
	 */
	public void setWeather(String weather) {
		this.weather = weather;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the creatTime
	 */
	public String getCreatTime() {
		return creatTime;
	}

	/**
	 * @param creatTime
	 *            the creatTime to set
	 */
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
