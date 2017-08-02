package com.tp.backend.controller.supplier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.GroupLayout.SequentialGroup;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.helpers.SyslogQuietWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP.Basic.Return;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.controller.supplier.ao.ContractAO;
import com.tp.backend.util.SupplerFreemarkerUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.common.vo.ord.OrderDeliveryConstant.deliveryOrderType;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.ConstractConstant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.Contract;
import com.tp.model.sup.SupplierBrand;
import com.tp.model.sup.SupplierCategory;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.usr.Department;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.sup.AuditRecordsProxy;
import com.tp.proxy.sup.ContractProxy;
import com.tp.proxy.sup.SupplierCategoryProxy;
import com.tp.proxy.sup.SupplierInfoProxy;
import com.tp.result.sup.ContractDTO;
import com.tp.result.sup.SupplierDTO;
import com.tp.service.prd.IItemService;
import com.tp.util.BeanUtil;

import freemarker.core._RegexBuiltins.replace_reBI;

@Controller
@RequestMapping("/supplier/contract/")
public class ContractController extends AbstractBaseController {

    @Autowired
    private ContractProxy contractProxy;
    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;
    @Autowired
    private AuditRecordsProxy auditRecordsProxy;
    @Autowired
    private SupplierCategoryProxy supplierCategoryProxy;
    @Autowired
    private IItemService itemService;
    
    @Autowired
    private SupplierInfoProxy supplierInfoProxy;
    
    @Autowired
    private ContractAO contractAO;
    
    
    private final static BillType billType = BillType.CONSTRACT;

    /**
     * 到新增合同页面 {method description}.
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public void contractAdd(final ModelMap model) {
    	List<Map<String,String>> departOptionList = generateDepartmentOpts();
        model.addAttribute("AduitStatusMap", SupplierConstant.AUDIT_STATUS_MAP_SELECT);
        model.addAttribute("costTypesMap", SupplierConstant.COST_TYPE_MAP);
        model.addAttribute("currencyTypes", SupplierConstant.SUPPLIER_CURRENCY_TYPES);
        model.addAttribute("settlementruleTypeMap", SupplierConstant.SETTLEMENTRULETYPE_MAP);
        model.addAttribute("settlementruleDayTypeMap",  SupplierConstant.SETTLEMENTRULEDAYTYPE_MAP);
        model.addAttribute("settlementRuleCountMap", SupplierConstant.SETTLEMENTRULECOUNT_MAP);
        //for js use
        model.addAttribute("departmentOpt", JSONObject.toJSONString(departOptionList));
        model.addAttribute("departmentList", departOptionList);
        model.addAttribute("contractPartyA", ConstractConstant.CONTRACT_PARTY_A_MAP);
        model.addAttribute("salesWayMap", ConstractConstant.SALES_WAY_MAP);
        model.addAttribute("supplierLinkTypes", SupplierConstant.SUPPLIER_LINK_TYPES);
    }
    /**
     * 生成部门的相关信息
     * 
     * @return
     */
    private List<Map<String,String>> generateDepartmentOpts() {
    	List<Map<String,String>> departmentList = new ArrayList<Map<String,String>>();
    	List<Department> departmentDOs = supplierInfoProxy.getDepartments();
    	if(null != departmentDOs && departmentDOs.size()>0) {
    		Map<String,String> departmentMap = null;
    		for(Department department : departmentDOs) {
    			departmentMap = new HashMap<String,String>();
    			departmentMap.put("id", department.getId()+"");
    			departmentMap.put("name", department.getName());
    			departmentList.add(departmentMap);
    		}
    	}
    	return departmentList;
    }
    /**
     * 跳转合同编辑页面
     * 
     * @return
     */
    @RequestMapping(value="edit",method=RequestMethod.GET)
    public void contractEdit(Model model,Long cId) {
    	ResultInfo<Contract> resutlInfo = contractProxy.queryById(cId);
    	if(!resutlInfo.success) {
    		model.addAttribute("errorMsg", "找不到此合同信息。");
    		return;
    	}
    	//校验状态
    	if(!AuditStatus.EDITING.getStatus().equals(resutlInfo.getData().getAuditStatus()) 
    			&& !AuditStatus.REFUSED.getStatus().equals(resutlInfo.getData().getAuditStatus())) {
    		model.addAttribute("errorMsg", "此状态不可以编辑。");
    		return;
    	}
    	setpageDetail(model,cId);
    }
    
