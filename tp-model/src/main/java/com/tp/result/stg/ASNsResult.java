package com.tp.result.stg;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tp.common.vo.stg.BMLStorageConstant.InputOrderType;


/**
 * 
 * <pre>
 * 入库明细信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ASNsResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1435231937810316959L;
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
	private List<ASNsDetailResult> Details;

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

	public String getTypeDesc(){
		if(StringUtils.isNoneBlank(this.Type)){
			return InputOrderType.getDescByCode(this.Type);
		}
		return null;
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

	public List<ASNsDetailResult> getDetails() {
		return Details;
	}

	public void setDetails(List<ASNsDetailResult> details) {
		Details = details;
	}

}
