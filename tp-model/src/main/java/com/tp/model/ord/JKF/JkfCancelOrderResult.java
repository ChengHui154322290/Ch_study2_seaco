/**
 * 
 */
package com.tp.model.ord.JKF;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfCancelOrderResult extends JkfBaseDO{
	private static final long serialVersionUID = -880489988123641872L;
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
		private static final long serialVersionUID = 9097142864433976620L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{
	
		private static final long serialVersionUID = -973214813674184928L;
		private ModifyCancelResult modifyCancelResult;

		public ModifyCancelResult getModifyCancelResult() {
			return modifyCancelResult;
		}

		public void setModifyCancelResult(ModifyCancelResult modifyCancelResult) {
			this.modifyCancelResult = modifyCancelResult;
		}	
	}
	
	/** 删单信息回执 **/
	public static class ModifyCancelResult implements Serializable{
		
		private static final long serialVersionUID = -3092591287994940641L;

		/** 业务编号	VARCHAR2(100)	必填 **/
		private String businessNo;
		
		/** 电商平台备案编号	VARCHAR2(20)	必填 **/
		private String companyCode;
		
		/** 审批结果	VARCHAR2(2)	必填	21:撤销成功 22:撤销失败**/
		private String approveResult;
		
		/** 审批意见	VARCHAR2(70)	必填	海关审批意见 **/
		private String approveComment;
		
		/** 处理时间	VARCHAR2(25)	必填	格式：yyyyMMddHHmmss **/
		private String processTime;

		public String getBusinessNo() {
			return businessNo;
		}

		public void setBusinessNo(String businessNo) {
			this.businessNo = businessNo;
		}

		public String getCompanyCode() {
			return companyCode;
		}

		public void setCompanyCode(String companyCode) {
			this.companyCode = companyCode;
		}

		public String getApproveResult() {
			return approveResult;
		}

		public void setApproveResult(String approveResult) {
			this.approveResult = approveResult;
		}

		public String getApproveComment() {
			return approveComment;
		}

		public void setApproveComment(String approveComment) {
			this.approveComment = approveComment;
		}

		public String getProcessTime() {
			return processTime;
		}

		public void setProcessTime(String processTime) {
			this.processTime = processTime;
		}
	}
}
