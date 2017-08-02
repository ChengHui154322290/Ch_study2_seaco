package com.tp.service.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ImageDownUtil;
import com.tp.common.util.OrderUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.ord.MemRealinfoDao;
import com.tp.dao.ord.OrderConsigneeDao;
import com.tp.dao.ord.OrderDeliveryDao;
import com.tp.dao.ord.OrderInfoDao;
import com.tp.dao.ord.OrderItemDao;
import com.tp.dao.ord.OrderPromotionDao;
import com.tp.dao.ord.OrderReceiptDao;
import com.tp.dao.ord.OrderStatusLogDao;
import com.tp.dao.ord.SubOrderDao;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SalesOrderQO;
import com.tp.result.ord.OrderResult;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderInfoService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

@Service
public class OrderInfoService extends BaseService<OrderInfo> implements IOrderInfoService {

	@Autowired
	private OrderInfoDao orderInfoDao;
	@Autowired
	private SubOrderDao subOrderDao;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private OrderDeliveryDao orderDeliveryDao;
	@Autowired
	private OrderPromotionDao orderPromotionDao;
	@Autowired
	private OrderReceiptDao orderReceiptDao;
	@Autowired
	private OrderStatusLogDao orderStatusDao;
	@Autowired
	private OrderConsigneeDao orderConsigneeDao;
	@Autowired
	private MemRealinfoDao memRealinfoDao;
	@Autowired
	private OrderStatusLogDao orderStatusLogDao;
	
	@Override
	public BaseDao<OrderInfo> getDao() {
		return orderInfoDao;
	}

	@Override
	public OrderResult queryOrderResultByCode(Long orderCode) {
		Boolean isParent = OrderUtils.isOrderCode(orderCode);
		Boolean isSub = OrderUtils.isSubOrderCode(orderCode);
		Map<String,Object> params = new HashMap<String,Object>();
		OrderInfo orderInfo = null;
		OrderResult orderResult = new OrderResult();
		if(isParent){
			params.put("parentOrderCode", orderCode);
			orderInfo = super.queryUniqueByParams(params);
			if(orderInfo!=null){
				List<OrderItem> orderItemList = wrapItemMainImage(orderItemDao.queryByParam(params));
				List<OrderPromotion> orderPromotionList = orderPromotionDao.queryByParam(params);
				MemRealinfo memRealinfo = memRealinfoDao.queryByParam(params).get(0);
				List<OrderStatusLog> orderStatusLogList = orderStatusLogDao.queryByParam(params);
				OrderDelivery orderDelivery = orderDeliveryDao.queryByParam(params).get(0);
				orderResult.setOrderInfo(orderInfo);
				orderResult.setOrderItemList(orderItemList);
				orderResult.setOrderPromotionList(orderPromotionList);
				orderResult.setMemRealinfo(memRealinfo);
				orderResult.setOrderStatusLogList(orderStatusLogList);
				orderResult.setDelivery(orderDelivery);
			}
		}else if(isSub){
			SubOrder subOrder = subOrderDao.selectOneByCode(orderCode);
			if(subOrder!=null){
				params.put("parentOrderCode", subOrder.getParentOrderCode());
				orderInfo = super.queryUniqueByParams(params);
				params.put("orderCode", orderCode);
				List<OrderItem> orderItemList = wrapItemMainImage(orderItemDao.queryByParam(params));
				List<OrderPromotion> orderPromotionList = orderPromotionDao.queryByParam(params);
				MemRealinfo memRealinfo = memRealinfoDao.queryByParam(params).get(0);
				List<OrderStatusLog> orderStatusLogList = orderStatusLogDao.queryByParam(params);
				OrderDelivery orderDelivery = orderDeliveryDao.queryByParam(params).get(0);
				orderResult.setOrderInfo(orderInfo);
				orderResult.setOrderItemList(orderItemList);
				orderResult.setOrderPromotionList(orderPromotionList);
				orderResult.setMemRealinfo(memRealinfo);
				orderResult.setOrderStatusLogList(orderStatusLogList);
				orderResult.setDelivery(orderDelivery);
			}
		}
		return orderResult;
	}

	@Override
	public OrderResult queryOrderById(Long orderId) {
		OrderResult orderResult = new OrderResult();
		OrderInfo orderInfo = super.queryById(orderId);
		if(orderInfo!=null){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("parentOrderCode", orderInfo.getParentOrderCode());
			List<OrderItem> orderItemList = wrapItemMainImage(orderItemDao.queryByParam(params));
			List<OrderPromotion> orderPromotionList = orderPromotionDao.queryByParam(params);
			MemRealinfo memRealinfo = memRealinfoDao.queryByParam(params).get(0);
			List<OrderStatusLog> orderStatusLogList = orderStatusLogDao.queryByParam(params);
			OrderDelivery orderDelivery = orderDeliveryDao.queryByParam(params).get(0);
			orderResult.setOrderInfo(orderInfo);
			orderResult.setOrderItemList(orderItemList);
			orderResult.setOrderPromotionList(orderPromotionList);
			orderResult.setMemRealinfo(memRealinfo);
			orderResult.setOrderStatusLogList(orderStatusLogList);
			orderResult.setDelivery(orderDelivery);
		}
		return orderResult;
	}
	
