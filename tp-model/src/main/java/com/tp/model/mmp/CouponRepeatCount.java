package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠券补发统计表
  */
public class CouponRepeatCount extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579104L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**优惠券补发规则id 数据类型bigint(20)*/
	private Long repeatId;
	
	/**用户id 数据类型bigint(20)*/
	private Long userId;
	
	/**已重发次数 数据类型int(11)*/
	private Integer count;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getRepeatId(){
		return repeatId;
	}
	public Long getUserId(){
		return userId;
	}
	public Integer getCount(){
		return count;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setRepeatId(Long repeatId){
		this.repeatId=repeatId;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setCount(Integer count){
		this.count=count;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
