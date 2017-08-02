package com.tp.m.ao.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tp.common.util.mem.SmsException;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.mem.MemberInfoConstant;
import com.tp.common.vo.mem.SessionKey;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.MemberInfoDto;
import com.tp.dto.mmp.MyCouponBasicDTO;
import com.tp.dto.ord.remote.OrderCountDTO;
import com.tp.exception.UserServiceException;
import com.tp.m.ao.dss.PromoterAO;
import com.tp.m.base.BaseVO;
import com.tp.m.base.MResultVO;
import com.tp.m.convert.PromoterConvert;
import com.tp.m.convert.UserConvert;
import com.tp.m.enums.CaptchaType;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.cache.TokenCacheHelper;
import com.tp.m.query.promoter.QueryPromoter;
import com.tp.m.query.user.QueryUser;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.promoter.DssLoginVO;
import com.tp.m.vo.promoter.PromoterInfoMobileVO;
import com.tp.m.vo.promoter.PromoterInfoVO;
import com.tp.m.vo.user.AccountVO;
import com.tp.m.vo.user.CaptchaVO;
import com.tp.m.vo.user.UserSupVO;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.mem.SendSmsProxy;
import com.tp.proxy.mmp.facade.CouponFacadeProxy;
import com.tp.proxy.ord.OrderInfoProxy;
import com.tp.query.mmp.MyCouponQuery;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IMemberInfoService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 帐户业务层
 *
 * @author zhuss
 * @2016年1月3日 下午4:58:54
 */
@Service
public class UserAO {

    private static final Logger log = LoggerFactory.getLogger(UserAO.class);

    @Autowired
    private TokenCacheHelper tokenCacheHelper;


    @Autowired
    private MemberInfoProxy memberInfoProxy;

    @Autowired
    private SendSmsProxy sendSmsProxy;

    @Autowired
    private OrderInfoProxy orderInfoProxy;

    @Autowired
    private CouponFacadeProxy couponFacadeProxy;
    @Autowired
    private PromoterInfoProxy promoterInfoProxy;

    @Autowired
    private PromoterAO promoterAO;

    @Autowired
    IMemberInfoService memberInfoService;

    @Autowired
    IPromoterInfoService promoterInfoService;

    @Autowired
    private UnionAO unionAO;

    /**
     * 用户 - 登录
     *
     * @param userTO
     * @return
     */
    public MResultVO<AccountVO> logon(QueryUser userTO) {
        try {
            //用户登录
            MemberInfoDto memberinfo = memberInfoProxy.login(UserConvert.convertMemCallDto(userTO));
            if (null == memberinfo || null == memberinfo.getUid()) return new MResultVO<>(MResultInfo.USER_NO_EXIST);
            //存入缓存
            tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(), new TokenCacheTO(memberinfo.getMobile(), memberinfo.getNickName(), memberinfo.getUid()));
            AccountVO rs = new AccountVO(memberinfo.getAppLoginToken(), memberinfo.getMobile(), memberinfo.getNickName());
            rs.setIsneedbindtel(StringUtil.isBlank(memberinfo.getMobile()) ? StringUtil.ONE : StringUtil.ZERO);
            rs.setPromoterinfo(memberinfo.getPromoterInfo());
            rs.setPromoterinfomobile(PromoterConvert.convertPromoterMoblieDTO2VO(memberinfo.getPromoterInfoMobile()));
            AuthHelper.MEMBER_ACCESS_MAP.remove(memberinfo.getUid());
            return new MResultVO<>(MResultInfo.LOGIN_SUCCESS, rs);
        } catch (MobileException ex) {
            log.error("[API接口 - 用户登录 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (UserServiceException use) {
            log.error("[API接口 - 用户登录 UserServiceException] = {}", use);
            return new MResultVO<>(use.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 用户登录 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.LOGIN_FAILED);
        }
    }


