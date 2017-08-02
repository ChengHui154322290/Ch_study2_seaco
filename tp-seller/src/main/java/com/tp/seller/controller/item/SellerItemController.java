package com.tp.seller.controller.item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.Option;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.SkuExportDto;
import com.tp.model.bse.Category;
import com.tp.model.bse.Spec;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.proxy.prd.ItemDetailProxy;
import com.tp.proxy.prd.ItemExportProxy;
import com.tp.proxy.prd.ItemProxy;
import com.tp.query.prd.ItemQuery;
import com.tp.result.bse.AttributeResult;
import com.tp.result.bse.CategoryResult;
import com.tp.result.bse.SpecGroupResult;
import com.tp.seller.ao.item.SellerItemAO;
import com.tp.seller.constant.SellerConstant;
import com.tp.seller.controller.base.BaseController;
import com.tp.seller.util.SessionUtils;
import com.tp.util.DateUtil;

/**
 *  商品控制器
 * @author szy
 * @version 0.0.1
 */
@Controller
@RequestMapping("/seller/item/")
public class SellerItemController  extends BaseController {
	
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SellerItemController.class);
	
	@Autowired 
	private ItemProxy itemProxy;
	@Autowired
	private ItemDetailProxy itemDetailProxy;
	@Autowired
	private CategoryProxy categoryProxy;
	@Autowired
	private ItemExportProxy itemExportProxy;
	@Autowired
	private SellerItemAO  sellerItemAO;
	
	/**跳转的路径*/
	private final static String DETAIL_LIST_TOURL="redirect:list.htm" ;

	/**
	 * 
	 * <pre>
	 * 		日期转换
	 * </pre>
	 *
	 * @param request
	 * @param binder
	 * @throws Exception
	 */
	@InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder binder)throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}
	
	/**
	 * 
	 * <pre>
	 * 		商品新增   
	 *      item/add.htm
	 * </pre>
	 *
	 * @param model
	 * @param itemId
	 * @throws Exception
	 */
	@RequestMapping(value={"/add"},method=RequestMethod.GET)
	public String add(Model model,Long itemId,HttpServletRequest request) throws Exception{
		final Long supplierId = SessionUtils.getSupplierId(request);
		ItemDto itemDto = itemProxy.initSpu(model, itemId);
		model.addAttribute("item", itemDto);
		model.addAttribute("supplierId", supplierId);
		return "seller/sellItem/add";
	}
	
	/**
	 * 根据分类id获得对应的属性和属性值选择页面
	 * @param catId
	 * 分类ID
	 * @param model
	 * @return String
	 */
	@RequestMapping(value="attribute-list",method=RequestMethod.GET)
	public String getAttributeByCatId(Long catId,Model model){
		if(null!=catId){
			 CategoryResult categoryResult = categoryProxy.selectAttrsAndValuesByCatId(catId);
			 model.addAttribute("categoryResult", categoryResult);
		}
		return "item/subpages/add_attribute";
	}
	
	
	/**
	 * 根据分类id获得对应的属性和属性值选择页面
	 * @param catId
	 * 分类ID
	 * @param model
	 * @return String
	 */
	@RequestMapping(value="getAttributeList",method=RequestMethod.GET)
	@ResponseBody
	public List<AttributeResult> getAttributeList(Long catId,Model model){
		List<AttributeResult> listAttributeResult = itemProxy.getAttributeAndValues(catId);
		// 基础数据的属性与已经选择的属性进行关联
		itemProxy.convetSelectAttr(listAttributeResult, null);
		return listAttributeResult;
	}
	
	//基础数据获取属性key与属性值
			
	
	/**
	 * 根据分类id获得子集分类
	 * @param catId
	 * @return List<Category>
	 */
	@RequestMapping(value="category-cld")
	@ResponseBody
	public List<Category> getCldCategoryList(Long catId){
		if(null==catId){
			return null;
		}
		List<Category> categoryList = categoryProxy.selectCldListById(catId);
		return categoryList;
	}
	
	/**
	 * 根据尺码组id获得尺码列表
	 * @param sizeGroupId
	 * @return
	 */
	@RequestMapping(value="getSizeList",method=RequestMethod.GET)
	@ResponseBody
	public List<Spec> getSizeList(Long sizeGroupId){
		if(null==sizeGroupId){
			return null;
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 	 保存商品spu以及部分prdid信息
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value={"/save"},method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Long> save(String info,String details,HttpServletRequest request){
		 String userName=(String) request.getSession().getAttribute(SellerConstant.USER_NAME_KEY);
		return itemProxy.saveItem(info, details, userName);
	}
	
	/**
	 * 
	 * <pre>
	 * 	  规格查询
	 * </pre>
	 *
	 * @param model
	 * @param smallId
	 * @return
	 */
	@RequestMapping(value={"/addSpec"},method=RequestMethod.GET)
	public String getColorAndSize(Model model,Long smallId){
		if(null==smallId){
			return  "/seller/item/add_spec";
		}
		List<SpecGroupResult> specGroupList = new ArrayList<SpecGroupResult>();
		specGroupList = itemProxy.getSpecGroupResultByCategoryId(smallId);
		model.addAttribute("specGroupList", specGroupList);
		return "/seller/sellItem/add_spec";
	}
	
	/**
	 * 
	 * <pre>
	 * 	  商品列表查询
	 * </pre>
	 *
	 * @param query
	 * @param pageNo
	 * @param model
	 * @param pageSize
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value={"/list"})
	public String list(Model model,ItemQuery query,Integer page,Integer size) throws Exception{
		// 获得商品分类信息 大类
		PageInfo<ItemResultDto> pageInfo = itemDetailProxy.listItem(query, page, model, size);
		// 查询所有的品牌
		itemProxy.initBrandList(model);
		// 查询所有的商家的供应商
		itemProxy.initSupplierList(model);
		model.addAttribute("wavesSign", query.getWavesSign());
	    model.addAttribute("page", pageInfo);
	  return "seller/sellItem/list";
	}
	
    /**
     * 商品查询
     * 
     * @return
     */
    @RequestMapping(value = "/itemQuery", method = RequestMethod.POST)
    public ModelAndView itemQuery(final HttpServletRequest request) {
    	Model model=new ExtendedModelMap();
        final ModelAndView mav = new ModelAndView();
        final PageInfo<ItemResultDto> sellerOrderPageInfo = sellerItemAO.queryItemByCondition(request, model);
        mav.addObject("page", sellerOrderPageInfo);
        mav.setViewName("seller/sellItem/subpage/item_list");
        return mav;
    }
	
	@RequestMapping(value={"/copy"})
	public String copy(ItemQuery query,Integer pageNo,Model model,@RequestParam(value="detailId") String detailId){
		// 复制逻辑
		itemProxy.copyItem(detailId, super.getUserName());
		return DETAIL_LIST_TOURL;
	}
	
	/**
	 * <pre>
	 * 	  商品列表导出
	 * </pre>
	 * @param query
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value={"/exportList"})
	public void exportList(ItemQuery query,HttpServletResponse response,final HttpServletRequest request) throws Exception{
		response.setHeader("Content-disposition", "attachment; filename=sku-list.xlsx");
        response.setContentType("application/x-download");
        final Long supplierId = SessionUtils.getSupplierId(request);
        query.setSupplierId(supplierId);
        try {
        	List<SkuExportDto> dataListTemp = itemExportProxy.exportSkuList(query);
        	List<SkuExportDto> dataList=new  ArrayList<SkuExportDto>();
        	for(SkuExportDto skuExportDto :dataListTemp){
        		Long skuSupplierId=skuExportDto.getSupplierId();//供应商导入系统对应的供应商
        		if(skuSupplierId!=null &&skuSupplierId.longValue()==  supplierId.longValue()){//如果不是本供应商导入的数据去除
        			dataList.add(skuExportDto);
        		}
        	}
        	String templatePath = "/WEB-INF/classes/template/sku-list.xlsx";
			String fileName = "sku-list_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list",dataList);
        	super.exportXLS(map, templatePath, fileName, response);
		} catch (Exception e) {
			LOGGER.error("商品列表导出异常", e.getMessage());
			throw new Exception("商品列表导出异常");
		}
	}
	
	
	/**
	 * 预览sku列表
	 * @param model
	 * @param detailIds json array
	 * @param status
	 */
	@RequestMapping(value={"/previewSku"})
	public String previewSku(Model model,String detailIds,String status){
		itemProxy.getSkuListByDetailIds(model, detailIds);
		model.addAttribute("status", status);
		return "seller/sellItem/pre_sku";
	}
	
	@ResponseBody
	@RequestMapping(value={"/changeItemStatus"} ,method=RequestMethod.POST)
	public ResultInfo<List<Option>> changeItemStatus(Integer status,String skuIds){
		return itemProxy.changeItemStatus(status, skuIds);
	}
}
