package com.tp.proxy.mem;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mem.CertificateConstant;
import com.tp.common.vo.mem.PassPortErrorCode;
import com.tp.common.vo.mem.MemberInfoConstant.Number;
import com.tp.dto.mem.CertificateDto;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.MemberDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IMemberDetailService;
import com.tp.util.StringUtil;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class MemberDetailProxy extends BaseProxy<MemberDetail>{

	@Autowired
	private IMemberDetailService memberDetailService;

	@Override
	public IBaseService<MemberDetail> getService() {
		return memberDetailService;
	}
	
	public MemberDetail insertDetailInfo(long uid, String ip) throws UserServiceException{		
		MemberDetail detail = new MemberDetail();
		detail.setUid(uid);
		detail.setCreateTime(new Date());
		detail.setIsCertificateCheck(Boolean.FALSE);
		detail.setIsDelete(Boolean.FALSE);
		detail.setRegistryPlatform(0);
		detail.setVerifyStatus(CertificateConstant.VerifyStatus.UNCOMMITTED);
		try {
			logger.debug("[begin]insert userDetail:"+detail.toString());
			detail = memberDetailService.insert(detail);
			if(detail.getId() == null)
				return null;
			logger.debug("[begin]insert userDetail:"+detail.toString());
		} catch(Exception e) {
			logger.error("[fail]insert userDetail:"+uid+".exception:"+e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		return detail;
	}

	
	public CertificateDto isCertificateCheck(Long userId) throws UserServiceException{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uid", userId);
		MemberDetail nU = memberDetailService.queryUniqueByParams(params);
		CertificateDto dto = null;
		
		if(nU != null && nU.getIsCertificateCheck()){
			dto = new CertificateDto();
			dto.setUserName(nU.getTrueName());
			dto.setIdCard(nU.getCertificateValue());
			dto.setIdType(nU.getCertificateType());
			dto.setPicA(nU.getPicA());
			dto.setPicB(nU.getPicB());
		}
		
		return dto;

	}

	public int update(MemberDetail userDetailDO) throws UserServiceException {
		try {
			return (Integer) memberDetailService.updateNotNullById(userDetailDO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
	}


	public PageInfo<MemberDetail> queryPageListByMemberDetail(
			MemberDetail userDetailDO) throws UserServiceException{
		try {
			if (userDetailDO != null) {
				PageInfo<MemberDetail> pageInfo = new PageInfo<MemberDetail>();
				pageInfo.setPage(userDetailDO.getStartPage());
				pageInfo.setSize(userDetailDO.getPageSize());
				pageInfo = memberDetailService.queryPageByObject(userDetailDO, pageInfo);
				return pageInfo;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
		return new PageInfo<MemberDetail>();
	}


	public MemberDetail selectByUid(Long uid) {
		try {
			MemberDetail query = new MemberDetail();
			query.setUid(uid);
			query.setIsDelete(Boolean.FALSE);
			List<MemberDetail> details = null;
			details = memberDetailService.queryByObject(query);
			if(null == details || details.size() == 0)
				return null;
			else
				return details.get(0);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
	}
	
	
	public MemberDetail findByUserId(Long userId) throws UserServiceException{
		if(null == userId) 
			throw new UserServiceException(PassPortErrorCode.USER_ID_IS_NULL.value);
		return selectByUid(userId);
	}
	
	public MemberDetail doRefreshAuthencation(MemberDetail userDetail,String picA,String picB) throws UserServiceException{
		try {
			if(null == userDetail.getId()) throw new UserServiceException("数据异常,用户扩展信息为空");
			if(null == userDetail.getUid()) throw new UserServiceException("数据异常,uid为空");
			//if(StringUtil.isNullOrEmpty(picA)|| StringUtil.isNullOrEmpty(picB)) throw new UserServiceException("请务必上传身份证正面、反面2张图片");
			
			userDetail.setPicA(picA);
			userDetail.setPicB(picB);
			userDetail.setModifyTime(new Date());
			logger.debug("[begin]用户\""+userDetail.getUid()+"\"更新实名认证身份证照片:"+userDetail.toString());
			this.memberDetailService.updateNotNullById(userDetail);
			logger.debug("[end]用户\""+userDetail.getUid()+"\"更新实名认证身份证照片:"+userDetail.toString());
		} catch (Exception e) {
			logger.debug("[end]用户\""+userDetail.getUid()+"\"更新实名认证身份证照片:"+userDetail.toString()+".exception:"+e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		return userDetail;
	}
	
	public MemberDetail doAuthencation(MemberDetail userDetail,String [] imageUrls) throws UserServiceException{
		try {
			if(StringUtil.isNullOrEmpty(userDetail.getTrueName())) throw new UserServiceException("真实姓名为空");
			if(StringUtil.isNull(userDetail.getCertificateType())) throw new UserServiceException("证件类型为空");
			if(StringUtil.isNullOrEmpty(userDetail.getCertificateValue())) throw new UserServiceException("证件号码为空");
			if(null == imageUrls || Number.ZERO == imageUrls.length) throw new UserServiceException("请务必上传身份证正面、反面2张图片");
			
			
			userDetail.setPicA(imageUrls[Number.ZERO]);
			if(imageUrls.length > Number.ONE) userDetail.setPicB(imageUrls[Number.ONE]);
			userDetail.setIsCertificateCheck(Boolean.TRUE);
			userDetail.setVerifyStatus(CertificateConstant.VerifyStatus.APPROVE);
			userDetail.setModifyTime(new Date());
			
			MemberDetail oUserDetail = findByUserId(userDetail.getUid());
			
			logger.debug("[begin]用户\""+userDetail.getUid()+"\"实名认证:"+userDetail.toString());
			if(null == oUserDetail){//新增
				logger.info("未找到用户扩展信息,执行新增");
				userDetail.setCreateTime(new Date());
				userDetail.setIsDelete(Boolean.FALSE);
				memberDetailService.insert(userDetail);
				if(userDetail.getId() != null)
					return null;
			}else{//修改
				logger.debug("找到用户扩展信息,执行修改");
				oUserDetail = setUserDetailDO(oUserDetail, userDetail);
				memberDetailService.updateById(oUserDetail);
				return oUserDetail;
			}
			logger.debug("[end]用户\""+userDetail.getUid()+"\"实名认证:"+userDetail.toString());
		} catch (Exception e) {
			logger.debug("[end]用户\""+userDetail.getUid()+"\"实名认证:"+userDetail.toString()+".exception:{}",e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		return userDetail;
	}
	
	private MemberDetail setUserDetailDO(MemberDetail old,MemberDetail ne){
		if(!StringUtil.isNullOrEmpty(ne.getTrueName())) old.setTrueName(ne.getTrueName());
		if(!StringUtil.isNull(ne.getCertificateType())) old.setCertificateType(ne.getCertificateType());
		if(!StringUtil.isNull(ne.getCertificateValue())) old.setCertificateValue(ne.getCertificateValue());
		if(!StringUtil.isNullOrEmpty(ne.getPicA())) old.setPicA(ne.getPicA());
		if(!StringUtil.isNullOrEmpty(ne.getPicB())) old.setPicB(ne.getPicB());
		if(!StringUtil.isNull(ne.getIsCertificateCheck())) old.setIsCertificateCheck(ne.getIsCertificateCheck());
		if(!StringUtil.isNull(ne.getVerifyStatus())) old.setVerifyStatus(ne.getVerifyStatus());
		if(!StringUtil.isNull(ne.getModifyTime())) old.setModifyTime(ne.getModifyTime());	
		return old;
	}
	
}
