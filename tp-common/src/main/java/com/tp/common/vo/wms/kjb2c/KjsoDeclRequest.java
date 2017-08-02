package com.tp.common.vo.wms.kjb2c;

import java.util.List;

import javax.validation.constraints.NotNull;


public class KjsoDeclRequest implements java.io.Serializable {

	private static final long serialVersionUID = -8563835513857419231L;
//	@NotEmpty
	private String storer;
//	@NotEmpty
	private String externalNo;
	private String externalNo2;
	@NotNull
	private Integer tdq; 
//	@NotEmpty
	private List<KjsoDeclRequestItem> items;
	public String getStorer() {
		return storer;
	}
	public void setStorer(String storer) {
		this.storer = storer;
	}
	public String getExternalNo() {
		return externalNo;
	}
	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
	public String getExternalNo2() {
		return externalNo2;
	}
	public void setExternalNo2(String externalNo2) {
		this.externalNo2 = externalNo2;
	}
	public Integer getTdq() {
		return tdq;
	}
	public void setTdq(Integer tdq) {
		this.tdq = tdq;
	}
	public List<KjsoDeclRequestItem> getItems() {
		return items;
	}
	public void setItems(List<KjsoDeclRequestItem> items) {
		this.items = items;
	}


}
