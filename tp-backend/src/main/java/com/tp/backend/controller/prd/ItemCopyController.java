package com.tp.backend.controller.prd;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemCopyDto;
import com.tp.proxy.prd.ItemValidateProxy;
import com.tp.service.prd.IItemManageService;

/**
 * 	复制商品
 * @author szy
 * @version 0.0.1
 */
@Controller
@RequestMapping("/item")
public class ItemCopyController extends AbstractBaseController {
	
	@Autowired
	private IItemManageService itemManageService;
	@Autowired
	private ItemValidateProxy	itemValidateProxy;
	
	@RequestMapping("/listPrdid")
	public String listPrdid(Model model,Long detailId){
		List<ItemCopyDto> list = itemManageService.getItemCopyByDetailId(detailId);
		model.addAttribute("list", list);
		model.addAttribute("detailId", detailId);
		return "item/subpages/copy_prdid";
	}
	
	@RequestMapping(value={"/copyPicAndDetail"},method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Long> copyPicAndDetail(Long detailId,Long itemId,String detailIds,String pics,String desc,String descMobile){
		List<String> picList = new ArrayList<String>();
		if(StringUtils.isNotBlank(pics)){
			JSONArray aryJson=JSONArray.fromObject(pics);
			picList = JSONArray.toList(aryJson, String.class);
		}
		
		List<Long> detailList = new ArrayList<Long>();
		if(StringUtils.isNotBlank(detailIds)){
			JSONArray aryJson=JSONArray.fromObject(detailIds);
			detailList = JSONArray.toList(aryJson, Long.class);
			
		}
		
		ResultInfo<Boolean> msg  =  itemValidateProxy.validItemCopy(picList, desc, descMobile);
		if(!msg.success){
			return new ResultInfo<Long>(msg.msg);
		}
		
		try{
			itemManageService.copyPrdPicAndDesc(itemId, detailList, picList, desc, descMobile, super.getUserName());
		}catch(Exception e){
			return new ResultInfo<>(new FailInfo(e));
		}
		return new ResultInfo<>(detailId);
	}
}
