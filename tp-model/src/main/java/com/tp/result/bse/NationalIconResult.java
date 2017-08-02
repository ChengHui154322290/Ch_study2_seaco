package com.tp.result.bse;

import java.io.Serializable;

import com.tp.model.bse.NationalIcon;

/**
 * 
 * <pre>
 *  国家名称及其图标封装类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class NationalIconResult implements Serializable {

	private static final long serialVersionUID = -2874326253114994651L;
	/**
	 * 国家名称
	 */
	private String nationalName;
	
	private NationalIcon nationalIconDO;

	public String getNationalName() {
		return nationalName;
	}

	public void setNationalName(String nationalName) {
		this.nationalName = nationalName;
	}

	public NationalIcon getNationalIconDO() {
		return nationalIconDO;
	}

	public void setNationalIconDO(NationalIcon nationalIconDO) {
		this.nationalIconDO = nationalIconDO;
	}

}
