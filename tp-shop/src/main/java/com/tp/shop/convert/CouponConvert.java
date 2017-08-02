package com.tp.shop.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.tp.common.vo.PageInfo;
import com.tp.dto.mmp.CouponReceiveDTO;
import com.tp.dto.mmp.ExchangeCouponCodeDTO;
import com.tp.dto.mmp.MyCouponDTO;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.enums.CouponEnum;
import com.tp.m.query.promotion.QueryCoupon;
import com.tp.m.util.DateUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.coupon.CouponVO;
import com.tp.query.mmp.MyCouponQuery;

/**
 * 优惠券封装类
 * @author zhuss
 * @2016年1月12日 下午1:24:25
 */
public class CouponConvert {

	/**
	 * 封装优惠券列表入参对象
	 * @param coupon
	 * @return
	 */
	public static MyCouponQuery convertCouponQuery(QueryCoupon coupon){
		MyCouponQuery m = new MyCouponQuery();
		m.setCouponUserStatus(convertCUsrStatu(coupon.getStatus()));
		m.setMemberId(coupon.getUserid());
		m.setPageSize(PageConstant.MAX_PAGESIZE);
		m.setStartPage(Integer.valueOf(coupon.getCurpage()));
		return m;
	}
	
	/**
	 * 封装兑换优惠券入参
	 * @param coupon
	 * @return
	 */
	public static ExchangeCouponCodeDTO convertExchangeQuery(QueryCoupon coupon){
		ExchangeCouponCodeDTO ecc = new ExchangeCouponCodeDTO();
		ecc.setUserId(coupon.getUserid());
		ecc.setMobile(coupon.getTel());
		ecc.setNickName(coupon.getTel());
		ecc.setExchangeCode(coupon.getCcode().trim().toUpperCase());
		ecc.setCouponId(coupon.getCouponId());
		if(coupon.getCid()!=null){
			ecc.setCouponUserId(Long.valueOf(coupon.getCid()));
		}
		return ecc;
	}
	
	/**
	 * 封装领取优惠券入参
	 * @param coupon
	 * @return
	 */
	public static CouponReceiveDTO convertReceiveQuery(QueryCoupon coupon){
		CouponReceiveDTO cr = new CouponReceiveDTO();
		cr.setCode(coupon.getCcode());
		cr.setMemberId(coupon.getUserid());
		cr.setMobile(coupon.getTel());
		return cr;
	}
	
	/**
	 * 封装优惠券集合
	 * @return
	 */
	public static Page<CouponVO> convertCouponList(PageInfo<MyCouponDTO> clist){
		Page<CouponVO> pages = new Page<>();
		if(null != clist){
			List<MyCouponDTO> ls = clist.getRows();
			List<CouponVO> cv = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(ls)){
				for(MyCouponDTO c : ls){
					cv.add(convertCoupon(c));
				}
				pages.setFieldTCount(cv, clist.getPage(), clist.getRecords());
			}
			pages.setCurpage(clist.getPage());
		}
		return pages;
	}
	
	/**
	 * 封装优惠券集合
	 * @return
	 */
	public static Page<CouponVO> convertCouponList(Map<String, List<OrderCouponDTO>> moc){
		Page<CouponVO> pages = new Page<>();
		if(MapUtils.isNotEmpty(moc)){
			List<CouponVO> cv = new ArrayList<>();
			for (String key : moc.keySet()) {
				List<OrderCouponDTO> oc = moc.get(key);
				if(CollectionUtils.isNotEmpty(oc)){
						for(OrderCouponDTO c : oc){
							cv.add(convertOrderCoupon(c));
						}
				}
			}
			pages.setFieldTCount(cv, PageConstant.DEFAULT_CURPAGE_INT,PageConstant.MAX_PAGESIZE);
		}
		return pages;
	}
	
	/**
	 * 封装优惠券对象
	 * @return
	 */
	public static CouponVO convertCoupon(MyCouponDTO cdto){
		CouponVO c = new CouponVO();
		c.setCid(StringUtil.getStrByObj(cdto.getCouponUserId()));
		c.setName(cdto.getCouponName());
		c.setPrice(StringUtil.getStrByObj(cdto.getFaceValue()));
		String rule = "";
		if(cdto.getCouponType() == CouponType.HAS_CONDITION.ordinal()){//优惠券才有此限制
			if(cdto.getNeedOverMon() > 0){
				rule = "满"+cdto.getNeedOverMon()+"可用";
			}
		}
		c.setExchangeXgMoney(cdto.getExchangeXgMoney());
		c.setRule(rule);
		c.setStatus(StringUtil.getStrByObj(cdto.getStatus()));
		c.setValidtime(DateUtil.convertFullTime(cdto.getCouponUseStime(), cdto.getCouponUseEtime(), DateUtil.DATE_SEP));
		List<String> includes = cdto.getRangeInclude();
		if(CollectionUtils.isNotEmpty(includes)){
			if(includes.size() > 10)c.setScope(cdto.getRangeInclude().subList(0, 10));
			c.setScope(cdto.getRangeInclude());
		}else{
			List<String> l = new ArrayList<>();
			l.add("全场通用");
			c.setScope(l);
		}
		c.setType(StringUtil.getStrByObj(cdto.getCouponType()));
		return c;
	}
	
	/**
	 * 封装优惠券对象
	 * @return
	 */
	public static CouponVO convertOrderCoupon(OrderCouponDTO ocdto){
		CouponVO c = new CouponVO();
		c.setCid(StringUtil.getStrByObj(ocdto.getCouponUserId()));
		c.setName(ocdto.getCouponName());
		c.setPrice(StringUtil.getStrByObj(ocdto.getFaceValue()));
		String rule = "";
		if(ocdto.getCouponType() == CouponType.HAS_CONDITION.ordinal()){//优惠券才有此限制
			if(ocdto.getNeedOverMon() > 0){
				rule = "满"+ocdto.getNeedOverMon()+"可用";
			}
		}
		c.setRule(rule);
		c.setValidtime(DateUtil.convertFullTime(ocdto.getCouponUseStime(), ocdto.getCouponUseEtime(), DateUtil.DATE_SEP));
		c.setType(StringUtil.getStrByObj(ocdto.getCouponType()));
		return c;
	}
	
	/**
	 * 封装用户中心的优惠券状态：0 正常 1 已使用 2 无效
	 * @param status : 1正常 2 已使用 3无效
	 * @return
	 */
	public static CouponUserStatus convertCUsrStatu(String status){
		if(status.equals(CouponEnum.Status.NORMAL.code)) return CouponUserStatus.NORMAL;
		if(status.equals(CouponEnum.Status.USED.code)) return CouponUserStatus.USED;
		if(status.equals(CouponEnum.Status.OVERDUE.code)) return CouponUserStatus.OVERDUE;
		 return CouponUserStatus.OVERDUE;
	}
}
