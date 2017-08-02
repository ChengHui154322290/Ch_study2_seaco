package com.tp.proxy.ord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.Constant.TF;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.ord.CartConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.ord.ItemInventoryDTO;
import com.tp.dto.ord.SalePropertyDTO;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.model.ord.CartItem;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.ord.compute.IAmountProxy;
import com.tp.proxy.ord.split.IOrderSplitProxy;
import com.tp.proxy.ord.validate.CartItemInfoValidateProxy;
import com.tp.proxy.stg.WarehouseProxy;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.service.IBaseService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.ord.ICartItemService;
import com.tp.service.prd.IItemService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.StringUtil;
/**
 * 购物车商品信息表代理层
 * @author szy
 *
 */
@Service
public class CartItemInfoProxy extends BaseProxy<CartItem>{

	@Autowired
	private ICartItemService cartItemService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private ISupplierInfoService supplierInfoService;
	@Autowired
	private ITopicService topicService;
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	@Autowired
	private CartItemInfoValidateProxy cartItemInfoValidateProxy;
	@Autowired
	private IOrderSplitProxy<ShoppingCartDto> orderSplitProxy;
	@Autowired
	private IAmountProxy<ShoppingCartDto> cartAmountProxy;
	@Autowired
	private WarehouseProxy warehouseProxy;
	
	@Override
	public IBaseService<CartItem> getService() {
		return cartItemService;
	}
	
	/**
	 * 组装购物车
	 * @param memberId
	 * @return
	 */
	public ShoppingCartDto initCart(Long memberId,String ip,Long areaId,Integer platformId,Long shopId){
		List<CartItem> cartItemInfoList = cartItemService.queryListByMemberId(memberId,shopId);
		ShoppingCartDto cart = new ShoppingCartDto();
		cart.setMemberId(memberId);
		cart.setIp(ip);
		cart.setAreaId(areaId);
		if(CollectionUtils.isNotEmpty(cartItemInfoList)){
			List<CartItemInfo> OrderCartItemList = new ArrayList<CartItemInfo>();
			for(CartItem cartItem:cartItemInfoList){
				CartItemInfo cartItemInfo = new CartItemInfo();
				BeanUtils.copyProperties(cartItem, cartItemInfo);
				cartItemInfo.setPlatformId(platformId);
				cartItemInfo.setAreaId(areaId);
				OrderCartItemList.add(cartItemInfo);
			}
			cart.setCartItemInfoList(OrderCartItemList);
			cart = initCart(cart);
		}
		//计算价格
		return cart;
	}
	
	/**
	 * 组装购物车
	 * @param memberId
	 * @return
	 */
	public ShoppingCartDto initCart(CartItemInfo cartItemInfo){
		ShoppingCartDto cart = new ShoppingCartDto();
		cart.setMemberId(cartItemInfo.getMemberId());
		cart.setIp(cartItemInfo.getIp());
		cart.setAreaId(cartItemInfo.getAreaId());
		List<CartItemInfo> OrderCartItemList = new ArrayList<CartItemInfo>();
		OrderCartItemList.add(cartItemInfo);
		cart.setCartItemInfoList(OrderCartItemList);
		cart = initCart(cart);
		//计算价格
		return cart;
	}
	public ShoppingCartDto initCart(ShoppingCartDto shoppingCartDto){
		List<CartItemInfo> cartItemInfoList = initCartItemInfo(shoppingCartDto.getCartItemInfoList());
		//验证失效(无库存、活动失效、商品下架、供应商作废)
		cartItemInfoList = validateCartItem(cartItemInfoList);
		shoppingCartDto.setCartItemInfoList(cartItemInfoList);
		//按有效、失效分组
		Map<Integer,List<CartItemInfo>> itemAbledMap = divideEnabledAndDisabled(cartItemInfoList);
		shoppingCartDto.setCartItemInfoList(itemAbledMap.get(TF.NO));
		shoppingCartDto.setDeleteCartItemList(itemAbledMap.get(TF.YES));
		if(CollectionUtils.isNotEmpty(cartItemInfoList)){
			//验证库存及其它
			shoppingCartDto.setCartItemInfoList(validateCartItemLimit(cartItemInfoList,shoppingCartDto));
			//把有效商品分组
			shoppingCartDto = orderSplitProxy.split(shoppingCartDto);
			//计算价格
			shoppingCartDto = cartAmountProxy.computeAmount(shoppingCartDto);
		}
		return shoppingCartDto;
	}
	public List<CartItemInfo> initCartItemInfo(List<CartItemInfo> cartItemInfoList){
		if(CollectionUtils.isNotEmpty(cartItemInfoList)){
			cartItemInfoList = initSkuInfo(cartItemInfoList);
			cartItemInfoList = initSupplierInfo(cartItemInfoList);
			cartItemInfoList = initInventory(cartItemInfoList);
			cartItemInfoList = initTopicInfo(cartItemInfoList);
			cartItemInfoList = initStock(cartItemInfoList);
		}
		return cartItemInfoList;
	}
	
