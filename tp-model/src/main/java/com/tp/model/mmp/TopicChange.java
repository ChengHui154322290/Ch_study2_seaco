package com.tp.model.mmp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 促销活动
  */
public class TopicChange extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579110L;

	/** 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**专题名称 数据类型varchar(255)*/
	private String name;
	
	/**0编辑中 1审批中 2已取消 3审核通过 4已驳回 5终止 数据类型tinyint(4)*/
	private Integer status;
	
	/**开始时间 数据类型datetime*/
	private Date startTime;
	
	/**限购政策 数据类型bigint(18)*/
	private Long limitPolicyId;
	
	/**变更活动专题Id 数据类型bigint(18)*/
	private Long changeTopicId;
	
	/**类型 0- 全部 1-单品团 2-品牌团 3-主题团 数据类型int(1)*/
	private Integer type;
	
	/**销售模式 1-不限 2-旗舰店 3-西狗国际商城 4-洋淘派 5-闪购 6-秒杀  7-阶梯团 数据类型int(1)*/
	private Integer salesPartten;
	
	/**洋淘派是否在首页显示 0-否 1-是 数据类型int(10)*/
	private Integer displayPages;
	
	/**专场的开始年龄段key 数据类型int(10)*/
	private Integer ageStartKey;
	
	/**专场的结束年龄段key 数据类型int(10)*/
	private Integer ageEndKey;
	
	/**专题编号 */
	private String number;
	
	/**折扣率 数据类型varchar(100)*/
	private String discount;
	
	/** 数据类型bigint(18)*/
	private Long brandId;
	
	/** 数据类型varchar(100)*/
	private String brandName;
	
	/**专题进度 0-未开始1-进行中2已结束 数据类型tinyint(4)*/
	private Integer progress;
	
	/**是否支持商户0-支持 1-不支持 数据类型tinyint(4)*/
	private Integer isSupportSupplier;
	
	/**运费模板 数据类型int(11)*/
	private Integer freightTemplet;
	
	/**活动卖点 数据类型varchar(100)*/
	private String topicPoint;
	
	/**排序 数据类型int(11)*/
	private Integer sortIndex;
	
	/**删除状态 0-正常 1-已删除 数据类型tinyint(4)*/
	private Integer deletion;
	
	/**持续时间0-长期有效 1-固定期限 数据类型tinyint(4)*/
	private Integer lastingType;
	
	/**结束时间 数据类型datetime*/
	private Date endTime;
	
	/**是否支持商家提报0-支持 1-不支持 数据类型tinyint(4)*/
	private Integer isSupportSupplierInfo;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**适用地区 数据类型varchar(100)*/
	private String areaStr;
	
	/**适用平台 数据类型varchar(100)*/
	private String platformStr;
	
	/**专题图片 数据类型varchar(255)*/
	private String image;
	
	/** 数据类型varchar(255)*/
	private String imageNew;
	
	/**手机端的专题图片 数据类型varchar(255)*/
	private String imageMobile;
	
	/**PC端的可能感兴趣图片 数据类型varchar(255)*/
	private String imageInterested;
	
	/**PC端的海淘图片 数据类型varchar(255)*/
	private String imageHitao;
	
	/**专题的详细介绍 数据类型text*/
	private String intro;
	
	/**手机端的专题描述详细 数据类型text*/
	private String introMobile;
	
	/**PC首页图片 数据类型varchar(255)*/
	private String pcImage;
	
	/**可能感兴趣图片 数据类型varchar(255)*/
	private String pcInterestImage;
	
	/**移动首页图片 数据类型varchar(255)*/
	private String mobileImage;
	
	/**商城图片 数据类型varchar(255)*/
	private String mallImage;
	
	/**海淘图片 数据类型varchar(255)*/
	private String haitaoImage;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;

	private Long supplierId;

	private String supplierName;
	
	/** 活动是否预占库存：0否1是 */
	private Integer reserveInventoryFlag;
	
	
	
    public Integer getReserveInventoryFlag() {
		return reserveInventoryFlag;
	}

	public void setReserveInventoryFlag(Integer reserveInventoryFlag) {
		this.reserveInventoryFlag = reserveInventoryFlag;
	}

	/**
     * 能否使用西狗币
     */
    private String canUseXgMoney;
	
	@Virtual
	private List<Long> promoterIdList;

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Long getLimitPolicyId(){
		return limitPolicyId;
	}
	public Long getChangeTopicId(){
		return changeTopicId;
	}
	public Integer getType(){
		return type;
	}
	public Integer getSalesPartten(){
		return salesPartten;
	}
	public Integer getDisplayPages(){
		return displayPages;
	}
	public Integer getAgeStartKey(){
		return ageStartKey;
	}
	public Integer getAgeEndKey(){
		return ageEndKey;
	}
	public String getNumber(){
		return number;
	}
	public String getDiscount(){
		return discount;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public Integer getProgress(){
		return progress;
	}
	public Integer getIsSupportSupplier(){
		return isSupportSupplier;
	}
	public Integer getFreightTemplet(){
		return freightTemplet;
	}
	public String getTopicPoint(){
		return topicPoint;
	}
	public Integer getSortIndex(){
		return sortIndex;
	}
	public Integer getDeletion(){
		return deletion;
	}
	public Integer getLastingType(){
		return lastingType;
	}
	public Date getEndTime(){
		return endTime;
	}
	public Integer getIsSupportSupplierInfo(){
		return isSupportSupplierInfo;
	}
	public String getRemark(){
		return remark;
	}
	public String getAreaStr(){
		return areaStr;
	}
	public String getPlatformStr(){
		return platformStr;
	}
	public String getImage(){
		return image;
	}
	public String getImageNew(){
		return imageNew;
	}
	public String getImageMobile(){
		return imageMobile;
	}
	public String getImageInterested(){
		return imageInterested;
	}
	public String getImageHitao(){
		return imageHitao;
	}
	public String getIntro(){
		return intro;
	}
	public String getIntroMobile(){
		return introMobile;
	}
	public String getPcImage(){
		return pcImage;
	}
	public String getPcInterestImage(){
		return pcInterestImage;
	}
	public String getMobileImage(){
		return mobileImage;
	}
	public String getMallImage(){
		return mallImage;
	}
	public String getHaitaoImage(){
		return haitaoImage;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}
	public void setLimitPolicyId(Long limitPolicyId){
		this.limitPolicyId=limitPolicyId;
	}
	public void setChangeTopicId(Long changeTopicId){
		this.changeTopicId=changeTopicId;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setSalesPartten(Integer salesPartten){
		this.salesPartten=salesPartten;
	}
	public void setDisplayPages(Integer displayPages){
		this.displayPages=displayPages;
	}
	public void setAgeStartKey(Integer ageStartKey){
		this.ageStartKey=ageStartKey;
	}
	public void setAgeEndKey(Integer ageEndKey){
		this.ageEndKey=ageEndKey;
	}
	public void setNumber(String number){
		this.number=number;
	}
	public void setDiscount(String discount){
		this.discount=discount;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setProgress(Integer progress){
		this.progress=progress;
	}
	public void setIsSupportSupplier(Integer isSupportSupplier){
		this.isSupportSupplier=isSupportSupplier;
	}
	public void setFreightTemplet(Integer freightTemplet){
		this.freightTemplet=freightTemplet;
	}
	public void setTopicPoint(String topicPoint){
		this.topicPoint=topicPoint;
	}
	public void setSortIndex(Integer sortIndex){
		this.sortIndex=sortIndex;
	}
	public void setDeletion(Integer deletion){
		this.deletion=deletion;
	}
	public void setLastingType(Integer lastingType){
		this.lastingType=lastingType;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	public void setIsSupportSupplierInfo(Integer isSupportSupplierInfo){
		this.isSupportSupplierInfo=isSupportSupplierInfo;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setAreaStr(String areaStr){
		this.areaStr=areaStr;
	}
	public void setPlatformStr(String platformStr){
		this.platformStr=platformStr;
	}
	public void setImage(String image){
		this.image=image;
	}
	public void setImageNew(String imageNew){
		this.imageNew=imageNew;
	}
	public void setImageMobile(String imageMobile){
		this.imageMobile=imageMobile;
	}
	public void setImageInterested(String imageInterested){
		this.imageInterested=imageInterested;
	}
	public void setImageHitao(String imageHitao){
		this.imageHitao=imageHitao;
	}
	public void setIntro(String intro){
		this.intro=intro;
	}
	public void setIntroMobile(String introMobile){
		this.introMobile=introMobile;
	}
	public void setPcImage(String pcImage){
		this.pcImage=pcImage;
	}
	public void setPcInterestImage(String pcInterestImage){
		this.pcInterestImage=pcInterestImage;
	}
	public void setMobileImage(String mobileImage){
		this.mobileImage=mobileImage;
	}
	public void setMallImage(String mallImage){
		this.mallImage=mallImage;
	}
	public void setHaitaoImage(String haitaoImage){
		this.haitaoImage=haitaoImage;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}

	public List<Long> getPromoterIdList() {
		return promoterIdList;
	}

	public void setPromoterIdList(List<Long> promoterIdList) {
		this.promoterIdList = promoterIdList;
	}

	public String getCanUseXgMoney() {
		return canUseXgMoney;
	}

	public void setCanUseXgMoney(String canUseXgMoney) {
		this.canUseXgMoney = canUseXgMoney;
	}
	
	
}
