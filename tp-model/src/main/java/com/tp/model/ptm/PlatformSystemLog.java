package com.tp.model.ptm;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 开放平台系统日志
  */
public class PlatformSystemLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450411493008L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**日志类型（1=订单相关，2＝仓库相关） 数据类型tinyint(3)*/
	private Integer type;
	
	/**日志级别（1=info, 2=error） 数据类型tinyint(3)*/
	private Integer level;
	
	/**日志状态（0＝未处理，1=已处理） 数据类型tinyint(3)*/
	private Integer status;
	
	/**日志内容 数据类型varchar(500)*/
	private String content;
	
	/**相关编号（例如订单号、运单号） 数据类型varchar(45)*/
	private String relationCode;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public Integer getType(){
		return type;
	}
	public Integer getLevel(){
		return level;
	}
	public Integer getStatus(){
		return status;
	}
	public String getContent(){
		return content;
	}
	public String getRelationCode(){
		return relationCode;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setLevel(Integer level){
		this.level=level;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setRelationCode(String relationCode){
		this.relationCode=relationCode;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
