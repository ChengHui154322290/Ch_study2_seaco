package com.tp.seller.ao.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.DetailDto;
import com.tp.dto.prd.DetailSkuDto;
import com.tp.dto.prd.InfoOpenDto;
import com.tp.dto.prd.ItemDetailOpenDto;
import com.tp.dto.prd.ItemDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.OpenPlantFormItemDto;
import com.tp.dto.prd.SkuOpenDto;
import com.tp.dto.prd.SpuInfoOpenDto;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.enums.ItemSendTypeEnum;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.prd.enums.SellerItemAuditTypeEnum;
import com.tp.dto.prd.enums.SellerItemBindLevelEnum;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.AttributeValue;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
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
import com.tp.query.prd.ItemQuery;
import com.tp.query.prd.OpenPlantFormItemQuery;
import com.tp.result.bse.AttributeResult;
import com.tp.result.bse.CategoryResult;
import com.tp.result.bse.SpecGroupResult;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.bse.ISpecService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.service.prd.IItemDetailSpecService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.prd.IOpenPlantFormItemDetailService;
import com.tp.service.prd.IOpenPlantFormSkuService;
import com.tp.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * <pre>
 * 商品管理的AO层操作
 * </pre>
 *
 * @author  szy
 * @version 0.0.1
 */
@Service
public class ItemAO {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemAO.class);
	
	@Autowired
	private IOpenPlantFormSkuService openPlantFormSkuService;
	
	@Autowired
	private IOpenPlantFormItemDetailService  openPlantFormItemDetailService;
	
	@Autowired
	private IItemSkuService itemSkuService;
	

	@Autowired
	private IItemManageService itemManageService;
	
	@Autowired
	private IFreightTemplateService  freightTemplateService ;
	
	/********old service************/

	@Autowired
	private ISpecGroupService specGroupService;
	@Autowired
	private ISpecService specService;
	@Autowired
	private IItemDetailSpecService itemDetailSpecService;
	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IDistrictInfoService districtInfoService;

	@Autowired
	private IBrandService brandService;
	
	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	
	@Autowired
	private IForbiddenWordsService forbiddenWordsService;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ItemAO.class);
	
	public static final String Message = "message";
	
	
	/***
	 *  查询主库信息
	 * @param skuId
	 * @return
	 */
	public  ItemDetailOpenDto showMajorSkuInfo(Long skuId){
		
			ItemDetailOpenDto returnDto = openPlantFormItemDetailService.getItemDetailOpenDtoBySkuId(skuId);
			
			return returnDto;
	}
	
	
	
	/***
	 *  更具供应商的barcode查询是否存在sku
	 * @param dataMap
	 * @return
	 */
	public SkuOpenDto sellerValidationWithBarCode(Map<String,Object> dataMap){
		if(StringUtils.isBlank(dataMap.get("barCode").toString()) || StringUtils.isBlank(dataMap.get("spId").toString())){
			return null;
		}else{
			try {
				SkuOpenDto retrunInfo = openPlantFormSkuService.getSkuInfoForSellerValidationWithBarCode(dataMap);
				if(retrunInfo != null && StringUtils.isNotBlank(retrunInfo.getMainPicture())){
					retrunInfo.setMainPicture(retrunInfo.getMainPicture());
					return retrunInfo;
				}else{
					return new SkuOpenDto();
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		return null;
	}
	
	
	
	/***
	 * 
	 * 供应商 商品列表查询
	 * @return
	 */
	public PageInfo<ItemResultDto> querySellerItemListCondition(ItemQuery query){
			try{
				
				if(null != query.getAuditStatus() && (query.getAuditStatus().equals(SellerItemAuditTypeEnum.A.getCode()) || query.getAuditStatus().equals(SellerItemAuditTypeEnum.D.getCode()))){
					OpenPlantFormItemQuery searchQuery = new OpenPlantFormItemQuery();
					BeanUtils.copyProperties(query, searchQuery);
					if(SellerItemAuditTypeEnum.A.getCode().endsWith(query.getAuditStatus())){
						searchQuery.setStatus(ItemStatusEnum.ONLINE.getValue());
					}
					if(SellerItemAuditTypeEnum.D.getCode().endsWith(query.getAuditStatus())){
						searchQuery.setStatus(ItemStatusEnum.CANCEL.getValue());
					}
					//查询商品主库
					PageInfo<OpenPlantFormItemDto>  itemList = openPlantFormSkuService.queryPageListByOpenPlantFormItemQuery(searchQuery, query.getStartPage(),query.getPageSize());
					PageInfo<ItemResultDto>  newReturn = new PageInfo<ItemResultDto>();
					
					if(itemList != null && CollectionUtils.isNotEmpty(itemList.getRows())){
						List<ItemResultDto> pageList = new ArrayList<ItemResultDto>();
						for(OpenPlantFormItemDto set : itemList.getRows()){
							ItemResultDto setNew = new ItemResultDto();
							org.springframework.beans.BeanUtils.copyProperties(set, setNew);
							setNew.setBarcode(set.getBarCode());
							setNew.setMainTitle(set.getItemName());
							setNew.setSkuId(set.getId());							
							pageList.add(setNew);
						}
						newReturn.setRows(pageList);
						newReturn.setRecords(itemList.getRecords());
						newReturn.setPage(itemList.getPage());
					}
					return newReturn;
				}else{
					PageInfo<ItemResultDto> sellerItemPageInfo = itemSkuService.searchItemByQuery(query);
					return sellerItemPageInfo;
					
				}
				
				
			}catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
			return null;
	}
	
	
	
	/***
	 * 
	 * 商家平台 绑定商品sku 信息
	 * 
	 * @param majorDetailId
	 * @param price
	 * @return
	 */
	public ResultInfo<Long> bindSellerSkuLevel(Long majorDetailId,Double price,Long spId){
		//入参数校验
		FailInfo failInfo = checkBindSkuLevelParms(majorDetailId, price);
		if(failInfo!=null){
			return new ResultInfo<Long>(failInfo);
		}
		//逻辑校验 商家平台是否存在 已经要绑定的数据 audit_stats in ('S','R')
		failInfo = checkBindSkuLevelData(majorDetailId, spId);
		if(failInfo!=null){
			return new ResultInfo<Long>(failInfo);
		}
		//保存数据
		return saveSellerBindSkuLevel(majorDetailId, spId,price);
	}

	
	
	
	
	/***
	 * 获取商家平台sku信息
	 * @param skuId
	 * @return
	 */
	public  ItemSku  getSellerSkuInfos(Long skuId){
		ItemSku infoDO =  itemSkuService.queryById(skuId);
		return  infoDO;
	}
	
	
	
	/***
	 * 获取商品的明细信息
	 * @param skuId
	 * @param spId
	 * @return
	 */
	public  ItemDetailOpenDto  getSellerItemSkuLevelDetail(ItemSku infoDO ){
		ItemDetailOpenDto returnDTO = new ItemDetailOpenDto();
		if(infoDO != null ){
			//sku 层级的
			if(infoDO.getBindLevel().equals(SellerItemBindLevelEnum.SKU.getCode())){
				returnDTO = openPlantFormItemDetailService.getItemDetailOpenDtoByDetailId(infoDO.getMajorDetailId());
			}
		}
		return  returnDTO;
	}
	
	
	
	/***
	 *  保存绑定 sku层级信息
	 * @param majorDetailId
	 * @param spId
	 * @param job
	 */
	private ResultInfo<Long> saveSellerBindSkuLevel(Long majorDetailId, Long spId,Double price) {
		ItemDetailOpenDto info = openPlantFormItemDetailService.getItemDetailOpenDtoByDetailId(majorDetailId);
		if(info != null ){
			ItemSku insertInfo = new ItemSku();
			//spu level
			insertInfo.setSpu(info.getInfoOpenDto().getSpu());
			insertInfo.setBrandName(info.getInfoOpenDto().getBrandName());
			insertInfo.setSpName(info.getInfoOpenDto().getSpuName());
			
			//insertInfo.setUnitId(info.getInfoOpenDto())
			insertInfo.setUnitName(info.getInfoOpenDto().getUnitName());
			
			//prdid level
			insertInfo.setPrdid(info.getDetailOpenDto().getPrdid());
			insertInfo.setDetailName(info.getDetailOpenDto().getMainTitle());
			insertInfo.setStatus(1);
			
			
			//SKU
			insertInfo.setMajorDetailId(majorDetailId);
			insertInfo.setSpId(spId);
			insertInfo.setBarcode(info.getDetailOpenDto().getBarcode());
			insertInfo.setUnitName(info.getInfoOpenDto().getUnitName());
			insertInfo.setBrandName(info.getInfoOpenDto().getBrandName());
			insertInfo.setCreateTime(new Date());
			
			
			insertInfo.setAuditStatus(SellerItemAuditTypeEnum.S.getCode());
			insertInfo.setBindLevel(SellerItemBindLevelEnum.SKU.getCode());
			insertInfo.setBasicPrice(price);
			itemSkuService.insert(insertInfo);
			Long saveId = insertInfo.getId();
			return new ResultInfo<Long>(saveId);
		}
		return new ResultInfo<Long>(new FailInfo("根据主产品ID查询不到信息"));
	}



	/***
	 * 校验绑定sku层级 数据
	 * @param majorDetailId
	 * @param spId
	 * @param job
	 */
	private FailInfo checkBindSkuLevelData(Long majorDetailId, Long spId) {
		ItemQuery  searchQuery = new ItemQuery();
		searchQuery.setMajorDetailId(majorDetailId);
		searchQuery.setSupplierId(spId);
		searchQuery.setBindLevel(SellerItemBindLevelEnum.SKU.getCode());
		List<ItemQuery> checkList =  itemSkuService.checkSellerSkuWithAuditStatusAndLevel(searchQuery);
		if(CollectionUtils.isNotEmpty(checkList)){
			return new FailInfo("相同的数据在提交中或者已经被驳回");
		}
		return null;
	}

	/**
	 * 	校验入参数
	 * @param majorDetailId
	 * @param price
	 * @param job
	 */
	private FailInfo checkBindSkuLevelParms(Long majorDetailId, Double price) {
		if(majorDetailId == null  || price == null ){
			return new FailInfo("输入参数校验没有通过.请核对。");
		}
		if(Double.isNaN(price)){
			return new FailInfo("市场价格有误。");
		}
		return null;
	}
	
	
	
	
	/****
	 * 
	 * 商家平台查询商品spu 名称信息 限制最大查询条数 100条
	 * @param spuName
	 * @return
	 */
	public List<InfoOpenDto> querySpuByName(String spuName){
		List<InfoOpenDto> returnList = openPlantFormItemDetailService.querySpuByNameForSeller(spuName);
		return returnList;
	}
	
	
	
	/***
	 * 商家平台 查询
	 * @param spu
	 * @return
	 */
	public ItemDetailOpenDto getExistSpuInfoForSeller(String spu){
		try {
			
			ItemDetailOpenDto returnDto = openPlantFormItemDetailService.getSpuInfoBySpuCode(spu);
				return returnDto;
		} catch (Exception e) {
				LOGGER.info("{}",e.getMessage());
		}
		return null;
	}
	
	
	
	/***
	 * 商家平台 查询
	 * @param spu
	 * @return
	 */
	public ItemDetailOpenDto getExistDetailInfoForSeller(Long majorDetailId){
		try {
			
			ItemDetailOpenDto returnDto = openPlantFormItemDetailService.getItemDetailOpenDtoByDetailId(majorDetailId);
				return returnDto;
		} catch (Exception e) {
				LOGGER.info("{}",e.getMessage());
		}
		return null;
	}
	
	
	/***
	 * 商家平台获取已经存在PRDID信息
	 * @param spu
	 * @return
	 */
	public SpuInfoOpenDto getExistPrdInfo(String spu){
		try {
			SpuInfoOpenDto  returnDto = openPlantFormItemDetailService.getSpuInfoOpenDtoBySpuCode(spu);
				return returnDto;
		} catch (Exception e) {
				LOGGER.info("{}",e.getMessage());
		}
		return null;
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

	
	/***
	 * 更具已有SPU信息保存prdid级别信息
	 * @param info
	 * @param details
	 * @param userId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ResultInfo<Long> savePrdWithExistSpu(String info,String details,Long spId) throws Exception {
		JSONArray aryJson=JSONArray.fromObject(details);
		LOGGER.info("item_info: "+info);
		LOGGER.info("item_detail: "+details);
		ItemInfo infoDO = (ItemInfo) JSONObject.toBean(JSONObject.fromObject(info), ItemInfo.class);
		List<ItemDetail> detailList = JSONArray.toList(JSONArray.fromObject(aryJson),ItemDetail.class);
		
		ItemDto item = new ItemDto();
		item.setItemInfo(infoDO);
		item.setItemDetailList(detailList);
		//数据验证
		FailInfo failInfo = this.validateSellerAddNewPrdOrSpu(infoDO, detailList, SellerItemBindLevelEnum.PRD.getCode());
		
		if(failInfo!=null){
			return new ResultInfo<Long>(failInfo);
		}
		Long  returnId =  itemManageService.saveItemWithExistSpu(item, spId);
		return new ResultInfo<Long>(returnId);
	}
	
	/***
	 *  新增spu信息
	 * @param info
	 * @param details
	 * @param spId
	 * @return
	 * @throws Exception
	 */
	public ResultInfo<Long> saveNewSpu(String info,String details,Long spId) throws Exception {
		JSONArray aryJson=JSONArray.fromObject(details);
		LOGGER.info("item_info: "+info);
		LOGGER.info("item_detail: "+details);
		ItemInfo infoDO = (ItemInfo) JSONObject.toBean(JSONObject.fromObject(info), ItemInfo.class);
		List<ItemDetail> detailList = JSONArray.toList(JSONArray.fromObject(aryJson),ItemDetail.class);
		
		ItemDto item = new ItemDto();
		item.setItemInfo(infoDO);
		item.setItemDetailList(detailList);
		
		if(StringUtil.isNullOrEmpty(infoDO.getId())){
			Category category  = categoryService.queryById(infoDO.getSmallId());
			if(null!=category){
				infoDO.setSmallCode(category.getCode());//设置小类的编码关联spu的编码
			}
		}
		//数据验证
		FailInfo failInfo  = validateSellerAddNewPrdOrSpu(infoDO, detailList, SellerItemBindLevelEnum.SKU.getCode());
		if(null!=failInfo){
			return  new ResultInfo<Long>(failInfo);
		}
		Long  returnId =  itemManageService.saveNewSpu(item, spId);
		return new ResultInfo<Long>(returnId);
	}
	
	/***
	 * 商家平台，商品信息验证
	 * @param infoDO
	 * @param details
	 * @param bindLevel
	 * @return
	 */
	private FailInfo validateSellerAddNewPrdOrSpu(ItemInfo infoDO,List<ItemDetail> details,String bindLevel){
		//绑定层级
		if(SellerItemBindLevelEnum.PRD.getCode().equals(bindLevel)){
			//小类ID不能为空
			if(infoDO.getSmallId() == null){
				return new FailInfo("小类信息不能为空");
			}
			//PRDID级别  验证barcode 和规格信息
			if (CollectionUtils.isEmpty(details)) {
				return new FailInfo("产品的prdid纬度信息不能为空");
			}
			//查看barcode  是否在主库中存在
			List<String> barcodeList = new ArrayList<>();
			for (ItemDetail detail : details) {
				if(StringUtils.isBlank(detail.getMainTitle())){
					return new FailInfo("产品对应的prdid纬度的名称不能为空");
				}
				if(StringUtils.isNotBlank(detail.getBarcode())){
					barcodeList.add(detail.getBarcode());
				}
			}
		
			if(CollectionUtils.isNotEmpty(barcodeList)){
				try{
						List<String> existList = openPlantFormItemDetailService.checkSellerBarcodeList(barcodeList);
						if(CollectionUtils.isNotEmpty(existList)){
							return new FailInfo("商品条形码已经存在".concat(StringUtils.join(existList, ";")));
						}
				}catch (Exception e) {
					return ExceptionUtils.println(new FailInfo(e), logger);
				}
			}
			//校验spu是否是均码
			//违禁词验证
			List<ForbiddenWords>  forbiddenWordsList = getForbiddenWords() ;
			for (ItemDetail detail : details) {
				if(StringUtils.isNotEmpty(detail.getMainTitle())){
					String forbiddenString = checkForbiddenWordsField(detail.getMainTitle(),"商品名称",forbiddenWordsList);
					if(StringUtils.isNotBlank(forbiddenString)){
						return new FailInfo(forbiddenString);
					}
				}
			}
		} else if(SellerItemBindLevelEnum.SKU.getCode().equals(bindLevel)){
			//spu层级验证
			StringBuffer message = new StringBuffer();
			// step1 校验 spu
			if(StringUtil.isNullOrEmpty(infoDO.getMainTitle())) {
				message.append("SPU名称不能为空 \n");
			}
			if (StringUtil.isNullOrEmpty(infoDO.getSmallId())) {
				message.append("产品关联的分类不能为空 \n");
			}
			if (StringUtil.isNullOrEmpty(infoDO.getUnitId())) {
				message.append("产品的单位不能为空 \n");
			} 
			if (StringUtil.isNullOrEmpty(infoDO.getBrandId())) {
				message.append("产品的品牌不能为空 \n");
			}
			
			if(StringUtils.isNotBlank(message.toString())){
				return new FailInfo(message.toString());
			}
			//违禁词验证
			List<ForbiddenWords>  forbiddenWordsList = getForbiddenWords() ;
			
			if(StringUtils.isNotEmpty(infoDO.getMainTitle())){
				String  forbiddenString = checkForbiddenWordsField(infoDO.getMainTitle(),"SPU名称",forbiddenWordsList);
				if(StringUtils.isNotBlank(forbiddenString)){
					return new FailInfo(forbiddenString);
				}
			}
			if(StringUtils.isNotEmpty(infoDO.getRemark())){
				String forbiddenString = checkForbiddenWordsField(infoDO.getRemark(),"备注",forbiddenWordsList);
				if(StringUtils.isNotBlank(forbiddenString)){
					return new FailInfo(forbiddenString);
				}
			}
			for (ItemDetail detail : details) {
				if(StringUtils.isNotEmpty(detail.getMainTitle())){
					String forbiddenString = checkForbiddenWordsField(detail.getMainTitle(),"商品名称",forbiddenWordsList);
					if(StringUtils.isNotBlank(forbiddenString)){
						return new FailInfo(forbiddenString);
					}
				}
			}
			//查看barcode  是否在主库中存在
			List<String> barcodeList = new ArrayList<>();
			for (ItemDetail detail : details) {
				if(StringUtils.isBlank(detail.getMainTitle())){
					return new FailInfo("产品对应的prdid纬度的名称不能为空");
				}
				if(StringUtils.isNotBlank(detail.getBarcode())){
					barcodeList.add(detail.getBarcode());
				}
			}
			//检验spu唯一性
			ItemInfo checkExist = itemManageService.checkSpuExsit(infoDO.getSmallId(),infoDO.getBrandId(),infoDO.getUnitId(),infoDO.getMainTitle(),infoDO.getId());
			if(checkExist !=null){
				return new FailInfo("相同的分类下SPU名称已经存在了,可以通过查询检索添加PRD");
			}
			//校验barcode
			if(CollectionUtils.isNotEmpty(barcodeList)){
				List<String> existList = openPlantFormItemDetailService.checkSellerBarcodeList(barcodeList);
				if(CollectionUtils.isNotEmpty(existList)){
					return new FailInfo("商品条形码已经存在".concat(StringUtils.join(existList, ";")));
				}
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * <pre>
	 *  验证违禁词
	 * </pre>
	 *
	 * @param sourceField
	 * @param fieldDesc
	 * @param fobiddenWords
	 * @return
	 */
	private  String checkForbiddenWordsField(String sourceField ,String fieldDesc,List<ForbiddenWords> fobiddenWords){
		StringBuffer sb = new StringBuffer(fieldDesc);
		boolean flag = false;
		sb.append("中有");
		if(CollectionUtils.isNotEmpty(fobiddenWords)){
			if(!StringUtil.isNullOrEmpty(sourceField)){
				for(ForbiddenWords forbiddenWordsDO :fobiddenWords ){
					String forbiddenWords = forbiddenWordsDO.getWords();
					if(!StringUtil.isNullOrEmpty(forbiddenWords)){
						int total = checkStrCount(sourceField,forbiddenWords);
						if(total>0){
							flag = true;
							sb.append(": 违禁词[").append(forbiddenWords)
							  .append("],总共出现").append(total).append("次。");
						}
					}
				}
			}
		}
		if(flag){
			return sb.toString();
		}else{
			return "";
		}
	}
	
	
	/**
	 * 
	 * <pre>
	 *  判断一个字符串出现另一个字符串的次数
	 * </pre>
	 *
	 * @param str
	 * @param checkStr
	 * @return
	 */
	private static  int checkStrCount(String str, String checkStr){
		int total = 0;
		for (String tmp = str; 	tmp!= null
				&&tmp.length()>=checkStr.length();){
		  if(tmp.indexOf(checkStr) == 0){
		    total ++;
		  }
		  tmp = tmp.substring(1);
		}
		return total;
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
		ForbiddenWords forbiddenWordsDO  = new ForbiddenWords();
		forbiddenWordsDO.setStatus(Constant.ENABLED.YES);
		List<ForbiddenWords> list  = forbiddenWordsService.queryByObject(forbiddenWordsDO);
		return list;
	}
	
	/***
	 * 更具绑定层级 初始化商品信息
	 * @param mav
	 * @param itemId
	 * @param bindLevel
	 * @return
	 * @throws Exception
	 */
	public ItemDto initSpuWithBindLevel(ModelAndView mav, Long itemId,String bindLevel) throws Exception {
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
			mav.addObject("groupList", groupList);
			// 展示prdid是否上架
			judgeSkuOnsale(detailDOList);
			itemDto.setItemDetailList(detailDOList);
		}
		if(SellerItemBindLevelEnum.SPU.getCode().equals(bindLevel)){
			// 获得商品分类信息 大类
			List<Category> categoryList = categoryService.getFirstCategoryList();
			mav.addObject("categoryList", categoryList);
			initBrandListForBindLevel(mav);
			DictionaryInfo dictionaryItemInfo = new DictionaryInfo();
			// 单位
			dictionaryItemInfo.setCode(DictionaryCode.c1001.getCode()); // 单位
			List<DictionaryInfo> unitList = dictionaryInfoService
					.queryByObject(dictionaryItemInfo);
			mav.addObject("unitList", unitList);
			// 尺码组
			SpecGroup specGroup = new SpecGroup();
			specGroup.setStatus(Constant.ENABLED.YES);
			List<SpecGroup> specGroupList = specGroupService.queryByObject(specGroup);
			mav.addObject("specGroupList", specGroupList);
			
			ItemInfo info = itemDto.getItemInfo();
			// 查询大类中类小类的名称
			Category c = categoryService.queryById(info.getLargeId());
			Category c1 = categoryService.queryById(info.getMediumId());
			Category c2 = categoryService.queryById(info.getSmallId());
			info.setLargeName(c.getName());
			info.setMediumName(c1.getName());
			info.setSmallName(c2.getName());
			//查询brand
			if(info.getBrandId() != null){
				Brand brandInfo = brandService.queryById(info.getBrandId());
				if(brandInfo != null){
					info.setBrandName(brandInfo.getName());
				}
			}
			//查询unit
			if(info.getUnitId() != null){
				DictionaryInfo	 unitInfo = dictionaryInfoService.queryById(info.getUnitId());
				if(unitInfo != null){
					info.setUnitName(unitInfo.getName());
				}
			}
			itemDto.setItemInfo(info);
		}else if(SellerItemBindLevelEnum.PRD.getCode().equals(bindLevel)){
			ItemInfo info = itemDto.getItemInfo();
			// 查询大类中类小类的名称
			info.setLargeName(info.getLargeName());
			info.setMediumName(info.getMediumName());
			info.setSmallName(info.getSmallName());
			itemDto.setItemInfo(info);
		}
		return itemDto;
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
	 * 获取所有代规格的所有的信息
	 * </pre>
	 *
	 * @param list
	 * @return
	 */
	public List<ItemDetail> getAllDetailWithSpec(List<ItemDetail> list,
			List<Long> specGroupIds) {
		List<Long> detailIds = getAllDetailIds(list);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemDetailSpec> detailSpecList = itemDetailSpecService.queryByParam(params);
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
				List<ItemDetailSpec> s = new ArrayList<ItemDetailSpec>();
				for (ItemDetailSpec spec : detailSpecList) {
					if (detail.getId().equals(spec.getDetailId())) {
						s.add(spec);
						if (!specGroupIds.contains(spec.getSpecGroupId())) {
							specGroupIds.add(spec.getSpecGroupId());
						}
					}
				}
				detail.setItemDetailSpecList(s);
			}
		}
		return list;
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
	 *   初始化品牌
	 * </pre>
	 *
	 * @param model
	 * @throws Exception
	 */
	public void initBrandListForBindLevel(ModelAndView mav)throws Exception {
		// 查询所有的品牌
		Brand brand = new Brand();  
		brand.setStatus(Constant.ENABLED.YES);
		List<Brand> brandList = brandService.queryByObject(brand);
		mav.addObject("brandList", brandList);
	}
	
	
	
	/***
	 * spu 存在 查询商品主信息
	 * @param mav
	 * @param detailId
	 * @throws Exception
	 */
	public void queryItemDetailWithExistSpu(ModelAndView mav, Long detailId) throws Exception {
		DetailDto detailDto = getItemDetailDtoById(detailId);
		mav.addObject("detail", detailDto.getItemDetail());
		mav.addObject("desc", detailDto.getItemDesc());
		mav.addObject("descMobile", detailDto.getItemDescMobile());
		mav.addObject("picList", detailDto.getPicList());
		mav.addObject("detailSkuList", detailDto.getDetailSkuList()); 
		// model.addAttribute("attrItemList",detailDto.getAttrItemList());
		mav.addObject("attrList", detailDto.getAttrList());
		ItemDto itemDto = getItemByItemId(detailDto.getItemDetail().getItemId());
		Long categoryId = itemDto.getItemInfo().getSmallId();
		//基础数据获取属性key与属性值
		List<AttributeResult> listAttributeResult = getAttributeAndValues(categoryId);
		// 基础数据的属性与已经选择的属性进行关联
		convetSelectAttr(listAttributeResult, detailDto.getAttrItemList());
		mav.addObject("listAttributeResult", listAttributeResult);
		//适用年龄 
		List<DictionaryInfo>  applyAgeList  = getAllApplyAgeList();
		mav.addObject("applyAgeList", applyAgeList);
	
		//销售规格
		mav.addObject("detailSpecList", detailDto.getDetailSkuList());
		//添加是否有西客商城商家标识符
		mav.addObject("hasXgSeller", detailDto.getHasXgSeller()); 
		
		//获取国家数据
		List<DistrictInfo> listCountry = getAllCountryList();
		mav.addObject("listCountry", listCountry);
		//获取配送方式数据
		mav.addObject("listSendType",ItemSendTypeEnum.values()); 
		
		List<FreightTemplate> freightTemplateList = getAllFreightTemplate();
		mav.addObject("freightTemplateList", freightTemplateList);
	}
	
	
	
	/***
	 * 获取国家数据
	 * 
	 */
	public List<DistrictInfo> getAllCountryList(){ 
		// 适用年龄
		DistrictInfo districtItemInfo = new DistrictInfo ();
		districtItemInfo.setType(2);
		List<DistrictInfo> list = districtInfoService.queryByObject(districtItemInfo);
		return list;
	}
	
	
	public List<DictionaryInfo> getAllApplyAgeList(){
		// 适用年龄
		DictionaryInfo dictionaryItemInfo = new DictionaryInfo ();
		dictionaryItemInfo.setCode(DictionaryCode.c1003.getCode()); // 适用年龄
		List<DictionaryInfo> list = dictionaryInfoService.queryByObject(dictionaryItemInfo);
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
		if (CollectionUtils.isNotEmpty(attrs)) {
			for (AttributeResult a : listAttributeResult) {
				List<AttributeValue> attributeValue = a.getAttributeValues();
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
	
	
	/***
	 * 商家平台 运费模板 国内 同一邮费
	 * @return
	 * @throws Exception
	 */
	public List<FreightTemplate> getAllFreightTemplate() throws Exception{
		FreightTemplate queryFreight = new FreightTemplate();
		queryFreight.setFreightType(0);
		queryFreight.setCalculateMode(1);
		List<FreightTemplate> list =freightTemplateService.queryByObject(queryFreight);
							
		return list;
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
	public DetailDto getItemDetailDtoById(Long detailId){
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
				DetailSkuDto detailSkuDto = new DetailSkuDto();
				BeanUtils.copyProperties(detailSku, detailSkuDto);
				if(detailSku.getSaleType()==ItemSaleTypeEnum.SEAGOOR.getValue()){
					detailDto.setHasXgSeller(ItemConstant.HAS_XIGOU_SELLER);
				}
			}
		}
		
		detailDto.setDetailSkuList(detailSkuArray);
		List<ItemAttribute> attrAllList = itemManageService.getAttributeByDetailId(detailId);
		List<ItemAttribute> attrItemList = new ArrayList<ItemAttribute>();
		List<ItemAttribute> attrList = new ArrayList<ItemAttribute>();
		for (ItemAttribute attr : attrAllList) {
			if (attr.getCustom().equals(ItemConstant.ATTR_FROM_BASEDATA)) {
				attrItemList.add(attr);
			} else if (attr.getCustom().equals(ItemConstant.ATTR_CUSTOM)) {
				attrList.add(attr);
			}
		}
		
		
		List<ItemDetailSpec> detailSpecList = itemManageService.getDetailSpecListByDetailId(detailId);
		List<ItemDetailSpec> detailSpecDtoList = new ArrayList<ItemDetailSpec>();
		List<Long> specIds = new ArrayList<Long>();
		List<Long> specGroupIds = new ArrayList<Long>();
		for(ItemDetailSpec detailSpec: detailSpecList){
			ItemDetailSpec detailSpecDto= new ItemDetailSpec();
			BeanUtils.copyProperties(detailSpecDto, detailSpec);
			specIds.add(detailSpec.getSpecId());
			specGroupIds.add(detailSpec.getSpecGroupId());
			detailSpecDtoList.add(detailSpecDto);
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
		return detailDto;
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
	public ResultInfo<Long> saveItemDetailWithBindLevel(ItemInfo infoDO,DetailDto detailDto,String[] picList,String skuListJson,String attrListJson,String attrItemJson,Long spId,String bindLevel,String auditType,Long skuId) throws Exception{
		ItemInfo info = new ItemInfo();
		if(skuId != null ){
			//验证数据层级
			ItemSku checkLevel = itemSkuService.queryById(skuId);
			//验证是否同供应商操作
			if(checkLevel.getSpId() != spId){
				return new ResultInfo<Long>(new FailInfo("数据状态验证失败，操作人不是该供应商相关人员"));
			}
			//验证绑定数据
			if(!checkLevel.getBindLevel().equals(bindLevel)){
				return new ResultInfo<Long>(new FailInfo("绑定层级不对"));
			}
			if(null==infoDO){
				info = null;
			}else{
				List<String> picListTmp =  new ArrayList<String>();
				if(null!=picList){
					picListTmp = Arrays.asList(picList);
				}
				detailDto.setPicList(picListTmp);
				LOGGER.info("skuListJson: {} ", skuListJson);
				LOGGER.info("attrListJson: {}", attrListJson);
				LOGGER.info("attrItemJson: {}", attrItemJson);
				//初始化数据
				initSellerDetail(detailDto, skuListJson, attrListJson, attrItemJson);
				
				detailDto.getItemDetail().setId(checkLevel.getDetailId());  
				
				FailInfo faiInfo = validateSellerPrdInfo(detailDto);
				if(faiInfo!=null){
					return new ResultInfo<Long>(faiInfo);
				}
			}
		}else{
			return new ResultInfo<Long>(new FailInfo("层级验证数据为空，请联系管理员"));
		}
		Long detailId = null;
		if(SellerItemBindLevelEnum.PRD.getCode().equals(bindLevel)){
			detailId = itemManageService.saveItemDetailWithExistSpu(info,detailDto,spId,auditType,skuId);
		}else if(SellerItemBindLevelEnum.SPU.getCode().equals(bindLevel)){
			detailId = itemManageService.saveItemDetailWithExistSpu(info,detailDto,spId,auditType,skuId);
		}
		if(detailId==null){
			return new ResultInfo<Long>(new FailInfo("商品未保存成功"));
		}
		return new ResultInfo<Long>(detailId);	
	}
	
	
	
	private FailInfo validateSellerPrdInfo(DetailDto detailDto){
		//  校验
		// step1 校验 prdid
		// step2校验 sku
		ItemDetail detail = detailDto.getItemDetail();
		if (StringUtil.isNullOrEmpty(detail.getItemTitle())) {
			return new FailInfo("商品名称不能为空");
		}else if (StringUtil.isNullOrEmpty(detail.getMainTitle())) {
			return new FailInfo("网站显示名称不能为空");
		}else if (StringUtil.isNullOrEmpty(detail.getStorageTitle())) {
			return new FailInfo("仓库名称不能为空");
		}else if (StringUtil.isNullOrEmpty(detail.getSubTitle())) {
			return new FailInfo("商品卖点(副标题)不能为空");
		} else if (StringUtil.isNullOrEmpty(detail.getBasicPrice())) {
			return new FailInfo("市场价不能为空");
		} else if (StringUtil.isNullOrEmpty(detail.getWeight())) {
			return new FailInfo("毛重不能为空");
		} else if (StringUtil.isNullOrEmpty(detail.getItemType())) {
			return new FailInfo("商品类型不能为空");
		} else if (StringUtil.isNullOrEmpty(detail.getPurRate())) {
			return new FailInfo("进项率不能为空");
		} else if (StringUtil.isNullOrEmpty(detail.getSaleRate())) {
			return new FailInfo("销项税率不能为空");
		}else if(Double.isNaN(detail.getBasicPrice())){
			return new FailInfo("sku市场价有误");
		} 
		
		
		ItemDesc desc = detailDto.getItemDesc();
		if(null==desc || StringUtils.isBlank(desc.getDescription())){
			return new FailInfo("PC模板不能为空");
		}
		
		ItemDescMobile descMobile = detailDto.getItemDescMobile();
		if(null==descMobile || StringUtils.isBlank(descMobile.getDescription())){
			return new FailInfo("手机模板不能为空");
		}
		List<String> picList = detailDto.getPicList();
		if(CollectionUtils.isEmpty(picList)){
			return new FailInfo("上传图片不能为空,图片只能传3-10张");
		}else{
			if(picList.size()>10||picList.size()<3){
				return new FailInfo("上传图片数量不对,应上传3-10张图片");
				
			}
		}
		List<ForbiddenWords>  list = getForbiddenWords() ;
		//违禁词校验
		String checkMsg = "" ; 
		if(StringUtils.isNotEmpty(detail.getItemTitle())){
			checkMsg = checkForbiddenWordsField(detail.getItemTitle(),"商品名称",list);
		}
		if(StringUtils.isNotEmpty(detail.getMainTitle())){
			checkMsg += checkForbiddenWordsField(detail.getMainTitle(),"网站显示名称",list);
		}
		if(StringUtils.isNotEmpty(detail.getStorageTitle())){
			checkMsg += checkForbiddenWordsField(detail.getStorageTitle(),"仓库名称",list);
		}
		if(StringUtils.isNotEmpty(detail.getSubTitle())){
			checkMsg += checkForbiddenWordsField(detail.getSubTitle(),"商品卖点(副标题)",list);
		}
		if(StringUtils.isNotEmpty(detail.getRemark())){
			checkMsg += checkForbiddenWordsField(detail.getRemark(),"备注",list);
		}
		if(StringUtils.isNotEmpty(detail.getSpecifications())){
			checkMsg += checkForbiddenWordsField(detail.getSpecifications(),"规格",list);
		}
		if(StringUtils.isNotEmpty(detail.getCartonSpec())){
			checkMsg += checkForbiddenWordsField(detail.getCartonSpec(),"箱规",list);
		}
		
		List<ItemAttribute> attrList = detailDto.getAttrItemList();
		
		if(CollectionUtils.isNotEmpty(attrList)){
			for(ItemAttribute attribute : attrList){
				if(null!=attribute && StringUtils.isNotEmpty(attribute.getAttrKey())){
					checkMsg += checkForbiddenWordsField(attribute.getAttrKey(),"自定义属性名",list);
				}
				if(null!=attribute && StringUtils.isNotEmpty(attribute.getAttrValue())){
					checkMsg += checkForbiddenWordsField(attribute.getAttrValue(),"自定义属性值",list);
				}
			}
		}
		if(null!=desc && StringUtils.isNotEmpty(desc.getDescription())){
			checkMsg += checkForbiddenWordsField(desc.getDescription(),"详情-PC模板",list);
		}
		if(null!=descMobile && StringUtils.isNotEmpty(descMobile.getDescription())){
			checkMsg += checkForbiddenWordsField(descMobile.getDescription(),"详情-手机模板",list);
		}
		if(StringUtils.isNotEmpty(checkMsg)){
			return new FailInfo(checkMsg);
		}
		else if (StringUtil.isNullOrEmpty(detail.getBarcode())) {
			return new FailInfo("产品的条码不能为空");
		}else if (StringUtil.isNullOrEmpty(detail.getMainTitle())) {
			return new FailInfo("产品对应的prdid纬度的名称不能为空");
		}
		return null;
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
	public void initSellerDetail(DetailDto detailDto, String skuListJson,
			String attrListJson, String attrItemJson) {
		List<DetailSkuDto> detailSkuList = JSONArray.toList(
				JSONArray.fromObject(skuListJson), DetailSkuDto.class);
		List<ItemAttribute> attrList = JSONArray.toList(
				JSONArray.fromObject(attrListJson), ItemAttribute.class);
		List<ItemAttribute> attrItemList = JSONArray.toList(
				JSONArray.fromObject(attrItemJson), ItemAttribute.class);
		detailDto.setDetailSkuList(detailSkuList);
		detailDto.setAttrItemList(attrItemList);
		detailDto.setAttrList(attrList);
	}
	/********old code************/
	
	
   
}

