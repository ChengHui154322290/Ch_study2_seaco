package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.PointMember;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPointMemberService;
/**
 * 会员积分记录表代理层
 * @author szy
 *
 */
@Service
public class PointMemberProxy extends BaseProxy<PointMember>{

	@Autowired
	private IPointMemberService pointMemberService;

	@Override
	public IBaseService<PointMember> getService() {
		return pointMemberService;
	}
	
	public ResultInfo<PointMember> queryPointMemberByMemberId(Long memberId){
		try{
			return new ResultInfo<PointMember>(pointMemberService.queryPointMemberByMemberId(memberId));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,memberId);
			return new ResultInfo<>(failInfo);
		}
	}
}
