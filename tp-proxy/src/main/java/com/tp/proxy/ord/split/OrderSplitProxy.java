package com.tp.proxy.ord.split;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.formatToPrice;
import static com.tp.util.BigDecimalUtil.multiply;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.ord.CartConstant;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.util.BigDecimalUtil;
import com.tp.util.JsonFormatUtils;
import com.tp.util.StringUtil;

/**
 * 根据规则拆分订单
 * @author szy
 *
 */
@Service
public class OrderSplitProxy implements IOrderSplitProxy<ShoppingCartDto> {
	private static final Logger logger = LoggerFactory.getLogger(OrderSplitProxy.class);
    public final static Double CUSTOMS_RATE_LIMIT=2000.00D;
    private final static Float RATE_DISCOUNT = 0.7f;
	/**
	 * 按订单类型、供应商、通关渠道、仓库、税费进行拆分成子订单
	 */
	@Override
	public ShoppingCartDto split(ShoppingCartDto shoppingCartDto) {
		List<CartItemInfo> cartItemInfoList = shoppingCartDto.getCartItemInfoList();
		Map<String,List<CartItemInfo>> cartItemInfoSplitMap = new HashMap<String,List<CartItemInfo>>();
		if(CollectionUtils.isNotEmpty(cartItemInfoList)){
			sort(cartItemInfoList);
			for(CartItemInfo cartItemInfo:cartItemInfoList){
				List<CartItemInfo> itemList = getList(cartItemInfoSplitMap,cartItemInfo);
				itemList.add(cartItemInfo);
			}
			//转换成预订单
			shoppingCartDto = transPreSubOrder(shoppingCartDto,cartItemInfoSplitMap);
			//根据预订单中的商品项满减分拆
			shoppingCartDto = splitSubOrderItemByFullDiscount(shoppingCartDto);
		}
		return shoppingCartDto;
	}
	
	/**
	 * 排序
	 * @param list
	 */
	private void sort(List<CartItemInfo> list){
		Collections.sort(list, new Comparator<CartItemInfo>(){
			@Override
			public int compare(CartItemInfo o1, CartItemInfo o2) {
				return o1.getSaleType().compareTo(o2.getSaleType())*1000000+
					   o1.getSupplierId().compareTo(o2.getSupplierId())*10000+
					   o1.getSeaChannel().compareTo(o2.getSeaChannel())*100+
					   o1.getWarehouseId().compareTo(o2.getWarehouseId());
			}
		});
	}
	/**
	 * 分拆到List
	 * @param subOrderMap
	 * @param cartItemInfo
	 * @return
	 */
	private List<CartItemInfo> getList(final Map<String,List<CartItemInfo>> subOrderMap,final CartItemInfo cartItemInfo){
		String key = "";
		// 主仓库ID不为空，且不为0:即同一主仓库的商品应在同一订单
		if (cartItemInfo.getMainWarehouseId() != null && cartItemInfo.getMainWarehouseId() > 0) {
			key = 	cartItemInfo.getSaleType() + SPLIT_SIGN.UNDERLINE 
					+ cartItemInfo.getMainWhSupplierId() + SPLIT_SIGN.UNDERLINE 
					+ cartItemInfo.getSeaChannel() + SPLIT_SIGN.UNDERLINE
					+ cartItemInfo.getMainWarehouseId()+SPLIT_SIGN.UNDERLINE
					+ cartItemInfo.getStorageType();
		}else{
			key = 	cartItemInfo.getSaleType()+SPLIT_SIGN.UNDERLINE
					+cartItemInfo.getSupplierId()+SPLIT_SIGN.UNDERLINE
					+cartItemInfo.getSeaChannel()+SPLIT_SIGN.UNDERLINE
					+cartItemInfo.getWarehouseId()+SPLIT_SIGN.UNDERLINE
					+cartItemInfo.getStorageType();
		}
		List<CartItemInfo> itemInfoList = subOrderMap.get(key);
		if(itemInfoList==null){
			itemInfoList = new ArrayList<CartItemInfo>();
		}
		subOrderMap.put(key, itemInfoList);
		return itemInfoList;
	}
	