	public OrderResult queryOrderByParam(Map<String,Object> params){
		OrderResult orderResult = new OrderResult();
		OrderInfo orderInfo = super.queryUniqueByParams(params);
		if(orderInfo!=null){
			params.clear();
			params.put("parentOrderCode", orderInfo.getParentOrderCode());
			List<OrderItem> orderItemList = wrapItemMainImage(orderItemDao.queryByParam(params));
			List<OrderPromotion> orderPromotionList = orderPromotionDao.queryByParam(params);
			MemRealinfo memRealinfo = memRealinfoDao.queryByParam(params).get(0);
			List<OrderStatusLog> orderStatusLogList = orderStatusLogDao.queryByParam(params);
			OrderDelivery orderDelivery = orderDeliveryDao.queryByParam(params).get(0);
			orderResult.setOrderInfo(orderInfo);
			orderResult.setOrderItemList(orderItemList);
			orderResult.setOrderPromotionList(orderPromotionList);
			orderResult.setMemRealinfo(memRealinfo);
			orderResult.setOrderStatusLogList(orderStatusLogList);
			orderResult.setDelivery(orderDelivery);
		}
		return orderResult;
	}
	@Override
	public List<OrderInfo> selectListByIdList(List<Long> idList) {
		if (CollectionUtils.isNotEmpty(idList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(idList, Constant.SPLIT_SIGN.COMMA)+")");
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc ");
			return orderInfoDao.queryByParam(params);
		} else {
			return new ArrayList<OrderInfo>(0);
		}
	}

	@Override
	public OrderInfo selectOneByCode(Long code) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", code);
		return super.queryUniqueByParams(params);
	}

	@Override
	@Transactional(readOnly = true)
	public PageInfo<OrderInfo> selectPageByQO(SalesOrderQO qo) {
		Map<String,Object> params = BeanUtil.beanMap(qo);
		params.remove("startPage");
		params.remove("pageSize");
		if(params.get("statusList")!=null){
			params.remove("statusList");
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " order_status in ("+StringUtil.join(qo.getStatusList(), Constant.SPLIT_SIGN.COMMA)+")");
		}
		return super.queryPageByParams(params, new PageInfo<OrderInfo>(qo.getStartPage(),qo.getPageSize()));
	}

	@Override
	public Integer deleteByCode(Long code, Long memberId) {
		OrderInfo order = new OrderInfo();
		order.setParentOrderCode(code);
		order.setMemberId(memberId);
		order.setDeleted(Constant.DELECTED.YES);
		int count = 0;
		count += orderInfoDao.deleteByCode(order);
		
		SubOrder sub = new SubOrder();
		sub.setOrderCode(code);
		sub.setMemberId(memberId);
		sub.setDeleted(Constant.DELECTED.YES);
		count += subOrderDao.deleteByOrderCode(sub);
		
		return count;
	}

	@Override
	public List<OrderInfo> querySalesOrderByUnPayIsExpired(int minute) {
		return orderInfoDao.querySalesOrderByUnPayIsExpired(minute);
	}
	
	@Override
	public List<OrderInfo> querySalesOrderByUnPayOverFifteenMinutes(int minute) {
		return orderInfoDao.querySalesOrderByUnPayOverFifteenMinutes(minute);
	}

	@Override
	public List<OrderInfo> getOrderNeedPushToCMBC(List<Integer> statuslist){
		return orderInfoDao.getOrderNeedPushToCMBC(statuslist);
	}

	
    @Override
    public List<OrderInfo> selectByCodeAndMemberID(Long orderCode, Long memberId) {
    	Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", orderCode);
		params.put("memberId", memberId);
		return orderInfoDao.queryByParam(params);
    }
    
    public List<OrderItem> wrapItemMainImage(List<OrderItem> orderItemList){
		if(CollectionUtils.isNotEmpty(orderItemList)){
			for(OrderItem orderItem:orderItemList){
				orderItem.setImg(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, orderItem.getImg()));
			}
		}
		return orderItemList;
	}
    
    @Override
	@Transactional
	public Boolean updateOrderSplitAmount(List<SubOrder> subOrderList) {
    	for(SubOrder subOrder:subOrderList){
    		List<OrderItem> orderItemList = subOrder.getOrderItemList();
    		for(OrderItem orderItem:orderItemList){
    			orderItemDao.updateOrderItemAmount(orderItem);
    		}
    	}
		return Boolean.TRUE;
	}
}
