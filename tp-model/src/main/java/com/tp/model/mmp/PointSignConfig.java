package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 签到积分奖励配置表
  */
public class PointSignConfig extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1473303924128L;

	/**连续签到ID 数据类型int(11)*/
	@Id
	private Integer id;
	
	/**连续签到天数 数据类型int(11)*/
	private Integer sequenceDay;
	
	/**奖励积分 数据类型int(11)*/
	private Integer point;
	
	/**是否可用:0不可用，1 可用，默认为1  数据类型tinyint(1)*/
	private Integer used;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Integer getId(){
		return id;
	}
	public Integer getSequenceDay(){
		return sequenceDay;
	}
	public Integer getPoint(){
		return point;
	}
	public Integer getUsed(){
		return used;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setSequenceDay(Integer sequenceDay){
		this.sequenceDay=sequenceDay;
	}
	public void setPoint(Integer point){
		this.point=point;
	}
	public void setUsed(Integer used){
		this.used=used;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
