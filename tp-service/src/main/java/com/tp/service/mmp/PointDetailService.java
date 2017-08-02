package com.tp.service.mmp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.dao.mmp.PointDetailDao;
import com.tp.dao.mmp.PointMemberDao;
import com.tp.dao.mmp.PointPackageDao;
import com.tp.dao.mmp.PointPackageDetailDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.PointDetail;
import com.tp.model.mmp.PointMember;
import com.tp.model.mmp.PointPackage;
import com.tp.model.mmp.PointPackageDetail;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.mmp.IPointPackageService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

@Service
public class PointDetailService extends BaseService<PointDetail> implements IPointDetailService {

	@Autowired
	private PointDetailDao pointDetailDao;
	@Autowired
	private PointPackageDao pointPackageDao;
	@Autowired
	private PointMemberDao pointMemberDao;
	@Autowired
	private PointPackageDetailDao pointPackageDetailDao;
	@Autowired
	private IPointPackageService pointPackageService;
	
	@Override
	public BaseDao<PointDetail> getDao() {
		return pointDetailDao;
	}

	/**
	 * 兑换积分
	 */
	public PointDetail insert(PointDetail pointDetail){
		Integer packageTime = DateUtil.getYear(new Date());
		Long memberId = pointDetail.getMemberId();
		PointPackage pointPackage = new PointPackage();
		pointPackage.setMemberId(memberId);
		pointPackage.setPackageStatus(PointConstant.PACKAGE_STATUS.USABLE.code);
		pointPackage.setSubTotalPoint(pointDetail.getPoint());
		pointPackage.setCreateUser(pointDetail.getCreateUser());
		pointPackage.setUpdateUser(pointDetail.getCreateUser());
		pointPackage.setPackageTime(packageTime);
		pointPackage.setBizType(pointDetail.getBizType());
		pointPackage = pointPackageService.insert(pointPackage);//保存
		
		PointMember pointMember = pointPackage.getPointMember();
		pointDetail.setOrgTotalPoint(pointMember.getTotalPoint());
		pointDetail = super.insert(pointDetail);
		PointPackageDetail pointPackageDetail = new PointPackageDetail();
		pointPackageDetail.setPointPackageId(pointPackage.getPointPackageId());
		pointPackageDetail.setPointDetailId(pointDetail.getPointDetailId());
		pointPackageDetail.setPoint(pointDetail.getPoint());
		pointPackageDetail.setCreateUser(pointDetail.getCreateUser());
		pointPackageDetailDao.insert(pointPackageDetail);
		return pointDetail;
	}
	
	/**
	 * 使用积分
	 * 1.获取要使用的积分
	 * 2.更新积分
	 * 3.记录积分使用情况
	 * @return
	 */
	public Integer updatePointByMemberUsed(PointDetail pointDetail){
		PointPackage pointPackage = new PointPackage();
		pointPackage.setMemberId(pointDetail.getMemberId());
		pointPackage.setSubTotalPoint(pointDetail.getPoint());
		pointPackage.setCreateUser(pointDetail.getCreateUser());
		pointPackage.setUpdateUser(pointDetail.getCreateUser());
		
		Map<PointPackage,Integer> resultMap = pointPackageService.updatePointByMinusPoint(pointPackage);
		if(MapUtils.isEmpty(resultMap)){
			return 0;
		}
		PointMember pointMember = pointMemberDao.queryByMemberId(pointDetail.getMemberId());
		pointDetail.setOrgTotalPoint(pointMember.getTotalPoint());
		pointDetail.setPointType(PointConstant.OPERATE_TYPE.MINUS.type);
		final PointDetail newPointDetail = super.insert(pointDetail);
		List<PointPackageDetail> pointPackageDetailList = new ArrayList<PointPackageDetail>();
		resultMap.forEach(new BiConsumer<PointPackage,Integer>(){
			public void accept(PointPackage t, Integer u) {
				PointPackageDetail pointPackageDetail = new PointPackageDetail();
				pointPackageDetail.setPointDetailId(newPointDetail.getPointDetailId());
				pointPackageDetail.setPointPackageId(t.getPointPackageId());
				pointPackageDetail.setPoint(u);
				pointPackageDetail.setCreateUser(newPointDetail.getCreateUser());
				pointPackageDetailList.add(pointPackageDetail);
			}
		});
		pointPackageDetailDao.batchInsert(pointPackageDetailList);
		return resultMap.size();
	}
	
