package com.tp.proxy.prd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.TopicProcess;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.prd.ItemDetailForTransDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.SkuExportDto;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuSupplier;
import com.tp.model.sup.QuotationProduct;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.bse.DictionaryInfoProxy;
import com.tp.proxy.bse.DistrictInfoProxy;
import com.tp.query.prd.ItemQuery;
import com.tp.result.sup.SupplierResult;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.prd.IItemDescService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemInfoService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.service.sup.IQuotationProductService;
import com.tp.util.StringUtil;

/**
 * 
 * <pre>
 * 	商品列表导出
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Service
public class ItemExportProxy {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ItemExportProxy.class);
	
	private final static int pageSize = 100;
	private final static String LINE_STR = "\r\n";
	@Autowired
	private IItemManageService itemManageService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IBrandService brandService;
	@Autowired
	private DictionaryInfoProxy dictionaryInfoProxy;
	
	@Autowired
	private DistrictInfoProxy districtInfoProxy;
	
	@Autowired 
	private IPurchasingManagementService purchasingManagementService;
	
	@Autowired
	private IItemSkuService itemSkuSerivce;
	
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private ITopicItemService topicItemService;
	
	@Autowired
	private ITopicService topicService;
	
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	
	@Autowired
	private IQuotationProductService quotationProductService;
	
	@Autowired
	private IItemInfoService itemInfoService;

	@Autowired
	private IItemDescService itemDescService;
	
	@Autowired 
	private IItemPicturesService itemPicturesService;
	
	public List<SkuExportDto> exportSkuList(ItemQuery query){
		query.setPageSize(pageSize);
		initItemQuery(query);
		List<SkuExportDto>  listAll  = new ArrayList<SkuExportDto>();
		query.setStartPage(1);
		PageInfo<ItemResultDto> itemPage = itemSkuSerivce.searchItemByQuery(query);
		if (null == itemPage || CollectionUtils.isEmpty(itemPage.getRows())) {
			return listAll;
		}
		List<SkuExportDto> list = bindBaseData(itemPage.getRows());
		if(CollectionUtils.isEmpty(list)){
			return listAll;
		}
		listAll.addAll(list);
		Integer totalPage = itemPage.getTotal();
		int i = 2;
		do{
			query.setStartPage(i);
			itemPage = itemSkuSerivce.searchItemByQuery(query);
			if (null == itemPage || CollectionUtils.isEmpty(itemPage.getRows())) {
				return listAll;
			}
			List<SkuExportDto> list1 = bindBaseData(itemPage.getRows());
			if(CollectionUtils.isNotEmpty(list1)){
				listAll.addAll(list1);
			}				
			i++;
		}while(i <= totalPage);
		
		return listAll;
	}; 
	
	public PageInfo<ItemResultDto> searchItemByQuery(ItemQuery query) {
		if(query.getCreateEndTime()!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(query.getCreateEndTime());
			calendar.add(Calendar.DATE, 1);
			query.setCreateEndTime(calendar.getTime());
		}
		PageInfo<ItemResultDto> itemPage = itemSkuSerivce.searchItemByQuery(query);
		return itemPage;
	}
	
	/**
	 * 
	 * <pre>
	 * 	绑定base数据
	 * </pre>
	 *
	 * @param list
	 */
	private List<SkuExportDto> bindBaseData(List<ItemResultDto> list){
		List<SkuExportDto> skuExportList = new ArrayList<SkuExportDto>();
		List<Long> itemIds = new ArrayList<Long>();
		List<Long> itemDetailIds = new ArrayList<Long>();
		List<Long> skuIds = new ArrayList<Long>();
		List<String> skuCodes = new ArrayList<>();
		// 单位
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", DictionaryCode.c1001.getCode());
		List<DictionaryInfo> unitList = dictionaryInfoProxy.queryByParam(params).getData();
		List<Long> supplierIdsList = new ArrayList<Long>();
		
		List<ItemSkuSupplier> associateSkuSupplierList  = new ArrayList<ItemSkuSupplier>();
		Map<Long,List<SupplierInfo>> skuAndSupplier = new HashMap<Long,List<SupplierInfo>>();
		if(CollectionUtils.isNotEmpty(list)){
			for(ItemResultDto itemResult : list){
				if(!itemIds.contains(itemResult.getItemId())){
					itemIds.add(itemResult.getItemId());
				}
				if (!itemDetailIds.contains(itemResult.getDetailId())) {
					itemDetailIds.add(itemResult.getDetailId());
				}
				//上架商品
				if (itemResult.getStatus() == 1 && !skuCodes.contains(itemResult.getSku())) {
					skuCodes.add(itemResult.getSku());
				}
				if(itemResult.getSupplierId().equals(ItemConstant.SUPPLIER_ID)){
					if(!skuIds.contains(itemResult.getSkuId())){
						skuIds.add(itemResult.getSkuId());
					}
				}else{
					ItemSkuSupplier skuSupplierInfo = new ItemSkuSupplier();
					skuSupplierInfo.setSkuId(itemResult.getSkuId());
					skuSupplierInfo.setSupplierId(itemResult.getSupplierId());
					associateSkuSupplierList.add(skuSupplierInfo);
					if(!supplierIdsList.contains(itemResult.getSupplierId())){
						supplierIdsList.add(itemResult.getSupplierId());
					}
				}
				SkuExportDto skuExportDto = new SkuExportDto();
				if(CollectionUtils.isNotEmpty(unitList)){
					for(DictionaryInfo dic : unitList){
						if(itemResult.getUnitId().equals(dic.getId())){
							skuExportDto.setUnitName(dic.getName());
							break;
						}
					}
				}
				skuExportDto.setItemId(itemResult.getItemId());
				skuExportDto.setDetailId(itemResult.getDetailId());
				skuExportDto.setSku(itemResult.getSku());
				skuExportDto.setSpu(itemResult.getSpu());
				skuExportDto.setPrdid(itemResult.getPrdid());
				skuExportDto.setBarcode(itemResult.getBarcode());
				skuExportDto.setMainTitle(itemResult.getMainTitle());
				skuExportDto.setSkuId(itemResult.getSkuId());
				skuExportDto.setBasicPrice(itemResult.getBasicPrice()==null?"":itemResult.getBasicPrice().toString());	
				skuExportDto.setStatus(itemResult.getStatusDesc());
				/**供应商类别 即saleType*/
				skuExportDto.setSaleType(itemResult.getSaleType()==null?"":itemResult.getSaleType().toString());
				skuExportDto.setSpName(itemResult.getSupplierName());
				skuExportDto.setInventory("");
				skuExportDto.setSalePrice(itemResult.getTopicPrice()!=null && !itemResult.getTopicPrice().equals(0)?""+itemResult.getTopicPrice():skuExportDto.getBasicPrice());
				skuExportDto.setSupplyPrice("");
				skuExportDto.setCountryId("");
				skuExportDto.setCountryName("");
				skuExportDto.setBrandCountryId("");
				skuExportDto.setBrandCountryName("");
				skuExportList.add(skuExportDto);
			}
			List<ItemInfo> infoList = itemManageService.getInfoListByIds(itemIds);
			List<ItemSkuSupplier> skuSupplierList =  new ArrayList<ItemSkuSupplier> ();
			skuSupplierList	= itemManageService.getSkuSupplierListBySkuIds(skuIds);
			if(null != skuSupplierList){
				if(null!= associateSkuSupplierList){
					skuSupplierList.addAll(associateSkuSupplierList);
				}
			}else{
				if(null!=associateSkuSupplierList){
					skuSupplierList = associateSkuSupplierList;
				}
			}
			
			if(CollectionUtils.isNotEmpty(skuSupplierList)){
				for(ItemSkuSupplier skuSupplierInfo : skuSupplierList){
					if(!supplierIdsList.contains(skuSupplierInfo.getSupplierId())){
						supplierIdsList.add(skuSupplierInfo.getSupplierId());
					}
				}
			}
			
			LOGGER.info("batch query supplier params : {}" , supplierIdsList);
			SupplierResult supplierResult = purchasingManagementService.getSuppliersByIds(supplierIdsList);
			if(null != supplierResult){
				List<SupplierInfo> supplierList = supplierResult.getSupplierInfoList();
				if(CollectionUtils.isNotEmpty(skuSupplierList)){
						for(ItemSkuSupplier skuSupplierInfo:skuSupplierList){
							for(SupplierInfo supplierDO : supplierList){
								if(supplierDO.getId().equals(skuSupplierInfo.getSupplierId())){
									if(skuAndSupplier.containsKey(skuSupplierInfo.getSkuId())){
										List<SupplierInfo> exsitList = skuAndSupplier.get(skuSupplierInfo.getSkuId());
										exsitList.add(supplierDO);
									}else{
										List<SupplierInfo> newList = new ArrayList<SupplierInfo>();
										newList.add(supplierDO);
										skuAndSupplier.put(skuSupplierInfo.getSkuId(), newList);
									}
								}
							}
						}
				}
			}
			//商品产地信息从detail查询
			List<ItemDetail> itemDetails = itemDetailService.selectByDetailIds(itemDetailIds);
			Map<Long, ItemDetail> detailMap = new HashMap<>();
			for (ItemDetail itemDetail : itemDetails) {
				detailMap.put(itemDetail.getId(), itemDetail);
			}
			
			
			//查询采购价
			Map<String, Double> mapSku2SupplyPrice = new HashMap<>();
			if(CollectionUtils.isNotEmpty(skuCodes)){
				params.clear();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "sku in(" + StringUtil.join(skuCodes, Constant.SPLIT_SIGN.COMMA) + ")");
				params.put("status", 1);
				params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "update_time asc");
				List<QuotationProduct> quotationProducts = quotationProductService.queryByParamNotEmpty(params);			
				//暂时按照一个sku对应一个采购价处理
				for (QuotationProduct quotationProduct : quotationProducts) {
					if (null != quotationProduct.getSupplyPrice()) {
						mapSku2SupplyPrice.put(quotationProduct.getSku(), quotationProduct.getSupplyPrice());
					}
				}
			}
					
			//查询已上线的商品
			List<TopicItem> validItems = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(skuCodes)){
				Map<String, Object> topicQueryParams = new HashMap<>();	
				List<DAOConstant.WHERE_ENTRY> whEntries = new ArrayList<>();
				whEntries.add(new DAOConstant.WHERE_ENTRY("sku", MYBATIS_SPECIAL_STRING.INLIST, skuCodes));
				topicQueryParams.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
				topicQueryParams.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " deletion = 0 ");
							
				List<TopicItem> topicItems = topicItemService.queryByParam(topicQueryParams);
				if(CollectionUtils.isNotEmpty(topicItems)){
					Set<Long> topicIds = new HashSet<>();
					for (TopicItem ti : topicItems) {
						topicIds.add(ti.getTopicId());
					}
					topicQueryParams.clear();
					topicQueryParams.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in( " + StringUtil.join(topicIds, Constant.SPLIT_SIGN.COMMA) + ") ");
					List<Topic> topics = topicService.queryByParam(topicQueryParams);
					List<Long> validTopicIds = new ArrayList<>();
					for (Topic t : topics) {
						Date now = new Date();
						if (Integer.valueOf(DeletionStatus.NORMAL.ordinal()).equals(t.getDeletion()) && 
								Integer.valueOf(TopicProcess.PROCESSING.ordinal()).equals(t.getProgress()) &&
								Integer.valueOf(TopicStatus.PASSED.ordinal()).equals(t.getStatus()) && 
								now.after(t.getStartTime()) && now.before(t.getEndTime())) {
							validTopicIds.add(t.getId());
						}
					}
					for (TopicItem item : topicItems) {
						if (validTopicIds.contains(item.getTopicId())) {
							validItems.add(item);
						}
					}				
				}	
			}
			
			Map<String, Integer> sku2InventoryMap = new HashMap<>();
			Map<String, List<TopicItem>> sku2TopicItemMap = new HashMap<>();
			if (CollectionUtils.isNotEmpty(validItems)) {
				List<SkuInventoryQuery> storageQueryList = new ArrayList<>();
				for (TopicItem topicItem : validItems) {
					SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
					skuInventoryQuery.setApp(App.PROMOTION);
					skuInventoryQuery.setBizId(topicItem.getTopicId().toString());
					skuInventoryQuery.setSku(topicItem.getSku());	
					skuInventoryQuery.setWarehouseId(topicItem.getStockLocationId());
					skuInventoryQuery.setBizPreOccupy(DEFAULTED.YES.equals(topicItem.getReserveInventoryFlag()));
					storageQueryList.add(skuInventoryQuery);
					List<TopicItem> topicItemList =	sku2TopicItemMap.get(topicItem.getSku()); 
					if (null == topicItemList) {
						topicItemList = new ArrayList<>();
						topicItemList.add(topicItem);
						sku2TopicItemMap.put(topicItem.getSku(), topicItemList);
					}else{
						topicItemList.add(topicItem);					
					}
				}
				//以库存查询出的为准，自动筛选掉同一专题下多个同一sku的商品重复累积库存的情况
				//Map<String, Integer> inventoryMap = inventoryQueryService.batchSelectInventory(storageQueryList);
				Map<String, Integer> inventoryMap = inventoryQueryService.querySalableInventory(storageQueryList);
				for (String key : inventoryMap.keySet()) {
					String[] strings = key.split(StorageConstant.straight);
					String sku = strings[1];
					Integer inventory = inventoryMap.get(key);
					if (null != inventory) {
						Integer skuInventory = sku2InventoryMap.get(sku);
						if(null != skuInventory){
							sku2InventoryMap.put(sku, skuInventory + inventory);
						}else{
							sku2InventoryMap.put(sku, inventory);
						}
					}
				}
			}
					
			List<Long> categoryIds = new ArrayList<Long>();
			List<Long> brandIds = new ArrayList<Long>();
			List<Long> unitIds = new ArrayList<Long>();			
			if(CollectionUtils.isNotEmpty(infoList)){
			
				for(ItemInfo infoDO : infoList){
					if(!categoryIds.contains(infoDO.getLargeId())){
						categoryIds.add(infoDO.getLargeId());
					}
					if(!categoryIds.contains(infoDO.getMediumId())){
						categoryIds.add(infoDO.getMediumId());
					}
					if(!categoryIds.contains(infoDO.getSmallId())){
						categoryIds.add(infoDO.getSmallId());
					}
					if(!brandIds.contains(infoDO.getBrandId())){
						brandIds.add(infoDO.getBrandId());
					}
					if(!unitIds.contains(infoDO.getUnitId())){
						unitIds.add(infoDO.getUnitId());
					}
				}			
				List<Category> categoryList = categoryService.selectByIdsAndStatus(categoryIds, ItemConstant.ALL_DATAS);
				if(CollectionUtils.isNotEmpty(categoryList)){
					for(ItemInfo infoDO : infoList){
						for(Category categoryDO : categoryList){
							if(infoDO.getLargeId().equals(categoryDO.getId())){
								infoDO.setLargeName(categoryDO.getName());
							}
							if(infoDO.getMediumId().equals(categoryDO.getId())){
								infoDO.setMediumName(categoryDO.getName());
							}
							if(infoDO.getSmallId().equals(categoryDO.getId())){
								infoDO.setSmallName(categoryDO.getName());
							}
						}
					}
				}
				//品牌
				List<Brand> brandList = brandService.selectListBrand(brandIds, ItemConstant.ALL_DATAS);						
				Map<Long, Brand> brandMap = new HashMap<>();
				if(CollectionUtils.isNotEmpty(brandList)){
					for(Brand brandDO: brandList){
						brandMap.put(brandDO.getId(), brandDO);
					}
					for(ItemInfo infoDO : infoList){
						Brand brand = brandMap.get(infoDO.getBrandId());
						if (null != brand) {							
							infoDO.setBrandName(brand.getName());
						}
					}
				}
				//产地
				Map<String, String> countryId2NameMap = districtInfoProxy.getAllCountryAndAllProvince();
				for(SkuExportDto skuExportDto : skuExportList){
					skuExportDto.setSpName(getSupplierNames(skuAndSupplier,skuExportDto.getSkuId()));
					skuExportDto.setSpType(getSupplierTypes(skuAndSupplier,skuExportDto.getSkuId()));
					for(ItemInfo infoDO : infoList){
						if(infoDO.getId().equals(skuExportDto.getItemId())){
							skuExportDto.setLargeName(infoDO.getLargeName());
							skuExportDto.setMediumName(infoDO.getMediumName());
							skuExportDto.setSmallName(infoDO.getSmallName());
							skuExportDto.setBrandName(infoDO.getBrandName());
							skuExportDto.setSpuName(infoDO.getMainTitle());//?\
							skuExportDto.setSupplierId(infoDO.getSupplierId());//设置是否供应商导入
							break;
						}
					}
					//品牌产地和商品产地
					ItemDetail itemDetail = detailMap.get(skuExportDto.getDetailId());
					if(null != itemDetail){
						Brand brand = brandMap.get(itemDetail.getBrandId());
						if (null != brand) {
							String brandCountryName = countryId2NameMap.get(brand.getCountryId().toString());
							if (null != brandCountryName) {
								skuExportDto.setBrandCountryId(brand.getCountryId().toString());
								skuExportDto.setBrandCountryName(brandCountryName);
							}
						}
						if (null != itemDetail.getCountryId()) {
							skuExportDto.setCountryId(itemDetail.getCountryId().toString());
							skuExportDto.setCountryName(countryId2NameMap.get(itemDetail.getCountryId().toString()));
						}
						//设置是否供应商导入
						skuExportDto.setSupplierId(itemDetail.getSupplierId());
					}
					//售价和库存
					Integer inventory = sku2InventoryMap.get(skuExportDto.getSku());
					if(null != inventory){
						skuExportDto.setInventory(inventory.toString());
						List<TopicItem> topicItemList = sku2TopicItemMap.get(skuExportDto.getSku());
						if (null != topicItemList && (skuExportDto.getSalePrice()==null || skuExportDto.getSalePrice().equals(0)) ) {
							skuExportDto.setSalePrice(topicItemList.get(0).getTopicPrice().toString());
						}
					}
					//采购价
					Double supplyPrice = mapSku2SupplyPrice.get(skuExportDto.getSku());
					if (null != supplyPrice) {
						skuExportDto.setSupplyPrice(supplyPrice.toString());
					}
				}
				return skuExportList;
			}
		}
		return null;
	}
	
	
	private String getSupplierNames(Map<Long,List<SupplierInfo>> skuAndSupplierMap,Long skuId){
		if(MapUtils.isNotEmpty(skuAndSupplierMap)&&null!=skuId){
			List<SupplierInfo> supplierList = skuAndSupplierMap.get(skuId);
			if(CollectionUtils.isNotEmpty(supplierList)){
				StringBuffer sb = new StringBuffer();
				int size = supplierList.size();
				int i = 0;
				for(SupplierInfo supplierDO : supplierList){
					i++;
					sb.append(supplierDO.getName());
					if(i<size){
						sb.append(LINE_STR);
					}
				}
				return sb.toString();
					
			}
		}
		return null;
	}
	
	private String getSupplierTypes(Map<Long,List<SupplierInfo>> skuAndSupplierMap,Long skuId){
		if(MapUtils.isNotEmpty(skuAndSupplierMap)&&null!=skuId){
			List<SupplierInfo> supplierList = skuAndSupplierMap.get(skuId);
			if(CollectionUtils.isNotEmpty(supplierList)){
				StringBuffer sb = new StringBuffer();
				int size = supplierList.size();
				int i = 0;
				for(SupplierInfo supplierDO : supplierList){
					i++;
					sb.append(supplierDO.getSuplierTypeName());
					if(i<size){
						sb.append(LINE_STR);
					}
				}
				return sb.toString();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * <pre>
	 *   初始化商品查询接口
	 * </pre>
	 *
	 * @param query
	 */
	private void  initItemQuery (ItemQuery query){
		String code = "";
		if(null!=query.getLargeId()){
			Category categoryDO = categoryService.queryById(query.getLargeId());
			code = categoryDO.getCode();
		}
		if(null!=query.getMediumId()){
			Category categoryDO = categoryService.queryById(query.getMediumId());
			code = categoryDO.getCode();
			
		}
		if(null!=query.getSmallId()){
			Category categoryDO = categoryService.queryById(query.getSmallId());
			code = categoryDO.getCode();
		}
		query.setCategoryCode(code);
	}
	
	/**
	 * 导出商品详情（翻译）
	 * @param detailIdList
	 * @return
	 * @throws Exception 
	 */
//	public List<ItemDetailForTransDto> exportDetailList(ItemQuery query,List<Long> detailIdList){
	public List<ItemDetailForTransDto> exportDetailList(ItemQuery query) throws Exception{
		List<Long> detailIdList = new ArrayList<Long>();
		List<ItemDetailForTransDto>  itemDetailForTransList  = new ArrayList<ItemDetailForTransDto>();
//		if(null==detailIdList||detailIdList.size()<=0){
			detailIdList = new ArrayList<Long>();
//			itemSkuSerivce.queryByParam(params);
			query.setPageSize(pageSize);
			initItemQuery(query);
			List<SkuExportDto>  listAll  = new ArrayList<SkuExportDto>();
			query.setStartPage(1);
			PageInfo<ItemResultDto> itemPage = itemSkuSerivce.searchItemByQuery(query);
			if (null == itemPage || CollectionUtils.isEmpty(itemPage.getRows())) {
				return null;
			}
			List<SkuExportDto> list = bindBaseData(itemPage.getRows());
			if(CollectionUtils.isEmpty(list)){
				return null;
			}
			listAll.addAll(list);
			Integer totalPage = itemPage.getTotal();
			int i = 2;
			do{
				query.setStartPage(i);
				itemPage = itemSkuSerivce.searchItemByQuery(query);
				if (null != itemPage && !CollectionUtils.isEmpty(itemPage.getRows())) {
					List<SkuExportDto> list1 = bindBaseData(itemPage.getRows());
					if(CollectionUtils.isNotEmpty(list1)){
						listAll.addAll(list1);
					}	
				}
				i++;
			}while(i <= totalPage);
			
			for(SkuExportDto skuExportDto:listAll){
				detailIdList.add(skuExportDto.getDetailId());
			}
//		}
		
			List<ItemDetail> itemDetailList = itemDetailService.selectByDetailIds(detailIdList);//根据detailId集合查itemDetail对象集合
			if(null == itemDetailList || itemDetailList.size()<=0){
				LOGGER.error("找不到相应的商品详情信息！");
				throw new Exception("找不到相应的商品详情信息！");
			}
			List<Long> itemIds = new ArrayList<Long>();
			for(ItemDetail itemDetail:itemDetailList){
				 Long itemId = itemDetail.getItemId();
				 itemIds.add(itemId);
			}
//			Map<String,Object> itemParam = new HashMap<String,Object>();
//			itemParam.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(itemIds, Constant.SPLIT_SIGN.COMMA)+")");
//			List<ItemInfo> itemInfoList = itemInfoService.queryByParam(itemParam);//根据itemId集合 获取itemInfo对象集合
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+StringUtil.join(detailIdList, Constant.SPLIT_SIGN.COMMA)+")");
			List<ItemDesc> itemDescList = itemDescService.queryByParam(params);//
			if(null == itemDescList || itemDescList.size()<=0){
				LOGGER.error("找不到相应的商品描述信息！");
				throw new Exception("找不到相应的商品描述信息！");
			}
			
			List<ItemSku> itemSkuList = itemSkuSerivce.queryByParam(params);
			if(null == itemSkuList || itemSkuList.size()<=0){
				LOGGER.error("找不到相应的sku信息！");
				throw new Exception("找不到相应的sku信息！");
			}
			
			List<Long> brandIdList = new ArrayList<Long>();
			for(ItemDetail itemDetail:itemDetailList){
				Long brandId = itemDetail.getBrandId();
				brandIdList.add(brandId);
			}
			List<Brand> brandList = brandService.selectListBrand(brandIdList, 1);//获取品牌
			if(null == brandList || brandList.size()<=0){
				LOGGER.error("找不到相应的品牌信息！");
				throw new Exception("找不到相应的品牌信息！");
			}
			for(ItemDetail itemDetail:itemDetailList){
				ItemDetailForTransDto itemDetailForTransDto = new ItemDetailForTransDto();
				itemDetailForTransDto.setDetailId(itemDetail.getId());
				itemDetailForTransDto.setItemTitle(itemDetail.getItemTitle());//商品名称
				for(ItemDesc itemDesc:itemDescList){
					if(itemDesc.getDetailId().equals(itemDetail.getId()) 
							&& itemDesc.getItemId().equals(itemDetail.getItemId())){//用itemId和detailId 将itemDetail对象和itemDesc对象进行关联
						itemDetailForTransDto.setDetailDesc(itemDesc.getDescription().replaceAll("<[^>]*>", "\n\t"));
					}
				}
				for(ItemSku itemSku:itemSkuList){
					if(itemSku.getDetailId().equals(itemDetail.getId()) 
							&& itemSku.getItemId().equals(itemDetail.getItemId())){//用itemId和detailId 将itemDetail对象和itemSku对象进行关联
						itemDetailForTransDto.setSku(itemSku.getSku());
						itemDetailForTransDto.setThirdCategoryId(itemSku.getCategoryId());
					}
				}
				for(Brand brand:brandList){
					if(brand.getId().equals(itemDetail.getBrandId())){
						itemDetailForTransDto.setBrandName(brand.getName());//品牌名称
					}
				}
				Map<String, Object> picParams = new HashMap<String, Object>();
				picParams.put("item_id",itemDetail.getItemId() );
				picParams.put("detail_id", itemDetail.getId());
				List<ItemPictures> pictures = itemPicturesService.queryByParam(picParams);
				String picUrl = "";
				for(ItemPictures itemPictures:pictures){
					picUrl += "http://item.qn.seagoor.com/"+itemPictures.getPicture()+"\r\n";
				}
				itemDetailForTransDto.setPicUrl(picUrl); 
				itemDetailForTransList.add(itemDetailForTransDto);
			}
			for(ItemDetailForTransDto itemDetailForTransDto:itemDetailForTransList){
				Category thirdCategory = categoryService.queryById(itemDetailForTransDto.getThirdCategoryId());
				Category secondCategory = categoryService.queryById(thirdCategory.getParentId());
				Category firstCategory = categoryService.queryById(secondCategory.getParentId());
				itemDetailForTransDto.setThirdCategoryName(thirdCategory.getName());
				itemDetailForTransDto.setSecondCategoryId(secondCategory.getId());
				itemDetailForTransDto.setSecondCategoryName(secondCategory.getName());
				itemDetailForTransDto.setFirstCategoryId(firstCategory.getId());
				itemDetailForTransDto.setFirstCategoryName(firstCategory.getName());
			}
//		for(Long detailId: detailIdList){
//			ItemDetailForTransDto itemDetailForTransDto = new ItemDetailForTransDto();
//			ItemDetail itemDetail = itemDetailService.queryById(detailId);
//			ItemInfo itemInfo = itemInfoService.queryById(itemDetail.getItemId());
//			
//			Map<String,Object> params = new HashMap<String,Object>();
//			params.put("item_id", itemDetail.getItemId());
//			params.put("detail_id", detailId);
//			ItemDesc itemDesc = itemDescService.queryUniqueByParams(params);
//			ItemSku itemSku = itemSkuSerivce.queryUniqueByParams(params);
//			
//			Brand brand = brandService.queryById(itemInfo.getBrandId());//获取品牌
//			
//			itemDetailForTransDto.setDetailId(detailId);
//			itemDetailForTransDto.setSku(itemSku.getSku());//商品sku
//			itemDetailForTransDto.setBrandName(brand.getName());//品牌名称
//			itemDetailForTransDto.setItemTitle(itemDetail.getItemTitle());//商品名称
//			itemDetailForTransDto.setDetailDesc(itemDesc.getDescription().replaceAll("<[^>]*>", "\n\t"));//商品详情描述
//			itemDetailForTransList.add(itemDetailForTransDto);
//		}
		
		return itemDetailForTransList;
	}; 
}
