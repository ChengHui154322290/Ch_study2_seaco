package com.tp.model.stg.vo.feedback;

import java.io.Serializable;

/**
 * 
 * @author szy
 *
 */
public class InvoicesVO implements Serializable{

	private static final long serialVersionUID = -2895990853031293760L;
	
	/**发票实体类**/
	private InvoiceVO invoice;

	public InvoiceVO getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceVO invoice) {
		this.invoice = invoice;
	}

	
	
	
}
