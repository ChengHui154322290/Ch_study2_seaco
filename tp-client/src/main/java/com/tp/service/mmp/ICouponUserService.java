package com.tp.service.mmp;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.*;
import com.tp.dto.mmp.enums.CouponSendType;
import com.tp.model.mmp.CouponUser;
import com.tp.query.mmp.CouponInfoQuery;
import com.tp.query.mmp.CouponOrderQuery;
import com.tp.service.IBaseService;

/**
 * 发放优惠券 Service
 * 
 */
public interface ICouponUserService extends IBaseService<CouponUser> {

	/**
	 * 给一批用户发放优惠券
	 * 
	 * @param coupId
	 *            优惠券批次id
	 * @param currentUserId
	 *            当前发放的用户id
	 * @param userIds
	 *            发放的对象用户id list
	 * @return
	 */
	public List<String> sendCouponToUser(List<Long> coupIds,
			long currentUserId, String currentUserName,
			List<String> loginNameList, Integer msgSend, String msgContent) throws Exception;

	/**
	 * 验证一个批次的优惠券是否已经被发放过，如果发放过，则不能再编辑
	 * 
	 * @param couponId
	 * @return
	 */
	public boolean checkCouponEdit(long couponId);

	/**
	 * 获得我的优惠券或者红包
	 * 
	 * @param userId
	 * @param couponType
	 *            0 : 满减券 1：现金券
	 * @param status
	 *            状态 0 可用，1 已使用 2 已过期
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	 PageInfo<MyCouponDTO> myCoupon(long userId, Integer couponType,Integer status, int startPage, int pageSize) throws Exception;

	 MyCouponPageDTO myCouponWithCount(long userId, int couponType,int status, int startPage, int pageSize) throws Exception;

	/**
	 * 获得红包和优惠券数量
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	 MyCouponBasicDTO myCouponBasicInfo(long userId) throws Exception;

	/**
	 * 将优惠券更改状态
	 */
	 boolean updateCouponUserStatus(List<Long> couponUserIds, int status)throws Exception;

	/***
	 * 订单之前，获得本订单可用的优惠券和红包
	 * 
	 * @param querys
	 * @return
	 */
	 List<OrderCouponDTO> queryOrderCouponList(CouponOrderQuery query)throws Exception;

	/**
	 * 订单系统获得单个优惠券信息
	 */
	 OrderCouponDTO queryOrderCoupon(long couponUserId) throws Exception;

	/**
	 * 删除优惠券
	 * 
	 * @param userId
	 * @param couponUserId
	 * @return
	 * @throws Exception
	 */
	 Boolean deleteCouponUser(Long userId, Long couponUserId)throws Exception;

	/***
	 * 查询优惠券信息(后端系统专用)
	 * 
	 * @param couponId
	 * @param couponName
	 * @param number
	 * @param status
	 * @return
	 */
	 PageInfo<CouponUserInfoDTO> queryCouponForBackend(CouponInfoQuery query);

	/**
	 * 将优惠券作废
	 * 
	 * @param couponUserId
	 * @return
	 * @throws Exception
	 */
	 Boolean invalidCouponUser(Long couponUserId) throws Exception;

	/**
	 * 发送达人活动专用优惠券
	 *
	 * @param userId
	 *            当前用户Id
	 * @param fansActId
	 *            达人活动Id
	 * @param fansActName
	 *            达人活动名称
	 * @param refCode
	 *            参考号码(兑换码)
	 * @return 优惠券对象
	 * @throws Exception
	 */
	 OrderCouponDTO issueSeaGoorFansCouponUser(Long userId,
			Long fansActId, String fansActName, String refCode, String mobile, String nickName)
			throws Exception;
	
	/**
	 * 领取优惠券
	 * @param couponId
	 * @param memberId
	 * @param mobile
	 * @param nickName
	 * @return
	 */
	 ResultInfo receiveCouponUser(Long couponId, Long memberId, String mobile, String nickName);

	/**
	 *  新用户自动领券
	 * @param login
	 * @return
     */
	 ResultInfo  newUserCoupon(String login);

	 ResultInfo receiveCouponUserForExchange(Long couponId, Long memberId, String mobile, String nickName,Long promoterId);

	/**
	 * 领取优惠券
	 * @param receiveCoupon
	 * @return
     */
	ResultInfo receiveCoupon(CouponReceiveDTO receiveCoupon);
	
	/**
	 * 根据用户优惠券ID查询优惠券信息
	 * @param idList
	 * @return
	 */
	List<OrderCouponDTO> queryCouponUserByIds(List<Long> idList);

	/**
	 * 用户分享自动领券
	 * @param login
	 * @param couponSendType
	 * @return
	 */
	ResultInfo<String> sendAutoCouponBySendType(String loginName, CouponSendType couponSendType);

	/**
	 * 推广员向指定手机号发送优惠券
	 * @param id 优惠券ID
	 * @param mobile 要发送的手机号
	 * @param number 发送的优惠券数量
	 * @return
	 */
	ResultInfo sendPromoteeCoupon(Long id, String mobile, int number);
	
	ResultInfo sendCouponToMobile(Long couponId, String mobile, int number);
}
