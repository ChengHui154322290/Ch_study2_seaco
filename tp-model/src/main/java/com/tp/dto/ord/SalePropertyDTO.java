package com.tp.dto.ord;


/**
 * 销售属性DTO
 *
 * @author szy
 * @version 0.0.1
 */
public class SalePropertyDTO implements BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8663135899860477709L;

	/** 规格名称（eg.红色、XL） */
	private String specName;
	
	/** 规格组名称（eg.颜色、尺码） */
	private String specGroupName;

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getSpecGroupName() {
		return specGroupName;
	}

	public void setSpecGroupName(String specGroupName) {
		this.specGroupName = specGroupName;
	}
	
	
}
