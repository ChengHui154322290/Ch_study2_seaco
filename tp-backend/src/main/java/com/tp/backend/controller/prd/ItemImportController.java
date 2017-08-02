package com.tp.backend.controller.prd;

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
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemDetailForTransDto;
import com.tp.dto.prd.SkuExportDto;
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.model.prd.ItemDetailImport;
import com.tp.model.prd.ItemImportList;
import com.tp.model.prd.ItemImportLog;
import com.tp.proxy.prd.ItemDetailImportProxy;
import com.tp.proxy.prd.ItemExportProxy;
import com.tp.proxy.prd.ItemImportLogProxy;
import com.tp.proxy.prd.ItemImportProxy;
import com.tp.query.prd.ItemQuery;
import com.tp.util.DateUtil;

/**
 * 	商品导入
 * @author szy
 * @version 0.0.1
 */

@Controller
@RequestMapping("/item")
public class ItemImportController  extends AbstractBaseController{
	
	private static final Logger  LOGGER = LoggerFactory.getLogger(ItemImportController.class);
	
	@Autowired
	ItemImportProxy itemImportProxy;
	
	@Autowired
	private ItemExportProxy itemExportProxy;
	
	@Autowired
	private ItemImportLogProxy itemImportLogProxy;
	
	@Autowired
	private ItemDetailImportProxy itemDetailImportProxy;
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
	@RequestMapping("/import")
	public void importExcel(Model model,Integer status,Integer page,Integer size){
		if (null == page) {
			page = 1;
		}
		if (null == size) {
			size = 10;
		}
		if(null == status){
			status = 1;
		}
		SkuImportLogDto skuImportLogDto =  itemImportProxy.queryImportLogDto(super.getUserName(),status, page, size);
		PageInfo<ItemImportList> pageInfo = itemImportProxy.queryImportList(skuImportLogDto, page, size);
		model.addAttribute("importLog", skuImportLogDto.getItemImportLog());
		model.addAttribute("page", pageInfo);
		model.addAttribute("status",status);
	}
	/**
	 * <pre>
	 *   导入查询页面
	 * </pre>
	 *
	 * @param model
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 */
	@RequestMapping("/importNew")
	public void importExcelNew(Model model,Integer status,Integer page,Integer size){
		if (null == page) {
			page = 1;
		}
		if (null == size) {
			size = 10;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("create_user", super.getUserName());
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
		
		List<ItemImportLog> itemImportLogList = itemImportLogProxy.queryByParam(params).getData();
		if(null!=itemImportLogList && itemImportLogList.size()>0){
			ItemImportLog itemImportLog = itemImportLogList.get(0);
			params.put("log_id", itemImportLog.getId());
			model.addAttribute("importLog", itemImportLog);
		}
//		SkuImportLogDto skuImportLogDto =  itemImportProxy.queryImportLogDto(super.getUserName(),status, page, size);
//		PageInfo<ItemImportList> pageInfo = itemImportProxy.queryImportList(skuImportLogDto, page, size);
		PageInfo<ItemDetailImport> info = new PageInfo<ItemDetailImport>();
		info.setPage(page);
		info.setSize(size);
		params.put("status", status);
		PageInfo<ItemDetailImport> pageInfo = itemDetailImportProxy.queryPageByParamNotEmpty(params, info).getData();
		model.addAttribute("page", pageInfo);
		model.addAttribute("status",status);
	}
	/**
	 * 
	 * <pre>
	 * 	附件上传，下载页面
	 * </pre>
	 *
	 * @return
	 */
	@RequestMapping("/uploadFile")
	public String uploadFile(){
		return "/item/upload_file";
	}
	
	
	
	@RequestMapping("/uploadSkuExcel")
	public String uploadSkuExcel(@RequestParam(value="fieName",required=false) String fieName,
			@RequestParam(value="waveSign",defaultValue="")String waveSign,
			HttpServletRequest request,Model model){
      Map<String,Object> map = new HashMap<String,Object>();
      try {
          //Map<String,Object> retMap = itemImportProxy.importSkuExcel(request, fieName);
    	  //加锁 
    	  Map<String,Object> retMap = itemImportProxy.uploadExcelToServer(request,fieName,waveSign,super.getUserName());
          if((Boolean)retMap.get(ItemImportProxy.SUCCESS_KEY)){
              map.put(ItemImportProxy.SUCCESS_KEY, true);
              map.put("logId", retMap.get("logId"));
              map.put("fileUrl",retMap.get(ItemImportProxy.UPLOADED_FILE_KEY));
              model.addAttribute("message","操作成功");
          } else {
              map.put(ItemImportProxy.SUCCESS_KEY, false);
              map.put(ItemImportProxy.MESSAGE_KEY, retMap.get(ItemImportProxy.MESSAGE_KEY));
              model.addAttribute("message",retMap.get(ItemImportProxy.MESSAGE_KEY));
          }
         
      } catch(Exception e){
    	  LOGGER.error("上传商品导入失败");
    	  LOGGER.error("文件上传异常：",e);
    	  model.addAttribute("message","操作失败，请联系管理员");
      }
      model.addAttribute("logId",map.get("logId"));
	  return"/item/import_message";
	}
	
	/**
	 * 
	 * <pre>
	 * 	下载模板
	 *  海淘商品的模板与非海淘商品的模板
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	@RequestMapping("/downExcelTemplate")
	public void downExcelTemplate(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="waveSign",defaultValue="")String waveSign) throws IOException, InvalidFormatException{
		request.setCharacterEncoding("UTF-8");
		itemImportProxy.getTemplateDatas(request,response,waveSign);
	}
	
	/**
	 * 
	 * <pre>
	 * 	导出
	 * </pre>
	 *
	 * @param logId
	 * @param status
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception 
	 */
	@RequestMapping("/export")
	public void exportExcel (Long logId, Integer status, HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		if(null==logId){
			LOGGER.error("导出参数: logId" + " 为空");
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			return ;
		}
		try {
			LOGGER.info("导出参数: logId" + logId +" ,status=" +status);
			request.setCharacterEncoding("UTF-8");
			String templatePath = "/WEB-INF/classes/template/sku-import-list.xlsx";
			String fileName = "sku-import-list_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			Map<String, Object> map = new HashMap<String, Object>();
			List<ItemImportList> importLists = itemImportProxy.exportList(logId, status);		
			map.put("list", importLists);
			super.exportXLS(map, templatePath, fileName, response);	
		} catch (Exception e) {
			LOGGER.error("导出商品导出列表失败", e.getMessage());
			throw new Exception("导出商品导出列表失败");
		}	
	}
	
	/**批量修改商品信息导入**/
	@RequestMapping(value = "batchEdit")
	public String batchEdit(){
		return "/item/item_batch_edit";
	}
	
	@RequestMapping(value = "downEditExcelTemplate", method = RequestMethod.GET)
	public void exportOrder(HttpServletResponse response) {
		response.setHeader("Content-disposition", "attachment; filename=edit-list.xlsx");
        response.setContentType("application/x-download");
		try {
			String templatePath = "/WEB-INF/classes/template/edit-list.xlsx";
			String fileName = "edit_list_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list",new ArrayList<>());
			super.exportXLS(map, templatePath, fileName,response);
		} catch (Exception e) {
			LOGGER.error("批量修改模板下载异常", e);
		}
	}

