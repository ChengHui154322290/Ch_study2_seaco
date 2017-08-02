package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 模板管理表
  */
public class Temple extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451849L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**页面表主键 数据类型varchar(50)*/
	private Long pageId;
	
	/**页面名称 数据类型varchar(30)*/
	private String pageName;
	
	/**模板名称 数据类型varchar(30)*/
	private String templeName;
	
	/**模板编号 数据类型varchar(30)*/
	private String templeCode;
	
	/**状态(0正常，1停用，2删除) 数据类型tinyint(2)*/
	private Integer status;
	
	/**顺序 数据类型tinyint(5)*/
	private Integer seq;
	
	/**元素类型 数据类型tinyint(2)*/
	private Integer elementType;
	
	/**元素数量 数据类型tinyint(5)*/
	private Integer elementNum;
	
	
	public Long getId(){
		return id;
	}
	public Long getPageId(){
		return pageId;
	}
	public String getPageName(){
		return pageName;
	}
	public String getTempleName(){
		return templeName;
	}
	public String getTempleCode(){
		return templeCode;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getSeq(){
		return seq;
	}
	public Integer getElementType(){
		return elementType;
	}
	public Integer getElementNum(){
		return elementNum;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPageId(Long pageId){
		this.pageId=pageId;
	}
	public void setPageName(String pageName){
		this.pageName=pageName;
	}
	public void setTempleName(String templeName){
		this.templeName=templeName;
	}
	public void setTempleCode(String templeCode){
		this.templeCode=templeCode;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setSeq(Integer seq){
		this.seq=seq;
	}
	public void setElementType(Integer elementType){
		this.elementType=elementType;
	}
	public void setElementNum(Integer elementNum){
		this.elementNum=elementNum;
	}
}
