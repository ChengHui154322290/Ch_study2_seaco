package com.tp.shop.ao.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.model.mem.MemberSignPoint;
import com.tp.proxy.mem.MemberSignPointProxy;

/**
 * 会员签到
 * @author szy
 *
 */
@Service
public class MemberSignPointAO {

	@Autowired
	private MemberSignPointProxy memberSignPointProxy;
	
	public MResultVO<List<MemberSignPoint>> queryMemberSignPointListByMemberId(Long memberId){
		ResultInfo<List<MemberSignPoint>> result = memberSignPointProxy.queryMemberSignPointListByMemberId(memberId);
		if(result.success){
			return new MResultVO<List<MemberSignPoint>>(MResultInfo.SUCCESS,result.getData());
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
	
}
