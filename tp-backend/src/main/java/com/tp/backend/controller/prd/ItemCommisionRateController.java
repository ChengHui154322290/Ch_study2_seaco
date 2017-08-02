package com.tp.backend.controller.prd;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.model.prd.ItemSku;
import com.tp.proxy.prd.ItemSkuProxy;
import com.tp.util.BeanUtil;

/**
 * 商品分销比例管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/item/commisionRate")
public class ItemCommisionRateController extends AbstractBaseController{
	
	private  Logger logger = LoggerFactory.getLogger(ItemCommisionRateController.class);
	
	@Autowired
	private ItemSkuProxy itemSkuProxy;

	/**
	 * 商品列表
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/list"})
	public void list(Model model,ItemSku itemSku){
		model.addAttribute("itemSku", itemSku);
		model.addAttribute("statusList", ItemStatusEnum.values());
		Map<String, Object> params = BeanUtil.beanMap(itemSku);
		itemSku.remove(params);
		if( params.containsKey("spuName")){
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " spu_name like '%"+params.get("spuName")+"%'");  
    		params.remove("spuName");
    	}
		if( params.containsKey("detailName")){
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " detail_name like '%"+params.get("detailName")+"%'");  
    		params.remove("detailName");
    	}
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		if(itemSku.getStartRate()!=null){
			whereList.add(new DAOConstant.WHERE_ENTRY("commision_rate",MYBATIS_SPECIAL_STRING.GT,itemSku.getStartRate()));
		}
		if(itemSku.getEndRate()!=null){
			whereList.add(new DAOConstant.WHERE_ENTRY("commision_rate",MYBATIS_SPECIAL_STRING.LT,itemSku.getEndRate()));
		}
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
		PageInfo<ItemSku> resultInfo =itemSkuProxy.queryPageByParam(params,new PageInfo<ItemSku>(itemSku.getStartPage(),itemSku.getPageSize())).getData();
		model.addAttribute("page", resultInfo);
		model.addAttribute("commisionTypeList",ItemConstant.COMMISION_TYPE.values());
	}
	
	/**
	 * 修改佣金比例
	 * @param model
	 * @return
	 */
	@RequestMapping(value="updateRate",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer> save(Long id,Double commisionRate,Integer commisionType){
		try{
			if(id == null) return new ResultInfo<>(new FailInfo("请选择一个商品"));
			if(commisionRate == null) return new ResultInfo<>(new FailInfo("请输入分销佣金比例"));
			ItemSku itemSku = new ItemSku();
			itemSku.setId(id);
			itemSku.setCommisionType(commisionType);
			itemSku.setCommisionRate(commisionRate);
			itemSku.setUpdateUser(super.getUserName());
			itemSku.setUpdateTime(new Date());
			return itemSkuProxy.updateNotNullById(itemSku);
		}catch(Exception e){
			logger.error("[商品管理 - 修改佣金比例 Exception] ={}",e); 
			return new ResultInfo<>(new FailInfo("修改失败"));
		}
	}
}
