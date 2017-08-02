package com.tp.query.mem.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.model.BaseDO;


public class ItemReviewQueryDTO extends BaseDO {


	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -188279246663473150L;

	/** 主键 */
	private Long id;

	/** 订单 code */
	private String orderCode;

	/** 商品 skuCode */
	private String skuCode;
	
	/** 商品id */
	private String pid;

	/** 用户ID  */
	private Long uid;

	/** 是否审核通过 */
	private Boolean isCheck;
	
	/** 是否删除 **/
	private Boolean isDelete;

	/** 是否匿名 */
	private Boolean isAnonymous;
	
	/** 是否置顶 2-不限 1-置顶 0-置底 **/
	private Integer isTop;
	
	/** 是否隐藏 2-不限 1-隐藏 0-仅自己可见 **/
	private Integer isHide;
	
	/** 是否由批量导入产生 1-是 0-不是 **/
	private Boolean isImport;
	
	/** 导入失败原因 **/
	private String failReason;
	
	/** 保留字段,必要时用于屏蔽用户评论 */
	private Integer status;
	
	
	/** 用于提取多少条记录,只针对findTopReview接口有用  */
	private Integer count = 1;
	
	private Date createBeginTime;
	
	private Date createEndTime;

	private List<String> prds = new ArrayList<String>();
	
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
	 * 设置
	 * 
	 * @param uid
	 */
	public void setUid(Long uid) {
		this.uid = uid;
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
	 * 设置 保留字段,必要时用于屏蔽用户评论
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
	 * 获取
	 * 
	 * @return uid
	 */
	public Long getUid() {
		return uid;
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
	 * 获取 保留字段,必要时用于屏蔽用户评论
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	
	
	public Boolean getIsCheck() {
		return isCheck;
	}
	

	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}


	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
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
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getCreateBeginTime() {
		return createBeginTime;
	}

	public void setCreateBeginTime(Date createBeginTime) {
		this.createBeginTime = createBeginTime;
	}

	public Date getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

	public List<String> getPrds() {
		return prds;
	}

	public void setPrds(List<String> prds) {
		this.prds = prds;
	}
	
	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		return "ItemReviewQueryDTO [id=" + id + ", orderCode=" + orderCode
				+ ", skuCode=" + skuCode + ", pid=" + pid + ", uid=" + uid
				+ ", isCheck=" + isCheck + ", isDelete=" + isDelete
				+ ", isAnonymous=" + isAnonymous + ", status=" + status + "]";
	}
	

}