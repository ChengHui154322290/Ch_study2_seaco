package com.tp.service.prd;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONArray;
import com.tp.common.util.ImageDownUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.ProductConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.ProductConstant.RATE_TYPE;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.StorageConstant.StorageType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.prd.ItemComonConstant;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.common.vo.prd.ItemRedisConstant;
import com.tp.dao.prd.ItemAttributeDao;
import com.tp.dao.prd.ItemDetailDao;
import com.tp.dao.prd.ItemDetailSpecDao;
import com.tp.dao.prd.ItemInfoDao;
import com.tp.dao.prd.ItemPicturesDao;
import com.tp.dao.prd.ItemSkuArtDao;
import com.tp.dao.prd.ItemSkuDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.LockStatus;
import com.tp.dto.mmp.enums.TIPurchaseMethod;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.SkuDto;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.prd.mq.PromotionItemMqDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.Brand;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.NationalIcon;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.model.bse.TaxRate;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.mmp.Topic;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.query.mmp.TopicItemInfoQuery;
import com.tp.query.prd.ItemQuery;
import com.tp.query.prd.SkuInfoQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.bse.SpecGroupResult;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.result.prd.SkuInfoResult;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.INationalIconService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.bse.ISpecService;
import com.tp.service.bse.ITaxRateService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.prd.IItemRemoteService;
import com.tp.service.prd.IItemService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.BeanUtil;
import com.tp.util.BigDecimalUtil;
import com.tp.util.StringUtil;

