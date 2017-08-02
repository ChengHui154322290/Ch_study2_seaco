/**
 * 
 */
package com.tp.backend.controller.mmp.coupon;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.CouponConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponUserInfoDTO;
import com.tp.dto.mmp.CouponUserOrderDto;
import com.tp.proxy.mmp.CouponUserProxy;
import com.tp.query.mmp.CouponInfoQuery;

import org.apache.commons.lang3.time.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/coupon")
public class CouponDetailController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CouponUserProxy couponUserProxy;

	@RequestMapping(value = "/detailList")
	public String showDetailList(Model model) {

		return "coupon/couponDetailList";
	}

	@RequestMapping(value = "/detailSearch")
	public String searchDetailList(CouponInfoQuery couponInfoQuery, Model model) {
		if (null == couponInfoQuery.getPage()) {
			couponInfoQuery.setPage(1);
		}
		if (null == couponInfoQuery.getSize()) {
			couponInfoQuery.setSize(30);
		}
		PageInfo<CouponUserInfoDTO> couponUserInfos = couponUserProxy.queryCouponForBackend(couponInfoQuery);
		model.addAttribute("queryAllCouponUsersByPage", couponUserInfos);
		model.addAttribute("couponInfoQuery", couponInfoQuery);
		return "coupon/couponDetailList";
	}

	@RequestMapping(value = "/viewDetail")
	public String showDetail(@RequestParam("id") Long couponUserId, Model model) {
		if (null == couponUserId) {
			return "";
		}
		CouponInfoQuery couponInfoQuery = new CouponInfoQuery();
		if (null == couponInfoQuery.getPage()) {
			couponInfoQuery.setPage(1);
		}
		if (null == couponInfoQuery.getSize()) {
			couponInfoQuery.setSize(20);
		}
		couponInfoQuery.setCouponUserId(couponUserId);
		PageInfo<CouponUserInfoDTO> couponUserInfos = couponUserProxy.queryCouponForBackend(couponInfoQuery);
		if (null != couponUserInfos && couponUserInfos.getRows().size() > 0) {
			CouponUserInfoDTO couponUserDto = couponUserInfos.getRows().get(0);
			if (CouponConstant.COUPON_USE_TYPE_BY_GET
					.equalsIgnoreCase(couponUserDto.getCouponUseType())) {
				if (null != couponUserDto.getUseReceiveDay()) {
					Calendar c = Calendar.getInstance();
					Date startTime = couponUserDto.getSendTime();

					c.setTime(startTime);
					c.add(Calendar.DAY_OF_MONTH,couponUserDto.getUseReceiveDay());
					couponUserDto.setCouponUseStime(startTime);
					couponUserDto.setCouponUseEtime(c.getTime());
				}
			}
			model.addAttribute("couponUserInfo", couponUserDto);
		}
		List<CouponUserOrderDto> subOrderDtos = couponUserProxy.getSalesOrderInfo(couponUserId);
		if (null != subOrderDtos) {
			model.addAttribute("subOrders", subOrderDtos);
		}
		return "coupon/couponDetailView";
	}

	@RequestMapping(value = "/cancel")
	@ResponseBody
	public ResultInfo cancelCoupon(@RequestParam("id") Long couponId, Model model) {
		if (null == couponId) {
			return new ResultInfo(new FailInfo("参数错误"));
		}
		return couponUserProxy.invalidCouponUser(couponId);
	}
}
