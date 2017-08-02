/**
 * 
 */
package com.tp.dto.cms.app;

import java.io.Serializable;
/**
 * code的实体类
 * @author szy
 *
 */
public class CodeBeanDTO implements Serializable {

	private static final long serialVersionUID = -1620952831373453144L;

	private String resCode;

	private String info;

	public CodeBeanDTO(String resCode, String info) {
		this.resCode = resCode;
		this.info = info;
	}
	
	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	
}
