package com.tp.backend.controller.ord;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.customs.DirectmailOrderClearaceExcelDTO;
import com.tp.dto.prd.excel.ExcelBaseDTO;
import com.tp.exception.ExcelContentInvalidException;
import com.tp.exception.ExcelParseException;
import com.tp.exception.ExcelRegexpValidFailedException;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.proxy.bse.ExpressInfoProxy;
import com.tp.proxy.ord.PersonalgoodsDeclareInfoProxy;
import com.tp.util.DateUtil;
import com.tp.util.ExcelUtil;
import com.tp.util.StringUtil;

@Controller
public class OrderCustomsController extends AbstractBaseController{

	private final static Log log = LogFactory.getLog(OrderCustomsController.class);
	
	@Autowired
	private ExpressInfoProxy expressInfoProxy;
	@Autowired
	private PersonalgoodsDeclareInfoProxy personalgoodsDeclareInfoProxy;
	
	@InitBinder
	public void init(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/** 清关单列表 */
	@RequestMapping(value="/order/customs/clearanceinfolist")
	public String clearanceInfoList(Model model, PersonalgoodsDeclareInfo info, Integer page, Integer size){
		ResultInfo<PageInfo<PersonalgoodsDeclareInfo>> pageResult = personalgoodsDeclareInfoProxy.queryPageByObject(info, new PageInfo<>(page, size));		
		ResultInfo<List<ExpressInfo>> expressList = expressInfoProxy.queryByObject(new ExpressInfo());
		model.addAttribute("page", pageResult.getData());
		model.addAttribute("expressList", expressList.getData());
		model.addAttribute("info", info);
		return "salesorder/clearanceinfolist";
	}
	
	@RequestMapping(value="/order/customs/directmailClearancePreview")
	public String directmailClearancePreview(Model model, String ordercodes){
		if (StringUtil.isNotEmpty(ordercodes)){
			Map<String, Object> params = new HashMap<>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "order_code in(" + ordercodes + ")");
			params.put("importType", 0);
			ResultInfo<List<PersonalgoodsDeclareInfo>> pgResultInfo = personalgoodsDeclareInfoProxy.queryByParam(params);
			if (pgResultInfo.isSuccess() && pgResultInfo.getData() != null){
				model.addAttribute("pgInfoList", pgResultInfo.getData());	
			}
		}
		return "salesorder/previewDirectmail";
	}
	
	@RequestMapping(value = "/order/customs/directmailClearance")
	@ResponseBody
	public ResultInfo<Boolean> directmailClearance(Model model, String ordercodes, PersonalgoodsDeclareInfo pgInfo){
		if(StringUtil.isEmpty(ordercodes)){
			return new ResultInfo<>(new FailInfo("列表为空"));
		}
		if (StringUtil.isEmpty(pgInfo.getBillNo()) || StringUtil.isEmpty(pgInfo.getTrafNo()) || StringUtil.isEmpty(pgInfo.getVoyageNo())){
			return new ResultInfo<>(new FailInfo("航班号或者提运单号或者运输工具编号为空"));
		}
		List<String> orderCodeList = new ArrayList<>(Arrays.asList(ordercodes.split(",")));
		return personalgoodsDeclareInfoProxy.updateDirectmailClearanceByPersonalgoosInfo(pgInfo, orderCodeList);
	}
	
	@RequestMapping(value = "/order/customs/directmailClearanceImport")
	public String directmailClearanceImport(Model model){
		return "salesorder/directmailClearanceImport";
	}
	
	@RequestMapping(value = "/order/customs/downDMClearanceTemplate")
	public void downloadDirectmailTemplate(HttpServletResponse response){
		response.setHeader("Content-disposition", "attachment; filename=directmail-clearance-list.xlsx");
		response.setContentType("application/x-download");
		try{
			String templatePath = "/WEB-INF/classes/template/directmail-clearance-list.xlsx";
			String fileName = "directmail-clearance-list_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			Map<String, Object> map = new HashMap<>();
			map.put("list", new ArrayList<>());
			super.exportXLS(map, templatePath, fileName, response);	
		}catch(Exception e){
			log.error("直邮报关模板下载异常", e);
		}
	}
	
	@RequestMapping(value = "/order/customs/uploadDMClearanceExcel")
	public String uploadDirectmailClearanceExcel(HttpServletRequest request, String fieName, Model model){
		try {
			File file = uploadFile(request, fieName);
			@SuppressWarnings("unchecked")
			List<DirectmailOrderClearaceExcelDTO> data = 
					(List<DirectmailOrderClearaceExcelDTO>) readExcelList(file, DirectmailOrderClearaceExcelDTO.class);
			ResultInfo<Boolean> resultInfo = personalgoodsDeclareInfoProxy.updateDirectmailClearanceByExcel(data);
			if (resultInfo.isSuccess()){
				model.addAttribute("message", "操作成功");
			}else{
				model.addAttribute("message", resultInfo.getMsg().getMessage());
			}
		} catch (Exception e) {
			log.error("直邮报关导表数据异常：" + e.getMessage());
			model.addAttribute("message", "直邮报关导表数据异常,请联系管理员");
		}
		return "salesorder/directmailClearanceImportResult";
	}

	
	//读取excel
	@SuppressWarnings("unchecked")
	private List<? extends ExcelBaseDTO> readExcelList(File file, Class<? extends ExcelBaseDTO> cl) throws org.apache.poi.openxml4j.exceptions.InvalidFormatException{
		try {
			ExcelUtil eh = ExcelUtil.readExcel(file, 0);
			return eh.toEntitys(cl);
		} catch (InvalidFormatException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (ExcelParseException e) {
			log.error(e.getMessage(), e);
		} catch (ExcelContentInvalidException e) {
			log.error(e.getMessage(), e);
		} catch (ExcelRegexpValidFailedException e) {
			log.error(e.getMessage(), e);
		} finally {
		}
		return null;
	}
	//读取文件
	private File uploadFile(final HttpServletRequest request, String fileName) throws Exception {
		File file = null;
		if (request instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			final MultipartFile multipartFile = multipartRequest.getFile(fileName);
			final String pathDir = request.getSession().getServletContext().getRealPath("upload");
			final File saveFile = new File(pathDir);
		
			if (!saveFile.exists()) {
				saveFile.mkdirs();
			}

			final String fName = multipartFile.getOriginalFilename();
			final String fPName = pathDir + File.separator + fName;
			file = new File(fPName);

			try {
				multipartFile.transferTo(file);
			} catch (final IllegalStateException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new Exception("文件上传失败");
		}
		return file;
	}
}
