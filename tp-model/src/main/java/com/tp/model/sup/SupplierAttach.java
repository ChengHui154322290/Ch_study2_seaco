package com.tp.model.sup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-证件信息表
  */
public class SupplierAttach extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274735L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**营业执照 数据类型varchar(100)*/
	private String businessLicense;
	
	/**税务登记证 数据类型varchar(100)*/
	private String taxregist;
	
	/**组织机构代码证 数据类型varchar(100)*/
	private String organize;
	
	/**商标注册证 数据类型varchar(100)*/
	private String brandRetist;
	
	/**一般纳税人资格证 数据类型varchar(100)*/
	private String taxpayer;
	
	/**银行开户许可证 数据类型varchar(100)*/
	private String depositBank;
	
	/**法人/授权人身份证明 数据类型varchar(100)*/
	private String agentLiscenceCredit;
	
	/**法定代表人授权委托书 数据类型varchar(100)*/
	private String agentLiscence;
	
	/**品牌授权证明 数据类型varchar(500)*/
	private String brandLiscence;
	
	/**特殊资质文件 数据类型varchar(500)*/
	private String specialPapers;
	
	/**产品质检报告 数据类型varchar(100)*/
	private String qualityLiscence;
	
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
	
	@Virtual
	private List<SupplierImage> supplierImageList = new ArrayList<SupplierImage>();
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getBusinessLicense(){
		return businessLicense;
	}
	public String getTaxregist(){
		return taxregist;
	}
	public String getOrganize(){
		return organize;
	}
	public String getBrandRetist(){
		return brandRetist;
	}
	public String getTaxpayer(){
		return taxpayer;
	}
	public String getDepositBank(){
		return depositBank;
	}
	public String getAgentLiscenceCredit(){
		return agentLiscenceCredit;
	}
	public String getAgentLiscence(){
		return agentLiscence;
	}
	public String getBrandLiscence(){
		return brandLiscence;
	}
	public String getSpecialPapers(){
		return specialPapers;
	}
	public String getQualityLiscence(){
		return qualityLiscence;
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
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setBusinessLicense(String businessLicense){
		this.businessLicense=businessLicense;
	}
	public void setTaxregist(String taxregist){
		this.taxregist=taxregist;
	}
	public void setOrganize(String organize){
		this.organize=organize;
	}
	public void setBrandRetist(String brandRetist){
		this.brandRetist=brandRetist;
	}
	public void setTaxpayer(String taxpayer){
		this.taxpayer=taxpayer;
	}
	public void setDepositBank(String depositBank){
		this.depositBank=depositBank;
	}
	public void setAgentLiscenceCredit(String agentLiscenceCredit){
		this.agentLiscenceCredit=agentLiscenceCredit;
	}
	public void setAgentLiscence(String agentLiscence){
		this.agentLiscence=agentLiscence;
	}
	public void setBrandLiscence(String brandLiscence){
		this.brandLiscence=brandLiscence;
	}
	public void setSpecialPapers(String specialPapers){
		this.specialPapers=specialPapers;
	}
	public void setQualityLiscence(String qualityLiscence){
		this.qualityLiscence=qualityLiscence;
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
	public List<SupplierImage> getSupplierImageList() {
		return supplierImageList;
	}
	public void setSupplierImageList(List<SupplierImage> supplierImageList) {
		this.supplierImageList = supplierImageList;
	}
}
