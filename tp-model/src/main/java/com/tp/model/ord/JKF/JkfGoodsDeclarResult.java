package com.tp.model.ord.JKF;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 企业从跨境电商通关服务平台接收个人物品申报单审单结果
 * 个人物品申报电子口岸回执
 */

@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfGoodsDeclarResult extends JkfBaseDO{
	private static final long serialVersionUID = -4371782828437317791L;
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
		private static final long serialVersionUID = 6486889475093320103L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{
		private static final long serialVersionUID = -4868335638287988478L;

		private JkfSign jkfSign;
		
		private JkfGoodsDeclar jkfGoodsDeclar;

		public JkfSign getJkfSign() {
			return jkfSign;
		}

		public void setJkfSign(JkfSign jkfSign) {
			this.jkfSign = jkfSign;
		}

		public JkfGoodsDeclar getJkfGoodsDeclar() {
			return jkfGoodsDeclar;
		}

		public void setJkfGoodsDeclar(JkfGoodsDeclar jkfGoodsDeclar) {
			this.jkfGoodsDeclar = jkfGoodsDeclar;
		}		
	}
	
	/** 签名信息 **/
	public static class JkfSign implements Serializable{
		private static final long serialVersionUID = -5940061544800512362L;

		/** 接收方备案编号	VARCHAR2(10)	必填	 **/
		private String companyCode;
		
		/** 业务编号	VARCHAR2 (256)	必填	4位电商编号+14位企业流水，电商平台/物流企业生成后发送服务平台 **/
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
	
	/** 个人物品申报单审批结果信息 **/
	public static class JkfGoodsDeclar implements Serializable{
		private static final long serialVersionUID = 5483175608573542957L;

		/** 个人物品申报单编号	VARCHAR2 (18)	是	关区号（4位）+年份（4位）+进出口标志（1位）+流水号（9位），服务平台生成后，反馈管理平台及电商平台 **/
		private String personalGoodsFormNo;
		
		/** 个人物品申报单状态	VARCHAR2 (2)		见参数表 **/
		private String approveResult;
		
		/** 四位审单状态码+冒号+海关审批意见	VARCHAR2(70)		四位审单状态码+冒号+审批意见，格式类似：3201:支付类型不存在. 四位审单状态码定义见参数文档。**/
		private String approveComment;
		
		/** 处理时间	VARCHAR2(30)		海关处理时间 格式要求：20140623101024 **/
		private String processTime;

		public String getPersonalGoodsFormNo() {
			return personalGoodsFormNo;
		}

		public void setPersonalGoodsFormNo(String personalGoodsFormNo) {
			this.personalGoodsFormNo = personalGoodsFormNo;
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
