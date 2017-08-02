package com.tp.world.ao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.model.mem.MemberSignPoint;
import com.tp.model.mmp.PointMember;
import com.tp.proxy.mem.MemberSignPointProxy;
import com.tp.proxy.mmp.PointMemberProxy;

/**
 * 会员签到
 * @author szy
 *
 */
@Service
public class MemberSignPointAO {

	@Autowired
	private MemberSignPointProxy memberSignPointProxy;
	
	@Autowired
	private PointMemberProxy pointMemberProxy;
	
	public MResultVO<List<MemberSignPoint>> queryMemberSignPointListByMemberId(Long memberId){
		ResultInfo<List<MemberSignPoint>> result = memberSignPointProxy.queryMemberSignPointListByMemberId(memberId);
		if(result.success){
			return new MResultVO<List<MemberSignPoint>>(MResultInfo.SUCCESS,result.getData());
		}
		return new MResultVO(MResultInfo.FAILED,result.msg.getMessage());
	}
	public MResultVO<List<PointMember>> querytotalPointListByMemberId(Long memberId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		ResultInfo<List<PointMember>> result = pointMemberProxy.queryByParam(params);
		if(result.success){
			return new MResultVO<List<PointMember>>(MResultInfo.SUCCESS,result.getData());
		}
		return new MResultVO(MResultInfo.FAILED,result.msg.getMessage());
	}
	
	public MResultVO<MemberSignPoint> insertMemberSignPoint(Long memberId,String memberName){
		ResultInfo<MemberSignPoint>  result = memberSignPointProxy.insertMemberSignPoint(memberId,memberName);
		if(result.success){
			return new MResultVO<MemberSignPoint>(MResultInfo.SUCCESS,result.getData());
		}
		return new MResultVO(MResultInfo.FAILED,result.msg.getMessage());
	}
	
	public MResultVO<List<MemberSignPoint>> showAndSignPoint(Long memberId,String memberName){
		ResultInfo<List<MemberSignPoint>> result = memberSignPointProxy.showAndSignPoint(memberId,memberName);
		if(result.success){
			return new MResultVO<List<MemberSignPoint>>(MResultInfo.SUCCESS,result.getData());
		}
		return new MResultVO(MResultInfo.FAILED,result.msg.getMessage());
	}
	public MResultVO<List<MemberSignPoint>> showSignPointDate(Long memberId){
		ResultInfo<List<MemberSignPoint>> result = memberSignPointProxy.showSignPointData(memberId);
		if(result.success){
			return new MResultVO<List<MemberSignPoint>>(MResultInfo.SUCCESS,result.getData());
		}
		return new MResultVO(MResultInfo.FAILED,result.msg.getMessage());
	}
}