    public PromoterInfoMobileVO dsslogon(Long memberId, String token) {
        // 增加分销员登录逻辑

        PromoterInfoMobileVO vo = new PromoterInfoMobileVO();
        MResultVO<DssLoginVO> dssresult = promoterAO.promoterLogin(memberId, token);
        if (MResultInfo.SUCCESS.code.equals(dssresult.getCode())) {
            DssLoginVO dssloginvo = dssresult.getData();
            if (dssloginvo != null) {
                // 卡券
                if (dssloginvo.getIscoupondss() != null && dssloginvo.getIscoupondss().equals(1)) {
                    vo.setIscoupondss("1");
                }
                // 店铺
                if (dssloginvo.getIsshopdss() != null && dssloginvo.getIsshopdss().equals(1)) {
                    vo.setIsshopdss("1");
                }
                // 扫码
                if (dssloginvo.getIsscandss() != null && dssloginvo.getIsscandss().equals(1)) {
                    vo.setIsscandss("1");
                    if(dssloginvo.getIsTopScandss()=="1"){
     				   vo.setIsTopScandss("1");//是顶级
     			   }else{
     				   vo.setIsTopScandss("0");//不是顶级扫码
     			   }
     				
                }
            }
        }

        MemberInfo meminfo = memberInfoService.queryById(memberId);
        if (meminfo != null) {
            Long shopId = meminfo.getShopPromoterId();
            PromoterInfo prominfo = promoterInfoService.queryById(shopId);
            if (prominfo != null) {
                vo.setShopmobile(prominfo.getMobile());
                vo.setShopnickname(prominfo.getNickName());
            }
        }
        return vo;
    }


