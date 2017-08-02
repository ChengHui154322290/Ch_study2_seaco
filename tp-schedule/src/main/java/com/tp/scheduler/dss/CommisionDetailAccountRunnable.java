package com.tp.scheduler.dss;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tp.common.vo.DssConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.model.dss.CommisionDetail;
import com.tp.proxy.dss.CommisionDetailProxy;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.util.DateUtil;

/**
 * 佣金入账
 * @author szy
 *
 */
@Component
public class CommisionDetailAccountRunnable extends AbstractJobRunnable {

	private static final Logger logger = LoggerFactory.getLogger(CommisionDetailAccountRunnable.class);
	private static final String CURRENT_JOB_PREFIXED = "CommisionDetailAccountRunnable";
	
	@Value("#{config['CommisionDetailAccountRunnable.CREATE_AFTER_DAY']}")
	private int CREATE_AFTER_DAY = -15;
	
	@Autowired
	private CommisionDetailProxy commisionDetailProxy;
	
	@Override
	public void execute() {
		logger.info("发货15天后佣金汇总到促销人员账号上");
		Date beginDate = DateUtil.parse(DateUtil.formatDate(DateUtil.getDateAfterDays(CREATE_AFTER_DAY-10)),DateUtil.WEB_FORMAT);
		Date endDate = DateUtil.addDays(beginDate, 10);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("collectStatus", DssConstant.COLLECT_STATUS.NO.code);
		List<WHERE_ENTRY> whereEntryList = new ArrayList<WHERE_ENTRY>();
		whereEntryList.add(new WHERE_ENTRY("create_time",MYBATIS_SPECIAL_STRING.LT,endDate));
		whereEntryList.add(new WHERE_ENTRY("create_time",MYBATIS_SPECIAL_STRING.GT,beginDate));
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereEntryList);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " promoter_id asc,biz_type asc");
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 1000);
		Integer size = 0;
		do{
			size = 0;
			List<CommisionDetail> commisionDetailList = commisionDetailProxy.queryByParam(params).getData(); 
			if(CollectionUtils.isNotEmpty(commisionDetailList)){
				size = commisionDetailList.size();
				commisionDetailProxy.updateByCollectCommision(commisionDetailList);
			}
		}while(size>0);
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}

}
