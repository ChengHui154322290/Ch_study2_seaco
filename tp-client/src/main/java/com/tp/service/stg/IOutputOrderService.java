package com.tp.service.stg;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.StorageConstant.StorageType;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.OutputOrderDto;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.stg.OutputOrder;
import com.tp.model.stg.OutputOrderDetail;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 下单完成后向仓库发送发货出库订单接口
  */
public interface IOutputOrderService extends IBaseService<OutputOrder>{
	/**
	 * 订单发货通知接口
	 * 
	 * @param orderDto
	 * 		订单信息
	 * @param type
	 * 		订单类型
	 * 
	 * @return
	 */
	public ResultInfo<String> deliverOutputOrder(OutputOrderDto orderDto,StorageType type)throws Exception;
	/**
	 * 发送出库指令
	 * @param outputOrderDO
	 * @param detailDOs
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws DAOException
	 */
	public ResultInfo<String> sendOutputOrder(OutputOrder outputOrderDO,
			List<OutputOrderDetail> detailDOs) throws RemoteException,
			MalformedURLException,Exception;
	
	/**
	 * 采购订单退货出库接口
	 * @param orderDto
	 * @param thirdpart
	 * @return
	 * 		<pre>
	 * 			001 请求参数有误
	 * 			002 该订单已发货出库
	 * 			003 发送出库单指令异常
	 * 			004 暂不支持的出库类型
	 * 			005 出库指令已发送成功，不要重复发送
	 * 			006 多次重试发送出库指令失败
     *		
	 * 		</pre>
	 * @throws StorageServiceException
	 */
	public ResultInfo<String> returnOutputOrder(OutputOrderDto orderDto)throws Exception;
	/**
	 * 根据订单编号进行商品出库操作
	 * 
	 * @return
	 */
	public ResultInfo<Boolean> exWarehouseService(OrderDelivery deliverDTO)throws Exception;
	/**
	 * 根据订单编号批量进行商品出库操作
	 * 
	 * @return
	 */
	public ResultOrderDeliverDTO batchExWarehouseService(List<OrderDelivery> deliverDTOs);
	
	/**
	 * 取消发货，自营商品将向仓库发送取消出库指令，商家商品
	 * 
	 * @param orderCode
	 * 		子订单编号
	 * @return
	 * 		<pre>
	 * 			返回值code说明：
	 *			000 取消成功 
	 * 			002 取消失败，订单已发运或者订单不存在
	 * 			003 取消失败，订单进入仓内作业（此时订单可拦截，可根据客户预设，视为取消成功或者失败）
	 * 			009 取消失败，取消时发生异常
	 * 		</pre>
	 * @return
	 * @throws Exception 
	 */
	public ResultInfo<String> cancelOutputOrder(Long orderCode);
	/**
	 * 查询失败的指令
	 * @param maxTime
	 * 		最大失败次数
	 * @return
	 */
	public List<OutputOrder> selectFailOutputOrder(int maxTime, int limitSize);
	/**
	 * 根据指令的id查询指令的明细
	 * @param outputOrderId
	 * @return
	 */
	public List<OutputOrderDetail> selectOuputorderDetailByOrderId(Long outputOrderId);
	
	public List<OutputOrder> selectOutputOrderByOrderCode(String orderCode);
}
