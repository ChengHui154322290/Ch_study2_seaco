package com.tp.model.wx;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.wx.MessageConstant;
import com.tp.model.BaseDO;

public class MessageInfo extends BaseDO implements Serializable{

	private static final long serialVersionUID = 5939891907634365882L;

	/** 数据类型int(11)*/
	@Id
	private Integer id;
	
	private String code;
	
	private String name;
	
	private String cKey;
	
	private String type;
	
	private String content;
	
	private Integer status;
	
	private Integer isDel;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	public String getSceneDesc(){
		return MessageConstant.SCENE.getDesc(code);
	}
	
	public String getTypeDesc(){
		return MessageConstant.TYPE.getDesc(type);
	}
	
	public String getStatusDesc(){
		return MessageConstant.STATUS.getDesc(status);
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
}