	/**
	 * 组装商品信息
	 * @param cartItemInfoList
	 * @return
	 */
	public List<CartItemInfo> initSkuInfo(List<CartItemInfo> cartItemInfoList){
		List<String> skuCodeList = new ArrayList<String>();
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			skuCodeList.add(cartItemInfo.getSkuCode());
		}
		List<ItemResultDto> itemList = itemService.getSkuList(skuCodeList);
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			for(ItemResultDto itemResult:itemList){
				if(cartItemInfo.getSkuCode().equals(itemResult.getSku())){
					cartItemInfo.setBarcode(itemResult.getBarcode());// 获取商品条形码
					cartItemInfo.setSkuCode(itemResult.getSku());
					cartItemInfo.setItemName(itemResult.getMainTitle());
					cartItemInfo.setItemCode(itemResult.getSpu());
					cartItemInfo.setItemId(itemResult.getItemId());
					cartItemInfo.setItemPic(itemResult.getImageUrl());
					cartItemInfo.setListPrice(itemResult.getBasicPrice());
					cartItemInfo.setSkuStatus(itemResult.getStatus());
					cartItemInfo.setBrandId(itemResult.getBrandId());
					cartItemInfo.setBrandName(itemResult.getBrandName());
					cartItemInfo.setLargeId(itemResult.getLargeId());
					cartItemInfo.setMediumId(itemResult.getMediumId());
					cartItemInfo.setSmallId(itemResult.getSmallId());
					cartItemInfo.setCategoryCode(itemResult.getCategoryCode());
					cartItemInfo.setCategoryName(itemResult.getCategoryName());
					cartItemInfo.setSupplierId(itemResult.getSupplierId());
					cartItemInfo.setSupplierName(itemResult.getSupplierName());
					cartItemInfo.setSupplierAlias(itemResult.getSupplierName());
					cartItemInfo.setWeightNet(itemResult.getWeightNet());
					cartItemInfo.setWeight(itemResult.getWeight());
					cartItemInfo.setTarrifRate(itemResult.getTarrifRateValue());
					cartItemInfo.setSkuType(itemResult.getWavesSign());
					cartItemInfo.setWavesSign(itemResult.getWavesSign());
					cartItemInfo.setCustomsRate(itemResult.getCustomsRate());
					cartItemInfo.setExciseRate(itemResult.getExciseRate());
					cartItemInfo.setAddedValueRate(itemResult.getAddedValueRate());
					cartItemInfo.setCommisionRate(itemResult.getCommisionRate());
					cartItemInfo.setItemType(itemResult.getItemType());//商品类型  	/**商品类型：1-正常商品，2-服务商品，3-二手商品,默认1*/
					if(CartConstant.TYPE_SEA==itemResult.getWavesSign()){
						cartItemInfo.setSaleType(itemResult.getWavesSign());
					}else{
						cartItemInfo.setSaleType(itemResult.getSaleType());
						cartItemInfo.setFreightTemplateId(itemResult.getFreightTemplateId());
					}
					cartItemInfo.setUnit(itemResult.getUnitName());
					cartItemInfo.setRefundDays(itemResult.getReturnDays());
					cartItemInfo.setSalePropertyList(getItemAttribute(itemResult));
					//新增(7.22)
					cartItemInfo.setUnitId(itemResult.getUnitId());
					cartItemInfo.setUnitQuantity(itemResult.getUnitQuantity());
					cartItemInfo.setWrapQuantity(itemResult.getWrapQuantity());
					cartItemInfo.setCountryId(itemResult.getCountryId());
				}
			}
		}
		return cartItemInfoList;
	}
	
	/**
	 * 组装供应商信息
	 * @param cartItemInfoList
	 * @return
	 */
	public List<CartItemInfo> initSupplierInfo(List<CartItemInfo> cartItemInfoList){
		List<Long> supplierIdList = new ArrayList<Long>();
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			supplierIdList.add(cartItemInfo.getSupplierId());
		}
		List<SupplierInfo> supplierInfoList = supplierInfoService.querySupplierInfoByIds(supplierIdList);
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			for(SupplierInfo supplierInfo:supplierInfoList){
				if(cartItemInfo.getSupplierId().equals(supplierInfo.getId())){
					cartItemInfo.setSupplierAlias(supplierInfo.getAlias());
					if(CartConstant.TYPE_SEA==cartItemInfo.getSkuType()){
						cartItemInfo.setFreightTemplateId(supplierInfo.getFreightTemplateId());
					}
				}
			}
		}
		return cartItemInfoList;
	}
	
	/**
	 * 组装仓库信息
	 * @param cartItemInfoList
	 * @return
	 */
	public List<CartItemInfo> initInventory(List<CartItemInfo> cartItemInfoList){
		List<ItemInventoryDTO> itemInventoryList = new ArrayList<ItemInventoryDTO>();
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			ItemInventoryDTO itemInventoryDTO = new ItemInventoryDTO();
			itemInventoryDTO.setTopicId(cartItemInfo.getTopicId());
			itemInventoryDTO.setSkuCode(cartItemInfo.getSkuCode());
			itemInventoryList.add(itemInventoryDTO);
		}
		List<ItemInventoryDTO> returnItemInventoryList = topicService.queryItemInventory(itemInventoryList);
		if(CollectionUtils.isNotEmpty(returnItemInventoryList)){
			List<ItemSkuArt> itemSkuArtList = new ArrayList<ItemSkuArt>();
			List<Long> channelIdList = new ArrayList<Long>();
			for(ItemInventoryDTO itemInventoryDTO:returnItemInventoryList){
				ItemSkuArt skuArt = new ItemSkuArt();
				skuArt.setSku(itemInventoryDTO.getSkuCode());
				skuArt.setBondedArea(itemInventoryDTO.getBondedArea());
				itemSkuArtList.add(skuArt);
				if(itemInventoryDTO.getBondedArea() != null){
					channelIdList.add(itemInventoryDTO.getBondedArea());	
				}
			}
			List<ClearanceChannels> seaChanneList = clearanceChannelsService.getClearanceChannelsListByIds(channelIdList);
			if(CollectionUtils.isNotEmpty(seaChanneList)){
				for(ClearanceChannels clearanceChannels:seaChanneList){
					for(ItemInventoryDTO itemInventoryDTO:returnItemInventoryList){
						if(clearanceChannels.getId().equals(itemInventoryDTO.getBondedArea())){
							itemInventoryDTO.setBondedAreaName(clearanceChannels.getName());
						}
					}
				}
			}
			List<ItemSkuArt> checkReturn = itemService.checkBoundedInfoForSales(itemSkuArtList);
			if(CollectionUtils.isNotEmpty(checkReturn)){
				for(ItemSkuArt skuArt:checkReturn){
					for(ItemInventoryDTO itemInventoryDTO:returnItemInventoryList){
						if(itemInventoryDTO.getSkuCode().equals(skuArt.getSku()) && itemInventoryDTO.getBondedArea()!=null && itemInventoryDTO.getBondedArea().equals(skuArt.getBondedArea())){
							itemInventoryDTO.setArticleNumber(skuArt.getArticleNumber());
						}
					}
				}
			}
		
			for(CartItemInfo cartItemInfo:cartItemInfoList){
				for(ItemInventoryDTO itemInventoryDTO:returnItemInventoryList){
					if(cartItemInfo.getSkuCode().equals(itemInventoryDTO.getSkuCode()) 
					&& cartItemInfo.getTopicId().equals(itemInventoryDTO.getTopicId())){
						cartItemInfo.setProductCode(itemInventoryDTO.getArticleNumber());
						cartItemInfo.setSeaChannel(itemInventoryDTO.getBondedArea());
						cartItemInfo.setSeaChannelName(itemInventoryDTO.getBondedAreaName());
						cartItemInfo.setStorageType(itemInventoryDTO.getStorageType());
					}
				}
			}
		}
		return cartItemInfoList;
	}
	
	
	/**
	 * 组装库存
	 * @param cartItemInfoList
	 * @return
	 */
	public List<CartItemInfo> initStock(List<CartItemInfo> cartItemInfoList){

		List<SkuInventoryQuery> storageQueryList = new ArrayList<SkuInventoryQuery>();
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			if(cartItemInfo.getTopicItem() == null){
				//商品专场信息不存在,库存设为0
				cartItemInfo.setStock(0); continue;
			}
			SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
			skuInventoryQuery.setApp(App.PROMOTION);
			skuInventoryQuery.setBizId(String.valueOf(cartItemInfo.getTopicId()));
			skuInventoryQuery.setSku(cartItemInfo.getSkuCode());
			skuInventoryQuery.setWarehouseId(cartItemInfo.getWarehouseId());
			skuInventoryQuery.setBizPreOccupy(DEFAULTED.YES.equals(cartItemInfo.getTopicInventoryFlag()));
			storageQueryList.add(skuInventoryQuery);	
		}
		Map<String, Integer> inventoryMap = inventoryQueryService.querySalableInventory(storageQueryList);
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			cartItemInfo.setStock(inventoryMap.get(cartItemInfo.getTopicId() + "-" + cartItemInfo.getSkuCode()));
		}
		return cartItemInfoList;
	
