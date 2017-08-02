package com.tp.backend.controller.wms;
//package com.tp.backend.controller.wms;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.tp.common.vo.PageInfo;
//import com.tp.common.vo.wms.KjGjrqRequestItem;
//import com.tp.model.wms.StockOutDecl;
//import com.tp.model.wms.StockOutDeclItem;
//import com.tp.proxy.wms.StockOutDeclProxy;
//
///**
// * 出库单报检确认
// * @author beck
// *
// */
//@Controller
//@RequestMapping("wms/stockoutdecl/")
//public class StockOutDeclController {
//
//	@Autowired
//	private StockOutDeclProxy stockOutDeclAO;
//	private static final String filePath = "excel/stockdecl.xlsx";
//	/**
//	 * 出库报检单列表
//	 * @return
//	 */
//	@RequestMapping(value = "/list")
//	public String list(StockOutDecl stockOutQO, Integer page,Integer size,  Model model){
//		
//		Integer startPage = page == null ? 1 : page;
//		Integer pageSize = size == null ? 10 : size;
//		PageInfo<StockOutDecl> pageinfo = stockOutDeclAO.stockOutDeclList(stockOutQO, startPage, pageSize);
//		model.addAttribute("page", pageinfo);
//		model.addAttribute("stockOutQO", stockOutQO);
//		if (CollectionUtils.isEmpty(pageinfo.getRows())) {
//			model.addAttribute("norecoders", "暂无记录");
//		}
//		return "/wms/stockoutdecl/list";
//	}
//	
//	@RequestMapping(value = "/viewItem")
//	public String viewStockOutDeclItem(String id, Model model) {
//
//		List<StockOutDeclItem> declItemDoList = stockOutDeclAO.viewStockOutDeclItem(id);
//		model.addAttribute("declItemDoList", declItemDoList);
//		return "/wms/stockoutdecl/viewItem";
//	}
//	
//	/**
//	 * 导入入区单据
//	 * @return
//	 */
//	@RequestMapping(value = "/importStockOutDecl")
//	public String importStockIn(){
//		return "/wms/stockoutdecl/import";
//	}
//
//	@RequestMapping(value = "/doImportStockOutDecl")
//	public String doImportStockOutDecl(@RequestParam String storer, @RequestParam String externalNo, @RequestParam String externalNo2, @RequestParam("stockOutDeclFile") MultipartFile stockOutDeclFile,
//			Model model, HttpServletRequest request) throws IOException{
//		Map<String, Object> resultMap = stockOutDeclAO.doImportStockIn(storer, externalNo, externalNo2, stockOutDeclFile.getInputStream());
//		model.addAttribute("message", resultMap.get("message"));
//		model.addAttribute("data", (List<KjGjrqRequestItem>) resultMap.get("data"));
//		model.addAttribute("storer", storer);
//		model.addAttribute("externalNo", externalNo);
//		model.addAttribute("externalNo2", externalNo2);
//		return "/wms/stockoutdecl/import";
//	}
//
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
//			response.setHeader("Content-Disposition", "attachment; filename=stockdecl.xlsx");
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
//}
