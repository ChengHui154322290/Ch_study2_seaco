package com.tp.service.cms.app; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.cms.AdvertTypeAPPEnum;
import com.tp.common.vo.cms.TempleConstant;
import com.tp.dao.cms.AdvertiseInfoDao;
import com.tp.dto.cms.app.AppAdvertiseInfoDTO;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.model.cms.AdvertiseInfo;
import com.tp.service.cms.app.IAdvertiseAppService;

/**
* APP的图片管理 Service
*/
@Service(value="advertiseAppService")
public class AdvertiseAppService  implements IAdvertiseAppService{

	@Autowired
	AdvertiseInfoDao advertiseInfoDao;
	
	/*@Autowired
	DfsDomainUtil dfsDomainUtil;*/
	
	private final static Log logger = LogFactory.getLog(AdvertiseAppService.class);
	
	/**
	 * 首页-广告位：给出图片地址，图片链接，以及图片所关联的活动ID
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppIndexAdvertReturnData queryIndexAdvertiseInfo() throws Exception{
		AppIndexAdvertReturnData returnDate = new AppIndexAdvertReturnData();
		//查询数据
    	List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_INDEX_ADVERT_TYPE);
    	//封装数据
    	List<AppAdvertiseInfoDTO<Object>> appAdvertiseInfoDTO = new ArrayList<AppAdvertiseInfoDTO<Object>>();
    	for(int i=0;i<cmsAdvertiseInfoDOList.size();i++){
    		AdvertiseInfo ssmode = cmsAdvertiseInfoDOList.get(i);
    		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    		//图片地址(app那边他们自己拼接，所以不需要这边加图片地址前缀)
    		/*if(!ssmode.getPath().contains("http")){
    			csmode.setImageurl(dfsDomainUtil.getFileFullUrl(ssmode.getPath()));
    		}else{
    			csmode.setImageurl(ssmode.getPath());
    		}*/
    		csmode.setImageurl(ssmode.getPath());
    		//链接
    		csmode.setLinkurl(ssmode.getLink());
    		//商品id
    		csmode.setProductid(ssmode.getProductid());
    		//sku
    		csmode.setSku(ssmode.getSku());
    		//活动类型
    		csmode.setType(ssmode.getActtype());
    		
    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(ssmode.getSku());
    			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
    			if(ssmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(ssmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}
    		
    		appAdvertiseInfoDTO.add(csmode);
    	}
    	
    	returnDate.setCount(cmsAdvertiseInfoDOList.size());
    	returnDate.setUrls(appAdvertiseInfoDTO);
		
