package com.tp.service.ord;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.ReceiptDetailDao;
import com.tp.model.ord.ReceiptDetail;
import com.tp.service.BaseService;
import com.tp.service.ord.IReceiptDetailService;

@Service
public class ReceiptDetailService extends BaseService<ReceiptDetail> implements IReceiptDetailService {

	@Autowired
	private ReceiptDetailDao receiptDetailDao;
	
	@Override
	public BaseDao<ReceiptDetail> getDao() {
		return receiptDetailDao;
	}
	@Override
	public ReceiptDetail selectListBySubOrderCode(Long subOrderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", subOrderCode);
		return super.queryUniqueByParams(params);
	}

	@Override
	public int updateBySubOrderCode(ReceiptDetail receipt) {
		return receiptDetailDao.updateBySubOrderCode(receipt);
	}
}
