package com.tp.service.prd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.common.vo.prd.ItemRedisConstant;
import com.tp.dao.prd.ItemDetailDao;
import com.tp.dao.prd.ItemDetailSalesCountDao;
import com.tp.dao.prd.ItemInfoDao;
import com.tp.dao.prd.ItemSkuDao;
import com.tp.dto.prd.DetailMainInfoDto;
import com.tp.dto.prd.ItemDetailSalesCountDto;
import com.tp.dto.prd.SkuDetailInfoDto;
import com.tp.dto.prd.SkuDto;
import com.tp.exception.ItemServiceException;
import com.tp.exception.ServiceException;
import com.tp.model.app.PushInfo;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSalesCount;
import com.tp.model.prd.ItemSku;
import com.tp.model.usr.UserInfo;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.bse.BrandService;
import com.tp.service.bse.SpecService;
import com.tp.service.mmp.FreightTemplateService;
import com.tp.service.prd.IItemDetailSalesCountService;
import com.tp.service.prd.IItemRemoteService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

@Service
public class ItemRemoteService implements IItemRemoteService {

	
	private final static Logger LOGGER = LoggerFactory.getLogger(ItemRemoteService.class);
	
	
	@Autowired
	private ItemDetailSalesCountDao itemDetailSalesCountDao;
	@Autowired
	private IItemDetailSalesCountService itemDetailSalesCountService;
	
	@Autowired
	private ItemSkuDao itemSkuDao;
	
	@Autowired
	private ItemDetailDao itemDetailDao;
	
	@Autowired
	JedisCacheUtil jedisCacheUtil;
	
	
//	@Value("${item_defalut_sales_count}")
//	private Long itemSalesDefaultCount = 12L;
	
	/***
	 * 商品更新实际销售数量
	 */
	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
	public int updateItemSalesCount(Map<String, Integer> skuCountMap) throws ItemServiceException {
		if(!skuCountMap.isEmpty()){
			 List<String> listSkus = new ArrayList<>();
			 for (Entry<String,Integer> entry : skuCountMap.entrySet()) {
				 	listSkus.add( entry.getKey());
			 }
			 if(CollectionUtils.isNotEmpty(listSkus)){
				 //查询对应detailId
				 try {
					Map<Long,Integer> updateMap = new HashMap<>();
					List<SkuDetailInfoDto> listDetails = itemSkuDao.selectSkuDetailInfoByListSkus(listSkus);
					 //合并相同details
					mergeDetailIdSalesCount(skuCountMap, updateMap, listDetails);
					//批量update 销售数量s
					createOrUpdateDetailSalesCount(updateMap);
					
				 } catch (Exception e) {
					 e.printStackTrace();
					 LOGGER.error("updateItemSalesCount  selectSkuDetailInfoByListSkus exception: ", e);
				}
			 }
		}else{
			throw new ItemServiceException("params is empty when invoking itemservice.updateItemSalesCount");
		}
		return 0;
	}

	
	
	
	/***
	 * 合并相同detail的下单量
	 * @param skuCountMap
	 * @param updateMap
	 * @param listDetails
	 */
	private void mergeDetailIdSalesCount(Map<String, Integer> skuCountMap,
			Map<Long, Integer> updateMap, List<SkuDetailInfoDto> listDetails) {
		if(CollectionUtils.isNotEmpty(listDetails)){
			//合并detailids
			for(SkuDetailInfoDto  dto:listDetails){
				if(skuCountMap.containsKey(dto.getSku())){
					if(skuCountMap.get(dto.getSku()) != null){
						dto.setRelSalesCount(skuCountMap.get(dto.getSku()));
					}
					if(updateMap.isEmpty()){
						//赋值
						updateMap.put(dto.getDetailId(), skuCountMap.get(dto.getSku()));
					}else{
						if(updateMap.containsKey(dto.getDetailId())){
							//相加
							updateMap.put(dto.getDetailId(), updateMap.get(dto.getDetailId())+skuCountMap.get(dto.getSku()));
						}else{
							//赋值
							updateMap.put(dto.getDetailId(), skuCountMap.get(dto.getSku()));
						}
					} 
				}
			}
		}
	}

	
	
	
	
