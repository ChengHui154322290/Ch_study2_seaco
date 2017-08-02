package com.tp.backend.controller.wms;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mem.MemberConstant.Bool;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.Stockasn;
import com.tp.model.wms.StockasnDetail;
import com.tp.model.wms.StockasnDetailFact;
import com.tp.model.wms.StockasnFact;
import com.tp.model.wms.Stockout;
import com.tp.proxy.wms.StockasnDetailFactProxy;
import com.tp.proxy.wms.StockasnDetailProxy;
import com.tp.proxy.wms.StockasnFactProxy;
import com.tp.proxy.wms.StockasnProxy;
import com.tp.util.DateUtil;
import com.tp.util.MD5Util;


@Controller
@RequestMapping("wms/stockin/")
public class StockInController extends AbstractBaseController {
	
	private static final Logger  LOGGER = LoggerFactory.getLogger(StockInController.class);
	@Autowired
	private StockasnProxy stockasnProxy;
	
	@Autowired
	private StockasnDetailProxy stockasnDetailProxy;
	
	@Autowired
	private StockasnFactProxy stockasnFactProxy;
	
	@Autowired
	private StockasnDetailFactProxy stockasnDetailFactProxy;
	
//	private static final String filePath = "template/stockin.xlsx";

	@RequestMapping(value = "/list")
	public String list(Stockasn stockasn, Integer page,Integer size, Model model){
		
		Integer startPage = page == null ? 1 : page;
		Integer pageSize = size == null ? 10 : size;
		PageInfo<Stockasn> pageInfo = new PageInfo<Stockasn>();
		pageInfo.setPage(startPage);
		pageInfo.setSize(pageSize);
		ResultInfo<PageInfo<Stockasn>> result = stockasnProxy.queryPageByObject(stockasn, pageInfo);
		model.addAttribute("page", result.getData());
		model.addAttribute("stockasn", stockasn);
		if (CollectionUtils.isEmpty(result.getData().getRows()) ) {
			model.addAttribute("norecoders", "暂无记录");
		}
		return "/wms/stockin/list";
	}
//	
//	@RequestMapping(value = "/viewItem")
//	public String viewStockAsnReceiveItem(String id, Model model) {
//
//		List<StockAsnReceiveItem> asnItemDoList = stockInAO.viewStockAsnReceiveItem(id);
//		model.addAttribute("asnItemDoList", asnItemDoList);
//		return "/wms/stockin/viewItem";
//	}
//	
	/**
	 * 导入入区单据
	 * @return
	 */
	@RequestMapping(value = "/importStockIn")
	public String importStockIn(String stockasnId,Model model){
//		return "/wms/stockout/exportConfig";
		String token = "token"+DateUtil.format(new Date(), DateUtil.LONG_FORMAT);
		token = MD5Util.encrypt(token);
		model.addAttribute("uploadToken",token);
		model.addAttribute("stockasnId",stockasnId);
		return "/wms/stockin/import";
	}
//	
//	@RequestMapping(value = "/doImportStockIn")
//	public String doImportStockIn(@RequestParam String agentCode, @RequestParam String agentName, @RequestParam("stockInFile") MultipartFile stockInFile,
//			Model model, HttpServletRequest request) throws IOException{
//		Map<String, Object> resultMap = stockInAO.doImportStockIn(agentCode, agentName, stockInFile.getInputStream());
//		model.addAttribute("message", resultMap.get("message"));
//		model.addAttribute("data", (List<KjGjrqRequestItem>) resultMap.get("data"));
//		model.addAttribute("agentCode", agentCode);
//		model.addAttribute("agentName", agentName);
//		return "/wms/stockin/import";
//	}
	
