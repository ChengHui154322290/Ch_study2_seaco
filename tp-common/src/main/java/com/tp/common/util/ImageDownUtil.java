package com.tp.common.util;

import org.apache.commons.lang3.StringUtils;

import com.tp.common.vo.Constant.IMAGE_SIZE;

/**
 * 图片路径
 * @author szy
 *
 */
public final class ImageDownUtil {
	/**
	 * 显示模式
	 * @author szy
	 *
	 */
	public enum IMAGE_SHOW_TYPE{
		type0(0),
		type1(1),
		type2(2),
		type3(3),
		type4(4),
		type5(5);
		public Integer code;
		IMAGE_SHOW_TYPE(Integer code){
			this.code = code;
		}
	}
	/**
	 * 获取原图
	 * @param domainPath
	 * @param imageName
	 * @return
	 */
	public static String getOriginalImage(final String domainPath,final String imageName){
		return domainPath+imageName;
	}
	
	public static String getThumbnail(final String domainPath,final String imageName,final IMAGE_SIZE showModel){
		return domainPath+imageName+"?imageView2/"+IMAGE_SHOW_TYPE.type1.code+"/w/"+showModel.width+"/h/"+showModel.height;
	}
	
	public static String getThumbnail(final String domainPath,final String imageName,final IMAGE_SIZE showModel,final Integer type){
		return domainPath+imageName+"?imageView2/"+type+"/w/"+showModel.width+"/h/"+showModel.height;
	}

	public static String getThumbnail(final String url,final Integer width){
		return url+"?imageView2/"+0+"/w/"+width;
	}
	
	/**
	 * 根据图片全路径，截取得到图片名称
	 * @param imageUrl
	 * @return
	 */
	public static String substringImageUrl(String imageUrl){
		if(StringUtils.isBlank(imageUrl)) return imageUrl;
		try{
			imageUrl=imageUrl.split("\\?")[0];
			int index=imageUrl.lastIndexOf("/");
			if(index==-1)return imageUrl;
			return imageUrl.substring(imageUrl.lastIndexOf("/"));
		}catch(Throwable exception){
			exception.printStackTrace();
		}
		return imageUrl;
		
	}
}
