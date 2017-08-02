package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠政策适用平台信息
  */
public class FullDiscountPlatform extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579108L;

	/**序号 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**优惠政策序号 数据类型bigint(18)*/
	private Long discountId;
	
	/**平台标示 参考pr_platform表 数据类型smallint(1)*/
	private Integer platformSymbol;
	
	/**平台url 数据类型varchar(200)*/
	private String platformUrl;
	
	/**删除标志位 0 - 正常 1 - 删除 数据类型smallint(1)*/
	private Integer deletion;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getDiscountId(){
		return discountId;
	}
	public Integer getPlatformSymbol(){
		return platformSymbol;
	}
	public String getPlatformUrl(){
		return platformUrl;
	}
	public Integer getDeletion(){
		return deletion;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setDiscountId(Long discountId){
		this.discountId=discountId;
	}
	public void setPlatformSymbol(Integer platformSymbol){
		this.platformSymbol=platformSymbol;
	}
	public void setPlatformUrl(String platformUrl){
		this.platformUrl=platformUrl;
	}
	public void setDeletion(Integer deletion){
		this.deletion=deletion;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
