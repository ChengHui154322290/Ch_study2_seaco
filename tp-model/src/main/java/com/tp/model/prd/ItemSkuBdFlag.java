package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class ItemSkuBdFlag extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403936644L;

	/**sku编号 数据类型varchar(50)*/
	private String sku;
	
	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**sku是否拉取北京商品推荐 0 ：是 1否  数据类型tinyint(4)*/
	private Integer pullFlag;
	
	/**0:不推荐商品1推荐商品 数据类型tinyint(4)*/
	private Integer noRecommend;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人ID 数据类型bigint(11)*/
	private Long createUserId;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人ID 数据类型bigint(11)*/
	private Long updateUserId;
	
	
	public String getSku(){
		return sku;
	}
	public Long getId(){
		return id;
	}
	public Integer getPullFlag(){
		return pullFlag;
	}
	public Integer getNoRecommend(){
		return noRecommend;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public Long getUpdateUserId(){
		return updateUserId;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPullFlag(Integer pullFlag){
		this.pullFlag=pullFlag;
	}
	public void setNoRecommend(Integer noRecommend){
		this.noRecommend=noRecommend;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUserId(Long updateUserId){
		this.updateUserId=updateUserId;
	}
}
