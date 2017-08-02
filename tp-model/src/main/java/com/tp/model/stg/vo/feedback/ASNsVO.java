/**
 * 
 */
package com.tp.model.stg.vo.feedback;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.tp.util.EnhanceDateConverter;

/**
 * @author szy
 *
 */
public class ASNsVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2920743329544314214L;
	/** 仓库入库流水号 */
	private String ASNNo;
	/** 客户采购订单号 */
	private String CustmorOrderNo;
	
	@XStreamConverter(value = EnhanceDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss" })
	private Date ExpectedArriveTime;
	
	private List<DetailVO> Details;

	public String getASNNo() {
		return ASNNo;
	}

	public void setASNNo(String aSNNo) {
		ASNNo = aSNNo;
	}

	public String getCustmorOrderNo() {
		return CustmorOrderNo;
	}

	public void setCustmorOrderNo(String custmorOrderNo) {
		CustmorOrderNo = custmorOrderNo;
	}

	public Date getExpectedArriveTime() {
		return ExpectedArriveTime;
	}

	public void setExpectedArriveTime(Date expectedArriveTime) {
		ExpectedArriveTime = expectedArriveTime;
	}

	public List<DetailVO> getDetails() {
		return Details;
	}

	public void setDetails(List<DetailVO> details) {
		Details = details;
	}
	
	
}
