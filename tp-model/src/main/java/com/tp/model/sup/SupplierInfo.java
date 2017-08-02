package com.tp.model.sup;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商主表
  */
public class SupplierInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836567625L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商编号(deprecated) 数据类型varchar(60)*/
	private String supplierCode;
	
	/**供应商或商家名称 数据类型varchar(80)*/
	private String name;
	
	/**供应商简称（别名） 数据类型varchar(80)*/
	private String alias;
	
	/**供应商类型（自营/代销/联营/主供应商） 数据类型varchar(60)*/
	private String supplierType;
	
	/**公司法人 数据类型varchar(60)*/
	private String legalPerson;
	
	/**联系人 数据类型varchar(60)*/
	private String linkName;
	
	/**联系地址 数据类型varchar(100)*/
	private String address;
	
	/**电子邮箱 数据类型varchar(60)*/
	private String email;
	
	/**联系电话 数据类型varchar(20)*/
	private String phone;
	
	/**传真 数据类型varchar(60)*/
	private String fax;
	
	/**审核状态 数据类型int(8)*/
	private Integer auditStatus;
	
	/**父供应商id（主供应商） 数据类型bigint(11)*/
	private Long parentSupplierId;
	
	/**父供应商名称 数据类型varchar(80)*/
	private String parentSupplierName;
	
	/**是否海淘 数据类型tinyint(1)*/
	private Integer isSea;
	
	/**运费模板id 数据类型bigint(11)*/
	private Long freightTemplateId;
	
	/**运费模板名称 数据类型varchar(80)*/
	private String freightTemplateName;
	
	/**进项税率（%） 数据类型double*/
	private Double incomeTaxRate;
	
	/**供应商描述 数据类型varchar(255)*/
	private String supplierDesc;
	
	/**品牌名称(逗号分隔) 数据类型text*/
	private String key1;
	
	/** 数据类型varchar(500)*/
	private String key2;
	
	/** 数据类型varchar(500)*/
	private String key3;
	
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
	
	/**
     * 供应商类型查询
     */
	@Virtual
    private List<String> supplierTypesQuery;
	@Virtual
	private String nameSearch;
	@Virtual
	private String saleType;
	@Virtual
	private String saleName;
	
	public String getSuplierTypeName() {
		return SupplierConstant.SUPPLIER_TYPES.get(supplierType);
    }
	
	public Long getId(){
		return id;
	}
	public String getSupplierCode(){
		return supplierCode;
	}
	public String getName(){
		return name;
	}
	public String getAlias(){
		return alias;
	}
	public String getSupplierType(){
		return supplierType;
	}
	public String getLegalPerson(){
		return legalPerson;
	}
	public String getLinkName(){
		return linkName;
	}
	public String getAddress(){
		return address;
	}
	public String getEmail(){
		return email;
	}
	public String getPhone(){
		return phone;
	}
	public String getFax(){
		return fax;
	}
	public Integer getAuditStatus(){
		return auditStatus;
	}
	public Long getParentSupplierId(){
		return parentSupplierId;
	}
	public String getParentSupplierName(){
		return parentSupplierName;
	}
	public Integer getIsSea(){
		return isSea;
	}
	public Long getFreightTemplateId(){
		return freightTemplateId;
	}
	public String getFreightTemplateName(){
		return freightTemplateName;
	}
	public Double getIncomeTaxRate(){
		return incomeTaxRate;
	}
	public String getSupplierDesc(){
		return supplierDesc;
	}
	public String getKey1(){
		return key1;
	}
	public String getKey2(){
		return key2;
	}
	public String getKey3(){
		return key3;
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
	public void setSupplierCode(String supplierCode){
		this.supplierCode=supplierCode;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setAlias(String alias){
		this.alias=alias;
	}
	public void setSupplierType(String supplierType){
		this.supplierType=supplierType;
	}
	public void setLegalPerson(String legalPerson){
		this.legalPerson=legalPerson;
	}
	public void setLinkName(String linkName){
		this.linkName=linkName;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public void setFax(String fax){
		this.fax=fax;
	}
	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}
	public void setParentSupplierId(Long parentSupplierId){
		this.parentSupplierId=parentSupplierId;
	}
	public void setParentSupplierName(String parentSupplierName){
		this.parentSupplierName=parentSupplierName;
	}
	public void setIsSea(Integer isSea){
		this.isSea=isSea;
	}
	public void setFreightTemplateId(Long freightTemplateId){
		this.freightTemplateId=freightTemplateId;
	}
	public void setFreightTemplateName(String freightTemplateName){
		this.freightTemplateName=freightTemplateName;
	}
	public void setIncomeTaxRate(Double incomeTaxRate){
		this.incomeTaxRate=incomeTaxRate;
	}
	public void setSupplierDesc(String supplierDesc){
		this.supplierDesc=supplierDesc;
	}
	public void setKey1(String key1){
		this.key1=key1;
	}
	public void setKey2(String key2){
		this.key2=key2;
	}
	public void setKey3(String key3){
		this.key3=key3;
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
	public List<String> getSupplierTypesQuery() {
		return supplierTypesQuery;
	}
	public void setSupplierTypesQuery(List<String> supplierTypesQuery) {
		this.supplierTypesQuery = supplierTypesQuery;
	}
	public String getNameSearch() {
		return nameSearch;
	}
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
	
}