    /**
     * 跳转合同展示页面
     * 
     * @return
     */
    @RequestMapping(value="show",method=RequestMethod.GET)
    public void contractShow(Model model,@RequestParam("cId") Long cid){
    	Contract contract = contractProxy.getContractById(cid);
    	if(null == contract) {
    		model.addAttribute("errorMsg", "找不到此合同信息。");
    		return;
    	}
    	//校验状态
    	setpageDetail(model,cid);
    	AuditRecords doCondition = new AuditRecords();
        doCondition.setStatus(Constant.ENABLED.YES);
        doCondition.setAuditId(cid);
        doCondition.setBillType(billType.getValue());
    	ResultInfo<List<AuditRecords>> auditRecords = auditRecordsProxy.queryByObject(doCondition);
    	model.addAttribute("auditRecords",auditRecords.getData());
    }
    
    /**
     * 设置页面的详细信息
     * 
     * @param mav
     * @return
     */
    private void setpageDetail(Model mav,Long cId){
    	List<Department> departmentDOs = supplierInfoProxy.getDepartments();
    	ContractDTO  contractVO= contractProxy.getContractById(cId);
    	mav.addAttribute("contractVO",contractVO);
    	mav.addAttribute("aduitStatusMap", SupplierConstant.AUDIT_STATUS_MAP_SELECT);
        mav.addAttribute("costTypesMap", SupplierConstant.COST_TYPE_MAP);
        mav.addAttribute("currencyTypes", SupplierConstant.SUPPLIER_CURRENCY_TYPES);
        mav.addAttribute("settlementruleTypeMap", SupplierConstant.SETTLEMENTRULETYPE_MAP);
        mav.addAttribute("settlementruleDayTypeMap",  SupplierConstant.SETTLEMENTRULEDAYTYPE_MAP);
        mav.addAttribute("contractTypesMap", SupplierConstant.SUPPLIER_TYPES);
        mav.addAttribute("departmentList",departmentDOs);
        mav.addAttribute("settlementRuleCountMap", SupplierConstant.SETTLEMENTRULECOUNT_MAP);
        mav.addAttribute("contractPartyA", ConstractConstant.CONTRACT_PARTY_A_MAP);
        //生成部门和用户列表的对应关系
        mav.addAttribute("departmentUserMap",generateDepartmentUsers(departmentDOs));
        mav.addAttribute("salesWayMap", ConstractConstant.SALES_WAY_MAP);
        mav.addAttribute("supplierLinkTypes", SupplierConstant.SUPPLIER_LINK_TYPES);
        if(null != contractVO.getContractSettlementRuleList() && contractVO.getContractSettlementRuleList().size()>0){
        	mav.addAttribute("contractSettlementRuleVO",contractVO.getContractSettlementRuleList().get(0));
        }
        if(null != contractVO){
        	SupplierInfo supplierInfo = new SupplierInfo();
        	supplierInfo.setId(contractVO.getSupplierId());
        	supplierInfo.setStatus(Constant.ENABLED.YES);
        	SupplierDTO supplierVO = supplierInfoProxy.queryInfoAllById(contractVO.getSupplierId());
        	mav.addAttribute("supplierVo",supplierVO);
        }
    }
    /**
     * 生成部门和用户的对应关系
     * 
     * @return
     */
    private Map<String,List<UserInfo>> generateDepartmentUsers(
			List<Department> departmentDOs) {
    	List<Long> departmentIds = new ArrayList<Long>();
    	Map<String,List<UserInfo>> retMap = new HashMap<String,List<UserInfo>>();
    	if (null == departmentDOs || departmentDOs.size() == 0) {
			return retMap;
		}
    	for (Department department : departmentDOs) {
    		departmentIds.add(department.getId());
		}
    	retMap = supplierInfoProxy.getDepartmentUsersMap(departmentIds);
		return retMap;
	}
    /**
     * 跳转到弹出的添加商品的页面 {method description}.
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "addproduct")
    public String addproduct(Model model,Long supplierId) {
        SupplierBrand supplierBrand=new SupplierBrand();
        supplierBrand.setSupplierId(supplierId);
        supplierBrand.setStatus(Constant.ENABLED.YES);
        List<SupplierCategory> supplierBrandDOs = supplierCategoryProxy.getSupplierCategoryInfo(supplierId);
        List<SupplierCategory> supplierBrandDOsUni = new ArrayList<SupplierCategory>();
        if(CollectionUtils.isNotEmpty(supplierBrandDOs)){
        	Set<Long> bSet = new HashSet<Long>();
        	for(SupplierCategory category : supplierBrandDOs){
        		if(!bSet.contains(category.getBrandId())){
        			supplierBrandDOsUni.add(category);
        			bSet.add(category.getBrandId());
        		}
        	}
        }
        model.addAttribute("categoryDOList", supplierBrandDOsUni);
        model.addAttribute("supplierId", supplierId);
        return "supplier/pop_table/addproduct";
    }
    
    
    /**
     * 跳转到押金变更的页面
     */
    @RequestMapping(value="addDepositchange")
    public String addDepositchange(Model model,String basevalue){
        model.addAttribute("basevalue", basevalue);
        return "supplier/pop_table/addDepositchange";
    }


