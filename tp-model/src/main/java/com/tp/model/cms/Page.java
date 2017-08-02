package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 页面管理表
  */
public class Page extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**页面名称 数据类型varchar(50)*/
	private String pageName;
	
	/**页面编号 数据类型varchar(30)*/
	private String pageCode;
	
	/**状态(0正常，1停用，2删除) 数据类型tinyint(2)*/
	private Integer status;
	
	/**顺序 数据类型tinyint(3)*/
	private Integer seq;
	
	
	public Long getId(){
		return id;
	}
	public String getPageName(){
		return pageName;
	}
	public String getPageCode(){
		return pageCode;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getSeq(){
		return seq;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPageName(String pageName){
		this.pageName=pageName;
	}
	public void setPageCode(String pageCode){
		this.pageCode=pageCode;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setSeq(Integer seq){
		this.seq=seq;
	}
}
