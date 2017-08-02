package com.tp.dto.mem;

import java.io.Serializable;

/**
 * 
 * <pre>
 *  手机端 用户证件号码和用户姓名 dto
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class CertificateDto implements Serializable {
	
	private static final long serialVersionUID = 1292333309686932220L;

	/** 用户姓名 */
	private String userName;
	
	/** 证件号码 */
	private String idCard;
	
	/** 证件类型 */
	private Integer idType;
	
	private String picA;
	
	/**实名验证照片b 数据类型varchar(200)*/
	private String picB;
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Integer getIdType() {
		return idType;
	}

	public void setIdType(Integer idType) {
		this.idType = idType;
	}

	public String getPicA() {
		return picA;
	}

	public void setPicA(String picA) {
		this.picA = picA;
	}

	public String getPicB() {
		return picB;
	}

	public void setPicB(String picB) {
		this.picB = picB;
	}
}