    @RequestMapping(value = {"list" })
    public void list(Model model,Contract contract,Integer page,Integer size) {
    	Map<String,Object> params = BeanUtil.beanMap(contract);
    	List<WHERE_ENTRY> whEntries = new ArrayList<>();
    	if(contract.getStartDate()!=null){
    		whEntries.add(new WHERE_ENTRY("start_date", MYBATIS_SPECIAL_STRING.GT, contract.getStartDate()));
//    		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " start_date >='"+DateUtil.formatDate(contract.getStartDate())+"'");
    	}
    	if(contract.getEndDate()!=null){
    		whEntries.add(new WHERE_ENTRY("end_date", MYBATIS_SPECIAL_STRING.LT, contract.getEndDate()));
//    		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " end_date <='"+DateUtil.formatDate(contract.getEndDate())+"'");
    	}
    	params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
    	params.remove("startDate");
    	params.remove("endDate");
        ResultInfo<PageInfo<Contract>> pageInfoResultInfo = contractProxy.queryPageByParam(params, new PageInfo<Contract>(page,size));
        model.addAttribute("page", pageInfoResultInfo.getData());
        model.addAttribute("contract", contract);
        model.addAttribute("contractTypesMap", SupplierConstant.SUPPLIER_TYPES);
        model.addAttribute("contractStatusMap", SupplierConstant.AUDIT_STATUS_MAP_SELECT);
    }

