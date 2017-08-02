/**
 * 
 */
package com.tp.model.ord.ceb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.tp.model.ord.JKF.JkfBaseDO;

/**
 * @author Administrator
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "orderReturn" })
@XmlRootElement(name = "CEB312Message")
public class CebOrderResponse extends JkfBaseDO{
	private static final long serialVersionUID = -7370475885440658339L;
	@XmlElement(name = "OrderReturn", required = true)
	protected List<OrderReturn> orderReturn;
	@XmlAttribute(name = "guid", required = true)
	protected String guid;
	@XmlAttribute(name = "version", required = true)
	protected String version;

	public List<OrderReturn> getOrderReturn() {
		if (orderReturn == null) {
			orderReturn = new ArrayList<OrderReturn>();
		}
		return this.orderReturn;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String value) {
		this.guid = value;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String value) {
		this.version = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "guid", "ebpCode", "ebcCode", "orderNo", "returnStatus", "returnTime",
			"returnInfo" })
	@XmlRootElement(name = "OrderReturn")
	public static class OrderReturn implements Serializable{
		private static final long serialVersionUID = -3200222688105809418L;
		@XmlElement(required = true)
		protected String guid;
		@XmlElement(required = true)
		protected String ebpCode;
		@XmlElement(required = true)
		protected String ebcCode;
		@XmlElement(required = true)
		protected String orderNo;
		@XmlElement(required = true)
		protected String returnStatus;
		@XmlElement(required = true)
		protected String returnTime;
		@XmlElement(required = true)
		protected String returnInfo;

		public String getGuid() {
			return guid;
		}

		public void setGuid(String value) {
			this.guid = value;
		}

		public String getEbpCode() {
			return ebpCode;
		}

		public void setEbpCode(String value) {
			this.ebpCode = value;
		}

		public String getEbcCode() {
			return ebcCode;
		}

		public void setEbcCode(String value) {
			this.ebcCode = value;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String value) {
			this.orderNo = value;
		}

		public String getReturnStatus() {
			return returnStatus;
		}

		public void setReturnStatus(String value) {
			this.returnStatus = value;
		}

		public String getReturnTime() {
			return returnTime;
		}

		public void setReturnTime(String value) {
			this.returnTime = value;
		}

		public String getReturnInfo() {
			return returnInfo;
		}

		public void setReturnInfo(String value) {
			this.returnInfo = value;
		}

	}
}
