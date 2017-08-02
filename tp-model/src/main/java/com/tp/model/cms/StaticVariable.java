package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 活动模板变量表
  */
public class StaticVariable extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451849L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**模板表主键 数据类型bigint(11)*/
	private Long templetId;
	
	/**变量名(中文) 数据类型varchar(50)*/
	private String variableChName;
	
	/**变量名(英文) 数据类型varchar(50)*/
	private String variableEnName;
	
	/**变量类型(如图片，文本等) 数据类型tinyint(2)*/
	private Integer type;
	
	
	public Long getId(){
		return id;
	}
	public Long getTempletId(){
		return templetId;
	}
	public String getVariableChName(){
		return variableChName;
	}
	public String getVariableEnName(){
		return variableEnName;
	}
	public Integer getType(){
		return type;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTempletId(Long templetId){
		this.templetId=templetId;
	}
	public void setVariableChName(String variableChName){
		this.variableChName=variableChName;
	}
	public void setVariableEnName(String variableEnName){
		this.variableEnName=variableEnName;
	}
	public void setType(Integer type){
		this.type=type;
	}
}
