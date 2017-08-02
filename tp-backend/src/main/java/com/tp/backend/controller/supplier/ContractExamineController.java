package com.tp.backend.controller.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.util.SupplerFreemarkerUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.Contract;
import com.tp.proxy.sup.ContractProxy;

/**
 * 
 * <pre>
 *   报价单审核
 * </pre>
 *
 * @author Administrator
 * @version $Id: QuotationExamineController.java, v 0.1 2015年1月7日 下午9:21:13 Administrator Exp $
 */
@Controller
@RequestMapping("/supplier/contractExam/")
public class ContractExamineController extends AbstractBaseController {
    
    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;
    
    @Autowired
    private ContractProxy contractProxy;
    
    /**
     * 跳转合同审批页面
     * 
     * @param quoId
     * @param request
     * @return
     */
    @RequestMapping("auditPage")
    public String toAuditPop(Model model,Long quoId){
        model.addAttribute("execId", quoId);
        model.addAttribute("actionUrl", "/supplier/contractExam/auditExec.htm");
        return "supplier/pop_table/audit_pop";
    }
    
    /**
     * 执行合同审批
     * 
     * @param request
     * @param quoId
     * @return
     */
    @RequestMapping("auditExec")
    @ResponseBody
    public ResultInfo<Boolean> auditExec(Model model,Long execId,Integer auditStatus,String auditContent){
        Contract quotation = contractProxy.queryById(execId).getData();
        model.addAttribute("nextTab", SupplierConstant.SP_CONTRACTLIST_TAB_ID);
        model.addAttribute("nextUrl", "/supplier/contract/list.htm");
        if(null == quotation || !AuditStatus.EXAMING.getStatus().equals(quotation.getAuditStatus())){
        	return new ResultInfo<>(new FailInfo("审核状态不对，无法提交审核。"));
        }
        return contractProxy.auditContract(quotation,auditStatus,auditContent,super.getUserId(),super.getUserName());
    }
    
    /**
     * 合同特定审批操作
     * 
     * @param request
     * @param execId
     * @return
     */
    @RequestMapping("contractAuditExec")
    @ResponseBody
    public ResultInfo<Boolean> contractAuditExec(Model model,Long cid,String status){
    	Contract contract = contractProxy.queryById(cid).getData();
        Integer setStatus = null;
        if("submit".equals(status)){
            setStatus = AuditStatus.EXAMING.getStatus();
        } else if("cancel".equals(status)){
            setStatus = AuditStatus.CANCELED.getStatus();
        } else if("stop".equals(status)){
            setStatus = AuditStatus.STOPED.getStatus();
        }
        if(null == contract || null == SupplierConstant.PREVIOUS_AUDIT_STATUS.get(setStatus) 
                || !SupplierConstant.PREVIOUS_AUDIT_STATUS.get(setStatus).contains(contract.getAuditStatus())){
            return new ResultInfo<>(new FailInfo("找不到供应商信息。"));
        }
        return contractProxy.auditContract(contract,setStatus,"",super.getUserId(),super.getUserName());
    }

}
