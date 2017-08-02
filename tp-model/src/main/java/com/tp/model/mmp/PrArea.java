package com.tp.model.mmp;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 区域
  */
public class PrArea extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579108L;

	/** 数据类型int(11)*/
	@Id
	private Integer id;
	
	/**地区名 数据类型varchar(20)*/
	private String name;
	
	
	public Integer getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
}
