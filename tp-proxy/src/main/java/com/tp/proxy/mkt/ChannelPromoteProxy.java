package com.tp.proxy.mkt;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.mkt.ChannelPromote;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mkt.IChannelPromoteService;
import com.tp.service.mkt.IQrcodeService;
import com.tp.util.StringUtil;
/**
 * 渠道推广代理层
 * @author szy
 *
 */
@Service
public class ChannelPromoteProxy extends BaseProxy<ChannelPromote>{

	@Autowired
	private IChannelPromoteService channelPromoteService;
	
	@Autowired
	private IQrcodeService qrcodeService;

	@Override
	public IBaseService<ChannelPromote> getService() {
		return channelPromoteService;
	}
	
	public ResultInfo<ChannelPromote> save(ChannelPromote channelPromote){
		try{
			if(StringUtil.isBlank(channelPromote.getUniqueId()))throw new ServiceException("用户唯一标识不能为空");
			if(StringUtil.isBlank(channelPromote.getChannel()))throw new ServiceException("渠道不能为空");
			//检查用户是否已经扫过二维码 是不做操作 否添加操作
			ChannelPromote cp = new ChannelPromote();
			cp.setUniqueId(channelPromote.getUniqueId());
			cp = channelPromoteService.queryUniqueByObject(cp);
			if(cp == null){
				return insert(channelPromote);
			}
			return new ResultInfo<>(channelPromote);
		}catch(ServiceException se){
			return new ResultInfo<>(new FailInfo(se.getMessage()));
		}catch(Exception se){
			return new ResultInfo<>(new FailInfo("操作失败"));
		}
	} 
	
	/**
	 * 保存渠道 并返回渠道二维码
	 * @param channel
	 * @param type
	 * @return
	 */
	public String saveChannel(String channel,int type,String actionName,String scenePrefix){
		//检查渠道信息是否已经存在，否则增加一条渠道信息
		ChannelPromote cp = channelPromoteService.checkChannel2Exist(channel, type);
		String qrcode = StringUtil.EMPTY;
		if(null == cp.getId()){
			//生成二维码
			if(StringUtil.isNotBlank(actionName)){
				String scene = scenePrefix +channel+"_"+type;
				qrcode = qrcodeService.createScanByWX(scene, actionName);
				if(StringUtil.isNotBlank(qrcode))cp.setQrcode(qrcode);
			}
			cp.setCreateTime(new Date());
			channelPromoteService.insert(cp);
		}else{
			if(StringUtil.isBlank(cp.getQrcode())){ //如果已经存在的渠道二维码没有值 则生成并插入
				if(StringUtil.isNotBlank(actionName)){
					String scene = scenePrefix +channel+"_"+type;
					qrcode = qrcodeService.createScanByWX(scene, actionName);
					if(StringUtil.isNotBlank(qrcode))cp.setQrcode(qrcode);
				}
				channelPromoteService.updateNotNullById(cp);
			}else qrcode = cp.getQrcode();
		}
		return qrcode;
	}
//	public String saveChannelNew(String channel,int type,String actionName,String scenePrefix,int choise){
//		//检查渠道信息是否已经存在，否则增加一条渠道信息
//		ChannelPromote cp = channelPromoteService.checkChannel2Exist(channel, type);
//		String qrcode = StringUtil.EMPTY;
//		if(null == cp.getId()){
//			//生成二维码
//			if(StringUtil.isNotBlank(actionName)){
//				String scene = scenePrefix +channel+"_"+type;
//				qrcode = qrcodeService.createScanByWXNew(scene, actionName,choise);
//				if(StringUtil.isNotBlank(qrcode))cp.setQrcode(qrcode);
//			}
//			cp.setCreateTime(new Date());
//			channelPromoteService.insert(cp);
//		}else{
//			if(StringUtil.isBlank(cp.getQrcode())){ //如果已经存在的渠道二维码没有值 则生成并插入
//				if(StringUtil.isNotBlank(actionName)){
//					String scene = scenePrefix +channel+"_"+type;
//					qrcode = qrcodeService.createScanByWXNew(scene, actionName,choise);
//					if(StringUtil.isNotBlank(qrcode))cp.setQrcode(qrcode);
//				}
//				channelPromoteService.updateNotNullById(cp);
//			}else qrcode = cp.getQrcode();
//		}
//		return qrcode;
//	}
	
	public ResultInfo<PageInfo<ChannelPromote>> statisticChannelPromote(Map<String, Object> params,PageInfo<ChannelPromote> info){
		try{
			PageInfo<ChannelPromote> data = channelPromoteService.statisticChannelPromote(params,info);
			return new ResultInfo<>(data);
		}catch(Exception e){
			return new ResultInfo<>(new FailInfo("操作失败"));
		}
	}
	
	public ResultInfo<Integer> updateIsFollowList(){
		try{
			Integer r = channelPromoteService.updateIsFollowList();
			if(r != null && r != 0)return new ResultInfo<>(r);
			return new ResultInfo<>(new FailInfo("没有可同步数据"));
		}catch(ServiceException s){
			return new ResultInfo<>(new FailInfo(s.getMessage()));
		} catch(Exception s){
			return new ResultInfo<>(new FailInfo("同步失败"));
		} 
	} 
//	public ResultInfo<Integer> updateIsFollowListNew(Integer choise){
//		try{
//			Integer r = channelPromoteService.updateIsFollowListNew(choise);
//			if(r != null && r != 0)return new ResultInfo<>(r);
//			return new ResultInfo<>(new FailInfo("没有可同步数据"));
//		}catch(ServiceException s){
//			return new ResultInfo<>(new FailInfo(s.getMessage()));
//		} catch(Exception s){
//			return new ResultInfo<>(new FailInfo("同步失败"));
//		} 
//	} 
}
