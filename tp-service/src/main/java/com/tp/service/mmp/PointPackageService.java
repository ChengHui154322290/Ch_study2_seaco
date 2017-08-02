package com.tp.service.mmp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.dao.mmp.PointMemberDao;
import com.tp.dao.mmp.PointPackageDao;
import com.tp.model.mmp.PointMember;
import com.tp.model.mmp.PointPackage;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPointPackageService;
import com.tp.util.DateUtil;

@Service
public class PointPackageService extends BaseService<PointPackage> implements IPointPackageService {

	private static final String LOCK_KEY ="point:member:";
	@Autowired
	private PointPackageDao pointPackageDao;
	@Autowired
	private PointMemberDao pointMemberDao;
	@Autowired
	private JedisDBUtil jedisDBUtil;
	
	@Override
	public BaseDao<PointPackage> getDao() {
		return pointPackageDao;
	}


	/**
	 * 插入积分打包信息
	 */
	@Override
	public PointPackage insert(PointPackage pointPackage){
		lock(pointPackage.getMemberId());
		PointPackage pointPackageOld = pointPackageDao.queryPointPackageByMemberIdAndPackageTime(pointPackage.getMemberId(),pointPackage.getPackageTime());
		if(null!=pointPackageOld){
			pointPackageOld.setSubTotalPoint(pointPackageOld.getSubTotalPoint()+pointPackage.getSubTotalPoint());
			pointPackageOld.setUpdateUser(pointPackage.getCreateUser());
			pointPackageDao.updateById(pointPackageOld);
			pointPackage.setPointPackageId(pointPackageOld.getPointPackageId());
		}else{
			pointPackageDao.insert(pointPackage);
		}
		PointMember pointMember = pointMemberDao.queryByMemberId(pointPackage.getMemberId());
		if(null==pointMember){
			pointMember = new PointMember();
			pointMember.setMemberId(pointPackage.getMemberId());
			pointMember.setTotalPoint(pointPackage.getSubTotalPoint());
			pointMember.setAccumulatePoint(pointPackage.getSubTotalPoint().longValue());
			pointMember.setCreateUser(pointPackage.getCreateUser());
			pointMember.setUpdateUser(pointPackage.getUpdateUser());
			pointMemberDao.insert(pointMember);
		}else{
			if(PointConstant.BIZ_TYPE.COUPON.code.equals(pointPackage.getBizType())){
				pointMember.setAccumulatePoint(pointMember.getAccumulatePoint()+pointPackage.getSubTotalPoint().longValue());
			}
			pointMember.setTotalPoint(pointMember.getTotalPoint()+pointPackage.getSubTotalPoint());
			pointMember.setUpdateUser(pointPackage.getUpdateUser());
			pointMemberDao.updateNotNullById(pointMember);
		}
		pointPackage.setPointMember(pointMember);
		unLock(pointPackage.getMemberId());
		return pointPackage;
	}
	
	/**
	 * 加积分（或返还积分）
	 * @param pointPackage
	 * @return
	 */
	public Integer updatePointByAddPoint(PointPackage pointPackage){
		if(null==pointPackage.getPackageTime()){
			Integer packageTime = DateUtil.getYear(new Date());
			pointPackage.setPackageTime(packageTime);
		}
		insert(pointPackage);
		return 1;
	}
	
	/**
	 * 使用积分
	 * @param pointPackage
	 * @return
	 */
	public Map<PointPackage,Integer> updatePointByMinusPoint(PointPackage pointPackage){
		lock(pointPackage.getMemberId());
		Integer points = pointPackage.getSubTotalPoint();
		Long memberId = pointPackage.getMemberId();
		PointMember pointMember = pointMemberDao.queryByMemberId(memberId);
		if(null==pointMember || pointMember.getTotalPoint()<points){
			unLock(pointPackage.getMemberId());
			return null;
		}
		PointMember updatePointMember = new PointMember();
		updatePointMember.setMemberId(memberId);
		updatePointMember.setOperateType(PointConstant.OPERATE_TYPE.MINUS.type);
		updatePointMember.setTotalPoint(points);
		updatePointMember.setUpdateUser(pointPackage.getUpdateUser());
		Integer result = pointMemberDao.updateTotalPointByMemberId(updatePointMember);
		if(result==0){
			unLock(pointPackage.getMemberId());
			return null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		params.put("packageStatus", PointConstant.PACKAGE_STATUS.USABLE.code);
		List<PointPackage> pointPackageList = pointPackageDao.queryByParam(params);
		if(CollectionUtils.isEmpty(pointPackageList)){
			unLock(pointPackage.getMemberId());
			return null;
		}
		Collections.sort(pointPackageList, MY_COMP);
		List<PointPackage> updatePointPackageList = new ArrayList<PointPackage>();
		Map<PointPackage,Integer> resultMap = new HashMap<PointPackage,Integer>();
		for(PointPackage t:pointPackageList){
			if(points>0){
				Integer point = t.getSubTotalPoint();
				if(points-t.getSubTotalPoint()>=0){
					points=points-t.getSubTotalPoint();
					t.setSubTotalPoint(0);
				}else{
					point = points;
					t.setSubTotalPoint(t.getSubTotalPoint()-points);
					points = 0;
				}
				t.setUpdateUser(pointPackage.getUpdateUser());
				updatePointPackageList.add(t);
				resultMap.put(t, point);
			}
		}
		if(!CollectionUtils.isEmpty(updatePointPackageList)){
			for(PointPackage t:updatePointPackageList){
				pointPackageDao.updateSubPointById(t);
			}
		}
		unLock(pointPackage.getMemberId());
		return resultMap;
	}
	
	 private static final Comparator<PointPackage> MY_COMP = new Comparator<PointPackage>() {
	        public int compare(PointPackage pp1, PointPackage pp2) {
	            if (pp1.getPackageTime()<pp2.getPackageTime()) {
	                return -1;
	            }
	            return +1;
	        }
	 };
	 
	 private void lock(Long memberId){
		int i=50;
		synchronized(LOCK_KEY+memberId){
			while(i<1000 && !jedisDBUtil.lock(LOCK_KEY+memberId,5)){
				try {
					TimeUnit.MILLISECONDS.sleep(i*=2);
				} catch (InterruptedException e) {
				}
			}
		}
		if(i>=1000){
			logger.info("编辑会员【{}】积分，分布式锁不可用，直接进入",memberId);
		}
	 }
	 
	 private void unLock(Long memberId){
		 jedisDBUtil.unLock(LOCK_KEY+memberId);
	 }
}
