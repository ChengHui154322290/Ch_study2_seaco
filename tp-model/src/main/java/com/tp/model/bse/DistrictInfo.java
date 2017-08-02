package com.tp.model.bse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 地区信息
  */
public class DistrictInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786417L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**父级ID 数据类型int(11)*/
	private Long parentId;
	
	/**地区名称 数据类型varchar(192)*/
	private String name;
	
	/**中文拼音 数据类型varchar(128)*/
	private String spelling;
	
	/**中文拼音简称 数据类型varchar(32)*/
	private String simpleSpelling;
	
	/**层级 层级 0-顶级 1-大洲 2-国家 3-政区 4-省 5-市  6-区 7-街道 数据类型tinyint(4)*/
	private Integer type;
	
	/** 数据类型varchar(100)*/
	private String pathUrl;
	
	/**排序 数据类型smallint(6)*/
	private Integer sortNo;
	
	/**是否有效：0-无效、1-有效，默认为1 数据类型tinyint(1)*/
	private Integer isDelete;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/** 更新时间 数据类型datetime*/
	private Date updateTime;
	
	/** 数据类型char(6)*/
	private String nationalCode;
	
	@Virtual
	private Integer selected;
	@Virtual
	private List<DistrictInfo> districtInfoList = new ArrayList<DistrictInfo>(); 
	
	public Long getId(){
		return id;
	}
	public Long getParentId(){
		return parentId;
	}
	public String getName(){
		return name;
	}
	public String getSpelling(){
		return spelling;
	}
	public String getSimpleSpelling(){
		return simpleSpelling;
	}
	public Integer getType(){
		return type;
	}
	public String getPathUrl(){
		return pathUrl;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public Integer getIsDelete(){
		return isDelete;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getNationalCode(){
		return nationalCode;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentId(Long parentId){
		this.parentId=parentId;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setSpelling(String spelling){
		this.spelling=spelling;
	}
	public void setSimpleSpelling(String simpleSpelling){
		this.simpleSpelling=simpleSpelling;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setPathUrl(String pathUrl){
		this.pathUrl=pathUrl;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setIsDelete(Integer isDelete){
		this.isDelete=isDelete;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setNationalCode(String nationalCode){
		this.nationalCode=nationalCode;
	}
	public Integer getSelected() {
		return selected;
	}
	public void setSelected(Integer selected) {
		this.selected = selected;
	}
	public List<DistrictInfo> getDistrictInfoList() {
		return districtInfoList;
	}
	public void setDistrictInfoList(List<DistrictInfo> districtInfoList) {
		this.districtInfoList = districtInfoList;
	}
}
