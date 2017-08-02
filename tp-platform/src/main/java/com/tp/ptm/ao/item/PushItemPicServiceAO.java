package com.tp.ptm.ao.item;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qiniu.QiniuUpload;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.tp.common.vo.Constant;
import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.dto.prd.PushItemPicDto;
import com.tp.dto.prd.excel.ExcelItemDetailForTransDto;
import com.tp.dto.ptm.ReturnData;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.ThreadUtil;

@Service
public class PushItemPicServiceAO {

	@Autowired
	private QiniuUpload uploader;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	private IItemSkuService itemSkuService;
	
	@Autowired
	private IItemPicturesService itemPicturesService;
	
	private final static String RUN_WORK_KEY = "platform-pushItemPic";
	
	/**
	 * 保存的路径
	 */
	@Value("${upload.tmp.path}")
	private String tempUpload;
	
	private static final Logger logger = LoggerFactory.getLogger(PushItemPicServiceAO.class);
	
	public ReturnData pushItemPic(PushItemPicDto pushItemPicDto,HttpServletRequest request,Long currentUserId, String appKey){
		if(pushItemPicDto.getSku()==null || "".equals(pushItemPicDto.getSku())){
			return  new ReturnData(Boolean.FALSE, 600002,"sku存在缺失");
		}
		Date current = new Date();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("sku", pushItemPicDto.getSku());
		ItemSku itemSku = itemSkuService.queryUniqueByParams(params);
		if(itemSku==null){
			return new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, "sku未匹配到商品");
		}
		ItemPictures itemMianPic = new ItemPictures();//商品主图
		ItemPictures itemPicture = null;//商品图片
		ItemPictures itemDescPic = null;//商品详情描述
		List<ItemPictures> itemPictures = new ArrayList<ItemPictures>();
		List<ItemPictures> itemDescPics = new ArrayList<ItemPictures>();
		//拼对象
		itemMianPic.setItemId(itemSku.getItemId());
		itemMianPic.setDetailId(itemSku.getDetailId());
		itemMianPic.setMain(1);// 0-否 1-是 
		itemMianPic.setPicture(pushItemPicDto.getMainPic());
		itemMianPic.setCreateTime(current);
		itemMianPic.setCreateUser(String.valueOf(currentUserId));
		
		for(String pic : pushItemPicDto.getPictures()){
			itemPicture = new ItemPictures();
			itemPicture.setPicture(pic);
			itemPicture.setItemId(itemSku.getItemId());
			itemPicture.setDetailId(itemSku.getDetailId());
			itemPicture.setCreateTime(current);
			itemPicture.setCreateUser(String.valueOf(currentUserId));
			itemPictures.add(itemPicture);
		}
		
		for(String descPic : pushItemPicDto.getDescPics()){
			itemDescPic = new ItemPictures();
			itemPicture.setPicture(descPic);
			itemDescPic.setItemId(itemSku.getItemId());
			itemDescPic.setDetailId(itemSku.getDetailId());
			itemDescPic.setCreateTime(current);
			itemDescPic.setCreateUser(String.valueOf(currentUserId));
			itemDescPics.add(itemDescPic);
		}
		
		sysnLoadImage(itemMianPic,itemPictures,itemDescPics);
		
		return new ReturnData(Boolean.TRUE) ;
	}
	
	/**
	 * 异步处理图片上传
	 */
	public void sysnLoadImage(final ItemPictures itemMianPic, final List<ItemPictures> itemPictures,final List<ItemPictures> itemDescPics){
		try{
			Runnable r = new Runnable() {
				@Override
				public void run() {
					Long start = System.currentTimeMillis();
					//加锁
					boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY);// 获得锁
					logger.info("=查看锁==platform-pushItemPic=={} ",lock);
					if(!lock){
						//获得锁的次数
						int count = 0;
						while(true){
							 if(count>300){
								//
//								itemImportService.updateImportLogStatus(logId, 5);//处理超时
								return;
							 }
							 lock = jedisCacheUtil.lock(RUN_WORK_KEY);
							 //锁的超时时间
							 jedisCacheUtil.setKeyExpire(RUN_WORK_KEY,300);//5 min
							 if(lock){
								 break;
							 }
							 count++;
							 ThreadUtil.sleep(1000L);
						}
					}else{
						jedisCacheUtil.setKeyExpire(RUN_WORK_KEY,300);//5 min
					}

					try {
						String mainPicQnPath = loadImageFormRometeToLocal(itemMianPic);
						itemMianPic.setPicture(mainPicQnPath);
						
						List<ItemPictures> newItemPicQnPaths = new ArrayList<ItemPictures>();
						List<ItemPictures> newDescPicQnPaths = new ArrayList<ItemPictures>();
						for(ItemPictures itemPicture:itemPictures){
							String itemPicQnPath = loadImageFormRometeToLocal(itemPicture);
							itemPicture.setPicture(itemPicQnPath);
							newItemPicQnPaths.add(itemPicture);
						}
						for(ItemPictures itemDescPic:itemDescPics){
							String descPicQnPath = loadImageFormRometeToLocal(itemDescPic);
							itemDescPic.setPicture(descPicQnPath);
							newDescPicQnPaths.add(itemDescPic);
						}
						//数据库操作
						itemPicturesService.replaceItemPics(itemMianPic,newItemPicQnPaths,newDescPicQnPaths);
					} catch(Exception e){
						logger.error(e.getMessage());
					}finally {
						jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
						logger.info("导入、解析、校验、保存excel耗时:  {}",
								((System.currentTimeMillis() - start)));
					}
				}
			};
			//执行线程....
			ThreadUtil.excAsync(r,false);
		} catch (ItemServiceException e) {
			jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
			logger.error("上传的excel批量插入日志表出错,错误信息如下：{}  ", e.getMessage());
		}finally{
			jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
		}
		
		
	}
	
	private String loadImageFormRometeToLocal(ItemPictures itemPicture) {
		String savePath = tempUpload;
		String picPath = itemPicture.getPicture();
		String prefix = picPath.substring(picPath.lastIndexOf(".") + 1);
		String fileName = UUID.randomUUID().toString() + "." + prefix;
//		List<String> dfsPicAddr = new ArrayList<String>();
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
			//图片上传dfs
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
}