	/**
	 * 把分组转换成预订单
	 * @param shoppingCartDto
	 * @param cartItemInfoSplitMap
	 * @return
	 */
	private ShoppingCartDto transPreSubOrder(ShoppingCartDto shoppingCartDto,Map<String,List<CartItemInfo>> cartItemInfoSplitMap){
		shoppingCartDto.getPreSubOrderList().clear();
		for(Map.Entry<String,List<CartItemInfo>> entry:cartItemInfoSplitMap.entrySet()){
			List<CartItemInfo> cartItemInfoList = entry.getValue();
			CartItemInfo cartItemInfo = cartItemInfoList.get(0);
			PreOrderDto subOrder = new PreOrderDto();
			//存在主仓库
			if(cartItemInfo.getMainWarehouseId() != null && cartItemInfo.getMainWarehouseId() > 0){
				subOrder.setDeliveryStockName(cartItemInfo.getMainWarehouseName());
				subOrder.setSupplierId(cartItemInfo.getMainWhSupplierId());
				subOrder.setWarehouseId(cartItemInfo.getMainWarehouseId());
				subOrder.setLngLat(cartItemInfo.getLngLat());
				subOrder.setWarehouseName(cartItemInfo.getMainWarehouseName());
				subOrder.setSupplierName(cartItemInfo.getMainWhSupplierName());
				subOrder.setSupplierAlias(cartItemInfo.getMainWhSupplierAlias());
			}else{
				subOrder.setDeliveryStockName(cartItemInfo.getWarehouseName());
				subOrder.setSupplierId(cartItemInfo.getSupplierId());
				subOrder.setWarehouseId(cartItemInfo.getWarehouseId());
				subOrder.setLngLat(cartItemInfo.getLngLat());
				subOrder.setSupplierName(cartItemInfo.getSupplierName());
				subOrder.setSupplierAlias(cartItemInfo.getSupplierAlias());
				subOrder.setWarehouseName(cartItemInfo.getWarehouseName());
			}		
			subOrder.setType(cartItemInfo.getSaleType());
			subOrder.setSeaChannel(cartItemInfo.getSeaChannel());			
			subOrder.setStorageType(cartItemInfo.getStorageType());
			subOrder.setMemberId(shoppingCartDto.getMemberId());			
			subOrder.setSeaChannelName(cartItemInfo.getSeaChannelName());			
			subOrder.setPutSign(cartItemInfo.getPutSign());
			subOrder.setOrderCode(System.currentTimeMillis());
			subOrder.setFreightTemplateId(cartItemInfo.getFreightTemplateId());
			subOrder.setFreight(Constant.ZERO);
			List<OrderItem> orderItemList = new ArrayList<OrderItem>();
			Integer quantityCount = 0;
			for(CartItemInfo cartItem:cartItemInfoList){
				quantityCount +=cartItem.getQuantity();
				OrderItem orderItem = createOrderItemInfo(cartItem,subOrder);
				orderItemList.add(orderItem);
			}
			subOrder.setOrderItemList(orderItemList);
			subOrder.setQuantity(quantityCount);
			initDeliverAddrList(subOrder,cartItemInfo);
			subOrder = (PreOrderDto) initItemTaxRate(subOrder);
			shoppingCartDto.getPreSubOrderList().add(subOrder);
		}
		return shoppingCartDto;
	}
	
	/**
	 * 进行满减分组
	 * @param shoppingCartDto
	 * @return
	 */
	private ShoppingCartDto splitSubOrderItemByFullDiscount(ShoppingCartDto shoppingCartDto){
		List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList();
		for(PreOrderDto subOrder:subOrderList){
			subOrder.getFullDiscountMap().put(null, subOrder.getOrderItemList());
		}
		return shoppingCartDto;
	}
	
