package com.tp.proxy.sup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.sup.SkuInfoVO;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.Spec;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.sup.QuotationProduct;
import com.tp.model.sup.SupplierInfo;
import com.tp.query.prd.SkuInfoQuery;
import com.tp.result.prd.SkuInfoResult;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.ISpecService;
import com.tp.service.prd.IItemService;
import com.tp.service.sup.IQuotationProductService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.StringUtil;

/**
 * 供应商处理商品ao 由于商品组队商品信息的组装不太好 所有有了这个类的存在
 *
 * @author szy
 */
@Service
public class SupplierItemProxy{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemService itemService;

    @Autowired
    private ISpecService specService;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private IDictionaryInfoService dictionaryInfoService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IQuotationProductService quotationProductService;

    @Autowired
    private ISupplierInfoService supplierInfoService;

    /**
     * <pre>
     * 设置商品基础
     * </pre>
     *
     * @param skuInfoResult
     */
    private void setItemBaseInfo(final SkuInfoResult skuInfoResult) {
        final Brand brand = brandService.queryById(skuInfoResult.getBrandId());
        if (null != brand) {
        	skuInfoResult.setBrandName(brand.getName());
        }
        final Long largeId = skuInfoResult.getLargeId();
        final Long midId = skuInfoResult.getMediumId();
        final Long smallId = skuInfoResult.getSmallId();
        final Category largeCate = categoryService.queryById(largeId);
        final Category midCate = categoryService.queryById(midId);
        final Category smallCate = categoryService.queryById(smallId);
        if (null != largeCate) {
            skuInfoResult.setBigCatId(largeCate.getId());
            skuInfoResult.setBigCatName(largeCate.getName());
        }
        if (null != midCate) {
            skuInfoResult.setMidCatId(midCate.getId());
            skuInfoResult.setMidCatName(midCate.getName());
        }
        if (null != smallCate) {
            skuInfoResult.setSmallCatId(smallCate.getId());
            skuInfoResult.setSmallCatName(smallCate.getName());
        }
        final DictionaryInfo unitDO = dictionaryInfoService.queryById(skuInfoResult.getUnitId());
        if (null != unitDO) {
            skuInfoResult.setUnitName(unitDO.getName());
        }
        // 设置默认价格
        setDefaultPrice(skuInfoResult);
    }

