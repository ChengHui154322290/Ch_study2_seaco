package com.tp.service.mem;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.tp.common.util.mem.SmsException;
import com.tp.dto.mem.MemCallDto;
import com.tp.dto.mem.MemberInfoDto;
import com.tp.dto.ord.HhbShopOrderInfoDTO;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.MemberInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 接口
  */
public interface IMemberInfoService extends IBaseService<MemberInfo>{
	List<MemberInfo> selectByIds(List<Long> ids) throws UserServiceException;

	MemberInfoDto login(MemCallDto loginDto) throws UserServiceException;

	MemberInfoDto register(MemCallDto registerDto) throws UserServiceException;

	MemberInfoDto registerWeb(MemCallDto registerDto) throws UserServiceException;

	MemberInfoDto registerApp(MemCallDto registerDto) throws UserServiceException;
	
	MemberInfoDto modifyMobile(MemCallDto registerDto,Long uId) throws UserServiceException;
	
	boolean checkMobile(MemCallDto registerDto) throws UserServiceException;
	
	MemberInfoDto modifyMobileMethod(MemCallDto registerDto,Long uId) throws UserServiceException;

	boolean resetPassword(String loginName, String newPwd, Boolean isSendSms, String ip) throws UserServiceException, SmsException, UnsupportedEncodingException;

	boolean updatePassword(Long userId, String loginName, String newPwd, String originalPwd, Boolean isSendSms, String ip) throws UserServiceException, SmsException, UnsupportedEncodingException;

	MemberInfoDto changePassword(MemberInfo user, String loginName, String password, String ip) throws UserServiceException;

	MemberInfoDto updatePasswordApp(MemCallDto updateDto);

	Boolean checkMobileExist(String mobile) throws UserServiceException;

	Long getLastUid() throws UserServiceException;
	/**根据账号查询可用的会员信息*/
	MemberInfo getByMobile(String loginName);
	/**根据账号查询会员信息*/
	MemberInfo getMemberInfoByMobile(String loginName);
	
	/**
	 * 根据会员ID更新所关联的推广员是谁
	 * @param memberId
	 * @param promoterId
	 * @return
	 */
	Integer updatePromoterIdByMemberId(Long memberId,Long promoterId);

	/**
	 * @param userName
	 * @return
	 * @throws UserServiceException
	 */
	Boolean checkUserNameExist(String userName) throws UserServiceException;

	/**
	 * 第三方联合登录
	 * @param loginDto
	 * @return
	 */
	MemberInfoDto unionLogin(MemCallDto loginDto);

	/**
	 * 联合登录用户绑定手机号
	 * @param bindDto
	 * @return
	 */
	MemberInfoDto bindMobile(MemCallDto bindDto);

	/**
	 * 注册分销员成功后默认登录
	 * @param uid
	 * @return
	 */
	MemberInfoDto registerLogin4Dss(Long uid);

	/**
	 * @param memberId
	 * @return
	 */
	MemberInfoDto getLoginInfoByMemId(Long memberId);
	/***
	 * 
	 * getThirdShopPoint:(获取第三方商城积分). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param openId
	 * @param chanelCode
	 * @return  
	 * @sinceJDK 1.8
	 */
    Double  getThirdShopPoint(String openId,String chanelCode);
//    /**
//     * 
//     * costThirdShopPoint:(扣除第三方商城积分). <br/>  
//     *  
//     * @author zhouguofeng  
//     * @param openId
//     * @param chanelCode
//     * @param costMoney
//     * @return  
//     * @sinceJDK 1.8
//     */
//    boolean costThirdShopPoint(String openId,String chanelCode,Double costMoney);
//    /**
//     * 
//     * costThirdShopPoint:(扣除第三方商城积分). <br/>  
//     *  
//     * @author zhouguofeng  
//     * @param openId
//     * @param chanelCode
//     * @param costMoney
//     * @return  
//     * @sinceJDK 1.8
//     */
//    boolean backThirdShopPoint(String openId,String chanelCode,Double backMoney);
    /**
     * 
     * sendOrderToThirdShop:(发送订单信息). <br/>  
     *  
     * @author zhouguofeng  
     * @param chanelCode
     * @param orderInfo
     * @return  
     * @sinceJDK 1.8
     */
    Map sendOrderToThirdShop(String chanelCode,HhbShopOrderInfoDTO  orderInfo);
}
