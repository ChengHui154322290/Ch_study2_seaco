package com.tp.proxy.mem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.mem.WhiteConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.ConsigneeAddressKVDTO;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.mem.MemberDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mem.WhiteInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.service.mem.IMemberDetailService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.IWhiteInfoService;
import com.tp.util.StringUtil;
/**
 * 白名单代理层
 * @author zhuss
 *
 */
@Service
public class WhiteInfoProxy extends BaseProxy<WhiteInfo>{

	@Autowired
	private IWhiteInfoService whiteInfoService;
	
	@Autowired
	private IMemberInfoService memberInfoService;
	
	@Autowired
	private IMemberDetailService memberDetailService;
	
	@Autowired
	private IConsigneeAddressService consigneeAddressService;

	@Override
	public IBaseService<WhiteInfo> getService() {
		return whiteInfoService;
	}
	
	public ResultInfo<?> save(WhiteInfo whiteInfo){
		if(whiteInfo.getId()==null){
			whiteInfo.validate(whiteInfo);
			//验证手机是否已经是白名单
			WhiteInfo v = new WhiteInfo();
			v.setMobile(whiteInfo.getMobile());
			v = whiteInfoService.queryUniqueByObject(v);
			if(null != v && null != v.getId())throw new UserServiceException("手机已是白名单");
			//验证手机号是否存在
			MemberInfo memberInfo = memberInfoService.getByMobile(whiteInfo.getMobile());
			if(null == memberInfo || null == memberInfo.getId())throw new UserServiceException("用户不存在");
			whiteInfo.setUserId(memberInfo.getId());
			//验证是否已实名
			MemberDetail memberDetail = memberDetailService.selectByUid(memberInfo.getId());
			if(null == memberDetail || StringUtil.isBlank(memberDetail.getCertificateValue()))throw new UserServiceException("用户未实名");
			whiteInfo.setTrueName(memberDetail.getTrueName());
			whiteInfo.setCertificateType(memberDetail.getCertificateType());
			whiteInfo.setCertificateValue(memberDetail.getCertificateValue().replaceAll("^(\\d{4})(.+)(\\d{4}.)$", "$1**********$3"));
			whiteInfo.setCreateTime(whiteInfo.getModifyTime());
			whiteInfo.setCreateUser(whiteInfo.getModifyUser());
			whiteInfo.setStatus(WhiteConstant.STATUS.YES.getCode());
			whiteInfo.setIsDel(0);
			return insert(whiteInfo);
		}else{
			return updateNotNullById(whiteInfo);
		}
	}
	
	public ResultInfo<List<ConsigneeAddressKVDTO>> addresslist(String mobile){
		MemberInfo memberInfo = memberInfoService.getByMobile(mobile);
		if(null != memberInfo && null != memberInfo.getId()){
			List<ConsigneeAddress> addresslist = consigneeAddressService.findByUserId(memberInfo.getId(), 10);
			if(CollectionUtils.isNotEmpty(addresslist)){
				List<ConsigneeAddressKVDTO> addressKV = new ArrayList<>();
				for(ConsigneeAddress address : addresslist){
					String fullAddress = address.getProvince()+address.getCity()+address.getCounty()+address.getStreet()+address.getAddress();
					addressKV.add(new ConsigneeAddressKVDTO(address.getId(),fullAddress));
				}
				return new ResultInfo<>(addressKV);
			}
		}
		return new ResultInfo<>(new FailInfo("用户未添加收货地址"));
	}
}
