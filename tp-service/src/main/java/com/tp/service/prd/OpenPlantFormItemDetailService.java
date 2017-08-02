package com.tp.service.prd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dao.prd.ItemAttributeDao;
import com.tp.dao.prd.ItemDescDao;
import com.tp.dao.prd.ItemDescMobileDao;
import com.tp.dao.prd.ItemDetailDao;
import com.tp.dao.prd.ItemDetailSpecDao;
import com.tp.dao.prd.ItemInfoDao;
import com.tp.dao.prd.ItemPicturesDao;
import com.tp.dao.prd.ItemSkuDao;
import com.tp.dao.prd.SkuOpenPlantFormDao;
import com.tp.datasource.ContextHolder;
import com.tp.dto.prd.CategoryOpenDto;
import com.tp.dto.prd.DetailOpenDto;
import com.tp.dto.prd.InfoOpenDto;
import com.tp.dto.prd.ItemDetailOpenDto;
import com.tp.dto.prd.PicturesOpenDto;
import com.tp.dto.prd.SimpleDetailOpenDto;
import com.tp.dto.prd.SkuDto;
import com.tp.dto.prd.SpuInfoOpenDto;
import com.tp.dto.prd.enums.ItemTypesEnum;
import com.tp.model.bse.AttributeValue;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.TaxRate;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.service.bse.IAttributeService;
import com.tp.service.bse.IAttributeValueService;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.bse.ISpecService;
import com.tp.service.bse.ITaxRateService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.service.prd.IOpenPlantFormItemDetailService;

/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author szy
 */
@Service
public class OpenPlantFormItemDetailService implements IOpenPlantFormItemDetailService {
	private final static Logger LOGGER = LoggerFactory.getLogger(OpenPlantFormItemDetailService.class);
	
	@Autowired
	private ItemInfoDao itemInfoDao;
	@Autowired
	private ItemDetailDao itemDetailDao;
	@Autowired
	private ItemPicturesDao itemPicturesDao;
    @Autowired
    private ItemDetailSpecDao itemDetailSpecDao;
    @Autowired
    private ItemAttributeDao itemAttributeDao;
    @Autowired
    private ItemDescDao itemDescDao;
    @Autowired
    private ItemDescMobileDao itemDescMobileDao;
	@Autowired
	private SkuOpenPlantFormDao skuOpenPlantFormDao;
	@Autowired
	private ItemSkuDao itemSkuDao;
	
	@Autowired
	private ISpecGroupService specGroupService;
	@Autowired
	private ISpecService specService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IBrandService  brandService;
	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	
    @Autowired
    private IAttributeService attributeService;
    @Autowired
    private IAttributeValueService attributeValueService;
    @Autowired
    private  ITaxRateService taxRateService;
    @Autowired
    private IDistrictInfoService districtInfoService;
	@Autowired
	private  IFreightTemplateService freightTemplateService;
	
	
	

	

