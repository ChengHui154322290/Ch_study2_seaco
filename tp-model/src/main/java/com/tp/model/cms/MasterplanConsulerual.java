package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 达人消费规则表
  */
public class MasterplanConsulerual extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**消费类型 数据类型varchar(10)*/
	private String cmcType;
	
	/**消费次数 数据类型int(10)*/
	private Integer cmcNum;
	
	/**消费额度 数据类型double(10,2)*/
	private Double cmcAmount;
	
	/**活动表主键 数据类型bigint(20)*/
	private Long cmcActivityId;
	
	
	public Long getId(){
		return id;
	}
	public String getCmcType(){
		return cmcType;
	}
	public Integer getCmcNum(){
		return cmcNum;
	}
	public Double getCmcAmount(){
		return cmcAmount;
	}
	public Long getCmcActivityId(){
		return cmcActivityId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCmcType(String cmcType){
		this.cmcType=cmcType;
	}
	public void setCmcNum(Integer cmcNum){
		this.cmcNum=cmcNum;
	}
	public void setCmcAmount(Double cmcAmount){
		this.cmcAmount=cmcAmount;
	}
	public void setCmcActivityId(Long cmcActivityId){
		this.cmcActivityId=cmcActivityId;
	}
}
