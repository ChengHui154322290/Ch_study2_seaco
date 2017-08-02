package com.tp.model.mmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠券信息表
  */
public class Coupon extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579102L;

	/**主键id 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**优惠券名称 数据类型varchar(50)*/
	private String couponName;
	
	/**当优惠券 使用类型为 0  时间段 需要赋值。 使用开始时间 数据类型datetime*/
	private Date couponUseStime;
	
	/**优惠券批次号数据类型int(10)*/
	private Integer batchNum;
	
	/**优惠券面值 数据类型int(10)*/
	private Integer faceValue;
		
	/**优惠券需满金额 数据类型int(10)*/
	private Integer needOverMon;
	
	/**优惠券类型  0 : 满减券  1：现金券 数据类型int(2)*/
	private Integer couponType;
	
	/**优惠券发放时间  发放开始时间 数据类型datetime*/
	private Date couponReleaseStime;
	
	/**优惠券发放时间  结束时间 数据类型datetime*/
	private Date couponReleaseEtime;
	
	/**优惠券使用 类型 0：时间段有效   1：领取有效 */
	private Integer couponUseType;
	
	/**当优惠券 使用类型为 0  时间段 需要赋值。 使用结束时间 数据类型datetime*/
	private Date couponUseEtime;
	
	/**领取 几日内有效 数据类型int(18)*/
	private Integer useReceiveDay;
	
	/**是否需要短信验证 数据类型int(2)*/
	private Integer needValidate;
	
	/**适用范围0:全部 1：自营+代销 2：联营 string 字符串 以 “,” 分割 数据类型varchar(20)*/
	private String useRange;
	
	/**优惠券状态 0：有效 1：无效 数据类型varchar(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	/**创建者id 数据类型bigint(18)*/
	private Long createUserId;
	
	/**修改者id 数据类型bigint(18)*/
	private Long updateUserId;
	
	/**备注 数据类型varchar(500)*/
	private String remark;
	
	/**适用平台 1：全网2：pc3：app 4：wap 5：快了孕期  string 字符串 以 “,” 分割 数据类型varchar(20)*/
	private String usePlantform;
	
	/**优惠券的发放数量 -1表示不作限制 数据类型int(11)*/
	private Integer couponCount;
	
	/**0 -全部 1 -非海淘 2- 海淘 数据类型varchar(20)*/
	private String hitaoSign;
	
	/**发券主体  1 西客商城 2 商户 数据类型int(1)*/
	private Integer sourceType;
	
	/**发券主体   商户ID 数据类型bigint(11)*/
	private Long sourceId;
	
	/**商户名称 数据类型varchar(50)*/
	private String sourceName;
	
	/**批次编码 数据类型varchar(50)*/
	private String code;
	
	/**发放类型 :0 可发可领 1 可发 2可领 数据类型int(1)*/
	private Integer offerType;
	/**使用范围 1:商品      2： 专场  3:品牌   4：类别*/
	private String useScope;
	
	/**仅支持扫码领取 1  不限制  2 仅支持扫码领取 数据类型tinyint(4)*/
	private Integer justScan;
	
	private Integer activeStatus;
	/*0:不兑换  1：允许兑换*/
	private String exchangeXgMoney;
    /**是否在领券中心展示 1：不展示  2：展示*/
	private String isShowReceive;
	/**优惠券图片*/
	private String couponImagePath;
	
	@Virtual
	private List<CouponRange> couponRangeList = new ArrayList<CouponRange>();
	@Virtual
	public transient Double factValue;
	@Virtual
	public transient Double orgItemAmount;
	/**领取时间 传参*/
	@Virtual
	public transient Date receiveDate;
	@Virtual
	private String operator;
	@Virtual
	private String topicId;//专场ID
	@Virtual
	private String operate;//操作  1：领取  2：使用
	@Virtual
	private String mobile;//用户所有ID
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Date getCouponUseStime() {
		return couponUseStime;
	}

	public void setCouponUseStime(Date couponUseStime) {
		this.couponUseStime = couponUseStime;
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
		if(faceValue!=null){
			factValue = faceValue.doubleValue();
		}
		this.faceValue = faceValue;
	}

	public Integer getNeedOverMon() {
		return needOverMon;
	}

	public void setNeedOverMon(Integer needOverMon) {
		this.needOverMon = needOverMon;
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

	public Integer getCouponUseType() {
		return couponUseType;
	}

	public void setCouponUseType(Integer couponUseType) {
		this.couponUseType = couponUseType;
	}

	public Date getCouponUseEtime() {
		return couponUseEtime;
	}

	public void setCouponUseEtime(Date couponUseEtime) {
		this.couponUseEtime = couponUseEtime;
	}

	public Integer getUseReceiveDay() {
		return useReceiveDay;
	}

	public void setUseReceiveDay(Integer useReceiveDay) {
		this.useReceiveDay = useReceiveDay;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
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

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public String getHitaoSign() {
		return hitaoSign;
	}

	public void setHitaoSign(String hitaoSign) {
		this.hitaoSign = hitaoSign;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOfferType() {
		return offerType;
	}

	public void setOfferType(Integer offerType) {
		this.offerType = offerType;
	}

	public Integer getJustScan() {
		return justScan;
	}

	public void setJustScan(Integer justScan) {
		this.justScan = justScan;
	}

	public List<CouponRange> getCouponRangeList() {
		return couponRangeList;
	}

	public void setCouponRangeList(List<CouponRange> couponRangeList) {
		this.couponRangeList = couponRangeList;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getExchangeXgMoney() {
		return exchangeXgMoney;
	}

	public void setExchangeXgMoney(String exchangeXgMoney) {
		this.exchangeXgMoney = exchangeXgMoney;
	}

	public String getUseScope() {
		return useScope;
	}

	public void setUseScope(String useScope) {
		this.useScope = useScope;
	}

	public String getIsShowReceive() {
		return isShowReceive;
	}

	public void setIsShowReceive(String isShowReceive) {
		this.isShowReceive = isShowReceive;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCouponImagePath() {
		return couponImagePath;
	}

	public void setCouponImagePath(String couponImagePath) {
		this.couponImagePath = couponImagePath;
	}


	
	

}
