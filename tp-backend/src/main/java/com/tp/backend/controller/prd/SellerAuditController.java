package com.tp.backend.controller.prd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemDetailOpenDto;
import com.tp.model.prd.ItemAuditDetail;
import com.tp.model.prd.ItemSku;
import com.tp.proxy.prd.ItemSkuProxy;
import com.tp.proxy.prd.SellerItemAuditProxy;
import com.tp.query.prd.SellerSkuQuery;

@Controller
@RequestMapping("/item")
public class SellerAuditController  extends AbstractBaseController{

	@Autowired
	private ItemSkuProxy itemSkuProxy;
	@Autowired
	private SellerItemAuditProxy sellerItemAuditProxy;
	/**
	 * 	日期转换
	 * @param request
	 * @param binder
	 * @throws Exception
	 */
	@InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder binder)throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}
	
	@RequestMapping(value={"/seller_skuInfo_list"})
	public  String add(Model model,Integer page,Integer size,SellerSkuQuery sellerSkuQuery) {
        PageInfo<ItemSku> queryAllLikedofItemSkuByPage = itemSkuProxy.queryAllLikedofItemSkuByPage(sellerSkuQuery,new PageInfo<ItemSku>(page,size));
		model.addAttribute("queryAllLikedofItemSkuByPage", queryAllLikedofItemSkuByPage);
		return "/item/seller_skuInfo_list";
	}
	
	
	@RequestMapping(value={"/seller_item_info"})
	public  String viewSellerItem(Model model,	Long sellerSkuId,String parentFrameId) throws Exception {
		Object plantFormItemDetail = itemSkuProxy.getSkuInfo(sellerSkuId);
		String supplierName = itemSkuProxy.getSupplierName(sellerSkuId);
		model.addAttribute("plantFormItemDetail", plantFormItemDetail);
		model.addAttribute("sellerSkuId", sellerSkuId);
		model.addAttribute("supplierName", supplierName);
		model.addAttribute("parentFrameId",parentFrameId);
		if(plantFormItemDetail instanceof ItemDetailOpenDto){
			return "/item/seller_item_info";
		} else {
			return "/item/seller_item_info_se";
		}
		
	}
	
	@RequestMapping(value={"/seller_item_audit"})
	public String viewOfAudit(Model model,String sellerSkuId)  throws Exception {
		Map<String, Integer> rejectTypes = itemSkuProxy.initRejectType();
		model.addAttribute("sellerSkuId", sellerSkuId);
		model.addAttribute("rejectTypes", rejectTypes);
		return "/item/seller_item_audit";	
	}
	
	
	@RequestMapping(value={"/sellerItemAudit"})
	@ResponseBody
	public ResultInfo<Boolean>  sellerItemAudit(Model model,ItemAuditDetail auditDetail,Long sellerSkuId)  throws Exception {
		auditDetail.setAuditUserName(super.getUserName());
		auditDetail.setAuditTime(new Date());
		return sellerItemAuditProxy.auditSellerItemWithBindLevel(super.getUserId(),super.getUserName(),sellerSkuId, auditDetail);
	}
}
