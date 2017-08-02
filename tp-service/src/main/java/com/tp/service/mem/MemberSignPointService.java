package com.tp.service.mem;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.MemberSignPointDao;
import com.tp.model.mem.MemberSignPoint;
import com.tp.model.mmp.PointMember;
import com.tp.service.BaseService;
import com.tp.service.mem.IMemberSignPointService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.mmp.IPointMemberService;

@Service
public class MemberSignPointService extends BaseService<MemberSignPoint> implements IMemberSignPointService {

	@Autowired
	private MemberSignPointDao memberSignPointDao;
	
	@Autowired
	private IPointMemberService pointMemberService;
	
	@Autowired
	private IPointDetailService pointDetailService;
	
	@Override
	public BaseDao<MemberSignPoint> getDao() {
		return memberSignPointDao;
	}
	@Transactional
	@Override
	public void changeMemberSignPoint(MemberSignPoint todaySignPoint,Long memberId,String memberName,Integer point){
		memberSignPointDao.insert(todaySignPoint);
		pointDetailService.addPointByMemberSign(memberId, memberName, point, new Date());
		Map<String,Object> params = new HashMap<String,Object>();
		params.clear();
		params.put("memberId", memberId);
		List<PointMember> pointList = pointMemberService.queryByParam(params);
		PointMember pointMember = new PointMember();
		if(CollectionUtils.isEmpty(pointList)){//会员积分记录为空，新增
			pointMember.setMemberId(memberId);
			pointMember.setAccumulatePoint(Long.valueOf(point));
			pointMember.setTotalPoint(point);
			pointMember.setCreateTime(new Date());
			pointMember.setCreateUser(memberName);
			pointMember.setUpdateTime(new Date());
			pointMember.setUpdateUser(memberName);
			pointMemberService.insert(pointMember);
		}else{//会员积分记录不为空，修改
			pointMember = pointList.get(0);
			pointMember.setAccumulatePoint(Long.sum(Long.valueOf(point), pointList.get(0).getAccumulatePoint()));
//			pointMember.setTotalPoint(point+Integer.parseInt(String.valueOf(pointList.get(0).getTotalPoint())));
			pointMember.setUpdateTime(new Date());
			pointMember.setUpdateUser(memberName);
			pointMemberService.updateById(pointMember);
		}
	}
}
