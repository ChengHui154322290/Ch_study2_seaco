package com.tp.backend.controller.supplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tp.backend.util.SupplerFreemarkerUtil;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.supplier.AuditConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.Audit;
import com.tp.proxy.sup.AuditProxy;
/**
 * 
 * <pre>
 *   供应商审批controller
 * </pre>
 *
 * @author Administrator
 * @version $Id: SupplierExamineController.java, v 0.1 2015年1月4日 上午9:24:45 Administrator Exp $
 */
@Controller
@RequestMapping("/supplier/")
public class SupplierExamineController extends SupplierBaseController {
    
    @Autowired
    private AuditProxy auditProxy;
    
    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;
    
    /**
     * 跳转到审批流设置列表页面
     * 
     */
    @RequestMapping(value="examinationList")
    public String examinationList(ModelMap model,HttpServletRequest request){
        Audit audit = new Audit();
        int index = getPageIndex(request, "index");//获取分页的首页
        int pageSize = getPageSize(request, "pageSize");//获取分页的大小
        audit = generateSearchInfo(audit, request);//生成查询条件
        //获取审批信息
        ResultInfo<PageInfo<Audit>> resultInfo = auditProxy.queryPageByObject(audit, new PageInfo<Audit>(index,pageSize));
        model.addAttribute("page", resultInfo.getData());
        model.addAttribute("billTypeMap",AuditConstant.BILL_TYPE_MAP);
        return "supplier/audit/examination_list";
    }
    
    /**
     * 跳转到审批流设置增加页面
     */
    @RequestMapping(value="examinationAdd",method=RequestMethod.GET)
    public String examinationAdd(Model model,HttpServletRequest request){
        return "supplier/audit/examination_add";
    }
    
    /**
     * 
     * <pre>
     *   审批流编辑
     * </pre>
     *
     * @return
     */
    @RequestMapping("examinationEdit")
    public String auditEdit(@RequestParam(required=true,value="id") Long auditId){
        return "supplier/audit/examination_edit";
    }
    
    /**
     * 
     * <pre>
     *   跳转到审批页面
     * </pre>
     *
     * @return
     */
    @RequestMapping("examination/toExaminationPage")
    public String getSupplierBrands(Model model,@RequestParam(value="supplierId",required=true)Long supplierId,HttpServletRequest request){
        model.addAttribute("execId", supplierId);
        model.addAttribute("actionUrl", "/supplier/supplierAuditExec.htm");
        return "supplier/pop_table/audit_pop";
    }
    
    /**
     * 
     * <pre>
     *   生成查询信息
     * </pre>
     *
     * @param Audit
     * @param request
     */
    private Audit generateSearchInfo(Audit audit, HttpServletRequest request) {
        Integer status = getIntegerVal(request, "status");
        audit.setStatus(status);
        String billType = getStringVal(request, "billType");
        String billVersion = getStringVal(request, "billVersion");
        String exUserName = getStringVal(request,"exUserName");
        audit.setBillType(billType);
        audit.setAuditVersion(billVersion);
        audit.setKeyWords(exUserName);
        return audit;
    }
    
}
