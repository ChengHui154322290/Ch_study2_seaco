package com.tp.service.prd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dao.prd.ItemAttributeDao;
import com.tp.dao.prd.ItemDescDao;
import com.tp.dao.prd.ItemDescMobileDao;
import com.tp.dao.prd.ItemDetailDao;
import com.tp.dao.prd.ItemDetailImportDao;
import com.tp.dao.prd.ItemDetailSpecDao;
import com.tp.dao.prd.ItemImportLogDao;
import com.tp.dao.prd.ItemInfoDao;
import com.tp.dao.prd.ItemPicturesDao;
import com.tp.dto.prd.CategoryOpenDto;
import com.tp.dto.prd.DetailOpenDto;
import com.tp.dto.prd.InfoOpenDto;
import com.tp.dto.prd.ItemDetailImportLogDto;
import com.tp.dto.prd.ItemDetailOpenDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.PicturesOpenDto;
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.dto.prd.enums.ItemTypesEnum;
import com.tp.dto.prd.excel.ExcelItemDetailForTransDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.AttributeValue;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.TaxRate;
import com.tp.model.mmp.TopicItem;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailImport;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemImportList;
import com.tp.model.prd.ItemImportLog;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.query.prd.DetailQuery;
import com.tp.service.BaseService;
import com.tp.service.bse.IAttributeService;
import com.tp.service.bse.IAttributeValueService;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.bse.ISpecService;
import com.tp.service.bse.ITaxRateService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.TopicItemService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

@Service
public class ItemDetailService extends BaseService<ItemDetail> implements IItemDetailService {

	@Autowired
	private ItemDetailDao itemDetailDao;
	@Autowired
	private ItemDetailSpecDao itemDetailSpecDao;
	@Autowired
	private ItemAttributeDao itemAttributeDao;
	@Autowired
	private ItemDescDao itemDescDao;
	@Autowired
	private ItemPicturesDao ItemPicturesDao;
	@Autowired
	private ItemDescMobileDao itemDescMobileDao;
	@Autowired
	private ItemInfoDao itemInfoDao;
	@Autowired
	private IAttributeService attributeService;
	@Autowired
	private IAttributeValueService attributeValueService;
	@Autowired
	private ISpecGroupService specGroupService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private ISpecService specService;
	@Autowired
	private ITaxRateService taxRateService;
	@Autowired
	private IDistrictInfoService districtInfoService;
	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	@Autowired
	private IBrandService brandService;
	@Autowired
	private IItemInfoService itemInfoService;
	@Autowired
	private IItemDescService itemDescService;
	@Autowired
	private IItemDescMobileService itemDescMobileService;
	@Autowired
	private IItemSkuService itemSkuSerivce;
	@Autowired
	private IItemPicturesService itemPicturesService;
	@Autowired
	private ItemImportLogDao itemImportLogDao;
	@Autowired
	private ItemDetailImportDao itemDetailImportDao;
	@Autowired
	private ITopicItemService topicItemService;
	
	@Override
	public BaseDao<ItemDetail> getDao() {
		return itemDetailDao;
	}
	
	@Override
	public List<ItemDetail> selectByDetailIds(List<Long> detailIds){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		return itemDetailDao.queryByParam(params);
	}
	@Override
	public ItemDetail selectByIdFromMaster(Long id) {
		return itemDetailDao.queryById(id);
	}


