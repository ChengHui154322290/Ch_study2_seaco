package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 关联专题
  */
public class RelateChange extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579109L;

	/** 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**关联专题一方id 数据类型bigint(18)*/
	private Long firstTopicId;
	
	/**变更活动专题Id 数据类型bigint(18)*/
	private Long topicChangeId;
	
	/**关联的另一方 数据类型bigint(18)*/
	private Long secondTopicId;
	
	/**删除状态 0 -正常 1- 删除 数据类型int(1)*/
	private Integer deletion;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getFirstTopicId(){
		return firstTopicId;
	}
	public Long getTopicChangeId(){
		return topicChangeId;
	}
	public Long getSecondTopicId(){
		return secondTopicId;
	}
	public Integer getDeletion(){
		return deletion;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setFirstTopicId(Long firstTopicId){
		this.firstTopicId=firstTopicId;
	}
	public void setTopicChangeId(Long topicChangeId){
		this.topicChangeId=topicChangeId;
	}
	public void setSecondTopicId(Long secondTopicId){
		this.secondTopicId=secondTopicId;
	}
	public void setDeletion(Integer deletion){
		this.deletion=deletion;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