	@RequestMapping(value = "/doImportStockIn")
	public String uploadSkuExcel(@RequestParam(value="stockInFile",required=false) MultipartFile file,HttpServletRequest request,
			Model model,@RequestParam(value="stockasnId",required=false) String stockasnId,String uploadToken){
      try {
    	  //加锁 
    	  String fileName = file.getName();
    	  ResultInfo<Boolean> uploadResult = stockasnProxy.uploadExcelToServer(request,fileName,super.getUserName(),Integer.valueOf(stockasnId),uploadToken);
          if(uploadResult.isSuccess()){
              model.addAttribute("message","操作成功");
          } else {
              model.addAttribute("message", uploadResult.getMsg().getMessage());
          } 
      } catch(Exception e){
    	  LOGGER.error("文件上传异常：",e);
    	  model.addAttribute("message","操作失败，请联系管理员");
      }
	  return"/item/import_message";
	}

//	@RequestMapping(value = "/downStockInTemplate")
//	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		response.setContentType("text/html;charset=UTF-8");
//		BufferedInputStream in = null;
//		BufferedOutputStream out = null;
//		request.setCharacterEncoding("UTF-8");
//		String path = StockInController.class.getClassLoader().getResource(filePath).getPath();
//		try {
//			File f = new File(path);
//			response.setContentType("application/x-excel");
//			response.setCharacterEncoding("UTF-8");
//			response.setHeader("Content-Disposition", "attachment; filename=stockin.xlsx");
//			response.setHeader("Content-Length", String.valueOf(f.length()));
//			in = new BufferedInputStream(new FileInputStream(f));
//			out = new BufferedOutputStream(response.getOutputStream());
//			byte[] data = new byte[1024];
//			int len = 0;
//			while (-1 != (len = in.read(data, 0, data.length))) {
//				out.write(data, 0, len);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (in != null) {
//				in.close();
//			}
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//	
	/**
	 * 展示入库单明细
	 * @param stockasnDetail
	 * @param page
	 * @param size
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/viewItem")
	public String viewItem(StockasnDetail stockasnDetail, Integer page,Integer size, Model model){
		ResultInfo<Stockasn> stockasnResult = stockasnProxy.queryById(stockasnDetail.getStockasnId());
		Integer startPage = page == null ? 1 : page;
		Integer pageSize = size == null ? 10 : size;
		PageInfo<StockasnDetail> pageInfo = new PageInfo<StockasnDetail>();
		pageInfo.setPage(startPage);
		pageInfo.setSize(pageSize);
		ResultInfo<PageInfo<StockasnDetail>> retResult = stockasnDetailProxy.queryPageByObject(stockasnDetail, pageInfo);
		model.addAttribute("page", retResult.getData());
		model.addAttribute("stockasnDetail", stockasnDetail);
		model.addAttribute("stockasn",stockasnResult.getData());
		
		return "/wms/stockin/viewItem";
	}
	/**
	 * 展示入库回执
	 * @param stockasnFact
	 * @param page
	 * @param size
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fact")
	public String viewFact(StockasnFact stockasnFact, Integer page,Integer size, Model model){
		ResultInfo<Stockasn> stockasnResult = stockasnProxy.queryById(stockasnFact.getStockasnId());
		Integer startPage = page == null ? 1 : page;
		Integer pageSize = size == null ? 10 : size;
		PageInfo<StockasnFact> pageInfo = new PageInfo<StockasnFact>();
		pageInfo.setPage(startPage);
		pageInfo.setSize(pageSize);
		ResultInfo<PageInfo<StockasnFact>> retResult = stockasnFactProxy.queryPageByObject(stockasnFact, pageInfo);
		model.addAttribute("page", retResult.getData());
		model.addAttribute("stockasnFact", stockasnFact);
		model.addAttribute("stockasn",stockasnResult.getData());
		return "/wms/stockin/fact";
	}
	
	@RequestMapping(value = "/detailFact")
	public String detailFact(StockasnDetailFact stockasnDetailFact, Integer page,Integer size, Model model){
		ResultInfo<StockasnFact> stockasnFactResult = stockasnFactProxy.queryById(stockasnDetailFact.getStockasnFactId());
		Integer startPage = page == null ? 1 : page;
		Integer pageSize = size == null ? 10 : size;
		PageInfo<StockasnDetailFact> pageInfo = new PageInfo<StockasnDetailFact>();
		pageInfo.setPage(startPage);
		pageInfo.setSize(pageSize);
		ResultInfo<PageInfo<StockasnDetailFact>> retResult = stockasnDetailFactProxy.queryPageByObject(stockasnDetailFact, pageInfo);
		model.addAttribute("page", retResult.getData());
		model.addAttribute("stockasnDetailFact", stockasnDetailFact);
		model.addAttribute("stockasnFact", stockasnFactResult.getData());
		return "/wms/stockin/detailFact";
	}

	/**
	 * 下载导入模板
	 */
	@RequestMapping(value = "/doExport")
	public void doExport(HttpServletResponse response, HttpServletRequest request) throws IOException {
		String path = StockInController.class.getResource("/").getPath()+"template/stockin-model.xlsx";
		// path是指欲下载的文件的路径。
        File file = new File(path);
        // 取得文件名。
        String filename = file.getName();

		InputStream fis = new BufferedInputStream(new FileInputStream(path));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
	}
	
    
}
	