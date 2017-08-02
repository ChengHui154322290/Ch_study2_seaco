package com.tp.proxy.sup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.FreightType;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.TaxRate;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.SupplierAttach;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.usr.Department;
import com.tp.model.usr.UserDetail;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.result.prd.SkuInfoResult;
import com.tp.result.sup.SupplierDTO;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.ITaxRateService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.ISupplierAttachService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.service.usr.IDepartmentService;
import com.tp.service.usr.IUserDetailService;
import com.tp.service.usr.IUserInfoService;
import com.tp.util.StringUtil;
/**
 * 供应商主表代理层
 * @author szy
 *
 */
@Service
public class SupplierInfoProxy extends BaseProxy<SupplierInfo>{

	@Autowired
	private ISupplierInfoService supplierInfoService;
	@Autowired
	private ISupplierAttachService supplierAttachService;

	@Autowired
	private IFreightTemplateService freightTemplateService;

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private ITaxRateService taxRateService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IDepartmentService departmentService;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private IClearanceChannelsService clearanceChannelsService;

	@Autowired
	private IUserDetailService userDetailService;
	
	@Autowired
	private SupplierItemProxy supplierItemProxy;
	    
	@Override
	public IBaseService<SupplierInfo> getService() {
		return supplierInfoService;
	}
	
	
	public SupplierDTO queryInfoAllById(Long supplierId){
		return supplierInfoService.queryAllInfoBySupplierId(supplierId);
	}
	/**
	 * <pre>
	 * 获取所有运费模板
	 * </pre>
	 *
	 * @return
	 */
	public List<FreightTemplate> getFreightTemplate(final Long templateId,final String templateName) {
		List<FreightTemplate> freightTemplates = new ArrayList<FreightTemplate>();
		try {
			if (null != templateId) {
				final FreightTemplate templateDO = freightTemplateService.queryById(templateId);
				if (null != templateDO && templateDO.getFreightType() != null
						&& FreightType.EXTERNAL.ordinal() == templateDO.getFreightType()) {
					freightTemplates.add(templateDO);
					return freightTemplates;
				} else {
					return freightTemplates;
				}
			}
			Map<String,Object> params = new HashMap<String,Object>();
			if(StringUtils.isNotBlank(templateName)){
				params.put("name", templateName);
			}
			params.put("freightType", FreightType.EXTERNAL.ordinal());
			freightTemplates = freightTemplateService.queryByParam(params);
		} catch (final Exception e) {
			logger.error("加载运费模板错误{}", e);
		}
		return freightTemplates;
	}

