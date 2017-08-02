package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 模板节点表
  */
public class SingleTepnode extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**位置名称 数据类型varchar(50)*/
	private String positionName;
	
	/**模板表主键 数据类型bigint(11)*/
	private Long singleTempleId;
	
	/**位置尺寸 数据类型varchar(20)*/
	private String positionSize;
	
	/**位置顺序 数据类型int(2)*/
	private Integer positionSort;
	
	/**埋点编码 数据类型varchar(50)*/
	private String buriedCode;
	
	/**创建人 数据类型int(10)*/
	private Integer creater;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型int(10)*/
	private Integer modifier;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/** 数据类型int(2)*/
	private Integer dr;
	
	
	public Long getId(){
		return id;
	}
	public String getPositionName(){
		return positionName;
	}
	public Long getSingleTempleId(){
		return singleTempleId;
	}
	public String getPositionSize(){
		return positionSize;
	}
	public Integer getPositionSort(){
		return positionSort;
	}
	public String getBuriedCode(){
		return buriedCode;
	}
	public Integer getCreater(){
		return creater;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Integer getModifier(){
		return modifier;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Integer getDr(){
		return dr;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPositionName(String positionName){
		this.positionName=positionName;
	}
	public void setSingleTempleId(Long singleTempleId){
		this.singleTempleId=singleTempleId;
	}
	public void setPositionSize(String positionSize){
		this.positionSize=positionSize;
	}
	public void setPositionSort(Integer positionSort){
		this.positionSort=positionSort;
	}
	public void setBuriedCode(String buriedCode){
		this.buriedCode=buriedCode;
	}
	public void setCreater(Integer creater){
		this.creater=creater;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifier(Integer modifier){
		this.modifier=modifier;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setDr(Integer dr){
		this.dr=dr;
	}
}
