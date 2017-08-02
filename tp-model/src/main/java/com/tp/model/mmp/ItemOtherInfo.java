package com.tp.model.mmp;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author zhs
  * 商品国旗等信息 for dss
  */
public class ItemOtherInfo extends BaseDO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6509697259622184234L;


	private Long itemId;
	
	private String sku;
	
	private String subTitle;
	
	private String clearName;
	
	private String dirctName;
		
	// 国旗图片
	private String picPath;

	
	private Double commisionRate;

	
	public Double getCommisionRate() {
		return commisionRate;
	}

	public void setCommisionRate(Double commisionRate) {
		this.commisionRate = commisionRate;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getClearName() {
		return clearName;
	}

	public void setClearName(String clearName) {
		this.clearName = clearName;
	}

	public String getDirctName() {
		return dirctName;
	}

	public void setDirctName(String dirctName) {
		this.dirctName = dirctName;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	
	
}
