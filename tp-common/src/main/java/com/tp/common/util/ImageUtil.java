package com.tp.common.util;

import com.tp.common.vo.Constant;

/**
 * Created by ldr on 2016/1/7.
 */
public class ImageUtil {

    public static String getImgFullUrl(Constant.IMAGE_URL_TYPE urlType ,String key){
        if(urlType == null || key == null || key.trim().isEmpty()){
            return  key;
        }
        return  urlType.url+ key;
    }

    public static String getCMSImgFullUrl(String key){
        return getImgFullUrl(Constant.IMAGE_URL_TYPE.cmsimg,key);
    }
}
