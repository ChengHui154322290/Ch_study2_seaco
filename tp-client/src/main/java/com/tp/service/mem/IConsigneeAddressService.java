package com.tp.service.mem;

import java.util.List;

import com.tp.model.mem.ConsigneeAddress;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 用户收货地址表接口
  */
public interface IConsigneeAddressService extends IBaseService<ConsigneeAddress>{

	Long updateIsdefaultByUserId(ConsigneeAddress consigneeAddress);

	List<ConsigneeAddress> findByUserIdOrderLimit(Long userId, Integer resultCount);

	void setDefaultAddress(Long memberId, Long consigneeId);

	List<ConsigneeAddress> findByUserId(Long id, Integer resultCount);

	List<String> getAddressList(List<ConsigneeAddress> address);

	ConsigneeAddress getDefaultAddress(Long userId);

}
