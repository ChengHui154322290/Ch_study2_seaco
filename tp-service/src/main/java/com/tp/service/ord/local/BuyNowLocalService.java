package com.tp.service.ord.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.ord.CartConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.CartLineSimpleDTO;
import com.tp.dto.ord.ItemInventoryDTO;
import com.tp.dto.ord.SalePropertyDTO;
import com.tp.dto.ord.remote.TopicPolicyDTO;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.ItemServiceException;
import com.tp.exception.OrderServiceException;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.sup.SupplierInfo;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.sup.SupplierResult;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.mmp.groupbuy.IGroupbuyInfoService;
import com.tp.service.ord.local.IBuyNowLocalService;
import com.tp.service.ord.remote.ICheckTopicRemoteService;
import com.tp.service.prd.ItemService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.util.BigDecimalUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 立即购买服务
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class BuyNowLocalService implements IBuyNowLocalService {

	private static final Logger logger = LoggerFactory.getLogger(BuyNowLocalService.class);
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private ITopicService topicInfoService;
	@Autowired
	private IPurchasingManagementService purchasingManagementService;
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	@Autowired
	private ICheckTopicRemoteService checkTopicRemoteService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private IGroupbuyInfoService groupbuyInfoService;
	
//	@Override
//	public String buyNow(Long memberId, String ip, Integer platform, CartLineDTO cartLine) throws OrderServiceException{
//		
//		//立即购买check
//		buyNowCheck(memberId, ip, platform, cartLine);
//		
//		// 初始化CartDTO
//		CartDTO cartDTO = new CartDTO();
//		CartDTO returnCartDTO = new CartDTO();
//		// 初始化CartLineDTO列表
//		List<CartLineDTO> cartLineDTOList = new ArrayList<CartLineDTO>();
//
//		// 组装查询商品列表skuCodeList
//		List<String> skuCodeList = new ArrayList<String>();
//		skuCodeList.add(cartLine.getSkuCode());
//		
//		List<ItemResultDto> itemList = itemService.getSkuList(skuCodeList);
//		
//		int skuType = itemList.get(0).getWavesSign();
//		
//		// 根据skuCodeList调用商品接口获取商品列表详情信息
//		List<SkuInventoryQuery> storageQueryList = new ArrayList<SkuInventoryQuery>();
//		List<ItemInventoryDTO> itemInventoryList = new ArrayList<ItemInventoryDTO>();
//		
//		// 组装查询库存列表storageQueryList
//		SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
//		skuInventoryQuery.setApp(App.PROMOTION);
//		skuInventoryQuery.setBizId(cartLine.getTopicId().toString());
//		skuInventoryQuery.setSku(cartLine.getSkuCode());
//		storageQueryList.add(skuInventoryQuery);
//		
//		// 组装查询仓库id列表itemInventoryList
//		ItemInventoryDTO itemInventoryDTO = new ItemInventoryDTO();
//		itemInventoryDTO.setTopicId(cartLine.getTopicId());
//		itemInventoryDTO.setSkuCode(cartLine.getSkuCode());
//		itemInventoryList.add(itemInventoryDTO);
//		
//		if (CollectionUtils.isNotEmpty(itemList)) {
//			Map<String, ItemResultDto> itemMap = new HashMap<String, ItemResultDto>();
//			Map<CartLineSimpleDTO, CartLineDTO> returnCartLineMap = new HashMap<CartLineSimpleDTO, CartLineDTO>();
//			List<String> validateSkuList = new ArrayList<String>();
//			List<Long> supplierIdList = new ArrayList<Long>();
//			
//			for (ItemResultDto item : itemList) {
//				// 获取有效skuList
//				if (item.getStatus() == CartConstant.ITEM_STATUS_1) {
//					validateSkuList.add(item.getSku());
//				}
//				// 组装成itemMap
//				itemMap.put(item.getSku(), item);
//				
//				// 组装供应商id列表
//				supplierIdList.add(item.getSupplierId());
//				
//			}
//
//			// 海淘添加
//			Map<Long, Long> freightTemplateMap = new HashMap<Long, Long>();
//			Map<Long,SupplierInfo> supplierInfoMap = new HashMap<Long,SupplierInfo>();
//			Map<String, ItemInventoryDTO> itemInventoryMap =  new HashMap<String, ItemInventoryDTO>();
//			if(CartConstant.TYPE_SEA==skuType){
//				// 根据供应商id列表，获取商家运费模板
//				try {
//					SupplierResult supplierResult = purchasingManagementService.getSuppliersByIds(supplierIdList);
//					if(supplierResult!=null && CollectionUtils.isNotEmpty(supplierResult.getSupplierInfoList())){
//						for(SupplierInfo supplier : supplierResult.getSupplierInfoList()){
//							freightTemplateMap.put(supplier.getId(), supplier.getFreightTemplateId());
//							supplierInfoMap.put(supplier.getId(), supplier);
//						}
//					}
//				} catch (Exception e) {
//					logger.error("根据supplierIdList: {} 获取供应商信息失败！", JSONArray.fromObject(supplierIdList).toString());
//					throw new OrderServiceException(e);					
//				}
//				
//				// 根据商品sku、topicId获取仓库ID和通关渠道
//				List<ItemSkuArt> checkList = new ArrayList<ItemSkuArt>();
//				List<Long> seaChannelIdList = new ArrayList<Long>();
//				List<ItemInventoryDTO> returnItemInventoryList = topicInfoService.queryItemInventory(itemInventoryList);
//				
//				if(CollectionUtils.isNotEmpty(returnItemInventoryList)){
//					for(ItemInventoryDTO itemInventory:returnItemInventoryList){
//						ItemSkuArt skuArt = new ItemSkuArt();
//						skuArt.setSku(itemInventory.getSkuCode());
//						skuArt.setBondedArea(itemInventory.getBondedArea());
//						checkList.add(skuArt);
//						seaChannelIdList.add(itemInventory.getBondedArea());
//					}
//				}
//				
//				// 根据seaChannelIdList查询BASE
//				List<ClearanceChannels> seaChanneList = clearanceChannelsService.getClearanceChannelsListByIds(seaChannelIdList);
//				
//				if(CollectionUtils.isNotEmpty(seaChanneList)){
//					for(ClearanceChannels clearanceChannels:seaChanneList){
//						for(ItemInventoryDTO itemInventory:returnItemInventoryList){
//							if(itemInventory.getBondedArea().equals(clearanceChannels.getId())){
//								itemInventory.setBondedAreaName(clearanceChannels.getName());
//							}
//						}
//					}
//				}
//				
//				// 查询商品海关信息,重新组装returnItemInventoryList
//				if(CollectionUtils.isNotEmpty(checkList)){
//					List<ItemSkuArt> checkReturn = itemService.checkBoundedInfoForSales(checkList);
//					if(CollectionUtils.isNotEmpty(checkReturn)){
//						for(ItemSkuArt skuArt:checkReturn){
//							for(ItemInventoryDTO itemInventory:returnItemInventoryList){
//								if(itemInventory.getSkuCode().equals(skuArt.getSku()) && itemInventory.getBondedArea().equals(skuArt.getBondedArea())){
//									itemInventory.setArticleNumber(skuArt.getArticleNumber());
//								}
//							}
//						}
//					}
//				}
//				
//				// 根据returnItemInventoryList组装itemInventoryMap
//				for(ItemInventoryDTO itemInventory:returnItemInventoryList){
//					itemInventoryMap.put(itemInventory.getTopicId() + "-" + itemInventory.getSkuCode(), itemInventory);
//				}
//				
//			}
//			
//			// 根据商品列表获取可用库存
//			Map<String, Integer> inventoryMap = inventoryQueryService.batchSelectInventory(storageQueryList);
//
//			// 根据返回商品list信息和缓存商品封装CartLineDTO
//			CartLineDTO cartLineDTO = new CartLineDTO();
//			cartLineDTO.setPlatformId(platform);
//			cartLineBuild(itemMap, inventoryMap, freightTemplateMap, itemInventoryMap, cartLine, cartLineDTO, skuType, supplierInfoMap);
//
//			// 添加到list
//			cartLineDTOList.add(cartLineDTO);
//			
//
//			if (CollectionUtils.isNotEmpty(cartLineDTOList)) {
//				// 根据skuType封装不同的购物车数据
//				if (CartConstant.TYPE_SEA == skuType) {//海淘数据封装
//					
//					// 海淘数据分组
//					Map<String, List<CartLineDTO>> seaMap = new LinkedHashMap<String, List<CartLineDTO>>();
//					Map<String, String> supplierMap = new LinkedHashMap<String, String>();
//					for (CartLineDTO line : cartLineDTOList) {
//						List<CartLineDTO> lineList = seaMap.get(line.getSupplierId().toString());
//						if (null == lineList) {
//							lineList = new ArrayList<>();
//							lineList.add(line);
//							seaMap.put(line.getSupplierId().toString(), lineList);
//							supplierMap.put(line.getSupplierId().toString(), line.getSupplierName());
//						} else {
//							lineList.add(line);
//						}
//					}
//					
//					// 封装CartDTO
//					cartDTO.setCartType(skuType);
//					cartDTO.setSeaMap(seaMap);
//					cartDTO.setSupplierMap(supplierMap);
//					cartDTO.setValidateSkuList(validateSkuList);
//					
//					// 促销模块封装CartDTO;
//					returnCartDTO = topicInfoService.cartValidate(cartDTO);
//					
//					// 封装最终显示的CartDTO
//					if (returnCartDTO != null && !returnCartDTO.getSeaMap().isEmpty()) {
//						for (Map.Entry<String, List<CartLineDTO>> entry : returnCartDTO.getSeaMap().entrySet()) {
//							// 对促销模块返回的CartDTO中的 lineList进行重新排序(区分有效无效商品列表)
//							List<CartLineDTO> validateLineList = new ArrayList<CartLineDTO>();
//							List<CartLineDTO> inValidateLineList = new ArrayList<CartLineDTO>();
//							List<CartLineDTO> allLineList = new ArrayList<CartLineDTO>();
//							
//							for (CartLineDTO cartLineDto : entry.getValue()) {
//								if (cartLineDto.getSkuStatus().intValue() == CartConstant.ITEM_STATUS_1 && cartLineDto.getTopicItemInfo().getValidate()) {
//									validateLineList.add(cartLineDto);
//								} else {
//									inValidateLineList.add(cartLineDto);
//								}
//							}
//							
//							allLineList.addAll(validateLineList);
//							allLineList.addAll(inValidateLineList);
//							
//							seaMap.put(entry.getKey(), allLineList);
//						}
//						
//						returnCartDTO.setSeaMap(seaMap);
//						
//						// 根据促销返回cartDTO封装页面展示
//						returnCartBuild(returnCartDTO, cartLine, returnCartLineMap, skuType);
//						
//					} else {
//						if (logger.isInfoEnabled()) {
//							logger.info("根据cartDTO: {} 获取促销信息失败！", JSONObject.fromObject(cartDTO).toString());
//						}
//					}
//					
//				}else{//西客商城数据封装
//					
//					// 封装CartDTO
//					cartDTO.setCartType(skuType);
//					cartDTO.setLineList(cartLineDTOList);
//					cartDTO.setValidateSkuList(validateSkuList);
//
//					// 促销模块封装CartDTO
//					returnCartDTO = topicInfoService.cartValidate(cartDTO);
//
//					// 封装最终显示的CartDTO
//					if (returnCartDTO != null && CollectionUtils.isNotEmpty(returnCartDTO.getLineList())) {
//						// 对促销模块返回的CartDTO中的 lineList进行重新排序(区分有效无效商品列表)
//						List<CartLineDTO> validateLineList = new ArrayList<CartLineDTO>();
//						List<CartLineDTO> inValidateLineList = new ArrayList<CartLineDTO>();
//						List<CartLineDTO> allLineList = new ArrayList<CartLineDTO>();
//
//						List<CartLineDTO> returnLinelist = returnCartDTO.getLineList();
//						for (CartLineDTO returnCartLine : returnLinelist) {
//							if (returnCartLine.getSkuStatus().intValue() == CartConstant.ITEM_STATUS_1 && returnCartLine.getTopicItemInfo().getValidate()) {
//								validateLineList.add(returnCartLine);
//							} else {
//								inValidateLineList.add(returnCartLine);
//							}
//						}
//						allLineList.addAll(validateLineList);
//						allLineList.addAll(inValidateLineList);
//
//						returnCartDTO.setLineList(allLineList);
//
//						// 根据促销返回cartDTO封装页面展示
//						returnCartBuild(returnCartDTO, cartLine, returnCartLineMap, skuType);
//					} else {
//						if (logger.isInfoEnabled()) {
//							logger.info("根据cartDTO: {} 获取促销信息失败！", JSONObject.fromObject(cartDTO).toString());
//						}
//					}
//					
//				}
//
//			}
//			
//			//存放到redis
//			String key = UUID.randomUUID().toString();
//			jedisCacheUtil.setCache(key, returnCartDTO);
//			
//			return key;
//		} else {
//			if (logger.isInfoEnabled()) {
//				logger.info("根据skuCodeList: {} 获取商品列表信息为空！", JSONArray.fromObject(skuCodeList).toString());
//			}
//		}
//		
//		return null;
//		
//	}
	
	/**
	 * <pre>
	 * 根据返回item信息封装cartLineDTO
	 * </pre>
	 * 
	 * @param itemMap
	 * @param freightTemplateMap
	 * @param itemMap
	 * @param itemInventoryMap
	 * @param cartLineDTO
	 */
	private void cartLineBuild(Map<String, ItemResultDto> itemMap, Map<String, Integer> inventoryMap, Map<Long, Long> freightTemplateMap, Map<String, ItemInventoryDTO> itemInventoryMap, CartLineDTO cartLine, CartLineDTO cartLineDTO, int skuType, Map<Long,SupplierInfo> supplierInfoMap) {
		// 商品通用属性赋值
		ItemResultDto itemResult = itemMap.get(cartLine.getSkuCode());
		Integer stock = inventoryMap.get(cartLine.getTopicId() + "-" + cartLine.getSkuCode());
		
		if (itemResult != null) {
			freightTemplateMap.get(itemResult.getSupplierId());
			cartLineDTO.setBarcode(itemResult.getBarcode());// 获取商品条形码
			cartLineDTO.setSkuCode(itemResult.getSku());
			cartLineDTO.setItemName(itemResult.getMainTitle());
			cartLineDTO.setItemCode(itemResult.getSpu());
			cartLineDTO.setItemId(itemResult.getItemId());
			cartLineDTO.setItemPic(itemResult.getImageUrl());
			cartLineDTO.setListPrice(itemResult.getBasicPrice());
			cartLineDTO.setSkuStatus(itemResult.getStatus());

			cartLineDTO.setBrandId(itemResult.getBrandId());
			cartLineDTO.setBrandName(itemResult.getBrandName());
			cartLineDTO.setLargeId(itemResult.getLargeId());
			cartLineDTO.setMediumId(itemResult.getMediumId());
			cartLineDTO.setSmallId(itemResult.getSmallId());
			cartLineDTO.setCategoryCode(itemResult.getCategoryCode());
			cartLineDTO.setCategoryName(itemResult.getCategoryName());
			cartLineDTO.setStock(stock);
			cartLineDTO.setSupplierId(itemResult.getSupplierId());
			cartLineDTO.setSupplierName(itemResult.getSupplierName());
			cartLineDTO.setSupplierAlias(itemResult.getSupplierName());
			cartLineDTO.setWeightNet(itemResult.getWeightNet());
			cartLineDTO.setWeight(itemResult.getWeight());
			cartLineDTO.setTarrifRate(itemResult.getTarrifRateValue());
			cartLineDTO.setCustomsRate(itemResult.getCustomsRate());
			cartLineDTO.setExciseRate(itemResult.getExciseRate());
			cartLineDTO.setAddedValueRate(itemResult.getAddedValueRate());
			
			if(MapUtils.isNotEmpty(supplierInfoMap)){
				SupplierInfo supplierInfo = supplierInfoMap.get(itemResult.getSupplierId());
				if(supplierInfo!=null){
					cartLineDTO.setSupplierAlias(supplierInfo.getAlias());
				}
			}
			
			if(CartConstant.TYPE_SEA==skuType){
				ItemInventoryDTO itemInventoryDTO = itemInventoryMap.get(cartLine.getTopicId() + "-" + cartLine.getSkuCode());
				cartLineDTO.setSaleType(itemResult.getWavesSign());
				cartLineDTO.setWavesSign(itemResult.getWavesSign());
				cartLineDTO.setFreightTemplateId(freightTemplateMap.get(itemResult.getSupplierId()));
				cartLineDTO.setProductCode(itemInventoryDTO.getArticleNumber());
				cartLineDTO.setSeaChannel(itemInventoryDTO.getBondedArea());
				cartLineDTO.setSeaChannelName(itemInventoryDTO.getBondedAreaName());
				cartLineDTO.setStorageType(itemInventoryDTO.getStorageType());
			}else{
				cartLineDTO.setSaleType(itemResult.getSaleType());
				cartLineDTO.setFreightTemplateId(itemResult.getFreightTemplateId());
			}
			
			cartLineDTO.setUnit(itemResult.getUnitName());
			cartLineDTO.setRefundDays(itemResult.getReturnDays());

			cartLineDTO.setAreaId(cartLine.getAreaId());
			cartLineDTO.setTopicId(cartLine.getTopicId());
			
			// 封装商品销售属性
			List<SalePropertyDTO> salePropList = new ArrayList<SalePropertyDTO>();
			getItemAttribute(itemResult, salePropList);
			cartLineDTO.setSalePropertyList(salePropList);

			// 封装购物车行信息
			cartLineDTO.setSelected(cartLine.getSelected());
			cartLineDTO.setCreateTime(cartLine.getCreateTime());
			cartLineDTO.setQuantity(cartLine.getQuantity());
		}
	}
	
	/**
	 * <pre>
	 * 获取商品属性信息
	 * </pre>
	 * 
	 * @param item
	 * @param salePropList
	 */
	private void getItemAttribute(ItemResultDto item, List<SalePropertyDTO> salePropList) {
		List<ItemDetailSpec> specList = item.getItemDetailSpecList();

		Collections.sort(specList, new Comparator<ItemDetailSpec>() {
			public int compare(ItemDetailSpec cp1, ItemDetailSpec cp2) {
				return cp1.getSort().compareTo(cp2.getSort());
			}
		});

		if (CollectionUtils.isNotEmpty(specList)) {
			for (ItemDetailSpec detailSpecDto : specList) {
				SalePropertyDTO saleProperty = new SalePropertyDTO();
				if (StringUtils.isNotBlank(detailSpecDto.getSpecName())) {
					saleProperty.setSpecName(detailSpecDto.getSpecName());
					saleProperty.setSpecGroupName(detailSpecDto.getSpecGroupName());
					salePropList.add(saleProperty);
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * 封装最终显示的CartDTO
	 * </pre>
	 * 
	 * @param returnCartDTO
	 * @param cartLineSimpleList
	 * @param returnCartLineMap
	 */
	private void returnCartBuild(CartDTO returnCartDTO, CartLineDTO cartLine, Map<CartLineSimpleDTO, CartLineDTO> returnCartLineMap, int cartType) {
		
		if(CartConstant.TYPE_SEA == cartType){
			for (Map.Entry<String, List<CartLineDTO>> entry : returnCartDTO.getSeaMap().entrySet()) {
				for (CartLineDTO cartLineDto : entry.getValue()) {
					CartLineSimpleDTO mapKey1 = new CartLineSimpleDTO();
					mapKey1.setTopicId(cartLineDto.getTopicId());
					mapKey1.setSkuCode(cartLineDto.getSkuCode());

					returnCartLineMap.put(mapKey1, cartLineDto);
				}
			}
			subReturnCartBuild(cartLine, returnCartLineMap);
		}else{
			for (CartLineDTO returnCartLineDTO : returnCartDTO.getLineList()) {
				CartLineSimpleDTO mapKey1 = new CartLineSimpleDTO();
				mapKey1.setTopicId(returnCartLineDTO.getTopicId());
				mapKey1.setSkuCode(returnCartLineDTO.getSkuCode());

				returnCartLineMap.put(mapKey1, returnCartLineDTO);
			}
			subReturnCartBuild(cartLine, returnCartLineMap);
		}
	}
	
	private void subReturnCartBuild(CartLineDTO cartLine, Map<CartLineSimpleDTO, CartLineDTO> returnCartLineMap) {
		CartLineSimpleDTO mapKey2 = new CartLineSimpleDTO();
		mapKey2.setTopicId(cartLine.getTopicId());
		mapKey2.setSkuCode(cartLine.getSkuCode());

		if (returnCartLineMap.get(mapKey2) == null || returnCartLineMap.get(mapKey2).getTopicItemInfo() == null) {
			if (logger.isErrorEnabled()) {
				logger.error("购物车商品详情:{}", JSONObject.fromObject(returnCartLineMap.get(mapKey2)));
			}
		}

		CartLineDTO cartLineDTO = returnCartLineMap.get(mapKey2);

		cartLineDTO.setSalePrice(cartLineDTO.getTopicItemInfo().getTopicPrice());

		cartLineDTO.setSubTotal(BigDecimalUtil.multiply(cartLineDTO.getTopicItemInfo().getTopicPrice(), cartLine.getQuantity()).doubleValue());

		if (cartLineDTO.getSkuStatus().intValue() == CartConstant.ITEM_STATUS_1 && cartLineDTO.getTopicItemInfo().getValidate()
				&& (cartLineDTO.getQuantity().intValue() <= cartLineDTO.getTopicItemInfo().getLimitAmount() && cartLineDTO.getQuantity().intValue() <= cartLineDTO.getStock())
				&& !cartLineDTO.getTopicItemInfo().getLocked()) {

			cartLineDTO.setStatus(CartConstant.CART_STATUS_0);
		} else {
			if (cartLineDTO.getSkuStatus().intValue() != CartConstant.ITEM_STATUS_1 || !cartLineDTO.getTopicItemInfo().getValidate()) {// 商品状态无效
				cartLineDTO.setStatus(CartConstant.CART_STATUS_2);
			} else if (cartLineDTO.getQuantity().intValue() > cartLineDTO.getStock() || cartLineDTO.getTopicItemInfo().getLocked()) {// 商品库存不足 或 已锁定
				cartLineDTO.setStatus(CartConstant.CART_STATUS_1);
			} else if (cartLineDTO.getQuantity().intValue() > cartLineDTO.getTopicItemInfo().getLimitAmount()) {// 超过限购数量
				cartLineDTO.setStatus(CartConstant.CART_STATUS_3);
			}
		}
	}
	
//	private ResultInfo<Boolean> buyNowCheck(Long memberId, String ip, Integer platform, CartLineDTO cartLineDTO){
//		List<String> itemQueryList = new ArrayList<String>();
//		List<SkuInventoryQuery> storageQueryList = new ArrayList<SkuInventoryQuery>();
//		List<TopicItemCartQuery> topicQueryList = new ArrayList<TopicItemCartQuery>();
//		
//		// 封装item验证list
//		itemQueryList.add(cartLineDTO.getSkuCode());
//
//		// 封装 storage验证list
//		SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
//		skuInventoryQuery.setApp(App.PROMOTION);
//		skuInventoryQuery.setBizId(cartLineDTO.getTopicId().toString());
//		skuInventoryQuery.setQuantity(cartLineDTO.getQuantity());
//		skuInventoryQuery.setSku(cartLineDTO.getSkuCode());
//
//		storageQueryList.add(skuInventoryQuery);
//
//		// 封装topic验证list
//		TopicItemCartQuery topicItemCartQuery = new TopicItemCartQuery();
//
//		topicItemCartQuery.setSku(cartLineDTO.getSkuCode());
//		topicItemCartQuery.setPlatform(platform);
//		topicItemCartQuery.setArea(cartLineDTO.getAreaId());
//		topicItemCartQuery.setTopicId(cartLineDTO.getTopicId());
//		topicItemCartQuery.setAmount(cartLineDTO.getQuantity());
//
//		topicItemCartQuery.setMemberId(memberId);
//		topicItemCartQuery.setUip(ip);
//
//		topicQueryList.add(topicItemCartQuery);
//
//		if (CollectionUtils.isNotEmpty(itemQueryList)) {
//			/* 该商品的有效性检查 */
//			try {
//				ResultInfo<Boolean> rs = itemService.checkItemList(itemQueryList);
//				if (!rs.success) {
//					throw new OrderServiceException(OrderErrorCodes.BUYNOW_VALIDATE_SKU_ERROR);
//				}
//			} catch (ItemServiceException e) {
//				throw new OrderServiceException(OrderErrorCodes.BUYNOW_VALIDATE_SKU_ERROR);
//			}
//
//			/* 获取商品限购信息 */
//			List<TopicPolicyDTO> topicPolicyList = new ArrayList<TopicPolicyDTO>();
//			try {
//				topicPolicyList = topicInfoService.queryTopicPolicyInfo(topicQueryList);
//			} catch (Exception e) {
//				logger.error("根据skuCodeList: {} 获取商品限购信息失败！", JSONArray.fromObject(topicQueryList).toString());
//				throw new OrderServiceException(OrderErrorCodes.GET_TOPIC_INFO_ERROR);
//			}
//
//			/* 本地限购检查 */
//			if (CollectionUtils.isNotEmpty(topicPolicyList)) {
//				for (TopicPolicyDTO topicPolicy : topicPolicyList) {
//					ResultInfo<Boolean> returnData = checkTopicRemoteService.checkTopicPolicy(topicPolicy);
//					if (!returnData.success) {
//						return returnData;
//					}
//				}
//			}
//
//			/* 促销限购检查 */
//			ResultInfo<Boolean> topicRM = topicInfoService.validateTopicItemList(topicQueryList);
//			if (!topicRM.success) {
//				logger.info(topicRM.msg.getMessage());
//				return topicRM;
//			}
//
//			/* 该商品的库存检查 */
//			Map<String, ResultInfo<String>> rmm = inventoryQueryService.checkAllInventory(storageQueryList);
//
//			for (ResultInfo<String> resultMessage : rmm.values()) {
//				if(!resultMessage.success){
//					return new ResultInfo<Boolean>(resultMessage.msg);
//				}
//			}
//		} else {
//			logger.info("没有立即购买商品！");
//			return new ResultInfo<Boolean>(new FailInfo("没有立即购买商品！"));
//		}
//		return new ResultInfo<>(Boolean.TRUE);
//	}

}
