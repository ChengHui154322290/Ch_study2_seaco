package com.tp.dto.prd;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * <pre>
 * 商品的简略信息分装类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 *          $
 */
public class SimpleDetailOpenDto implements Serializable {

	private static final long serialVersionUID = -5956987401698510789L;

	private Long id;

	/** 条码 */
	private String barcode;

	/** 编号 spu+3位随机码 */
	private String prdid;

	/** 商品名称 */
	private String itemTitle;

	/** 规格的信息 */
	private Map<String, Map<String, String>> specInfo;
	
	/** 状态 **/
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public Map<String, Map<String, String>> getSpecInfo() {
		return specInfo;
	}

	public void setSpecInfo(Map<String, Map<String, String>> specInfo) {
		this.specInfo = specInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
