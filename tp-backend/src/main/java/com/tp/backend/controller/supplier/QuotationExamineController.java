package com.tp.backend.controller.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.util.SupplerFreemarkerUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.QuotationInfo;
import com.tp.proxy.sup.QuotationInfoProxy;

/**
 * 
 * <pre>
 *   报价单审核
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Controller
@RequestMapping("/supplier/quotationExam/")
public class QuotationExamineController extends AbstractBaseController {
    
    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;
    
    @Autowired
    private QuotationInfoProxy quotationInfoProxy;
    
    @RequestMapping("auditPage")
    public String toAuditPop(Model model,Long quoId){
        model.addAttribute("execId", quoId);
        model.addAttribute("actionUrl", "/supplier/quotationExam/auditExec.htm");
        return "supplier/pop_table/audit_pop";
    }
    
    @RequestMapping("auditExec")
    @ResponseBody
    public ResultInfo<Boolean> auditExec(Long execId,Integer auditStatus,String auditContent){
        ModelMap map = new ModelMap();
        QuotationInfo quotation = quotationInfoProxy.queryById(execId).getData();
        map.put("nextTab", SupplierConstant.SP_QUATATIONLIST_TAB_ID);
        map.put("nextUrl", "/supplier/quotationList.htm");
        if(null == quotation || !AuditStatus.EXAMING.getStatus().equals(quotation.getAuditStatus())){
            return new ResultInfo<>(new FailInfo("审核状态不对，无法提交审核。"));
        }
        return quotationInfoProxy.auditQuotation(quotation,auditStatus,auditContent, super.getUserId(),super.getUserName());
    }

}
