package com.tp.service.cms;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.cms.TempleConstant;
import com.tp.dao.cms.AdvertiseInfoDao;
import com.tp.dto.cms.temple.HomePageAdData;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.AdvertiseInfo;
import com.tp.service.cms.ISinglePictureTemService;

/**
 * 首页的图片管理
 * @author szy
 *
 */
@Service(value="singlePictureTemService")
public class SinglePictureTemService implements ISinglePictureTemService{
	
	@Autowired
	AdvertiseInfoDao advertiseInfoDao;
	
	/*@Autowired
	DfsDomainUtil dfsDomainUtil;*/
	
	@Autowired
	SwitchBussiesConfigDao switchBussiesConfigDao;
	
	/**
     * 首页-最上面-轮播图
     * @return 广告字符串
     * @throws Exception
     */
	@Override
	public String loadHomePageAdInfo() throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	List<HomePageAdData> homePageAdList = new  ArrayList<HomePageAdData>();
    	//3表示：首页轮播图
    	List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_INDEX_CAROUSER_TEMPLE);
    	//下面是拼装数据
    	for(AdvertiseInfo c:cmsAdvertiseInfoDOList){
    		HomePageAdData ad = new HomePageAdData();
    		
    		/*if(c.getPath() != null && !"".equals(c.getPath())){
    			if(!c.getPath().contains("http")){
            		ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
        		}else{
        			ad.setImageSrc(c.getPath());
        		}
    		}*/
    		if(CmsTempletUtil.isNotNullAndNoHttp(c.getPath())){
    			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
    			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(c.getPath()));
    		}else{
    			ad.setImageSrc(c.getPath());
    		}
    		
    		ad.setLink(CmsTempletUtil.getHttpStr(c.getLink()));
    		/*if(c.getLink() != null && !"".equals(c.getLink())){
    			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
            		ad.setLink("http://"+c.getLink());
        		}else{
        			ad.setLink(c.getLink());
        		}
    		}*/
    		
    		homePageAdList.add(ad);
    	}
    	templateData.put("adList", homePageAdList);
    	//模板加载数据
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/roll.flt",new StringWriter());
	    return str;
	}

	/**
     * 首页-右边-最优惠的加载
     * @return
     * @throws Exception
     */
	@Override
	public String loadPreferentialHtml() throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	List<HomePageAdData> homePageAdList1 = new  ArrayList<HomePageAdData>();
    	List<List<HomePageAdData>> lst = new  ArrayList<List<HomePageAdData>>();
    	//查询数据
    	List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_INDEX_RIGHT_TEMPLE);
    	
    	if(cmsAdvertiseInfoDOList != null){
        	for(int i=0,j=cmsAdvertiseInfoDOList.size();i<j;i++){
        		AdvertiseInfo c = cmsAdvertiseInfoDOList.get(i);
        		cmsAdvertiseInfoDOList.get(i).setLink(CmsTempletUtil.getHttpStr(c.getLink()));
        		/*if(c.getLink() != null && !"".equals(c.getLink())){
        			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
        				cmsAdvertiseInfoDOList.get(i).setLink("http://"+c.getLink());
            		}
        		}*/
        	}
    	}
    	
    	//封装数据
    	lst = getPreAdvert(homePageAdList1, lst, cmsAdvertiseInfoDOList);
    	templateData.put("preList", lst);
    	//模板加载数据
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/preferential.flt",new StringWriter());
	    return str;
	}

	/**
	 * 最后疯抢-右边-最优惠加载
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public String loadRushedPreferentialHtml() throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	List<HomePageAdData> homePageAdList1 = new  ArrayList<HomePageAdData>();
    	List<List<HomePageAdData>> lst = new  ArrayList<List<HomePageAdData>>();
    	//查询数据
    	List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_LASTDATE_RIGHT_TEMPLE);
    	
    	if(cmsAdvertiseInfoDOList != null){
        	for(int i=0,j=cmsAdvertiseInfoDOList.size();i<j;i++){
        		AdvertiseInfo c = cmsAdvertiseInfoDOList.get(i);
        		cmsAdvertiseInfoDOList.get(i).setLink(CmsTempletUtil.getHttpStr(c.getLink()));
        		/*if(c.getLink() != null && !"".equals(c.getLink())){
        			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
        				cmsAdvertiseInfoDOList.get(i).setLink("http://"+c.getLink());
            		}
        		}*/
        	}
    	}
    	
    	//封装数据
    	lst = getPreAdvert(homePageAdList1, lst, cmsAdvertiseInfoDOList);
    	templateData.put("preList", lst);
    	//模板加载数据
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/preferential.flt",new StringWriter());
	    return str;
	}
	
	 /**
     * 读取首页刚进去-弹出层
     * @return 广告字符串
     * @throws Exception
     */
	@Override
	public String loadIndexPicLinkHtml() throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	List<HomePageAdData> homePageAdList = new  ArrayList<HomePageAdData>();
    	//7表示：首页轮播图
    	List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_INDEX_STATPIC_TEMPLE);
    	//下面是拼装数据
    	for(AdvertiseInfo c:cmsAdvertiseInfoDOList){
    		HomePageAdData ad = new HomePageAdData();
        	/*if(!c.getPath().contains("http")){
        		ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
    		}else{
    			ad.setImageSrc(c.getPath());
    		}*/
    		if(CmsTempletUtil.isNotNullAndNoHttp(c.getPath())){
    			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
    			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(c.getPath()));
    		}else{
    			ad.setImageSrc(c.getPath());
    		}
        	
        	ad.setLink(CmsTempletUtil.getHttpStr(c.getLink()));
        	/*if(c.getLink() != null && !"".equals(c.getLink())){
    			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
    				ad.setLink("http://"+c.getLink());
        		}else{
        			ad.setLink(c.getLink());
        		}
    		}*/
        	
        	//保证只有条数据
        	if(homePageAdList.size() < 1){
        		homePageAdList.add(ad);
        	}
        	
    	}
    	templateData.put("adList", homePageAdList);
    	//模板加载数据
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/indexfloat.flt",new StringWriter());
	    return str;
	}
	
	/**
	 * PC端页面的-首页和最后疯抢的右边广告图片-数据封装-公告方法
	 * @param homePageAdList1
	 * @param lst
	 * @param cmsAdvertiseInfoDOList
	 * @return
	 */
	private List<List<HomePageAdData>> getPreAdvert(List<HomePageAdData> homePageAdList1,
			List<List<HomePageAdData>> lst,
			List<AdvertiseInfo> cmsAdvertiseInfoDOList) {
		
    	//下面是拼装数据
    	for(int i=0;i<cmsAdvertiseInfoDOList.size();i++){
    		HomePageAdData ad1 = new HomePageAdData();
    		AdvertiseInfo cmsAdvertDO = cmsAdvertiseInfoDOList.get(i);
//			List<HomePageAdData> retListTemp = homePageAdList1;
    		List<HomePageAdData> retListTemp = new ArrayList<HomePageAdData>();
        	/*if(!cmsAdvertDO.getPath().contains("http")){
        		ad1.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsAdvertDO.getPath()));
    		}else{
    			ad1.setImageSrc(cmsAdvertDO.getPath());
    		}*/
        	
    		if(CmsTempletUtil.isNotNullAndNoHttp(cmsAdvertDO.getPath())){
    			//ad1.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsAdvertDO.getPath()));
    			ad1.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(cmsAdvertDO.getPath()));
    		}else{
    			ad1.setImageSrc(cmsAdvertDO.getPath());
    		}
    		
        	ad1.setLink(CmsTempletUtil.getHttpStr(cmsAdvertDO.getLink()));
    		/*if(cmsAdvertDO.getLink() != null && !"".equals(cmsAdvertDO.getLink())){
    			if(!cmsAdvertDO.getLink().contains("http://") && !cmsAdvertDO.getLink().contains("https://")){
    				ad1.setLink("http://"+cmsAdvertDO.getLink());
        		}else{
        			ad1.setLink(cmsAdvertDO.getLink());
        		}
    		}*/
        	
        	retListTemp.add(ad1);
        	lst.add(retListTemp);	
    	}
    	
		/*int flagStr = 0;
    	//下面是拼装数据
    	for(int i=0;i<cmsAdvertiseInfoDOList.size();i++){
    		HomePageAdData ad1 = new HomePageAdData();
    		CmsAdvertiseInfoDO cmsAdvertDO = cmsAdvertiseInfoDOList.get(i);
    		if(flagStr != cmsAdvertDO.getPosition() && homePageAdList1.size()>0){
    			List<HomePageAdData> retListTemp = homePageAdList1;
    			lst.add(retListTemp);
    			homePageAdList1.clear();
            	if(!cmsAdvertDO.getPath().contains("http")){
            		ad1.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsAdvertDO.getPath()));
        		}else{
        			ad1.setImageSrc(cmsAdvertDO.getPath());
        		}
            	ad1.setLink(cmsAdvertDO.getLink());
            	homePageAdList1.add(ad1);
            	flagStr = cmsAdvertDO.getPosition();
    		}else{
    			flagStr = cmsAdvertDO.getPosition();
    			if(!cmsAdvertDO.getPath().contains("http")){
            		ad1.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsAdvertDO.getPath()));
        		}else{
        			ad1.setImageSrc(cmsAdvertDO.getPath());
        		}
            	ad1.setLink(cmsAdvertDO.getLink());
            	homePageAdList1.add(ad1);
    		}
    	}*/
    	
    	return lst;
	}

	/**
	 * 用户管理-用户登录-图片(一张)
	 * @return 
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public HomePageAdData loadUserIndexLogin() throws Exception {
		HomePageAdData ad = new HomePageAdData();
    	List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.USER_INDEX_LOGINC_PICTURE);
    	if(cmsAdvertiseInfoDOList != null && cmsAdvertiseInfoDOList.size()>0){
    		AdvertiseInfo cd = cmsAdvertiseInfoDOList.get(0);
    		if(cd.getPath() != null && !"".equals(cd.getPath())){
    			/*if(!cd.getPath().contains("http")){
            		ad.setImageSrc(dfsDomainUtil.getFileFullUrl(cd.getPath()));
        		}else{
        			ad.setImageSrc(cd.getPath());
        		}*/
    			
    			if(CmsTempletUtil.isNotNullAndNoHttp(cd.getPath())){
    				//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(cd.getPath()));
    				ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(cd.getPath()));
        		}else{
        			ad.setImageSrc(cd.getPath());
        		}
    			
    			ad.setLink(CmsTempletUtil.getHttpStr(cd.getLink()));
    			/*if(cd.getLink() != null && !"".equals(cd.getLink())){
        			if(!cd.getLink().contains("http://") && !cd.getLink().contains("https://")){
        				ad.setLink("http://"+cd.getLink());
            		}else{
            			ad.setLink(cd.getLink());
            		}
        		}*/
    			
    		}
    	}
    	return ad;
	}

	/**
     * 海淘首页-特卖精选图片
     * @return 海淘特卖精选的html字符串
     * @throws Exception
     */
	@Override
	public String haiTaoLoadSpecialSellAdInfo() throws Exception {
		return null;
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
    	List<AdvertiseInfo>  cmsAdvertiseInfoDOList = advertiseInfoDao.selectDynamic(cmsAdvertiseInfoDO);
		return cmsAdvertiseInfoDOList;
	}

	/**
     * 首页-顶端-大图
     * @return html字符串
     * @throws Exception
     */
	@Override
	public Map<String, HomePageAdData> indexLoadTopicBigSmalAdInfo() throws Exception {
		Map<String, HomePageAdData> templateData = new HashMap<String, HomePageAdData>();
		
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_INDEX_TOPIC_BPICTURE);
		if(cmsAdvertiseInfoDOList != null && cmsAdvertiseInfoDOList.size()>0){
			AdvertiseInfo c = cmsAdvertiseInfoDOList.get(0);
			
			HomePageAdData ad = new HomePageAdData();
    		/*if(c.getPath() != null && !"".equals(c.getPath())){
    			if(!c.getPath().contains("http")){
            		ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
        		}else{
        			ad.setImageSrc(c.getPath());
        		}
    		}*/
			if(CmsTempletUtil.isNotNullAndNoHttp(c.getPath())){
				//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
				ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(c.getPath()));
    		}else{
    			ad.setImageSrc(c.getPath());
    		}
    		ad.setLink(CmsTempletUtil.getHttpStr(c.getLink()));
    		/*if(c.getLink() != null && !"".equals(c.getLink())){
    			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
            		ad.setLink("http://"+c.getLink());
        		}else{
        			ad.setLink(c.getLink());
        		}
    		}*/
    		
    		templateData.put("bigPic", ad);
		}
		
    	
		List<AdvertiseInfo> cmsSmalAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_INDEX_TOPIC_SPICTURE);
		if(cmsSmalAdvertiseInfoDOList != null && cmsSmalAdvertiseInfoDOList.size()>0){
			AdvertiseInfo c = cmsSmalAdvertiseInfoDOList.get(0);
			
			HomePageAdData ad1 = new HomePageAdData();
    		/*if(c.getPath() != null && !"".equals(c.getPath())){
    			if(!c.getPath().contains("http")){
    				ad1.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
        		}else{
        			ad1.setImageSrc(c.getPath());
        		}
    		}*/
			if(CmsTempletUtil.isNotNullAndNoHttp(c.getPath())){
				//ad1.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
				ad1.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(c.getPath()));
    		}else{
    			ad1.setImageSrc(c.getPath());
    		}
    		ad1.setLink(CmsTempletUtil.getHttpStr(c.getLink()));
    		/*if(c.getLink() != null && !"".equals(c.getLink())){
    			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
    				ad1.setLink("http://"+c.getLink());
        		}else{
        			ad1.setLink(c.getLink());
        		}
    		}*/
    		
    		templateData.put("smallPic", ad1);
		}
    	
    	return templateData;
	}

	/**
     * 首页-预留广告位
     * @return html字符串
     * @throws Exception
     */
	@Override
	public String indexLoadAllowAdInfo() throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<HomePageAdData> homePageAdList = new  ArrayList<HomePageAdData>();
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_INDEX_ALLOW_PICTURE);
		//下面是拼装数据
    	for(AdvertiseInfo c:cmsAdvertiseInfoDOList){
    		HomePageAdData ad = new HomePageAdData();
    		/*if(c.getPath() != null && !"".equals(c.getPath())){
    			if(!c.getPath().contains("http")){
            		ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
        		}else{
        			ad.setImageSrc(c.getPath());
        		}
    		}*/
    		if(CmsTempletUtil.isNotNullAndNoHttp(c.getPath())){
    			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
    			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(c.getPath()));
    		}else{
    			ad.setImageSrc(c.getPath());
    		}
    		ad.setLink(CmsTempletUtil.getHttpStr(c.getLink()));
    		/*if(c.getLink() != null && !"".equals(c.getLink())){
    			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
            		ad.setLink("http://"+c.getLink());
        		}else{
        			ad.setLink(c.getLink());
        		}
    		}*/
    		
    		homePageAdList.add(ad);
    	}
    	templateData.put("adList", homePageAdList);
    	//模板加载数据
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/headad.flt",new StringWriter());
	    return str;
	}

	/**
     * 首页-底部广告位
     * @return html字符串
     * @throws Exception
     */
	@Override
	public String indexLoadFootAdInfo() throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<HomePageAdData> homePageAdList = new  ArrayList<HomePageAdData>();
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_INDEX_FOOT_PICTURE);
		//下面是拼装数据
    	for(AdvertiseInfo c:cmsAdvertiseInfoDOList){
    		HomePageAdData ad = new HomePageAdData();
    		/*if(c.getPath() != null && !"".equals(c.getPath())){
    			if(!c.getPath().contains("http")){
            		ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
        		}else{
        			ad.setImageSrc(c.getPath());
        		}
    		}*/
    		if(CmsTempletUtil.isNotNullAndNoHttp(c.getPath())){
    			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
    			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(c.getPath()));
    		}else{
    			ad.setImageSrc(c.getPath());
    		}
    		ad.setLink(CmsTempletUtil.getHttpStr(c.getLink()));
    		/*if(c.getLink() != null && !"".equals(c.getLink())){
    			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
            		ad.setLink("http://"+c.getLink());
        		}else{
        			ad.setLink(c.getLink());
        		}
    		}*/
    		
    		homePageAdList.add(ad);
    	}
    	templateData.put("adList", homePageAdList);
    	//模板加载数据
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/bottomad.flt",new StringWriter());
	    return str;
	}

	/**
     * 首页-右侧广告位
     * @return html字符串
     * @throws Exception
     */
	@Override
	public Map<String,String> indexLoadRingAdInfo() throws Exception {
		Map<String,String> returnData = new HashMap<String,String>();
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<HomePageAdData> homePageAdList = new  ArrayList<HomePageAdData>();
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.CMS_INDEX_RING_PICTURE);
		//下面是拼装数据
    	for(AdvertiseInfo c:cmsAdvertiseInfoDOList){
    		HomePageAdData ad = new HomePageAdData();
    		/*if(c.getPath() != null && !"".equals(c.getPath())){
    			if(!c.getPath().contains("http")){
            		ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
        		}else{
        			ad.setImageSrc(c.getPath());
        		}
    		}*/
    		if(CmsTempletUtil.isNotNullAndNoHttp(c.getPath())){
    			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(c.getPath()));
    			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(c.getPath()));
    		}else{
    			ad.setImageSrc(c.getPath());
    		}
    		ad.setLink(CmsTempletUtil.getHttpStr(c.getLink()));
    		/*if(c.getLink() != null && !"".equals(c.getLink())){
    			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
            		ad.setLink("http://"+c.getLink());
        		}else{
        			ad.setLink(c.getLink());
        		}
    		}*/
    		
    		homePageAdList.add(ad);
    	}
    	if(homePageAdList.size() > 0){
	    	if(null != homePageAdList.get(0)){
	    		templateData.put("ad", homePageAdList.get(0));
	    		templateData.put("adindex", "1");
	    		//模板加载数据
	        	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
	    				templateData,"/rightad.flt",new StringWriter());
	        	returnData.put("1", str);
	    	}
	    	if(homePageAdList.size() > 1){
		    	if(null != homePageAdList.get(1)){
		    		templateData.put("ad", homePageAdList.get(1));
		    		templateData.put("adindex", "2");
		    		//模板加载数据
		        	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
		    				templateData,"/rightad.flt",new StringWriter());
		        	returnData.put("2", str);
		    	}
	    	}
    	
    	}
    	
    	
	    return returnData;
	}
	
}
