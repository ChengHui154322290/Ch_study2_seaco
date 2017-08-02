package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 快递100物流公司编号与仓库系统的物流编号对照表
  */
public class ExpressCodeInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786418L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**快递公司id 数据类型bigint(20)*/
	private Long expressId;
	
	/**仓库编号 数据类型varchar(50)*/
	private String whCode;
	
	/**仓库中的物流公司编号 数据类型varchar(50)*/
	private String whExpressCode;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**最后修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getExpressId(){
		return expressId;
	}
	public String getWhCode(){
		return whCode;
	}
	public String getWhExpressCode(){
		return whExpressCode;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setExpressId(Long expressId){
		this.expressId=expressId;
	}
	public void setWhCode(String whCode){
		this.whCode=whCode;
	}
	public void setWhExpressCode(String whExpressCode){
		this.whExpressCode=whExpressCode;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
