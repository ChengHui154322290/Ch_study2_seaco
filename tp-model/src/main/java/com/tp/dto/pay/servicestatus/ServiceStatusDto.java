package com.tp.dto.pay.servicestatus;

import java.io.Serializable;

public class ServiceStatusDto implements Serializable {
	private static final long serialVersionUID = 3363401699885662789L;

	private Boolean masterDbOk;
	private Boolean slaveDbOk;
	private Boolean mqOk;

	public ServiceStatusDto() {
	}

	public Boolean getMasterDbOk() {
		return masterDbOk;
	}

	public void setMasterDbOk(Boolean masterDbOk) {
		this.masterDbOk = masterDbOk;
	}

	public Boolean getSlaveDbOk() {
		return slaveDbOk;
	}

	public void setSlaveDbOk(Boolean slaveDbOk) {
		this.slaveDbOk = slaveDbOk;
	}

	public Boolean getMqOk() {
		return mqOk;
	}

	public void setMqOk(Boolean mqOk) {
		this.mqOk = mqOk;
	}

	public boolean isAllOk() {
		return true;
	}

	public String getSummary() {
		if (isAllOk()) {
			return "所有服务正常：";
		} else {
			return "某些服务不正常：";
		}
	}
}
