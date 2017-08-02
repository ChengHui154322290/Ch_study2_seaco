package com.tp.proxy.sup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.model.bse.Category;
import com.tp.model.sup.SupplierCategory;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierCategoryService;
/**
 * 供应商-品类表代理层
 * @author szy
 *
 */
@Service
public class SupplierCategoryProxy extends BaseProxy<SupplierCategory>{

	@Autowired
	private ISupplierCategoryService supplierCategoryService;

	@Override
	public IBaseService<SupplierCategory> getService() {
		return supplierCategoryService;
	}

	public List<SupplierCategory> getSupplierCategoryInfo(Long supplierId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("supplierId", supplierId);
		params.put("status", Constant.ENABLED.YES);
		return supplierCategoryService.queryByParamNotEmpty(params);
	}

	 /**
     * 获取供应商分类信息
     *
     * @param spId
     * @param cid
     * @param cType
     * @return
     */
    public List<Category> getSupplierCategory(final Long spId, final Long brandId, final Long cid, final String cType) {
        final SupplierCategory supplierCategory = new SupplierCategory();
        Map<String,Object> params = new HashMap<String,Object>();
        final List<Category> retList = new ArrayList<Category>();
        supplierCategory.setSupplierId(spId);
        supplierCategory.setStatus(Constant.ENABLED.YES);
        params.put("supplierId", spId);
        params.put("status", Constant.ENABLED.YES);
        if ("0".equals(cType)) {
            supplierCategory.setBrandId(cid);
            params.put("brandId", cid);
        } else if ("1".equals(cType)) {
            supplierCategory.setBrandId(brandId);
            supplierCategory.setCategoryBigId(cid);
            params.put("brandId", brandId);
            params.put("categoryBigId", cid);
        } else {
            supplierCategory.setBrandId(brandId);
            supplierCategory.setCategoryMidId(cid);
            params.put("brandId", brandId);
            params.put("categoryMidId", cid);
        }
        final List<SupplierCategory> supplierCategoryList = supplierCategoryService.queryByParam(params);
        if (null != supplierCategoryList && supplierCategoryList.size() > 0) {
            if ("0".equals(cType)) {
                final Set<Long> cSet = new HashSet<Long>();
                for (final SupplierCategory spCategory : supplierCategoryList) {
                    if (null != spCategory.getCategoryBigId() && !cSet.contains(spCategory.getCategoryBigId())) {
                        final Category categoryDO = new Category();
                        categoryDO.setId(spCategory.getCategoryBigId());
                        categoryDO.setName(spCategory.getCategoryBigName());
                        retList.add(categoryDO);
                    }
                    cSet.add(spCategory.getCategoryBigId());
                }
            } else if ("1".equals(cType)) {
                final Set<Long> cSet = new HashSet<Long>();
                for (final SupplierCategory spCategory : supplierCategoryList) {
                    if (null != spCategory.getCategoryMidId() && !cSet.contains(spCategory.getCategoryMidId())) {
                        final Category categoryDO = new Category();
                        categoryDO.setId(spCategory.getCategoryMidId());
                        categoryDO.setName(spCategory.getCategoryMidName());
                        retList.add(categoryDO);
                    }
                    cSet.add(spCategory.getCategoryMidId());
                }
            } else if ("2".equals(cType)) {
                final Set<Long> cSet = new HashSet<Long>();
                for (final SupplierCategory spCategory : supplierCategoryList) {
                    if (null != spCategory.getCategorySmallId() && !cSet.contains(spCategory.getCategorySmallId())) {
                        final Category categoryDO = new Category();
                        categoryDO.setId(spCategory.getCategorySmallId());
                        categoryDO.setName(spCategory.getCategorySmallName());
                        retList.add(categoryDO);
                    }
                    cSet.add(spCategory.getCategorySmallId());
                }
            }

        }
        return retList;
    }
}
