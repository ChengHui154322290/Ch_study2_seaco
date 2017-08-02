package com.tp.dto.ord.remote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.dto.ord.SalePropertyDTO;
import com.tp.model.ord.OrderItem;

/**
 * 订单行DTO
 * 
 * @author 项硕
 * @version 2015年1月11日 下午5:14:18
 */
public class OrderLineDTO extends OrderItem {

	private static final Logger log = LoggerFactory.getLogger(OrderLineDTO.class);
	private static final long serialVersionUID = 7070872784217813262L;
	
	public List<SalePropertyDTO> getSalePropertyList() {
		if (StringUtils.isNoneBlank(getSalesProperty())) {
			try {
				return new ObjectMapper().readValue(getSalesProperty(), new TypeReference<List<SalePropertyDTO>>() {});
			} catch (Throwable e) {
				log.error("购物车行销售属性转换错误");
			}
		}
		return new ArrayList<SalePropertyDTO>(0);
	}
	
	public Double getDiscount() {
		return getOriginalSubTotal() - getSubTotal();
	}
	
	/**
	 * 实付价格
	 * 
	 * @return
	 */
	public Double getRealPrice() {
		return new BigDecimal(getSubTotal()).divide(new BigDecimal(getQuantity()), 2, RoundingMode.HALF_UP).doubleValue();
	}
}
