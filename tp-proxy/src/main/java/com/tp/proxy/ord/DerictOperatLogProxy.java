package com.tp.proxy.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.ord.DerictOperatLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IDerictOperatLogService;


@Service
public class DerictOperatLogProxy extends BaseProxy<DerictOperatLog> {
	@Autowired
	private IDerictOperatLogService derictOperatLogService ;
	
	@Override
	public IBaseService<DerictOperatLog> getService() {
		return derictOperatLogService;
	}
	
	public List<DerictOperatLog> queryByOrderCode(String orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("order_code",orderCode );
		return derictOperatLogService.queryByParam(params);
	}
	/**
	 * 返回最后一条推送失败的原因
	 * @param orderCode
	 * @return
	 */
	public List<DerictOperatLog> queryMessageByOrderCode(Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("order_code",orderCode );
		params.put("operat_type", "1");
		params.put("is_success", "0");
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
		return derictOperatLogService.queryByParam(params);
	}

	
}
