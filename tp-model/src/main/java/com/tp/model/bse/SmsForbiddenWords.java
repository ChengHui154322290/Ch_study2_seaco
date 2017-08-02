package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 短信违禁词
  */
public class SmsForbiddenWords extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786420L;

	/**短信违禁词表ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**短信违禁词编号 数据类型varchar(50)*/
	private String code;
	
	/** 数据类型tinyint(4)*/
	private Integer type;
	
	/**违禁词 数据类型varchar(255)*/
	private String words;
	
	/**状态:0-无效, 1-有效 数据类型tinyint(4)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createdTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	
	public Long getId(){
		return id;
	}
	public String getCode(){
		return code;
	}
	public Integer getType(){
		return type;
	}
	public String getWords(){
		return words;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setWords(String words){
		this.words=words;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreatedTime(Date createdTime){
		this.createdTime=createdTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
