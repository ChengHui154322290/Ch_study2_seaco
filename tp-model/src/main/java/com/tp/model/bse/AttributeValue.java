package com.tp.model.bse;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 属性值 表
  */
public class AttributeValue extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786415L;

	/**编号 数据类型varchar(50)*/
	private String code;
	
	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**属性项 id 数据类型bigint(11)*/
	private Long attributeId;
	
	/**名字 数据类型varchar(50)*/
	private String name;
	
	/**备注 数据类型text*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	/**0-无效, 1-有效 数据类型tinyint(4)*/
	private Integer status;
	
	@Virtual
	private Integer isSelect;
	
	public String getCode(){
		return code;
	}
	public Long getId(){
		return id;
	}
	public Long getAttributeId(){
		return attributeId;
	}
	public String getName(){
		return name;
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
	public Integer getStatus(){
		return status;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setAttributeId(Long attributeId){
		this.attributeId=attributeId;
	}
	public void setName(String name){
		this.name=name;
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
	public void setStatus(Integer status){
		this.status=status;
	}
	public Integer getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(Integer isSelect) {
		this.isSelect = isSelect;
	}
}
