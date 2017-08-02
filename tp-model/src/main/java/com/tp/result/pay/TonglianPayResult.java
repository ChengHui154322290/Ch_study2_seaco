package com.tp.result.pay;

import java.io.Serializable;

/**
 * 通联支付接口返回
 * @author chenghui
 *
 */
public class TonglianPayResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TxInfo txInfo;
	
	private String signMsg ; 
	
	public TxInfo getTxInfo() {
		return txInfo;
	}

	public void setTxInfo(TxInfo txInfo) {
		this.txInfo = txInfo;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public static class TxInfo implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String retCode;
		
		private String retMsg;
		
		private String paymentOrderNo;
		
		private String charset;

		public String getRetCode() {
			return retCode;
		}

		public void setRetCode(String retCode) {
			this.retCode = retCode;
		}

		public String getRetMsg() {
			return retMsg;
		}

		public void setRetMsg(String retMsg) {
			this.retMsg = retMsg;
		}

		public String getPaymentOrderNo() {
			return paymentOrderNo;
		}

		public void setPaymentOrderNo(String paymentOrderNo) {
			this.paymentOrderNo = paymentOrderNo;
		}

		public String getCharset() {
			return charset;
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}
		
		
	}
}
