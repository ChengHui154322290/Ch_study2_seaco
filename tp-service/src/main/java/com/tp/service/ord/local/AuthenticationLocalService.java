/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.service.ord.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mem.MemberDetail;
import com.tp.service.mem.IMemberDetailService;
import com.tp.service.ord.local.IAuthenticationLocalService;

/**
 * <pre>
 * 实名认证本地服务实现类
 * </pre>
 * 
 * @author szy
 * @time 2015-3-18 下午3:34:36
 */
@Service
public class AuthenticationLocalService implements IAuthenticationLocalService {

	@Autowired
	private IMemberDetailService memberDetailService;

	@Override
	public MemberDetail getUserDetailInfo(Long memberId) {
		return memberDetailService.selectByUid(memberId);
	}

	@Override
	public MemberDetail doAuthentication(MemberDetail userDetail, String[] imageUrls) {
		MemberDetail memberDetail = memberDetailService.doAuthencation(userDetail, imageUrls);
		return memberDetail;
	}

	@Override
	public MemberDetail doModifyAuthentication(MemberDetail userDetail, String picA, String picB) {
		MemberDetail memberDetail = memberDetailService.doRefreshAuthencation(userDetail, picA, picB);
		return memberDetail;
	}
}
