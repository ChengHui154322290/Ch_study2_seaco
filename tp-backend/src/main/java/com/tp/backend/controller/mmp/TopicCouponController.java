package com.tp.backend.controller.mmp;


import com.tp.common.vo.PageInfo;
import com.tp.model.mmp.Coupon;
import com.tp.proxy.mmp.CouponProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(value="/topicCoupon")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TopicCouponController {

	Logger log = LoggerFactory.getLogger(this.getClass());	@Autowired
	private CouponProxy couponProxy;
	
	@RequestMapping(value="/{rowIndex}/search")
	public String search(@PathVariable(value="rowIndex") Integer rowIndex, final ModelMap model,WebRequest request) {
		model.put("selectRow", rowIndex);
		return "promotion/topicCouponSearch";
	}
	
	@RequestMapping(value="/add")
	public String add(final ModelMap model,WebRequest request) {
		return "promotion/subpages/topicCoupon";
	}
	
	@RequestMapping(value = "/searchCoupon", method = RequestMethod.POST)
	public String searchCoupon(Coupon coupon, @RequestParam(value = "selectRow") Integer selectRow,
							   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
							   @RequestParam(value = "pageId", defaultValue = "1") Integer pageId, Model model) {
		try {
			PageInfo<Coupon> pageDO = couponProxy.queryValidCoupon(coupon, pageId, pageSize);
			model.addAttribute("queryAllCouponByPage", pageDO);
			model.addAttribute("selectRow", selectRow);
			model.addAttribute("Coupon", coupon);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "promotion/topicCouponSearch";
	}
	
	@RequestMapping(value="/{couponIndex}/uploadImg")
	public String uploadImg(@PathVariable Integer couponIndex,final ModelMap model,WebRequest request) {
		model.put("couponIndex", couponIndex);
		return "promotion/topicCouponUploadPic";
	}
}
