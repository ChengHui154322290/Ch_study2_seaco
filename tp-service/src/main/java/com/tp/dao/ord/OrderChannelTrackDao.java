package com.tp.dao.ord;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.dto.ord.remote.YiQiFaOrderDto;
import com.tp.model.ord.OrderChannelTrack;

public interface OrderChannelTrackDao extends BaseDao<OrderChannelTrack> {

	List<YiQiFaOrderDto> queryOrderListByYiQiFa(Map<String,Object> params);
}