	@Override
	public ItemDetail checkBarcodeFromMaster(String barcode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("barcode", barcode);
		List<ItemDetail> returnList = itemDetailDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(returnList)){
			return returnList.get(0);
		}else{
			return null;
		}
	}

	@Override
	public PageInfo<ItemResultDto> queryPageByQuery(DetailQuery query) {
		List<ItemResultDto> itemList = itemDetailDao.queryPageByQuery(query);
		if(CollectionUtils.isEmpty(itemList)){
			return new PageInfo<ItemResultDto>();
		}
		Integer count = itemDetailDao.queryCountByQuery(query);
		count = (count==null?0:count);
		PageInfo<ItemResultDto> page = new PageInfo<ItemResultDto>();
		page.setRows(itemList);
		page.setPage(query.getStartPage());
		page.setSize(query.getPageSize());
		page.setRecords(count);
		return page;
	}

	@Override
	public ItemDetailOpenDto getItemDetailOpenDtoByDetailId(Long detailId) {
		ItemDetailOpenDto detailSellerDto =new ItemDetailOpenDto();
		InfoOpenDto infoSellerDto = this.getInfoOpenDtoByDetailId(detailId);
		DetailOpenDto SellerDtoByDetail = this.getDetailOpenDtoByDetailId(detailId);
		String descDeatail = this.getDescDeatail(detailId);
		String descMobileDeatail = this.getDescMobileDeatail(detailId);
		List<PicturesOpenDto> picDtoByDetailList= this.getListOfPicDtoByDetailId(detailId);
		detailSellerDto.setInfoOpenDto(infoSellerDto);
		detailSellerDto.setDetailOpenDto(SellerDtoByDetail);
		detailSellerDto.setDescMobileDetail(descMobileDeatail);
		detailSellerDto.setDescDetail(descDeatail);
		detailSellerDto.setListOfPictures(picDtoByDetailList);
		return detailSellerDto;
	}
	
	
	private List<PicturesOpenDto> getListOfPicDtoByDetailId(Long detailId) {
		List<PicturesOpenDto> listOfPicturesDto = new ArrayList<PicturesOpenDto>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		List<ItemPictures> list = ItemPicturesDao.queryByParam(params);
		if (CollectionUtils.isNotEmpty(list)) {
			for (ItemPictures picture : list) {
				PicturesOpenDto target = new PicturesOpenDto();
				BeanUtils.copyProperties(picture, target);
				listOfPicturesDto.add(target);
			}
		}
		return listOfPicturesDto;
	}

	private String getDescMobileDeatail(Long detailId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		List<ItemDescMobile> list = itemDescMobileDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getDescription();
		}	
		return null;		
	}

	private String getDescDeatail(Long detailId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		List<ItemDesc>list = itemDescDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getDescription();
		}
		return null;
	}

	private DetailOpenDto getDetailOpenDtoByDetailId(Long detailId) {
		DetailOpenDto detailSellerDto = new DetailOpenDto();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		List<ItemDetailSpec> listOfDetailSpec = itemDetailSpecDao.queryByParam(params);
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
			detailSellerDto.setSpecGroupAndSpec(specAndGroupResult);
		}

		List<ItemAttribute> resultOfAttribute = itemAttributeDao.queryByParam(params);
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
			detailSellerDto.setCustomeAttr(resultOfCustomeAttr);
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
			detailSellerDto.setBaseAttr(resultOfBaseAttr);
		}

		ItemDetail detailDO = itemDetailDao.queryById(detailId);
		if (detailDO != null) {
		BeanUtils.copyProperties(detailDO, detailSellerDto);
			Integer itemType = detailDO.getItemType();
			String key = ItemTypesEnum.getByValue(itemType).getKey();
			detailSellerDto.setItemTypeName(key);
			Long saleRateId = detailDO.getSaleRate();
			if (null != saleRateId) {
				String saleRate = taxRateService.queryById(saleRateId).getRate().toString();
				detailSellerDto.setSaleRateName(saleRate);
			}
			// 销项税
			Long purRateId = detailDO.getPurRate();
			if (null != purRateId) {
				String purRate = taxRateService.queryById(purRateId).getRate().toString();
				detailSellerDto.setPurRateName(purRate);
			}

			if (detailDO.getWavesSign().equals(ItemConstant.HAI_TAO)) {
				Long tarrifRateId = detailDO.getTarrifRate();
				if (null != tarrifRateId) {
					TaxRate taxRateDO = taxRateService.queryById(tarrifRateId);
					String remark=taxRateDO.getRemark();
				    String rate =((Integer)taxRateDO.getRate().intValue()).toString();
					detailSellerDto.setTarrifRateName(rate+"%-"+remark);
				}
				Long countryId = detailDO.getCountryId();
				if (null != countryId && detailDO.getCountryId().intValue() != 0) {
					String countryName = districtInfoService.queryById(countryId).getName();
					detailSellerDto.setCountryName(countryName);
				}
			}

			if ((null != detailDO.getApplyAgeId()) && (detailDO.getApplyAgeId().intValue() != 0)) {
				String applyAgeName = dictionaryInfoService.queryById(detailDO.getApplyAgeId()).getName();
				detailSellerDto.setApplyAgeName(applyAgeName);
			}
		}
		return detailSellerDto;
	}

	/**
	 * 
	 * <pre>
	 *  根据detailId 返回对应的InfoOpenDto
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @throws ItemServiceException
	 */
	private InfoOpenDto getInfoOpenDtoByDetailId(Long detailId){		
		    InfoOpenDto infoSellerDto = new InfoOpenDto();
			ItemDetail  detailDO= itemDetailDao.queryById(detailId);
			if (detailDO != null) {
				String spu = detailDO.getSpu();
				Long itemId = detailDO.getItemId();
				ItemInfo resultInfo = itemInfoDao.queryById(itemId);
				if (resultInfo !=null) {
					Brand brand = brandService.queryById(resultInfo.getBrandId());
					if(null != brand){
						infoSellerDto.setBarandId(resultInfo.getBrandId());
						infoSellerDto.setBrandName(brand.getName());	
					}			
					infoSellerDto.setSpu(spu);
					infoSellerDto.setSpuName(resultInfo.getMainTitle());
					
					DictionaryInfo dicInfoResult = dictionaryInfoService.queryById(resultInfo.getUnitId());
					if(null != dicInfoResult){
						infoSellerDto.setUnitId(resultInfo.getUnitId());
						infoSellerDto.setUnitName(dicInfoResult.getName());
					}
					
					infoSellerDto.setRemark(resultInfo.getRemark());
					List<Long> cateIdList = new ArrayList<Long>();
					cateIdList.add(resultInfo.getLargeId());
					cateIdList.add(resultInfo.getMediumId());
					cateIdList.add(resultInfo.getSmallId());
					if(CollectionUtils.isNotEmpty(cateIdList)){
						List<Category> listOfCate = categoryService.selectByIdsAndStatus(cateIdList, 2);
						if(CollectionUtils.isNotEmpty(listOfCate) && listOfCate.size()==3){
							CategoryOpenDto categoryOpenDto = new CategoryOpenDto();
							categoryOpenDto.setBigCateName(listOfCate.get(0).getName());
							categoryOpenDto.setMiddCateName(listOfCate.get(1).getName());
							categoryOpenDto.setSmallCateName(listOfCate.get(2).getName());
							infoSellerDto.setCategoryOpenDto(categoryOpenDto);
						}
						
					}
					return infoSellerDto;
				}
			}
			return infoSellerDto;	
	}
	
	/**
	 * 导入数据拼接，封装，保存
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Override
	@Transactional
	public void saveItemDetailForTrans(List<ExcelItemDetailForTransDto> excelList){
		
		List<Long> detailIdList = new ArrayList<Long>();
		for(ExcelItemDetailForTransDto itemDetailForTransDto:excelList){
			Long detailId = itemDetailForTransDto.getDetailId();//
			detailIdList.add(detailId);
//			itemDetailForTransDto.getSku();//
//			itemDetailForTransDto.getBrandName();//品牌名称
//			itemDetailForTransDto.getBrandStory();//品牌故事
//			itemDetailForTransDto.getItemTitle();//商品名称
//			itemDetailForTransDto.getDetailDesc();//商品详情描述
//			itemDetailForTransDto.getMaterial();//材质
//			itemDetailForTransDto.getSpecialInstructions();//特殊说明
		}
		List<ItemDetail> itemDetailList = selectByDetailIds(detailIdList);//根据detailId集合查itemDetail对象集合
		List<Long> itemIds = new ArrayList<Long>();
		for(ItemDetail itemDetail:itemDetailList){
			 Long itemId = itemDetail.getItemId();
			 itemIds.add(itemId);
			 for(ExcelItemDetailForTransDto itemDetailForTransDto :excelList){
				 if(itemDetail.getId().equals(itemDetailForTransDto.getDetailId())){
					 itemDetail.setItemTitle(itemDetailForTransDto.getItemTitle());//商品名称
					 itemDetail.setMainTitle(itemDetailForTransDto.getItemTitle());//产品前台展示名称
					 itemDetail.setStorageTitle(itemDetailForTransDto.getItemTitle());//存储在仓库中的名称
					 itemDetail.setSubTitle(itemDetailForTransDto.getItemTitle());//卖点描述
					 itemDetail.setSpuName(itemDetailForTransDto.getItemTitle());
					 updateById(itemDetail);//更新itemDetail
				 }
			 }
		}
		
		Map<String,Object> itemParam = new HashMap<String,Object>();
		itemParam.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(itemIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemInfo> itemInfoList = itemInfoService.queryByParam(itemParam);//根据itemId集合 获取itemInfo对象集合
//		List<ItemInfo> itemInfoListNew = new ArrayList<ItemInfo>();
		for(ItemDetail itemDetail:itemDetailList){
			for(ItemInfo itemInfo :itemInfoList){
				if(itemInfo.getId().equals(itemDetail.getItemId())){
					itemInfo.setMainTitle(itemDetail.getMainTitle());//spu名称
					itemInfoService.updateById(itemInfo);
				}
			}
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+StringUtil.join(detailIdList, Constant.SPLIT_SIGN.COMMA)+")");
		List<ItemSku> itemSkuList = itemSkuSerivce.queryByParam(params);
		for(ItemDetail itemDetail:itemDetailList){
			for(ItemSku itemSku :itemSkuList){
				if(itemSku.getDetailId().equals(itemDetail.getId()) && itemSku.getItemId().equals(itemDetail.getItemId())){
					itemSku.setDetailName(itemDetail.getItemTitle());//sku表中的商品名称
					itemSku.setSpuName(itemDetail.getItemTitle());
					itemSkuSerivce.updateById(itemSku);
				}
			}
		}
		
		List<ItemDesc> itemDescList = itemDescService.queryByParam(params);//
		ItemDescMobile itemDescMobile = null;
		for(ExcelItemDetailForTransDto itemDetailForTransDto:excelList){
			for(ItemDesc itemDesc: itemDescList){
				try{
					if(itemDesc.getDetailId().equals(itemDetailForTransDto.getDetailId())){
						String brandName = itemDetailForTransDto.getBrandName();
						String itemTitle = itemDetailForTransDto.getItemTitle();//商品名称
						String brandStory = itemDetailForTransDto.getBrandStory();//品牌故事
						String detailDesc = itemDetailForTransDto.getItemDetailDesc();//商品详情描述
						String material = itemDetailForTransDto.getMaterial();//材质
						String specialInstructions = itemDetailForTransDto.getSpecialInstructions();//特殊说明
						Map<String, Object> para = new HashMap<String,Object>();
						para.put("item_id", itemDesc.getItemId());
						para.put("detail_id", itemDesc.getDetailId());
						ItemSku itemSku = itemSkuSerivce.queryUniqueByParams(para);
						String articleCode = itemSku.getArticleCode();//原厂货号
						String skuCode = itemSku.getSku();
						Map<String, Object> param = new HashMap<String,Object>();
						param.put("sku", skuCode);
						List<TopicItem> TopicItemList = topicItemService.queryByParam(param);
						if(null!=TopicItemList&& TopicItemList.size()>0){
							for(TopicItem topicItem:TopicItemList){
								topicItem.setName(itemTitle);
								topicItemService.updateById(topicItem);
							}
						}
						List<ItemPictures> picList = itemPicturesService.queryByParam(para); 
						String picUrl = "";
						for(ItemPictures itemPictures:picList){
							picUrl += "<div><img src=\"http://item.qn.seagoor.com/"+itemPictures.getPicture()+"\" width=\"100%\" title=\""+itemPictures.getPicture()+"\" alt=\""+itemPictures.getPicture()+"\" /> </div> ";
						}
						/** 拼商品详情描述 **/
