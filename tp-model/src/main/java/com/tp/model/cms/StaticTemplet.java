package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 活动模板表
  */
public class StaticTemplet extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451849L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**模板名称 数据类型varchar(50)*/
	private String templetName;
	
	/**模板类型 数据类型tinyint(2)*/
	private Integer type;
	
	/**上传模板路径名(html的上传路径) 数据类型varchar(50)*/
	private String path;
	
	/**状态(0正常，1禁用) 数据类型tinyint(1)*/
	private Integer status;
	
	
	public Long getId(){
		return id;
	}
	public String getTempletName(){
		return templetName;
	}
	public Integer getType(){
		return type;
	}
	public String getPath(){
		return path;
	}
	public Integer getStatus(){
		return status;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTempletName(String templetName){
		this.templetName=templetName;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setPath(String path){
		this.path=path;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
}
