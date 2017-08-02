package com.tp.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.pay.SeagoorPayRefundInfoDao;
import com.tp.model.pay.SeagoorPayRefundInfo;
import com.tp.service.BaseService;
import com.tp.service.pay.ISeagoorPayRefundInfoService;

import java.util.List;
import java.util.Map;

@Service
public class SeagoorPayRefundInfoService extends BaseService<SeagoorPayRefundInfo> implements ISeagoorPayRefundInfoService {

	@Autowired
	private SeagoorPayRefundInfoDao seagoorPayRefundInfoDao;
	
	@Override
	public BaseDao<SeagoorPayRefundInfo> getDao() {
		return seagoorPayRefundInfoDao;
	}



	public  PageInfo<SeagoorPayRefundInfo> queryByParamsForPage(Map<String,Object> param){

		List<SeagoorPayRefundInfo> seagoorPayRefundInfos = seagoorPayRefundInfoDao.queryByParamForPage(param);
		Integer count = seagoorPayRefundInfoDao.queryByParamForPageCount(param);

		PageInfo<SeagoorPayRefundInfo> pageInfo = new PageInfo<>();
		pageInfo.setSize(Integer.valueOf(param.get("size").toString()));
		pageInfo.setRecords(count);
		pageInfo.setRows(seagoorPayRefundInfos);
		return  pageInfo;
	}


}
