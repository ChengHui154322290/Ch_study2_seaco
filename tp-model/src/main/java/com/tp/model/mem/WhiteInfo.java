package com.tp.model.mem;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.mem.WhiteConstant;
import com.tp.exception.AppServiceException;
import com.tp.model.BaseDO;
import com.tp.util.StringUtil;

/**
 * 白名单
 * @author zhuss
 * @2016年4月13日 下午2:32:11
 */
public class WhiteInfo extends BaseDO implements Serializable{

	private static final long serialVersionUID = -488879659124534998L;
	
	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**用户ID 数据类型bigint(20)*/
	private Long userId;
	
	/**手机号  数据类型varcher(20)*/
	private String mobile;
	
	/**级别 1 SVIP 2 卡VIP 数据类型tinyint(2)*/
	private Integer level;
	
	/**真实姓名 数据类型varcher(50)*/
	private String trueName;
	
	/**证件类型 数据类型tinyint(2)*/
	private Integer certificateType;
	
	/**证件值 数据类型varcher(100)*/
	private String certificateValue;
	
	private Integer addressId;
	
	/**收货地址 数据类型varcher(500)*/
	private String consigneeAddress;
	
	/**历史消费记录 数据类型double*/
	private Double historyAmount;
	
	/**备注 数据类型varcher(500)*/
	private String remark;
	
	/**状态0冻结 1发布 数据类型tinyint(1)*/
	private Integer status;
	
	/**是否删除0否1是 数据类型tinyint(1)*/
	private Integer isDel;
	
	/**创建人 数据类型varcher(20)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型varcher(20)*/
	private String modifyUser;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	public String getLevelDesc(){
		return WhiteConstant.LEVEL.getDesc(level);
	}
	
	public String getStatusDesc(){
		return WhiteConstant.STATUS.getDesc(status);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public Integer getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(Integer certificateType) {
		this.certificateType = certificateType;
	}

	public String getCertificateValue() {
		return certificateValue;
	}

	public void setCertificateValue(String certificateValue) {
		this.certificateValue = certificateValue;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	public Double getHistoryAmount() {
		return historyAmount;
	}

	public void setHistoryAmount(Double historyAmount) {
		this.historyAmount = historyAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public void validate(WhiteInfo whiteInfo){
		if(StringUtil.isEmpty(whiteInfo.getMobile())) throw new AppServiceException("手机号不能为空");
		if(StringUtil.isNull(whiteInfo.getLevel())) throw new AppServiceException("级别不能为空");
	}
}
