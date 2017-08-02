/**
 * 
 */
package com.tp.m.ao.wms;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.wms.sto.STOStockoutBackDto;
import com.tp.dto.wms.sto.STOStockoutBackItemDto;
import com.tp.dto.wms.sto.STOStocksyncInfoDto;
import com.tp.dto.wms.sto.STOStocksyncInfoItemDto;
import com.tp.model.wms.StockoutBack;
import com.tp.model.wms.StocksyncInfo;
import com.tp.proxy.wms.StockoutBackProxy;
import com.tp.proxy.wms.StocksyncInfoProxy;

/**
 * @author Administrator
 *
 */
@Service
public class STOCallbackAO {
	
	private static final Logger logger = LoggerFactory.getLogger(STOCallbackAO.class);
	
	@Autowired
	private StockoutBackProxy stockoutBackProxy;
	
	@Autowired
	private StocksyncInfoProxy stocksyncInfoProxy;
	
	//订单状态回传
	public boolean stockoutCallback(STOStockoutBackDto dto){
		if (null == dto) return false;
		if(!validate(dto)) return false;
		try {
			List<StockoutBack> stockoutBackList = convertToStockoutBack(dto);
			stockoutBackProxy.processStockoutBack(stockoutBackList);
			return true;
		} catch (Exception e) {
			logger.error("处理出库单回执异常", e);
			return false;
		}
	}
	//库存状态回传
	public boolean skuInventoryCallback(STOStocksyncInfoDto dto){
		if (null == dto) return false;
		try {
			List<StocksyncInfo> infos = convertToStocksyncInfo(dto);
			stocksyncInfoProxy.processStocksync(infos, WMSCode.STO_HZ);
		} catch (Exception e) {
			logger.error("库存回传处理异常", e);
			return false;
		}
		return true;
	}
	
	/** 数据校验 */
	private boolean validate(STOStockoutBackDto dto){
		if (CollectionUtils.isEmpty(dto.getOrderList())) {
			return false;
		}
		return true;
	}
	
	
	private List<StockoutBack> convertToStockoutBack(STOStockoutBackDto dto){
		List<StockoutBack> backList = new ArrayList<>();
		for (STOStockoutBackItemDto itemDto : dto.getOrderList()) {
			if (itemDto.getStatus() == 1) {
				StockoutBack back = new StockoutBack();
				back.setExpressNo(itemDto.getMailNo());
				back.setOrderCode(itemDto.getTxLogisticID());
				back.setStatus(itemDto.getStatus());
				back.setWmsCode(WMSCode.STO_HZ.code);
				backList.add(back);
			}
		}
		return backList;
	}
	
	private List<StocksyncInfo> convertToStocksyncInfo(STOStocksyncInfoDto dto){
		List<StocksyncInfo> infoList = new ArrayList<>();
		for (STOStocksyncInfoItemDto itemDto : dto.getInventoryList()) {
			StocksyncInfo info = new StocksyncInfo();
			info.setWmsCode(WMSCode.STO_HZ.code);
			info.setStockSku(itemDto.getItemsku());
			info.setSkuName(itemDto.getItemName());
			info.setStockInventory(itemDto.getSurplusInventory().longValue());
			infoList.add(info);
		}
		return infoList;
	}
}
