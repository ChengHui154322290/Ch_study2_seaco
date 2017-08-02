package com.tp.dao.mmp;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.ExchangeCouponChannelCode;

public interface ExchangeCouponChannelCodeDao extends BaseDao<ExchangeCouponChannelCode> {

    void batchInsert(List<ExchangeCouponChannelCode> list);

    int updateStatus(ExchangeCouponChannelCode couponChannelCode);

    List<ExchangeCouponChannelCode> queryByTimeAndStatus(Map<String, Object> param);

    List<Map<String,String>> queryExchangeCountDetails(Long actId);
    
    Integer queryExchangeCouponByParamCount(ExchangeCouponChannelCode query);
    
    List<ExchangeCouponChannelCode> queryExchangeCouponByParam(ExchangeCouponChannelCode query);
    
    /**
     * 查询批次中最大的序列
     * @param couponId
     * @return
     */
	Long queryCodeSeqMaxByCouponId(Long couponId);
	
	/**
	 * 绑定卡券到推广员
	 * @param params
	 * @return
	 */
	Integer updatePromoterIdBind(Map<String,Object> params);
	
	Integer updateCouponStatusEnabled(Map<String,Object> params);
	
	Integer cancleCouponStatusEnabled(Map<String,Object> params);
	
	List<ExchangeCouponChannelCode> queryExchangeCouponByParams(Map<String,Object> query);
}
