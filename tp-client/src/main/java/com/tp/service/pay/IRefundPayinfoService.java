package com.tp.service.pay;

import java.io.File;
import java.util.List;

import com.tp.model.pay.RefundPayinfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 退款支付明细表接口
  */
public interface IRefundPayinfoService extends IBaseService<RefundPayinfo>{
	
	/**
	 * 根据退款编号取得退款支付信息
	 * @param refundNo
	 * @return
	 */
	RefundPayinfo selectByRefundNo(Long refundNo);
	
	/**
	 * 按退款的业务编号批量查询
	 * 
	 * @param refundNos
	 * @return
	 */
	List<RefundPayinfo> selectByRefundNos(List<Long> refundNos);

	File getRefundFile(List<Long> refundNos);

	int updateDynamicByUnrefunded(RefundPayinfo refundPayinfoDO);
}
