package com.tp.result.mem.app;

import java.util.Date;

import com.tp.model.BaseDO;

public class ItemReviewResultDTO extends BaseDO {


	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -8009956891027188236L;

	/** 主键 */
	private Long id;

	/** 订单ID */
	private String orderCode;

	/** 用户或默认选择后的商品属性详情 */
	private String sku;

	/** 商品 skuCode */
	private String skuCode;
	
	/** 商品id*/
	private String pid;

	/** 用户id */
	private Long uid;
	
	/** 用户名 冗余字段 */
	private String userName;

	/** 评论主题 */
	private String subject;

	/** 评论内容 */
	private String content;

	/** 是否审核通过 */
	private Boolean isCheck;
	
	/** 是否删除 **/
	private Boolean isDelete;

	/** 是否匿名 */
	private Boolean isAnonymous;
	
	/** 是否置顶 2-不限 1-置顶 0-置底 **/
	private Integer isTop;
	
	/** 是否隐藏 2-不限 1-隐藏 0-仅自己可见 **/
	private Boolean isHide;
	
	/** 是否由批量导入产生 1-是 0-不是 **/
	private Boolean isImport;
	
	/** 导入失败原因 **/
	private String failReason;

	/** 打分 */
	private Integer mark;

	/** 保留字段,必要时用于屏蔽用户评论 */
	private Integer status;

	/**  */
	private Date createTime;

	/**  */
	private Date modifyTime;

	/**  */
	private Date deleteTime;

	
	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 订单ID
	 * 
	 * @param orderCode
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/**
	 * 设置 用户或默认选择后的商品属性详情
	 * 
	 * @param sku
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * 设置
	 * 
	 * @param uid
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * 设置 评论主题
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 设置 评论内容
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 设置 是否审核通过
	 * 
	 * @param isCheck
	 */
	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}

	/**
	 * 设置 是否匿名
	 * 
	 * @param isAnonymous
	 */
	public void setIsAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	/**
	 * 设置 打分
	 * 
	 * @param mark
	 */
	public void setMark(Integer mark) {
		this.mark = mark;
	}

	/**
	 * 设置 保留字段,必要时用于屏蔽用户评论
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 设置
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 设置
	 * 
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 设置
	 * 
	 * @param deleteTime
	 */
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
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
	 * 获取 订单ID
	 * 
	 * @return orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * 获取 用户或默认选择后的商品属性详情
	 * 
	 * @return sku
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * 获取
	 * 
	 * @return uid
	 */
	public Long getUid() {
		return uid;
	}

	/**
	 * 获取 评论主题
	 * 
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 获取 评论内容
	 * 
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 获取 是否审核通过
	 * 
	 * @return isCheck
	 */
	public Boolean getIsCheck() {
		return isCheck;
	}

	/**
	 * 获取 是否匿名
	 * 
	 * @return isAnonymous
	 */
	public Boolean getIsAnonymous() {
		return isAnonymous;
	}

	/**
	 * 获取 打分
	 * 
	 * @return mark
	 */
	public Integer getMark() {
		return mark;
	}

	/**
	 * 获取 保留字段,必要时用于屏蔽用户评论
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 获取
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 获取
	 * 
	 * @return modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * 获取
	 * 
	 * @return deleteTime
	 */
	public Date getDeleteTime() {
		return deleteTime;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Boolean getIsHide() {
		return isHide;
	}

	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
	}
	
	public Boolean getIsImport() {
		return isImport;
	}

	public void setIsImport(Boolean isImport) {
		this.isImport = isImport;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	@Override
	public String toString() {
		return "ItemReviewResultDTO [id=" + id + ", orderCode=" + orderCode
				+ ", sku=" + sku + ", skuCode=" + skuCode + ", pid=" + pid
				+ ", uid=" + uid + ", userName=" + userName + ", subject="
				+ subject + ", content=" + content + ", isCheck=" + isCheck
				+ ", isDelete=" + isDelete + ", isAnonymous=" + isAnonymous
				+ ", mark=" + mark + ", status=" + status + ", createTime="
				+ createTime + ", modifyTime=" + modifyTime + ", deleteTime="
				+ deleteTime + "]";
	}
	
}