package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 税率表
  */
public class TaxRate extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786421L;

	/**主键id 默认自增 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**code码 数据类型varchar(11)*/
	private String code;
	
	/**税率类型
关税税率 tarrifRate
采购税率 saleRate 
销售税率 pinRate  数据类型varchar(255)*/
	private String type;
	
	/**税率 输入多少存多少 如 17%则直接存17 数据类型double*/
	private Double rate;
	
	/** 数据类型int(255)*/
	private Integer dutiableValue;
	
	/**状态  1有效 0    无效 数据类型tinyint(4)*/
	private Integer status;
	
	/** 数据类型varchar(255)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public String getCode(){
		return code;
	}
	public String getType(){
		return type;
	}
	public Double getRate(){
		return rate;
	}
	public Integer getDutiableValue(){
		return dutiableValue;
	}
	public Integer getStatus(){
		return status;
	}
	public String getRemark(){
		return remark;
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
	public void setCode(String code){
		this.code=code;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setRate(Double rate){
		this.rate=rate;
	}
	public void setDutiableValue(Integer dutiableValue){
		this.dutiableValue=dutiableValue;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
