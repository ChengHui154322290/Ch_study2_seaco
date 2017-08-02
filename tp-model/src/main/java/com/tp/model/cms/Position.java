package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 位置管理表
  */
public class Position extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**模块表主键 数据类型bigint(11)*/
	private Long templeId;
	
	/**位置名称 数据类型varchar(50)*/
	private String positionName;
	
	/**位置编号 数据类型varchar(30)*/
	private String positionCode;
	
	/**状态(0正常，1停用，2删除) 数据类型tinyint(2)*/
	private Integer status;
	
	/**页面名称 数据类型varchar(50)*/
	private String pageName;
	
	/**模块名称 数据类型varchar(50)*/
	private String templeName;
	
	/**顺序 数据类型tinyint(5)*/
	private Integer seq;
	
	/**元素类型 数据类型tinyint(2)*/
	private Integer elementType;
	
	/**页面表主键 数据类型bigint(11)*/
	private Long pageId;
	
	
	public Long getId(){
		return id;
	}
	public Long getTempleId(){
		return templeId;
	}
	public String getPositionName(){
		return positionName;
	}
	public String getPositionCode(){
		return positionCode;
	}
	public Integer getStatus(){
		return status;
	}
	public String getPageName(){
		return pageName;
	}
	public String getTempleName(){
		return templeName;
	}
	public Integer getSeq(){
		return seq;
	}
	public Integer getElementType(){
		return elementType;
	}
	public Long getPageId(){
		return pageId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTempleId(Long templeId){
		this.templeId=templeId;
	}
	public void setPositionName(String positionName){
		this.positionName=positionName;
	}
	public void setPositionCode(String positionCode){
		this.positionCode=positionCode;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setPageName(String pageName){
		this.pageName=pageName;
	}
	public void setTempleName(String templeName){
		this.templeName=templeName;
	}
	public void setSeq(Integer seq){
		this.seq=seq;
	}
	public void setElementType(Integer elementType){
		this.elementType=elementType;
	}
	public void setPageId(Long pageId){
		this.pageId=pageId;
	}
}
