package com.tp.backend.controller.supplier;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tp.backend.controller.supplier.ao.SupplierAO;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.BankType;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.Option;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.TaxRate;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.SupplierAttach;
import com.tp.model.sup.SupplierBankAccount;
import com.tp.model.sup.SupplierCategory;
import com.tp.model.sup.SupplierImage;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierInvoice;
import com.tp.model.sup.SupplierLink;
import com.tp.model.sup.SupplierUser;
import com.tp.model.sup.SupplierXgLink;
import com.tp.model.usr.Department;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.bse.BrandProxy;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.proxy.sup.AuditRecordsProxy;
import com.tp.proxy.sup.SupplierAttachProxy;
import com.tp.proxy.sup.SupplierBankAccountProxy;
import com.tp.proxy.sup.SupplierCategoryProxy;
import com.tp.proxy.sup.SupplierImageProxy;
import com.tp.proxy.sup.SupplierInfoProxy;
import com.tp.proxy.sup.SupplierInvoiceProxy;
import com.tp.proxy.sup.SupplierLinkProxy;
import com.tp.proxy.sup.SupplierUserProxy;
import com.tp.result.sup.SupplierDTO;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

@Controller
@RequestMapping("/supplier/")
public class SupplierController extends SupplierBaseController {

    private final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    private SupplierInfoProxy supplierInfoProxy;
    @Autowired
    private SupplierLinkProxy supplierLinkProxy;
    @Autowired
    private SupplierCategoryProxy supplierCategoryProxy;
    @Autowired
    private SupplierBankAccountProxy supplierBankAccountProxy;
    @Autowired
    private SupplierInvoiceProxy supplierInvoiceProxy;
    @Autowired
    private SupplierAttachProxy supplierAttachProxy;
    @Autowired
    private SupplierImageProxy supplierImageProxy;
    @Autowired
    private AuditRecordsProxy auditRecordsProxy;
    @Autowired
    private SupplierUserProxy supplierUserProxy;
    @Autowired
    private CategoryProxy categoryProxy;
    @Autowired
    private BrandProxy brandProxy;
    @Autowired
    private SupplierAO supplierAO;
    
    @RequestMapping(value = { "/supplierList" })
    public String list(final Model model,SupplierInfo supplierInfo,Integer page,Integer size,String brandName) {
    	Map<String,Object> params = BeanUtil.beanMap(supplierInfo);
    	params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " update_time desc");

    	if( params.containsKey("name")){
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like '%"+params.get("name")+"%'");  
    		params.remove("name");
    	}
    	
