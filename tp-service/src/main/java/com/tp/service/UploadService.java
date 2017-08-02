package com.tp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.redis.util.JedisDBUtil;
import com.tp.service.IUploadService;

@Service
public class UploadService implements IUploadService {

	@Autowired
	private JedisDBUtil jedisDBUtil;
	@Override
	public String upload(File file) {
		 jedisDBUtil.setDB(file.getName(), file);
		 return file.getName();
	}

	@Override
	public List<String> upload(List<File> fileList) {
		List<String> fileNameList = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(fileList)){
			for(File file:fileList){
				fileNameList.add(upload(file));
			}
		}
		return fileNameList;
	}

	@Override
	public File downFile(String fileId) {
		return (File)jedisDBUtil.getDB(fileId);
	}

}
