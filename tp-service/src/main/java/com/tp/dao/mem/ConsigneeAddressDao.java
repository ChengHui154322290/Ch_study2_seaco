package com.tp.dao.mem;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.model.mem.ConsigneeAddress;

public interface ConsigneeAddressDao extends BaseDao<ConsigneeAddress> {
	Long updateByUserId(ConsigneeAddress consigneeAddress);

	List<ConsigneeAddress> findByUserIdOrderLimit(Map<String,Object> params);

	Long updateIsdefaultByUserId(ConsigneeAddress con);
}
