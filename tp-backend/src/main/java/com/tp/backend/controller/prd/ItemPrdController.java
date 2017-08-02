package com.tp.backend.controller.prd;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemDto;
import com.tp.proxy.prd.ItemPrdProxy;
import com.tp.proxy.prd.ItemProxy;
import com.tp.result.bse.SpecGroupResult;

/**
 * 
 * <pre>
 *  商品prd控制器
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Controller
@RequestMapping("/item")
public class ItemPrdController  extends AbstractBaseController{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ItemPrdController.class);
	@Autowired
	private ItemPrdProxy itemPrdProxy;
	@Autowired
	private ItemProxy itemProxy;
	/**
	 * 查询同一组prd下面信息
	 * @param model
	 * @param detailId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "queryPrd", method = RequestMethod.GET)
	public String detail(Model model, Long itemId,HttpServletRequest request) throws Exception {
		LOGGER.info("查询商品的prid列表,detailId为 {} ", itemId);
		ItemDto itemDto = itemProxy.initSpu(model, itemId);
		model.addAttribute("item", itemDto);
		return "/item/item_prd";
	}
	
	
	/**
	 * 
	 * <pre>
	 * 	  规格查询
	 * </pre>
	 *
	 * @param model
	 * @param smallId
	 * @return
	 */
	@RequestMapping(value={"/getSpecByItemId"},method=RequestMethod.GET)
	public String getSpecByItemId(Model model,String specGroupIds){
		if(StringUtils.isBlank(specGroupIds)){
			return "/item/item_spec";
		}
		//通过接口查询
		List<SpecGroupResult> specGroupList = new ArrayList<SpecGroupResult>();
		specGroupList = itemProxy.getSpecGroupResultBySpecGroupIds(specGroupIds);
		model.addAttribute("specGroupList", specGroupList);
		return "/item/item_spec";
	}
	
	

	/**
	 * 
	 * <pre>
	 * 		更新prdid信息，保存商品属性，商品详情，图片，sku信息
	 * </pre>
	 * 
	 * @param detailDto
	 * @param picList
	 * @param skuListJson
	 * @param attrListJson
	 * @param attrItemJson
	 * @param request
	 * @param response
	 * @param bindResult
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveItemPrds", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Long> saveItemPrds(String info,String details) {
		// 获取session用户
		return itemPrdProxy.saveItemPrds(info, details, super.getUserName());
		//step1 校验 prd规格符合要求
		//step2 新增item_detail， item_spec
		
	}
}
