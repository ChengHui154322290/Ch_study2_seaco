package com.tp.backend.controller.wms;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.StocksyncInfo;
import com.tp.proxy.wms.StocksyncInfoProxy;


@Controller
@RequestMapping("wms/stocksync/")
public class StocksyncController extends AbstractBaseController {
	private static final Logger  LOGGER = LoggerFactory.getLogger(StocksyncController.class);
	
	@Autowired
	private StocksyncInfoProxy stocksyncInfoProxy;
	
	@RequestMapping(value = "/list")
	public String list(StocksyncInfo stocksyncInfo, Integer page,Integer size, Model model){
		
		Integer startPage = page == null ? 1 : page;
		Integer pageSize = size == null ? 10 : size;
		PageInfo<StocksyncInfo> pageInfo = new PageInfo<StocksyncInfo>();
		pageInfo.setPage(startPage);
		pageInfo.setSize(pageSize);
		ResultInfo<PageInfo<StocksyncInfo>> result = stocksyncInfoProxy.queryPageByObject(stocksyncInfo, pageInfo);
		model.addAttribute("page", result.getData());
		model.addAttribute("stocksyncInfo", stocksyncInfo);
		if (CollectionUtils.isEmpty(result.getData().getRows()) ) {
			model.addAttribute("norecoders", "暂无记录");
		}
		return "/wms/stocksync/list";
	}
	
	
}
