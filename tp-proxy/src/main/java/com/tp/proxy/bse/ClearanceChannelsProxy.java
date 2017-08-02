package com.tp.proxy.bse;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ClearanceChannels;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IClearanceChannelsService;
/**
 * 通关渠道信息代理层
 * @author szy
 *
 */
@Service
public class ClearanceChannelsProxy extends BaseProxy<ClearanceChannels>{

	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;

	@Override
	public IBaseService<ClearanceChannels> getService() {
		return clearanceChannelsService;
	}

	/**
	 * 添加
	 * @param clearanceChannels
	 */
	public ResultInfo<?> addClearanceChannel(ClearanceChannels clearanceChannels){
		String name = clearanceChannels.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("请输入名称"));
		}
		String code = clearanceChannels.getCode();
		if(StringUtils.isBlank(code)){
			return new ResultInfo<>(new FailInfo("请输入通关渠道编码"));
		}
		
		ClearanceChannels channels =new ClearanceChannels();
		channels.setCode(clearanceChannels.getCode().trim());
		List<ClearanceChannels> list = clearanceChannelsService.queryByObject(channels);
		if(CollectionUtils.isNotEmpty(list)){
			return new ResultInfo<>(new FailInfo("存在相同通关渠道编码,请更换一个"));
		}
		ClearanceChannels channelsDO2 =new ClearanceChannels();
		channelsDO2.setName(clearanceChannels.getName().trim());
		List<ClearanceChannels> list2 = clearanceChannelsService.queryByObject(channelsDO2);
		if(CollectionUtils.isNotEmpty(list2)){
			return new ResultInfo<>(new FailInfo("存在相同名称,请更换一个"));
		}
		try {
			forbiddenWordsProxy.checkForbiddenWordsField(clearanceChannels.getRemark(),"备注");
		} catch (Exception exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,clearanceChannels);
			return new ResultInfo<>(failInfo);
		}
		ClearanceChannels insertClearanceChannel=new ClearanceChannels();
		insertClearanceChannel.setCode(code.trim());
		insertClearanceChannel.setName(name.trim());
		insertClearanceChannel.setRemark(clearanceChannels.getRemark().trim());
		insertClearanceChannel.setStatus(clearanceChannels.getStatus());
		insertClearanceChannel.setCreateTime(new Date());
		insertClearanceChannel.setModifyTime(new Date());
		clearanceChannelsService.insert(insertClearanceChannel);
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	/**
	 * 
	 * <pre>
	 * 更新通关渠道
	 * </pre>
	 *
	 * @param clearanceChannels
	 * @param isAllField
	 */
	public ResultInfo<?> updateClearanceChannel(ClearanceChannels clearanceChannels, Boolean isAllField) throws Exception {
		String name = clearanceChannels.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("请输入名称"));
		}
		ClearanceChannels channelsDO2 =new ClearanceChannels();
		channelsDO2.setName(clearanceChannels.getName().trim());
		List<ClearanceChannels> list2 = clearanceChannelsService.queryByObject(channelsDO2);
		if(CollectionUtils.isNotEmpty(list2)){
			 for(ClearanceChannels clerDO:list2){
			        Long id = clerDO.getId();
			        if(!id.equals(clearanceChannels.getId())){
			           return new ResultInfo<>(new FailInfo("存在相同名称"));
			        }
			    }  
		}
		try {
			forbiddenWordsProxy.checkForbiddenWordsField(clearanceChannels.getRemark(),"备注");
		} catch (Exception exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,clearanceChannels);
			return new ResultInfo<>(failInfo);
		}
		ClearanceChannels insertClearanceChannel=new ClearanceChannels();
		insertClearanceChannel.setId(clearanceChannels.getId());
		insertClearanceChannel.setName(name.trim());
		insertClearanceChannel.setRemark(clearanceChannels.getRemark().trim());
		insertClearanceChannel.setStatus(clearanceChannels.getStatus());
		insertClearanceChannel.setModifyTime(new Date());
		if(isAllField){
			clearanceChannelsService.updateById(insertClearanceChannel);
		}else{
			clearanceChannelsService.updateNotNullById(insertClearanceChannel);
		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	public List<ClearanceChannels> getAllClearanceChannelsByStatus(
			int channelStatusValid) {
		// TODO Auto-generated method stub
		return clearanceChannelsService.getAllClearanceChannelsByStatus(channelStatusValid);
	}
}
