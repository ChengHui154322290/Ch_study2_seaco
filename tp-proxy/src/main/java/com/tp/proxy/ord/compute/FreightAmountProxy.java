package com.tp.proxy.ord.compute;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.subtract;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.dto.common.FailInfo;
import com.tp.dto.map.DistancesResult;
import com.tp.dto.map.LngLatResult;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.PreOrderDto;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.map.MapAPIProxy;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.util.StringUtil;
import com.tp.util.ThreadUtil;

/**
 * 计算运费
 * @author szy
 *
 */
@Service
public  class FreightAmountProxy implements IAmountProxy<OrderInitDto>{

	private static final Logger logger = LoggerFactory.getLogger(FreightAmountProxy.class);
	@Autowired
	private MapAPIProxy mapAPIProxy;
	@Autowired
	private IFreightTemplateService freightTemplateService;
	@Autowired
	private IConsigneeAddressService consigneeAddressService;
	@Override
	public OrderInitDto computeAmount(OrderInitDto orderInitDto) {
		if(!OrderConstant.OrderType.FAST.code.equals(orderInitDto.getPreSubOrderList().get(0).getType())){
			return computeFreight(orderInitDto);
		}
		return computeAmountFast(orderInitDto);
	}
	
	/**
	 * 计算运费
	 * 有全场模板则用全场的，无则按订单类型及供应商分别取最小包邮模板,如果合计金额达不到包邮则要支付邮费
	 * @param shoppingCartDto
	 * @return
	 */
	public OrderInitDto computeFreight(final OrderInitDto shoppingCartDto){
		shoppingCartDto.setFreight(ZERO);
		List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList();
		//是否有全场包邮
		FreightTemplate globalTemplate = freightTemplateService.queryGlobalTemplate();
		if(globalTemplate!=null 
		&& globalTemplate.getFreePostage()!=null
		&& globalTemplate.getPostage()!=null
		&& shoppingCartDto.getActuallyAmount().compareTo(globalTemplate.getFreePostage().doubleValue())<0){
			Double subTotal = ZERO;
			Double postage = globalTemplate.getPostage().doubleValue();
			shoppingCartDto.setFreight(globalTemplate.getPostage().doubleValue());
			OrderItem maxAmountOrderItem = null;
			for(SubOrder subOrder:subOrderList){
				for(OrderItem orderItem:subOrder.getOrderItemList()){
					Double subFreight = toPrice(multiply(divide(orderItem.getItemAmount(),shoppingCartDto.getOrginItemAmount(),6),postage));
					orderItem.setFreight(subFreight);
					subTotal = toPrice(add(subTotal,subFreight));
					if(maxAmountOrderItem==null || (maxAmountOrderItem.getItemAmount()<orderItem.getItemAmount())){
						maxAmountOrderItem = orderItem;
					}
					orderItem.setOrigFreight(subFreight);
					orderItem.setOriginalSubTotal(toPrice(add(add(orderItem.getOrigItemAmount(),subFreight),orderItem.getOrigTaxFee())));
					orderItem.setSubTotal(toPrice(add(orderItem.getTaxFee(),add(orderItem.getItemAmount(),subFreight))));
				}
			}
			if(subTotal.compareTo(postage)!=0){
				Double subFreight = toPrice(add(maxAmountOrderItem.getFreight(),subtract(postage,subTotal)));
				maxAmountOrderItem.setFreight(subFreight);
				maxAmountOrderItem.setOriginalSubTotal(toPrice(add(maxAmountOrderItem.getOriginalSubTotal(),subFreight)));
				maxAmountOrderItem.setSubTotal(toPrice(add(maxAmountOrderItem.getSubTotal(),subFreight)));
			}
		}else if(globalTemplate==null){
			Map<String,List<PreOrderDto>> subOrderMap = new HashMap<String,List<PreOrderDto>>();
			for(PreOrderDto subOrder:subOrderList){
				String key = subOrder.getType()+SPLIT_SIGN.UNDERLINE+subOrder.getSupplierId();
				List<PreOrderDto> orderList = subOrderMap.get(key);
				if(orderList==null){
					orderList = new ArrayList<PreOrderDto>();
					subOrderMap.put(key, orderList);
				}
				orderList.add(subOrder);
			}
			List<Long> templateIdList = new ArrayList<Long>();
			for(PreOrderDto subOrder:subOrderList){
				templateIdList.add(subOrder.getFreightTemplateId());
			}
			List<FreightTemplate> freightTemplateList = freightTemplateService.queryItemFreightTemplateByTemplateIdList(templateIdList);
			if(CollectionUtils.isNotEmpty(freightTemplateList)){
				for(Map.Entry<String,List<PreOrderDto>> entry:subOrderMap.entrySet()){
					Double entryTotal =  ZERO;
					for(PreOrderDto subOrder:entry.getValue()){
						for(OrderItem orderItem:subOrder.getOrderItemList()){
							entryTotal = toPrice(add(entryTotal,orderItem.getItemAmount()));
						}
					}
					FreightTemplate currrentFreightTemplate = null;
					for(FreightTemplate freightTemplate:freightTemplateList){
						for(PreOrderDto subOrder:entry.getValue()){
							if(freightTemplate.getId().equals(subOrder.getFreightTemplateId())){
								if(currrentFreightTemplate==null){
									currrentFreightTemplate = freightTemplate;
								}else if(currrentFreightTemplate.getFreePostage()!=null && freightTemplate.getFreePostage()!=null
								       && currrentFreightTemplate.getFreePostage()>freightTemplate.getFreePostage()){
									currrentFreightTemplate = freightTemplate;
								}
							}
						}
					}
					if(currrentFreightTemplate!=null && currrentFreightTemplate.getFreePostage()!=null && currrentFreightTemplate.getPostage()!=null){
						Double freePostage = currrentFreightTemplate.getFreePostage().doubleValue();
						if(freePostage>entryTotal){
							Double postage = currrentFreightTemplate.getPostage().doubleValue();
							shoppingCartDto.setFreight(postage);
							Double subFreightTotal = ZERO;
							OrderItem maxOrderItem = null;
							for(PreOrderDto subOrder:entry.getValue()){
								for(OrderItem orderItem:subOrder.getOrderItemList()){
									Double subFreight = toPrice(multiply(divide(orderItem.getItemAmount(),shoppingCartDto.getPayableAmount(),6),postage));
									orderItem.setFreight(subFreight);
									subFreightTotal = toPrice(add(subFreightTotal,subFreight));
									if(maxOrderItem==null || (maxOrderItem.getFreight()<subFreight)){
										maxOrderItem = orderItem;
									}
									orderItem.setOriginalSubTotal(toPrice(add(orderItem.getOriginalSubTotal(),subFreight)));
									orderItem.setSubTotal(toPrice(add(orderItem.getSubTotal(),subFreight)));
								}
							}
							if(postage!=subFreightTotal){
								Double subFreight = toPrice(add(maxOrderItem.getFreight(),subtract(postage,subFreightTotal)));
								maxOrderItem.setFreight(subFreight);
								maxOrderItem.setOriginalSubTotal(toPrice(add(maxOrderItem.getOriginalSubTotal(),subFreight)));
								maxOrderItem.setSubTotal(toPrice(add(maxOrderItem.getSubTotal(),subFreight)));
							}
						}
					}
				}
			}
		}
		shoppingCartDto.setOrginFreight(shoppingCartDto.getFreight());
		return shoppingCartDto;
	}

	
	/**
	 * 计算速购订单运费
	 * @param orderInitDto
	 * @return
	 */
	public OrderInitDto computeAmountFast(OrderInitDto orderInitDto){
		CartItemInfo cartItemInfo = orderInitDto.getCartItemInfoList().get(0);
		Double freight = operate(3,8);
		String lngLat = cartItemInfo.getLngLat();
		ConsigneeAddress consigneeAddress = orderInitDto.getConsigneeAddress();
		if(consigneeAddress==null){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", orderInitDto.getMemberId());
			if(null!=orderInitDto.getConsigneeId()){
				params.put("id", orderInitDto.getConsigneeId());
			}else{
				params.put("isDefault", Constant.DEFAULTED.YES);
			}
			consigneeAddress = consigneeAddressService.queryUniqueByParams(params);
		}
		String destination = getLngLat(consigneeAddress);
		if(StringUtil.isNotBlank(lngLat) && StringUtil.isNotBlank(destination)){
			DistancesResult result = mapAPIProxy.distance(lngLat, destination);
			if(result.getStatus()==1 && CollectionUtils.isNotEmpty(result.getResults())){
				Integer mileage = Math.round(result.getResults().get(0).getDistance()/1000);
				Integer weight = Long.valueOf(Math.round(cartItemInfo.getWeight()/1000)).intValue();
				freight = operate(mileage,weight);
			}
		}
		cartItemInfo.setFreight(freight);
		cartItemInfo.setSubTotal(toPrice(add(cartItemInfo.getSubTotal(),freight)));
		cartItemInfo.setRealSubTotal(toPrice(add(cartItemInfo.getRealSubTotal(),freight)));
		SubOrder subOrder = orderInitDto.getPreSubOrderList().get(0);
		subOrder.setTotal(toPrice(add(subOrder.getTotal(),freight)));
		subOrder.setOriginalTotal(toPrice(add(subOrder.getOriginalTotal(),freight)));
		subOrder.setFreight(freight);
		OrderItem orderItem = subOrder.getOrderItemList().get(0);
		orderItem.setFreight(freight);
		orderItem.setOrigFreight(freight);
		orderItem.setOriginalSubTotal(toPrice(add(orderItem.getOriginalSubTotal(),freight)));
		orderItem.setSubTotal(toPrice(add(orderItem.getSubTotal(),freight)));
		orderInitDto.setFreight(freight);
		orderInitDto.setOrginFreight(freight);
		return orderInitDto;
	}
	
