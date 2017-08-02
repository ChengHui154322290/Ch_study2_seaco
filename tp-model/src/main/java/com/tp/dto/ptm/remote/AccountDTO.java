package com.tp.dto.ptm.remote;

import org.springframework.beans.BeanUtils;

import com.tp.common.vo.ptm.AccountConstant.AccountStatus;
import com.tp.model.ptm.PlatformAccount;

/**
 * 开放平台账户DTO
 * 
 * @author 项硕
 * @version 2015年5月7日 下午3:51:58
 */
public class AccountDTO extends PlatformAccount {

	private static final long serialVersionUID = 823478536517129535L;
	
	/**
	 * 账户状态中文释义
	 * 
	 * @return
	 */
	public String getStatusStr() {
		return AccountStatus.getCnName(getStatus());
	}
	
	/**
	 * DO转DTO
	 * 
	 * @param account
	 * @return
	 */
	public static AccountDTO toDTO(PlatformAccount account) {
		if (null != account) {
			AccountDTO dto = new AccountDTO();
			BeanUtils.copyProperties(account, dto);
			return dto;
		}
		return null;
	}
}
