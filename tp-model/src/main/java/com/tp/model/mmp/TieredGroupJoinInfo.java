package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 阶梯团参团信息表
  */
public class TieredGroupJoinInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579109L;

	/**序号 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**活动序号 数据类型bigint(18)*/
	private Long topicId;
	
	/**活动名称 数据类型varchar(255)*/
	private String topicName;
	
	/**团序号 数据类型bigint(18)*/
	private Long groupId;
	
	/**团编号 数据类型varchar(30)*/
	private String groupNumber;
	
	/**参团人序号 数据类型bigint(18)*/
	private Long memberId;
	
	/**参团人 数据类型varchar(30)*/
	private String memberName;
	
	/**参团人手机 数据类型varchar(20)*/
	private String memberPhone;
	
	/**参团人邮箱 数据类型varchar(100)*/
	private String memberEmail;
	
	/**是否团长 1-是 0-否 数据类型tinyint(1)*/
	private Integer leader;
	
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
	public Long getTopicId(){
		return topicId;
	}
	public String getTopicName(){
		return topicName;
	}
	public Long getGroupId(){
		return groupId;
	}
	public String getGroupNumber(){
		return groupNumber;
	}
	public Long getMemberId(){
		return memberId;
	}
	public String getMemberName(){
		return memberName;
	}
	public String getMemberPhone(){
		return memberPhone;
	}
	public String getMemberEmail(){
		return memberEmail;
	}
	public Integer getLeader(){
		return leader;
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
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setTopicName(String topicName){
		this.topicName=topicName;
	}
	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}
	public void setGroupNumber(String groupNumber){
		this.groupNumber=groupNumber;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setMemberName(String memberName){
		this.memberName=memberName;
	}
	public void setMemberPhone(String memberPhone){
		this.memberPhone=memberPhone;
	}
	public void setMemberEmail(String memberEmail){
		this.memberEmail=memberEmail;
	}
	public void setLeader(Integer leader){
		this.leader=leader;
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
