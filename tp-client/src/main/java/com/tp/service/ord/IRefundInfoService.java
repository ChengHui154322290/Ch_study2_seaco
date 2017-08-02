package com.tp.service.ord;

import com.tp.model.ord.RefundInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 退款单接口
  */
public interface IRefundInfoService extends IBaseService<RefundInfo>{

	RefundInfo selectByRefundNo(Long refundNo);

	void operateAfterRefund(Long refundNo, Boolean isSuccess);

}
