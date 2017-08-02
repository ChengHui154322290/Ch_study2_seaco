package com.tp.dto.ptm.salesorder;


import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.tp.common.util.OrderUtils;
import com.tp.common.vo.OrderConstant;
import com.tp.dto.ptm.DateTimeDeserializer;

/**
 * 订单迷你DTO
 * 
 * @author 项硕
 * @version 2015年5月7日 下午3:51:58
 */
public class SubOrder4PlatformQO implements Serializable {

	private static final long serialVersionUID = 823478536517129535L;
	
	private static final int DEFAULT_PAGE_NO = 1;	// 默认第一页
	private static final int DEFAULT_PAGE_SIZE = 50;	// 默认每页条数
	private static final int MAX_PAGE_SIZE = 100;	// 最大每页条数

	private Integer pageSize = DEFAULT_PAGE_SIZE;
	private Integer pageNo = DEFAULT_PAGE_NO;
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private Date startTime;	// 下单开始时间
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private Date endTime;	// 下单结束时间
	private Integer status;	// 订单状态
	private Long supplierId;	// 供应商ID
	private Long code;	// 子单号
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		if (null == pageSize) {
			this.pageSize = DEFAULT_PAGE_SIZE;
		}
		
		if (pageSize > MAX_PAGE_SIZE) {
			this.pageSize = MAX_PAGE_SIZE;
		} else if (pageSize <= 0) {
			this.pageSize = DEFAULT_PAGE_SIZE;
		} else {
			this.pageSize = pageSize;
		}
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		if (null == pageSize) {
			this.pageNo = DEFAULT_PAGE_NO;
		}
		
		if (pageNo <= 0) {
			this.pageNo = DEFAULT_PAGE_NO;
		} else {
			this.pageNo = pageNo;
		}
	}
	public Date getStartTime() {
		return startTime;
	}
	@JsonDeserialize(using = DateTimeDeserializer.class)
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		if (null == OrderConstant.ORDER_STATUS.getCnName(status)) {	// 无效的订单状态
			return;
		}
		this.status = status;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		if (!OrderUtils.isOrderCode(code) && !OrderUtils.isSubOrderCode(code)) {	// 非法的订单号
			return;
		}
		this.code = code;
	}
	
}
