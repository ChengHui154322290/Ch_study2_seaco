package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 活动兑换码主表
  */
public class ExchangeCouponChannel extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579106L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**活动名称 数据类型varchar(50)*/
	private String actName;
	
	/**渠道(百度1) 数据类型tinyint(4)*/
	private Integer channel;
	
	/**类型(百宝箱1) 数据类型tinyint(4)*/
	private Integer type;
	
	/**兑换码数量 数据类型int(8)*/
	private Integer num;
	
	/**已兑换码数量 数据类型int(8)*/
	private Integer useNum;
	
	/**状态(0启用，1禁用) 数据类型tinyint(1)*/
	private Integer status;
	
	/**活动开始时间 数据类型datetime*/
	private Date startDate;
	
	/**活动结束时间 数据类型datetime*/
	private Date endDate;

	private Date createTime;

	private String createUser;

	private Date updateTime;

	private String updateUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getUseNum() {
		return useNum;
	}

	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
