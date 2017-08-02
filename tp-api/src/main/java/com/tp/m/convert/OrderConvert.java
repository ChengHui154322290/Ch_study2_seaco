package com.tp.m.convert;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.SalePropertyDTO;
import com.tp.dto.ord.SeaOrderItemDTO;
import com.tp.dto.ord.SeaOrderItemWithSupplierDTO;
import com.tp.dto.ord.SeaOrderItemWithWarehouseDTO;
import com.tp.dto.ord.remote.OrderDetails4UserDTO;
import com.tp.dto.ord.remote.OrderList4UserDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.OrderEnum;
import com.tp.m.helper.ImgHelper;
import com.tp.m.query.order.QueryOrder;
import com.tp.m.to.order.LogDetailTO;
import com.tp.m.to.order.OrderLineTO;
import com.tp.m.util.DateUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.groupbuy.OrderRedeemItemVo;
import com.tp.m.vo.order.LogisticCompanyVO;
import com.tp.m.vo.order.LogisticVO;
import com.tp.m.vo.order.OrderDetailVO;
import com.tp.m.vo.order.OrderVO;
import com.tp.m.vo.order.PayOrderLineVO;
import com.tp.m.vo.order.SubmitOrderInfoVO;
import com.tp.m.vo.product.ProductVO;
import com.tp.m.vo.product.ProductWithWarehouseVO;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.ord.OrderChannelTrack;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.ExpressLogInfoDTO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.util.Base64Util;
import com.tp.util.BigDecimalUtil;

/**
 * 订单封装类
 * @author zhuss
 * @2016年1月7日 下午5:31:28
 */
public class OrderConvert {
	
	/**
	 * 封装提交订单入参对象
	 * @param orderInitDto
	 * @param order
	 */
	public static void convertSumit(OrderInitDto orderInitDto,QueryOrder order){
		String token = order.getToken();
		if(StringUtil.isNotBlank(order.getUuid())){
			token = order.getUuid();
		}
		List<Long> couponIds = new ArrayList<Long>();
		if(StringUtil.isNotBlank(order.getCid())){
			couponIds.add(StringUtil.getLongByStr(order.getCid()));
		}
		//优惠券集合
		if(CollectionUtils.isNotEmpty(order.getCidlist())){
			for(String cid : order.getCidlist()){
				couponIds.add(StringUtil.getLongByStr(cid));
			}
		}
		orderInitDto.setUsedPointSign(Boolean.TRUE.toString().equals(order.getUsedPointSign()));
		orderInitDto.setToken(token);
		orderInitDto.setMemberId(order.getUserid());
		orderInitDto.setConsigneeId(Long.parseLong(order.getAid()));
		orderInitDto.setIp(order.getIp());
		orderInitDto.setOrderSource(PlatformEnum.getCodeByName(order.getApptype()));
		orderInitDto.setCouponIds(couponIds);
		orderInitDto.setGroupId(NumberUtils.isNumber(order.getGid())?Long.parseLong(order.getGid()):null);
		orderInitDto.setTpin(order.getTpin());
		initChannelTrack(orderInitDto,order);
	}
	
