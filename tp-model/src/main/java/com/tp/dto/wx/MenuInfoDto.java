package com.tp.dto.wx;

import java.io.Serializable;
import java.util.List;

import com.tp.model.wx.MenuInfo;

public class MenuInfoDto implements Serializable{

	private static final long serialVersionUID = 7298669297851861429L;

	private Integer id;
	
	private Integer pid;
	
	private String name;
	
	private String cKey;
	
	private String type;
	
	private String vUrl;
	
	private boolean open = true;
	
	private List<MenuInfoDto> children;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getcKey() {
		return cKey;
	}

	public void setcKey(String cKey) {
		this.cKey = cKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getvUrl() {
		return vUrl;
	}

	public void setvUrl(String vUrl) {
		this.vUrl = vUrl;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public List<MenuInfoDto> getChildren() {
		return children;
	}

	public void setChildren(List<MenuInfoDto> children) {
		this.children = children;
	}
	
	public void toDto(MenuInfo menuInfo){
		this.id =menuInfo.getId();
		this.cKey =menuInfo.getcKey();
		this.name =menuInfo.getName();
		this.pid =menuInfo.getPid();
		this.type =menuInfo.getType();
		this.vUrl =menuInfo.getvUrl();
	}
}
