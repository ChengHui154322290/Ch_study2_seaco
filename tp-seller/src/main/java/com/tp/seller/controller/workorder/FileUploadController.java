package com.tp.seller.controller.workorder;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.tp.seller.ao.workorder.DfsAo;
import com.tp.seller.util.AjaxBean;


@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/seller/workorder")
public class FileUploadController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DfsAo dfsAo;
	
	@ResponseBody
	@RequestMapping("/uploadFile")
	public String uploadFile(HttpServletRequest request,String id) {
		JSONObject json = new JSONObject();
		try {
			logger.debug("开始文件上传");
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			
			CommonsMultipartFile orginalFile = (CommonsMultipartFile) multipartRequest
					.getFile("file");// 表单中对应的文件名；
			
			if (orginalFile != null && !orginalFile.isEmpty()) {// 如果有文章中带有附件
				String filename = orginalFile.getOriginalFilename();
				
				String root = request.getSession().getServletContext().getRealPath("");
				
				File file = new File(root, "upload");
				
				
				if(!file.exists()) file.mkdir();
				
				String realFileName = file.getAbsolutePath()+"\\"+ System.currentTimeMillis()+filename.substring(filename.lastIndexOf(".")); 
				
				DataOutputStream out = new DataOutputStream(
						new FileOutputStream(realFileName));// 存放文件的绝对路径
				InputStream is = null;// 附件输入流
				try {
					is = orginalFile.getInputStream();
					byte[] b = new byte[is.available()];
					is.read(b);
					out.write(b);
				} catch (IOException exception) {
					exception.printStackTrace();
				} finally {
					if (is != null) {
						is.close();
					}
					if (out != null) {
						out.close();
					}
				}
				
				String fileId = dfsAo.uploadPicture(realFileName);
				logger.info("上传图片至dfs,image key :" + fileId);
				File realFile = new File(realFileName);
				realFile.deleteOnExit();
				json.put("id", id);
				json.put("key", fileId);
			}
			
			
		} catch (Exception e) {
			logger.error("[ERROR]文件上传错误:"+e.getMessage(),e);
		}
		return json.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/removeUploadFile")
	public AjaxBean removeUploadFile(HttpServletRequest request,String fileId) {
		AjaxBean bean = new AjaxBean();
		try {
			logger.info("删除DFS上的文件:"+fileId);
			if(null == fileId) throw new Exception("无法删除,fileId为空");
			this.dfsAo.removePicture(fileId);
			bean.setState(true);
		} catch (Exception e) {
			bean.setState(false);
			bean.setMessage(e.getMessage());
			logger.error("删除DFS上的文件错误:"+e.getMessage(),e);
		}
		return bean;
	}
	
	
 }
