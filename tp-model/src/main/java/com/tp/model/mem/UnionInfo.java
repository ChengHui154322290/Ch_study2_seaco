package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class UnionInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1453693388582L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**用户ID 数据类型bigint(11)*/
	private Long memId;
	
	/**关联值 数据类型varchar(50)*/
	private String unionVal;
	
	/**链接类型（1. 微信） 数据类型tinyint(2)*/
	private Integer type;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/** 是否删除 */
	private Boolean isDeleted;
	
	public Long getId(){
		return id;
	}
	public Long getMemId(){
		return memId;
	}
	public String getUnionVal(){
		return unionVal;
	}
	public Integer getType(){
		return type;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setMemId(Long memId){
		this.memId=memId;
	}
	public void setUnionVal(String unionVal){
		this.unionVal=unionVal;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