	/**
	 * 封装待支付的子订单列表
	 * @param porders
	 * @return
	 */
	public static List<PayOrderLineVO> convertUnPayOrders(List<PaymentInfo> porders,QueryOrder order){
		List<PayOrderLineVO> plist = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(porders)){
			for(PaymentInfo pi : porders){
				PayOrderLineVO pl = new PayOrderLineVO();
				pl.setOrdercode(StringUtil.getStrByObj(pi.getBizCode()));
				pl.setOrderprice(NumberUtil.sfwr(pi.getAmount()));
				pl.setPayid(StringUtil.getStrByObj(pi.getPaymentId()));
				//List<PaymentGateway> ways = paymentGatewayProxy.queryPaymentGateWayLists(pi.getOrderType(),pi.getChannelId());
				pl.setPayways(PayConvert.counvertPayways(order));
				plist.add(pl);
			}
		}
		return plist;
	}
	
	/**
	 * 封装提交订单页面信息
	 * @return
	 */
	public static SubmitOrderInfoVO convertSubmitOrderInfo(SeaOrderItemDTO seaorderitem){
		SubmitOrderInfoVO vo = new SubmitOrderInfoVO();
		if(null != seaorderitem){
			vo.setItemsprice(NumberUtil.sfwr(seaorderitem.getTotalPrice()));
			vo.setFreight(NumberUtil.sfwr(seaorderitem.getTotalFreight()));
			vo.setTaxes(NumberUtil.sfwr(seaorderitem.getTotalTaxfee()));
			vo.setPrice(NumberUtil.sfwr(seaorderitem.getPayPrice()));
			vo.setDisprice(NumberUtil.sfwr(seaorderitem.getTotalDiscount()));
			vo.setIsfirstminus(seaorderitem.getFirstMinus()?StringUtil.ONE:StringUtil.ZERO);
			vo.setFirstcoupon("5");//首单立减5元
			vo.setTotalcoupon(StringUtil.getStrByObj(seaorderitem.getTotalCoupon()));
			vo.setProductinfo(convertProductWithWarehouseList(seaorderitem));
		}
		return vo;
	}
	
	/**
	 * 封装提交订单页面的商品信息集合
	 * @param seaorderitem
	 * @return
	 */
	public static List<ProductWithWarehouseVO> convertProductWithWarehouseList(SeaOrderItemDTO seaorderitem){
		List<ProductWithWarehouseVO> productinfo = new ArrayList<>();
		//获取海淘供应商拆分信息
		List<SeaOrderItemWithSupplierDTO> seaOrderItemWithSupplierList= seaorderitem.getSeaOrderItemWithSupplierList();
		if(CollectionUtils.isNotEmpty(seaOrderItemWithSupplierList)){
			for(SeaOrderItemWithSupplierDTO supplier : seaOrderItemWithSupplierList){
				//供应商下面有多个仓库
				List<SeaOrderItemWithWarehouseDTO> warehouselist = supplier.getSeaOrderItemWithWarehouseList();
				if(CollectionUtils.isNotEmpty(warehouselist)){
					for(SeaOrderItemWithWarehouseDTO warhouse : warehouselist){
						productinfo.add(convertProductWithWarehouse(warhouse));
					}
				}
			}
		}
		return productinfo;
	}
	
	/**
	 * 封装提交订单页面的商品信息
	 * @param warhouse
	 * @return
	 */
	public static ProductWithWarehouseVO convertProductWithWarehouse(SeaOrderItemWithWarehouseDTO warhouse){
		ProductWithWarehouseVO pw = new ProductWithWarehouseVO();
		if(null != warhouse){
			pw.setChannel(warhouse.getSeaChannelName());
			pw.setChannelcode(warhouse.getSeaChannelCode());
			//编码为空--->国内直发
			if(StringUtil.isBlank(warhouse.getSeaChannelCode()) || StringUtil.isBlank(warhouse.getSeaChannelName())){
				pw.setChannelcode("GNZF");
				pw.setChannel(OrderConstant.OrderType.DOMESTIC.cnName);
			}
			pw.setFreight(NumberUtil.sfwr(warhouse.getTotalFreight()));
			pw.setPrice(NumberUtil.sfwr(warhouse.getTotalPayPrice()));
			pw.setItemsprice(NumberUtil.sfwr(warhouse.getTotalPrice()));
			pw.setStoragetype(StringUtil.getStrByObj(warhouse.getStorageType()));
			pw.setTaxes(NumberUtil.sfwr(warhouse.getTotalTaxfee()));
			pw.setWarehouseid(StringUtil.getStrByObj(warhouse.getWarehouseId()));
			pw.setWarehousename(warhouse.getWarehouseName());
			//海外直邮  税率免费
			if(StringUtil.equals(warhouse.getStorageType(), OrderConstant.OrderType.OVERSEASMAIL.code)){
				pw.setIsfreetax(StringUtil.ONE);
			}//保税区 小于50免邮
			/*else if(StringUtil.equals(warhouse.getStorageType(), SubOrderConstant.OrderType.BONDEDAREA.code)){
				//pw.setIsfreetax(warhouse.getTotalTaxfee() >50 ?StringUtil.ZERO:StringUtil.ONE);
			}*/
			else pw.setIsfreetax(StringUtil.ZERO);
			List<ProductVO> products = new ArrayList<>();
			List<CartLineDTO> cartlines = warhouse.getCartLineList();
			if(CollectionUtils.isNotEmpty(cartlines)){
				for(CartLineDTO cl : cartlines)
				products.add(CartConvert.convertCartLine(cl,false));
			}
			pw.setProducts(products);
		}
		return pw;
	}
	
	/**
	 * 封装订单列表入参
	 * @param order
	 * @return
	 */
	public static SubOrderQO convertPageOrderQuery(QueryOrder order){
		SubOrderQO  sq = new SubOrderQO();
		OrderConstant.ORDER_STATUS status =  convertOrderStatus(order.getType());
		if (!order.getType().equals(OrderEnum.QueryType.ALL.code))
		sq.setOrderStatus(status.code);
		sq.setMemberId(order.getUserid());
		sq.setPageSize(PageConstant.DEFAULT_PAGESIZE);
		sq.setStartPage(StringUtil.getCurpageDefault(order.getCurpage()));
		sq.setTypeList(new ArrayList<Integer>());
		OrderType[] orderTypes = OrderConstant.OrderType.values();
		for(OrderType orderType:orderTypes){
			if(!OrderConstant.FAST_ORDER_TYPE.equals(orderType.code)){
				sq.getTypeList().add(orderType.code);
			}
		}
		return sq;
	}
	
	
	/**
	 * 封装全部订单
	 * @param pageOrders
	 * @return
	 */
	public static Page<OrderVO> convertPageOrderList(PageInfo<OrderList4UserDTO> pageOrders){
		Page<OrderVO> pages = new Page<OrderVO>();
		if(null != pageOrders){
			List<OrderVO> l = new ArrayList<>();
			List<OrderList4UserDTO> rows = pageOrders.getRows();
			if(CollectionUtils.isNotEmpty(rows)){
				for (OrderList4UserDTO od : rows) {
					OrderVO ot = new OrderVO();
					if (od.getIsParent()) { //父订单
						ot = convertOrder(od);
					} else { //子订单
						ot = convertSubOrder(od);
					}
					l.add(ot);
				}
				pages.setFieldTCount(l, pageOrders.getPage(), pageOrders.getRecords());
			}
			pages.setCurpage(pageOrders.getPage());
		}
		return pages;
	}
	
	/**
	 * 封装父订单数据
	 * @param od
	 * @return
	 */
	public static OrderVO convertOrder(OrderList4UserDTO od){
		OrderVO vo = new OrderVO();
		OrderInfo order = od.getOrderInfo();
		if(null != order){
			// 海淘订单并待付款
			vo.setOrdercode(StringUtil.getStrByObj(order.getParentOrderCode()));
			vo.setOrdercount(StringUtil.getStrByObj(order.getQuantity()));
			vo.setOrderprice(NumberUtil.sfwr(order.getPayTotal()));
			vo.setOrdertime(DateUtil.formatDateTime(order.getCreateTime()));
			vo.setPayway(StringUtil.getStrByObj(order.getPayWay()));
			vo.setPaywaydesc(order.getPayWayStr());
			vo.setPaywaylist(order.getPayWayCodeListString());
			vo.setStatus(StringUtil.getStrByObj(order.getOrderStatus()));
			vo.setStatusdesc(order.getStatusStr());
			List<OrderItem> orderItemList = od.getOrderItemList();
			List<OrderLineTO> lines = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(orderItemList)){
				for(OrderItem oi : orderItemList){
					lines.add(convertOrderLine(oi,order.getOrderStatus()));
				}
				vo.setLines(lines);
			}
			vo.setLinecount(StringUtil.getStrByObj(lines.size()));
			//已支付 (待发货) 超过30分钟不允许取消
			if(null != order.getPayTime() && StringUtil.equals(order.getOrderStatus(), OrderConstant.ORDER_STATUS.DELIVERY.code)){
				int minutes = (int) ((new Date().getTime() - order.getPayTime().getTime()) / 1000 / 60); 
				vo.setIscancancel(minutes > 30?StringUtil.ZERO:StringUtil.ONE);
			}
		}
		return vo;
	}
	
	/**
	 * 封装子订单数据
	 * @param od
	 * @return
	 */
	public static OrderVO convertSubOrder(OrderList4UserDTO od){
		OrderVO vo = new OrderVO();
		SubOrder order = od.getSubOrder();
		if(null != order){
			vo.setOrdertype(StringUtil.getStrByObj(order.getType()));
			vo.setChannelid(StringUtil.getStrByObj(order.getSeaChannel()));
			vo.setOrdercode(StringUtil.getStrByObj(order.getOrderCode()));
			vo.setOrdercount(StringUtil.getStrByObj(order.getQuantity()));
			vo.setOrderprice(NumberUtil.sfwr(order.getPayTotal()));
			vo.setOrdertime(DateUtil.formatDateTime(order.getCreateTime()));
			vo.setPayway(StringUtil.getStrByObj(order.getPayWay()));
			vo.setPaywaydesc(order.getPayWayStr());
			vo.setPaywaylist(order.getPayWayCodeListString());
			vo.setStatus(StringUtil.getStrByObj(order.getOrderStatus()));
			vo.setStatusdesc(order.getStatusStr());
			List<OrderItem> orderItemList = od.getOrderItemList();
			List<OrderLineTO> lines = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(orderItemList)){
				for(OrderItem oi : orderItemList){
					lines.add(convertOrderLine(oi,order.getOrderStatus()));
				}
				vo.setLines(lines);
			}
			vo.setLinecount(StringUtil.getStrByObj(lines.size()));
			//已支付 (待发货) 超过30分钟不允许取消
			if(null != order.getPayTime() && StringUtil.equals(order.getOrderStatus(), OrderConstant.ORDER_STATUS.DELIVERY.code)){
				int minutes = (int) ((new Date().getTime() - order.getPayTime().getTime()) / 1000 / 60); 
				vo.setIscancancel(minutes > 30?StringUtil.ZERO:StringUtil.ONE);
			}
		}
		return vo;
	}
	
	/**
	 * 封装订单详情
	 * @return
	 */
	public static OrderDetailVO convertDetail(OrderDetails4UserDTO orderDetail,List<OrderRedeemItem> orderRedeemItemList){
		OrderDetailVO vo = new OrderDetailVO();
		if(null != orderDetail){
			if (orderDetail.getIsParent()) { //父订单
				vo = convertOrderDetail(orderDetail);
			} else { //子订单
				vo = convertSubOrderDetail(orderDetail);
			}
			List<OrderRedeemItemVo> orderRedeemItemVoList=new ArrayList<OrderRedeemItemVo>();
			Double   leftMoney=(double) 0;
			if(orderRedeemItemList!=null){
				for(OrderRedeemItem orderRedeemItem:orderRedeemItemList){
					byte[] redeemCodeBase64=Base64Util.decrypt(orderRedeemItem.getRedeemCode());//将base64转化为明码
					try {
					  String 	redeemCode = new String(redeemCodeBase64, "UTF-8");
					  orderRedeemItem.setRedeemCode(redeemCode);//设置兑换码明码
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();  
					}
					OrderRedeemItemVo  orderRedeemItemVo=new OrderRedeemItemVo();
					BeanUtils.copyProperties(orderRedeemItem, orderRedeemItemVo);//对象属性拷贝
					if(PaymentConstant.REDEEM_CODE_STATUS.NO_USE.code.equals(orderRedeemItem.getRedeemCodeState())){//未使用状态可以退回金额
						leftMoney=leftMoney+orderRedeemItem.getSalesPrice();//可退金额
					}
					orderRedeemItemVoList.add(orderRedeemItemVo);
				}
				vo.setLeftMoney(String.valueOf(leftMoney));//可退回金额
				vo.setOrderRedeemItemList(orderRedeemItemVoList);//兑换码
			}
			
		}
		return vo;
	}
	
	/**
	 * 封装父订单详情数据
	 * @param od
	 * @return
	 */
	public static OrderDetailVO convertOrderDetail(OrderDetails4UserDTO od){
		OrderDetailVO vo = new OrderDetailVO();
		OrderInfo order = od.getOrderInfo();
		if(null != order){
			vo.setOrdercode(StringUtil.getStrByObj(order.getParentOrderCode()));
			vo.setOrdercount(StringUtil.getStrByObj(order.getQuantity()));
			vo.setOrderprice(NumberUtil.sfwr(order.getPayTotal()));
			vo.setOrdertime(DateUtil.formatDateTime(order.getCreateTime()));
			vo.setPayway(StringUtil.getStrByObj(order.getPayWay()));
			vo.setPaywaydesc(order.getPayWayStr());
			vo.setPaywaylist(order.getPayWayCodeListString());
			vo.setStatus(StringUtil.getStrByObj(order.getOrderStatus()));
			vo.setStatusdesc(order.getStatusStr());
			convertOrderConsignee(od.getOrderConsignee(),vo);//SET收货人信息
			vo.setFreight(NumberUtil.sfwr(order.getFreight()));
			vo.setTaxes(NumberUtil.sfwr(order.getTaxTotal()));
			vo.setDisprice(NumberUtil.sfwr(order.getDiscountTotal()));
			vo.setBaseprice(NumberUtil.sfwr(order.getPayTotal()));
			vo.setTotalpoint(NumberUtil.sfwr(BigDecimalUtil.toPrice(BigDecimalUtil.divide(order.getTotalPoint(),100,2))));
			List<OrderItem> orderItemList = od.getOrderItemList();
			List<OrderLineTO> lines = new ArrayList<>();
			BigDecimal orgTaxFee = BigDecimal.ZERO;
			if(CollectionUtils.isNotEmpty(orderItemList)){
				for(OrderItem oi : orderItemList){
					lines.add(convertOrderLine(oi,order.getOrderStatus()));
					if (!OrderConstant.OrderType.DOMESTIC.getCode().equals(oi.getSalesType())
							   && !ClearanceChannelsEnum.HWZY.id.equals(oi.getSeaChannel())){
						orgTaxFee = orgTaxFee.add(
								new BigDecimal(oi.getSalesPrice()).multiply(new BigDecimal(oi.getQuantity()))
								 .multiply(new BigDecimal(oi.getTaxRate())).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP));
						}
				}
				vo.setLines(lines);
			}
			vo.setTaxes(NumberUtil.sfwr(orgTaxFee.setScale(2, RoundingMode.HALF_UP).doubleValue()));
			vo.setLinecount(StringUtil.getStrByObj(lines.size()));
			//已支付 (待发货) 超过30分钟不允许取消
			if(null != order.getPayTime() && StringUtil.equals(order.getOrderStatus(), OrderConstant.ORDER_STATUS.DELIVERY.code)){
				int minutes = (int) ((new Date().getTime() - order.getPayTime().getTime()) / 1000 / 60); 
				vo.setIscancancel(minutes > 30?StringUtil.ZERO:StringUtil.ONE);
			}
		}
		return vo;
	}
	
	/**
	 * 封装子订单详情数据
	 * @param od
	 * @return
	 */
	public static OrderDetailVO convertSubOrderDetail(OrderDetails4UserDTO od){
		OrderDetailVO vo = new OrderDetailVO();
		SubOrder order = od.getSubOrder();
		if(null != order){
			vo.setOrdertype(StringUtil.getStrByObj(order.getType()));
			vo.setChannelid(StringUtil.getStrByObj(order.getSeaChannel()));
			vo.setOrdercode(StringUtil.getStrByObj(order.getOrderCode()));
			vo.setOrdercount(StringUtil.getStrByObj(order.getQuantity()));
			vo.setOrderprice(NumberUtil.sfwr(order.getPayTotal()));
			vo.setOrdertime(DateUtil.formatDateTime(order.getCreateTime()));
			vo.setPayway(StringUtil.getStrByObj(order.getPayWay()));
			vo.setPaywaydesc(order.getPayWayStr());
			vo.setPaywaylist(order.getPayWayCodeListString());
			vo.setStatus(StringUtil.getStrByObj(order.getOrderStatus()));
			vo.setStatusdesc(order.getStatusStr());
			convertOrderConsignee(od.getOrderConsignee(),vo);//SET收货人信息
			vo.setFreight(NumberUtil.sfwr(order.getFreight()));
			vo.setReceiveTel(order.getReceiveTel());//接收号码
			vo.setTaxes(NumberUtil.sfwr(order.getOrgTaxFee()));
			vo.setDisprice(NumberUtil.sfwr(order.getDiscount()));
			vo.setBaseprice(NumberUtil.sfwr(order.getPayTotal()));
			vo.setTotalpoint(NumberUtil.sfwr(BigDecimalUtil.toPrice(BigDecimalUtil.divide(order.getPoints(),100,2))));;
			List<OrderItem> orderItemList = od.getOrderItemList();
			List<OrderLineTO> lines = new ArrayList<>();
			BigDecimal orgTaxFee = BigDecimal.ZERO;
			if(CollectionUtils.isNotEmpty(orderItemList)){
				for(OrderItem oi : orderItemList){
					lines.add(convertOrderLine(oi,order.getOrderStatus()));
					if (!OrderConstant.OrderType.DOMESTIC.getCode().equals(order.getType())
							   && !ClearanceChannelsEnum.HWZY.id.equals(order.getSeaChannel())){
						orgTaxFee = orgTaxFee.add(
								new BigDecimal(oi.getSalesPrice()).multiply(new BigDecimal(oi.getQuantity()))
								 .multiply(new BigDecimal(oi.getTaxRate())).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP));
						}
				}
				vo.setLines(lines);
			}
			vo.setTaxes(NumberUtil.sfwr(orgTaxFee.setScale(2, RoundingMode.HALF_UP).doubleValue()));
			vo.setLinecount(StringUtil.getStrByObj(lines.size()));
			//已支付 (待发货) 超过30分钟不允许取消
			if(null != order.getPayTime() && StringUtil.equals(order.getOrderStatus(), OrderConstant.ORDER_STATUS.DELIVERY.code)){
				int minutes = (int) ((new Date().getTime() - order.getPayTime().getTime()) / 1000 / 60); 
				vo.setIscancancel(minutes > 30?StringUtil.ZERO:StringUtil.ONE);
			}
		}
		return vo;
	}
	
	/**
	 * 封装订单的收货人信息
	 * @param orderConsignee
	 * @return
	 */
	public static void convertOrderConsignee(OrderConsignee orderConsignee,OrderDetailVO detai){
		if(null != orderConsignee){
			StringBuffer sb = new StringBuffer();
			sb.append(orderConsignee.getProvinceName());
			sb.append(orderConsignee.getCityName());
			sb.append(orderConsignee.getCountyName());
			sb.append(orderConsignee.getTownName());
			sb.append(orderConsignee.getAddress());
			detai.setAddress(sb.toString());
			detai.setName(orderConsignee.getName());
			detai.setTel(orderConsignee.getMobile());
		}
	}
	
	/**
	 * 封装订单行
	 * @param oi
	 * @return
	 */
	public static OrderLineTO convertOrderLine(OrderItem oi,Integer orderStatus){
		OrderLineTO ol = new OrderLineTO();
		if(null != oi){
			ol.setLineid(StringUtil.getStrByObj(oi.getId()));
			ol.setCount(StringUtil.getStrByObj(oi.getQuantity()));
			ol.setImgurl(ImgHelper.getImgUrl(oi.getImg(), ImgEnum.Width.WIDTH_180));
			ol.setLineprice(NumberUtil.sfwr(BigDecimalUtil.toPrice(BigDecimalUtil.multiply(oi.getSalesPrice(), oi.getQuantity()))));
			ol.setName(oi.getSpuName());
			ol.setPrice(NumberUtil.sfwr(oi.getSalesPrice()));
			ol.setSku(oi.getSkuCode());
			ol.setTid(StringUtil.getStrByObj(oi.getTopicId()));
			ol.setSpecs(execSalePorper(oi.getSalePropertyList()));
			ol.setCommision(NumberUtil.sfwr(oi.getCommision()));
			if(StringUtil.equals(orderStatus, OrderConstant.ORDER_STATUS.FINISH.code) && oi.getRefundStatus() == null)ol.setIsreturn(StringUtil.ZERO);
			else{
				if(StringUtil.equals(oi.getRefundStatus(), RejectConstant.REJECT_STATUS.CANCELED.code)||StringUtil.equals(oi.getRefundStatus(), RejectConstant.REJECT_STATUS.REJECTFAIL.code))ol.setIsreturn(StringUtil.ZERO);
				else ol.setIsreturn(StringUtil.ONE);
			} 
		}
		return ol;
	}
	
	/**
	 * 封装订单行规格
	 * @param content
	 * @return
	 */
	public static List<String> execSalePorper(List<SalePropertyDTO> salePropertyList){
		return ShoppingCartConvert.convertProductSpecs(salePropertyList);
//		List<String> rsList = new ArrayList<String>();
//		if(StringUtil.isNotBlank(content)){
//			List<Map<String, String>> mlist = JsonUtil.jsonStringToList(content);
//			for(Map<String, String> m : mlist){
//				Collection<String> l = m.values();
//				String vals = "";
//				for(String s1 : l)
//					vals += s1+":";
//				rsList.add(vals.substring(0,vals.length()-1));
//			}
//		}
//		return rsList;
	}
	
	/**
	 * 封装订单状态
	 * @return
	 */
	public static OrderConstant.ORDER_STATUS convertOrderStatus(String type){
		if(type.equals(OrderEnum.QueryType.UNPAY.code))return OrderConstant.ORDER_STATUS.PAYMENT;
		if(type.equals(OrderEnum.QueryType.UNRECEIPT.code))return OrderConstant.ORDER_STATUS.RECEIPT;
		if(type.equals(OrderEnum.QueryType.UNUSE.code))return OrderConstant.ORDER_STATUS.UNUSE;
		return null;
	}
	
	/**
	 * 封装物流信息
	 * @param orderDelivery
	 * @return
	 */
	public static LogisticVO convertLogistic(SubOrderExpressInfoDTO expressInfo){
		LogisticVO lv = new LogisticVO();
		if(null != expressInfo){
			lv.setCompany(expressInfo.getCompanyName());
			lv.setLogcode(expressInfo.getPackageNo());
			List<ExpressLogInfoDTO> expressLogInfoList = expressInfo.getExpressLogInfoDTOList();
			if(CollectionUtils.isNotEmpty(expressLogInfoList)){
				List<LogDetailTO> loglist = new ArrayList<>();
				for(ExpressLogInfoDTO ei : expressLogInfoList){
					loglist.add(new LogDetailTO(ei.getContext(),ei.getDataTime()));
				}
				lv.setLoglist(loglist);
			}
		}
		return lv;
	}
	
	/**
	 * 封装物流公司列表
	 * @param expressInfolist
	 * @return
	 */
	public static List<LogisticCompanyVO> convertLogisticCompany(List<ExpressInfo> expressInfolist){
		List<LogisticCompanyVO> list = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(expressInfolist)){
			for(ExpressInfo expressInfo : expressInfolist){
				list.add(new LogisticCompanyVO(expressInfo.getCode(),expressInfo.getName()));
			}
		}
		return list;
	}
	
	private static void initChannelTrack(OrderInitDto orderInitDto,QueryOrder order){
		if(StringUtil.isNotBlank(order.getChannelcode())){
			OrderChannelTrack orderChannelTrack = new OrderChannelTrack();
			orderChannelTrack.setChannelCode(order.getChannelcode());
			orderChannelTrack.setSource(order.getChannelsource());
			orderChannelTrack.setClientCode(order.getClientcode());
			orderChannelTrack.setDistributeCode(order.getDistributecode());
			orderChannelTrack.setSessionId(order.getToken());
			orderInitDto.setOrderChannelTrack(orderChannelTrack);
		}
	}
}
