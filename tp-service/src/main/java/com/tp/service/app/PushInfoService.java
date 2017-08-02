package com.tp.service.app;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gexin.rp.sdk.base.IPushResult;
import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.app.PushConstant;
import com.tp.dao.app.PushInfoDao;
import com.tp.model.app.PushInfo;
import com.tp.service.BaseService;
import com.tp.service.app.IPushInfoService;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 * APP推送消息接口实现类
 * @author zhuss
 * @2016年2月24日 下午6:45:15
 */
@Service
public class PushInfoService extends BaseService<PushInfo> implements IPushInfoService {
	

	@Autowired
	private PushInfoDao pushInfoDao;
	
	@Override
	public BaseDao<PushInfo> getDao() {
		return pushInfoDao;
	}
	
	@Autowired
	private GeTuiService geTuiService;

	private boolean sendMessageBase(PushInfo pushInfo,Boolean notFixedTime) {
		if(null == pushInfo.getId()){ //首次发送
			pushInfoDao.insert(pushInfo);
			if(notFixedTime && pushInfo.getPushWay() == 2)return true;
		}else{ //再次发送
			pushInfo = pushInfoDao.queryById(pushInfo.getId());
			if(notFixedTime && pushInfo.getPushWay() == 2)return true;
		}
		IPushResult result = geTuiService.pushMessage(pushInfo);
		if(result!=null && StringUtil.isNotBlank(result.getResponse().get("contentId"))){
			pushInfo.setPushStatus(PushConstant.PUSH_STATUS.YES.getCode());
			pushInfo.setContentId(result.getResponse().get("contentId").toString());
			pushInfo.setModifyDate(new Date());
			pushInfoDao.updateNotNullById(pushInfo);
			return Boolean.TRUE;
		} 
		return false;
	}
	
	@Override
	public boolean sendMessage(PushInfo pushInfo) {
		return sendMessageBase(pushInfo,Boolean.TRUE);
	}
	
	@Override
	public void sendTimerMessage(Integer interval){
		Date currentDate = new Date();
		currentDate.setTime(currentDate.getTime()+5000);
		String currentSendTime = DateUtil.format(currentDate, "yyyy-MM-dd HH:mm:ss");
		currentDate.setTime(currentDate.getTime()-interval*1000);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),"send_date >='"+DateUtil.format(currentDate, "yyyy-MM-dd HH:mm:ss")+"' and send_date <='"+currentSendTime+"'");
		params.put("pushWay", PushConstant.PUSH_WAY.YES.getCode());
		params.put("pushStatus", PushConstant.PUSH_STATUS.NO.getCode());
		// 基于当前时间,获取待推送消息
		List<PushInfo> pushInfoList = pushInfoDao.queryByParam(params);
		for(PushInfo pushInfo:pushInfoList){
			sendMessageBase(pushInfo,Boolean.FALSE);
		}
	}
}
