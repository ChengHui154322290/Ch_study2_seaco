package com.tp.dto.ptm.remote;

import java.io.Serializable;

/**
 * 账户列表DTO
 * 
 * @author 项硕
 * @version 2015年5月7日 下午3:51:58
 */
public class Account4ListDTO implements Serializable {

	private static final long serialVersionUID = 823478536517129535L;

	private AccountDTO account;
	
	public Account4ListDTO() {}
	public Account4ListDTO(AccountDTO account) {
		this.account = account;
	}
	
	public AccountDTO getAccount() {
		return account;
	}
	public void setAccount(AccountDTO account) {
		this.account = account;
	}
}
