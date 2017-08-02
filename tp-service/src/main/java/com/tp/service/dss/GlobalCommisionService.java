package com.tp.service.dss;

import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.dss.GlobalCommisionDao;
import com.tp.model.dss.GlobalCommision;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.prd.ItemSku;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.dss.IGlobalCommisionService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.prd.IItemSkuService;

@Service
public class GlobalCommisionService extends BaseService<GlobalCommision> implements IGlobalCommisionService {

	private static final String GLOBAL_COMMISION_KEY = "dss:global:commision";
	@Autowired
	private GlobalCommisionDao globalCommisionDao;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	@Autowired
	private IItemSkuService  itemSkuService;
	@Autowired
	IPromoterInfoService promoterInfoService;	

	@Override
	public BaseDao<GlobalCommision> getDao() {
		return globalCommisionDao;
	}

	@Override
	public GlobalCommision queryLastGlobalCommision() {
		GlobalCommision globalCommision = (GlobalCommision)jedisCacheUtil.getCache(GLOBAL_COMMISION_KEY);
		if(null==globalCommision){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "id desc");
			params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), "1");
			globalCommision = super.queryUniqueByParams(params);
			if(globalCommision!=null){
				jedisCacheUtil.setCache(GLOBAL_COMMISION_KEY,globalCommision,1000000);
			}
		}
		return globalCommision;
	}
	

	

	@Override
	public GlobalCommision insert(GlobalCommision globalCommision){
		super.insert(globalCommision);
		jedisCacheUtil.deleteCacheKey(GLOBAL_COMMISION_KEY);
		return globalCommision;
	}

}
