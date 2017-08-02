package com.tp.model.ord.JKF;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfTaxIsNeedResult extends JkfBaseDO{
	private static final long serialVersionUID = -5300801090897298475L;
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
		private static final long serialVersionUID = 687085579362116115L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{
		private static final long serialVersionUID = 2667571863169720377L;
		private JkfSign jkfSign;
		private JkfTaxIsNeedDto jkfTaxIsNeedDto;
		public JkfSign getJkfSign() {
			return jkfSign;
		}
		public void setJkfSign(JkfSign jkfSign) {
			this.jkfSign = jkfSign;
		}
		public JkfTaxIsNeedDto getJkfTaxIsNeedDto() {
			return jkfTaxIsNeedDto;
		}
		public void setJkfTaxIsNeedDto(JkfTaxIsNeedDto jkfTaxIsNeedDto) {
			this.jkfTaxIsNeedDto = jkfTaxIsNeedDto;
		}		
	}
	
	/** 签名信息 **/
	public static class JkfSign implements Serializable{
		private static final long serialVersionUID = -3026638228510621882L;

		/**  接收方企业备案编号	VARCHAR2(20)	非空  **/
		private String companyCode;
		
		/**  业务编号	VARCHAR2(100)	非空	运单号  **/
		private String businessNo;
		
		/**  业务类型	VARCHAR2(30)	非空	业务类型TAXISNEED  **/
		private String businessType;

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
	}
	
	
	/**  税款划转信息  **/
	public static class JkfTaxIsNeedDto implements Serializable{
		private static final long serialVersionUID = 1750967635504443343L;

		/**  个人物品申报单	VARCHAR2(18)	必填  **/
		private String personalGoodsFormNo ;
		
		/**  税款金额	VARCHAR2(60)	必填  **/
		private String taxAmount;
		
		/** 应征标记	VARCHAR2(1)	必填	0免征，1应征   **/
		private String isNeed;

		public String getPersonalGoodsFormNo() {
			return personalGoodsFormNo;
		}

		public void setPersonalGoodsFormNo(String personalGoodsFormNo) {
			this.personalGoodsFormNo = personalGoodsFormNo;
		}

		public String getTaxAmount() {
			return taxAmount;
		}

		public void setTaxAmount(String taxAmount) {
			this.taxAmount = taxAmount;
		}

		public String getIsNeed() {
			return isNeed;
		}

		public void setIsNeed(String isNeed) {
			this.isNeed = isNeed;
		}	
	}
}
