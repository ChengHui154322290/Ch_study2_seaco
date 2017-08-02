package com.tp.service.prd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dao.prd.ItemAttributeDao;
import com.tp.dao.prd.ItemDetailDao;
import com.tp.dao.prd.ItemDetailSpecDao;
import com.tp.dao.prd.ItemImportListDao;
import com.tp.dao.prd.ItemImportLogDao;
import com.tp.dao.prd.ItemInfoDao;
import com.tp.dao.prd.ItemSkuArtDao;
import com.tp.dao.prd.ItemSkuDao;
import com.tp.dao.prd.ItemSkuSupplierDao;
import com.tp.datasource.ContextHolder;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ExcelImportSucessDto;
import com.tp.dto.prd.ExcelSkuDto;
import com.tp.dto.prd.ItemSkuModifyDto;
import com.tp.dto.prd.SkuImportDto;
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemImportList;
import com.tp.model.prd.ItemImportLog;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.prd.ItemSkuSupplier;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierUser;
import com.tp.result.sup.SupplierResult;
import com.tp.service.prd.IItemImportService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.service.sup.ISupplierUserService;
import com.tp.util.StringUtil;
/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Service
public class ItemImportService implements IItemImportService {
	
	private static final Logger  LOGGER = LoggerFactory.getLogger(ItemImportService.class);
	@Autowired
	private ItemInfoDao itemInfoDao;
	@Autowired
	private ItemDetailDao itemDetailDao;
	@Autowired
	private ItemSkuDao itemSkuDao;
	@Autowired
	private ItemAttributeDao itemAttributeDao;
	@Autowired
	private ItemDetailSpecDao itemDetailSpecDao;
	@Autowired
	private ItemSkuSupplierDao itemSkuSupplierDao;
	@Autowired
	private ItemImportLogDao itemImportLogDao;
	@Autowired
	private ItemImportListDao itemImportListDao;	
	@Autowired
	private IItemManageService itemManageService;
	@Autowired
	private ISupplierUserService supplierUserService;
	@Autowired
	private ItemSkuArtDao itemSkuArtDao;
	@Autowired
	private IPurchasingManagementService  purchasingManagementService;
	
