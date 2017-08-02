package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.CancelInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 取消单接口
  */
public interface ICancelInfoService extends IBaseService<CancelInfo>{

	/**
	 * 根据退款编码查询取消单
	 * @param refundCodeList
	 * @return
	 */
	List<CancelInfo> queryCancelItemListByRefundNoList(List<Long> refundCodeList);
	
	/**
	 * 取消订单，造成取消单
	 * @param subOrder
	 * @param orderItemList
	 * @param refundNo
	 * @return
	 */
	CancelInfo addCancelInfo(SubOrder subOrder, List<OrderItem> orderItemList,Long refundNo);

}
