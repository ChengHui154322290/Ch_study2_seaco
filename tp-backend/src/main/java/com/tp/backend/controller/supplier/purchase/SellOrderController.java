package com.tp.backend.controller.supplier.purchase;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.controller.supplier.ao.SellOrderAO;
import com.tp.backend.util.SupplerFreemarkerUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.common.vo.supplier.entry.OrderAuditStatus;
import com.tp.common.vo.supplier.entry.PurcharseType;
import com.tp.common.vo.supplier.entry.RefundOrderAuditStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.TaxRate;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.PurchaseProduct;
import com.tp.proxy.bse.TaxRateProxy;
import com.tp.proxy.sup.AuditRecordsProxy;
import com.tp.proxy.sup.PurchaseInfoProxy;
import com.tp.proxy.sup.PurchaseProductProxy;
import com.tp.proxy.sup.SupplierInfoProxy;
import com.tp.util.BeanUtil;

@Controller
@RequestMapping("/supplier/")
public class SellOrderController extends AbstractBaseController {
    
    @Autowired
    private PurchaseInfoProxy purchaseInfoProxy;
    @Autowired
    private PurchaseProductProxy purchaseProductProxy;
    
    @Autowired
    private AuditRecordsProxy auditRecordsProxy;
    
    @Autowired
    private SupplierInfoProxy supplierInfoProxy;
    @Autowired
    private TaxRateProxy taxRateProxy;
    @Autowired
    private SellOrderAO sellOrderAO;
    
    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;
    
    
    private final static String ORDER_TYPE_CHOOSE_STR = "sellorder";
    private final static String ORDER_TYPE_CHOOSE_STR2 = "sellOrder";
    
    private final static String ORDER_TYEP_VIEW = "sell_order";
    private final static BillType billType = BillType.SELL;
    
