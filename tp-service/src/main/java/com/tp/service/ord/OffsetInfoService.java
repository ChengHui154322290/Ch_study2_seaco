package com.tp.service.ord;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.ord.OffsetConstant;
import com.tp.dao.ord.OffsetInfoDao;
import com.tp.dao.ord.OffsetLogDao;
import com.tp.model.ord.OffsetInfo;
import com.tp.model.ord.OffsetLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IOffsetInfoService;
import com.tp.util.CodeCreateUtil;

@Service
public class OffsetInfoService extends BaseService<OffsetInfo> implements IOffsetInfoService {

	@Autowired
	private OffsetInfoDao offsetInfoDao;
	@Autowired
	private OffsetLogDao offsetLogDao;
	
	@Override
	public BaseDao<OffsetInfo> getDao() {
		return offsetInfoDao;
	}

	public OffsetInfo insert(OffsetInfo offsetInfo){
		offsetInfo.setOffsetCode(CodeCreateUtil.initRejectCode());
		offsetInfo.setOffsetStatus(OffsetConstant.OFFSET_STATUS.APPLY.code);
		offsetInfoDao.insert(offsetInfo);
		OffsetLog log = new OffsetLog();
		log.setOffsetCode(offsetInfo.getOffsetCode());
		log.setActionType(OffsetConstant.OFFSET_ACTION_TYPE.APPLY.code);
		log.setCurrentOffsetStatus(OffsetConstant.OFFSET_STATUS.APPLY.code);
		log.setOffsetId(offsetInfo.getOffsetId());
		log.setOperatorName(offsetInfo.getCreateUser());
		log.setOperatorType(Constant.LOG_AUTHOR_TYPE.MEMBER.code);
		log.setOrderCode(offsetInfo.getOrderCode());
		log.setLogContent("申请补偿 "+offsetInfo.getRemarks());
		log.setCreateTime(new Date());
		log.setCreateUser(offsetInfo.getCreateUser());
		offsetLogDao.insert(log);
		return offsetInfo;
	}
}
