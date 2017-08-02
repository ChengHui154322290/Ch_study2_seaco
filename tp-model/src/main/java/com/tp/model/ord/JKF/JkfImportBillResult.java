package com.tp.model.ord.JKF;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfImportBillResult extends JkfBaseDO{
	private static final long serialVersionUID = -4587515043057219469L;
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
		private static final long serialVersionUID = 4399776984516222017L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{
		private static final long serialVersionUID = -4089443412530803776L;
		private JkfSign jkfSign;
		private List<BillResult> billResultList;
		public JkfSign getJkfSign() {
			return jkfSign;
		}
		public void setJkfSign(JkfSign jkfSign) {
			this.jkfSign = jkfSign;
		}
		
		@XmlElementWrapper(name = "billResultList")
		@XmlElement(name="billResult")
		public List<BillResult> getBillResultList() {
			return billResultList;
		}
		public void setBillResultList(List<BillResult> billResultList) {
			this.billResultList = billResultList;
		}	
	}
	
	/** 签名信息 **/
	public static class JkfSign implements Serializable{
		private static final long serialVersionUID = -7477936057195442120L;

		/** 企业 备案编号	VARCHAR2(20)	是	接收方企业备案编号 **/
		private String companyCode;
		
		/** 业务编码	VARCHAR2(30)	是	对应的核放单号 **/
		private String businessNo;

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
	}
	
	/** 处理结果 **/
	public static class BillResult implements Serializable{
		private static final long serialVersionUID = 6669921433079434937L;

		/** 分运单号集合	VARCHAR2(512)	是	　分运单号之间逗号分割 **/
		private String wayBillNos;
		
		/** 出区状态	VARCHAR2(2)	是	1:已出区 2:未出区 **/
		private String outState;
		
		/** 出区时间	VARCHAR2(20)	是	yyyy-MM-dd HH:mm:ss **/
		private String outTime;

		public String getWayBillNos() {
			return wayBillNos;
		}

		public void setWayBillNos(String wayBillNos) {
			this.wayBillNos = wayBillNos;
		}

		public String getOutState() {
			return outState;
		}

		public void setOutState(String outState) {
			this.outState = outState;
		}

		public String getOutTime() {
			return outTime;
		}

		public void setOutTime(String outTime) {
			this.outTime = outTime;
		}	
	}
}
