package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.OrderImportInfo;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.prd.IItemManageService;
import com.tp.util.StringUtil;

/**
 * 
 * <pre>
 * 后台校验商品信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 *          Exp $
 */
@Service
public class OrderImportValidateProxy {
	
	
	@Autowired IItemManageService itemManageService;
	
	@Autowired IForbiddenWordsService forbiddenWordsService;
	
	/**
	 * 
	 * <pre>
	 * 	  校验保存的商品(spu,prdid)的信息
	 * </pre>
	 *
	 * @param info
	 * @param details
	 * @param type 1: 只验证info,2验证全部
	 * @return
	 */
	public ResultInfo<Boolean> validItem(OrderImportInfo info) {
		ResultInfo<Boolean> msg = new ResultInfo<Boolean>(Boolean.TRUE);
		// step1 校验 spu
		if(StringUtil.isNullOrEmpty(info.getOrderCode())) {
			return new ResultInfo<Boolean>(new FailInfo("订单编号不能为空"));
		}
		
	
		
		return msg;
	}

	
	
}
