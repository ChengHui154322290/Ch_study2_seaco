package com.tp.seller.controller.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.tp.common.vo.Constant;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.usr.UserHandler;
import com.tp.util.ResourceUtil;

import net.sf.jxls.transformer.XLSTransformer;

public class BaseController {
    @InitBinder
    public void init(final HttpServletRequest request, final ServletRequestDataBinder binder) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    /**
   	 * 获取session中用户信息
   	 * @return
   	 */
   	public UserInfo getUserInfo(){
   		Object obj =UserHandler.getUser();
   		if(obj==null){
   			UserInfo userInfo = new UserInfo();
   			userInfo.setId(1L);
   			userInfo.setUserName("测试平台开发 ");
   			return userInfo;
   		}
   		return (UserInfo)obj;
   	}
   	/**
   	 * 
   	 * getUserName:(获取当前登录用户信息). <br/>  
   	 * TODO(这里描述这个方法适用条件 – 可选).<br/>   
   	 *  
   	 * @author zhouguofeng  
   	 * @return  
   	 * @sinceJDK 1.8
   	 */
   	public String getUserName(){
   		UserInfo userInfo = UserHandler.getUser();
   		if(userInfo!=null){
   			return userInfo.getUserName();
   		}
   		return null;
   	}
   	/**
   	 * 
   	 * exportXLS:(Excel导入功能用). <br/>  
   	 * @author zhouguofeng  
   	 * @param map
   	 * @param path
   	 * @param fileName
   	 * @param response  
   	 * @sinceJDK 1.8
   	 */
   	public void exportXLS(Map<String, Object> map, String path,String fileName,HttpServletResponse response ){
   		try {
   			File templateResource = ResourceUtil.getResourceFile(path);
   			String templateFileName = templateResource.getAbsolutePath();
   			String destFileName = Constant.getTempDir() + "/" + fileName ;
   			XLSTransformer transformer = new XLSTransformer();
   			transformer.transformXLS(templateFileName, map, destFileName);
   			if(fileName.indexOf(".xlsx") != -1	){
   				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
   			}else{
   				response.setContentType("application/vnd.ms-excel");
   			}
   			response.setHeader("Content-disposition", "attachment;filename=" + fileName);   
   			File file = new File(destFileName);
   			InputStream inputStream=new FileInputStream(destFileName);  
   			if (file != null && file.exists()) {
   				OutputStream os=response.getOutputStream();  
   	            byte[] b=new byte[1024];  
   	            int length;  
   	            while((length=inputStream.read(b))>0){  
   	                os.write(b,0,length);  
   	            }  
   	            inputStream.close();  
   			}
   		} catch (Exception e) {
   			throw new RuntimeException(e);
   		}
   	}
}
