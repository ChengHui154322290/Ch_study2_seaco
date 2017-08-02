package com.tp.model.ord.JKF;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 接口调用的同步回执、发送企业的异步回执报文格式一致
 *  
 */

@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfReceiptResult extends JkfBaseDO{
	private static final long serialVersionUID = 4829324621911553678L;

	private Head head;
	private Body body;
	
	@XmlAttribute
	private String version = "1.0.0";
	
	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public static class Head implements Serializable{
		private static final long serialVersionUID = -8869231683832366204L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{
		private static final long serialVersionUID = -4293539029935167799L;
		private List<JkfResult> list;

		@XmlElementWrapper(name="list")
		@XmlElement(name="jkfResult")
		public List<JkfResult> getList() {
			return list;
		}

		public void setList(List<JkfResult> list) {
			this.list = list;
		}	
	}
	
	/** 处理结 果 **/
	public static class JkfResult implements Serializable{
		private static final long serialVersionUID = 6898811310503773143L;

		/** 企业备案编号	VARCHAR2(10)	非空 **/
		private String companyCode;
		
		/** 业务编号	VARCHAR2(24)	非空 **/
		private String businessNo;
		
		/** 业务类型	VARCHAR2(10)	非空	业务类型代码 **/
		private String businessType;
		
		/** 申报类型	CHAR(1)	非空	申报类型代码  **/
		private String declareType;
		
		/** 处理结果	CHAR(1)	非空**/
		private String chkMark;
		
		/** 通知日期	VARCHAR2(10)	非空	YYYY-MM-DD **/
		private String noticeDate;
		
		/** 通知时间	VARCHAR2(8)	非空	HH:MM  **/
		private String noticeTime;
		
		/** 备注	VARCHAR2(254)	可空	ZJPORTRESULT表示浙江电子口岸反馈处理结果  **/
		private String note;
		
		/** 处理结果文字信息记录列表 **/
		private List<JkfResultDetail> resultList;
		
		public String getCompanyCode() {
			return companyCode;
		}
		public void setCompanyCode(String companyCode) {
			this.companyCode = companyCode;
		}
		public String getBusinessNo() {
			return businessNo;
		}
		public void setBusinessNo(String businessNo) {
			this.businessNo = businessNo;
		}
		public String getBusinessType() {
			return businessType;
		}
		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}
		public String getDeclareType() {
			return declareType;
		}
		public void setDeclareType(String declareType) {
			this.declareType = declareType;
		}
		public String getChkMark() {
			return chkMark;
		}
		public void setChkMark(String chkMark) {
			this.chkMark = chkMark;
		}
		public String getNoticeDate() {
			return noticeDate;
		}
		public void setNoticeDate(String noticeDate) {
			this.noticeDate = noticeDate;
		}
		public String getNoticeTime() {
			return noticeTime;
		}
		public void setNoticeTime(String noticeTime) {
			this.noticeTime = noticeTime;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		@XmlElementWrapper(name="resultList")
		@XmlElement(name="jkfResultDetail")
		public List<JkfResultDetail> getResultList() {
			return resultList;
		}
		public void setResultList(List<JkfResultDetail> resultList) {
			this.resultList = resultList;
		}		
	}
	
	/** 处理结果文字信息记录明细  **/
	public static class JkfResultDetail implements Serializable{
		private static final long serialVersionUID = 5205360003014722619L;
		/** 5位数据校验状态码+冒号+处理结果文字信息	VARCHAR2(70)	非空	　5位校验状态码+冒号+处理结果文字信息，格式类似：22001:企业编号未备 5位校验码定义见文档 **/
		private String resultInfo;

		@XmlElement(name="resultInfo")
		public String getResultInfo() {
			return resultInfo;
		}

		public void setResultInfo(String resultInfo) {
			this.resultInfo = resultInfo;
		}		
	}
}
