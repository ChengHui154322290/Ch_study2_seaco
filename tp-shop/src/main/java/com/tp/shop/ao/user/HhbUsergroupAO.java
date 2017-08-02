package com.tp.shop.ao.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.user.QueryUser;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.user.AccountVO;
import com.tp.model.mem.HhShopMemberInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.usr.HhbUsergroupProxy;
import com.tp.shop.ao.promotion.HhbgroupAo;
import com.tp.shop.helper.cache.TokenCacheHelper;

@Service
public class HhbUsergroupAO {
	private static final Logger log = LoggerFactory.getLogger(HhbUsergroupAO.class);
	
	@Autowired
    private HhbgroupAo hhbgroupAo;
	
	@Autowired
    private MemberInfoProxy memberInfoProxy;
	
	@Autowired
    private HhbUsergroupProxy hhbUsergroupProxy;
	
	@Autowired
    private TokenCacheHelper tokenCacheHelper;
	
	public MResultVO<AccountVO> loginOrRegist(String openId,String channelCode){
		try {
			//调接口，获取用户在HHB上的用户数据
			HhShopMemberInfo hhShopMemberInfo = hhbgroupAo.getHhShopMemberInfo(openId);
			//如果用户数据为空，则提示未登录
			if(null==hhShopMemberInfo||"".equals(hhShopMemberInfo)
					||null==hhShopMemberInfo.getPhone()||"".equals(hhShopMemberInfo.getPhone())){
				return new MResultVO<>(MResultInfo.LOGIN_FAILED);
			}else{
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("mobile", hhShopMemberInfo.getPhone());
				ResultInfo<List<MemberInfo>> memResult = memberInfoProxy.queryByParam(params);
				if(null==memResult.data || memResult.data.size()<=0){//没有用户，注册
					
					MemberInfo memberInfo = new MemberInfo();
					memberInfo.setMobile(hhShopMemberInfo.getPhone());
					if(StringUtils.isNotEmpty(hhShopMemberInfo.getName())){
						memberInfo.setNickName(hhShopMemberInfo.getName());
					}else{
						memberInfo.setNickName(hhShopMemberInfo.getPhone());
					}
					memberInfo.setTpin(openId);
					memberInfo.setCreateTime(new Date());
					memberInfo.setModifyTime(new Date());
					memberInfo.setChannelCode(channelCode);
					memberInfo.setPlatForm(2);//平台来源，2：wap
					memberInfo.setSource(2);//网站来源，1：来自西客  2：其他（HHB）
					Long memId = hhbUsergroupProxy.hhbRegister(memberInfo,hhShopMemberInfo.getAddress(),hhShopMemberInfo.getContact());
//					memberInfoProxy.insert(memberInfo);
					
					String loginToken = hhbUsergroupProxy.getAppLoginToken(memberInfo);
					AccountVO rs = new AccountVO(loginToken, memberInfo.getMobile(), memberInfo.getNickName());
					rs.setName(memberInfo.getNickName());
					rs.setAddress(hhShopMemberInfo.getAddress());
					rs.setContact(hhShopMemberInfo.getContact());
					rs.setChannelCode(channelCode);
					//存入缓存
		            tokenCacheHelper.setTokenCache(loginToken, new TokenCacheTO(memberInfo.getMobile(), memberInfo.getNickName(), memId));
		            rs.setIsneedbindtel(StringUtil.isBlank(memberInfo.getMobile()) ? StringUtil.ONE : StringUtil.ZERO);
//		            rs.setPromoterinfo(memberInfo.getPromoterInfo());
//		            rs.setPromoterinfomobile(PromoterConvert.convertPromoterMoblieDTO2VO(memberInfo.getPromoterInfoMobile()));
					return new MResultVO<>(MResultInfo.LOGIN_SUCCESS, rs);
				}else if(null==memResult.getData().get(0).getTpin()||"".equals(memResult.getData().get(0).getTpin())){//有用户，没tpin，更新
						MemberInfo memberInfo = memResult.getData().get(0);
						memberInfo.setTpin(openId);
						memberInfoProxy.updateById(memberInfo);
//					MemberInfo memberInfo = memResult.data.get(0);
					String loginToken = hhbUsergroupProxy.getAppLoginToken(memberInfo);
					AccountVO rs = new AccountVO(loginToken, memberInfo.getMobile(), memberInfo.getNickName());
					rs.setName(memberInfo.getNickName());
					rs.setAddress(hhShopMemberInfo.getAddress());
					rs.setContact(hhShopMemberInfo.getContact());
					rs.setChannelCode(channelCode);
					//存入缓存
		            tokenCacheHelper.setTokenCache(loginToken, new TokenCacheTO(memberInfo.getMobile(), memberInfo.getNickName(), memberInfo.getId()));
		            rs.setIsneedbindtel(StringUtil.isBlank(memberInfo.getMobile()) ? StringUtil.ONE : StringUtil.ZERO);
//		            rs.setPromoterinfo(memberinfo.getPromoterInfo());
//		            rs.setPromoterinfomobile(PromoterConvert.convertPromoterMoblieDTO2VO(memberinfo.getPromoterInfoMobile()));
		            return new MResultVO<>(MResultInfo.LOGIN_SUCCESS, rs);
				}else{//有用户，登录
					MemberInfo memberInfo = memResult.data.get(0);
					String loginToken = hhbUsergroupProxy.getAppLoginToken(memberInfo);
					AccountVO rs = new AccountVO(loginToken, memberInfo.getMobile(), memberInfo.getNickName());
					rs.setName(memberInfo.getNickName());
					rs.setAddress(hhShopMemberInfo.getAddress());
					rs.setContact(hhShopMemberInfo.getContact());
					rs.setChannelCode(channelCode);
					//存入缓存
		            tokenCacheHelper.setTokenCache(loginToken, new TokenCacheTO(memberInfo.getMobile(), memberInfo.getNickName(), memberInfo.getId()));
		            rs.setIsneedbindtel(StringUtil.isBlank(memberInfo.getMobile()) ? StringUtil.ONE : StringUtil.ZERO);
//		            rs.setPromoterinfo(memberinfo.getPromoterInfo());
//		            rs.setPromoterinfomobile(PromoterConvert.convertPromoterMoblieDTO2VO(memberinfo.getPromoterInfoMobile()));
		            return new MResultVO<>(MResultInfo.LOGIN_SUCCESS, rs);
				}
			}
        } catch (Exception ex) {
            log.error("[API接口 - 用户登录 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.LOGIN_FAILED);
        }
	}
}
