package com.tp.proxy.prd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.BseConstant;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mem.MemberConstant.Bool;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.Option;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.DetailDto;
import com.tp.dto.prd.DetailSkuDto;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.dto.prd.ItemDto;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.enums.ItemSendTypeEnum;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.AttributeValue;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.CustomsUnitInfo;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.prd.ItemSkuSupplier;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.bse.BrandProxy;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.proxy.bse.ClearanceChannelsProxy;
import com.tp.proxy.bse.CustomsUnitInfoProxy;
import com.tp.proxy.bse.DictionaryInfoProxy;
import com.tp.proxy.bse.DistrictInfoProxy;
import com.tp.proxy.bse.SpecGroupProxy;
import com.tp.result.bse.AttributeResult;
import com.tp.result.bse.CategoryResult;
import com.tp.result.bse.SpecGroupResult;
import com.tp.result.sup.SupplierResult;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.bse.ISpecService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.service.prd.IItemDetailSpecService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.prd.IItemService;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * <pre>
 * 商品管理的AO层操作
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Service
public class ItemProxy {
	private static final Logger logger = LoggerFactory.getLogger(ItemProxy.class);
	@Autowired
	private IItemManageService itemManageService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private ISpecGroupService specGroupService;
	@Autowired
	private ISpecService specService;
	@Autowired
	private IItemDetailSpecService detailSpecService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private CategoryProxy categoryServiceProxy;
	
	@Autowired
	private DistrictInfoProxy districtInfoProxy;

	@Autowired
	private BrandProxy brandProxy;
	@Autowired
	private SpecGroupProxy specGroupProxy;
	@Autowired
	private DictionaryInfoProxy dictionaryInfoProxy;
	
	@Autowired
	private IFreightTemplateService  freightTemplateService ;
	
	@Autowired
	private IForbiddenWordsService forbiddenWordsService;
	
	@Autowired
	private IPurchasingManagementService purchasingManagementService;
	
	@Autowired
	private ItemValidateProxy itemValidateProxy;
	
	@Autowired
	private ClearanceChannelsProxy clearanceChannelsProxy;
	
	@Autowired
	private CustomsUnitInfoProxy customsUnitInfoProxy;
	
	@Autowired
	private IItemSkuArtService itemSkuArtService;

	
	private final static Logger LOGGER = LoggerFactory.getLogger(ItemProxy.class);
	
	public static final String Message = "message";
	
	
	
	/**
	 * 
	 * <pre>
	 *  初始化商品 
	 * </pre>
	 *
	 * @param item
	 * @return
	 */
	private ItemDto initItem(ItemDto item) {
		ItemInfo info = null;
		info = item.getItemInfo();
		info.setCreateTime(new Date());
		info.setUpdateTime(new Date());
		// 登陆用户中取值
		info.setCreateUser(Constant.AUTHOR_TYPE.SYSTEM);
		info.setUpdateUser(Constant.AUTHOR_TYPE.SYSTEM);
		item.setItemInfo(info);
		List<ItemDetail> detailList = item.getItemDetailList();
		List<ItemDetail> detailListTmp = new ArrayList<ItemDetail>();
		if (CollectionUtils.isNotEmpty(detailList)) {
			for (ItemDetail detail : detailList) {
				detail.setCreateTime(new Date());
				detail.setUpdateTime(new Date());
				detail.setCreateUser(Constant.AUTHOR_TYPE.SYSTEM);
				detail.setUpdateUser(Constant.AUTHOR_TYPE.SYSTEM);
				detailListTmp.add(detail);
			}
		}
		
		if(null!=detailList){
			detailList = detailListTmp;
			item.setItemDetailList(detailList);
		}
		return item;
	}
	
