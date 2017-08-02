package com.tp.service.ord;

import com.tp.model.ord.ReceiptDetail;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 发票明细表接口
  */
public interface IReceiptDetailService extends IBaseService<ReceiptDetail>{

	ReceiptDetail selectListBySubOrderCode(Long subOrderCode);

	int updateBySubOrderCode(ReceiptDetail receipt);

}
