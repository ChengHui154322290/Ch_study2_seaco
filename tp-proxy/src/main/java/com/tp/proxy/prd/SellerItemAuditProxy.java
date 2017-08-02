package com.tp.proxy.prd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemOpenSaveDto;
import com.tp.dto.prd.enums.ItemDataSourceTypeEnum;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.prd.enums.SellerItemAuditTypeEnum;
import com.tp.dto.prd.enums.SellerItemBindLevelEnum;
import com.tp.model.bse.Category;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemAuditDetail;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.SupplierInfo;
import com.tp.service.bse.ICategoryService;
import com.tp.service.prd.IItemAttributeService;
import com.tp.service.prd.IItemAuditDetailService;
import com.tp.service.prd.IItemDescMobileService;
import com.tp.service.prd.IItemDescService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemDetailSpecService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.sup.ISupplierInfoService;

/****
 *	商家平台 审核类
 * @author szy
 *
 */
@Service
public class SellerItemAuditProxy {
	
	
	/*****************商品主库service***************************/
	@Autowired
	private IItemSkuService itemSkuService;
	
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private ISupplierInfoService  supplierInfoService;
	
	@Autowired
	private IItemManageService itemManageService;
	
	@Autowired
	private IItemDetailSpecService itemDetailSpecService;
	
	@Autowired
	private ICategoryService categoryService;
	
	
	/*****************商家平台service***************************/
	@Autowired
	private IItemDescService itemDescService;
	
	@Autowired
	private IItemDescMobileService itemDescMobileService;
	
	
	@Autowired
	private IItemAttributeService itemAttributeService;
	
	@Autowired
	private IItemAuditDetailService itemAuditDetailService;
	
