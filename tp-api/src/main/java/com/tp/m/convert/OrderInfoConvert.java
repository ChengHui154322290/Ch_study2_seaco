package com.tp.m.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.SalePropertyDTO;
import com.tp.dto.ord.remote.OrderDetails4UserDTO;
import com.tp.dto.ord.remote.OrderList4UserDTO;
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
import com.tp.m.vo.order.LogisticCompanyVO;
import com.tp.m.vo.order.LogisticVO;
import com.tp.m.vo.order.OrderDetailVO;
import com.tp.m.vo.order.OrderVO;
import com.tp.m.vo.order.SubmitOrderInfoVO;
import com.tp.m.vo.product.ProductWithWarehouseVO;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.ExpressLogInfoDTO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.util.BigDecimalUtil;

/**
 * 订单封装类
 * @author szy
 * @2016年1月7日 下午5:31:28
 */
public class OrderInfoConvert {
	
	/**
	 * 封装提交订单页面信息
	 * @return
	 */
	public static SubmitOrderInfoVO convertSubmitOrderInfo(OrderInitDto orderInitDto,QueryOrder order){
		SubmitOrderInfoVO vo = new SubmitOrderInfoVO();
		if(null != orderInitDto){
			vo.setItemsprice(NumberUtil.sfwr(orderInitDto.getOrginItemAmount()));
			vo.setFreight(NumberUtil.sfwr(orderInitDto.getOrginFreight()));
			vo.setTaxes(NumberUtil.sfwr(orderInitDto.getOrginTaxFee()));
			vo.setPrice(NumberUtil.sfwr(orderInitDto.getSummation()));
			vo.setTotalcoupon(NumberUtil.sfwr(orderInitDto.getDiscountTotal()));
			vo.setIsfirstminus(orderInitDto.getFirstMinus()?StringUtil.ONE:StringUtil.ZERO);
			vo.setTotalpoint(StringUtil.getStrByObj(orderInitDto.getTotalPoint()));
			vo.setUsedpoint(StringUtil.getStrByObj(BigDecimalUtil.divide(orderInitDto.getUsedPoint(), 100,6).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			vo.setUsedpointsign(StringUtil.getStrByObj(order.getUsedPointSign()));
			vo.setFirstcoupon("5");//首单立减5元
			vo.setReceiveTel(order.getReceiveTel());
			List<ProductWithWarehouseVO> plist = ShoppingCartConvert.convertProductWithWarehouseList(orderInitDto.getPreSubOrderList(),false);
			vo.setProductinfo(plist);
			//设置支付方式
			if(CollectionUtils.isNotEmpty(plist)){
				vo.setPaywaylist(PayConvert.counvertPayways(order));
			}
		}
		return vo;
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
	public static OrderDetailVO convertDetail(OrderDetails4UserDTO orderDetail){
		OrderDetailVO vo = new OrderDetailVO();
		if(null != orderDetail){
			if (orderDetail.getIsParent()) { //父订单
				vo = convertOrderDetail(orderDetail);
			} else { //子订单
				vo = convertSubOrderDetail(orderDetail);
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
			//vo.setTaxes(order.get);
			vo.setDisprice(NumberUtil.sfwr(order.getDiscount()));
			vo.setBaseprice(NumberUtil.sfwr(order.getPayTotal()));
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
			vo.setStatus(StringUtil.getStrByObj(order.getOrderStatus()));
			vo.setStatusdesc(order.getStatusStr());
			convertOrderConsignee(od.getOrderConsignee(),vo);//SET收货人信息
			vo.setFreight(NumberUtil.sfwr(order.getFreight()));
			vo.setTaxes(NumberUtil.sfwr(order.getTaxFee()));
			vo.setDisprice(NumberUtil.sfwr(order.getDiscount()));
			vo.setBaseprice(NumberUtil.sfwr(order.getPayTotal()));
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
			ol.setLineprice(NumberUtil.sfwr(oi.getSubTotal()));
			ol.setName(oi.getSpuName());
			ol.setPrice(NumberUtil.sfwr(oi.getPrice()));
			ol.setSku(oi.getSkuCode());
			ol.setTid(StringUtil.getStrByObj(oi.getTopicId()));
			ol.setSpecs(execSalePorper(oi.getSalePropertyList()));
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
	public static List<String> execSalePorper(List<SalePropertyDTO> properties){
		return ShoppingCartConvert.convertProductSpecs(properties);
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
}
