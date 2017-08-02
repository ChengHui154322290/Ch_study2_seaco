package com.tp.backend.controller.supplier.ao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.sup.SkuInfoVO;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.ISpecService;
import com.tp.service.prd.IItemService;
import com.tp.service.sup.IQuotationProductService;
import com.tp.service.sup.ISupplierInfoService;

/**
 * 供应商处理商品ao 由于商品组队商品信息的组装不太好 所有有了这个类的存在
 *
 * @author yfxie
 */
@Service
public class SupplierItemAO extends SupplierBaseAO {

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
	public Map<String, SkuInfoVO> getSkuInfoBySkus(List<String> skuCodes) {
        final Map<String, SkuInfoVO> skuInfoMap = new HashMap<String, SkuInfoVO>();
        final List<SkuInfoVO> skuInfoList = getSkuInfoList(skuCodes);
        for (int i = 0; i < skuInfoList.size(); i++) {
            final SkuInfoVO skuInfo = skuInfoList.get(i);
            skuInfoMap.put(skuInfo.getSku(), skuInfo);
        }
        return skuInfoMap;
    }
	
    private List<SkuInfoVO> getSkuInfoList(final List<String> skuCodes) {
        final List<SkuInfoVO> skuInfoList = new ArrayList<SkuInfoVO>();
        if (null != skuCodes && skuCodes.size() > 0) {
            final List<ItemResultDto> itemResult = itemService.getSkuList(skuCodes);
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
	private void setSkuDetail(final List<SkuInfoVO> skuInfoList,
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
		        final SkuInfoVO skuInfo = new SkuInfoVO();
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
    private void setSkuListDetailInfo(final List<SkuInfoVO> skuInfoList, final List<Long> brandIds,
        final List<Long> bigIds, final List<Long> midIds, final List<Long> smallIds) {
        final Map<Long, Category> bigCategoryMap = getCategoryMap(bigIds);
        final Map<Long, Category> midCategoryMap = getCategoryMap(midIds);
        final Map<Long, Category> smallCategoryMap = getCategoryMap(smallIds);
        final Map<Long, Brand> brandMap = getBrandMap(brandIds);
        for (final SkuInfoVO skuInfo : skuInfoList) {
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
}
