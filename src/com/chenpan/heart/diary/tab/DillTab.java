package com.chenpan.heart.diary.tab;

import com.j256.ormlite.field.DatabaseField;

public class DillTab {
	@DatabaseField(generatedId = true)
	private int ID;
	/**
	 * 详细时间 格式2015-12-24
	 */
	@DatabaseField
	private String detailtime = "";
	/**
	 * 记录年月的时间  格式2015-12
	 */
	@DatabaseField
	private String time="";
	/**
	 * 账户类型  信用卡/现金
	 */
	@DatabaseField
	private String accountType="";
	/**
	 * 花费类型 收入/支出  1是收入0是支出
	 */
	@DatabaseField
	private String takeType="";
	/**
	 * 种类  购物……
	 */
	@DatabaseField
	private String type="";
	/**
	 * 图片下标
	 */
	@DatabaseField
	private String indexImg="0";
	/**
	 * @return the indexImg
	 */
	public String getIndexImg() {
		return indexImg;
	}
	/**
	 * @param indexImg the indexImg to set
	 */
	public void setIndexImg(String indexImg) {
		this.indexImg = indexImg;
	}
	/**
	 * 金额
	 */
	@DatabaseField
	private String money="";
	/**
	 * 详细说明，用作记录具体买了说明啊这些
	 * 
	 */
	@DatabaseField
	private String declare="";
	/**
	 * @return the detailtime
	 */
	
	public String getDetailtime() {
		return detailtime;
	}
	/**
	 * @param detailtime the detailtime to set
	 */
	public void setDetailtime(String detailtime) {
		this.detailtime = detailtime;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the takeType
	 */
	public String getTakeType() {
		return takeType;
	}
	/**
	 * @param takeType the takeType to set
	 */
	public void setTakeType(String takeType) {
		this.takeType = takeType;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the money
	 */
	public String getMoney() {
		return money;
	}
	/**
	 * @param money the money to set
	 */
	public void setMoney(String money) {
		this.money = money;
	}
	/**
	 * @return the declare
	 */
	public String getDeclare() {
		return declare;
	}
	/**
	 * @param declare the declare to set
	 */
	public void setDeclare(String declare) {
		this.declare = declare;
	}
	
	
}
