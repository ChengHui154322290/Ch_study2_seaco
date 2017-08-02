package com.tp.proxy.ord;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.OrderChannelTrack;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderChannelTrackService;
/**
 * 订单推广渠道信息跟踪表代理层
 * @author szy
 *
 */
@Service
public class OrderChannelTrackProxy extends BaseProxy<OrderChannelTrack>{

	@Autowired
	private IOrderChannelTrackService orderChannelTrackService;

	@Override
	public IBaseService<OrderChannelTrack> getService() {
		return orderChannelTrackService;
	}
	
	public ResultInfo<String> queryOrderListByChannelYiQiFaParams(Map<String,Object> params) {
		try{
			return new ResultInfo<>(orderChannelTrackService.queryOrderListByChannelYiQiFaParams(params));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params);
			return new ResultInfo<>(failInfo);
		}
	}
}
