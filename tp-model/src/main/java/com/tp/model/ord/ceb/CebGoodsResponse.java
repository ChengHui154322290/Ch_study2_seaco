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
@XmlType(name = "", propOrder = { "inventoryReturn" })
@XmlRootElement(name = "CEB622Message")
public class CebGoodsResponse extends JkfBaseDO{

	private static final long serialVersionUID = 711134606569491038L;
	@XmlElement(name = "InventoryReturn", required = true)
	protected List<InventoryReturn> inventoryReturn;
	@XmlAttribute(name = "guid", required = true)
	protected String guid;
	@XmlAttribute(name = "version", required = true)
	protected String version;

	public List<InventoryReturn> getInventoryReturn() {
		if (inventoryReturn == null) {
			inventoryReturn = new ArrayList<InventoryReturn>();
		}
		return this.inventoryReturn;
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
	@XmlType(name = "", propOrder = { "guid", "customsCode", "ebpCode", "ebcCode", "agentCode", "copNo", "preNo",
			"invtNo", "returnStatus", "returnTime", "returnInfo" })
	@XmlRootElement(name = "InventoryReturn")
	public static class InventoryReturn implements Serializable{
		private static final long serialVersionUID = -2763189599103619148L;
		@XmlElement(required = true)
		protected String guid;
		protected String customsCode;
		protected String ebpCode;
		protected String ebcCode;
		protected String agentCode;
		@XmlElement(required = true)
		protected String copNo;
		@XmlElement(required = true)
		protected String preNo;
		protected String invtNo;
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

		public String getCustomsCode() {
			return customsCode;
		}

		public void setCustomsCode(String value) {
			this.customsCode = value;
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

		public String getAgentCode() {
			return agentCode;
		}

		public void setAgentCode(String value) {
			this.agentCode = value;
		}

		public String getCopNo() {
			return copNo;
		}

		public void setCopNo(String value) {
			this.copNo = value;
		}

		public String getPreNo() {
			return preNo;
		}

		public void setPreNo(String value) {
			this.preNo = value;
		}

		public String getInvtNo() {
			return invtNo;
		}

		public void setInvtNo(String value) {
			this.invtNo = value;
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
