package com.tp.backend.controller.supplier.purchase;

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
import org.springframework.web.servlet.ModelAndView;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.controller.supplier.ao.PurchaseOrderAO;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.common.vo.supplier.entry.OrderAuditStatus;
import com.tp.common.vo.supplier.entry.PurcharseType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.TaxRate;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.PurchaseInfo;
import com.tp.proxy.bse.TaxRateProxy;
import com.tp.proxy.sup.AuditRecordsProxy;
import com.tp.proxy.sup.PurchaseInfoProxy;
import com.tp.proxy.sup.SupplierInfoProxy;
import com.tp.util.BeanUtil;

@Controller
@RequestMapping("/supplier/")
public class PurchaseOrderController extends AbstractBaseController {
    
    @Autowired
    private PurchaseInfoProxy purchaseInfoProxy;
    @Autowired
    private AuditRecordsProxy auditRecordsProxy;
    @Autowired
    private SupplierInfoProxy supplierInfoProxy;
    @Autowired
    private TaxRateProxy taxRateProxy;
    @Autowired
    private PurchaseOrderAO purchaseOrderAO;
    
    private final static String ORDER_TYPE_CHOOSE_STR = "purchaseorder";
    private final static String ORDER_TYPE_CHOOSE_STR2 = "purchaseOrder";
    
    private final static BillType billType = BillType.PURCHARSE;
    
    private final static String ORDER_TYEP_VIEW = "purchase_order";
    
    @RequestMapping(value="purchaseorderList")
    public String purchaseorderList(Model model,PurchaseInfo purchaseInfo,Integer page,Integer size){
        purchaseInfo.setPurchaseType(PurcharseType.PURCHARSE.getValue());
        Map<String,Object> param = BeanUtil.beanMap(purchaseInfo);
        param.remove("purchaseProductList");
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(),"id desc");
        ResultInfo<PageInfo<PurchaseInfo>> resultInfo = purchaseInfoProxy.queryPageByParamNotEmpty(param, new PageInfo<PurchaseInfo>(page,size));        model.addAttribute("page",resultInfo.getData());
        // by zhs 01151731 修改purchaseInfo 为 purchaseDO
//        model.addAttribute("purchaseInfo",purchaseInfo);
        model.addAttribute("purchaseDO",purchaseInfo);        
        model.addAttribute("auditStatusMap",SupplierConstant.O_AUDIT_STATUS_MAP_ALL);
        model.addAttribute("auditStatusMapStr", SupplierConstant.O_AUDIT_STATUS_MAP_SELECT);
        model.addAttribute("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
        model.addAttribute("orderTypeMap", SupplierConstant.PURCHARSE_TYPE_LEVEL_MAP);
        return "supplier/order/"+ORDER_TYEP_VIEW+"_list";
    }

