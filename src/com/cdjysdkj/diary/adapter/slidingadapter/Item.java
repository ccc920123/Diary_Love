package com.cdjysdkj.diary.adapter.slidingadapter;

/**
 * Created by zhouweilong on 15/12/14.
 */
public class Item {
    private String name;
    /**
     * 时间
     */
    private String dateName;
    private boolean isSelected;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

	/**
	 * @return the dateName时间
	 */
	public String getDateName() {
		return dateName;
	}

	/**
	 * @param dateName the dateName to set 时间
	 */
	public void setDateName(String dateName) {
		this.dateName = dateName;
	}
    
}
