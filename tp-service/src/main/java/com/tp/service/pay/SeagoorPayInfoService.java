package com.tp.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.pay.SeagoorPayInfoDao;
import com.tp.model.pay.SeagoorPayInfo;
import com.tp.service.BaseService;
import com.tp.service.pay.ISeagoorPayInfoService;

import java.util.List;
import java.util.Map;

@Service
public class SeagoorPayInfoService extends BaseService<SeagoorPayInfo> implements ISeagoorPayInfoService {

	@Autowired
	private SeagoorPayInfoDao seagoorPayInfoDao;
	
	@Override
	public BaseDao<SeagoorPayInfo> getDao() {
		return seagoorPayInfoDao;
	}


	@Override
	public PageInfo<SeagoorPayInfo> queryByParamForPage(Map<String, Object> param) {
		PageInfo<SeagoorPayInfo> page = new PageInfo<>();
		List<SeagoorPayInfo> seagoorPayInfos = seagoorPayInfoDao.queryByParamForPage(param);
		page.setRows(seagoorPayInfos);
		page.setRecords(seagoorPayInfoDao.queryByParamForPageCount(param));
		page.setSize(Integer.valueOf(param.get("size").toString()));
		return page;
	}


}
