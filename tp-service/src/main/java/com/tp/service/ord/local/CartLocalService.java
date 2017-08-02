package com.tp.service.ord.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.ord.CartConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.CartConstant.ShowCartTab;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.ErrorCodeType;
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
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.result.sup.SupplierResult;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.mmp.groupbuy.IGroupbuyInfoService;
import com.tp.service.ord.ICartService;
import com.tp.service.ord.local.ICartLocalService;
import com.tp.service.ord.remote.ICheckTopicRemoteService;
import com.tp.service.prd.IItemService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.JsonFormatUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 购物车服务
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class CartLocalService implements ICartLocalService {

	private static final Logger logger = LoggerFactory.getLogger(CartLocalService.class);

	@Autowired
	private ICartService cartService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private ITopicService topicService;
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	@Autowired
	private ICheckTopicRemoteService checkTopicRemoteService;
	@Autowired
	private IPurchasingManagementService purchasingManagementService;
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	@Autowired
	private IGroupbuyInfoService groupbuyInfoService;

	@Override
	public CartDTO findMemberCart(Long memberId, int cartType) {
		return loadCart(memberId, cartType);
	}
	@Override
	public int getCartQuantity(Long memberId) {
		// 查询会员所有购物车行
		int totalQuantity = 0;
		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);
		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
			for (CartLineSimpleDTO cartLineSimple : cartLineSimpleList) {
				totalQuantity += cartLineSimple.getQuantity();
			}
		}
		return totalQuantity;
	}
	
	@Override
	public int getCartQuantityByCartType(Long memberId, int cartType) {
		// 查询会员所有购物车行
		int totalQuantity = 0;
		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);
		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
			for (CartLineSimpleDTO cartLineSimple : cartLineSimpleList) {
				if(cartLineSimple.getType().intValue()==cartType){
					totalQuantity += cartLineSimple.getQuantity();
				}
			}
		}
		return totalQuantity;
	}

	@Override
	public String showCartTab(Long memberId) {
		int totalQuantity1 = 0;
		int totalQuantity2 = 0;
		String showFlag = "";
		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);
		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
			for (CartLineSimpleDTO cartLineSimple : cartLineSimpleList) {
				if(cartLineSimple.getType().intValue()==CartConstant.TYPE_COMMON){
					totalQuantity1 += cartLineSimple.getQuantity();
				}else{
					totalQuantity2 += cartLineSimple.getQuantity();
				}
			}
		}
		
		if(totalQuantity1 > 0 && totalQuantity2 > 0){
			showFlag = ShowCartTab.ALL.cnName;
		}else if(totalQuantity2 > 0){
			showFlag = ShowCartTab.SEA.cnName;
		}else{
			showFlag = ShowCartTab.COMMON.cnName;
		}
		
		return showFlag;
	}
	
//	@Override
//	public ResultInfo<CartLineSimpleDTO> addToCart(CartLineDTO cartLine, Long memberId, String ip, int platformType) {
//		CartLineSimpleDTO lineDTO = new CartLineSimpleDTO();
//
//		/* 获取该用户所有购物车行 */
//		List<CartLineSimpleDTO> lineList = getCartLineFromRedis(memberId);
//
//		int skuTotal = cartLine.getQuantity(); // 该商品的总购买数量
//		int lineTotal = lineList.size(); // 购物车行数
//
//		/* 该商品若已在购物车中，则为修改购物车行商品数量，反之，则为新增购物车行 */
//		boolean isContained = isContained(cartLine.getSkuCode(), cartLine.getTopicId(), lineList);
//		if (isContained) {
//			for (CartLineSimpleDTO line : lineList) {
//				if (cartLine.getSkuCode().equals(line.getSkuCode()) && cartLine.getTopicId().equals(line.getTopicId())) {
//					lineDTO = line;
//					skuTotal += line.getQuantity();
//					break;
//				}
//			}
//		} else {
//			lineTotal++;
//		}
//
//		ResultInfo<Boolean> resultInfo = addCartValidate(cartLine, memberId, ip, platformType, skuTotal, lineTotal);
//		if(!resultInfo.success){
//			return new ResultInfo<CartLineSimpleDTO>(resultInfo.msg);
//		}
//
//		/* 所有检查通过，加入购物车 */
//		if (isContained) {
//			lineDTO.setQuantity(cartLine.getQuantity() + lineDTO.getQuantity());
//		} else {
//			CartLineSimpleDTO line = new CartLineSimpleDTO();
//
//			// 根据sku获取商品详情
//			List<String> skuCodeList = new ArrayList<String>();
//			skuCodeList.add(cartLine.getSkuCode());
//			List<ItemResultDto> itemList = itemService.getSkuList(skuCodeList);
//			if (CollectionUtils.isNotEmpty(itemList)) {
//				line.setSelected(true);
//				line.setMemberId(memberId);
//				line.setQuantity(cartLine.getQuantity());
//				line.setSkuCode(cartLine.getSkuCode());
//				line.setTopicId(cartLine.getTopicId());
//				line.setAreaId(cartLine.getAreaId());
//				line.setType(itemList.get(0).getWavesSign());
//				lineList.add(line);
//				lineDTO = line;
//			}
//		}
//		cartService.insertCartLineSimpleDTOList(memberId, lineList);
//		return new ResultInfo<CartLineSimpleDTO>(lineDTO);
//	}

	@Override
	public CartDTO loadCart(Long memberId, int cartType) {
		logger.info("此方法已注");
		return null;
	}
