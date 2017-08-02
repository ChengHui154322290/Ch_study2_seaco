package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class FavoritePromotion extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451874935465L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**活动ID 数据类型bigint(20)*/
	private Long promotionId;
	
	/**用户ID 数据类型bigint(20)*/
	private Long uid;
	
	/**商品skuCode 数据类型varchar(32)*/
	private String skuCode;
	
	/** 数据类型varchar(50)*/
	private String mobile;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	/** 数据类型datetime*/
	private Date deleteTime;
	
	/**是否删除 数据类型tinyint(1)*/
	private Boolean isDelete;
	
	/**开团时间 数据类型datetime*/
	private Date onSaleTime;
	
	/**平台来源  0:pc 1:app 2:wap 3:ios 4:BTM  5:wx 数据类型tinyint(1)*/
	private Integer platForm;
	
	/**网站来源 0:来自西客商城 1:来自seagoor 数据类型tinyint(1)*/
	private Integer source;
	
	
	public Long getId(){
		return id;
	}
	public Long getPromotionId(){
		return promotionId;
	}
	public Long getUid(){
		return uid;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getMobile(){
		return mobile;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Date getDeleteTime(){
		return deleteTime;
	}
	public Boolean getIsDelete(){
		return isDelete;
	}
	public Date getOnSaleTime(){
		return onSaleTime;
	}
	public Integer getPlatForm(){
		return platForm;
	}
	public Integer getSource(){
		return source;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPromotionId(Long promotionId){
		this.promotionId=promotionId;
	}
	public void setUid(Long uid){
		this.uid=uid;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setDeleteTime(Date deleteTime){
		this.deleteTime=deleteTime;
	}
	public void setIsDelete(Boolean isDelete){
		this.isDelete=isDelete;
	}
	public void setOnSaleTime(Date onSaleTime){
		this.onSaleTime=onSaleTime;
	}
	public void setPlatForm(Integer platForm){
		this.platForm=platForm;
	}
	public void setSource(Integer source){
		this.source=source;
	}
}
