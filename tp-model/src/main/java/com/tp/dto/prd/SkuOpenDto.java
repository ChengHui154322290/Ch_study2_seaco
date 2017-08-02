package com.tp.dto.prd;

import java.io.Serializable;

/***
 * sku 对外平台 sku信息
 *
 */
public class SkuOpenDto  implements Serializable{
	private static final long serialVersionUID = 3436351629121967428L;
	/**skucode**/
	private String sku;
	/**主图**/
	private String mainPicture;
	/**条形码**/
	private String barCode;
	/**名称**/
	private String name;
	/**状态**/
	private String status;
	/**detailId**/
	private Long detailId;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getMainPicture() {
		return mainPicture;
	}

	public void setMainPicture(String mainPicture) {
		this.mainPicture = mainPicture;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

}
