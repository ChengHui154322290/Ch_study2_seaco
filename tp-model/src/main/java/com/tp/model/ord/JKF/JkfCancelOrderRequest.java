/**
 * 
 */
package com.tp.model.ord.JKF;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Administrator
 * 
 */
@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfCancelOrderRequest extends JkfBaseDO{
	
	private static final long serialVersionUID = 3894010284869015599L;
	private Head head = new Head();
	private Body body = new Body();
	
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
		
		private static final long serialVersionUID = -589051738163017093L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{

		private static final long serialVersionUID = -55753740210190781L;

		private JkfSign jkfSign = new JkfSign();
		
		private List<ModifyCancel> modifyCancelList = new ArrayList<ModifyCancel>();
		
		@XmlElementWrapper(name="modifyCancelList")
		@XmlElement(name="modifyCancel")
		public List<ModifyCancel> getModifyCancelList() {
			return modifyCancelList;
		}
		public void setModifyCancelList(List<ModifyCancel> modifyCancelList) {
			this.modifyCancelList = modifyCancelList;
		}
		public JkfSign getJkfSign() {
			return jkfSign;
		}
		public void setJkfSign(JkfSign jkfSign) {
			this.jkfSign = jkfSign;
		}	
	}
	
	public static class JkfSign implements Serializable{
		
		private static final long serialVersionUID = -3842883035738545026L;

		/** 接收方企业备案编号	VARCHAR2(20)	必填 **/
		private String companyCode;
		
		/** 业务编号	VARCHAR2(100)	必填 **/
		private String businessNo;
		
		/** 业务类型	VARCHAR2(30)	必填	业务类型 **/
		private String businessType;
		
		/** 申报类型	CHAR(1)	必填	固定填写1 **/
		private String declareType;

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
	}
	
	public static class ModifyCancel implements Serializable{
		
		private static final long serialVersionUID = 7134231396971012034L;

		/** 电商企业编码	VARCHAR2(20)	必填	电商平台下的商家备案编号 **/
		private String eCommerceCode;
		
		/** 电商平台编码	VARCHAR2(20)	必填	企业在跨境电商通关服务平台的备案编号 **/
		private String eCompanyCode;
		
		/** 分运单号	VARCHAR2(50)	必填	 **/
		private String subCarriageNo;
		
		/** 删单原因	NVARCHAR2(255)	必填 **/
		private String reason;

		public String geteCommerceCode() {
			return eCommerceCode;
		}

		public void seteCommerceCode(String eCommerceCode) {
			this.eCommerceCode = eCommerceCode;
		}

		public String geteCompanyCode() {
			return eCompanyCode;
		}

		public void seteCompanyCode(String eCompanyCode) {
			this.eCompanyCode = eCompanyCode;
		}

		public String getSubCarriageNo() {
			return subCarriageNo;
		}

		public void setSubCarriageNo(String subCarriageNo) {
			this.subCarriageNo = subCarriageNo;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}
}