	/**
	 * 
	 * <pre>
	 *   初始化品牌
	 * </pre>
	 *
	 * @param model
	 * @throws Exception
	 */
	public void initBrandList(Model model) {
		// 查询所有的品牌
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		List<Brand> brandList = brandProxy.queryByParam(params).getData();
		model.addAttribute("brandList", brandList);
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param model
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public ItemDto initSpu(Model model, Long itemId) throws Exception {
		// 获得商品分类信息 大类
		List<Category> categoryList = categoryService.getFirstCategoryList();
		model.addAttribute("categoryList", categoryList);
		initBrandList(model);
		Map<String,Object> queryParams = new HashMap<String,Object>();
		queryParams.put("code", DictionaryCode.c1001.getCode());
		List<DictionaryInfo> unitList = dictionaryInfoProxy.queryByParam(queryParams).getData();
		model.addAttribute("unitList", unitList);
		// 尺码组
		queryParams.clear();
		queryParams.put("status", Constant.ENABLED.YES);
		List<SpecGroup> specGroupList = specGroupProxy.queryByParam(queryParams).getData();
		model.addAttribute("specGroupList", specGroupList);
	
		ItemDto itemDto = null;
		if (itemId != null) {
			itemDto = getItemByItemId(itemId);
			// 规格组查询
			List<ItemDetail> detailDOList = new ArrayList<ItemDetail>();
			detailDOList.addAll(itemDto.getItemDetailList());
			// 查询规格
			List<Long> specGroupIds = new ArrayList<Long>();
			detailDOList = getAllDetailWithSpec(detailDOList, specGroupIds);
			List<SpecGroup> groupList = new ArrayList<SpecGroup>();
			if (!specGroupIds.isEmpty()) {
				groupList = getAllSpecGroups(specGroupIds);
			}
			model.addAttribute("groupList", groupList);
			String specGroupIdsStr = specGroupIds.toString();
			if(StringUtil.isNoneBlank(specGroupIdsStr)){
				model.addAttribute("specGroupIdsStr", specGroupIdsStr.substring(1, specGroupIdsStr.length()-1));
			}
			// 展示prdid是否上架
			judgeSkuOnsale(detailDOList);

			itemDto.setItemDetailList(detailDOList);
			ItemInfo info = itemDto.getItemInfo();
			// 查询大类中类小类的名称
			Category c = categoryService.queryById(info.getLargeId());
			Category c1 = categoryService.queryById(info.getMediumId());
			Category c2 = categoryService.queryById(info.getSmallId());
			info.setLargeName(c.getName());
			info.setMediumName(c1.getName());
			info.setSmallName(c2.getName());
			List<Category> midCategoryList = categoryServiceProxy.selectCldListById(info.getLargeId());
			List<Category> smlCategoryList = categoryServiceProxy.selectCldListById(info.getMediumId());
			model.addAttribute("midCategoryList", midCategoryList);
			model.addAttribute("smlCategoryList", smlCategoryList);
			itemDto.setItemInfo(info);
		}
		return itemDto;
	}
	
	/**
	 * 
	 * <pre>
	 *  添加供应商
	 * </pre>
	 *
	 * @param skuId
	 * @param skuSupplierList
	 * @return
	 */
	public ResultInfo<Boolean> addSkuSupplier(Long skuId,String skuSupplierList) {
		LOGGER.info("维护sku与供应商的对应关系，传入参数:skuId{} ,skuSupplierList:{} ", skuId,skuSupplierList);
		if (null==skuId) {
			return new ResultInfo<>(new FailInfo("sku不能为空"));
		}
		try {
			itemManageService.addSkuSupplier(skuId, skuSupplierList);
			return new ResultInfo<>(Boolean.TRUE);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResultInfo<>(new FailInfo("sku不能为空"));
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 	删除供应商
	 * </pre>
	 *
	 * @param skuSupplierId
	 * @return
	 * @throws ItemServiceException
	 */
	public ResultInfo<Boolean> deleteSkuSupplier(Long skuSupplierId) {
		LOGGER.info("删除供应商的对应关系，传入参数:skuSupplierId{}  ", skuSupplierId);
		if (null==skuSupplierId) {
			return new ResultInfo<>(new FailInfo("删除主键不能为空"));
		}
		try {
			itemManageService.deleteSkuSupplier(skuSupplierId);
			return new ResultInfo<>(Boolean.TRUE);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResultInfo<>(new FailInfo("sku不能为空"));
		}
	}

	/**
	 * 
	 * <pre>
	 * 保存spu和部分prdid信息
	 * </pre>
	 *
	 * @param item
	 * @return
	 * @throws ItemServiceException
	 */
	public ResultInfo<Long> saveItem(String info,String details,String userName ) {
		//校验逻辑
		JSONArray aryJson=JSONArray.fromObject(details);
		LOGGER.info("item_info: "+info);
		LOGGER.info("item_detail: "+details);
		ItemInfo infoDO = (ItemInfo) JSONObject.toBean(JSONObject.fromObject(info), ItemInfo.class);
		ItemDetail[] detailList = (ItemDetail[]) JSONArray.toArray(JSONArray.fromObject(aryJson),ItemDetail.class);
		
		ItemDto item = new ItemDto();
		if(null==infoDO.getId()){
			Category category  = categoryService.queryById(infoDO.getSmallId());
			if(null!=category){
				infoDO.setSmallCode(category.getCode());//设置小类的编码关联spu的编码
			}
		}
		item.setItemInfo(infoDO);
		item.setItemDetailList(Arrays.asList(detailList));
		ResultInfo<Boolean> resultInfo = itemValidateProxy.validItem(infoDO, detailList,2);
		if(!resultInfo.success){
			return new ResultInfo<Long>(resultInfo.msg);
		}
		//
		try{
			Long itemId = itemManageService.saveItem(initItem(item),userName);
			return new ResultInfo<>(itemId);
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
			return new ResultInfo<>(new FailInfo("保存失败"));
		}
		
	}

	/**
	 * 
	 * <pre>
	 * 	查询prid明细以及sku纬度 信息
	 * </pre>
	 *
	 * @param model
	 * @param detailId
	 * @throws Exception 
	 */
	public void queryItemDetail(Model model, Long detailId) throws Exception {
		DetailDto detailDto = getItemDetailDtoById(detailId);
		model.addAttribute("detail", detailDto.getItemDetail());
		model.addAttribute("desc", detailDto.getItemDesc());
		model.addAttribute("descMobile", detailDto.getItemDescMobile());
		model.addAttribute("picList", detailDto.getPicList());
		model.addAttribute("detailSkuList", detailDto.getDetailSkuList()); 
		// model.addAttribute("attrItemList",detailDto.getAttrItemList());
		model.addAttribute("attrList", detailDto.getAttrList());

		Iterator<ItemAttribute> itemAttributeIterator = detailDto.getDummyAttrList().iterator();
		while (itemAttributeIterator.hasNext()){
			ItemAttribute attribute = itemAttributeIterator.next();
			if(attribute.getAttrKey() !=null && attribute.getAttrKey().equals("effectTimeStart")){
				model.addAttribute("effectTimeStart",attribute.getAttrValue());
				itemAttributeIterator.remove();
			}else if(attribute.getAttrKey() !=null && attribute.getAttrKey().equals("effectTimeEnd")){
				model.addAttribute("effectTimeEnd",attribute.getAttrValue());
				itemAttributeIterator.remove();
			}else if(attribute.getAttrKey() !=null && attribute.getAttrKey().equals("includeFestival")){
				model.addAttribute("includeFestival",attribute.getAttrValue());
				itemAttributeIterator.remove();
			}
		}
		model.addAttribute("dummyAttrList", detailDto.getDummyAttrList());
		ItemDto itemDto = getItemByItemId(detailDto.getItemDetail().getItemId());
		Long categoryId = itemDto.getItemInfo().getSmallId();
		//基础数据获取属性key与属性值
		List<AttributeResult> listAttributeResult = getAttributeAndValues(categoryId);
		// 基础数据的属性与已经选择的属性进行关联
		convetSelectAttr(listAttributeResult, detailDto.getAttrItemList());
		model.addAttribute("listAttributeResult", listAttributeResult);
		//运费模板
		List<FreightTemplate> freightTemplateList = getAllFreightTemplate();
		model.addAttribute("freightTemplateList", freightTemplateList);
		//适用年龄 
		List<DictionaryInfo>  applyAgeList  = getAllApplyAgeList();
		model.addAttribute("applyAgeList", applyAgeList);
	
		//销售规格
		model.addAttribute("detailSpecList", detailDto.getItemDetailSpecList());
		//添加是否有西客商城商家标识符
		model.addAttribute("hasXgSeller", detailDto.getHasXgSeller()); 
		
		//获取国家数据
		List<DistrictInfo> listCountry = getAllCountryList();
		model.addAttribute("listCountry", listCountry);
		//获取配送方式数据
		model.addAttribute("listSendType",ItemSendTypeEnum.values()); 
	}

	/**
	 * <pre>
	 * 	 初始化prdid以及属性信息
	 * </pre>
	 *
	 * @param detailDto
	 * @param skuListJson
	 * @param attrListJson
	 * @param attrItemJson
	 * @param request
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void initDetail(DetailDto detailDto, String skuListJson,
			String attrListJson, String attrItemJson,String dummyAttrListJson) {
		List<DetailSkuDto> detailSkuList = (List<DetailSkuDto>) JSONArray.toList(
				JSONArray.fromObject(skuListJson), DetailSkuDto.class);
		List<ItemAttribute> attrList = (List<ItemAttribute>) JSONArray.toList(
				JSONArray.fromObject(attrListJson), ItemAttribute.class);
		List<ItemAttribute> attrItemList = (List<ItemAttribute>) JSONArray.toList(
				JSONArray.fromObject(attrItemJson), ItemAttribute.class);
		detailDto.setDetailSkuList(detailSkuList);
		detailDto.setAttrItemList(attrItemList);
		detailDto.setAttrList(attrList);
		detailDto.setDummyAttrList(com.alibaba.fastjson.JSONArray.parseArray(dummyAttrListJson,ItemAttribute.class));
	}

	/**
	 * 
	 * <pre>
	 * 	保存商品的prdid以及sku信息
	 * </pre>
	 *
	 * @param detailDto
	 * @return
	 */
	public ResultInfo<Long> saveItemDetail(ItemInfo infoDO,
			DetailDto detailDto,String[] picList,String skuListJson,String attrListJson,
			String attrItemJson,String dummyAttrListJson,String userName,Model model){
		
		ItemInfo info = new ItemInfo();
		if(null==infoDO){
			info = null;
		}else{
			info.setId(infoDO.getId());
			info.setMainTitle(infoDO.getMainTitle());
			info.setRemark(infoDO.getRemark());
			info.setBrandId(infoDO.getBrandId());
			info.setLargeId(infoDO.getLargeId());
			info.setMediumId(infoDO.getMediumId());
			info.setSmallId(infoDO.getSmallId());
			info.setUnitId(infoDO.getUnitId());
			info.setCreateUser(userName);
			info.setUpdateTime(new Date());
			info.setUpdateUser(userName);
			info.setSupplierId(infoDO.getSupplierId());
			Category category  = categoryService.queryById(infoDO.getSmallId());
			if(null!=category){
				info.setSmallCode(category.getCode());//设置小类的编码关联spu的编码
			}
		}
		List<String> picListTmp =  new ArrayList<String>();
		if(null!=picList){
			picListTmp = Arrays.asList(picList);
		}
		detailDto.setPicList(picListTmp);
		LOGGER.info("skuListJson: {} ", skuListJson);
		LOGGER.info("attrListJson: {}", attrListJson);
		LOGGER.info("attrItemJson: {}", attrItemJson);
		LOGGER.info("dummyAttrJson: {}", dummyAttrListJson);
		initDetail(detailDto, skuListJson, attrListJson, attrItemJson,dummyAttrListJson);
		// 校验逻辑
		//validItem(info, null, 1);
		//validItemDetail(info,detailDto);
		//validItemMobileDescWhenPutOn(detailDto);
		//海淘商品
		try {
			Long detailId = itemManageService.saveItemDetail(info,detailDto,userName);
			return new ResultInfo<Long>(detailId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResultInfo<>(new FailInfo("保存商品信息出错"));
		}
	}

	/**
	 * 
	 * <pre>
	 * 通过itemid查询ItemDto
	 * </pre>
	 *
	 * @param itemId
	 * @return
	 */
	public ItemDto getItemByItemId(Long itemId) {
		return itemManageService.getItemByItemId(itemId,2);
	}

	/**
	 * 
	 * <pre>
	 * 查询商品detail信息
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @throws ItemServiceException
	 */
	public ItemDetail getItemDetailById(Long detailId)
			throws ItemServiceException {
		return itemManageService.getItemDetailByDetailId(detailId);
	}

	/**
	 * 
	 * <pre>
	 * 查询商品DetailDto信息
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @throws ItemServiceException
	 */
	public DetailDto getItemDetailDtoById(Long detailId)
			throws ItemServiceException {
		DetailDto detailDto = new DetailDto();
		ItemDetail detailDO = itemManageService.getItemDetailByDetailId(detailId);
		detailDto.setItemDetail(detailDO);

		ItemDesc descDO = itemManageService.getDescByDetailId(detailId);
		ItemDescMobile descMobileDO = itemManageService.geteDescMobilByDetailId(detailId);
		detailDto.setItemDesc(descDO);
		detailDto.setItemDescMobile(descMobileDO);
		List<ItemPictures> picturesList = itemManageService.getPicsByDetailId(detailId);
		List<String> picList = new ArrayList<String>();
		for (ItemPictures pic : picturesList) {
			picList.add(pic.getPicture());
		}
		detailDto.setPicList(picList);
		List<ItemSku> detailSkuList = itemManageService.getDetailSkuListByDetailId(detailId);
		List<DetailSkuDto> detailSkuArray = new ArrayList<DetailSkuDto>();
		if(CollectionUtils.isNotEmpty(detailSkuList)){
			for(ItemSku detailSku : detailSkuList){
				if(detailSku.getSaleType()==ItemSaleTypeEnum.SEAGOOR.getValue()){
					detailDto.setHasXgSeller(ItemConstant.HAS_XIGOU_SELLER);
				}
				DetailSkuDto detailSkuDto = new DetailSkuDto();
				org.springframework.beans.BeanUtils.copyProperties(detailSku, detailSkuDto);
				detailSkuArray.add(detailSkuDto);
			}
		}
		detailDto.setDetailSkuList(detailSkuArray);
		List<ItemAttribute> attrAllList = itemManageService.getAttributeByDetailId(detailId);

		List<ItemAttribute> attrItemArray = new ArrayList<ItemAttribute>();
		List<ItemAttribute> attrListArray = new ArrayList<ItemAttribute>();
		List<ItemAttribute> attrItemList = new ArrayList<ItemAttribute>();
		List<ItemAttribute> attrList = new ArrayList<ItemAttribute>();
		List<ItemAttribute> dummyAttrList = new ArrayList<>();
		for (ItemAttribute attr : attrAllList) {
			if (attr.getCustom().equals(ItemConstant.ATTR_FROM_BASEDATA)) {
				attrItemList.add(attr);
			} else if (attr.getCustom().equals(ItemConstant.ATTR_CUSTOM)) {
				attrList.add(attr);
			}else if(attr.getCustom().equals(ItemConstant.ATTR_DUMMY)){
				dummyAttrList.add(attr);
			}
		}
		
		
		List<ItemDetailSpec> detailSpecList = itemManageService.getDetailSpecListByDetailId(detailId);
		List<ItemDetailSpec> detailSpecDtoList = new ArrayList<ItemDetailSpec>();
		List<Long> specIds = new ArrayList<Long>();
		List<Long> specGroupIds = new ArrayList<Long>();
		for(ItemDetailSpec detailSpec: detailSpecList){
			ItemDetailSpec detailSpecDto= new ItemDetailSpec();
			try {
				BeanUtils.copyProperties(detailSpecDto, detailSpec);
				specIds.add(detailSpec.getSpecId());
				specGroupIds.add(detailSpec.getSpecGroupId());
				detailSpecDtoList.add(detailSpecDto);
			} catch (IllegalAccessException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		List<SpecGroup> specGroup =  specGroupService.selectListSpecGroup(specGroupIds, ItemConstant.EFFECTIVE_DATAS);
		List<Spec> specList = specService.selectListSpec(specIds, ItemConstant.EFFECTIVE_DATAS);
		
		for(ItemDetailSpec detailSpecDto: detailSpecDtoList){
			for(SpecGroup group : specGroup){
				if(group.getId().equals(detailSpecDto.getSpecGroupId())){
					detailSpecDto.setSpecGroupName(group.getName());
					break;
				}
			}
			for(Spec spec : specList){
				if(spec.getId().equals(detailSpecDto.getSpecId())){
					detailSpecDto.setSpecName(spec.getSpec());
					break;
				}
			}
		}
		detailDto.setItemDetailSpecList(detailSpecDtoList);
		detailDto.setAttrItemList(attrItemList);
		detailDto.setAttrList(attrList);
		detailDto.setDummyAttrList(dummyAttrList);
		return detailDto;
	}
	
	/**
	 * 
	 * <pre>
	 *   作废sku
	 * </pre>
	 *
	 * @param skuId
	 * @param userName
	 * @return
	 */
	public Integer cancelSku(Long skuId,String userName){
		return itemManageService.cancelSku(skuId, userName);
	}

	
	/**
	 * 
	 * <pre>
	 * 		根据 一组 规格Id获取 规格 列表 信息
	 * </pre>
	 *
	 * @param ids
	 * @return
	 */
	public List<Spec> getDetailSpecs(List<Long> ids) {
		return specService.selectListSpec(ids, ItemConstant.EFFECTIVE_DATAS);
	}

	/**
	 * 
	 * <pre>
	 * 初始化供应商类型
	 * </pre>
	 *
	 * @return
	 */
	public Map<String, String> initSupplierType() {
		// 商家类型
		Map<String, String> supplierTypes = new HashMap<String, String>();
		SupplierType[] values = SupplierType.values();
		for (SupplierType sType : values) {
			supplierTypes.put(sType.getValue(), sType.getName());
		}
		return supplierTypes;
	}
	
	/**
	 * 
	 * <pre>
	 *   获取属性
	 * </pre>
	 *
	 * @param categoryId
	 * @return
	 */
	public List<AttributeResult> getAttributeAndValues(Long categoryId) {
		CategoryResult categoryResult = categoryService.getAttributeAndValues(
				categoryId, ItemConstant.EFFECTIVE_DATAS);
		List<AttributeResult> list = categoryResult.getAttributesAndValues();
		if(CollectionUtils.isNotEmpty(list)){ //删除属性值为有效，下面的属性值为无效的
			Iterator<AttributeResult> it = list.iterator();
		    while (it.hasNext()) {
		    	AttributeResult res = (AttributeResult) it.next();
		    	if(CollectionUtils.isEmpty(res.getAttributeValues())){
		    		it.remove();
		    	}
		    }
		}
		return list;
	}
	
	/**
	 * 
	 * <pre>
	 * 	匹配属性组与商品关联信息.
	 * </pre>
	 *
	 * @param listAttributeResult
	 * @param attrs
	 */
	public void convetSelectAttr(List<AttributeResult> listAttributeResult,
			List<ItemAttribute> attrs) {
		for (AttributeResult a : listAttributeResult) {
			List<AttributeValue> attributeValue = a.getAttributeValues();
			if (CollectionUtils.isNotEmpty(attrs)) {
				for (ItemAttribute attr : attrs) {
					if (attr.getAttrId().equals(a.getAttribute().getId())) {
						for (AttributeValue attrValue : attributeValue) {
							if (attr.getAttrValueId().equals(attrValue.getId())) {
								attrValue.setIsSelect(ItemConstant.ATTR_SELECTED);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * <pre>
	 * 查询自营的供应商列表
	 * </pre>
	 *
	 * @param skuId
	 * @return
	 */
	public List<ItemSkuSupplier> getSkuSupplier(Long skuId) {
		return itemManageService.getSkuSupplierListBySkuId(skuId);
	}
	
	
	
	/***
	 * 通过skuID查询货号信息
	 * @param skuId
	 * @return
	 */
	public List<ItemSkuArt> getSkuArtNumber(Long skuId) {
		return itemManageService.getSkuArtNumberBySkuId(skuId);
	}
	
	
	/***
	 * 保存sku对应的货号信息
	 * @param skuArtDO
	 * @param skuId
	 * @return
	 */
	public ResultInfo<Boolean>  saveSkuArtNumer(ItemSkuArt skuArtDO){
		try{
			skuArtDO = trimItemSkuArt(skuArtDO);
			ResultInfo<Boolean> validateResult = validateItemSkuArt(skuArtDO);
			if (!validateResult.isSuccess()) return validateResult;
			if (skuArtDO.getId() == null){
				itemManageService.saveSkuArt(skuArtDO);	
			}else{
				itemSkuArtService.updateNotNullById(skuArtDO);
			}
			
		}catch (Exception e) {
			logger.error("保存出错:{},\r\n参数:{},{}",e,skuArtDO,skuArtDO.getSkuId());
			return new ResultInfo<>(new FailInfo("系统出错"));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	public ItemSkuArt trimItemSkuArt(ItemSkuArt skuArtDo){
		skuArtDo.setArticleNumber(StringUtil.trim(skuArtDo.getArticleNumber()));
		skuArtDo.setHsCode(StringUtil.trim(skuArtDo.getHsCode()));
		skuArtDo.setItemFirstUnit(StringUtil.trim(skuArtDo.getItemFirstUnit()));
		skuArtDo.setItemFirstUnitCode(StringUtil.trim(skuArtDo.getItemFirstUnitCode()));
		skuArtDo.setItemSecondUnit(StringUtil.trim(skuArtDo.getItemSecondUnit()));
		skuArtDo.setItemSecondUnitCode(StringUtil.trim(skuArtDo.getItemSecondUnitCode()));
		return skuArtDo;
	}
	
 	private ResultInfo<Boolean> validateItemSkuArt(ItemSkuArt skuArtDO){	
 		//校验是否sku对应在海关的货号
		Map<String, Object> params = new HashMap<>();
		params.put("bondedArea", skuArtDO.getBondedArea());
		params.put("skuId", skuArtDO.getSkuId());
		if (skuArtDO.getId() != null){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id not in(" + skuArtDO.getId() + ") ");
		}
		ItemSkuArt checkBonedAreaResult = itemSkuArtService.queryUniqueByParams(params);
		if(checkBonedAreaResult != null){
			return new ResultInfo<>(new FailInfo("同一个sku只能有一种通关渠道,修改,请删除原有数据。"));
		}
		params.clear();
		params.put("bondedArea", skuArtDO.getBondedArea());
		params.put("articleNumber", skuArtDO.getArticleNumber());
		if (skuArtDO.getId() != null){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id not in (" + skuArtDO.getId() + ") ");
		}
		//(暂时规定,待关务确定)
		ItemSkuArt checkArticleNumberResult = itemSkuArtService.queryUniqueByParams(params);
		if (checkArticleNumberResult != null) {
			return new ResultInfo<>(new FailInfo("同一保税区的备案号已存在，与系统SKU一一对应，不允许重复：" + skuArtDO.getArticleNumber()));
		}
		if(skuArtDO.getBondedArea() == null){
			return new ResultInfo<>(new FailInfo("通关渠道不能为空。"));
		}		
		if (StringUtils.isEmpty(skuArtDO.getItemFirstUnitCode())) {
			return new ResultInfo<>(new FailInfo("第一单位不能为空"));
		}	
		
		if (StringUtil.isEmpty(skuArtDO.getArticleNumber())){
			return new ResultInfo<>(new FailInfo("料号不能为空"));
		}
		
		ResultInfo<List<CustomsUnitInfo>> unitResultInfo = customsUnitInfoProxy.queryByObject(new CustomsUnitInfo());
		if (!unitResultInfo.isSuccess() || unitResultInfo.getData() == null) {
			LOGGER.error("海关对应申报单位数据查询失败");
			return new ResultInfo<>(new FailInfo("申报单位查询失败"));
		}
		Map<String, CustomsUnitInfo> code2UnitInfoMap = new HashMap<>();
		for (CustomsUnitInfo unitInfo : unitResultInfo.getData()) {
			code2UnitInfoMap.put(unitInfo.getCode(), unitInfo);
		}
		if(StringUtils.isNotEmpty(skuArtDO.getItemFirstUnitCode())){
			CustomsUnitInfo firstUnitInfo = code2UnitInfoMap.get(skuArtDO.getItemFirstUnitCode());
			if (firstUnitInfo == null) {
				return new ResultInfo<>(new FailInfo("第一申报单位Code对应单位信息不存在"));
			}
			skuArtDO.setItemFirstUnit(firstUnitInfo.getName());
		}
		if (StringUtils.isNotEmpty(skuArtDO.getItemSecondUnitCode())) {
			CustomsUnitInfo secondUnitInfo = code2UnitInfoMap.get(skuArtDO.getItemSecondUnitCode());
			if (secondUnitInfo == null) {
				return new ResultInfo<>(new FailInfo("第二单位code对应单位信息不存在"));
			}
			skuArtDO.setItemSecondUnit(secondUnitInfo.getName());
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	/***
	 * 删除sku货号信息
	 * @param id
	 * @return
	 */
	public ResultInfo<Boolean>  deleteSkuArtInfo(Long id){
		try{
			itemManageService.deleteSkuArtInfo(id);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,id);
			return new ResultInfo<>(failInfo);
		}
	}
	
	
	

	/**
	 * 
	 * <pre>
	 *  取所有有效的规格组，以及规格组下面的所有的规格
	 * </pre>
	 *
	 * @return
	 */
	public List<SpecGroupResult> getSpecGroupResultByCategoryId(Long categoryId) {
		return specGroupService.getSpecGroupResultByCategoryId(categoryId);
	}
	
	
	/**
	 * 获取prd的规格组与规格
	 * @param specGroupIds
	 * @param status
	 * @return
	 */
	public List<SpecGroupResult> getSpecGroupResultBySpecGroupIds(List<Long> specGroupIds,int status) {
		return specGroupService.selectListSpecGroupResult(specGroupIds,status);
	}

	
	/**
	 * 获取prd的规格组与规格
	 * @param specGroupIdsStr
	 * @param status
	 * @return
	 */
	public List<SpecGroupResult> getSpecGroupResultBySpecGroupIds(String specGroupIdsStr,int status) {
		List<Long> specGroupIds = new ArrayList<Long>();
		if(StringUtil.isBlank(specGroupIdsStr)){
			return null;
		}
		
		String [] strs = specGroupIdsStr.split(",");
		for(String str : strs){
			Long specGroupId  = Long.parseLong(str.trim());
			if(!specGroupIds.contains(specGroupId)){
				specGroupIds.add(specGroupId);
			}
		}
		return getSpecGroupResultBySpecGroupIds(specGroupIds,status);
	}
	
	/**
	 * 获取prd的所有规格组与规格
	 * status =2 
	 * @param specGroupIdsStr
	 * @return
	 */
	public List<SpecGroupResult> getSpecGroupResultBySpecGroupIds(String specGroupIdsStr) {
		return getSpecGroupResultBySpecGroupIds(specGroupIdsStr,1);
	}
	/**
	 * 
	 * <pre>
	 * 	 获取 所有的prdid id
	 * </pre>
	 *
	 * @param list
	 * @return
	 */
	public List<Long> getAllDetailIds(List<ItemDetail> list) {
		List<Long> ids = new ArrayList<Long>();
		for (ItemDetail detail : list) {
			ids.add(detail.getId());
		}
		return ids;
	}

	/**
	 * 
	 * <pre>
	 * 	 获取 所有的具体规格 id
	 * </pre>
	 *
	 * @param list
	 * @return
	 */
	public List<Long> getAllSpecIds(List<ItemDetailSpec> list) {
		List<Long> ids = new ArrayList<Long>();
		for (ItemDetailSpec spec : list) {
			ids.add(spec.getSpecId());
		}
		return ids;
	}
	
	/**
	 * 
	 * <pre>
	 * 		获取所有的运费模板(所有有效的单品的运费模板)
	 * 		
	 * </pre>
	 *
	 * @return
	 * @throws Exception
	 */
	public List<FreightTemplate> getAllFreightTemplate(){
		List<FreightTemplate> list =freightTemplateService.selectByCalculateMode(ItemConstant.SINGLE_FREIGHTTEMPLATE_STATUS);
		return list;
	}
	


	public List<DictionaryInfo> getAllApplyAgeList(){
		// 适用年龄
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", DictionaryCode.c1003.getCode());
		List<DictionaryInfo> list = dictionaryInfoProxy.queryByParam(params).getData();
		return list;
	}
	
	
	/***
	 * 获取国家数据
	 * 
	 */
	public List<DistrictInfo> getAllCountryList(){ 
		// 适用年龄
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "type in ("+BseConstant.DISTRICT_LEVEL.CONTRY.code+","+BseConstant.DISTRICT_LEVEL.PROVINCE.code+")");
		List<DistrictInfo> list = districtInfoProxy.queryByParam(params).getData();
		return list;
	}

	
	
	
	/**
	 * 
	 * <pre>
	 * 获取所有代规格的所有的信息
	 * </pre>
	 *
	 * @param list
	 * @return
	 */
	public List<ItemDetail> getAllDetailWithSpec(List<ItemDetail> list,
			List<Long> specGroupIds) {
		List<Long> detailIds = getAllDetailIds(list);
		List<ItemDetailSpec> detailSpecList = detailSpecService
				.selectDetailSpecListByDetailIds(detailIds);
		// 获取基础数据的规格
		List<Long> specIds = getAllSpecIds(detailSpecList);

		List<Spec> specList = new ArrayList<Spec>();
		// 均码
		boolean flag = false;
		if (!detailSpecList.isEmpty() && specIds.size() == 1
				&& specIds.get(0).equals(ItemConstant.FREE_SIZE_ID)) {
			Spec s = new Spec();
			s.setId(ItemConstant.FREE_SIZE_ID);
			s.setSpec(ItemConstant.FREE_SIZE_NAME);
			specList.add(s);
			flag = true;
		} else {
			specList = specService.selectListSpec(specIds,ItemConstant.EFFECTIVE_DATAS);
		}

		for (ItemDetailSpec spec : detailSpecList) {
			for (Spec s : specList) {
				if (s.getId().equals(spec.getSpecId())) {
					spec.setSpecName(s.getSpec());
				}
			}
		}
		if (!flag) {
			for (ItemDetail detail : list) {
				List<ItemDetailSpec> itemDetailSpecList = new ArrayList<ItemDetailSpec>();
				for (ItemDetailSpec spec : detailSpecList) {
					if (detail.getId().equals(spec.getDetailId())) {
						itemDetailSpecList.add(spec);
						if (!specGroupIds.contains(spec.getSpecGroupId())) {
							specGroupIds.add(spec.getSpecGroupId());
						}
					}
				}
				detail.setItemDetailSpecList(itemDetailSpecList);
			}
		}
		return list;
	}

	/**
	 * 
	 * <pre>
	 * 查询spu中所有的规格组
	 * </pre>
	 *
	 * @param groupIds
	 * @return
	 */
	public List<SpecGroup> getAllSpecGroups(List<Long> groupIds) {
		List<SpecGroup> res = new ArrayList<SpecGroup>();
		// 均码处理
		if (null != groupIds && !groupIds.isEmpty() && groupIds.size() == 1
				&& groupIds.get(0).equals(ItemConstant.FREE_SIZE_ID)) {
			SpecGroup spec = new SpecGroup();
			spec.setId(ItemConstant.FREE_SIZE_ID);
			spec.setName(ItemConstant.FREE_SIZE_NAME);
			res.add(spec);
		} else {
			res = specGroupService.selectListSpecGroup(groupIds,
					ItemConstant.EFFECTIVE_DATAS);
		}
		return res;
	}

	/**
	 * 
	 * <pre>
	 *   判断sku是否上下架
	 * </pre>
	 *
	 * @param detailList
	 */
	public void judgeSkuOnsale(List<ItemDetail> detailList) {
		List<Long> detailIds = new ArrayList<Long>();
		for (ItemDetail detail : detailList) {
			detailIds.add(detail.getId());
		}

		List<ItemSku> list = itemManageService
				.getSkuDetailListByDetailIds(detailIds);
		for (ItemDetail detail : detailList) {
			if (!list.isEmpty()) {
				for (ItemSku sku : list) {
					if (sku.getDetailId().equals(detail.getId())) {
						detail.setOnSaleStr(ItemConstant.ON_SALES_STR);
						break;
					} else {
						detail.setOnSaleStr(ItemConstant.OFF_SALES_STR);
					}
				}
			} else {
				detail.setOnSaleStr(ItemConstant.OFF_SALES_STR);
			}

		}
	}
	
	/**
	 * 
	 * <pre>
	 *    获取违禁词列表
	 * </pre>
	 *
	 * @return
	 */
	public List<ForbiddenWords> getForbiddenWords(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		return forbiddenWordsService.queryByParam(params);
	}
	
	/**
	 * 
	 * <pre>
	 *  选择供应商
	 * </pre>
	 *
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param supplierNameQuery
	 * @param supplierTypeQuery
	 * @param hasMeitunSeller
	 * @param addNewSupplierFlag
	 * @param supplierIdQuery
	 * @return
	 */
	public String selectSupplier(Model model, Integer pageNo, Integer pageSize,
			String supplierNameQuery, String supplierTypeQuery,
			Integer hasMeitunSeller, Integer addNewSupplierFlag,
			Long supplierIdQuery,Long skuId) {
		// 初始化供应商类型信息
		Map<String, String> supplierTypeMap = initSupplierType();
		model.addAttribute("supplierTypes", supplierTypeMap);
		PageInfo<SupplierInfo> page = new PageInfo<SupplierInfo>();
		List<SupplierType> supplierTypeList = new ArrayList<SupplierType>();
		model.addAttribute("addNewSupplierFlag", addNewSupplierFlag);
		//新增供应商
		if(null!=addNewSupplierFlag && addNewSupplierFlag==1){
			if(StringUtil.isBlank(supplierTypeQuery)){
				supplierTypeList.add(SupplierType.SELL );
				supplierTypeList.add(SupplierType.PURCHASE );
			}else{
				if(supplierTypeQuery.equals(SupplierType.SELL.getValue())){
					supplierTypeList.add(SupplierType.SELL);
				}
				if(supplierTypeQuery.equals(SupplierType.PURCHASE.getValue())){
					supplierTypeList.add(SupplierType.PURCHASE);
				}
			}
			
			page = getSupplierListWidthCondition(supplierIdQuery,supplierTypeList,supplierNameQuery,pageNo,pageSize);
			model.addAttribute("page", page);
			List<Long> supplierIdList = new ArrayList<Long>();
			String supplierIds = "";
			if(null!=skuId){
				List<ItemSkuSupplier> list = itemManageService.getSkuSupplierListBySkuId(skuId);
				if(CollectionUtils.isNotEmpty(list)){
					for(ItemSkuSupplier skuSupplier : list){
						supplierIdList.add(skuSupplier.getSupplierId());
					}
				}
				
			}
			if(CollectionUtils.isNotEmpty(supplierIdList)){
				supplierIds = org.apache.commons.lang.StringUtils.join(supplierIdList,",");
			}
			model.addAttribute("supplierIds", supplierIds);
			model.addAttribute("skuId", skuId);
			return "/item/item_supplier_add";
			
		}else{//选择供应商
			if(null!=hasMeitunSeller&&hasMeitunSeller!=ItemConstant.HAS_XIGOU_SELLER){
				//自营西客商城
				if(StringUtil.isBlank(supplierTypeQuery)){
					supplierTypeList.add(SupplierType.SELL );
					supplierTypeList.add(SupplierType.PURCHASE );
				}else{
					if(supplierTypeQuery.equals(SupplierType.SELL.getValue())){
						supplierTypeList.add(SupplierType.SELL);
					}
					if(supplierTypeQuery.equals(SupplierType.ASSOCIATE.getValue())){
						supplierTypeList.add(SupplierType.ASSOCIATE);
					}
					if(supplierTypeQuery.equals(SupplierType.PURCHASE.getValue())){
						supplierTypeList.add(SupplierType.PURCHASE );
					}
				}
				
			}else{
				supplierTypeList.add(SupplierType.ASSOCIATE);
			}
			//查询条件判断
			if(StringUtil.isNotEmpty(supplierTypeQuery)){
				model.addAttribute("sellerType", ItemSaleTypeEnum.SEAGOOR.getValue());
				if(SupplierType.ASSOCIATE.getValue().equals(supplierTypeQuery)){
					model.addAttribute("sellerType", ItemSaleTypeEnum.SELLER.getValue());
				}
			}
		}
		page = getSupplierListWidthCondition(supplierIdQuery,supplierTypeList,supplierNameQuery,pageNo,pageSize);
		model.addAttribute("page", page);
		model.addAttribute("hasXgSeller", hasMeitunSeller);
		model.addAttribute("supplierTypeQueryHidden", supplierTypeQuery);
		return "/item/item_supplier";
	}
	
	/**
	 * 
	 * <pre>
	 *   通过条件获取供应商列表
	 * </pre>
	 *
	 * @param supplierId
	 * @param supplierTypeList
	 * @param supplierName
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo<SupplierInfo> getSupplierListWidthCondition(Long supplierId,List<SupplierType> supplierTypeList, String supplierName,int startPage, int pageSize){
		SupplierResult result = purchasingManagementService.getSupplierListWithCondition(supplierId, supplierTypeList,supplierName, startPage, pageSize);
		PageInfo<SupplierInfo> page  = new PageInfo<SupplierInfo>();
		page.setPage(result.getStartPage());
		page.setSize(result.getPageSize());
		page.setRows(result.getSupplierInfoList());
		page.setRecords(result.getTotalCount().intValue());
		return page;
	}
	
	/**
	 * 
	 * <pre>
	 * 	 复制商品
	 * </pre>
	 *
	 * @param detailIds
	 * @param userId
	 * @return
	 */
	public ResultInfo<Boolean> copyItem(String  detailIds,String userName){
		try{
			if(StringUtil.isNoneBlank(detailIds)){
				itemManageService.copyItem(detailIds, userName);
				return new ResultInfo<Boolean>(Boolean.TRUE);
			}
		}catch(ItemServiceException e){
			return new ResultInfo<>(new FailInfo(e));
		}
		return new ResultInfo<>(new FailInfo("没有参数"));
	}
	
	/***
	 * 获取通关渠道信息
	 */
	public List<ClearanceChannels>  getHaiGunChannel(){
		List<ClearanceChannels>  returnList = clearanceChannelsProxy.getAllClearanceChannelsByStatus(1);
		return returnList;
	}
	
	/**
	 * 
	 * <pre>
	 *   初始化商家
	 * </pre>
	 *
	 * @param model
	 * @throws Exception
	 */
	public void initSupplierList(Model model)throws Exception {
		// 查询所有的品牌
		Brand brand = new Brand();
		brand.setStatus(Constant.ENABLED.YES);
		List<SupplierType> supplierTypes = new ArrayList<SupplierType>();
		supplierTypes.add(SupplierType.ASSOCIATE);
		SupplierResult result = purchasingManagementService.getSuppliersByTypes(supplierTypes, 1, Integer.MAX_VALUE);
		List<SupplierInfo> supplierList  = new ArrayList<SupplierInfo>();
		SupplierInfo supplier = new SupplierInfo();
		supplier.setId(ItemConstant.SUPPLIER_ID);
		supplier.setName(ItemConstant.SUPPLIER_NAME);
		supplierList.add(supplier);
		List<SupplierInfo> list = result.getSupplierInfoList();
		if(CollectionUtils.isNotEmpty(list)){
			supplierList.addAll(list);
		}
		
		model.addAttribute("supplierList", supplierList);
	}
	
	/**
	 * 查询sku列表信息
	 * @param model
	 * @param args
	 */
	public void getSkuListByDetailIds(Model model,String args){
		LOGGER.info("查询sku列表信息： {}",args);
		String detailIds = StringUtil.defaultIfBlank(args, "");
		if(StringUtil.isNotBlank(detailIds)){
			List<Long> detailIdList  = new ArrayList<Long>();
			if(detailIds.indexOf(",")==-1){
				detailIdList.add(Long.parseLong(detailIds));
			}else{
				String [] ids = detailIds.split(",");
				if(null!=ids && ids.length>1){
					for(int i = 0 ; i < ids.length; i ++){
						detailIdList.add(Long.parseLong(ids[i]));
					}
				}
			}
			List<ItemSku> skuList = itemManageService.getSkuListByDetailIds(detailIdList);
			model.addAttribute("skuList", skuList);
		}
	}
	
	public ResultInfo<List<Option>> changeItemStatus(Integer status , String args ){
		try{
			String skuIds = StringUtil.defaultIfBlank(args, "");
			if(StringUtil.isNotBlank(skuIds)){
				List<Long> skuIdList  = new ArrayList<Long>();
				if(skuIds.indexOf(",")==-1){
					skuIdList.add(Long.parseLong(skuIds));
				}else{
					String [] ids = skuIds.split(",");
					if(null!=ids && ids.length>1){
						for(int i = 0 ; i < ids.length; i ++){
							skuIdList.add(Long.parseLong(ids[i]));
						}
					}
				}
				return itemManageService.changeItemStatus(skuIdList, status);
			}
		}catch(Exception e){
			logger.error("修改商品状态出错：{}",e);
			return new ResultInfo<>(new FailInfo("修改商品状态出错"));
		}
		return new ResultInfo<>(new FailInfo("传入参数不正确"));
	}
	
	/***
	 * 手机端海淘接口
	 * @param skuCode
	 * @param topicId
	 * @return
	 * @
	 * @throws DAOException
	 */
	public InfoDetailDto queryItemSkuTopicInfoForAPPHaiTao(String skuCode,String topicId){
		return itemService.queryItemSkuTopicInfoForAPPHaiTao(skuCode,topicId);
	}
}