	/***
	 * 更新或插入销售数量
	 * @param updateMap
	 * @throws Exception
	 */
	private void createOrUpdateDetailSalesCount(Map<Long, Integer> updateMap) {
		if(!updateMap.isEmpty()){
			 //更新
			 for (Entry<Long,Integer> entry : updateMap.entrySet()) {
				 		//检验detail_id 是否存在记录
				 	Long detailId = entry.getKey();
				 	int detailCount =  itemDetailSalesCountDao.checkDetailIdExist(detailId); 
				 	if(detailCount > 0){
				 		ItemDetailSalesCount updateRealCount = new ItemDetailSalesCount();
				 		updateRealCount.setDetailId(detailId);
				 		updateRealCount.setRelSalesCount(entry.getValue() == null ? 0L : Long.valueOf(entry.getValue()));
				 		updateRealCount.setUpdateTime(new Date());  
				 		itemDetailSalesCountDao.updateRealSalesCount(updateRealCount);
				 	}else{
				 		
				 		ItemDetail detailInfo = itemDetailDao.queryById(detailId);
				 		//插入detail_id 对应数据
				 		ItemDetailSalesCount insertRealCount = new ItemDetailSalesCount();
				 		insertRealCount.setDetailId(detailId);
				 		insertRealCount.setRelSalesCount(entry.getValue() == null ? 0L : Long.valueOf(entry.getValue()));
				 		insertRealCount.setCreateTime(new Date());
				 		insertRealCount.setCreateUser("0");
				 		insertRealCount.setUpdateTime(new Date());
				 		insertRealCount.setUpdateUser("0");
				 		insertRealCount.setDefaultSalesCount(Long.valueOf(new Random().nextInt(100)));
				 		if(detailInfo != null){
				 			insertRealCount.setBarcode(detailInfo.getBarcode());
				 			insertRealCount.setPrdid(detailInfo.getPrdid());
				 			insertRealCount.setMainTitle(detailInfo.getMainTitle());
				 		}
				 		insertRealCount.setUpdateDefaultCountTime(new Date());
				 		itemDetailSalesCountDao.insert(insertRealCount);
				 	}
			 }
		}
	}



//	@Override
	public PageInfo<ItemDetailSalesCountDto> exportsDetailSalesPageList(
			ItemDetailSalesCountDto dto) throws ItemServiceException {
		/*if(dto != null && dto.getExportIds() != null && dto.getExportIds().length >0){
			dto.setStartPage(1);
			dto.setPageSize(dto.getExportIds().length);
			return this.exportsDetailSalesPageListWithIds(dto);
		}
		return new Page<DetailSalesCountDto>();*/
		return null;
	}
	
	
	/*private Page<DetailSalesCountDto> exportsDetailSalesPageListWithIds(DetailSalesCountDto dto) {
		if (dto != null) {
			Long totalCount = this.selectDetailSalesCountDynamic(dto);
			List<DetailSalesCountDto> resultList = this.selectDetailSalesDynamicPageQuery(dto);
			Page<DetailSalesCountDto> page = new Page<DetailSalesCountDto>();
			page.setPageNo(dto.getStartPage());
			page.setPageSize(dto.getPageSize());
			page.setTotalCount(totalCount.intValue());
			page.setList(resultList);
			return page;
		}
		return new Page<DetailSalesCountDto>();
	}*/




	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateDetailSalesCountDefaultCount(ItemDetailSalesCountDto dto)
			throws ItemServiceException {
		try {
			return itemDetailSalesCountDao.updateDetailSalesCountDefaultCount(dto);
		} catch (Exception e) {
			LOGGER.error(e.toString());
            throw new ServiceException(e);
		}
	}




	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String insertWithPRDIDS(List<ItemDetailSalesCount> insertList)
			throws ItemServiceException {
		if(CollectionUtils.isNotEmpty(insertList)){
			List<String> listPrd = new ArrayList<>();
			Map<String,ItemDetailSalesCount> infoMap = new HashMap<>();
			for(ItemDetailSalesCount checkDO:insertList){
				listPrd.add(checkDO.getPrdid());
				infoMap.put(checkDO.getPrdid(), checkDO);
			}
			try {
					//插入操作  获取商品名称 barcode
				Map<String, Object> params = new HashMap<>();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "prdid in('" + StringUtil.join(listPrd, "'"+Constant.SPLIT_SIGN.COMMA+"'") + "')");
				List<ItemDetail> listMainInfo = itemDetailDao.queryByParam(params);
					
					Map<String,ItemDetail> mainInfoMap = new HashMap<>();
					
					if(CollectionUtils.isNotEmpty(listMainInfo)){
						for(ItemDetail mianInfo :listMainInfo){
							mainInfoMap.put(mianInfo.getPrdid(), mianInfo);
						}
					}
					
					List<String> notExist = new ArrayList<>();
					for(ItemDetailSalesCount insertDO:insertList){
						if(!mainInfoMap.containsKey(insertDO.getPrdid())){
							notExist.add(insertDO.getPrdid());
						}
					}
					//不存在prdid
					if(CollectionUtils.isNotEmpty(notExist)){
						 return ItemConstant.CAN_NOT_GET.concat(StringUtils.join(notExist, ";"));
					}
					
					
					//更新列表
					Map<String, Object> listExistParam = new HashMap<>();
					listExistParam.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "prdid in('" + StringUtil.join(listPrd, "'"+Constant.SPLIT_SIGN.COMMA+"'") + "')");
					List<ItemDetailSalesCount> listExist = itemDetailSalesCountDao.queryByParam(listExistParam);
					List<ItemDetailSalesCount> updateList = new ArrayList<>();
					List<String> existCountPrdList = new ArrayList<>();
					//更新列表创建
					if(CollectionUtils.isNotEmpty(listExist)){
						for(ItemDetailSalesCount dto:listExist){
							if(infoMap.containsKey(dto.getPrdid())){
								updateList.add(infoMap.get(dto.getPrdid()));
								existCountPrdList.add(dto.getPrdid());
							}
						}
					}
					
					//更新列表
					if(CollectionUtils.isNotEmpty(updateList)){
						for(ItemDetailSalesCount updateDO :updateList){
							if(mainInfoMap.containsKey(updateDO.getPrdid())){
								updateDO.setDetailId(mainInfoMap.get(updateDO.getPrdid()).getId());
							}
							updateDO.setUpdateTime(new Date());
							updateDO.setUpdateDefaultCountTime(new Date());
							itemDetailSalesCountDao.updateDefaultCountByDetailId(updateDO);
						}
					}
					//插入列表
					for(ItemDetailSalesCount insertDO:insertList){
						if(mainInfoMap.containsKey(insertDO.getPrdid())){
							insertDO.setBarcode(mainInfoMap.get(insertDO.getPrdid()).getBarcode());
							insertDO.setMainTitle(mainInfoMap.get(insertDO.getPrdid()).getMainTitle());
							insertDO.setDetailId(mainInfoMap.get(insertDO.getPrdid()).getId());
						}
						if(!existCountPrdList.contains(insertDO.getPrdid())){
							itemDetailSalesCountDao.insert(insertDO);
						}
					}
				} catch (Exception e) {
					LOGGER.error(e.toString());
		            throw new ServiceException(e);
				}
			}
		return ItemConstant.ITEM_DETAIL_SALES_SUCCESS;
	}




	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String insertWithBarcodes(List<ItemDetailSalesCount> insertList)throws ItemServiceException {
		if(CollectionUtils.isNotEmpty(insertList)){
			List<String> listBarcode = new ArrayList<>();
			Map<String,ItemDetailSalesCount> infoMap = new HashMap<>();
			for(ItemDetailSalesCount checkDO:insertList){
				listBarcode.add(checkDO.getBarcode());
				infoMap.put(checkDO.getBarcode(), checkDO);
			}
			//批量校验barcode是否存在
			try {
					//插入操作  获取商品名称 barcode
				
				Map<String, Object> params = new HashMap<>();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "barcode in('" + StringUtil.join(listBarcode, "'"+Constant.SPLIT_SIGN.COMMA+"'") + "')");
				List<ItemDetail> listMainInfo = itemDetailDao.queryByParam(params);
				
					
					Map<String,ItemDetail> mainInfoMap = new HashMap<>();
					if(CollectionUtils.isNotEmpty(listMainInfo)){
						for(ItemDetail mianInfo :listMainInfo){
							mainInfoMap.put(mianInfo.getBarcode(), mianInfo);
						}
					}
					
					List<String> notExist = new ArrayList<>();
					for(ItemDetailSalesCount insertDO:insertList){
						if(!mainInfoMap.containsKey(insertDO.getBarcode())){
							notExist.add(insertDO.getBarcode());
						}
					}
					if(CollectionUtils.isNotEmpty(notExist)){
						 return ItemConstant.CAN_NOT_GET.concat(StringUtils.join(notExist, ";"));
					}
					
					Map<String, Object> listExistParam = new HashMap<>();
					listExistParam.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "barcode in('" + StringUtil.join(listBarcode, "'"+Constant.SPLIT_SIGN.COMMA+"'") + "')");
					List<ItemDetailSalesCount> listExist = itemDetailSalesCountDao.queryByParam(listExistParam);
					
//					List<DetailMainInfoDto> listExist = detailSalesCountDAO.checkExistBarcode(listBarcode);
					List<ItemDetailSalesCount> updateList = new ArrayList<>();
					List<String> existCountBarcodeList = new ArrayList<>();
					
					//更新列表创建
					if(CollectionUtils.isNotEmpty(listExist)){
						for(ItemDetailSalesCount dto:listExist){
							if(infoMap.containsKey(dto.getBarcode())){
								updateList.add(infoMap.get(dto.getBarcode()));
								existCountBarcodeList.add(dto.getBarcode());
							}
						}
					}
					//更新列表
					if(CollectionUtils.isNotEmpty(updateList)){
						for(ItemDetailSalesCount updateDO :updateList){
							if(mainInfoMap.containsKey(updateDO.getBarcode())){
								updateDO.setDetailId(mainInfoMap.get(updateDO.getBarcode()).getId());
							}
							updateDO.setUpdateTime(new Date());
							updateDO.setUpdateDefaultCountTime(new Date());
							itemDetailSalesCountDao.updateDefaultCountByDetailId(updateDO);
						}
					}
					//插入列表
					for(ItemDetailSalesCount insertDO:insertList){
						if(mainInfoMap.containsKey(insertDO.getBarcode())){
							insertDO.setPrdid(mainInfoMap.get(insertDO.getBarcode()).getPrdid());
							insertDO.setMainTitle(mainInfoMap.get(insertDO.getBarcode()).getMainTitle());
							insertDO.setDetailId(mainInfoMap.get(insertDO.getBarcode()).getId());
						}
						if(!existCountBarcodeList.contains(insertDO.getBarcode())){
							itemDetailSalesCountDao.insert(insertDO);
						}
					}
					
				} catch (Exception e) {
					LOGGER.error(e.toString());
		            throw new ServiceException(e);
				}
			}
			return ItemConstant.ITEM_DETAIL_SALES_SUCCESS;
	}

	/***
	 * 更具skucode 获取该sku当前的销量
	 */
	@Override
	public int getSalesCountBySku(String sku) throws ItemServiceException {
		//缓存数据获取
		try {
				Integer rediscount = (Integer) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_SKU_SALES_COUNT.concat(sku));
				if(rediscount !=null){
					return rediscount;
				}
			} catch (Exception e) {
				LOGGER.error(e.toString());		
		}
		ItemSku query = new ItemSku();
		query.setSku(sku);
		ItemSku detailInfo = itemSkuDao.queryByObject(query).get(0);
		try {
			List<DetailMainInfoDto> listDetailIds = itemDetailDao.selectListDetailIdsByItemId(detailInfo.getItemId());
			if(CollectionUtils.isNotEmpty(listDetailIds)){
				List<Long> detailIds = new ArrayList<>();
				for(DetailMainInfoDto dto:listDetailIds){
					detailIds.add(dto.getDetailId());
				}
				//更具detailId 获取销售量  
				try {
						 Integer salesCount = itemDetailSalesCountDao.getDetailSalesCountByDetailIds(detailIds);
						 try {
							 jedisCacheUtil.setCache(ItemRedisConstant.ITEM_SKU_SALES_COUNT.concat(sku), salesCount,60);
						 	} catch (Exception e) {
						 }
						 return salesCount;
					} catch (Exception e) {
						LOGGER.error(e.toString());		
				}
				}
			} catch (Exception e1) {
				LOGGER.error(e1.toString());
			}
		return 0;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getSalesCountBySkuList(List<String> skuList)
			throws ItemServiceException {
		if(CollectionUtils.isNotEmpty(skuList)){
			int listHash = skuList.hashCode();
			try {
					Map<String,Integer> skuCountMap = (Map<String, Integer>) jedisCacheUtil.getCache(ItemRedisConstant.ITEM_SKU_LIST_SALES_COUNT.concat(String.valueOf(listHash)));
					if(skuCountMap != null){
						return skuCountMap;
					}
				} catch (Exception e) {
					LOGGER.error(e.toString());
			}
			//批量获取detailIds
				try {
					List<SkuDetailInfoDto> listDetail =   itemSkuDao.selectSkuDetailInfoByListSkus(skuList);
						if(CollectionUtils.isNotEmpty(listDetail)){
							Map<String,Long> skuItemIdMap = new HashMap<String,Long>();
								for(SkuDetailInfoDto skuDto:listDetail){
									skuItemIdMap.put(skuDto.getSku(), skuDto.getItemId());
							 }
								
							 Map<String,Integer> skuCountMap = new HashMap<>();	
							  for (Entry<String, Long> entry : skuItemIdMap.entrySet()) { 
									List<DetailMainInfoDto> listDetailIds = itemDetailDao.selectListDetailIdsByItemId(entry.getValue());
									if(CollectionUtils.isNotEmpty(listDetailIds)){
										List<Long> itemDetailIds = new ArrayList<>();
										for(DetailMainInfoDto dto:listDetailIds){
											itemDetailIds.add(dto.getDetailId());
										}
										
										if(CollectionUtils.isNotEmpty(itemDetailIds)){
											skuCountMap.put(entry.getKey(),itemDetailSalesCountDao.getDetailSalesCountByDetailIds(itemDetailIds));
										}
									}
							  }
								//set redis cache
								try {
										jedisCacheUtil.setCache(ItemRedisConstant.ITEM_SKU_LIST_SALES_COUNT.concat(String.valueOf(listHash)), skuCountMap,60);
								} catch (Exception e) {
									LOGGER.error(e.toString());
								}
								return skuCountMap;
							}
					} catch (Exception e) {
						LOGGER.error(e.toString());
				}
		}else{
			return new HashMap<String,Integer>();
		}
		return new HashMap<String,Integer>();
	}




	@Override
	public SkuDto getPostageInfoAndItemInfo(String skuCode)
			throws ItemServiceException {
		/*if(StringUtils.isNotBlank(skuCode)){
			try {
				SkuDto postageInfo =itemSkuDao.getPostageInfoAndItemInfo(skuCode);
				if(postageInfo != null){
					//品牌信息
					DetailDO detailInfo = itemDetailDao.selectById(postageInfo.getDetailId(), DatasourceKey.master_item_dataSource);
					if(null != postageInfo.getBrandId()){
						BrandDO brandInfo = brandService.selectById(postageInfo.getBrandId());
						if(null != brandInfo){
							postageInfo.setBrandName(brandInfo.getName());
						}
					}
					//主图信息
					InfoDetailDto queryDto = new InfoDetailDto();
					queryDto.setDetailId(postageInfo.getDetailId());
					List<PicturesDto>   listPictures = itemInfoDao.selectItemPictures(queryDto,DatasourceKey.master_item_dataSource);
					if(CollectionUtils.isNotEmpty(listPictures)){
						postageInfo.setMainPicture(listPictures.get(0).getPicture());
					}
					//获取运费信息
					try {
						List<FreightTemplateDO>  freights = freightTemplateService.queryItemFreightTemplate(detailInfo.getFreightTemplateId());
						if(CollectionUtils.isNotEmpty(freights)){
							for (FreightTemplateDO freight : freights) {
								if(freight.getCalculateMode().equals(ItemComonConstant.ITEM_FREIGHT_SINGLE)){
									postageInfo.setPostage(freight.getPostage().toString());
								}
								if(freight.getCalculateMode().equals(ItemComonConstant.ITEM_FREIGHT_ALL)){
									if(freight.getFreePostage() != null){
										postageInfo.setFreePostage(freight.getFreePostage().toString());
									}
									if(freight.getAftPostage() != null){
										postageInfo.setAftPostage(freight.getAftPostage().toString());
									}
								}
							}
							}
						} catch (Exception e) {	
							e.printStackTrace();
						LOGGER.error(e.toString());
					}
					//获取规格信息
					try {
						List<Long> detailIds = new ArrayList<>();
						detailIds.add(postageInfo.getDetailId());
						List<SpecDetailDto> specList =  itemSkuDao.selectSpecByDetailIds(detailIds, DatasourceKey.master_item_dataSource);
						if(CollectionUtils.isNotEmpty(specList)){
							List<Long> specIds = new ArrayList<>();
							for(SpecDetailDto dto: specList){
								if(null != dto.getSpecId() &&  dto.getSpecId() != -1){
									specIds.add(dto.getSpecId());
								}
							}
							if(CollectionUtils.isNotEmpty(specIds)){
								List<SpecDO> listSpec = specService.selectListSpecDO(specIds, ItemComonConstant.ITEM_GROUP_STATUS_ALL);
								List<String> sepcDescList = new ArrayList<>();
								if(CollectionUtils.isNotEmpty(listSpec)){
									for(SpecDO sepcInfo :listSpec){
										if(StringUtils.isNotBlank(sepcInfo.getSpec())){
											sepcDescList.add(sepcInfo.getSpec());
										}
									}
								}
								if(CollectionUtils.isNotEmpty(sepcDescList)){
									postageInfo.setSepcDesc(sepcDescList);
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error(e.toString());
					}
					
					return postageInfo;
				}else{
					LOGGER.error("sku={} is not find data!",skuCode);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}else{
			throw new ItemServiceException("getPostageInfoAndItemInfo  param is empty");
		}*/
		return null;
	}




	/* (non-Javadoc)
	 * @see com.tp.service.prd.IItemRemoteService#queryDetailSalesPageListByStartPageSize(com.tp.dto.prd.ItemDetailSalesCountDto, int, int)
	 */
	@Override
	public PageInfo<ItemDetailSalesCountDto> queryDetailSalesPageListByStartPageSize(ItemDetailSalesCountDto dto, int startPage, int pageSize) {
		if(dto != null && startPage > 0 &&  pageSize >0){
			dto.setStartPage(startPage);
			dto.setPageSize(pageSize);
			return this.queryPageListByDetailSalesCountDto(dto);
		}
		return new PageInfo<ItemDetailSalesCountDto>();
	}
	public PageInfo<ItemDetailSalesCountDto> queryPageListByDetailSalesCountDto(ItemDetailSalesCountDto dto) {
		if (dto != null) {
			ItemDetailSalesCount query = new ItemDetailSalesCount();
			BeanUtils.copyProperties(dto, query);
			
			Map<String, Object> param = BeanUtil.beanMap(dto);

			PageInfo<ItemDetailSalesCount> page = new PageInfo<ItemDetailSalesCount>(dto.getStartPage(),dto.getPageSize());
			page = itemDetailSalesCountService.queryPageByParam(param,page);
			
			List<ItemDetailSalesCountDto> dtos = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(page.getRows())) {
				for(ItemDetailSalesCount salesCount : page.getRows()) {
					ItemDetailSalesCountDto newDto = new ItemDetailSalesCountDto();
					BeanUtils.copyProperties(salesCount, newDto);
					
					dtos.add(newDto);
				}
			}
			PageInfo<ItemDetailSalesCountDto> dtoPage = new PageInfo<>();
			dtoPage.setRows(dtos);
			dtoPage.setRecords(page.getRecords());
			dtoPage.setPage(dto.getStartPage());
			dtoPage.setSize(dto.getPageSize());
			
			return dtoPage;
		}
		return new PageInfo<ItemDetailSalesCountDto>();
	}




	/* (non-Javadoc)
	 * @see com.tp.service.prd.IItemRemoteService#getMaintitlesByDetailIds(java.util.List)
	 */
	@Override
	public List<DetailMainInfoDto> getMaintitlesByDetailIds(List<Long> detailIds) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in(" + StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA) + ")");
			
			List<ItemDetail> result = itemDetailDao.queryByParam(params);
			
			List<DetailMainInfoDto> dtos = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(result)) {
				for(ItemDetail detail : result) {
					DetailMainInfoDto dto = new DetailMainInfoDto();
					dto.setBarcode(detail.getBarcode());
					dto.setDetailId(detail.getId());
					dto.setMainTitle(detail.getMainTitle());
					dto.setPrdid(detail.getPrdid());
					dtos.add(dto);
				}
			}
			return dtos;
		}catch(Exception e){
			LOGGER.error(e.toString());
            throw new ServiceException(e);
		}
	}
}
