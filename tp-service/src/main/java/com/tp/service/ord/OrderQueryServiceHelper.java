package com.tp.service.ord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.tp.common.vo.StorageConstant.InvoiceType;
import com.tp.dto.ord.remote.ReceiptDetailDTO;
import com.tp.model.BaseDO;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderReceipt;
import com.tp.model.ord.ReceiptDetail;
import com.tp.model.ord.SubOrder;

/**
 * 服务帮助类
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderQueryServiceHelper {
	
	private static final Logger log = Logger.getLogger(OrderQueryServiceHelper.class);
	/**
	 * 提取父订单的ID列表
	 * 
	 * @param list
	 * @return
	 */
	public static <T extends BaseDO> List<Long> extractOrderIdList(List<T> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			List<Long> idList = new ArrayList<Long>(list.size());
			for (T obj : list) {
				if (obj instanceof SubOrder) {
					idList.add(((SubOrder) obj).getParentOrderId());
				} else if (obj instanceof OrderInfo) {
					idList.add(((OrderInfo) obj).getId());
				}
			}
			return idList;
		}
		return new ArrayList<Long>(0);
	}
	
	/**
	 * 提取子订单编号列表
	 * 
	 * @param list
	 * @return
	 */
	public static <T extends BaseDO> List<Long> extractSubCodeList(List<T> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			List<Long> subCodeList = new ArrayList<Long>(list.size());
			for (T obj : list) {
				if (obj instanceof SubOrder) {
					subCodeList.add(((SubOrder) obj).getOrderCode());
				}
			}
			return subCodeList;
		}
		return new ArrayList<Long>(0);
	}
	
	/**
	 * 将DO转成DTO
	 * 
	 * @param sub
	 * @return
	 */
	@Deprecated
	public static <T extends BaseDO, R extends T> R convert2DTO(T source, Class<R> targetClass) {
		if (null != source) {
			try {
				R target = targetClass.newInstance();
				BeanUtils.copyProperties(source, target);
				return target;
			} catch (Exception e) {
				log.error("DO与DTO转换错误", e);
			}
		}
		return null;
	}

	/**
	 * 父订单列表转MAP（key - id）
	 * 
	 * @param orderList
	 * @return
	 */
	public static Map<Long, OrderInfo> toOrderMap_id(List<OrderInfo> orderList) {
		if (CollectionUtils.isNotEmpty(orderList)) {
			Map<Long, OrderInfo> map = new HashMap<Long, OrderInfo>();
			for (OrderInfo dto : orderList) {
				map.put(dto.getId(), dto);
			}
			return map;
		}
		return new HashMap<Long, OrderInfo>(0);
	}

	/**
	 * 订单行转MAP（key - 子订单ID）
	 * 
	 * @param lineList
	 * @return
	 */
	public static Map<Long, List<OrderItem>> toLineMap_subId(List<OrderItem> lineList) {
		if (CollectionUtils.isNotEmpty(lineList)) {
			Map<Long, List<OrderItem>> map = new HashMap<Long, List<OrderItem>>();
			for (OrderItem line : lineList) {
				List<OrderItem> list = map.get(line.getOrderId());
				if (null == list) {
					list = new ArrayList<OrderItem>();
					map.put(line.getOrderId(), list);
				}
				list.add(line);
			}
			return map;
		}
		return new HashMap<Long, List<OrderItem>>(0);
	}
	
	/**
	 * 订单行转MAP（key - 父订单ID）
	 * 
	 * @param lineList
	 * @return
	 */
	public static Map<Long, List<OrderItem>> toLineMapParentorderId(List<OrderItem> lineList) {
		if (CollectionUtils.isNotEmpty(lineList)) {
			Map<Long, List<OrderItem>> map = new HashMap<Long, List<OrderItem>>();
			for (OrderItem line : lineList) {
				List<OrderItem> list = map.get(line.getParentOrderId());
				if (null == list) {
					list = new ArrayList<OrderItem>();
					map.put(line.getParentOrderId(), list);
				}
				list.add(line);
			}
			return map;
		}
		return new HashMap<Long, List<OrderItem>>(0);
	}


	/**
	 * 物流信息转MAP（key - 子订单code）
	 * 
	 * @param deliveryList
	 * @return
	 */
	public static Map<Long, OrderDelivery> toDeliveryMap_subCode(List<OrderDelivery> deliveryList) {
		if (CollectionUtils.isNotEmpty(deliveryList)) {
			Map<Long, OrderDelivery> map = new HashMap<Long, OrderDelivery>();
			for (OrderDelivery dto : deliveryList) {
				map.put(dto.getOrderCode(), dto);
			}
			return map;
		}
		return new HashMap<Long, OrderDelivery>(0);
	}


	/**
	 * 子订单列表转MAP（key - 父订单ID）
	 * 
	 * @param subList
	 * @return
	 */
	public static Map<Long, List<SubOrder>> toSubMap_orderId(List<SubOrder> subList) {
		if (CollectionUtils.isNotEmpty(subList)) {
			Map<Long, List<SubOrder>> map = new HashMap<Long, List<SubOrder>>();
			for (SubOrder sub : subList) {
				List<SubOrder> list = map.get(sub.getParentOrderId());
				if (null == list) {
					list = new ArrayList<SubOrder>();
					map.put(sub.getParentOrderId(), list);
				}
				list.add(sub);
			}
			return map;
		}
		return new HashMap<Long, List<SubOrder>>(0);
	}

	/**
	 * 收货人列表转MAP（key - 父订单ID）
	 * 
	 * @param consigneeList
	 * @return
	 */
	public static Map<Long, OrderConsignee> toConsigneeMap_orderId(List<OrderConsignee> consigneeList) {
		if (CollectionUtils.isNotEmpty(consigneeList)) {
			Map<Long, OrderConsignee> map = new HashMap<Long, OrderConsignee>();
			for (OrderConsignee con : consigneeList) {
				map.put(con.getParentOrderId(), con);
			}
			return map;
		}
		return new HashMap<Long, OrderConsignee>(0);
	}

	/**
	 * 提取
	 * 
	 * @param promotionList
	 * @return
	 */
	public static List<Long> extractSubIdListFromPromotion(List<OrderPromotion> promotionList) {
		if (CollectionUtils.isNotEmpty(promotionList)) {
			List<Long> idList = new ArrayList<Long>(promotionList.size());
			for (OrderPromotion promotion : promotionList) {
				idList.add(promotion.getOrderId());
			}
			return idList;
		}
		return new ArrayList<Long>(0);
	}

	/**
	 * 促销列表转MAP - key 子订单ID
	 * 
	 * @param promotionList
	 * @return
	 */
	public static Map<Long, List<OrderPromotion>> toPromotionMap_subId(List<OrderPromotion> promotionList) {
		if (CollectionUtils.isNotEmpty(promotionList)) {
			Map<Long, List<OrderPromotion>> map = new HashMap<Long, List<OrderPromotion>>();
			for (OrderPromotion pro : promotionList) {
				List<OrderPromotion> list = map.get(pro.getOrderId());
				if (null == list) {
					list = new ArrayList<OrderPromotion>();
					map.put(pro.getOrderId(), list);
				}
				list.add(pro);
			}
			return map;
		}
		return new HashMap<Long, List<OrderPromotion>>(0);
	}

	/**
	 * 子订单列表转MAP - key 子订单ID
	 * 
	 * @param subList
	 * @return
	 */
	public static Map<Long, SubOrder> toSubMap_id(List<SubOrder> subList) {
		if (CollectionUtils.isNotEmpty(subList)) {
			Map<Long, SubOrder> map = new HashMap<Long, SubOrder>();
			for (SubOrder sub : subList) {
				map.put(sub.getId(), sub);
			}
			return map;
		}
		return new HashMap<Long, SubOrder>(0);
	}

	/**
	 * 提取父订单编号列表
	 * 
	 * @param subList
	 * @return
	 */
	public static List<Long> extractParentOrderCodeList(List<SubOrder> subList) {
		Set<Long> orderCodeSet = new HashSet<Long>();
		if (CollectionUtils.isNotEmpty(subList)) {
			for (SubOrder sub : subList) {
				orderCodeSet.add(sub.getParentOrderCode());
			}
			return new ArrayList<Long>(orderCodeSet);
		} else {
			return new ArrayList<Long>(0);
		}
	}

	/**
	 * 转会员实名map
	 * 
	 * @param realinfoList
	 * @return
	 */
	public static Map<Long, MemRealinfo> toRealinfoMap_orderCode(List<MemRealinfo> realinfoList) {
		if (CollectionUtils.isNotEmpty(realinfoList)) {
			Map<Long, MemRealinfo> map = new HashMap<Long, MemRealinfo>();
			for (MemRealinfo mem : realinfoList) {
				map.put(mem.getParentOrderCode(), mem);
			}
			return map;
		}
		return new HashMap<Long, MemRealinfo>(0);
	}
	
	private static final char COMMA = ',';

	/**
	 * 转ReceiptDetailDTO
	 * 
	 * @param receiptList
	 * @return
	 */
	public static ReceiptDetailDTO convert2ReceiptDetailDTO(List<ReceiptDetail> receiptList) {
		if (CollectionUtils.isNotEmpty(receiptList)) {
			StringBuilder numberSb = new StringBuilder();	// 发票号码
			for (Iterator<ReceiptDetail> it = receiptList.iterator(); it.hasNext();) {
				ReceiptDetail receipt = it.next();
				if (null != receipt && StringUtils.isNotBlank(receipt.getReceiptNo())) {
					numberSb.append(receipt.getReceiptNo());
				}
				if (it.hasNext()) {
					numberSb.append(COMMA);
				}
			}
			
			ReceiptDetailDTO receiptDetail = new ReceiptDetailDTO();
			receiptDetail.setNumberStr(numberSb.toString());
			receiptDetail.setTypeStr(InvoiceType.getDescByCode(receiptList.get(0).getType()));
			return receiptDetail;
		}
		
		return null;
	}

	/**
	 * 转发票map
	 * 
	 * @param receiptList
	 * @return
	 */
	public static Map<Long, OrderReceipt> toReceiptMap_orderId(List<OrderReceipt> receiptList) {
		if (CollectionUtils.isNotEmpty(receiptList)) {
			Map<Long, OrderReceipt> map = new HashMap<Long, OrderReceipt>();
			for (OrderReceipt receipt : receiptList) {
				map.put(receipt.getParentOrderId(), receipt);
			}
			return map;
		}
		return Collections.emptyMap();
	}
}
