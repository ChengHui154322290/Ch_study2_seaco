package com.tp.proxy.mmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.ExchangeCouponCodeDTO;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.ExchangeCouponChannelCode;
import com.tp.model.mmp.PointDetail;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.facade.CouponFacadeProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IExchangeCouponChannelCodeService;
import com.tp.service.mmp.IPointDetailService;
/**
 * 积分日志详情表代理层
 * @author szy
 *
 */
@Service
public class PointDetailProxy extends BaseProxy<PointDetail>{

	@Autowired
	private IPointDetailService pointDetailService;
	@Autowired
	private ICouponService couponService;
	@Autowired
	private ICouponUserService couponUserService;
	@Autowired
	private IExchangeCouponChannelCodeService exchangeCouponChannelCodeService;
	@Autowired
	private CouponFacadeProxy couponFacadeProxy;

	@Override
	public IBaseService<PointDetail> getService() {
		return pointDetailService;
	}
	
	/**
	 * 使用优惠券兑换积分
	 * 1.根据现金券编码查询到优惠券信息
	 * 2.获取到兑换金额
	 * 3.生成积分信息
	 * 4.如果成功则修改现金券
	 * 5.记录现金券使用情况
	 * @param couponCode
	 */
	public ResultInfo<Boolean> exchangePoint(ExchangeCouponCodeDTO exchangeCouponCodeDTO){
		String couponId=exchangeCouponCodeDTO.getCouponId();// 优惠券场次
		Coupon coupon=new Coupon();
		List<Long> couponUserIds = new ArrayList<Long>();
		if(StringUtils.isBlank(couponId)){
			ResultInfo<Boolean> result = couponFacadeProxy.exchangeCouponsCode(exchangeCouponCodeDTO);
			if(!result.success){
				return result;
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("memberId", exchangeCouponCodeDTO.getUserId());
			params.put("exchangeCode", exchangeCouponCodeDTO.getExchangeCode());
			ExchangeCouponChannelCode exchangeCouponChannelCode = exchangeCouponChannelCodeService.queryUniqueByParams(params);
			if(null==exchangeCouponChannelCode){
				return new ResultInfo<>(new FailInfo("未兑换成功"));
			}
			 coupon = couponService.queryById(exchangeCouponChannelCode.getCouponId());
			if(CouponType.NO_CONDITION.ordinal()!=coupon.getCouponType()){//不是现金券不能兑换成功积分
				return result;
			}
			couponUserIds.add(exchangeCouponChannelCode.getCouponUserId());
		
		}else{
			couponUserIds.add(exchangeCouponCodeDTO.getCouponUserId());
			coupon = couponService.queryById(Long.valueOf(couponId));
		}
		PointDetail pointDetail = new PointDetail();
		pointDetail.setBizId(exchangeCouponCodeDTO.getExchangeCode());
		pointDetail.setBizType(PointConstant.BIZ_TYPE.COUPON.code);
		pointDetail.setCreateUser(Constant.AUTHOR_TYPE.MEMBER+exchangeCouponCodeDTO.getNickName());
		pointDetail.setMemberId(exchangeCouponCodeDTO.getUserId());
		pointDetail.setPoint(coupon.getFaceValue()*100);
		pointDetail.setTitle(PointConstant.BIZ_TYPE.COUPON.title);
		pointDetail.setPointType(PointConstant.OPERATE_TYPE.ADD.type);
		try {
			pointDetail = pointDetailService.insert(pointDetail);
			couponUserService.updateCouponUserStatus(couponUserIds, CouponUserStatus.USED.ordinal());
			return new ResultInfo<Boolean>(Boolean.TRUE);
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,pointDetail,couponUserIds);
			return new ResultInfo<>(failInfo);
		}
	}

}