	@Override
	public List<SkuImportDto> importSku(List<SkuImportDto> list,Long logId,List<ExcelSkuDto> validFailIndexList,String createUser ,String importFrom) {
		//处理日志监控
		//一批执行的时间
		//单个sku的时间
		Long start = System.currentTimeMillis();
		if(CollectionUtils.isNotEmpty(validFailIndexList)){
			itemImportListDao.batchUpdateDynamic(validFailIndexList);
			//更新log
			ItemImportLog itemImportLog = new ItemImportLog();
			itemImportLog.setId(logId);
			itemImportLog.setFailCount(validFailIndexList.size());
			itemImportLogDao.updateCountById(itemImportLog);
		}
		for(SkuImportDto sku : list){
			Long oneSkustart = System.currentTimeMillis();
			ResultInfo<?> resultInfo = new ResultInfo<>();
			try{
				ExcelImportSucessDto excelImportSucessDto = new ExcelImportSucessDto();
	            if("supplier".equals(importFrom)){//来源于供应商，prd_item_info 保存供应商
	            	sku.setImportFrom(importFrom);
	            	SupplierUser supplierUser=supplierUserService.getSupplierUserByUserName(createUser);
					if(supplierUser!=null){
						sku.setSupplierId(supplierUser.getSupplierId());
					}
				}
				resultInfo = importOneSku(sku,createUser,excelImportSucessDto);
				if(!resultInfo.success){
					ItemImportList itemImportList = new ItemImportList();
					itemImportList.setStatus(2);
					itemImportList.setOpMessage(resultInfo.msg.getMessage());
					itemImportList.setLogId(logId);
					itemImportList.setExcelIndex(sku.getExcelIndex());
					itemImportListDao.updateByLogId(itemImportList);
					//更新log
					ItemImportLog itemImportLog = new ItemImportLog();
					itemImportLog.setId(logId);
					itemImportLog.setFailCount(1);
					itemImportLogDao.updateCountById(itemImportLog);
				}else{
					ItemImportList itemImportList = new ItemImportList();
					itemImportList.setStatus(1);
					itemImportList.setOpMessage("");
					itemImportList.setLogId(logId);
					itemImportList.setExcelIndex(sku.getExcelIndex());
					itemImportList.setSpuCode(excelImportSucessDto.getSpuCode());
					itemImportList.setPrdid(excelImportSucessDto.getPrdidCode());
					itemImportList.setSkuCode(excelImportSucessDto.getSkuCode());
					itemImportListDao.updateByLogId(itemImportList);
					ItemImportLog itemImportLog = new ItemImportLog();
					itemImportLog.setId(logId);
					itemImportLog.setSuccessCount(1);
					itemImportLogDao.updateCountById(itemImportLog);
				}
				sku.setImportStatus(resultInfo.success?1:0);
			}
			catch(Exception e){
				resultInfo.setMsg(new FailInfo(e));
				try {
					ItemImportList itemImportList = new ItemImportList();
					itemImportList.setStatus(2);
					itemImportList.setOpMessage(resultInfo.getMsg().getMessage());
					itemImportList.setLogId(logId);
					itemImportList.setExcelIndex(sku.getExcelIndex());
					itemImportListDao.updateByLogId(itemImportList);
					ItemImportLog itemImportLog = new ItemImportLog();
					itemImportLog.setId(logId);
					itemImportLog.setFailCount(1);
					itemImportLogDao.updateCountById(itemImportLog);
				} catch (Exception e2) {
					LOGGER.error("{}", e);
				}
			}
			LOGGER.info("导入一个商品，logId:{},excelIndex:{},耗时： {} ",logId,sku.getExcelIndex(), (System.currentTimeMillis()-oneSkustart));
		}
		ItemImportLog itemImportLog = new ItemImportLog();
		itemImportLog.setId(logId);
		itemImportLog.setStatus(4);
		itemImportLogDao.updateNotNullById(itemImportLog);
		LOGGER.info("导入商品，logId : "+logId+"耗时: "+ (System.currentTimeMillis()-start));
		return list;
	}
	
