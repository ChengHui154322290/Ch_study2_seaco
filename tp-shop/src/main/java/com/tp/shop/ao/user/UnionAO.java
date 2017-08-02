package com.tp.shop.ao.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DssConstant;
import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.common.vo.mem.MemberUnionType;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.MemberInfoDto;
import com.tp.exception.UserServiceException;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.user.QueryUser;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.StringUtil;
import com.tp.m.util.VerifyUtil;
import com.tp.m.vo.user.AccountVO;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mkt.ChannelPromote;
import com.tp.proxy.dss.ChannelInfoProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.mkt.ChannelPromoteProxy;
import com.tp.shop.convert.PromoterConvert;
import com.tp.shop.convert.UserConvert;
import com.tp.shop.helper.cache.TokenCacheHelper;

/**
 * 用户 - 联合账户业务层
 * @author zhuss
 * @2016年1月3日 下午4:58:54
 */
@Service
public class UnionAO {

	private static final Logger log = LoggerFactory.getLogger(UnionAO.class);
	
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	
	@Autowired
	private TokenCacheHelper tokenCacheHelper;
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	@Autowired
	private ChannelPromoteProxy channelPromoteProxy;

	@Autowired
	private ChannelInfoProxy channelInfoProxy;
	/**
	 * 联合登录
	 * @param userTO
	 * @return
	 */
	public MResultVO<AccountVO> unionLogin(QueryUser userTO){
		try{
			setShopPromoterId(userTO);
//			setChannelPromoter(userTO);
			MemberInfoDto memberinfo = memberInfoProxy.unionLogin(UserConvert.convertMemCallDto(userTO));
			if(null == memberinfo || null == memberinfo.getUid())return new MResultVO<>(MResultInfo.USER_NO_EXIST);
			//存入缓存
			tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(),new TokenCacheTO(memberinfo.getMobile(),memberinfo.getNickName(),memberinfo.getUid()));
			AccountVO rs = new AccountVO(memberinfo.getAppLoginToken(),memberinfo.getMobile(),memberinfo.getNickName());
			rs.setSource(userTO.getUniontype());
			rs.setIsneedbindtel(StringUtil.isBlank(memberinfo.getMobile())?StringUtil.ONE:StringUtil.ZERO);
			rs.setPromoterinfo(memberinfo.getPromoterInfo());
			rs.setPromoterinfomobile( PromoterConvert.convertPromoterMoblieDTO2VO( memberinfo.getPromoterInfoMobile() ) );
			if(null != memberinfo.getMemberDetail())rs.setHeadimg(memberinfo.getMemberDetail().getAvatarUrl());
			return new MResultVO<>(MResultInfo.LOGIN_SUCCESS,rs);
		}catch(MobileException ex){
			log.error("[API接口 - 用户联合登录 MobileException] = {}",ex);
			return new MResultVO<>(ex);
		}catch(UserServiceException use){
			log.error("[API接口 - 用户联合登录 UserServiceException] = {}",use);
			return new MResultVO<>(use.getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 用户联合登录 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.LOGIN_FAILED);
		}
	}
	
	public void setChannelPromoter(QueryUser userTO){
		if(StringUtil.isNotBlank(userTO.getUnionval())&&StringUtil.equals(userTO.getUniontype(),MemberUnionType.WEIXIN.code)){
			ChannelPromote cp = new ChannelPromote();
			cp.setUniqueId(userTO.getUnionval());
			cp = channelPromoteProxy.queryUniqueByObject(cp).getData();
			if(null != cp){
				userTO.setChannelcode(cp.getChannel());
				userTO.setTpin(cp.getUniqueId());
				boolean isTel = VerifyUtil.verifyTelephone(cp.getChannel());
				if(isTel){
					PromoterInfo pi = new PromoterInfo();
					pi.setMobile(cp.getChannel());
					pi.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
					pi = promoterInfoProxy.queryUniqueByObject(pi).getData();
					if(null != pi)userTO.setScanPromoterId(pi.getPromoterId());
				}
			}
		}
	}
	
	private void setShopPromoterId(QueryUser userTO) {
		userTO.setScanPromoterId(null);
		userTO.setShopMobile(null);
		userTO.setShopPromoterId(null);
		if(StringUtils.isNotBlank(userTO.getChannelcode())){
			ResultInfo<ChannelInfo> resultInfo = channelInfoProxy.getChannelInfoByCode(userTO.getChannelcode());
			if (resultInfo.isSuccess() && resultInfo.getData() != null && resultInfo.getData().getPromoterInfo() != null) {
				userTO.setShopPromoterId(resultInfo.getData().getPromoterInfo().getPromoterId().toString());
			}
		}
	}
}