    /**
     * 用户 - 获取手机验证码
     *
     * @param userTO
     * @return
     */
    public MResultVO<CaptchaVO> getCaptcha(QueryUser userTO) {
        try {
            SessionKey sessionKey = UserConvert.getSmsType(userTO.getType());
            //验证手机号是否已经存在
            MemberInfo member = getMemberInfoByMobile(userTO.getTel());
            if (userTO.getType().equals(CaptchaType.REGIST.code)) {//注册
                if (member != null && StringUtils.isNotEmpty(member.getPassword()))
                    return new MResultVO<>(MResultInfo.TEL_EXIST);
            } else if (userTO.getType().equals(CaptchaType.UPDATE_PWD.code)) {//修改密码
                if (member == null) return new MResultVO<>(MResultInfo.TEL_NO_EXIST);
            } else if (userTO.getType().equals(CaptchaType.BIND_TEL.code)) {//绑定手机
                if (member != null) return new MResultVO<>(MResultInfo.TEL_EXIST);
//            } else if (userTO.getType().equals(CaptchaType.MODIFY_MOBILE.code)) {//更换手机
//                if (member != null) return new MResultVO<>(MResultInfo.TEL_EXIST);
            }

            //发送短信
            Integer code = sendSmsProxy.sendSms4App(userTO.getTel(), sessionKey.key, userTO.getIp());
            if (null != code) {
                return new MResultVO<>(MResultInfo.CAPTCHA_SUCCESS, new CaptchaVO(StringUtil.getStrByObj(MemberInfoConstant.SMS_CODE_RETRY_TIME)));
            }
            return new MResultVO<>(MResultInfo.CAPTCHA_FAILED);
        } catch (MobileException ex) {
            log.error("[API接口 - 用户获取手机验证码 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (SmsException se) {
            log.error("[API接口 - 用户获取手机验证码 MobileException] = {}", se);
            return new MResultVO(String.valueOf(0-se.getErrorCode()), se.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 用户获取手机验证码 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.CAPTCHA_FAILED);
        }
    }
    public MResultVO<CaptchaVO> getCaptchaNew(QueryUser userTO) {
        try {
            SessionKey sessionKey = UserConvert.getSmsType(userTO.getType());
            //验证手机号是否已经存在
            MemberInfo member = getMemberInfoByMobile(userTO.getTel());
            if (userTO.getType().equals(CaptchaType.REGIST.code)) {//注册
                if (member != null && StringUtils.isNotEmpty(member.getPassword()))
                    return new MResultVO<>(MResultInfo.TEL_EXIST);
            } else if (userTO.getType().equals(CaptchaType.UPDATE_PWD.code)) {//修改密码
                if (member == null) return new MResultVO<>(MResultInfo.TEL_NO_EXIST);
            } else if (userTO.getType().equals(CaptchaType.BIND_TEL.code)) {//绑定手机
                if (member != null) return new MResultVO<>(MResultInfo.TEL_EXIST);
//            } else if (userTO.getType().equals(CaptchaType.MODIFY_MOBILE.code)) {//更换手机
//                if (member != null) return new MResultVO<>(MResultInfo.TEL_EXIST);
            }

            //发送短信
            Integer code = sendSmsProxy.sendSms4AppNew(userTO.getTel(), sessionKey.key, userTO.getIp());
            if (null != code) {
                return new MResultVO<>(MResultInfo.CAPTCHA_SUCCESS, new CaptchaVO(StringUtil.getStrByObj(MemberInfoConstant.SMS_CODE_RETRY_TIME)));
            }
            return new MResultVO<>(MResultInfo.CAPTCHA_FAILED);
        } catch (MobileException ex) {
            log.error("[API接口 - 用户获取手机验证码 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (SmsException se) {
            log.error("[API接口 - 用户获取手机验证码 MobileException] = {}", se);
            return new MResultVO(String.valueOf(0-se.getErrorCode()), se.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 用户获取手机验证码 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.CAPTCHA_FAILED);
        }
    }
    
    /**
     * 用户 - 注册
     *
     * @param userTO
     * @return
     */
    public MResultVO<AccountVO> regist(QueryUser userTO) {
        try {
            //用户注册
            setShopPromoterId(userTO);
            //渠道信息
            unionAO.setChannelPromoter(userTO);
            MemberInfoDto memberinfo = memberInfoProxy.registerApp(UserConvert.convertMemCallDto(userTO));
            if (null == memberinfo || null == memberinfo.getUid()) return new MResultVO<>(MResultInfo.REGISTER_FAILED);
            //存入缓存
            tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(), new TokenCacheTO(memberinfo.getMobile(), memberinfo.getNickName(), memberinfo.getUid()));
            AccountVO rs = new AccountVO(memberinfo.getAppLoginToken(), memberinfo.getMobile(), memberinfo.getUsername());
            rs.setIsneedbindtel(StringUtil.isBlank(memberinfo.getMobile()) ? StringUtil.ONE : StringUtil.ZERO);
            rs.setPromoterinfo(memberinfo.getPromoterInfo());
            rs.setPromoterinfomobile(PromoterConvert.convertPromoterMoblieDTO2VO(memberinfo.getPromoterInfoMobile()));
            return new MResultVO<>(MResultInfo.REGISTER_SUCCESS, rs);
        } catch (MobileException ex) {
            log.error("[API接口 - 用户注册 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (UserServiceException use) {
            log.error("[API接口 - 用户注册 UserServiceException] = {}", use);
            return new MResultVO<>(use.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 用户注册 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.REGISTER_FAILED);
        }
    }
    

    private void setShopPromoterId(QueryUser userTO) {
        if (StringUtils.isNotBlank(userTO.getShopMobile())) {
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("mobile", userTO.getShopMobile());
            query.put("promoterType", PROMOTER_TYPE.DISTRIBUTE.code);
            ResultInfo<PromoterInfo> promoterInfo = promoterInfoProxy.queryUniqueByParams(query);
            if (promoterInfo.getData() != null) {
                userTO.setShopPromoterId("" + promoterInfo.getData().getPromoterId());
            }
        }
    }

    /**
     * 用户 - 忘记密码
     *
     * @param userTO
     * @return
     */
    public MResultVO<AccountVO> modifyPwd(QueryUser userTO) {
        try {
            MemberInfoDto memberinfo = memberInfoProxy.updatePasswordApp(UserConvert.convertMemCallDto(userTO));
            if (null == memberinfo || null == memberinfo.getUid()) return new MResultVO<>(MResultInfo.OPERATION_FAILED);
            tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(), new TokenCacheTO(memberinfo.getMobile(), memberinfo.getNickName(), memberinfo.getUid()));
            AccountVO rs = new AccountVO(memberinfo.getAppLoginToken(), memberinfo.getMobile(), memberinfo.getUsername());
            rs.setIsneedbindtel(StringUtil.isBlank(memberinfo.getMobile()) ? StringUtil.ONE : StringUtil.ZERO);
            rs.setPromoterinfo(memberinfo.getPromoterInfo());
            rs.setPromoterinfomobile(PromoterConvert.convertPromoterMoblieDTO2VO(memberinfo.getPromoterInfoMobile()));
            return new MResultVO<>(MResultInfo.MODIFY_SUCCESS, rs);
        } catch (MobileException ex) {
            log.error("[API接口 - 忘记密码 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (UserServiceException use) {
            log.error("[API接口 - 忘记密码 UserServiceException] = {}", use);
            return new MResultVO<>(use.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 忘记密码 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.MODIFY_FAILED);
        }
    }

    /**
     * 用户 - 解除绑定
     *
     * @param userTO
     * @return
     */
    public MResultVO<BaseVO> relieveUnion(QueryUser userTO) {
        try {
            /*boolean result = unionInfoProxy.unbindUnionLogin(userTO.getUnionval(),MemberUnionType.getByCode(StringUtil.isBlank(userTO.getUniontype())?0:Integer.valueOf(userTO.getUniontype())));
			log.info("[API接口 - 解除绑定帐号 result] = {}",result);
			if(result)return new MResultVO<>(MResultInfo.OPERATION_SUCCESS);*/
            return new MResultVO<>(MResultInfo.OPERATION_SUCCESS);
        } catch (MobileException ex) {
            log.error("[API接口 - 解除绑定帐号 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (Exception ex) {
            log.error("[API接口 - 解除绑定帐号 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.MODIFY_FAILED);
        }
    }

    /**
     * 用户 - 角标数量
     *
     * @param userTO
     * @return
     */
    public MResultVO<UserSupVO> supCount(Long userId) {
        try {
            UserSupVO us = new UserSupVO();
            List<Integer> orderTypeList = new ArrayList<Integer>();
            for(OrderType orderType:OrderConstant.OrderType.values()){
            	if(!OrderConstant.FAST_ORDER_TYPE.equals(orderType.code)){
            		orderTypeList.add(orderType.code);
            	}
            }
            //获取待付款和待收货的角标数量
            ResultInfo<OrderCountDTO> res = orderInfoProxy.findOrderCountDTOByMemberId(userId,orderTypeList);
            if (res.isSuccess()) {
                OrderCountDTO oc = res.getData();
                if (null != oc) {
                    us.setUnpaycount(StringUtil.getStrByObj(oc.getPayment()));//获取待付款数量
                    us.setUnreceiptcount(StringUtil.getStrByObj(oc.getReception()));//获取待收货数量
                    us.setUnusecount(StringUtil.getStrByObj(oc.getUnusecount()));//待使用
                }
            }
            //获取优惠券数量
            MyCouponQuery query = new MyCouponQuery();
            query.setMemberId(userId);
            ResultInfo<MyCouponBasicDTO> couponCount = couponFacadeProxy.myCouponBasicInfo(query);
            if (couponCount.isSuccess()) {
                MyCouponBasicDTO coupon = couponCount.getData();
                if (null != coupon) us.setCouponcount(StringUtil.getStrByObj(coupon.getTotalCount()));
            }
            return new MResultVO<>(MResultInfo.SUCCESS, us);
        } catch (MobileException ex) {
            log.error("[API接口 - 角标数量 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (Exception ex) {
            log.error("[API接口 - 角标数量 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.CONN_ERROR);
        }
    }

    /**
     * 用户 - 初始化用户
     *
     * @param userId
     * @return
     */
    public MResultVO<AccountVO> initUser(Long userId) {
        try {
            MemberInfoDto memberinfo = memberInfoProxy.getLoginInfoByMemId(userId);
            if (null == memberinfo || null == memberinfo.getUid()) return new MResultVO<>(MResultInfo.USER_NO_EXIST);
            //存入缓存
            tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(), new TokenCacheTO(memberinfo.getMobile(), memberinfo.getNickName(), memberinfo.getUid()));
            AccountVO rs = new AccountVO(memberinfo.getAppLoginToken(), memberinfo.getMobile(), memberinfo.getNickName());
            rs.setIsneedbindtel(StringUtil.isBlank(memberinfo.getMobile()) ? StringUtil.ONE : StringUtil.ZERO);
            rs.setPromoterinfo(memberinfo.getPromoterInfo());
            rs.setPromoterinfomobile(PromoterConvert.convertPromoterMoblieDTO2VO(memberinfo.getPromoterInfoMobile()));

            return new MResultVO<>(MResultInfo.OPERATION_SUCCESS, rs);
        } catch (MobileException ex) {
            log.error("[API接口 - 初始化用户 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (UserServiceException use) {
            log.error("[API接口 - 初始化用户 UserServiceException] = {}", use);
            return new MResultVO<>(use.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 初始化用户Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.OPERATION_FAILED);
        }
    }
    
    /**
   	 * 
   	 * saveScanPromoter:(保存扫码员信息). <br/>  
   	 * TODO(保存扫码员信息).<br/>   
   	 *  
   	 * @author zhouguofeng  
   	 * @param request
   	 * @return  
   	 * @sinceJDK 1.8
   	 */
   	public String saveScanPromoter( QueryUser userTO){
   		QueryPromoter promoter = new QueryPromoter();
   		String mobile=userTO.getLoginname();
   		if(StringUtils.isBlank(mobile)){
   			mobile=userTO.getTel();
   		}
   		promoter.setUserMobile(userTO.getTel());//设置手机号码
   		if(StringUtils.isBlank(promoter.getInviteCode())){
   			String inviteCode=promoterInfoService.initInviteCode();
   			PromoterInfo  promoterInfo=new PromoterInfo();
   			if(userTO.getUserid()!=null){
   				promoterInfo.setMemberId(userTO.getUserid());
   			}
   			promoterInfo.setPromoterName(mobile);//默认昵称
   			promoterInfo.setInviteCode(inviteCode);
   			promoterInfo.setPromoterLevel(1);
   			promoterInfo.setCommisionRate((float) 50);
   			promoterInfo.setCreateUser(mobile);
   			promoterInfo.setUpdateUser(mobile);
   			if(userTO.getScanPromoterId()!=null){//通过扫码进入默认为下一级扫码推广
   				Long toPromoterId=promoterInfoProxy.getTopPromoterById(userTO.getScanPromoterId());
   				promoterInfo.setTopPromoterId(toPromoterId);//设置顶级分销员
   				promoterInfo.setParentPromoterId(userTO.getScanPromoterId());
   			}
   			ResultInfo<PromoterInfo> result = promoterInfoProxy.insertScan(promoterInfo);
   			log.info("扫码注册结果--------------"+JsonUtil.convertObjToStr(result)+"result--"+result.getMsg()+"result"+result.success);
   			if(result.success==false){
   				 return JsonUtil.convertObjToStr(new MResultVO<MResultInfo>(MResultInfo.PROMOTER_NAME_SCAN_IS_EXIST));
   			}else{
   				promoter.setInviteCode(result.getData().getInviteCode());
   			}
   			System.out.println(result.getData().getPromoterId());
   		}
   		
   		//查询该手机号码是否已经是扫码推广员
   		if(promoter.getUserMobile()!=null){
   			MResultVO<PromoterInfoVO> promoterInfo =promoterAO.getPromoterInfoByTelAndType(promoter.getUserMobile(),PROMOTER_TYPE.SCANATTENTION.code);
   	   		if(promoterInfo.getData()!=null){//扫码员已存在
   	   			 return JsonUtil.convertObjToStr(new MResultVO<MResultInfo>(MResultInfo.PROMOTER_SCAN_IS_EXIST));
   	   		}
   		}
   		MResultVO<PromoterInfoVO> inviteCodeInfo= promoterAO.getPromoterInfoByInviteCode(promoter.getInviteCode());
   		promoter.setPromoterid(inviteCodeInfo.getData().getPromoterid());//设置
   		if(log.isInfoEnabled()){
   			log.info("[API接口 -推广员账户详情 入参] = {}",JsonUtil.convertObjToStr(promoter));
   		}
   		promoter.setCredentialType(DssConstant.CARD_TYPE.IDENTITY_CARD.code);
//   		StringBuffer smsCodeKey = new StringBuffer(promoter.getUserMobile()).append(":")
//   				.append(SessionKey.REGISTER_DSS.value);
   		
   		MResultVO<MResultInfo> result=promoterAO.updatePromoter(promoter);//更新带背景的二维码图片
   		if(log.isInfoEnabled()){
   			log.info("[API接口 -推广员认证信息更新 返回值] = {}",JsonUtil.convertObjToStr(result));
   		}
   		
   		return JsonUtil.convertObjToStr(result);
   	}

    /**
     * 检查手机号是否存在
     *
     * @param tel
     * @return
     */
    public boolean checkTelExist(String tel) {
        try {
            Boolean isExist = memberInfoProxy.checkMobileExist(tel);
            if (null == isExist) throw new MobileException(MResultInfo.SYSTEM_ERROR);
            return isExist;
        } catch (UserServiceException ex) {
            log.error("[API接口 - 检查手机号是否存在 UserServiceException] = {}", ex.getMessage());
            throw new MobileException(ex.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 检查手机号是否存在 Exception] = {}", ex);
            throw new MobileException(MResultInfo.SYSTEM_ERROR);
        }
    }

    private MemberInfo getMemberInfoByMobile(String mobile) {
        MemberInfo query = new MemberInfo();
        query.setMobile(mobile);
        return memberInfoProxy.queryUniqueByObject(query).getData();
    }
    
    /**
     * 用户 - 修改手机号
     *
     * @param userTO
     * @return
     */
    public MResultVO<AccountVO> modifyMobile(QueryUser userTO,Long uId) {
        try {
            //用户注册
            setShopPromoterId(userTO);
            //渠道信息
            unionAO.setChannelPromoter(userTO);
//            MemberInfoDto memberinfo = memberInfoProxy.registerApp(UserConvert.convertMemCallDto(userTO));
            MemberInfoDto memberinfo = memberInfoProxy.modifyMobile(UserConvert.convertMemCallDto(userTO),uId);
            if (null == memberinfo || null == memberinfo.getUid()) return new MResultVO<>(MResultInfo.MODIFY_FAILED);
            //存入缓存
            tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(), new TokenCacheTO(memberinfo.getMobile(), memberinfo.getNickName(), memberinfo.getUid()));
            AccountVO rs = new AccountVO(memberinfo.getAppLoginToken(), memberinfo.getMobile(), memberinfo.getUsername());
            rs.setIsneedbindtel(StringUtil.isBlank(memberinfo.getMobile()) ? StringUtil.ONE : StringUtil.ZERO);
            rs.setPromoterinfo(memberinfo.getPromoterInfo());
            rs.setPromoterinfomobile(PromoterConvert.convertPromoterMoblieDTO2VO(memberinfo.getPromoterInfoMobile()));
            return new MResultVO<>(MResultInfo.MODIFY_SUCCESS, rs);
        } catch (MobileException ex) {
            log.error("[API接口 - 用户修改手机号 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (UserServiceException use) {
            log.error("[API接口 - 用户修改手机号 UserServiceException] = {}", use);
            return new MResultVO<>(use.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 用户修改手机号 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.REGISTER_FAILED);
        }
    }
    /**
     * 用户 - 验证手机号
     *
     * @param userTO
     * @return
     */
    public MResultVO<Boolean> checkMobile(QueryUser userTO) {
        try {
            Boolean flag = memberInfoProxy.checkMobile(UserConvert.convertMemCallDto(userTO));
            return new MResultVO<>(MResultInfo.CONFIRE_SUCCESS, flag);
        } catch (MobileException ex) {
            log.error("[API接口 - 用户修改手机号 MobileException] = {}", ex);
            return new MResultVO<>(ex);
        } catch (UserServiceException use) {
            log.error("[API接口 - 用户修改手机号 UserServiceException] = {}", use);
            return new MResultVO<>(use.getMessage());
        } 
    }
    
    
}
