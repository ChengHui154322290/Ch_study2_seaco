package com.tp.common.vo.mmp;;

/**
* 日志记录 常量
* @author szy
*/
public interface ExchangeCodeConstant {
	/** 兑换码状态-封存  */
	int STATUS_SEALED = -1;
   /** 兑换码状态-未使用  */
    int STATUS_EXCHANGE = 0;

   /** 兑换码状态-已使用  */
    int STATUS_USE_EXCHANGE = 1;
    /**作废*/
    int STATUS_CANCLE=3;

   /** 兑换码活动状态-正常  */
    int STATUS_ACT_EXCHANGE_NOMAL = 0;

   /** 兑换码活动状态-已终止  */
    int STATUS_ACT_EXCHANGE_END = 1;

}
