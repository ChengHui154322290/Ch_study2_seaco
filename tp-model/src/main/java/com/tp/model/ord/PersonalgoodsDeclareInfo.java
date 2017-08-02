package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 个人物品申报数据表
  */
public class PersonalgoodsDeclareInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1465203515899L;

	/**主键ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**申报单申报平台如浙江杭州电子口岸对应JKF 数据类型varchar(20)*/
	private String declareCustoms;
	
	/**个人物品申报单单号(订单号) 数据类型varchar(30)*/
	private String declareNo;
	
	/**个人物品申报单预录入号码(入库时生成) 数据类型varchar(30)*/
	private String preEntryNo;
	
	/**个人物品申报单编号(海关生成) 数据类型varchar(50)*/
	private String personalgoodsNo;
	
	/**物流公司编号 数据类型varchar(20)*/
	private String companyNo;
	
	/**物流公司名称 数据类型varchar(255)*/
	private String companyName;
	
	/**快递单号(海淘订单只有一个包裹) 数据类型varchar(255)*/
	private String expressNo;
	
	/** 进口类型：0直邮进口1保税进口 */
	private Integer importType;
	
	/** 航班班次号（直邮参数） */
	private String voyageNo;
	
	/** 运输工具编号（直邮参数，可以填写航班号） */
	private String trafNo;
	
	/** 总提运单号（直邮参数） */
	private String billNo;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	public Long getId(){
		return id;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getDeclareCustoms(){
		return declareCustoms;
	}
	public String getDeclareNo(){
		return declareNo;
	}
	public String getPreEntryNo(){
		return preEntryNo;
	}
	public String getPersonalgoodsNo(){
		return personalgoodsNo;
	}
	public String getCompanyNo(){
		return companyNo;
	}
	public String getCompanyName(){
		return companyName;
	}
	public String getExpressNo(){
		return expressNo;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setDeclareCustoms(String declareCustoms){
		this.declareCustoms=declareCustoms;
	}
	public void setDeclareNo(String declareNo){
		this.declareNo=declareNo;
	}
	public void setPreEntryNo(String preEntryNo){
		this.preEntryNo=preEntryNo;
	}
	public void setPersonalgoodsNo(String personalgoodsNo){
		this.personalgoodsNo=personalgoodsNo;
	}
	public void setCompanyNo(String companyNo){
		this.companyNo=companyNo;
	}
	public void setCompanyName(String companyName){
		this.companyName=companyName;
	}
	public void setExpressNo(String expressNo){
		this.expressNo=expressNo;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public Integer getImportType() {
		return importType;
	}
	public void setImportType(Integer importType) {
		this.importType = importType;
	}
	public String getVoyageNo() {
		return voyageNo;
	}
	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}
	public String getTrafNo() {
		return trafNo;
	}
	public void setTrafNo(String trafNo) {
		this.trafNo = trafNo;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
}