	@Override
	public ItemDetailOpenDto getItemDetailOpenDtoByDetailId(Long detailId)  {
		ItemDetailOpenDto detailOpenDto =new ItemDetailOpenDto();
		InfoOpenDto infoOpenDto = this.getInfoOpenDtoByDetailId(detailId);
		DetailOpenDto openDtoByDetail = this.getDetailOpenDtoByDetailId(detailId);
		String descDeatail = this.getDescDeatail(detailId);
		String descMobileDeatail = this.getDescMobileDeatail(detailId);
		List<PicturesOpenDto> picDtoByDetailList= this.getListOfPicDtoByDetailId(detailId);
		detailOpenDto.setInfoOpenDto(infoOpenDto);
		detailOpenDto.setDetailOpenDto(openDtoByDetail);
		detailOpenDto.setDescMobileDetail(descMobileDeatail);
		detailOpenDto.setDescDetail(descDeatail);
		detailOpenDto.setListOfPictures(picDtoByDetailList);
		return detailOpenDto;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 根据detailId 返回对应的的图片list
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	private List<PicturesOpenDto> getListOfPicDtoByDetailId(Long detailId)  {
		List<PicturesOpenDto> listOfPicturesDto = new ArrayList<PicturesOpenDto>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		List<ItemPictures> list = new ArrayList<ItemPictures>();
		list = itemPicturesDao.queryByParam(params);
		if (CollectionUtils.isNotEmpty(list)) {
			for (ItemPictures picture : list) {
				PicturesOpenDto target = new PicturesOpenDto();
				BeanUtils.copyProperties(picture, target);
				listOfPicturesDto.add(target);
			}
		}
		return listOfPicturesDto;
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 根据detailId 返回对应德DetailOpenDto
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	private DetailOpenDto getDetailOpenDtoByDetailId(Long detailId)   {
		DetailOpenDto detailOpenDto = new DetailOpenDto();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		List<ItemDetailSpec> listOfDetailSpec = new ArrayList<ItemDetailSpec>();
		listOfDetailSpec = itemDetailSpecDao.queryByParam(params);
		if (CollectionUtils.isNotEmpty(listOfDetailSpec)) {
			HashMap<String, String> specAndGroupResult = new HashMap<String, String>();
			for (ItemDetailSpec detailSpecDO2 : listOfDetailSpec) {
				if (detailSpecDO2.getSpecId().intValue() == -1|| detailSpecDO2.getSpecGroupId().intValue() == -1) {
					break;
				}
				String groupName = specGroupService.queryById(detailSpecDO2.getSpecGroupId()).getName();
				String specName = specService.queryById(detailSpecDO2.getSpecId()).getSpec();
				specAndGroupResult.put(groupName, specName);
			}
			detailOpenDto.setSpecGroupAndSpec(specAndGroupResult);
		}
		List<ItemAttribute> resultOfAttribute = new ArrayList<ItemAttribute>();
		resultOfAttribute = itemAttributeDao.queryByParam(params);
		if (CollectionUtils.isNotEmpty(resultOfAttribute)) {
			HashMap<Long, List<Long>> resultOfLong = new HashMap<Long, List<Long>>();
			HashMap<String, String> resultOfCustomeAttr = new HashMap<String, String>();
			HashMap<String, List<String>> resultOfBaseAttr = new HashMap<String, List<String>>();
			for (ItemAttribute attribute : resultOfAttribute) {
				if (attribute.getCustom().equals(ItemConstant.ATTR_FROM_BASEDATA)) {
					Long attrId = attribute.getAttrId();
					List<Long> list = resultOfLong.get(attrId);
					if (CollectionUtils.isEmpty(list)) {
						list = new ArrayList<Long>();
					}
					Long attrValueId = attribute.getAttrValueId();
					list.add(attrValueId);
					resultOfLong.put(attrId, list);
				} else {
					resultOfCustomeAttr.put(attribute.getAttrKey(),attribute.getAttrValue());
				}
			}
			detailOpenDto.setCustomeAttr(resultOfCustomeAttr);
			Iterator<Entry<Long, List<Long>>> iterator = resultOfLong.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Long, List<Long>> next = iterator.next();
				String name = attributeService.queryById(next.getKey()).getName();
				List<Long> valueOfCateValue = next.getValue();
				if (CollectionUtils.isNotEmpty(valueOfCateValue)) {
					List<AttributeValue> resultOfAttributeValue = attributeValueService.queryByIdsAndStatus(valueOfCateValue, 2);
					if (CollectionUtils.isNotEmpty(resultOfAttributeValue)) {
						List<String> cateValue = new ArrayList<String>();
						for (AttributeValue attributeValueDO : resultOfAttributeValue) {
							cateValue.add(attributeValueDO.getName());
						}
						resultOfBaseAttr.put(name, cateValue);
					}
				}
			}
			detailOpenDto.setBaseAttr(resultOfBaseAttr);
		}

		ItemDetail detail = new ItemDetail();
		detail = itemDetailDao.queryById(detailId);
		if (null == detail) {
			params.put("id", detailId);
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			detail = itemDetailDao.queryByParam(params).get(0);
		}
		if (detail != null) {
		BeanUtils.copyProperties(detail, detailOpenDto);
			Integer itemType = detail.getItemType();
			String key = ItemTypesEnum.getByValue(itemType).getKey();
			detailOpenDto.setItemTypeName(key);
			Long saleRateId = detail.getSaleRate();
			if (null != saleRateId) {
				String saleRate = taxRateService.queryById(saleRateId).getRate().toString();
				detailOpenDto.setSaleRateName(saleRate);
			}
			// 销项税
			Long purRateId = detail.getPurRate();
			if (null != purRateId) {
				String purRate = taxRateService.queryById(purRateId).getRate().toString();
				detailOpenDto.setPurRateName(purRate);
			}
            if(null !=detail.getFreightTemplateId()){
            	FreightTemplate freightTemplate = freightTemplateService.queryById(detail.getFreightTemplateId());
            	if(null != freightTemplate){
            		detailOpenDto.setFreightTemplateName(freightTemplate.getName());
            	}
            }
			
			if (detail.getWavesSign().equals(ItemConstant.HAI_TAO)) {
				Long tarrifRateId = detail.getTarrifRate();
				if (null != tarrifRateId) {
					TaxRate taxRateDO = taxRateService.queryById(tarrifRateId);
					String remark=taxRateDO.getRemark();
				    String rate =((Integer)taxRateDO.getRate().intValue()).toString();
					detailOpenDto.setTarrifRateName(rate+"%-"+remark);
				}
				Long countryId = detail.getCountryId();
				if (null != countryId && detail.getCountryId().intValue() != 0) {
					String countryName = districtInfoService.queryById(countryId).getName();
					detailOpenDto.setCountryName(countryName);
				}
			}

			if ((null != detail.getApplyAgeId()) && (detail.getApplyAgeId().intValue() != 0)) {
				String applyAgeName = dictionaryInfoService.queryById(detail.getApplyAgeId()).getName();
				detailOpenDto.setApplyAgeName(applyAgeName);
			}
		}
		return detailOpenDto;
	}
	
	/**
	 * 
	 * <pre>
	 * 根据detailId 返回客户端的描述
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	private String getDescDeatail(Long detailId)  {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemDesc>list = itemDescDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getDescription();
		}
		return null;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 根据detailId 返回手机端的描述
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	private String getDescMobileDeatail(Long detailId)  {
		ItemDescMobile descMobileDO =  new ItemDescMobile();
		descMobileDO.setDetailId(detailId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		List<ItemDescMobile> list = itemDescMobileDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getDescription();
		}	
		return null;		
	}
	
	
	/**
	 * 
	 * <pre>
	 *  根据detailId 返回对应的InfoOpenDto
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	private InfoOpenDto getInfoOpenDtoByDetailId(Long detailId) {
		InfoOpenDto infoOpenDto = new InfoOpenDto();
			ItemDetail detail = itemDetailDao.queryById(detailId);
			if (null == detail) {
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("id", detailId);
				params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
				detail = itemDetailDao.queryByParam(params).get(0);
			}
			if (detail != null) {
				String spu = detail.getSpu();
				Long itemId = detail.getItemId();
				ItemInfo resultInfo = itemInfoDao.queryById(itemId);
				if (resultInfo !=null) {
					Brand brand = brandService.queryById(resultInfo.getBrandId());
					infoOpenDto.setBarandId(resultInfo.getBrandId());
					infoOpenDto.setBrandName(brand.getName());
					infoOpenDto.setSpu(spu);
					infoOpenDto.setSpuName(resultInfo.getMainTitle());
					
					DictionaryInfo dicInfoResult = dictionaryInfoService.queryById(resultInfo.getUnitId());
					infoOpenDto.setUnitId(resultInfo.getUnitId());
					infoOpenDto.setUnitName(dicInfoResult.getName());
					infoOpenDto.setRemark(resultInfo.getRemark());
					List<Long> cateIdList = new ArrayList<Long>();
					cateIdList.add(resultInfo.getLargeId());
					cateIdList.add(resultInfo.getMediumId());
					cateIdList.add(resultInfo.getSmallId());	
					if(CollectionUtils.isNotEmpty(cateIdList)){
						List<Category> listOfCate = categoryService.queryCategoryByParams(cateIdList, null);
						if(CollectionUtils.isNotEmpty(listOfCate) && listOfCate.size()==3){
							CategoryOpenDto categoryOpenDto = new CategoryOpenDto();
							categoryOpenDto.setBigCateName(listOfCate.get(0).getName());
							categoryOpenDto.setMiddCateName(listOfCate.get(1).getName());
							categoryOpenDto.setSmallCateName(listOfCate.get(2).getName());
							infoOpenDto.setCategoryOpenDto(categoryOpenDto);
						}	
					}	
					return infoOpenDto;
				}
			}
			return infoOpenDto;	
	}


	
	/***
	 * 获取spu名称 商家平台
	 */
	@Override
	public List<InfoOpenDto> querySpuByNameForSeller(String spuName)
			 {
		
		List<InfoOpenDto>  retrunList = skuOpenPlantFormDao.querySpuByNameForSeller(spuName);
		
		return retrunList;
	}

	
	@Override
	public ItemDetailOpenDto getSpuInfoBySpuCode(String spu)  {
		 	ItemDetailOpenDto returnDto = new ItemDetailOpenDto();
		 	InfoOpenDto infoOpenDto = new InfoOpenDto();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("spu", spu);
			List<ItemInfo> list = itemInfoDao.queryByParam(params);
			if (CollectionUtils.isNotEmpty(list)) {
				ItemInfo resultInfo = list.get(0);
				Brand brand = brandService.queryById(resultInfo.getBrandId());
				infoOpenDto.setBarandId(resultInfo.getBrandId());
				infoOpenDto.setBrandName(brand.getName());
				infoOpenDto.setSpu(spu);
				infoOpenDto.setSpuName(resultInfo.getMainTitle());
				
				DictionaryInfo dicInfoResult = dictionaryInfoService.queryById(resultInfo.getUnitId());
				infoOpenDto.setUnitId(resultInfo.getUnitId());
				infoOpenDto.setUnitName(dicInfoResult.getName());
				infoOpenDto.setRemark(resultInfo.getRemark());
				List<Long> cateIdList = new ArrayList<Long>();
				cateIdList.add(resultInfo.getLargeId());
				cateIdList.add(resultInfo.getMediumId());
				cateIdList.add(resultInfo.getSmallId());				
				List<Category> listOfCate = categoryService.queryCategoryByParams(cateIdList, null);
				CategoryOpenDto categoryOpenDto = new CategoryOpenDto();
				categoryOpenDto.setBigCateName(listOfCate.get(0).getName());
				categoryOpenDto.setMiddCateName(listOfCate.get(1).getName());
				categoryOpenDto.setSmallCateName(listOfCate.get(2).getName());
				categoryOpenDto.setSmallId(resultInfo.getSmallId());
				infoOpenDto.setCategoryOpenDto(categoryOpenDto);
				returnDto.setInfoOpenDto(infoOpenDto);
			}
		return returnDto;	
	}


	@Override
	public SpuInfoOpenDto getSpuInfoOpenDtoBySpuCode(String spuCode) {
		if(StringUtils.isBlank(spuCode)){
			return null;
		}
		SpuInfoOpenDto spuInfoOpenDto=new SpuInfoOpenDto();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("spu", spuCode);
		List<ItemDetail> list = itemDetailDao.queryByParam(params);
		if(CollectionUtils.isEmpty(list)){
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			list = itemDetailDao.queryByParam(params);
		}
		if(CollectionUtils.isNotEmpty(list)){	
			List<SimpleDetailOpenDto> result =new ArrayList<SimpleDetailOpenDto>();
			HashMap<String, String> groupInfo=new HashMap<String,String>();
			for (ItemDetail detailDO : list) {
				SimpleDetailOpenDto simpleDetailOpenDto=new SimpleDetailOpenDto();
			    BeanUtils.copyProperties(detailDO, simpleDetailOpenDto);
				ItemDetailSpec detailSpec = new ItemDetailSpec();
				detailSpec.setDetailId(detailDO.getId());
				List<ItemDetailSpec> listOfDetailSpec = new ArrayList<ItemDetailSpec>();
				listOfDetailSpec = itemDetailSpecDao.queryByObject(detailSpec);
				
				HashMap<String, Map<String, String>> mapSpecInfo=new HashMap<String, Map<String,String>>();
				for (ItemDetailSpec detailSpecDO2 : listOfDetailSpec) {
					if (detailSpecDO2.getSpecId().intValue() == -1|| detailSpecDO2.getSpecGroupId().intValue() == -1) {
						break;
					}	
					HashMap<String, String> specInfo=new HashMap<String,String>();
					String groupName = specGroupService.queryById(detailSpecDO2.getSpecGroupId()).getName();
					String specName = specService.queryById(detailSpecDO2.getSpecId()).getSpec();
					groupInfo.put(detailSpecDO2.getSpecGroupId().toString(),groupName);
					specInfo.put(detailSpecDO2.getSpecId().toString(),specName);
					mapSpecInfo.put(detailSpecDO2.getSpecGroupId().toString(),specInfo);		
				}
				simpleDetailOpenDto.setSpecInfo(mapSpecInfo);
				result.add(simpleDetailOpenDto);
			}	
			spuInfoOpenDto.setSpecGroupInfo(groupInfo);
			spuInfoOpenDto.setListOfSimpleDetailOpenDto(result);
		}
		return spuInfoOpenDto;
	}


	@Override
	public List<String> checkSellerBarcodeList(List<String> barcode) {
		return  skuOpenPlantFormDao.checkSellerBarcodeList(barcode, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
	}
	
	
	@Override
	public ItemDetailOpenDto getItemDetailOpenDtoBySkuId(Long skuId)
			 {
		ItemDetailOpenDto detailOpenDto =new ItemDetailOpenDto();
		Long detailId=0l;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", skuId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		ItemSku skuInfo = itemSkuDao.queryByParam(params).get(0);
		detailId = skuInfo.getDetailId();
		SkuDto setDto = new SkuDto();
		setDto.setMainTitle(skuInfo.getDetailName());
		setDto.setSku(skuInfo.getSku());
		setDto.setBasicPrice(skuInfo.getBasicPrice());
		detailOpenDto.setSkuDto(setDto);
		InfoOpenDto infoOpenDto = this.getInfoOpenDtoByDetailId(detailId);
		DetailOpenDto openDtoByDetail = this.getDetailOpenDtoByDetailId(detailId);
		String descDeatail = this.getDescDeatail(detailId);
		String descMobileDeatail = this.getDescMobileDeatail(detailId);
		List<PicturesOpenDto> picDtoByDetailList= this.getListOfPicDtoByDetailId(detailId);
		detailOpenDto.setInfoOpenDto(infoOpenDto);
		detailOpenDto.setDetailOpenDto(openDtoByDetail);
		detailOpenDto.setDescMobileDetail(descMobileDeatail);
		detailOpenDto.setDescDetail(descDeatail);
		detailOpenDto.setListOfPictures(picDtoByDetailList);
		return detailOpenDto;
	}
}
