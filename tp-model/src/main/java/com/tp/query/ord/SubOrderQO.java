package com.tp.query.ord;

import java.util.Date;
import java.util.List;

import com.tp.model.ord.SubOrder;
/**询对象
 * 
 * @author szy
 * @version 0.0.1
 */
public class SubOrderQO extends SubOrder {

	private static final long serialVersionUID = 7070872784217813262L;
	
	/** 是否有支付单号 - 是 */
	public static final Integer HAS_PAY_CODE_YES = 1;
	/** 是否有支付单号 - 否*/
	public static final Integer HAS_PAY_CODE_NO = 0;

	/** 下单时间范围 - 开始时间 */
	private Date startTime;
	/** 下单时间范围 - 结束时间 */
	private Date endTime;
	/** 子订单状态列表 */
	private List<Integer> statusList;
	/** 父订单ID列表 */
	private List<Long> orderIdList;
	/** 发货时间范围 － 开始时间 */
	private Date deliverBeginTime;
	/** 发货时间范围 － 结束时间 */
	private Date deliverEndTime;
	/** 完成时间范围 － 开始时间 */
	private Date doneBeginTime;
	/** 完成时间范围 － 结束时间 */
	private Date doneEndTime;
	/** 子订单类型列表 */
	private List<Integer> typeList;
	
	/**父或子订单编码*/
	private Long code;
	
	/* ------------------------------------ backend模块 ------------------------------------ */
	
	/** 会员登录名 */
	private String loginName;
	/** 是否有支付单号 */
	private Integer hasPayCode;
	
	private String promoterName;
	
	private String shopPromoterName;
	
	private String scanPromoterName;
		
	/**开始时间*/
	private Date createBeginTime;
	/**结束时间*/
	private Date createEndTime;
	public String getScanPromoterName() {
		return scanPromoterName;
	}
	public void setScanPromoterName(String scanPromoterName) {
		this.scanPromoterName = scanPromoterName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public List<Integer> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}
	public List<Long> getOrderIdList() {
		return orderIdList;
	}
	public void setOrderIdList(List<Long> orderIdList) {
		this.orderIdList = orderIdList;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Date getDeliverBeginTime() {
		return deliverBeginTime;
	}
	public void setDeliverBeginTime(Date deliverBeginTime) {
		this.deliverBeginTime = deliverBeginTime;
	}
	public Date getDeliverEndTime() {
		return deliverEndTime;
	}
	public void setDeliverEndTime(Date deliverEndTime) {
		this.deliverEndTime = deliverEndTime;
	}
	public Date getDoneBeginTime() {
		return doneBeginTime;
	}
	public void setDoneBeginTime(Date doneBeginTime) {
		this.doneBeginTime = doneBeginTime;
	}
	public Date getDoneEndTime() {
		return doneEndTime;
	}
	public void setDoneEndTime(Date doneEndTime) {
		this.doneEndTime = doneEndTime;
	}
	public Integer getHasPayCode() {
		return hasPayCode;
	}
	public void setHasPayCode(Integer hasPayCode) {
		this.hasPayCode = hasPayCode;
	}
	public List<Integer> getTypeList() {
		return typeList;
	}
	public void setTypeList(List<Integer> typeList) {
		this.typeList = typeList;
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getPromoterName() {
		return promoterName;
	}
	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}
	public String getShopPromoterName() {
		return shopPromoterName;
	}
	public void setShopPromoterName(String shopPromoterName) {
		this.shopPromoterName = shopPromoterName;
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
	
}
