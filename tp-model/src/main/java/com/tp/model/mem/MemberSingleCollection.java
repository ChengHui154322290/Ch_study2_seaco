package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 单品收藏
  */
public class MemberSingleCollection extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451281300756L;

	/**主键id 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**用户id 数据类型bigint(20)*/
	private Long userId;
	
	/**商品skuCode 数据类型varchar(32)*/
	private String skuCode;
	
	/**活动id 数据类型bigint(20)*/
	private Long topicId;
	
	/**商品pid 数据类型varchar(32)*/
	private String prdId;
	
	/**商品logo 数据类型varchar(100)*/
	private String prdLogo;
	
	/**品牌id 数据类型bigint(20)*/
	private Long brandId;
	
	/**商品名称 数据类型varchar(60)*/
	private String itemTitle;
	
	/**商品前台显示名称 数据类型varchar(60)*/
	private String mainTitle;
	
	/**收藏时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**是否有效 默认1 有效 数据类型tinyint(4)*/
	private Boolean state;
	
	
	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public Long getTopicId(){
		return topicId;
	}
	public String getPrdId(){
		return prdId;
	}
	public String getPrdLogo(){
		return prdLogo;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getItemTitle(){
		return itemTitle;
	}
	public String getMainTitle(){
		return mainTitle;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public Boolean getState(){
		return state;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setPrdId(String prdId){
		this.prdId=prdId;
	}
	public void setPrdLogo(String prdLogo){
		this.prdLogo=prdLogo;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setItemTitle(String itemTitle){
		this.itemTitle=itemTitle;
	}
	public void setMainTitle(String mainTitle){
		this.mainTitle=mainTitle;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setState(Boolean state){
		this.state=state;
	}
}
