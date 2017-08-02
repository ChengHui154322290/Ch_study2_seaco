/**  
 * Project Name:xg-seller  
 * File Name:SellerShopController.java  
 * Package Name:com.tp.seller.controller.shop  
 * Date:2016年9月22日上午9:38:17  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/  
   
package com.tp.seller.controller.shop;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.annotation.Token;
import com.tp.common.vo.Constant;
import com.tp.dto.cms.ReturnData;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierShop;
import com.tp.proxy.sup.SupplierShopProxy;
import com.tp.proxy.sup.SupplierUserProxy;
import com.tp.seller.constant.SellerConstant;
import com.tp.seller.controller.base.BaseController;

/**  
 * ClassName:SellerShopController <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2016年9月22日 上午9:38:17 <br/>  
 * @author   zhouguofeng  
 * @version    
 * @since   JDK 1.8  
 * @see        
 */
@Controller
@RequestMapping("/seller/shop")
public class SellerShopController extends BaseController{
	
	private final static Log logger = LogFactory.getLog(SellerShopController.class);
	@Autowired
	private SupplierShopProxy  supplierShopProxy;
	@Autowired
	private SupplierUserProxy  supplierUserProxy;
	
	/**
	 * 
	 * <pre>
	 * 		店铺信息 
	 * </pre>
	 *
	 * @param model
	 * @param itemId
	 * @throws Exception
	 */
	@RequestMapping(value={"/shopInfo"},method=RequestMethod.GET)
	public String shopInfo(Model model,HttpServletRequest request) throws Exception{
	 Long supplierUserId=(Long) request.getSession().getAttribute(SellerConstant.USER_ID_KEY);
		SupplierInfo supplierInfo=supplierUserProxy.getSupplierInfoByUserId(supplierUserId);
//	    Topic  topic=new Topic();
//	    topic.setSupplierId(topic.getSupplierId());
//	    ResultInfo<List<Topic>> topicListInfo= topicProxy.queryByObject(topic);
		model.addAttribute("supplierId", supplierInfo.getId());
		model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.item.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.item.name());
        SupplierShop supplierShop=supplierShopProxy.getSupplierShopInfo(supplierInfo.getId());
        if(supplierShop==null){
        	  supplierShop=new  SupplierShop();
        	  supplierShop.setShopName(supplierInfo.getName());
        }
        model.addAttribute("supplierShop", supplierShop);
        return "seller/shop/shopInfo";

	}
	
	/**
	 * 
	 * save:(保存供应商店铺信息). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param supplierShop
	 * @param request
	 * @return  
	 * @sinceJDK 1.8
	 */
	@Token(validateToken = true)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public ReturnData save(@ModelAttribute SupplierShop supplierShop,HttpServletRequest request){
		SupplierShop supplierShoppara=new  SupplierShop();
        boolean  saveResult=false;
		ReturnData returnData = null;
		supplierShop.setStatus(1);//启用
		supplierShoppara.setShopName(supplierShop.getShopName());//店铺名称
	
		SupplierShop supplierShopResult =supplierShopProxy.getSupplierShopInfoByObject(supplierShoppara);
		if(supplierShopResult!=null&&supplierShopResult.getId()!=supplierShop.getId()){
			returnData = new ReturnData(Boolean.FALSE,"店铺名重复！");
			logger.error("店铺名重复！");
			return returnData;		}
		if(supplierShop.getId()!=null && supplierShop.getId()>0){
			ResultInfo<Integer> result = supplierShopProxy.updateById(supplierShop);
			saveResult=result.isSuccess();
			if(!result.isSuccess()){
				returnData = new ReturnData(Boolean.FALSE,"供应商店铺保存报错");
				logger.error("供应商店铺保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,result.getData().intValue());
			}
		}else{
			ResultInfo<SupplierShop> result = supplierShopProxy.insert(supplierShop);
			saveResult=result.isSuccess();
			if(!result.isSuccess()){
				returnData = new ReturnData(Boolean.FALSE,"供应商店铺保存报错");
				logger.error("供应商店铺保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,result.getMsg());
			}
		}
    	return returnData;
	}

}
  
