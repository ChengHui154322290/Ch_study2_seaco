package com.tp.common.vo.wms.kjb2c;


public class KjsoDeclRequestItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1263779709323771626L;
//	@NotEmpty
	private String sku;
//	@NotEmpty
	private String declNo;
	// @NotEmpty
	private String manifestNo;
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getDeclNo() {
		return declNo;
	}
	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}
	public String getManifestNo() {
		return manifestNo;
	}
	public void setManifestNo(String manifestNo) {
		this.manifestNo = manifestNo;
	}
}