	/**
	 * 同城速购根据里程及重量计算运费 15+1.5*(mileage-3)+5*(weight-8)
	 * @param mileage
	 * @param weight
	 * @return
	 */
	private Double operate(int mileage,int weight){
		if(mileage<3 && weight < 8){ 
			return 15.0;
		}
		Double f1 = 15+1.5*((mileage < 3 ? 3:mileage)-3)+5*((weight < 8 ? 8 : weight) - 8);
		Double f2 = 33 + 3.0 * ((mileage < 5 ? 5 : mileage) - 5);
		return f1 > f2 ? f2 : f1;
	}

	private String getLngLat(ConsigneeAddress consigneeAddress){
		String destination = consigneeAddress.getLongitude()+Constant.SPLIT_SIGN.COMMA+consigneeAddress.getLatitude();
		if(consigneeAddress.getLatitude()==null || consigneeAddress.getLongitude()==null){
			LngLatResult result = mapAPIProxy.geocode(consigneeAddress.getCounty()+consigneeAddress.getStreet()+consigneeAddress.getAddress(),consigneeAddress.getCity());
			if(result.getStatus()==1 && CollectionUtils.isNotEmpty(result.getGeocodes())){
				destination = result.getGeocodes().get(0).getLocation();
				String[] deliveryLngLat = destination.split(Constant.SPLIT_SIGN.COMMA);
				consigneeAddress.setLongitude(deliveryLngLat[0]);
				consigneeAddress.setLatitude(deliveryLngLat[1]);
				
				Runnable runnable = new Runnable(){
					@Override
					public void run() {
						try {
							consigneeAddressService.updateNotNullById(consigneeAddress);
						} catch (Throwable throwable) {
							ExceptionUtils.print(new FailInfo(throwable), logger,consigneeAddress);
							
						}
					}
					
				};
				ThreadUtil.excAsync(runnable,false);
				
			}
		}
		return destination;
	}
}
