package com.tp.backend.controller.supplier.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.util.SupplerFreemarkerUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.common.vo.supplier.entry.OrderAuditStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.PurchaseInfo;
import com.tp.proxy.sup.PurchaseInfoProxy;

/**
 * 
 * <pre>
 *   退货单审核
 * </pre>
 *
 * @author Administrator
 * @version $Id: PurchaseOrderBackExamineController.java, v 0.1 2015年1月10日 下午1:28:40 Administrator Exp $
 */
@Controller
@RequestMapping("/supplier/purchaseOrderBackExam/")
public class PurchaseOrderBackExamineController extends AbstractBaseController {
    
    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;
    
    @Autowired
    private PurchaseInfoProxy purchaseInfoProxy;
    
    private final static String ORDER_TYPE_CHOOSE_STR = "purchaseorderback";
    private final static String ORDER_TYPE_CHOOSE_STR2 = "purchaseOrderBack";
    
    private final static BillType billType = BillType.PURCHARSE_RETURN;
    
    /**
     * 
     * <pre>
     * 
     * </pre>
     *
     * @param quoId
     * @return
     */
    @RequestMapping("auditPage")
    public String toAuditPop(Model model,Long quoId){
    	model.addAttribute("execId", quoId);
    	model.addAttribute("actionUrl", "/supplier/"+ORDER_TYPE_CHOOSE_STR2+"Exam/auditExec.htm");
        return "supplier/pop_table/audit_pop";
    }
    
    /**
     * 
     * <pre>
     *   
     * </pre>
     *
     * @param request
     * @param quoId
     * @return
     */
    @RequestMapping("auditExec")
    @ResponseBody
    public ResultInfo<Boolean> auditExec(Model model,Long execId,Integer auditStatus,String auditContent){
        PurchaseInfo purchaseInfo = purchaseInfoProxy.queryById(execId).getData();
        model.addAttribute("nextTab", SupplierConstant.SP_PURCHASEPURCHARSEBACKLIST_TAB_ID);
        model.addAttribute("nextUrl", "/supplier/"+ORDER_TYPE_CHOOSE_STR+"List.htm");
        if(null == purchaseInfo || !OrderAuditStatus.EXAMING.getStatus().equals(purchaseInfo.getAuditStatus())){
            return new ResultInfo<>(new FailInfo("审核状态不对，无法提交审核。"));
        }
        return purchaseInfoProxy.auditOrder(purchaseInfo,auditStatus,auditContent, billType,super.getUserName(),super.getUserId());
    }

}
