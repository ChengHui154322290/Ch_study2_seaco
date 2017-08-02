package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 积分规则配置表 
  */
public class PointRuleConfig extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451281300756L;

	/**规则ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**场景代码 数据类型varchar(50)*/
	private String sceneCode;
	
	/**积分数 数据类型int(10)*/
	private Integer point;
	
	/**0：无固定期限 1：固定期限 数据类型tinyint(1)*/
	private Integer deadLineFlag;
	
	/**规则描述 数据类型varchar(500)*/
	private String remark;
	
	/**0:pc 1:app 2:wap 数据类型tinyint(1)*/
	private Integer platForm;
	
	/**当dead_line_flag 为1时 规则生效的开始时间 数据类型datetime*/
	private Date beginTime;
	
	/**当dead_line_flag 为1时 规则生效的结束时间 数据类型datetime*/
	private Date endTime;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	/**创建人ID 数据类型bigint(20)*/
	private Long createUserId;
	
	/**创建人姓名 数据类型varchar(50)*/
	private String createUserName;
	
	/**修改人id 数据类型bigint(20)*/
	private Long modifyUserId;
	
	/**修改人姓名 数据类型varchar(50)*/
	private String modifyUserName;
	
	/**是否逻辑删除 1: 删除 0: 未删除 数据类型tinyint(1)*/
	private Boolean isDelete;
	
	/**0：不可用 1：可用 数据类型tinyint(4)*/
	private Boolean state;
	
	
	public Long getId(){
		return id;
	}
	public String getSceneCode(){
		return sceneCode;
	}
	public Integer getPoint(){
		return point;
	}
	public Integer getDeadLineFlag(){
		return deadLineFlag;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getPlatForm(){
		return platForm;
	}
	public Date getBeginTime(){
		return beginTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public String getCreateUserName(){
		return createUserName;
	}
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public String getModifyUserName(){
		return modifyUserName;
	}
	public Boolean getIsDelete(){
		return isDelete;
	}
	public Boolean getState(){
		return state;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSceneCode(String sceneCode){
		this.sceneCode=sceneCode;
	}
	public void setPoint(Integer point){
		this.point=point;
	}
	public void setDeadLineFlag(Integer deadLineFlag){
		this.deadLineFlag=deadLineFlag;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setPlatForm(Integer platForm){
		this.platForm=platForm;
	}
	public void setBeginTime(Date beginTime){
		this.beginTime=beginTime;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateUserName(String createUserName){
		this.createUserName=createUserName;
	}
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
	public void setModifyUserName(String modifyUserName){
		this.modifyUserName=modifyUserName;
	}
	public void setIsDelete(Boolean isDelete){
		this.isDelete=isDelete;
	}
	public void setState(Boolean state){
		this.state=state;
	}
}
