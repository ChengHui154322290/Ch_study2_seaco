package com.tp.proxy.ord.split;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.dto.mmp.enums.CouponStatus;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponRange;
import com.tp.model.ord.CartItemInfo;

@Service
public class CouponFilterProxy {
	/**
	 * 计算价格过滤器
	 * @param couponCartItemMap
	 * @return
	 */
	public Map<Coupon,List<CartItemInfo>> operateAmountfilter(Map<Coupon,List<CartItemInfo>> couponCartItemMap){
		Set<DeleteFilter> filterSet = new LinkedHashSet<DeleteFilter>();
		filterSet.add(new WapDeleteFilter());
		filterSet.add(new UnWapDeleteFilter());
		filterSet.add(new MainBodyFilter());
		filterSet.add(new SeaFilter());
		filterSet.add(new PassFilter());
		filterSet.add(new TimeValidFilter());
		filterSet.add(new PlatformFilter());
		Iterator<DeleteFilter> iterator = filterSet.iterator();
		while(iterator.hasNext()){
			couponCartItemMap = filter(couponCartItemMap,iterator.next());
		}
		Set<DeleteFilterAll> filterAllSet = new LinkedHashSet<DeleteFilterAll>();
		filterAllSet.add(new AmountFilter());
		Iterator<DeleteFilterAll> iteratorAll = filterAllSet.iterator();
		while(iteratorAll.hasNext()){
			couponCartItemMap = filter(couponCartItemMap,iteratorAll.next());
		}
		return couponCartItemMap;
	}
	
	public List<Coupon> filterByOrder(Map<Coupon,List<CartItemInfo>> couponCartItemMap){
		if(MapUtils.isNotEmpty(couponCartItemMap)){
			couponCartItemMap.forEach(new BiConsumer<Coupon,List<CartItemInfo>>(){
				public void accept(Coupon t, List<CartItemInfo> u) {
					if(CollectionUtils.isNotEmpty(u)){
						u.removeIf(new Predicate<CartItemInfo>(){
							public boolean test(CartItemInfo t) {
								return !t.getSelectedBoolean();
							}
						});
					}
				}
			});
		}
		operateAmountfilter(couponCartItemMap);
		List<Coupon> couponList = new ArrayList<Coupon>();
		if(MapUtils.isNotEmpty(couponCartItemMap)){
			for(Map.Entry<Coupon,List<CartItemInfo>> entry:couponCartItemMap.entrySet()){
				if(CollectionUtils.isNotEmpty(entry.getValue())){
					couponList.add(entry.getKey());
				}
			}
		}
		return couponList;
	}
	
	public Map<Coupon,List<CartItemInfo>> filter(Map<Coupon,List<CartItemInfo>> couponCartItemMap,DeleteFilterAll deleteFilter){
		couponCartItemMap.forEach(new BiConsumer<Coupon,List<CartItemInfo>>(){
			public void accept(Coupon coupon, List<CartItemInfo> cartItemInfoList) {
				if(CollectionUtils.isNotEmpty(cartItemInfoList)){
					Boolean deleted = deleteFilter.deleted(coupon, cartItemInfoList);
					if(!deleted){
						cartItemInfoList.removeAll(cartItemInfoList);
					}
				}
			}
		});
		return couponCartItemMap;
	}
	
	public Map<Coupon,List<CartItemInfo>> filter(Map<Coupon,List<CartItemInfo>> couponCartItemMap,DeleteFilter deleteFilter){
		couponCartItemMap.forEach(new BiConsumer<Coupon,List<CartItemInfo>>(){
			public void accept(Coupon coupon, List<CartItemInfo> cartItemInfoList) {
				if(CollectionUtils.isNotEmpty(cartItemInfoList)){
					cartItemInfoList.removeIf(new Predicate<CartItemInfo>(){
						public boolean test(CartItemInfo itemInfo) {
							if(!deleteFilter.deleted(coupon, itemInfo)){
								return Boolean.TRUE;
							}
							return false;
						}
					});
				}
			}
		});
		return couponCartItemMap;
	}
	
