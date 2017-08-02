package com.tp.backend.controller.wms;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutBack;
import com.tp.model.wms.StockoutBackDetail;
import com.tp.model.wms.StockoutDetail;
import com.tp.proxy.wms.StockoutBackDetailProxy;
import com.tp.proxy.wms.StockoutBackProxy;

@Controller
@RequestMapping("/wms/stockoutBack/")
public class StockOutBackController {

	private static final Logger logger = LoggerFactory.getLogger(StockOutBackController.class);
	@Autowired
	private StockoutBackProxy stockoutBackProxy;
	@Autowired
	private StockoutBackDetailProxy outBackDetailProxy;
	
	public StockoutBackDetailProxy getOutBackDetailProxy() {
		return outBackDetailProxy;
	}
	public void setOutBackDetailProxy(StockoutBackDetailProxy outBackDetailProxy) {
		this.outBackDetailProxy = outBackDetailProxy;
	}
	public StockoutBackProxy getStockoutBackProxy() {
		return stockoutBackProxy;
	}
	public void setStockoutBackProxy(StockoutBackProxy stockoutBackProxy) {
		this.stockoutBackProxy = stockoutBackProxy;
	}

	/**
	 * 获取出库单回执列表
	 */
	@RequestMapping(value = "/list")
	public String list(Integer page, Integer size, StockoutBack stockoutBack,Model model){
		PageInfo<StockoutBack> info = new PageInfo<StockoutBack>();
		if(page==null||page==0){
			page = 1;
		}
		if(size==null||size==0){
			size = 10;
		}
		info.setPage(page);
		info.setSize(size);
		ResultInfo<PageInfo<StockoutBack>> stockoutBackPageInfo = stockoutBackProxy.queryPageByObject(stockoutBack, info);
		model.addAttribute("stockOutBackPages", stockoutBackPageInfo.getData() );
		model.addAttribute("stockOutBackReq", stockoutBack);
//		model.addAttribute("statusList", StockOutStatus.values());
		return "/wms/stockoutback/list";
	}
	
	/**
	 * 发货单回执明细
	 */
	@RequestMapping(value = "/viewItem")
	public String viewItem(Long id,StockoutBackDetail stockoutBackDetail,Model model){
		/* 查询出库单主表信息 start*/
		ResultInfo<StockoutBack> stockoutBackResult = stockoutBackProxy.queryById(id);
		/* 查询出库单主表信息 end*/
		stockoutBackDetail = new StockoutBackDetail();
		stockoutBackDetail.setStockoutBackId(id);
		ResultInfo<List<StockoutBackDetail>> outDetailResult = outBackDetailProxy.queryByObject(stockoutBackDetail);
		model.addAttribute("stockoutback",stockoutBackResult.getData());
		model.addAttribute("outBackItemList", outDetailResult.getData());
		return "/wms/stockoutback/viewItem";
	}
}
