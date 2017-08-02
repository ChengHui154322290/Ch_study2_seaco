package com.tp.backend.controller.prd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.annotation.Token;
import com.tp.common.vo.Constant;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.DetailDto;
import com.tp.dto.prd.ItemDto;
import com.tp.model.bse.CustomsUnitInfo;
import com.tp.model.bse.TaxRate;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.prd.ItemSkuSupplier;
import com.tp.proxy.bse.CustomsUnitInfoProxy;
import com.tp.proxy.bse.TaxRateProxy;
import com.tp.proxy.prd.ItemProxy;
import com.tp.proxy.prd.ItemSkuArtProxy;

/**
 * 商品prdid,sku纬度的修改
 * @author szy
 * @version 0.0.1
 */
@RequestMapping("/item")
@Controller
public class ItemDetailController extends AbstractBaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemDetailController.class);

	@Autowired
	private ItemProxy itemProxy;
	@Autowired
	private TaxRateProxy taxRateProxy;
	@Autowired
	private CustomsUnitInfoProxy customsUnitInfoProxy;
	@Autowired
	private ItemSkuArtProxy itemSkuArtProxy; 
	
	/**
	 * 
	 * <pre>
	 * 初始化prdid纬度信息
	 * </pre>
	 * 
	 * @param model
	 * @param detailId
	 * @return
	 * @throws Exception
	 */
	@Token(genTokenId = true)
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String detail(Model model, Long detailId,HttpServletRequest request) throws Exception {
		LOGGER.info("查询商品的prid纬度以及sku纬度信息,detailId为 {} ", detailId);
		// 初始化税率
		initRate(model);
		model.addAttribute("detailId", detailId);
		DetailDto detailDto = itemProxy.getItemDetailDtoById(detailId);
		ItemDto itemDto = itemProxy.initSpu(model, detailDto.getItemDetail().getItemId());
		model.addAttribute("item", itemDto);
		if (detailId != null) {
			itemProxy.queryItemDetail(model, detailId);
		}
		String sessionId = WebUtils.getSessionId(request);
		
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.item.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.item.name());
		return "/item/add_detail";
	}

	/**
	 * 
	 * <pre>
	 * 查询供应商信息
	 * </pre>
	 * 
	 * @param model
	 * @param detailId
	 * @param index
	 * @param pageSize
	 * @param supplierName
	 * @param supplierType
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "selectSupplier")
	public String selectSupplier(
			Model model,
			@RequestParam(value = "page", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize,
			String supplierNameQuery, String supplierTypeQuery,
			Integer hasXgSeller, Integer addNewSupplierFlag, Long skuId,
			HttpServletRequest request, Long supplierIdQuery) {
		return itemProxy.selectSupplier(model, pageNo, pageSize,
				supplierNameQuery, supplierTypeQuery, hasXgSeller,
				addNewSupplierFlag, supplierIdQuery, skuId);
	}

	@RequestMapping(value = "addSkuSupplier")
	@ResponseBody
	public ResultInfo<Boolean> addSkuSupplier(Long skuId, String skuSupplierList) {
		return itemProxy.addSkuSupplier(skuId, skuSupplierList);
	}
	
	@RequestMapping(value = "deleteSkuSupplier")
	@ResponseBody
	public ResultInfo<Boolean> deleteSkuSupplier(Long skuSupplierId) {
		return itemProxy.deleteSkuSupplier(skuSupplierId);
	}

	/**
	 * 
	 * <pre>
	 * 		更新prdid信息，保存商品属性，商品详情，图片，sku信息
	 * </pre>
	 * 
	 * @param detailDto
	 * @param picList
	 * @param skuListJson
	 * @param attrListJson
	 * @param attrItemJson
	 * @param request
	 * @param response
	 * @param bindResult
	 * @return
	 * @throws Exception
	 */
	@Token(validateToken = true)
	@RequestMapping(value = "saveDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Long> save(Model model, DetailDto detailDto, String[] picList,
			String skuListJson, String attrListJson, String attrItemJson,String dummyAttrListJson,
			ItemInfo info,String infoId){

		// 获取session用户
		if(StringUtils.isNoneBlank(infoId)){
			info.setId(Long.parseLong(infoId));
		}
		return itemProxy.saveItemDetail(info,detailDto, picList, skuListJson, attrListJson, attrItemJson, dummyAttrListJson,super.getUserName(), model);
	}

	@RequestMapping(value = "cancelSku", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Boolean> cancelSku(Model model, Long skuId){
		LOGGER.info("作废sku参数: skuId:{},userId:{}", skuId, super.getUserId());
		try{
			itemProxy.cancelSku(skuId, super.getUserName());
		}catch(Exception e){
			return new ResultInfo<Boolean>(new FailInfo(e));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}

	/**
	 * 
	 * <pre>
	 * 查询供应商列表
	 * </pre>
	 * 
	 * @param model
	 * @param skuId
	 * @return String
	 */
	@RequestMapping(value = "getSkuSupplier")
	public String getSkuSupplier(Model model, Long skuId) {
		List<ItemSkuSupplier> skuSupplierList = itemProxy.getSkuSupplier(skuId);
		model.addAttribute("skuSupplierList", skuSupplierList);
		model.addAttribute("skuId", skuId);
		return "/item/sku_supplier";

	}
	
	
	
	
	/***
	 * 获取sku对应的货号信息
	 * @param model
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "getSkuArtNumber")
	public String getSkuArtNumber(Model model, Long skuId) {
		List<ItemSkuArt> skuArtList = itemProxy.getSkuArtNumber(skuId);
		model.addAttribute("skuArtList", skuArtList);
		model.addAttribute("skuId", skuId);
		//赋值 海关key
		model.addAttribute("channels", itemProxy.getHaiGunChannel());
		return "/item/sku_art";
	}
	
	
	
	/***
	 * 添加skuId对应的货号信息
	 * @param model
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "toAddSkuArtNumber")
	public String toAddSkuArtNumber(Model model, Long skuId) {
		List<ItemSkuArt> skuArtList = itemProxy.getSkuArtNumber(skuId);
		model.addAttribute("skuArtList", skuArtList);
		model.addAttribute("skuId", skuId);
		//添加通关渠道key
		model.addAttribute("channels", itemProxy.getHaiGunChannel());
		model.addAttribute("customsUnitList", customsUnitInfoProxy.queryByObject(new CustomsUnitInfo()).getData());
		return "/item/sku_art_add";
	}
	
	@RequestMapping(value = "toEditSkuArtNumber")
	public String toEditSkuArtNumber(Model model, Long id) {
		ResultInfo<ItemSkuArt> queryResult = itemSkuArtProxy.queryById(id);
		model.addAttribute("skuArtDO", queryResult.getData());
		//添加通关渠道key
		model.addAttribute("channels", itemProxy.getHaiGunChannel());
		model.addAttribute("customsUnitList", customsUnitInfoProxy.queryByObject(new CustomsUnitInfo()).getData());
		return "/item/sku_art_edit";
	}
	
	/***
	 * 添加skuId对应的货号信息
	 * @param model
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "saveSkuArtNumber")
	@ResponseBody
	public ResultInfo<Boolean> addSkuArtNumber(ItemSkuArt skuArtDo) {
		if (skuArtDo == null) {
			LOGGER.debug("数据不能为空");
			return new ResultInfo<Boolean>(new FailInfo("新增数据不能为空。"));
		}
		
		if(skuArtDo.getBondedArea() == null){
			LOGGER.debug("新增商品备案信息，海关信息不能为空。");
			return new ResultInfo<Boolean>(new FailInfo("通关渠道不能为空。"));
		}
		
		if (StringUtils.isEmpty(skuArtDo.getItemFirstUnitCode())) {
			LOGGER.error("第一申报单位不能空");
			return new ResultInfo<>(new FailInfo("第一单位不能为空"));
		}	
		return itemProxy.saveSkuArtNumer(skuArtDo);
		
	}
	/**
	 * 查询商品单位 
	 */
	@RequestMapping(value = "customs_unit_list")
	@ResponseBody
	public ResultInfo<List<CustomsUnitInfo>> unitList(Long bondedArea){
		if (bondedArea == null) {
			LOGGER.error("通关渠道为空");
			return new ResultInfo<>(new FailInfo("通关渠道为空"));
		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("bondedArea", bondedArea);
		return customsUnitInfoProxy.queryByObject(new CustomsUnitInfo());
	}
	
	@RequestMapping(value = "deleteSkuArtInfo")
	@ResponseBody
	public ResultInfo<Boolean> deleteSkuArtInfo(Long id) {
		
		if (id == null) {
			LOGGER.debug("数据不能为空");
			return new ResultInfo<Boolean>(new FailInfo("请选择要删除的数据。"));
		}
		return itemProxy.deleteSkuArtInfo(id);
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 查询税率信息
	 * </pre>
	 * 
	 * @param model
	 */
	private void initRate(Model model) {
		// 关税
		TaxRate taxRateDO = new TaxRate();
		taxRateDO.setType(TaxRateEnum.TARRIFRATE.getType());
		taxRateDO.setStatus(Constant.ENABLED.YES);
		List<TaxRate> tarrifList = taxRateProxy.queryByObject(taxRateDO).getData();
		model.addAttribute("tarrifList", tarrifList);

		// 进项
		taxRateDO = new TaxRate();
		taxRateDO.setType(TaxRateEnum.PRIMERATE.getType());
		taxRateDO.setStatus(Constant.ENABLED.YES);
		List<TaxRate> purrateList = taxRateProxy.queryByObject(taxRateDO).getData();
		model.addAttribute("purRateList", purrateList);

		// 销项
		taxRateDO = new TaxRate();
		taxRateDO.setType(TaxRateEnum.SALERATE.getType()); //销项
		taxRateDO.setStatus(Constant.ENABLED.YES);
		List<TaxRate> saleList =taxRateProxy.queryByObject(taxRateDO).getData();
		model.addAttribute("saleRateList", saleList);
		
		// 海关税
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		params.put("type", TaxRateEnum.CUSTOMS.getType());
		List<TaxRate> customsList = taxRateProxy.queryByParam(params).getData();
		model.addAttribute("customsList", customsList);
		// 消费税
		params.put("type", TaxRateEnum.EXCISE.getType());
		List<TaxRate> exciseList = taxRateProxy.queryByParam(params).getData();
		model.addAttribute("exciseList", exciseList);
		//增值税率
		params.put("type", TaxRateEnum.ADDEDVALUE.getType());
		List<TaxRate> addedValueList = taxRateProxy.queryByParam(params).getData();
		model.addAttribute("addedValueList", addedValueList);
	}
}
