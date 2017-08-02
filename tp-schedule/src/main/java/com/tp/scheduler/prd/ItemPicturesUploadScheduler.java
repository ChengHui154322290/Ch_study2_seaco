/**  
 * Project Name:xg-schedule  
 * File Name:ItemPicturesUploadScheduler.java  
 * Package Name:com.tp.scheduler.prd  
 * Date:2016年11月30日下午3:08:31  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/

package com.tp.scheduler.prd;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiniu.QiniuUpload;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.tp.common.vo.Constant;
import com.tp.model.prd.ItemPictures;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.prd.IItemPicturesService;

/**
 * ClassName:ItemPicturesUploadScheduler <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月30日 下午3:08:31 <br/>
 * 
 * @author zhouguofeng
 * @version
 * @since JDK 1.8
 * @see
 */
@Component
public class ItemPicturesUploadScheduler extends AbstractJobRunnable {
	@Autowired
	private IItemPicturesService itemPicturesService;
	@Autowired
	private QiniuUpload uploader;
	private static final Logger logger = LoggerFactory.getLogger(ItemPicturesUploadScheduler.class);
	/**
	 * 保存的路径
	 */
	@Value("${upload.tmp.path}")
	private String tempUpload;
	@Override
	public void execute() {
		updatePictureToQiNiu();
	}
	private void updatePictureToQiNiu() {
		List<ItemPictures> unUploadPicList = itemPicturesService.getUnUploadPictures();
		for (ItemPictures itemPicture : unUploadPicList) {
			// 图片转存
			String qiniuPath = this.loadImageFormRometeToLocal(itemPicture);
			itemPicture.setPicture(qiniuPath);
			try {
				itemPicturesService.updateById(itemPicture);//update picture path
			} catch (Exception e) {
				logger.error(e.getMessage());
				// TODO Auto-generated catch block  
				e.printStackTrace();  
				
				
			}

		}
	}

	private String loadImageFormRometeToLocal(ItemPictures itemPicture) {
		String savePath = tempUpload;
		String picPath = itemPicture.getPicture();
		String prefix = picPath.substring(picPath.lastIndexOf(".") + 1);
		String fileName = UUID.randomUUID().toString() + "." + prefix;
		List<String> dfsPicAddr = new ArrayList<String>();
		if (StringUtils.isBlank(savePath)) {
			logger.error("图片上传路径配置错误");
			return "";
		}
		HttpURLConnection connection = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		File localPic=null;
		URL url;
		try {
			picPath = picPath.replace("\\", "");
			url = new URL(picPath);
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(6000000);
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			in = new DataInputStream(connection.getInputStream());
			File localFile = new File(savePath);
			if (!localFile.exists()) {
				Boolean b = localFile.mkdirs();
				if (!b) {
					logger.error("创建文件夹失败" + localFile);
					return "";
				}
			}
			out = new DataOutputStream(new FileOutputStream(savePath + fileName));
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			connection.disconnect();
			 localPic = new File(savePath + fileName);
			if (!localPic.exists()) {
				logger.error("图片下载失败" + localFile);
				return "";
			}
			 fileName= this.uploadPic(localPic);
			if ("".equals(fileName)) {
				logger.error("图片上传dfs失败");
				return "";
			}else{
				return fileName;
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return "";

		} catch (IOException e) {
			  
			e.printStackTrace();  
			return "";
			
		}finally{
			try {
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
				if(localPic!=null){
					localPic.delete();
				}
			} catch (IOException e) {
				  
				// TODO Auto-generated catch block  
				e.printStackTrace();  
				
			}
			connection.disconnect();
		}

	}

	/**
	 * 
	 * <pre>
	 * 上传图片到dfs
	 * </pre>
	 *
	 * @param picture
	 * @return String
	 */
	private String uploadPic(File file) throws QiniuException {
		String format = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		String targetName = UUID.randomUUID().toString().replaceAll("-", "");
		Response response = uploader.uploadFile(file.getAbsolutePath(), targetName + "." + format,
				Constant.IMAGE_URL_TYPE.item.name());
		if (response.isOK()) {
			return targetName + "." + format;
		}
		return "";
	}



	@Override
	public String getFixed() {
		  
		// TODO Auto-generated method stub  
		return null;
	}
}