/**
 * 
 * <pre>
 * 商品对外接口实现
 * </pre>
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class ItemService implements IItemService {
	private final static Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	private ItemInfoDao itemInfoDao;
	@Autowired
	private ItemSkuDao itemSkuDao;
	@Autowired
	private ItemDetailDao itemDetailDao;
	@Autowired
	private ItemPicturesDao itemPicturesDao;
	@Autowired
	private ItemAttributeDao itemAttributeDao;
	@Autowired
	private ItemDetailSpecDao itemDetailSpecDao;
	@Autowired
	private ItemSkuArtDao itemSkuArtDao;
	@Autowired
	private IFreightTemplateService freightTemplateService;
	@Autowired
	private IItemSkuService  itemSkuService;
	@Autowired
	private ITopicService topicService;
	@Autowired
	private IInventoryQueryService  inventoryQueryService;
	@Autowired
	JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	private ISpecGroupService specGroupService;
	@Autowired
	private ISpecService specService;
	@Autowired
	private ITaxRateService taxRateService;
	
	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	
	@Autowired
	private IBrandService  brandService;
	
	
	
	/***********手机海淘新增service************/
	@Autowired
	private IWarehouseService	 warehouseService;
	@Autowired
	private IDistrictInfoService	 districtInfoService;
	
	@Autowired
	private INationalIconService nationalIconService;
	
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	
	@Autowired
	private ISupplierInfoService  supplierInfoService;

	@Autowired
	private IItemRemoteService itemRemoteService;
	@Value("#{settings['xg.item.detail.replace.images']}")
	private String replaceimages;
	
	@Override
	public ResultInfo<Boolean> checkItem(String skuCode){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sku", skuCode);
		List<ItemSku> skuList =  itemSkuDao.queryByParam(params);
		if(CollectionUtils.isEmpty(skuList)){
			return new ResultInfo<>(new FailInfo("商品基本信息为空 "));
		}
		if(skuList.get(0).getStatus()!=Constant.DEFAULTED.YES){
			return new ResultInfo<>(new FailInfo("商品未上架 "));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}

	@Override
	public List<ItemResultDto> getSkuList(List<String> skuCodes)
			{
		LOGGER.info("通过sku列表查询相关的商品信息,传入参数为： skuCodes:{}" ,skuCodes);
		if(CollectionUtils.isEmpty(skuCodes)){
			return null;
		}
		List<ItemResultDto> resultList = new ArrayList<ItemResultDto>();

		//sku纬度信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "sku in ('"+StringUtil.join(skuCodes, "'"+Constant.SPLIT_SIGN.COMMA+"'")+"')");
		List<ItemSku> skuList =itemSkuDao.queryByParam(params);
		if(CollectionUtils.isEmpty(skuList)){
			return null;
		}
		List<Long> detailIds = new ArrayList<Long>();
		List<Long> itemIds = new ArrayList<Long>();
		ItemResultDto result = null;
		for (ItemSku itemSku : skuList) {
			detailIds.add(itemSku.getDetailId());
			itemIds.add(itemSku.getItemId());
			result = new ItemResultDto();
			result.setBarcode(itemSku.getBarcode());
			result.setBasicPrice(itemSku.getBasicPrice());
			result.setCreateTime(itemSku.getCreateTime());
			result.setItemId(itemSku.getItemId());
			result.setDetailId(itemSku.getDetailId());
			result.setMainTitle(itemSku.getDetailName());
			result.setSku(itemSku.getSku());
			result.setPrdid(itemSku.getPrdid());
			result.setSkuId(itemSku.getId());
			result.setSpu(itemSku.getSpu());
			result.setStatus(itemSku.getStatus());
			result.setSaleType(itemSku.getSaleType());
			result.setSupplierCode(itemSku.getSpCode());
			result.setSupplierId(itemSku.getSpId());
			result.setSupplierName(itemSku.getSpName());
			result.setCommisionRate(itemSku.getCommisionRate());
			resultList.add(result);
		}
		//prdid纬度信息
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemDetail> detailList = itemDetailDao.queryByParam(params);
		if(!CollectionUtils.isEmpty(detailList)){
			for (ItemDetail itemDetail : detailList) {
				for (ItemResultDto item : resultList) {
					if(item.getDetailId().longValue()==itemDetail.getId().longValue()){
						item.setSubTitle(itemDetail.getSubTitle());
						item.setWavesSign(itemDetail.getWavesSign());
						item.setTarrifRateId(itemDetail.getTarrifRate());//关税
						item.setWeightNet(itemDetail.getWeightNet());//净重 
						item.setWeight(itemDetail.getWeight());//毛重 
						item.setFreightTemplateId(itemDetail.getFreightTemplateId());
						item.setCartonSpec(itemDetail.getCartonSpec());//箱规
						item.setSpecifications(itemDetail.getSpecifications());//规格
						item.setMainTitle(itemDetail.getMainTitle());//统一下前台显示名称..
						item.setSendType(itemDetail.getSendType());
						item.setCountryId(itemDetail.getCountryId());//产地
						item.setReturnDays(itemDetail.getReturnDays());
						item.setStorageTitle(itemDetail.getStorageTitle());//仓库中 商品名称
						item.setCustomsRateId(itemDetail.getCustomsRate());
						item.setAddedValueRateId(itemDetail.getAddedValueRate());
						item.setExciseRateId(itemDetail.getExciseRate());
						//新增
						item.setUnitId(itemDetail.getUnitId());
						item.setUnitQuantity(itemDetail.getUnitQuantity());
						item.setWrapQuantity(itemDetail.getWrapQuantity());
					}
				}
			}
		}
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemDetailSpec> detailSpecList = itemDetailSpecDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(detailSpecList)){
			Map<Long, List<ItemDetailSpec>> map = new HashMap<Long, List<ItemDetailSpec>>();
			for (ItemDetailSpec detailSpec : detailSpecList) {
				Long detailId = detailSpec.getDetailId();
				List<ItemDetailSpec> detailSpecs = map.get(detailId);
				if(CollectionUtils.isEmpty(detailSpecs)){
					detailSpecs = new ArrayList<ItemDetailSpec>();
					map.put(detailId, detailSpecs);
				}
				detailSpecs.add(detailSpec);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemDetailSpec>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setItemDetailSpecList(spec2SpecDto(entry.getValue()));//转换成dto
					}
				}
			}
		}
		//spu纬度信息
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in ("+StringUtil.join(itemIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemInfo> infoList = itemInfoDao.queryByParam(params);
		if(!CollectionUtils.isEmpty(infoList)){
			for (ItemInfo infoDO : infoList) {
				for (ItemResultDto item : resultList) {
					if(item.getItemId().longValue()==infoDO.getId().longValue()){
						item.setBrandId(infoDO.getBrandId().longValue());
						if( infoDO.getBrandId() !=null){
							Brand brand = brandService.queryById(infoDO.getBrandId());
							if(brand != null){
								item.setBrandName(brand.getName());
							}
						}
						item.setLargeId(infoDO.getLargeId());
						item.setMediumId(infoDO.getMediumId());
						item.setSmallId(infoDO.getSmallId());
						item.setUnitId(infoDO.getUnitId().longValue());
						item.setUnitName(infoDO.getUnitName());
					}
				}
			}
		}
		//查询主图
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemPictures> picturesDOs = itemPicturesDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(picturesDOs)){
			Map<Long, List<ItemPictures>> map = new HashMap<Long, List<ItemPictures>>();
			for (ItemPictures picturesDO : picturesDOs) {
				Long detailId = picturesDO.getDetailId();
				List<ItemPictures> list = map.get(detailId);
				if(CollectionUtils.isEmpty(list)){
					list = new ArrayList<ItemPictures>();
					map.put(detailId, list);
				}
				list.add(picturesDO);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemPictures>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setImageUrl(entry.getValue().get(0).getPicture());
					}
				}
			}
			
		}
		//属性信息
		List<ItemAttribute> attributeDOs = itemAttributeDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(attributeDOs)){
			Map<Long, List<ItemAttribute>> map = new HashMap<Long, List<ItemAttribute>>();
			for (ItemAttribute attr : attributeDOs) {
				Long detailId = attr.getDetailId();
				List<ItemAttribute> list = map.get(detailId);
				if(CollectionUtils.isEmpty(list)){
					list = new ArrayList<ItemAttribute>();
					map.put(detailId, list);
				}
				list.add(attr);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemAttribute>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setItemAttribute(entry.getValue());
					}
				}
			}
		}
		//base服务 查询关税税率
		getItemResultDtoByBase(resultList);
		return resultList;
	}

	@Override
	public ResultInfo<Boolean> checkItemList(List<String> skuCodes){
		LOGGER.info("批量检验商品是存在以及上架,传入参数为： skuCodes:{}" ,skuCodes);
		for (String skuCode : skuCodes) {
			ResultInfo<Boolean> resultInfo = checkItem(skuCode);
			if (!resultInfo.success) {
				return resultInfo;
			}
		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	@Override
	public InfoDetailDto selectItemIdBySkuId(String skuCode)
			{
		return itemSkuDao.selectItemIdBySkuId(skuCode);
	}

	@Override
	public String selectDetailIdDesc(Long detailId) {
		return itemInfoDao.selectDetailIdDesc(detailId);
	}

	@Override
	public List<ItemPictures> selectItemPictures(InfoDetailDto dto){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", dto.getDetailId());
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " main desc");
		return itemPicturesDao.queryByParam(params);
	}

	@Override
	public SkuInfoResult selectXgSkuInfo(SkuInfoQuery sku) {
		if(sku==null){
			throw new ItemServiceException("查询sku不能为空");
		}
		if(StringUtils.isBlank(sku.getSku())&&StringUtils.isBlank(sku.getBarcode())){
			throw new ItemServiceException("查询sku,sku编码,barcode不能都为空");
		}
		ItemSku itemSku = new ItemSku();
		itemSku.setSku(sku.getSku());
		itemSku.setBarcode(sku.getBarcode());
		itemSku.setSaleType(ItemSaleTypeEnum.SEAGOOR.getValue());
		SkuInfoResult skuInfo = new SkuInfoResult();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sku", itemSku.getSku());
		params.put("barcode", itemSku.getBarcode());
		params.put("saleType", itemSku.getSaleType());
		List<ItemSku> list = itemSkuDao.queryByParam(params);
		if (CollectionUtils.isNotEmpty(list)) {
			itemSku = list.get(0);
			skuInfo.setBarcode(itemSku.getBarcode());
			skuInfo.setCategoryCode(itemSku.getCategoryCode());
			skuInfo.setSku(itemSku.getSku());
			skuInfo.setSkuName(itemSku.getDetailName());
			skuInfo.setBrandId(itemSku.getBrandId());
			skuInfo.setStatus(itemSku.getStatus());
			skuInfo.setUnitId(itemSku.getUnitId());
			skuInfo.setBasicPrice(itemSku.getBasicPrice());
			if (null != itemSku.getDetailId()) {
				ItemDetail detail = itemDetailDao.queryById(itemSku.getDetailId());
				if (null != detail) {
					skuInfo.setSpecifications(detail.getSpecifications());
					skuInfo.setCartonSpec(detail.getCartonSpec());
					skuInfo.setWavesSign(detail.getWavesSign());// 是否为海淘商品
					// 规格
					params.clear();
					params.put("detailId", detail.getId());
					List<ItemDetailSpec> itemDetailSpecList = itemDetailSpecDao.queryByParam(params);
					skuInfo.setItemDetailSpecList(itemDetailSpecList);
				}
			}
			// 类别
			if (null != itemSku.getItemId()) {
				ItemInfo info = itemInfoDao.queryById(itemSku.getItemId());
				if (null != info) {
					skuInfo.setLargeId(info.getLargeId());
					skuInfo.setMediumId(info.getMediumId());
					skuInfo.setSmallId(info.getSmallId());
				}
			}
		} else {
			LOGGER.error("通过sku,供应商id查询商品信息,传入参数为： sku:{}" ,sku.toString());
			throw new ItemServiceException("系统中不存在自营的Sku");
		}
		return skuInfo;
	}

	@Override
	public List<SkuDto> querySkuDtoListForPromotion(List<String> listSku){
		List<SkuDto> skuList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(listSku)){
			skuList = itemSkuDao.querySkuDtoListForPromotion(listSku);
			if(CollectionUtils.isNotEmpty(skuList)){
				//获取规格details
				List<Long> detailIds = new ArrayList<>();
				for(SkuDto dto:skuList){
					detailIds.add(dto.getDetailId());
				}
				//获取规格组
				Map<String,Object> params = new HashMap<String,Object>();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
				List<ItemDetailSpec> listSpecDetail = itemDetailSpecDao.queryByParam(params);
				if(CollectionUtils.isNotEmpty(listSpecDetail)){
					for(int i=0;i<skuList.size();i++){
						for(ItemDetailSpec sdto: listSpecDetail){
							//detailId相等
							if(sdto.getDetailId().equals(skuList.get(i).getDetailId())){
								if(CollectionUtils.isEmpty(skuList.get(i).getItemDetailSpecList())){
									List<ItemDetailSpec> newSpecList = new ArrayList<>();
									newSpecList.add(sdto);
									skuList.get(i).setItemDetailSpecList(newSpecList);
								}else{
									skuList.get(i).getItemDetailSpecList().add(sdto);
								}
								
							}
						}
					}
				}
				//获取主图信息
				List<ItemPictures> listPicutres = itemPicturesDao.queryByParam(params);
				if(CollectionUtils.isNotEmpty(listPicutres)){
					for(int i=0;i<skuList.size();i++){
						for(ItemPictures pdto: listPicutres){
							//detailId相等
							if(pdto.getDetailId().equals(skuList.get(i).getDetailId())){
								if(CollectionUtils.isEmpty(skuList.get(i).getItemPicturesList())){
									List<ItemPictures> newPictureList = new ArrayList<>();
									newPictureList.add(pdto);
									skuList.get(i).setItemPicturesList(newPictureList);
								}else{
									skuList.get(i).getItemPicturesList().add(pdto);
								}
								
							}
						}
					}
				}
			}
		}else{
			throw new ItemServiceException("活动获取sku信息传入sku列表为空");
		}
		return skuList;
	}

	@Override
	public List<SkuDto> querySkuDtoListForPromotionWithBarCodeAndSpCode(List<SkuDto> dtoList) {
		List<SkuDto> skuList = new ArrayList<>();
		if(dtoList != null && CollectionUtils.isNotEmpty(dtoList)){

			skuList = itemSkuDao.querySkuDtoListForPromotionWithBarCodeAndSpCodeList(dtoList);
			if(CollectionUtils.isNotEmpty(skuList)){
				//获取规格details
				List<Long> detailIds = new ArrayList<>();
				for(SkuDto dto:skuList){
					detailIds.add(dto.getDetailId());
				}
				//获取规格组
				Map<String,Object> params = new HashMap<String,Object>();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
				List<ItemDetailSpec> listSpecDetail = itemDetailSpecDao.queryByParam(params);
				if(CollectionUtils.isNotEmpty(listSpecDetail)){
					for(int i=0;i<skuList.size();i++){
						for(ItemDetailSpec sdto: listSpecDetail){
							//detailId相等
							if(sdto.getDetailId().equals(skuList.get(i).getDetailId())){
								if(CollectionUtils.isEmpty(skuList.get(i).getItemDetailSpecList())){
									List<ItemDetailSpec> newSpecList = new ArrayList<>();
									newSpecList.add(sdto);
									skuList.get(i).setItemDetailSpecList(newSpecList);
								}else{
									skuList.get(i).getItemDetailSpecList().add(sdto);
								}
								
							}
						}
					}
				}
				//获取主图信息
				List<ItemPictures> listPicutres = itemPicturesDao.queryByParam(params);
				if(CollectionUtils.isNotEmpty(listPicutres)){
					for(int i=0;i<skuList.size();i++){
						for(ItemPictures pdto: listPicutres){
							//detailId相等
							if(pdto.getDetailId().equals(skuList.get(i).getDetailId())){
								if(CollectionUtils.isEmpty(skuList.get(i).getItemPicturesList())){
									List<ItemPictures> newPictureList = new ArrayList<>();
									newPictureList.add(pdto);
									skuList.get(i).setItemPicturesList(newPictureList);
								}else{
									skuList.get(i).getItemPicturesList().add(pdto);
								}
								
							}
						}
					}
				}
			}
		}else{
			throw new ItemServiceException("活动获取sku信息传入barCode 或 spCode为空");
		}
		return skuList;
	}
	
	/***
	 * 
	 * APP 端获取商品活动信息
	 * @ 
	 */
	@Override
	public InfoDetailDto queryItemSkuTopicInfoForAPP(String skuCode, String activityId){
			Assert.hasLength(skuCode, "APP获取商品详情接口传入skuCode参数为空。");
			Assert.hasLength(activityId, "APP获取商品详情接口传入topicId参数为空。");	
			
			InfoDetailDto returnDto = validateItemCacheExpire(skuCode,activityId);
			if (returnDto != null) {
				/**商品下架**/
				if(ItemComonConstant.ITEM_UNDERCARRIAGE.equals(returnDto.getStatus()) || ItemComonConstant.ITEM_NO_USE.equals(returnDto.getStatus())){
					//下架商品 获取商品图片信息等
					returnDto.setItemPicturesList(getItemDetailPictureInfo(returnDto));
					//下架商品 获取商品描述，商品详情，活动等信息
					checkItemUnderCarriage(skuCode, activityId, returnDto,false);
					return returnDto;
				}
				InfoDetailDto  currentSkuInfo = itemSkuDao.selectItemIdBySkuId(skuCode);
				List<SkuDto> listDetailIds = getListDetails(currentSkuInfo,skuCode);
				List<Long> ids = new ArrayList<>();
				if(!listDetailIds.isEmpty()){
					for(SkuDto dto :listDetailIds){
						ids.add(dto.getDetailId());
					}
					List<SkuDto> listSkuDetailInfo = getListSkuDetailInfo(ids,skuCode);
					getAPPItemSkuInfos(skuCode, activityId, returnDto, listDetailIds, listSkuDetailInfo);
					initRate(returnDto);
				}
				return returnDto;
			} else {
				/**实体数据库查询**/
				return getDBItemInfo(skuCode, activityId);
			}
	}
	
	
	
	
	
	/****
	 * 获取sku detailinfo
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<SkuDto> getListSkuDetailInfo(List<Long> ids,String skuCode) {
		List<SkuDto> listSkuDetailInfo  = (List<SkuDto>) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_SKU_LISTDETAILINFO.concat(skuCode));
		if(CollectionUtils.isNotEmpty(listSkuDetailInfo)){
			return listSkuDetailInfo;
		}
		listSkuDetailInfo = itemSkuService.selectSkuDetailInfo(ids);
		if(CollectionUtils.isNotEmpty(listSkuDetailInfo)){
			jedisCacheUtil.setCache(ItemRedisConstant.ITEM_FRONT_SKU_LISTDETAILINFO.concat(skuCode), listSkuDetailInfo,60);
		}
		return listSkuDetailInfo;
	}

	
	
	/***
	 * 验证 redis 商品缓存是否过期
	 * @param skuCode
	 * @return
	 */
	private InfoDetailDto validateItemCacheExpire(String skuCode,String activityId) {
		InfoDetailDto returnDto = (InfoDetailDto) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_SKU_APP_JEDIS.concat(skuCode).concat("_").concat(activityId));
		return returnDto;
	}
	
	/***
	 *  校验商品是否下架
	 * @param skuCode
	 * @param activityId
	 * @param infoDto
	 * @param setRedis
	 */
	private void checkItemUnderCarriage(String skuCode, String activityId,InfoDetailDto infoDto,boolean setRedis) {
		/**获取sku信息***/
		List<Long> detailIds = new ArrayList<>();
		detailIds.add(infoDto.getDetailId());
		List<SkuDto> listSkuDetailInfo = itemSkuService.selectSkuDetailInfo(detailIds);
		infoDto.setMainTitle(listSkuDetailInfo.get(0).getMainTitle());
		infoDto.setSubTitle(listSkuDetailInfo.get(0).getSubTitle());
		/***活动信息**/
		TopicItemInfoQuery  query = new TopicItemInfoQuery();
		query.setTopicId(Long.valueOf(activityId));
		List<String> skuIds = new ArrayList<>();
		skuIds.add(skuCode);
		query.setSkuList(skuIds); 
		List<TopicItemInfoResult> listQuantity = topicService.getTopicItemInfo(query);
		if(CollectionUtils.isNotEmpty(listQuantity)){
			infoDto.setTopicName(listQuantity.get(0).getTopicName());
			infoDto.setItemTags(listQuantity.get(0).getItemTags());
			infoDto.setXgPrice(listQuantity.get(0).getTopicPrice());
		}
		//获取描述信息
		infoDto.setDetailDesc(getItemDetailDesc(infoDto.getDetailId()));
		//获取detail 信息,添加海淘商品
		List<Long> listIds = new ArrayList<>();
		listIds.add(infoDto.getDetailId());
		List<SkuDto> detailInfo = itemSkuService.selectSkuDetailInfo(listIds);		
		if(CollectionUtils.isNotEmpty(detailInfo)){
			if(detailInfo.get(0).getWavesSign() == ItemConstant.HAI_TAO){
				infoDto.setApplyAgeId(detailInfo.get(0).getApplyAgeId());
				infoDto.setIsHT(ItemComonConstant.ITEM_HAI_TAO); 
				//获取国家图片
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("countryId", infoDto.getCountryId());
				NationalIcon nationalIcon =  nationalIconService.queryUniqueByParams(params);
				if(nationalIcon!=null && StringUtils.isNotBlank(nationalIcon.getPicPath())){
					infoDto.setCountryImagePath(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.basedata.url,nationalIcon.getPicPath()));
				}
				//国家名字
				DistrictInfo districtInfo =	districtInfoService.queryById(infoDto.getCountryId());
				if(districtInfo != null){
					infoDto.setCountryName(districtInfo.getName());
				}
			}
		}
		if(CollectionUtils.isNotEmpty(listQuantity)){
			infoDto.setTopicName(listQuantity.get(0).getTopicName());
			infoDto.setTopicType(listQuantity.get(0).getTopicType().toString());
			if((detailInfo.get(0).getWavesSign() == ItemConstant.HAI_TAO)  && listQuantity.get(0).getStockLocationId() !=  null){
				//查询保税区名称
				Warehouse wareHouse = warehouseService.queryById(listQuantity.get(0).getStockLocationId());
				
				if(wareHouse != null && wareHouse.getBondedArea()!=null){
					LOGGER.info("获取海淘商品所在仓库的类型为111111111："+wareHouse.getType());
					ClearanceChannels channelDO =  clearanceChannelsService.queryById(wareHouse.getBondedArea());
					if(wareHouse.getType() == StorageType.BONDEDAREA.getValue() || wareHouse.getType() == StorageType.OVERSEASMAIL.getValue()){
						if(channelDO != null){
							infoDto.setSendType(channelDO.getName());
						}
					}else{
						if(StorageType.DOMESTIC.getValue() == wareHouse.getType() ){
							infoDto.setSendType(StorageType.DOMESTIC.getName());
						}else if(StorageType.COMMON.getValue() == wareHouse.getType()){
							infoDto.setSendType(StorageType.COMMON.getName());
						}
					}
					
					infoDto.setRateName(ProductConstant.RATE_TYPE.SYNTHESIS.cnName);
					infoDto.setRateType(ProductConstant.RATE_TYPE.SYNTHESIS.code);
					if(StorageType.DOMESTIC.getValue().equals(wareHouse.getType()) || (channelDO!=null && ClearanceChannelsEnum.HWZY.id.equals(channelDO.getCode()))){
						infoDto.setRateType(ProductConstant.RATE_TYPE.POSTAL.code);
					}
				}
			}
		}
		if(setRedis){
			setRedisItemInfo(skuCode,activityId,infoDto);
		}
	}
	
	
	
	/***
	 * 验证 redis 商品缓存是否过期
	 * @param skuCode
	 * @return
	 * @ 
	 */
	private String getItemDetailDesc(Long detailId)  { 
		
		try{
			if (jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_DETAIL_DESC.concat(detailId.toString())) != null) { 
				return  jedisCacheUtil.getCacheString(ItemRedisConstant.ITEM_FRONT_DETAIL_DESC.concat(detailId.toString()));
			}
		}catch(Exception e){
			LOGGER.error("获取redis缓存中商品detailId对应的描述信息错误,detailId:".concat(detailId.toString()).concat(e.getMessage()), e);
		}
		//更具detailId 获取商品详情描述
		try {
			String detailDesc = itemInfoDao.selectDetailIdDesc(Long.valueOf(detailId));
			if(StringUtils.isNotBlank(detailDesc)){
				jedisCacheUtil.setCacheString(ItemRedisConstant.ITEM_FRONT_DETAIL_DESC.concat(detailId.toString()), detailDesc);
				return detailDesc;
			}
		} catch (ItemServiceException e) {
			LOGGER.error("获取数据库中商品detailId对应的描述信息错误,detailId:".concat(detailId.toString()).concat(e.getMessage()), e);
		}
		return "";
	}
	
	
	/***
	 * 放入商品缓存 
	 */
	private void setRedisItemInfo(String skuCode,String activityId,InfoDetailDto dto){
			try{
				jedisCacheUtil.setCache(ItemRedisConstant.ITEM_SKU_APP_JEDIS.concat(skuCode).concat("_").concat(activityId),dto,60);
			}catch (Exception e) {
				LOGGER.error("存储商品缓存异常：".concat(e.getMessage()), e);
			}
	}

	/****
	 * 更具sku code 获取 数据库商品信息
	 * 
	 * @param id
	 * @return
	 * @ 
	 */
	private InfoDetailDto getDBItemInfo(String skuCode,String activityId)  {
		
		InfoDetailDto infoDto = new InfoDetailDto();
		/**sku 基本信息包括  skuid sku code sku基本价 供应商等信息**/ 
		try {
			infoDto.setSku(skuCode);
			infoDto = itemSkuDao.selectItemIdBySkuId(skuCode);
			ItemInfo ItemInfo = itemInfoDao.queryById(infoDto.getItemId());
			if(ItemInfo !=null){
				ItemInfo setInfo = new ItemInfo();
				if(ItemInfo.getLargeId() != null){
					setInfo.setLargeId(ItemInfo.getLargeId());
				}
				if(ItemInfo.getMediumId() != null){
					setInfo.setMediumId(ItemInfo.getMediumId());
				}
				if(ItemInfo.getSmallId() != null){
					setInfo.setSmallId(ItemInfo.getSmallId());
				}
				infoDto.setItemInfo(setInfo);
			}
			/**商品为下架商品**/
			if(ItemComonConstant.ITEM_UNDERCARRIAGE.equals(infoDto.getStatus()) || ItemComonConstant.ITEM_NO_USE.equals(infoDto.getStatus())){
				
				  //下架商品 获取商品图片信息等
					infoDto.setItemPicturesList(getItemDetailPictureInfo(infoDto));
					
					checkItemUnderCarriage(skuCode, activityId, infoDto,true);
					return infoDto;
				}
			} catch (ItemServiceException e1) {
				LOGGER.error("获取商品基本信息出错:sku code" + skuCode, e1);
		}
		/**detilIds 查询数组**/
		List<Long> ids = new ArrayList<>();
		try {
			/**更具skucode 和 spid +item 获取 detail_id 数组***/
			List<SkuDto> listDetailIds = getListDetails(infoDto,skuCode);
			if(!listDetailIds.isEmpty()){
				for(SkuDto dto :listDetailIds){
					ids.add(dto.getDetailId());
				}
				List<SkuDto> listSkuDetailInfo = getListSkuDetailInfo(ids,skuCode);
				/**获取sku信息组***/
				getAPPItemSkuInfos(skuCode, activityId, infoDto, listDetailIds,
						listSkuDetailInfo);
			}
		} catch (ItemServiceException e2) {
			LOGGER.error("处理活动商品数据信息错误：".concat(e2.getMessage()),e2);
		}
		
		/**更具detail 数组获取规格信息 **/
		getSkuSpecList(skuCode, activityId, infoDto);
		
		infoDto.setSalesCount(itemRemoteService.getSalesCountBySku(skuCode));
		initRate(infoDto);
		setRedisItemInfo(skuCode, activityId, infoDto);
		return infoDto;
	}
	
	
	
	/****
	 * 更具sku code 获取 数据库商品信息
	 * 
	 * @param id
	 * @return
	 * @ 
	 */
	private InfoDetailDto getDBItemInfoHaiTao(String skuCode,String activityId)  {
		
		InfoDetailDto infoDto = new InfoDetailDto();
		/**sku 基本信息包括  skuid sku code sku基本价 供应商等信息**/ 
		try {
			infoDto.setSku(skuCode);
			infoDto = itemSkuDao.selectItemIdBySkuId(skuCode);
			ItemInfo ItemInfo = itemInfoDao.queryById(infoDto.getItemId());
			if(ItemInfo !=null){
				ItemInfo setInfo = new ItemInfo();
				if(ItemInfo.getLargeId() != null){
					setInfo.setLargeId(ItemInfo.getLargeId());
				}
				if(ItemInfo.getMediumId() != null){
					setInfo.setMediumId(ItemInfo.getMediumId());
				}
				if(ItemInfo.getSmallId() != null){
					setInfo.setSmallId(ItemInfo.getSmallId());
				}
				setInfo.setUnitId(ItemInfo.getUnitId());
				if(ItemInfo.getUnitId() != null){
					DictionaryInfo unitInfo = dictionaryInfoService.queryById(ItemInfo.getUnitId());
					if(unitInfo != null) setInfo.setUnitName(unitInfo.getName());
				}
				infoDto.setItemInfo(setInfo);
			}
			//获取品牌信息
			if(infoDto.getBrandId() != null){
				Brand brandInfo = brandService.queryById(infoDto.getBrandId());
				if(brandInfo != null){
					infoDto.setBrandName(brandInfo.getName());
					infoDto.setCountryId(brandInfo.getCountryId().longValue());
				}else{
					infoDto.setBrandName("");
				}
			}
			else{
				infoDto.setBrandName("");
				LOGGER.error("无法获取供应商信息:供应商ID".concat(infoDto.getBrandId().toString()));
			}
			
			/**商品为下架商品**/
			if(ItemComonConstant.ITEM_UNDERCARRIAGE.equals(infoDto.getStatus()) || ItemComonConstant.ITEM_NO_USE.equals(infoDto.getStatus())){
				
				  //下架商品 获取商品图片信息等
					infoDto.setItemPicturesList(getItemDetailPictureInfo(infoDto));
					
					checkItemUnderCarriage(skuCode, activityId, infoDto,true);
					initRate(infoDto);
					return infoDto;
				}
			} catch (ItemServiceException e1) {
				LOGGER.error("获取商品基本信息出错:sku code" + skuCode, e1);
		}
		/**detilIds 查询数组**/
		List<Long> ids = new ArrayList<>();
		try {
			/**更具skucode 和 spid +item 获取 detail_id 数组***/
			List<SkuDto> listDetailIds = getListDetails(infoDto,skuCode);
			if(!listDetailIds.isEmpty()){
				for(SkuDto dto :listDetailIds){
					ids.add(dto.getDetailId());
				}
				List<SkuDto> listSkuDetailInfo = getListSkuDetailInfo(ids,skuCode);
				/**获取sku信息组***/
				getAPPItemSkuInfosHaiTao(skuCode, activityId, infoDto, listDetailIds,
						listSkuDetailInfo);
			}
		} catch (ItemServiceException e2) {
			LOGGER.error("处理活动商品数据信息错误：".concat(e2.getMessage()),e2);
		}
		
		/**更具detail 数组获取规格信息 **/
		getSkuSpecList(skuCode, activityId, infoDto);
		
		infoDto.setSalesCount(itemRemoteService.getSalesCountBySku(skuCode));
		initRate(infoDto);

		ItemAttribute attributeQuery = new ItemAttribute();
		attributeQuery.setDetailId(infoDto.getDetailId());
		List<ItemAttribute> itemAttributeList = itemAttributeDao.queryByObject(attributeQuery);
		infoDto.setItemAttribute(itemAttributeList);

		setRedisItemInfo(skuCode, activityId, infoDto);
		return infoDto;
	}
	
	
	/***
	 * 获取listdetais
	 * @param currentSkuInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<SkuDto> getListDetails(InfoDetailDto currentSkuInfo,String skuCode) {

		try {
			
			long startTime = System.currentTimeMillis();
			List<SkuDto> listDetailIds = (List<SkuDto>) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_SKU_LISTDETAILS.concat(skuCode));
			if(CollectionUtils.isNotEmpty(listDetailIds)){
				return listDetailIds;
			}
			long endTime = System.currentTimeMillis();
			LOGGER.info("获取缓存中listdetails信息 :"+(endTime-startTime));
		} catch (Exception e) { 
			LOGGER.error("获取缓存中sku信息异常,skucode :".concat(skuCode));
		}
		ItemSku itemSku= new ItemSku();
		itemSku.setSpId(currentSkuInfo.getSpId());
		itemSku.setItemId(currentSkuInfo.getItemId());
		itemSku.setStatus(ItemStatusEnum.ONLINE.getValue());
		List<SkuDto> listDetailIds = itemSkuDao.selectProductDeatilsByItemIdAndSpId(itemSku);
		if(CollectionUtils.isNotEmpty(listDetailIds)){
			jedisCacheUtil.setCache(ItemRedisConstant.ITEM_FRONT_SKU_LISTDETAILS.concat(skuCode), listDetailIds);
		}	
		return listDetailIds;
	}
	
	

	/***
	 *  获取sku信息
	 * @param skuCode
	 * @param activityId
	 * @param infoDto
	 * @param listDetailIds
	 * @param listSkuDetailInfo
	 * @ 
	 */
	private void getAPPItemSkuInfos(String skuCode, String activityId,
			InfoDetailDto infoDto, List<SkuDto> listDetailIds,
			List<SkuDto> listSkuDetailInfo)  {
		if(CollectionUtils.isNotEmpty(listSkuDetailInfo)){
			
			/**验证sku是否在活动中**/
			List<String> checkSkuActivity = new ArrayList<>();
			
			/**验证存在在活动中sku**/
			Map<String,String> existIds = new HashMap<>();
			
			 int length = listSkuDetailInfo.size();
			for (int i = 0; i < length; i++) {
				for(SkuDto dto :listDetailIds){
					if(dto.getDetailId().equals(listSkuDetailInfo.get(i).getDetailId())){
						/**确认是否是当前sku 信息***/
						if(infoDto.getDetailId().equals(listSkuDetailInfo.get(i).getDetailId())){ 
							//清空缓存
							
							infoDto.setIsOverTime(null);
							infoDto.setIsNotStart(null);
							infoDto.setOutOfStock(null);
							infoDto.setIsError(null);
							infoDto.setListOnSaleAndWillSale(null);
							
							//本sku设置颜色 尺寸信息 这是设置页面 sku 信息
							listSkuDetailInfo.get(i).setSku(dto.getSku());
							infoDto.setMainTitle(listSkuDetailInfo.get(i).getMainTitle());
							infoDto.setSubTitle(listSkuDetailInfo.get(i).getSubTitle());
							SkuDto currentSku = new SkuDto();
							//查询规格信息
							List<ItemDetailSpec> listSpec = queryCurrentSkuDetailsInfo(dto);
							//设置skucode
							currentSku.setSku(skuCode);
							currentSku.setId(infoDto.getId()); 
							currentSku.setWavesSign(listSkuDetailInfo.get(i).getWavesSign());
							//获取运费价格
						
							//获取运费价格
							try {
								List<FreightTemplate>  freights = freightTemplateService.queryItemFreightTemplate(listSkuDetailInfo.get(i).getFreightTemplateId());
								if(CollectionUtils.isNotEmpty(freights)){
									for (FreightTemplate freight : freights) {
										
										if(freight.getCalculateMode().equals(ItemComonConstant.ITEM_FREIGHT_SINGLE)){
											currentSku.setPostage(freight.getPostage().toString());
										}
										
										if(freight.getCalculateMode().equals(ItemComonConstant.ITEM_FREIGHT_ALL)){
											if(freight.getFreePostage() != null){
												currentSku.setFreePostage(freight.getFreePostage().toString());
											}
											if(freight.getAftPostage() != null){
												currentSku.setAftPostage(freight.getAftPostage().toString());
											}
										}
									}
									
								}
							} catch (Exception e) {
								LOGGER.error("获取运费末班出错：运费模板id：".concat(listSkuDetailInfo.get(i).getFreightTemplateId().toString()));
							}
							
							//设置当前sku的规格信息
							currentSku.setListSpec(JSONArray.toJSONString(listSpec)); 
							currentSku.setDetailId(infoDto.getDetailId());
							/**当前sku 促销信息***/
						
							List<TopicItemInfoResult> listQuantity = new ArrayList<>();
							
							try {
								TopicItemInfoQuery  query = new TopicItemInfoQuery();
								query.setTopicId(Long.valueOf(activityId));
								List<String> skuIds = new ArrayList<>();
								skuIds.add(skuCode);
								query.setSkuList(skuIds); 
								//listQuantity = 	topicService.getTopicItemInfo(query);
							} catch (Exception e) {
								LOGGER.error("获取活动中 接口异常",e);
							}
							
							if(!listQuantity.isEmpty()){
								/**西客商城价，基本价格，折扣计算***/
								infoDto.setTopicName(listQuantity.get(0).getTopicName());
								infoDto.setXgPrice(listQuantity.get(0).getTopicPrice());
								if(listQuantity.get(0).getTopicPrice() == Double.valueOf(0)){
									infoDto.setDiscount(0.0);
								}else{
									BigDecimal bg = new BigDecimal((listQuantity.get(0).getTopicPrice() * 10 / infoDto.getBasicPrice()));
									infoDto.setDiscount(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());	
								}
								currentSku.setMaxNum(listQuantity.get(0).getLimitAmount());
								TopicItemInfoResult topicItemInfoResult = listQuantity.get(0);
								Integer count = inventoryQueryService.querySalableInventory(App.PROMOTION, activityId, skuCode, 
										topicItemInfoResult.getStockLocationId(), DEFAULTED.YES.equals(topicItemInfoResult.getTopicInventoryFlag()));
								currentSku.setQuantity(count);	
								if(count <= 0){
									infoDto.setOutOfStock(ItemComonConstant.ITEM_OUT_OF_STOCK);
								}
								
								if(TopicStatus.STOP.ordinal() == listQuantity.get(0).getTopicStatus()){
									infoDto.setIsOverTime(ItemComonConstant.ITEM_FRONT_ACTIVITY_OVERTIME);
									checkActivityTime(infoDto,listQuantity.get(0).getEndTime().toString(),listQuantity.get(0).getStartTime().toString());
								}else{
									//手机端加入锁定逻辑
									if(listQuantity.get(0).getLockStatus() == LockStatus.LOCK.ordinal()){
										infoDto.setOutOfStock(ItemComonConstant.ITEM_OUT_OF_STOCK);
										currentSku.setQuantity(0);
									}
									
									//是否是长期活动
									if(ItemComonConstant.ITEM_FRONT_ACTIVITY_FOREVER == listQuantity.get(0).getLastingType()){
										infoDto.setActivityType(ItemComonConstant.ITEM_FRONT_ACTIVITY_FOREVER.toString());
										checkActivityTime(infoDto,listQuantity.get(0).getEndTime().toString(),listQuantity.get(0).getStartTime().toString());
										infoDto.setCutDownTime(ItemComonConstant.ITEM_FOEVER_CUTDOWN);
									}else{
										//验证活动时间
										checkActivityTime(infoDto,listQuantity.get(0).getEndTime().toString(),listQuantity.get(0).getStartTime().toString());
									}
								}
								
							}
							/**设置当前sku 信息***/
							infoDto.setSkuDto(currentSku);
							/**图片 json list***/
							infoDto.setItemPicturesList(getItemDetailPictureInfo(infoDto));
							/***描述信息***/
							infoDto.setDetailDesc(getItemDetailAPPDesc(infoDto.getDetailId()));
						}
						
						checkSkuActivity.add(dto.getSku());
						
						/**detailID 是否在活动中**/
						existIds.put(dto.getSku(),dto.getDetailId().toString());
						
						listSkuDetailInfo.get(i).setSku(dto.getSku());
						/***设置detail组规格信息***/
						List<ItemDetailSpec> listSpec = getSkuDetailSpecInfos(listSkuDetailInfo, i);
						
						listSkuDetailInfo.get(i).setListSpec(JSONArray.toJSONString(listSpec)); 
					}
				}
			}
			
			/***同意活动验证**/
			infoDto.setListSkus(JSONArray.toJSONString(listSkuDetailInfo));
			
			
			/***验证sku是否中活动中存在**/
			
			List<Long> toAddDeatils = new ArrayList<Long>();
			
			/***验证sku是否中活动中存在**/
			try {
				if(CollectionUtils.isNotEmpty(checkSkuActivity)){
						TopicItemInfoQuery  query = new TopicItemInfoQuery();
						query.setTopicId(Long.valueOf(activityId));
						List<String> skuIds = new ArrayList<>();
						skuIds.add(skuCode);
						query.setSkuList(skuIds); 
						List<String> existSkus  = topicService.checkTopicSkuList(Long.valueOf(activityId), checkSkuActivity);
						
						List<SkuDto> existSkuList = new ArrayList<>();
						if(CollectionUtils.isNotEmpty(existSkus)){
							for(SkuDto checkDto:listSkuDetailInfo){
								if(existSkus.contains(checkDto.getSku())){
									existSkuList.add(checkDto);
								}
							}
							infoDto.setListSkus(JSONArray.toJSONString(existSkuList));
							
							//教研detailIds
							if(!existIds.isEmpty()){
							      for (Entry<String,String> entry : existIds.entrySet()) {  
							    	  if(existSkus.contains(entry.getKey())){
							    		  toAddDeatils.add(Long.valueOf(entry.getValue()));
							    	  }
							        }  
								}
								infoDto.setExistDetailIds(toAddDeatils);
						}
					}
			} catch (Exception e) {
				LOGGER.error("教研sku是否在活动中失败： 活动Id:".concat(activityId.toString()), e);
				
			}
			
		}
	}
	
	
	
	
	/***
	 * 放入规格信息
	 * @param listSkuDetailInfo
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ItemDetailSpec> getSkuDetailSpecInfos(
			List<SkuDto> listSkuDetailInfo, int i) {
		try {
			List<ItemDetailSpec> listRedis = (List<ItemDetailSpec>) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_SKU_DETAIL_SPEC_INFO.concat(listSkuDetailInfo.get(i).getDetailId().toString()));
			if(CollectionUtils.isNotEmpty(listRedis)){
				return listRedis;
			}
		} catch (Exception e) {
			LOGGER.error("获取缓存中规格信息失败");
		}
		List<ItemDetailSpec> listSpec = itemSkuService.selectSkuDetailSpecInfos(listSkuDetailInfo.get(i).getDetailId());
		try {
			jedisCacheUtil.setCache(ItemRedisConstant.ITEM_FRONT_SKU_DETAIL_SPEC_INFO.concat(listSkuDetailInfo.get(i).getDetailId().toString()), listSpec);
		} catch (Exception e) {
			LOGGER.error("放入redis 缓存规格失败。");
		}
		return listSpec;
	}

	
	
	
	@SuppressWarnings("unchecked")
	private List<ItemDetailSpec> queryCurrentSkuDetailsInfo(SkuDto dto) {
		
		try {
			List<ItemDetailSpec> listSpec  =	 (List<ItemDetailSpec>) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_CURRENT_SKU_DETAIL_INFO.concat(dto.getDetailId().toString()));
			if(CollectionUtils.isNotEmpty(listSpec)){
				return listSpec;
			}
			
		} catch (Exception e) {
			LOGGER.error("获取当前sku detailsinfo 出错:".concat(dto.getDetailId().toString()), e);
		}
		
		
		List<ItemDetailSpec> listSpec = itemSkuService.selectSkuDetailSpecInfos(dto.getDetailId());
		
		try {
			jedisCacheUtil.setCache(ItemRedisConstant.ITEM_FRONT_CURRENT_SKU_DETAIL_INFO.concat(dto.getDetailId().toString()), listSpec);
			} catch (Exception e) {
				LOGGER.error("放入 skudetail 缓存信息出错:".concat(dto.getDetailId().toString()), e);
		}
		return listSpec;
	}

	
	
	/****
	 * 校验活动开始情况 活动超时情况
	 * @param dto
	 * @param endDate
	 * @param startData
	 */
	private void  checkActivityTime(InfoDetailDto dto,String endDate,String startData) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			one =df.parse(df.format(sdf.parse(endDate)));
			two =df.parse(df.format(sdf.parse(startData)));
			long time1 = one.getTime();
			long time2 = two.getTime();
			dto.setStoretime(time2);
			dto.setEndtime(time1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 获取item  detialid  图片信息
	 * @param infoDto
	 * @return
	 * @ 
	 */
	@SuppressWarnings("unchecked")
	private List<ItemPictures> getItemDetailPictureInfo(InfoDetailDto infoDto) {
		// sku 对应图片信息
		List<ItemPictures> listPictures;
		//图片做缓存
		try{
			listPictures = (List<ItemPictures>) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_DETAIL_PICTURE_LIST.concat(infoDto.getDetailId().toString()));
			if(CollectionUtils.isNotEmpty(listPictures)){
				for(ItemPictures itemPictures:listPictures){
					if(StringUtil.isNotBlank(itemPictures.getPicture()) && !itemPictures.getPicture().startsWith("http")){
						itemPictures.setPicture(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, itemPictures.getPicture()));
					}
				}
				return listPictures;
			}
		}catch(Exception e){
			LOGGER.error("获取redis缓存中商品detailId对应的描述信息错误,detailId:".concat(infoDto.getDetailId().toString()).concat(e.getMessage()), e);
		}
		listPictures = selectItemPictures(infoDto);
		if (CollectionUtils.isNotEmpty(listPictures)) {
			for(ItemPictures itemPictures:listPictures){
				if(StringUtil.isNotBlank(itemPictures.getPicture()) && !itemPictures.getPicture().startsWith("http")){
					itemPictures.setPicture(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, itemPictures.getPicture()));
				}
			}
			jedisCacheUtil.setCache(ItemRedisConstant.ITEM_FRONT_DETAIL_PICTURE_LIST.concat(infoDto.getDetailId().toString()), listPictures);
			return listPictures;
		}
		return new ArrayList<ItemPictures>();
	}
	
	/***
	 * 获取 规格熟悉组集合
	 * @param skuCode
	 * @param activityId
	 * @param infoDto
	 * @param ids
	 */
	private void getSkuSpecList(String skuCode, String activityId,
			InfoDetailDto infoDto) {
		try{
			String  listGroup = jedisCacheUtil.getCacheString(ItemRedisConstant.ITEM_FRONT_SKU_SPEC_LIST.concat(skuCode).concat("_").concat(activityId));
			if(StringUtils.isNotBlank(listGroup)){
				infoDto.setListSpecGroup(listGroup);
			}
		}catch (Exception e) {
			LOGGER.error("获取缓存中的规格数组出错：".concat(e.getMessage()),e);
		}
		
		/**重新抓取规格信息***/
		if(jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_SKU_APP_SPEC_LIST.concat(infoDto.getSku()).concat("_").concat(activityId)) != null){
			@SuppressWarnings("unchecked")
			List<SpecGroupResult>  specGroup = (List<SpecGroupResult>) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_SKU_APP_SPEC_LIST.concat(infoDto.getSku()).concat("_").concat(activityId));
			infoDto.setSpecGroupList(specGroup);
		}else{
			getAPPSkuDetailsSpecList(infoDto,activityId);
		}
		
	}
	
	/*****
	 * 获取sku 上的规格集合
	 * @param infoDto
	 * @param ids
	 */
	private void getAPPSkuDetailsSpecList(InfoDetailDto infoDto,String activityId) {
		try {
			
			if(CollectionUtils.isEmpty(infoDto.getExistDetailIds())){
				return;
			}
			List<ItemDetailSpec> listSpecs= itemSkuService.selectSpecByDetailIds(infoDto.getExistDetailIds());
			List<Long> groupIds = new ArrayList<>();
			List<Long> specIds = new ArrayList<>();
			if(!listSpecs.isEmpty()){
				//属性分组
				for(ItemDetailSpec dto:listSpecs){
					if(!groupIds.contains(dto.getSpecGroupId())){
						groupIds.add(dto.getSpecGroupId());
					}
					if(!specIds.contains(dto.getSpecId())){
						specIds.add(dto.getSpecId());
					}
				}
				
				Map<Long,List<Long>> groupSpecList= new HashMap<>();
				for(ItemDetailSpec dto:listSpecs){
					if(!groupSpecList.containsKey(dto.getSpecGroupId())){
						List<Long> specids =  new ArrayList<>();
						groupSpecList.put(dto.getSpecGroupId(),specids);
					}
				}
				for(ItemDetailSpec dto:listSpecs){
					if(groupSpecList.containsKey(dto.getSpecGroupId())){
						List<Long> oldList= groupSpecList.get(dto.getSpecGroupId());
						if(oldList ==  null || oldList.isEmpty()){
							oldList = new ArrayList<>();
						}
						if(!oldList.contains(dto.getSpecId())){
							oldList.add(dto.getSpecId());
							groupSpecList.put(dto.getSpecGroupId(), oldList);
						}
					}
				}
				//不同的属性组 做集合
			    List<SpecGroupResult> listGroup = specGroupService.selectListSpecGroupResult(groupIds,ItemComonConstant.ITEM_GROUP_STATUS_ALL);
			    
			    List<Spec> listSpec = specService.selectListSpec(specIds, ItemComonConstant.ITEM_GROUP_STATUS_ALL);
			    
			    
			    if(!listGroup.isEmpty()){
			    	for(int i=0;i<listGroup.size();i++){
			    			List<Long> exist = groupSpecList.get(listGroup.get(i).getSpecGroup().getId());
			    			List<Spec> newList = new ArrayList<>();
			    			for( Spec specDo : listGroup.get(i).getSpecDoList()){
			    				if(exist.contains(specDo.getId())){
			    					newList.add(specDo);
			    				}
			    			}
			    			listGroup.get(i).setSpecDoList(newList);
			    	 }
			    	
		    	
			    if(CollectionUtils.isNotEmpty(listSpec) &&  CollectionUtils.isNotEmpty(listGroup)){
			    	for(int i=0;i<listGroup.size();i++){
			    		List<Spec> newList = new ArrayList<>();
			    		List<Long> exist = groupSpecList.get(listGroup.get(i).getSpecGroup().getId());
			    		for(Spec sdo:listSpec){
			    			if(exist.contains(sdo.getId())){
			    				newList.add(sdo);
			    			}
			    		}
			    		listGroup.get(i).setSpecDoList(newList);
			    	}
			    }
			    
			    	/**放入缓存**/
				    try{
				    	jedisCacheUtil.setCache(ItemRedisConstant.ITEM_FRONT_SKU_APP_SPEC_LIST.concat(infoDto.getSku()).concat("_").concat(activityId),listGroup);
				    }catch (Exception e) {
				    	LOGGER.error(e.getMessage());
					}
			    }
			    
			    
			   infoDto.setSpecGroupList(listGroup);
			    
			}
		} catch (ItemServiceException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	/***
	 * 验证 redis 商品缓存是否过期
	 * @param skuCode
	 * @return
	 * @ 
	 */
	private String getItemDetailAPPDesc(Long detailId)  {
		
		try{
			if (jedisCacheUtil.getCache(ItemRedisConstant.ITEM_FRONT_DETAIL_APP_DESC.concat(detailId.toString())) != null) { 
				return  jedisCacheUtil.getCacheString(ItemRedisConstant.ITEM_FRONT_DETAIL_APP_DESC.concat(detailId.toString()));
			}
		}catch(Exception e){
			LOGGER.error("获取redis缓存中商品detailId对应的描述信息错误,detailId:".concat(detailId.toString()).concat(e.getMessage()), e);
		}
		//更具detailId 获取商品详情描述
		try {
			String detailDesc = itemInfoDao.selectDetailIdMobileDesc(detailId);
			if(StringUtils.isNotBlank(detailDesc)){
				//手机端 图片添加  width="100%"
				String  changeDesc= detailDesc.replaceAll("alt=\"\"", "alt=\"\" width=\"100%\" ");   
				jedisCacheUtil.setCacheString(ItemRedisConstant.ITEM_FRONT_DETAIL_APP_DESC.concat(detailId.toString()), changeDesc);
				return changeDesc;
			}
		} catch (ItemServiceException e) {
			LOGGER.error("获取数据库中商品detailId对应的描述信息错误,detailId:".concat(detailId.toString()).concat(e.getMessage()), e);
		}
		return "";
	}
	
	/**
	 * 
	 * <pre>
	 *  封装规格的dto信息
	 * </pre>
	 *
	 * @param detailSpecList
	 * @return
	 */
	private List<ItemDetailSpec> spec2SpecDto(List<ItemDetailSpec> detailSpecList){
		List<ItemDetailSpec> res  = new ArrayList<ItemDetailSpec>();
		List<Long> specIds = new ArrayList<Long>();
		List<Long> specGroupIds = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(detailSpecList)){
			for(ItemDetailSpec detailSpec: detailSpecList){
				specIds.add(detailSpec.getSpecId());
				specGroupIds.add(detailSpec.getSpecGroupId());
			}
			List<SpecGroup> specGroupList = specGroupService.selectListSpecGroup(specGroupIds, ItemConstant.EFFECTIVE_DATAS);
			List<Spec> specList = specService.selectListSpec(specIds, ItemConstant.EFFECTIVE_DATAS);
			for(ItemDetailSpec detailSpec: detailSpecList){
				ItemDetailSpec dto = new ItemDetailSpec();
				dto.setSort(detailSpec.getSort());
				dto.setSpecId(detailSpec.getSpecId());
				dto.setSpecGroupId(detailSpec.getSpecGroupId());
				if(CollectionUtils.isNotEmpty(specGroupList)){
					for(SpecGroup specGroup: specGroupList){
						if(specGroup.getId().equals(detailSpec.getSpecGroupId())){
							dto.setSpecGroupName(specGroup.getName());
							break;
						}
					}
				}
				//遍历规格
				if(CollectionUtils.isNotEmpty(specList)){
					for(Spec spec: specList){
						if(spec.getId().equals(detailSpec.getSpecId())){
							dto.setSpecName(spec.getSpec());
							break;
						}
					}
				}
				res.add(dto);
			}
		}
		return res;
	}

	/**
	 * 
	 * <pre>
	 * 获取税率
	 * </pre>
	 *
	 * @param list
	 */
	private void getItemResultDtoByBase (List<ItemResultDto> list){
		if(CollectionUtils.isNotEmpty(list)){
			for(ItemResultDto itemResultDto: list){
				if(itemResultDto.getUnitId() !=null){
					DictionaryInfo unitInfo = dictionaryInfoService.queryById(itemResultDto.getUnitId());
					if(unitInfo != null){
						itemResultDto.setUnitName(unitInfo.getName());
					}
				}
				if(itemResultDto.getWavesSign()==2){
					TaxRate taxRate = taxRateService.queryById(itemResultDto.getTarrifRateId());
					if(null!=taxRate){
						itemResultDto.setTarrifRateValue(taxRate.getRate());
					}
					taxRate = taxRateService.queryById(itemResultDto.getAddedValueRateId());
					if(null!=taxRate){
						itemResultDto.setAddedValueRate(taxRate.getRate());
					}
					taxRate = taxRateService.queryById(itemResultDto.getCustomsRateId());
					if(null!=taxRate){
						itemResultDto.setCustomsRate(taxRate.getRate());
					}
					taxRate = taxRateService.queryById(itemResultDto.getExciseRateId());
					if(null!=taxRate){
						itemResultDto.setExciseRate(taxRate.getRate());
					}
				}
			}
		}
	}
	

	@Override
	public List<ItemDetail> queryPrdByBarcodeAndName(String barcode,
			String mainTitle) {
		if(StringUtils.isBlank(mainTitle)&&StringUtils.isBlank(barcode)){
			return null;
		}
		ItemDetail itemDetail = new ItemDetail();
		if(StringUtils.isNotBlank(mainTitle)){
			itemDetail.setMainTitle(mainTitle.trim());
		}
		if(StringUtils.isNotBlank(barcode)){
			itemDetail.setBarcode(barcode.trim());
		}
		List<ItemDetail> detailList = itemDetailDao.selectItemDetailsByBarcodeAndName(itemDetail);
		return detailList;
	}

	@Override
	public List<ItemDetail> queryPrdByCodeList(List<String> prdList){
		if(CollectionUtils.isEmpty(prdList)){
			return null;
		}
		return itemDetailDao.selectItemDetailsByPrdList(prdList);
	}

	@Override
	public ItemDetail queryPrdByPrdCode(String prd) {
		if(StringUtils.isBlank(prd)){
			return null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("prdId", prd);
		List<ItemDetail> detailList = itemDetailDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(detailList)){
			return detailList.get(0);
		}else{
			return null;
		}
	}

	@Override
	public String getPrdidCode(String skuCode){
		if(StringUtils.isBlank(skuCode)){
			return null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sku", skuCode);
		List<ItemSku> list = itemSkuDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getPrdid();
		}
		return null;
	}

	@Override
	public String getSpuCode(String skuCode) {
		if(StringUtils.isBlank(skuCode)){
			return null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sku", skuCode);
		List<ItemSku> list = itemSkuDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getSpu();
		}
		return null;
	}

	@Override
	public List<ItemResultDto> getSkuListForSupplierWithSpIdAndBarCodes(
			Long spId, Integer saleType,List<String> barCodes) {
		LOGGER.info("通过sku列表查询相关的商品信息,传入参数为： skuCodes:{}" ,barCodes);
		if(CollectionUtils.isEmpty(barCodes)){
			return null;
		}
		if(null == saleType){
			throw new ItemServiceException("销售类型不能为空");
		}
		/**	SEAGOOR("西客" ,0),SELLER("商家",1); */
		if(saleType.intValue() != 0 
				&& saleType.intValue()!= 1) {
			throw new ItemServiceException("销售类型只能为1或者0");
		}
		if(null == spId){
			throw new ItemServiceException("供应商Id不能为空");
		}
		
		List<ItemResultDto> resultList = new ArrayList<ItemResultDto>();

		//sku纬度信息
		Map<String,Object> params = new HashMap<String,Object>();
		if(saleType.intValue() == ItemConstant.SUPPLIER_ID){
			params.put("spId", ItemConstant.SUPPLIER_ID);
		}else{
			params.put("spId", spId);
		}
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " barcode in ('"+StringUtil.join(barCodes, "'"+Constant.SPLIT_SIGN.COMMA+"'")+"')");
		List<ItemSku> skuList =itemSkuDao.queryByParam(params);  
		if(CollectionUtils.isEmpty(skuList)){
			return null;
		}
		List<Long> detailIds = new ArrayList<Long>();
		List<Long> itemIds = new ArrayList<Long>();
		List<Long> skuIds = new ArrayList<Long>();
		ItemResultDto result = null;
		for (ItemSku itemSku : skuList) {
			if(!detailIds.contains(itemSku.getDetailId())){
				detailIds.add(itemSku.getDetailId());
			}
			if(!itemIds.contains(itemSku.getItemId())){
				itemIds.add(itemSku.getItemId());
			}
			skuIds.add(itemSku.getId());
			
			result = new ItemResultDto();
			result.setBarcode(itemSku.getBarcode());
			result.setBasicPrice(itemSku.getBasicPrice());
			result.setCreateTime(itemSku.getCreateTime());
			result.setItemId(itemSku.getItemId());
			result.setDetailId(itemSku.getDetailId());
			result.setMainTitle(itemSku.getDetailName());
			result.setSku(itemSku.getSku());
			result.setSkuId(itemSku.getId());
			result.setSpu(itemSku.getSpu());
			result.setStatus(itemSku.getStatus());
			result.setSaleType(itemSku.getSaleType());
			result.setSupplierCode(itemSku.getSpCode());
			result.setSupplierId(itemSku.getSpId());
			result.setSupplierName(itemSku.getSpName());
			resultList.add(result);
		}
		
		
		//prdid纬度信息
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemDetail> detailList = itemDetailDao.queryByParam(params);
		if(!CollectionUtils.isEmpty(detailList)){
			for (ItemDetail itemDetail : detailList) {
				for (ItemResultDto item : resultList) {
					if(item.getDetailId().longValue()==itemDetail.getId().longValue()){
						params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+item.getDetailId()+")");
						List<ItemDetailSpec> detailSpecList = itemDetailSpecDao.queryByParam(params);
						item.setItemDetailSpecList(spec2SpecDto(detailSpecList));//转换成dto
						item.setSubTitle(itemDetail.getSubTitle());
						item.setWavesSign(itemDetail.getWavesSign());
						item.setTarrifRateId(itemDetail.getTarrifRate());//关税
						item.setWeightNet(itemDetail.getWeightNet());//净重 
						item.setWeight(itemDetail.getWeight());//毛重 
						item.setFreightTemplateId(itemDetail.getFreightTemplateId());
						item.setCartonSpec(itemDetail.getCartonSpec());//箱规
						item.setSpecifications(itemDetail.getSpecifications());//规格
						item.setMainTitle(itemDetail.getMainTitle());//统一下前台显示名称..
					}
				}
			}
		}
		
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemDetailSpec> detailSpecList = itemDetailSpecDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(detailSpecList)){
			Map<Long, List<ItemDetailSpec>> map = new HashMap<Long, List<ItemDetailSpec>>();
			for (ItemDetailSpec detailSpec : detailSpecList) {
				Long detailId = detailSpec.getDetailId();
				List<ItemDetailSpec> detailSpecs = map.get(detailId);
				if(CollectionUtils.isEmpty(detailSpecs)){
					detailSpecs = new ArrayList<ItemDetailSpec>();
					map.put(detailId, detailSpecs);
				}
				detailSpecs.add(detailSpec);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemDetailSpec>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setItemDetailSpecList(spec2SpecDto(entry.getValue()));//转换成dto
					}
				}
			}
		}
		
		//spu纬度信息
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(itemIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemInfo> infoList = itemInfoDao.queryByParam(params);
		if(!CollectionUtils.isEmpty(infoList)){
			for (ItemInfo infoDO : infoList) {
				for (ItemResultDto item : resultList) {
					if(item.getItemId().longValue()==infoDO.getId().longValue()){
						item.setBrandId(infoDO.getBrandId().longValue());
						if( infoDO.getBrandId() !=null){
							Brand brand = brandService.queryById(infoDO.getBrandId());
							if(brand != null){
								item.setBrandName(brand.getName());
							}
						}
						item.setLargeId(infoDO.getLargeId());
						item.setMediumId(infoDO.getMediumId());
						item.setSmallId(infoDO.getSmallId());
						item.setUnitId(infoDO.getUnitId().longValue());
					}
				}
			}
		}
		
		//查询主图
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemPictures> picturesDOs = itemPicturesDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(picturesDOs)){
			Map<Long, List<ItemPictures>> map = new HashMap<Long, List<ItemPictures>>();
			for (ItemPictures picturesDO : picturesDOs) {
				Long detailId = picturesDO.getDetailId();
				List<ItemPictures> list = map.get(detailId);
				if(CollectionUtils.isEmpty(list)){
					list = new ArrayList<ItemPictures>();
					map.put(detailId, list);
				}
				list.add(picturesDO);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemPictures>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setImageUrl(entry.getValue().get(0).getPicture());
					}
				}
			}
			
		}
		
		//属性信息
		List<ItemAttribute> attributeDOs = itemAttributeDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(attributeDOs)){
			Map<Long, List<ItemAttribute>> map = new HashMap<Long, List<ItemAttribute>>();
			for (ItemAttribute attr : attributeDOs) {
				Long detailId = attr.getDetailId();
				List<ItemAttribute> list = map.get(detailId);
				if(CollectionUtils.isEmpty(list)){
					list = new ArrayList<ItemAttribute>();
					map.put(detailId, list);
				}
				list.add(attr);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemAttribute>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setItemAttribute(entry.getValue());
					}
				}
			}
		}
		//base服务 查询关税税率
		getItemResultDtoByBase(resultList);
		return resultList;
	}

	@Override
	public List<ItemSkuArt> checkBoundedInfoForSales(List<ItemSkuArt> checkList)
			{
		Assert.notEmpty(checkList);
		if(CollectionUtils.isNotEmpty(checkList)){
			List<ItemSkuArt> returnList = new ArrayList<>();
			for(ItemSkuArt checkDO:checkList){
				List<ItemSkuArt> getDO = itemSkuArtDao.queryByObject(checkDO);
				if(CollectionUtils.isNotEmpty(getDO)){
					returnList.add(getDO.get(0));
				}
			}
			return returnList;
		}
		return null;
	}
	//手机端海淘接口
	@Override
	public InfoDetailDto queryItemSkuTopicInfoForAPPHaiTao(String skuCode,String activityId){
		
		Assert.hasLength(skuCode, "APP获取商品详情接口传入skuCode参数为空。");
		Assert.hasLength(activityId, "APP获取商品详情接口传入topicId参数为空。");	
		
		InfoDetailDto returnDto = validateItemCacheExpire(skuCode,activityId);
		if (returnDto != null) {
			/**商品下架**/
			if(ItemComonConstant.ITEM_UNDERCARRIAGE.equals(returnDto.getStatus()) || ItemComonConstant.ITEM_NO_USE.equals(returnDto.getStatus())){
				//下架商品 获取商品图片信息等
				returnDto.setItemPicturesList(getItemDetailPictureInfo(returnDto));
				//下架商品 获取商品描述，商品详情，活动等信息
				checkItemUnderCarriage(skuCode, activityId, returnDto,false);
				initRate(returnDto);
				return replaceDesc(returnDto);
			}
			InfoDetailDto  currentSkuInfo = itemSkuDao.selectItemIdBySkuId(skuCode);
			List<SkuDto> listDetailIds = getListDetails(currentSkuInfo,skuCode);
			List<Long> ids = new ArrayList<>();
			if(!listDetailIds.isEmpty()){
				for(SkuDto dto :listDetailIds){
					ids.add(dto.getDetailId());
				}
				List<SkuDto> listSkuDetailInfo = getListSkuDetailInfo(ids,skuCode);
				getAPPItemSkuInfosHaiTao(skuCode, activityId, returnDto, listDetailIds, listSkuDetailInfo);
				initRate(returnDto);
			}
			return replaceDesc(returnDto);
		} else {
			/**实体数据库查询**/
			return replaceDesc(getDBItemInfoHaiTao(skuCode, activityId));
		}
	}
	
	public InfoDetailDto replaceDesc(final InfoDetailDto infoDetailDto){
		String detailDesc = infoDetailDto.getDetailDesc();
		if(StringUtil.isNotBlank(detailDesc) && ArrayUtils.isNotEmpty(replaceimages.split(SPLIT_SIGN.COMMA))){
			for(String restr:replaceimages.split(SPLIT_SIGN.COMMA)){
				detailDesc = detailDesc.replaceAll("<img\\s+src=\".+"+restr+".jpg.+/>", "");
			}
		}
		infoDetailDto.setDetailDesc(detailDesc);
		return infoDetailDto;
	}
	
	
	