	@Autowired
	private IItemPicturesService itemPicturesService;
	
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SellerItemAuditProxy.class);
	
	
	/***
	 * 商家平台 商品审核 主入口
	 * @param userId
	 * @param sellerSkuId
	 * @param auditDO
	 * @return
	 */
	public  ResultInfo<Boolean>   auditSellerItemWithBindLevel(Long userId,String userName,Long sellerSkuId,ItemAuditDetail auditDO){
		ItemSku  checkInfo = getSellerItemBasicInfo(sellerSkuId);
		if(SellerItemBindLevelEnum.SKU.getCode().equals(checkInfo.getBindLevel())){
			return  auditSellerItemSkuLevel(userId, userName,sellerSkuId, auditDO);
		}else if(SellerItemBindLevelEnum.PRD.getCode().equals(checkInfo.getBindLevel())){
			return  auditSellerItemPRDLevel(userId,userName, sellerSkuId, auditDO);
		}else if(SellerItemBindLevelEnum.SPU.getCode().equals(checkInfo.getBindLevel())){
			return  auditSellerItemSPULevel(userId,userName, sellerSkuId, auditDO);
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	
	//审核sku
	/***
	 *  数据检索都来自 master_data_base
	 * @param userId
	 * @param sellerSkuId
	 * @param auditType
	 * @return
	 */
	public ResultInfo<Boolean> auditSellerItemSkuLevel(Long userId,String userName,Long sellerSkuId,ItemAuditDetail auditDO){
		if(userName == null || sellerSkuId== null ){
			return new ResultInfo<>(new FailInfo("传入参数数据不完整"));
		}
		ItemSku  checkInfo = getSellerItemBasicInfo(sellerSkuId);
		if(checkInfo != null){
			if(!SellerItemBindLevelEnum.SKU.getCode().equals(checkInfo.getBindLevel())){
				return new ResultInfo<>(new FailInfo("获取商家平台对应商品sku层级信息与本逻辑不符合"));
			}
			if(checkInfo.getDetailId() == null){
				return new ResultInfo<>(new FailInfo("商家平台sku层级数据获取商品主库detail信息detailId为空"));
			}
			if(SellerItemAuditTypeEnum.A.getCode().equals(auditDO.getAuditResult())){
				//审核通过
				ItemDetail detilInfo = itemDetailService.selectByIdFromMaster(checkInfo.getDetailId());
				if(detilInfo != null ){
					//审核通过
					return approveSellerSkuBindLevel(userId,userName, checkInfo,detilInfo,auditDO);
				}else{
					return new ResultInfo<>(new FailInfo("获取商品主库中PRD信息为空"));
				}
			}else if(SellerItemAuditTypeEnum.R.getCode().equals(auditDO.getAuditResult())){
				try{
					checkInfo.setAuditStatus(SellerItemAuditTypeEnum.R.getCode());
					itemSkuService.updateNotNullById(checkInfo);
					insertSellerAudit(userId, checkInfo, auditDO);
					return new ResultInfo<>(Boolean.TRUE);
				}catch (Exception e) {
					LOGGER.error("审核失败:{}",e);
					return new ResultInfo<>(new FailInfo("审核失败"));
				}
				
				
			}
		}else{
			return new ResultInfo<>(new FailInfo("获取商家平台对应商品sku信息为空"));
		}
		return new ResultInfo<>(new FailInfo("参数不正确，不能审核"));
	}


	/***
	 * 审核通过 商家平台绑定sku
	 * @param userId
	 * @param returnObj
	 * @param checkInfo
	 * @param detilInfo
	 */
	private ResultInfo<Boolean> approveSellerSkuBindLevel(Long userId,String userName,ItemSku checkInfo, ItemDetail detilInfo,ItemAuditDetail auditDO) {
		/**
		//检验prd下存在该供应商的sku
		ItemSku query = new ItemSku();
		query.setSpId(checkInfo.getSpId());
		query.setDetailId(detilInfo.getId());
		ItemSku checkDO = itemSkuService.checQniqueSkuWithDetailIdAndSpId(query);
		if(checkDO != null ){
			return new ResultInfo<>(new FailInfo("相同DetailID下 该商家已经有SKU存在。"));
		}
		
		ItemInfo spuInfo = itemManageService.getItemInfoFromMasterDataBase(detilInfo.getItemId());
		//插入商家平台的数据
		ItemSku  skuDO = new ItemSku();
		skuDO.setItemId(detilInfo.getItemId());
		skuDO.setSpu(detilInfo.getSpu());
		skuDO.setSpuName(detilInfo.getItemTitle());
		skuDO.setBrandId(detilInfo.getBrandId());
		skuDO.setUnitId(detilInfo.getUnitId());
		skuDO.setItemType(detilInfo.getItemType());
		skuDO.setPrdid(detilInfo.getPrdid());
		skuDO.setDetailId(detilInfo.getId());
		skuDO.setCategoryCode(detilInfo.getCategoryCode());
		skuDO.setDetailName(detilInfo.getMainTitle());
		skuDO.setBarcode(detilInfo.getBarcode());
		skuDO.setCategoryId(spuInfo.getSmallId());
		String skuCode = itemManageService.getSkuCode(detilInfo.getPrdid());
		skuDO.setSku(skuCode);
		//上架状态设置为商家
		skuDO.setStatus(ItemStatusEnum.ONLINE.getValue());
		//销售类型设置为商家
		skuDO.setSaleType(ItemSaleTypeEnum.SELLER.getValue());
		skuDO.setSpId(checkInfo.getSpId());
		SupplierInfo supperInfo =	supplierInfoService.queryById(checkInfo.getSpId());
		skuDO.setSpCode(supperInfo.getSupplierCode());
		skuDO.setSpName(supperInfo.getName());
		skuDO.setBasicPrice(checkInfo.getBasicPrice());
		skuDO.setSort(100);
		skuDO.setCreateTime(new Date());
		skuDO.setCreateUser(userName);
		skuDO.setDataSource(ItemDataSourceTypeEnum.SELLER.getCode());
		itemSkuService.insert(skuDO);
		Long skuId = skuDO.getId();*/
		checkInfo.setStatus(ItemStatusEnum.ONLINE.getValue());
		checkInfo.setPrdid(detilInfo.getPrdid());
		checkInfo.setAuditStatus(SellerItemAuditTypeEnum.A.getCode());
		itemSkuService.updateNotNullById(checkInfo);
		insertSellerAudit(userId, checkInfo, auditDO);
		return new ResultInfo<>(Boolean.TRUE);
	}

	
	/***
	 * 插入审核日志
	 * @param userId
	 * @param checkInfo
	 * @param auditDO
	 */
	private void insertSellerAudit(Long userId,
			ItemSku checkInfo, ItemAuditDetail auditDO) {
		//添加审核日志
		//插入审核表日志
		ItemAuditDetail auditMessage = new ItemAuditDetail();
		auditMessage.setAuditLevel(checkInfo.getBindLevel());
		auditMessage.setAuditResult(auditDO.getAuditResult());
		auditMessage.setAuditDesc(auditDO.getAuditDesc());
		auditMessage.setSellerSkuId(checkInfo.getId());
		auditMessage.setAuditUserId(userId);
		auditMessage.setAuditTime(new Date());
		auditMessage.setSupplyerId(checkInfo.getSpId());
		itemAuditDetailService.insertAuditDetail(auditMessage);
	}
	
	
	
	//审核PRD
	/***
	 * 审核PRD层级数据
	 * @param userId
	 * @param sellerSkuId
	 * @param auditType
	 * @return
	 */
	public ResultInfo<Boolean> auditSellerItemPRDLevel(Long userId,String userName,Long sellerSkuId,ItemAuditDetail auditItemDetail){
			//异常数据 插入 需从商品数据库做删除
			Map<String,String> expDataInsert = new HashMap<>();
			//商品数据库查询查询时间
			Map<String,String> timeMap = new HashMap<>();
			String previousLog = "商家平台PRD层级商品审核,审核人Id:".concat(userId.toString()).concat("商家平台商品库sellerSkuId：").concat(sellerSkuId.toString());
			JSONObject returnObj  = new JSONObject();
			if(userId == null || sellerSkuId== null ){
				return new ResultInfo<>(new FailInfo("传入参数数据不完整"));
			}
			ItemSku  checkInfo = getSellerItemBasicInfo(sellerSkuId);
		 if(checkInfo != null){
				if(!SellerItemBindLevelEnum.PRD.getCode().equals(checkInfo.getBindLevel())){
					return new ResultInfo<>(new FailInfo("获取商家平台对应商品sku层级信息与本逻辑不符合"));
				}
				if(SellerItemAuditTypeEnum.A.getCode().equals(auditItemDetail.getAuditResult())){
					//审核批准
					//查询商家平台 PRD信息 获取商品主库SPU级别信息
					ItemDetail  sellerDetilInfo = itemDetailService.selectByIdFromMaster(checkInfo.getDetailId());
					if(sellerDetilInfo != null ){
						ItemInfo majorSpuInfo = itemManageService.getItemInfoFromMasterDataBaseBySpuCode(sellerDetilInfo.getSpu());
						if( majorSpuInfo != null){
							//验证barCode是否存在
							ItemDetail checkBarcode  =  itemDetailService.checkBarcodeFromMaster(sellerDetilInfo.getBarcode());
							try{
								timeMap.put("barcode_load_time", previousLog.concat(" 同步获取商品主库条形码：").concat(sellerDetilInfo.getBarcode()).concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")));
								LOGGER.info(previousLog.concat(" 同步获取商品主库条形码：").concat(sellerDetilInfo.getBarcode()).concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")));
								}catch (Exception e) {
									
							 }
							if(checkBarcode != null){
								return new ResultInfo<>(new FailInfo("商品主库已经存在改条形码：".concat(sellerDetilInfo.getBarcode())));
							}
							//检验 商家平台规格数据是否和商品主库的数据相同
							List<ItemDetail> groupMajorDetailIds = itemManageService.getGroupDetailIdFromMasterDataBaseWithItemId(majorSpuInfo.getId());
							if(CollectionUtils.isNotEmpty(groupMajorDetailIds)){
								Map<Long,List<Long>> checkSpecMap = new HashMap<>();
								//每个detailId中的所有规格信息
								Map<Long,List<String>> eachDetail = new HashMap<>();
								List<Long> detailIds = new ArrayList<>();
								for(ItemDetail checkDO: groupMajorDetailIds){
										detailIds.add(checkDO.getId());
									}
								 //1:获取商品主库 spu下规格信息
								if(CollectionUtils.isNotEmpty(detailIds)){
									List<ItemDetailSpec>  checkSpecList  =itemDetailSpecService.selectDetailSpecListByDetailIdsFromMaster(detailIds);
									try{
										timeMap.put("spec_info_load_time", previousLog.concat(" 同步获取商品主库规格信息：").concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")));
										LOGGER.info(previousLog.concat(" 同步获取商品主库规格信息：").concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")));
										}catch (Exception e) {
									}
								
									
									if(CollectionUtils.isNotEmpty(checkSpecList)){
										for(ItemDetailSpec checkSpec:checkSpecList){
											if(!checkSpecMap.containsKey(checkSpec.getSpecGroupId())){
												List<Long> mapList = new ArrayList<>();
												mapList.add(checkSpec.getSpecId());
												checkSpecMap.put(checkSpec.getSpecGroupId(), mapList);
											}else{
												List<Long> mapList = checkSpecMap.get(checkSpec.getSpecGroupId());
												if(CollectionUtils.isEmpty(mapList)){
													List<Long> newList = new ArrayList<>();
													newList.add(checkSpec.getSpecId());
													checkSpecMap.put(checkSpec.getSpecGroupId(), newList);
												}else{
													if(!mapList.contains(checkSpec.getSpecId())){
														mapList.add(checkSpec.getSpecId());
														checkSpecMap.put(checkSpec.getSpecGroupId(), mapList);
													}
													
												}
											}
											//相同PRDID（规格信息设置成一组）
											if(eachDetail.containsKey(checkSpec.getDetailId())){
												if(CollectionUtils.isEmpty(eachDetail.get(checkSpec.getDetailId()))){
													List<String> listSpec = new ArrayList<>();
													listSpec.add(checkSpec.getSpecGroupId().toString().concat(checkSpec.getSpecId().toString()));
													eachDetail.put(checkSpec.getDetailId(), listSpec);
												}else{
													List<String> listSpec  = eachDetail.get(checkSpec.getDetailId());
													listSpec.add(checkSpec.getSpecGroupId().toString().concat(checkSpec.getSpecId().toString()));
													eachDetail.put(checkSpec.getDetailId(), listSpec);
												}
											}else{
													List<String> listSpec = new ArrayList<>();
													listSpec.add(checkSpec.getSpecGroupId().toString().concat(checkSpec.getSpecId().toString()));
													eachDetail.put(checkSpec.getDetailId(), listSpec);
											}
										}
									}
								}
								
								//2:获取商家平台库中 当前审核PRD 规格信息
								List<Long> sellerDetialIds = new ArrayList<>();
								sellerDetialIds.add(checkInfo.getDetailId());
								List<ItemDetailSpec> sellerSpecList = itemDetailSpecService.selectDetailSpecListByDetailIdsFromMaster(sellerDetialIds);
								if(CollectionUtils.isNotEmpty(sellerSpecList) && !checkSpecMap.isEmpty() && !eachDetail.isEmpty()){
									Map<Long,List<String>> eachSellerDetail = new HashMap<>();
									for(ItemDetailSpec checkSellerSpec :sellerSpecList){
										if(!checkSpecMap.containsKey(checkSellerSpec.getSpecGroupId())){
											return new ResultInfo<>(new FailInfo("商家平台规格组信息和商品主库不一致。商家平台groupId:".concat(checkSellerSpec.getSpecGroupId().toString())));
										}
										if(eachSellerDetail.containsKey(checkSellerSpec.getDetailId())){
											if(CollectionUtils.isEmpty(eachSellerDetail.get(checkSellerSpec.getDetailId()))){
												List<String> listSpec = new ArrayList<>();
												listSpec.add(checkSellerSpec.getSpecGroupId().toString().concat(checkSellerSpec.getSpecId().toString()));
												eachSellerDetail.put(checkSellerSpec.getDetailId(), listSpec);
											}else{
												List<String> listSpec  = eachSellerDetail.get(checkSellerSpec.getDetailId());
												listSpec.add(checkSellerSpec.getSpecGroupId().toString().concat(checkSellerSpec.getSpecId().toString()));
												eachSellerDetail.put(checkSellerSpec.getDetailId(), listSpec);
											}
										}else{
												List<String> listSpec = new ArrayList<>();
												listSpec.add(checkSellerSpec.getSpecGroupId().toString().concat(checkSellerSpec.getSpecId().toString()));
												eachSellerDetail.put(checkSellerSpec.getDetailId(), listSpec);
										}
									}
									//主库list
									List<String> checkList = new ArrayList<>();
									//商家平台kulist
									List<String> sellerCheckList = new ArrayList<>();
									//获取所有组的信息
									for (Entry<Long, List<String>> entry : eachDetail.entrySet()) {
											List<String> groupString = entry.getValue();
											checkList.add(StringUtils.join(groupString, ";"));
									}
									for (Entry<Long, List<String>> entry : eachSellerDetail.entrySet()) {
										List<String> groupString = entry.getValue();
										sellerCheckList.add(StringUtils.join(groupString, ";"));
									}
									//判断每个组的信息是否重复
									if(CollectionUtils.isNotEmpty(checkList) && CollectionUtils.isNotEmpty(sellerCheckList)){
										for(String checkString :sellerCheckList){
											if(checkList.contains(checkString)){
												return new ResultInfo<>(new FailInfo("商品主库存在相同的规格信息"));
											 }
										  }
									   }
								    }
							 }
							
							   //3:通过校验  插入item_detail 数据
							try {
								 combinePRDLevelDataAndDoSave(userId,userName,checkInfo,sellerDetilInfo, majorSpuInfo,auditItemDetail);
							} catch (Exception e) {
								return new ResultInfo<>(new FailInfo(e.getMessage()));
							}
							return new ResultInfo<>(Boolean.TRUE);
						}else{
							return new ResultInfo<>(new FailInfo("更具商家平台SPU获取主库信息为空,SPU:".concat(sellerDetilInfo.getSpu())));
						}
					}else{
						return new ResultInfo<>(new FailInfo("获取商家平台PRD信息为空，导致无法查询主库SPU"));
					}
				}else if(SellerItemAuditTypeEnum.R.getCode().equals(auditItemDetail.getAuditResult())){
					//驳回
					try{
						checkInfo.setAuditStatus(SellerItemAuditTypeEnum.R.getCode());
						itemSkuService.updateNotNullById(checkInfo);
						insertSellerAudit(userId, checkInfo, auditItemDetail);
						return new ResultInfo<>(Boolean.TRUE);
					}catch (Exception e) {
						LOGGER.error("审核 失败:{},参数:{},{},{}",e,userId, checkInfo, auditItemDetail);
						return new ResultInfo<>(new FailInfo("审核失败"));
					}
			}
		 }else{
			return new ResultInfo<Boolean>(new FailInfo("获取商家平台对应商品sku信息为空"));
		}
		 return new ResultInfo<>(new FailInfo("传入参数不正确，未作操作"));
	}
		

	
	/***
	 * 审核SPU层级
	 * @param userId
	 * @param sellerSkuId
	 * @param auditType
	 * @return
	 */
	public ResultInfo<Boolean> auditSellerItemSPULevel(Long userId,String userName,Long sellerSkuId,ItemAuditDetail auditItemDetail){
		//异常数据 插入 需从商品数据库做删除
		Map<String,String> expDataInsert = new HashMap<>();
		//商品数据库查询查询时间
		Map<String,String> timeMap = new HashMap<>();
		String previousLog = "商家平台SPU层级商品审核,审核人Id:".concat(userId.toString()).concat("商家平台商品库sellerSkuId：").concat(sellerSkuId.toString());
		JSONObject returnObj  = new JSONObject();
		if(userId == null || sellerSkuId== null ){
			return new ResultInfo<>(new FailInfo("传入参数数据不完整"));
		}
		ItemSku  checkInfo = getSellerItemBasicInfo(sellerSkuId);
	 if(checkInfo != null){
			if(!SellerItemBindLevelEnum.SPU.getCode().equals(checkInfo.getBindLevel())){
				return new ResultInfo<>(new FailInfo("获取商家平台对应商品sku层级信息与本逻辑不符合"));
			}
			if(SellerItemAuditTypeEnum.A.getCode().equals(auditItemDetail.getAuditResult())){
				//审核批准
				//验证商家平台的spu是否和商品库中冲突
				ItemInfo  spuInfo = getSellerItemMainInfo(checkInfo.getItemId());
				if(spuInfo !=null  ){
					if(checkInfo.getMajorItemId() == null){
						//验证spu的唯一性
						ItemInfo checkSpuExist =	itemManageService.checkSpuExsit(spuInfo.getSmallId(),spuInfo.getBrandId(),spuInfo.getUnitId(),spuInfo.getMainTitle(),null);
						if(checkSpuExist !=null){
							return new ResultInfo<>(new FailInfo("相同的分类下SPU名称已经存在了"));
						}
					}
					//校验PRDid信息
					ItemDetail  sellerDetilInfo = itemDetailService.selectByIdFromMaster(checkInfo.getDetailId());
					if(sellerDetilInfo != null ){
							//验证barCode是否存在
							ItemDetail checkBarcode  =  itemDetailService.checkBarcodeFromMaster(sellerDetilInfo.getBarcode());
							try{
								timeMap.put("barcode_load_time", previousLog.concat(" 同步获取商品主库条形码：").concat(sellerDetilInfo.getBarcode()).concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")));
								LOGGER.info(previousLog.concat(" 同步获取商品主库条形码：").concat(sellerDetilInfo.getBarcode()).concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")));
								}catch (Exception e) {
									
							 }
							if(checkBarcode != null){
								return new ResultInfo<>(new FailInfo("商品主库已经存在改条形码：".concat(sellerDetilInfo.getBarcode())));
							}
							//seller 第一条提交PRD 审核
							if(checkInfo.getMajorItemId() == null){
								//直接封装数据 插入 item_info 插入item_detail item_sku 数据
								try {
										combineSPULevelDataAndDoSave(userId,userName,checkInfo,sellerDetilInfo, spuInfo,auditItemDetail);
								} catch (Exception e) {
									return new ResultInfo<>(new FailInfo(e.getMessage()));
								}
								return new ResultInfo<Boolean>(Boolean.TRUE);
							}else{	
								//存在已经审核过数据
								ItemInfo majorSpuInfo = itemManageService.getItemInfoFromMasterDataBaseBySpuCode(checkInfo.getSpu());
								//检验 商家平台规格数据是否和商品主库的数据相同
								List<ItemDetail> groupMajorDetailIds = itemManageService.getGroupDetailIdFromMasterDataBaseWithItemId(checkInfo.getMajorItemId());
								if(CollectionUtils.isNotEmpty(groupMajorDetailIds)){
									Map<Long,List<Long>> checkSpecMap = new HashMap<>();
									//每个detailId中的所有规格信息
									Map<Long,List<String>> eachDetail = new HashMap<>();
									List<Long> detailIds = new ArrayList<>();
									for(ItemDetail checkDO: groupMajorDetailIds){
											detailIds.add(checkDO.getId());
										}
									 //1:获取商品主库 spu下规格信息
									if(CollectionUtils.isNotEmpty(detailIds)){
										List<ItemDetailSpec>  checkSpecList  =itemDetailSpecService.selectDetailSpecListByDetailIdsFromMaster(detailIds);
										try{
											timeMap.put("spec_info_load_time", previousLog.concat(" 同步获取商品主库规格信息：").concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")));
											LOGGER.info(previousLog.concat(" 同步获取商品主库规格信息：").concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")));
											}catch (Exception e) {
										}
										if(CollectionUtils.isNotEmpty(checkSpecList)){
											for(ItemDetailSpec checkSpec:checkSpecList){
												if(!checkSpecMap.containsKey(checkSpec.getSpecGroupId())){
													List<Long> mapList = new ArrayList<>();
													mapList.add(checkSpec.getSpecId());
													checkSpecMap.put(checkSpec.getSpecGroupId(), mapList);
												}else{
													List<Long> mapList = checkSpecMap.get(checkSpec.getSpecGroupId());
													if(CollectionUtils.isEmpty(mapList)){
														List<Long> newList = new ArrayList<>();
														newList.add(checkSpec.getSpecId());
														checkSpecMap.put(checkSpec.getSpecGroupId(), newList);
													}else{
														if(!mapList.contains(checkSpec.getSpecId())){
															mapList.add(checkSpec.getSpecId());
															checkSpecMap.put(checkSpec.getSpecGroupId(), mapList);
														}
													}
												}
												//相同PRDID（规格信息设置成一组）
												if(eachDetail.containsKey(checkSpec.getDetailId())){
													if(CollectionUtils.isEmpty(eachDetail.get(checkSpec.getDetailId()))){
														List<String> listSpec = new ArrayList<>();
														listSpec.add(checkSpec.getSpecGroupId().toString().concat(checkSpec.getSpecId().toString()));
														eachDetail.put(checkSpec.getDetailId(), listSpec);
													}else{
														List<String> listSpec  = eachDetail.get(checkSpec.getDetailId());
														listSpec.add(checkSpec.getSpecGroupId().toString().concat(checkSpec.getSpecId().toString()));
														eachDetail.put(checkSpec.getDetailId(), listSpec);
													}
												}else{
														List<String> listSpec = new ArrayList<>();
														listSpec.add(checkSpec.getSpecGroupId().toString().concat(checkSpec.getSpecId().toString()));
														eachDetail.put(checkSpec.getDetailId(), listSpec);
												}
											}
										}
									}
									
									//2:获取商家平台库中 当前审核PRD 规格信息
									List<Long> sellerDetialIds = new ArrayList<>();
									sellerDetialIds.add(checkInfo.getDetailId());
									List<ItemDetailSpec> sellerSpecList = itemDetailSpecService.selectDetailSpecListByDetailIdsFromMaster(sellerDetialIds);
									if(CollectionUtils.isNotEmpty(sellerSpecList) && !checkSpecMap.isEmpty() && !eachDetail.isEmpty()){
										Map<Long,List<String>> eachSellerDetail = new HashMap<>();
										for(ItemDetailSpec checkSellerSpec :sellerSpecList){
											if(!checkSpecMap.containsKey(checkSellerSpec.getSpecGroupId())){
												return new ResultInfo<>(new FailInfo("商家平台规格组信息和商品主库不一致。商家平台groupId:".concat(checkSellerSpec.getSpecGroupId().toString())));
											}
											if(eachSellerDetail.containsKey(checkSellerSpec.getDetailId())){
												if(CollectionUtils.isEmpty(eachSellerDetail.get(checkSellerSpec.getDetailId()))){
													List<String> listSpec = new ArrayList<>();
													listSpec.add(checkSellerSpec.getSpecGroupId().toString().concat(checkSellerSpec.getSpecId().toString()));
													eachSellerDetail.put(checkSellerSpec.getDetailId(), listSpec);
												}else{
													List<String> listSpec  = eachSellerDetail.get(checkSellerSpec.getDetailId());
													listSpec.add(checkSellerSpec.getSpecGroupId().toString().concat(checkSellerSpec.getSpecId().toString()));
													eachSellerDetail.put(checkSellerSpec.getDetailId(), listSpec);
												}
											}else{
													List<String> listSpec = new ArrayList<>();
													listSpec.add(checkSellerSpec.getSpecGroupId().toString().concat(checkSellerSpec.getSpecId().toString()));
													eachSellerDetail.put(checkSellerSpec.getDetailId(), listSpec);
											}
										}
										//主库list
										List<String> checkList = new ArrayList<>();
										//商家平台kulist
										List<String> sellerCheckList = new ArrayList<>();
										//获取所有组的信息
										for (Entry<Long, List<String>> entry : eachDetail.entrySet()) {
												List<String> groupString = entry.getValue();
												checkList.add(StringUtils.join(groupString, ";"));
										}
										for (Entry<Long, List<String>> entry : eachSellerDetail.entrySet()) {
											List<String> groupString = entry.getValue();
											sellerCheckList.add(StringUtils.join(groupString, ";"));
										}
										//判断每个组的信息是否重复
										if(CollectionUtils.isNotEmpty(checkList) && CollectionUtils.isNotEmpty(sellerCheckList)){
											for(String checkString :sellerCheckList){
												if(checkList.contains(checkString)){
													return new ResultInfo<>(new FailInfo("商品主库存在相同的规格信息"));
												 }
											  }
										   }
									    }
								 }
								
								 //3:通过校验  插入item_detail 数据
								try {
									 combinePRDLevelDataAndDoSave(userId,userName, checkInfo,sellerDetilInfo, majorSpuInfo,auditItemDetail);
								} catch (Exception e) {
									return new ResultInfo<>(new FailInfo(e.getMessage()));
								}
								return new ResultInfo<>(Boolean.TRUE);
							}
					}else{
						return new ResultInfo<>(new FailInfo("获取商家平台PRD信息为空，导致无法查询主库SPU"));
					}
				}
			}else if(SellerItemAuditTypeEnum.R.getCode().equals(auditItemDetail.getAuditResult())){
				try{
					checkInfo.setAuditStatus(SellerItemAuditTypeEnum.R.getCode());
					itemSkuService.updateNotNullById(checkInfo);
					insertSellerAudit(userId, checkInfo, auditItemDetail);
					return new ResultInfo<Boolean>(Boolean.TRUE);
				}catch (Exception e) {
					LOGGER.error("审核失败:{},参数:{},{},{}",e,userId, checkInfo, auditItemDetail);
					return new ResultInfo<>(new FailInfo("传入参数数据不完整"));
				}
			
		}
	 }else{
		 return new ResultInfo<>(new FailInfo("获取商家平台对应商品sku信息为空"));
	 }
	 return new ResultInfo<>(new FailInfo("传入参数不能审核"));
   }
	
	
	
	

	
	/****
	 * 组合PRD层级数据和保存
	 * @param userId
	 * @param checkInfo
	 * @param sellerDetilInfo
	 * @param majorSpuInfo
	 * @throws DAOException
	 */
	private void combinePRDLevelDataAndDoSave(Long userId,String userName,
			ItemSku checkInfo,
			ItemDetail sellerDetilInfo,
			ItemInfo majorSpuInfo,ItemAuditDetail auditItemDetail){
		ItemOpenSaveDto saveDto = new ItemOpenSaveDto();
		//sellerDetilInfo
		ItemDetail insertDetail = new ItemDetail();
		BeanUtils.copyProperties(sellerDetilInfo, insertDetail);
		insertDetail.setId(null);
		insertDetail.setSpu(majorSpuInfo.getSpu());
		insertDetail.setItemId(majorSpuInfo.getId());
		insertDetail.setStatus(ItemStatusEnum.ONLINE.getValue());
		insertDetail.setSpuName(majorSpuInfo.getMainTitle());
		insertDetail.setBrandId(majorSpuInfo.getBrandId());
		insertDetail.setUnitId(majorSpuInfo.getUnitId());
		insertDetail.setCategoryCode(majorSpuInfo.getSmallCode());
		insertDetail.setCreateTime(new Date());
		insertDetail.setUpdateTime(new Date());
		insertDetail.setCreateUser(userName);
		insertDetail.setUpdateUser(userName);
		
		saveDto.setItemDetail(insertDetail);
		
		
		
		//图片信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", sellerDetilInfo.getId());
		List<ItemPictures>  savePictture = itemPicturesService.queryByParam(params);
		if(savePictture != null){
			List<ItemPictures> saveList = new ArrayList<>();
			for(ItemPictures saveDO :savePictture){
				ItemPictures newPic = new ItemPictures();
				BeanUtils.copyProperties(saveDO, newPic);
				newPic.setId(null);
				newPic.setItemId(majorSpuInfo.getId()); 
				newPic.setDetailId(null);
				newPic.setCreateTime(new Date());
				saveList.add(newPic);
			}
			saveDto.setListPic(saveList);
		}
		ItemDesc saveDesc = itemDescService.selectByDetailIdFromMaster(sellerDetilInfo.getId());
		if(saveDesc != null){
			ItemDesc saveItemDesc = new ItemDesc();
			BeanUtils.copyProperties(saveDesc, saveItemDesc);
			saveItemDesc.setId(null);
			saveItemDesc.setDetailId(null);
			saveItemDesc.setCreateUser(userName);
			saveItemDesc.setUpdateUser(userName);
			saveItemDesc.setCreateTime(new Date());
			saveItemDesc.setUpdateTime(new Date());
			saveItemDesc.setItemId(majorSpuInfo.getId());
			saveDto.setItemDesc(saveItemDesc);
			
		}
		//手机端描述
		ItemDescMobile saveMobileDO = itemDescMobileService.selectByDetailFromMaster(sellerDetilInfo.getId());
		
		if(saveMobileDO != null){
			ItemDescMobile saveMobile = new ItemDescMobile();
			BeanUtils.copyProperties(saveMobileDO, saveMobile);
			saveMobile.setId(null);
			saveMobile.setDetailId(null);
			saveMobile.setCreateUser(userName);
			saveMobile.setUpdateUser(userName);
			saveMobile.setCreateTime(new Date());
			saveMobile.setUpdateTime(new Date());
			saveMobile.setItemId(majorSpuInfo.getId());
			saveDto.setItemDescMobile(saveMobile);
		}
		
		
		//属性组信息
		List<ItemAttribute> saveAttrList = itemAttributeService.selectAttrListByDetailIdFromMaster(sellerDetilInfo.getId());
		if(CollectionUtils.isNotEmpty(saveAttrList)){
			List<ItemAttribute> saveList = new ArrayList<>();
			for(ItemAttribute saveAttr:saveAttrList){
				ItemAttribute saveDO = new ItemAttribute();
				BeanUtils.copyProperties(saveAttr, saveDO);
				saveDO.setId(null);
				saveDO.setItemId(majorSpuInfo.getId());
				saveDO.setDetailId(null);
				saveList.add(saveDO);
			}
			saveDto.setAttributeList(saveList);
		}
		//规格组
		
		List<Long> detailIdsForSpec = new ArrayList<>();
		detailIdsForSpec.add(sellerDetilInfo.getId());
		
		List<ItemDetailSpec> saveSpecList= itemDetailSpecService.selectDetailSpecListByDetailIdsFromMaster(detailIdsForSpec);
		
		
		if(CollectionUtils.isNotEmpty(saveSpecList)){
			List<ItemDetailSpec> saveList = new ArrayList<>();
			for(ItemDetailSpec saveSpec :saveSpecList ){
				ItemDetailSpec saveNew = new ItemDetailSpec();
				BeanUtils.copyProperties(saveSpec, saveNew);
				saveNew.setDetailId(null);
				saveNew.setCreateTime(new Date());
				saveNew.setUpdateTime(new Date());
				saveList.add(saveNew);
			}
			saveDto.setListDetailSpec(saveList);
		}
		
		
		ItemSku saveSku  = new ItemSku();
		BeanUtils.copyProperties(checkInfo, saveSku);
		saveSku.setDetailId(null);
		saveSku.setSpuName(majorSpuInfo.getMainTitle());
		saveSku.setUnitId(majorSpuInfo.getUnitId());
		saveSku.setCategoryCode(majorSpuInfo.getSmallCode());
		saveSku.setCategoryId(majorSpuInfo.getSmallId());
		saveSku.setDetailName(sellerDetilInfo.getMainTitle());
		saveSku.setBrandId(majorSpuInfo.getBrandId());
		saveSku.setBasicPrice(checkInfo.getBasicPrice());
		saveSku.setId(null);
		saveDto.setItemSku(saveSku);
		Long skuId =itemManageService.saveOpenPlantFromItem(saveDto);
		
		ItemSku backDO = itemSkuService.selectByIdFromMaster(skuId);
		if(backDO != null){
			try{
				checkInfo.setSku(backDO.getSku());
				checkInfo.setStatus(ItemStatusEnum.ONLINE.getValue());
				checkInfo.setPrdid(backDO.getPrdid());
				checkInfo.setAuditStatus(SellerItemAuditTypeEnum.A.getCode());
				itemSkuService.updateNotNullById(checkInfo);
				insertSellerAudit(userId, checkInfo, auditItemDetail);
			}catch (Exception e) {
				
			}
		}
	}
	
	
	
	/****
	 * 组合PRD层级数据和保存
	 * @param userId
	 * @param checkInfo
	 * @param sellerDetilInfo
	 * @param majorSpuInfo
	 * @throws DAOException
	 */
	@SuppressWarnings("unused")
	private void combineSPULevelDataAndDoSave(Long userId,String userName,ItemSku checkInfo,ItemDetail sellerDetilInfo,ItemInfo spuInfo,ItemAuditDetail auditItemDetail){
		ItemOpenSaveDto saveDto = new ItemOpenSaveDto();
		//seller item_info 信息
		ItemInfo saveInfo = new ItemInfo();
		BeanUtils.copyProperties(spuInfo, saveInfo);		
		saveInfo.setCreateTime(new Date());
		saveInfo.setUpdateTime(new Date());
		saveInfo.setCreateUser(userName);
		saveInfo.setUpdateUser(userName);
		saveInfo.setSpu(null);
		//设置小类编码
		Category category  = categoryService.queryById(spuInfo.getSmallId());
		if(null!=category){
			saveInfo.setSmallCode(category.getCode());//设置小类的编码关联spu的编码
		}
		saveDto.setItemInfo(saveInfo);
		//sellerDetilInfo
		ItemDetail insertDetail = new ItemDetail();
		BeanUtils.copyProperties(sellerDetilInfo, insertDetail);
		insertDetail.setId(null);
		insertDetail.setSpu(null);
		insertDetail.setItemId(null);
		insertDetail.setStatus(ItemStatusEnum.ONLINE.getValue());
		insertDetail.setSpuName(spuInfo.getMainTitle());
		insertDetail.setBrandId(spuInfo.getBrandId());
		insertDetail.setUnitId(spuInfo.getUnitId());
		insertDetail.setCategoryCode(spuInfo.getSmallCode());
		insertDetail.setCreateTime(new Date());
		insertDetail.setUpdateTime(new Date());
		insertDetail.setCreateUser(userName);
		insertDetail.setUpdateUser(userName);
		saveDto.setItemDetail(insertDetail);
		//图片信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", sellerDetilInfo.getId());
		List<ItemPictures>  savePictture = itemPicturesService.queryByParam(params);
		
		if(savePictture != null){
			List<ItemPictures> saveList = new ArrayList<>();
			for(ItemPictures saveDO :savePictture){
				ItemPictures newPic = new ItemPictures();
				BeanUtils.copyProperties(saveDO, newPic);
				newPic.setId(null);
				newPic.setItemId(null); 
				newPic.setDetailId(null);
				newPic.setCreateTime(new Date());
				saveList.add(newPic);
			}
			saveDto.setListPic(saveList);
		}
		//描述
		ItemDesc saveDesc = itemDescService.selectByDetailIdFromMaster(sellerDetilInfo.getId());
		if(saveDesc != null){
			ItemDesc saveItemDesc = new ItemDesc();
			BeanUtils.copyProperties(saveDesc, saveItemDesc);
			saveItemDesc.setId(null);
			saveItemDesc.setDetailId(null);
			saveItemDesc.setCreateUser(userName);
			saveItemDesc.setUpdateUser(userName);
			saveItemDesc.setCreateTime(new Date());
			saveItemDesc.setUpdateTime(new Date());
			saveItemDesc.setItemId(null);
			saveDto.setItemDesc(saveItemDesc);
			
		}
		//手机端描述
		ItemDescMobile saveMobileDO = itemDescMobileService.selectByDetailFromMaster(sellerDetilInfo.getId());
		
		if(saveMobileDO != null){
			ItemDescMobile saveMobile = new ItemDescMobile();
			BeanUtils.copyProperties(saveMobileDO, saveMobile);
			saveMobile.setId(null);
			saveMobile.setDetailId(null);
			saveMobile.setCreateUser(userName);
			saveMobile.setUpdateUser(userName);
			saveMobile.setCreateTime(new Date());
			saveMobile.setUpdateTime(new Date());
			saveMobile.setItemId(null);
			saveDto.setItemDescMobile(saveMobile);
		}
		//属性组信息
		List<ItemAttribute> saveAttrList = itemAttributeService.selectAttrListByDetailIdFromMaster(sellerDetilInfo.getId());
		if(CollectionUtils.isNotEmpty(saveAttrList)){
			List<ItemAttribute> saveList = new ArrayList<>();
			for(ItemAttribute saveAttr:saveAttrList){
				ItemAttribute saveDO = new ItemAttribute();
				BeanUtils.copyProperties(saveAttr, saveDO);
				saveDO.setId(null);
				saveDO.setItemId(null);
				saveDO.setDetailId(null);
				saveList.add(saveDO);
			}
			saveDto.setAttributeList(saveList);
		}
		//规格组
		List<Long> detailIdsForSpec = new ArrayList<>();
		detailIdsForSpec.add(sellerDetilInfo.getId());
		
		List<ItemDetailSpec> saveSpecList= itemDetailSpecService.selectDetailSpecListByDetailIdsFromMaster(detailIdsForSpec);
		
		if(CollectionUtils.isNotEmpty(saveSpecList)){
			List<ItemDetailSpec> saveList = new ArrayList<>();
			for(ItemDetailSpec saveSpec :saveSpecList ){
				ItemDetailSpec saveNew = new ItemDetailSpec();
				BeanUtils.copyProperties(saveSpec, saveNew);
				saveNew.setDetailId(null);
				saveNew.setCreateTime(new Date());
				saveNew.setUpdateTime(new Date());
				saveList.add(saveNew);
			}
			saveDto.setListDetailSpec(saveList);
		}
		
		ItemSku saveSku  = new ItemSku();
		BeanUtils.copyProperties(checkInfo, saveSku);
		saveSku.setDetailId(null);
		saveSku.setItemId(null);
		saveSku.setSpuName(spuInfo.getMainTitle());
		saveSku.setUnitId(spuInfo.getUnitId());
		saveSku.setCategoryCode(spuInfo.getSmallCode());
		saveSku.setCategoryId(spuInfo.getSmallId());
		saveSku.setDetailName(sellerDetilInfo.getMainTitle());
		saveSku.setBrandId(spuInfo.getBrandId());
		saveSku.setBasicPrice(checkInfo.getBasicPrice());
		saveSku.setId(null);
		saveDto.setItemSku(saveSku);
		ItemOpenSaveDto  returnDto =itemManageService.saveOpenPlantForsSpuFromItem(saveDto);
		
		ItemSku backDO = itemSkuService.selectByIdFromMaster(returnDto.getItemSku().getId());
		if(backDO != null){
			try{
				//单挑更新
				checkInfo.setSku(backDO.getSku());
				checkInfo.setStatus(ItemStatusEnum.ONLINE.getValue());
				checkInfo.setPrdid(backDO.getPrdid());
				checkInfo.setSpu(backDO.getSpu());
				checkInfo.setAuditStatus(SellerItemAuditTypeEnum.A.getCode());
				checkInfo.setMajorItemId(backDO.getItemId());
				itemSkuService.updateNotNullById(checkInfo);
				//itemSkuService
				ItemSku updateLast = new ItemSku();
				updateLast.setItemId(checkInfo.getItemId());
				updateLast.setStatus(ItemStatusEnum.ONLINE.getValue());
				updateLast.setSpu(backDO.getSpu());
				updateLast.setMajorItemId(backDO.getItemId());
				itemSkuService.updateSkuInfoWithItemSpuInfo(updateLast);
				//插入审核日志表
				insertSellerAudit(userId, checkInfo, auditItemDetail);
			 }catch (Exception e) {
				// 插入back数据库惊醒,timetask 处理任务
			}
		}
	}
	/***
	 * 获取商家平台sku信息
	 * @param sellerSkuId
	 * @return
	 */
	private  ItemSku getSellerItemBasicInfo(Long sellerSkuId){
		
			return itemSkuService.selectByIdFromMaster(sellerSkuId);
	}
	/***
	 * 获取商家平台spu信息
	 * @param sellerSkuId
	 * @return
	 */
	private  ItemInfo getSellerItemMainInfo(Long sellerItemId){
			return itemManageService.getItemInfoFromMasterDataBase(sellerItemId);
	}
}
