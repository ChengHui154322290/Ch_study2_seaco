package com.tp.model.pay;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 海关信息
  */
public class CustomsInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1459847151153L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**通关渠道ID 数据类型bigint(20)*/
	private Long channelsId;
	
	/**商户在海关的备案编号 数据类型varchar(30)*/
	private String merCode;
	
	/**商户在海关的备案名称 数据类型varchar(50)*/
	private String merName;
	
	/**海关编码 数据类型varchar(20)*/
	private String customsCode;
	
	/**是否需要推送海关 数据类型tinyint(2)*/
	private Boolean push;
	
	/**支付方式ID 数据类型int(11)*/
	private Long gatewayId;
	
	/**支付平台在海关的编号 数据类型varchar(16)*/
	private String payplatCode;
	
	/** 支付平台在海关的名称 */
	private String payplatName;
	
	public Long getId(){
		return id;
	}
	public Long getChannelsId(){
		return channelsId;
	}
	public String getMerCode(){
		return merCode;
	}
	public String getMerName(){
		return merName;
	}
	public String getCustomsCode(){
		return customsCode;
	}
	public Boolean getPush(){
		return push;
	}
	public Long getGatewayId(){
		return gatewayId;
	}
	public String getPayplatCode(){
		return payplatCode;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setChannelsId(Long channelsId){
		this.channelsId=channelsId;
	}
	public void setMerCode(String merCode){
		this.merCode=merCode;
	}
	public void setMerName(String merName){
		this.merName=merName;
	}
	public void setCustomsCode(String customsCode){
		this.customsCode=customsCode;
	}
	public void setPush(Boolean push){
		this.push=push;
	}
	public void setGatewayId(Long gatewayId){
		this.gatewayId=gatewayId;
	}
	public void setPayplatCode(String payplatCode){
		this.payplatCode=payplatCode;
	}
	public String getPayplatName() {
		return payplatName;
	}
	public void setPayplatName(String payplatName) {
		this.payplatName = payplatName;
	}
}
