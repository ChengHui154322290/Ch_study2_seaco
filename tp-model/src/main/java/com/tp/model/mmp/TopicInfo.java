package com.tp.model.mmp;

import java.util.Date;

import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;

/**
 * 促销活动
 * 
 * @author szy
 */

public class TopicInfo extends BaseDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3444968741285807841L;

	/** 主键 */
	private Long id;

	/** 专题名称 */
	private String name;

	/** 类型 0- 全部 1-单品团 2-品牌团 3-主题团 */
	private Integer type;

	/** 专题编号 */
	private String number;

	/** 折扣 */
	private String discount;

	/** 状态 0-有效 1-无效 */
	private Integer status;

	/** 专题进度 0-未开始1-进行中2已结束 */
	private Integer progress;

	/** 是否支持商户0-支持 1-不支持 */
	private Integer isSupportSupplier;

	/** 运费模板 */
	private Integer freightTemplet;

	/** 排序 */
	private Integer sortIndex;

	/** 删除状态 0-正常 1-已删除 */
	private Integer deletion;

	/** 持续时间0-长期有效 1-固定期限 */
	private Integer lastingType;

	/** 开始时间 */
	private Date startTime;

	/** 结束时间 */
	private Date endTime;

	/** 是否支持商家提报0-支持 1-不支持 */
	private Integer isSupportSupplierInfo;

	/** 备注 */
	private String remark;

	/** 限购政策 */
	private Long limitPolicyId;

	/** 适用地区 */
	private String areaStr;

	/** 适用平台 */
	private String paltformStr;

	/** 专题图片 */
	private String image;

	/** 手机端的专题图片 */
	private String imageMobile;

	/** 专题的详细介绍 */
	private String intro;

	/** 手机端的专题描述详细 */
	private String introMobile;

	/** 创建时间 */
	private Date createTime;

	/** 更新时间 */
	private Date updateTime;

	/** 创建者id */
	private Long createUserId;

	/** 创建者名称 */
	private String createUserName;

	/** 修改者Id */
	private Long updateUser;

	/** 修改者名称 */
	private String modifyUserName;

	/** 品牌Id */
	private Long brandId;

	/** 品牌名称 */
	private String brandName;

	/** 今日上新图片 */
	private String imageNew;

	/** 可能感兴趣图片 */
	private String imageInterested;

	/** 海淘图片 */
	private String imageHitao;

	/** 活动卖点 */
	private String topicPoint;

	/** 销售模式 */
	private Integer salesPartten;

	/** PC首页图片 */
	private String pcImage;
	
	/** PC感兴趣首页图片 */
	private String pcInterestImage;
	
	/** 商城图片 */
	private String mallImage;

	/** 海淘图片 */
	private String haitaoImage;
	
	/** 移动首页图片 */
	private String mobileImage;

	
	/** 专卖上下架情况 0:下架 1:上架 */
	@Virtual
	private Integer onShelves;

	
	public Integer getOnShelves() {
		return onShelves;
	}

	public void setOnShelves(Integer onShelves) {
		this.onShelves = onShelves;
	}

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 专题名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置 类型 0- 全部 1-单品团 2-品牌团 3-主题团
	 * 
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 设置 专题编号
	 * 
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * 设置 状态 0-有效 1-无效
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 设置 专题进度 0-未开始1-进行中2已结束
	 * 
	 * @param progress
	 */
	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	/**
	 * 设置 是否支持商户0-支持 1-不支持
	 * 
	 * @param isSupportSupplier
	 */
	public void setIsSupportSupplier(Integer isSupportSupplier) {
		this.isSupportSupplier = isSupportSupplier;
	}

	/**
	 * 设置 运费模板
	 * 
	 * @param freightTemplet
	 */
	public void setFreightTemplet(Integer freightTemplet) {
		this.freightTemplet = freightTemplet;
	}

	/**
	 * 设置 排序
	 * 
	 * @param sortIndex
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * 设置 删除状态 0-正常 1-已删除
	 * 
	 * @param deletion
	 */
	public void setDeletion(Integer deletion) {
		this.deletion = deletion;
	}

	/**
	 * 设置 持续时间0-长期有效 1-固定期限
	 * 
	 * @param lastingType
	 */
	public void setLastingType(Integer lastingType) {
		this.lastingType = lastingType;
	}

	/**
	 * 设置 开始时间
	 * 
	 * @param startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 设置 结束时间
	 * 
	 * @param endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 设置 是否支持商家提报0-支持 1-不支持
	 * 
	 * @param isSupportSupplierInfo
	 */
	public void setIsSupportSupplierInfo(Integer isSupportSupplierInfo) {
		this.isSupportSupplierInfo = isSupportSupplierInfo;
	}

	/**
	 * 设置 备注
	 * 
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 设置 限购政策
	 * 
	 * @param limitPolicyId
	 */
	public void setLimitPolicyId(Long limitPolicyId) {
		this.limitPolicyId = limitPolicyId;
	}

	/**
	 * 设置 适用地区
	 * 
	 * @param areaStr
	 */
	public void setAreaStr(String areaStr) {
		this.areaStr = areaStr;
	}

	/**
	 * 设置 适用平台
	 * 
	 * @param paltformStr
	 */
	public void setPaltformStr(String paltformStr) {
		this.paltformStr = paltformStr;
	}

	/**
	 * 设置 专题图片
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 设置 手机端的专题图片
	 * 
	 * @param imageMobile
	 */
	public void setImageMobile(String imageMobile) {
		this.imageMobile = imageMobile;
	}

	/**
	 * 设置 专题的详细介绍
	 * 
	 * @param intro
	 */
	public void setIntro(String intro) {
		this.intro = intro;
	}

	/**
	 * 设置 手机端的专题描述详细
	 * 
	 * @param introMobile
	 */
	public void setIntroMobile(String introMobile) {
		this.introMobile = introMobile;
	}

	/**
	 * 设置 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 设置 更新时间
	 * 
	 * @param updateTime
	 */
	public void setModifyTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 设置 创建者id
	 * 
	 * @param createUserId
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 获取 专题名称
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取 类型 0- 全部 1-单品团 2-品牌团 3-主题团
	 * 
	 * @return type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 获取 专题编号
	 * 
	 * @return number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * 获取 状态 0-有效 1-无效
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 获取 专题进度 0-未开始1-进行中2已结束
	 * 
	 * @return progress
	 */
	public Integer getProgress() {
		return progress;
	}

	/**
	 * 获取 是否支持商户0-支持 1-不支持
	 * 
	 * @return isSupportSupplier
	 */
	public Integer getIsSupportSupplier() {
		return isSupportSupplier;
	}

	/**
	 * 获取 运费模板
	 * 
	 * @return freightTemplet
	 */
	public Integer getFreightTemplet() {
		return freightTemplet;
	}

	/**
	 * 获取 排序
	 * 
	 * @return sortIndex
	 */
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * 获取 删除状态 0-正常 1-已删除
	 * 
	 * @return deletion
	 */
	public Integer getDeletion() {
		return deletion;
	}

	/**
	 * 获取 持续时间0-长期有效 1-固定期限
	 * 
	 * @return lastingType
	 */
	public Integer getLastingType() {
		return lastingType;
	}

	/**
	 * 获取 开始时间
	 * 
	 * @return startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 获取 结束时间
	 * 
	 * @return endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * 获取 是否支持商家提报0-支持 1-不支持
	 * 
	 * @return isSupportSupplierInfo
	 */
	public Integer getIsSupportSupplierInfo() {
		return isSupportSupplierInfo;
	}

	/**
	 * 获取 备注
	 * 
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 获取 限购政策
	 * 
	 * @return limitPolicyId
	 */
	public Long getLimitPolicyId() {
		return limitPolicyId;
	}

	/**
	 * 获取 适用地区
	 * 
	 * @return areaStr
	 */
	public String getAreaStr() {
		return areaStr;
	}

	/**
	 * 获取 适用平台
	 * 
	 * @return paltformStr
	 */
	public String getPaltformStr() {
		return paltformStr;
	}

	/**
	 * 获取 专题图片
	 * 
	 * @return image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * 获取 手机端的专题图片
	 * 
	 * @return imageMobile
	 */
	public String getImageMobile() {
		return imageMobile;
	}

	/**
	 * 获取 专题的详细介绍
	 * 
	 * @return intro
	 */
	public String getIntro() {
		return intro;
	}

	/**
	 * 获取 手机端的专题描述详细
	 * 
	 * @return introMobile
	 */
	public String getIntroMobile() {
		return introMobile;
	}

	/**
	 * 获取 创建时间
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 获取 更新时间
	 * 
	 * @return updateTime
	 */
	public Date getModifyTime() {
		return updateTime;
	}

	/**
	 * 获取 创建者id
	 * 
	 * @return createUserId
	 */
	public Long getCreateUserId() {
		return createUserId;
	}

	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName
	 *            the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the updateUser
	 */
	public Long getModifyUserId() {
		return updateUser;
	}

	/**
	 * @param updateUser
	 *            the updateUser to set
	 */
	public void setModifyUserId(Long updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the modifyUserName
	 */
	public String getModifyUserName() {
		return modifyUserName;
	}

	/**
	 * @param modifyUserName
	 *            the modifyUserName to set
	 */
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}

	/**
	 * @return the brandId
	 */
	public Long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId
	 *            the brandId to set
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * @param brandName
	 *            the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the imageNew
	 */
	public String getImageNew() {
		return imageNew;
	}

	/**
	 * @param imageNew
	 *            the imageNew to set
	 */
	public void setImageNew(String imageNew) {
		this.imageNew = imageNew;
	}

	/**
	 * @return the imageInterested
	 */
	public String getImageInterested() {
		return imageInterested;
	}

	/**
	 * @param imageInterested
	 *            the imageInterested to set
	 */
	public void setImageInterested(String imageInterested) {
		this.imageInterested = imageInterested;
	}

	/**
	 * @return the topicPoint
	 */
	public String getTopicPoint() {
		return topicPoint;
	}

	/**
	 * @param topicPoint
	 *            the topicPoint to set
	 */
	public void setTopicPoint(String topicPoint) {
		this.topicPoint = topicPoint;
	}

	/**
	 * @return the imageHitao
	 */
	public String getImageHitao() {
		return imageHitao;
	}

	/**
	 * @param imageHitao
	 *            the imageHitao to set
	 */
	public void setImageHitao(String imageHitao) {
		this.imageHitao = imageHitao;
	}

	/**
	 * @return the salesPartten
	 */
	public Integer getSalesPartten() {
		return salesPartten;
	}

	/**
	 * @param salesPartten
	 *            the salesPartten to set
	 */
	public void setSalesPartten(Integer salesPartten) {
		this.salesPartten = salesPartten;
	}

	/**
	 * @return the pcImage
	 */
	public String getPcImage() {
		return pcImage;
	}

	/**
	 * @param pcImage
	 *            the pcImage to set
	 */
	public void setPcImage(String pcImage) {
		this.pcImage = pcImage;
	}

	/**
	 * @return the mobileImage
	 */
	public String getMobileImage() {
		return mobileImage;
	}

	/**
	 * @param mobileImage
	 *            the mobileImage to set
	 */
	public void setMobileImage(String mobileImage) {
		this.mobileImage = mobileImage;
	}

	/**
	 * @return the pcInterestImage
	 */
	public String getPcInterestImage() {
		return pcInterestImage;
	}

	/**
	 * @param pcInterestImage the pcInterestImage to set
	 */
	public void setPcInterestImage(String pcInterestImage) {
		this.pcInterestImage = pcInterestImage;
	}

	/**
	 * @return the mallImage
	 */
	public String getMallImage() {
		return mallImage;
	}

	/**
	 * @param mallImage the mallImage to set
	 */
	public void setMallImage(String mallImage) {
		this.mallImage = mallImage;
	}

	/**
	 * @return the haitaoImage
	 */
	public String getHaitaoImage() {
		return haitaoImage;
	}

	/**
	 * @param haitaoImage the haitaoImage to set
	 */
	public void setHaitaoImage(String haitaoImage) {
		this.haitaoImage = haitaoImage;
	}
}