package com.tp.dao.pay;

import com.tp.common.dao.BaseDao;
import com.tp.model.pay.SeagoorPayRefundInfo;

import java.util.List;
import java.util.Map;

public interface SeagoorPayRefundInfoDao extends BaseDao<SeagoorPayRefundInfo> {

    List<SeagoorPayRefundInfo> queryByParamForPage(Map<String,Object> param);

    Integer queryByParamForPageCount(Map<String,Object> param);

}
