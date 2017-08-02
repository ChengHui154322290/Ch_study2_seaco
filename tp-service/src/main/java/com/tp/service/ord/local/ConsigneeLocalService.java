/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.service.ord.local;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.Constant;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.service.ord.local.IConsigneeLocalService;

/**
 * <pre>
 * 收货人本业务接口实现类
 * </pre>
 * 
 * @author szy
 * @version 0.0.1
 *          zhufuhua Exp $
 */
@Service
public class ConsigneeLocalService implements IConsigneeLocalService {
	private static final Logger logger = LoggerFactory.getLogger(ConsigneeLocalService.class);

	private static final Integer DEFAULT_GET_CONSIGNEE_NUM = 10;
	@Autowired
	private IConsigneeAddressService consigneeAddressService;

	@Override
	public LinkedList<ConsigneeAddress> getConsigneeInfoList(Long memberId) {
		List<ConsigneeAddress> caDtos = consigneeAddressService.findByUserIdOrderLimit(memberId, DEFAULT_GET_CONSIGNEE_NUM);

		if (CollectionUtils.isEmpty(caDtos)) {
			logger.info("该用户没有收货人信息");
			return null;
		} else {
			LinkedList<ConsigneeAddress> list = new LinkedList<ConsigneeAddress>();
			ConsigneeAddress defaultConsigneeAddress = null;
			for (ConsigneeAddress caDto : caDtos) {
				if (Constant.DELECTED.YES.equals(caDto.getIsDefault())) {// 默认收获人信息
					defaultConsigneeAddress = caDto;
				} else {
					list.add(caDto);
				}
			}
			if (defaultConsigneeAddress != null) {// 将默认收货人信息排到列表最前面
				list.addFirst(defaultConsigneeAddress);
			}
			return list;
		}
	}

	@Override
	public Long saveConsigneeInfo(ConsigneeAddress consigneeDTO) {
		Long consigneeId = null;
		if (consigneeDTO.getUserId() != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("保存用户收货人信息时入参数据为：{}", JSONObject.toJSONString(consigneeDTO));
			}
			consigneeAddressService.insert(consigneeDTO);
			consigneeId = consigneeDTO.getId();
		}
		return consigneeId;
	}

	@Override
	public boolean deleteConsigneeInfo(Long memberId, Long consigneeId) {
		if (memberId != null) {
			ConsigneeAddress dto = consigneeAddressService.queryById(consigneeId);
			if (dto != null && memberId.equals(dto.getUserId())) {
				consigneeAddressService.deleteById(consigneeId);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setDefaultConsignee(Long memberId, Long consigneeId) {
		try {
			consigneeAddressService.setDefaultAddress(memberId, consigneeId);
			return true;
		} catch (Exception e) {
			logger.error("设置默认收货人异常", e.getMessage());
		}
		return false;
	}
}
