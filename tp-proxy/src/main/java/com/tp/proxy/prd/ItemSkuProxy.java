package com.tp.proxy.prd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.CategoryOpenDto;
import com.tp.dto.prd.ItemDetailOpenDto;
import com.tp.dto.prd.enums.AuditResultEnum;
import com.tp.dto.prd.enums.SellerItemBindLevelEnum;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.BaseProxy;
import com.tp.query.prd.SellerSkuQuery;
import com.tp.result.sup.SupplierResult;
import com.tp.service.IBaseService;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemInfoService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.prd.IOpenPlantFormItemDetailService;
import com.tp.service.sup.IPurchasingManagementService;
/**
 * SPU对应SKU维度信息代理层
 * @author szy
 *
 */
@Service
public class ItemSkuProxy extends BaseProxy<ItemSku>{

	@Autowired
	private IItemSkuService itemSkuService;
	@Autowired
	private IItemInfoService itemInfoService;
	@Autowired
	private IPurchasingManagementService purchasingManagementService;
	@Autowired
	private IOpenPlantFormItemDetailService openPlantFormItemDetailService;
	@Autowired
	private IFreightTemplateService freightTemplateService;
	@Autowired
	private IItemDetailService itemDetailService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	@Autowired
	private IBrandService brandService;
	@Override
	public IBaseService<ItemSku> getService() {
		return itemSkuService;
	}

	public  PageInfo<ItemSku> queryAllLikedofItemSkuByPage(SellerSkuQuery sellerSkuQuery,PageInfo<ItemSku> pageInfo)  {
		if(null==sellerSkuQuery){
			sellerSkuQuery=new SellerSkuQuery();	
		}
		if(StringUtils.isNotBlank(sellerSkuQuery.getDetailName())){
			sellerSkuQuery.setDetailName(sellerSkuQuery.getDetailName().trim());
		}
		if(StringUtils.isNotBlank(sellerSkuQuery.getUnitName())){
			sellerSkuQuery.setDetailName(sellerSkuQuery.getUnitName().trim());
		}
		if(StringUtils.isNotBlank(sellerSkuQuery.getSupplierName())){
			SupplierResult supplierResult = purchasingManagementService.fuzzyQueryAllSupplierByName(sellerSkuQuery.getSupplierName().trim(), 1, 1000);
			List<SupplierInfo> supplierList = supplierResult.getSupplierInfoList();
			if(CollectionUtils.isNotEmpty(supplierList)){
				List<Long> list =new ArrayList<Long>();
				for (SupplierInfo supplierDO : supplierList) {
					list.add(supplierDO.getId());
				}
				sellerSkuQuery.setSupplierIds(list);
			}
		}
		PageInfo<ItemSku> page = itemSkuService.queryAllLikedSkuByBySellerSkuQueryPage(sellerSkuQuery, pageInfo);
		if(CollectionUtils.isNotEmpty(page.getRows())){
			List<ItemSku> list = page.getRows();
			Set<Long> supplerIds =new HashSet<Long>();
			for (ItemSku sku : list) {
				Long spId = sku.getSpId();
				if(null==spId){
					continue;
				}
				supplerIds.add(sku.getSpId());
			}
			SupplierResult supplierResult = purchasingManagementService.getSuppliersByIds(new ArrayList<Long>(supplerIds));
			for (ItemSku sku : list) {
				List<SupplierInfo> supplierList = supplierResult.getSupplierInfoList();
                for (SupplierInfo supplier : supplierList) {
					if(sku.getSpId().equals(supplier.getId())){
						sku.setSpName(supplier.getName());
					}
				}
			}
		}
		return page;
	}
	public Object getSkuInfo (Long id){
		ItemSku skuDO = itemSkuService.queryById(id);
		if(null != skuDO){
			if(SellerItemBindLevelEnum.SKU.getCode().equals(skuDO.getBindLevel())){
				Long majorDetailId = skuDO.getDetailId();
				ItemDetailOpenDto itemDetailOpenDto= openPlantFormItemDetailService.getItemDetailOpenDtoByDetailId(majorDetailId);
				Long freightTemplateId = itemDetailOpenDto.getDetailOpenDto().getFreightTemplateId();
				String name = freightTemplateService.queryById(freightTemplateId).getName();
				itemDetailOpenDto.getDetailOpenDto().setFreightTemplateName(name);
				return itemDetailOpenDto;
				
			} else if(SellerItemBindLevelEnum.PRD.getCode().equals(skuDO.getBindLevel())){
				Long detailId = skuDO.getDetailId();
				ItemDetailOpenDto itemDetailSellerDto = itemDetailService.getItemDetailOpenDtoByDetailId(detailId);
			//	Long itemId = itemDetailSellerDto.getDetailSellerDto().getItemId();			
				String majorSpuCode = itemDetailSellerDto.getDetailOpenDto().getSpu();			
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("spu", majorSpuCode);
				List<ItemInfo> list = itemInfoService.queryByParam(params);
				if(CollectionUtils.isEmpty(list)){
					return null;
				}
				ItemInfo itemInfo=list.get(0);
				// itemInfo = itemInfoService.selectById(itemId);
				Long brandId = itemInfo.getBrandId();
				Brand brandDO = brandService.queryById(brandId);
				itemDetailSellerDto.getInfoOpenDto().setBrandName(brandDO.getName());
				String mainTitle = itemInfo.getMainTitle();
				itemDetailSellerDto.getInfoOpenDto().setSpuName(mainTitle);
				String remark = itemInfo.getRemark();
				itemDetailSellerDto.getInfoOpenDto().setRemark(remark);
				List<Long> cateIdList = new ArrayList<Long>();
				cateIdList.add(itemInfo.getLargeId());
				cateIdList.add(itemInfo.getMediumId());
				cateIdList.add(itemInfo.getSmallId()); 
				
				if(CollectionUtils.isNotEmpty(cateIdList)){
					List<Category> listOfCate = categoryService.selectByIdsAndStatus(cateIdList, 2);
					CategoryOpenDto categorySellerDto = new CategoryOpenDto();
					categorySellerDto.setBigCateName(listOfCate.get(0).getName());
					categorySellerDto.setMiddCateName(listOfCate.get(1).getName());
					categorySellerDto.setSmallCateName(listOfCate.get(2).getName());
					itemDetailSellerDto.getInfoOpenDto().setCategoryOpenDto(categorySellerDto);
				}
				Long unitId = itemInfo.getUnitId();
				DictionaryInfo idctionaryInfo = dictionaryInfoService.queryById(unitId);
				Long freightTemplateId = itemDetailSellerDto.getDetailOpenDto().getFreightTemplateId();
				String name = freightTemplateService.queryById(freightTemplateId).getName();
				itemDetailSellerDto.getDetailOpenDto().setFreightTemplateName(name);
				itemDetailSellerDto.getInfoOpenDto().setUnitName(idctionaryInfo.getName());				
				return itemDetailSellerDto;
				
			} else if(SellerItemBindLevelEnum.SPU.getCode().equals(skuDO.getBindLevel())){
				Long detailId = skuDO.getDetailId();
				ItemDetailOpenDto itemDetailSellerDto = itemDetailService.getItemDetailOpenDtoByDetailId(detailId);
				Long freightTemplateId = itemDetailSellerDto.getDetailOpenDto().getFreightTemplateId();
				FreightTemplate freightTemplate = freightTemplateService.queryById(freightTemplateId);
				if(null!=freightTemplate){
					itemDetailSellerDto.getDetailOpenDto().setFreightTemplateName(freightTemplate.getName());
				}
				return itemDetailSellerDto;
			}
		}
		logger.info("根据商家平台sku的id为查询到数据,异常");
		return null;
	}
	public Map<String, Integer> initRejectType() {
		// 税率类型类型
		Map<String, Integer> rejectTypes = new HashMap<String, Integer>();
		AuditResultEnum[] values = AuditResultEnum.values();
		for (AuditResultEnum sTax : values) {
			rejectTypes.put(sTax.getKey(), sTax.getValue());
		}
		return rejectTypes;
	}

