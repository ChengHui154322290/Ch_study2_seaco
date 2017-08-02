package com.tp.result.sup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tp.model.sup.SupplierAttach;
import com.tp.model.sup.SupplierBankAccount;
import com.tp.model.sup.SupplierBrand;
import com.tp.model.sup.SupplierCategory;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierInvoice;
import com.tp.model.sup.SupplierLink;
import com.tp.model.sup.SupplierUser;
import com.tp.model.sup.SupplierWarehouse;
import com.tp.model.sup.SupplierXgLink;

public class SupplierDTO extends SupplierInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8480852130709721186L;
	 /** 供应商品牌 */
    private List<SupplierBrand> supplierBrandList;

    /** 供应商品类 */
    private List<SupplierCategory> supplierCategoryList;
    /** 供应商品类 */
    private List<SupplierCategory> uniSupplierCategoryList;
    /** 供应商附件 */
    private SupplierAttach supplierAttach;

    /** 供应商联系人 */
    private List<SupplierLink> supplierLinkList;

    /** 西客联系人 */
    private List<SupplierXgLink> supplierXgLinkList;

    /** 银行账户信息 */
    private List<SupplierBankAccount> supplierBankAccountList;

    /** 海关备案号信息 */
    private List<SupplierCustomsRecordation> supplierCustomsRecordationList;
    
	/** 供应商开票信息 */
    private List<SupplierInvoice> supplierInvoiceList;

    /** 供应商仓库 */
    private List<SupplierWarehouse> supplierWarehouseList;

    /** 商家平台 */
    private SupplierUser supplierUser;

	public List<SupplierBrand> getSupplierBrandList() {
		return supplierBrandList;
	}

	public void setSupplierBrandList(List<SupplierBrand> supplierBrandList) {
		this.supplierBrandList = supplierBrandList;
	}

	public List<SupplierCategory> getSupplierCategoryList() {
		return supplierCategoryList;
	}

	public void setSupplierCategoryList(List<SupplierCategory> supplierCategoryList) {
		this.supplierCategoryList = supplierCategoryList;
	}

	public SupplierAttach getSupplierAttach() {
		return supplierAttach;
	}

	public void setSupplierAttach(SupplierAttach supplierAttach) {
		this.supplierAttach = supplierAttach;
	}

	public List<SupplierLink> getSupplierLinkList() {
		return supplierLinkList;
	}

	public void setSupplierLinkList(List<SupplierLink> supplierLinkList) {
		this.supplierLinkList = supplierLinkList;
	}

	public List<SupplierXgLink> getSupplierXgLinkList() {
		return supplierXgLinkList;
	}

	public void setSupplierXgLinkList(List<SupplierXgLink> supplierXgLinkList) {
		this.supplierXgLinkList = supplierXgLinkList;
	}

	public List<SupplierBankAccount> getSupplierBankAccountList() {
		return supplierBankAccountList;
	}

	public void setSupplierBankAccountList(
			List<SupplierBankAccount> supplierBankAccountList) {
		this.supplierBankAccountList = supplierBankAccountList;
	}

	public List<SupplierCustomsRecordation> getSupplierCustomsRecordationList() {
		return supplierCustomsRecordationList;
	}

	public void setSupplierCustomsRecordationList(
			List<SupplierCustomsRecordation> supplierCustomsRecordationList) {
		this.supplierCustomsRecordationList = supplierCustomsRecordationList;
	}

	public List<SupplierInvoice> getSupplierInvoiceList() {
		return supplierInvoiceList;
	}

	public void setSupplierInvoiceList(List<SupplierInvoice> supplierInvoiceList) {
		this.supplierInvoiceList = supplierInvoiceList;
	}

	public List<SupplierWarehouse> getSupplierWarehouseList() {
		return supplierWarehouseList;
	}

	public void setSupplierWarehouseList(
			List<SupplierWarehouse> supplierWarehouseList) {
		this.supplierWarehouseList = supplierWarehouseList;
	}

	public SupplierUser getSupplierUser() {
		return supplierUser;
	}

	public void setSupplierUser(SupplierUser supplierUser) {
		this.supplierUser = supplierUser;
	}

	public List<SupplierCategory> getUniSupplierCategoryList() {
    	uniSupplierCategoryList = new ArrayList<SupplierCategory>();
    	if(null != supplierCategoryList && supplierCategoryList.size()>0){
    		Set<Long> brandId = new HashSet<Long>();
    		for(SupplierCategory category : supplierCategoryList){
    			if(!brandId.contains(category.getBrandId())){
    				uniSupplierCategoryList.add(category);
    				brandId.add(category.getBrandId());
    			}
    		}
    	}
		return uniSupplierCategoryList;
	}

	public void setUniSupplierCategoryList(
			List<SupplierCategory> uniSupplierCategoryList) {
		this.uniSupplierCategoryList = uniSupplierCategoryList;
	}
}
