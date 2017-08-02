package com.tp.backend.controller.supplier.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.controller.supplier.ao.PurchaseOrderAO;
import com.tp.backend.controller.supplier.ao.PurchaseOrderBackAO;
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
public class PurchaseOrderBackController extends AbstractBaseController {
    
    @Autowired
    private PurchaseInfoProxy purchaseInfoProxy;
    @Autowired
    private PurchaseProductProxy purchaseProductProxy;
    @Autowired
    private TaxRateProxy taxRateProxy;
    @Autowired
    private AuditRecordsProxy auditRecordsProxy;
    @Autowired
    private SupplierInfoProxy supplierInfoProxy;
    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;
    
    // by zhs 01162100 增加 purchaseOrderBackAO 变量
    @Autowired
    private PurchaseOrderBackAO purchaseOrderBackAO;
    
    private final static String ORDER_TYPE_CHOOSE_STR = "purchaseorderback";
    private final static String ORDER_TYPE_CHOOSE_STR2 = "purchaseOrderBack";
    
    private final static String ORDER_TYEP_VIEW = "purchaseback_order";
    private final static BillType billType = BillType.PURCHARSE_RETURN;
    
    @RequestMapping(value=ORDER_TYPE_CHOOSE_STR+"List")
    public String purchaseorderList(Model model,PurchaseInfo purchaseInfo,Integer page,Integer size) {
        purchaseInfo.setPurchaseType(PurcharseType.PURCHARSE_RETURN.getValue());
        ResultInfo<PageInfo<PurchaseInfo>> pageInfoResultInfo = purchaseInfoProxy.queryPageByObject(purchaseInfo, new PageInfo<PurchaseInfo>(page,size));
        model.addAttribute("page",pageInfoResultInfo.getData());
        model.addAttribute("purchaseInfo",purchaseInfo);
        model.addAttribute("auditStatusMap",SupplierConstant.REFUND_O_AUDIT_STATUS_MAP_ALL);
        model.addAttribute("auditStatusMapStr", SupplierConstant.REFUND_O_AUDIT_STATUS_MAP_SELECT);
        model.addAttribute("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
        model.addAttribute("purcharseTypeMap", SupplierConstant.REFUND_PURCHARSE_TYPE_LEVEL_MAP);
        return "supplier/order/purchaseback_order_list";
    }

    @RequestMapping(value=ORDER_TYPE_CHOOSE_STR+"Add",method=RequestMethod.GET)
    public String purchaseorderAdd(final Model model,HttpServletRequest request){
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("type", TaxRateEnum.PRIMERATE);
    	params.put("status", Constant.ENABLED.YES);
    	ResultInfo<List<TaxRate>> taxRateResultInfo = taxRateProxy.queryByParam(params);
    	model.addAttribute("taxRateVOs",taxRateResultInfo.getData());
        model.addAttribute("supplierCurrencyTypes", SupplierConstant.SUPPLIER_CURRENCY_TYPES);
        return "supplier/order/purchaseback_order_add";
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
    @RequestMapping(ORDER_TYPE_CHOOSE_STR+"/getOrderItemInfoForm")
    public String getItemInfoForm(Model model,PurchaseProduct purchasePruduct){
    	purchasePruduct.setStatus(Constant.ENABLED.YES);
    	purchasePruduct.setAuditStatus(RefundOrderAuditStatus.PURCHARSE_FINISHED.getStatus());
    	purchasePruduct.setPurchaseType(PurcharseType.PURCHARSE.getValue());
    	Map<String,Object> params = BeanUtil.beanMap(purchasePruduct);
    	ResultInfo<PageInfo<PurchaseProduct>> purcharseProductListResultInfo= purchaseProductProxy.queryPageByParam(params,new PageInfo<PurchaseProduct>(1,100));
    	model.addAttribute("supplierId", purchasePruduct.getSupplierId());
    	model.addAttribute("orders", purcharseProductListResultInfo.getData().getRows());
    	model.addAttribute("orderTypeShow", "采购订单");
    	model.addAttribute("actionUrl", "/supplier/purchaseorderback/getOrderItemInfoForm.htm");
    	model.addAttribute("supplierInfo", supplierInfoProxy.queryById(purchasePruduct.getSupplierId()).getData());
    	return "supplier/pop_table/refund_items_pop";
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
//   public ResultInfo<PurchaseInfo> saveOrderInfo(PurchaseInfo purchaseInfo){
    // by zhs 01162100 修改saveOrderInfo逻辑
    public ResultInfo<PurchaseInfo> saveOrderInfo(Model model,HttpServletRequest request,Long purId){
    	//验证
    	Map<String,Object> retMap = purchaseOrderBackAO.genPurchaseOrderInfo(request,true);
        if(checkResult(retMap)) {
        	PurchaseInfo purchaseInfo = (PurchaseInfo)retMap.get(SupplierConstant.DATA_KEY);
        	//保存
        	if(null != purId){
        		purchaseInfo.setId(purId);
        		ResultInfo<Boolean> resultInfo = purchaseInfoProxy.updateOrderInfo(purchaseInfo, OrderAuditStatus.EDITING.getStatus());
        		if(resultInfo.success){
        			return new ResultInfo<>(purchaseInfo);
        		}else{
        			return new ResultInfo<>(resultInfo.msg);
        		}
        	}else{
        		return purchaseInfoProxy.savePurchaseOrderInfo(purchaseInfo);
        	}
        }
    	return new ResultInfo<PurchaseInfo>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
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
    public ResultInfo<Boolean> purchaseOrderCancel(Long purId,String setStatus) {
        if("cancel".equals(setStatus)){
            PurchaseInfo purchaseInfo = new PurchaseInfo();
            purchaseInfo.setId(purId);
            return purchaseInfoProxy.auditOrder(purchaseInfo, RefundOrderAuditStatus.CANCELED.getStatus(), "",billType,super.getUserName(),super.getUserId());
        }
       return new ResultInfo<Boolean>(new FailInfo("操作动作不是取消"));
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
    // by zhs 01162221 修改purchaseOrderSub逻辑
//    public ResultInfo<PurchaseInfo> purchaseOrderSub(PurchaseInfo purchaseInfo,Long purId,String status) {
//	    return new ResultInfo<>(purchaseInfo);
    @ResponseBody
       public ResultInfo<PurchaseInfo> purchaseOrderSub(Model model,HttpServletRequest request,Long purId,String status) {
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

        Map<String,Object> retMap = purchaseOrderBackAO.genPurchaseOrderInfo(request,false);
        if(checkResult(retMap)) {
        	PurchaseInfo purchaseInfo = (PurchaseInfo)retMap.get(SupplierConstant.DATA_KEY);
		    ResultInfo<Boolean> result = null;
		    //验证    	        
	        purchaseInfo.setPurchaseType(PurcharseType.PURCHARSE_RETURN.getValue());
	        if(null == purId){
			    ResultInfo<PurchaseInfo> resultInfo = null;
//	        	purchaseInfo.setAuditStatus(setStatus);
	            resultInfo = purchaseInfoProxy.savePurchaseOrderInfo(purchaseInfo);
	            if(resultInfo.success){
		            purId = resultInfo.getData().getId();
	            } else {
	                return resultInfo;     	
				}
		        purchaseInfo.setId(purId);
		        result = new ResultInfo<>(true);
	        } else {
	        	purchaseInfo.setId(purId);
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
            return "supplier/order/purchaseback_o_show";
        }
        setDetailInfo(model,purchaseInfo);
        AuditRecords doCondition = new AuditRecords();
        doCondition.setStatus(Constant.ENABLED.YES);
        doCondition.setAuditId(purId);
        doCondition.setBillType(BillType.PURCHARSE_RETURN.getValue());
        ResultInfo<List<AuditRecords>> auditRecords = auditRecordsProxy.queryByObject(doCondition);
        model.addAttribute("auditRecords",auditRecords.getData());
        return "supplier/order/purchaseback_o_show";
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
        PurchaseInfo purchaseInfo = purchaseInfoProxy.getPurchaseOrderById(purId);
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
    public ResultInfo<Boolean> purchaseOrdeEditSave(PurchaseInfo purchaseInfo,@RequestParam(value="purId",required=true) Long purId){
    	//验证
    	purchaseInfo.setId(purId);

    	return purchaseInfoProxy.updateOrderInfo(purchaseInfo,null);
    }

}
