package com.tp.dao.mmp;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.ExchangeCouponChannel;

public interface ExchangeCouponChannelDao extends BaseDao<ExchangeCouponChannel> {

    int updateUseNumById(ExchangeCouponChannel exchangeCouponChannel);

}