    @RequestMapping(value=ORDER_TYPE_CHOOSE_STR+"Add",method=RequestMethod.GET)
    public String purchaseorderAdd(final ModelMap model,HttpServletRequest request){
        model.addAttribute("supplierCurrencyTypes", SupplierConstant.SUPPLIER_CURRENCY_TYPES);
        List<TaxRate> taxRateList = taxRateProxy.getRatesByType(TaxRateEnum.PRIMERATE);
        model.addAttribute("taxRateVOs",taxRateList);
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
    public String getItemInfoForm(Model model,Long supplierId,String rateSel) {
        BigDecimal bd =new BigDecimal(0);
        if(null != rateSel && !"".equals( rateSel)){
        	bd = new BigDecimal( rateSel);
        	bd=bd.setScale(1);
        	 rateSel=bd.toString();
        }
        model.addAttribute("rateSelOption",getRateOptionStr(TaxRateEnum.PRIMERATE));
        model.addAttribute("trafficRage",getRateOptionStr(TaxRateEnum.TARRIFRATE));
        model.addAttribute("supplierId", supplierId);
        //获取供应商信息   用的字段现在为进项税率
        model.addAttribute("supplierInfo", supplierInfoProxy.queryById(supplierId));
        return "supplier/pop_table/order_item_pop";
    }
    
    /**
     * 获取税率的选择字符串 
     * 
     * @return
     */
    private List<Map<String,String>> getRateOptionStr(TaxRateEnum rateEnum){
    	List<TaxRate> taxRateVOs = taxRateProxy.getRatesByType(rateEnum);
        List<Map<String,String>> optionsMap = genSelectMaps(taxRateVOs);
        return optionsMap;
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
    				id = bd.setScale(1,BigDecimal.ROUND_HALF_UP).toString();
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
     *   保存采购订单
     * </pre>
     *
     * @return
     */
    @RequestMapping(ORDER_TYPE_CHOOSE_STR+"Save")
    @ResponseBody
    public ResultInfo<PurchaseInfo> saveOrderInfo(Model model,HttpServletRequest request,Long purId){
    	//验证
    	Map<String,Object> retMap = purchaseOrderAO.genPurchaseOrderInfo(request,true);
        if(checkResult(retMap)) {
        	PurchaseInfo purchaseInfo = (PurchaseInfo)retMap.get(SupplierConstant.DATA_KEY);
        	//保存
        	if(null != purId){
             	purchaseInfo.setId(purId);
             	ResultInfo<Boolean> resultInfo =  purchaseInfoProxy.updateOrderInfo(purchaseInfo, OrderAuditStatus.EDITING.getStatus());
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
    @ResponseBody
    public ResultInfo<Boolean> purchaseOrderCancel(Long purId,String status) {
        if("cancel".equals(status)){
            PurchaseInfo purchaseInfo = new PurchaseInfo();
            purchaseInfo.setId(purId);
            return purchaseInfoProxy.auditOrder(purchaseInfo, OrderAuditStatus.CANCELED.getStatus(), "",billType,super.getUserName(),super.getUserId());
        }
        return new ResultInfo<>(new FailInfo("传入参数不对"));
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
    public ResultInfo<PurchaseInfo> purchaseOrderSub(Model model,HttpServletRequest request,Long purId,String status) {
        Integer setStatus = null;
        if("submit".equals(status)){
            setStatus = OrderAuditStatus.EXAMING.getStatus();
        } else if("cancel".equals(status)){
            setStatus = OrderAuditStatus.CANCELED.getStatus();
        } else if("save".equals(status)){
            setStatus = OrderAuditStatus.EDITING.getStatus();
        } else {
            setStatus = OrderAuditStatus.EXAMING.getStatus();
        }
        // by zhs 01171629 auditOrder函数专门用于auditStatus修改，其它用于修改除auditStatus外的数据表字段
        Map<String,Object> retMap = purchaseOrderAO.genPurchaseOrderInfo(request,false);
        if(checkResult(retMap)) {
        	PurchaseInfo purchaseInfo = (PurchaseInfo)retMap.get(SupplierConstant.DATA_KEY);
		    ResultInfo<Boolean> result = null;
		    //验证
		    if(null == purId){
			    ResultInfo<PurchaseInfo> resultInfo = null;
//		    	purchaseInfo.setStatus(setStatus);
//			    purchaseInfo.setAuditStatus(setStatus);
		    	resultInfo = purchaseInfoProxy.savePurchaseOrderInfo(purchaseInfo);
		        if(resultInfo.success) {
		            purId = resultInfo.getData().getId();
		        }else{
		        	return resultInfo;
		        }
		        purchaseInfo.setId(purId);
		        result = new ResultInfo<>(true);
		    } else {
		    	purchaseInfo.setId(purId);
		        /**
		         * 保存供应商基本信息
		         */
		        /*ResultInfo<Boolean> */ result = purchaseInfoProxy.updateOrderInfo(purchaseInfo,  null);
		        if(!result.success){
		        	return new ResultInfo<>(result.msg);
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
            return "supplier/order/purchase_o_show";
        }
        setDetailInfo(model,purchaseInfo);
        AuditRecords doCondition = new AuditRecords();
        doCondition.setStatus(Constant.ENABLED.YES);
        doCondition.setAuditId(purId);
        doCondition.setBillType(BillType.PURCHARSE.getValue());
        List<AuditRecords> auditRecords = auditRecordsProxy.queryByObject(doCondition).getData();
        model.addAttribute("purIdshow", purId);
        model.addAttribute("auditRecords",auditRecords);
        return "supplier/order/purchase_o_show";
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
//        ModelAndView mav = new ModelAndView();
        // by zhs 01161550 purchaseInfo 修改为 purchaseVO
        PurchaseInfo purchaseVO = purchaseInfoProxy.getPurchaseOrderById(purId);
        if(null == purchaseVO){
            return "supplier/order/"+ORDER_TYEP_VIEW+"_edit";
        }
        model.addAttribute("purIdedit", purId);
        setDetailInfo(model,purchaseVO);
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
    	model.addAttribute("supplierInfo", supplierInfoProxy.queryById(purchaseInfo.getSupplierId()).getData());
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
    public ResultInfo<Boolean> purchaseOrdeEditSave(HttpServletRequest request,Long purId){
    	//验证
    	Map<String,Object> retMap = purchaseOrderAO.genPurchaseOrderInfo(request,true);
        if(checkResult(retMap)) {
        	PurchaseInfo purchaseInfo = (PurchaseInfo)retMap.get(SupplierConstant.DATA_KEY);
        	purchaseInfo.setId(purId);
        	return purchaseInfoProxy.updateOrderInfo(purchaseInfo,null);
        }
        return new ResultInfo<>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }
    
    /**
     * 
     * <pre>
     * 校验结果
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public boolean checkResult(Map<String,Object> resultMap){
        try {
            return null != resultMap && null != resultMap.get(SupplierConstant.SUCCESS_KEY)
                    && (Boolean)resultMap.get(SupplierConstant.SUCCESS_KEY);
        } catch(Exception e){
            return false;
        }
    }
}
