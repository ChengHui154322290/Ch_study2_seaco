package com.tp.m.ao.promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.CouponConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.MyCouponDTO;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.enums.CouponSendType;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.convert.CouponConvert;
import com.tp.m.enums.CouponEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.promotion.QueryCoupon;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.coupon.CouponVO;
import com.tp.m.vo.coupon.OfflineCouponCodeVO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponRange;
import com.tp.model.mmp.CouponUser;
import com.tp.model.mmp.ExchangeCouponChannelCode;
import com.tp.model.mmp.TopicCoupon;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.mmp.CouponProxy;
import com.tp.proxy.mmp.CouponRangeProxy;
import com.tp.proxy.mmp.CouponUserProxy;
import com.tp.proxy.mmp.ExchangeCouponChannelCodeProxy;
import com.tp.proxy.mmp.PointDetailProxy;
import com.tp.proxy.mmp.TopicCouponProxy;
import com.tp.proxy.mmp.facade.CouponFacadeProxy;
import com.tp.proxy.ord.OrderCouponProxy;
import com.tp.query.mmp.MyCouponQuery;

/**
 * 优惠券业务层
 * @author zhuss
 * @2016年1月4日 下午6:55:20
 */
@Service
public class CouponAO {

	private static final Logger log=LoggerFactory.getLogger(CouponAO.class);
	
	@Autowired
	private CouponFacadeProxy couponFacadeProxy;

	
	@Autowired
	private CouponUserProxy couponUserProxy;
	
	@Autowired
	private OrderCouponProxy orderCouponProxy;
	@Autowired
	private CouponRangeProxy  couponRangeProxy;
	@Autowired
	private TopicCouponProxy  topicCouponProxy;
	@Autowired
	private MemberInfoProxy  memberInfoProxy;
	@Autowired
	private CouponProxy couponProxy;
	@Autowired
	private ExchangeCouponChannelCodeProxy exchangeCouponChannelCodeProxy;
	@Autowired
	private PointDetailProxy pointDetailProxy;
	
	public MResultVO<?> exchange(QueryCoupon coupon){
		Map<String,Object> params = new HashMap<String,Object>();
		Number couponId=null;//兑换码批次
		if(StringUtils.isBlank(coupon.getCcode())){//现金券兑换西客币
			params.put("id", coupon.getCid());
			CouponUser couponUser=couponUserProxy.queryById(Long.valueOf(coupon.getCid())).getData();
			if (CouponConstant.COUPON_VALIDD!=couponUser.getStatus()){
				return new MResultVO<>("该现金券已无效");
			}else{
				couponId=couponUser.getBatchId();
				coupon.setCouponId(String.valueOf(couponId));
			}
			
		}else{
			params.put("exchangeCode", coupon.getCcode());
			ExchangeCouponChannelCode exchangeCouponChannelCode = exchangeCouponChannelCodeProxy.queryUniqueByParams(params).getData();
			if(null==exchangeCouponChannelCode){
				return new MResultVO<>("优惠券编码已使用");
			}else{
				couponId=exchangeCouponChannelCode.getCouponId();
				coupon.setCid(String.valueOf(couponId));
			}
		}
		
		
		
		Coupon couponDto = couponProxy.queryById(couponId).getData();
		if(CouponType.NO_CONDITION.ordinal()!=couponDto.getCouponType()){//不是现金券不能兑换成功积分
			coupon.setPoint(null);
		}
		if("point".equals(coupon.getPoint())){
			return exchangePoint(coupon);
		}else{
			return exchangeCoupon(coupon);
		}
	}
	
