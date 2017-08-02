package com.tp.model.sch;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class SearchKeyCensus extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1457063446437L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型tinyint(255)*/
	private Integer searchType;
	
	/** 数据类型varchar(255)*/
	private String searchKey;
	
	/** 数据类型datetime*/
	private Date searchDate;
	
	/** 数据类型int(11)*/
	private Integer searchCount;
	
	
	public Long getId(){
		return id;
	}
	public Integer getSearchType(){
		return searchType;
	}
	public String getSearchKey(){
		return searchKey;
	}
	public Date getSearchDate(){
		return searchDate;
	}
	public Integer getSearchCount(){
		return searchCount;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSearchType(Integer searchType){
		this.searchType=searchType;
	}
	public void setSearchKey(String searchKey){
		this.searchKey=searchKey;
	}
	public void setSearchDate(Date searchDate){
		this.searchDate=searchDate;
	}
	public void setSearchCount(Integer searchCount){
		this.searchCount=searchCount;
	}
}
