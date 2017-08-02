package com.tp.service.ord;

import com.tp.common.vo.ord.OrderCodeType;

/**
 * 编号生成器
 * 
 * @author szy
 * @version 0.0.1
 */
public interface IOrderCodeGeneratorService {

	/**
	 * 生成订单编号
	 * 
	 * @param type
	 * @return
	 */
	Long generate(OrderCodeType type);
}
