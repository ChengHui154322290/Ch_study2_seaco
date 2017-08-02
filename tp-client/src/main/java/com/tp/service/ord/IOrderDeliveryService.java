package com.tp.service.ord;

import java.util.List;

import com.tp.dto.mmp.ReturnData;
import com.tp.dto.ord.remote.ExpressModifyDTO;
import com.tp.model.ord.OrderDelivery;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 订单物流状态表接口
  */
public interface IOrderDeliveryService extends IBaseService<OrderDelivery>{

	OrderDelivery selectOneBySubOrderCode(Long subCode);

	List<OrderDelivery> selectListBySubCodeList(List<Long> subCodeList);

	Integer batchInsert(List<OrderDelivery> orderDeliveryDOList);

	List<OrderDelivery> queryNotSuccessPostKuaidi100List(OrderDelivery orderDelivery);

	Integer batchUpdatePostKuaidi100(List<OrderDelivery> orderDeliveryDOList);

	Integer updatePostKuaidi100(OrderDelivery orderDelivery);

	List<OrderDelivery> selectListBySubCodeAndPackageNo(Long subOrderCode,String packageNo);
	/**
	 * @param salesExpressModifyDTO
	 * @return
	 */
	ReturnData modifyExpressNo(ExpressModifyDTO salesExpressModifyDTO);

	/**
	 * @param passExpressModifyList
	 * @return
	 */
	List<com.tp.dto.mmp.ReturnData> batchModifyExpressNo(List<ExpressModifyDTO> passExpressModifyList);
}