	public Map<Coupon,List<CartItemInfo>> filter(Map<Coupon,List<CartItemInfo>> couponCartItemMap,DeleteFilter deleteFilter,CartItemInfo cartItemInfo){
		couponCartItemMap.forEach(new BiConsumer<Coupon,List<CartItemInfo>>(){
			public void accept(Coupon coupon, List<CartItemInfo> cartItemInfoList) {
				if(CollectionUtils.isNotEmpty(cartItemInfoList)){
					cartItemInfoList.removeIf(new Predicate<CartItemInfo>(){
						public boolean test(CartItemInfo itemInfo) {
							if(itemInfo.getSkuCode().equals(cartItemInfo.getSkuCode()) 
							&& itemInfo.getTopicId().equals(cartItemInfo.getTopicId())
							&& !deleteFilter.deleted(coupon, itemInfo)){
								return Boolean.TRUE;
							}
							return false;
						}
					});
				}
			}
		});
		return couponCartItemMap;
	}
	
	interface Filter<T>{
		Boolean deleted(Coupon coupon,T t);
	}
	interface DeleteFilter extends Filter<CartItemInfo>{
		Boolean deleted(Coupon coupon,CartItemInfo itemInfo);
	}
	interface DeleteFilterAll extends Filter<List<CartItemInfo>>{
		Boolean deleted(Coupon coupon,List<CartItemInfo> itemInfoList);
	}
	/**包含过滤*/
	class WapDeleteFilter implements DeleteFilter{
		public Boolean deleted(Coupon coupon, CartItemInfo itemInfo) {
			List<CouponRange> couponRangeList = coupon.getCouponRangeList();
			if(CollectionUtils.isNotEmpty(couponRangeList)){
				List<CouponRange> wapCouponRangeList = new ArrayList<CouponRange>();
				for(CouponRange couponRange:couponRangeList){
					if(couponRange.getRangeType()==0){
						wapCouponRangeList.add(couponRange);
					}
				}
				if(CollectionUtils.isNotEmpty(wapCouponRangeList)){
					for(int i=0;i<wapCouponRangeList.size();i++){
						CouponRange couponRange = wapCouponRangeList.get(i);
						//类别:大中小
						if(couponRange.getCategorySmallId()!=null && couponRange.getCategorySmallId().equals(itemInfo.getSmallId())){
							return Boolean.TRUE;
						}
						if(couponRange.getCategoryMiddleId()!=null && couponRange.getCategoryMiddleId().equals(itemInfo.getMediumId())){
							return Boolean.TRUE;
						}
						if(couponRange.getCategoryId()!=null && couponRange.getCategoryId().equals(itemInfo.getLargeId())){
							return Boolean.TRUE;
						}
						//品牌
						if(couponRange.getBrandId()!=null && couponRange.getBrandId().equals(itemInfo.getBrandId())){
							return Boolean.TRUE;
						}
						//商品  暂时SKU.
						if(couponRange.getCode()!=null && couponRange.getCode().equals(itemInfo.getSkuCode()) && "0".equals(couponRange.getType()) ){
							return Boolean.TRUE;
						}
						//专场
						if(couponRange.getCode()!=null && couponRange.getCode().equals(String.valueOf(itemInfo.getTopicId()))&& "2".equals(couponRange.getType())){
							return Boolean.TRUE;
						}
					}
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		}
	}
	
	/**不包含过滤*/
	class UnWapDeleteFilter implements DeleteFilter{
		public Boolean deleted(Coupon coupon, CartItemInfo itemInfo) {
			List<CouponRange> couponRangeList = coupon.getCouponRangeList();
			if(CollectionUtils.isNotEmpty(couponRangeList)){
				List<CouponRange> unWapCouponRangeList = new ArrayList<CouponRange>();
				for(CouponRange couponRange:couponRangeList){
					if(couponRange.getRangeType()==1){
						unWapCouponRangeList.add(couponRange);
					}
				}
				if(CollectionUtils.isNotEmpty(unWapCouponRangeList)){
					for(int i=0;i<unWapCouponRangeList.size();i++){
						CouponRange couponRange = unWapCouponRangeList.get(i);
						//类别:大中小
						if(couponRange.getCategorySmallId()!=null && couponRange.getCategorySmallId().equals(itemInfo.getSmallId())){
							return Boolean.FALSE;
						}
						if(couponRange.getCategorySmallId()==null &&couponRange.getCategoryMiddleId()!=null && couponRange.getCategoryMiddleId().equals(itemInfo.getMediumId())){
							return Boolean.FALSE;
						}
						if(couponRange.getCategoryMiddleId()==null&&couponRange.getCategoryId()!=null && couponRange.getCategoryId().equals(itemInfo.getLargeId())){
							return Boolean.FALSE;
						}
						//品牌
						if(couponRange.getBrandId()!=null && couponRange.getBrandId().equals(itemInfo.getBrandId())){
							return Boolean.FALSE;
						}
						//商品  暂时SKU.
						if(couponRange.getCode()!=null && couponRange.getCode().equals(itemInfo.getSkuCode())&&"0".equals(couponRange.getType()) ){
							return Boolean.FALSE;
						}
						//专场
						if(couponRange.getCode()!=null && couponRange.getCode().equals(String.valueOf(itemInfo.getTopicId()))&&"2".equals(couponRange.getType()) ){
							return Boolean.FALSE;
						}
					}
				}
			}
			return Boolean.TRUE;
		}
	}
	
	/**主体*/
	class MainBodyFilter implements DeleteFilter{
		public Boolean deleted(Coupon coupon, CartItemInfo itemInfo) {
			if(coupon.getSourceId()==null || coupon.getSourceId()==0){
				return Boolean.TRUE;
			}
			if(coupon.getSourceId().equals(itemInfo.getSupplierId())){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
	}
	/**海购，非海购  0全1非2是*/
	class SeaFilter implements DeleteFilter{
		public Boolean deleted(Coupon coupon, CartItemInfo itemInfo) {
			if(coupon.getHitaoSign()==null || coupon.getHitaoSign().contains("0")){
				return Boolean.TRUE;
			}
			if(coupon.getHitaoSign().contains(""+itemInfo.getWavesSign())){
				return  Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
	}

	/**是否已满足金额*/
	class AmountFilter implements DeleteFilterAll{
		public Boolean deleted(Coupon coupon,List<CartItemInfo> itemInfoList){
			if(coupon.getCouponType().equals(CouponType.HAS_CONDITION.ordinal())){
				if(CollectionUtils.isNotEmpty(itemInfoList)){
					Double orgItemAmount = Constant.ZERO;
					for(CartItemInfo cartItemInfo:itemInfoList){
						orgItemAmount = toPrice(add(orgItemAmount,multiply(cartItemInfo.getSalePrice(), cartItemInfo.getQuantity()))).doubleValue();
					}
					if(orgItemAmount>=coupon.getNeedOverMon()){
						return Boolean.TRUE;
					}
				}
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
	}
	
	/**是否审核通过*/
	class PassFilter implements DeleteFilter{
		public Boolean deleted(Coupon coupon, CartItemInfo itemInfo) {
			return coupon.getStatus() == CouponStatus.PASSED.ordinal();
		}
	}
	
	/**时间有效性*/
	class TimeValidFilter implements DeleteFilter{
		public Boolean deleted(Coupon coupon, CartItemInfo itemInfo) {
			Integer useType = coupon.getCouponUseType();
            if (useType != null) { // 惠券使用 类型 0：时间段有效 1：领取有效
                if (useType == 0) {// 时间段有效
                    Date useStime = coupon.getCouponUseStime();
                    Date useEtime = coupon.getCouponUseEtime();
                    Date now = new Date();
                    if ((now.after(useStime) || now.equals(useStime)) && (now.before(useEtime) || now.equals(useEtime))) {
                        // 在可用时间段以内，继续执行
                    } else {// 不在使用时间段
                        return Boolean.FALSE;
                    }

                } else {// 领取后几日内有效
                    Integer receiveDay = coupon.getUseReceiveDay();
                    Date now = new Date();
                    Date receiveTime = coupon.receiveDate;
                    if (receiveDay != null && receiveTime != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(receiveTime);
                        calendar.add(Calendar.DAY_OF_MONTH, receiveDay);
                        Date newTime = calendar.getTime();
                        if (now.before(newTime) || now.equals(newTime)) {
                            // 在可用时间段以内，继续执行
                        } else {// 过了使用日期
                        	 return Boolean.FALSE;
                        }
                    } else {
                        // 此优惠券有问题
                    	return Boolean.FALSE;
                    }
                }
            }
            return Boolean.TRUE;
		}
		
	}

	/**适用下单平台*/
	class PlatformFilter implements DeleteFilter{
		public Boolean deleted(Coupon coupon, CartItemInfo itemInfo) {
			// 所用平台是否符合要求
            String userPlatform = coupon.getUsePlantform();
            if (userPlatform != null && !"".equals(userPlatform)) {
                String[] plarray = userPlatform.split(",");
                List<String> plList = Arrays.asList(plarray);
                if (!plList.contains(PlatformEnum.ALL.getCode() + "")) {
                    if (!plList.contains(itemInfo.getPlatformId().toString() + "")) {
                       return Boolean.FALSE;// 不在符合条件的平台
                    }
                }
            }
            return Boolean.TRUE;
		}
		
	}
}
