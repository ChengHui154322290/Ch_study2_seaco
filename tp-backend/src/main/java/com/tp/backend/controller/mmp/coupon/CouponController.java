package com.tp.backend.controller.mmp.coupon;

import com.alibaba.fastjson.JSON;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.CouponConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponDto;
import com.tp.dto.mmp.enums.CouponOfferType;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.bse.Category;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponAudit;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.bse.BrandProxy;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.proxy.mmp.CouponProxy;
import com.tp.result.bse.AutoCompleteResult;
import com.tp.result.sup.SupplierResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/***
 * 
 * 优惠券管理控制器
*/

@Controller
@RequestMapping("/coupon")
public class CouponController extends AbstractBaseController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryProxy categoryProxy;

	@Autowired
	private BrandProxy brandProxy;

	@Autowired
	private CouponProxy couponProxy;

	/***
	 * 
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Model model, Coupon cdo,
			@RequestParam(value = "startPage", defaultValue = "1") Integer startPage,
			@RequestParam(value = "size", defaultValue = "10") Integer size) throws Exception {
		ModelAndView mv = new ModelAndView();
		PageInfo<Coupon> queryAllCouponByPage = couponProxy.queryAllLikedofBrandByPage(cdo, startPage, size);
		mv.addObject("queryAllCouponByPage", queryAllCouponByPage);
		mv.addObject("couponDO", cdo);
		mv.setViewName("/coupon/coupon_list");
		return mv;
	}

	/***
	 * 新增优惠券页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addCoupon")
	public ModelAndView addCoupon() {
		ModelAndView mv = new ModelAndView();
		List<Category> categoryList = categoryProxy.getFirstCategoryList();
		mv.setViewName("/coupon/addCoupon");
		mv.addObject("bucketURL", Constant.IMAGE_URL_TYPE.item.url);
		mv.addObject("bucketname", Constant.IMAGE_URL_TYPE.item.name());
		mv.addObject("platformEnum", PlatformEnum.values());
		mv.addObject("categoryList", categoryList);
		return mv;
	}

	/***
	 * 修改优惠券信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toEditCoupon")
	public ModelAndView toEditCoupon(@RequestParam(value = "id") Long id) throws Exception {
		if (id == null) {
			//ResultUtil.throwExcepion(new ResultInfo(ResultInfo.TYPE_RESULT_FAIL, 909, "id为空,异常"));
		}
		ModelAndView mv = new ModelAndView();
		CouponDto dto = couponProxy.getCouponInfosById(id);
		List<Category> categoryList = categoryProxy.getFirstCategoryList();
		List<CouponAudit> auditList = couponProxy.queryCouponAudit(id);
		if ( auditList != null && auditList.size() > 0)
			mv.addObject("auditList", auditList);
		mv.addObject("categoryList", categoryList);
		mv.addObject("couponDto", dto);
		mv.addObject("infos", JSON.toJSONString(dto));
		mv.addObject("platformEnum", PlatformEnum.values());
		mv.addObject("bucketURL", Constant.IMAGE_URL_TYPE.item.url);
		mv.addObject("bucketname", Constant.IMAGE_URL_TYPE.item.name());
		mv.setViewName("/coupon/editCoupon");
		return mv;
	}
	
	/***
	 * 查看优惠券信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toViewCoupon")
	public ModelAndView toViewCoupon(@RequestParam(value = "id") Long id) throws Exception {
		if (id == null) {
			//ResultUtil.throwExcepion(new ResultInfo(ResultInfo.TYPE_RESULT_FAIL, 909, "id为空,异常"));
		}
		ModelAndView mv = new ModelAndView();
		CouponDto dto = couponProxy.getCouponInfosById(id);
		List<Category> categoryList = categoryProxy.getFirstCategoryList();
		List<CouponAudit> auditList = couponProxy.queryCouponAudit(id);
		if ( auditList != null && auditList.size() > 0)
			mv.addObject("auditList", auditList);
		mv.addObject("categoryList", categoryList);
		mv.addObject("couponDto", dto);
		mv.addObject("onlyView", true);
		mv.addObject("infos", JSON.toJSONString(dto));
		mv.addObject("platformEnum", PlatformEnum.values());
		mv.addObject("bucketURL", Constant.IMAGE_URL_TYPE.item.url);
		mv.addObject("bucketname", Constant.IMAGE_URL_TYPE.item.name());
		mv.setViewName("/coupon/editCoupon");
		return mv;
	}

	/***
	 * 根据品牌名称 获取品牌的id
	 * 
	 * @param term
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryBrand")
	@ResponseBody
	public List<AutoCompleteResult> queryBrandsByName(String term) throws Exception {
		byte bb[];
		bb = term.getBytes("ISO-8859-1");
		term = new String(bb, "UTF-8");
		return brandProxy.selectAtuoCompleteBrand(term);
	}

	/****
	 * 保存优惠券信息
	 */
	@RequestMapping(value = { "/saveCoupon" }, method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo saveCoupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserInfo user = getUserInfo();
		String coupon = request.getParameter("coupon");
		CouponDto saveDto = JSON.parseObject(coupon, CouponDto.class);
		 if (saveDto.getOfferType() == CouponOfferType.SEND.getCode()) {
			 saveDto.setIsShowReceive(CouponConstant.COUPON_UN_SHOW_CENTER);//发送的默认不在卡券中心展示
		 }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		saveDto.setCreateTime(sdf.format(new Date()));
		saveDto.setCreateUserId(user.getId());
		couponProxy.saveCoupon(saveDto);
		return new ResultInfo("新增优惠券成功");
	}

	@RequestMapping(value = { "/updateCoupon" }, method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo updateCoupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserInfo user = getUserInfo();
		String coupon = request.getParameter("coupon");
		CouponDto saveDto = JSON.parseObject(coupon, CouponDto.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		saveDto.setModifyTime(sdf.format(new Date()));
		couponProxy.updateCoupon(saveDto);
		return new ResultInfo("编辑优惠券成功");
	}

	/***
	 * 优惠券发放页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sendCouponToUser")
	public ModelAndView sendCouponToUser() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("/coupon/sendCouponToUser");
		return mv;
	}
	
	/***
	 * 验证是否能编辑优惠券
	 */
	@RequestMapping(value = "/check_edit")
	@ResponseBody
	public Boolean checkCouponEdit(Long couponId, HttpServletRequest request, HttpServletResponse response){
		return couponProxy.checkCouponEdit(couponId);
	}
	
	/***
	 * 终止优惠券
	 * @param couponId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/stop_coupon")
	@ResponseBody
	public Boolean stopCoupon(Long couponId, HttpServletRequest request, HttpServletResponse response){
		UserInfo user = getUserInfo();
		return couponProxy.stopCoupon(couponId, user).isSuccess();
	}
	
	/***
	 * 
	 * 驳回审核
	 * @param couponId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/refused_coupon")
	@ResponseBody
	public Boolean invalidCoupon(Long couponId, HttpServletRequest request, HttpServletResponse response){
		UserInfo user = getUserInfo();
		String reason = request.getParameter("reason");
		return couponProxy.refusedCoupon(couponId, reason, user).isSuccess();
	}
	
	/**
	 * 批准 审核通过
	 * @param couponId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/approve_coupon")
	@ResponseBody
	public Boolean approveCoupon(Long couponId, HttpServletRequest request, HttpServletResponse response){
		UserInfo user = getUserInfo();
		return couponProxy.approveCoupon(couponId,  user).isSuccess();
	}
	/**
	 *取消优惠券
	 * @param couponId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/cancel_coupon")
	@ResponseBody
	public Boolean cancelCoupon(Long couponId, HttpServletRequest request, HttpServletResponse response){
		UserInfo user = getUserInfo();
		return couponProxy.cancelCoupon(couponId,  user).isSuccess();
	}
	
	

	@RequestMapping(value = "/supplier/query")
	public String showSearchBrand(ModelMap model) {
		model.addAttribute("pageSize", "10");
		model.addAttribute("currPage", "1");
		model.addAttribute("totalPage", "1");
		model.addAttribute("brandCount", "0");
		return "coupon/supplierSearch";
	}

	@RequestMapping(value = "/supplier/search", method = RequestMethod.POST)
	public String searchBrand(@RequestParam("supplierId") Long supplierId,
			@RequestParam("name") String name,
			@RequestParam("startPage") Integer startPage,
			@RequestParam("pageSize") Integer pageSize, ModelMap model,
			HttpServletRequest request) {
		SupplierResult supplierResult = couponProxy.searchSupplier(supplierId, name, startPage, pageSize);
		model.addAttribute("supplierList", supplierResult.getSupplierInfoList());
		Long totalCount = supplierResult.getTotalCount();
		model.addAttribute("supplierCount", totalCount);
		Long countPage = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			countPage++;
		}
		model.addAttribute("totalPage", countPage);
		model.addAttribute("currPage", supplierResult.getStartPage());
		model.addAttribute("pageSize", supplierResult.getPageSize());
		return "coupon/supplierList";
	}

	@RequestMapping(value = "/supplier/confirm", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<SupplierInfo> searchBrandInfo(@RequestParam("supplierId") Long supplierId,ModelMap model) {
		return couponProxy.searchSupplierInfoById(supplierId);
	}
	
	@RequestMapping(value = "/active")
	@ResponseBody
	public Boolean activeCoupon(Long couponId, Integer activeStatus){
		UserInfo user = getUserInfo();
		return couponProxy.activeCoupon(couponId,activeStatus,user).isSuccess();
	}
}
