package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 统计分享的各项数据。 
  */
public class StatisticShare extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557816L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**产品ID 数据类型varchar(20)*/
	private String productId;
	
	/**0：微信 1：新浪 2：豆瓣等
 数据类型smallint(6)*/
	private Integer type;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(20)*/
	private String specialId;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public String getProductId(){
		return productId;
	}
	public Integer getType(){
		return type;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getSpecialId(){
		return specialId;
	}
	public Long getStaBaseId(){
		return staBaseId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setProductId(String productId){
		this.productId=productId;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setSpecialId(String specialId){
		this.specialId=specialId;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
