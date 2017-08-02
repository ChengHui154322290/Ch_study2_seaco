package com.tp.backend.controller.supplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.dto.sup.excel.ExcelQuotationInfoDTO;
import com.tp.dto.sup.excel.ExcelQuotationProductDTO;
import com.tp.model.prd.ItemImportList;
import com.tp.proxy.prd.ItemImportProxy;
import com.tp.proxy.sup.QuotationImportProxy;
import com.tp.util.DateUtil;

/**
 * 	报价单导入
 * @author zhs
 * @version 0.0.1
 */

@Controller
@RequestMapping("/supplier/")
public class QuotationImportController  extends AbstractBaseController{
	
	private static final Logger  LOGGER = LoggerFactory.getLogger(QuotationImportController.class);
	
	@Autowired
	QuotationImportProxy quotationImportProxy;
	
	/**
	 * 
	 * <pre>
	 *   导入查询页面
	 * </pre>
	 *
	 * @param model
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 */
	@RequestMapping("/quotationImport")
	public String importExcel(Model model,@RequestParam(value = "status", defaultValue = "0") Integer status,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer size){
		model.addAttribute("sumcount", quotationImportProxy.getQuoSumCountCache());
		model.addAttribute("successcount", quotationImportProxy.getQueSuccessCountCache());		
		model.addAttribute("fail_quotationinfo",  quotationImportProxy.getQuoFailMapCache() );	
        return "supplier/quotation/quotation_import";
	}
	
	/**
	 * 
	 * <pre>
	 * 	附件上传，下载页面
	 * </pre>
	 *
	 * @return
	 */
	@RequestMapping("/quotationUploadFile")
	public String uploadFile(){
		return "/supplier/quotation/quotation_upload_file";
	}

	
	/**
	 * 
	 * <pre>
	 * 	下载模板
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	@RequestMapping("/downExcelTemplate")
	public void downExcelTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException{
		request.setCharacterEncoding("UTF-8");
		quotationImportProxy.getTemplateDatas(request,response);
	}

	
	@RequestMapping("/uploadQuotationExcel")
	public String uploadSkuExcel(@RequestParam(value="fieName",required=false) String fieName,
			@RequestParam(value="waveSign",defaultValue="")String waveSign,
			HttpServletRequest request,Model model){
      Map<String,Object> map = new HashMap<String,Object>();
      try {
          //Map<String,Object> retMap = itemImportProxy.importSkuExcel(request, fieName);
    	  //加锁 
    	  Map<String,Object> retMap = quotationImportProxy.uploadExcelToServer(request,fieName,waveSign,super.getUserName());
          if((Boolean)retMap.get(ItemImportProxy.SUCCESS_KEY)){
              map.put(ItemImportProxy.SUCCESS_KEY, true);
              map.put("logId", retMap.get("logId"));
              map.put("fileUrl",retMap.get(ItemImportProxy.UPLOADED_FILE_KEY));
              model.addAttribute("message","操作完成");	
          } else {
              map.put(ItemImportProxy.SUCCESS_KEY, false);
              map.put(ItemImportProxy.MESSAGE_KEY, retMap.get(QuotationImportProxy.MESSAGE_KEY));
              model.addAttribute("message",retMap.get(QuotationImportProxy.MESSAGE_KEY));
          }
         
      } catch(Exception e){
    	  LOGGER.error("上传商品导入失败");
    	  LOGGER.error("文件上传异常：",e);
    	  model.addAttribute("message","操作失败，请联系管理员");
      }
      model.addAttribute("logId",map.get("logId"));
	  return"/supplier/quotation/quotation_import_message";
	}
	
	


}
