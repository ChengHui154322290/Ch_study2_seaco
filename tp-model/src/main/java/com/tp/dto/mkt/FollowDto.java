package com.tp.dto.mkt;

import java.io.Serializable;

public class FollowDto implements Serializable{
    
	private static final long serialVersionUID = -3054613663763716900L;

	private String unique_id;
	
	private Integer is_follow;

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	public Integer getIs_follow() {
		return is_follow;
	}

	public void setIs_follow(Integer is_follow) {
		this.is_follow = is_follow;
	}
}
