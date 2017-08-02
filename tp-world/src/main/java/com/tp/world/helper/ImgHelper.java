package com.tp.world.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ImageDownUtil;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.util.StringUtil;
import com.tp.util.Base64Util;
import com.tp.util.RandomUtil;

@Service
public class ImgHelper {
	
	private static Logger log = LoggerFactory.getLogger(ImgHelper.class);
	
	public static final String DETAIL_SUFFIX=".jpg";
	public static final String DETAIL_IMG_WIDTH=".jpg?imageView2/2/w/";
	public static final String IMG_TAG = "<img";

	@Autowired
	private PropertiesHelper propertiesHelper;
	
	/**
	 * 给图片进行分辨率处理
	 * @param url
	 * @param imgWidth
	 * @return
	 */
	public static String getImgUrl(String url,ImgEnum.Width imgWidth){
		try{
			if(StringUtil.isBlank(url))return null;
			if(StringUtil.equals(imgWidth, ImgEnum.Width.WIDTH_0))return url;
			return ImageDownUtil.getThumbnail(url, imgWidth.width);
		}catch(Exception ex){
			log.error("[调用ImageDownUtil -getThumbnail  Exception] ={}",ex);
		}
		return url;
	}
	
	/**
	 * 处理App富文本图片格式转换
	 * @param inText
	 * @return
	 */
	public static String replaceImgInHTML(String inText,ImgEnum.Width imgWidth) {
		if(StringUtil.isBlank(inText))return null;
		if(null == imgWidth)return inText;
		return inText.replace(DETAIL_SUFFIX, DETAIL_IMG_WIDTH+imgWidth.width);
	}
	
	/**
	 * 分隔JSON格式的图片
	 * @param jsonImg
	 * @return
	 */
	public static List<String> splitJsonImg(String jsonImg,String flag){
		if(StringUtil.isBlank(jsonImg)) return null;
		String[] imgs = jsonImg.split(flag);
		List<String> strs = new ArrayList<>();
		for(int i = 0;i<imgs.length;i++){
			strs.add(imgs[i]);
		}
		return strs;
	}
	
	/**
	 * 分隔逗号格式的JSON图片字符串
	 * @param jsonImg
	 * @param flag
	 * @return
	 */
	public static List<String> splitJsonImg2DH(String jsonImg){
		return splitJsonImg(jsonImg,",");
	}
	
	/**
     * TODO:将byte数组以Base64方式编码为字符串
     * @param bytes 待编码的byte数组
     * @return 编码后的字符串
     * */
    public String encode(byte[] bytes){
        return Base64Util.encrypt(bytes);
    }
    
    /**
     * TODO:将以Base64方式编码的字符串解码为byte数组
     * @param encodeStr 待解码的字符串
     * @return 解码后的byte数组
     * @throws IOException 
     * */
    public byte[] decode(String encodeStr){
        byte[] bt = null;  
        bt = Base64Util.decrypt(encodeStr);
        return bt;
    }
    
    /**
     * TODO:将两个byte数组连接起来后，返回连接后的Byte数组
     * @param front 拼接后在前面的数组
     * @param after 拼接后在后面的数组
     * @return 拼接后的数组
     * */
    public byte[] connectBytes(byte[] front, byte[] after){
        byte[] result = new byte[front.length + after.length];
        System.arraycopy(front, 0, result, 0, after.length);
        System.arraycopy(after, 0, result, front.length, after.length);
        return result;
    }
    
    /**
     * TODO:将图片以Base64方式编码为字符串
     * @param imgUrl 图片的绝对路径（例如：D:\\jsontest\\abc.jpg）
     * @return 编码后的字符串
     * @throws IOException 
     * */
    public String encodeImage(String imgUrl){
        FileInputStream fis = null;
        byte[] rs = null;
		try {
			fis = new FileInputStream(imgUrl);
			rs = new byte[fis.available()];
			fis.read(rs);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			log.error("图片文件没有找到={}",imgUrl);
			throw new MobileException(MResultInfo.IMAGE_DECODE_ERROR);
		} catch (IOException e) {
			log.error(e.getMessage());
			log.error("图片文件加载出错={}",imgUrl);
			throw new MobileException(MResultInfo.IMAGE_DECODE_ERROR);
		} finally {
			try {
				if(null != fis){
					fis.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
				log.error("图片文件加载出错={}",imgUrl);
				throw new MobileException(MResultInfo.IMAGE_DECODE_ERROR);
			}
		}
        return encode(rs);
    }
    
    
   /**
    * 字节流解码成文件
    * @param inStream
    * @return
    */
   public File decodeImage(byte[] inStream){
	   String fileName = System.currentTimeMillis()+RandomUtil.createRadom(6, 2);
	   String path = propertiesHelper.uploadTmpPath +fileName +".jpg";
	   File outFile = new File(path);
	   if(outFile.length() > 2*1024*1024 ){
		   if(log.isInfoEnabled()){
			   log.info("==========上传图片大小超过2M=========="+outFile.length()+"Bytes");
		   }
		   throw new MobileException(MResultInfo.IMAGE_MAX_ERROR);
	   }
	   FileOutputStream outStream = null;
	try {
		outStream = new FileOutputStream(outFile);
		byte[] inOutb = inStream;
		//写出流
		outStream.write(inOutb);
	} catch (FileNotFoundException e) {
		log.error("图片文件解码出错={}",e.getMessage());
		throw new MobileException(MResultInfo.IMAGE_DECODE_ERROR);
	} catch (IOException e) {
		log.error("图片文件解码出错={}",e.getMessage());
		throw new MobileException(MResultInfo.IMAGE_DECODE_ERROR);
	} finally { 
		if(null != outStream){
			try {
				outStream.close();
			} catch (IOException e) {
				log.error("图片文件解码关闭流文件出错={}",e.getMessage());
				throw new MobileException(MResultInfo.IMAGE_DECODE_ERROR);
			}
		}
	}
	   return outFile;
   }
}
