package com.tp.model.wx;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;

public class MenuInfo extends BaseDO implements Serializable{

	private static final long serialVersionUID = -1636214907068031337L;

	/** 数据类型int(11)*/
	@Id
	private Integer id;
	
	private Integer pid;
	
	private String name;
	
	private String cKey;
	
	private String type;
	
	private String vUrl;
	
	private Integer isDel;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	//用于验证的数量
	@Virtual
	private Integer countByPid;
	
	/*public String getLevelDesc(){
		return MenuConstant.LEVEL.getDesc(pid);
	}
	
	public String getTypeDesc(){
		return MenuConstant.TYPE.getDesc(type);
	}*/
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getcKey() {
		return cKey;
	}

	public void setcKey(String cKey) {
		this.cKey = cKey;
	}

	public String getvUrl() {
		return vUrl;
	}

	public void setvUrl(String vUrl) {
		this.vUrl = vUrl;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getCountByPid() {
		return countByPid;
	}

	public void setCountByPid(Integer countByPid) {
		this.countByPid = countByPid;
	}
}
