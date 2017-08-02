package com.tp.service.cms;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.tp.common.vo.cms.TempleConstant;
import com.tp.util.DateUtil;


public class CmsTempletUtil {

	/**
     * 根据价格计算折扣信息
     * @param priceNow 现价
     * @param priceOld 原价
     * @return
     */
    public static String getPriceDiscount(double priceNow, double priceOld){
    	return Math.round(priceNow/priceOld*100)/10.0 == 10 ?"":Math.round(priceNow/priceOld*100)/10.0+"折";
    }
    
    /**
     * 建立item的链接地址:商品地址
     * @param activityId 活动id
     * @param skuId	skuid
     * @return
     */
    public static String splitJoinLinkAdress(String cmsChaimedAdress,Long activityId,String skuId){
    	String str = activityId+"-"+skuId;
    	return cmsChaimedAdress.replaceAll("parames", str);
    }
    
    /**
     * 建立活动topic的链接地址:活动地址
     * @param activityId 活动id
     * @param skuId	skuid
     * @return
     */
    public static String splitJoinTopicAdress(String cmsTopicAdress,Long activityId){
    	return cmsTopicAdress.replaceAll("parames", activityId.toString());
    }
    
    /**
     * 通过国家的名称去匹配国家的img地址
     * @param countryName 
     * @param countryImg
     * @return
     */
    public static String getCountryImg(String countryName){
    	String str = "";
    	if(countryName != null){
    		if("日本".equals(countryName)){
    			str = TempleConstant.CMS_IMG_JAPAN;
    		}else if("英国".equals(countryName)){
    			str = TempleConstant.CMS_IMG_ENGLAND;
    		}else if("德国".equals(countryName)){
    			str = TempleConstant.CMS_IMG_GERMANY;
    		}else if("美国".equals(countryName)){
    			str = TempleConstant.CMS_IMG_AMERICA;
    		}else if("韩国".equals(countryName)){
    			str = TempleConstant.CMS_IMG_KOREA;
    		}else if("澳大利亚".equals(countryName)){
    			str = TempleConstant.CMS_IMG_AUSTRALIA;
    		}
    	}
    	return str;
    }

    /**
     * 通过日期，判断是否明天，后天，或者星期
     * @param countryName 
     * @param countryImg
     * @return
     */
    public static String getWeekName(String dateStr,Integer dateNum){
    	String str = "";
    	
    	//先判断是否为明天和后天
    	Date tomorDate = DateUtil.getDateAfterDays(1);
    	if(dateStr.equals(DateUtil.formatDate(tomorDate))){
    		return "明天";
    	}
    	Date hoiuDate = DateUtil.getDateAfterDays(2);
    	if(dateStr.equals(DateUtil.formatDate(hoiuDate))){
    		return "后天";
    	}
    	
    	//如果不是明天为后天就直接显示星期几
    	if(dateNum == 1){
    		str="星期天";
    	}else if(dateNum == 2){
    		str="星期一";
    	}else if(dateNum == 3){
    		str="星期二";
    	}else if(dateNum == 4){
    		str="星期三";
    	}else if(dateNum == 5){
    		str="星期四";
    	}else if(dateNum == 6){
    		str="星期五";
    	}else if(dateNum == 7){
    		str="星期六";
    	}
    	return str;
    }
    
    /**
     * 把日期格式2015-04-17转化为 04/19
     * @return
     */
    public static String getDateStr(String dateStr){
    	if(dateStr != null){
    		return dateStr.substring(5, 10).replaceAll("-", "/");
    	}else{
    		return "";
    	}
    }
    
    /**
     * 把链接拼接上http
     * @return
     */
    public static String getHttpStr(String str){
    	if(!StringUtils.isEmpty(str) && !str.contains("http://") && !str.contains("https://")){
    		str = "http://"+str;
		}
    	return str;
    }
    
    /**
     * 判断字符串是否包含http,如果不为空，且不包含http的，返回true；为空或者包含http的，返回false
     * @return
     */
    public static boolean isNotNullAndNoHttp(String str){
    	if(!StringUtils.isEmpty(str) && !str.contains("http")){
    		return true;
		}
    	return false;
    }
}
