package com.tp.service.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.ord.OrderImportInfoDao;
import com.tp.dao.ord.OrderImportLogDao;
import com.tp.datasource.ContextHolder;
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.model.ord.OrderImportInfo;
import com.tp.model.ord.OrderImportLog;
import com.tp.model.prd.ItemImportList;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderImportInfoService;

@Service
public class OrderImportInfoService extends BaseService<OrderImportInfo> implements IOrderImportInfoService {

	@Autowired
	private OrderImportInfoDao orderImportInfoDao;
	@Autowired
	private OrderImportLogDao orderImportLogDao;
	
	@Override
	public BaseDao<OrderImportInfo> getDao() {
		return orderImportInfoDao;
	}

	@Override
	public OrderImportLog saveImportLogDto(OrderImportLog skuImportLogDto) {
		  
		// TODO Auto-generated method stub  
		return null;
	}

	@Override
	public SkuImportLogDto queryOrderImport(String createUser, int status, int pageNo, int pageSize) {

		SkuImportLogDto res = new SkuImportLogDto();
		//log主表
		OrderImportLog importLog = new OrderImportLog();
		importLog.setCreateUser(createUser);
		OrderImportLog orderImportLog = orderImportLogDao.selectByLastOne(importLog,ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		if(null!=orderImportLog){
			//log子表
			Map<String,Object> params = new HashMap<String,Object>();
			if(status!=0){
				params.put("status", status);
			}
			params.put("logId", orderImportLog.getId());
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			Integer totalCout =  orderImportInfoDao.queryByParamCount(params);
			params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), pageNo <= 0?"0," + pageSize : (pageNo - 1)*pageSize + "," + pageSize);
			List<OrderImportInfo> list = orderImportInfoDao.queryPageByParam(params); //调用分页方法
			res.setTotalCount(totalCout.longValue());
//			res.setImportList(list);
		}
		return res;
	
	}
  
}