/*						String decs =   "<div>"
											+"<div style=\"height: 17px; line-height: 17px; font-size: 13px; background-image: url(img/item/title.png); background-position: center;background-repeat: no-repeat; background-size: 100%; text-align: center; font-weight: bolder;\">商品信息</div>"
											+"<div style=\"background: #fff; padding-top: 15px; padding-bottom: 20px\">"
												+"<div style=\"margin-left: 25px; margin-bottom: 10px; font-size: 12px; line-height: 20px;\">"
													+"<div style=\"line-height: 20px;float: left;font-weight: 600;\">"
													+"<div>商品名称</div>"
													+"<div>原厂货号</div>"
													+"<div>商品材质</div>"
													+"<div>特殊说明</div>"					
												+"</div>"
												+"<div style=\"line-height: 20px;border-left: 1px solid #ccc;margin-left: 70px;padding-left: 20px;\">"
													+"<div>"+itemTitle+"</div>"
													+"<div>"+articleCode+"</div>"
													+"<div>"+material+"</div>"
													+"<div>"+specialInstructions+"</div>	"				
												+"</div>"
											+"</div>"
											+"<div style=\"padding: 10px;font-size: 11px;color: #666;border: 1px solid #ccc;width: 315px;margin: 0 auto; text-align: justify;\"><span style=\"font-weight: 600; \">温馨提示：</span>详情里的商品参数、功能及附件仅供参考，请以收到实物为准。因显示器不同图片与实物可能有细微色差，不属于质量问题，因测量方式不同，若有少许误差，皆属于合理范围。</div>"
											+"</div>"
										+"</div>"
										+"<div>"
											+"<div style=\"height: 17px; line-height: 17px; font-size: 13px; background-image: url(img/item/title.png); background-position: center;background-repeat: no-repeat; background-size: 100%; text-align: center; font-weight: bolder;\">品牌故事</div>"
											+"<div style=\"padding: 10px 20px 20px 20px;font-size: 12px;color: #666; text-align: justify; background-color: #fff; line-height: 20px;\">"
												+"<div>"+brandStory+"</div>"
											+"</div>"
										+"</div>"
										+"<div>"
											+"<div style=\"height: 17px; line-height: 17px; font-size: 13px; background-image: url(img/item/title.png); background-position: center;background-repeat: no-repeat; background-size: 100%; text-align: center; font-weight: bolder;\">详情描述</div>"
											+"<div style=\"padding: 10px 20px 20px 20px;font-size: 12px;color: #666; text-align: justify; background-color: #fff; line-height: 20px;\">"
												+"<div>"+detailDesc+"</div>"
											+"</div>"
										+"</div>"
										+picUrl ;
						*/
						
						String tableStr = "<table style=\"margin-left:25px;margin-bottom:10px;font-size:12px;line-height:20px;\">";
						if(null!=itemTitle && !"".equals(itemTitle)){
							tableStr += "<tr >"
											+"<td style=\"line-height:20px;float:left;font-weight:600;width: 100px;text-align: center;\">商品名称</td>"
											+"<td style=\"line-height:20px;border-left:1px solid #ccc;margin-left:70px;padding-left:20px;text-align: left;\">"+itemTitle+"</td>"
										+"</tr>";
						}
						if(null!=articleCode && !"".equals(articleCode)){
							tableStr += "<tr >"
									+"<td style=\"line-height:20px;float:left;font-weight:600;width: 100px;text-align: center;\">原厂货号</td>"
									+"<td style=\"line-height:20px;border-left:1px solid #ccc;margin-left:70px;padding-left:20px;text-align: left;\">"+articleCode+"</td>"
								+"</tr>";
						}
						if(null!=material && !"".equals(material)){
							tableStr += "<tr >	"
									+"<td style=\"line-height:20px;float:left;font-weight:600;width: 100px;text-align: center;\">商品材质</td>"
									+"<td style=\"line-height:20px;border-left:1px solid #ccc;margin-left:70px;padding-left:20px;text-align: left;\">"+material+"</td>"
								+"</tr>";
						}
						if(null!=specialInstructions && !"".equals(specialInstructions)){
							tableStr += "<tr >"
									+"<td style=\"line-height:20px;float:left;font-weight:600;width: 100px;text-align: center;\">特殊说明</td>"
									+"<td style=\"line-height:20px;border-left:1px solid #ccc;margin-left:70px;padding-left:20px;text-align: left;\">"+specialInstructions+"</td>"
								+"</tr>";
						}
						tableStr += "</table>";
											
						String decs =   "<div>"
								+"<div style=\"height: 17px; line-height: 17px; font-size: 13px; background-image: url(img/item/title.png); background-position: center;background-repeat: no-repeat; background-size: 100%; text-align: center; font-weight: bolder;\">商品信息</div>"
								+"<div style=\"background: #fff; padding-top: 15px; padding-bottom: 20px\">"
								+tableStr
								+"<div style=\"padding: 10px;font-size: 11px;color: #666;border: 1px solid #ccc;width: 315px;margin: 0 auto; text-align: justify;\"><span style=\"font-weight: 600; \">温馨提示：</span>详情里的商品参数、功能及附件仅供参考，请以收到实物为准。因显示器不同图片与实物可能有细微色差，不属于质量问题，因测量方式不同，若有少许误差，皆属于合理范围。</div>"
								+"</div>"
							+"</div>"
							+"<div>"
								+"<div style=\"height: 17px; line-height: 17px; font-size: 13px; background-image: url(img/item/title.png); background-position: center;background-repeat: no-repeat; background-size: 100%; text-align: center; font-weight: bolder;\">品牌故事-"+brandName+"</div>"
								+"<div style=\"padding: 10px 20px 20px 20px;font-size: 12px;color: #666; text-align: justify; background-color: #fff; line-height: 20px;\">"
									+"<div>"+brandStory+"</div>"
								+"</div>"
							+"</div>"
							+"<div>"
								+"<div style=\"height: 17px; line-height: 17px; font-size: 13px; background-image: url(img/item/title.png); background-position: center;background-repeat: no-repeat; background-size: 100%; text-align: center; font-weight: bolder;\">详情描述</div>"
								+"<div style=\"padding: 10px 20px 20px 20px;font-size: 12px;color: #666; text-align: justify; background-color: #fff; line-height: 20px;\">"
									+"<div>"+detailDesc+"</div>"
								+"</div>"
							+"</div>"
							+picUrl ;
						
						itemDesc.setDescription(decs);
						itemDescService.updateById(itemDesc);
						itemDescMobile = new ItemDescMobile();
						BeanUtil.copyProperties(itemDescMobile, itemDesc);
						itemDescMobileService.updateById(itemDescMobile);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ItemDetailImportLogDto saveImportLogDto(ItemDetailImportLogDto itemDetailImportLogDto) {
		ItemImportLog itemImportLog = itemDetailImportLogDto.getItemImportLog();
		String createUser = itemImportLog.getCreateUser();
		//逻辑删除以前的
		ItemImportLog deleteLog = new ItemImportLog ();
		deleteLog.setDeleteSign(0);
		deleteLog.setCreateUser(createUser);
		itemImportLogDao.deleteByUserId(deleteLog);
		itemImportLog.setSuccessCount(0);
		itemImportLog.setFailCount(0);
		itemImportLogDao.insert(itemImportLog);
		//批量batch明细的数据
		if(CollectionUtils.isNotEmpty(itemDetailImportLogDto.getImportList())){
			for(ItemDetailImport itemDetailImport : itemDetailImportLogDto.getImportList()){
				itemDetailImport.setLogId(itemImportLog.getId());
				itemDetailImportDao.insert(itemDetailImport);
			}
		}
		return itemDetailImportLogDto;
	}
	
}
