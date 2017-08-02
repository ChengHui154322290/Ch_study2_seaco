package com.tp.service.pay.kqpay;

import java.io.Serializable;

public class GatewayRefundQueryResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	private String version; // 接口版本
	
	private String signType; // 签名类型
	
	private String merchantAcctId; // 人民币账号
	
	private int recordCount; // 查询结果总数
	
	private int pageCount; // 总页数
	
	private String currentPage; // 记录集当前页码
	
	private int  pageSize; // 当前页记录条数 	
	
	private String signMsg; // 签名字符串	

	private String errCode;  //错误代码 	
	
	private GatewayRefundQueryResultDto[] results;//结果对象集合

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getMerchantAcctId() {
		return merchantAcctId;
	}

	public void setMerchantAcctId(String merchantAcctId) {
		this.merchantAcctId = merchantAcctId;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public GatewayRefundQueryResultDto[] getResults() {
		return results;
	}

	public void setResults(GatewayRefundQueryResultDto[] results) {
		this.results = results;
	}

}