//	public CartDTO loadCart(Long memberId, int cartType) {
//		// 初始化CartDTO
//		CartDTO cartDTO = new CartDTO();
//		CartDTO returnCartDTO = new CartDTO();
//		// 初始化CartLineDTO列表
//		List<CartLineDTO> cartLineDTOList = new ArrayList<CartLineDTO>();
//		// 查询会员所有购物车行
//		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);
//		
//		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
//			// 根据cartType过滤购物车
//			List<CartLineSimpleDTO> cartLineSimpleList_sub = new ArrayList<CartLineSimpleDTO>();
//			for (CartLineSimpleDTO cartLineSimpleFilter : cartLineSimpleList) {
//				if (cartLineSimpleFilter.getType().intValue() == cartType) {
//					cartLineSimpleList_sub.add(cartLineSimpleFilter);
//				}
//				cartLineSimpleFilter.setMemberId(memberId);
//			}
//
//			// 根据skuCodeList调用商品接口获取商品列表详情信息
//			List<String> skuCodeList = new ArrayList<String>();
//			List<SkuInventoryQuery> storageQueryList = new ArrayList<SkuInventoryQuery>();
//			List<ItemInventoryDTO> itemInventoryList = new ArrayList<ItemInventoryDTO>();
//			for (CartLineSimpleDTO cartLineSimpleDTO : cartLineSimpleList_sub) {
//				// 组装查询商品列表skuCodeList
//				skuCodeList.add(cartLineSimpleDTO.getSkuCode());
//
//				// 组装查询库存列表storageQueryList
//				SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
//				skuInventoryQuery.setApp(App.PROMOTION);
//				skuInventoryQuery.setBizId(cartLineSimpleDTO.getTopicId().toString());
//				skuInventoryQuery.setSku(cartLineSimpleDTO.getSkuCode());
//				storageQueryList.add(skuInventoryQuery);
//				
//				// 组装查询仓库id列表itemInventoryList
//				ItemInventoryDTO itemInventoryDTO = new ItemInventoryDTO();
//				itemInventoryDTO.setTopicId(cartLineSimpleDTO.getTopicId());
//				itemInventoryDTO.setSkuCode(cartLineSimpleDTO.getSkuCode());
//				itemInventoryList.add(itemInventoryDTO);
//			}
//			
//			List<ItemResultDto> itemList = itemService.getSkuList(skuCodeList);
//
//			if (CollectionUtils.isNotEmpty(itemList)) {
//				Map<String, ItemResultDto> itemMap = new HashMap<String, ItemResultDto>();
//				Map<CartLineSimpleDTO, CartLineDTO> returnCartLineMap = new HashMap<CartLineSimpleDTO, CartLineDTO>();
//				List<String> validateSkuList = new ArrayList<String>();
//				List<Long> supplierIdList = new ArrayList<Long>();
//				
//				for (ItemResultDto item : itemList) {
//					// 获取有效skuList
//					if (item.getStatus() == CartConstant.ITEM_STATUS_1) {
//						validateSkuList.add(item.getSku());
//					}
//					// 组装成itemMap
//					itemMap.put(item.getSku(), item);
//					
//					// 组装供应商id列表
//					supplierIdList.add(item.getSupplierId());
//					
//				}
//
//				// 海淘添加
//				Map<Long, Long> freightTemplateMap = new HashMap<Long, Long>();
//				Map<Long,SupplierInfo> supplierInfoMap = new HashMap<Long,SupplierInfo>();
//				Map<String, ItemInventoryDTO> itemInventoryMap =  new HashMap<String, ItemInventoryDTO>();
//				if(CartConstant.TYPE_SEA==cartType){
//					// 根据供应商id列表，获取商家运费模板
//					try {
//						SupplierResult supplierResult = purchasingManagementService.getSuppliersByIds(supplierIdList);
//						if(supplierResult!=null && CollectionUtils.isNotEmpty(supplierResult.getSupplierInfoList())){
//							for(SupplierInfo supplier : supplierResult.getSupplierInfoList()){
//								freightTemplateMap.put(supplier.getId(), supplier.getFreightTemplateId());
//								supplierInfoMap.put(supplier.getId(), supplier);
//							}
//						}
//					} catch (Exception e) {
//						logger.error("根据supplierIdList: {} 获取供应商信息失败！", JSONArray.fromObject(supplierIdList).toString());
//						throw new OrderServiceException(e);					
//					}
//					
//					// 根据商品sku、topicId获取仓库ID和通关渠道
//					List<ItemSkuArt> checkList = new ArrayList<ItemSkuArt>();
//					List<Long> seaChannelIdList = new ArrayList<Long>();
//					List<ItemInventoryDTO> returnItemInventoryList = topicService.queryItemInventory(itemInventoryList);
//					
//					if (logger.isInfoEnabled()) {
//						logger.info("根据sku,topicId获取仓库id和通关渠道list：{}", JSONArray.fromObject(returnItemInventoryList).toString());
//					}
//					
//					if(CollectionUtils.isNotEmpty(returnItemInventoryList)){
//						for(ItemInventoryDTO itemInventoryDTO:returnItemInventoryList){
//							ItemSkuArt skuArt = new ItemSkuArt();
//							skuArt.setSku(itemInventoryDTO.getSkuCode());
//							skuArt.setBondedArea(itemInventoryDTO.getBondedArea());
//							checkList.add(skuArt);
//							if(itemInventoryDTO.getBondedArea() != null)
//							seaChannelIdList.add(itemInventoryDTO.getBondedArea());
//						}
//					}
//					
//					// 根据seaChannelIdList查询BASE
//					List<ClearanceChannels> seaChanneList = clearanceChannelsService.getClearanceChannelsListByIds(seaChannelIdList);
//					
//					if (logger.isInfoEnabled()) {
//						logger.info("根据seaChannelIdList查询BASE返回数据：{}", JSONArray.fromObject(seaChanneList).toString());
//					}
//					
//					if(CollectionUtils.isNotEmpty(seaChanneList)){
//						for(ClearanceChannels clearanceChannels:seaChanneList){
//							for(ItemInventoryDTO itemInventoryDTO:returnItemInventoryList){
//								if(clearanceChannels.getId().equals(itemInventoryDTO.getBondedArea())){
//									itemInventoryDTO.setBondedAreaName(clearanceChannels.getName());
//								}
//							}
//						}
//					}
//					
//					// 查询商品海关信息,重新组装returnItemInventoryList
//					if(CollectionUtils.isNotEmpty(checkList)){
//						List<ItemSkuArt> checkReturn = itemService.checkBoundedInfoForSales(checkList);
//						if(CollectionUtils.isNotEmpty(checkReturn)){
//							for(ItemSkuArt skuArt:checkReturn){
//								for(ItemInventoryDTO itemInventoryDTO:returnItemInventoryList){
//									if(itemInventoryDTO.getSkuCode().equals(skuArt.getSku()) && itemInventoryDTO.getBondedArea()!=null && itemInventoryDTO.getBondedArea().equals(skuArt.getBondedArea())){
//										itemInventoryDTO.setArticleNumber(skuArt.getArticleNumber());
//									}
//								}
//							}
//						}
//					}
//					
//					// 根据returnItemInventoryList组装itemInventoryMap
//					for(ItemInventoryDTO itemInventoryDTO:returnItemInventoryList){
//						itemInventoryMap.put(itemInventoryDTO.getTopicId() + "-" + itemInventoryDTO.getSkuCode(), itemInventoryDTO);
//					}
//					
//				}
//				
//				// 根据商品列表获取可用库存
//				Map<String, Integer> inventoryMap = inventoryQueryService.batchSelectInventory(storageQueryList);
//				if (logger.isInfoEnabled()) {
//					logger.info("调用库存接口返回商品可用库存,inventoryMap：{}", JSONObject.fromObject(inventoryMap).toString());
//				}
//
//				for (CartLineSimpleDTO cartLineSimpleDTO : cartLineSimpleList_sub) {
//					// 根据返回商品list信息和缓存商品封装CartLineDTO
//					CartLineDTO cartLineDTO = new CartLineDTO();
//					cartLineBuild(itemMap, inventoryMap, freightTemplateMap, itemInventoryMap, cartLineSimpleDTO, cartLineDTO,supplierInfoMap);
//
//					// 添加到list
//					cartLineDTOList.add(cartLineDTO);
//				}
//
//				if (CollectionUtils.isNotEmpty(cartLineDTOList)) {
//					// 对cartLineDTOList 按加入购物车时间进行降序排列
//					Collections.sort(cartLineDTOList, new Comparator<CartLineDTO>() {
//						public int compare(CartLineDTO cl1, CartLineDTO cl2) {
//							if (cl1.getCreateTime() == null || cl2.getCreateTime() == null) {
//								return 0;
//							}
//
//							return cl2.getCreateTime().compareTo(cl1.getCreateTime());
//						}
//					});
//
//					// 根据cartType封装不同的购物车数据
//					if (CartConstant.TYPE_SEA == cartType) {//海淘数据封装
//						
//						// 海淘数据分组
//						Map<String, List<CartLineDTO>> seaMap = new LinkedHashMap<String, List<CartLineDTO>>();
//						Map<String, String> supplierMap = new LinkedHashMap<String, String>();
//						for (CartLineDTO line : cartLineDTOList) {
//							line.setMemberId(memberId);
//							List<CartLineDTO> lineList = seaMap.get(line.getSupplierId().toString());
//							if (null == lineList) {
//								lineList = new ArrayList<>();
//								lineList.add(line);
//								seaMap.put(line.getSupplierId().toString(), lineList);
//								
//								supplierMap.put(line.getSupplierId().toString(), line.getSupplierName());
//							} else {
//								lineList.add(line);
//							}
//						}
//						
//						// 封装CartDTO
//						cartDTO.setCartType(cartType);
//						cartDTO.setSeaMap(seaMap);
//						cartDTO.setSupplierMap(supplierMap);
//						cartDTO.setValidateSkuList(validateSkuList);
//						
//						// 促销模块封装CartDTO;
//						returnCartDTO = topicService.cartValidate(cartDTO);
//
//						//删除专场中不存在的商品
//						if(returnCartDTO!= null && returnCartDTO.getSeaMap()!= null ){
//							for(String key:returnCartDTO.getSeaMap().keySet()){
//								List<CartLineDTO> cartLineDTOs = returnCartDTO.getSeaMap().get(key);
//								if(CollectionUtils.isNotEmpty(cartLineDTOs)){
//									Iterator<CartLineDTO> iterator = cartLineDTOs.iterator();
//									while (iterator.hasNext()){
//										CartLineDTO cartLineDTO = iterator.next();
//										if(cartLineDTO != null && cartLineDTO.getTopicItemInfo()==null){
//											iterator.remove();
//											Iterator<CartLineSimpleDTO> cartLineSimpleIterator =cartLineSimpleList_sub.iterator();
//												while (cartLineSimpleIterator.hasNext()){
//													CartLineSimpleDTO cartLineSimple = cartLineSimpleIterator.next();
//													if(cartLineSimple != null && StringUtils.equals(cartLineSimple.getSkuCode(),cartLineDTO.getSkuCode())&&
//															cartLineSimple.getTopicId() != null &&cartLineSimple.getTopicId().equals(cartLineDTO.getTopicId())){
//														cartLineSimpleIterator.remove();
//													}
//
//												}
//											logger.error("CART_ITEM_NOT_FIND_IN_TOPIC_ITEM,DEL_FROM_CART.INFO:"+ JSON.toJSONString(cartLineDTO));
//											this.deleteCart(cartLineDTO.getSkuCode(),cartLineDTO.getTopicId(),memberId);
//											}
//										}
//									}
//								}
//							}
//
//
//						if (logger.isInfoEnabled()) {
//							logger.info("调用促销信息返回数据 returnCartDTO: {}", JsonFormatUtils.format(returnCartDTO));
//						}
//						
//						// 封装最终显示的CartDTO
//						if (returnCartDTO != null && !returnCartDTO.getSeaMap().isEmpty()) {
//							for (Map.Entry<String, List<CartLineDTO>> entry : returnCartDTO.getSeaMap().entrySet()) {
//								// 对促销模块返回的CartDTO中的 lineList进行重新排序(区分有效无效商品列表)
//								List<CartLineDTO> validateLineList = new ArrayList<CartLineDTO>();
//								List<CartLineDTO> inValidateLineList = new ArrayList<CartLineDTO>();
//								List<CartLineDTO> allLineList = new ArrayList<CartLineDTO>();
//								
//								for (CartLineDTO cartLine : entry.getValue()) {
//									if (cartLine.getSkuStatus().intValue() == CartConstant.ITEM_STATUS_1 && cartLine.getTopicItemInfo().getValidate()) {
//										validateLineList.add(cartLine);
//									} else {
//										inValidateLineList.add(cartLine);
//									}
//								}
//								
//								allLineList.addAll(validateLineList);
//								allLineList.addAll(inValidateLineList);
//								
//								seaMap.put(entry.getKey(), allLineList);
//							}
//							
//							returnCartDTO.setSeaMap(seaMap);
//							
//							// 根据促销返回cartDTO封装页面展示
//							returnCartBuild(returnCartDTO, cartLineSimpleList_sub, returnCartLineMap, cartType);
//							
//						} else {
//							if (logger.isInfoEnabled()) {
//								logger.info("根据cartDTO: {} 获取促销信息失败！", JSONObject.fromObject(cartDTO).toString());
//							}
//						}
//						
//					}else{ //西客商城数据封装
//						
//						// 封装CartDTO
//						cartDTO.setCartType(cartType);
//						cartDTO.setLineList(cartLineDTOList);
//						cartDTO.setValidateSkuList(validateSkuList);
//
//						// 促销模块封装CartDTO
//						returnCartDTO = topicService.cartValidate(cartDTO);
//						if (logger.isInfoEnabled()) {
//							logger.info("调用促销信息返回数据 returnCartDTO: {}", JsonFormatUtils.format(returnCartDTO));
//						}
//
//						// 封装最终显示的CartDTO
//						if (returnCartDTO != null && CollectionUtils.isNotEmpty(returnCartDTO.getLineList())) {
//							// 对促销模块返回的CartDTO中的 lineList进行重新排序(区分有效无效商品列表)
//							List<CartLineDTO> validateLineList = new ArrayList<CartLineDTO>();
//							List<CartLineDTO> inValidateLineList = new ArrayList<CartLineDTO>();
//							List<CartLineDTO> allLineList = new ArrayList<CartLineDTO>();
//
//							List<CartLineDTO> returnLinelist = returnCartDTO.getLineList();
//							for (CartLineDTO returnCartLine : returnLinelist) {
//								if (returnCartLine.getSkuStatus().intValue() == CartConstant.ITEM_STATUS_1 && returnCartLine.getTopicItemInfo().getValidate()) {
//									validateLineList.add(returnCartLine);
//								} else {
//									inValidateLineList.add(returnCartLine);
//								}
//							}
//							allLineList.addAll(validateLineList);
//							allLineList.addAll(inValidateLineList);
//
//							returnCartDTO.setLineList(allLineList);
//
//							// 根据促销返回cartDTO封装页面展示
//							returnCartBuild(returnCartDTO, cartLineSimpleList_sub, returnCartLineMap, cartType);
//						} else {
//							if (logger.isInfoEnabled()) {
//								logger.info("根据cartDTO: {} 获取促销信息失败！", JSONObject.fromObject(cartDTO).toString());
//							}
//						}
//						
//					}
//
//				}
//
//			} else {
//				if (logger.isInfoEnabled()) {
//					logger.info("根据skuCodeList: {} 获取商品列表信息为空！", JSONArray.fromObject(skuCodeList).toString());
//				}
//			}
//
//		} else {
//			logger.info("会员: {} 从redis获取购物车列表信息为空！", memberId);
//		}
//
//		return returnCartDTO;
//	}

	@Override
	public List<TopicItemInfoResult> loadLastestSingleTopic() {
		List<TopicItemInfoResult> listResult = new ArrayList<TopicItemInfoResult>();
		try {
			listResult = topicService.queryLastestSingleTopic(3);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return listResult;
	}

	@Override
	public void selectCart(String skuCode, Long topicId, Boolean selected, Long memberId) {
		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);
		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
			// 更新redis selected状态
			for (CartLineSimpleDTO cartLineSimpleDTO : cartLineSimpleList) {
				if (cartLineSimpleDTO.getSkuCode() != null && cartLineSimpleDTO.getSkuCode().equals(skuCode) && cartLineSimpleDTO.getTopicId() != null
						&& cartLineSimpleDTO.getTopicId().equals(topicId)) {
					cartService.updateCartLineSimpleDTOBySelected(cartLineSimpleDTO, selected, memberId);
				}
			}
		} else {
			logger.info("会员: {} 从redis获取购物车列表信息为空！", memberId);
		}
	}

	@Override
	public void selectAllCart(Boolean selectedAll, int cartType, Long memberId) {
		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);

		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
			// 更新redis selected状态
			for (CartLineSimpleDTO cartLineSimpleDTO : cartLineSimpleList) {
				if(cartLineSimpleDTO.getType().intValue()==cartType){
					cartService.updateCartLineSimpleDTOBySelected(cartLineSimpleDTO, selectedAll, memberId);
				}
			}
		} else {
			logger.info("会员: {} 从redis获取购物车列表信息为空！", memberId);
		}
	}

	@Override
	public void updateCart(CartLineDTO cartLine, Long memberId, int platformType) {

		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);

		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
			// 更新redis 购买商品数量
			for (CartLineSimpleDTO cartLineSimpleDTO : cartLineSimpleList) {
				if (cartLineSimpleDTO.getSkuCode() != null && cartLineSimpleDTO.getSkuCode().equals(cartLine.getSkuCode()) && cartLineSimpleDTO.getTopicId() != null
						&& cartLineSimpleDTO.getTopicId().equals(cartLine.getTopicId())) {
					cartService.updateCartLineSimpleDTOByQuantity(cartLineSimpleDTO, cartLine.getQuantity(), memberId);
				}
			}
		} else {
			logger.info("会员: {} 从redis获取购物车列表信息为空！", memberId);
		}
	}

	@Override
	public void deleteCart(String skuCode, Long topicId, Long memberId) {
		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);

		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
			for (CartLineSimpleDTO cartLineSimpleDTO : cartLineSimpleList) {
				if (cartLineSimpleDTO.getSkuCode() != null && cartLineSimpleDTO.getSkuCode().equals(skuCode) && cartLineSimpleDTO.getTopicId() != null
						&& cartLineSimpleDTO.getTopicId().equals(topicId)) {
					cartService.deleteCartLineSimpleDTO(cartLineSimpleDTO, memberId);
				}
			}
		} else {
			logger.info("会员: {} 从redis获取购物车列表信息为空！", memberId);
		}
	}
	
	@Override
	public void deleteAllCart(int cartType, Long memberId) {
		List<CartLineSimpleDTO> cartLineSimpleList = getCartLineFromRedis(memberId);

		if (CollectionUtils.isNotEmpty(cartLineSimpleList)) {
			for (CartLineSimpleDTO cartLineSimpleDTO : cartLineSimpleList) {
				if (cartLineSimpleDTO.getType() != null && (cartLineSimpleDTO.getType().intValue()==cartType)) {
					cartService.deleteCartLineSimpleDTO(cartLineSimpleDTO, memberId);
				}
			}
		} else {
			logger.info("会员: {} 从redis获取购物车列表信息为空！", memberId);
		}
	}	

