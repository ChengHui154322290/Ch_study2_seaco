package com.tp.backend.controller.wms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.wms.StockOutColsEnum;
import com.tp.common.vo.wms.StockOutStatus;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.StockoutDto;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.usr.UserInfo;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutDetail;
import com.tp.model.wms.StockoutInvoice;
import com.tp.proxy.usr.UserHandler;
import com.tp.proxy.wms.StockoutDetailProxy;
import com.tp.proxy.wms.StockoutInvoiceProxy;
import com.tp.proxy.wms.StockoutProxy;
import com.tp.util.DateUtil;

//import com.meitun.base.domain.ExpressInfoDO;
//import com.meitun.wms.domain.StockOutDO;
//import com.meitun.wms.domain.StockOutDeclItemDO;
//import com.meitun.wms.domain.StockOutItemDO;
//import com.meitun.wms.enums.StockOutColsEnum;
//import com.meitun.wms.enums.StockOutStatus;
//import com.meitun.wms.util.Page;

@Controller
@RequestMapping("wms/stockout/")
public class StockOutController {

//	@Autowired
//	private StockOutProxy stockOutAO;
	private static final Logger logger = LoggerFactory.getLogger(StockOutController.class);
	@Autowired
	private StockoutProxy stockoutProxy;
	
	@Autowired
	private StockoutDetailProxy stockoutDetailProxy;
	
	@Autowired
	private StockoutInvoiceProxy stockoutInvoiceProxy;
	
	public StockoutInvoiceProxy getStockoutInvoiceProxy() {
		return stockoutInvoiceProxy;
	}
	public void setStockoutInvoiceProxy(StockoutInvoiceProxy stockoutInvoiceProxy) {
		this.stockoutInvoiceProxy = stockoutInvoiceProxy;
	}
	public StockoutDetailProxy getStockoutDetailProxy() {
		return stockoutDetailProxy;
	}
	public void setStockoutDetailProxy(StockoutDetailProxy stockoutDetailProxy) {
		this.stockoutDetailProxy = stockoutDetailProxy;
	}
	public StockoutProxy getStockoutProxy() {
		return stockoutProxy;
	}
	public void setStockoutProxy(StockoutProxy stockoutProxy) {
		this.stockoutProxy = stockoutProxy;
	}
	
//	public final static String excel = "海关-货物放行订单列表";
//	public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private static final String filePath = "template/stockout.xlsx";
//	
//    /**
//     * 默认时间格式
//     */
//    public final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
//
//	/**
//	 * 出库单列表
//	 * @return
//	 */
//	@RequestMapping(value = "/list")
//	public String list( @RequestParam(defaultValue = "1") Integer page, Integer size, StockOut stockOutDO, Model model){
//		if(page==null){
//			page = 1;
//		}
////		stockOutDO.setStartPage(page);
//		ResultInfo< PageInfo<StockOut>> stockOutPages =  stockOutAO.queryPageByObject(stockOutDO, new PageInfo<StockOut>(page, size));
//		model.addAttribute("stockOutPages", stockOutPages.getData() );
//		model.addAttribute("stockOutDO", stockOutDO);
//		model.addAttribute("statusList", StockOutStatus.values());
//		return "/wms/stockout/list";
//	}
//	
//	
//	@RequestMapping(value = "/updateStockOutStatus")
//	public String updateStatus(String ids, Model model){
//		model.addAttribute("statusList", StockOutStatus.values());
//		model.addAttribute("ids", ids);
//		return "/wms/stockout/update_stockout";
//	}
//	
//	/**
//	 * 更新出库单状态
//	 * @return
//	 */
//	@RequestMapping(value = "doUpdateStockOutStatus")
//	public void doUpdateStockOutStatus(String ids, String status){
//		stockOutAO.doUpdateStockOutStatus(ids, status);
//	}
//
//	@RequestMapping(value = "/viewItem")
//	public String viewStockOutItem(String id, Model model) {
//
//		List<StockOutItem> outItemDoList = stockOutAO.viewStockOutItem(id);
//		model.addAttribute("outItemDoList", outItemDoList);
//		return "/wms/stockout/viewItem";
//	}
//	
//	@RequestMapping(value = "/exportConfig")
//	public String exportConfig(Model model) {
//		UserInfo user = UserHandler.getUser();
//		if(user == null || user.getId() == null) {
//			model.addAttribute("msg", "userError");
//		} else {
//			Map<String, String> configColMap = stockOutAO.getExportConfig(user.getId());			
//			model.addAttribute("configColMap", configColMap);
//			model.addAttribute("stockOutColsList", StockOutColsEnum.values());
//		}
//		return "/wms/stockout/exportConfig";
//	}
//	
//	@RequestMapping(value = "/doExport")
//	public void doExport(String colCodes, HttpServletResponse response, HttpServletRequest request) throws IOException {
//		UserInfo user = UserHandler.getUser();
//		HSSFWorkbook workbook = stockOutAO.genStockExcel(colCodes, user.getId());
//        String fileName = excel + dateFormat.format(new Date()) + ".xls";
//        fileName = encodeFilename(fileName, request);
//        export(response, fileName, workbook);
//	}
//
    public static void export(final HttpServletResponse response, final String fileName, final Workbook workbook) {
        ServletOutputStream fileOut = null;
        try {
            // 导出到文件
            response.setContentType("application/vnd.ms-excel");
            fileOut = response.getOutputStream();
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
            workbook.write(fileOut);
            fileOut.close();
        } catch (final Exception e) {
        	logger.error(e.getMessage(), e);
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (final IOException e) {
                	logger.error(e.getMessage(), e);
                }
            }
        }
    }
