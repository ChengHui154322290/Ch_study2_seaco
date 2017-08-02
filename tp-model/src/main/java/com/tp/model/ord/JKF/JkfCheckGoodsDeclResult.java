package com.tp.model.ord.JKF;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *	个人物品申报单状态查询接口返回结果 
 */

@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfCheckGoodsDeclResult extends JkfBaseDO{
	private static final long serialVersionUID = -4764965990796957914L;
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
		private static final long serialVersionUID = 461623372837970025L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{
		private static final long serialVersionUID = 3551427355333500376L;
		private CheckGoodsDecl checkGoodsDecl;

		public CheckGoodsDecl getCheckGoodsDecl() {
			return checkGoodsDecl;
		}

		public void setCheckGoodsDecl(CheckGoodsDecl checkGoodsDecl) {
			this.checkGoodsDecl = checkGoodsDecl;
		}		
	}
	
	public static class CheckGoodsDecl implements Serializable{
		private static final long serialVersionUID = -2636702499613757167L;

		/**  分运单号	VARCHAR2(50)	非空  **/
		private String wayBillNo;
		
		/**  申报单是否存在	CHAR(1)	非空	1存在，0不存在  **/
		private Integer isDeclareExist;
		
		/**  订单是否存在	CHAR(1)	非空	1存在，0不存在  **/
		private Integer isOrderExist;
		
		/**  支付单是否存在	CHAR(1)	非空	1存在，0不存在  **/
		private Integer isPayInfoExist;
		
		/**  运单是否存在	CHAR(1)	非空	1存在，0不存在  **/
		private Integer isWayBillExist;

		public String getWayBillNo() {
			return wayBillNo;
		}

		public void setWayBillNo(String wayBillNo) {
			this.wayBillNo = wayBillNo;
		}

		public Integer getIsDeclareExist() {
			return isDeclareExist;
		}

		public void setIsDeclareExist(Integer isDeclareExist) {
			this.isDeclareExist = isDeclareExist;
		}

		public Integer getIsOrderExist() {
			return isOrderExist;
		}

		public void setIsOrderExist(Integer isOrderExist) {
			this.isOrderExist = isOrderExist;
		}

		public Integer getIsPayInfoExist() {
			return isPayInfoExist;
		}

		public void setIsPayInfoExist(Integer isPayInfoExist) {
			this.isPayInfoExist = isPayInfoExist;
		}

		public Integer getIsWayBillExist() {
			return isWayBillExist;
		}

		public void setIsWayBillExist(Integer isWayBillExist) {
			this.isWayBillExist = isWayBillExist;
		}		
	}
}
