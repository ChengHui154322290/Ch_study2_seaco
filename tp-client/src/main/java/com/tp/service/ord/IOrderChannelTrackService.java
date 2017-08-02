package com.tp.service.ord;

import java.util.List;
import java.util.Map;

import com.tp.dto.ord.remote.YiQiFaOrderDto;
import com.tp.model.ord.OrderChannelTrack;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 订单推广渠道信息跟踪表接口
  */
public interface IOrderChannelTrackService extends IBaseService<OrderChannelTrack>{

	public List<YiQiFaOrderDto> queryOrderListByYiQiFa(Map<String,Object> params);
	
	public void pushOrderByChannelYiQiFaParentOrderCode(Long parentOrderCode);
	public String queryOrderListByChannelYiQiFaParams(Map<String,Object> params);
}
