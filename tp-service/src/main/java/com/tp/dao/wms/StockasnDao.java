package com.tp.dao.wms;

import com.tp.common.dao.BaseDao;
import com.tp.model.wms.Stockasn;

import java.util.List;

public interface StockasnDao extends BaseDao<Stockasn> {

    Integer updateStatusToSuccess(List<Long> ids);


}
