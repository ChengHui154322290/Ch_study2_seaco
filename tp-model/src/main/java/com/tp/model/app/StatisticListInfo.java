package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 请求每个列表和详情页面文件时，后台返回结果（页面数据或者错误）时统计，主要用于统计每个页面的瞬间开销，请求成功失败的情况等信息
  */
public class StatisticListInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557815L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**列表/页面 所属分类ID  “0”首页“1”海淘“2”秒杀“3”购物“4“用户中心
 数据类型bigint(20)*/
	private Long fromCategoryId;
	
	/**列表所属分类名 列表所属分类名首页 ”海淘 秒杀 购物 用户中心
 数据类型varchar(100)*/
	private String fromCategoryName;
	
	/**请求分页列表时的当前页 数据类型int(11)*/
	private Integer currentPage;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**商品号，无商品号那么设置为“0” 数据类型varchar(50)*/
	private String itemNum;
	
	/**商品名称，无商品号那么设置为“” 数据类型varchar(100)*/
	private String itemName;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public Long getFromCategoryId(){
		return fromCategoryId;
	}
	public String getFromCategoryName(){
		return fromCategoryName;
	}
	public Integer getCurrentPage(){
		return currentPage;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getItemNum(){
		return itemNum;
	}
	public String getItemName(){
		return itemName;
	}
	public Long getStaBaseId(){
		return staBaseId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setFromCategoryId(Long fromCategoryId){
		this.fromCategoryId=fromCategoryId;
	}
	public void setFromCategoryName(String fromCategoryName){
		this.fromCategoryName=fromCategoryName;
	}
	public void setCurrentPage(Integer currentPage){
		this.currentPage=currentPage;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setItemNum(String itemNum){
		this.itemNum=itemNum;
	}
	public void setItemName(String itemName){
		this.itemName=itemName;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
