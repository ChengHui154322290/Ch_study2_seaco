package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 图片类型表
  */
public class AdvertiseType extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451847L;

	/**接口标识 数据类型varchar(50)*/
	private String ident;
	
	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**类型名称 数据类型varchar(50)*/
	private String name;
	
	/**状态 数据类型varchar(10)*/
	private String status;
	
	
	public String getIdent(){
		return ident;
	}
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getStatus(){
		return status;
	}
	public void setIdent(String ident){
		this.ident=ident;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setStatus(String status){
		this.status=status;
	}
}
