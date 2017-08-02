package com.tp.model.mmp;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠券补发规则表
  */
public class CouponRepeatInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579104L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**补券规则id 数据类型bigint(20)*/
	private Long repeatId;
	
	/**消耗的优惠券批次id 数据类型bigint(20)*/
	private Long batchId;
	
	/**是否重发,0:不重发 1:重发 数据类型tinyint(1)*/
	private Integer state;
	
	
	public Long getId(){
		return id;
	}
	public Long getRepeatId(){
		return repeatId;
	}
	public Long getBatchId(){
		return batchId;
	}
	public Integer getState(){
		return state;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setRepeatId(Long repeatId){
		this.repeatId=repeatId;
	}
	public void setBatchId(Long batchId){
		this.batchId=batchId;
	}
	public void setState(Integer state){
		this.state=state;
	}
}