//
    public static String encodeFilename(final String filename, final HttpServletRequest request) {
        /**
         * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon;
         * Alexa Toolbar) 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10)
         * Gecko/20050717 Firefox/1.0.6
         */
        final String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (-1 != agent.indexOf("MSIE") || -1 != agent.indexOf("rv:11.0"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("Mozilla"))) {
                return MimeUtility.encodeText(filename, "UTF-8", "B");
            }
            return filename;
        } catch (final Exception ex) {
            return filename;
        }
    }
//
//	@RequestMapping(value = "/downStockOutTemplate")
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
//			response.setHeader("Content-Disposition", "attachment; filename=stockout.xlsx");
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
//	@RequestMapping(value = "/importExpress")
//	public String importExpress() {
//		return "/wms/stockout/importExpress";
//	}
//	
//	@RequestMapping(value = "/doImportExpress")
//	public void doImportExpress(@RequestParam("expressExcel") MultipartFile expressExcel, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String msg = stockOutAO.doImportExpress(expressExcel.getInputStream());
//		if(StringUtils.isBlank(msg)) {
//			msg = "导入成功！";
//		}
//		response.getWriter().write(msg);
//	}
//	
//	@RequestMapping(value = "/selectExpressInfo")
//	public String selectExpressInfo(String ids, Model model) {
//		List<StockOut> stockOutList = stockOutAO.getStockOutByIds(ids);
//		List<ExpressInfo> expressList = stockOutAO.getExpressList();
//		model.addAttribute("stockOutList", stockOutList);
//		model.addAttribute("expressList", expressList);
//		return "/wms/stockout/selectExpressInfo";
//	}
//	
//	@RequestMapping(value = "/doUpdateStockOutStatusByExpress")
////	public void doUpdateStockOutStatusByExpress(String expressCode, String expressName, StockOutDOModelView stockOutDOModelView, HttpServletResponse response) throws IOException {
//	public void doUpdateStockOutStatusByExpress(String expressCode, String expressName,  HttpServletRequest request, HttpServletResponse response) throws IOException {
////		List<StockOut> stockOutList = stockOutDOModelView.getStockOutList();		
//		List<StockOut> stockOutList = getStockOutList(request);		
//		String msg = stockOutAO.doUpdateStockOutStatusByExpress(expressCode, expressName, stockOutList);
//		if(StringUtils.isBlank(msg)) {
//			msg = "状态更新成功！";
//		}
//		response.getWriter().write(msg);
//	}
//	
//	private List<StockOut> getStockOutList(HttpServletRequest reuqest){
//
//		List<StockOut> listStock = new ArrayList<StockOut>();
//		String externalNo = "";
//		String expressId = "";
//		String id = "";
//		Date startCreateTime;	 	
//		int i =0;
//		do{
//			// stockOutList[${i}].id
//			String keyId = String.format("stockOutList[%d].id", i);
//			id = reuqest.getParameter( keyId);
//			String keyExternalNo = String.format("stockOutList[%d].externalNo", i);
//			externalNo = reuqest.getParameter( keyExternalNo);
//			String keyExternalId = String.format("stockOutList[%d].expressId", i);
//			expressId = reuqest.getParameter( keyExternalId);
//			String keyStartCreateTime = String.format("stockOutList[%d].startCreateTime", i);
//	        startCreateTime = getDate(reuqest, "keyStartCreateTime", "yyyy-MM-dd");
//			++i;
//			if(externalNo == null){
//				break;
//			}
//			
//			ResultInfo<StockOut>  resStockout = stockOutAO.queryById(Long.valueOf(id));
//			if( resStockout.isSuccess() ){
//				StockOut stockout = resStockout.getData();
//				stockout.setExternalNo( externalNo);
//				stockout.setExpressId(expressId);
//				listStock.add(stockout);					
//			}
//			
//		} while( externalNo != null);
//		return listStock;
//	}
//	
//
//    /**
//     * <pre>
//     * 获取字符串列表
//     * </pre>
//     *
//     * @param request
//     * @param name
//     * @return
//     */
//    private String[] getStringValues(final HttpServletRequest request, final String name) {
//        final String vals[] = request.getParameterValues(name);
//        String valRet[] = null;
//        if (null != vals && vals.length > 0) {
//            valRet = new String[vals.length];
//            for (int i = 0; i < vals.length; i++) {
//                valRet[i] = setBlankNull(vals[i]);
//            }
//        }
//        return valRet;
//    }
//    
//    /**
//     * <pre>
//     * 获取时间
//     * </pre>
//     *
//     * @param request
//     * @param name
//     * @param format
//     * @return
//     */
//    public Date getDate(final HttpServletRequest request, final String name, final String format) {
//        Date date = null;
//        String dateFormat = DEFAULT_TIME_FORMAT;
//        if (checkIsNull(request, name)) {
//            return null;
//        }
//        if (null != format) {
//            dateFormat = format;
//        }
//        final String dateVal = request.getParameter(name);
//        if (null == dateVal || "".equals(dateVal.trim())) {
//            return null;
//        }
//        try {
//            final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//            date = sdf.parse(dateVal);
//        } catch (final Exception e) {
//        }
//        return date;
//    }
//    
//    /**
//     * <pre>
//     * 设置空值为null
//     * </pre>
//     *
//     * @return
//     */
//    private String setBlankNull(final String inputStr) {
//        if (null == inputStr || "".equals(inputStr.trim())) {
//            return null;
//        }
//        return inputStr;
//    }
//    
//    /**
//     * <pre>
//     * 校验是否为空
//     * </pre>
//     *
//     * @param request
//     * @param name
//     * @return
//     */
//    private boolean checkIsNull(final HttpServletRequest request, final String name) {
//        if (null == name || null == request) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//	
	/**
	 * 获取出库单列表
	 */
	@RequestMapping(value = "/list")
	public String list(Integer page, Integer size, Stockout stockout,Model model){
		PageInfo<Stockout> info = new PageInfo<Stockout>();
		if(page==null||page==0){
			page = 1;
		}
		if(size==null||size==0){
			size = 10;
		}
		info.setPage(page);
		info.setSize(size);
		ResultInfo<PageInfo<Stockout>> stockoutPageInfo = stockoutProxy.queryPageByObject(stockout, info);
		model.addAttribute("stockOutPages", stockoutPageInfo.getData() );
		model.addAttribute("stockOutReq", stockout);
//		model.addAttribute("statusList", StockOutStatus.values());
		return "/wms/stockout/list";
	}
	/**
	 * 发货单明细
	 */
	@RequestMapping(value = "/viewItem")
	public String viewItem(Long id,StockoutDetail stockoutDetail,Model model){
		/* 查询出库单主表信息 start*/
		ResultInfo<Stockout> stockoutResult = stockoutProxy.queryById(id);
		/* 查询出库单主表信息 end*/
		stockoutDetail = new StockoutDetail();
		stockoutDetail.setStockoutId(id);
		ResultInfo<List<StockoutDetail>> outDetailResult = stockoutDetailProxy.queryByObject(stockoutDetail);
		model.addAttribute("stockout",stockoutResult.getData());
		model.addAttribute("outItemList", outDetailResult.getData());
		return "/wms/stockout/viewItem";
	}
	/**
	 * 发货单发票信息
	 */
	@RequestMapping(value = "/viewInvoice")
	public String viewInvoice(Long id,Model model){
		/* 查询出库单主表信息 start*/
		ResultInfo<Stockout> stockoutResult = stockoutProxy.queryById(id);
		/* 查询出库单主表信息 end*/
		StockoutInvoice stockoutInvoice = new StockoutInvoice(); 
		stockoutInvoice.setOrderCode(stockoutResult.getData().getOrderCode());
		ResultInfo<List<StockoutInvoice>> outInvoiceResult = stockoutInvoiceProxy.queryByObject(stockoutInvoice);
		model.addAttribute("stockout",stockoutResult.getData());
		model.addAttribute("outInvoiceList", outInvoiceResult.getData());
		return "/wms/stockout/viewInvoice";
	}
	
	/**
	 * 导出出库单信息
	 */
	@RequestMapping(value = "/doExport")
	public void doExport( HttpServletResponse response, HttpServletRequest request) throws IOException {
		Stockout stockout = new Stockout();
		String consignee = URLDecoder.decode(request.getParameter("consignee"),"utf-8");
	    if("".equals(request.getParameter("orderCode"))){
	    	stockout.setOrderCode(null);
	    }else{
	    	stockout.setOrderCode(request.getParameter("orderCode"));
	    }
	    if("".equals(request.getParameter("expressNo"))){
	    	stockout.setExpressNo(null);
	    }else{
	    	stockout.setExpressNo(request.getParameter("expressNo"));
	    }
	    if("".equals(consignee)){
	    	stockout.setConsignee(null);
	    }else{
	    	stockout.setConsignee(consignee);
	    }
	    if("".equals(request.getParameter("mobile"))){
	    	stockout.setMobile(null);
	    }else{
	    	stockout.setMobile(request.getParameter("mobile"));
	    }
	    if("".equals(request.getParameter("status"))){
	    	stockout.setStatus(null);
	    }else{
	    	stockout.setStatus(Integer.valueOf(request.getParameter("status")));
	    }
		HSSFWorkbook workbook = stockoutProxy.formatStockExcel(stockout);
		String fileName = "出库单信息" + DateUtil.format(new Date(),DateUtil.LONG_FORMAT) + ".xls";
        fileName = encodeFilename(fileName, request);
        export(response, fileName, workbook);
	}
	
}
