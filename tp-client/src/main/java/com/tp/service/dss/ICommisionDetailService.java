package com.tp.service.dss;

import java.util.List;
import java.util.Map;

import com.tp.model.dss.CommisionDetail;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 佣金明细接口
  */
public interface ICommisionDetailService extends IBaseService<CommisionDetail>{

	/**
	 * 订单提佣
	 * @param commisionDetail
	 * @return
	 */
	public void insertByOrder(List<CommisionDetail> commisionDetailList);
	/**
	 * 退款退还佣金
	 * @param commisionDetail
	 * @return
	 */
	public void insertByRefund(List<CommisionDetail> commisionDetailList);
	
	/**
	 * 更新收货时间
	 * @param commisionDetail
	 * @return
	 */
	public Integer updateReceiveTimeByOrderCode(CommisionDetail commisionDetail);
	
	/**
	 * 把到期的未汇总的佣金进行汇总
	 * @param commisionDetailList
	 * @return
	 */
	public void updateByCollectCommision(List<CommisionDetail> commisionDetailList);
	
	/**
	 * 根据促销人员统计
	 * @param promoterId
	 * @return
	 */
	public Map<String,Number> queryStatisticByOrderPromoterId(Map<String,Object> params);

	
	/**
	 * 根据促销人员统计 （入）
	 * @param promoterId
	 * @return
	 */
	public Map<String,Number> queryStatisticByOrderPromoterId_In(Map<String,Object> params);

	
	/**
	 * 根据促销人员统计 （冲）
	 * @param promoterId
	 * @return
	 */
	public Map<String,Number> queryStatisticByOrderPromoterId_Out(Map<String,Object> params);

	/**
	 * 根据分销员统计订单金额，及父级对应提佣
	 * @param promoterId
	 * @return
	 */
	public List<CommisionDetail> queryStatisticByBizAmountSum(List<Long> idChildren,Integer indirect);
	
	/**
	 * 根据分销员统计订单金额，及父级对应提佣
	 * @param promoterId
	 * @return
	 */
	List<CommisionDetail> queryStatisticByBizAmountCommisionSum(List<Long> idChildren, Integer indirect,Long beneficiary);
	

	/**
	 * 获取 OrdeCode
	 * @param commisionDetail
	 * @return
	 */	
	List<Long> queryDistinctOrderCode(CommisionDetail commisionDetail);
	
	/**
	 * 获取 OrdeCode
	 * @param commisionDetail
	 * @return
	 */	
	List<Long> queryAllCildPromotersDistinctOrderCode(CommisionDetail commisionDetail);
}
