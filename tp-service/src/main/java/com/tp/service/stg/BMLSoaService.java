/**
 * 
 */
package com.tp.service.stg;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.stg.BML.ASNsDetail;
import com.tp.dto.stg.BML.ASNsDto;
import com.tp.dto.stg.BML.OrderDetails;
import com.tp.dto.stg.BML.OrdersDto;
import com.tp.dto.stg.BML.OutPutBackShipSkuDto;
import com.tp.dto.stg.BML.OutputBackShipDto;
import com.tp.dto.stg.BML.ResponseDto;
import com.tp.dto.stg.BML.StockDto;
import com.tp.result.stg.ASNsDetailResult;
import com.tp.result.stg.ASNsResult;
import com.tp.result.stg.OrderDetailsResult;
import com.tp.result.stg.OrdersResult;
import com.tp.result.stg.OutPutBackShipSkuResult;
import com.tp.result.stg.OutputBackShipResult;
import com.tp.result.stg.ResponseResult;
import com.tp.result.stg.StockResult;
import com.tp.service.stg.IBMLSoaService;
import com.tp.service.stg.client.BMLSoapClient;

/**
 * @author szy
 *
 */
@Service
public class BMLSoaService implements IBMLSoaService {

	Logger logger = LoggerFactory.getLogger(BMLSoaService.class);
	
	@Autowired
	private BMLSoapClient bMLSoapClient;
    
	@Override
	public ResponseResult queryOrderStatusByOrderCode(String orderCode)  {
		if(orderCode==null || orderCode.trim().equals("")){
			
			return null;
		}
		try {
			ResponseDto responseDto = bMLSoapClient.searchOrderStatus(orderCode);
			ResponseResult responseResult =new ResponseResult();
			BeanUtils.copyProperties(responseDto, responseResult);
			return responseResult ;	
		} catch (RemoteException | MalformedURLException | ServiceException e) {
			logger.error(e .getMessage());
		}
		return null;
	}

	@Override
	public List<StockResult> searchInventory(String sku){
		if (sku == null || sku.trim().equals("")) {
			return null;
		}
		try {
	            List<StockDto> stockDtoList= bMLSoapClient.searchInventory(sku);
	            List<StockResult> stockResults = new ArrayList<StockResult>();
	            if(CollectionUtils.isNotEmpty(stockDtoList)){
	            	StockResult stockResult =null;
	            	for (StockDto stockDto2 : stockDtoList) {
	            		 stockResult =new StockResult();
	            		 BeanUtils.copyProperties(stockDto2, stockResult);
	            		 stockResults.add(stockResult);
	            		 
	            		 
	            		 
					}
	            }
	            return stockResults;
	            
		} catch (RemoteException | MalformedURLException | ServiceException e) {
			logger.error(e .getMessage());
		}
		return null;
	}

	@Override
	public List<OutputBackShipResult> shipmentInfoQueryByOrderId(String orderNo){
		if (orderNo == null || orderNo.trim().equals("")) {
			return null;
		}
		try {
			List<OutputBackShipDto> outputBackShipDto = bMLSoapClient.shipmentInfoQueryByOrderId(orderNo);
			
			List<OutputBackShipResult> backShipResults = new ArrayList<OutputBackShipResult>();
			OutputBackShipResult backShipResult = null;
			for (OutputBackShipDto outputBackShipDto2 : outputBackShipDto) {
				backShipResult = new OutputBackShipResult();
				BeanUtils.copyProperties(outputBackShipDto2, backShipResult);
				backShipResults.add(backShipResult);
				List<OutPutBackShipSkuDto> backShipDtos = outputBackShipDto2.getSend();
				List<OutPutBackShipSkuResult> backShipSkuResults = new ArrayList<OutPutBackShipSkuResult>();
				for (OutPutBackShipSkuDto outPutBackShipSkuDto : backShipDtos) {
					OutPutBackShipSkuResult backShipSkuResult = new OutPutBackShipSkuResult();
					backShipSkuResult.setSkuCode(outPutBackShipSkuDto.getSkuCode());
					backShipSkuResult.setSkuNum(outPutBackShipSkuDto.getSkuNum());
					backShipSkuResults.add(backShipSkuResult);
				}
				backShipResult.setSend(backShipSkuResults);
			}
           return backShipResults;
		} catch (RemoteException | MalformedURLException | ServiceException e) {
			logger.error(e .getMessage());
		}
		
		return null;
	}

	@Override
	public List<ASNsResult> noticeOfArrivalQueryById(String orderNo){
		if (orderNo == null || orderNo.trim().equals("")) {
			return null;
		}
		try {
			List<ASNsDto> list = bMLSoapClient.noticeOfArrivalQueryById(orderNo);
			List<ASNsResult> asNsResults = new ArrayList<ASNsResult>();
			for (ASNsDto resource : list) {
				ASNsResult target = new ASNsResult();
				BeanUtils.copyProperties(resource, target);
				asNsResults.add(target);
				
				List<ASNsDetailResult> asNsDetailResults = new ArrayList<ASNsDetailResult>(); 
				List<ASNsDetail> asNsDetails = resource.getDetails();
				for (ASNsDetail asNsDetail : asNsDetails) {
					ASNsDetailResult asNsDetailResult = new ASNsDetailResult();
					asNsDetailResult.setExpectedQty(asNsDetail.getExpectedQty());
					asNsDetailResult.setReceivedQty(asNsDetail.getReceivedQty());
					asNsDetailResult.setReceivedTime(asNsDetail.getReceivedTime());
					asNsDetailResult.setSkuCode(asNsDetail.getSkuCode());
					asNsDetailResults.add(asNsDetailResult);
				}
				target.setDetails(asNsDetailResults);
				
			}
			return asNsResults;
		} catch (RemoteException | MalformedURLException | ServiceException e) {
			logger.error(e .getMessage());
		}
		return null;
	}

	@Override
	public List<OrdersResult> orderDetailQueryById(String orderNo){
		if (orderNo == null || orderNo.trim().equals("")) {
			return null;
		}
		try {
			List<OrdersDto> list = bMLSoapClient.orderDetailQueryById(orderNo);
			List<OrdersResult> ordersResults =new ArrayList<OrdersResult>();
			for(OrdersDto ordersDto : list){
				OrdersResult ordersResult =new  OrdersResult();
				BeanUtils.copyProperties(ordersDto, ordersResult);
				ordersResults.add(ordersResult);
				
				List<OrderDetails> details = ordersDto.getList();
				List<OrderDetailsResult> detailsResults = new ArrayList<OrderDetailsResult>(); 
				for (OrderDetails orderDetails : details) {
					OrderDetailsResult detailsResult = new OrderDetailsResult();
					detailsResult.setPrice(orderDetails.getPrice());
					detailsResult.setQtyShipped(orderDetails.getQtyShipped());
					detailsResult.setSkuCode(orderDetails.getSkuCode());
					detailsResults.add(detailsResult);
				}
				ordersResult.setList(detailsResults);
			}
			return ordersResults;
		} catch (RemoteException | MalformedURLException | ServiceException e) {
			logger.error(e .getMessage());
		}
		return null;
	}
	
}
