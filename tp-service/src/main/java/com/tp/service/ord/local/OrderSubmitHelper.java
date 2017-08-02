package com.tp.service.ord.local;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.tp.dto.ord.CartLineDTO;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.ord.OrderConsignee;

/**
 * 下单助手
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderSubmitHelper {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(OrderSubmitHelper.class);

	/**
	 * 提取sku code
	 * 
	 * @param lineList
	 * @return
	 */
	public static List<String> extractSkuCodeList(List<CartLineDTO> lineList) {
		if (CollectionUtils.isNotEmpty(lineList)) {
			List<String> list = new ArrayList<String>(lineList.size());
			for (CartLineDTO line : lineList) {
				list.add(line.getSkuCode());
			}
			return list;
		}
		
		return new ArrayList<String>();
	}

	/**
	 * 用户收货地址转订单收货地址
	 * 
	 * @param address
	 * @return
	 */
	public static OrderConsignee convert2OrderConsignee(ConsigneeAddress address) {
		Assert.notNull(address);
		
		OrderConsignee consignee = new OrderConsignee();
		consignee.setName(address.getName());// 收货人
		consignee.setAddress(address.getAddress());
		consignee.setProvinceId(address.getProvinceId());
		consignee.setProvinceName(address.getProvince());
		consignee.setCityId(address.getCityId());
		consignee.setCityName(address.getCity());
		consignee.setCountyId(address.getCountyId());
		consignee.setCountyName(address.getCounty());
		consignee.setTownId(address.getStreetId());
		consignee.setEmail(address.getEmail());
		consignee.setMobile(address.getMobile());
		consignee.setPostcode(address.getZipCode());
		consignee.setTelephone(address.getPhone());
		consignee.setConsigneeId(address.getId());// 会员收货地址ID
		return consignee;
	}
}