	/**
	 * 
	 * <pre>
	 * 	 导入一个sku信息
	 * </pre>
	 *
	 * @param sku
	 * @param userId
	 * @param excelImportSucessDto
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Boolean> importOneSku(SkuImportDto sku,String createUser,ExcelImportSucessDto excelImportSucessDto){ 
		ResultInfo<Boolean> resultInfo = new ResultInfo<Boolean>();
		long itemId = 0L;
		long detailId = 0L;
		String spu ="";
		String categoryCode = "";
		String prdid = "";
		String skuCode = "";
		try {
			Date date = new Date();
			ItemInfo info = itemManageService.checkSpuExsit(sku.getSmallId(), sku.getBrandId(), sku.getUnitId(), sku.getSpuName().trim(),null);
			if(null==info){
				ItemInfo itemInfo = new ItemInfo();
				categoryCode = sku.getCategoryCode();
				spu = itemManageService.getSpuCode(categoryCode);
				itemInfo.setSpu(spu);
				if("supplier".equals(sku.getImportFrom())){//来源于供应商导入
					itemInfo.setSupplierId(sku.getSupplierId());
				}
				itemInfo.setMainTitle(sku.getSpuName());
				itemInfo.setLargeId(sku.getLargId());
				itemInfo.setMediumId(sku.getBrandId());
				itemInfo.setSmallId(sku.getSmallId());
				itemInfo.setCreateUser(createUser);
				itemInfo.setCreateTime(date);
				itemInfo.setMediumId(sku.getMediumId());
				itemInfo.setUnitId(sku.getUnitId());
				itemInfo.setBrandId(sku.getBrandId());
				itemInfo.setUpdateTime(date);
				itemInfo.setUpdateUser(createUser);
				itemInfo.setRemark(sku.getSpuRemark());
				itemInfoDao.insert(itemInfo);
				itemId = itemInfo.getId();
				boolean flag = itemManageService.checkBarcodeExsit(sku.getBarcode(), null, itemId);
				if(!flag){
					LOGGER.error("导入新增商品中，商品条码出现重复");
					throw new ItemServiceException( "系统中已经存在此条码:"+sku.getBarcode());
				}
				
				//插入detail
				ItemDetail detail = importDetail(itemId,categoryCode,spu,sku,date,createUser);
				prdid = detail.getPrdid();
				detailId = detail.getId();
				ItemSku retSku = importSku(itemId,detailId,spu,prdid,categoryCode,sku,date,createUser);
				skuCode = retSku.getSku();
			}else{
				itemId = info.getId();
				sku.setItemId(itemId);
				spu = info.getSpu();
				categoryCode = sku.getCategoryCode();
				Map<Long,Long>specMap = new HashMap<Long,Long>();
				if(null!=sku.getSpec1GroupId()&&null!=sku.getSpec1Id()){
					specMap.put(sku.getSpec1GroupId(), sku.getSpec1Id());
				}
				if(null!=sku.getSpec2GroupId()&&null!=sku.getSpec2Id()){
					specMap.put(sku.getSpec2GroupId(), sku.getSpec2Id());
				}
				if(null!=sku.getSpec3GroupId()&&null!=sku.getSpec3Id()){
					specMap.put(sku.getSpec3GroupId(), sku.getSpec3Id());
				}
				
				ItemDetail de = new ItemDetail();
				de.setItemId(itemId);
				de.setBarcode(sku.getBarcode());
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("itemId", itemId);
				params.put("barcode", sku.getBarcode());
				params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
				List<ItemDetail> itemDetailList = itemDetailDao.queryByParam(params);
				if(CollectionUtils.isNotEmpty(itemDetailList)){
					ItemDetail dd = itemDetailList.get(0);
					prdid= dd.getPrdid();
					detailId = dd.getId();
				}
				
				boolean checkBarcode = itemManageService.checkBarcodeExsit(sku.getBarcode(), null,info.getId());
				//prdid规格校验
				int flag = itemManageService.checkPrdidSpec(info.getId(), specMap);
				if(checkBarcode){//不存在 
					if(flag!=1){
						throw new ItemServiceException( "商品系统中：Spu："+spu+"已经存在此规格");
					}else{
						ItemDetail detail = importDetail(info.getId(),categoryCode,info.getSpu(),sku,date,createUser);
						//sku,supplier
						prdid = detail.getPrdid();
						detailId = detail.getId();
						ItemSku retSku =  importSku(itemId,detailId,spu,prdid,categoryCode,sku,date,createUser);
						skuCode = retSku.getSku();
					}
				}else{
					if(flag==4){
						checkBarcode = itemManageService.checkBarcodeExsitInSku(detailId,sku.getBarcode(), null, sku.getSupplierId(), sku.getSaleType());
						if (!checkBarcode) {//存在
							throw new ItemServiceException( "商品系统中已经存在此条码:"
									+sku.getBarcode()+",供应商:"+sku.getSupplierName()+";\n");
						}else{
							ItemSku retSku =   importSku(itemId,detailId,spu,prdid,categoryCode,sku,date,createUser);
							skuCode = retSku.getSku();
						}
					}else{
						throw new ItemServiceException("商品系统中已经存在此条码在prdid中，但是规格不一样;\n");
					}
				}
			}
			//返回值
			excelImportSucessDto.setSpuCode(spu);
			excelImportSucessDto.setPrdidCode(prdid);
			excelImportSucessDto.setSkuCode(skuCode);
		} catch (Exception e) {
			resultInfo.setMsg(new FailInfo(e));
			LOGGER.error(e.getMessage(), e);
			excelImportSucessDto =null;
			throw new ItemServiceException(e.getMessage());
		}
		return resultInfo;
	}
	
	private ItemDetail importDetail(Long itemId,String categoryCode,String spu,SkuImportDto sku,Date date,String createUser){
		//插入detail
		ItemDetail itemDetail = new ItemDetail();
		String prdid = itemManageService.getPrdidCode(spu);
		itemDetail.setSpu(spu);
		itemDetail.setPrdid(prdid);
		itemDetail.setItemId(itemId);
		itemDetail.setSpuName(sku.getSpuName());
		
		if("supplier".equals(sku.getImportFrom())){//来源于供应商导入
			itemDetail.setSupplierId(sku.getSupplierId());
		}
		
		/**商品名称*/
		itemDetail.setMainTitle(sku.getSkuName());
		itemDetail.setSubTitle(sku.getSkuSubTitle());//热点
		itemDetail.setStorageTitle(sku.getSkuName()); //仓库商品名称
		itemDetail.setItemTitle(sku.getSkuName());
		
		
		itemDetail.setRemark(sku.getSkuRemark());
		itemDetail.setBarcode(sku.getBarcode());
		itemDetail.setItemType(sku.getItemType());
		itemDetail.setCreateUser(createUser);
		itemDetail.setCreateTime(date);
		itemDetail.setUpdateTime(date);
		itemDetail.setUpdateUser(createUser);
		
