/**
 * 
 */
package com.tp.service.stg;

import java.util.List;

import com.tp.result.stg.ASNsResult;
import com.tp.result.stg.OrdersResult;
import com.tp.result.stg.OutputBackShipResult;
import com.tp.result.stg.ResponseResult;
import com.tp.result.stg.StockResult;

/**
 * @author szy
 * （标杆）各种查询接口
 */
public interface IBMLSoaService {

	/**
	 * 
	 * <pre>
	 *  根据商品订单号返回订单状态
	 * </pre>
	 *
	 * @param orderCode
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 */
      public ResponseResult  queryOrderStatusByOrderCode (String orderCode);
      
      
      
      
      /**
       * 
       * <pre>
       * 根据sku查询标杆库存信息
       * </pre>
       *
       * @param sku
       * @return
       * @throws StorageServiceException
       */
      public List<StockResult> searchInventory(String sku);
      
      
      /**
       * 
       * <pre>
       *  根据商品订单号查询发货信息
       * </pre>
       *
       * @param orderNo
       * @return
       * @throws StorageServiceException
       */
      public List<OutputBackShipResult> shipmentInfoQueryByOrderId(String orderNo);
      
      
      
      /**
       * 
       * <pre>
       *  根据客户采购订单号查询入库单明细
       * </pre>
       *
       * @param orderNo
       * @return
       * @throws StorageServiceException
       */
      public List<ASNsResult> noticeOfArrivalQueryById(String orderNo);

      /**
       * 
       * <pre>
       * 根据订单订单号查询出库明细
       * </pre>
       *
       * @param orderNo
       * @return
       * @throws StorageServiceException
       */
  	  public List<OrdersResult> orderDetailQueryById(String orderNo );
	
}
