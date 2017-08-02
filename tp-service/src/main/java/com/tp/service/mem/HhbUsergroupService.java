package com.tp.service.mem;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.mem.PasswordHelper;
import com.tp.dao.mem.MemberInfoDao;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.mem.MemberInfo;
import com.tp.service.BaseService;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.service.mem.IHhbUsergroupService;

@Service
public class HhbUsergroupService extends BaseService<MemberInfo> implements IHhbUsergroupService {
	
	@Autowired
	private MemberInfoDao memberInfoDao;
	
	@Autowired
	private IConsigneeAddressService consigneeAddressService;
	
	@Override
	public BaseDao<MemberInfo> getDao() {
		return memberInfoDao;
	}
	
	@Override
	public String getAppLoginToken(MemberInfo memberInfo) {
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
	
	@Transactional
	@Override
	public Long hhbRegister(MemberInfo memberInfo, String address, String contact) {
		memberInfoDao.insert(memberInfo);
		ConsigneeAddress consigneeAddress = new ConsigneeAddress();
		consigneeAddress.setAddress(address);
		consigneeAddress.setName(contact);
		consigneeAddress.setMobile(memberInfo.getMobile());
		consigneeAddress.setCreateTime(new Date());
		consigneeAddress.setUpdateTime(new Date());
		consigneeAddress.setPlatForm(5);//平台来源 5：微信
		consigneeAddressService.insert(consigneeAddress);
		return memberInfo.getId();
	}

	

}