//		
//		List<SkuInventoryQuery> storageQueryList = new ArrayList<SkuInventoryQuery>();
//		for(CartItemInfo cartItemInfo:cartItemInfoList){
//			SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
//			skuInventoryQuery.setApp(App.PROMOTION);
//			skuInventoryQuery.setBizId(String.valueOf(cartItemInfo.getTopicId()));
//			skuInventoryQuery.setSku(cartItemInfo.getSkuCode());
//			storageQueryList.add(skuInventoryQuery);
//		}
//		Map<String, Integer> inventoryMap = inventoryQueryService.batchSelectInventory(storageQueryList);
//		for(CartItemInfo cartItemInfo:cartItemInfoList){
//			cartItemInfo.setStock(inventoryMap.get(cartItemInfo.getTopicId() + "-" + cartItemInfo.getSkuCode()));
//		}
//		return cartItemInfoList;
	}

	/**
	 * 组装活动信息、销售价及小计
	 * @param cartItemInfoList
	 * @return
	 */
	public List<CartItemInfo> initTopicInfo(List<CartItemInfo> cartItemInfoList){
		List<TopicItemCartQuery> queryPolicyInfos = new ArrayList<TopicItemCartQuery>();
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			TopicItemCartQuery topicItemCartQuery = new TopicItemCartQuery();
			topicItemCartQuery.setTopicId(cartItemInfo.getTopicId());
			topicItemCartQuery.setSku(cartItemInfo.getSkuCode());
			queryPolicyInfos.add(topicItemCartQuery);
		}
		List<TopicItem> topicItemList = topicService.queryTopicItemInfoByTopicIdAndSku(queryPolicyInfos);
		if(CollectionUtils.isNotEmpty(topicItemList)){
			//查询仓库信息
			List<Long> warehouseIds = new ArrayList<>();
			for(TopicItem topicItem: topicItemList){
				warehouseIds.add(topicItem.getStockLocationId());
			}
			Map<Long, Warehouse> warehouseMap = getWarehouseByIds(warehouseIds);
			for(CartItemInfo cartItemInfo:cartItemInfoList){
				for(TopicItem topicItem:topicItemList){
					if(cartItemInfo.getSkuCode().equals(topicItem.getSku()) 
					 && cartItemInfo.getTopicId().equals(topicItem.getTopicId())){
						cartItemInfo.setTopicItem(topicItem);
						cartItemInfo.setWarehouseId(topicItem.getStockLocationId());
						cartItemInfo.setWarehouseName(topicItem.getStockLocation()); 
						//增加主仓库信息及主仓库对应供应商信息（用于拆单）2016.11.25
						if(warehouseMap.get(topicItem.getStockLocationId()) != null){
							Warehouse warehouse = warehouseMap.get(topicItem.getStockLocationId());
							cartItemInfo.setMainWarehouseId(warehouse.getMainWarehouseId());
							cartItemInfo.setMainWarehouseName(warehouse.getMainWarehouseName());
							cartItemInfo.setMainWhSupplierId(warehouse.getMainSpId());
							cartItemInfo.setMainWhSupplierName(warehouse.getMainSpName());
							cartItemInfo.setMainWhSupplierAlias(warehouse.getMainSpName());
							cartItemInfo.setWarehouseAddress(warehouse.getAddress());
							cartItemInfo.setLngLat(warehouse.getLngLat());
						}
						
						cartItemInfo.setPutSign(topicItem.getPutSign());
						cartItemInfo.setSalePrice(topicItem.getTopicPrice());
						cartItemInfo.setSeaChannel(topicItem.getBondedArea());
						cartItemInfo.setWhType(topicItem.getWhType());
						cartItemInfo.setStock(topicItem.getLimitTotal());
						if(OrderConstant.FAST_ORDER_TYPE.equals(cartItemInfo.getWhType())){
							cartItemInfo.setSaleType(OrderConstant.FAST_ORDER_TYPE);
						}else if(OrderConstant.OrderType.BUY_COUPONS.code.equals(cartItemInfo.getWhType())){
							cartItemInfo.setSaleType(OrderConstant.OrderType.BUY_COUPONS.code);
						}else if(CartConstant.TYPE_SEA==cartItemInfo.getWavesSign()){
							cartItemInfo.setSaleType(cartItemInfo.getWhType());
						} 
						//设置是否预占库存
						cartItemInfo.setTopicInventoryFlag(topicItem.getReserveInventoryFlag());
					}
					
					
				}
			}
		}
		initPutSign(cartItemInfoList);
		return cartItemInfoList;
	}
	/**
	 * 组装促销活动信息
	 * @param cartItemInfoList
	 * @return
	 */
	public List<CartItemInfo> initPromotion(List<CartItemInfo> cartItemInfoList){
		return cartItemInfoList;
	}
	
	/**初始化推送标识*/
	public List<CartItemInfo> initPutSign(List<CartItemInfo> cartItemInfoList){
		List<Long> warehouseIdList = new ArrayList<Long>();
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			if (cartItemInfo.getWarehouseId() != null) {
				warehouseIdList.add(cartItemInfo.getWarehouseId());	
			}
			if (cartItemInfo.getMainWarehouseId() != null && cartItemInfo.getMainWarehouseId() > 0){//主仓库
				warehouseIdList.add(cartItemInfo.getMainWarehouseId());
			}
		}
		Map<Long, Warehouse> warehouseMap = new HashMap<>();
		List<Warehouse> warehouseList = null;
		if(CollectionUtils.isNotEmpty(warehouseIdList)){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(warehouseIdList, SPLIT_SIGN.COMMA)+")");
			warehouseList = warehouseProxy.queryByParam(params).getData();
			for(Warehouse warehouse : warehouseList){
				warehouseMap.put(warehouse.getId(), warehouse);
			}
		}
		
		if(CollectionUtils.isNotEmpty(warehouseList)){
			for(CartItemInfo cartItemInfo:cartItemInfoList){
				Warehouse warehouse = warehouseMap.get(cartItemInfo.getWarehouseId());
				//putSign
				if (cartItemInfo.getMainWarehouseId() != null && cartItemInfo.getMainWarehouseId() > 0
					&& warehouseMap.get(cartItemInfo.getMainWarehouseId()) != null){//主仓库
					cartItemInfo.setPutSign(warehouseMap.get(cartItemInfo.getMainWarehouseId()).getPutSign());
				}else if(warehouse != null){
					cartItemInfo.setPutSign(warehouse.getPutSign());
				}
				//deliverAddr
				if (warehouse != null){
					cartItemInfo.setDeliverAddr(warehouse.getDeliverAddr());
				}
			}
		}
		return cartItemInfoList;
	}
	/**
	 * 验证购物车中的商品
	 * @param cartItemInfoList
	 * @return
	 */
	public List<CartItemInfo> validateCartItem(List<CartItemInfo> cartItemInfoList){
		Date now = new Date();
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			cartItemInfo.setStatus(CartConstant.CART_STATUS_0);
			TopicItem topicItem = cartItemInfo.getTopicItem();
			if(null==topicItem){
				cartItemInfo.setFailInfo(new FailInfo("专场过期或不存在"));
				cartItemInfo.setStatus(CartConstant.CART_STATUS_2);
				cartItemInfo.setDisabled(TF.YES);
			}else{
				Topic topic = topicItem.getTopic();
				if(TF.YES.equals(topicItem.getLockStatus())){
					cartItemInfo.setFailInfo(new FailInfo("已锁不能下单"));
					cartItemInfo.setDisabled(TF.YES);
					cartItemInfo.setStatus(CartConstant.CART_STATUS_1);
				}else if(TopicStatus.PASSED.ordinal()!=topic.getStatus()){
					cartItemInfo.setFailInfo(new FailInfo("活动未审核通过"));
					cartItemInfo.setStatus(CartConstant.CART_STATUS_2);
					cartItemInfo.setDisabled(TF.YES);
				}else if(now.before(topic.getStartTime())){
					cartItemInfo.setFailInfo(new FailInfo("活动未开始"));
					cartItemInfo.setStatus(CartConstant.CART_STATUS_2);
					cartItemInfo.setDisabled(TF.YES);
				}else if(now.after(topic.getEndTime())){
					cartItemInfo.setFailInfo(new FailInfo("活动已结束"));
					cartItemInfo.setStatus(CartConstant.CART_STATUS_2);
					cartItemInfo.setDisabled(TF.YES);
				}
				if(!ItemStatusEnum.ONLINE.getValue().equals(cartItemInfo.getSkuStatus())){
					cartItemInfo.setFailInfo(new FailInfo("商品已下架"));
					cartItemInfo.setStatus(CartConstant.CART_STATUS_2);
					cartItemInfo.setDisabled(TF.YES);
				}
				if(TF.NO.equals(cartItemInfo.getStock())){
					cartItemInfo.setFailInfo(new FailInfo("商品无活动库存"));
					cartItemInfo.setStatus(CartConstant.CART_STATUS_1);
					cartItemInfo.setDisabled(TF.YES);
				}
				//供应商作废
				if(StorageConstant.StorageType.FAST.value.equals(cartItemInfo.getStorageType())
						  && !OrderConstant.OrderType.FAST.name().equals(cartItemInfo.getChannelCode())){
					cartItemInfo.setFailInfo(new FailInfo("速购商品请到<a href='http://fast.51seaco.com'>速购平台</a>购买"));
					cartItemInfo.setStatus(CartConstant.CART_STATUS_1);
					cartItemInfo.setDisabled(TF.YES);
				}
			}
		}
		
		
		return cartItemInfoList;
	}
	
	/**
	 * 验证购物车商品的限购
	 * @param cartItemInfoList
	 * @return
	 */
	public List<CartItemInfo> validateCartItemLimit(List<CartItemInfo> cartItemInfoList,ShoppingCartDto cart){
		for(CartItemInfo cartItemInfo:cartItemInfoList){
			FailInfo failInfo = cartItemInfoValidateProxy.validateCartItem(cartItemInfo,cart);
			cartItemInfo.setStatus(CartConstant.CART_STATUS_0);
			if(null!=failInfo){
				cartItemInfo.setFailInfo(failInfo);
				cartItemInfo.setStatus(CartConstant.CART_STATUS_3);
			}
			if(cartItemInfo.getStock()<cartItemInfo.getQuantity()){
				cartItemInfo.setFailInfo(new FailInfo("库存不足"));
				cartItemInfo.setStatus(CartConstant.CART_STATUS_1);
			}
		}
		return cartItemInfoList;
	}
	
	/**
	 * 按有效、失效分组
	 * @param cartItemInfoList
	 * @return
	 */
	public Map<Integer,List<CartItemInfo>> divideEnabledAndDisabled(List<CartItemInfo> cartItemInfoList){
		Map<Integer,List<CartItemInfo>> itemAbledMap = new HashMap<Integer,List<CartItemInfo>>();
		List<CartItemInfo> disCartItemInfoList = new ArrayList<CartItemInfo>();
		for(int i=0;i<cartItemInfoList.size();i++){
			CartItemInfo cartItemInfo = cartItemInfoList.get(i);
			if(TF.YES.equals(cartItemInfo.getDisabled())){
				disCartItemInfoList.add(cartItemInfo);
				cartItemInfoList.remove(cartItemInfo);
				i--;
			}
		}
		itemAbledMap.put(TF.NO, cartItemInfoList);
		itemAbledMap.put(TF.YES, disCartItemInfoList);
		return itemAbledMap;
	}
	/**
	 * 获取商品属性信息
	 * @param item
	 * @param salePropList
	 */
	public List<SalePropertyDTO> getItemAttribute(ItemResultDto item) {
		List<SalePropertyDTO> salePropList = new ArrayList<SalePropertyDTO>();
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
		return salePropList;
	}
	
	private Map<Long, Warehouse> getWarehouseByIds(List<Long> ids){
		Map<Long, Warehouse> warehouseMap = new HashMap<>();
		if(CollectionUtils.isEmpty(ids)) return warehouseMap;
		List<Warehouse> warehouses = warehouseProxy.getWarehouseByIds(ids);
		for(Warehouse warehouse : warehouses){
			warehouseMap.put(warehouse.getId(), warehouse);
		}
		return warehouseMap;
	}
}