		itemDetail.setManufacturer(sku.getManufacturer());
		itemDetail.setBasicPrice(sku.getBasicPrice());
		
		itemDetail.setFreightTemplateId(sku.getFreightTemplateId());
		
		itemDetail.setVolumeHigh(sku.getVolumeHigh());
		itemDetail.setVolumeLength(sku.getVolumeLength());
		itemDetail.setVolumeWidth(sku.getVolumeWidth());
		itemDetail.setWeight(sku.getWeight());
		itemDetail.setWeightNet(sku.getWeightNet());
		
		
		itemDetail.setExpSign(sku.getExpSign());
		itemDetail.setExpDays(sku.getExpDays());
		itemDetail.setReturnDays(sku.getReturnDays());
		
		itemDetail.setWavesSign(sku.getWavesSign());
		itemDetail.setTarrifRate(sku.getTarrifRate());
		itemDetail.setCustomsRate(sku.getCustomsRateId());
		itemDetail.setExciseRate(sku.getExciseRateId());
		itemDetail.setAddedValueRate(sku.getAddedvalueRateId());
		itemDetail.setBrandId(sku.getBrandId());
		itemDetail.setUnitId(sku.getUnitId());
		itemDetail.setCategoryCode(categoryCode);
		
		itemDetail.setCartonSpec(sku.getCartonSpec());
		itemDetail.setSpecifications(sku.getSpecifications());
		itemDetail.setStatus(ItemStatusEnum.OFFLINE.getValue());
		//remark
		//适用年龄
		itemDetail.setApplyAgeId(sku.getApplyAgeId());
		
		itemDetail.setCountryId(sku.getCountryId());
		itemDetail.setSendType(sku.getSendType());
		
