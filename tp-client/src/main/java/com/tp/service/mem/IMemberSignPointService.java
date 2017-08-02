package com.tp.service.mem;

import com.tp.model.mem.MemberSignPoint;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 会员签到获取积分日志表接口
  */
public interface IMemberSignPointService extends IBaseService<MemberSignPoint>{

	void changeMemberSignPoint(MemberSignPoint todaySignPoint,Long memberId,String memberName,Integer point);
}
