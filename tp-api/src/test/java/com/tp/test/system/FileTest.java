package com.tp.test.system;


import java.io.File;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.qiniu.QiniuUpload;
import com.qiniu.http.Response;
import com.tp.common.vo.Constant;
import com.tp.test.base.BaseTest;

/**
 * 位置单元测试
 * @author Administrator
 *
 */
public class FileTest extends BaseTest{
	
	@Autowired
	private QiniuUpload uploader;

	@Test
	public void upload() throws Exception {
		File localFile = new File("D:\\aaa.jpg");
		Response response= uploader.uploadFile(localFile.getPath(), localFile.getName(),Constant.IMAGE_URL_TYPE.cmsimg.name());
		System.out.println("========"+response.bodyString());
	}
}
