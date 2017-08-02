package com.tp.dto.ptm.remote;


import java.io.Serializable;
import java.util.List;

/**
 * 账户详情DTO
 * 
 * @author 项硕
 * @version 2015年5月7日 下午3:51:58
 */
public class Account4DetailDTO implements Serializable {

	private static final long serialVersionUID = 823478536517129535L;

	private AccountDTO account;
	private List<SupplierRelationDTO> supplierList;
	
	public Account4DetailDTO() {}
	public Account4DetailDTO(AccountDTO account, List<SupplierRelationDTO> supplierList) {
		this.account = account;
		this.supplierList = supplierList;
	}
	
	public AccountDTO getAccount() {
		return account;
	}
	public void setAccount(AccountDTO account) {
		this.account = account;
	}
	public List<SupplierRelationDTO> getSupplierList() {
		return supplierList;
	}
	public void setSupplierList(List<SupplierRelationDTO> supplierList) {
		this.supplierList = supplierList;
	}
}
