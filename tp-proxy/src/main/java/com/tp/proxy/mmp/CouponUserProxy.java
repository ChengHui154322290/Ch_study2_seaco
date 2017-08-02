package com.tp.proxy.mmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponUserInfoDTO;
import com.tp.dto.mmp.CouponUserOrderDto;
import com.tp.dto.mmp.enums.CouponSendType;
import com.tp.dto.ord.remote.SubOrder4CouponDTO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponUser;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.BaseProxy;
import com.tp.query.mmp.CouponInfoQuery;
import com.tp.service.IBaseService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ICouponService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.util.StringUtil;

import net.sf.json.JSONArray;

/**
 * 发放优惠券代理层
 * @author szy
 *
 */
@Service
public class CouponUserProxy extends BaseProxy<CouponUser>{

	@Autowired
	private ICouponUserService couponUserService;

	@Autowired
	private IMemberInfoService userService;
	
	@Autowired
	private ICouponService couponService;

	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;

	@Override
	public IBaseService<CouponUser> getService() {
		return couponUserService;
	}


	/**
	 * 根据优惠券Id获得试用订单信息
	 *
	 * @param couponId
	 * @return
	 */
	public List<CouponUserOrderDto> getSalesOrderInfo(Long couponId) {
		List<CouponUserOrderDto> dtos = new ArrayList<CouponUserOrderDto>();
		SubOrder4CouponDTO subDto = salesOrderRemoteService.findSubOrderCouponByCouponId(couponId);
		if (null == subDto || null == subDto.getSubMap()) {
			return dtos;
		}
		Iterator<SubOrder> subKey = subDto.getSubMap().keySet().iterator();
		while (subKey.hasNext()) {
			SubOrder orderDto =  subKey.next();
			if (null == orderDto) {
				break;
			}
			CouponUserOrderDto userOrderDto = new CouponUserOrderDto();
			userOrderDto.setCode(String.valueOf(orderDto.getOrderCode()));
			userOrderDto.setPayTotal(orderDto.getPayTotal());
			userOrderDto.setDiscount(subDto.getSubMap().get(orderDto));
			dtos.add(userOrderDto);
		}
		return dtos;
	}

	public boolean checkCouponEdit(long cid) {
		boolean res = couponUserService.checkCouponEdit(cid);
		return res;
	}

	/**
	 * 根据页面提供条件查询数据
	 *
	 * @param query
	 * @return
	 */
	public PageInfo<CouponUserInfoDTO> queryCouponForBackend(CouponInfoQuery query) {
		return couponUserService.queryCouponForBackend(query);
	}

	/**
	 * 作废优惠券
	 *
	 * @param couponUserId
	 * @return
	 */
	public ResultInfo invalidCouponUser(Long couponUserId) {
		try {
			couponUserService.invalidCouponUser(couponUserId);
		} catch (Exception e) {
			return  new ResultInfo(new FailInfo( e.getMessage()));
		}
		return new ResultInfo();
	}

	/**
	 * 发放优惠券
	 * @param couponIdStr
	 * @param userIdStr
	 * @param allMember
	 * @param currentUserId
	 * @param currentUserName
	 * @param msgSend
     * @param msgContent
     * @return
     */
	public ResultInfo issueCoupon(String couponIdStr, String userIdStr, Integer allMember, Long currentUserId, String currentUserName, Integer msgSend, String msgContent) {
		if (!"".equals(couponIdStr)) {
			if (allMember == 0) {// 非全体用户
				if (!"".equals(userIdStr)) {
					Long[] couponIdArr = (Long[]) JSONArray.toArray(JSONArray.fromObject(couponIdStr), Long.class);
					String[] userIdArr = userIdStr.replace("，", "," ).split(",");
					List<String> loginNameList = new ArrayList<String>();
					loginNameList = Arrays.asList(userIdArr);
					try {
						List<Long> couponIds = Arrays.asList(couponIdArr);
						List<String> res = couponUserService.sendCouponToUser(couponIds, currentUserId, currentUserName, loginNameList, msgSend, msgContent);
						if (res != null && res.size() > 0) {
							return new ResultInfo(new FailInfo( res.toString()));
						}
						return new ResultInfo();
					} catch (Exception e) {
						return new ResultInfo(new FailInfo( e.getMessage()));
					}
				} else {
					return  new ResultInfo(new FailInfo( "用户列表为空！"));
				}
			} else {// 全体用户
				try {
					MemberInfo memberInfo = new MemberInfo();
					memberInfo.setState(true);
					int pageSize = 100;
					int  pageId = 1;
					PageInfo<MemberInfo> page =userService.queryPageByObject(memberInfo, new PageInfo<MemberInfo>(pageId, pageSize));//TODO 分页分布
					//System.out.println("发送 优惠券1 pageId............................................" + pageId);
					int totalCount = page.getRecords();
					List<String> resList = new ArrayList<String>();
					List<MemberInfo> userList = page.getRows();
					if(userList != null  && userList.size() > 0){
						List<String> res = sendCoupon(couponIdStr, currentUserId, currentUserName, userList, msgSend, msgContent);
						if(res != null && res.size() > 0)
							resList.addAll(res);
					}
					if(totalCount > pageSize){
						int residue = totalCount % pageSize;
						int times = totalCount / pageSize;
						if(residue != 0)
							times = times + 1;

						for(int i = 2; i <= times ; i++){
							PageInfo<MemberInfo>pageRes =userService.queryPageByObject(memberInfo,new PageInfo<MemberInfo>( i, pageSize));
							//System.out.println("发送 优惠券 pageId............................................" + i);
							userList = pageRes.getRows();
							if(userList != null  && userList.size() > 0){
								List<String> res = sendCoupon(couponIdStr, currentUserId, currentUserName, userList, msgSend, msgContent);
								if(res != null && res.size() > 0)
									resList.addAll(res);
							}
						}
					}

					if(resList != null && resList.size() > 0){
						return new ResultInfo(new FailInfo( resList.toString()));
					}
					return new ResultInfo();
				} catch (Exception e) {
					return new ResultInfo( e.getMessage());
				}
			}

		} else {
			return new ResultInfo(new FailInfo( "传入的优惠券为空！"));
		}
	}

