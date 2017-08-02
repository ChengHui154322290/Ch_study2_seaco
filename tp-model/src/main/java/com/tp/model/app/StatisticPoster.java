package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * APP启动展示启动海报后投递，用于统计启动 和 保证展现量数据
  */
public class StatisticPoster extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557815L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**name 启动海报名称 数据类型varchar(45)*/
	private String name;
	
	/**图片规格尺寸 数据类型varchar(45)*/
	private String size;
	
	/**屏幕方向 数据类型smallint(6)*/
	private Integer orientation;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getSize(){
		return size;
	}
	public Integer getOrientation(){
		return orientation;
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
	public void setName(String name){
		this.name=name;
	}
	public void setSize(String size){
		this.size=size;
	}
	public void setOrientation(Integer orientation){
		this.orientation=orientation;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
