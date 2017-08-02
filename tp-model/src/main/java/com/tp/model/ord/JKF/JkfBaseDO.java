package com.tp.model.ord.JKF;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import com.tp.common.vo.customs.JKFConstant.JKFBusinessType;
import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;

public class JkfBaseDO implements Serializable{

	private static final long serialVersionUID = -5738052020936324285L;
	
	private JKFFeedbackType receiptType;
	
	private JKFBusinessType businessType;

	@XmlTransient
	public JKFFeedbackType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(JKFFeedbackType receiptType) {
		this.receiptType = receiptType;
	}

	@XmlTransient
	public JKFBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(JKFBusinessType businessType) {
		this.businessType = businessType;
	}
}
