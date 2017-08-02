package com.tp.service.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.mem.CertificateConstant;
import com.tp.dao.mem.MemberDetailDao;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.MemberDetail;
import com.tp.service.BaseService;
import com.tp.service.mem.IMemberDetailService;
import com.tp.util.StringUtil;

@Service
public class MemberDetailService extends BaseService<MemberDetail> implements IMemberDetailService {

	@Autowired
	private MemberDetailDao memberDetailDao;
	
	@Override
	public BaseDao<MemberDetail> getDao() {
		return memberDetailDao;
	}

	@Override
	public MemberDetail selectByUid(Long uid) {
		MemberDetail query = new MemberDetail();
		query.setUid(uid);
		List<MemberDetail> list = memberDetailDao.queryByObject(query);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public MemberDetail doAuthencation(MemberDetail userDetail,String [] imageUrls) throws UserServiceException{
		try {
			if(StringUtil.isNullOrEmpty(userDetail.getTrueName())) throw new UserServiceException("真实姓名为空");
			if(StringUtil.isNull(userDetail.getCertificateType())) throw new UserServiceException("证件类型为空");
			if(StringUtil.isNullOrEmpty(userDetail.getCertificateValue())) throw new UserServiceException("证件号码为空");
			if(null == imageUrls || 0 == imageUrls.length) throw new UserServiceException("请务必上传身份证正面、反面2张图片");
			
			
			userDetail.setPicA(imageUrls[0]);
			if(imageUrls.length > 1) userDetail.setPicB(imageUrls[1]);
			userDetail.setIsCertificateCheck(Boolean.TRUE);
			userDetail.setVerifyStatus(CertificateConstant.VerifyStatus.APPROVE);
			userDetail.setModifyTime(new Date());
			
			MemberDetail oUserDetail = selectByUid(userDetail.getUid());
			
			logger.debug("[begin]用户\""+userDetail.getUid()+"\"实名认证:"+userDetail.toString());
			if(null == oUserDetail){//新增
				logger.info("未找到用户扩展信息,执行新增");
				userDetail.setCreateTime(new Date());
				userDetail.setIsDelete(Boolean.FALSE);
				memberDetailDao.insert(userDetail);
			}else{//修改
				logger.debug("找到用户扩展信息,执行修改");
				oUserDetail = setMemberDetail(oUserDetail, userDetail);
				memberDetailDao.updateNotNullById(oUserDetail);
				return oUserDetail;
			}
			logger.debug("[end]用户\""+userDetail.getUid()+"\"实名认证:"+userDetail.toString());
		} catch (Exception e) {
			logger.debug("[end]用户\""+userDetail.getUid()+"\"实名认证:"+userDetail.toString()+".exception:"+e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		return userDetail;
	}
	private MemberDetail setMemberDetail(MemberDetail old,MemberDetail ne){
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
	@Override
	public MemberDetail doRefreshAuthencation(MemberDetail userDetail, String picA, String picB) {
		try {
			if(null == userDetail.getId()) throw new UserServiceException("数据异常,用户扩展信息为空");
			if(null == userDetail.getUid()) throw new UserServiceException("数据异常,uid为空");
			if(StringUtil.isNullOrEmpty(picA)|| StringUtil.isNullOrEmpty(picB)) throw new UserServiceException("请务必上传身份证正面、反面2张图片");
			
			userDetail.setPicA(picA);
			userDetail.setPicB(picB);
			userDetail.setModifyTime(new Date());
			logger.debug("[begin]用户\""+userDetail.getUid()+"\"更新实名认证身份证照片:"+userDetail.toString());
			memberDetailDao.updateNotNullById(userDetail);
			logger.debug("[end]用户\""+userDetail.getUid()+"\"更新实名认证身份证照片:"+userDetail.toString());
		} catch (Exception e) {
			logger.debug("[end]用户\""+userDetail.getUid()+"\"更新实名认证身份证照片:"+userDetail.toString()+".exception:"+e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		return userDetail;
	}

}
