package com.tp.service.mmp;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.ExchangeCouponCodeDTO;
import com.tp.model.mmp.ExchangeCouponChannelCode;
import com.tp.service.IBaseService;

/**
  * @author szy
  * 活动兑换码明细表接口
  */
public interface IExchangeCouponChannelCodeService extends IBaseService<ExchangeCouponChannelCode> {


    /**
     * 插入  活动兑换码明细
     * @param ExchangeCouponChannelCode
     * @param num
     * @return
     */
    List<ExchangeCouponChannelCode> generateCode(ExchangeCouponChannelCode ExchangeCouponChannelCode, Integer num) throws  Exception;


    void batchInsert(List<ExchangeCouponChannelCode> list);

    /**
     *  用户兑换优惠券
     * @param codeModel
     * @return
     */
    ResultInfo exchangeCouponsCode(ExchangeCouponCodeDTO codeModel);

    List<ExchangeCouponChannelCode> queryByTimeAndStatus(Map<String,Object> params);


    List<Map<String,String>> queryExchangeCountDetails(Long actId);


	PageInfo<ExchangeCouponChannelCode> queryExchangeCouponByParam(ExchangeCouponChannelCode query);
   
	
    List<ExchangeCouponChannelCode> queryExchangeCouponByParams(Map<String,Object> params);
	/**
	 * 绑定卡券到推广员
	 * @param params
	 * @return
	 */
	Integer updatePromoterIdBind(Map<String,Object> params);
	
	/**
	 * 封存卡券
	 * @param params
	 * @return
	 */
	Integer updateCouponStatusEnabled(Map<String,Object> params);
	/**
	 * 
	 * updateCouponStatusCancled:(作废卡券). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param params
	 * @return  
	 * @sinceJDK 1.8
	 */
    Integer updateCouponStatusCancled(Map<String,Object> params);
}