		return returnDate ;
	}
	
	/**
	 * 首页-功能标签信息：给出图片地址，图片链接
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppIndexAdvertReturnData queryIndexFunlabInfo() throws Exception{
		AppIndexAdvertReturnData returnDate = new AppIndexAdvertReturnData();
		//查询数据
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_INDEX_FUNCTION_TYPE);
    	//封装数据
    	List<AppAdvertiseInfoDTO<Object>> appAdvertiseInfoDTO = new ArrayList<AppAdvertiseInfoDTO<Object>>();
    	for(int i=0;i<cmsAdvertiseInfoDOList.size();i++){
    		AdvertiseInfo ssmode = cmsAdvertiseInfoDOList.get(i);
    		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    		//图片路径src
    		/*if(!ssmode.getPath().contains("http")){
    			csmode.setImageurl(dfsDomainUtil.getFileFullUrl(ssmode.getPath()));
    		}else{
    			csmode.setImageurl(ssmode.getPath());
    		}*/
    		csmode.setImageurl(ssmode.getPath());
    		//图片链接
    		csmode.setLinkurl(ssmode.getLink());
    		//类型
    		csmode.setType(ssmode.getActtype());
    		//图片标题
    		csmode.setName(ssmode.getAdvertname());
    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(ssmode.getSku());
    			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
    			if(ssmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(ssmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}
    		appAdvertiseInfoDTO.add(csmode);
    	}
    	
    	returnDate.setCount(cmsAdvertiseInfoDOList.size());
    	returnDate.setTables(appAdvertiseInfoDTO);
		
		return returnDate ;
	}
	
	/**
	 * 秒杀-广告位信息：图片src ，link ，图片所关联的商品id。
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppAdvertiseInfoDTO<Object> querySecKillAdvertiseInfo() throws Exception{
		//查询数据
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_SECKILL_ADVERT_TYPE);
		
		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
		
		if(cmsAdvertiseInfoDOList!=null && cmsAdvertiseInfoDOList.size()>0){
			AdvertiseInfo advertmode = cmsAdvertiseInfoDOList.get(0);
			/*if(!advertmode.getPath().contains("http")){
    			csmode.setImageurl(dfsDomainUtil.getFileFullUrl(advertmode.getPath()));
    		}else{
    			csmode.setImageurl(advertmode.getPath());
    		}*/
			csmode.setImageurl(advertmode.getPath());
			
			//活动类型
    		csmode.setType(advertmode.getActtype());
    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(advertmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(advertmode.getSku());
    			appSingleInfoDTO.setSpecialid(advertmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(advertmode.getActtype())){
    			if(advertmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(advertmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(advertmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}
    		
			csmode.setLinkurl(advertmode.getLink());
			csmode.setProductid(advertmode.getProductid());
		}
		return csmode;
	}
	
	/**
	 * 海淘-广告位信息：图片src ，link ，图片所关联的商品id。
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppAdvertiseInfoDTO<Object> queryGrainAdvertiseInfo() throws Exception{
		//查询数据
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_GRAIN_ADVERT_TYPE);
		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    	if(cmsAdvertiseInfoDOList!=null && cmsAdvertiseInfoDOList.size()>0){
    		AdvertiseInfo ssmode = cmsAdvertiseInfoDOList.get(0);
    		/*if(!ssmode.getPath().contains("http")){
    			csmode.setImageurl(dfsDomainUtil.getFileFullUrl(ssmode.getPath()));
    		}else{
    			csmode.setImageurl(ssmode.getPath());
    		}*/
    		csmode.setImageurl(ssmode.getPath());
    		
    		//活动类型
    		csmode.setType(ssmode.getActtype());
    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(ssmode.getSku());
    			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
    			if(ssmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(ssmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}
    		
    		csmode.setLinkurl(ssmode.getLink());
    		csmode.setProductid(ssmode.getProductid());
    	}
		return csmode ;
	}
	
	/**
	 * 广告-启动页面：图片名称 url  link  商品id  启动时间。
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppAdvertiseInfoDTO<Object> queryAdvertPulseInfo() throws Exception{
		//查询数据
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_PULSE_ADVERT_TYPE);
		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    	if(cmsAdvertiseInfoDOList!=null && cmsAdvertiseInfoDOList.size()>0){
    		AdvertiseInfo ssmode = cmsAdvertiseInfoDOList.get(0);
    		/*if(!ssmode.getPath().contains("http")){
    			csmode.setImageurl(dfsDomainUtil.getFileFullUrl(ssmode.getPath()));
    		}else{
    			csmode.setImageurl(ssmode.getPath());
    		}*/
    		csmode.setImageurl(ssmode.getPath());
    		csmode.setLinkurl(ssmode.getLink());
    		csmode.setProductid(ssmode.getProductid());
    		csmode.setName(ssmode.getAdvertname());
    		csmode.setTime(ssmode.getTime());
    		
    		//活动类型
    		csmode.setType(ssmode.getActtype());
    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(ssmode.getSku());
    			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
    			if(ssmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(ssmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}
    		
    	}
		return csmode ;
	}
	
	/**
	 * 广告-支付成功：图片url  link  商品id
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppAdvertiseInfoDTO<Object> queryAdvertPayInfo() throws Exception{
		//查询数据
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_PAY_ADVERT_TYPE);
		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    	if(cmsAdvertiseInfoDOList!=null && cmsAdvertiseInfoDOList.size()>0){
    		AdvertiseInfo ssmode = cmsAdvertiseInfoDOList.get(0);
    		/*if(!ssmode.getPath().contains("http")){
    			csmode.setImageurl(dfsDomainUtil.getFileFullUrl(ssmode.getPath()));
    		}else{
    			csmode.setImageurl(ssmode.getPath());
    		}*/
    		csmode.setImageurl(ssmode.getPath());
    		
    		//活动类型
    		csmode.setType(ssmode.getActtype());
    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(ssmode.getSku());
    			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
    			if(ssmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(ssmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}
    		
    		csmode.setLinkurl(ssmode.getLink());
    		csmode.setProductid(ssmode.getProductid());
    	}
		return csmode ;
	}
	
	/**
	 * wap-首页弹框：图片url 名称 类型 管理信息
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppAdvertiseInfoDTO<Object> queryWapAdvertInfo() throws Exception{
		//查询数据
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_WAPINDEX_ADVERT_TYPE);
		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    	if(cmsAdvertiseInfoDOList!=null && cmsAdvertiseInfoDOList.size()>0){
    		AdvertiseInfo ssmode = cmsAdvertiseInfoDOList.get(0);
    		/*if(!ssmode.getPath().contains("http")){
    			csmode.setImageurl(dfsDomainUtil.getFileFullUrl(ssmode.getPath()));
    		}else{
    			csmode.setImageurl(ssmode.getPath());
    		}*/
    		csmode.setImageurl(ssmode.getPath());
    		csmode.setName(ssmode.getLink());
    		csmode.setProductid(ssmode.getProductid());
    		csmode.setType(ssmode.getActtype());
    		
    		//活动类型
    		csmode.setType(ssmode.getActtype());
    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(ssmode.getSku());
    			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
    			if(ssmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(ssmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}
    		
    	}
		return csmode ;
	}
	
	/**
	 * wap-今日精选：图片url 名称 
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppAdvertiseInfoDTO<Object> queryWapChosenHeadPic() throws Exception {
		//查询数据
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_WAP_CHOSEN_PICTURE);
		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    	if(cmsAdvertiseInfoDOList!=null && cmsAdvertiseInfoDOList.size()>0){
    		AdvertiseInfo ssmode = cmsAdvertiseInfoDOList.get(0);
    		/*if(!ssmode.getPath().contains("http")){
    			csmode.setImageurl(dfsDomainUtil.getFileFullUrl(ssmode.getPath()));
    		}else{
    			csmode.setImageurl(ssmode.getPath());
    		}*/
    		csmode.setImageurl(ssmode.getPath());
    		csmode.setLinkurl(ssmode.getLink());
    	}
		return csmode ;
	}
	
	
	/**
	 * 调用查询图片信息，参数为传入图片类型
	 * @param type
	 * @return
	 * @throws DAOException
	 */
	private List<AdvertiseInfo> queryAdvertBase(String type) throws Exception {
		AdvertiseInfo cmsAdvertiseInfoDO = new AdvertiseInfo();
		cmsAdvertiseInfoDO.setIdent(type);
    	//查询添加当前日期
    	cmsAdvertiseInfoDO.setStartdate(new Date());
    	cmsAdvertiseInfoDO.setEnddate(new Date());
    	cmsAdvertiseInfoDO.setStatus("0");
    	cmsAdvertiseInfoDO.setPlatformType(Integer.valueOf(TempleConstant.CMS_PLATFORM_APP_TYPE));
    	List<AdvertiseInfo>  cmsAdvertiseInfoDOList = advertiseInfoDao.selectDynamic(cmsAdvertiseInfoDO);
		return cmsAdvertiseInfoDOList;
	}

	
}