//	@Override
//	public ResultInfo<Boolean> checkCart(Long memberId, String ip, Integer platform, List<CartLineDTO> cartLineList) {
//		List<String> itemQueryList = new ArrayList<String>();
//		List<SkuInventoryQuery> storageQueryList = new ArrayList<SkuInventoryQuery>();
//		List<TopicItemCartQuery> topicQueryList = new ArrayList<TopicItemCartQuery>();
//		if (CollectionUtils.isNotEmpty(cartLineList)) {
//			for (CartLineDTO cartLineDTO : cartLineList) {
//				// 封装item验证list
//				itemQueryList.add(cartLineDTO.getSkuCode());
//
//				// 封装 storage验证list
//				SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
//				skuInventoryQuery.setApp(App.PROMOTION);
//				skuInventoryQuery.setBizId(cartLineDTO.getTopicId().toString());
//				skuInventoryQuery.setQuantity(cartLineDTO.getQuantity());
//				skuInventoryQuery.setSku(cartLineDTO.getSkuCode());
//
//				storageQueryList.add(skuInventoryQuery);
//
//				// 封装topic验证list
//				TopicItemCartQuery topicItemCartQuery = new TopicItemCartQuery();
//
//				topicItemCartQuery.setSku(cartLineDTO.getSkuCode());
//				topicItemCartQuery.setPlatform(platform);
//				topicItemCartQuery.setArea(cartLineDTO.getAreaId());
//				topicItemCartQuery.setTopicId(cartLineDTO.getTopicId());
//				topicItemCartQuery.setAmount(cartLineDTO.getQuantity());
//
//				topicItemCartQuery.setMemberId(memberId);
//				topicItemCartQuery.setUip(ip);
//
//				topicQueryList.add(topicItemCartQuery);
//			}
//
//			if (CollectionUtils.isNotEmpty(itemQueryList)) {
//				/* 该商品的有效性检查 */
//				try {
//					ResultInfo<?> rs = itemService.checkItemList(itemQueryList);
//					if (!rs.success) {
//						return new ResultInfo<Boolean>(rs.msg);
//					}
//				} catch (ItemServiceException e) {
//					return new ResultInfo<Boolean>(new FailInfo("选择的部分商品失效，无法购买"));
//				}
//
//				/* 获取商品限购信息 */
//				List<TopicPolicyDTO> topicPolicyList = new ArrayList<TopicPolicyDTO>();
//				try {
//					topicPolicyList = topicService.queryTopicPolicyInfo(topicQueryList);
//				} catch (Exception e) {
//					logger.error("根据skuCodeList: {} 获取商品限购信息失败！", JSONArray.fromObject(topicQueryList).toString());
//					throw new OrderServiceException(OrderErrorCodes.GET_TOPIC_INFO_ERROR);
//				}
//
//				/* 本地限购检查 */
//				if (CollectionUtils.isNotEmpty(topicPolicyList)) {
//					for (TopicPolicyDTO topicPolicy : topicPolicyList) {
//						ResultInfo<Boolean> returnData = checkTopicRemoteService.checkTopicPolicy(topicPolicy);
//						if (!returnData.success) {
//							return returnData;
//						}
//					}
//				}
//
//				/* 促销限购检查 */
//				ResultInfo<Boolean> topicRM = topicService.validateTopicItemList(topicQueryList);
//				if(!topicRM.success){
//					return topicRM;
//				}
//
//				/* 该商品的库存检查 */
//				Map<String,ResultInfo<String>> rmm = inventoryQueryService.checkAllInventory(storageQueryList);
//
//				for (ResultInfo<String> resultMessage : rmm.values()) {
//					if(!resultMessage.success){
//						return new ResultInfo<>(resultMessage.msg);
//					}
//				}
//			} else {
//				logger.info("购物车中没有商品被选中！");
//				return new ResultInfo<>(new FailInfo("购物车中没有商品被选中！"));
//			}
//		} else {
//			logger.info("购物车中没有商品被选中！");
//			return new ResultInfo<>(new FailInfo("购物车中没有商品被选中！"));
//		}
//		return new ResultInfo<>(Boolean.TRUE);
//	}

	/**
	 * <pre>
	 * 添加购物车check
	 * </pre>
	 * 
	 * @param cartLine
	 * @param memberId
	 * @param platformType
	 * @param skuTotal
	 * @param lineTotal
	 * 
	 * @return
	 */
