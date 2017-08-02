package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 横竖屏
  */
public class StatisticScreen extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557816L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**横竖屏名称“0”：竖屏；”1”：横屏 数据类型smallint(6)*/
	private Integer orientation;
	
	/**保持横屏或者竖屏的时间 单位为秒 数据类型int(11)*/
	private Integer holdTime;
	
	/**当前用户在哪个界面	1：在播放界面 0：在其他界面 数据类型smallint(6)*/
	private Integer currentView;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public Integer getOrientation(){
		return orientation;
	}
	public Integer getHoldTime(){
		return holdTime;
	}
	public Integer getCurrentView(){
		return currentView;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getStaBaseId(){
		return staBaseId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrientation(Integer orientation){
		this.orientation=orientation;
	}
	public void setHoldTime(Integer holdTime){
		this.holdTime=holdTime;
	}
	public void setCurrentView(Integer currentView){
		this.currentView=currentView;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
