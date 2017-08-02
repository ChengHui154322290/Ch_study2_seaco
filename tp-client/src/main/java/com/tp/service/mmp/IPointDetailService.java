package com.tp.service.mmp;

import java.util.Date;

import com.tp.common.vo.mmp.PointConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.PointDetail;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 积分日志详情表接口
  */
public interface IPointDetailService extends IBaseService<PointDetail>{

	/**
	 * 使用积分
	 * 1.获取要使用的积分
	 * 2.更新积分
	 * 3.记录积分使用情况
	 * @return
	 */
	public Integer updatePointByMemberUsed(PointDetail pointDetail);
	/**
	 * 返还积分
	 * @param pointDetail
	 * @return
	 */
	public ResultInfo<Integer> updatePointByRefund(PointDetail pointDetail);
	/***
	 * 作废积分，过期积分作废
	 * @param pointDetail
	 * @return
	 */
	public Integer updatePointByDiscard(Integer packageTime);
	
	/**
	 * 会员注册送积分
	 * @param memberId
	 * @param memberName
	 * @param point
	 * @return
	 */
	public ResultInfo<Boolean> addPointByMemberRegister(Long memberId,String memberName,Integer point);
	
	/**
	 * 会员登录送积分
	 * @param memberId
	 * @param memberName
	 * @param point
	 * @return
	 */
	public ResultInfo<Boolean> addPointByMemberLogin(Long memberId,String memberName,Integer point);
	
	/**
	 * 会员签到送积分
	 * @param memberId
	 * @param memberName
	 * @param point
	 * @param signDate
	 * @return
	 */
	public ResultInfo<Boolean> addPointByMemberSign(Long memberId,String memberName,Integer point,Date signDate);
}
