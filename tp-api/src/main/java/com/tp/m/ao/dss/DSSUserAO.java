package com.tp.m.ao.dss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DssConstant;
import com.tp.common.vo.mem.PassPortErrorCode;
import com.tp.common.vo.mem.SessionKey;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.MemberInfoDto;
import com.tp.exception.UserServiceException;
import com.tp.m.base.MResultVO;
import com.tp.m.convert.PromoterConvert;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.cache.TokenCacheHelper;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.user.AccountVO;
import com.tp.model.dss.PromoterInfo;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.redis.util.JedisCacheUtil;

/**
 * 分销员 业务层
 * @author lei
 * @2016年3月9日 下午3:48:38
 */
@Service
public class DSSUserAO {
	private static final Logger log=LoggerFactory.getLogger(DSSUserAO.class);
	
	@Autowired
	PromoterInfoProxy promoterInfoProxy;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	@Autowired
	private TokenCacheHelper tokenCacheHelper;
	
	public ResultInfo<PromoterInfo> getPromoterInfo(String mobile) {
		PromoterInfo query = new PromoterInfo();
		query.setMobile(mobile);
		query.setPromoterType(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
		return promoterInfoProxy.queryUniqueByObject(query);
	}
    
	
	public ResultInfo<PromoterInfo> getScanPromoterInfo(String mobile) {
		PromoterInfo query = new PromoterInfo();
		query.setMobile(mobile);
		query.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
		return promoterInfoProxy.queryUniqueByObject(query);
	}
	/**
	 * @param promoterInfo
	 * @return
	 */
	public MResultVO<AccountVO> insert(PromoterInfo promoterInfo) {
		try{
			StringBuffer smsCodeKey = new StringBuffer(promoterInfo.getMobile()).append(":").append(SessionKey.REGISTER_DSS.value);
			Object o = jedisCacheUtil.getCache(smsCodeKey.toString());
			if (null == o)
				throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
			Integer realSmsCode = Integer.parseInt(this.jedisCacheUtil.getCache(smsCodeKey.toString()).toString());
			// 校验验证吗
			if (Integer.valueOf(promoterInfo.getCaptcha()).intValue() != realSmsCode.intValue())
				throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
			promoterInfo.setPromoterType(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			ResultInfo<PromoterInfo> promotor = promoterInfoProxy.insert(promoterInfo);
			if(!promotor.success) {
				return new MResultVO<>(promotor.getMsg().getDetailMessage());
			}
			MemberInfoDto memberinfo = memberInfoProxy.registerLogin4Dss(promotor.getData().getMemberId());
			//存入缓存
			tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(),new TokenCacheTO(memberinfo.getMobile(),memberinfo.getNickName(),memberinfo.getUid()));
			AccountVO rs = new AccountVO(memberinfo.getAppLoginToken(),memberinfo.getMobile(),memberinfo.getNickName());
			rs.setIsneedbindtel(StringUtil.isBlank(memberinfo.getMobile())?StringUtil.ONE:StringUtil.ZERO);
			rs.setPromoterinfo(memberinfo.getPromoterInfo());
			rs.setPromoterinfomobile( PromoterConvert.convertPromoterMoblieDTO2VO( memberinfo.getPromoterInfoMobile() ) );
			return new MResultVO<>(MResultInfo.LOGIN_SUCCESS,rs);
		
		}catch(MobileException ex){
			log.error("[API接口 - 注册分销登录 MobileException] = {}",ex);
			return new MResultVO<>(ex);
		}catch(UserServiceException use){
			log.error("[API接口 - 注册分销登录 UserServiceException] = {}",use);
			return new MResultVO<>(use.getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 注册分销登录 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.LOGIN_FAILED);
		}
	}
	/**
	 * 更新账户信息
	 * updateAccountInfo:(这里用一句话描述这个方法的作用). <br/>  
	 * @author zhouguofeng  
	 * @param promoterInfo
	 * @return  
	 * @sinceJDK 1.8
	 */
//	public  boolean  updateAccountInfo(PromoterInfo promoterInfo){
//		promoterInfoProxy.updateById(promoterInfo);
//		return true;
//		
//	}
}
