package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.Date;

public class CouponDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Long id;

	/** 优惠券批次号 */
	private Integer batchNum;

	/** 优惠券面值 */
	private Integer faceValue;

	/** 优惠券需满金额 */
	private Integer needOverMon;

	/** 优惠券名称 */
	private String couponName;

	/** 优惠券类型  0 : 满减券  1：现金券 */
	private Integer couponType;

	/** 优惠券发放时间  发放开始时间 */
	private Date couponReleaseStime;

	/** 优惠券发放时间  结束时间 */
	private Date couponReleaseEtime;

	/** 优惠券使用 类型 0：时间段有效   1：领取有效 */
	private String couponUseType;

	/** 当优惠券 使用类型为 0  时间段 需要赋值。 使用开始时间 */
	private Date couponUseStime;

	/** 当优惠券 使用类型为 0  时间段 需要赋值。 使用结束时间 */
	private Date couponUseEtime;

	/** 是否需要短信验证 */
	private Integer needValidate;

	/** 适用范围 1：全网2：pc3：app 4：wap 5：快了孕期  string 字符串 以 “,” 分割 */
	private String useRange;
	
	/**使用范围 1:商品      2： 专场*/
	private String useScope;
	/**专场Id*/
	private  String topicId;

	/** 优惠券状态 0：有效 1：无效 */
	private String status;
	
	private Integer activeStatus;

	/** 创建时间 */
	private String createTime;

	/** 更新时间 */
	private String modifyTime;

	/** 创建者id */
	private Long createUserId;

	/** 修改者id */
	private Long updateUserId;

	/** 备注 */
	private String remark;

	/** 适用平台 string 字符串 以 “,” 分割 */
	private String usePlantform;
	
	private Integer useReceiveDay;
	
	/**关联组信息**/
	private String couponRangeGroup;
	/** 不包含范围**/
	private String couponRangeGroupNoInclude;
	
	private Integer couponCount;
	
	/** 海淘是否使用*/
	private String hitaoSign;
	
	/**发券主体 **/
	private Integer sourceType;
	/**商户ID **/
	private Long sourceId;
	/** 商家名称 **/
	private String sourceName;

	/** 是否可以领取*/
	private Integer offerType;

	/** 批次编号**/
	private String couponNumber;

	/** 仅支持扫码领取*/
	private Integer justScan;
	
	/** 推广员编号 **/
	private Long promoterId;
	
	/** 推广员名称 **/
	private String promoterName;

	/*0:不兑换  1：允许兑换*/
	private String exchangeXgMoney;
	 /**是否在领券中心展示 1：不展示  2：展示*/
    private String isShowReceive;
    /**优惠券图片*/
	private String couponImagePath;
    
	public Integer getOfferType() {
		return offerType;
	}

	public void setOfferType(Integer offerType) {
		this.offerType = offerType;
	}

	public String getCouponNumber() {
		return couponNumber;
	}

	public void setCouponNumber(String couponNumber) {
		this.couponNumber = couponNumber;
	}

	public Integer getJustScan() {
		return justScan;
	}

	public void setJustScan(Integer justScan) {
		this.justScan = justScan;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getCouponRangeGroupNoInclude() {
		return couponRangeGroupNoInclude;
	}

	public void setCouponRangeGroupNoInclude(String couponRangeGroupNoInclude) {
		this.couponRangeGroupNoInclude = couponRangeGroupNoInclude;
	}

	public String getHitaoSign() {
		return hitaoSign;
	}

	public void setHitaoSign(String hitaoSign) {
		this.hitaoSign = hitaoSign;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(Integer batchNum) {
		this.batchNum = batchNum;
	}

	public Integer getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(Integer faceValue) {
		this.faceValue = faceValue;
	}

	public Integer getNeedOverMon() {
		return needOverMon;
	}

	public void setNeedOverMon(Integer needOverMon) {
		this.needOverMon = needOverMon;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Date getCouponReleaseStime() {
		return couponReleaseStime;
	}

	public void setCouponReleaseStime(Date couponReleaseStime) {
		this.couponReleaseStime = couponReleaseStime;
	}

	public Date getCouponReleaseEtime() {
		return couponReleaseEtime;
	}

	public void setCouponReleaseEtime(Date couponReleaseEtime) {
		this.couponReleaseEtime = couponReleaseEtime;
	}

	public String getCouponUseType() {
		return couponUseType;
	}

	public void setCouponUseType(String couponUseType) {
		this.couponUseType = couponUseType;
	}

	public Date getCouponUseStime() {
		return couponUseStime;
	}

	public void setCouponUseStime(Date couponUseStime) {
		this.couponUseStime = couponUseStime;
	}

	public Date getCouponUseEtime() {
		return couponUseEtime;
	}

	public void setCouponUseEtime(Date couponUseEtime) {
		this.couponUseEtime = couponUseEtime;
	}

	public Integer getNeedValidate() {
		return needValidate;
	}

	public void setNeedValidate(Integer needValidate) {
		this.needValidate = needValidate;
	}

	public String getUseRange() {
		return useRange;
	}

	public void setUseRange(String useRange) {
		this.useRange = useRange;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUsePlantform() {
		return usePlantform;
	}

	public void setUsePlantform(String usePlantform) {
		this.usePlantform = usePlantform;
	}

	public Integer getUseReceiveDay() {
		return useReceiveDay;
	}

	public void setUseReceiveDay(Integer useReceiveDay) {
		this.useReceiveDay = useReceiveDay;
	}

	public String getCouponRangeGroup() {
		return couponRangeGroup;
	}

	public void setCouponRangeGroup(String couponRangeGroup) {
		this.couponRangeGroup = couponRangeGroup;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public Long getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

	public String getPromoterName() {
		return promoterName;
	}

	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}
	
	public String getExchangeXgMoney() {
		return exchangeXgMoney;
	}

	public void setExchangeXgMoney(String exchangeXgMoney) {
		this.exchangeXgMoney = exchangeXgMoney;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getUseScope() {
		return useScope;
	}

	public void setUseScope(String useScope) {
		this.useScope = useScope;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getIsShowReceive() {
		return isShowReceive;
	}

	public void setIsShowReceive(String isShowReceive) {
		this.isShowReceive = isShowReceive;
	}

	public String getCouponImagePath() {
		return couponImagePath;
	}

	public void setCouponImagePath(String couponImagePath) {
		this.couponImagePath = couponImagePath;
	}
	
}
