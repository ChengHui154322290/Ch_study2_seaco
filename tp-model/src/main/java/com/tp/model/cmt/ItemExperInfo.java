package com.tp.model.cmt;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 体验商品信息
  */
public class ItemExperInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446075L;

	/**商品编码 数据类型varchar(32)*/
	@Id
	private String pid;
	
	/**商品名称 数据类型varchar(100)*/
	private String productName;
	
	/**库存数量 数据类型int(11)*/
	private Integer store;
	
	/**图片地址 数据类型varchar(200)*/
	private String url;
	
	/**状态 数据类型tinyint(4)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**同步时间 数据类型datetime*/
	private Date syncTime;
	
	/**spu_id 数据类型bigint(18)*/
	private Long spuId;
	
	/**备注 数据类型varchar(200)*/
	private String mark;
	
	/**spu 数据类型varchar(50)*/
	private String spu;
	
	
	public String getPid(){
		return pid;
	}
	public String getProductName(){
		return productName;
	}
	public Integer getStore(){
		return store;
	}
	public String getUrl(){
		return url;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getSyncTime(){
		return syncTime;
	}
	public Long getSpuId(){
		return spuId;
	}
	public String getMark(){
		return mark;
	}
	public String getSpu(){
		return spu;
	}
	public void setPid(String pid){
		this.pid=pid;
	}
	public void setProductName(String productName){
		this.productName=productName;
	}
	public void setStore(Integer store){
		this.store=store;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setSyncTime(Date syncTime){
		this.syncTime=syncTime;
	}
	public void setSpuId(Long spuId){
		this.spuId=spuId;
	}
	public void setMark(String mark){
		this.mark=mark;
	}
	public void setSpu(String spu){
		this.spu=spu;
	}
}
