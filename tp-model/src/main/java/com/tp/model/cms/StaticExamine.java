package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * cms静态活动操作日志表
  */
public class StaticExamine extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**操作人姓名 数据类型varchar(30)*/
	private String cmeUserName;
	
	/**操作人id 数据类型bigint(20)*/
	private Long cmeUserId;
	
	/**审核结果 数据类型varchar(10)*/
	private String cmeResult;
	
	/**审核意见 数据类型varchar(512)*/
	private String cmeSuggestion;
	
	/**审核时间 数据类型datetime*/
	private Date cmeDate;
	
	/**专题活动表主键 数据类型bigint(20)*/
	private Long cmeActivityId;
	
	
	public Long getId(){
		return id;
	}
	public String getCmeUserName(){
		return cmeUserName;
	}
	public Long getCmeUserId(){
		return cmeUserId;
	}
	public String getCmeResult(){
		return cmeResult;
	}
	public String getCmeSuggestion(){
		return cmeSuggestion;
	}
	public Date getCmeDate(){
		return cmeDate;
	}
	public Long getCmeActivityId(){
		return cmeActivityId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCmeUserName(String cmeUserName){
		this.cmeUserName=cmeUserName;
	}
	public void setCmeUserId(Long cmeUserId){
		this.cmeUserId=cmeUserId;
	}
	public void setCmeResult(String cmeResult){
		this.cmeResult=cmeResult;
	}
	public void setCmeSuggestion(String cmeSuggestion){
		this.cmeSuggestion=cmeSuggestion;
	}
	public void setCmeDate(Date cmeDate){
		this.cmeDate=cmeDate;
	}
	public void setCmeActivityId(Long cmeActivityId){
		this.cmeActivityId=cmeActivityId;
	}
}