    @RequestMapping(value=ORDER_TYPE_CHOOSE_STR+"List")
    public String purchaseorderList(Model model,PurchaseInfo purchaseInfo,Integer page,Integer size){
    	purchaseInfo.setPurchaseType(PurcharseType.SELL.getValue());
        PageInfo<PurchaseInfo> resultInfo= purchaseInfoProxy.queryPageByObject(purchaseInfo, new PageInfo<PurchaseInfo>(page,size)).getData();
        model.addAttribute("page",resultInfo);
        model.addAttribute("purchaseDO",purchaseInfo);
        model.addAttribute("auditStatusMap",SupplierConstant.O_AUDIT_STATUS_MAP_ALL);
        model.addAttribute("auditStatusMapStr", SupplierConstant.O_AUDIT_STATUS_MAP_SELECT);
        model.addAttribute("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
        model.addAttribute("orderTypeMap", SupplierConstant.PURCHARSE_TYPE_LEVEL_MAP);
        return "supplier/order/"+ORDER_TYEP_VIEW+"_list";
    }

    @RequestMapping(value=ORDER_TYPE_CHOOSE_STR+"Add",method=RequestMethod.GET)
    public String purchaseorderAdd(final ModelMap model,HttpServletRequest request){
    	List<TaxRate> taxRateVO = taxRateProxy.getRatesByType(TaxRateEnum.PRIMERATE);
    	model.addAttribute("taxRateVOs",taxRateVO);
        model.addAttribute("supplierCurrencyTypes", SupplierConstant.SUPPLIER_CURRENCY_TYPES);
        return "supplier/order/"+ORDER_TYEP_VIEW+"_add";
    }
    
    /**
     * 
     * <pre>
     *   添加订单商品form
     * </pre>
     *
     * @param request
     * @param supplierId
     * @return
     */
    @RequestMapping(ORDER_TYPE_CHOOSE_STR+"/getItemInfoForm")
    public String getItemInfoForm(Model model,PurchaseProduct purchasePruduct,Long supplierId,Long orderId,String rateSel){
    	BigDecimal bd =new BigDecimal(0);
        if(null !=rateSel && !"".equals(rateSel)){
        	bd = new BigDecimal(rateSel);
        	bd=bd.setScale(1);
        	rateSel=bd.toString();
        }
        // by zhs 0122 注释无效代码
//    	purchasePruduct.setSupplierId(supplierId);
//    	purchasePruduct.setPurchaseId(orderId);
//        purchasePruduct.setStatus(Constant.ENABLED.YES); purchasePruduct.setPurchaseType(PurcharseType.SELL.getValue());
//        purchasePruduct.setAuditStatus(RefundOrderAuditStatus.PURCHARSE_FINISHED.getStatus());
//        Map<String,Object> params = BeanUtil.beanMap(purchasePruduct);
//        List<PurchaseProduct> orderProductList = purchaseProductProxy.queryByParamNotEmpty(params).getData();
//        model.addAttribute("orders", orderProductList);
        model.addAttribute("orderTypeShow", "代销订单");
        model.addAttribute("actionUrl", "/supplier/sellorderback/getOrderItemInfoForm.htm");        
        model.addAttribute("rateSelOption",getRateOptionStr(TaxRateEnum.PRIMERATE));
        model.addAttribute("trafficRage",getRateOptionStr(TaxRateEnum.TARRIFRATE));
        model.addAttribute("supplierId", supplierId);
        //获取供应商信息   用的字段现在为进项税率
        model.addAttribute("supplierDO", supplierInfoProxy.queryById(supplierId).getData());
        return "supplier/pop_table/order_item_pop";
    }
    /**
     * 
     * <pre>
     *   保存采购订单
     * </pre>
     *
     * @return
     */
    @RequestMapping(ORDER_TYPE_CHOOSE_STR+"Save")
    @ResponseBody
    public ResultInfo<PurchaseInfo> saveOrderInfo(Model model, HttpServletRequest request,Long purId){
    	//验证
    	 Map<String,Object> retMap = sellOrderAO.genPurchaseOrderInfo(request,true);
         if(checkResult(retMap)) {
        	 PurchaseInfo purchaseInfo = (PurchaseInfo)retMap.get(SupplierConstant.DATA_KEY);
	    	if(null != purId){
	        	purchaseInfo.setId(purId);
	        	ResultInfo<Boolean> resultInfo =purchaseInfoProxy.updateOrderInfo(purchaseInfo, OrderAuditStatus.EDITING.getStatus());
	        	if(resultInfo.success){
	        		return new ResultInfo<>(purchaseInfo);
	        	}else{
	        		return new ResultInfo<>(resultInfo.msg);
	        	}
	    	}else{
	    		return purchaseInfoProxy.savePurchaseOrderInfo(purchaseInfo);
	    	}
         }
         return new ResultInfo<>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }
    
    /**
     * 
     * 
     * @param taxRateVO
     * @return
     */
    private List<Map<String,String>> genSelectMaps(List<TaxRate> taxRateVOs){
    	List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
    	if(null != taxRateVOs && taxRateVOs.size()>0){
    		for(TaxRate supplierRate : taxRateVOs){
    			Map<String,String> dataMap = new HashMap<String,String>();
    			String id ="";
    			BigDecimal bd = new BigDecimal(supplierRate.getRate());
    			if(null != bd){
    				try{
        				id = bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString();    					
    				}
    				catch(Exception e){
							e.printStackTrace();  
    				}
    			}
    			dataMap.put("id", id);
    			dataMap.put("name", supplierRate.getRate()+"%");
    			mapList.add(dataMap);
    		}
    	}
    	return mapList;
    }
    
    /**
     * 
     * <pre>
     *   取消
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping(ORDER_TYPE_CHOOSE_STR2+"Cancel")
    @ResponseBody
    public ResultInfo<Boolean> purchaseOrderCancel(Model model,Long purId,String status) {
        if("cancel".equals(status)){
            PurchaseInfo purchase = new PurchaseInfo();
            purchase.setId(purId);
            return purchaseInfoProxy.auditOrder(purchase, RefundOrderAuditStatus.CANCELED.getStatus(), "",billType,super.getUserName(),super.getUserId());
        }
        return new ResultInfo<>(new FailInfo("不能取消"));
    }
    
    /**
     * 
     * <pre>
     *   报价单提交
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping(ORDER_TYPE_CHOOSE_STR2+"Sub")
    @ResponseBody    
	// by zhs 01181513 修改purchaseOrderSub
    public ResultInfo<PurchaseInfo> purchaseOrderSub(HttpServletRequest request,Long purId,String status) {
        Integer setStatus = null;
        if("submit".equals(status)){
            setStatus = RefundOrderAuditStatus.EXAMING.getStatus();
        } else if("cancel".equals(status)){
            setStatus = RefundOrderAuditStatus.CANCELED.getStatus();
        } else if("save".equals(status)){
            setStatus = RefundOrderAuditStatus.EDITING.getStatus();
        } else {
            setStatus = RefundOrderAuditStatus.EXAMING.getStatus();
        }
        Map<String,Object> retMap = sellOrderAO.genPurchaseOrderInfo(request,false);
        if(checkResult(retMap)) {
        	PurchaseInfo purchaseInfo = (PurchaseInfo)retMap.get(SupplierConstant.DATA_KEY);
		    ResultInfo<Boolean> result = null;
		    //验证    	        
	        purchaseInfo.setPurchaseType(PurcharseType.SELL.getValue());
	        if(null == purId){
	            ResultInfo<PurchaseInfo> resultInfo = null;
//	        	purchaseInfo.setPurchaseType(PurcharseType.SELL_RETURN.getValue());
//	        	purchaseInfo.setStatus(setStatus);
	            resultInfo= purchaseInfoProxy.savePurchaseOrderInfo(purchaseInfo);
	            if(resultInfo.success){
	                purId = resultInfo.getData().getId();
	            }else {
	                return resultInfo;	
				}
		        purchaseInfo.setId(purId);
		        result = new ResultInfo<>(true);
	        } else {
	        	purchaseInfo.setId(purId);
	            /**
	             * 保存供应商基本信息
	             */
	        	result = purchaseInfoProxy.updateOrderInfo(purchaseInfo, null);
	        	if(!result.success){
	        		return new ResultInfo<PurchaseInfo>(result.msg);
	        	}
	        }
	        if(result!=null && result.success && !"save".equals(status)){
	        	purchaseInfo.setId(purId);
	           purchaseInfoProxy.auditOrder(purchaseInfo, setStatus, "",billType,super.getUserName(),super.getUserId());
	        }
	        return new ResultInfo<>(purchaseInfo);
        }
        return new ResultInfo<>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }
    /**
     * 
     * <pre>
     *   报价单展示
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping(ORDER_TYPE_CHOOSE_STR2+"Show")
    public String orderShow(Model model,Long purId){
        PurchaseInfo  purchaseInfo = purchaseInfoProxy.getPurchaseOrderById(purId);
        if(null == purchaseInfo){
            return "supplier/order/sellback_o_show";
        }
        setDetailInfo(model,purchaseInfo);
        AuditRecords doCondition = new AuditRecords();
        doCondition.setStatus(Constant.ENABLED.YES);
        doCondition.setAuditId(purId);
        doCondition.setBillType(billType.getValue());
        List<AuditRecords> auditRecords = auditRecordsProxy.queryByObject(doCondition).getData();
        model.addAttribute("auditRecords",auditRecords);
        // by zhs 01181516  修改return
//        return "supplier/order/sellback_o_show";
        return "supplier/order/sell_o_show";
    }
    /**
     * 
     * <pre>
     *   报价单编辑页面
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping(ORDER_TYPE_CHOOSE_STR2+"Edit")
    public String purchaseOrdeEdti(Model model,Long purId){
        PurchaseInfo  purchaseInfo = purchaseInfoProxy.getPurchaseOrderById(purId);
        if(null == purchaseInfo){
            return "supplier/order/"+ORDER_TYEP_VIEW+"_edit";
        }
        setDetailInfo(model,purchaseInfo);
        return "supplier/order/"+ORDER_TYEP_VIEW+"_edit";
    }
    /**
     * 
     * <pre>
     *   设置详细信息
     * </pre>
     *
     * @param mav
     * @return
     */
    private void setDetailInfo(Model model,PurchaseInfo  purchaseInfo){
    	//进项税率
    	List<TaxRate> taxRateVOs = taxRateProxy.getRatesByType(TaxRateEnum.PRIMERATE);
    	//关税
    	List<TaxRate> traffiRateVOs = taxRateProxy.getRatesByType(TaxRateEnum.TARRIFRATE);
    	model.addAttribute("taxRateVOs", taxRateVOs);
    	model.addAttribute("traffiRateVOs", traffiRateVOs);
    	model.addAttribute("purchaseVO",purchaseInfo);
    	model.addAttribute("statusShow",SupplierConstant.O_AUDIT_STATUS_MAP_ALL.get(purchaseInfo.getAuditStatus()));
    	model.addAttribute("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
    	model.addAttribute("supplierCurrencyTypes", SupplierConstant.SUPPLIER_CURRENCY_TYPES);
    	model.addAttribute("supplierDO", supplierInfoProxy.queryById(purchaseInfo.getSupplierId()).getData());
    }
    /**
     * 
     * <pre>
     *   报价单编辑之后的保存
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping(ORDER_TYPE_CHOOSE_STR2+"EditSave")
    @ResponseBody
    public ResultInfo<Boolean> purchaseOrdeEditSave(Model model,PurchaseInfo purchaseInfo,Long purId){
    	return purchaseInfoProxy.updateOrderInfo(purchaseInfo,null);
    }
    
    private List<Map<String,String>> getRateOptionStr(TaxRateEnum rateEnum){
    	List<TaxRate> taxRateVOs = taxRateProxy.getRatesByType(rateEnum);
        List<Map<String,String>> optionsMap = genSelectMaps(taxRateVOs);
        return optionsMap;
    }
}
