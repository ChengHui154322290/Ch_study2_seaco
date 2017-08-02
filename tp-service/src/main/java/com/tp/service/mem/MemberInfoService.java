package com.tp.service.mem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.tp.common.dao.BaseDao;
import com.tp.common.util.mem.PasswordHelper;
import com.tp.common.util.mem.SmsException;
import com.tp.common.vo.MqMessageConstant;
import com.tp.common.vo.QrcodeConstant;
import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.common.vo.mem.CertificateConstant;
import com.tp.common.vo.mem.MemberInfoConstant;
import com.tp.common.vo.mem.MemberUnionType;
import com.tp.common.vo.mem.PassPortErrorCode;
import com.tp.common.vo.mem.SessionKey;
import com.tp.common.vo.mem.SmsUtil;
import com.tp.common.vo.mem.SuccessCode;
import com.tp.common.vo.mkt.ChannelPromoteConstant;
import com.tp.dao.mem.MemberDetailDao;
import com.tp.dao.mem.MemberInfoDao;
import com.tp.dto.cmbc.MemberCMBCDto;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.MemCallDto;
import com.tp.dto.mem.MemberInfoDto;
import com.tp.dto.mem.ResultCode;
import com.tp.dto.ord.HhbShopOrderInfoDTO;
import com.tp.dto.promoter.PromoterInfoMobileDTO;
import com.tp.exception.UserServiceException;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mem.ModifyMobileLog;
import com.tp.model.mem.UnionInfo;
import com.tp.model.mkt.ChannelPromote;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.IMemberPointService;
import com.tp.service.mem.IModifyMobileLogService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.mem.IUnionInfoService;
import com.tp.service.mkt.IChannelPromoteService;
import com.tp.service.mkt.IQrcodeService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.util.StringUtil;

@Service
public class MemberInfoService extends BaseService<MemberInfo> implements IMemberInfoService {

	@Autowired
	private MemberInfoDao memberInfoDao;
	@Autowired
	private MemberDetailDao memberDetailDao;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	ISendSmsService sendSmsService;
	@Autowired
	private ICouponUserService couponUserSerivce;
	@Autowired
	private IUnionInfoService unionInfoService;
	@Autowired
	private IPromoterInfoService promoterInfoService;
	@Autowired
	private IChannelPromoteService channelPromoteService;
	@Autowired
	private IPointDetailService pointDetailService;
	@Autowired
	private IModifyMobileLogService modifyMobileLogService;
	@Autowired
	private IQrcodeService qrcodeService;
	@Autowired
	RabbitMqProducer rabbitMqProducer;
	@Value("#{settings['member.registerpoint']}")
	private Integer registerpoint = 0;
	
	private static String SHOP_TOKEN_PREFIX = "dss_shop_";
	private static String COUPON_TOKEN_PREFIX = "dss_coupon_";
	private static String SCAN_TOKEN_PREFIX = "dss_scan_";
	private static Integer TOKEN_LIVE = 365*86400;
    @Autowired
	Map<String, IMemberPointService> allMemberPointService;
	@Override
	public BaseDao<MemberInfo> getDao() {
		return memberInfoDao;
	}

	public static Logger logger = LoggerFactory.getLogger(MemberInfoService.class);
	
