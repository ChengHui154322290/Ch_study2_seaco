package com.tp.model.sup;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 合同-产品-资质证明信息表
  */
public class ContractQualifications extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274732L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long contractId;
	
	/**商品id 数据类型bigint(11)*/
	private Long productId;
	
	/**品牌id 数据类型bigint(11)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(80)*/
	private String brandName;
	
	/**大类id 数据类型bigint(11)*/
	private Long bigId;
	
	/**大类名称 数据类型varchar(80)*/
	private String bigName;
	
	/**中类id 数据类型bigint(11)*/
	private Long midId;
	
	/**中类名称 数据类型varchar(80)*/
	private String midName;
	
	/**小类id 数据类型bigint(11)*/
	private Long smallId;
	
	/**小类名称 数据类型varchar(80)*/
	private String smallName;
	
	/**证件id 数据类型varchar(100)*/
	private String papersId;
	
	/**证件名称 数据类型varchar(80)*/
	private String papersName;
	
	/** 数据类型varchar(255)*/
	private String url;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建操作者id 数据类型varchar(32)*/
	private String createUser;
	
	/**更新操作者id 数据类型varchar(32)*/
	private String updateUser;
	
	/** 是否已经选中 */
	@Virtual
    private Boolean hasChecked;
	
	
	public Long getId(){
		return id;
	}
	public Long getContractId(){
		return contractId;
	}
	public Long getProductId(){
		return productId;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public Long getBigId(){
		return bigId;
	}
	public String getBigName(){
		return bigName;
	}
	public Long getMidId(){
		return midId;
	}
	public String getMidName(){
		return midName;
	}
	public Long getSmallId(){
		return smallId;
	}
	public String getSmallName(){
		return smallName;
	}
	public String getPapersId(){
		return papersId;
	}
	public String getPapersName(){
		return papersName;
	}
	public String getUrl(){
		return url;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setContractId(Long contractId){
		this.contractId=contractId;
	}
	public void setProductId(Long productId){
		this.productId=productId;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setBigId(Long bigId){
		this.bigId=bigId;
	}
	public void setBigName(String bigName){
		this.bigName=bigName;
	}
	public void setMidId(Long midId){
		this.midId=midId;
	}
	public void setMidName(String midName){
		this.midName=midName;
	}
	public void setSmallId(Long smallId){
		this.smallId=smallId;
	}
	public void setSmallName(String smallName){
		this.smallName=smallName;
	}
	public void setPapersId(String papersId){
		this.papersId=papersId;
	}
	public void setPapersName(String papersName){
		this.papersName=papersName;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public Boolean getHasChecked() {
		return hasChecked;
	}
	public void setHasChecked(Boolean hasChecked) {
		this.hasChecked = hasChecked;
	}
}
