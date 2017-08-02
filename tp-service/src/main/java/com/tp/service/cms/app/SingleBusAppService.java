package com.tp.service.cms.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.cms.TempleConstant;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.mmp.Topic;
import com.tp.query.mmp.CmsTopicSimpleQuery;
import com.tp.service.cms.app.ISingleBusAppService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.SendSmsService;
import com.tp.service.mmp.ITopicService;


/**
* APP的模板业务管理 Service
* @author szy
*/
@Service(value="singleBusAppService")
public class SingleBusAppService implements ISingleBusAppService{

	@Autowired
	private ITopicService topicService;
	
	@Autowired
	SendSmsService sendSmsService;
	
	@Autowired
	IMemberInfoService memberInfoService;
	
	/*@Autowired
	SwitchBussiesConfigDao switchBussiesConfigDao;*/
	
	/**
	 * 首页-今日上新
	 * @param pageStart 起始页
	 * @param pageSize  页大小
	 * @param platFormType 平台标识
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageInfo<AppSingleInfoDTO> queryIndexNowSingleInfo(int pageStart,int pageSize,PlatformEnum platformType) throws Exception {
		if(pageStart == 0){
			pageStart = Integer.parseInt(TempleConstant.CMS_START_PAGE);
		}
		if(pageSize == 0){
			pageSize = Integer.parseInt(TempleConstant.CMS_PAGE_SIZE);
		}
		
		CmsTopicSimpleQuery paramCmsTopicSimpleQuery = new CmsTopicSimpleQuery();
    	paramCmsTopicSimpleQuery.setPageId(pageStart);
    	paramCmsTopicSimpleQuery.setPageSize(pageSize);
    	paramCmsTopicSimpleQuery.setPlatformType(platformType.ordinal());
    	PageInfo<TopicDetailDTO> pg = topicService.getTodayAllTopic(paramCmsTopicSimpleQuery);
    	
    	List<TopicDetailDTO> modelst = pg.getRows();
    	
    	List<AppSingleInfoDTO> lst = new ArrayList<AppSingleInfoDTO>();
    	for(int i=0;i<modelst.size();i++){
    		AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    		TopicDetailDTO amode = modelst.get(i);
    		Topic tp = amode.getTopic();
    		
    		appSingleInfoDTO.setDiscount(tp.getDiscount());
    		appSingleInfoDTO.setImageurl(tp.getImageMobile());//旧
    		appSingleInfoDTO.setMobileImage(tp.getMobileImage());//新
    		appSingleInfoDTO.setName(tp.getName());
    		appSingleInfoDTO.setSpecialid(tp.getId());
    		
    		if(amode.getStartTime() != null){
    			appSingleInfoDTO.setStartTime(tp.getStartTime().getTime());
    		}
    		if(amode.getEndTime() != null){
    			appSingleInfoDTO.setEndTime(tp.getEndTime().getTime());
    		}
    		//设置活动是否长期有效
			appSingleInfoDTO.setLastingType(tp.getLastingType());
    		lst.add(appSingleInfoDTO);
    	}
    	
    	PageInfo<AppSingleInfoDTO>  page = new PageInfo<AppSingleInfoDTO>();
    	page.setRecords(pg.getRecords());
    	page.setRows(lst);
    	page.setPage(pageStart);
    	page.setSize(pageSize);
    	
	    return page;
	}

	/**
	 * 单品开团提醒:即给用户发送短信提醒或者邮件提醒
	 * @param userId(用户id) 
	 * @return
	 * @throws Exception
	 */
	/*@Override
	public void remindOpenGroup(Long userId) throws Exception {
		//通过用户id查询用户基本信息
		UserDO userDO = memberInfoService.selectById(userId);
		if(userDO != null){
			String mobile = userDO.getMobile();
			if(!DataUtil.isNvl(mobile)){
				
				//此处暂时是测试，模板需要定义出来，并独立出去
				String message = "您好, {0} 用户已关注此主题活动";  
				MessageFormat messageFormat = new MessageFormat(message);  
				Object[] array = new Object[]{userDO.getNickName()};  
				String value = messageFormat.format(array); 
				
				sendSmsService.sendSms(mobile, value);
			}
		}
		
	}*/
	
	
}
