package com.tp.service.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.dfsutils.util.DfsDomainUtil;

@Component(value = "switchBussiesConfigDao")
public class SwitchBussiesConfigDao {

	/** PC图片上传开关 **/
	/*@Value("${pc.switch.picsize.opr}")
	private String pcSwitchPicSizeOpr;*/
	
	/** APP图片上传开关 **/
	/*@Value("${app.switch.picsize.opr}")
	private String appSwitchPicSizeOpr;*/
	
	@Autowired
	DfsDomainUtil dfsDomainUtil;
	
	private final static Log logger = LogFactory.getLog(SwitchBussiesConfigDao.class);
	
	/**
     * 获取图片地址，按比例压缩(PC)
     * @return
     */
    public String getPictureSrc_PC(String picSrc,Integer size){
    	try {
			/*if(Boolean.parseBoolean(pcSwitchPicSizeOpr.trim())){
				return dfsDomainUtil.getSnapshotUrl(picSrc,size);
			}*/
    		return dfsDomainUtil.getSnapshotUrl(picSrc,size);
		} catch (Exception e) {
			logger.error("获取图片地址，按比例压缩(PC)报错", e);
			e.printStackTrace();
		}
    	return picSrc;
    }
    
    /**
     * 获取图片地址，按比例压缩(APP)
     * @return
     */
    /*public String getPictureSrc_APP(String picSrc,Integer size){
    	try {
			if(Boolean.parseBoolean(appSwitchPicSizeOpr.trim())){
				return dfsDomainUtil.getSnapshotUrl(picSrc,size);
			}
		} catch (Exception e) {
			logger.error("获取图片地址，按比例压缩(APP)报错", e);
			e.printStackTrace();
		}
		return picSrc;
    }*/
    
    /**
     * 获取图片地址，直接取原图(PC)
     * @return
     */
    public String getFullPictureSrc_PC(String picSrc){
    	try {
			/*if(Boolean.parseBoolean(pcSwitchPicSizeOpr.trim())){
				return dfsDomainUtil.getFileFullUrl(picSrc);
			}*/
    		return dfsDomainUtil.getFileFullUrl(picSrc);
		} catch (Exception e) {
			logger.error("获取图片地址，直接取原图(PC)报错", e);
			e.printStackTrace();
		}
		return picSrc;
    }
    
    /**
     * 获取图片地址，直接取原图(APP)
     * @return
     */
    /*public String getFullPictureSrc_APP(String picSrc){
    	try {
			if(Boolean.parseBoolean(appSwitchPicSizeOpr.trim())){
				return dfsDomainUtil.getFileFullUrl(picSrc);
			}
		} catch (Exception e) {
			logger.error("获取图片地址，直接取原图(APP)报错", e);
			e.printStackTrace();
		}
		return picSrc;
    }*/
    
}