    /**
     * 保存合同的基本信息
     */
    @RequestMapping(value = "baseSave", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<Long> contractBaseInfoSave(Model model,HttpServletRequest request) {
    	/**
         * 获取页面的消息
         */
        Map<String, Object> retMap = contractAO.genContractBaseInfo(request, true);

        if (checkResult(retMap)) {
            ContractDTO contractDTO = (ContractDTO) retMap.get(SupplierConstant.DATA_KEY);
            ResultInfo<Long> resultInfo = contractProxy.saveContractBaseInfo(contractDTO);
            return resultInfo;
        }
    	return new ResultInfo<Long>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }
    
    /**
     * 保存合同编辑信息
     */
    @RequestMapping(value = "editSave", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<Long> contractEditInfoSave(Model model,HttpServletRequest request,@RequestParam(value="cid",required=true) Long cid) {
    	/**
    	 * 获取页面的消息
    	 */
    	Map<String, Object> retMap = contractAO.genContractBaseInfo(request, true);
    	if (checkResult(retMap)) {
    		ContractDTO contractDTO = (ContractDTO) retMap.get(SupplierConstant.DATA_KEY);
    		contractDTO.setId(cid);
    		return contractProxy.updateContractBaseInfo(contractDTO);
    	}
    	return new ResultInfo<Long>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }
    
    /**
     * 保存合同编辑信息
     */
    @RequestMapping(value = "editSub", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<Long> contractEditInfoSub(Model model,HttpServletRequest request,Long cid,String statusStr) {
        Integer setStatus = null;
        if("submit".equals(statusStr)){
            setStatus = AuditStatus.EXAMING.getStatus();
        } else if("cancel".equals(statusStr)){
            setStatus = AuditStatus.CANCELED.getStatus();
        } else if("save".equals(statusStr)){
            setStatus = AuditStatus.EDITING.getStatus();
        } else {
            setStatus = AuditStatus.EXAMING.getStatus();
        }
        Map<String,Object> retMap = contractAO.genContractBaseInfo(request,true);
        if(checkResult(retMap)) {
        	ContractDTO contractDTO = (ContractDTO)retMap.get(SupplierConstant.DATA_KEY);
	        contractDTO.setAuditStatus(setStatus);
	        if(null == cid){
	            ResultInfo<Long> resultInfo = contractProxy.saveContractBaseInfo(contractDTO);
	            if(resultInfo.success){
	                cid = resultInfo.data;
	            }
	            if(resultInfo.success && !"save".equals(statusStr)){
	                contractDTO.setId(cid);
	                auditRecordsProxy.saveAuditRecords(contractDTO, setStatus, "",billType,super.getUserName(),super.getUserId());
	            }
	            return resultInfo;
	        } else {
	        	 ResultInfo<Long> resultInfo = contractProxy.updateContractBaseInfo(contractDTO);
	        	 if(resultInfo.success && !"save".equals(statusStr)){
	                 contractDTO.setId(cid);
	                 auditRecordsProxy.saveAuditRecords(contractDTO, setStatus, "",billType,super.getUserName(),super.getUserId());
	             }
	        	 return resultInfo;
	        }
        }
        return new ResultInfo<Long>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }
    
    /**
     * 
     * <pre>
     *   校验合同信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("getContractInfo")
    @ResponseBody
    public ResultInfo<Contract> checkContractInfo(Contract contract){

    	Map<String,Object> params = BeanUtil.beanMap(contract);
    	ResultInfo<List<Contract>> contractList = contractProxy.queryByParam(params);
    	if(contractList.success && CollectionUtils.isNotEmpty(contractList.getData()) && contractList.getData().size()==1){
        	// by zhs 01142111 判断成功直接返回
    		return new ResultInfo<Contract>(contractList.getData().get(0));
    	}
    	
    	return new ResultInfo<Contract>(new FailInfo("校验合同不通过"));
    }
    
    /**
     * 获取供应商详细信息
     * 
     * @param request
     * @return
     */
    @RequestMapping("getSupplierContractInfo")
    @ResponseBody
    public ResultInfo<Map<String,Object>> getSupplierDetailInfo(Long supplierId) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	SupplierDTO supplierInfo = supplierInfoProxy.queryInfoAllById(supplierId);
        supplierInfo.setId(supplierId);
        supplierInfo.setStatus(Constant.ENABLED.YES);
        //合同编号
        String contractCode = contractProxy.generateContractCode(supplierInfo.getSupplierType(), supplierInfo.getIsSea());
        params.put("contractCode", contractCode);
        
        //合同大标题
        String template = contractProxy.getContractTemplate(supplierInfo.getSupplierType(), supplierInfo.getIsSea());
        String templateName = ConstractConstant.CONTRACT_TEMPLATE_MAP.get(template);
        String title = ConstractConstant.CONTRACT_TITLE_MAP.get(template);
        if(null == title){
        	title = "";
        }
        Calendar calendar = Calendar.getInstance();
        String contractTitle = title + supplierInfo.getName() + calendar.get(Calendar.YEAR) + CommonUtil.getMinIntegerDigits(new Long(1+calendar.get(Calendar.MONTH)), 2);
        params.put("contractTitle", contractTitle);
        params.put("contractTemplateName", templateName);
        
        params.put(SupplierConstant.SUCCESS_KEY, true);
        params.put(SupplierConstant.DATA_KEY, supplierInfo);
        return new ResultInfo<Map<String,Object>>(params);
        
    }
    
    /**
     * 
     * <pre>
     *   校验合同信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("getContracts")
    public String getContracts(Model model,Contract contract,Integer page,Integer size){
        contract.setStatus(Constant.ENABLED.YES);
        contract.setAuditStatus(AuditStatus.THROUGH.getStatus());
        Map<String,Object> params = BeanUtil.beanMap(contract);
        // by zhs 01141914 改为针对contractName，contractCode的模糊查询
        //      contractName, contractCode
        // sql contract_code, contract_name
        String sqlLike="";
        String key = "contractCode";
        String valContractCode = GetStrValFromMap(params, key);
        if ( !valContractCode.isEmpty() ) {
            params.remove(key);      
			sqlLike +="contract_code like '%" + valContractCode +"%'";
		}        
        key = "contractName";
        String valContractName = GetStrValFromMap(params, key);
        if ( !valContractName.isEmpty() ) {
            params.remove(key);  
            if (!sqlLike.isEmpty()) {
				sqlLike += " AND ";
			}
			sqlLike += "contract_name like '%" + valContractName +"%'";
		}
        if ( !sqlLike.isEmpty()) {
            params.put(MYBATIS_SPECIAL_STRING.LIKE.name(),  sqlLike);        	        						
		}
        ///////// 
        params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), page==null? 1:page+","+size==null?20:size);
        ResultInfo<List<Contract>> contractList = contractProxy.queryByParam(params);
        model.addAttribute("contracts", contractList);
        model.addAttribute("contract", contract);
        model.addAttribute("supplierId", contract.getSupplierId());
        model.addAttribute("contractTypesMap", SupplierConstant.SUPPLIER_TYPES);
        model.addAttribute(SupplierConstant.SUCCESS_KEY, true);
        return "supplier/pop_table/contract_pop";
    }
    
    
    /**
     * 
     *   获取map值
     *
     * @param mapparm
     * @param key
     * @return
     */
    // by zhs 01141914
    public String GetStrValFromMap( Map<String,Object> mapparm, String key ){
        if (mapparm.containsKey(key)) {
        	Object parObj = mapparm.get(key);
        	if (parObj != null) {
               	return String.valueOf( parObj );
			}           	
		}
    	return "";
    }
        
    @RequestMapping("contractPreview")
    public void contractPreview(@RequestParam(value="cid") Long contractId){
    	contractProxy.previewConract(contractId);
    }
    
    @RequestMapping("contractDownload")
    public void contractDownload(@RequestParam(value="cid") Long contractId,HttpServletRequest request,
    		HttpServletResponse reponse){
    	//contractProxy.downloadConract(contractId);
    }
    
}
