package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品prdid 销售数量表
  */
public class ItemDetailSalesCount extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1458121355808L;

	/**item_detai 表id 数据类型bigint(18)*/
	private Long detailId;
	
	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**item_detail prdid 编码 数据类型varchar(50)*/
	private String prdid;
	
	/**商品网站显示名称 数据类型varchar(60)*/
	private String mainTitle;
	
	/**商品条形码 数据类型varchar(20)*/
	private String barcode;
	
	/**初始销售数量 数据类型bigint(20)*/
	private Long defaultSalesCount;
	
	/**实际销售数量 数据类型bigint(20)*/
	private Long relSalesCount;
	
	/**最后修改基数时间 数据类型datetime*/
	private Date updateDefaultCountTime;
	
	/**创建人ID，0:系统 数据类型varchar(32)*/
	private String createUser;
	
	/**创建人 数据类型datetime*/
	private Date createTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getDetailId(){
		return detailId;
	}
	public Long getId(){
		return id;
	}
	public String getPrdid(){
		return prdid;
	}
	public String getMainTitle(){
		return mainTitle;
	}
	public String getBarcode(){
		return barcode;
	}
	public Long getDefaultSalesCount(){
		return defaultSalesCount;
	}
	public Long getRelSalesCount(){
		return relSalesCount;
	}
	public Date getUpdateDefaultCountTime(){
		return updateDefaultCountTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPrdid(String prdid){
		this.prdid=prdid;
	}
	public void setMainTitle(String mainTitle){
		this.mainTitle=mainTitle;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setDefaultSalesCount(Long defaultSalesCount){
		this.defaultSalesCount=defaultSalesCount;
	}
	public void setRelSalesCount(Long relSalesCount){
		this.relSalesCount=relSalesCount;
	}
	public void setUpdateDefaultCountTime(Date updateDefaultCountTime){
		this.updateDefaultCountTime=updateDefaultCountTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
