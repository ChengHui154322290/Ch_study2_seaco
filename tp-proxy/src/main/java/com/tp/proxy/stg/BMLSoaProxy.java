package com.tp.proxy.stg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.result.stg.ASNsResult;
import com.tp.result.stg.OrdersResult;
import com.tp.result.stg.OutputBackShipResult;
import com.tp.result.stg.ResponseResult;
import com.tp.result.stg.StockResult;
import com.tp.service.stg.IBMLSoaService;

@Service
public class BMLSoaProxy {
	
	@Autowired
    private IBMLSoaService bMLSoaService;
	/**
	 * 订单状态查询（标杆）
	 * <pre>
	 *  根据订单号,获取订单状态
	 * </pre>
	 *
	 * @param orderCode
	 * @return
	 */
	public ResponseResult  queryOrderStatusByOrderCode(String orderCode){
		return bMLSoaService.queryOrderStatusByOrderCode(orderCode);		
	}
	
	/**
	 * 库存查询（标杆）
	 * <pre>
	 * 根据sku,查询标杆库存信息
	 * </pre>
	 *
	 * @param sku
	 * @return
	 */
	public List<StockResult>  searchInventory(String sku){
		return bMLSoaService.searchInventory(sku);
	}
	
	/**
	 * 发货信息查询（标杆）
	 * @param orderNo
	 * @return
	 */
	public List<OutputBackShipResult> getOutputBackShipResult(String orderNo){
		return bMLSoaService.shipmentInfoQueryByOrderId(orderNo);
	}
	
	/**
	 * 根据订单编号获得订单出库明细（标杆）
	 * @param orderCode
	 */
	public List<OrdersResult> selectOrderDetailByCode(String orderCode) {
		List<OrdersResult> ordersResults = bMLSoaService.orderDetailQueryById(orderCode);
		return ordersResults;
	}
	/**
	 * 入库明细查询
	 * @param orderCode
	 * @return
	 */
	public List<ASNsResult> selectInputOrderByOrderCode(String orderCode){
		List<ASNsResult> asNsResult= bMLSoaService.noticeOfArrivalQueryById(orderCode);
		return asNsResult;
	}
	
}
