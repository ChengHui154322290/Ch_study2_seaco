package com.tp.proxy.mem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.util.mem.SmsException;
import com.tp.common.vo.Constant.TF;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mem.CertificateConstant;
import com.tp.common.vo.mem.MemberConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.BackendUserDto;
import com.tp.dto.mem.MemCallDto;
import com.tp.dto.mem.MemberInfoDto;
import com.tp.dto.mmp.MyCouponBasicDTO;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.mem.MemberDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.service.mem.IMemberDetailService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class MemberInfoProxy extends BaseProxy<MemberInfo>{

	
	public static final String FEMALE = "女";
	
	public static final String MALE = "男";
	
	@Autowired
	private IMemberInfoService memberInfoService;
	
	@Autowired
	private IMemberDetailService memberDetailService;
	
	@Autowired
	private IConsigneeAddressService consigneeAddressService;
	
	@Autowired
	private ICouponUserService couponUserService;
	
	@Override
	public IBaseService<MemberInfo> getService() {
		return memberInfoService;
	}
	public IMemberInfoService getMemberInfoService(){
		return memberInfoService;
	}

	public static Logger logger = LoggerFactory.getLogger(MemberInfoProxy.class);
	
	public List<MemberInfo> selectByIds(List<Long> ids) throws UserServiceException {
		try {
			if (null == ids || ids.isEmpty())
				return null;
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: selectByIds -> " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
		return memberInfoService.selectByIds(ids);
	}

	public MemberInfoDto login(MemCallDto loginDto) throws UserServiceException {
		return memberInfoService.login(loginDto);
	}
	public MemberInfoDto getLoginInfoByMemId(Long memberId) throws UserServiceException {
		return memberInfoService.getLoginInfoByMemId(memberId);
	}
	public MemberInfoDto registerLogin4Dss(Long uid) {
		return memberInfoService.registerLogin4Dss(uid);
	}

	public MemberInfoDto unionLogin(MemCallDto loginDto) {
		return memberInfoService.unionLogin(loginDto);
	}
	public MemberInfoDto bindMobile(MemCallDto bindDto) {
		return memberInfoService.bindMobile(bindDto);
	}
	public MemberInfoDto registerWeb(MemCallDto registerDto) throws UserServiceException {
		return memberInfoService.registerWeb(registerDto);
	}

	public MemberInfoDto register(MemCallDto registerDto) throws UserServiceException {
		return memberInfoService.register(registerDto);
	}

	public MemberInfoDto registerApp(MemCallDto registerDto) throws UserServiceException {
		return memberInfoService.registerApp(registerDto);
	}
 

	public boolean resetPassword(String loginName, String newPwd, Boolean isSendSms,String ip) throws UserServiceException, SmsException, UnsupportedEncodingException {
		return memberInfoService.resetPassword(loginName, newPwd, isSendSms, ip);
	}
	
	public boolean updatePassword(Long userId,String loginName, String newPwd,String originalPwd, Boolean isSendSms,String ip)
			throws UserServiceException, SmsException, UnsupportedEncodingException {
		return memberInfoService.updatePassword(userId, loginName, newPwd, originalPwd, isSendSms, ip);
	}

	public MemberInfoDto updatePasswordApp(MemCallDto updateDto) {
		return memberInfoService.updatePasswordApp(updateDto);
	}

	public Boolean checkMobileExist(String mobile) throws UserServiceException{
		return memberInfoService.checkMobileExist(mobile);
	}
	
	
	public MemberInfoDto changePassword(MemberInfo memberInfo,String loginName, String password,String ip) throws UserServiceException {
		return memberInfoService.changePassword(memberInfo, loginName, password, ip);
	}
	
	public Long getLastUid() throws UserServiceException {
		return memberInfoService.getLastUid();
	}
	
	public BackendUserDto getBackendInfoByLoginName(String loginName) 
			throws Exception {
		BackendUserDto backend = new BackendUserDto();
		MemberInfo user = memberInfoService.getMemberInfoByMobile(loginName);
		if(null == user)
			return backend;
		if(null != user.getSex()) {
			if(user.getSex() == MemberConstant.Sex.MALE)
				backend.setSex(MALE);
			else
				backend.setSex(FEMALE);
		}
		backend.setCreateTime(user.getCreateTime());
		if(user.getState()){
			backend.setStatus(1);
		}else{
			backend.setStatus(0);
		}
		
		if(!StringUtil.isNullOrEmpty(user.getEmail()) && !user.getEmail().contains("@mobile.com"))
			backend.setEmail(user.getEmail() + "（已验证）");
		if(!StringUtil.isNullOrEmpty(user.getMobile())) {
			backend.setMobile(user.getMobile() + "（已绑定）");
		}		
		// Get user's detail information.
		MemberDetail detail = memberDetailService.selectByUid(user.getId());
		if(null != detail) {
			if(null != detail.getTrueName())
				backend.setTrueName(detail.getTrueName());
			
			if(null != detail.getCertificateValue()) {
				if(null != detail.getCertificateType()) {
					if(detail.getCertificateType() == CertificateConstant.Type.ID_CARD)
						backend.setIdCardNo(detail.getCertificateValue());
				}
			}
			if(null != detail.getBirthday())
				backend.setBirthday(DateUtil.getBirthdayStr(detail.getBirthday()));
			if(null != detail.getLastLoginTime())
				backend.setLastLoginTime(detail.getLastLoginTime());	
			else
				backend.setLastLoginTime(null);
		}
		// Get consignee's address information.
		List<ConsigneeAddress> address = new ArrayList<ConsigneeAddress>();
		address = consigneeAddressService.findByUserId(user.getId(), null);
		backend.setAddress(consigneeAddressService.getAddressList(address));
		// Get user's coupon information.
		MyCouponBasicDTO myCoupon = couponUserService.myCouponBasicInfo(user.getId());
		if(null != myCoupon) {
			if(null != myCoupon.getRedPacketCount())
				backend.setVoucherNum(myCoupon.getRedPacketCount());
			if(null != myCoupon.getNormalCount())
				backend.setCouponNum(myCoupon.getNormalCount());
		}
		return backend;
	}
	
	public ResultInfo<Integer> updatePromoterIdByMemberId(Long memberId,Long PromoterId){
		try{
			Integer result = memberInfoService.updatePromoterIdByMemberId(memberId,PromoterId);
			if(TF.NO.equals(result)){
				return new ResultInfo<>(new FailInfo("会员已绑定其它推广员"));
			}
			return new ResultInfo<>(result);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,memberId,PromoterId);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public ResultInfo<List<MemberInfo>> queryMemberInfoByLike(String likeName){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " mobile like concat('"+likeName+"','%') or nick_name like concat('"+likeName+"','%') or id='"+likeName+"'");
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 20);
		try{
			return new ResultInfo<>(memberInfoService.queryByParam(params));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,likeName);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public MemberInfoDto modifyMobile(MemCallDto modifyMobileDto,Long uId) throws UserServiceException {
		return memberInfoService.modifyMobile(modifyMobileDto,uId);
	}
	public boolean checkMobile(MemCallDto modifyMobileDto) throws UserServiceException {
		return memberInfoService.checkMobile(modifyMobileDto);
	}
}
