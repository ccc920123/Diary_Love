package com.cdjysdkj.diary.adapter.slidingadapter;

/**
 * 
 */
public class Item {
    private String name;
    /**
     * ʱ��
     */
    private String dateName;
    
    private int image;
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
	 * @return the dateNameʱ��
	 */
	public String getDateName() {
		return dateName;
	}

	/**
	 * @param dateName the dateName to set ʱ��
	 */
	public void setDateName(String dateName) {
		this.dateName = dateName;
	}

	/**
	 * @return the image#{bare_field_comment}
	 */
	public int getImage() {
		return image;
	}

	/**
	 * @param image the image to set #{bare_field_comment}
	 */
	public void setImage(int image) {
		this.image = image;
	}
    
}