    	if( params.containsKey("key1")){
    		if( params.containsKey(MYBATIS_SPECIAL_STRING.LIKE.name()) ){
            	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), params.get(MYBATIS_SPECIAL_STRING.LIKE.name()) + " and key1 like '%"+params.get("key1")+"%'");      			
    		}else{
            	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(),  "key1 like '%"+params.get("key1")+"%'");      			    			
    		}
    		params.remove("key1"); 
    	}

        final PageInfo<SupplierInfo> pageInfo = supplierInfoProxy.queryPageByParam(params,new PageInfo<SupplierInfo>(page,size)).getData();// 获取供应商信息
        model.addAttribute("page", pageInfo);
        model.addAttribute("supplierInfo", supplierInfo);
        model.addAttribute("brandName", brandName);
        model.addAttribute("supplierTypeMap", SupplierConstant.SUPPLIER_TYPES);
        model.addAttribute("auditStatusMap", SupplierConstant.AUDIT_STATUS_MAP_SELECT);
        model.addAttribute("auditStatusMapAll", SupplierConstant.AUDIT_STATUS_MAP_SELECT_ALL);
        return "supplier/supplier_list";
    }

    /**
     * 跳转到供应商列表页面
     */
    @RequestMapping(value = "/supplierAddSucc", method = RequestMethod.GET)
    public void supplierAddSucc() {
    }

    /**
     * 跳转到供应商添加页面
     */
    @RequestMapping(value = "/supplierAdd", method = RequestMethod.GET)
    public String supplierAdd(final Model model, final HttpServletRequest request) {
        model.addAttribute("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
        model.addAttribute("supplierTypeExplanations", SupplierConstant.SUPPLIERTYPE_EXPLANATIONS);
        List<Category> categorys = new ArrayList<Category>();
        try {
             Map<String,Object> params = new HashMap<String,Object>();
             params.put("status", Constant.ENABLED.YES);
             params.put("parentId", 0L);
            categorys = categoryProxy.queryByParam(params).getData();
        } catch (final Exception e) {
            logger.error("Query first category error.");
            logger.error(e.getMessage(), e);
        }
        model.addAttribute("categorys", categorys);
        model.addAttribute("defaultVal", "大类");
        // 进项税率列表
        final List<TaxRate> jsRateList = supplierInfoProxy.getRatesByType(TaxRateEnum.PRIMERATE);

        model.addAttribute("bankTypeList", genBankTypeStr());
        model.addAttribute("customsChannelList", genCustomsChannelStr());
        model.addAttribute("jsRateList", jsRateList);
        model.addAttribute("supplierUserStatusMap", SupplierConstant.SUPPLIERUSERSTATUS_MAP);
        model.addAttribute("departmentList", generateDepartmentOpts());
        return "supplier/supplier_add";
    }

    /**
     * 生成部门的相关信息
     *
     * @return
     */
    private List<Department> generateDepartmentOpts() {
        final List<Department> departments = supplierInfoProxy.getDepartments();
        return departments;
    }

    /**
     * 生成银行类型字符串
     *
     * @return
     */
    private BankType[] genBankTypeStr() {
    	return BankType.values();
    }

    /**
     * 生成通关渠道字符串
     *
     * @return
     */
    private List<ClearanceChannels> genCustomsChannelStr() {
       final List<ClearanceChannels> ClearanceChannels = supplierInfoProxy.getClearanceChannels();
       return ClearanceChannels;
    }

    @RequestMapping(value = "/supplierBaseSave", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<Long> supplierBaseInfoSave(HttpServletRequest request) {
    	final Map<String, Object> retMap = supplierAO.genSupplierBaseInfo(request, true);
        if (supplierAO.checkResult(retMap)) {
            final SupplierDTO supplierDTO = (SupplierDTO) retMap.get(SupplierConstant.DATA_KEY);
            return supplierInfoProxy.saveSupplierBaseInfo(supplierDTO);
        }
        return new ResultInfo<>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }

    /**
     * 去文件上传页面
     *
     * @return
     */
    @RequestMapping(value = "/toSupplierLicenAdd", method = RequestMethod.GET)
    public String toSupplierLicenAdd(Model model,final Long spId) {
        // 1: 获取供应商id 并校验状态
        final SupplierInfo supplierInfo = supplierInfoProxy.queryById(spId).getData();
        if (!AuditStatus.WAIT_UPLOAD_FILE.getStatus().equals(supplierInfo.getAuditStatus())) {
            return "supplier/supplier_add_licen";
        }
        // 2: 设置供应商信息 返回到页面
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("supplierId", spId);
        params.put("status", Constant.ENABLED.YES);
        List<SupplierCategory> categoryList = supplierCategoryProxy.queryByParam(params).getData();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("supplierId", spId);
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.supplier.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.supplier.name());
        return "supplier/supplier_add_licen";
    }

    /**
     * <pre>
     * 保存供应商附件信息
     * </pre>
     *
     * @return
     */
    @RequestMapping(value = "/supplierLicenSave", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<Boolean> supplierLicenSave(final HttpServletRequest request,final Long spId,Integer needAudit) {
        // 1: 获取供应商id 并校验状态
        final SupplierInfo supplierInfo = supplierInfoProxy.queryById(spId).getData();
        if (!AuditStatus.WAIT_UPLOAD_FILE.getStatus().equals(supplierInfo.getAuditStatus())) {
        	return new ResultInfo<>(new FailInfo("此状态不可上传附件！请刷新列表页面。"));
        }
        /*
         * 获取页面的消息
         */
        final Map<String, Object> retMap = supplierAO.genSupplierLicenInfo(request, spId, true);
        if (checkResult(retMap)) {
            final SupplierAttach supplierAttach = (SupplierAttach) retMap.get(SupplierConstant.DATA_KEY);
	        supplierAttach.setSupplierId(supplierInfo.getId());
	        supplierAttach.setStatus(Constant.ENABLED.YES);
	        supplierAttach.setCreateUser(super.getUserName());
	        ResultInfo<Boolean> resultInfo = supplierInfoProxy.saveSupplierLicenInfo(supplierAttach);
	        if(!resultInfo.success){
	        	return new ResultInfo<>(resultInfo.msg);
	        }
	        supplierInfo.setAuditStatus(AuditStatus.EDITING.getStatus());
	        ResultInfo<Integer> result= supplierInfoProxy.updateNotNullById(supplierInfo);
	        if(!result.success){
	        	return new ResultInfo<>(resultInfo.msg);
	        }
	        if (Constant.SELECTED.YES.equals(needAudit)) {
	            // 执行审核操作
	        	return supplierInfoProxy.auditSupplier(supplierInfo, AuditStatus.EXAMING.getStatus(),"",super.getUserId(),super.getUserName());
	        }
	        return new ResultInfo<>(Boolean.TRUE);
        }else{
        	return new ResultInfo<Boolean>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
        }
    }

    /**
     * 跳转到供应商编辑页面
     */
    @RequestMapping(value = "/supplierEdit", method = RequestMethod.GET)
    public ModelAndView supplierEdit(final ModelMap model,
        @RequestParam(value = "spId", required = true) final Long spId, final HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav = setSuppplierDetail(mav, spId, request);
        mav.setViewName("supplier/supplier_edit");
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.supplier.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.supplier.name());
        return mav;
    }

    /**
     * <pre>
     * 供应商详情
     * </pre>
     *
     * @param spId
     * @return
     */
    @RequestMapping(value = "supplierDetail")
    public ModelAndView supplierDetail(@RequestParam(value = "spId", required = true) final Long spId,
        final HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav = setSuppplierDetail(mav, spId, request);
        final AuditRecords doCondition = new AuditRecords();
        doCondition.setStatus(Constant.ENABLED.YES);
        doCondition.setAuditId(spId);
        doCondition.setBillType(BillType.SPLIST.getValue());
        final List<AuditRecords> auditRecords = auditRecordsProxy.queryByObject(doCondition).getData();
        mav.addObject("auditRecords", auditRecords);
        mav.addObject("bucketURL", Constant.IMAGE_URL_TYPE.supplier.url);
        mav.addObject("bucketname", Constant.IMAGE_URL_TYPE.supplier.name());
        mav.setViewName("supplier/supplier_show");
        return mav;
    }

    @RequestMapping("supplierAuditSave")
    @ResponseBody
    public ResultInfo<Boolean> auditSave(final Long spIdAudit,String status) {
        final SupplierInfo supplierInfo = supplierInfoProxy.queryById(spIdAudit).getData();
        Integer setStatus = null;
        if ("submit".equals(status)) {
            setStatus = AuditStatus.EXAMING.getStatus();
        } else if ("cancel".equals(status)) {
            setStatus = AuditStatus.CANCELED.getStatus();
        } else if ("stop".equals(status)) {
            setStatus = AuditStatus.STOPED.getStatus();
        }
        if (null == supplierInfo || null == SupplierConstant.PREVIOUS_AUDIT_STATUS.get(setStatus)
            || !SupplierConstant.PREVIOUS_AUDIT_STATUS.get(setStatus).contains(supplierInfo.getAuditStatus())) {
        	return new ResultInfo<>(new FailInfo("找不到供应商信息。"));
        }
        return supplierInfoProxy.auditSupplier(supplierInfo, setStatus,"",super.getUserId(),super.getUserName());
    }

    /**
     * <pre>
     * 执行审批
     * </pre>
     *
     * @param spIdAudit
     * @param request
     * @return
     */
    @RequestMapping("supplierAuditExec")
    @ResponseBody
    public ResultInfo<Boolean> auditExec(final Long execId,Integer auditStatus,String auditContent) {
        final ModelMap map = new ModelMap();
        final SupplierInfo supplierInfo = supplierInfoProxy.queryById(execId).getData();
        map.put("nextTab", SupplierConstant.SP_SUPPLIERLIST_TAB_ID);
        map.put("nextUrl", "/supplier/supplierList.htm");
        if (null == supplierInfo || !AuditStatus.EXAMING.getStatus().equals(supplierInfo.getAuditStatus())) {
        	return new ResultInfo<>(new FailInfo("审核状态不对，无法提交审核。"));
        }
        return supplierInfoProxy.auditSupplier(supplierInfo, auditStatus, auditContent, super.getUserId(),super.getUserName());
    }

    /**
     * <pre>
     * 设置供应商详情信息
     * </pre>
     *
     * @param mav
     * @return
     */
    private ModelAndView setSuppplierDetail(final ModelAndView mav, final Long supplierId,
        final HttpServletRequest request) {
        final SupplierInfo supplierInfo = supplierInfoProxy.queryById(supplierId).getData();
        if (null == supplierInfo) {
        	mav.addObject("errorMessage", "找不到供应商信息。");
            return mav;
        }
        final SupplierDTO supplierDTO = supplierInfoProxy.queryInfoAllById(supplierId);
        final List<Department> departments = supplierInfoProxy.getDepartments();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("supplierId", supplierId);
        params.put("status", Constant.ENABLED.YES);
        final SupplierAttach supplierAttacheVO = supplierAttachProxy.queryUniqueByParams(params).getData();
        if(supplierAttacheVO!=null){
        	List<SupplierImage> supplierImageList = supplierImageProxy.queryByParam(params).getData();
        	supplierAttacheVO.setSupplierImageList(supplierImageList);
        }
        final List<SupplierCategory> supplierCateogyrVo = supplierDTO.getSupplierCategoryList();
        setCategorySiblingsInfo(mav, supplierCateogyrVo);
        final List<Category> categorys = categoryProxy.getFirstCategoryList();
        /**
         * 根据供应商Id获取商家信息
         */
        final SupplierUser supplierUserVO = supplierUserProxy.getSupplierUserById(supplierId);

        mav.addObject("categorys", categorys);
        // 进项税率列表
        final List<TaxRate> jsRateList = supplierInfoProxy.getRatesByType(TaxRateEnum.PRIMERATE);
        final List<SupplierCategory> brandList = supplierCategoryProxy.getSupplierCategoryInfo(supplierId);
        mav.addObject("brandList", brandList);
        mav.addObject("customsChannelList", genCustomsChannelStr());
        mav.addObject("jsRateList", jsRateList);
        mav.addObject("daleiCateList", categoryProxy.getFirstCategoryList());
        mav.addObject("supplierAttacheVO", supplierAttacheVO);
        mav.addObject("supplierVO", supplierInfo);
        mav.addObject("supplierUserVO", supplierUserVO);
        mav.addObject("supplierId", supplierId);
        mav.addObject("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
        mav.addObject("supplierTypeExplanations", SupplierConstant.SUPPLIERTYPE_EXPLANATIONS);
        mav.addObject("supplierBankTypes", SupplierConstant.SUPPLIER_BANK_TYPES);
        mav.addObject("supplierRecordations", supplierInfoProxy.getClearanceChannels());
        mav.addObject("supplierCurrencyTypes", SupplierConstant.SUPPLIER_CURRENCY_TYPES);
        mav.addObject("supplierLinkTypes", SupplierConstant.SUPPLIER_LINK_TYPES);
        mav.addObject("supplierUserStatusMap", SupplierConstant.SUPPLIERUSERSTATUS_MAP);
        mav.addObject("statusShow", SupplierConstant.AUDIT_STATUS_MAP_ALL.get(supplierInfo.getAuditStatus()));
        mav.addObject("departmentList", departments);
        mav.addObject("supplierDTO", supplierDTO);
        // 生成部门和用户列表的对应关系
        mav.addObject("departmentUserMap", generateDepartmentUsers(supplierDTO.getSupplierXgLinkList()));
        return mav;
    }

    /**
     * 生成部门和用户的对应关系
     *
     * @return
     */
    private Map<String, List<UserInfo>> generateDepartmentUsers(final List<SupplierXgLink> supplierXgLinkList) {
        final List<Long> departmentIds = new ArrayList<Long>();
        Map<String, List<UserInfo>> retMap = new HashMap<String, List<UserInfo>>();
        if (null == supplierXgLinkList || supplierXgLinkList.size() == 0) {
            return retMap;
        }
        for (final SupplierXgLink linkVO : supplierXgLinkList) {
            departmentIds.add(Long.parseLong(linkVO.getDeptId()));
        }
        retMap = supplierInfoProxy.getDepartmentUsersMap(departmentIds);
        return retMap;
    }

    /**
     * 设置同级的分类map
     *
     * @return
     */
    private ModelAndView setCategorySiblingsInfo(final ModelAndView mav, final List<SupplierCategory> supplierCateogyrVo) {
        final List<Long> cIdList = new ArrayList<Long>();
        Map<String, List<Category>> categoryMap = new HashMap<String, List<Category>>();
        if (null != supplierCateogyrVo && supplierCateogyrVo.size() > 0) {
            for (final SupplierCategory categoryVO : supplierCateogyrVo) {
                cIdList.add(categoryVO.getCategoryBigId());
                cIdList.add(categoryVO.getCategoryMidId());
                cIdList.add(categoryVO.getCategorySmallId());
            }
            categoryMap = categoryProxy.getCategorySiblings(cIdList);
        }
        mav.addObject("categorySiblingsMap", categoryMap);
        return mav;
    }

    /**
     * <pre>
     * 供应商编辑信息保存
     * </pre>
     *
     * @return
     */
    @RequestMapping(value = "supplierEditSave", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<Boolean> supplierEditSave(final HttpServletRequest request,final Long spId,String statusAudit){
    	// by zhs 0129
        Integer setStatus = null;
        if ("submit".equals(statusAudit)) {
            setStatus = AuditStatus.EXAMING.getStatus();
        }else {
        	setStatus = AuditStatus.EDITING.getStatus();
		}
        /**
         * 是否需要校验非空
         */
        final Boolean needCheckNull = Boolean.FALSE;
        /*
         * 获取页面的供应商基本信息
         */
        final Map<String, Object> baseInfoMap = supplierAO.genSupplierBaseInfo(request, needCheckNull);
        if (!checkResult(baseInfoMap)) {
        	return new ResultInfo<Boolean>(new FailInfo((String) baseInfoMap.get(SupplierConstant.MESSAGE_KEY)));
        }
        final SupplierDTO supplierDTO = (SupplierDTO) baseInfoMap.get(SupplierConstant.DATA_KEY);
        supplierDTO.setId(spId);
        /*
         * 获取供应商证件相关信息
         */
        final Map<String, Object> licenInfoMap = supplierAO.genSupplierLicenInfo(request, spId, needCheckNull);
        if (!checkResult(licenInfoMap)) {
        	return new ResultInfo<Boolean>(new FailInfo((String) licenInfoMap.get(SupplierConstant.MESSAGE_KEY)));
        }
        final SupplierAttach supplierAttachDTO = (SupplierAttach) licenInfoMap.get(SupplierConstant.DATA_KEY);

        supplierDTO.setAuditStatus(setStatus);

//        final Map<String, Object> updateInfo = supplierAO.updateSupplierInfo(supplierDTO, supplierAttachDTO);
//
//        if (!checkResult(updateInfo)) {
//        	return new ResultInfo<Boolean>(new FailInfo((String) updateInfo.get(SupplierConstant.MESSAGE_KEY)));
//        }
//        supplierDTO.setId(spId);
//        if (null != setStatus) {
//            supplierDTO.setAuditStatus(setStatus);
//        } else {
//            supplierDTO.setAuditStatus(AuditStatus.EDITING.getStatus());
//        }
        ResultInfo<Boolean> resultInfo = supplierInfoProxy.updateSupplierInfo(supplierDTO, supplierAttachDTO);
        if (resultInfo.success) {
            auditRecordsProxy.saveAuditRecords(supplierDTO, setStatus, "", BillType.SPLIST,super.getUserId(),super.getUserName());
        }
        return resultInfo;
    }

    /**
     * <pre>
     * 获取供应商信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("getSuppliers")
    public String getSupplierInfo(ModelMap map,HttpServletRequest request,String supplierType) {
        Map<String,Object> params = generateSearchInfo(request);
        params.put("status", Constant.ENABLED.YES);
        params.put("auditStatus",AuditStatus.THROUGH.getStatus());
        params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " update_time desc,supplier_type asc");
        
        // by zhs 0115 增加 supplierType查询条件
//        if (supplierType != null) {
//            if(	 params.containsKey("supplierType") ){
//            	params.remove("supplierType");
//            	params.put("supplierType", supplierType);
//            }
//		}
        ///////////
        final PageInfo<SupplierInfo> page = supplierInfoProxy.queryPageByParamNotEmpty(params, new PageInfo<SupplierInfo>(1, 500)).getData();
        map.addAllAttributes(params);
        map.addAttribute("sList", page.getRows());
        map.addAttribute("supplierTypeMap", SupplierConstant.SUPPLIER_TYPES);
        map.addAttribute("supplierType", supplierType);
        return "supplier/pop_table/suplier_pop";
    }
    
    @RequestMapping("checkSupplierInfo")
    @ResponseBody
    public ResultInfo<SupplierInfo> checkSupplierInfo(SupplierInfo supplierInfo) {
        final ModelMap map = new ModelMap();
        supplierInfo.setSupplierType(null);
        final PageInfo<SupplierInfo> page = supplierInfoProxy.queryPageByObject(supplierInfo, new PageInfo<SupplierInfo>(1, 10)).getData();
        final List<SupplierInfo> suppliers = page.getRows();
        SupplierInfo supplierVO = null;
        
        if (null != suppliers && suppliers.size() == 1) {
            supplierVO = suppliers.get(0);
            supplierVO.setSupplierType(SupplierConstant.SUPPLIER_TYPES.get(supplierVO.getSupplierType()));
            map.put("supplier", supplierVO);
            if(StringUtils.isNotBlank(supplierVO.getName()) || StringUtils.isNotBlank(supplierVO.getAlias())){
            	return new ResultInfo<>(supplierVO);
            }
        } else if(null != suppliers && suppliers.size() > 1 && (StringUtils.isNotBlank(supplierInfo.getName()) || StringUtils.isNotBlank(supplierInfo.getAlias()))){
        	return new ResultInfo<>(new FailInfo("有多个结果"));
        } else {
        	return new ResultInfo<>(new FailInfo("不存在"));
        }
        return new ResultInfo<>(new FailInfo("出错"));
    }

    /**
     * <pre>
     * 获取供应商品牌
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getSupplierBrands")
    public String getSupplierBrands(Model model,Integer brandId,String brandName) {
        final Map<String, Object> searchPrams = new HashMap<String, Object>();
        if(brandId!=null){
        	searchPrams.put("id", brandId);
        }
        if(StringUtil.isNotBlank(brandName)){
        	searchPrams.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " name like concat('%','"+brandName+"','%')");
        }
        searchPrams.put("status", Constant.ENABLED.YES);
        final List<Brand> brandList = brandProxy.queryByParam(searchPrams).getData();
        List<Category> categorys = categoryProxy.getFirstCategoryList();
        model.addAttribute("categorys", categorys);
        model.addAttribute("defaultVal", "大类");
        model.addAttribute("brands", brandList);
        model.addAttribute("categoryHtml", "");
        return "supplier/pop_table/brand_pop";
    }
    
    @RequestMapping("/getCategorys")
    public String getCategoryOption(Model model,Integer brandId,String brandName){
    	List<Category> categorys = categoryProxy.getFirstCategoryList();
    	 model.addAttribute("categorys", categorys);
         model.addAttribute("defaultVal", "大类");
    	return "supplier/common/base/category_option";
    }
    /**
     * <pre>
     * 获取供应商品牌
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getbrandNames")
    @ResponseBody
    public List<SupplierCategory> getbrandNames(ModelMap map,final Long spId) {
    	return supplierCategoryProxy.getSupplierCategoryInfo(spId);
    }

    /**
     * 获取供应商分类
     *
     * @param spId
     * @param request
     * @return
     */
    @RequestMapping("getSpCategorys")
    @ResponseBody
    public ResultInfo<List<Option>> getSupplierCategory(Long spId,Long brandId,Long cid,String cType) {//spId=153&brandId=61&cid=61&cType=0
        final List<Category> categoryList = supplierCategoryProxy.getSupplierCategory(spId,brandId, cid, cType);
        List<Option> optionList = new ArrayList<Option>();
        if(CollectionUtils.isNotEmpty(categoryList)){
        	for(Category category:categoryList){
        		optionList.add(new Option(category.getId().intValue(),category.getName()));
        	}
        }
        return new ResultInfo<>(optionList);
    }

    /**
     * <pre>
     * 校验品牌信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/checkBrandInfo")
    @ResponseBody
    public ResultInfo<Brand> checkBrandInfo(Long brandId,String brandName) {
        final Map<String, Object> searchPrams = new HashMap<String, Object>();
        if(brandId!=null){
        	searchPrams.put("id", brandId);
        }
        searchPrams.put("name", brandName);
        return brandProxy.queryUniqueByParams(searchPrams);
    }
    
    /**
     * 获取供应商联系人信息
     * 
     * @param request
     * @return
     */
    @RequestMapping("/getSupplierLinkers")
    @ResponseBody
    public ResultInfo<List<SupplierLink>> getSupplierLinkers(String linkerType,String linkName,Long supplierId) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("supplierId", supplierId);
    	params.put("status", Constant.ENABLED.YES);
    	params.put("linkType", linkerType);
    	if(StringUtils.isNotBlank(linkName)){
    		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),"link_name like concat ('%','"+linkName+"','%')");
    	}
        return supplierLinkProxy.queryByParamNotEmpty(params);
    }
    
    /**
     * 获取供应商银行信息
     * 
     * @return
     */
    @RequestMapping("/getSupplierBank")
    @ResponseBody
    public List<SupplierBankAccount> getSupplierBanks(Long supplierId,String bankAccount) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("supplierId", supplierId);
    	params.put("bankAccount", bankAccount);
    	return supplierBankAccountProxy.queryByParam(params).getData();
    }
    
    /**
     * 获取供应商银行信息
     * 
     * @return
     */
    @RequestMapping("/getSupplierInvoices")
    public List<SupplierInvoice> getSupplierInvoices(Long supplierId,String invoiceInfo) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("supplierId", supplierId);
    	params.put("invoiceInfo", invoiceInfo);
    	return supplierInvoiceProxy.queryByParam(params).getData();
    }
    /**
     * <pre>
     * 生成查询条件
     * </pre>
     *
     * @param supplierDo
     */
    private Map<String,Object> generateSearchInfo(final HttpServletRequest request) {
    	Map<String,Object> params = new HashMap<String,Object>();

    	// by zhs 01141743 增加name的查询
        String valname = getStringVal(request, "name");
        if(StringUtil.isNotBlank(valname)){
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like '%"+valname+"%'");
        }
    	///////////////
        
        final Long supplierId = getLongVal(request, "supplierId");
        if (null != supplierId) {
        	params.put("id", supplierId);
        }
        params.put("alias",getStringVal(request, "simpleName"));
        String nameSearch = getStringVal(request, "nameSearch");
        String supplierName = getStringVal(request, "supplierName");
        if(StringUtil.isNotBlank(nameSearch)){
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like concat('%','"+nameSearch+"','%')");
        }
        if(StringUtil.isNotBlank(supplierName)){
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like concat('%','"+nameSearch+"','%')");
        }
        params.put("key1",getStringVal(request, "brandName"));
        params.put("isSea",getStringVal(request, "isSeaWashes"));	
        params.put("auditStatus",getIntegerVal(request, "supplierStatus"));
        final String supplierType = getStringVal(request, "supplierType");
        if ("contract".equals(supplierType)) {
        	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " supplier_type in ('"+SupplierType.SELL.getValue()
        			+"','"+SupplierType.PURCHASE.getValue()+"','"+SupplierType.ASSOCIATE.getValue()+"')");
        } else {
            params.put("supplierType",getStringVal(request, "brandName"));
        }
        return params;
    }
}
