package com.tp.seller.ao.item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dfsutils.file.GeneralFile;
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.service.DfsService;

@Service
public class DfsAO {

	@Autowired
	DfsService dfsService;
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
			File file=new File(path);
			if(!file.exists()){
				continue;
			}
			ImgFile info=new ImgFile();
			info.setFile(file);
			String fileid=dfsService.uploadFile(info);
			try {
				info.clear();//删除本地缓存文件
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(fileid)&&!path.equals(fileid)){
				resultPaths.add(fileid);
			}
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
		if(StringUtils.isBlank(picture)){
			return null;
		}
		File file=new File(picture);
		if(!file.exists()){
			return null;
		}
		ImgFile info=new ImgFile();
		info.setFile(file);
		String fileid=dfsService.uploadFile(info);
		return fileid;
	}
	
	/**
	 * 
	 * <pre>
	 *  上传图片到dfs
	 * </pre>
	 *
	 * @param picture
	 * @return String
	 */
	public String uploadPic(File picFile){
		GeneralFile info=new GeneralFile();
		info.setFile(picFile);
		String fileid=dfsService.uploadFile(info);
		return fileid;
	}
	
}