	@RequestMapping(value = "uploadEditExcel")
	public String uploadEditExcel(HttpServletRequest request, String fieName, Model model){
		try {
			 ResultInfo<Map<String, Object>> resultInfo = itemImportProxy.batchModifyItemFromExcel(request, fieName, super.getUserName());
			 if (resultInfo.success) {
				 model.addAttribute("successCnt", resultInfo.getData().get("successCnt"));
				 model.addAttribute("failCnt", resultInfo.getData().get("failCnt"));
				 if (null == resultInfo.getData() || resultInfo.getData().isEmpty()) {
					 model.addAttribute("message", "更新成功");
				}else{
					@SuppressWarnings("unchecked")
					Map<String, String> resultMap = (Map<String, String>)resultInfo.getData().get("resultTable");
					model.addAttribute("errorMap", resultMap);
				}								
			}else {
				model.addAttribute("message", resultInfo.getMsg().getMessage());
			}
		} catch (Exception e) {
			model.addAttribute("message", "批量更新商品数据异常：" + e.getMessage());
			LOGGER.error("批量更新商品数据异常：" + e.getMessage());
		}
		return "/item/item_batch_edit_error";
	}
	/**
	 * 商品导出（翻译）
	 * @param request
	 * @param detailIdList
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/loadItemDetail")
//	public void loadItemDetail(HttpServletRequest request, HttpServletResponse response,ItemQuery query, List<Long> detailIdList, Model model) throws Exception{
	public void loadItemDetail(HttpServletRequest request, HttpServletResponse response,ItemQuery query, Model model) throws Exception{
		response.setHeader("Content-disposition", "attachment; filename=item_detail_template.xlsx");
        response.setContentType("application/x-download");
        try {
//        	if(query==null){
//        		query = new ItemQuery();
//        		query.setSupplierId(16L);
//        	}
//        	if(16L != query.getSupplierId()){
//        		throw new Exception("此功能只适用于中国通用技术集团意大利公司(Genertec Italia)");
//        	}
//        	List<ItemDetailForTransDto> detailList = itemExportProxy.exportDetailList(query,detailIdList);
        	List<ItemDetailForTransDto> detailList = itemExportProxy.exportDetailList(query);
        	String templatePath = "/WEB-INF/classes/template/item_detail_template.xlsx";
			String fileName = "item_detail_template_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list",detailList);
        	super.exportXLS(map, templatePath, fileName, response);
		} catch (Exception e) {
			LOGGER.error("商品列表导出异常", e.getMessage());
			throw new Exception("商品详情列表导出异常");
		}
	}
	
	/**
	 * <pre>
	 * 	附件上传，下载页面
	 * </pre>
	 * @return
	 */
	@RequestMapping("/uploadFileNew")
	public String uploadFileNew(){
		return "/item/upload_file_new";
	}
	
	/**
	 * 商品详情导入（翻译）
	 */
	@RequestMapping("/uploadItemDetail")
	public String uploadItemDetail(@RequestParam(value="fieName",required=false) String fieName,HttpServletRequest request, Model model) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
        try {
        	Map<String,Object> retMap = itemImportProxy.uploadDetailExcelToServer(request,fieName,super.getUserName());
        	
        	if((Boolean)retMap.get(ItemImportProxy.SUCCESS_KEY)){
                map.put(ItemImportProxy.SUCCESS_KEY, true);
                map.put("logId", retMap.get("logId"));
                map.put("fileUrl",retMap.get(ItemImportProxy.UPLOADED_FILE_KEY));
                model.addAttribute("message","操作成功");
            } else {
                map.put(ItemImportProxy.SUCCESS_KEY, false);
                map.put(ItemImportProxy.MESSAGE_KEY, retMap.get(ItemImportProxy.MESSAGE_KEY));
                model.addAttribute("message",retMap.get(ItemImportProxy.MESSAGE_KEY));
            }
           
        } catch(Exception e){
      	  LOGGER.error("上传商品详情导入失败");
      	  LOGGER.error("文件上传异常：",e);
      	  model.addAttribute("message","操作失败，请联系管理员");
        }
        model.addAttribute("logId",map.get("logId"));
        return "/item/import_message"; 
	}
}
