package com.tp.backend.controller.mmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.model.prd.ItemSku;
import com.tp.proxy.prd.ItemSkuProxy;
import com.tp.util.BeanUtil;

/**
 * SKU促销价格统一设置
 * @author szy
 *
 */
@Controller
@RequestMapping(value = "/promotion/topicprice/")
public class TopicPriceManageController  extends AbstractBaseController {
	
	private  Logger logger = LoggerFactory.getLogger(TopicPriceManageController.class);
	
	@Autowired
	private ItemSkuProxy itemSkuProxy;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void list(Model model){
		model.addAttribute("statusList", ItemStatusEnum.values());
	}
	
	@RequestMapping(value="list",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<ItemSku> list(Model model,ItemSku itemSku){
		model.addAttribute("itemSku", itemSku);
		Map<String, Object> params = BeanUtil.beanMap(itemSku);
		itemSku.remove(params);
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		if( params.containsKey("spuName")){
			whereList.add(new DAOConstant.WHERE_ENTRY("spu_name",MYBATIS_SPECIAL_STRING.LIKE,params.get("spuName")));
    		params.remove("spuName");
    	}
		if( params.containsKey("detailName")){
			whereList.add(new DAOConstant.WHERE_ENTRY("detail_name",MYBATIS_SPECIAL_STRING.LIKE,params.get("detailName")));
    		params.remove("detailName");
    	}
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
		return itemSkuProxy.queryPageByParam(params,new PageInfo<ItemSku>(itemSku.getStartPage(),itemSku.getPageSize())).getData();
	}
	
	/**
	 * 修改促销价格
	 * @param model
	 * @return
	 */
	@RequestMapping(value="updatetopicprice",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer> updatePrice(Long id,Double topicPrice){
		try{
			ItemSku itemSku = new ItemSku();
			itemSku.setId(id);
			itemSku.setTopicPrice(topicPrice);
			itemSku.setUpdateUser(super.getUserName());
			itemSku.setUpdateTime(new Date());
			return itemSkuProxy.updateTopicPrice(itemSku);
		}catch(Exception e){
			logger.error("[商品管理 - 修改促销价格Exception] ={}",e); 
			return new ResultInfo<>(new FailInfo("修改失败"));
		}
	}
	
	@RequestMapping(value="batchupdatetopicprice",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer> updateBatchPrice(Model model,@RequestParam("skuList[]") List<String> skuList,Float discount){
		try{
			return itemSkuProxy.updateBatchPrice(skuList,Float.valueOf(discount/10),super.getUserName());
		}catch(Exception e){
			logger.error("[商品管理 - 修改促销价格Exception] ={}",e); 
			return new ResultInfo<>(new FailInfo("修改失败"));
		}
	}
}