	private List<String> sendCoupon(String couponIdStr, Long currentUserId, String currentUserName, List<MemberInfo> userList, Integer msgSend, String msgContent) throws Exception {
		Long[] couponIdArr = (Long[]) JSONArray.toArray(JSONArray.fromObject(couponIdStr), Long.class);
		List<String> loginNameList = new ArrayList<String>();
		for(MemberInfo memberInfo : userList){
			if(memberInfo.getMobile() != null && !"".equals(memberInfo.getMobile()))
				loginNameList.add(memberInfo.getMobile());
		}
		List<String> res = couponUserService.sendCouponToUser(Arrays.asList(couponIdArr), currentUserId, currentUserName, loginNameList, msgSend, msgContent);
		return res;
	}
	
	public ResultInfo<String> sendAutoCouponBySendType(String loginName, CouponSendType couponSendType) {
		return couponUserService.sendAutoCouponBySendType(loginName, couponSendType);
	}


	/**
	 * @param id
	 * @param string
	 * @param i
	 * @return 
	 */
	public ResultInfo sendMobileCouponUnique(Long id, String mobile, int num) {
		Coupon coupon = couponService.queryById(id);
		if(coupon != null) {
			CouponUser couponUserQuery = new CouponUser();
			couponUserQuery.setBatchId(coupon.getId());
			couponUserQuery.setToUserMobile(mobile);
			CouponUser couponUser = couponUserService.queryUniqueByObject(couponUserQuery);
			if(couponUser != null) {
				return new ResultInfo(new FailInfo("已领过优惠券"));
			}
		}
		return couponUserService.sendCouponToMobile(id, mobile, num);
	}
	
	public ResultInfo<List<CouponUser>> queryCouponListbyCouponUserIdList(List<Long> couponUserIdList){
		try{
			List<CouponUser> couponUserList = new ArrayList<CouponUser>();
			if(CollectionUtils.isNotEmpty(couponUserIdList)){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(couponUserIdList, SPLIT_SIGN.COMMA)+")");
				couponUserList = couponUserService.queryByParam(params);
				if(CollectionUtils.isNotEmpty(couponUserList)){
					List<Long> couponIdList = new ArrayList<Long>();
					for(CouponUser couponUser:couponUserList){
						couponIdList.add(couponUser.getBatchId());
					}
					List<Coupon> couponList = couponService.queryCouponByCouponIdList(couponIdList);
					if(CollectionUtils.isNotEmpty(couponList)){
						for(CouponUser couponUser:couponUserList){
							for(Coupon coupon:couponList){
								if(coupon.getId().equals(couponUser.getBatchId())){
									couponUser.setCoupon(coupon);
								}
							}
						}
					}
				}
			}
			return new ResultInfo<>(couponUserList);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,couponUserIdList);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public ResultInfo<List<CouponUser>> queryCouponListbyCouponUserIdList(Map<String,Object> params){
		try{
			List<CouponUser> couponUserList = new ArrayList<CouponUser>();
			couponUserList = couponUserService.queryByParam(params);
			if(CollectionUtils.isNotEmpty(couponUserList)){
				List<Long> couponIdList = new ArrayList<Long>();
				for(CouponUser couponUser:couponUserList){
					couponIdList.add(couponUser.getBatchId());
				}
				List<Coupon> couponList = couponService.queryCouponByCouponIdList(couponIdList);
				if(CollectionUtils.isNotEmpty(couponList)){
					for(CouponUser couponUser:couponUserList){
						for(Coupon coupon:couponList){
							if(coupon.getId().equals(couponUser.getBatchId())){
								couponUser.setCoupon(coupon);
							}
						}
					}
				}
			}
			return new ResultInfo<>(couponUserList);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params);
			return new ResultInfo<>(failInfo);
		}
	}
	
}