    /**
     * 设置标准价(供应商最新的一次报价单里面的价格)
     *
     * @param skuInfoResult
     */
    private void setDefaultPrice(final SkuInfoResult skuInfoResult) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("sku", skuInfoResult.getSku());
        params.put("supplierId", skuInfoResult.getSpId());
        params.put("status", Constant.ENABLED.YES);
        params.put("auditStatus", AuditStatus.THROUGH.getStatus());
        final QuotationProduct quoResult = quotationProductService.queryUniqueByParams(params);
        if (null != quoResult) {
            skuInfoResult.setStarndardPrice(new BigDecimal(quoResult.getStandardPrice()));
            skuInfoResult.setSupplyPrice(new BigDecimal(quoResult.getSupplyPrice()));
        }
    }

    /**
     * <pre>
     *   查询必须为skuCode或者 barCode且supplierId
     * </pre>
     *
     * @param skuCode
     * @param barCode
     * @param supplierId
     * @return
     */
    public SkuInfoResult getSkuInfoByCode(final String skuCode, final String barCode, final Long supplierId) {
        final SkuInfoQuery queryInfo = new SkuInfoQuery();
        if(StringUtil.isNotBlank(skuCode)){
            queryInfo.setSku(skuCode);
        }
        queryInfo.setBarcode(barCode);
        queryInfo.setSupplierId(supplierId);
        if(null == supplierId){
        	logger.info("方法：getSkuInfoByCode查询supplierId为空，返回null。");
        	return null;
        }
        if (StringUtils.isEmpty(skuCode) && StringUtils.isEmpty(barCode)) {
            logger.info("方法：getSkuInfoByCode查询字段skuCode和barCode为空，返回null。");
            return null;
        }
        
        SupplierInfo supplierInfo = supplierInfoService.queryById(supplierId);
        if(null == supplierInfo){
        	logger.info("方法：getSkuInfoByCode查询的供应商id找不到。");
            return null;
        }
        if(SupplierType.isXg(supplierInfo.getSupplierType())){
        	//queryInfo.setSupplierId(ItemConstant.SUPPLIER_ID);
        	queryInfo.setSaleType(0);
        	return itemService.selectSkuInfo(queryInfo);
        } else {
        	queryInfo.setSaleType(1);
        	return itemService.selectSkuInfo(queryInfo);
        }
    }

    /**
     * 设置sku信息
     *
     * @param skuInfoResult
     * @param skuInfoQuery
     */
    private void setPropInfo(final SkuInfoResult skuInfoResult, final SkuInfoResult skuInfoQuery) {
        final List<ItemDetailSpec> detailSpecList = skuInfoQuery.getItemDetailSpecList();
        if (null != detailSpecList) {
            final int len = detailSpecList.size();
            if (len > 0) {
                final Long propValId = detailSpecList.get(0).getSpecId();
                final Spec specDO = specService.queryById(propValId);
                if (null != specDO) {
                    skuInfoResult.setPropValue1(specDO.getSpec());
                }
            }
            if (len > 1) {
                final Long propValId = detailSpecList.get(1).getSpecId();
                final Spec specDO = specService.queryById(propValId);
                if (null != specDO) {
                    skuInfoResult.setPropValue2(specDO.getSpec());
                }
            }
            if (len > 2) {
                final Long propValId = detailSpecList.get(2).getSpecId();
                final Spec specDO = specService.queryById(propValId);
                if (null != specDO) {
                    skuInfoResult.setPropValue2(specDO.getSpec());
                }
            }
        }
    }

    /**
     * <pre>
     * 根据barcode或者sku查询商品信息
     * </pre>
     *
     * @return
     */
    public SkuInfoResult getSkuInfoByBarCodeOrSku(final String skuCode, final String barCode, final Long supplierId) {
        SkuInfoResult skuInfoResult = null;
        final SkuInfoResult skuInfoQuery = getSkuInfoByCode(skuCode, barCode, supplierId);
        if (null != skuInfoQuery) {
            skuInfoResult = new SkuInfoResult();
            BeanUtils.copyProperties(skuInfoQuery, skuInfoResult);
            setItemBaseInfo(skuInfoResult);
            setPropInfo(skuInfoResult, skuInfoQuery);
        }
        return skuInfoResult;
    }

    /**
     * 获取商品信息
     *
     * @param skuCodes
     * @return
     */
    public Map<String, SkuInfoResult> getSkuInfoBySkus(final List<String> skuCodes) {
        final Map<String, SkuInfoResult> skuInfoMap = new HashMap<String, SkuInfoResult>();
        final List<SkuInfoResult> skuInfoList = getSkuInfoList(skuCodes);
        for (int i = 0; i < skuInfoList.size(); i++) {
            final SkuInfoResult skuInfo = skuInfoList.get(i);
            skuInfoMap.put(skuInfo.getSku(), skuInfo);
        }
        return skuInfoMap;
    }
    
    /**
     * 获取商品信息
     *
     * @param skuCodes
     * @return
     */
    public Map<String, SkuInfoResult> getSkuInfoBySkus(final List<String> skuCodes,Long suplierId) {
        final Map<String, SkuInfoResult> skuInfoMap = new HashMap<String, SkuInfoResult>();
        final List<SkuInfoResult> skuInfoList = getSkuInfoList(skuCodes,suplierId);
        for (int i = 0; i < skuInfoList.size(); i++) {
            final SkuInfoResult skuInfo = skuInfoList.get(i);
            skuInfoMap.put(skuInfo.getSku(), skuInfo);
        }
        return skuInfoMap;
    }

    /**
     * 获取商品信息BarcodeAsKey
     *
     * @param skuCodes
     * @return
     */
    public Map<String, SkuInfoResult> getSkuInfoBySkusBarcodeAsKey(final List<String> skuCodes,Long supplierId) {
        final Map<String, SkuInfoResult> skuInfoMap = new HashMap<String, SkuInfoResult>();
        final List<SkuInfoResult> skuInfoList = getSkuInfoList(skuCodes,supplierId);
        for (int i = 0; i < skuInfoList.size(); i++) {
            final SkuInfoResult skuInfo = skuInfoList.get(i);
            skuInfoMap.put(skuInfo.getBarcode(), skuInfo);
        }
        return skuInfoMap;
    }

    private List<SkuInfoResult> getSkuInfoList(final List<String> skuCodes) {
        final List<SkuInfoResult> skuInfoList = new ArrayList<SkuInfoResult>();
        if (null != skuCodes && skuCodes.size() > 0) {
            final List<ItemResultDto> itemResult = itemService.getSkuList(skuCodes);
            setSkuDetail(skuInfoList, itemResult);
        }
        return skuInfoList;
    }
    
    private List<SkuInfoResult> getSkuInfoList(final List<String> skuCodes,Long supplierId) {
        final List<SkuInfoResult> skuInfoList = new ArrayList<SkuInfoResult>();
        final SupplierInfo supplierInfo = supplierInfoService.queryById(supplierId);
        if (null == supplierInfo) {
            logger.info("Can not find supplier with supplierId : {}", supplierId);
            return skuInfoList;
        }
        if (null != skuCodes && skuCodes.size() > 0) {
        	int supplierTypeItem = 1;
            if (SupplierType.PURCHASE.getValue().equals(supplierInfo.getSupplierType())
                || SupplierType.SELL.getValue().equals(supplierInfo.getSupplierType())) {
                //supplierId = ItemConstant.SUPPLIER_ID;
                supplierTypeItem = 0;
            }
            final List<ItemResultDto> itemResult = itemService.getSkuListForSupplierWithSpIdAndSkuCodes(supplierId, supplierTypeItem, skuCodes);
            setSkuDetail(skuInfoList, itemResult);
        }
        return skuInfoList;
    }

    /**
     * 设置sku的详细信息
     * 
     * @param skuInfoList
     * @param itemResult
     */
	private void setSkuDetail(final List<SkuInfoResult> skuInfoList,
			final List<ItemResultDto> itemResult) {
		if (null != itemResult && itemResult.size() > 0) {
		    final List<Long> brandIds = new ArrayList<Long>();
		    final List<Long> bigIds = new ArrayList<Long>();
		    final List<Long> midIds = new ArrayList<Long>();
		    final List<Long> smallIds = new ArrayList<Long>();
		    for (final ItemResultDto item : itemResult) {
		        brandIds.add(item.getBrandId());
		        bigIds.add(item.getLargeId());
		        midIds.add(item.getMediumId());
		        smallIds.add(item.getSmallId());
		        final SkuInfoResult skuInfo = new SkuInfoResult();
		        BeanUtils.copyProperties(item, skuInfo);
		        skuInfo.setSkuName(item.getMainTitle());
		        skuInfoList.add(skuInfo);
		    }
		    // 设置详细信息
		    setSkuListDetailInfo(skuInfoList, brandIds, bigIds, midIds, smallIds);
		}
	}

    /**
     * 设置sku的详细信息
     *
     * @param skuInfoList
     * @param brandIds
     * @param bigIds
     * @param midIds
     * @param smallIds
     */
    private void setSkuListDetailInfo(final List<SkuInfoResult> skuInfoList, final List<Long> brandIds,
        final List<Long> bigIds, final List<Long> midIds, final List<Long> smallIds) {
        final Map<Long, Category> bigCategoryMap = getCategoryMap(bigIds);
        final Map<Long, Category> midCategoryMap = getCategoryMap(midIds);
        final Map<Long, Category> smallCategoryMap = getCategoryMap(smallIds);
        final Map<Long, Brand> brandMap = getBrandMap(brandIds);
        for (final SkuInfoResult skuInfo : skuInfoList) {
            final Category bigCategory = bigCategoryMap.get(skuInfo.getLargeId());
            final Category midCategory = midCategoryMap.get(skuInfo.getMediumId());
            final Category smallCategory = smallCategoryMap.get(skuInfo.getSmallId());
            final Brand barndDO = brandMap.get(skuInfo.getBrandId());
            if (null != bigCategory) {
                skuInfo.setBigCatId(bigCategory.getId());
                skuInfo.setBigCatName(bigCategory.getName());
            }
            if (null != midCategory) {
                skuInfo.setMidCatId(midCategory.getId());
                skuInfo.setMidCatName(midCategory.getName());
            }
            if (null != smallCategory) {
                skuInfo.setSmallCatId(smallCategory.getId());
                skuInfo.setSmallCatName(smallCategory.getName());
            }
            if (null != barndDO) {
                skuInfo.setBrandId(barndDO.getId());
                skuInfo.setBrandName(barndDO.getName());
            }
        }
    }

    /**
     * <pre>
     * 品牌map
     * </pre>
     *
     * @return
     */
    public Map<Long, Brand> getBrandMap(final List<Long> ids) {
        final Map<Long, Brand> brandMap = new HashMap<Long, Brand>();
        if (null != ids && ids.size() > 0) {
            // 此处品牌接口的状态没有提供常量定义或者枚举类 所以暂时状态写 1
            final List<Brand> brandList = brandService.selectListBrand(ids, 1);
            if (null != brandList && brandList.size() > 0) {
                for (final Brand brand : brandList) {
                    brandMap.put(brand.getId(), brand);
                }
            }
        }
        return brandMap;
    }

    /**
     * <pre>
     * 获取分类的map
     * </pre>
     *
     * @param idList
     * @return
     */
    public Map<Long, Category> getCategoryMap(final List<Long> idList) {
        final Map<Long, Category> categoryMap = new HashMap<Long, Category>();
        if (null != idList && idList.size() > 0) {
            final List<Category> cDos = categoryService.queryCategoryByParams(idList, 1);
            if (null != cDos && cDos.size() > 0) {
                for (final Category cDo : cDos) {
                    categoryMap.put(cDo.getId(), cDo);
                }
            }
        }
        return categoryMap;
    }

    /**
     * 根据条码获取sku信息
     * 
     * @param barcodes
     * @return
     */
    public Map<String, SkuInfoResult> getSkuInfoByBarcodes(final Long supplierId, final List<String> barcodes) {
        final Map<String, SkuInfoResult> skuInfoMap = new HashMap<String, SkuInfoResult>();
        final List<SkuInfoResult> skuInfoList = getSkuInfoList(supplierId, barcodes);
        for (int i = 0; i < skuInfoList.size(); i++) {
            final SkuInfoResult skuInfo = skuInfoList.get(i);
            skuInfoMap.put(skuInfo.getSku(), skuInfo);
        }
        return skuInfoMap;
    }

    /**
     * 根据条码获取sku信息BarcodeAsKey
     * 
     * @param barcodes
     * @return
     */
    public Map<String, SkuInfoResult> getSkuInfoByBarcodesBarcodeAsKey(final Long supplierId,
        final ArrayList<String> barcodes) {
        final Map<String, SkuInfoResult> skuInfoMap = new HashMap<String, SkuInfoResult>();
        final List<SkuInfoResult> skuInfoList = getSkuInfoList(supplierId, barcodes);
        for (int i = 0; i < skuInfoList.size(); i++) {
            final SkuInfoResult skuInfo = skuInfoList.get(i);
            skuInfoMap.put(skuInfo.getBarcode(), skuInfo);
        }
        return skuInfoMap;
    }

    private List<SkuInfoResult> getSkuInfoList(Long supplierId, final List<String> barcodes) {
        final SupplierInfo supplierInfo = supplierInfoService.queryById(supplierId);
        final List<SkuInfoResult> skuInfoList = new ArrayList<SkuInfoResult>();
        if (null == supplierInfo) {
            logger.info("Can not find supplier with supplierId : {}", supplierId);
            return skuInfoList;
        }
        if (null != barcodes && barcodes.size() > 0) {
        	int supplierTypeItem = 1;
            if (SupplierType.PURCHASE.getValue().equals(supplierInfo.getSupplierType())
                || SupplierType.SELL.getValue().equals(supplierInfo.getSupplierType())) {
                //supplierId = ItemConstant.SUPPLIER_ID;
                supplierTypeItem = 0;
            }
            logger.info("getSkuListForSupplierWithSpIdAndBarCodes input supplierId:{},barcodes:{}", supplierId,
                barcodes);
            final List<ItemResultDto> itemResult = itemService.getSkuListForSupplierWithSpIdAndBarCodes(supplierId, supplierTypeItem, barcodes);
            logger.info("getSkuListForSupplierWithSpIdAndBarCodes response itemResult:{}", itemResult);
            setSkuDetail(skuInfoList, itemResult);

        }
        return skuInfoList;
    }

    /**
     * 设置sku单位名称
     * 
     * @param unitIds
     * @param skuInfoRetList
     */
    public void setSkuUnitName(final List<Long> unitIds, final List<SkuInfoResult> skuInfoRetList) {
        if (null == skuInfoRetList) {
            return;
        }
        final Map<Long, String> unitMap = genUnitNameMap(unitIds);
        for (final SkuInfoResult skuInfo : skuInfoRetList) {
            skuInfo.setUnitName(unitMap.get(skuInfo.getUnitId()));
        }
    }

    /**
     * 生成单位id和单位名称的map
     * 
     * @return
     */
    private Map<Long, String> genUnitNameMap(final List<Long> ids) {
        final Map<Long, String> retMap = new HashMap<Long, String>();
        final List<DictionaryInfo> disctionInfoList = dictionaryInfoService.selectListByIds(ids);
        if (null != disctionInfoList && disctionInfoList.size() > 0) {
            for (final DictionaryInfo disctoryInfo : disctionInfoList) {
                retMap.put(disctoryInfo.getId(), disctoryInfo.getName());
            }
        }
        return retMap;
    }
    

}