	/**
	 * 兑换优惠券
	 * @param coupon
	 * @return
	 */
	public MResultVO<?> exchangeCoupon(QueryCoupon coupon){
		Map<String,String> param = new HashMap<String,String>();
		param.put("type", "EXCHANGE_SUCCESS");
		try {
			ResultInfo<?> result = couponFacadeProxy.exchangeCouponsCode(CouponConvert.convertExchangeQuery(coupon));
			if(result.isSuccess())return new MResultVO<>(MResultInfo.EXCHANGE_SUCCESS,param);
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 内部领取优惠券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 内部领取优惠券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.EXCHANGE_FAILED);
		}
	}
	
	/**
	 * 兑换优惠券
	 * @param coupon
	 * @return
	 */
	public MResultVO<?> exchangePoint(QueryCoupon coupon){
		Map<String,String> param = new HashMap<String,String>();
		param.put("type", "EXCHANGE_POINT_SUCCESS");
		try {
			ResultInfo<?> result = pointDetailProxy.exchangePoint(CouponConvert.convertExchangeQuery(coupon));
			if(result.isSuccess())return new MResultVO<>(MResultInfo.EXCHANGE_POINT_SUCCESS,param);
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 内部领取优惠券兑换积分  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 内部领取优惠券兑换积分 Exception]={}", e);
			return new MResultVO<>(MResultInfo.EXCHANGE_FAILED);
		}
	}
	
	/**
	 * 领取优惠券：可以根据用户token领取，也可以是手机号(用户不需要登录,不过需要手机验证码)
	 * @param coupon
	 * @return
	 */
	public MResultVO<CouponUser> receive(QueryCoupon coupon){
		try {
			ResultInfo result = couponFacadeProxy.receiveCoupon(CouponConvert.convertReceiveQuery(coupon));
			if(result.isSuccess()){
				Coupon couponInfo=new Coupon();
				couponInfo.setCode(coupon.getCcode());
				ResultInfo<List<Coupon>> couponInfos=couponProxy.queryByObject(couponInfo);
				Map<String,Object> myParams=new HashMap<String,Object>();
				if(couponInfos.getData().size()>0){
					myParams.put("batchId", couponInfos.getData().get(0).getId());
				}
				myParams.put("status", CouponUserStatus.NORMAL);
				myParams.put("toUserMobile", coupon.getTel());
				ResultInfo<List<CouponUser>>  myThisCoupon = couponUserProxy.queryCouponListbyCouponUserIdList(myParams);
				CouponUser  couponUser=new  CouponUser();
				if(myThisCoupon!=null){
					couponUser.setId(myThisCoupon.getData().get(0).getId());
				}
				return new MResultVO<>(MResultInfo.RECEIVE_SUCCESS,couponUser);
			}
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 领取优惠券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 领取优惠券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 根据手机号获取多张优惠券
	 * @param coupon
	 * @return
	 */
	public MResultVO<MResultInfo> receiveCouponsByTel(QueryCoupon coupon){
		try {
			ResultInfo result = new ResultInfo();
			for(String code : coupon.getCcodes()){
				coupon.setCcode(code);
				result = couponFacadeProxy.receiveCoupon(CouponConvert.convertReceiveQuery(coupon));
				if(!result.isSuccess()){
					if(result.getMsg().getCode().intValue() != -3)break;
				}
			}
			if(result.isSuccess()){
				return new MResultVO<>(MResultInfo.RECEIVE_SUCCESS);
			}
			Integer code= result.getMsg().getCode();
			return new MResultVO<>(code == null?"-1":code.toString(),result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 领取优惠券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 领取优惠券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 优惠券列表
	 * @param coupon
	 * @return
	 */
	public MResultVO<Page<CouponVO>> couponList(QueryCoupon coupon){
		try {
			//移动端个人中心返回优惠券或者红包不要翻页，最简单的办法是设置一个大的page size
			if(coupon.getType().equals(CouponEnum.ListFromType.LIST_FROM_USER.code)){//个人中心
				return queryCouponListByUser(coupon);
			}else if(coupon.getType().equals(CouponEnum.ListFromType.LIST_FROM_CAN_RECEIVE.code)){//可领取优惠券列表
					return new MResultVO<>("可领取优惠券 功能暂不提供 敬请期待!");
			}else if(coupon.getType().equals(CouponEnum.ListFromType.LIST_FROM_ORDER.code)){//订单
				return queryCouponListByOrder(coupon);
			}
			return new MResultVO<>(MResultInfo.FAILED);
		} catch (MobileException e) {
			log.error("[API接口 - 优惠券列表 MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 优惠券列表 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	/**
	 * 优惠券中心领取列表
	 * @param coupon
	 * @return
	 */
	public MResultVO<Page<CouponVO>> receiveCenterlist(Coupon coupon) {
		AssertUtil.notNull(coupon.getMobile(), "用户Id为空");
		MemberInfo memberInfo=new MemberInfo();
		memberInfo.setMobile(coupon.getMobile());
		memberInfo=memberInfoProxy.getService().queryUniqueByObject(memberInfo);
		List<CouponVO> couponVoList = new ArrayList<CouponVO>();
		List<CouponVO> allReceivedcouponVoList = new ArrayList<CouponVO>();
		try {
			if (coupon.getPageSize() == null) {
				coupon.setPageSize(PageConstant.DEFAULT_PAGESIZE_PROMOTER);
			}
			if (coupon.getStartPage() == null) {
				coupon.setStartPage(Integer.valueOf(PageConstant.DEFAULT_CURPAGE));
			}
			PageInfo<Coupon> pageCouponInfo = couponProxy.queryAllLikedofBrandByPage(coupon,
					Integer.valueOf(coupon.getStartPage()), PageConstant.DEFAULT_PAGESIZE_PROMOTER);
			for (int i = 0, len = pageCouponInfo.getRows().size(); i < len; i++) {
				CouponRange CouponRange = new CouponRange();
				CouponVO couponVO = new CouponVO();
				CouponRange.setCouponId(pageCouponInfo.getRows().get(i).getId());
				List<CouponRange> crList = couponRangeProxy.getService().queryByObject(CouponRange);
//				if (crList.size() > 0) {
//					couponVO.setBrandId(crList.get(0).getBrandId());// 品牌Id
//					couponVO.setBrandName(crList.get(0).getBrandName());// 品牌名称
//					couponVO.setCategoryId(crList.get(0).getCategoryId());// 大类名称
//					couponVO.setCategoryMiddleId(crList.get(0).getCategoryMiddleId());// 中类名称
//					couponVO.setCategorySmallId(crList.get(0).getCategorySmallId());// 小类名称
//					couponVO.setSkuCode(crList.get(0).getCode());// sku编码
//					couponVO.setSkuName(crList.get(0).getSkuName());// sku名称
//				}
				couponVO.setCouponcode(pageCouponInfo.getRows().get(i).getCode());//优惠券编码
				couponVO.setCid(String.valueOf(pageCouponInfo.getRows().get(i).getId()));// 优惠券ID
				couponVO.setName(pageCouponInfo.getRows().get(i).getCouponName());// 优惠券名称
				couponVO.setPrice(String.valueOf(pageCouponInfo.getRows().get(i).getFaceValue()));//优惠券金额
				couponVO.setNeedOverMon(pageCouponInfo.getRows().get(i).getNeedOverMon());//优惠券需满金额
				couponVO.setCouponImagePath(Constant.IMAGE_URL_TYPE.item.url + pageCouponInfo.getRows().get(i).getCouponImagePath());//优惠券图片
//				couponVO.setUseScope(pageCouponInfo.getRows().get(i).getUseScope());// 使用范围
				pageCouponInfo.getRows().get(i).setCouponRangeList(crList);
				TopicCoupon topicCoupon = new TopicCoupon();
				topicCoupon.setCouponId(pageCouponInfo.getRows().get(i).getId());
				List<TopicCoupon> topicCouponList = topicCouponProxy.getService().queryByObject(topicCoupon);// 查询
				if (topicCouponList != null && topicCouponList.size() > 0) {
					pageCouponInfo.getRows().get(i).setTopicId(String.valueOf(topicCouponList.get(0).getTopicId()));
					couponVO.setTopicId(String.valueOf(topicCouponList.get(0).getTopicId()));
				}
				
				Map<String,Object> myParams=new HashMap<String,Object>();
				myParams.put("toUserMobile", coupon.getMobile());
				myParams.put("batchId", pageCouponInfo.getRows().get(i).getId());
				ResultInfo<List<CouponUser>>  myThisCoupon = couponUserProxy.queryCouponListbyCouponUserIdList(myParams);
				if (myThisCoupon.getData() != null && myThisCoupon.getData().size() > 0 ) {
					if(CouponUserStatus.NORMAL.getCode()==myThisCoupon.getData().get(0).getStatus()){
						pageCouponInfo.getRows().get(i).setOperate("2");// 待使用
						couponVO.setOperate("2");
					}else{
						pageCouponInfo.getRows().get(i).setOperate("4");// 已使用
						couponVO.setOperate("4");
					}
					couponVO.setCouponUserId(String.valueOf(myThisCoupon.getData().get(0).getId()));
				} else {
					pageCouponInfo.getRows().get(i).setOperate("1");// 待领取
					couponVO.setOperate("1");
				}
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("batchId", pageCouponInfo.getRows().get(i).getId());
				ResultInfo<List<CouponUser>>  couponUsers = couponUserProxy.queryCouponListbyCouponUserIdList(params);
				if(!"2".equals(couponVO.getOperate())){
					if( !"4".equals(couponVO.getOperate())){//不展示待使用以外的状态
						if(couponUsers!=null && couponUsers.getData()!=null){
							Integer totalCoupon=pageCouponInfo.getRows().get(i).getCouponCount();//优惠券发放数量
							if(totalCoupon!=-1&&totalCoupon<=couponUsers.getData().size()){//如果已发送数量超过总数
								couponVO.setOperate("3");//已领完
								allReceivedcouponVoList.add(couponVO);//已领完的排在最后
							}else{
								couponVoList.add(couponVO);
							}
						}else{
							couponVoList.add(couponVO);
						}
					}
					
				}else{
					couponVoList.add(couponVO);
				}
				
			}
			couponVoList.addAll(allReceivedcouponVoList);
			Page<CouponVO> couponvoPageInfo = new Page<CouponVO>();
			couponvoPageInfo.setCurpage(pageCouponInfo.getPage());
			couponvoPageInfo.setPagesize(pageCouponInfo.getSize());
			couponvoPageInfo.setTotalcount(pageCouponInfo.getRecords());
			couponvoPageInfo.setTotalpagecount(pageCouponInfo.getTotal());
			couponvoPageInfo.setList(couponVoList);
			MResultVO<Page<CouponVO>> results = new MResultVO<Page<CouponVO>>(MResultInfo.SUCCESS, couponvoPageInfo);

			return results;
		} catch (MobileException e) {
			log.error("[API接口 - 优惠券列表 MobileException]={}", e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("[API接口 - 优惠券列表 Exception]={}", e);
			return null;
		}
	}
	/**
	 * 用户中心的优惠券列表
	 * @return
	 */
	public MResultVO<Page<CouponVO>> queryCouponListByUser(QueryCoupon coupon){
		ResultInfo<PageInfo<MyCouponDTO>> myCL = couponFacadeProxy.myCoupon(CouponConvert.convertCouponQuery(coupon));
		if(myCL.isSuccess()){
			PageInfo<MyCouponDTO> clist= myCL.getData();
			return new MResultVO<>(MResultInfo.SUCCESS,CouponConvert.convertCouponList(clist));
		}
		log.error("[调用Service接口 - 用户中心优惠券列表(myCoupon) FAILED] = {}",myCL.getMsg().toString());
		return new MResultVO<>(myCL.getMsg().getMessage());
	}
	
	/**
	 * 提交订单页面的可用优惠券列表
	 * @return
	 */
	public MResultVO<Page<CouponVO>> queryCouponListByOrder(QueryCoupon coupon){
		//Map<String, List<CouponDTO>>key=优惠券类型 0 : 满减券 1：现金券 ，value=用户的优惠券列表 CartConstant.TYPE_SEA
		ResultInfo<Map<String, List<OrderCouponDTO>>> res = orderCouponProxy.queryOrderCoupon(coupon.getUserid(), coupon.getUuid(), RequestHelper.getPlatformByName(coupon.getApptype()).code,0L);
		if(res.isSuccess()){
			Map<String, List<OrderCouponDTO>> ocm = res.getData();
			return new MResultVO<>(MResultInfo.SUCCESS,CouponConvert.convertCouponList(ocm));
		}
		log.error("[调用Service接口 - 提交订单页面的可用优惠券列表(myCoupon) FAILED] = {}",res.getMsg().toString());
		return new MResultVO<>(res.getMsg().getMessage());
	}
	
	/**
	 * 提交订单页面的可用优惠券总数
	 * @return
	 */
	public MResultVO<Integer> queryOrderCouponCount(QueryCoupon coupon){
		//Map<String, List<CouponDTO>>key=优惠券类型 0 : 满减券 1：现金券 ，value=用户的优惠券列表 CartConstant.TYPE_SEA
		ResultInfo<Integer> res = orderCouponProxy.queryOrderCouponCount(coupon.getUserid(), coupon.getUuid(), RequestHelper.getPlatformByName(coupon.getApptype()).code,0L);
		if(res.isSuccess()){
			Integer ocm = res.getData();
			return new MResultVO<>(MResultInfo.SUCCESS,ocm);
		}
		log.error("[调用Service接口 - 提交订单页面的可用优惠券总数(myCoupon) FAILED] = {}",res.getMsg().toString());
		return new MResultVO<>(res.getMsg().getMessage());
	}
	
	
	/**
	 * 首次操作获取优惠券:1 新用户自动发放2 分享自动发放 
	 * @param coupon
	 * @return
	 */
	public MResultVO<MResultInfo> receiveOnly(QueryCoupon coupon){
		try {
			CouponSendType couponSendType = CouponSendType.parse(StringUtil.getIntegerByStr(coupon.getSendtype()));
			ResultInfo<String> result = couponUserProxy.sendAutoCouponBySendType(coupon.getTel(),couponSendType);
			if(result.isSuccess()){
				return new MResultVO<>(MResultInfo.RECEIVE_SUCCESS);
			}
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 首次操作获取优惠券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 首次操作获取优惠券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 获取线下推广优惠码
	 * @param uid
	 * @return
	 */
	public MResultVO<OfflineCouponCodeVO> getOfflineCouponCode(Long uid) {
		try{
			String code = couponProxy.getOfflineCouponCode(String.format("%08d", uid));
			//String qrcodeImg = activityCacheHelper.getQrcodeCache();
			return new MResultVO<>(MResultInfo.SUCCESS,new OfflineCouponCodeVO(code,""));
		}catch(MobileException ex){
			log.error("[API接口 - 获取线下推广优惠码] = {}",ex);
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 获取线下推广优惠码] = {}",ex);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
