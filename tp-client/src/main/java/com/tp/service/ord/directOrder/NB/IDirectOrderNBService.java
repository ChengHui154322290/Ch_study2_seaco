package com.tp.service.ord.directOrder.NB;

import java.util.List;

import com.tp.dto.ord.directOrderNB.DirectOrderNBDto;
import com.tp.dto.ord.directOrderNB.RetMessageNBDto;
import com.tp.model.ord.SubOrder;
import com.tp.model.prd.ItemSku;

public interface IDirectOrderNBService{
	/**
	 * 推送直邮订单
	 * @param 
	 * @return
	 */
	RetMessageNBDto pushDirectOrderNB(SubOrder subOrder,List<ItemSku> itemSkuList);
	RetMessageNBDto pushDirectOrderNBTest(SubOrder subOrder,List<ItemSku> itemSkuList);
	/**
	 * 获取运单信息
	 * @param 
	 * @return
	 */
	RetMessageNBDto getOrderMessageNB(String saleOrderCode);
	
	/**
	 * 取消订单
	 * @param 
	 * @return
	 */
	RetMessageNBDto cancelOrderNB(String saleOrderCode);
	
	/**
	 * 获取商品信息
	 * @param 
	 * @return
	 */
	RetMessageNBDto getProductListNB(String page,String limit);
	
	/**
	 * 获取库存信息
	 * @param 
	 * @return
	 */
	RetMessageNBDto getProductStoreNB(List<String> productNumberCodes);
	RetMessageNBDto getProductStoreNBTest(List<String> productNumberCodes);
}
