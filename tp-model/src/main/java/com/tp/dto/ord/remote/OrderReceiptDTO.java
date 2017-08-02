package com.tp.dto.ord.remote;

import com.tp.common.vo.ord.OrderReceiptConstant.ReceiptContentType;
import com.tp.common.vo.ord.OrderReceiptConstant.ReceiptTitleType;
import com.tp.common.vo.ord.OrderReceiptConstant.ReceiptType;
import com.tp.model.ord.OrderReceipt;

/**
 * 订单发票DTO
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderReceiptDTO extends OrderReceipt {

	private static final long serialVersionUID = 7070872784217813262L;
	
	public String getTypeStr() {
		return ReceiptType.getCnName(getType());
	}
	public String getTitleTypeStr() {
		return ReceiptTitleType.getCnName(getTitleType());
	}
	public String getContentTypeStr() {
		return ReceiptContentType.getCnName(getContentType());
	}
	public String getTitleStr() {
		return ReceiptTitleType.PERSON.code.equals(getTitleType()) ? ReceiptTitleType.PERSON.cnName : getTitle();
	}
}
