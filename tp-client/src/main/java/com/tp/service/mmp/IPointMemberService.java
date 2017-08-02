package com.tp.service.mmp;

import com.tp.model.mmp.PointMember;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 会员积分记录表接口
  */
public interface IPointMemberService extends IBaseService<PointMember>{

	public PointMember queryPointMemberByMemberId(Long memberId);
}
