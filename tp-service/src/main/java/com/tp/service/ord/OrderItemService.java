package com.tp.service.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ImageDownUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.ord.OrderItemDao;
import com.tp.model.ord.OrderItem;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderItemService;
import com.tp.util.StringUtil;

@Service
public class OrderItemService extends BaseService<OrderItem> implements IOrderItemService {

	@Autowired
	private OrderItemDao orderItemDao;
	
	@Override
	public BaseDao<OrderItem> getDao() {
		return orderItemDao;
	}
	@Override
	public List<OrderItem> selectListByParentOrderIdList(List<Long> parentOrderIdList) {
		if (CollectionUtils.isNotEmpty(parentOrderIdList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "parent_order_id in ("+StringUtil.join(parentOrderIdList, Constant.SPLIT_SIGN.COMMA)+")");
			return wrapItemMainImage(params);
		} else {
			return new ArrayList<OrderItem>(0);
		}
	}
	@Override
	public List<OrderItem> selectListByOrderIdList(List<Long> orderIdList) {
		if (CollectionUtils.isNotEmpty(orderIdList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "order_id in ("+StringUtil.join(orderIdList, Constant.SPLIT_SIGN.COMMA)+")");
			return wrapItemMainImage(params);
		} else {
			return new ArrayList<OrderItem>(0);
		}
	}
	@Override
	public List<OrderItem> selectListBySubId(long subOrderId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderId", subOrderId);
		return wrapItemMainImage(params);
	}

	@Override
	public List<OrderItem> selectListByOrderId(long orderId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderId", orderId);
		return wrapItemMainImage(params);
	}
	
	public List<OrderItem> wrapItemMainImage(Map<String,Object> params){
		List<OrderItem> orderItemList = queryByParam(params);
		if(CollectionUtils.isNotEmpty(orderItemList)){
			for(OrderItem orderItem:orderItemList){
				orderItem.setImg(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, orderItem.getImg()));
			}
		}
		return orderItemList;
	}
}
