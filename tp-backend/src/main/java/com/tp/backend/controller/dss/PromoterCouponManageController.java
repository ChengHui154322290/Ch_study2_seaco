package com.tp.backend.controller.dss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.model.mmp.Coupon;
import com.tp.proxy.mmp.CouponProxy;

/**
 * 推荐卡券管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/promotercoupon/")
public class PromoterCouponManageController extends AbstractBaseController {

	@Autowired
	private CouponProxy couponProxy;
	
	@RequestMapping("index")
	public void index(Model model,Long promoterId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("promoterId", promoterId);
		params.put("status", Constant.DISABLED.NO);
		params.put("couponType", CouponType.NO_CONDITION.ordinal());
		ResultInfo<List<Coupon>> result = couponProxy.queryByParam(params);
		model.addAttribute("couponList", result.getData());
	}
	
	@RequestMapping("querycouponlist")
	@ResponseBody
	public List<Coupon> queryCouponList(Model model,Long promoterId,String couponName){
		Map<String,Object> params = new HashMap<String,Object>();
		if(promoterId!=null){
			params.put("promoterId", promoterId);
			params.put("couponType", CouponType.NO_CONDITION.ordinal());
		}
		if(StringUtils.isNotBlank(couponName)){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " coupon_name like concat('"+couponName+"','%')");
			if(couponName.matches("^\\d+$")){
				params.remove(MYBATIS_SPECIAL_STRING.COLUMNS.name());
				params.put("id", Long.valueOf(couponName));
			}
		}
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 10);
		ResultInfo<List<Coupon>> result = couponProxy.queryByParam(params);
		return result.getData();
	}
}
