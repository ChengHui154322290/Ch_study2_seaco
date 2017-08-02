package com.tp.shop.ao.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.CertificateDto;
import com.tp.dto.mem.MemberInfoDto;
import com.tp.exception.UserServiceException;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.user.QueryUser;
import com.tp.m.query.user.QueryUserAuth;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.vo.user.AccountVO;
import com.tp.m.vo.user.UserAuthVO;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberDetail;
import com.tp.proxy.dss.ChannelInfoProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.mem.MemberDetailProxy;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.shop.convert.PromoterConvert;
import com.tp.shop.convert.UserConvert;
import com.tp.shop.helper.cache.TokenCacheHelper;

/**
 * 用户 - 账户安全业务层
 * @author zhuss
 * @2016年1月3日 下午4:58:54
 */
@Service
public class SafetyAO {

	private static final Logger log = LoggerFactory.getLogger(SafetyAO.class);
	
	@Autowired
	private MemberDetailProxy memberDetailProxy;
	
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	
	@Autowired
	private TokenCacheHelper tokenCacheHelper;
	
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	
	@Autowired
	private ChannelInfoProxy channelInfoProxy;
	
	/**
	 * 绑定手机号
	 * @return
	 */
	public MResultVO<MResultInfo> bindTel(QueryUser userTO){
		try{
			MemberInfoDto memberinfo = memberInfoProxy.bindMobile(UserConvert.convertMemCallDto(userTO));
			if(null == memberinfo || null == memberinfo.getUid())return new MResultVO<>(MResultInfo.OPERATION_FAILED);
			tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(),new TokenCacheTO(memberinfo.getMobile(),memberinfo.getNickName(),memberinfo.getUid()));
			return new MResultVO<>(MResultInfo.OPERATION_SUCCESS);
		}catch (MobileException e) {
			log.error("[API接口 - 绑定手机号  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch(UserServiceException use){
			log.error("[API接口 - 绑定手机号 UserServiceException] = {}",use);
			return new MResultVO<>(use.getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 绑定手机号 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 绑定联合帐号
	 * @return
	 */
	public MResultVO<AccountVO> bindUnion(QueryUser userTO){
		try{
			setShopPromoterId(userTO);
			MemberInfoDto memberinfo = memberInfoProxy.bindMobile(UserConvert.convertMemCallDto(userTO));
			if(null == memberinfo || null == memberinfo.getUid())return new MResultVO<>(MResultInfo.OPERATION_FAILED);
			tokenCacheHelper.setTokenCache(memberinfo.getAppLoginToken(),new TokenCacheTO(memberinfo.getMobile(),memberinfo.getNickName(),memberinfo.getUid()));
			AccountVO rs = new AccountVO(memberinfo.getAppLoginToken(),memberinfo.getMobile(),memberinfo.getNickName());
			rs.setPromoterinfo(memberinfo.getPromoterInfo());
			rs.setPromoterinfomobile( PromoterConvert.convertPromoterMoblieDTO2VO( memberinfo.getPromoterInfoMobile() ) );
			return new MResultVO<>(MResultInfo.OPERATION_SUCCESS,rs);
		}catch (MobileException e) {
			log.error("[API接口 - 绑定联合帐号  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch(UserServiceException use){
			log.error("[API接口 - 绑定联合帐号 UserServiceException] = {}",use);
			return new MResultVO<>(use.getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 绑定联合帐号 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
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
	/**
	 * 用户 -检查是否实名认证
	 * @param userTO
	 * @return
	 */
	public MResultVO<UserAuthVO> checkauth(Long userId){
		try{
			CertificateDto dto = memberDetailProxy.isCertificateCheck(userId);
			if(null != dto)return new MResultVO<>(MResultInfo.SUCCESS,UserConvert.convertUserAuth(dto));
			return new MResultVO<>(MResultInfo.USER_NO_AUTH);
		}catch (MobileException e) {
			log.error("[API接口 - 检查是否实名认证 MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch(Exception ex){
			log.error("[API接口 - 检查是否实名认证 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 用户 -实名认证
	 * @param userTO
	 * @return
	 */
	public MResultVO<UserAuthVO> auth(QueryUserAuth userTO){
		try{
            String[] numPics =new String[2];
			//身份证照片默认为空
			numPics[0] = userTO.getImgfront() == null?StringUtils.EMPTY:userTO.getImgfront();
			numPics[1] = userTO.getImgback() == null?StringUtils.EMPTY:userTO.getImgback();
			MemberDetail ud = new MemberDetail();
			//检查是否已经认证
			CertificateDto dto = memberDetailProxy.isCertificateCheck(userTO.getUserid());
			/*if(dto!=null){
				BeanUtils.copyProperties(dto, ud);
			}*/
			ud.setTrueName(userTO.getName());
			ud.setCertificateValue(userTO.getCode());
			ud.setPicA(userTO.getImgfront());
			ud.setPicB(userTO.getImgback());
			ud.setCertificateType(1);
			ud.setUid(userTO.getUserid());
			if(null != dto){ //修改实名认证
				MemberDetail userDetail = memberDetailProxy.selectByUid(userTO.getUserid());
				ud.setId(userDetail.getId());
				ud = memberDetailProxy.doRefreshAuthencation(ud, numPics[0], numPics[1]);
			}else{
				ud = memberDetailProxy.doAuthencation(ud, numPics);
			}
			if(null == ud)return new MResultVO<>(MResultInfo.AUTH_FAILED);
			return new MResultVO<>(MResultInfo.AUTH_SUCCESS,UserConvert.convertUserAuth(ud));
		}catch (MobileException e) {
			log.error("[API接口 - 用户实名认证 MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch(Exception ex){
			log.error("[API接口 - 用户实名认证 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