	//根据sellerSkuId获取到供应商名称
	public String getSupplierName(Long sellerSkuId){
		if(null == sellerSkuId){
			return null;
		}
		ItemSku itemSku = super.queryById(sellerSkuId).getData();
		if(null == itemSku){
			return null;
		}
		Long spId = itemSku.getSpId();
	    List<Long> list =new ArrayList<Long>();
	    list.add(spId);
		SupplierResult supplierResult = purchasingManagementService.getSuppliersByIds(list);
		if(CollectionUtils.isNotEmpty(supplierResult.getSupplierInfoList())){
			return supplierResult.getSupplierInfoList().get(0).getName();
		}
		return null;
	}

	/**
	 * 根据折扣批量更新促销价格
	 * @param idList
	 * @param discount
	 * @param userName
	 * @return
	 */
	public ResultInfo<Integer> updateBatchPrice(List<String> skuList, Float discount, String userName) {
		try{
			return new ResultInfo<Integer>(itemSkuService.updateBatchPrice(skuList,discount,userName));
		}catch(Throwable e){
			FailInfo failInfo = ExceptionUtils.println(new FailInfo(e), logger, skuList,discount,userName);
			return new ResultInfo<>(failInfo);
		}
		
	}

	public ResultInfo<Integer> updateTopicPrice(ItemSku itemSku) {
		try{
			return new ResultInfo<Integer>(itemSkuService.updateTopicPrice(itemSku));
		}catch(Throwable e){
			FailInfo failInfo = ExceptionUtils.println(new FailInfo(e), logger, itemSku);
			return new ResultInfo<>(failInfo);
		}
	}

}
