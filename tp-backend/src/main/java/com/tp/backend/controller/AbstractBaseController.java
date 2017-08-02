package com.tp.backend.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.tp.common.vo.Constant;
import com.tp.common.vo.UserConstant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.usr.UserHandler;
import com.tp.util.ResourceUtil;


public abstract class AbstractBaseController{

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		initSubBinder(binder);
	}
	public CustomDateEditor initDateEditor(String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return new CustomDateEditor(dateFormat, true);
	}
	public void initSubBinder(ServletRequestDataBinder binder){ 
		
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
	 * 设置用户信息到session中
	 * @param userInfo
	 */
	public void setUserInfo(UserInfo userInfo){
		RequestContextHolder.getRequestAttributes().setAttribute(UserConstant.USER_SESSION_KEY, userInfo, RequestAttributes.SCOPE_SESSION);
	}
	
	public Long getUserId(){
		UserInfo userInfo = getUserInfo();
		if(userInfo!=null){
			return Long.valueOf(userInfo.getId());
		}
		return null;
	}
	
	public String getUserName(){
		UserInfo userInfo = UserHandler.getUser();
		if(userInfo!=null){
			return userInfo.getUserName();
		}
		return null;
	}
	/**
	 * 获取Ip
	 * @return
	 */
	public String getRequestIp(){
		return (String)RequestContextHolder.getRequestAttributes().getAttribute(UserConstant.USER_VISIT_IP, RequestAttributes.SCOPE_SESSION);
	}
	
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
    /**
     * 
     * <pre>
     * 校验结果
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public boolean checkResult(Map<String,Object> resultMap){
        try {
            return null != resultMap && null != resultMap.get(SupplierConstant.SUCCESS_KEY)
                    && (Boolean)resultMap.get(SupplierConstant.SUCCESS_KEY);
        } catch(Exception e){
            return false;
        }
    }
}
