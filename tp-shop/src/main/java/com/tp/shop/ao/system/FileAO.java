package com.tp.shop.ao.system;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiniu.QiniuUpload;
import com.qiniu.http.Response;
import com.tp.common.vo.Constant;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.vo.system.UploadVO;
import com.tp.shop.helper.ImgHelper;

/**
 * 文件管理业务层
 * @author zhuss
 */
@Service
public class FileAO {

	private static Logger log = LoggerFactory.getLogger(FileAO.class);
	
	@Autowired
	private ImgHelper imgHelper;
	
	@Autowired
	private QiniuUpload uploader;
	
	/**
	 * 上传图片
	 * @param position
	 * @return
	 */
	public MResultVO<UploadVO> uploadImg(String imgStream){
		try{
			byte[] out = imgHelper.decode(imgStream);
			File localFile = imgHelper.decodeImage(out);
			Response response= uploader.uploadFile(localFile.getPath(), localFile.getName(),Constant.IMAGE_URL_TYPE.cmsimg.name());
			//String path = uploader.upload(out, Constant.IMAGE_URL_TYPE.cmsimg.name(), ImgHelper.DETAIL_SUFFIX);
			String path = Constant.IMAGE_URL_TYPE.cmsimg.url+localFile.getName();
			//删除本地临时文件
			if (localFile.isFile() && localFile.exists()) {  
				localFile.delete();
			} 
			if(response.isOK()){
				return new MResultVO<>(MResultInfo.IMAGE_UPLOAD_SUCCESS,new UploadVO(path));
			}
			return new MResultVO<>(MResultInfo.IMAGE_UPLOAD_FAILED);
		}catch(Exception e){
			log.error("[API接口 - 上传图片 Exception] = {}",e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	} 
}
