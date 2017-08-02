/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.result.ord;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * <pre>
 * 子订单快递信息
 * </pre>
 * 
 * @author szy
 * @time 2015-2-3 下午6:07:09
 */
public class SubOrderExpressInfoDTO implements Serializable {
	private static final long serialVersionUID = -7117836073543842673L;
	/** 子订单编号 */
	private Long subOrderCode;
	/** 退货单编号 */
	private Long rejectNo;
	/** 物流公司ID */
	private String companyId;
	/** 物流公司名称 */
	private String companyName;
	/** 运单号 */
	private String packageNo;
	/** 物流日志记录信息列表，内部以实现根据时间倒序排序，如有需要，可以使用Collections.sort(list)进行排序 */
	private List<ExpressLogInfoDTO> expressLogInfoList;

	public Long getSubOrderCode() {
		return subOrderCode;
	}

	public void setSubOrderCode(Long subOrderCode) {
		this.subOrderCode = subOrderCode;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	public List<ExpressLogInfoDTO> getExpressLogInfoDTOList() {
		if (CollectionUtils.isNotEmpty(expressLogInfoList)) {
			Collections.sort(expressLogInfoList, new Comparator<ExpressLogInfoDTO>() {
				public int compare(ExpressLogInfoDTO o1, ExpressLogInfoDTO o2) {
					return o2.getDataTime().compareTo(o1.getDataTime());
				}
			});
		}
		return expressLogInfoList;
	}

	public void setExpressLogInfoDTOList(List<ExpressLogInfoDTO> expressLogInfoList) {
		this.expressLogInfoList = expressLogInfoList;
	}

	public List<ExpressLogInfoDTO> getExpressLogInfoList() {
		if (CollectionUtils.isNotEmpty(expressLogInfoList)) {
			Collections.sort(expressLogInfoList, new Comparator<ExpressLogInfoDTO>() {
				public int compare(ExpressLogInfoDTO o1, ExpressLogInfoDTO o2) {
					return o2.getDataTime().compareTo(o1.getDataTime());
				}
			});
		}
		return expressLogInfoList;
	}

	public void setExpressLogInfoList(List<ExpressLogInfoDTO> expressLogInfoList) {
		this.expressLogInfoList = expressLogInfoList;
	}

	public Long getRejectNo() {
		return rejectNo;
	}

	public void setRejectNo(Long rejectNo) {
		this.rejectNo = rejectNo;
	}

}
