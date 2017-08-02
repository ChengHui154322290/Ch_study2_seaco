package com.tp.model.mmp;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 发券关联用户表
  */
public class CouponSendUserIds extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579105L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**发送外键 数据类型bigint(20)*/
	private Long sendId;
	
	/**发送者信息 数据类型varchar(20)*/
	private String userIds;
	
	/**状态 数据类型tinyint(1)*/
	private Integer state;
	
	
	public Long getId(){
		return id;
	}
	public Long getSendId(){
		return sendId;
	}
	public String getUserIds(){
		return userIds;
	}
	public Integer getState(){
		return state;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSendId(Long sendId){
		this.sendId=sendId;
	}
	public void setUserIds(String userIds){
		this.userIds=userIds;
	}
	public void setState(Integer state){
		this.state=state;
	}
}
