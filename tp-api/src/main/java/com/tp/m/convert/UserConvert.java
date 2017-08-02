package com.tp.m.convert;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.vo.Constant;
import com.tp.common.vo.mem.MemberUnionType;
import com.tp.common.vo.mem.SessionKey;
import com.tp.dto.mem.CertificateDto;
import com.tp.dto.mem.MemCallDto;
import com.tp.enums.common.SourceEnum;
import com.tp.m.enums.CaptchaType;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.user.QueryUser;
import com.tp.m.query.user.QueryUserAuth;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.util.VerifyUtil;
import com.tp.m.vo.user.UserAuthVO;
import com.tp.model.mem.MemberDetail;

/**
 * 用户封装类
 * @author zhuss
 * @2016年1月9日 下午5:51:56
 */
public class UserConvert {
	
	private static final Logger log = LoggerFactory.getLogger(UserConvert.class);
	
	/**
	 * 封装用户入参
	 * @param userTO
	 * @return
	 */
	public static MemCallDto convertMemCallDto(QueryUser userTO){
		MemCallDto call = new MemCallDto();
		call.setUnionType(MemberUnionType.getByCode(StringUtil.isBlank(userTO.getUniontype())?0:Integer.valueOf(userTO.getUniontype())));
		call.setUnionVal(userTO.getUnionval());
		//登录传入loginname
		if(VerifyUtil.verifyTelephone(userTO.getLoginname()))call.setMobile(userTO.getLoginname());
		call.setNickName(userTO.getNickname());
		call.setUserName(userTO.getUnionval());
		call.setPassword(userTO.getPwd());
		call.setIp(userTO.getIp());
		call.setPlatform(RequestHelper.getPlatformByName(userTO.getApptype()));
		call.setSource(SourceEnum.XG);
		call.setSmsCode(StringUtil.getIntegerByStr(userTO.getCaptcha()));
		//设置渠道信息
		call.setChannelCode(userTO.getChannelcode()==null? null:userTO.getChannelcode().trim());
		call.setTpin(userTO.getTpin() == null ? null: userTO.getTpin().trim());
		call.setScanPromoterId(userTO.getScanPromoterId());
		
		call.setAvatarUrl(userTO.getHeadimg());
		call.setShopPromoterId(StringUtil.getLongByStr(userTO.getShopPromoterId()));
		call.setAdvertFrom(userTO.getAdvertFrom());//广告来源
		//注册 + 忘记密码  传入tel 
		if(StringUtil.isBlank(call.getMobile()))call.setMobile(userTO.getTel());
		call.setMemberId(userTO.getUserid());
		if(log.isInfoEnabled()){
			log.info("[调用会员接口    入参] = {}",JsonUtil.convertObjToStr(call));
		}
		return call;
	}

	/**
	 * 封装实名认证
	 * @param dto
	 * @return
	 */
	public static UserAuthVO convertUserAuth(MemberDetail ud){
		UserAuthVO authvo = new UserAuthVO();
		if(null != ud){
			authvo.setCode(ud.getCertificateValue());
			authvo.setName(ud.getTrueName());
			authvo.setImgfront(StringUtil.isBlank(ud.getPicA())? "" : Constant.IMAGE_URL_TYPE.cmsimg.url+ud.getPicA());
			authvo.setImgback(StringUtil.isBlank(ud.getPicB())? "" : Constant.IMAGE_URL_TYPE.cmsimg.url+ud.getPicB());
		}
		return authvo;
	}
	
	/**
	 * 封装实名认证
	 * @param dto
	 * @return
	 */
	public static UserAuthVO convertUserAuth(CertificateDto dto){
		UserAuthVO authvo = new UserAuthVO();
		if(null != dto){
			authvo.setCode(dto.getIdCard());
			authvo.setName(dto.getUserName());
			authvo.setImgfront(StringUtils.isBlank(dto.getPicA())? "" : Constant.IMAGE_URL_TYPE.cmsimg.url+dto.getPicA());
			authvo.setImgback(StringUtils.isBlank(dto.getPicB())? "" : Constant.IMAGE_URL_TYPE.cmsimg.url+dto.getPicB());
		}
		return authvo;
	}
	
	/**
	 * 封装实名认证入参
	 * @param userTO
	 * @return
	 */
	public static MemberDetail convertMemberDetail(QueryUserAuth userTO){
		MemberDetail ud = new MemberDetail();
		ud.setUid(userTO.getUserid());
		ud.setTrueName(VerifyUtil.escapeJSAndEmoji(userTO.getName()));
		ud.setCertificateType(0);
		ud.setCertificateValue(userTO.getCode().toUpperCase());
		return ud;
	}
	
	/**
	 * 手机验证码获取Session的KEY
	 * @param type
	 * @return
	 */
	public static SessionKey getSmsType(String type){
		if(StringUtil.equals(type,CaptchaType.REGIST.code))//注册
			return SessionKey.APP_REGISTER;
		if(StringUtil.equals(type,CaptchaType.UPDATE_PWD.code))
			return SessionKey.APP_UPDATE_PASSWORD;
		if(StringUtil.equals(type,CaptchaType.BIND_TEL.code))
			return SessionKey.APP_BINDPHONE;
		if(StringUtil.equals(type,CaptchaType.RECEIVE_COUPON.code))
			return SessionKey.RECEIVE_COUPON;
		if(StringUtil.equals(type,CaptchaType.REGIST_DSS.code))
			return SessionKey.REGISTER_DSS;
		if(StringUtil.equals(type,CaptchaType.BIND_UNION.code))
			return SessionKey.UNION_BINDMOBILE;
		if(StringUtil.equals(type,CaptchaType.MODIFY_MOBILE.code))
			return SessionKey.MODIFY_MOBILE;
		throw new MobileException(MResultInfo.TYPE_NOT_IN_SCOPE);
	}
}
