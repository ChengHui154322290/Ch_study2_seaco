package com.tp.m.vo.seagoorpay;

import java.util.List;

import com.tp.m.query.seagoorpay.SeagoorPayBaseVO;

/**
 * Created by ldr on 2016/11/28.
 */
public class SeagoorPayRefundQueryResultVO extends SeagoorPayBaseVO {

    List<SeagoorPayRefundResultVO> refund_list ;

    public List<SeagoorPayRefundResultVO> getRefund_list() {
        return refund_list;
    }

    public void setRefund_list(List<SeagoorPayRefundResultVO> refund_list) {
        this.refund_list = refund_list;
    }
}
