package com.tp.dao.pay;

import com.tp.common.dao.BaseDao;
import com.tp.model.pay.SeagoorPayInfo;

import java.util.List;
import java.util.Map;

public interface SeagoorPayInfoDao extends BaseDao<SeagoorPayInfo> {

    List<SeagoorPayInfo> queryByParamForPage(Map<String,Object> param);

    Integer queryByParamForPageCount(Map<String,Object> param);

}
