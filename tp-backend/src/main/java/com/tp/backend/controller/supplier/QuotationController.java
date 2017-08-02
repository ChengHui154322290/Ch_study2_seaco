package com.tp.backend.controller.supplier;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.controller.supplier.ao.QuotationAO;
import com.tp.backend.util.SupplerFreemarkerUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.*;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.OrderLine4ExcelDTO;
import com.tp.dto.sup.enums.QuotationPriceLogType;
import com.tp.dto.sup.excel.ExcelQuotationExportDTO;
import com.tp.model.sup.*;
import com.tp.proxy.sup.*;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.prd.SkuInfoResult;
import com.tp.service.prd.IItemService;
import com.tp.util.BeanUtil;
import com.tp.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.DocumentException;
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

/**
 * <pre>
 * 报价controller
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@RequestMapping("/supplier/")
@Controller
public class QuotationController extends AbstractBaseController {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuotationInfoProxy quotationInfoProxy;
    
    @Autowired
    private QuotationProductProxy quotationProductProxy;

    @Autowired
    private SupplerFreemarkerUtil supplerFreemarkerUtil;

    @Autowired
    private AuditRecordsProxy auditRecordsProxy;

    @Autowired
    private ContractProxy contractProxy;

    @Autowired
    private SupplierInfoProxy supplierInfoProxy;

    @Autowired
    private IItemService itemService;
    @Autowired
    private QuotationAO quotationAO;

    @Autowired
    private QuotationPriceLogProxy quotationPriceLogProxy;

    private final static BillType billType = BillType.PRICE;

    /**
     * 跳转到报价单列表页面
     */
    @RequestMapping(value = { "/quotationList" })
    public String list(ModelMap model, QuotationInfo quotationInfo,Integer page,Integer size) {
    	Map<String,Object> params = BeanUtil.beanMap(quotationInfo);
    	params.remove("startDate");
    	params.remove("endDate");
    	params.remove("quotationProductList");
    	if(quotationInfo.getStartDate()!=null){
    		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " start_date >='"+DateUtil.formatDate(quotationInfo.getStartDate())+"'");
    	}
    	if(quotationInfo.getEndDate()!=null){
    		params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " end_date <='"+DateUtil.formatDate(quotationInfo.getEndDate())+"'");
    	}
        params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(),"id desc");
        PageInfo<QuotationInfo> resultInfo = quotationInfoProxy.queryPageByParam(params,new PageInfo<QuotationInfo>(page,size)).getData();
        model.addAttribute("page", resultInfo);
        model.addAttribute("quotationDO", quotationInfo);
        model.addAttribute("auditStatusMap", SupplierConstant.O_AUDIT_STATUS_MAP_ALL);
        model.addAttribute("auditStatusMapStr", SupplierConstant.O_AUDIT_STATUS_MAP_SELECT);
        model.addAttribute("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
        return "supplier/quotation/quotation_list";
    }

    /**
     * 跳转到增加报价单页面
     */
    @RequestMapping(value = "quotationAdd", method = RequestMethod.GET)
    public String quotationAdd(final ModelMap modelMap, Long spId,Long cId) {
        if (null != spId && null != cId) {
            Contract contract = contractProxy.getContractById(cId);
            SupplierInfo supplierInfo = supplierInfoProxy.queryById(spId).getData();
            // TO 此处还需要加审核状态的校验
            if (null != spId && null != contract && spId.equals(contract.getSupplierId())) {
                modelMap.put("quotationType", QuotationType.CONTRACT_TYPE.getValue());
                modelMap.put("supplier", supplierInfo);
                modelMap.put("contract", contract);
            }
        }
        modelMap.put("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
        return "supplier/quotation/quotation_add";
    }

    /**
     * <pre>
     * 获取商品信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("quotation/getItemInfoForm")
    public String getItemInfoForm(Model model,Long supplierId,String supplierType) {
        model.addAttribute("supplierId", supplierId);
        model.addAttribute("supplierType", supplierType);
        return "supplier/pop_table/quota_item_pop";
    }


    /**
     * <pre>
     * 修改商品信息
     * </pre>

     * @return
     */
    @RequestMapping("quotation/modifyItem")
    public String modifyItem(Model model,String index,String product) {
        model.addAttribute("index", index);
        model.addAttribute("product", JSON.parseObject(product,QuotationProduct.class));
        return "supplier/pop_table/quota_item_pop_edit";
    }

    /**
     * <pre>
     * 商品历史价格
     * </pre>
     *
     * @return
     */
    @RequestMapping("quotation/productPriceHis")
    public String productPriceHis(Model model,String quotationProductId) {

        if(NumberUtils.isNumber(quotationProductId)){
            Long productId = Long.parseLong(quotationProductId);
            ResultInfo<List<QuotationPriceLog>> result = quotationPriceLogProxy.getLogByQuotationProductId(productId);
            if(result.isSuccess()){

                List<QuotationPriceLog> basePrices = new ArrayList<>();
                List<QuotationPriceLog> sumPrices = new ArrayList<>();
                for(QuotationPriceLog log: result.getData()){
                    if(log.getType().equals(QuotationPriceLogType.BASE_PRICE.getCode()))
                        basePrices.add(log);
                    else if(log.getType().equals(QuotationPriceLogType.SUM_PRICE.getCode()))
                        sumPrices.add(log);
                }
                sort(basePrices);
                sort(sumPrices);
                if(basePrices.size()>5) basePrices = basePrices.subList(0,5);
                if(sumPrices.size()>5) sumPrices = sumPrices.subList(0,5);
                model.addAttribute("basePrices",basePrices);
                model.addAttribute("sumPrices",sumPrices);
            }else {
                model.addAttribute("resultInfo",JSON.toJSONString(result.getMsg()));
            }

        }
        return "supplier/pop_table/quota_item_pop_price_log";
    }

    private void sort(List<QuotationPriceLog> basePrices) {
        Collections.sort(basePrices, new Comparator<QuotationPriceLog>() {
            @Override
            public int compare(QuotationPriceLog o1, QuotationPriceLog o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
    }

    /**
     * 粘贴输入
     * 
     * @param request
     * @return
     */
    @RequestMapping("quotation/pasteItemInForm")
    public String pasteItemInForm(Model model,Long supplierId,String supplierType) {
    	model.addAttribute("supplierId", supplierId);
    	model.addAttribute("supplierType", supplierType);
        return "supplier/pop_table/paste_pop";
    }

    /**
     * 获取粘贴输入的数据
     * 
     * @param request
     * @return
     */
    @RequestMapping("quotation/pasteInfo")
    @ResponseBody
    public List<SkuInfoResult> pasteInfo( ModelMap map,Long supplierId,String pasetinfos) {
        List<SkuInfoResult> skuInfoList = supplierInfoProxy.getSkuInfoWithPasteInfo(pasetinfos, supplierId);
        return skuInfoList;
    }

    @RequestMapping("quotationSave")
    @ResponseBody
    public ResultInfo<QuotationInfo> saveQuotationInfo(ModelAndView mav,HttpServletRequest request) {
    	//验证
    	Map<String, Object> retMap = quotationAO.genQuotationInfo(request, true);
    	if (checkResult(retMap)) {
    		QuotationInfo quotationInfo = (QuotationInfo) retMap.get(SupplierConstant.DATA_KEY);
    		quotationInfo.setCreateUser(super.getUserName());
    		quotationInfo.setUpdateUser(super.getUserName());
    		return quotationInfoProxy.saveQuotationInfo(quotationInfo);
         }
    	return new ResultInfo<QuotationInfo>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }

    /**
     * <pre>
     * 报价单提交
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("quotationSub")
    @ResponseBody
    public ResultInfo<QuotationInfo> quotationSub(HttpServletRequest request,Long quoId,String status ) {
        Integer setStatus = null;
        if ("submit".equals(status)) {
            setStatus = OrderAuditStatus.EXAMING.getStatus();
        } else if ("cancel".equals(status)) {
            setStatus = OrderAuditStatus.CANCELED.getStatus();
        } else if ("save".equals(status)) {
            setStatus = OrderAuditStatus.EDITING.getStatus();
        } else {
            setStatus = OrderAuditStatus.EXAMING.getStatus();
        }
        //验证
        ResultInfo<QuotationInfo> resultInfo = null;
        Map<String, Object> retMap = quotationAO.genQuotationInfo(request, true);
        if (checkResult(retMap)) {
        	QuotationInfo quotationInfo = (QuotationInfo) retMap.get(SupplierConstant.DATA_KEY);
        	 if (null == quoId) {
             	quotationInfo.setAuditStatus(setStatus);
             	quotationInfo.setCreateUser(super.getUserName());
             	quotationInfo.setUpdateUser(super.getUserName());
             	resultInfo = quotationInfoProxy.saveQuotationInfo(quotationInfo);
                 if (resultInfo.success) {
                     quoId = resultInfo.getData().getId();
                 }
             } else {
             	quotationInfo.setId(quoId);
             	quotationInfo.setUpdateUser(super.getUserName());
             	resultInfo = quotationInfoProxy.updateQuotationInfo(quotationInfo, setStatus);
             }
             if (resultInfo.success && !"save".equals(status)) {
             	quotationInfo.setId(quoId);
                 auditRecordsProxy.saveAuditRecords(quotationInfo, setStatus, "", billType,super.getUserId(),super.getUserName());
             }
             return resultInfo;
        }
        return new ResultInfo<QuotationInfo>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }

    /**
     * <pre>
     * 报价单展示
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("quotationShow")
    public String quotationShow(Model model,Long quoId) {
        QuotationInfo quotationVO = quotationInfoProxy.queryById(quoId).getData();
        if (null == quotationVO) {
            return "supplier/quotation/quotation_show";
        }
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setQuotationId(quoId);
        quotationProduct.setStatus(SupplierConstant.STATUS_ENABLE);
        List<QuotationProduct> quotationProducts = quotationProductProxy.queryByObject(quotationProduct).getData();
        quotationVO.setQuotationProductList(quotationProducts);
        
        setDetailInfo(model,quotationVO);
        AuditRecords doCondition = new AuditRecords();
        doCondition.setStatus(Constant.ENABLED.YES);
        doCondition.setAuditId(quoId);
        doCondition.setBillType(BillType.PRICE.getValue());
        List<AuditRecords> auditRecords = auditRecordsProxy.queryByObject(doCondition).getData();
        model.addAttribute("auditRecords", auditRecords);
        return "supplier/quotation/quotation_show";
    }

    /**
     * <pre>
     * 报价单编辑页面
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("quotationEdit")
    public String quotationEdti(Model model,Long quoId) {
        QuotationInfo quotationVO = quotationInfoProxy.queryById(quoId).getData();
        if (null == quotationVO) {
            return "supplier/quotation/quotation_edit";
        }
        SupplierInfo supplierInfo = supplierInfoProxy.queryById(quotationVO.getSupplierId()).getData();
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setQuotationId(quoId);
        quotationProduct.setStatus(SupplierConstant.STATUS_ENABLE);
        List<QuotationProduct> quotationProducts = quotationProductProxy.queryByObject(quotationProduct).getData();
        quotationVO.setQuotationProductList(quotationProducts);
        
        quotationVO.setSupplierType(supplierInfo.getSupplierType());
        setDetailInfo(model, quotationVO);
        return "supplier/quotation/quotation_edit";
    }

    /**
     * <pre>
     * 设置详细信息
     * </pre>
     *
     * @param mav
     * @return
     */
    private void setDetailInfo(Model model, QuotationInfo quotationVO) {
    	model.addAttribute("quotationVO", quotationVO);
		model.addAttribute("statusShow", SupplierConstant.O_AUDIT_STATUS_MAP_ALL.get(quotationVO.getAuditStatus()));
    	model.addAttribute("supplierTypes", SupplierConstant.SUPPLIER_TYPES);
    }

    /**
     * <pre>
     * 报价单编辑之后的保存
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("quotationEditSave")
    @ResponseBody
    public ResultInfo<QuotationInfo> quotationEditSave(Model model,HttpServletRequest request,Long quoId) {
    	//验证
    	 Map<String, Object> retMap = quotationAO.genQuotationInfo(request, true);
         if (checkResult(retMap)) {
        	 QuotationInfo quotationInfo = (QuotationInfo) retMap.get(SupplierConstant.DATA_KEY);
             quotationInfo.setId(quoId);
             return quotationInfoProxy.updateQuotationInfo(quotationInfo, AuditStatus.EDITING.getStatus());
         }
         return new ResultInfo<QuotationInfo>(new FailInfo(retMap.get(SupplierConstant.MESSAGE_KEY).toString()));
    }

    /**
     * <pre>
     * 报价单下载
     * </pre>
     *
     * @param quoId
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping("quotation/downLoadPdf")
    public void downLoadPdf(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "quoId", required = true) Long quoId) throws IOException {
        QuotationInfo quotation = quotationInfoProxy.queryById(quoId).getData();
        SupplierInfo supplierInfo = supplierInfoProxy.queryById(quotation.getSupplierId()).getData();
        Contract contract = contractProxy.getContractById(quotation.getContractId());
        String fileName = null;
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        //quotationInfoProxy.writeToPdf(response, quotation, supplierInfo, contract);
    }

    /**
     * <pre>
     * 报价单预览
     * </pre>
     *
     * @param quoId
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping("quotation/previewPdf")
    public void previewPdf(Model model, HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "quoId", required = true) Long quoId) throws IOException {
        QuotationInfo quotation = quotationInfoProxy.queryById(quoId).getData();
        SupplierInfo supplier = supplierInfoProxy.queryById(quotation.getSupplierId()).getData();
        Contract contract = contractProxy.getContractById(quotation.getContractId());
        //quotationInfoProxy.writeToPdf(response, quotation, supplier, contract);
    }
    
    
	/**
	 * 导出报价单
	 * 
	 * @param qo
	 * @param response
	 */
	@RequestMapping(value = "quotationExport")
	public void exportQuotation(QuotationInfo quotationInfo, HttpServletResponse response) {
		response.setHeader("Content-disposition", "attachment; filename=quotation-list.xlsx");
        response.setContentType("application/x-download");
		try {
			List<ExcelQuotationExportDTO> dataList = getExcelQuotation(quotationInfo);
			String templatePath = "/WEB-INF/classes/template/quotation-list.xlsx";
			String fileName = "quotation_list_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list",dataList);
			super.exportXLS(map, templatePath, fileName,response);
		} catch (Exception e) {
			LOGGER.error("报价单导出异常", e);
		}
	}
    
    private List<ExcelQuotationExportDTO> getExcelQuotation( QuotationInfo quotationInfo) {    	
    	Map<String,Object> params = BeanUtil.beanMap(quotationInfo);
    	params.remove("startDate");
    	params.remove("endDate");
    	params.remove("quotationProductList");
    	if(quotationInfo.getStartDate()!=null){
    		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " start_date >='"+DateUtil.formatDate(quotationInfo.getStartDate())+"'");
    	}
    	if(quotationInfo.getEndDate()!=null){
    		params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " end_date <='"+DateUtil.formatDate(quotationInfo.getEndDate())+"'");
    	}
    	params.put("status", Constant.ENABLED.YES);
        ResultInfo<List<QuotationInfo> > resultInfo = quotationInfoProxy.queryByParamNotEmpty(params);
        if( resultInfo.isSuccess() ){
        	List<ExcelQuotationExportDTO> quolist = new ArrayList<ExcelQuotationExportDTO>();
            for(QuotationInfo quo : resultInfo.getData() ){            	
            	Map<String,Object> prdQuery = new HashMap<String, Object>();
            	prdQuery.put("quotation_id", quo.getId());            	
            	prdQuery.put("status", Constant.ENABLED.YES);
            	ResultInfo<List<QuotationProduct> > resPrd = quotationProductProxy.queryByParamNotEmpty(prdQuery);
            	if(resPrd.isSuccess() ){
            		for(QuotationProduct prd : resPrd.getData() ){
            			ExcelQuotationExportDTO  excQuo = new ExcelQuotationExportDTO();
            			excQuo.setQuotationCode( quo.getId().toString() );
            			excQuo.setQuotationName( quo.getQuotationName());
            			excQuo.setSupplierName( quo.getSupplierName());
            			excQuo.setContractCode( quo.getContractCode());
            			excQuo.setContractType(SupplierType.getNameByValue( quo.getContractType()));
            			excQuo.setStartDate( new SimpleDateFormat("yyyy-MM-dd").format(quo.getStartDate()) );
            			excQuo.setEndDate( new SimpleDateFormat("yyyy-MM-dd").format( quo.getEndDate()));
            			//////////////////////////////////////////
            			excQuo.setProductCode( prd.getProductCode());
            			excQuo.setProductName( prd.getProductName());
            			excQuo.setBrandName( prd.getBrandName());
            			excQuo.setBigName( prd.getBigName());
            			excQuo.setMidName( prd.getMidName());
            			excQuo.setSmallName( prd.getSmallName());
            			excQuo.setSku( prd.getSku());
            			excQuo.setBarCode( prd.getBarCode());
            			excQuo.setProductUnit( prd.getProductUnit());
            			excQuo.setStandardPrice( prd.getStandardPrice());
            			excQuo.setSalePrice( prd.getSalePrice());
            			excQuo.setSupplyPrice( prd.getSupplyPrice());
            			excQuo.setCommissionPercent( prd.getCommissionPercent());
            			excQuo.setBoxProp( prd.getBoxProp());
            			excQuo.setProductProp( prd.getProductProp());
                        excQuo.setBasePrice(prd.getBasePrice());
                        excQuo.setFreight(prd.getFreight());
                        excQuo.setMulTaxRate(prd.getMulTaxRate());
                        excQuo.setTarrifTaxRate(prd.getTarrifTaxRate());
                        excQuo.setSumPrice(prd.getSumPrice());
            			quolist.add(excQuo);
            		}            		
            	}            	
            }        	
            return quolist;
        }            	
        return null;
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