	/**
	 * <pre>
	 * 获取所有仓库信息
	 * </pre>
	 */
	public List<Warehouse> getStorages(final Long id, final String name) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		if(StringUtil.isNoneBlank(name)){
			params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like concat('%','"+name+"','%')");
		}
		// 此方法现先限定只能加载200个仓库
		return warehouseService.queryByParam(params);
	}

	/**
	 * <pre>
	 * 根据条件查询仓库信息
	 * </pre>
	 *
	 * @param supplierId
	 * @param warehouseId
	 * @param warehouseName
	 * @return
	 */
	public List<Warehouse> getStorageWithCondition(final Long supplierId,
			final Long warehouseId, final String warehouseName) {
		Warehouse warehouseDORes = null;
		final List<Warehouse> retList = new ArrayList<Warehouse>();
		if (null != warehouseId) {
			warehouseDORes = warehouseService.queryById(warehouseId);
			if (null != warehouseDORes) {
				retList.add(warehouseDORes);
			}
			return retList;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		if (null != supplierId) {
			final SupplierInfo supplierInfo = supplierInfoService.queryById(supplierId);
			if (null != supplierInfo
			   && (SupplierType.PURCHASE.getValue().equals(supplierInfo.getSupplierType()) 
				 || SupplierType.SELL.getValue().equals(supplierInfo.getSupplierType()))) {
				params.put("spId", 0);
			} else if (null != supplierInfo) {
				params.put("spId", supplierInfo.getId());
			}
		}
		if(StringUtils.isNotBlank(warehouseName)){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " name like concat ('%','"+warehouseName+"','%')");
		}
		return warehouseService.queryByParam(params);
	}
	/**
	 * <pre>
	 * 获取仓库do
	 * </pre>
	 *
	 * @param warehouseId
	 * @return
	 */
	public Warehouse getWarehouseById(final Long warehouseId) {
		return warehouseService.queryById(warehouseId);
	}

	/**
	 * <pre>
	 *
	 * </pre>
	 *
	 * @param taxRateType
	 * @return
	 */
	public List<TaxRate> getRatesByType(final TaxRateEnum taxRateType) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status",Constant.ENABLED.YES);
		if (null != taxRateType) {
			params.put("type",taxRateType.getType());
		}
		return taxRateService.queryByParam(params);
	}

	/**
	 * 校验仓库师傅属于该供应商
	 */
	public boolean checkWarehoueBelongsToSupplier(
			final Warehouse warehouseDO, final SupplierInfo supplierInfo) {
		if (null == warehouseDO || null == supplierInfo) {
			return false;	
		}
		if ((SupplierType.PURCHASE.getValue().equals(supplierInfo.getSupplierType()) 
			|| SupplierType.SELL.getValue().equals(supplierInfo.getSupplierType()))
		  && new Long("0").equals(warehouseDO.getSpId())) {
			return true;
		} else if (null != supplierInfo.getId() && supplierInfo.getId().equals(warehouseDO.getSpId())) {
			return true;
		}
		return false;
	}

	/**
	 * 根据分类获取资质证明
	 *
	 * @param cid
	 */
	public ResultInfo<List<DictionaryInfo>> getLicensByCategoryId(final Long cid) {
		try{
			return new ResultInfo<>(categoryService.selectCategoryCertsByCategoryId(cid));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,cid);
			return new ResultInfo<>(failInfo);
		}
	}

	/**
	 * 获取部门信息
	 *
	 * @return
	 */
	public List<Department> getDepartments() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		return departmentService.queryByParam(params);
	}

	/**
	 * 获取通关渠道信息
	 *
	 * @return
	 */
	public List<ClearanceChannels> getClearanceChannels() {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("status", Constant.ENABLED.YES);
		List<ClearanceChannels> clearanceChannels = clearanceChannelsService.queryByParam(param);
		return clearanceChannels;
	}

	/**
	 * 根据id获取用户和部门的关系
	 *
	 * @param departmentIds
	 * @return
	 */
	public Map<String, List<UserInfo>> getDepartmentUsersMap(final List<Long> departmentIds) {
		final Map<String, List<UserInfo>> retMap = new HashMap<String, List<UserInfo>>();
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "department_id in(" + StringUtil.join(departmentIds, Constant.SPLIT_SIGN.COMMA) + ")");
		List<UserInfo> userDoList = userInfoService.queryByParamNotEmpty(params);
		if (null == userDoList || userDoList.size() == 0) {
			return retMap;
		}
		List<UserInfo> userList = null;
		for (final UserInfo userDO : userDoList) {
			final Long departmentId = userDO.getDepartmentId();
			if (retMap.containsKey(departmentId + "")) {
				userList = retMap.get(departmentId + "");
			} else {
				userList = new ArrayList<UserInfo>();
			}
			userList.add(userDO);
			retMap.put(departmentId + "", userList);
		}
		return retMap;
	}

	/**
	 * 根据部门id获取用户列表
	 *
	 * @param bmId
	 * @return
	 */
	public List<Map<String, Object>> getUsersByDepartmentId(final Long bmId) {
		final List<Map<String, Object>> userMapList = new ArrayList<Map<String, Object>>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("departId", bmId);
		params.put("disabled", Constant.DISABLED.NO);
		final List<UserInfo> userList = userInfoService.queryByParam(params);
		Map<String, Object> userMap = null;
		if (null != userList && userList.size() > 0) {
			for (final UserInfo user : userList) {
				userMap = new HashMap<String, Object>();
				userMap.put("id", user.getId());
				userMap.put("name", user.getUserName());
				userMapList.add(userMap);
			}
		}
		return userMapList;
	}

	/**
	 * 根据用户id取详细信息
	 * 
	 * @param userId
	 * @return
	 */
	public ResultInfo<UserDetail> getDetailByUserId(Long userId) {
		try{
			return new ResultInfo<>(userDetailService.findByUserId(userId));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,userId);
			return new ResultInfo<>(failInfo);
		}
	}


	public List<SkuInfoResult> getSkuInfoWithPasteInfo(String pasetinfos,
			Long supplierId) {
        final SupplierInfo supplierInfo = supplierInfoService.queryById(supplierId);
        final List<SkuInfoResult> skuInfoRetList = new ArrayList<SkuInfoResult>();
        final List<String[]> skuInfoFromPage = analyzePageInfo(pasetinfos, supplierInfo.getSupplierType());
        final Map<Integer, SkuInfoResult> skuInfoMap = new HashMap<Integer, SkuInfoResult>();
        final Map<String, Integer> skuCodeIndexMap = new HashMap<String, Integer>();
        final Map<String, Integer> barCodeIndexMap = new HashMap<String, Integer>();
        // 封装sku
        generateIndexSKuMap(skuInfoFromPage, skuInfoMap, skuCodeIndexMap, barCodeIndexMap);
        final Map<String, SkuInfoResult> skuInfoMapRes = supplierItemProxy.getSkuInfoBySkus(new ArrayList<String>(
            skuCodeIndexMap.keySet()), supplierId);
        final Map<String, SkuInfoResult> skuInfoMapResBarcodeAsKey = supplierItemProxy.getSkuInfoBySkusBarcodeAsKey(
            new ArrayList<String>(skuCodeIndexMap.keySet()), supplierId);
        final Map<String, SkuInfoResult> skuInfoMapBarRes = supplierItemProxy.getSkuInfoByBarcodes(supplierId,
            new ArrayList<String>(barCodeIndexMap.keySet()));
        final Map<String, SkuInfoResult> skuInfoMapBarResBarcodeAsKey = supplierItemProxy.getSkuInfoByBarcodesBarcodeAsKey(
            supplierId, new ArrayList<String>(barCodeIndexMap.keySet()));
        skuInfoMapBarRes.putAll(skuInfoMapRes);
        skuInfoMapBarResBarcodeAsKey.putAll(skuInfoMapResBarcodeAsKey);
        final List<Long> unitIds = new ArrayList<Long>();
        for (final Map.Entry<String, SkuInfoResult> skuInfoTable : skuInfoMapBarRes.entrySet()) {
            final String skuCode = skuInfoTable.getKey();
            final SkuInfoResult retSkuVO = skuInfoTable.getValue();
            if (null == retSkuVO) {
                continue;
            }
            final String barCode = retSkuVO.getBarcode();
            if (skuCodeIndexMap.containsKey(skuCode)) {
                final Integer index = skuCodeIndexMap.get(skuCode);
                retSkuVO.setStarndardPrice(skuInfoMap.get(index).getStarndardPrice());
                retSkuVO.setSupplyPrice(skuInfoMap.get(index).getSupplyPrice());
                retSkuVO.setMarketPrice(skuInfoMap.get(index).getMarketPrice());
                retSkuVO.setCommissionPer(skuInfoMap.get(index).getCommissionPer());
                unitIds.add(retSkuVO.getUnitId());
                retSkuVO.setPasteSuccessOrFail(1);
                skuInfoRetList.add(retSkuVO);
            } else if (barCodeIndexMap.containsKey(barCode)) {
                final Integer index = barCodeIndexMap.get(barCode);
                retSkuVO.setStarndardPrice(skuInfoMap.get(index).getStarndardPrice());
                retSkuVO.setSupplyPrice(skuInfoMap.get(index).getSupplyPrice());
                retSkuVO.setMarketPrice(skuInfoMap.get(index).getMarketPrice());
                retSkuVO.setCommissionPer(skuInfoMap.get(index).getCommissionPer());
                unitIds.add(retSkuVO.getUnitId());
                retSkuVO.setPasteSuccessOrFail(1);
                skuInfoRetList.add(retSkuVO);
            }
        }
        supplierItemProxy.setSkuUnitName(unitIds, skuInfoRetList);

        for (final Map.Entry<String, Integer> skuCode : skuCodeIndexMap.entrySet()) {
            if (skuInfoMapBarRes.containsKey(skuCode.getKey())) {
                continue;
            } else {
                final SkuInfoResult retSkuVO = new SkuInfoResult();
                retSkuVO.setSku(skuCode.getKey());
                retSkuVO.setPasteSuccessOrFail(0);
                retSkuVO.setPasteIndex(skuCode.getValue() + 1);
                skuInfoRetList.add(retSkuVO);
            }
        }
        for (final Map.Entry<String, Integer> barCode : barCodeIndexMap.entrySet()) {
            if (skuInfoMapBarResBarcodeAsKey.containsKey(barCode.getKey())) {
                continue;
            } else {
                final SkuInfoResult retSkuVO = new SkuInfoResult();
                retSkuVO.setBarcode(barCode.getKey());
                retSkuVO.setPasteSuccessOrFail(0);
                retSkuVO.setPasteIndex(barCode.getValue() + 1);
                skuInfoRetList.add(retSkuVO);
            }
        }
        return skuInfoRetList;
    }
	
	/**
     * 解析页面信息
     * 
     * @param pageInfo
     * @return
     */
    private List<String[]> analyzePageInfo(String pageInfo, final String supplierType) {
        final List<String[]> retList = new ArrayList<String[]>();
        if (null == pageInfo) {
            return retList;
        }
        pageInfo = pageInfo.replaceAll("(\\n){2,}", "\n");
        if (pageInfo.length() > 1 && "\n".equals(pageInfo.substring(pageInfo.length() - 1, pageInfo.length()))) {
            pageInfo = pageInfo.substring(0, pageInfo.length() - 1);
        }
        final String[] lineArr = pageInfo.split("\n");
        for (int i = 0; i < lineArr.length; i++) {
            final String[] oneLine = (lineArr[i] + " ").split("\t");
            final String[] dest = new String[6];
            if (oneLine.length == 5 && SupplierType.PURCHASE.getValue().equals(supplierType)) {
                System.arraycopy(oneLine, 0, dest, 0, 5);
                dest[5] = "";
                retList.add(dest);
            } else if (oneLine.length == 6) {
                retList.add(oneLine);
            }
        }
        return retList;
    }
    
    /**
     * 分装索引和sku的map
     * 
     * @param skuInfoFromPage
     * @param skuInfoMap
     * @param barCodeIndexMap
     * @param skuCodeIndexMap
     */
    private void generateIndexSKuMap(final List<String[]> skuInfoFromPage, final Map<Integer, SkuInfoResult> skuInfoMap,
        final Map<String, Integer> skuCodeIndexMap, final Map<String, Integer> barCodeIndexMap) {
        for (int i = 0; i < skuInfoFromPage.size(); i++) {

            final String[] oneLine = skuInfoFromPage.get(i);
            final String barcodeStr = oneLine[0].trim();
            final String skuStr = oneLine[1].trim();
            final String marketPriceStr = oneLine[2].trim();
            final String salesPriceStr = oneLine[3].trim();
            final String supplierPriceStr = oneLine[4].trim();
            final String commonssionStr = oneLine[5].trim();
            final BigDecimal marketPrice = CommonUtil.getBigDecimalVal(marketPriceStr);
            final BigDecimal salesPrice = CommonUtil.getBigDecimalVal(salesPriceStr);
            final BigDecimal supplierPrice = CommonUtil.getBigDecimalVal(supplierPriceStr);
            final BigDecimal commonssion = CommonUtil.getBigDecimalVal(commonssionStr);
            if (!StringUtil.isBlank(skuStr)) {
                skuCodeIndexMap.put(skuStr, new Integer(i));
            } else if (!StringUtil.isBlank(barcodeStr)) {
                barCodeIndexMap.put(barcodeStr, new Integer(i));
            } else {
                continue;
            }
            final SkuInfoResult skuInfo = new SkuInfoResult();
            skuInfo.setBarcode(barcodeStr);
            skuInfo.setSku(skuStr);
            skuInfo.setMarketPrice(marketPrice);
            skuInfo.setStarndardPrice(salesPrice);
            skuInfo.setSupplyPrice(supplierPrice);
            skuInfo.setCommissionPer(commonssion);
            skuInfoMap.put(new Integer(i), skuInfo);
        }
    }


	public ResultInfo<Long> saveSupplierBaseInfo(SupplierDTO supplierDTO) {
		try{
			return supplierInfoService.saveSupplierBaseInfo(supplierDTO);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,supplierDTO);
			return new ResultInfo<>(failInfo);
		}
	}


	public ResultInfo<Boolean> saveSupplierLicenInfo(SupplierAttach supplierAttach) {
		try{
			return supplierInfoService.saveSupplierLicenInfo(supplierAttach,supplierAttach.getSupplierId());
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,supplierAttach);
			return new ResultInfo<>(failInfo);
		}
	}


	public ResultInfo<Boolean> auditSupplier(SupplierInfo supplierInfo,	Integer auditStatus,String auditContent,Long userId,String userName) {
        final AuditRecords record = new AuditRecords();
        record.setAuditId(supplierInfo.getId());
        record.setAuditStatus(auditStatus);
        record.setUserId(userId);
        record.setUserName(userName);
        record.setContent(auditContent);
        record.setBillType(BillType.SPLIST.getValue());
        record.setStatus(Constant.ENABLED.YES);
        record.setOperate(SupplierConstant.AUDIT_RESULT.get(auditStatus));
        record.setCreateTime(new Date());
        record.setCreateUser(userName);
        record.setUpdateUser(userName);
		try{
			return supplierInfoService.auditSupplier(supplierInfo,auditStatus,record);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,supplierInfo);
			return new ResultInfo<>(failInfo);
		}
	}

	/**
     * <pre>
     * 更新供应商基本信息和附件信息
     * </pre>
     *
     * @param supplierDTO
     * @param supplierAttachDTO
     * @return
     */
    public ResultInfo<Boolean> updateSupplierInfo(final SupplierDTO supplierDTO, final SupplierAttach supplierAttach) {
        final SupplierAttach spDO = new SupplierAttach();
        spDO.setSupplierId(supplierDTO.getId());
        spDO.setStatus(Constant.ENABLED.YES);
        try {
        	ResultInfo<Boolean> ret = supplierInfoService.updateSupplierInfo(supplierDTO);
        	if ( !ret.success) {
            	FailInfo failInfo = ExceptionUtils.print( ret.getMsg() , logger );
    			return new ResultInfo<>(failInfo);            	
			}
            supplierAttachService.updateAttachInfoBySupplierId(supplierAttach);
        } catch (final Exception exception) {
        	FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,supplierDTO,supplierAttach);
			return new ResultInfo<>(failInfo);
        }
        return new ResultInfo<>(Boolean.TRUE);
    }
}