		//判断唯一
		ItemDetail queryItemDetail= new ItemDetail();
		queryItemDetail.setBarcode(itemDetail.getBarcode());
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("barcode", itemDetail.getBarcode());
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemDetail> detailList = itemDetailDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(detailList)){
			LOGGER.error("barcode不能重复,barcode为:{}",queryItemDetail.getBarcode());
			throw new ItemServiceException("barcode不能重复,barcode为:"+queryItemDetail.getBarcode());
		}
		itemDetailDao.insert(itemDetail);
		Long detailId= itemDetail.getId();
		//自定义属性
		List<ItemAttribute>  attrList = sku.getAttriList();
		if(attrList!=null && !attrList.isEmpty()){
			for(ItemAttribute a : attrList){
				a.setCustom(1);
				a.setItemId(itemId);
				a.setDetailId(detailId);
				itemAttributeDao.insert(a);
			}
			
		}
		//规格,导入约定是3个及以下
		ItemDetailSpec detailSpecDO1 = new ItemDetailSpec();
		detailSpecDO1.setDetailId(detailId);
		detailSpecDO1.setSpecId(sku.getSpec1Id());
		detailSpecDO1.setSpecGroupId(sku.getSpec1GroupId());
		detailSpecDO1.setCreateTime(date);
		detailSpecDO1.setUpdateTime(date);
		if(null!=sku.getSpec1Id()&&0!=sku.getSpec1Id()){
			detailSpecDO1.setSort(1);
			itemDetailSpecDao.insert(detailSpecDO1);
		}
		
		ItemDetailSpec detailSpecDO2 = new ItemDetailSpec();
		detailSpecDO2.setDetailId(detailId);
		detailSpecDO2.setSpecId(sku.getSpec2Id());
		detailSpecDO2.setSpecGroupId(sku.getSpec2GroupId());
		detailSpecDO2.setCreateTime(date);
		detailSpecDO2.setUpdateTime(date);
		if(null!=sku.getSpec2Id()&&0!=sku.getSpec2Id()){
			detailSpecDO2.setSort(2);
			itemDetailSpecDao.insert(detailSpecDO2);
		}
		
		ItemDetailSpec detailSpecDO3 = new ItemDetailSpec();
		detailSpecDO3.setDetailId(detailId);
		detailSpecDO3.setSpecId(sku.getSpec3Id());
		detailSpecDO3.setSpecGroupId(sku.getSpec3GroupId());
		detailSpecDO3.setCreateTime(date);
		detailSpecDO3.setUpdateTime(date);
		if(null!=sku.getSpec3Id()&&0!=sku.getSpec3Id()){
			detailSpecDO3.setSort(3);
			itemDetailSpecDao.insert(detailSpecDO3);
		}
		//均码
		if((null==sku.getSpec1Id())&&(null==sku.getSpec2Id())&&null==sku.getSpec3Id()){
			ItemDetailSpec detailSpecDO4 = new ItemDetailSpec();
			detailSpecDO4.setDetailId(detailId);
			detailSpecDO4.setSpecId(ItemConstant.FREE_SIZE_ID); 
			detailSpecDO4.setSpecGroupId(ItemConstant.FREE_SIZE_ID);
			detailSpecDO4.setCreateTime(date);
			detailSpecDO4.setUpdateTime(date);
			detailSpecDO4.setSort(1);
			itemDetailSpecDao.insert(detailSpecDO4);
		}
		
		ItemDetail detail = new ItemDetail();
		detail.setId(detailId);
		detail.setPrdid(prdid);
		return detail;
	}

	
	private ItemSku importSku(Long itemId,Long detailId,String spu,String prdid,String categoryCode ,
			SkuImportDto sku,Date d,String createUser) throws Exception{
		//插入sku
		String skuCode = "" ;
		Long skuId = null;
		ItemSku retItemSku = new ItemSku();
		if(sku.getSaleType()==ItemSaleTypeEnum.SELLER.getValue().intValue()){
			ItemSku itemSku = new ItemSku();
			skuCode = itemManageService.getSkuCode(prdid);
			itemSku.setSpuName(sku.getSpuName());
			itemSku.setSku(skuCode);
			itemSku.setSpu(spu);
			itemSku.setPrdid(prdid);
			itemSku.setItemId(itemId);
			itemSku.setDetailId(detailId);
			itemSku.setBarcode(sku.getBarcode());
			itemSku.setBasicPrice(sku.getBasicPrice());
			itemSku.setStatus(sku.getStatus());
			itemSku.setItemType(sku.getItemType());
			itemSku.setSaleType(sku.getSaleType());
			itemSku.setDetailName(sku.getSkuName());
			itemSku.setCreateUser(createUser);
			itemSku.setUpdateUser(createUser);
			itemSku.setCreateTime(d);
			itemSku.setUnitId(sku.getUnitId());
			itemSku.setBrandId(sku.getBrandId());
			itemSku.setCategoryCode(categoryCode);
			itemSku.setCategoryId(sku.getSmallId());
			itemSku.setSpId(sku.getSupplierId());
			itemSku.setSpName(sku.getSupplierName());
			itemSku.setSpCode(sku.getSupplierId().toString());
			itemSku.setSort(1);
			itemSkuDao.insert(itemSku);
			skuId = itemSku.getId();
			retItemSku.setSku(skuCode); 
		}else{
			//校验是否存在
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("saleType", ItemSaleTypeEnum.SEAGOOR.getValue());
			params.put("barcode", sku.getBarcode());
			params.put("detailId", detailId);
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			List<ItemSku> list = itemSkuDao.queryByParam(params);
			ItemSku itemSku = new ItemSku();
			itemSku.setSaleType(ItemSaleTypeEnum.SEAGOOR.getValue());
			itemSku.setBarcode(sku.getBarcode());
			itemSku.setDetailId(detailId);
			if(CollectionUtils.isNotEmpty(list)){
				//skuSupplierItem 
				retItemSku = list.get(0);
				skuId = retItemSku.getId();
				ItemSkuSupplier skuSupplierItem = new ItemSkuSupplier();
				skuSupplierItem.setCreateTime(d);
				skuSupplierItem.setSkuId(skuId);
				skuSupplierItem.setSupplierId(sku.getSupplierId());
				skuSupplierItem.setSupplierName(sku.getSupplierName());
				itemSkuSupplierDao.insert(skuSupplierItem);
			}else{
				skuCode = itemManageService.getSkuCode(prdid);
				retItemSku.setSku(skuCode);
				itemSku.setSku(skuCode);
				itemSku.setSpuName(sku.getSpuName());
				itemSku.setSpu(spu);
				itemSku.setPrdid(prdid);
				itemSku.setItemId(itemId);
				itemSku.setDetailId(detailId);
				itemSku.setBarcode(sku.getBarcode());
				itemSku.setBasicPrice(sku.getBasicPrice());
				itemSku.setStatus(sku.getStatus());
				itemSku.setItemType(sku.getItemType());
				itemSku.setSaleType(sku.getSaleType());
				itemSku.setDetailName(sku.getSkuName());
				itemSku.setCreateUser(createUser);
				itemSku.setUpdateUser(createUser);
				itemSku.setCreateTime(d);
				itemSku.setUnitId(sku.getUnitId());
				itemSku.setBrandId(sku.getBrandId());
				itemSku.setCategoryCode(categoryCode);
				itemSku.setCategoryId(sku.getSmallId());
//				if("supplier".equals(sku.getImportFrom())){//来源于供应商导入
				itemSku.setSpId(sku.getSupplierId());
				itemSku.setSpName(sku.getSupplierName());
				itemSku.setSpCode(sku.getSupplierCode());
//				}else{
//					itemSku.setSpId(ItemConstant.SUPPLIER_ID);
//					itemSku.setSpName(ItemConstant.SUPPLIER_NAME);
//					itemSku.setSpCode(ItemConstant.SUPPLIER_ID+"");
//				}
				
				
				itemSku.setSort(1);
				itemSkuDao.insert(itemSku);
				skuId = itemSku.getId();
				
				ItemSkuSupplier skuSupplierItem = new ItemSkuSupplier();
				skuSupplierItem.setCreateTime(d);
				skuSupplierItem.setSkuId(skuId);
				skuSupplierItem.setSupplierId(sku.getSupplierId());
				skuSupplierItem.setSupplierName(sku.getSupplierName());
				itemSkuSupplierDao.insert(skuSupplierItem);
			}
		}
		
		if(sku.getWavesSign() == 2){
			List<ItemSkuArt>  skuArtInfoList = sku.getSkuArtList();
			if(CollectionUtils.isNotEmpty(skuArtInfoList)){
				Map<String,Object> params = new HashMap<String,Object>();
				for(ItemSkuArt skuArt : skuArtInfoList){
					params.put("skuId", skuId);
					params.put("bondedArea", skuArt.getBondedArea());
					params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
					List<ItemSkuArt> skuArtList = itemSkuArtDao.queryByParam(params);
					if(CollectionUtils.isNotEmpty(skuArtList)){
						throw new Exception("sku存在相同的保税区,不能保存成功！");
					}
					ItemSkuArt skuArtDO = new ItemSkuArt();
					skuArtDO.setSku(skuCode);//sku号
					skuArtDO.setArticleNumber(skuArt.getArticleNumber());//商品备案号
					skuArtDO.setSkuId(skuId);//skuID
					skuArtDO.setBondedArea(skuArt.getBondedArea());// 通关渠道
					skuArtDO.setHsCode(skuArt.getHsCode());//HS编码
					skuArtDO.setItemDeclareName(skuArt.getItemDeclareName());//商品报关名称
					skuArtDO.setItemFeature(skuArt.getItemFeature());//商品特征
					itemSkuArtDao.insert(skuArtDO);
				}
				
			}
		}
		return retItemSku;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SkuImportLogDto saveImportLogDto(SkuImportLogDto skuImportLogDto)
			{
		Long logId = 0l;
		ItemImportLog itemImportLog = skuImportLogDto.getItemImportLog();
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
		if(CollectionUtils.isNotEmpty(skuImportLogDto.getImportList())){
			for(ItemImportList itemImportList : skuImportLogDto.getImportList()){
				itemImportList.setLogId(itemImportLog.getId());
				itemImportListDao.insert(itemImportList);
			}
		}
		return skuImportLogDto;
	}

	/** 
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ItemServiceException
	 */
	@Override
	public SkuImportLogDto querySkuImport(String createUser,int status, int pageNo,int pageSize) {
		SkuImportLogDto res = new SkuImportLogDto();
		//log主表
		ItemImportLog importLog = new ItemImportLog();
		importLog.setCreateUser(createUser);
		ItemImportLog itemImportLog = itemImportLogDao.selectByLastOne(importLog,ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		if(null!=itemImportLog){
			res.setItemImportLog(itemImportLog);
			//log子表
			Map<String,Object> params = new HashMap<String,Object>();
			if(status!=0){
				params.put("status", status);
			}
			params.put("logId", itemImportLog.getId());
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			Integer totalCout =  itemImportListDao.queryByParamCount(params);
			params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), pageNo <= 0?"0," + pageSize : (pageNo - 1)*pageSize + "," + pageSize);
			List<ItemImportList> list = itemImportListDao.queryPageByParam(params); //调用分页方法
			res.setTotalCount(totalCout.longValue());
			res.setImportList(list);
		}
		return res;
	}
	
	@Override
	public SkuImportLogDto querySkuImportById(Long logId,Integer status) {
		SkuImportLogDto res = new SkuImportLogDto();
		//log主表
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", logId);
		ItemImportLog itemImportLog = itemImportLogDao.queryByParam(params).get(0);
		if(null!=itemImportLog){
			res.setItemImportLog(itemImportLog);
			//log子表
			ItemImportList itemImportList = new ItemImportList();
			itemImportList.setLogId(logId);
			if(status != 0)
				itemImportList.setStatus(status);
			List <ItemImportList> importList = itemImportListDao.queryByObject(itemImportList);
			res.setImportList(importList);
		}
		return res;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long saveImportLog(ItemImportLog itemImportLog){
		Long res = 0l;
		switch(itemImportLog.getStatus()){
		case 1:
			//返回主键
			itemImportLogDao.insert(itemImportLog);
			res = itemImportLog.getId();
			break;
		default :
			//返回行
			res = Integer.valueOf(itemImportLogDao.updateNotNullById(itemImportLog)).longValue();
		}
		return res;
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer updateImportLogStatus(Long logId,int status)
			{
		ItemImportLog itemImportLog = new ItemImportLog();
		itemImportLog.setId(logId);
		itemImportLog.setStatus(status);
		return itemImportLogDao.updateNotNullById(itemImportLog);
	}

	/**
	 * 根据导入的excel修改SKU表等相关数据 
	 * basicPrice,sort,status等参数修改可单独修改SKU对应的数据
	 * 网站显示名,条形码,副标题等需要修改prd_item_detail表
	 **/
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Map<String, String>> importModifySku(List<ItemSkuModifyDto> list){
		if (CollectionUtils.isEmpty(list)) {
			return new ResultInfo<>(new HashMap<String, String>());
		}
		Map<String, String> resultMap = new HashMap<>();
		List<String> skuCodeList = new ArrayList<>();
		Map<String, ItemSkuModifyDto> mapModify = new HashMap<>();
		for (ItemSkuModifyDto dto : list) {
			if (StringUtil.isNotEmpty(dto.getSku()) && null == mapModify.get(dto.getSku())) {
				skuCodeList.add(dto.getSku());
				mapModify.put(dto.getSku(), dto);
			}
		}
		Map<String, ItemSku> skuCode2ItemMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(skuCodeList)) {
			//查询SKU
			Map<String, Object> params = new HashMap<>();			
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "sku in( " + StringUtil.join(skuCodeList, Constant.SPLIT_SIGN.COMMA) + ")");
			List<ItemSku> skuList = itemSkuDao.queryByParam(params);
			if (CollectionUtils.isNotEmpty(skuList)) {
				//构造查询列表和索引表	
				List<Long> detailIds = new ArrayList<>();
				for (ItemSku itemSku : skuList) {
					detailIds.add(itemSku.getDetailId());
					skuCode2ItemMap.put(itemSku.getSku(), itemSku);
				}
				//查询同一detailId的ItemSku
				params.clear();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in(" + StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA) + ")");	
				List<ItemSku> sameDetailIdSkuList = itemSkuDao.queryByParam(params);
				
				Map<Long, Boolean> detailId2OnLine = new HashMap<>();
				for (ItemSku itemSku : sameDetailIdSkuList) {
					if (1 == itemSku.getStatus()) {
						detailId2OnLine.put(itemSku.getDetailId(), Boolean.TRUE);
					}
				}
				for (ItemSku skuE : skuList) {
					ItemSkuModifyDto modifyDto = mapModify.get(skuE.getSku());
					if(null == modifyDto){
						continue;
					}
					//需要修改detail表的逻辑判断(若新增加参数,这里需增加复合判断)
					boolean isNeedUpdateDetail = false;
					if (StringUtil.isNotEmpty(modifyDto.getMainTitle()) || StringUtil.isNotEmpty(modifyDto.getDetailBasicPrice())) {
						isNeedUpdateDetail = true;
					}
					
					if (0 != skuE.getStatus()) {
						resultMap.put(skuE.getSku(), "商品已上架或者作废,不允许修改！");
						continue;
					}
					
					//若只修改sku的价格而不修改detail的数据则可以允许修改
					Boolean sameDetailIdOnline = detailId2OnLine.get(skuE.getDetailId());
					if (isNeedUpdateDetail && Boolean.TRUE == sameDetailIdOnline) {
						resultMap.put(skuE.getSku(), "同一PRD下商品已上架,不允许修改商品详情数据！");
						continue;
					}	

					ItemSku itemSku = new ItemSku();
					ItemDetail itemDetail = new ItemDetail();
					itemDetail.setId(skuE.getDetailId());
					itemSku.setId(skuE.getId());
					
					//网站标题修改
					if (StringUtil.isNotEmpty(modifyDto.getMainTitle())) {
						itemSku.setDetailName(modifyDto.getMainTitle());
						itemDetail.setMainTitle(modifyDto.getMainTitle());
					}
					//市场价修改
					if(StringUtil.isNotEmpty(modifyDto.getBasicPrice())){
						itemSku.setBasicPrice(Double.valueOf(modifyDto.getBasicPrice()));
					}
					//商品详情市场价修改
					if (StringUtil.isNotEmpty(modifyDto.getDetailBasicPrice())) {
						itemDetail.setBasicPrice(Double.valueOf(modifyDto.getDetailBasicPrice()));
					}
						
					try {
						if (isNeedUpdateDetail) {
							itemSkuDao.updateNotNullById(itemSku);
							itemDetailDao.updateNotNullById(itemDetail);
						}else {
							itemSkuDao.updateNotNullById(itemSku);
						}
					} catch (Exception e) {
						LOGGER.error("modify sku info error" + e.getMessage());
						resultMap.put(skuE.getSku(), "导入失败,异常信息：" + e.getMessage());
					}			
				}
			}	
		}
		//SKU不存在
		for (ItemSkuModifyDto dto : list) {
			if (null == skuCode2ItemMap.get(dto.getSku())) {
				resultMap.put(dto.getSku(), "SKU不存在");
			}
		}
		return new ResultInfo<>(resultMap);
	}
}