	/**
	 * 保存订单商品行信息
	 * @param cartItemInfo
	 * @return tempOrderLine
	 */
	private OrderItem createOrderItemInfo(CartItemInfo cartItemInfo,PreOrderDto subOrder) {
		OrderItem orderItem = new OrderItem();
		orderItem.setQuantity(cartItemInfo.getQuantity());
		orderItem.setListPrice(cartItemInfo.getListPrice());
		orderItem.setSalesPrice(cartItemInfo.getSalePrice());
		orderItem.setPrice(cartItemInfo.getSalePrice());
		orderItem.setSalesType(cartItemInfo.getSaleType());
		orderItem.setImg(cartItemInfo.getItemPic());// 图片路径
		orderItem.setSkuCode(cartItemInfo.getSkuCode());// SKU编号
		orderItem.setBarCode(cartItemInfo.getBarcode());// 商品条形码
		orderItem.setSpuCode(cartItemInfo.getItemCode());// 商品编号
		orderItem.setSpuId(cartItemInfo.getItemId());// 商品ID
		orderItem.setSpuName(cartItemInfo.getItemName());// 商品名称
		orderItem.setBrandId(cartItemInfo.getBrandId());
		orderItem.setBrandName(cartItemInfo.getBrandName());
		orderItem.setSupplierId(cartItemInfo.getSupplierId());
		orderItem.setSupplierName(cartItemInfo.getSupplierName());
		orderItem.setWarehouseId(cartItemInfo.getWarehouseId());
		orderItem.setTopicId(cartItemInfo.getTopicId());// 设置促销ID
		orderItem.setUnit(cartItemInfo.getUnit());
		orderItem.setTaxFee(cartItemInfo.getTaxfFee());
		orderItem.setTaxRate(cartItemInfo.getTarrifRate());
		orderItem.setStorageType(cartItemInfo.getStorageType());
		orderItem.setSeaChannel(cartItemInfo.getSeaChannel());
		orderItem.setOrderCode(subOrder.getOrderCode());
		orderItem.setMemberId(cartItemInfo.getMemberId());
		orderItem.setCustomsRate(cartItemInfo.getCustomsRate());
		orderItem.setExciseRate(cartItemInfo.getExciseRate());
		orderItem.setAddedValueRate(cartItemInfo.getAddedValueRate());
		orderItem.setSelected(cartItemInfo.getSelected());
		orderItem.setCommisionRate(cartItemInfo.getCommisionRate());
		orderItem.setStatus(cartItemInfo.getStatus());
		orderItem.setIsSea(CartConstant.TYPE_SEA==cartItemInfo.getWavesSign()?Boolean.TRUE:Boolean.FALSE);
		if(cartItemInfo.getTopicItem()!=null){
			orderItem.setStock(cartItemInfo.getTopicItem().getLimitTotal());
			orderItem.setLimitCount(cartItemInfo.getTopicItem().getLimitAmount());
		}else{
			orderItem.setStock(99);
			orderItem.setLimitCount(99);
		}
		orderItem.setCouponAmount(Constant.ZERO);
		orderItem.setOrderCouponAmount(Constant.ZERO);
		orderItem.setType(OrderConstant.ITEM_SALES_TYPE.NO_GIFT.code);
		// 设置销售属性
		orderItem.setSalesProperty(JsonFormatUtils.format(cartItemInfo.getSalePropertyList()));// 设置销售属性
		if (null == cartItemInfo.getWeight() || null == cartItemInfo.getQuantity()) {// 如果weight为空,或者数量为空
			orderItem.setWeight(0D);
		} else {
			orderItem.setWeight(BigDecimalUtil.multiply(cartItemInfo.getWeight(), cartItemInfo.getQuantity())
					.doubleValue());// 设置重量
		}
		orderItem.setTaxRate(cartItemInfo.getTarrifRate());// 税率
		orderItem.setTaxFee(cartItemInfo.getTaxfFee());// 设置海淘税费
		orderItem.setProductCode(cartItemInfo.getProductCode());
		orderItem.setRefundDays(cartItemInfo.getRefundDays());
		orderItem.setOriginalSubTotal(BigDecimalUtil.add(cartItemInfo.getSubTotal(), cartItemInfo.getTaxfFee())
				.doubleValue());// 订单行原始金额(按促销价钱计算)+税费
		orderItem.setFreight(Constant.ZERO);// 初始化行运费
		
		//新增(7.22)
		orderItem.setUnitId(cartItemInfo.getUnitId());
		orderItem.setUnitQuantity(cartItemInfo.getUnitQuantity());
		orderItem.setWrapQuantity(cartItemInfo.getWrapQuantity());
		orderItem.setWeightNet(cartItemInfo.getWeightNet());
		orderItem.setCountryId(cartItemInfo.getCountryId());
		
		//新增(5.4)
		orderItem.setTopicInventoryFlag(cartItemInfo.getTopicInventoryFlag());
		return orderItem;
	}
	
	
	/**
	 * 税率设置
	 * @param cartItemInfoList
	 * @return
	 */
	public SubOrder initItemTaxRate(SubOrder subOrder){
		if(CollectionUtils.isNotEmpty(subOrder.getOrderItemList())){
			for(OrderItem orderItem:subOrder.getOrderItemList()){
				if(orderItem.getSelectedBoolean() && orderItem.getIsSea()){
					if (!OrderConstant.OrderType.DOMESTIC.getCode().equals(subOrder.getStorageType())
						&& !OrderConstant.OrderType.FAST.getCode().equals(subOrder.getStorageType())
						) { //国内直发 ，免税费，但是要显示，所以不加入总价
		                if (!ClearanceChannelsEnum.HWZY.id.equals(subOrder.getSeaChannel())) {//海外直邮，免税费，但是要显示，也不加入总价
		                	Double taxRate = (new BigDecimal(orderItem.getAddedValueRate()).add(new BigDecimal(orderItem.getExciseRate()))
		                			             .divide(new BigDecimal(100).subtract(new BigDecimal(orderItem.getExciseRate())),6,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))).doubleValue();
		                	if(formatToPrice(multiply(orderItem.getPrice(),orderItem.getQuantity())).doubleValue()>=CUSTOMS_RATE_LIMIT){
		                		orderItem.setTaxRate(add(taxRate,orderItem.getCustomsRate()).doubleValue());
		                	}else{
		                		orderItem.setTaxRate(multiply(taxRate,RATE_DISCOUNT).doubleValue());
		                	}
		                }
		            }
				}
			}			
		}
		return subOrder;
	}
	
	public SubOrder initDeliverAddrList(SubOrder subOrder,CartItemInfo cartItemInfo){
		if(StringUtil.isNotBlank(cartItemInfo.getDeliverAddr())){
			List<Long> deliveryAddressList = new ArrayList<Long>();
			for(String address:cartItemInfo.getDeliverAddr().split(",")){
				deliveryAddressList.add(Long.valueOf(address));
			}
			subOrder.setDeliveryAddressList(deliveryAddressList);
		}
		return subOrder;
	}
}
