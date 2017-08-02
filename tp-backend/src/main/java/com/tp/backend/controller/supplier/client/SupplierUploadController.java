package com.tp.backend.controller.supplier.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.SupplierUploadAO;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.dto.common.ResultInfo;

/**
 * 
 * <pre>
 *  供应商文件上传
 * </pre>
 *
 * @author 谢云峰
 * @version $Id: SupplierUploadController.java, v 0.1 2014年12月26日 上午10:27:58 Administrator Exp $
 */
@Controller
@RequestMapping("/supplier/upload/")
public class SupplierUploadController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
	private SupplierUploadAO supplierUploadAO;
    
    @RequestMapping("fileUpload")
    @ResponseBody
    public  ResultInfo<Boolean> fileUpload(HttpServletRequest request,String fileName){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            Map<String,Object> retMap = supplierUploadAO.uploadFile(request, fileName);
            if((Boolean)retMap.get(SupplierConstant.SUCCESS_KEY)){
                map.put(SupplierConstant.SUCCESS_KEY, true);
                map.put("fileUrl",retMap.get(SupplierConstant.UPLOADED_FILE_KEY));
                map.put("absUrl", retMap.get(SupplierConstant.UPLOADED_FILE_REAL_PATH));
            } else {
                map.put(SupplierConstant.SUCCESS_KEY, false);
                map.put(SupplierConstant.MESSAGE_KEY, retMap.get(SupplierConstant.MESSAGE_KEY));
            }
        } catch(Exception e){
            logger.error("file upload error:{}",e);
        }
        return new ResultInfo<>(Boolean.TRUE);
    }
    
    @RequestMapping("download")
    public void downloadAction(HttpServletRequest request,@RequestParam(value="fileId",required=true)String fileId,HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        supplierUploadAO.download(request,response, fileId);
    }
    
    /**
     * pdf预览
     * 
     */
    @RequestMapping(value="showPdf")
    public void showPdf(HttpServletResponse response,@RequestParam(value="tagId",required=true) String tagId){
    	InputStream ins = null;
    	int len = 0;
    	byte b[] = new byte[1024];
    	try {
    		File file = ResourceUtils.getFile("pdf/contract_template.pdf");
    		ins = new FileInputStream(file);
			while((len = ins.read(b))>0){
				response.getOutputStream().write(b,0,len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
