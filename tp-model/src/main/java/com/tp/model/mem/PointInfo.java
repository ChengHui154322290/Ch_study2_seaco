package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 用户积分明细表
  */
public class PointInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451281300756L;

	/**主键ID 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**用户ID 数据类型bigint(11)*/
	private Long userId;
	
	/**积分年份 数据类型smallint(4)*/
	private Integer year;
	
	/**0:pc 1:app 2:wap 数据类型tinyint(1)*/
	private Integer platForm;
	
	/**积分场景代码 数据类型varchar(50)*/
	private String sceneCode;
	
	/**获取或者是消耗的积分 数据类型int(10)*/
	private Integer point;
	
	/**0：表示增加积分  1：表示消耗积分 数据类型tinyint(1)*/
	private Integer pointFlag;
	
	/**积分发放时间 数据类型datetime*/
	private Date sendTime;
	
	/**积分过期时间 数据类型datetime*/
	private Date expireTime;
	
	/**积分描述 数据类型varchar(500)*/
	private String remark;
	
	/**0: 冻结 1: 正常 2：无效 数据类型tinyint(1)*/
	private Integer state;
	
	
	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public Integer getYear(){
		return year;
	}
	public Integer getPlatForm(){
		return platForm;
	}
	public String getSceneCode(){
		return sceneCode;
	}
	public Integer getPoint(){
		return point;
	}
	public Integer getPointFlag(){
		return pointFlag;
	}
	public Date getSendTime(){
		return sendTime;
	}
	public Date getExpireTime(){
		return expireTime;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getState(){
		return state;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setYear(Integer year){
		this.year=year;
	}
	public void setPlatForm(Integer platForm){
		this.platForm=platForm;
	}
	public void setSceneCode(String sceneCode){
		this.sceneCode=sceneCode;
	}
	public void setPoint(Integer point){
		this.point=point;
	}
	public void setPointFlag(Integer pointFlag){
		this.pointFlag=pointFlag;
	}
	public void setSendTime(Date sendTime){
		this.sendTime=sendTime;
	}
	public void setExpireTime(Date expireTime){
		this.expireTime=expireTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setState(Integer state){
		this.state=state;
	}
}
