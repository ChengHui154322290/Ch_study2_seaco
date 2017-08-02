/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.service.ord.local;

import com.tp.model.mem.MemberDetail;

/**
 * <pre>
 * 实名认证本地服务类
 * </pre>
 * 
 * @author szy
 * @time 2015-3-18 下午3:29:24
 */
public interface IAuthenticationLocalService {
	/**
	 * 
	 * <pre>
	 * 检测用户的实名认证信息
	 * </pre>
	 * 
	 * @param memberId
	 *            用户ID
	 * @return 实名认证信息，如果为null，则没有进行实名认证
	 */
	MemberDetail getUserDetailInfo(Long memberId);

	/**
	 * 
	 * <pre>
	 * 用户进行实名认证
	 * </pre>
	 * 
	 * @param userDetail
	 *            用户实名认证信息
	 * @param imageUrls
	 *            实名认证图片URL
	 * @return 实名认证具体信息
	 */
	MemberDetail doAuthentication(MemberDetail userDetail, String[] imageUrls);

	/**
	 * 
	 * <pre>
	 * 用户进行实名认证修改
	 * </pre>
	 * 
	 * @param userDetail
	 *            用户实名认证信息
	 * @param picA
	 *            实名认证图片URL
	 * @param picB
	 *            实名认证图片URL
	 * @return 实名认证具体信息
	 */
	MemberDetail doModifyAuthentication(MemberDetail userDetail, String picA, String picB);
}
