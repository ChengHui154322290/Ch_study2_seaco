package com.tp.proxy.ord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponUser;
import com.tp.model.ord.CartItemInfo;
import com.tp.proxy.mmp.CouponUserProxy;
import com.tp.proxy.ord.split.CouponFilterProxy;

@Service
public class OrderCouponProxy {

	private static final Logger logger = LoggerFactory.getLogger(OrderCouponProxy.class);
	@Autowired
	private CouponFilterProxy couponFilterProxy;
	@Autowired
	private CouponUserProxy couponUesrProxy;
	@Autowired
	private CartProxy cartProxy;
	
	/**
	 * 获取下单时可以使用的优惠券、现金券列表
	 * @param memberId 会员
	 * @param token 
	 * @param sourceType 下单平台
	 * @return
	 */
	public ResultInfo<Map<String, List<OrderCouponDTO>>> queryOrderCoupon(Long memberId,String token, Integer sourceType,Long shopId){
		ShoppingCartDto shoppingCartDto = cartProxy.getCartDto(memberId, token,shopId);
		List<CartItemInfo> cartItemInfoList = shoppingCartDto.getCartItemInfoList();
		if(CollectionUtils.isEmpty(cartItemInfoList)){
			return new ResultInfo<Map<String, List<OrderCouponDTO>>>();
		}
		cartItemInfoList.forEach(new Consumer<CartItemInfo>(){
			public void accept(CartItemInfo t) {
				t.setPlatformId(sourceType);
			}
		});
		try{
			Map<String,List<OrderCouponDTO>> resultMap = new HashMap<String,List<OrderCouponDTO>>();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("toUserId", memberId);
			params.put("status", CouponUserStatus.NORMAL.ordinal());
			List<CouponUser> couponUserList = couponUesrProxy.queryCouponListbyCouponUserIdList(params).getData();
			if(CollectionUtils.isNotEmpty(couponUserList)){
				Map<Coupon,List<CartItemInfo>> couponCartItemMap = new HashMap<Coupon,List<CartItemInfo>>();
				for(CouponUser couponUser:couponUserList){
					couponUser.getCoupon().receiveDate = couponUser.getCreateTime();
					couponCartItemMap.put(couponUser.getCoupon(), new ArrayList<CartItemInfo>(cartItemInfoList));
				}
				List<Coupon> couponList= couponFilterProxy.filterByOrder(couponCartItemMap);
				if(CollectionUtils.isNotEmpty(couponList)){
					for(Coupon coupon:couponList){
						for(CouponUser couponUser:couponUserList){
							if(couponUser.getCoupon().getId().equals(coupon.getId())){
								OrderCouponDTO orderCoupon = initOrderCouponDto(couponUser);
								List<OrderCouponDTO> orderCouponList = resultMap.get(coupon.getCouponType().toString());
								if(orderCouponList==null){
									orderCouponList = new ArrayList<OrderCouponDTO>();
									
								}
								orderCouponList.add(orderCoupon);
								resultMap.put(coupon.getCouponType().toString(), orderCouponList);
							}
						}
					}
				}
			}
			return new ResultInfo<>(resultMap);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,shoppingCartDto,memberId,sourceType);
			return new ResultInfo<>(failInfo);
		}
	}
	
	/**
	 * 获取下单时可以使用的优惠券、现金券总数
	 * @param memberId 会员
	 * @param token 
	 * @param sourceType 下单平台
	 * @return
	 */
	public ResultInfo<Integer> queryOrderCouponCount(Long memberId,String token, Integer sourceType,Long shopId){
		ShoppingCartDto shoppingCartDto = cartProxy.getCartDto(memberId, token,shopId);
		List<CartItemInfo> cartItemInfoList = shoppingCartDto.getCartItemInfoList();
		if(CollectionUtils.isEmpty(cartItemInfoList)){
			return new ResultInfo<Integer>(0);
		}
		cartItemInfoList.forEach(new Consumer<CartItemInfo>(){
			public void accept(CartItemInfo t) {
				t.setPlatformId(sourceType);
			}
		});
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("toUserId", memberId);
			params.put("status", CouponUserStatus.NORMAL.ordinal());
			List<CouponUser> couponUserList = couponUesrProxy.queryCouponListbyCouponUserIdList(params).getData();
			if(CollectionUtils.isNotEmpty(couponUserList)){
				Map<Coupon,List<CartItemInfo>> couponCartItemMap = new HashMap<Coupon,List<CartItemInfo>>();
				for(CouponUser couponUser:couponUserList){
					couponUser.getCoupon().receiveDate = couponUser.getCreateTime();
					couponCartItemMap.put(couponUser.getCoupon(), new ArrayList<CartItemInfo>(cartItemInfoList));
				}
				List<Coupon> couponList= couponFilterProxy.filterByOrder(couponCartItemMap);
				if(CollectionUtils.isNotEmpty(couponList)){
					couponUserList.removeIf(new Predicate<CouponUser>(){
						public boolean test(CouponUser couponUser) {
							for(Coupon coupon:couponList){
								if(couponUser.getCoupon().getId().equals(coupon.getId())){
									return false;
								}
							}
							return true;
						}
					});
				}else{
					return new ResultInfo<>(0);
				}
				return new ResultInfo<>(couponUserList.size());
			}
			return new ResultInfo<>(0);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,shoppingCartDto,memberId,sourceType);
			return new ResultInfo<>(failInfo);
		}
	}
	
	private OrderCouponDTO initOrderCouponDto(CouponUser couponUser){
		Coupon coupon = couponUser.getCoupon();
        OrderCouponDTO couponDto = new OrderCouponDTO();
        couponDto.setCouponUserId(couponUser.getId());
        couponDto.setCouponName(coupon.getCouponName());
        couponDto.setCouponType(coupon.getCouponType());
        couponDto.setFaceValue(coupon.getFaceValue());
        couponDto.setNeedOverMon(coupon.getNeedOverMon());
        couponDto.setSourceId(coupon.getSourceId());
        couponDto.setSourceName(coupon.getSourceName());
        couponDto.setSourceType(coupon.getSourceType());

        if (Integer.valueOf(coupon.getCouponUseType()) == 0) {
            if (coupon.getCouponUseStime() != null)
                couponDto.setCouponUseStime(coupon.getCouponUseStime());
            if (coupon.getCouponUseStime() != null)
                couponDto.setCouponUseEtime(coupon.getCouponUseEtime());
        } else {
            Integer receiveDay = coupon.getUseReceiveDay();
            Date receiveTime = couponUser.getCreateTime();
            if (receiveDay != null && receiveTime != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(receiveTime);
                calendar.add(Calendar.DAY_OF_MONTH, receiveDay);
                Date newTime = calendar.getTime();

                couponDto.setCouponUseStime(receiveTime);
                couponDto.setCouponUseEtime(newTime);
            }
        }

        return couponDto;
    }
}
