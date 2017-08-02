package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 达人奖励规则表
  */
public class MasterplanRewardrual extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**奖励类型 数据类型varchar(10)*/
	private String cmrType;
	
	/**奖励规则阀值始 数据类型int(10)*/
	private Integer cmrThresholdStart;
	
	/**奖励规则阀值止 数据类型int(10)*/
	private Integer cmrThresholdEnd;
	
	/**奖励额度 数据类型double(10,2)*/
	private Double cmrAmount;
	
	/**奖励方式 数据类型varchar(10)*/
	private String cmrWay;
	
	/**活动表主键 数据类型bigint(20)*/
	private Long cmrActivityId;
	
	
	public Long getId(){
		return id;
	}
	public String getCmrType(){
		return cmrType;
	}
	public Integer getCmrThresholdStart(){
		return cmrThresholdStart;
	}
	public Integer getCmrThresholdEnd(){
		return cmrThresholdEnd;
	}
	public Double getCmrAmount(){
		return cmrAmount;
	}
	public String getCmrWay(){
		return cmrWay;
	}
	public Long getCmrActivityId(){
		return cmrActivityId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCmrType(String cmrType){
		this.cmrType=cmrType;
	}
	public void setCmrThresholdStart(Integer cmrThresholdStart){
		this.cmrThresholdStart=cmrThresholdStart;
	}
	public void setCmrThresholdEnd(Integer cmrThresholdEnd){
		this.cmrThresholdEnd=cmrThresholdEnd;
	}
	public void setCmrAmount(Double cmrAmount){
		this.cmrAmount=cmrAmount;
	}
	public void setCmrWay(String cmrWay){
		this.cmrWay=cmrWay;
	}
	public void setCmrActivityId(Long cmrActivityId){
		this.cmrActivityId=cmrActivityId;
	}
}
