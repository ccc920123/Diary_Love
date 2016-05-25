package com.cdjysdkj.diary.bean;

public class TelBean {
	/*error_code	int	返回码
 	reason	string	返回说明
 	result	string	返回结果集
 	province	string	省份
 	city	string	城市
 	areacode	string	区号
 	zip	string	邮编
 	company	string	运营商
 	card	string	卡类型*/
	
	private int error_code;
	private String reason;
	private String result;
	private String province;
	private String city;
	private String areacode;
	private String zip;
	private String company;
	private String card;
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	
}
