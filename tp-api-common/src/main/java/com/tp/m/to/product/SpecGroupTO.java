package com.tp.m.to.product;

import java.util.List;

import com.tp.m.base.BaseTO;

/**
 * 规格
 * @author zhuss
 * @2016年1月5日 下午3:26:22
 */
public class SpecGroupTO implements BaseTO{

	private static final long serialVersionUID = -2845663394158795503L;

	private String groupid;
	private String groupname;
	private List<SpecGroupDetailTO> groupdetails;
	
	
	public SpecGroupTO() {
		super();
	}
	public SpecGroupTO(String groupid, String groupname,
			List<SpecGroupDetailTO> groupdetails) {
		super();
		this.groupid = groupid;
		this.groupname = groupname;
		this.groupdetails = groupdetails;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public List<SpecGroupDetailTO> getGroupdetails() {
		return groupdetails;
	}
	public void setGroupdetails(List<SpecGroupDetailTO> groupdetails) {
		this.groupdetails = groupdetails;
	}
}