	/**
	 * 返还积分
	 * @param pointDetail
	 * @return
	 */
	public ResultInfo<Integer> updatePointByRefund(PointDetail pointDetail){
		Long pointDetailId = pointDetail.getPointDetailId(); 
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pointDetailId", pointDetailId);
		params.put("memberId", pointDetail.getMemberId());
		params.put("bizId", pointDetail.getBizId());
		params.put("bizType", pointDetail.getRelateBizType() == null ? PointConstant.BIZ_TYPE.ORDER.code: pointDetail.getRelateBizType());
		params.put("pointType", PointConstant.OPERATE_TYPE.MINUS.type);
		PointDetail pointDetailDto = null;
		List<PointDetail> pointDetailList = pointDetailDao.queryByParamNotEmpty(params);
		if(CollectionUtils.isNotEmpty(pointDetailList)){
			pointDetailDto = pointDetailList.get(0);
		}
		if(null==pointDetailDto){
			return new ResultInfo<>(new FailInfo("返还积分没有找到使用积分记录",5320));
		}
		pointDetailId = pointDetailDto.getPointDetailId();
		if(!PointConstant.OPERATE_TYPE.MINUS.type.equals(pointDetailDto.getPointType())){
			return new ResultInfo<>(new FailInfo("关联到积分详情不能返还积分",5320));
		}
		if(pointDetailDto.getPoint()<pointDetail.getPoint()){
			pointDetail.setPoint(pointDetailDto.getPoint());
			ExceptionUtils.println(new FailInfo("返还积分比使用积分多",5330), logger, pointDetailDto,pointDetail);
			return new ResultInfo<>(new FailInfo("返还积分比使用积分多",5330));
		}
		List<Long> pointPackageIdList = new ArrayList<Long>();
		Map<Long,Integer> pointPackageMap = new HashMap<Long,Integer>(); 
		List<PointPackageDetail> pointPackageDetailList = pointPackageDetailDao.queryListByPointDetailId(pointDetailId);
		
		Integer sumPoint = 0;
		Long maxPointPackage = pointPackageDetailList.get(0).getPointPackageId();
		for(PointPackageDetail pointPackageDetail:pointPackageDetailList){
			Double rate = BigDecimalUtil.divide(pointPackageDetail.getPoint(), pointDetailDto.getPoint(),6).doubleValue();
			Integer point = BigDecimalUtil.multiply(pointDetail.getPoint(), rate).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			pointPackageDetail.setPoint(point);
			pointPackageDetail.setPointDetailId(null);
			pointPackageDetail.setCreateUser(pointDetail.getCreateUser());
			pointPackageIdList.add(pointPackageDetail.getPointPackageId());
			pointPackageMap.put(pointPackageDetail.getPointPackageId(), point);
			sumPoint+=point;
			if(pointPackageMap.get(maxPointPackage)<pointPackageDetail.getPoint()){
				maxPointPackage = pointPackageDetail.getPointPackageId();
			}
		}
		if(sumPoint!=pointDetail.getPoint()){
			Integer surplusPoint = pointDetail.getPoint()-sumPoint;//剩余积分
			pointPackageMap.put(maxPointPackage,pointPackageMap.get(maxPointPackage)+surplusPoint);
		}
		
		params.clear();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " point_package_id in ("+StringUtil.join(pointPackageIdList, SPLIT_SIGN.COMMA)+")");
		List<PointPackage> pointPackageList = pointPackageDao.queryByParam(params);
		for(PointPackage pointPackage:pointPackageList){//记录不多
			Integer point = pointPackageMap.get(pointPackage.getPointPackageId());
			if(PointConstant.PACKAGE_STATUS.USABLE.code.equals(pointPackage.getPackageStatus())){
				pointPackage.setSubTotalPoint(point);
				pointPackage.setUpdateUser(pointDetail.getCreateUser());
				pointPackageService.updatePointByAddPoint(pointPackage);
			}else{//已作废的积分不返还,但记录详情
				PointDetail discardPointDetail = new PointDetail();
				discardPointDetail.setTitle("作废过期的返还积分");
				discardPointDetail.setBizId(pointDetail.getBizId());
				discardPointDetail.setBizType(PointConstant.BIZ_TYPE.DISCARD.code);
				discardPointDetail.setPoint(point);
				discardPointDetail.setMemberId(pointDetailDto.getMemberId());
				discardPointDetail.setPointType(PointConstant.OPERATE_TYPE.MINUS.type);
				discardPointDetail.setCreateUser(pointDetail.getCreateUser());
				PointMember pointMember = pointMemberDao.queryByMemberId(pointDetailDto.getMemberId());
				discardPointDetail.setOrgTotalPoint(pointMember.getTotalPoint());
				pointDetailDao.insert(discardPointDetail);
				PointPackageDetail pointPackageDetail = new PointPackageDetail();
				pointPackageDetail.setPointDetailId(discardPointDetail.getPointDetailId());
				pointPackageDetail.setPointPackageId(pointPackage.getPointPackageId());
				pointPackageDetail.setPoint(point);
				pointPackageDetail.setCreateUser(pointDetail.getCreateUser());
				pointPackageDetailList.add(pointPackageDetail);
			}
		}
		PointMember pointMember = pointMemberDao.queryByMemberId(pointDetailDto.getMemberId());
		pointDetail.setOrgTotalPoint(pointMember.getTotalPoint());
		pointDetail.setPointDetailId(null);
		pointDetail.setPointType(PointConstant.OPERATE_TYPE.ADD.type);
		pointDetailDao.insert(pointDetail);
		for(PointPackageDetail pointPackageDetail:pointPackageDetailList){
			if(pointPackageDetail.getPointDetailId()==null){
				pointPackageDetail.setPointDetailId(pointDetail.getPointDetailId());
			}
		}
		pointPackageDetailDao.batchInsert(pointPackageDetailList);
		return new ResultInfo<Integer>(pointPackageList.size());
	}
	
	/***
	 * 作废积分，过期积分作废
	 * @param pointDetail
	 * @return
	 */
	public Integer updatePointByDiscard(Integer packageTime){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("packageTime", packageTime);
		params.put("packageStatus",PointConstant.PACKAGE_STATUS.USABLE.code);
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), "100");
		List<PointPackage> pointPackageList = pointPackageDao.queryByParam(params);
		if(CollectionUtils.isEmpty(pointPackageList)){
			return 0;
		}
		List<Long> memberIdList = new ArrayList<Long>();
		for(PointPackage pointPackage:pointPackageList){
			memberIdList.add(pointPackage.getMemberId());
		}
		params.clear();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " member_id in ("+StringUtil.join(memberIdList, SPLIT_SIGN.COMMA)+")");
		List<PointMember> pointMemberList = pointMemberDao.queryByParam(params);
		Map<Long,PointMember> pointMemberMap = new HashMap<Long,PointMember>();
		for(PointMember pointMember:pointMemberList){
			pointMemberMap.put(pointMember.getMemberId(), pointMember);
		}
		for(PointPackage pointPackage:pointPackageList){
			PointMember pointMember = pointMemberMap.get(pointPackage.getMemberId());
			Integer points = pointPackage.getSubTotalPoint();
			if(pointPackage.getSubTotalPoint()>0){
				PointDetail discardPointDetail = new PointDetail();
				discardPointDetail.setTitle(PointConstant.BIZ_TYPE.DISCARD.title);
				discardPointDetail.setBizId(pointPackage.getPackageTime()+"年积分");
				discardPointDetail.setBizType(PointConstant.BIZ_TYPE.DISCARD.code);
				discardPointDetail.setPoint(points);
				discardPointDetail.setMemberId(pointPackage.getMemberId());
				discardPointDetail.setPointType(PointConstant.OPERATE_TYPE.MINUS.type);
				discardPointDetail.setCreateUser(Constant.AUTHOR_TYPE.SYSTEM);
				discardPointDetail.setOrgTotalPoint(pointMember.getTotalPoint());
				pointDetailDao.insert(discardPointDetail);
				
				PointPackageDetail pointPackageDetail = new PointPackageDetail();
				pointPackageDetail.setPointDetailId(discardPointDetail.getPointDetailId());
				pointPackageDetail.setPointPackageId(pointPackage.getPointPackageId());
				pointPackageDetail.setPoint(points);
				pointPackageDetail.setCreateUser(Constant.AUTHOR_TYPE.SYSTEM);
				pointPackageDetailDao.insert(pointPackageDetail);
			}
			pointPackage.setPackageStatus(PointConstant.PACKAGE_STATUS.EXPIRED.code);
			pointPackage.setUpdateUser(Constant.AUTHOR_TYPE.SYSTEM);
			pointPackageDao.updateById(pointPackage);
			pointMember.setTotalPoint(pointPackage.getSubTotalPoint());
			pointMember.setAccumulatePoint(null);
			pointMember.setOperateType(PointConstant.OPERATE_TYPE.MINUS.type);
			pointMember.setMemberId(pointPackage.getMemberId());
			pointMemberDao.updateTotalPointByMemberId(pointMember);
			
		}
		Integer size = pointMemberList.size();
		size+=updatePointByDiscard(packageTime);
		return size;
	}
	
	/**
	 * 会员注册送积分
	 * @param memberId
	 * @return
	 */
	public ResultInfo<Boolean> addPointByMemberRegister(Long memberId,String memberName,Integer point){
		PointDetail pointDetail = new PointDetail();
		pointDetail.setBizId("");
		pointDetail.setBizType(PointConstant.BIZ_TYPE.MEMBER_REGISTER.code);
		pointDetail.setCreateUser(Constant.AUTHOR_TYPE.MEMBER+memberName);
		pointDetail.setMemberId(memberId);
		pointDetail.setPoint(point);
		pointDetail.setTitle(PointConstant.BIZ_TYPE.MEMBER_REGISTER.title);
		pointDetail.setPointType(PointConstant.OPERATE_TYPE.ADD.type);
		try {
			pointDetail = insert(pointDetail);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,pointDetail);
			return new ResultInfo<>(failInfo);
		}
	}
	
	/***
	 * 登录送积分
	 * @param memberId
	 * @return
	 */
	public ResultInfo<Boolean> addPointByMemberLogin(Long memberId,String memberName,Integer point){
		PointDetail pointDetail = new PointDetail();
		pointDetail.setBizId("");
		pointDetail.setBizType(PointConstant.BIZ_TYPE.MEMBER_LOGIN.code);
		pointDetail.setCreateUser(Constant.AUTHOR_TYPE.MEMBER+memberName);
		pointDetail.setMemberId(memberId);
		pointDetail.setPoint(point);
		pointDetail.setTitle(PointConstant.BIZ_TYPE.MEMBER_LOGIN.title);
		pointDetail.setPointType(PointConstant.OPERATE_TYPE.ADD.type);
		try {
			pointDetail = insert(pointDetail);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,pointDetail);
			return new ResultInfo<>(failInfo);
		}
	}
	
	/**
	 * 签到送积分
	 * @param memberId 会员ID
	 * @param signDate 签到日期
	 * @return
	 */
	public ResultInfo<Boolean> addPointByMemberSign(Long memberId,String memberName,Integer point,Date signDate){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		params.put("bizType", PointConstant.BIZ_TYPE.MEMBER_SIGN.code);
		params.put("bizId", DateUtil.format(signDate, "yyyy-MM-dd"));
		PointDetail pointDetail = queryUniqueByParams(params);
		if(pointDetail!=null){
			return new ResultInfo<Boolean>(new FailInfo("本日已签到"));
		}
		pointDetail = new PointDetail();
		pointDetail.setBizId(DateUtil.format(signDate, "yyyy-MM-dd"));
		pointDetail.setBizType(PointConstant.BIZ_TYPE.MEMBER_SIGN.code);
		pointDetail.setCreateUser(Constant.AUTHOR_TYPE.MEMBER+memberName);
		pointDetail.setMemberId(memberId);
		pointDetail.setPoint(point);
		pointDetail.setTitle(PointConstant.BIZ_TYPE.MEMBER_SIGN.title);
		pointDetail.setPointType(PointConstant.OPERATE_TYPE.ADD.type);
		try {
			pointDetail = insert(pointDetail);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,pointDetail);
			return new ResultInfo<>(failInfo);
		}
	}
}
