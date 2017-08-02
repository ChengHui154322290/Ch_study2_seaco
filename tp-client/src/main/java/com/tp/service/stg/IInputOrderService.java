package com.tp.service.stg;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InputOrderDto;
import com.tp.model.stg.InputOrder;
import com.tp.model.stg.InputOrderDetail;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 入库订单接口
  */
public interface IInputOrderService extends IBaseService<InputOrder>{
	/**
	 * 商品入库预约单
	 * @param inputOrderDto
	 * 		入库商品信息
	 * @return
	 * 	<pre>
	 * 		003-发送采购单入库指令异常
	 * 		001-参数错误
	 * 	</pre>
	 */
	public ResultInfo<String> sendInputOrder(InputOrderDto inputOrderDto)throws Exception;

	/**
	 * 向标杆系统发送入库指令（采购入库预约）
	 * @param orderDo
	 * @param detailDOs
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws DAOException
	 */
	public ResultInfo<String> sendInputOrderToWms(InputOrder orderDo,
			List<InputOrderDetail> detailDOs) throws RemoteException,
			MalformedURLException,Exception;
	
	/**
	 * 查询发送指令失败次数不超过给定值的出库单信息
	 * @param maxTime
	 * 		最大失败次数
	 * @param limitSize
	 * 		最大条数
	 * @return
	 */
	public List<InputOrder> selectFailInputOrder(int maxTime, int limitSize);

	/**
	 * 根据入库指令查询入库明细
	 * @param inputOrderId
	 * @return
	 */
	public List<InputOrderDetail> selectOrderDetailByOrderId(Long inputOrderId);

	public List<InputOrder> selectInputOrderByOrderCode(String orderCode);
}
