package com.tp.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tp.dfsutils.file.GeneralFile;
import com.tp.dfsutils.service.DfsService;
import com.tp.service.IBaseService;
/**
 * 活动元素表代理层
 * @author szy
 *
 */
@Service
public class DfsProxy extends BaseProxy{
	@Autowired
	DfsService dfsService;
	
	public IBaseService getService() {
		return null;
	}
	/**
	 * 将本地图片上传到文件服务器
	 * @param pictures
	 * 		本地图片路径，路径为绝对路径
	 * @return
	 */
	public List<String> uploadPictures(List<String> pictures){
		if(CollectionUtils.isEmpty(pictures)){
			return null;
		}
		List<String> resultPaths = new ArrayList<String>();
		
		for (String path : pictures) {
			File file = new File(path);
			if(!file.exists()){
				continue;
			}
			GeneralFile info = new GeneralFile();
			info.setFile(file);
			String fileid = dfsService.uploadFile(info);
			try {
				info.clear();//删除本地缓存文件
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultPaths.add(fileid);
		}
		
		return resultPaths;
	}
	
	/**
	 * 将本地图片上传到文件服务器
	 * @param picture
	 * 		本地图片路径，路径为绝对路径
	 * @return
	 */
	public String uploadPicture(String picture){
		if(StringUtils.isEmpty(picture)){
			return null;
		}
		File file = new File(picture);
		if(!file.exists()){
			return null;
		}
		GeneralFile info = new GeneralFile();
		info.setFile(file);
		
		
		String fileid = dfsService.uploadFile(info);
		return fileid;
	}
	
	/**
	 * 
	 * <pre>
	 * 从dfs上删除照片
	 * </pre>
	 *
	 * @param fileId fileId为上传至dfs上返回的key
	 */
	public void removePicture(String fileId){
		dfsService.deleteFile(fileId);
	}
}
