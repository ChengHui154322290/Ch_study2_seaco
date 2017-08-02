package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 模板节点数据表
  */
public class SingleTepactivity extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**模板节点表主键 数据类型bigint(11)*/
	private Long singleTepnodeId;
	
	/**专题编号 数据类型varchar(50)*/
	private String activityCode;
	
	/**专题名称 数据类型varchar(50)*/
	private String activityName;
	
	/**SKU编号 数据类型varchar(50)*/
	private String skuCode;
	
	/**商品名称 数据类型varchar(50)*/
	private String goodsName;
	
	/**商家 数据类型varchar(50)*/
	private String seller;
	
	/**规格参数 数据类型varchar(50)*/
	private String standardParams;
	
	/**限购总量 数据类型int(10)*/
	private Integer limitTotal;
	
	/**限购数量 数据类型int(10)*/
	private Integer limitNumber;
	
	/**活动价 数据类型double(10,2)*/
	private Double sellingPrice;
	
	/**开始时间 数据类型datetime*/
	private Date startdate;
	
	/**结束时间 数据类型datetime*/
	private Date enddate;
	
	/**创建人 数据类型int(10)*/
	private Integer creater;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型bigint(11)*/
	private Long activityId;
	
	/** 数据类型tinyint(4)*/
	private Integer superscript;
	
	/** 开始时间str */
	@Virtual
	private String startdateStr;
	
	/** 结束时间str */
	@Virtual
	private String enddateStr;
	
	/** 总页数 */
	@Virtual
	private Integer totalCount;
	
	/** 总条数 */
	@Virtual
	private Integer totalCountNum;
	
	
	public Long getId(){
		return id;
	}
	public Long getSingleTepnodeId(){
		return singleTepnodeId;
	}
	public String getActivityCode(){
		return activityCode;
	}
	public String getActivityName(){
		return activityName;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getGoodsName(){
		return goodsName;
	}
	public String getSeller(){
		return seller;
	}
	public String getStandardParams(){
		return standardParams;
	}
	public Integer getLimitTotal(){
		return limitTotal;
	}
	public Integer getLimitNumber(){
		return limitNumber;
	}
	public Double getSellingPrice(){
		return sellingPrice;
	}
	public Date getStartdate(){
		return startdate;
	}
	public Date getEnddate(){
		return enddate;
	}
	public Integer getCreater(){
		return creater;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getActivityId(){
		return activityId;
	}
	public Integer getSuperscript(){
		return superscript;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSingleTepnodeId(Long singleTepnodeId){
		this.singleTepnodeId=singleTepnodeId;
	}
	public void setActivityCode(String activityCode){
		this.activityCode=activityCode;
	}
	public void setActivityName(String activityName){
		this.activityName=activityName;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setGoodsName(String goodsName){
		this.goodsName=goodsName;
	}
	public void setSeller(String seller){
		this.seller=seller;
	}
	public void setStandardParams(String standardParams){
		this.standardParams=standardParams;
	}
	public void setLimitTotal(Integer limitTotal){
		this.limitTotal=limitTotal;
	}
	public void setLimitNumber(Integer limitNumber){
		this.limitNumber=limitNumber;
	}
	public void setSellingPrice(Double sellingPrice){
		this.sellingPrice=sellingPrice;
	}
	public void setStartdate(Date startdate){
		this.startdate=startdate;
	}
	public void setEnddate(Date enddate){
		this.enddate=enddate;
	}
	public void setCreater(Integer creater){
		this.creater=creater;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}
	public void setSuperscript(Integer superscript){
		this.superscript=superscript;
	}
	public String getStartdateStr() {
		return startdateStr;
	}
	public void setStartdateStr(String startdateStr) {
		this.startdateStr = startdateStr;
	}
	public String getEnddateStr() {
		return enddateStr;
	}
	public void setEnddateStr(String enddateStr) {
		this.enddateStr = enddateStr;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getTotalCountNum() {
		return totalCountNum;
	}
	public void setTotalCountNum(Integer totalCountNum) {
		this.totalCountNum = totalCountNum;
	}
}
