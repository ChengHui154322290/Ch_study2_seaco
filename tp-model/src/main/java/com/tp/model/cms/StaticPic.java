package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 活动图片信息表
  */
public class StaticPic extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451849L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**图片名称 数据类型varchar(50)*/
	private String name;
	
	/**图片路径 数据类型varchar(128)*/
	private String path;
	
	/**链接 数据类型varchar(50)*/
	private String link;
	
	/** 数据类型varchar(128)*/
	private String area;
	
	/**自定义 数据类型varchar(3000)*/
	private String defined;
	
	/**变量表主键 数据类型bigint(20)*/
	private Long variableId;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getPath(){
		return path;
	}
	public String getLink(){
		return link;
	}
	public String getArea(){
		return area;
	}
	public String getDefined(){
		return defined;
	}
	public Long getVariableId(){
		return variableId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPath(String path){
		this.path=path;
	}
	public void setLink(String link){
		this.link=link;
	}
	public void setArea(String area){
		this.area=area;
	}
	public void setDefined(String defined){
		this.defined=defined;
	}
	public void setVariableId(Long variableId){
		this.variableId=variableId;
	}
}
