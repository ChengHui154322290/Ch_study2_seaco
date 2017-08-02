package com.tp.proxy.mmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.ExceptionUtils;
import com.tp.common.util.VerifyUtil;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.util.mmp.DataUtil;
import com.tp.common.util.mmp.DateTimeFormatUtil;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.CouponConstant;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponDto;
import com.tp.dto.mmp.CouponRangeDto;
import com.tp.dto.mmp.enums.CouponSendSendStatus;
import com.tp.dto.mmp.enums.CouponSendType;
import com.tp.dto.mmp.enums.CouponStatus;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.mmp.enums.OfferType;
import com.tp.exception.ServiceException;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponAudit;
import com.tp.model.mmp.CouponRange;
import com.tp.model.mmp.CouponSend;
import com.tp.model.mmp.CouponSendAudit;
import com.tp.model.mmp.TopicCoupon;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.result.sup.SupplierResult;
import com.tp.service.IBaseService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ICouponRangeService;
import com.tp.service.mmp.ICouponSendService;
import com.tp.service.mmp.ICouponService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IExchangeCouponChannelCodeService;
import com.tp.service.mmp.ITopicCouponService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.util.BeanUtil;

/**
 * 优惠券信息表代理层
 *
 * @author szy
 */
@Service
public class CouponProxy extends BaseProxy<Coupon> {

	@Autowired
	private ICouponService couponService;

	@Autowired
	private IPurchasingManagementService pmService;

	@Autowired
	private ICouponUserService couponUserService;

	@Autowired
	private ICouponRangeService couponRangeService;

	@Autowired
	private IItemSkuService itemSkuService;

	@Autowired
	private ICouponSendService couponSendService;

	@Autowired
	private IMemberInfoService memberInfoService;

	@Autowired
	private IPromoterInfoService promoterInfoService;

	@Autowired
	private ITopicCouponService topicCouponService;

	@Autowired
	private IExchangeCouponChannelCodeService exchangeCouponChannelCodeService;

	@Override
	public IBaseService<Coupon> getService() {
		return couponService;
	}

