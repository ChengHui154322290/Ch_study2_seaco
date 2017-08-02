package com.tp.m.controller.system;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.system.FileAO;
import com.tp.m.base.MResultVO;
import com.tp.m.controller.payment.PayController;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.system.QueryFile;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.system.UploadVO;
import com.tp.util.DateUtil;

/**
 * 文件控制器
 * @author zhuss
 * @2016年1月13日 下午4:20:52
 */
@Controller
@RequestMapping("/file")
public class FileController {
	
	private static final Logger log = LoggerFactory.getLogger(PayController.class);
	
	@Autowired
	private FileAO fileAO;
	
	/**
	 * 上传图片
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/uploadimg",method = RequestMethod.POST)
	@ResponseBody
	public String uploadImg(String fileName,HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryFile file = (QueryFile) JsonUtil.getObjectByJsonStr(jsonStr, QueryFile.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 上传图片 入参] = {}",JsonUtil.convertObjToStr(file));
			}
			AssertUtil.notBlank(file.getImgstream(), MResultInfo.IMAGE_NULL);
			MResultVO<UploadVO> result = fileAO.uploadImg(file.getImgstream());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 上传图片 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 上传图片  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 上传图片 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 下载文件
	 * @param fileName
	 * @param response
	 */
	@RequestMapping("/download")
	@ResponseBody
	public void downloadFile(String imgPath, HttpServletRequest request, HttpServletResponse response) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			request.setCharacterEncoding("UTF-8");
			String fileName = "qrcode_"+DateUtil.format(new Date(), "yyyyMMddHHmmss")+".png";
			URL url = new URL(imgPath);
			URLConnection con = url.openConnection();   
			con.setConnectTimeout(5 * 1000);  
            InputStream inStream = con.getInputStream();
			long fileLength = con.getContentLengthLong();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(inStream);// 获取输入流
			bos = new BufferedOutputStream(response.getOutputStream());	// 输出流
			byte[] buff = new byte[2048];
			int bytesRead = 0;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			// 关闭流
			bis.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