/***
 *  获取sku信息
 * @param skuCode
 * @param activityId
 * @param infoDto
 * @param listDetailIds
 * @param listSkuDetailInfo
 * @ 
 */
private void getAPPItemSkuInfosHaiTao(String skuCode, String activityId,InfoDetailDto infoDto, List<SkuDto> listDetailIds,
		List<SkuDto> listSkuDetailInfo){
	if(CollectionUtils.isNotEmpty(listSkuDetailInfo)){
		/**验证sku是否在活动中**/
		List<String> checkSkuActivity = new ArrayList<>();
		/**验证存在在活动中sku**/
		Map<String,String> existIds = new HashMap<>();
		int length = listSkuDetailInfo.size();
		
		for (int i = 0; i < length; i++) {
			for(SkuDto dto :listDetailIds){
				if(dto.getDetailId().equals(listSkuDetailInfo.get(i).getDetailId())){
					/**确认是否是当前sku 信息***/
					if(infoDto.getDetailId().equals(listSkuDetailInfo.get(i).getDetailId())){ 
						//清空缓存
						
						infoDto.setIsOverTime(null);
						infoDto.setIsNotStart(null);
						infoDto.setOutOfStock(null);
						infoDto.setIsError(null);
						infoDto.setListOnSaleAndWillSale(null);
						infoDto.setLimitCount(null);
						infoDto.setSendType(null);
						infoDto.setCountryImagePath(null);
						infoDto.setIsHT(null);
						
						//本sku设置颜色 尺寸信息 这是设置页面 sku 信息
						listSkuDetailInfo.get(i).setSku(dto.getSku());
						infoDto.setMainTitle(listSkuDetailInfo.get(i).getMainTitle());
						infoDto.setSubTitle(listSkuDetailInfo.get(i).getSubTitle());
						infoDto.setTarrifRate(null);
						infoDto.setItemType(listSkuDetailInfo.get(i).getItemType());
						//加入海淘逻辑
						if(listSkuDetailInfo.get(i).getWavesSign() == ItemConstant.HAI_TAO){
							infoDto.setApplyAgeId(listSkuDetailInfo.get(i).getApplyAgeId());
							infoDto.setIsHT(ItemComonConstant.ITEM_HAI_TAO); 
							//获取国家图片
							Map<String,Object> params = new HashMap<String,Object>();
							params.put("countryId", infoDto.getCountryId());
							NationalIcon nationalIcon =  nationalIconService.queryUniqueByParams(params);
							if(nationalIcon!=null && nationalIcon.getPicPath()!=null){
								infoDto.setCountryImagePath(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.basedata.url, nationalIcon.getPicPath()));
							}
							//国家名字
							DistrictInfo districtInfo =	districtInfoService.queryById(listSkuDetailInfo.get(i).getCountryId());
							if(districtInfo != null){
								infoDto.setCountryName(districtInfo.getName());
							}
							//海淘设置运费模板
							SupplierInfo supplierInfo = supplierInfoService.queryById(infoDto.getSpId());
							if(supplierInfo != null ){
								listSkuDetailInfo.get(i).setFreightTemplateId(supplierInfo.getFreightTemplateId());
							}
						}
						
						SkuDto currentSku = new SkuDto();
						//查询规格信息
						List<ItemDetailSpec> listSpec = queryCurrentSkuDetailsInfo(dto);
						//设置skucode
						currentSku.setSku(skuCode);
						currentSku.setId(infoDto.getId()); 
						currentSku.setWavesSign(listSkuDetailInfo.get(i).getWavesSign());
						//获取运费价格
					
						//获取运费价格
						List<FreightTemplate>  freights = freightTemplateService.queryItemFreightTemplate(listSkuDetailInfo.get(i).getFreightTemplateId());
						if(CollectionUtils.isNotEmpty(freights)){
							for (FreightTemplate freight : freights) {
								if(freight.getId() == listSkuDetailInfo.get(i).getFreightTemplateId()){
									//海淘商品 邮费类型为0
									if(freight.getCalculateMode().equals(ItemComonConstant.ITEM_FREIGHT_ALL)){
										if(freight.getFreePostage() != null){
											currentSku.setFreePostage(freight.getFreePostage().toString());
										}	
										currentSku.setPostage(freight.getPostage().toString());
									}
								}
							}
						}
						//设置当前sku的规格信息
						currentSku.setListSpec(JSONArray.toJSONString(listSpec)); 
						currentSku.setDetailId(infoDto.getDetailId());
						/**当前sku 促销信息***/
					
						TopicItemInfoQuery  query = new TopicItemInfoQuery();
						query.setTopicId(Long.valueOf(activityId));
						List<String> skuIds = new ArrayList<>();
						skuIds.add(skuCode);
						query.setSkuList(skuIds); 
						List<TopicItemInfoResult> listQuantity = topicService.getTopicItemInfo(query);
						Topic topic = topicService.queryById(Long.valueOf(activityId));
						if(CollectionUtils.isNotEmpty(listQuantity)){
							/**西客商城价，基本价格，折扣计算***/
							infoDto.setTopicName(listQuantity.get(0).getTopicName());
							infoDto.setXgPrice(listQuantity.get(0).getTopicPrice());
							infoDto.setLimitCount(listQuantity.get(0).getLimitAmount());
							infoDto.setItemTags(listQuantity.get(0).getItemTags());
							infoDto.setSalesPattern(topic == null ? null: topic.getSalesPartten());
							if(listQuantity.get(0).getTopicPrice() == Double.valueOf(0)){
								infoDto.setDiscount(0.0);
							}else{
								BigDecimal bg = new BigDecimal((listQuantity.get(0).getTopicPrice() * 10 / infoDto.getBasicPrice()));
								infoDto.setDiscount(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());	
							}
							currentSku.setMaxNum(listQuantity.get(0).getLimitAmount());
							
							if(TIPurchaseMethod.NORMAL.getKey() == listQuantity.get(0).getPurchaseMethod()){
								//两个按钮都有
								infoDto.setPurchasePage(ItemComonConstant.ITEM_PAGE_BOTH_BUTTON);
							}else if(TIPurchaseMethod.AT_ONCE_PURCHASE.getKey() == listQuantity.get(0).getPurchaseMethod()){
								//只有立即购买按钮
								infoDto.setPurchasePage(ItemComonConstant.ITEM_PAGE_ONLY);
							}
							
							if(listQuantity.get(0).getStockLocationId() != null){
								//查询保税区名称
								Warehouse wareHouse = warehouseService.queryById(listQuantity.get(0).getStockLocationId());
								if(wareHouse != null){
									ClearanceChannels channelDO =  clearanceChannelsService.queryById(wareHouse.getBondedArea());
									LOGGER.info("获取海淘商品所在仓库的类型为："+wareHouse.getType());
									if(StorageType.BONDEDAREA.getValue().equals(wareHouse.getType()) || 
											StorageType.OVERSEASMAIL.getValue().equals(wareHouse.getType()) ||
											StorageType.COMMON_SEA.getValue().equals(wareHouse.getType())){
										if(channelDO != null)  infoDto.setSendType(channelDO.getName());
									}else{
										if(StorageType.DOMESTIC.getValue().equals(wareHouse.getType())){
											infoDto.setSendType(StorageType.DOMESTIC.getName());
										}else if(StorageType.COMMON.getValue().equals(wareHouse.getType())){
											infoDto.setSendType(StorageType.COMMON.getName());
										}
									}
									//税率类型设置									
									infoDto.setRateName(ProductConstant.RATE_TYPE.SYNTHESIS.cnName);
									infoDto.setRateType(ProductConstant.RATE_TYPE.SYNTHESIS.code);
									if (StorageType.DOMESTIC.getValue().equals(wareHouse.getType())){
										infoDto.setRateType(RATE_TYPE.TAXFREE.code);
										infoDto.setRateName(RATE_TYPE.TAXFREE.cnName);
									}else if(StorageType.OVERSEASMAIL.getValue().equals(wareHouse.getType()) && //境外直发海外直邮
											ClearanceChannelsEnum.HWZY.id.equals(channelDO.getId())){
										infoDto.setRateType(ProductConstant.RATE_TYPE.POSTAL.code); //行邮税
										infoDto.setRateName(RATE_TYPE.POSTAL.cnName);
									}
								}
							}
							
							
//							int count = inventoryQueryService.selectInvetory(App.PROMOTION, activityId, skuCode);
//							currentSku.setQuantity(count);	
//							if(count <= 0){
//								infoDto.setOutOfStock(ItemComonConstant.ITEM_OUT_OF_STOCK);
//							}
							TopicItemInfoResult topicItemInfoResult = listQuantity.get(0);
							Integer count = inventoryQueryService.querySalableInventory(App.PROMOTION, activityId, skuCode, 
									topicItemInfoResult.getStockLocationId(), DEFAULTED.YES.equals(topicItemInfoResult.getTopicInventoryFlag()));
							currentSku.setQuantity(count);	
							if(count <= 0){
								infoDto.setOutOfStock(ItemComonConstant.ITEM_OUT_OF_STOCK);
							}
							
							if(TopicStatus.STOP.ordinal() == listQuantity.get(0).getTopicStatus()){
								infoDto.setIsOverTime(ItemComonConstant.ITEM_FRONT_ACTIVITY_OVERTIME);
								checkActivityTime(infoDto,listQuantity.get(0).getEndTime().toString(),listQuantity.get(0).getStartTime().toString());
							}else{
								//手机端加入锁定逻辑
								if(listQuantity.get(0).getLockStatus() == LockStatus.LOCK.ordinal()){
									infoDto.setOutOfStock(ItemComonConstant.ITEM_OUT_OF_STOCK);
									infoDto.setStatus(ItemComonConstant.TOPIC_BACKORDERED);
									currentSku.setQuantity(0);
								}
								
								//是否是长期活动
								if(ItemComonConstant.ITEM_FRONT_ACTIVITY_FOREVER == listQuantity.get(0).getLastingType()){
									infoDto.setActivityType(ItemComonConstant.ITEM_FRONT_ACTIVITY_FOREVER.toString());
									checkActivityTime(infoDto,listQuantity.get(0).getEndTime().toString(),listQuantity.get(0).getStartTime().toString());
									infoDto.setCutDownTime(ItemComonConstant.ITEM_FOEVER_CUTDOWN);
								}else{
									//验证活动时间
									checkActivityTime(infoDto,listQuantity.get(0).getEndTime().toString(),listQuantity.get(0).getStartTime().toString());
								}
							}
							
						}
						/**设置当前sku 信息***/
						infoDto.setSkuDto(currentSku);
						/**图片 json list***/
						infoDto.setItemPicturesList(getItemDetailPictureInfo(infoDto));
						/***描述信息***/
						infoDto.setDetailDesc(getItemDetailAPPDesc(infoDto.getDetailId()));
					}
					
					checkSkuActivity.add(dto.getSku());
					
					/**detailID 是否在活动中**/
					existIds.put(dto.getSku(),dto.getDetailId().toString());
					
					listSkuDetailInfo.get(i).setSku(dto.getSku());
					/***设置detail组规格信息***/
					List<ItemDetailSpec> listSpec = getSkuDetailSpecInfos(listSkuDetailInfo, i);
					
					listSkuDetailInfo.get(i).setListSpec(JSONArray.toJSONString(listSpec)); 
				}
			}
		}
		
		/***同意活动验证**/
		infoDto.setListSkus(JSONArray.toJSONString(listSkuDetailInfo));
		/***验证sku是否中活动中存在**/
		List<Long> toAddDeatils = new ArrayList<Long>();
		/***验证sku是否中活动中存在**/
		try {
			if(CollectionUtils.isNotEmpty(checkSkuActivity)){
				TopicItemInfoQuery  query = new TopicItemInfoQuery();
				query.setTopicId(Long.valueOf(activityId));
				List<String> skuIds = new ArrayList<>();
				skuIds.add(skuCode);
				query.setSkuList(skuIds); 
				List<String> existSkus  = topicService.checkTopicSkuList(Long.valueOf(activityId), checkSkuActivity);
				
				List<SkuDto> existSkuList = new ArrayList<>();
				if(CollectionUtils.isNotEmpty(existSkus)){
					for(SkuDto checkDto:listSkuDetailInfo){
						if(existSkus.contains(checkDto.getSku())){
							existSkuList.add(checkDto);
						}
					}
					infoDto.setListSkus(JSONArray.toJSONString(existSkuList));
					
					//教研detailIds
					if(!existIds.isEmpty()){
					      for (Entry<String,String> entry : existIds.entrySet()) {  
					    	  if(existSkus.contains(entry.getKey())){
					    		  toAddDeatils.add(Long.valueOf(entry.getValue()));
					    	  }
					        }  
						}
						infoDto.setExistDetailIds(toAddDeatils);
				}
			}
		} catch (Exception e) {
			LOGGER.error("教研sku是否在活动中失败： 活动Id:".concat(activityId.toString()), e);
		}
		}
	}

	@Override
	public SkuInfoResult selectSkuInfo(SkuInfoQuery sku)
			{
		if(null == sku){
			throw new ItemServiceException("查询sku不能为空");
		}
		if(null == sku.getSaleType()){
			throw new ItemServiceException("销售类型不能为空");
		}
		/** SEAGOOR("西客" ,0),SELLER("商家",1); */
		if(sku.getSaleType().intValue() != 0 
				&& sku.getSaleType().intValue()!= 1) {
			throw new ItemServiceException("销售类型只能为1或者0");
		}
		if(null == sku.getSupplierId()){
			throw new ItemServiceException("供应商Id不能为空");
		}
		if(StringUtils.isBlank(sku.getSku())&&StringUtils.isBlank(sku.getBarcode())){
			throw new ItemServiceException("查询sku,sku编码,barcode不能都为空");
		}
		ItemSku itemSku = new ItemSku();
		itemSku.setSku(sku.getSku());
		itemSku.setBarcode(sku.getBarcode());
		itemSku.setSaleType(sku.getSaleType());
//		if(sku.getSaleType() == ItemSaleTypeEnum.SEAGOOR.getValue().intValue()){
//			itemSku.setSpId(ItemConstant.SUPPLIER_ID);
//		}else{
		itemSku.setSpId(sku.getSupplierId());
//		}
		SkuInfoResult skuInfo = new SkuInfoResult();
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("sku", itemSku.getSku());
			params.put("barcode", itemSku.getBarcode());
			params.put("saleType", itemSku.getSaleType());
			params.put("spId", itemSku.getSpId());
			List<ItemSku> list = itemSkuDao.queryByParamNotEmpty(params);
			if (CollectionUtils.isNotEmpty(list)) {
				itemSku = list.get(0);
				skuInfo.setBarcode(itemSku.getBarcode());
				skuInfo.setCategoryCode(itemSku.getCategoryCode());
				skuInfo.setSku(itemSku.getSku());
				skuInfo.setSkuName(itemSku.getDetailName());
				skuInfo.setBrandId(itemSku.getBrandId());
				skuInfo.setStatus(itemSku.getStatus());
				skuInfo.setUnitId(itemSku.getUnitId());
				skuInfo.setBasicPrice(itemSku.getBasicPrice());
				if (null != itemSku.getDetailId()) {
					ItemDetail detail = itemDetailDao.queryById(itemSku.getDetailId());
					if (null != detail) {
						skuInfo.setSpecifications(detail.getSpecifications());
						skuInfo.setCartonSpec(detail.getCartonSpec());
						skuInfo.setWavesSign(detail.getWavesSign());// 是否为海淘商品
						// 规格
						params.clear();
						params.put("detailId", detail.getId());
						List<ItemDetailSpec> itemDetailSpecList = itemDetailSpecDao.queryByParam(params);
						skuInfo.setItemDetailSpecList(itemDetailSpecList);
					}
				}
				// 类别
				if (null != itemSku.getItemId()) {
					ItemInfo info = itemInfoDao.queryById(itemSku.getItemId());
					if (null != info) {
						skuInfo.setLargeId(info.getLargeId());
						skuInfo.setMediumId(info.getMediumId());
						skuInfo.setSmallId(info.getSmallId());
					}
				}
			} else {
				LOGGER.error("通过sku,供应商id查询商品信息,传入参数为： sku:{}" ,sku.toString());
				throw new ItemServiceException("Sku在系统中不存在");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return skuInfo;
	}

	@Override
	public List<ItemResultDto> getSkuListForSupplierWithSpIdAndSkuCodes(
			Long spId, Integer saleType, List<String> skuCodes)
			{
		LOGGER.info("通过sku列表查询相关的商品信息,传入参数为： skuCodes:{}" ,skuCodes);
		if(CollectionUtils.isEmpty(skuCodes)){
			return null;
		}
		if(null == saleType){
			throw new ItemServiceException("销售类型不能为空");
		}
		/** SEAGOOR("西客" ,0),SELLER("商家",1); */
		if(saleType.intValue() != 0 
				&& saleType.intValue()!= 1) {
			throw new ItemServiceException("销售类型只能为1或者0");
		}
		if(null == spId){
			throw new ItemServiceException("供应商Id不能为空");
		}
		List<ItemResultDto> resultList = new ArrayList<ItemResultDto>();
		//sku纬度信息
		Map<String,Object> params = new HashMap<String,Object>();
		if(saleType.intValue() == ItemConstant.SUPPLIER_ID){
			params.put("spId", ItemConstant.SUPPLIER_ID);
		}else{
			params.put("spId", spId);
		}
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," sku in ('"+StringUtil.join(skuCodes, "'"+Constant.SPLIT_SIGN.COMMA+"'")+"')");
		List<ItemSku> skuList =itemSkuDao.queryByParam(params);
		if(CollectionUtils.isEmpty(skuList)){
			return null;
		}
		List<Long> detailIds = new ArrayList<Long>();
		List<Long> itemIds = new ArrayList<Long>();
		List<Long> skuIds = new ArrayList<Long>();
		ItemResultDto result = null;
		for (ItemSku itemSku : skuList) {
			
			if(!detailIds.contains(itemSku.getDetailId())){
				detailIds.add(itemSku.getDetailId());
			}
			if(!itemIds.contains(itemSku.getItemId())){
				itemIds.add(itemSku.getItemId());
			}
			skuIds.add(itemSku.getId());
			result = new ItemResultDto();
			result.setBarcode(itemSku.getBarcode());
			result.setBasicPrice(itemSku.getBasicPrice());
			result.setCreateTime(itemSku.getCreateTime());
			result.setItemId(itemSku.getItemId());
			result.setDetailId(itemSku.getDetailId());
			result.setMainTitle(itemSku.getDetailName());
			result.setSku(itemSku.getSku());
			result.setSkuId(itemSku.getId());
			result.setSpu(itemSku.getSpu());
			result.setStatus(itemSku.getStatus());
			result.setSaleType(itemSku.getSaleType());
			result.setSupplierCode(itemSku.getSpCode());
			result.setSupplierId(itemSku.getSpId());
			result.setSupplierName(itemSku.getSpName());
			resultList.add(result);
		}
		//prdid纬度信息
		params.remove("spId");
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemDetail> detailList = itemDetailDao.queryByParam(params);
		if(!CollectionUtils.isEmpty(detailList)){
			for (ItemDetail itemDetail : detailList) {
				for (ItemResultDto item : resultList) {
					if(item.getDetailId().longValue()==itemDetail.getId().longValue()){
						params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," detail_id in ("+item.getDetailId()+")");
						List<ItemDetailSpec> detailSpecList = itemDetailSpecDao.queryByParam(params);
						item.setItemDetailSpecList(spec2SpecDto(detailSpecList));//转换成dto
						item.setSubTitle(itemDetail.getSubTitle());
						item.setWavesSign(itemDetail.getWavesSign());
						item.setTarrifRateId(itemDetail.getTarrifRate());//关税
						item.setWeightNet(itemDetail.getWeightNet());//净重 
						item.setWeight(itemDetail.getWeight());//毛重 
						item.setFreightTemplateId(itemDetail.getFreightTemplateId());
						item.setCartonSpec(itemDetail.getCartonSpec());//箱规
						item.setSpecifications(itemDetail.getSpecifications());//规格
						item.setMainTitle(itemDetail.getMainTitle());//统一下前台显示名称..
					}
				}
			}
		}
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemDetailSpec> detailSpecList = itemDetailSpecDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(detailSpecList)){
			Map<Long, List<ItemDetailSpec>> map = new HashMap<Long, List<ItemDetailSpec>>();
			for (ItemDetailSpec detailSpec : detailSpecList) {
				Long detailId = detailSpec.getDetailId();
				List<ItemDetailSpec> detailSpecs = map.get(detailId);
				if(CollectionUtils.isEmpty(detailSpecs)){
					detailSpecs = new ArrayList<ItemDetailSpec>();
					map.put(detailId, detailSpecs);
				}
				detailSpecs.add(detailSpec);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemDetailSpec>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setItemDetailSpecList(spec2SpecDto(entry.getValue()));//转换成dto
					}
				}
			}
		}
		//spu纬度信息
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," id in ("+StringUtil.join(itemIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemInfo> infoList = itemInfoDao.queryByParam(params);
		if(!CollectionUtils.isEmpty(infoList)){
			for (ItemInfo itemInfo : infoList) {
				for (ItemResultDto item : resultList) {
					if(item.getItemId().longValue()==itemInfo.getId().longValue()){
						item.setBrandId(itemInfo.getBrandId().longValue());
						if( itemInfo.getBrandId() !=null){
							Brand brand = brandService.queryById(itemInfo.getBrandId());
							if(brand != null){
								item.setBrandName(brand.getName());
							}
						}
						item.setLargeId(itemInfo.getLargeId());
						item.setMediumId(itemInfo.getMediumId());
						item.setSmallId(itemInfo.getSmallId());
						item.setUnitId(itemInfo.getUnitId().longValue());
					}
				}
			}
		}
		//查询主图
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemPictures> itemPicturesList = itemPicturesDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(itemPicturesList)){
			Map<Long, List<ItemPictures>> map = new HashMap<Long, List<ItemPictures>>();
			for (ItemPictures picturesDO : itemPicturesList) {
				Long detailId = picturesDO.getDetailId();
				List<ItemPictures> list = map.get(detailId);
				if(CollectionUtils.isEmpty(list)){
					list = new ArrayList<ItemPictures>();
					map.put(detailId, list);
				}
				list.add(picturesDO);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemPictures>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setImageUrl(entry.getValue().get(0).getPicture());
					}
				}
			}
			
		}
		//属性信息
		List<ItemAttribute> attributeDOs = itemAttributeDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(attributeDOs)){
			Map<Long, List<ItemAttribute>> map = new HashMap<Long, List<ItemAttribute>>();
			for (ItemAttribute attr : attributeDOs) {
				Long detailId = attr.getDetailId();
				List<ItemAttribute> list = map.get(detailId);
				if(CollectionUtils.isEmpty(list)){
					list = new ArrayList<ItemAttribute>();
					map.put(detailId, list);
				}
				list.add(attr);
			}
			for (ItemResultDto item : resultList) {
				for(Entry<Long, List<ItemAttribute>> entry : map.entrySet()){
					if(item.getDetailId().longValue()==entry.getKey().longValue()){
						item.setItemAttribute(entry.getValue());
					}
				}
			}
		}
		//base服务 查询关税税率
		getItemResultDtoByBase(resultList);
		return resultList;
	}
	
	@Override
	public List<ItemSku>queryByItemQueryNotEmpty(ItemQuery itemquery ){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sku", itemquery.getSku());
		params.put("barcode", itemquery.getBarcode());
		params.put("sale_type", itemquery.getSaleType());
		params.put("sp_id", itemquery.getSupplierId());

		ItemSku sku = new ItemSku();
		sku.setSku(itemquery.getSku());
		sku.setSaleType(itemquery.getSaleType());
		sku.setSpId(itemquery.getSupplierId());

		return itemSkuDao.queryByParamNotEmpty(BeanUtil.beanMap(sku));
	}

	
	@Override
	public List<ItemDetailSpec> queryByDetailId(Long detailid ){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detail_id", detailid);
		return itemDetailSpecDao.queryByParam(params);		
	}

	//计算商品税率和税金
	public void initRate(InfoDetailDto infoDetail){
		ItemDetail itemDetail = itemDetailDao.queryById(infoDetail.getDetailId());
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+itemDetail.getTarrifRate()+SPLIT_SIGN.COMMA+
				itemDetail.getCustomsRate()+SPLIT_SIGN.COMMA+
				itemDetail.getAddedValueRate()+SPLIT_SIGN.COMMA+
				itemDetail.getExciseRate()+")");
		List<TaxRate> taxRateList = taxRateService.queryByParam(params);
		Map<Long,TaxRate> taxMap = new HashMap<Long,TaxRate>();
		for(TaxRate taxRate:taxRateList){
			taxMap.put(taxRate.getId(), taxRate);
		}
		if(ProductConstant.RATE_TYPE.POSTAL.code.equals(infoDetail.getRateType())){
			//行邮税暂时不计算，直邮之类的商品以跨境综合税为准
			//Double taxRate = getPostalTaxRate(itemDetail, taxMap, infoDetail.getXgPrice());
			Double taxRate = getSynthesisTaxRate(itemDetail, taxMap, infoDetail.getXgPrice());
			infoDetail.setTaxRate(multiply(taxRate,100).doubleValue());
			infoDetail.setRateFee(toPrice(multiply(infoDetail.getXgPrice(),taxRate)).doubleValue());
		}else if(ProductConstant.RATE_TYPE.SYNTHESIS.code.equals(infoDetail.getRateType())){
			Double taxRate = getSynthesisTaxRate(itemDetail, taxMap, infoDetail.getXgPrice());
			infoDetail.setTaxRate(multiply(taxRate,100).doubleValue());
			infoDetail.setRateFee(toPrice(multiply(infoDetail.getXgPrice(),taxRate)).doubleValue());
		}else{
			infoDetail.setTaxRate(0.0);
			infoDetail.setRateFee(0.0);
		}
	}
	//计算跨境综合税
	private Double getSynthesisTaxRate(ItemDetail itemDetail, Map<Long, TaxRate> taxMap, Double itemPrice){
		Double customsRate = taxMap.get(itemDetail.getCustomsRate()).getRate();
		Double addedValueRate = taxMap.get(itemDetail.getAddedValueRate()).getRate();
		Double exciseRate = taxMap.get(itemDetail.getExciseRate()).getRate();
		Double taxRate = (new BigDecimal(addedValueRate).add(new BigDecimal(exciseRate))
	             .divide(new BigDecimal(100).subtract(new BigDecimal(exciseRate)),6,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))).doubleValue();
		if(itemPrice>=2000){
			taxRate = add(taxRate,customsRate).doubleValue();
    	}else{
    		taxRate = multiply(taxRate,0.7f).doubleValue();
    	}
		taxRate = BigDecimalUtil.divide(taxRate,100,6).doubleValue();
		return taxRate;
	}
	//计算行邮税
	private Double getPostalTaxRate(ItemDetail itemDetail, Map<Long, TaxRate> taxMap, Double itemPrice){
		Double taxRate = BigDecimalUtil.divide(taxMap.get(itemDetail.getTarrifRate()).getRate(),100,6).doubleValue();
		Double rate = multiply(taxRate,100).doubleValue();
		return rate;
	}
	
	@Override
	public List<PromotionItemMqDto> queryItemByUpdateTime(Date updateTime) {
		List<ItemSku> itemSkus = itemSkuDao.queryByUpdateTime(updateTime);
		if(org.springframework.util.CollectionUtils.isEmpty(itemSkus)) return Collections.EMPTY_LIST;

		Set<Long> itemIds = new HashSet<>();
		for(ItemSku itemSku: itemSkus){
			itemIds.add(itemSku.getItemId());
		}

		List<ItemInfo> itemInfos =itemInfoDao.queryByItemIds(new ArrayList<>(itemIds));

		List<ItemPictures> itemPictures = itemPicturesDao.queryMainPicByItemIds(new ArrayList<>(itemIds));

		List<PromotionItemMqDto> dtos = new ArrayList<>();
		for(ItemSku sku: itemSkus){
			PromotionItemMqDto dto = new PromotionItemMqDto();
			dto.setBasicPrice(sku.getBasicPrice());
			dto.setBrandId(sku.getBrandId());
			dto.setMainTitle(sku.getDetailName());
			dto.setSkuCode(sku.getSku());
			dto.setStatus(sku.getStatus());

			ItemInfo itemInfo = getItemInfo(itemInfos, sku);
			if (itemInfo != null) {
				dto.setLargeCateId(itemInfo.getLargeId());
				dto.setMiddleCateId(itemInfo.getMediumId());
				dto.setSmallCateId(itemInfo.getSmallId());
				dto.setBrandId(itemInfo.getBrandId());
			}

			ItemPictures pic = getPromotionItemMqDtos(itemPictures, sku);

			if(pic != null){
				dto.setMainPic(pic.getPicture());
			}
			dtos.add(dto);
		}
		return dtos;
	}

	private ItemPictures getPromotionItemMqDtos(List<ItemPictures> itemPictures, ItemSku sku) {
		for(ItemPictures pic: itemPictures){
            if(sku.getItemId().equals(pic.getItemId())) return pic;
        }
		return null;
	}

	private ItemInfo getItemInfo(List<ItemInfo> itemInfos, ItemSku sku) {
		for(ItemInfo itemInfo: itemInfos){
            if(itemInfo.getId().equals(sku.getItemId())){
                return itemInfo;
            }
        }
		return null;
	}
}





