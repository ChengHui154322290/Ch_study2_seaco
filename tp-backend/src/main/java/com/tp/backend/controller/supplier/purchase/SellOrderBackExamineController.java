package com.tp.backend.controller.supplier.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/supplier/sellOrderBackExam/")
public class SellOrderBackExamineController extends AbstractBaseController {
    @Autowired
    private PurchaseInfoProxy purchaseInfoProxy;
    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;
    
    private final static String ORDER_TYPE_CHOOSE_STR = "sellorderback";
    private final static String ORDER_TYPE_CHOOSE_STR2 = "sellOrderBack";
    
    private final static BillType billType = BillType.SELL_RETURN;
    
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
    public ResultInfo<Boolean> auditExec(Model model,@RequestParam(value="execId",required=true) Long execId,Integer auditStatus,String auditContent){
        ModelMap map = new ModelMap();
        PurchaseInfo purchaseInfo = purchaseInfoProxy.queryById(execId).getData();
        map.put("nextTab", SupplierConstant.SP_SELLPURCHARSEBACKLIST_TAB_ID);
        map.put("nextUrl", "/supplier/"+ORDER_TYPE_CHOOSE_STR+"List.htm");
        if(null == purchaseInfo || !OrderAuditStatus.EXAMING.getStatus().equals(purchaseInfo.getAuditStatus())){
        	return new ResultInfo<>(new FailInfo("审核状态不对，无法提交审核。"));
        }
        return purchaseInfoProxy.auditOrder(purchaseInfo,auditStatus,auditContent,billType,super.getUserName(),super.getUserId());
    }

}
