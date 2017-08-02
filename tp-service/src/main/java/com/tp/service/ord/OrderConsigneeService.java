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
import com.tp.dao.ord.OrderConsigneeDao;
import com.tp.model.ord.OrderConsignee;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderConsigneeService;
import com.tp.util.StringUtil;

@Service
public class OrderConsigneeService extends BaseService<OrderConsignee> implements IOrderConsigneeService {

	@Autowired
	private OrderConsigneeDao orderConsigneeDao;
	
	@Override
	public BaseDao<OrderConsignee> getDao() {
		return orderConsigneeDao;
	}
	
	@Override
	public OrderConsignee selectOneByOrderId(long orderId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderId", orderId);
		return super.queryUniqueByParams(params);
	}

	@Override
	public List<OrderConsignee> selectListByOrderIdList(List<Long> orderIdList) {
		if (CollectionUtils.isNotEmpty(orderIdList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "parent_order_id in ("+StringUtil.join(orderIdList, Constant.SPLIT_SIGN.COMMA)+")");
			return orderConsigneeDao.queryByParam(params);
		} else {
			return new ArrayList<OrderConsignee>(0);
		}
	}
	/**
	 * 查询订单对应的收货地址信息
	 * 
	 * @param OrderConsignee
	 * @return
	 */
	@Override
	public OrderConsignee selectOneByOrderCode(Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", orderCode);
		return super.queryUniqueByParams(params);
	}
}