	/**
	 * 查询推广员优惠券数量
	 **/
	public ResultInfo<Map<String, Integer>> queryPromoterCouponCount(Long promoterId) {
		if (null == promoterId) {
			return new ResultInfo<>(new FailInfo("非法参数"));
		}
		Map<String, Object> params = new HashMap<>();
		Map<String, Integer> result = new HashMap<>();
		try {
			// 查询兑换码总数
			params.put("promoterId", promoterId);
			Integer totalCount = exchangeCouponChannelCodeService.queryByParamCount(params);
			result.put("totalCount", totalCount);
			// 已兑换总数
			params.put("status", 1);
			Integer exchangedCount = exchangeCouponChannelCodeService.queryByParamCount(params);
			result.put("exchangedCount", exchangedCount);
			result.put("unexchangeCount", Integer.valueOf((totalCount - exchangedCount)));
			// 已消费总数
			params.put("status", CouponUserStatus.USED.getCode());
			result.put("usedCount", couponUserService.queryByParamCount(params));
			return new ResultInfo<>(result);
		} catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception), logger, promoterId);
			return new ResultInfo<>(new FailInfo("查询推广员优惠券总数量失败"));
		}
	}

	/***
	 * 保存优惠券信息
	 *
	 * @param saveDto
	 * @throws Exception
	 */
	// @Transactional
	public void saveCoupon(CouponDto saveDto) throws Exception {
		Coupon saveDo = new Coupon();
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
		BeanUtils.copyProperties(saveDo, saveDto);
		saveDo.setCode(getCode());
		process(saveDo);
		saveDo = couponService.insert(saveDo);
		Long id = saveDo.getId();
		List<JSONObject> listRange = (List<JSONObject>) JSONArray.parse(saveDto.getCouponRangeGroup());
		if (CollectionUtils.isNotEmpty(listRange)) {
			for (JSONObject obj : listRange) {
				CouponRange saveRange = new CouponRange();
				saveRange.setCouponId(id);
				if (obj.get("categoryId") != null && StringUtils.isNotBlank(obj.get("categoryId").toString())) {
					saveRange.setCategoryId(Long.valueOf(obj.get("categoryId").toString()));
				}
				if (obj.get("code") != null && StringUtils.isNotBlank(obj.get("code").toString()) && "0".equals(obj.get("type"))) {
					saveRange.setCode(obj.get("code").toString());
					ItemSku ItemSku = new ItemSku();
					ItemSku.setSku(obj.get("code").toString());
					List<ItemSku> skuList = itemSkuService.queryByObject(ItemSku);
					if (skuList != null && skuList.size() > 0)
						ItemSku = skuList.get(0);
					saveRange.setSkuName(ItemSku.getDetailName());
				}
				if (obj.get("code") != null && StringUtils.isNotBlank(obj.get("code").toString()) && "2".equals(obj.get("type").toString())) {
					saveRange.setCode(obj.get("code").toString());
				}
				if (obj.get("brandId") != null && StringUtils.isNotBlank(obj.get("brandId").toString())) {
					saveRange.setBrandId(Long.valueOf(obj.get("brandId").toString()));
					saveRange.setBrandName(obj.get("brandName").toString());
				}
				if (obj.get("type") != null && StringUtils.isNotBlank(obj.get("type").toString())) {
					saveRange.setType(obj.get("type").toString());
				}
				if (obj.get("categoryMiddleId") != null
						&& StringUtils.isNotBlank(obj.get("categoryMiddleId").toString())) {
					saveRange.setCategoryMiddleId(Long.valueOf(obj.get("categoryMiddleId").toString()));
				}
				if (obj.get("categorySmallId") != null
						&& StringUtils.isNotBlank(obj.get("categorySmallId").toString())) {
					saveRange.setCategorySmallId(Long.valueOf(obj.get("categorySmallId").toString()));
				}
				if (obj.get("attributeName") != null && StringUtils.isNotBlank(obj.get("attributeName").toString())) {
					saveRange.setAttributeName(obj.get("attributeName").toString());
				}
				if (checkSaveRelation(saveRange)) {
					saveRange.setRangeType(0);
					couponRangeService.insert(saveRange);
				}
			}
		}

		List<JSONObject> listRangeNoInclude = (List<JSONObject>) JSONArray
				.parse(saveDto.getCouponRangeGroupNoInclude());
		if (CollectionUtils.isNotEmpty(listRangeNoInclude)) {
			for (JSONObject obj : listRangeNoInclude) {
				CouponRange saveRange = new CouponRange();
				saveRange.setCouponId(id);
				if (obj.get("categoryId") != null && StringUtils.isNotBlank(obj.get("categoryId").toString())) {
					saveRange.setCategoryId(Long.valueOf(obj.get("categoryId").toString()));
				}
				if (obj.get("code") != null && StringUtils.isNotBlank(obj.get("code").toString())) {
					saveRange.setCode(obj.get("code").toString());
					ItemSku ItemSku = new ItemSku();
					ItemSku.setSku(obj.get("code").toString());
					List<ItemSku> skuList = itemSkuService.queryByObject(ItemSku);
					if (skuList != null && skuList.size() > 0)
						ItemSku = skuList.get(0);
					saveRange.setSkuName(ItemSku.getDetailName());
				}
				if (obj.get("brandId") != null && StringUtils.isNotBlank(obj.get("brandId").toString())) {
					saveRange.setBrandId(Long.valueOf(obj.get("brandId").toString()));
					saveRange.setBrandName(obj.get("brandName").toString());
				}
				if (obj.get("type") != null && StringUtils.isNotBlank(obj.get("type").toString())) {
					saveRange.setType(obj.get("type").toString());
				}
				if (obj.get("categoryMiddleId") != null
						&& StringUtils.isNotBlank(obj.get("categoryMiddleId").toString())) {
					saveRange.setCategoryMiddleId(Long.valueOf(obj.get("categoryMiddleId").toString()));
				}
				if (obj.get("categorySmallId") != null
						&& StringUtils.isNotBlank(obj.get("categorySmallId").toString())) {
					saveRange.setCategorySmallId(Long.valueOf(obj.get("categorySmallId").toString()));
				}
				if (obj.get("attributeName") != null && StringUtils.isNotBlank(obj.get("attributeName").toString())) {
					saveRange.setAttributeName(obj.get("attributeName").toString());
				}
				if (checkSaveRelation(saveRange)) {
					saveRange.setRangeType(1);
					couponRangeService.insert(saveRange);
				}
			}
		}
		// 保存对应的专场信息
		if (CouponConstant.COUPON_USE_TOPIC.equals(saveDto.getUseScope())
				&& StringUtils.isNoneBlank(saveDto.getTopicId())) {
			CouponRange saveRange = new CouponRange();
			saveRange.setCouponId(saveDto.getId());
			saveRange.setType("1");// 专场
			couponRangeService.deleteByObject(saveRange);// 先删除记录
			saveRange.setCode(saveDto.getTopicId());
			couponRangeService.insert(saveRange);
		}

	}

	private String getSkuName(String sku) {
		ItemSku ItemSku = new ItemSku();
		ItemSku.setSku(sku);
		List<ItemSku> skuList = itemSkuService.queryByObject(ItemSku);
		if (skuList != null && skuList.size() > 0)
			return skuList.get(0).getDetailName();
		return "";
	}

	public String getCode() {
		return getCode(10);
	}

	public String getCode(int times) {
		if (times < 0) {
			throw new ServiceException("获取code错误");
		}
		String code = DataUtil.radomCode();
		Coupon coupon = new Coupon();
		coupon.setCode(code);
		Integer count = couponService.queryByObjectCount(coupon);
		if (count == 0) {
			return code;
		} else {
			return getCode(--times);
		}
	}

	private boolean checkSaveRelation(CouponRange saveRange) {

		if (saveRange.getBrandName() == null && saveRange.getCategoryId() == null
				&& saveRange.getCategoryMiddleId() == null && saveRange.getCategorySmallId() == null
				&& (saveRange.getCode() == null || "".equals(saveRange.getCode()))) {
			return false;
		}
		return true;
	}

	/**
	 * 获取有效的优惠券信息
	 *
	 * @param cdo
	 * @param startPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageInfo<Coupon> queryValidCoupon(Coupon cdo, int startPage, int pageSize) {

		cdo.setStatus(CouponStatus.PASSED.ordinal());
		return this.queryAllLikedofBrandByPage(cdo, startPage, pageSize);
	}

	/***
	 * 分页查询批次信息
	 *
	 * @param brandDO
	 * @param startPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageInfo<Coupon> queryAllLikedofBrandByPage(Coupon cdo, int startPage, int pageSize) {
		if (cdo == null) {
			cdo = new Coupon();
		}

		if (cdo != null && startPage > 0 && pageSize > 0) {
			cdo.setStartPage(startPage);
			cdo.setPageSize(pageSize);

			if (cdo.getSourceType() == null || (cdo.getSourceType() != null && cdo.getSourceType() == 1)) {
				cdo.setSourceId(null);
				cdo.setSourceName(null);
			}
			cdo.setCouponName(StringUtils.isBlank(cdo.getCouponName()) ? null : cdo.getCouponName().trim());
			PageInfo<Coupon> page = couponService.queryPageByObjectWithLike(cdo,
					new PageInfo<Coupon>(startPage, pageSize));
			return page;
		}
		return new PageInfo<>();
	}

	/***
	 * 获取优惠券信息
	 */
	public CouponDto getCouponInfosById(Long couponId) {
		/** 优惠券信息 **/
		CouponDto detailInfo = couponService.getCouponInfosById(couponId);
		/** 查询对应的专场ID */
		TopicCoupon topicCoupon = new TopicCoupon();
		topicCoupon.setCouponId(couponId);
//		topicCoupon = topicCouponService.queryUniqueByObject(topicCoupon);
//		if (topicCoupon != null) {
//			detailInfo.setTopicId(String.valueOf(topicCoupon.getTopicId()));
//		}
		/** 优惠券范围信息 **/

		List<CouponRangeDto> listRange = couponRangeService.selectCouponRangeByCouponId(couponId);
		if (CollectionUtils.isNotEmpty(listRange)) {
			List<CouponRangeDto> listRangeInclude = new ArrayList<>();
			List<CouponRangeDto> listRangeNo = new ArrayList<>();
			for (CouponRangeDto dto : listRange) {
				if (dto.getRangeType() == 0) {
					if (dto.getType() == "1") {//专场信息
						detailInfo.setTopicId(dto.getCode());
					}
					listRangeInclude.add(dto);
				}
				else {
					listRangeNo.add(dto);
				}

			}
			if (listRangeInclude.size() > 0)
				detailInfo.setCouponRangeGroup(JSONArray.toJSONString(listRangeInclude));
			if (listRangeNo.size() > 0)
				detailInfo.setCouponRangeGroupNoInclude(JSONArray.toJSONString(listRangeNo));
		}

		if (detailInfo != null && detailInfo.getPromoterId() != null) {
			PromoterInfo promoterInfo = promoterInfoService.queryById(detailInfo.getPromoterId());
			if (promoterInfo != null)
				detailInfo.setPromoterName(promoterInfo.getPromoterName());
		}

		return detailInfo;
	}

	/***
	 * 保存优惠券信息
	 *
	 * @param saveDto
	 * @throws Exception
	 */
	// @Transactional
	public void updateCoupon(CouponDto saveDto) throws Exception {
		Coupon saveDo = new Coupon();
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
		BeanUtils.copyProperties(saveDo, saveDto);
		process(saveDo);

		couponService.updateNotNullById(saveDo);
		/*** 删除原商品范围 ***/
		couponRangeService.deleteByCouponId(saveDo.getId());
		List<JSONObject> listRange = (List<JSONObject>) JSONArray.parse(saveDto.getCouponRangeGroup());
		if (CollectionUtils.isNotEmpty(listRange)) {
			for (JSONObject obj : listRange) {
				CouponRange saveRange = new CouponRange();
				saveRange.setCouponId(saveDo.getId());
				if (obj.get("categoryId") != null && StringUtils.isNotBlank(obj.get("categoryId").toString())) {
					saveRange.setCategoryId(Long.valueOf(obj.get("categoryId").toString()));
				}
				if (obj.get("code") != null && StringUtils.isNotBlank(obj.get("code").toString()) && "0".equals(obj.get("type"))) {
					saveRange.setCode(obj.get("code").toString());
					saveRange.setSkuName(getSkuName(obj.get("code").toString()));
				}
				if (obj.get("code") != null && StringUtils.isNotBlank(obj.get("code").toString()) && "2".equals(obj.get("type").toString())) {
					saveRange.setCode(obj.get("code").toString());
				}
				if (obj.get("brandId") != null && StringUtils.isNotBlank(obj.get("brandId").toString())) {
					saveRange.setBrandId(Long.valueOf(obj.get("brandId").toString()));
					saveRange.setBrandName(obj.get("brandName").toString());
				}
				if (obj.get("type") != null && StringUtils.isNotBlank(obj.get("type").toString())) {
					saveRange.setType(obj.get("type").toString());
				}
				if (obj.get("categoryMiddleId") != null
						&& StringUtils.isNotBlank(obj.get("categoryMiddleId").toString())) {
					saveRange.setCategoryMiddleId(Long.valueOf(obj.get("categoryMiddleId").toString()));
				}
				if (obj.get("categorySmallId") != null
						&& StringUtils.isNotBlank(obj.get("categorySmallId").toString())) {
					saveRange.setCategorySmallId(Long.valueOf(obj.get("categorySmallId").toString()));
				}
				/*
				 * if (obj.get("brandName") != null &&
				 * StringUtils.isNotBlank(obj.get("brandName").toString())) {
				 * saveRange.setBrandName(obj.get("brandName").toString()); }
				 */
				if (obj.get("attributeName") != null && StringUtils.isNotBlank(obj.get("attributeName").toString())) {
					saveRange.setAttributeName(obj.get("attributeName").toString());
				}
				if (checkSaveRelation(saveRange)) {
					saveRange.setRangeType(0);
					couponRangeService.insert(saveRange);
				}

			}
		}
		// 不包含
		List<JSONObject> listRangeNo = (List<JSONObject>) JSONArray.parse(saveDto.getCouponRangeGroupNoInclude());
		if (CollectionUtils.isNotEmpty(listRangeNo)) {
			for (JSONObject obj : listRangeNo) {
				CouponRange saveRange = new CouponRange();
				saveRange.setCouponId(saveDo.getId());
				if (obj.get("categoryId") != null && StringUtils.isNotBlank(obj.get("categoryId").toString())) {
					saveRange.setCategoryId(Long.valueOf(obj.get("categoryId").toString()));
				}
				if (obj.get("code") != null && StringUtils.isNotBlank(obj.get("code").toString())) {
					saveRange.setCode(obj.get("code").toString());
					saveRange.setSkuName(getSkuName(obj.get("code").toString()));
				}
				if (obj.get("brandId") != null && StringUtils.isNotBlank(obj.get("brandId").toString())) {
					saveRange.setBrandId(Long.valueOf(obj.get("brandId").toString()));
					saveRange.setBrandName(obj.get("brandName").toString());
				}
				if (obj.get("type") != null && StringUtils.isNotBlank(obj.get("type").toString())) {
					saveRange.setType(obj.get("type").toString());
				}
				if (obj.get("categoryMiddleId") != null
						&& StringUtils.isNotBlank(obj.get("categoryMiddleId").toString())) {
					saveRange.setCategoryMiddleId(Long.valueOf(obj.get("categoryMiddleId").toString()));
				}
				if (obj.get("categorySmallId") != null
						&& StringUtils.isNotBlank(obj.get("categorySmallId").toString())) {
					saveRange.setCategorySmallId(Long.valueOf(obj.get("categorySmallId").toString()));
				}
				/*
				 * if (obj.get("brandName") != null &&
				 * StringUtils.isNotBlank(obj.get("brandName").toString())) {
				 * saveRange.setBrandName(obj.get("brandName").toString()); }
				 */
				if (obj.get("attributeName") != null && StringUtils.isNotBlank(obj.get("attributeName").toString())) {
					saveRange.setAttributeName(obj.get("attributeName").toString());
				}
				if (checkSaveRelation(saveRange)) {
					saveRange.setRangeType(1);
					couponRangeService.insert(saveRange);
				}

			}
		}
	}

	private void process(Coupon saveDo) {
		if (saveDo.getCouponUseStime() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(saveDo.getCouponUseStime());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			saveDo.setCouponUseStime(calendar.getTime());
		}
		if (saveDo.getCouponUseEtime() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(saveDo.getCouponUseEtime());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			saveDo.setCouponUseEtime(calendar.getTime());
		}

		if ((saveDo.getCouponReleaseStime() != null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(saveDo.getCouponReleaseStime());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			saveDo.setCouponReleaseStime(calendar.getTime());
		}

		if (saveDo.getCouponReleaseEtime() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(saveDo.getCouponReleaseEtime());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			saveDo.setCouponReleaseEtime(calendar.getTime());
		}
	}

	/**
	 * 验证是否能编辑优惠券
	 *
	 * @param couponId
	 * @return
	 */
	public Boolean checkCouponEdit(Long couponId) {
		return couponUserService.checkCouponEdit(couponId);
	}

	/***
	 * 设为终止
	 *
	 * @param couponId
	 * @return
	 */
	public ResultInfo stopCoupon(final Long couponId, final UserInfo user) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				Coupon Coupon = new Coupon();
				Coupon.setId(couponId);
				Coupon.setStatus(CouponStatus.STOP.ordinal());
				if (user != null){
					Coupon.setUpdateUserId(user.getId());
					Coupon.setModifyTime(new Date());
				}
				couponService.updateNotNullById(Coupon);
				result.setSuccess(true);
			}
		});
		return result;
	}

	public ResultInfo refusedCoupon(final Long couponId, final String reason, final UserInfo user) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				ResultInfo resultInfo = couponService.refuseCoupon(couponId, user.getId(), user.getLoginName(), reason);
				if (!resultInfo.isSuccess()) {
					result.setMsg(resultInfo.getMsg());
				}
				result.setSuccess(resultInfo.isSuccess());
			}
		});
		return result;
	}

	public ResultInfo approveCoupon(final Long couponId, final UserInfo user) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				ResultInfo resultInfo = couponService.approveCoupon(couponId, user.getId(), user.getLoginName());
				if (!resultInfo.isSuccess()) {
					result.setMsg(resultInfo.getMsg());
				}
				result.setSuccess(resultInfo.isSuccess());
			}
		});
		return result;
	}

	public ResultInfo cancelCoupon(final Long couponId, final UserInfo user) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				ResultInfo resultInfo = couponService.cancelCoupon(couponId, user.getId(), user.getLoginName());
				if (!resultInfo.isSuccess()) {
					result.setMsg(resultInfo.getMsg());
				}
				result.setSuccess(resultInfo.isSuccess());
			}
		});
		return result;
	}

	/**
	 * 查询优惠券的审批流程
	 *
	 * @param id
	 * @return
	 */
	public List<CouponAudit> queryCouponAudit(Long id) {
		return couponService.queryCouponAudit(id);
	}

	public ResultInfo<PageInfo<CouponSend>> queryCouponSendList(final CouponSend cdo, final Integer startPage,
			final Integer pageSize) {
		final Map<String, Object> params = BeanUtil.beanMap(cdo);
		params.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
		final ResultInfo<PageInfo<CouponSend>> result = new ResultInfo<>();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				if (cdo != null && startPage > 0 && pageSize > 0) {
					cdo.setStartPage(startPage);
					cdo.setPageSize(pageSize);
					PageInfo<CouponSend> page = couponSendService.queryPageByParam(params,
							new PageInfo<CouponSend>(startPage, pageSize));
					result.setData(page);
					result.setSuccess(true);

				}
			}
		});
		return result;
	}

	public ResultInfo saveCouponSend(final String couponIdStr, final String userIdStr, final Integer allMember,
			final Long currentUserId, String currentUserName, final Integer msgSend, final String msgContent,
			final String name, final Integer status, final Integer couponSendType, final String startTime,
			final String endTime) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				AssertUtil.notBlank(couponIdStr, "传入的优惠券为空");
				if (!"".equals(userIdStr) || 1 == allMember
						|| couponSendType == CouponSendType.AUTO_NEWUSER.ordinal()) {
					List<Long> couponIdArr = JSONArray.parseArray(couponIdStr, Long.class);
					StringBuffer sb = new StringBuffer();
					for (Long cid : couponIdArr) {
						if (cid == null) {
							continue;
						}
						sb.append(cid);

						Coupon coupon = couponService.queryById(cid);
						if (coupon == null) {
							throw new ServiceException("优惠券[" + cid + "]不存在");
						}
						if (coupon.getStatus() != CouponStatus.PASSED.ordinal()) {
							throw new ServiceException("优惠券[" + cid + "]状态异常,仅状态为审核通过的才可发放");
						}
						if (coupon.getOfferType() != null
								&& coupon.getOfferType() == OfferType.ONLY_EXCHANGE.ordinal()) {
							throw new ServiceException("优惠券[" + cid + "]仅可以用领取");
						}

						sb.append(",");
					}

					AssertUtil.notBlank(sb.toString(), "传入的优惠券为空");

					Integer userLength = 0;
					String userCodes = "";
					if (userIdStr != null && !"".equals(userIdStr)) {
						String[] userIdArr = userIdStr.replace("，", ",").split(",");
						userLength = userIdArr.length * couponIdArr.size();
					}
					userCodes = processUserCodes(allMember, userIdStr);

					Date now = new Date();
					CouponSend couponSend = new CouponSend();
					couponSend.setCouponIds(sb.toString());
					couponSend.setCreateTime(now);
					couponSend.setCreateUser(currentUserId);
					if (startTime != null && !"".equals(startTime)) {
						String NstartTime = startTime + " 00:00:00";
						couponSend.setStartTime(DateTimeFormatUtil.parseyyyyMMddHHmmssDate(NstartTime));// 有效期
					}
					if (endTime != null && !"".equals(endTime)) {
						String NendTime = endTime + " 23:59:59";
						couponSend.setEndTime(DateTimeFormatUtil.parseyyyyMMddHHmmssDate(NendTime));// 有效期

					}
					couponSend.setModifyTime(now);
					couponSend.setMsgContent(msgContent);
					couponSend.setName(name);
					couponSend.setSendMsg(msgSend);
					couponSend.setSendStatus(CouponSendSendStatus.NOSEND.ordinal());
					couponSend.setStatus(status);
					couponSend.setToAll(allMember);
					couponSend.setToUserIds(userCodes);
					couponSend.setType(couponSendType);
					couponSend.setSendSize((long) 0);
					couponSend.setSendResult("");
					couponSend.setSendSize(userLength.longValue());
					couponSendService.insert(couponSend);

				} else {
					throw new ServiceException("用户列表为空！");
				}
			}
		});
		return result;
	}

	private String processUserCodes(Integer allMember, String userIdStr) {
		String userCodes = "";
		if (allMember != null && allMember == 0) {
			if (StringUtils.isBlank(userIdStr)) {
				throw new ServiceException("传入的用户清单为空");
			}
			String[] users = userIdStr.replace("，", ",").split(",");
			if (users == null || users.length == 0) {
				throw new ServiceException("传入的用户清单为空");
			}
			StringBuilder sbb = new StringBuilder();
			for (String u : users) {
				if (StringUtils.isBlank(u))
					continue;
				if (!VerifyUtil.verifyTelephone(u.trim()))
					throw new ServiceException("手机号错误:" + u);
				sbb.append(u.trim()).append(",");
			}
			String us = sbb.toString();
			if (StringUtils.isBlank(us)) {
				throw new ServiceException("传入的用户清单为空");
			}
			userCodes = us.substring(0, us.length() - 1);

		}
		return userCodes;
	}

	public ResultInfo updateCouponSend(final Long id, final String couponIdStr, final String userIdStr,
			final Integer allMember, Long currentUserId, String currentUserName, final Integer msgSend,
			final String msgContent, final String name, final Integer status, final Integer couponSendType,
			final String startTime, final String endTime) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				AssertUtil.notBlank(couponIdStr, "传入的优惠券为空");

				if (!"".equals(userIdStr) || 1 == allMember
						|| couponSendType == CouponSendType.AUTO_NEWUSER.ordinal()) {
					List<Long> couponIdArr = JSONArray.parseArray(couponIdStr, Long.class);
					StringBuffer sb = new StringBuffer();
					for (Long cid : couponIdArr) {
						if (cid == null) {
							continue;
						}
						sb.append(cid);
						sb.append(",");
					}
					AssertUtil.notBlank(sb.toString(), "传入的优惠券为空");

					Integer userLength = 0;
					if (userIdStr != null && !"".equals(userIdStr)) {
						String[] userIdArr = userIdStr.replace("，", ",").split(",");
						userLength = userIdArr.length * couponIdArr.size();
					}

					Date now = new Date();
					CouponSend couponSend = new CouponSend();
					couponSend.setCouponIds(sb.toString());
					if (startTime != null && !"".equals(startTime)) {
						String NstartTime = startTime + " 00:00:00";
						couponSend.setStartTime(DateTimeFormatUtil.parseyyyyMMddHHmmssDate(NstartTime));// 有效期
					}
					if (endTime != null && !"".equals(endTime)) {
						String NendTime = endTime + " 23:59:59";
						couponSend.setEndTime(DateTimeFormatUtil.parseyyyyMMddHHmmssDate(NendTime));// 有效期
					}
					couponSend.setModifyTime(now);
					couponSend.setMsgContent(msgContent);
					couponSend.setName(name);
					couponSend.setSendMsg(msgSend);
					couponSend.setSendStatus(CouponSendSendStatus.NOSEND.ordinal());
					couponSend.setStatus(status);
					couponSend.setToAll(allMember);
					couponSend.setToUserIds(processUserCodes(allMember, userIdStr));
					couponSend.setType(couponSendType);
					couponSend.setSendSize((long) 0);
					couponSend.setSendResult("");
					couponSend.setSendSize(userLength.longValue());
					couponSend.setId(id);
					couponSendService.updateNotNullById(couponSend);

				} else {
					throw new ServiceException("用户列表为空！");
				}
			}
		});

		return result;

	}

	/***
	 * 设为终止
	 *
	 * @param couponId
	 * @return
	 */
	public ResultInfo stopCouponSend(final Long couponSendId, final UserInfo user) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				ResultInfo resultInfo = couponSendService.stopCouponSend(couponSendId, user.getId(),
						user.getLoginName());
				if (!resultInfo.isSuccess()) {
					result.setMsg(resultInfo.getMsg());
				}
				result.setSuccess(resultInfo.isSuccess());
			}
		});
		return result;
	}

	public ResultInfo refusedCouponSend(final Long couponSendId, final String reason, final UserInfo user) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				ResultInfo resultInfo = couponSendService.refuseCouponSend(couponSendId, user.getId(),
						user.getLoginName(), reason);
				if (!resultInfo.isSuccess()) {
					result.setMsg(resultInfo.getMsg());
				}
				result.setSuccess(resultInfo.isSuccess());
			}
		});
		return result;
	}

	public ResultInfo approveCouponSendAuto(final Long couponSendId, final UserInfo user) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				// 更改状态
				ResultInfo resultInfo = couponSendService.approveCouponSend(couponSendId, user.getId(),
						user.getLoginName(), null);
				if (!resultInfo.isSuccess()) {
					result.setMsg(resultInfo.getMsg());
				}
				result.setSuccess(result.isSuccess());
			}
		});
		return result;
	}

	public ResultInfo approveCouponSend(Long couponSendId, UserInfo user) {
		return issueCoupon(couponSendId, user);
	}

	public Boolean cancelCouponSend(Long couponSendId, UserInfo user) {
		try {
			ResultInfo result = couponSendService.cancelCouponSend(couponSendId, user.getId(), user.getLoginName());
			return true;
		} catch (Exception e) {
			logger.info("批准优惠券失败");
			return false;
		}

	}

	/**
	 * 查询优惠券的审批流程
	 *
	 * @param id
	 * @return
	 */
	public List<CouponSendAudit> queryCouponSendAudit(Long id) {
		return couponSendService.queryCouponSendAudit(id);
	}

	/**
	 * 查询优惠券发放DO
	 */
	public CouponSend queryCouponSendDO(Long id) {
		return couponSendService.queryById(id);
	}

	public Coupon queryCouponDO(long cid) {
		return couponService.queryById(cid);
	}

	public ResultInfo issueCoupon(final Long couponSendId, final UserInfo user) {
		final ResultInfo result = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				CouponSend couponSend = couponSendService.queryById(couponSendId);

				AssertUtil.notNull(couponSend, "传入的优惠券发送为空");

				String couponIdStr = couponSend.getCouponIds();
				String userIdStr = couponSend.getToUserIds();
				Integer allMember = couponSend.getToAll();
				Long currentUserId = couponSend.getCreateUser();
				String currentUserName = "";
				Integer msgSend = couponSend.getSendMsg();
				String msgContent = couponSend.getMsgContent();

				AssertUtil.notBlank(couponIdStr, "传入的优惠券为空");

				List<Long> couponIds = new ArrayList<Long>();
				String[] couponIdArr = couponIdStr.replace("，", ",").split(",");
				if (couponIdArr != null && couponIdArr.length > 0) {
					for (String cid : couponIdArr) {
						couponIds.add(Long.valueOf(cid));
					}
				}
				if (allMember == 0) { // 非全体用户
					AssertUtil.notBlank(userIdStr, "用户列表为空");
					String[] userIdArr = userIdStr.replace("，", ",").split(",");
					List<String> loginNameList = new ArrayList<String>();
					loginNameList = Arrays.asList(userIdArr);
					List<String> res = couponUserService.sendCouponToUser(couponIds, currentUserId, currentUserName,
							loginNameList, msgSend, msgContent);
					// 更改状态
					couponSendService.approveCouponSend(couponSendId, user.getId(), user.getLoginName(), res);
					if (!org.springframework.util.CollectionUtils.isEmpty(res)) {
						result.setMsg(new FailInfo(res.toString()));
					}
				} else { // 全体用户
					MemberInfo memberInfo = new MemberInfo();
					memberInfo.setState(true);
					int pageSize = 100;
					int pageId = 1;
					PageInfo<MemberInfo> pageInfo = memberInfoService.queryPageByObject(memberInfo,
							new PageInfo<MemberInfo>(pageId, pageSize));// TODO
																		// 分页分布
					// System.out.println("发送 优惠券1
					// pageId............................................" +
					// pageId);
					int totalCount = pageInfo.getRecords();
					List<String> resList = new ArrayList<String>();
					List<MemberInfo> userList = pageInfo.getRows();
					if (userList != null && userList.size() > 0) {
						List<String> res = sendCoupon(couponIds, currentUserId, currentUserName, userList, msgSend,
								msgContent);
						if (res != null && res.size() > 0)
							resList.addAll(res);
					}
					if (totalCount > pageSize) {
						int residue = totalCount % pageSize;
						int times = totalCount / pageSize;
						if (residue != 0)
							times = times + 1;

						for (int i = 2; i <= times; i++) {
							PageInfo<MemberInfo> pageRes = memberInfoService.queryPageByObject(memberInfo,
									new PageInfo<MemberInfo>(i, pageSize));
							// System.out.println("发送 优惠券
							// pageId............................................"
							// + i);
							userList = pageRes.getRows();
							if (userList != null && userList.size() > 0) {
								List<String> res = sendCoupon(couponIds, currentUserId, currentUserName, userList,
										msgSend, msgContent);
								if (res != null && res.size() > 0)
									resList.addAll(res);
							}
						}
					}
					// 更改状态
					ResultInfo resultInfo = couponSendService.approveCouponSend(couponSendId, user.getId(),
							user.getLoginName(), resList);
					if (resList != null && resList.size() > 0) {
						result.setMsg(new FailInfo(resList.toString()));
					}
				}
			}
		});
		return result;
	}

	private List<String> sendCoupon(List<Long> couponIds, Long currentUserId, String currentUserName,
			List<MemberInfo> userList, Integer msgSend, String msgContent) throws Exception {
		List<String> loginNameList = new ArrayList<String>();
		for (MemberInfo memberInfo : userList) {
			if (memberInfo.getMobile() != null && !"".equals(memberInfo.getMobile()))
				loginNameList.add(memberInfo.getMobile());
		}
		List<String> res = couponUserService.sendCouponToUser(couponIds, currentUserId, currentUserName, loginNameList,
				msgSend, msgContent);
		return res;
	}

	public ResultInfo<SupplierInfo> searchSupplierInfoById(final Long supplierId) {
		final ResultInfo<SupplierInfo> result = new ResultInfo<>();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				List<SupplierType> supplierTypes = new ArrayList<>();
				supplierTypes.add(SupplierType.ASSOCIATE);
				SupplierResult resultS = pmService.fuzzyQuerySupplier(supplierId, supplierTypes, null, 1, 1);
				if (resultS.getTotalCount() >= 1) {
					SupplierInfo supplier = resultS.getSupplierInfoList().get(0);
					result.setData(supplier);
				}
			}
		});
		return result;
	}

	public SupplierResult searchSupplier(Long supplierId, String name, Integer startPage, Integer pageSize) {
		try {
			List<SupplierType> supplierTypes = new ArrayList<SupplierType>();
			supplierTypes.add(SupplierType.ASSOCIATE);
			SupplierResult result = pmService.fuzzyQuerySupplier(supplierId, supplierTypes, name, startPage, pageSize);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public String getOfflineCouponCode(String couponCodeKey) {
		return couponService.queryOfflineCouponCode(couponCodeKey);
	}

	public ResultInfo<Boolean> activeCoupon(Long couponId, Integer activeStatus, UserInfo user) {
		Coupon coupon = new Coupon();
		coupon.setId(couponId);
		coupon.setActiveStatus(activeStatus);
		coupon.setUpdateUserId(user.getId());
		coupon.setOperator(user.getUserName());
		try {
			return new ResultInfo<Boolean>(couponService.activeCoupon(coupon));
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, coupon);
			return new ResultInfo<Boolean>(failInfo);
		}
	}
}
