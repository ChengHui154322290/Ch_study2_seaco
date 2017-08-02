package com.tp.backend.controller.prd;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.result.SubmitResultInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemDetailSalesCountDto;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.prd.ItemDetailSalesCountProxy;
import com.tp.proxy.usr.UserHandler;



/***
 * 
 * 商品销售信息
 * @author caihui
 *
 */
@Controller
@RequestMapping("/item/itemDetailSales")
public class ItemDetailSalesController {

	private final static Logger LOGGER = LoggerFactory.getLogger(ItemDetailSalesController.class);
	
	
	
	@Autowired
	private ItemDetailSalesCountProxy itemDetailSalesCountProxy;
	
	
	
	/***
	 * 商品销售信息列表
	 * @param model
	 * @param isdo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Model model,
			ItemDetailSalesCountDto isdo,
			@RequestParam(value = "startPage", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize
				) throws Exception{
			ModelAndView mv = new ModelAndView();
			
			PageInfo<ItemDetailSalesCountDto>  list = itemDetailSalesCountProxy.queryListOfItemSalesCount(isdo,pageNo,pageSize);
			if(list != null){
				mv.addObject("listSales", list);
			}else{
				mv.addObject("onData","没有查找到对应信息。" );
			}
			mv.addObject("ado", isdo);
		    mv.setViewName("/item/itemDetailSales/list");
			return mv;
	}
	
	
	/***
	 * 查看详细销售信息 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewDetailSales")
	public ModelAndView viewDetailSales(Model model,Long id) throws Exception{
		ModelAndView mv = new ModelAndView();
		ItemDetailSalesCountDto dto = itemDetailSalesCountProxy.getDetailSalesCountDtoInfoById(id);
		mv.addObject("dto", dto);
	    mv.setViewName("/item/itemDetailSales/viewDetail");
		return mv;
	}
	
	
	@RequestMapping(value = "/viewDetailSalesForUpdate")
	public ModelAndView viewDetailSalesForUpdate(Model model,Long id) throws Exception{
		ModelAndView mv = new ModelAndView();
		ItemDetailSalesCountDto dto = itemDetailSalesCountProxy.getDetailSalesCountDtoInfoById(id);
		mv.addObject("dto", dto);
	    mv.setViewName("/item/itemDetailSales/viewDetailForUpdate");
		return mv;
	}
	
	@RequestMapping(value = "/copyBarcode")
	public ModelAndView copyBarcode(Model model,Long id) throws Exception{
		ModelAndView mv = new ModelAndView();
	    mv.setViewName("/item/itemDetailSales/copyBarcode");
		return mv;
	}
	
	@RequestMapping(value = "/copyPRDID")
	public ModelAndView copyPRDID(Model model,Long id) throws Exception{
		ModelAndView mv = new ModelAndView();
	    mv.setViewName("/item/itemDetailSales/copyPRDID");
		return mv;
	}
	
	
	@RequestMapping(value = "/insertWithPRDIDS")
	@ResponseBody
	public ResultInfo insertWithPRDIDS(HttpServletRequest requset) throws Exception{
		String prdidList = requset.getParameter("prdidList");
		
		if(StringUtils.isNotBlank(prdidList)){
			Long userId=0l;
			UserInfo user = UserHandler.getUser();
			if(user != null){
				userId = user.getId();
			}
			return itemDetailSalesCountProxy.insertWithPRDIDS(prdidList,userId.toString());
			
		}else{
				return  new ResultInfo(new FailInfo("数据不能为空。",1010));
		}
	
	}
	
	
	
	
	@RequestMapping(value = "/insertWithBarcodes")
	@ResponseBody
	public ResultInfo insertWithBarcodes(HttpServletRequest requset) throws Exception{
		String barcodeList = requset.getParameter("barcodeList");
		
		if(StringUtils.isNotBlank(barcodeList)){
			Long userId=0l;
			UserInfo user = UserHandler.getUser();
			if(user != null){
				userId = user.getId();
			}
			return itemDetailSalesCountProxy.insertWithBarcodes(barcodeList,userId.toString());
			
		}else{
				return new ResultInfo(new FailInfo("数据不能为空。", 1010));
		}
	
	}
	
	
	
	
	/***
	 * 调整基数
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/adjustDetailSales")
	@ResponseBody
	public ResultInfo adjustDetailSales(ItemDetailSalesCountDto dto) throws Exception {
		if (dto == null || dto.getDefaultSalesCount() ==null) {
			LOGGER.debug("数据不能为空");
			return  new ResultInfo(new FailInfo("数据不能为空。", 1010));
		}
		if(!NumberUtils.isNumber(dto.getDefaultSalesCount().toString())){
			return  new ResultInfo(new FailInfo("调整基数的数量有误", 1010));
		}
		UserInfo user = UserHandler.getUser();
		if(null!=user){
			dto.setUpdateUserId(user.getId());
		}
	    return 	itemDetailSalesCountProxy.adjustDetailSales(dto);
	}
	
	
	/***
	 * 导出所有
	 * @param ado
	 * @param request
	 * @param response
	 * @param model
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	@RequestMapping("/exportAll")
	public void exportAllExcel(ItemDetailSalesCountDto ado,HttpServletRequest request,HttpServletResponse response, Model model)throws InvalidFormatException, IOException {
		if(ado == null){
			LOGGER.error("已囤数据导出参数:" + " 为空");
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			return ;
		}
		request.setCharacterEncoding("UTF-8");
//		itemDetailSalesCountProxy.exportAll(request,response,ado);
	}
	
	
	
	@RequestMapping("/exportWithCondition")
	public void exportWithCondition(ItemDetailSalesCountDto ado,HttpServletRequest request,HttpServletResponse response, Model model)throws InvalidFormatException, IOException {
		if(ado == null || (ado.getExportIds()== null || ado.getExportIds().length < 1)){
			LOGGER.error("已囤数据导出参数:" + " 为空");
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			return ;
		}
		request.setCharacterEncoding("UTF-8");
//		itemDetailSalesCountProxy.exportWithCondition(request,response,ado);
	}
}
