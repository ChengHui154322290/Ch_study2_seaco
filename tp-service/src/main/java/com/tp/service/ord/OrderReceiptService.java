package com.tp.service.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.ord.OrderReceiptDao;
import com.tp.model.ord.OrderReceipt;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderReceiptService;
import com.tp.util.StringUtil;

@Service
public class OrderReceiptService extends BaseService<OrderReceipt> implements IOrderReceiptService {

	@Autowired
	private OrderReceiptDao orderReceiptDao;
	
	@Override
	public BaseDao<OrderReceipt> getDao() {
		return orderReceiptDao;
	}
	@Override
	public OrderReceipt selectOneByOrderId(Long orderId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderId", orderId);
		return super.queryUniqueByParams(params);
	}

	@Override
	public List<OrderReceipt> selectListByOrderIdList(List<Long> orderIdList) {
		if (CollectionUtils.isNotEmpty(orderIdList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "parent_order_id in ("+StringUtil.join(orderIdList, Constant.SPLIT_SIGN.COMMA)+")");
			return super.queryByParam(params);
		} else {
			return new ArrayList<OrderReceipt>(0);
		}
	}
}