//	private ResultInfo<Boolean> addCartValidate(CartLineDTO cartLine, Long memberId, String ip, Integer platformType, Integer skuTotal, Integer lineTotal) {
//		/* 购物车不能超过99行 */
//		if (lineTotal > CartConstant.MAX_LINE_QUANTITY) {
//			logger.info("购物车行数超过最大限制数 :{}", CartConstant.MAX_LINE_QUANTITY);
//			return new ResultInfo<Boolean>(new FailInfo("购物车行数超过最大限制数"));
//		}
//
//		/* 单个商品购买数量不能超过99个 */
//		if (skuTotal > CartConstant.MAX_SKU_QUANTITY) {
//			logger.info("单个商品(skuCode: {})购买数量超过最大限制数:{}", cartLine.getSkuCode(), CartConstant.MAX_SKU_QUANTITY);
//			return new ResultInfo<Boolean>(new FailInfo(String.format("单个商品[%s]购买数量超过最大限制数%d", cartLine.getSkuCode(), CartConstant.MAX_SKU_QUANTITY)));
//		}
//
//		/* 该商品的有效性检查 */
//		try {
//			ResultInfo<Boolean> rm = itemService.checkItem(cartLine.getSkuCode());
//			if (!rm.success) {
//				logger.info("商品skuCode: {},返回信息：{}", cartLine.getSkuCode(), rm.getMsg().getDetailMessage());
//				return new ResultInfo<Boolean>(new FailInfo(String.format("商品skuCode: %s,返回信息：%s", cartLine.getSkuCode(), rm.getMsg().getDetailMessage())));
//			}
//		} catch (ItemServiceException e) {
//			return new ResultInfo<Boolean>(new FailInfo("商品已失效，加入购物车失败",OrderErrorCodes.ADDCART_VALIDATE_SKU_ERROR));
//		}
//
//		/* 获取商品限购信息 */
//		List<TopicPolicyDTO> resultList = getTopicPolicyInfo(cartLine, memberId, ip, platformType, skuTotal);
//		if (CollectionUtils.isNotEmpty(resultList)) {
//			TopicPolicyDTO topicPolicy = resultList.get(0);
//			/* 本地限购检查 */
//			ResultInfo<Boolean> returnData = checkTopicRemoteService.checkTopicPolicy(topicPolicy);
//
//			if (!returnData.success) {
//				FailInfo failInfo = returnData.getMsg();
//				if (failInfo.getCode() != null && failInfo.getCode() == OrderErrorCodes.PROMOTION_USERID_LIMIT_POLICY) {
//					return new ResultInfo<Boolean>(new FailInfo("此用户已达到购买上限 ",OrderErrorCodes.LIMIT_USER_ERROR));
//				} else if (failInfo.getCode() != null && failInfo.getCode() == OrderErrorCodes.PROMOTION_IP_LIMIT_POLICY) {
//					return new ResultInfo<Boolean>(new FailInfo("此IP已达到购买上限",OrderErrorCodes.LIMIT_IP_ERROR));
//				} else {
//					return new ResultInfo<Boolean>(new FailInfo("找不到专题对应的商品 ",OrderErrorCodes.LIMIT_IP_ERROR));
//				}
//			} else {
//				/* 促销限购检查 */
//				ResultInfo<Boolean> topicRM = validateSingleTopicItem(cartLine, skuTotal, platformType, memberId, ip);
//				if (!topicRM.success) {
//					logger.info(topicRM.getMsg().getMessage());
//					FailInfo failInfo = topicRM.getMsg();
//					Integer errorCode = failInfo.getCode();
//					if (errorCode == ErrorCodeType.LIMIT_AMOUNT.ordinal()) {
//						return new ResultInfo<Boolean>(new FailInfo("商品购买数量超过限购数量",OrderErrorCodes.QUANTITY_SKU_RESTRICTION_ERROR));
//					} else if (errorCode== ErrorCodeType.AREA.ordinal()) {
//						return new ResultInfo<Boolean>(new FailInfo("专题不适用当前地区",OrderErrorCodes.AREA_TOPIC_CONSISTENCY__ERROR));
//					} else if (errorCode == ErrorCodeType.PLATFORM.ordinal()) {
//						return new ResultInfo<Boolean>(new FailInfo("专题不适用当前平台",OrderErrorCodes.PLATFORM_TOPIC_CONSISTENCY__ERROR));
//					} else if (errorCode == ErrorCodeType.REGISTER_TIME.ordinal()) {
//						return new ResultInfo<Boolean>(new FailInfo("注册时间不符合限购要求",OrderErrorCodes.REGISTIME_RESTRICTION_ERROR));
//					} else if (errorCode == ErrorCodeType.UID.ordinal()) {// 5002
//						return new ResultInfo<Boolean>(new FailInfo("此用户已达到购买上限",OrderErrorCodes.LIMIT_USER_ERROR));
//					} else if (errorCode == ErrorCodeType.UIP.ordinal()) {// 5003
//						return new ResultInfo<Boolean>(new FailInfo("此IP已达到购买上限",OrderErrorCodes.LIMIT_IP_ERROR));
//					} else if (errorCode == ErrorCodeType.OTHER.ordinal()) {
//						return new ResultInfo<Boolean>(new FailInfo("找不到专题对应的商品",OrderErrorCodes.TOPIC_SKU_CONSISTENCY_ERROR));
//					} else if (errorCode == ErrorCodeType.OVERDUE.ordinal()) {
//						return new ResultInfo<Boolean>(new FailInfo("商品已失效，加入购物车失败",OrderErrorCodes.ADDCART_VALIDATE_SKU_ERROR));
//					}else if (errorCode == ErrorCodeType.LOCKED.ordinal()) {	// 促销商品锁定，显示库存不足
//						return new ResultInfo<Boolean>(new FailInfo("商品有效库存不足",OrderErrorCodes.QUANTITY_SKU_VALIDATE_ERROR));
//					}else{
//						return topicRM;
//					}
//				}
//			}
//		}
//
//		/* 该商品的库存检查 */
//		boolean stockCheck = inventoryQueryService.checkInventoryQuantity(App.PROMOTION, cartLine.getTopicId().toString(), cartLine.getSkuCode(), skuTotal);
//		if (!stockCheck) {
//			return new ResultInfo<Boolean>(new FailInfo(String.format("商品skuCode: %s 库存验证失败！",cartLine.getSkuCode())));
//		}
//		return new ResultInfo<Boolean>(Boolean.TRUE);
//	}

	private List<TopicPolicyDTO> getTopicPolicyInfo(CartLineDTO cartLine, Long memberId, String ip, int platformType, int skuTotal) {
		TopicItemCartQuery query = new TopicItemCartQuery();
		List<TopicItemCartQuery> queryList = new ArrayList<TopicItemCartQuery>();

		query.setPlatform(platformType);
		query.setArea(cartLine.getAreaId());
		query.setTopicId(cartLine.getTopicId());
		query.setSku(cartLine.getSkuCode());
		query.setAmount(skuTotal);
		query.setMemberId(memberId);
		query.setUip(ip);

		queryList.add(query);

		try {
			List<TopicPolicyDTO> topicPolicyList = topicService.queryTopicPolicyInfo(queryList);
			return topicPolicyList;
		} catch (Exception e) {
			logger.error("商品skuCode: {}获取商品限购信息失败！", cartLine.getSkuCode());
			throw new OrderServiceException(OrderErrorCodes.GET_TOPIC_INFO_ERROR);
		}
	}

	/**
	 * <pre>
	 * 查询会员所有购物车行
	 * </pre>
	 * 
	 * @param memberId
	 * @return
	 */
	public List<CartLineSimpleDTO> getCartLineFromRedis(Long memberId) {
		return cartService.selectCartLineDTOListByMemberId(memberId);
	}

	/**
	 * 购物车中是否包含该sku
	 * 
	 * @param skuCode
	 * @param lineList
	 * @return
	 */
	private boolean isContained(String skuCode, Long topicId, List<CartLineSimpleDTO> lineList) {
		CartLineSimpleDTO line = new CartLineSimpleDTO();
		line.setSkuCode(skuCode);
		line.setTopicId(topicId);
		return lineList.contains(line);
	}

	/**
	 * @param skuCode
	 * @param quantity
	 * @param platformId
	 * @param areaId
	 * @param topicId
	 * @return
	 */
	private ResultInfo<Boolean> validateSingleTopicItem(CartLineDTO cartLine, Integer quantity, Integer platformType, Long memberId, String ip) {
		TopicItemCartQuery query = new TopicItemCartQuery();

		query.setPlatform(platformType);
		query.setArea(cartLine.getAreaId());
		query.setTopicId(cartLine.getTopicId());
		query.setSku(cartLine.getSkuCode());
		query.setAmount(quantity);
		query.setMemberId(memberId);
		query.setUip(ip);
		return topicService.validateSingleTopicItem(query);
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

		if (CollectionUtils.isNotEmpty(specList)) {
			Collections.sort(specList, new Comparator<ItemDetailSpec>() {
				public int compare(ItemDetailSpec cp1, ItemDetailSpec cp2) {
					return cp1.getSort().compareTo(cp2.getSort());
				}
			});
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
	 * 根据返回item信息封装cartLineDTO
	 * </pre>
	 * 
	 * @param itemMap
	 * @param freightTemplateMap
	 * @param itemMap
	 * @param itemInventoryMap
	 * @param cartLineDTO
	 */
	private void cartLineBuild(Map<String, ItemResultDto> itemMap, Map<String, Integer> inventoryMap, Map<Long, Long> freightTemplateMap, Map<String, ItemInventoryDTO> itemInventoryMap, CartLineSimpleDTO cartLineSimpleDTO, CartLineDTO cartLineDTO,Map<Long,SupplierInfo> supplierInfoMap) {
		// 商品通用属性赋值
		ItemResultDto itemResult = itemMap.get(cartLineSimpleDTO.getSkuCode());
		Integer stock = inventoryMap.get(cartLineSimpleDTO.getTopicId() + "-" + cartLineSimpleDTO.getSkuCode());
		
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

			if(MapUtils.isNotEmpty(supplierInfoMap)){
				SupplierInfo supplierInfo = supplierInfoMap.get(itemResult.getSupplierId());
				if(supplierInfo!=null){
					cartLineDTO.setSupplierAlias(supplierInfo.getAlias());
				}
			}
			if(CartConstant.TYPE_SEA==cartLineSimpleDTO.getType()){
				ItemInventoryDTO itemInventoryDTO = itemInventoryMap.get(cartLineSimpleDTO.getTopicId() + "-" + cartLineSimpleDTO.getSkuCode());
				cartLineDTO.setSaleType(itemResult.getWavesSign());
				cartLineDTO.setWavesSign(itemResult.getWavesSign());
				cartLineDTO.setFreightTemplateId(freightTemplateMap.get(itemResult.getSupplierId()));
				cartLineDTO.setProductCode(itemInventoryDTO.getArticleNumber());
				cartLineDTO.setSeaChannel(itemInventoryDTO.getBondedArea());
				cartLineDTO.setSeaChannelName(itemInventoryDTO.getBondedAreaName());
				cartLineDTO.setStorageType(itemInventoryDTO.getStorageType());
				cartLineDTO.setCustomsRate(itemResult.getCustomsRate());
				cartLineDTO.setExciseRate(itemResult.getExciseRate());
				cartLineDTO.setAddedValueRate(itemResult.getAddedValueRate());
			}else{
				cartLineDTO.setSaleType(itemResult.getSaleType());
				cartLineDTO.setFreightTemplateId(itemResult.getFreightTemplateId());
			}
			
			cartLineDTO.setUnit(itemResult.getUnitName());
			cartLineDTO.setRefundDays(itemResult.getReturnDays());

			cartLineDTO.setAreaId(cartLineSimpleDTO.getAreaId());
			cartLineDTO.setPlatformId(PlatformEnum.PC.getCode());
			cartLineDTO.setTopicId(cartLineSimpleDTO.getTopicId());
			
			// 封装商品销售属性
			List<SalePropertyDTO> salePropList = new ArrayList<SalePropertyDTO>();
			getItemAttribute(itemResult, salePropList);
			cartLineDTO.setSalePropertyList(salePropList);

			// 封装购物车行信息
			cartLineDTO.setSelected(cartLineSimpleDTO.getSelected());
			cartLineDTO.setCreateTime(cartLineSimpleDTO.getCreateTime());
			cartLineDTO.setQuantity(cartLineSimpleDTO.getQuantity());
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
	private void returnCartBuild(CartDTO returnCartDTO, List<CartLineSimpleDTO> cartLineSimpleList, Map<CartLineSimpleDTO, CartLineDTO> returnCartLineMap, int cartType) {
		
		if(CartConstant.TYPE_SEA == cartType){
			for (Map.Entry<String, List<CartLineDTO>> entry : returnCartDTO.getSeaMap().entrySet()) {
				for (CartLineDTO cartLine : entry.getValue()) {
					CartLineSimpleDTO mapKey1 = new CartLineSimpleDTO();
					mapKey1.setTopicId(cartLine.getTopicId());
					mapKey1.setSkuCode(cartLine.getSkuCode());

					returnCartLineMap.put(mapKey1, cartLine);
				}
			}
			subReturnCartBuild(cartLineSimpleList, returnCartLineMap);
		}else{
			for (CartLineDTO returnCartLineDTO : returnCartDTO.getLineList()) {
				CartLineSimpleDTO mapKey1 = new CartLineSimpleDTO();
				mapKey1.setTopicId(returnCartLineDTO.getTopicId());
				mapKey1.setSkuCode(returnCartLineDTO.getSkuCode());

				returnCartLineMap.put(mapKey1, returnCartLineDTO);
			}
			subReturnCartBuild(cartLineSimpleList, returnCartLineMap);
		}
		
	}

	private void subReturnCartBuild(List<CartLineSimpleDTO> cartLineSimpleList, Map<CartLineSimpleDTO, CartLineDTO> returnCartLineMap) {
		for (CartLineSimpleDTO cartLineSimpleDTO : cartLineSimpleList) {
			CartLineSimpleDTO mapKey2 = new CartLineSimpleDTO();
			mapKey2.setTopicId(cartLineSimpleDTO.getTopicId());
			mapKey2.setSkuCode(cartLineSimpleDTO.getSkuCode());

			if (returnCartLineMap.get(mapKey2) == null || returnCartLineMap.get(mapKey2).getTopicItemInfo() == null) {
				if (logger.isErrorEnabled()) {
					logger.error("购物车商品详情:{}", JSONObject.fromObject(returnCartLineMap.get(mapKey2)));
				}
			}

			CartLineDTO cartLine = returnCartLineMap.get(mapKey2);

			cartLine.setSalePrice(cartLine.getTopicItemInfo().getTopicPrice());

			cartLine.setSubTotal(BigDecimalUtil.multiply(cartLine.getTopicItemInfo().getTopicPrice(), cartLineSimpleDTO.getQuantity()).doubleValue());

			if (logger.isInfoEnabled()) {
				logger.info("购物车商品详情:{}", JSONObject.fromObject(cartLine));
			}

			if (logger.isDebugEnabled()) {
				logger.debug("加载购物车：sku为[{}]的商品，活动有效性为{}，是否锁定为{}。", 
						cartLine.getSkuCode(), cartLine.getTopicItemInfo().getValidate(), cartLine.getTopicItemInfo().getLocked());
			}
			
			if (cartLine.getSkuStatus().intValue() == CartConstant.ITEM_STATUS_1 && cartLine.getTopicItemInfo().getValidate()
					&& (cartLine.getQuantity().intValue() <= cartLine.getTopicItemInfo().getLimitAmount() && cartLine.getQuantity().intValue() <= cartLine.getStock())
					&& !cartLine.getTopicItemInfo().getLocked()) {

				cartLine.setStatus(CartConstant.CART_STATUS_0);
			} else {
				if (cartLine.getSkuStatus().intValue() != CartConstant.ITEM_STATUS_1 || !cartLine.getTopicItemInfo().getValidate()) {// 商品状态无效
					cartLine.setStatus(CartConstant.CART_STATUS_2);
				} else if (cartLine.getQuantity().intValue() > cartLine.getStock() || cartLine.getTopicItemInfo().getLocked()) {// 商品库存不足 或 已锁定
					cartLine.setStatus(CartConstant.CART_STATUS_1);
				} else if (cartLine.getQuantity().intValue() > cartLine.getTopicItemInfo().getLimitAmount()) {// 超过限购数量
					cartLine.setStatus(CartConstant.CART_STATUS_3);
				}
			}
		}
	}

	@Override
	public CartDTO getValidateCart(Long memberId, int cartType) {
		CartDTO cart = loadCart(memberId, cartType);

		int totalQuatity = 0;
		if(CartConstant.TYPE_SEA==cartType){
			Map<String, List<CartLineDTO>> seaMap =  cart.getSeaMap();
			Map<String, List<CartLineDTO>> validateSeaMap =  new LinkedHashMap<String, List<CartLineDTO>>();
			if(MapUtils.isNotEmpty(seaMap)){
				for (Map.Entry<String, List<CartLineDTO>> entry : seaMap.entrySet()) {
					
					List<CartLineDTO> validateCartList = new ArrayList<>();
					
					for(CartLineDTO cartLineDTO: entry.getValue()){
						if (cartLineDTO.getSelected()) {
							if (cartLineDTO.getSkuStatus().intValue() == CartConstant.ITEM_STATUS_1 && cartLineDTO.getTopicItemInfo().getValidate()) {
								validateCartList.add(cartLineDTO);
	
								totalQuatity += cartLineDTO.getQuantity();
							}
						}
					}
					if(CollectionUtils.isNotEmpty(validateCartList)){
						validateSeaMap.put(entry.getKey(), validateCartList);
					}
				}
			}
			cart.setSeaMap(validateSeaMap);
			cart.setQuantity(totalQuatity);
			
		}else{
			List<CartLineDTO> cartList = cart.getLineList();
			List<CartLineDTO> validateCartList = new ArrayList<>();

			if (CollectionUtils.isNotEmpty(cartList)) {
				for (CartLineDTO cartLineDTO : cartList) {
					if (cartLineDTO.getSelected()) {
						if (cartLineDTO.getSkuStatus().intValue() == CartConstant.ITEM_STATUS_1 && cartLineDTO.getTopicItemInfo().getValidate()) {
							validateCartList.add(cartLineDTO);

							totalQuatity += cartLineDTO.getQuantity();
						}
					}
				}
				cart.setLineList(validateCartList);
				cart.setQuantity(totalQuatity);
			}
		}

		return cart;
	}
}
