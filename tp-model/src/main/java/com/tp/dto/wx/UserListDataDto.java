package com.tp.dto.wx;

import java.io.Serializable;
import java.util.List;

public class UserListDataDto implements Serializable{

	private static final long serialVersionUID = 7683666481288611496L;

	private List<String> openid;

	public List<String> getOpenid() {
		return openid;
	}

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}
}
