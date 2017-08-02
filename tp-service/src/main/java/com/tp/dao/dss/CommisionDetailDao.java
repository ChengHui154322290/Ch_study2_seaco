package com.tp.dao.dss;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.dss.CommisionDetail;

public interface CommisionDetailDao extends BaseDao<CommisionDetail> {

	void batchInsert(@Param("commisionDetailList")List<CommisionDetail> commisionDetailList);
	/**
	 * 更新收货时间
	 * @param commisionDetail
	 * @return
	 */
	public Integer updateReceiveTimeByOrderCode(CommisionDetail commisionDetail);
	
	/**
	 * 根据促销人员统计
	 * @param promoterId
	 * @return
	 */
	public Map<String,Number> queryStatisticByOrderPromoterId(Map<String,Object> params);

	
	/**
	 * 根据促销人员统计（入）
	 * @param promoterId
	 * @return
	 */
	public Map<String,Number> queryStatisticByOrderPromoterId_In(Map<String,Object> params);

	/**
	 * 根据促销人员统计（冲）
	 * @param promoterId
	 * @return
	 */
	public Map<String,Number> queryStatisticByOrderPromoterId_Out(Map<String,Object> params);

	/**
	 * 根据分销员统计订单金额，及父级对应提佣
	 * @param promoterId
	 * @return
	 */
	public List<CommisionDetail> queryStatisticByBizAmountSum(@Param("idChildren")List<Long> idChildren,@Param("indirect") Integer indirect);
	
	/**
	 * 根据分销员统计订单金额，及父级对应提佣
	 * @param promoterId
	 * @return
	 */
	public List<CommisionDetail> queryStatisticByBizAmountCommisionSum(@Param("idChildren")List<Long> idChildren,@Param("indirect") Integer indirect,@Param("beneficiary") Long beneficiary);
	
	/**
	 * 获取 ordercode
	 * @param commisionDetail
	 * @return
	 */	
	public List<Long> queryDistinctOrderCode(CommisionDetail commisionDetail);
	
	/**
	 * 获取 ordercode
	 * @param commisionDetail
	 * @return
	 */	
	public List<Long> queryAllCildPromotersDistinctOrderCode(CommisionDetail commisionDetail);
}