	public MemberInfo insert(MemberInfo memberInfo) throws UserServiceException {
		try {
			MemberInfo user = getByMobile(memberInfo.getMobile());
			if (null != user) {
				logger.error(">>>>>[ERROR!]：>>>插入MemberInfo时，发现该用户已存在，手机号: "
						+ memberInfo.getMobile());
				return user;
			}
			logger.info(">>>>>[begin]: insert member info:" + memberInfo.toString());
			memberInfo.setState(Boolean.TRUE);
			memberInfoDao.insert(memberInfo);
			return memberInfo;
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
	}
	
	private MemberInfo save(MemberInfo memberInfo) throws UserServiceException {
		try {
			logger.info(">>>>>[begin]: save member info:" + memberInfo.toString());
			if(null!=memberInfo.getId()){//更新
				updateNotNullById(memberInfo);
				
			}else{
				MemberInfo user = getByMobile(memberInfo.getMobile());
				if (null != user) {
					logger.error(">>>>>[ERROR!]：>>>插入MemberInfo时，发现该用户已存在，手机号: "
							+ memberInfo.getMobile());
					throw new UserServiceException(PassPortErrorCode.USER_EXISTS.code,PassPortErrorCode.USER_EXISTS.value);
				}
				memberInfo.setState(Boolean.TRUE);
				memberInfoDao.insert(memberInfo);
			}
			return memberInfo;
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
	}
	

	public void loginUpdate(MemberInfo memberInfo) throws UserServiceException {
		try {
			memberInfo.setModifyTime(new Date());
			updateNotNullById(memberInfo);
		} catch (Exception e) {
			logger.error("loginUpdate>>>>>[ERROR!]: " + e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
	}

	@Override
	public List<MemberInfo> selectByIds(List<Long> ids) throws UserServiceException {
		try {
			if (null == ids || ids.isEmpty())
				return null;
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: selectByIds -> " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
		return memberInfoDao.selectByIds(ids);
	}

	public MemberInfoDto getDtoByLoginName(String loginName)
			throws UserServiceException {
		return toDto(getByMobile(loginName));
	}

	public MemberInfo getByMobile(String mobile) throws UserServiceException {
		MemberInfo user = null;
		try {
			logger.info("--------------------getByLoginName begin--------------------------");
			logger.info("loginName:"+mobile);
			MemberInfo query = new MemberInfo();
			if (mobile.contains(MemberInfoConstant.EMAIL_SPL)) {
				// email
				query.setEmail(mobile);
			} else {
				// mobile
				query.setMobile(mobile);
			}
			query.setState(Boolean.TRUE);
			long startTime = System.currentTimeMillis();
			List<MemberInfo> users = queryByObject(query);
			logger.info(">>>>>[duration]: " + "getByLoginName -> " + (System.currentTimeMillis() - startTime) + "毫秒");		
			if(null!=users&&!users.isEmpty()){
				logger.info("userInfo:"+users.get(0).toString());
				user = users.get(0);
			}
		} catch (Exception e) {
			logger.error("getByLoginName>>>>>>[ERROR]:"+e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		logger.info("--------------------getByLoginName end--------------------------");
		return user;
	}
	
	private int updateMemInfoById(Long uid, MemCallDto loginDto) {
		MemberInfo toBeUpdated = new MemberInfo();
		toBeUpdated.setId(uid);
		toBeUpdated.setNickName(loginDto.getNickName());
		toBeUpdated.setModifyTime(new Date());
		logger.info("更新用户{}的昵称为{}", uid, loginDto.getNickName());
		return updateNotNullById(toBeUpdated);
	}

	private MemberDetail saveOrupdateUserDetail(long uid, String ip, String avatarUrl) {
		logger.debug(">>>>>[enter]: saveOrupdateUserDetail");
		MemberDetail detail = new MemberDetail();
		try {
			detail.setUid(uid);
			List<MemberDetail> detailList = memberDetailDao.queryByObject(detail);

			// 如果存在该用户，则填充用户id，直接做更新
			if (null != detailList && !detailList.isEmpty())
				detail.setId(detailList.get(0).getId());

			detail.setModifyTime(new Date());
			detail.setLastLoginTime(new Date());
			detail.setLastLoginIp(ip);
			detail.setAvatarUrl(avatarUrl);
			long startTime = 0l;
			if (null != detail.getId()) {// 修改
				startTime = System.currentTimeMillis();
				memberDetailDao.updateNotNullById(detail);
				logger.debug(">>>>>[update]: member_detail -> " + "uid: " + uid);
				logger.info(">>>>>[duration]: " + "updateMemberDetailInfo -> " + (System.currentTimeMillis() - startTime) + "毫秒");		
			} else {
				// 否则，新增
				detail.setCreateTime(new Date());
				detail.setIsCertificateCheck(false);
				detail.setIsDelete(false);
				detail.setRegistryPlatform(0);
				detail.setVerifyStatus(CertificateConstant.VerifyStatus.UNCOMMITTED);
				startTime = System.currentTimeMillis();
				memberDetailDao.insert(detail);
				logger.debug(">>>>>[insert]: uc_user_detail -> " + "uid: " + uid);
				logger.info(">>>>>[duration]: " + "insertUserDetailInfo -> " + (System.currentTimeMillis() - startTime) + "毫秒");		
			}
		logger.debug(">>>>>[quit]: saveOrupdateUserDetail");
		} catch (UserServiceException e) {
			logger.error(">>>>>[ERROR!]: update userDetail:" + detail.toString() + ".exception:" + e.getMessage());
			throw new UserServiceException(e.getMessage());
		} catch (Exception e) {
			logger.error(">>>>>[ERROR!]: update userDetail:" + detail.toString() + ".exception:" + e.getMessage());
			throw new UserServiceException(e.getMessage());
		}

		return detail;
	}

	@Override
	public MemberInfoDto login(MemCallDto loginDto) throws UserServiceException {
		MemberInfoDto dto = null;
		try {
			MemberInfo memberInfo = null;
			boolean unionLogin = false;
			UnionInfo unionInfo = null;
			if(StringUtils.hasText(loginDto.getUnionVal())){
				UnionInfo query = new UnionInfo();
				query.setUnionVal(loginDto.getUnionVal());
				query.setType(MemberUnionType.WEIXIN.code);
				unionInfo = unionInfoService.queryUniqueByObject(query);
				if(unionInfo != null && !unionInfo.getIsDeleted()){
					memberInfo = queryById(unionInfo.getMemId());
					unionLogin = true;
					logger.info("使用联合登录unionVal:{}, type:{}", loginDto.getUnionVal(), loginDto.getUnionType().code);
				}
				else{
					if(loginDto.getMobile() == null)
						throw new UserServiceException(PassPortErrorCode.UNION_VAL_NOT_BIND.code, PassPortErrorCode.UNION_VAL_NOT_BIND.value);
				}
			}

			logger.debug(">>>>>[begin]: user login:" + loginDto.getMobile());
			long start = System.currentTimeMillis();
			
			if(memberInfo == null && loginDto.getMobile() != null){
				memberInfo = getByMobile(loginDto.getMobile());
			}
			
			logger.info(">>>>>[login]: getByLoginName -> " + (System.currentTimeMillis() - start) + " 毫秒");
			if (null == memberInfo) 
				return dto;
			
			if (!unionLogin && !memberInfo.getPassword().endsWith(getPassword(loginDto.getPassword(), memberInfo.getSalt()))) {
				logger.error("ser login:{}", PassPortErrorCode.USERNAME_OR_PASS_WRONG.value);
				throw new UserServiceException(PassPortErrorCode.USERNAME_OR_PASS_WRONG.code, PassPortErrorCode.USERNAME_OR_PASS_WRONG.value);
			}
			dto = toDto(memberInfo);
			start = System.currentTimeMillis();
			dto.setMemberDetail(saveOrupdateUserDetail(dto.getUid(), loginDto.getIp(), loginDto.getAvatarUrl()));
			logger.info(">>>>>[login]: saveOrupdateUserDetail -> " + (System.currentTimeMillis() - start) + " 毫秒");
			logger.debug(">>>>>[end]: user login:" + memberInfo.getMobile());
//			ResultInfo message = couponUserSerivce.newUserCoupon(memberInfo.getMobile());
//			if(message.success){
//				logger.info("用户登录["+memberInfo.getMobile()+"]发券成功!");
//			}
//			else {
//				logger.info("用户登录["+memberInfo.getMobile()+"]发券失败!");
//			}
			
			//登录发送积分
			//pointDetailService.addPointByMemberLogin(memberInfo.getId(), memberInfo.getUserName(), 10);
			// 联合登录绑定
			if(StringUtils.hasText(loginDto.getUnionVal()) && !unionLogin){
				if(unionInfo == null){
					UnionInfo newData = new UnionInfo();
					newData.setCreateTime(new Date());
					newData.setMemId(memberInfo.getId());
					newData.setType(loginDto.getUnionType().code);
					newData.setUnionVal(loginDto.getUnionVal());
					unionInfoService.insert(newData);
				}
				else {
					unionInfo.setIsDeleted(false);
					unionInfoService.updateNotNullById(unionInfo);
				}
				logger.info("联合登录绑定unionVal:{}, loginName:{}, type:{}", loginDto.getUnionVal(), memberInfo.getMobile(), MemberUnionType.WEIXIN.code);
			}
			
		} catch (UserServiceException e) {
			logger.error(">>>>>[ERROR!]: login name: " + loginDto.getMobile() + " -> " + e.getMessage());
			throw new UserServiceException(e.getMessage());
		} catch (Exception e) {
			logger.error(">>>>>[ERROR!]: login name: " + loginDto.getMobile() + " -> "
					+ e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		return dto;
	}
	
	@Override
	public MemberInfoDto registerLogin4Dss(Long uid) {
		MemberInfo member = queryById(uid);
		MemberInfoDto dto = toDto(member);
		dto.setMemberDetail(saveOrupdateUserDetail(dto.getUid(), null, null));
		return dto;
	}
	
	
	private String getPromoterInfoByUser(Long memId, String token, PromoterInfoMobileDTO dto) {
		PromoterInfo query = new PromoterInfo();
		query.setMemberId(memId);
		query.setPromoterType(PROMOTER_TYPE.DISTRIBUTE.code);
		PromoterInfo promoterInfo = promoterInfoService.queryUniqueByObject(query);
		Map<String, String> result = new HashMap<>();
		if(promoterInfo != null) {
			result.put("nickname", (promoterInfo.getNickName()==null ? promoterInfo.getPromoterName() : promoterInfo.getNickName()));
			result.put("weixin", promoterInfo.getWeixin());
			result.put("qq", promoterInfo.getQq());
			result.put("mobile", promoterInfo.getMobile());
			result.put("email", promoterInfo.getEmail());
			result.put("name", promoterInfo.getPromoterName());
			result.put("credentialType", promoterInfo.getCredentialTypeCn());
			result.put("credentialCode", promoterInfo.getCredentialCode());
			result.put("bankName", promoterInfo.getBankName());
			result.put("bankAccount", promoterInfo.getBankAccount());
			result.put("alipay", promoterInfo.getAlipay());
			result.put("pageShow", promoterInfo.getPageShow().toString());
			
			dto.setNickname( (promoterInfo.getNickName()==null ? promoterInfo.getPromoterName() : promoterInfo.getNickName()) );
			dto.setWeixin( promoterInfo.getWeixin() );
			dto.setQq( promoterInfo.getQq() );
			dto.setMobile( promoterInfo.getMobile() );
			dto.setEmail( promoterInfo.getEmail() );
			dto.setName( promoterInfo.getPromoterName() );
			dto.setCredentialtype(promoterInfo.getCredentialTypeCn());
			dto.setCredentialcode( promoterInfo.getCredentialCode() );
			dto.setBankname( promoterInfo.getBankName() );
			dto.setBankaccount( promoterInfo.getBankAccount() );
			dto.setAlipay( promoterInfo.getAlipay() );
			dto.setPageshow( promoterInfo.getPageShow().toString() );
		}
		/////////
		query.setPromoterType(null);
		List<PromoterInfo> promoterList = promoterInfoService.queryByObject(query);
		if(promoterList !=null && !promoterList.isEmpty()){
			for(PromoterInfo p : promoterList){
				if (0 == p.getPromoterType()) {				//卡券推广员
					result.put("iscoupondss", "1");
					dto.setIscoupondss("1");
					setCache(COUPON_TOKEN_PREFIX + token, p.getPromoterId());
				}else if(1 == p.getPromoterType()){			//店铺推广员
					result.put("isshopdss", "1");
					dto.setIsshopdss("1");
					setCache(SHOP_TOKEN_PREFIX + token, p.getPromoterId());
				}else if(2 == p.getPromoterType()){			//扫码推广员
					result.put("isscandss", "1");
					dto.setIsscandss("1");
					setCache(SCAN_TOKEN_PREFIX + token, p.getPromoterId());
				}	
			}
		}
		
		MemberInfo meminfo = memberInfoDao.queryById(memId);
		if(meminfo != null){
			Long shopId = meminfo.getShopPromoterId();
			PromoterInfo prominfo =  promoterInfoService.queryById(shopId);
			if(prominfo != null){
				result.put("shopmobile", prominfo.getMobile());
				result.put("shopnickname", prominfo.getNickName());
				dto.setShopmobile( prominfo.getMobile() );
				dto.setShopnickname( prominfo.getNickName());
			}
		}
				
		return new Gson().toJson(result);
	}
	
	private void setCache(String key , Object value){
		boolean result =  jedisCacheUtil.setCache(key, value, TOKEN_LIVE);
		if(!result){
			logger.error("[缓存工具-设置key{} value{} 失败]",key, value.toString());
		}
	}
	
	@Override
	/**
	 * 第三方联合登录
	 * @param loginDto
	 * @return
	 */
	public MemberInfoDto unionLogin(MemCallDto loginDto) {
		MemberInfoDto dto = null;
		try {
			MemberInfo memberInfo = null;
			boolean unionLogin = false;
			UnionInfo unionInfo = null;
			if(StringUtils.hasText(loginDto.getUnionVal()) && loginDto.getUnionType() != null){
				UnionInfo query = new UnionInfo();
				query.setUnionVal(loginDto.getUnionVal());
				query.setType(loginDto.getUnionType().code);
				query.setIsDeleted(false);
				unionInfo = unionInfoService.queryUniqueByObject(query);
				if(unionInfo != null){
					memberInfo = queryById(unionInfo.getMemId());
					unionLogin = true;
					logger.info("使用联合登录unionVal:{}, type:{}", loginDto.getUnionVal(), loginDto.getUnionType().code);
				}
				else{
					if(loginDto.getMobile() == null && loginDto.getUserName() == null){
						throw new UserServiceException(PassPortErrorCode.UNION_VAL_NOT_BIND.code, PassPortErrorCode.UNION_VAL_NOT_BIND.value);
					}
					memberInfo = registerForUnionMember(loginDto);
				}
			}
			else {
				throw new UserServiceException(PassPortErrorCode.UNION_NOT_NULL.code, PassPortErrorCode.UNION_NOT_NULL.value);
			}

			logger.debug(">>>>>[begin]: user login:" + loginDto.getMobile());
			long start = System.currentTimeMillis();
			
			if (null == memberInfo) 
				return dto;
			
			dto = toDto(memberInfo);
			start = System.currentTimeMillis();
			// 检查到昵称有变化 更新数据库
			if(!StringUtils.isEmpty(loginDto.getNickName()) && !loginDto.getNickName().equals(memberInfo.getNickName())){
				updateMemInfoById(dto.getUid(), loginDto);
			}
			if(memberInfo.getScanPromoterId() == null && loginDto.getScanPromoterId() != null) {
				updateScanInfoById(dto.getUid(),loginDto);
			}
			dto.setMemberDetail(saveOrupdateUserDetail(dto.getUid(), loginDto.getIp(), loginDto.getAvatarUrl()));
			logger.info(">>>>>[login]: saveOrupdateUserDetail -> " + (System.currentTimeMillis() - start) + " 毫秒");
			logger.debug(">>>>>[end]: user login:" + memberInfo.getMobile());
			
			// 联合登录绑定
			if(StringUtils.hasText(loginDto.getUnionVal()) && !unionLogin){
				UnionInfo query = new UnionInfo();
				query.setUnionVal(loginDto.getUnionVal());
				query.setType(loginDto.getUnionType().code);
				query.setIsDeleted(true);
				unionInfo = unionInfoService.queryUniqueByObject(query);
				if(unionInfo == null){
					UnionInfo newData = new UnionInfo();
					newData.setCreateTime(new Date());
					newData.setMemId(memberInfo.getId());
					newData.setType(loginDto.getUnionType().code);
					newData.setUnionVal(loginDto.getUnionVal());
					unionInfoService.insert(newData);
				}
				else {
					unionInfo.setIsDeleted(false);
					unionInfoService.updateNotNullById(unionInfo);
				}
				logger.info("联合登录绑定unionVal:{}, loginName:{}, type:{}", loginDto.getUnionVal(), memberInfo.getMobile(), MemberUnionType.WEIXIN.code);
			}
			
		} catch (UserServiceException e) {
			logger.error(">>>>>[ERROR!]: login name: " + loginDto.getMobile() + " -> " + e.getMessage());
			throw new UserServiceException(e.getMessage());
		} catch (Exception e) {
			logger.error(">>>>>[ERROR!]: login name: " + loginDto.getMobile() + " -> " + e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		return dto;
	}

	/**
	 * @param uid
	 * @param loginDto
	 */
	private int updateScanInfoById(Long uid, MemCallDto loginDto) {
		MemberInfo toBeUpdated = new MemberInfo();
		toBeUpdated.setId(uid);
		toBeUpdated.setNickName(loginDto.getNickName());
		toBeUpdated.setChannelCode(loginDto.getChannelCode());
		toBeUpdated.setTpin(loginDto.getTpin());
		toBeUpdated.setScanPromoterId(loginDto.getScanPromoterId());
		toBeUpdated.setModifyTime(new Date());
		logger.info("更新用户{}的昵称为{}", uid, loginDto.getNickName());
		return updateNotNullById(toBeUpdated);
	}

	public MemberInfo insertMemberBasicInfo(MemCallDto registerDto) {
		logger.debug(">>>>>[enter]: insertUserBasicInfo");
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setMobile(registerDto.getMobile());
		memberInfo.setChannelCode(registerDto.getChannelCode());
		memberInfo.setTpin(registerDto.getTpin());
		memberInfo.setScanPromoterId(registerDto.getScanPromoterId());
		
		memberInfo.setUserName(registerDto.getUserName());
		memberInfo.setSalt(PasswordHelper.getSalt());
		memberInfo.setShopPromoterId(registerDto.getShopPromoterId());
		memberInfo.setAdvertFrom(registerDto.getAdvertFrom());
		if(registerDto.getUserName() != null) {
			if (checkUserNameExist(registerDto.getUserName()))
				throw new UserServiceException(PassPortErrorCode.USER_EXISTS.code, PassPortErrorCode.USER_EXISTS.value);
		}
		if(StringUtil.isBlank(memberInfo.getChannelCode()) && (
				MemberUnionType.WEIXIN==(registerDto.getUnionType()) || MemberUnionType.WEIXIN_UN == registerDto.getUnionType())){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("uniqueId", registerDto.getUnionVal());
			params.put("source", ChannelPromoteConstant.SOURCE.WX.getCode());
			ChannelPromote channelPromote= channelPromoteService.queryUniqueByParams(params);
			if(channelPromote!=null){
				memberInfo.setChannelCode(channelPromote.getChannel());
			}
		}
		// 手机为空 为注册联合登录用户，密码默认为空
		if(registerDto.getMobile() != null){
			MemberInfo member = getByMobile(registerDto.getMobile());
			if(member != null) {
				if(StringUtils.isEmpty(member.getPassword())) {
					// 绑定过联合登录的手机号 注册为用户
					member.setPassword(getPassword(registerDto.getPassword(), member.getSalt()));
					updateById(member);
					logger.info("绑定过联合登录的手机号 注册为用户mobile:{},userName:{}", member.getMobile());
					return member;
				}
				else {
					throw new UserServiceException(PassPortErrorCode.MOBILE_REGISTERED.code, PassPortErrorCode.MOBILE_REGISTERED.value);
				}
			}
			memberInfo.setPassword(getPassword(registerDto.getPassword(), memberInfo.getSalt()));
		}
		memberInfo.setNickName(registerDto.getNickName());
		memberInfo.setCreateTime(new Date());
		memberInfo.setState(Boolean.TRUE);
		if (registerDto.getPlatform() != null){
			memberInfo.setPlatForm(registerDto.getPlatform().getCode());
			memberInfo.setPlatformLevel(registerDto.getPlatform().getCode());			
		}
		memberInfo.setSource(registerDto.getSource().code);
		memberInfo.setIp(registerDto.getIp());
		
		try {
			memberInfoDao.insert(memberInfo);
		} catch (Exception e) {
			logger.error(">>>>>[ERROR!]: insertMemberBasicInfo -> " + e.getMessage(), e);
			throw new UserServiceException(PassPortErrorCode.REGISTER_FAIL.code, PassPortErrorCode.REGISTER_FAIL.value);
		}
		
		logger.debug(">>>>>[quit]: insertMemberBasicInfo");
		return memberInfo;
	}

	//Propagation.REQUIRED ：有事务就处于当前事务中，没事务就创建一个事务   isolation=Isolation.DEFAULT：事务数据库的默认隔离级别     readOnly=false：可写 针对 增删改操作
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	@Override
	public MemberInfoDto registerWeb(MemCallDto registerDto) throws UserServiceException {
		MemberInfoDto dto = new MemberInfoDto();
		try {
			if (StringUtil.isNullOrEmpty(registerDto.getMobile()) || StringUtil.isNullOrEmpty(registerDto.getPassword())) {
				dto.setCode(new ResultCode(PassPortErrorCode.PARAM_MISSING.code));
				logger.error(">>>>>[ERROR!]: " + "login name or password is null.");
				return dto;
			}
			if (checkMobileExist(registerDto.getMobile())) {
				dto.setCode(new ResultCode(PassPortErrorCode.USER_EXISTS.code));
				logger.error(">>>>>[ERROR!]: " + "user existed.");
				return dto;
			}
			MemberInfo user = insertMemberBasicInfo(registerDto);
			dto = toDto(user);
			dto.setMemberDetail(saveOrupdateUserDetail(dto.getUid(), registerDto.getIp(), registerDto.getAvatarUrl()));
			dto.setCode(new ResultCode(SuccessCode.REGISTER_SUCCESS.code));
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: registerWeb -> " + ex.getMessage(), ex);
			dto.setCode(new ResultCode(PassPortErrorCode.REGISTER_FAIL.code));
		}
		return dto;
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	@Override
	public MemberInfoDto register(MemCallDto registerDto) throws UserServiceException {
		try {
			logger.debug(">>>>>[begin]: user register:" + registerDto.getMobile() + "ip:" + registerDto.getIp());
			
			if (StringUtil.isNullOrEmpty(registerDto.getMobile()))
				throw new UserServiceException(PassPortErrorCode.USERNAME_MISSING.value);
			if (StringUtil.isNullOrEmpty(registerDto.getPassword()))
				throw new UserServiceException(PassPortErrorCode.PASSWORD_MISSING.value);
			MemberInfo isExist = getByMobile(registerDto.getMobile());
			if (isExist != null && !StringUtils.isEmpty(isExist.getPassword()))
				throw new UserServiceException(PassPortErrorCode.USER_EXISTS.code, PassPortErrorCode.USER_EXISTS.value);

			MemberInfo user = insertMemberBasicInfo(registerDto);
			updateUnionInfo(registerDto,user);
			MemberInfoDto dto = toDto(user);
			dto.setMemberDetail(saveOrupdateUserDetail(dto.getUid(), registerDto.getIp(), registerDto.getAvatarUrl()));
			logger.debug(">>>>>[end]: user register:" + registerDto.getMobile() + "ip:" + registerDto.getIp());
			logger.info("Try to send a new User coupon.");
			// cmbc register push
			if(MemberUnionType.CMBC.equals(registerDto.getUnionType())  ){
				MemberCMBCDto memCmbc = new MemberCMBCDto();
				updateMemCmbcInfo(memCmbc, registerDto, user);
				try {				
					rabbitMqProducer.sendP2PMessage(MqMessageConstant.CMBC_NEW_REGISTER, memCmbc);
				} catch (MqClientException e) {
					e.printStackTrace();
				}							
			}
			
			ResultInfo message = couponUserSerivce.newUserCoupon(registerDto.getMobile());
			if(message.success){
				logger.info("用户注册["+registerDto.getMobile()+"]发券成功!");
			}
			else 
				logger.info("用户注册["+registerDto.getMobile()+"]发券失败!");
			//发放积分,前期使用直接调用
			//pointDetailService.addPointByMemberRegister(user.getId(), user.getUserName(), registerpoint);
			return dto;
		} catch (UserServiceException e) {
			logger.error(">>>>>[ERROR!]: user register:" + registerDto.getMobile() + "ip:" + registerDto.getIp()
					+ " -> " + e.getMessage());
			throw new UserServiceException(e.getMessage());
		} catch (Exception e) {
			logger.error(">>>>>[ERROR!]: user register:" + registerDto.getMobile() + "ip:" + registerDto.getIp()
					+ " -> " + e.getMessage());
			e.printStackTrace();
			throw new UserServiceException(e.getMessage());
		}
	}

	@Override
	public MemberInfoDto registerApp(MemCallDto registerDto) throws UserServiceException {
		if (StringUtil.isNullOrEmpty(registerDto.getSmsCode()))
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_IS_NULL.value);
		StringBuffer smsCodeKey = new StringBuffer(registerDto.getMobile()).append(":")
				.append(SessionKey.APP_REGISTER.value);
		Object o = jedisCacheUtil.getCache(smsCodeKey.toString());
		logger.info("smsCodeKey:"+smsCodeKey);
		logger.info("smsCode Object:"+o);
		logger.info("user input smsCode:"+registerDto.getSmsCode());
		if (null == o)
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
		Integer realSmsCode = Integer.parseInt(this.jedisCacheUtil.getCache(smsCodeKey.toString()).toString());
		// 校验验证吗
		if (registerDto.getSmsCode().intValue() != realSmsCode.intValue())
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
		//return register(userName, password, null,
		//		MemberInfoConstant.RequestSource.MOBILE);
		return register(registerDto);
	}
	@Override
	@Transactional
	public MemberInfoDto bindMobile(MemCallDto bindDto) {
		logger.info("union login bind mobile:{}", bindDto);
		if(bindDto.getMobile() == null) {
			throw new UserServiceException(PassPortErrorCode.MOBILE_IS_NULL.value);
		}
		if (StringUtil.isNullOrEmpty(bindDto.getSmsCode()))
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_IS_NULL.value);
		
		StringBuffer smsCodeKey = new StringBuffer(bindDto.getMobile()).append(":").append(SessionKey.UNION_BINDMOBILE.value);
		Object o = jedisCacheUtil.getCache(smsCodeKey.toString());
		logger.info("smsCodeKey:"+smsCodeKey);
		logger.info("smsCode Object:"+o);
		logger.info("user input smsCode:"+bindDto.getSmsCode());
		if (null == o)
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
		Integer realSmsCode = Integer.parseInt(this.jedisCacheUtil.getCache(smsCodeKey.toString()).toString());
		// 校验验证吗
		if (bindDto.getSmsCode().intValue() != realSmsCode.intValue())
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
		
		UnionInfo unionQuery = new UnionInfo();
		unionQuery.setMemId(bindDto.getMemberId());
		if(bindDto.getUnionType() != null)
			unionQuery.setType(bindDto.getUnionType().code);
		unionQuery.setIsDeleted(false);
		List<UnionInfo> unionInfos = unionInfoService.queryByObject(unionQuery);
		
		if(CollectionUtils.isEmpty(unionInfos)) {
			throw new UserServiceException(PassPortErrorCode.UNION_NOT_EXIST.code, PassPortErrorCode.UNION_NOT_EXIST.value);
		}
		UnionInfo unionInfo = unionInfos.get(0);
		
		MemberInfo memberInfo = null;
		
		MemberInfo query = new MemberInfo();
		query.setMobile(bindDto.getMobile());
		memberInfo = queryUniqueByObject(query);
		
		// 手机号不存在，绑定当前账号
		if(memberInfo == null){
			memberInfo = queryById(bindDto.getMemberId());
			memberInfo.setMobile(bindDto.getMobile());
			memberInfo.setModifyTime(new Date());
			memberInfo.setChannelCode(bindDto.getChannelCode());
			memberInfo.setTpin(bindDto.getTpin());
			memberInfo.setScanPromoterId(bindDto.getScanPromoterId());
			int row = updateNotNullById(memberInfo);
			unionInfo.setIsDeleted(false);
			unionInfoService.updateById(unionInfo);
			if(row > 0) {
				logger.info("用户userName=[{}],绑定手机号[{}]成功", bindDto.getUserName(), bindDto.getMobile());
			}
			else {
				logger.info("用户userName=[{}],绑定手机号[{}]失败", bindDto.getUserName(), bindDto.getMobile());
			}
			ResultInfo message = couponUserSerivce.newUserCoupon(bindDto.getMobile());
			if(message.success){
				logger.info("新手机用户绑定["+bindDto.getMobile()+"]发券成功!");
			}
			else 
				logger.info("新手机用户绑定["+bindDto.getMobile()+"]发券失败!");
		}
		// 手机号已存在 绑定已存在用户, 并生成新的联合登录信息
		else {
			UnionInfo existQuery = new UnionInfo();
			existQuery.setUnionVal(unionInfo.getUnionVal());
			existQuery.setMemId(memberInfo.getId());
			UnionInfo existUnionInfo = unionInfoService.queryUniqueByObject(existQuery);
			if(existUnionInfo != null) {
				// 已绑定过
				if(!existUnionInfo.getIsDeleted()) {
					throw new UserServiceException(PassPortErrorCode.USER_HASH_BIND_MOBILE.code, PassPortErrorCode.USER_HASH_BIND_MOBILE.value);
				}
				existUnionInfo.setIsDeleted(false);
				unionInfoService.updateById(existUnionInfo);
				logger.info("用户userName=[{}],绑定手机号已绑定过手机号{}", bindDto.getUserName(), bindDto.getMobile());
			}
			else {
				unionInfo.setIsDeleted(true);
				unionInfoService.updateById(unionInfo);
				UnionInfo newData = new UnionInfo();
				newData.setCreateTime(new Date());
				newData.setMemId(memberInfo.getId());
				newData.setType(unionInfo.getType());
				newData.setUnionVal(unionInfo.getUnionVal());
				unionInfoService.insert(newData);
				logger.info("用户userName=[{}],绑定手机号[{}]成功", bindDto.getUserName(), bindDto.getMobile());
			}
			MemberInfo oldMemberInfo = queryById(bindDto.getMemberId());
			memberInfo.setChannelCode(oldMemberInfo.getChannelCode());
			memberInfo.setTpin(oldMemberInfo.getTpin());
			memberInfo.setScanPromoterId(oldMemberInfo.getScanPromoterId());
			int row = updateNotNullById(memberInfo);
			if(row > 0) {
				logger.info("用户userName=[{}],绑定手机号[{}]成功", bindDto.getUserName(), bindDto.getMobile());
			}
			else {
				logger.info("用户userName=[{}],绑定手机号[{}]失败", bindDto.getUserName(), bindDto.getMobile());
			}
		}
		
		
		return toDto(memberInfo);
	}
	
	public boolean unbindMobile() {
		return false;
	}
	
	
	private MemberInfo registerForUnionMember(MemCallDto registerDto) throws UserServiceException {
		try {
			logger.debug(">>>>>[begin]: user register:" + registerDto.getUserName() + "ip:" + registerDto.getIp());
			
			if (StringUtil.isNullOrEmpty(registerDto.getUserName()))
				throw new UserServiceException(PassPortErrorCode.USERNAME_MISSING.code, PassPortErrorCode.USERNAME_MISSING.value);
			MemberInfo query = new MemberInfo();
			query.setUserName(registerDto.getUserName());
			MemberInfo memberInfo = queryUniqueByObject(query);
			if(memberInfo != null) return memberInfo;
			

			MemberInfo user = insertMemberBasicInfo(registerDto);
			MemberDetail detail = saveOrupdateUserDetail(user.getId(), registerDto.getIp(), registerDto.getAvatarUrl());
			logger.debug(">>>>>[end]: user register:" + registerDto.getUserName() + "ip:" + registerDto.getIp());
			return user;
		} catch (UserServiceException e) {
			logger.error(">>>>>[ERROR!]: user register:" + registerDto.getUserName() + "ip:" + registerDto.getIp() + " -> " + e.getMessage());
			throw new UserServiceException(e.getMessage());
		} catch (Exception e) {
			logger.error(">>>>>[ERROR!]: user register:" + registerDto.getUserName() + "ip:" + registerDto.getIp() + " -> " + e.getMessage());
			throw new UserServiceException(e);
		}
	}
 
	public Boolean isEmailUsed(String email) throws UserServiceException {
		boolean isUsed = false;
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setEmail(email);
		try {
			List<MemberInfo> memberInfos = memberInfoDao.queryByObject(memberInfo);
			if(null != memberInfos && memberInfos.size() > 0)
				isUsed = true;
		} catch (Exception e) {
			logger.info("isEmailUsed>>>>>[ERROR]: " + e.getMessage());	
			throw new UserServiceException(e.getMessage());
		}
		return isUsed;
	}
	
	public Boolean checkEmailExist(long uid, String email) throws UserServiceException {
		boolean isExist = false;
		try {
			MemberInfo user = new MemberInfo();
			long startTime = System.currentTimeMillis();
			user = memberInfoDao.queryById(uid);
			if((null != user) && 
					(!StringUtil.isNullOrEmpty(user.getEmail())) &&
					email.equals(user.getEmail()))
				isExist = true;
			logger.info(">>>>>[duration]: " + "checkEmailExist -> " + (System.currentTimeMillis() - startTime) + "毫秒");		
		} catch (Exception e) {
			logger.info("checkEmailExist>>>>>[ERROR]: " + e.getMessage());	
			throw new UserServiceException(e.getMessage());
		}		
		return isExist;
	}

	public Boolean checkUserBindEmailBefore(String mobile) {
		boolean isBind = false;
		MemberInfo memberInfo = new MemberInfo();
		long startTime = System.currentTimeMillis();
		memberInfo = getByMobile(mobile);
		if((null != memberInfo) && (!StringUtil.isNullOrEmpty(memberInfo.getEmail())))
			isBind = true;
		logger.info(">>>>>[duration]: " + "checkUserBindEmailBefore -> " + (System.currentTimeMillis() - startTime) + "毫秒");				
		return isBind;
	}
	
	@Override
	public MemberInfoDto getLoginInfoByMemId(Long memberId) {
		MemberInfo member = queryById(memberId);
		return toDto(member);
	}
	
	private MemberInfoDto toDto(MemberInfo memberInfo) {
		if (memberInfo == null)
			return null;
		MemberInfoDto dto = new MemberInfoDto();
		if(!StringUtil.isNullOrEmpty(memberInfo.getEmail()))
			dto.setEmail(memberInfo.getEmail());
		dto.setUid(memberInfo.getId());
		if(!StringUtil.isNullOrEmpty(memberInfo.getUserName()))
			dto.setUsername(memberInfo.getUserName());
		if(!StringUtil.isNullOrEmpty(memberInfo.getMobile()))
			dto.setMobile(memberInfo.getMobile());
		if(null!=memberInfo.getCreateTime())
			dto.setCreateTime(memberInfo.getCreateTime());
		if(null != memberInfo.getNickName()) {
			dto.setNickName(memberInfo.getNickName());
		}
		
		if(null != memberInfo.getSex())
			dto.setSex(memberInfo.getSex());
		dto.setAppLoginToken(getAppLoginToken(memberInfo));
		PromoterInfoMobileDTO promotermobile = new PromoterInfoMobileDTO();
		dto.setPromoterInfo(getPromoterInfoByUser(memberInfo.getId(), dto.getAppLoginToken(), promotermobile) );
		dto.setPromoterInfoMobile(promotermobile);		
		return dto;
	}

	@Override
	public boolean resetPassword(String loginName, String newPwd, Boolean isSendSms,String ip) throws UserServiceException, SmsException, UnsupportedEncodingException {
		if(StringUtil.isNullOrEmpty(loginName)) throw new UserServiceException(PassPortErrorCode.USER_NOT_EXISTS.code,PassPortErrorCode.USER_NOT_EXISTS.value);
		
		MemberInfo user = getByMobile(loginName);
		
		if(null == user) throw new UserServiceException(PassPortErrorCode.USER_NOT_EXISTS.code,PassPortErrorCode.USER_NOT_EXISTS.value);
		
		logger.info(">>>>>[begin]: resetPassword" + loginName);
		
		this.changePassword(user, loginName, newPwd,ip);
		logger.info(">>>>>[end]: resetPassword" + loginName);
		if (isSendSms&&!StringUtil.isNullOrEmpty(user.getMobile())){
			String content = SmsUtil.getResetPwdSmsContent(SmsUtil.getRandomNumber());
			String shortName = promoterInfoService.queryShortNameByChannelCode(user.getChannelCode());
			if(StringUtil.isNoneBlank(content) && StringUtil.isNoneBlank(shortName)){
				content = "【"+shortName+"】"+content;
			}
			sendSmsService.sendSms(user.getMobile(),content,ip);
		}
		return true;
	}
	
	@Override
	public boolean updatePassword(Long userId,String loginName, String newPwd,String originalPwd, Boolean isSendSms,String ip)
			throws UserServiceException, SmsException, UnsupportedEncodingException {
		if(StringUtil.isNullOrEmpty(loginName)) throw new UserServiceException(PassPortErrorCode.USER_NOT_EXISTS.code,PassPortErrorCode.USER_NOT_EXISTS.value);
		MemberInfo memberInfo = null;
		
		if(null!=userId) memberInfo = this.memberInfoDao.queryById(userId);
		else if(!StringUtil.isNullOrEmpty(loginName)) {
			loginName = loginName.trim();
			memberInfo = getByMobile(loginName);
		}else throw new UserServiceException(PassPortErrorCode.PARAM_MISSING.code,PassPortErrorCode.PARAM_MISSING.value);
		
		if(null == memberInfo) throw new UserServiceException(PassPortErrorCode.USER_NOT_EXISTS.code,PassPortErrorCode.USER_NOT_EXISTS.value);
		
		logger.info(">>>>>[begin]: updatePassword" + loginName);
		
		
		
		logger.info("user.password:" + memberInfo.getPassword());
		
		if(!StringUtil.isNullOrEmpty(originalPwd)&&!getPassword(originalPwd,memberInfo.getSalt()).equals(memberInfo.getPassword())) throw new  UserServiceException(PassPortErrorCode.PASSWORD_ERROR.code,PassPortErrorCode.PASSWORD_ERROR.value); 
		
		this.changePassword(memberInfo, loginName, newPwd,ip);
		logger.info(">>>>>[end]: updatePassword" + loginName);
		if (isSendSms&&!StringUtil.isNullOrEmpty(memberInfo.getMobile())){
			String shortName = promoterInfoService.queryShortNameByChannelCode(memberInfo.getChannelCode());
			String content = SmsUtil.getResetPwdSmsContent(SmsUtil.getRandomNumber());
			if(StringUtil.isNoneBlank(shortName)){
				content = "【"+shortName+"】"+content;
			}
			sendSmsService.sendSms(memberInfo.getMobile(),content,ip);
		}
		return true;
	}

	@Override
	public MemberInfoDto updatePasswordApp(MemCallDto updateDto) {
		try {
			Assert.notNull(updateDto);
			if (StringUtil.isNullOrEmpty(updateDto.getSmsCode())) throw new UserServiceException(PassPortErrorCode.SMS_CODE_IS_NULL.value);

			StringBuffer smsCodeKey = new StringBuffer(updateDto.getMobile()).append(":") .append(SessionKey.APP_UPDATE_PASSWORD.value);

			Object o = jedisCacheUtil.getCache(smsCodeKey.toString());
			logger.info("smsCodeKey:"+smsCodeKey);
			logger.info("smsCode Object:"+o);
			logger.info("user input smsCode:"+updateDto.getSmsCode());
			if (null == o) throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);

			Integer realSmsCode = Integer.parseInt(jedisCacheUtil.getCache(smsCodeKey.toString()).toString());

			// 校验验证吗
			if (realSmsCode.intValue() != updateDto.getSmsCode().intValue())
				throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);

			MemberInfo memberInfo = getByMobile(updateDto.getMobile());
			
			return changePassword(memberInfo,updateDto.getMobile(),updateDto.getPassword(),updateDto.getIp());
			
		} catch (UserServiceException e) {
			logger.error(">>>>>[ERROR!]: updatePasswordApp", e);
			throw new UserServiceException(e.getMessage());
		}  catch (Exception e) {
			logger.error(">>>>>[ERROR!]: updatePasswordApp", e);
			throw new UserServiceException(e.getMessage());
		}
	}

	@Override
	public Boolean checkMobileExist(String mobile) throws UserServiceException{
		
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setMobile(mobile);
		long startTime = System.currentTimeMillis();
		Integer count = queryByObjectCount(memberInfo);
		logger.info(">>>>>[duration]: " + "checkMobileExist -> " + (System.currentTimeMillis() - startTime) + "毫秒");		
		if (count > 0)
			return true;
		else
			return false;
	}
	
	@Override
	public Boolean checkUserNameExist(String userName) throws UserServiceException{
		
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setUserName(userName);
		long startTime = System.currentTimeMillis();
		Integer count = queryByObjectCount(memberInfo);
		logger.info(">>>>>[duration]: " + "checkUserNameExist -> " + (System.currentTimeMillis() - startTime) + "毫秒");		
		if (count > 0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * 修改密码
	 * @param loginName
	 * @param password
	 * @return 修改密码后的
	 * @throws UserServiceException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public MemberInfoDto changePassword(MemberInfo memberInfo,String loginName, String password,String ip) throws UserServiceException {
		MemberInfoDto dto = null;
		logger.info("-------------------------begin change password----------------------------");
		try {
				logger.info("-------------------------update local userinfo start----------------------------");
				memberInfo.setSalt(PasswordHelper.getSalt());
				memberInfo.setPassword(getPassword(password, memberInfo.getSalt()));
				memberInfo.setModifyTime(new Date());
				updateNotNullById(memberInfo);
				logger.info(memberInfo.toString());
			dto = toDto(memberInfo);
			logger.info("-------------------------end change password----------------------------");
		} catch (UserServiceException e) {
			logger.error("change password fail ,cause:" + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error("change password fail ,cause:" + e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
		logger.error("--------------------change password success-------------------------------");
		return dto;
	}
	
	@Override
	public Long getLastUid() throws UserServiceException {
		MemberInfo user = memberInfoDao.selectLastUser();
		if(null == user)
			return null;
		else
			return user.getId();
	}
	
//	@Override
	public MemberInfoDto urlLogin(String nl,String ip) throws UserServiceException{
		MemberInfoDto user = null;
		try {
			logger.info("----------------------------------[URL LOGIN BEGIN]----------------------------------");
			
			if(!nl.contains("%01")&&!nl.contains("%02"))
				nl = URLEncoder.encode(nl,"UTF-8");
			
			
			Cookie[] cookies = getUrlLoginCookies(nl);

			if(null == cookies || cookies.length == 0) throw new UserServiceException("nl为空");
			
			String userId = getCookie(cookies, "u");
			String time = getCookie(cookies, "t");
			String ued = getCookie(cookies, "ued");
			String sig = getCookie(cookies, "sig");
			
			if(StringUtil.isNullOrEmpty(userId) 
					|| StringUtil.isNullOrEmpty(time)
					|| StringUtil.isNullOrEmpty(ued)){
				logger.info("cookie内容 不合法");
				throw new UserServiceException("cookie内容 不合法");
			}
			
			logger.info("cookie内容  [u] : "+userId);
			logger.info("cookie内容  [t] : "+time);
			logger.info("cookie内容  [sig] : "+sig);
			logger.info("cookie内容  [ued] : "+ued);
			
			if(!PasswordHelper.checkCookieUed(userId, time, ued)){
				logger.info("生成ued["+PasswordHelper.getCookieUed(userId, time)+"]ued,cookie ued["+ued+"] ued不匹配   不合法 返回 不做登录操作");
				throw new UserServiceException("生成ued["+PasswordHelper.getCookieUed(userId, time)+"]ued,cookie ued["+ued+"] ued不匹配   不合法 返回 不做登录操作");
			}
			
			if (StringUtil.isNullOrEmpty(userId) ) 
				throw new UserServiceException(PassPortErrorCode.USER_ID_IS_NULL.code,PassPortErrorCode.USER_ID_IS_NULL.value);
			long startTime = System.currentTimeMillis();
			
			MemberInfo localUser = queryById(Long.valueOf(userId));
			
			if(null == localUser) throw new UserServiceException(PassPortErrorCode.USER_NOT_EXISTS.code,PassPortErrorCode.USER_NOT_EXISTS.value);
			
			
			user = toDto(localUser);
			user.setMemberDetail(saveOrupdateUserDetail(user.getUid(), ip, null));
			logger.info("----------------------------------[耗时"+((System.currentTimeMillis() - startTime) + "毫秒")+"]----------------------------------");		
		} catch (Exception e) {
			logger.error("----------------------------------[URL LOGIN ERROR]----------------------------------:" + e.getMessage(), e);
			if(e instanceof UserServiceException){
				throw (UserServiceException)e;
			}else throw new UserServiceException(PassPortErrorCode.INIT_USER_FAIL.code,PassPortErrorCode.INIT_USER_FAIL.value);
		}
		logger.info("----------------------------------[URL LOGIN END]----------------------------------");
		return user;
	}
	
	/*@Override
	public MemberInfoDto urlLogin_app(String loginStr,String ip){
		MemberInfoDto user = null;
		try {
			logger.info("----------------------------------[APP URL LOGIN BEGIN]----------------------------------");
			if (StringUtil.isNullOrEmpty(loginStr) ) 
				throw new UserServiceException(SecurityStatusCode.STATUS_4.code,SecurityStatusCode.STATUS_4.value);
			long startTime = System.currentTimeMillis();
			
			MemberInfo babyTreeUser = this.getBabyTreeUser(loginStr,ip);
			
			if(null == babyTreeUser) throw new UserServiceException(PassPortErrorCode.USER_NOT_EXISTS.code,PassPortErrorCode.USER_NOT_EXISTS.value);
			
			
			MemberInfo query = new MemberInfo();
			query.setEncUserId(babyTreeUser.getEncUserId());
			List<MemberInfo> localUsers = this.memberInfoDao.selectDynamic(query);
			MemberInfo localUser = null != localUsers&&!localUsers.isEmpty()?localUsers.get(0):null; 
			
			
			if(null == localUser && !StringUtil.isNullOrEmpty(babyTreeUser.getMobile())) localUser = getByLoginName(babyTreeUser.getMobile());
			
			if(null == localUser && !StringUtil.isNullOrEmpty(babyTreeUser.getEmail())) localUser = getByLoginName(babyTreeUser.getEmail());
			
			if(null != localUser){//本地有用户信息
				logger.info("-------------------------has userinfo for local,begin update----------------------------");
				logger.info("-------------------------update userinfo start----------------------------");
				babyTreeUser.setId(localUser.getId());
				logger.info(babyTreeUser.toString());
				logger.info("-------------------------update userinfo end----------------------------");
				this.memberInfoDao.updateDynamic(babyTreeUser);
			}else{//本地无用户信息
				logger.info("-------------------------not userinfo for local,begin synchronous----------------------------");
				logger.info("-------------------------synchronous userinfo start----------------------------");
				babyTreeUser.setCreateTime(new Date());
				logger.info("-------------------------synchronous userinfo end----------------------------");
				long uid = memberInfoDao.insert(babyTreeUser);
				babyTreeUser.setId(uid);
			}
			user = toDto(babyTreeUser);
			user.setUserDetailDO(saveOrupdateUserDetail(user.getUid(), null));
			logger.info("----------------------------------[耗时"+((System.currentTimeMillis() - startTime) + "毫秒")+"]----------------------------------");		
		} catch (Exception e) {
			logger.error("----------------------------------[URL LOGIN ERROR]----------------------------------:" + e.getMessage(), e);
			throw new UserServiceException(PassPortErrorCode.INIT_USER_FAIL.code,PassPortErrorCode.INIT_USER_FAIL.value);
		}
		logger.info("----------------------------------[APP URL LOGIN END]----------------------------------");
		return user;
	}
	
	
	private MemberInfo getBabyTreeUser(String loginStr,String ip) throws HttpException, IOException{
		logger.info("loginStr:"+loginStr);
		if(StringUtil.isNullOrEmpty(loginStr)) return null;
		if(!loginStr.contains("_")) return null;
		String [] s = loginStr.split("_");
		
		if(null == s || s.length == 0) return null;
		
		String encUserId = s[0];
		String loginTs = s[2];
		
		MemberInfo ud = this.security.getUserSafeInfo(encUserId,ip);
		if(null == ud) throw new UserServiceException(PassPortErrorCode.USER_ID_IS_NULL.code,PassPortErrorCode.USER_ID_IS_NULL.value);
		String newLoginString = SecurityUtil.getLoginStr(encUserId, ud.getSalt(), loginTs);
		
		if(!loginStr.equals(newLoginString)) throw new UserServiceException(SecurityStatusCode.STATUS_4.code,SecurityStatusCode.STATUS_4.value);
		
		return ud;
	}*/
	
	private String getPassword(String password, String salt){
		if(password == null) return null;
		return Hex.encodeHexString(DigestUtils.md5(password+salt));
	}
	public  Cookie [] getUrlLoginCookies(String cookies) throws UnsupportedEncodingException{
		logger.info("cookies:"+cookies);
		if(StringUtil.isNullOrEmpty(cookies)) return null;
		
		if(!cookies.contains("%01") || !cookies.contains("%02")) return null;
		String [] part1 = cookies.split("%01");
		logger.info("part1:"+part1);
		
		if(null == part1 || part1.length == 0) return null;
		List<Cookie> list = new ArrayList<Cookie>();
		for (String cookie : part1) {
			String [] part2 = cookie.split("%02");
			if(null == part2 || part2.length == 0) continue;;
			list.add(new Cookie(part2[0], part2[1]));
		}
		return list.toArray(new Cookie[list.size()]);
	}
	public static String getCookie(Cookie [] cookies, 
			String key) {	
		String value = "";
		if(null != cookies  && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(key)) {
					value = cookie.getValue();
				}			
			}
		}
		return value;
	}
	/**
	 * 生成token
	 * @param user
	 * @param operator 操作类型 调用枚举 MemberToken
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getToken(MemberInfo user,String operator) throws UnsupportedEncodingException{
		logger.info("-----------------------------build user token begin-----------------------------");
		if(null == user) return null;
		StringBuffer tokenStr = new StringBuffer();
		logger.info("user:"+user.toString());
		tokenStr.append(user.getId()).append(":").append(user.getCreateTime()).append("-").append(operator);
		
		tokenStr = new StringBuffer(PasswordHelper.md5(tokenStr.toString()));
		
		tokenStr.append(PasswordHelper.$secretkey);
		
		String token = PasswordHelper.md5(tokenStr.toString());
		logger.info("user token:"+token);
		logger.info("-----------------------------build user token end-----------------------------");
		return token;
	}
	
	public static String getAppLoginToken(MemberInfo memberInfo){
		StringBuffer tokenStr = new StringBuffer();
		tokenStr.append(memberInfo.getId()).append(memberInfo.getMobile()).append(memberInfo.getPassword()).append(PasswordHelper.$secretkey);
		try {
			String token = PasswordHelper.md5(tokenStr.toString());
			logger.info("user token:"+token);
			return token;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	public static boolean checkAppLoginToken(MemberInfo memberInfo, String orgiToken){
		if(StringUtils.isEmpty(orgiToken)){
			return false;
		}
		StringBuffer tokenStr = new StringBuffer();
		tokenStr.append(memberInfo.getMobile()).append(memberInfo.getPassword()).append(PasswordHelper.$secretkey);
		try {
			String token = PasswordHelper.md5(tokenStr.toString());
			logger.info("local token:{}, orgiToken:{}",token, orgiToken);
			return orgiToken.equals(token);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	/**
	 * 
	 * @param user
	 * @param operator
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static boolean checkToken(MemberInfo user,String operator,String token) throws UnsupportedEncodingException{
		boolean isValidate = false;
		
		if(null == user || null == operator || null == token) return isValidate;
		
		String newToken = getToken(user, operator);
		
		if(newToken.equals(token)) isValidate = true;
		
		return isValidate;
	}
	
	
	/**
	 * 根据会员ID更新所关联的推广员是谁
	 * @param memberId
	 * @param promoterId
	 * @return
	 */
	public Integer updatePromoterIdByMemberId(Long memberId,Long promoterId){
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setPromoterId(promoterId);
		memberInfo.setId(memberId);
		return memberInfoDao.updatePromoterIdByMemberId(memberInfo);
	}
	
	public void updateUnionInfo(final MemCallDto registerDto,final MemberInfo memberInfo){
		if(registerDto.getUnionType()==null || StringUtil.isBlank(registerDto.getUnionVal())){
			return;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("unionVal", registerDto.getUnionVal());
		params.put("type", registerDto.getUnionType().code);
		try{
		UnionInfo  unionInfo = unionInfoService.queryUniqueByParams(params);
		if(unionInfo == null){
			UnionInfo newData = new UnionInfo();
			newData.setCreateTime(new Date());
			newData.setMemId(memberInfo.getId());
			newData.setType(registerDto.getUnionType().code);
			newData.setUnionVal(registerDto.getUnionVal());
			unionInfoService.insert(newData);
		}
		else {
			unionInfo.setIsDeleted(false);
			unionInfoService.updateNotNullById(unionInfo);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info("联合登录绑定unionVal:{}, loginName:{}, type:{}", registerDto.getUnionVal(), memberInfo.getMobile(), MemberUnionType.WEIXIN.code);
	}
	
	
	public void updateMemCmbcInfo(MemberCMBCDto memCmbc, final MemCallDto registerDto,final MemberInfo memberInfo){	
		memCmbc.setMemberId(memberInfo.getId());
		memCmbc.setTpin(memberInfo.getTpin());
		memCmbc.setUnionType(registerDto.getUnionType());
		memCmbc.setUnionVal(registerDto.getUnionVal());		
		memCmbc.setMobile(registerDto.getMobile());
	}
   /**
    * 
    * 根据登录号查询会员信息.  
    * @see com.tp.service.mem.IMemberInfoService#getMemberInfoByMobile(java.lang.String)
    */
	@Override
	public MemberInfo getMemberInfoByMobile(String loginName) {
		MemberInfo user = null;
		try {
			logger.info("--------------------getByLoginName begin--------------------------");
			logger.info("loginName:"+loginName);
			MemberInfo query = new MemberInfo();
			if (loginName.contains(MemberInfoConstant.EMAIL_SPL)) {
				// email
				query.setEmail(loginName);
			} else {
				// mobile
				query.setMobile(loginName);
			}
			long startTime = System.currentTimeMillis();
			List<MemberInfo> users = queryByObject(query);
			logger.info(">>>>>[duration]: " + "getByLoginName -> " + (System.currentTimeMillis() - startTime) + "毫秒");		
			if(null!=users&&!users.isEmpty()){
				logger.info("userInfo:"+users.get(0).toString());
				user = users.get(0);
			}
		} catch (Exception e) {
			logger.error("getByLoginName>>>>>>[ERROR]:"+e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		logger.info("--------------------getByLoginName end--------------------------");
		return user;
	
	}
	
	 /**
     * 
     * getThirdShopPoint:(获取对应商城的积分信息). <br/>  
     *  
     * @author zhouguofeng  
     * @param openId
     * @param chanelCode
     * @return  
     * @sinceJDK 1.8
     */
    public  Double  getThirdShopPoint(String openId,String chanelCode){
    	logger.error("chanelCode:--------------"+chanelCode);
    	logger.error("allMemberPointService:--------------"+allMemberPointService.keySet().size());
    	
    	IMemberPointService  memberPointService	=allMemberPointService.get(chanelCode+"MemberPointService");
    	logger.error("memberPointService==null:--------------"+(memberPointService==null));
    	if(memberPointService==null){
    		return 0d;
    	}
    	logger.error("获取到惠惠宝的积分:--------------"+memberPointService.getMemberPorit(openId));
    	return memberPointService.getMemberPorit(openId);
    }
    
    
//    /**
//     * 
//     * costThirdShopPoint:(调用接口扣除第三方接口积分). <br/>  
//     *  
//     * @author zhouguofeng  
//     * @param openId
//     * @param chanelCode
//     * @param costMoney
//     * @return  
//     * @sinceJDK 1.8
//     */
//    public boolean costThirdShopPoint(String openId,String chanelCode,Double costMoney){
//    	IMemberPointService  memberPointService	=allMemberPointService.get(chanelCode+"MemberPointService");
//    	return memberPointService.costMemberPoint(openId, costMoney);
//    }
//   /**
//    * 
//    * 退还积分（可选）.  
//    * @see com.tp.service.mem.IMemberInfoService#backThirdShopPoint(java.lang.String, java.lang.String, java.lang.Double)
//    */
//	@Override
//	public boolean backThirdShopPoint(String openId, String chanelCode, Double backMoney) {
//		  
//		IMemberPointService  memberPointService	=allMemberPointService.get(chanelCode+"MemberPointService");
//    	return memberPointService.backMemberPoint(openId, backMoney);
//	}
//	
	public Map sendOrderToThirdShop(String chanelCode,HhbShopOrderInfoDTO  orderInfo){
		IMemberPointService  memberPointService	=allMemberPointService.get(chanelCode+"MemberPointService");
		return memberPointService.sendOrderInfo(orderInfo);
	}

	@Override
	public MemberInfoDto modifyMobile(MemCallDto modifyMobileDto,Long uId) throws UserServiceException {
		if (StringUtil.isNullOrEmpty(modifyMobileDto.getSmsCode()))
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_IS_NULL.value);
		StringBuffer smsCodeKey = new StringBuffer(modifyMobileDto.getMobile()).append(":")
				.append(SessionKey.MODIFY_MOBILE.value);
		Object o = jedisCacheUtil.getCache(smsCodeKey.toString());
		logger.info("smsCodeKey:"+smsCodeKey);
		logger.info("smsCode Object:"+o);
		logger.info("user input smsCode:"+modifyMobileDto.getSmsCode());
		if (null == o)
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
		Integer realSmsCode = Integer.parseInt(this.jedisCacheUtil.getCache(smsCodeKey.toString()).toString());
		// 校验验证吗
		if (modifyMobileDto.getSmsCode().intValue() != realSmsCode.intValue())
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
		//return register(userName, password, null,
		//		MemberInfoConstant.RequestSource.MOBILE);
		return modifyMobileMethod(modifyMobileDto,uId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	@Override
	public MemberInfoDto modifyMobileMethod(MemCallDto modifyMobileDto,Long uId) throws UserServiceException {
		try {
			logger.debug(">>>>>[begin]: user modify mobile:" + modifyMobileDto.getMobile() + "ip:" + modifyMobileDto.getIp());
			
			if (StringUtil.isNullOrEmpty(modifyMobileDto.getMobile()))
				throw new UserServiceException(PassPortErrorCode.SMS_MOBILE_IS_NULL.value);
			MemberInfo isExist = getByMobile(modifyMobileDto.getMobile());
			if (isExist != null && !StringUtils.isEmpty(isExist.getPassword()))
				throw new UserServiceException(PassPortErrorCode.MOBILE_BIND_BEFORE.code, PassPortErrorCode.MOBILE_BIND_BEFORE.value);

			MemberInfo user = modifyMobileBasicInfo(modifyMobileDto,uId);
			updateUnionInfo(modifyMobileDto,user);
			MemberInfoDto dto = toDto(user);
			dto.setMemberDetail(saveOrupdateUserDetail(dto.getUid(), modifyMobileDto.getIp(), modifyMobileDto.getAvatarUrl()));
			logger.debug(">>>>>[end]: user modify mobile:" + modifyMobileDto.getMobile() + "ip:" + modifyMobileDto.getIp());
			logger.info("Try to send a new User coupon.");
			// cmbc modify mobile push
			if(MemberUnionType.CMBC.equals(modifyMobileDto.getUnionType())  ){
				MemberCMBCDto memCmbc = new MemberCMBCDto();
				updateMemCmbcInfo(memCmbc, modifyMobileDto, user);
				try {				
					rabbitMqProducer.sendP2PMessage(MqMessageConstant.CMBC_NEW_REGISTER, memCmbc);
				} catch (MqClientException e) {
					e.printStackTrace();
				}							
			}
			
//			ResultInfo message = couponUserSerivce.newUserCoupon(modifyMobileDto.getMobile());
//			if(message.success){
//				logger.info("用户注册["+modifyMobileDto.getMobile()+"]发券成功!");
//			}
//			else 
//				logger.info("用户注册["+modifyMobileDto.getMobile()+"]发券失败!");
			//发放积分,前期使用直接调用
			//pointDetailService.addPointByMemberRegister(user.getId(), user.getUserName(), registerpoint);
			return dto;
		} catch (UserServiceException e) {
			logger.error(">>>>>[ERROR!]: user modify mobile:" + modifyMobileDto.getMobile() + "ip:" + modifyMobileDto.getIp()
					+ " -> " + e.getMessage());
			throw new UserServiceException(e.getMessage());
		} catch (Exception e) {
			logger.error(">>>>>[ERROR!]: user modify mobile:" + modifyMobileDto.getMobile() + "ip:" + modifyMobileDto.getIp()
					+ " -> " + e.getMessage());
			e.printStackTrace();
			throw new UserServiceException(e.getMessage());
		}
	}
	
	public MemberInfo modifyMobileBasicInfo(MemCallDto modifyMobileDto,Long uId) {
		logger.debug(">>>>>[enter]: modifyMobileBasicInfo");
//		MemberInfo memberInfo = new MemberInfo();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", modifyMobileDto.getMobile());
		List<MemberInfo> memberInfoList = memberInfoDao.queryByParam(params);//查询手机号是否已存在
		if(memberInfoList.size()>0){
			throw new UserServiceException(PassPortErrorCode.MOBILE_REGISTERED.code, PassPortErrorCode.MOBILE_REGISTERED.value);
		}
		MemberInfo memberInfo = memberInfoDao.queryById(uId);
		ModifyMobileLog modifyMobileLog = new ModifyMobileLog();
		modifyMobileLog.setOldTel(memberInfo.getMobile());
		modifyMobileLog.setNewTel(modifyMobileDto.getMobile());
		modifyMobileLog.setCreateTime(new Date());
		modifyMobileLog.setCreateUser(String.valueOf(memberInfo.getId()));
		modifyMobileLogService.insert(modifyMobileLog);//更换手机号日志记录
		memberInfo.setMobile(modifyMobileDto.getMobile());
		memberInfo.setModifyTime(new Date());
		params.clear();
		params.put("member_id", uId);
		List<PromoterInfo> promoterInfos = promoterInfoService.queryByParam(params);
		for(PromoterInfo promoterInfo : promoterInfos){
			if(StringUtil.isNotBlank(promoterInfo.getInviter()) && PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterInfo.getPromoterType())){
				PromoterInfo query = new PromoterInfo();
				query.setMobile(promoterInfo.getInviter());
				query.setPromoterType(PROMOTER_TYPE.DISTRIBUTE.code);
				PromoterInfo inviter = promoterInfoService.queryUniqueByObject(query);
				if(inviter != null) {
					promoterInfo.setParentPromoterId(inviter.getPromoterId());
				}
			}
			//如果是扫码推广的话 默认生成二维码图片 扫码推广员
			if(PROMOTER_TYPE.SCANATTENTION.code.equals(promoterInfo.getPromoterType()) && StringUtil.isNotBlank(memberInfo.getMobile())){
				//渠道表插入企业渠道
//				ChannelPromote cp = channelPromoteService.checkChannel2Exist(memberInfo.getMobile(), 2);
				ChannelPromote cp = channelPromoteService.checkChannel2Exist(modifyMobileLog.getOldTel(), 2);
				
				String qrcode = StringUtil.EMPTY;
				if(null == cp.getId()){
					//生成二维码
					if(StringUtil.isNotBlank(QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name())){
						String scene = QrcodeConstant.QRCODE_PROMOTER_USER_CODE +memberInfo.getMobile()+"_"+2;
						qrcode = qrcodeService.createScanByWX(scene, QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name());
						if(StringUtil.isNotBlank(qrcode))cp.setQrcode(qrcode);
					}
					cp.setCreateTime(new Date());
					channelPromoteService.insert(cp);
				}else{
					if(StringUtil.isNotBlank(QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name())){
						String scene = QrcodeConstant.QRCODE_PROMOTER_USER_CODE +memberInfo.getMobile()+"_"+2;
						qrcode = qrcodeService.createScanByWX(scene, QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name());
						if(StringUtil.isNotBlank(qrcode))cp.setQrcode(qrcode);
					}
					channelPromoteService.updateNotNullById(cp);
				}
//				String qrcode = channelPromoteProxy.saveChannel(obj.getMobile(),2,QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name(),QrcodeConstant.QRCODE_PROMOTER_USER_CODE);
				if(StringUtil.isNotBlank(qrcode)) promoterInfo.setScanAttentionImage(qrcode);
			}
			
//			promoterInfo.setPromoterName(modifyMobileDto.getMobile());
			promoterInfo.setMobile(modifyMobileDto.getMobile());
			promoterInfoService.updateById(promoterInfo);
		}
		
		try {
			memberInfoDao.updateById(memberInfo);
		} catch (Exception e) {
			logger.error(">>>>>[ERROR!]: modifyMobileBasicInfo -> " + e.getMessage(), e);
			throw new UserServiceException(PassPortErrorCode.MODIFY_MOBILE_FAIL.code, PassPortErrorCode.MODIFY_MOBILE_FAIL.value);
		}
		
		logger.debug(">>>>>[quit]: modifyMobileBasicInfo");
		return memberInfo;
	}
	
	@Override
	public boolean checkMobile(MemCallDto modifyMobileDto) throws UserServiceException {
		if (StringUtil.isNullOrEmpty(modifyMobileDto.getSmsCode()))
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_IS_NULL.value);
		StringBuffer smsCodeKey = new StringBuffer(modifyMobileDto.getMobile()).append(":")
				.append(SessionKey.MODIFY_MOBILE.value);
		Object o = jedisCacheUtil.getCache(smsCodeKey.toString());
		logger.info("smsCodeKey:"+smsCodeKey);
		logger.info("smsCode Object:"+o);
		logger.info("user input smsCode:"+modifyMobileDto.getSmsCode());
		if (null == o)
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
		Integer realSmsCode = Integer.parseInt(this.jedisCacheUtil.getCache(smsCodeKey.toString()).toString());
		// 校验验证吗
		if (modifyMobileDto.getSmsCode().intValue() != realSmsCode.intValue()){
			throw new UserServiceException(PassPortErrorCode.SMS_CODE_ERROR.value);
		}else{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("mobile", modifyMobileDto.getMobile());
			List<MemberInfo> memList = memberInfoDao.queryByParam(params);
			if(null!=memList&&memList.size()>0){
				return true ;
			}else{
				throw new UserServiceException(PassPortErrorCode.USER_NOT_EXISTS.value);
			}
		}
	}
}
