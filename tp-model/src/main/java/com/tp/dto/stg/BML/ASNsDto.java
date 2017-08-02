package com.tp.dto.stg.BML;

import java.util.List;

/**
 * 
 * <pre>
 * 入库信息dto
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ASNsDto {
	/**
	 * 仓库ASN编号
	 */
	private String ASNNo;
	/**
	 * 入库类型
	 */
	private String Type;
	/**
	 * 客户入库单号
	 */
	private String CustmorOrderNo;
	/**
	 * 预计入库时间
	 */
	private String ExpectedArriveTime;
	/**
	 * 入库的详细信息
	 */
	private List<ASNsDetail> Details;

	public String getASNNo() {
		return ASNNo;
	}

	public void setASNNo(String aSNNo) {
		ASNNo = aSNNo;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getCustmorOrderNo() {
		return CustmorOrderNo;
	}

	public void setCustmorOrderNo(String custmorOrderNo) {
		CustmorOrderNo = custmorOrderNo;
	}

	public String getExpectedArriveTime() {
		return ExpectedArriveTime;
	}

	public void setExpectedArriveTime(String expectedArriveTime) {
		ExpectedArriveTime = expectedArriveTime;
	}

	public List<ASNsDetail> getDetails() {
		return Details;
	}

	public void setDetails(List<ASNsDetail> details) {
		Details = details;
	}

}
