package com.tp.service.prd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dao.prd.ItemAttributeDao;
import com.tp.dao.prd.ItemCodeDao;
import com.tp.dao.prd.ItemDescDao;
import com.tp.dao.prd.ItemDescMobileDao;
import com.tp.dao.prd.ItemDetailDao;
import com.tp.dao.prd.ItemDetailSpecDao;
import com.tp.dao.prd.ItemInfoDao;
import com.tp.dao.prd.ItemPicturesDao;
import com.tp.dao.prd.ItemSkuArtDao;
import com.tp.dao.prd.ItemSkuDao;
import com.tp.dao.prd.ItemSkuSupplierDao;
import com.tp.datasource.ContextHolder;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.Option;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.DetailDto;
import com.tp.dto.prd.DetailSkuDto;
import com.tp.dto.prd.ItemCopyDto;
import com.tp.dto.prd.ItemDto;
import com.tp.dto.prd.ItemOpenSaveDto;
import com.tp.dto.prd.enums.ItemDataSourceTypeEnum;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.prd.enums.SellerItemAuditTypeEnum;
import com.tp.dto.prd.enums.SellerItemBindLevelEnum;
import com.tp.dto.prd.mq.PromotionItemMqDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemCode;
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
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.prd.IItemManageService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ItemManageService implements IItemManageService {

	private static final Logger  LOGGER = LoggerFactory.getLogger(ItemManageService.class);

	@Autowired
	private ItemInfoDao itemInfoDao;
	@Autowired
	private ItemDetailDao itemDetailDao;
	@Autowired
	private ItemSkuDao itemSkuDao;
	
	@Autowired
	private ItemPicturesDao itemPicturesDao;
	@Autowired
	private ItemDescDao itemDescDao;
	@Autowired
	private ItemDescMobileDao itemDescMobileDao;
	@Autowired
	private ItemDetailSpecDao itemDetailSpecDao ;
	@Autowired
	private ItemCodeDao itemCodeDao;
	@Autowired
	private ItemSkuSupplierDao itemSkuSupplierDao;
	@Autowired
	private ItemAttributeDao itemAttributeDao;
	@Autowired
	private ItemSkuArtDao itemSkuArtDao;
	
	@Autowired
	private ISupplierInfoService supplierInfoService;
	
	@Autowired
	RabbitMqProducer rabbitMqProducer;
	
	@Autowired
	private IItemInfoService itemInfoService;
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Long saveItem(ItemDto item,String createUser){
		// 创建日期应该调用的服务器的时间？？
		ItemInfo itemInfo = null;
		itemInfo = item.getItemInfo();
		List<ItemDetail> itemDetailList = item.getItemDetailList();
		Long itemId = null;
		// 修改商品
		if(null!=itemInfo.getId()){
			updateItem(item,createUser);
			return itemInfo.getId();
		}
		String spu = getSpuCode(itemInfo.getSmallCode());
		itemInfo.setSpu(spu);
		itemInfo.setCreateUser(createUser);
		itemInfoDao.insert(itemInfo);
		itemId = itemInfo.getId();
		// 获取新增detail列表
		if (CollectionUtils.isNotEmpty(itemDetailList)) {
			for (ItemDetail itemDetail : itemDetailList) {
				if(StringUtils.isNotBlank(itemDetail.getBarcode())){
					boolean checkFlag = checkBarcodeExsit(itemDetail.getBarcode(),null,null);
					if(!checkFlag){
						LOGGER.error( "商品系统中已经存在此条码");
						throw new ItemServiceException( "商品系统中已经存在此条码");
					}
					itemDetail.setSupplierId(itemInfo.getSupplierId());//供应商ID（供应商系统商品编辑用）
					itemDetail.setItemId(itemId);
					itemDetail.setSpu(spu);
					itemDetail.setSpuName(itemInfo.getMainTitle());
					String prdId = getPrdidCode(spu);
					itemDetail.setPrdid(prdId);
					itemDetail.setBrandId(itemInfo.getBrandId());
					itemDetail.setUnitId(itemInfo.getUnitId());
					itemDetail.setStatus(ItemStatusEnum.OFFLINE.getValue());
					itemDetail.setCategoryCode(itemInfo.getSmallCode());
					itemDetail.setCreateUser(createUser);
					itemDetailDao.insert(itemDetail);
					Long detailId = itemDetail.getId();
					
					String specGroupIds = itemDetail.getSpecGroupIds();
					if(StringUtils.isNotBlank(specGroupIds)){
						String [] str = specGroupIds.split(ItemConstant.DEFAULT_SEPARATOR);
						for(String s : str){
							String [] ids = s.split(ItemConstant.DEFAULT_JOIN);
							ItemDetailSpec itemDetailSpec = new ItemDetailSpec();
							itemDetailSpec.setSort(1);
							itemDetailSpec.setDetailId(detailId);
							itemDetailSpec.setCreateTime(new Date());
							itemDetailSpec.setUpdateTime(new Date());
							itemDetailSpec.setSpecGroupId(Long.parseLong(ids[0]));
							itemDetailSpec.setSpecId(Long.parseLong(ids[1]));
							itemDetailSpecDao.insert(itemDetailSpec);
						}
						
					}else{//均码处理
						ItemDetailSpec detailSpec = new ItemDetailSpec();
						detailSpec.setSort(1);
						detailSpec.setDetailId(detailId);
						detailSpec.setCreateTime(new Date());
						detailSpec.setUpdateTime(new Date());
						detailSpec.setSpecGroupId(ItemConstant.FREE_SIZE_ID);
						detailSpec.setSpecId(ItemConstant.FREE_SIZE_ID);
						itemDetailSpecDao.insert(detailSpec);
					}
				}
				
			}
		}
		return itemId;
	}
	
	/**
	 * 
	 * <pre>
	 *  更新商品
	 * </pre>
	 *
	 * @param item
	 * @
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void updateItem(ItemDto item,String createUser){
		//商品SPU纬度只能修改
		//修改时间，修改人，单位， 中文名称，品牌，展示名称，副标题
		ItemInfo itemInfo = new ItemInfo();
		itemInfo.setLargeId(item.getItemInfo().getLargeId());
		itemInfo.setMediumId(item.getItemInfo().getMediumId());
		itemInfo.setSmallId(item.getItemInfo().getSmallId());
		itemInfo.setMainTitle(item.getItemInfo().getMainTitle());
		itemInfo.setRemark(item.getItemInfo().getRemark());
		itemInfo.setUnitId(item.getItemInfo().getUnitId());
		itemInfo.setBrandId(item.getItemInfo().getBrandId());
		itemInfo.setUpdateTime(new Date());
		itemInfo.setUpdateUser(createUser);
		itemInfo.setId(item.getItemInfo().getId());
		itemInfoDao.updateNotNullById(itemInfo);
		//修改prdid纬度信息
		List<ItemDetail> itemDetailList = item.getItemDetailList();
		if (CollectionUtils.isNotEmpty(itemDetailList)) {
			for (ItemDetail itemDetail : itemDetailList) {
				ItemDetail detail = new ItemDetail();
				detail.setId(itemDetail.getId());
				detail.setBrandId(itemDetail.getBrandId());
				detail.setUnitId(itemDetail.getUnitId());
				detail.setBarcode(itemDetail.getBarcode());
				detail.setMainTitle(itemDetail.getMainTitle());
				detail.setRemark(itemInfo.getRemark());
				detail.setCreateUser(createUser);
				itemDetailDao.updateNotNullById(detail);
			}
		}
	}
	
	@Override
	public ItemDto getItemByItemId(Long itemId,int type ){
		ItemDto itemDto = new ItemDto();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", itemId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		ItemInfo itemInfo = itemInfoDao.queryByParam(params).get(0);
		itemDto.setItemInfo(itemInfo);
		if(type==1){
			return itemDto;
		}
		params.put("itemId", itemId);
		params.remove("id");
		List<ItemDetail> itemDetailList = itemDetailDao.queryByParam(params);
		itemDto.setItemDetailList(itemDetailList);
		return itemDto;
	}
	
	
	/************************************************SKU纬度方法***********************************************************/
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Long saveItemDetail(ItemInfo info,DetailDto detailDto,String createUser)  {
		Long detailId =null;
		int res = 0;
		Long itemId = null;
		//  创建日期应该调用的服务器的时间？
		ItemDetail itemDetail = detailDto.getItemDetail();
		detailId = itemDetail.getId();
		List<DetailSkuDto> skuList = detailDto.getDetailSkuList();
		//更新info里面的spu名称与备注
		itemInfoDao.updateNotNullById(info);
		//给itemId赋值
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		params.put("id", detailId);
		itemId = itemDetailDao.queryByParam(params).get(0).getItemId();
		itemDetail.setUpdateTime(new Date());
		itemDetail.setBrandId(info.getBrandId());
		itemDetail.setUnitId(info.getUnitId());
		itemDetail.setCategoryCode(info.getSmallCode());
		itemDetail.setSpuName(info.getMainTitle());
		itemDetail.setUpdateUser(createUser);
		//获取 是否海淘商品,1-否，2-是，默认1
		if(itemDetail.getWavesSign().intValue()==1){
			itemDetail.setTarrifRate(null);
			itemDetail.setCountryId(null);
			itemDetail.setSendType(null);
		}
		//获取 是否保质期管理:1 是，2-否 默认1
		if(itemDetail.getExpSign().intValue()==2){
			itemDetail.setExpDays(null);
		}
		res =  itemDetailDao.updateNotNullById(itemDetail);
		//强制更新
		try {
			if(StringUtils.isBlank(itemDetail.getManufacturer())){
				itemDetail.setManufacturer(null);
			}
			if(StringUtils.isBlank(itemDetail.getSpecifications())){
				itemDetail.setSpecifications(null);   
			}
			
			//res = itemDetailDao.updateCommonInfo(detail);
			} catch (Exception e) {
				LOGGER.error("保存商品常规信息出错",e.getMessage()); 
		}
		if(res<=0){
			throw new ItemServiceException("保存商品失败了");
		}
		//保存商品信息
		saveItemDetail(itemId, detailId, detailDto,createUser);
		//查询info,detail表冗余到sku中去 
		params.clear();
		params.put("id", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		ItemDetail ItemDetail = itemDetailDao.queryByParam(params).get(0);
		params.put("id", itemId);
		ItemInfo itemInfo = itemInfoDao.queryByParam(params).get(0);
		// 获取skuId
		Date cur = new Date();
		if (skuList != null) {
			for (DetailSkuDto detailSkuDto : skuList) {
				// 如果sku有id 说明只能更新SKU的价格，商品号 ，上下架
				if(null != detailSkuDto.getId()){
					ItemSku  itemSku = new ItemSku();
					itemSku.setId(detailSkuDto.getId());
					itemSku.setStatus(detailSkuDto.getStatus());
					itemSku.setBasicPrice(detailSkuDto.getBasicPrice());
					itemSku.setSort(detailSkuDto.getSort());
					itemSku.setBarcode(itemDetail.getBarcode());
					itemSku.setSpuName(info.getMainTitle());
					itemSku.setUpdateTime(cur);
					itemSku.setUpdateUser(createUser);
					if(info.getSupplierId()!=null ){
						itemSku.setSpId(info.getSupplierId());
					}
					itemSkuDao.updateNotNullById(itemSku);
				}else{
					ItemSku  itemSku = new ItemSku();
					
					itemSku.setItemId(itemId);
					itemSku.setSpuName(itemInfo.getMainTitle());
					itemSku.setBrandId(itemInfo.getBrandId());
					itemSku.setSpu(itemInfo.getSpu());
					itemSku.setUnitId(itemInfo.getUnitId());
					
					itemSku.setItemType(ItemDetail.getItemType());
					itemSku.setPrdid(ItemDetail.getPrdid());
					itemSku.setDetailId(detailId);
					itemSku.setCategoryCode(ItemDetail.getCategoryCode());
					itemSku.setCategoryId(itemInfo.getSmallId());
					itemSku.setDetailName(ItemDetail.getMainTitle());
					itemSku.setBarcode(itemDetail.getBarcode());
					String skuCode = getSkuCode(ItemDetail.getPrdid());
					itemSku.setSku(skuCode);
					itemSku.setStatus(detailSkuDto.getStatus());
					itemSku.setSaleType(detailSkuDto.getSaleType());
					//itemSku.setSaleType(detailSkuDto.getSaleType());
					itemSku.setSpId(detailSkuDto.getSpId());
					itemSku.setSpCode(detailSkuDto.getSpCode());
					itemSku.setSpName(detailSkuDto.getSpName());
					itemSku.setBasicPrice(detailSkuDto.getBasicPrice());
					itemSku.setSort(detailSkuDto.getSort());
					itemSku.setCreateTime(cur);
					itemSku.setUpdateTime(cur);
					itemSku.setCreateUser(createUser);
					itemSku.setUpdateUser(createUser);
					if(info.getSupplierId()!=null ){
						itemSku.setSpId(info.getSupplierId());
					}
					itemSkuDao.insert(itemSku);
					Long skuId = itemSku.getId();
					String skuSupplierList = detailSkuDto.getSkuSupplierList();
					if(StringUtils.isNotBlank(skuSupplierList)){
						String temp =skuSupplierList.replaceAll("\\\\","");
						ItemSkuSupplier[] skuSupplierArray= (ItemSkuSupplier[]) JSONArray.toArray(JSONArray.fromObject(temp),ItemSkuSupplier.class);
						for(ItemSkuSupplier skuSupplierInfo : skuSupplierArray){
							skuSupplierInfo.setCreateTime(new Date());
							skuSupplierInfo.setSkuId(skuId);
							itemSkuSupplierDao.insert(skuSupplierInfo);
						}
					}
				}
			}
			params.clear();
			params.put("id", detailId);
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			ItemDetail itemDetailResult = itemDetailDao.queryByParam(params).get(0);
			//更新sku名称
			updateSkuByDetailId(itemDetailResult,info.getSmallId());
			//更新detail是否上下架
			updateDetailStatus(detailId);
			//更新prd
			updatePrdInfoByItemId(info,createUser);
			info.setSmallCode(itemDetailResult.getCategoryCode());
			updateSkuByItemId(info);
			//发送消息
		//	send2PromotionMessage(detailId,itemDetailResult);
		}
		return detailId;
	}
	/**
	 * 
	 * <pre>
	 * 	   保存商品详情，图片，属性
	 * </pre>
	 *
	 * @param detailDto
	 * @throws DAOException 
	 */
	private void saveItemDetail(Long itemId,Long detailId,DetailDto detailDto,String createUser){
		//保存详情
		ItemDesc itemDesc =new ItemDesc();
		itemDesc.setDetailId(detailId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemDesc> itemDescList = itemDescDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(itemDescList)){
			itemDescDao.deleteById(itemDescList.get(0).getId());
		}
		ItemDesc desc = detailDto.getItemDesc();
		desc.setItemId(itemId);
		desc.setDetailId(detailId);
		desc.setCreateUser(createUser);
		desc.setUpdateUser(createUser);
		desc.setCreateTime(new Date());
		desc.setUpdateTime(new Date());
		itemDescDao.insert(desc);
		List<ItemDescMobile> descMobileList = itemDescMobileDao.queryByParam(params);
		if(descMobileList!= null && !descMobileList.isEmpty()){
			itemDescMobileDao.deleteById(descMobileList.get(0).getId());
		}
		ItemDescMobile descMobile = detailDto.getItemDescMobile();
		descMobile.setItemId(itemId);
		descMobile.setDetailId(detailId);
		descMobile.setCreateUser(createUser);
		descMobile.setUpdateUser(createUser);
		descMobile.setCreateTime(new Date());
		descMobile.setUpdateTime(new Date());
		itemDescMobileDao.insert(descMobile);
		//图片
		ItemPictures picture =new ItemPictures();
		picture.setDetailId(detailId);
		List<ItemPictures> pictureList =itemPicturesDao.queryByParam(params);
		params.remove(DAOConstant.DATA_SOURCE_KEY);
		if(CollectionUtils.isNotEmpty(pictureList)){
			itemPicturesDao.deleteByParam(params);
		}
		List<String> picList = detailDto.getPicList();
		int flag = 0;//第一张主图了
		if(CollectionUtils.isNotEmpty(picList)){
			for(String pic : picList){
				ItemPictures itemPictures = new ItemPictures();
				itemPictures.setPicture(pic);
				itemPictures.setDetailId(detailId);
				itemPictures.setItemId(itemId);
				itemPictures.setCreateTime(new Date());
				itemPictures.setCreateUser(createUser);
				if(flag==0){
					itemPictures.setMain(Constant.DEFAULTED.YES);	
				}else{
					itemPictures.setMain(Constant.DEFAULTED.NO);
				}
				itemPicturesDao.insert(itemPictures);
				flag++;
			}
		}
		
		//保存属性
		//先删除，再添加
		itemAttributeDao.deleteByParam(params);
		List<ItemAttribute> attrItemList = detailDto.getAttrItemList();
		for(ItemAttribute attr : attrItemList){
			if(StringUtils.isNotBlank(attr.getAttrValueIds())){
				String [] str = attr.getAttrValueIds().split(ItemConstant.DEFAULT_SEPARATOR);
				for(String s : str){
					ItemAttribute a = new ItemAttribute();
					a.setItemId(itemId);
					a.setCustom(ItemConstant.ATTR_FROM_BASEDATA);
					a.setDetailId(detailId);
					a.setAttrValueId(Long.parseLong(s));
					a.setAttrId(attr.getAttrId());
					a.setCreateTime(new Date());
					a.setUpdateTime(new Date());
					itemAttributeDao.insert(a);	
				}
			}
		}
		List<ItemAttribute> attrList = detailDto.getAttrList();
		for(ItemAttribute attr : attrList){
			attr.setItemId(itemId);
			attr.setCustom(ItemConstant.ATTR_CUSTOM);
			attr.setDetailId(detailId);
			attr.setCreateTime(new Date());
			attr.setUpdateTime(new Date());
			itemAttributeDao.insert(attr);
		}
		//服务商品属性 用custome=2 区分~~
		for(ItemAttribute attr: detailDto.getDummyAttrList()){
			attr.setItemId(itemId);
			attr.setCustom(ItemConstant.ATTR_DUMMY);
			attr.setDetailId(detailId);
			attr.setCreateTime(new Date());
			attr.setUpdateTime(new Date());
			itemAttributeDao.insert(attr);
		}

	}
    
    @SuppressWarnings("unused")
	private void saveItemPics(List<String> picsList,Long itemId,Long detailId){
    	//第一个图片为主图
    	int i = 0; 
    	if(CollectionUtils.isNotEmpty(picsList)){
    		for(String picture : picsList){
        		ItemPictures pic = new ItemPictures();
        		if(i==0){
        			pic.setMain(Constant.DEFAULTED.YES);
        		}else{
        			pic.setMain(Constant.DEFAULTED.NO);
        		}
        		pic.setPicture(picture);
        		pic.setDetailId(detailId);
        		pic.setItemId(itemId);
        		pic.setCreateUser("系统");
        		i++;
        		itemPicturesDao.insert(pic);
        	}
    	}
    }

	@Override
	public ItemDetail getItemDetailByDetailId(Long detailId) {
		return itemDetailDao.queryById(detailId);
	}

	@Override
	public ItemDesc getDescByDetailId(Long detailId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemDesc> list = itemDescDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public ItemDescMobile geteDescMobilByDetailId(Long detailId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		List<ItemDescMobile> list = itemDescMobileDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ItemPictures> getPicsByDetailId(Long detailId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		return itemPicturesDao.queryByParam(params);
	}

	@Override
	public List<ItemSku> getSkuListByDetailId(Long detailId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		return itemSkuDao.queryByParam(params);
	}

	
	@Override
	public String getSpuCode(String smallCode)  {
		return getUniqueCode(smallCode,1);
	}
	
	@Override
	public String getPrdidCode(String spuCode)  {
		return getUniqueCode(spuCode,2);
	}

	@Override
	public String getSkuCode(String prdIdCode)  {
		return getUniqueCode(prdIdCode,3);
	}
	
	
	
	@Override
	public List<ItemSku> getDetailSkuListByDetailId(Long detailId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "sort asc,sp_id asc");
		return itemSkuDao.queryByParam(params);
	}
	

	@Override
	public List<ItemSkuSupplier> getSkuSupplierListBySkuId(Long skuId)  {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", skuId);
		return  itemSkuSupplierDao.queryByParam(params);
	}


	@Override
	public List<ItemSku> getSkuDetailListByDetailIds(List<Long> detailIds) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.DEFAULTED.YES);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+StringUtil.join(detailIds,Constant.SPLIT_SIGN.COMMA)+")");
		return itemSkuDao.queryByParam(params);
	}


	@Override
	public List<ItemAttribute> getAttributeByDetailId(Long detailId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		return itemAttributeDao.queryByParam(params);
	}
	
	/**
	 * 
	 * <pre>
	 *  生成唯一编码：spu，prdid，sku
	 * 	type:1 获取spu 
	 *  type:2 获取prdid
	 *  type:3获取sku纬度 
	 * </pre>
	 *
	 * @param smallCode
	 * @param type
	 * @return
	 * @
	 */
	private String getUniqueCode(String code,int type)  {
		
		String errorMsg = "";
		if(type==1){
			errorMsg = "获取SPU编码出错";
		}else if(type==2){
			errorMsg = "获取prdid编码出错"; 
		}else {
			errorMsg = "获取SKU编码出错"; 
		}
		
		int bits = 0;
		if(type==1){
			bits = 4 ;
		}else if(type==2){
			bits =2 ;
		}else{
			bits =2 ;
		}
		
		if(StringUtil.isBlank(code)){
			throw new ItemServiceException(errorMsg);
		}
		ItemCode codeDO = new ItemCode();
		codeDO.setCode(code);
		int value = 0;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", code);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemCode> list  =itemCodeDao.queryByParam(params);
		if(CollectionUtils.isEmpty(list)){
			codeDO.setValue(ItemConstant.CODE_INIT_VALUE);
			params.put("value", ItemConstant.CODE_INIT_VALUE);
			itemCodeDao.insert(codeDO);
		}else{
			itemCodeDao.updateCode(code);
		}
		List<ItemCode> listCode  =itemCodeDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(listCode)){
			value = listCode.get(0).getValue();
		}else{
			throw new ItemServiceException("获取商品编码失败");
		}
		return getCode(code,value,bits);
	}

	/**
	 * 
	 * <pre>
	 * 	  获取生成的编码
	 * </pre>
	 *
	 * @param code
	 * @param value
	 * @param bits
	 * @return
	 */
	private String getCode(String code,int value,int bits){
		String res = code;
		int length = (value+"").length();
		StringBuffer s = new StringBuffer();
		if(length  <= bits){
			for(int i = 0 ; i< bits-length;i++){
				s.append("0");
			}
			res+=s.toString()+value;
		}else{
			res+=value;
		} 
		return res;
	}
	
	/**
	 * 
	 * <pre>
	 *  barcode 唯一判断
	 * </pre>
	 *
	 * @param barcode
	 * @param itemId
	 * @param detailId
	 * @return boolean
	 */
	@Override
	public boolean checkBarcodeExsit(String barcode,Long detailId,Long itemId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("barcode", barcode);
		if(null!=itemId){
			params.put("itemId", itemId);
		}
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemDetail> list = itemDetailDao.queryByParam(params);
		if(CollectionUtils.isEmpty(list)){
			return true;
		}else{
			if(list.size()>1){
				LOGGER.debug("barcode :{} {} ",barcode ,"在系统中出现了重复，请联系管理员");
				return false;
			}else if(list.size()==1){//修改的时判断重复
				if(list.get(0).getId().equals(detailId)){
					return true;
				}else{
					LOGGER.debug("barcode: {} {}", barcode ,"在系统中出现了重复，请联系管理员");
					return false;
				}
			}
			
		}
		return false;
	}

	@Override
	public boolean checkBarcodeExsitInSku(Long detailId,String barcode, Long skuId,Long supplierId,int saleType){
		//返回true 代表不存在
		//返回false 代表已经存在
		ItemSku itemSku = new ItemSku();
		itemSku.setBarcode(barcode);
		itemSku.setDetailId(detailId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("barcode", barcode);
		params.put("detailId", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemSku> list = itemSkuDao.queryByParam(params);
		if(CollectionUtils.isEmpty(list)){
			return true;
		}else{
			Long xgSkuId = null;
			List<Long> spIds = new ArrayList<Long>();
			for(ItemSku sku : list){
				if(sku.getSaleType().intValue()==ItemSaleTypeEnum.SEAGOOR.getValue().intValue()){
					xgSkuId = sku.getId();
				}else{
					spIds.add(sku.getSpId());
				}
			}
			if(saleType== ItemSaleTypeEnum.SEAGOOR.getValue().intValue()){
				if(null==xgSkuId){
					return true;
				}
				params.remove("barcode");
				params.remove("detailId");
				params.put("skuId", xgSkuId);
				params.put("supplierId", supplierId);
				List<ItemSkuSupplier> skuSupplierList = itemSkuSupplierDao.queryByParam(params);
				if(CollectionUtils.isNotEmpty(skuSupplierList)){
					LOGGER.error("barcode :{},spplierId:  {}, skuId:{} ",barcode,supplierId ,skuId,"在系统中出现了重复，请联系管理员");
					return false;
				}else{
					return true;
				}
				
			}else{//联营
				if(spIds.indexOf(supplierId)==-1){
					return true;
				}else{
					LOGGER.error("barcode :{},spplierId:  {}, skuId:{} ",barcode,supplierId ,skuId,"在系统中出现了重复，请联系管理员");
					return false;
				}
			}
			
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void copyItem(String detailIds,String createUser){
		if(StringUtil.isBlank(detailIds)){
			LOGGER.error("请选择好商品再复制");
			throw new ItemServiceException("请选择好商品再复制");
		}
		String [] detailArray = detailIds.split(",");
		if(null == detailArray || detailArray.length==0){
			LOGGER.error("请选择好商品再复制");
			throw new ItemServiceException("请选择好商品再复制");
		}
		for(String str: detailArray){
			Long detailId = Long.parseLong(str);
			//复制商品
			copyOneDetailItem(detailId,createUser);
		}
		
	}
	
	/**
	 * 
	 * <pre>
	 * 	复制一个商品
	 * </pre>
	 *
	 * @param detailId
	 * @param createUser
	 */
	private void copyOneDetailItem(Long srcDetailId,String createUser){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		ItemDetail detail = itemDetailDao.queryById(srcDetailId);
		params.put("detailId", srcDetailId);
		List<ItemSku> skuList = itemSkuDao.queryByParam(params);
		ItemInfo info = itemInfoDao.queryById(detail.getItemId());
		
		info.setId(null);
		detail.setId(null);
		Date d = new Date();
		String spu = getSpuCode(detail.getCategoryCode());
		info.setSpu(spu);
		info.setCreateTime(d);
		info.setUpdateTime(d);
		info.setUpdateUser(createUser);
		info.setCreateUser(createUser);
		itemInfoDao.insert(info);
		Long itemId = info.getId();
		String prdid = getPrdidCode(spu);
		detail.setPrdid(prdid);
		detail.setItemId(itemId);
		detail.setCreateTime(d);
		detail.setCreateUser(createUser);
		detail.setUpdateTime(d);
		detail.setUpdateUser(createUser);
		detail.setBarcode(null);//复制时候，barcode默认为空
		itemDetailDao.insert(detail);
		Long detailId = detail.getId();
		List<ItemDesc> descList = itemDescDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(descList)){
			for(ItemDesc desc: descList){
				desc.setId(null);
				desc.setDetailId(detailId);
				desc.setItemId(itemId);
				desc.setCreateUser(createUser);
				desc.setCreateTime(d);
				desc.setUpdateUser(createUser);
				desc.setUpdateTime(d);
				itemDescDao.insert(desc);
			}
			
		}
		
		List<ItemDescMobile> descMobileList = itemDescMobileDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(descMobileList)){
			for(ItemDescMobile descMobile: descMobileList){
				descMobile.setId(null);
				descMobile.setDetailId(detailId);
				descMobile.setItemId(itemId);
				descMobile.setCreateUser(createUser);
				descMobile.setCreateTime(d);
				descMobile.setUpdateUser(createUser);
				descMobile.setUpdateTime(d);
				itemDescMobileDao.insert(descMobile);
			}
			
		}
		List<ItemAttribute> attributeList= itemAttributeDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(attributeList)){
			for(ItemAttribute attribute: attributeList){
				attribute.setId(null);
				attribute.setDetailId(detailId);
				attribute.setItemId(itemId);
				itemAttributeDao.insert(attribute);
			}
			
		}
		List<ItemDetailSpec> detailSpecList= itemDetailSpecDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(detailSpecList)){
			for(ItemDetailSpec detailSpec: detailSpecList){
				detailSpec.setId(null);
				detailSpec.setDetailId(detailId);
				detailSpec.setCreateTime(d);
				detailSpec.setUpdateTime(d);
				itemDetailSpecDao.insert(detailSpec);
			}
			
		}
		List<ItemPictures> picturesList =  itemPicturesDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(picturesList)){
			for(ItemPictures pictures: picturesList){
				pictures.setId(null);
				pictures.setDetailId(detailId);
				pictures.setItemId(itemId);
				pictures.setCreateTime(d);
				pictures.setCreateUser(createUser);
				itemPicturesDao.insert(pictures);
			}
		}
		if(CollectionUtils.isNotEmpty(skuList)){
			for(ItemSku sku: skuList){
				Long srcSkuId = sku.getId()	;
				sku.setId(null);
				String skuCode = getSkuCode(prdid);
				sku.setSku(skuCode);
				sku.setItemId(itemId);
				sku.setDetailId(detailId);
				sku.setCreateTime(d);
				sku.setCreateUser(createUser);
				sku.setUpdateUser(createUser);
				sku.setBarcode(null);//复制时候，barcode默认为空
				itemSkuDao.insert(sku);
				Long skuId = sku.getId();
				ItemSkuSupplier skuSupplierInfo = new ItemSkuSupplier();
				skuSupplierInfo.setSkuId(srcSkuId);
				params.remove("detailId");
				params.put("skuId", srcSkuId);
				List<ItemSkuSupplier> skuSupplierList = itemSkuSupplierDao.queryByParam(params);
				if(null!=skuSupplierList&&!skuSupplierList.isEmpty()){
					for(ItemSkuSupplier skuSupplier: skuSupplierList){
						skuSupplier.setId(null);
						skuSupplier.setSkuId(skuId);
						skuSupplier.setCreateTime(d);
						itemSkuSupplierDao.insert(skuSupplier);
					}
				}
			}
		}
	}

	@Override
	public List<ItemDetailSpec> getDetailSpecListByDetailId(Long detailId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		return itemDetailSpecDao.queryByParam(params);
	}


	@Override
	public Integer cancelSku(Long skuId, String createUser){
		if(null == skuId){
			LOGGER.error("作废sku出错:skuId必填");
			return 0;
		}
		ItemSku itemSku = new ItemSku();
		itemSku.setId(skuId);
		itemSku.setStatus(ItemStatusEnum.CANCEL.getValue());
		itemSku.setUpdateUser(createUser);
		Integer count = 0 ;
		count = itemSkuDao.updateNotNullById(itemSku);
		if(count<1){
			LOGGER.error("作废sku出错:没有更新到记录行");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		params.put("id", skuId);
		itemSku= itemSkuDao.queryByParam(params).get(0);
		updateDetailStatus(itemSku.getDetailId());
		return count;
	}


	@Override
	public Integer addSkuSupplier(Long skuId, String skuSupplierList){
		Integer count = 0 ; 
		if(StringUtils.isNotBlank(skuSupplierList)){
			String temp =skuSupplierList.replaceAll("\\\\","");
			ItemSkuSupplier[] skuSupplierArray= (ItemSkuSupplier[]) JSONArray.toArray(JSONArray.fromObject(temp),ItemSkuSupplier.class);
			count = skuSupplierArray.length;
			for(ItemSkuSupplier skuSupplierInfo : skuSupplierArray){
				skuSupplierInfo.setCreateTime(new Date());
				skuSupplierInfo.setSkuId(skuId);
				itemSkuSupplierDao.insert(skuSupplierInfo);
			}
		}
		
		return count;
	}


	@Override
	public Integer deleteSkuSupplier(Long skuSupplierId) {
		return itemSkuSupplierDao.deleteById(skuSupplierId);
	}


	@Override
	public List<ItemInfo> getInfoListByIds(List<Long> itemIds){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(itemIds, Constant.SPLIT_SIGN.COMMA)+")");
		return itemInfoDao.queryByParam(params);
	}
	
	@Override
	public List<ItemDetail> selectByDetailIds(List<Long> detailIds){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
		return itemDetailDao.queryByParam(params);
	}
	
	/**
	 * 
	 * <pre>
	 *  更新detail状态
	 * </pre>
	 *
	 * @param detailId
	 * @throws DAOException
	 */
	private void updateDetailStatus(Long detailId){
		//更新detail是否上下架
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		params.put("detailId", detailId);
		List<ItemSku> list = itemSkuDao.queryByParam(params);
		Integer status = ItemStatusEnum.OFFLINE.getValue();
		if (CollectionUtils.isNotEmpty(list)) {
			int cancelCount = 0 ; 
			for (ItemSku sku : list) {
				if (sku.getStatus().equals(ItemStatusEnum.ONLINE.getValue())) {
					status= ItemStatusEnum.ONLINE.getValue();
					break;
				}else if(sku.getStatus().equals(ItemStatusEnum.CANCEL.getValue())){
					cancelCount ++;
				}
			}
			if(cancelCount!=0&&cancelCount==list.size()){
				status= ItemStatusEnum.CANCEL.getValue();
			}
			
		} else {
			status = ItemStatusEnum.OFFLINE.getValue();
		}
		
		ItemDetail changeStatus = new ItemDetail();
		changeStatus.setId(detailId);
		changeStatus.setStatus(status);
		itemDetailDao.updateNotNullById(changeStatus);
	}
	
	/**
	 * 
	 * <pre>
	 *  批量更新
	 * </pre>
	 *
	 * @param detailId
	 * @param smallId
	 * @throws DAOException
	 */
	private void updateSkuByDetailId(ItemDetail detail,Long smallId) {
		if(null==detail){
			return ;
		}
		ItemSku sku = new ItemSku();
		sku.setDetailId(detail.getId());
		sku.setDetailName(detail.getMainTitle());
		sku.setBarcode(detail.getBarcode());
		sku.setItemType(detail.getItemType());
		sku.setBrandId(detail.getBrandId());
		sku.setUnitId(detail.getUnitId());
		sku.setCategoryCode(detail.getCategoryCode());
		sku.setCategoryId(smallId);
		itemSkuDao.batchUpdateByDetailId(sku);
	}
	
	/**
	 * 
	 * <pre>
	 *  批量更新
	 * </pre>
	 *
	 * @param infoDO
	 * @throws DAOException
	 */
	private void updateSkuByItemId(ItemInfo itemInfo) {
		if(null==itemInfo){
			return ;
		}
		ItemSku sku = new ItemSku();
		sku.setItemId(itemInfo.getId());
		sku.setBrandId(itemInfo.getBrandId());
		sku.setUnitId(itemInfo.getUnitId());
		sku.setCategoryCode(itemInfo.getSmallCode());
		sku.setCategoryId(itemInfo.getSmallId());
		itemSkuDao.batchUpdateByItemId(sku);
	}
	
	/**
	 * 更新信息
	 * @param info
	 * @param createUser
	 * @throws DAOException
	 */
	private void updatePrdInfoByItemId(ItemInfo info,String createUser){
		ItemDetail ItemDetail = new ItemDetail ();
		ItemDetail.setCategoryCode(info.getSmallCode());
		ItemDetail.setBrandId(info.getBrandId());
		ItemDetail.setUnitId(info.getUnitId());
		ItemDetail.setUpdateTime(new Date());
		ItemDetail.setUpdateUser(createUser);
		ItemDetail.setItemId(info.getId());
		itemDetailDao.updatePrdInfoByItemId(ItemDetail);
		
	}


	@Override
	public ItemInfo checkSpuExsit(Long smallId, Long brandId, Long unitId,
			String mainTitle,Long infoId)  {
		if(null==smallId||null==brandId
		  ||null==unitId||StringUtils.isBlank(mainTitle)){
			LOGGER.error("检查商品spu信息是否存在:分类，品牌，单位，商品名称参数不能为空");
			throw new ItemServiceException("检查商品SPU信息是否存在:分类，品牌，单位，商品名称参数不能为空");
		}
		
		ItemInfo info = new ItemInfo();
		info.setBrandId(brandId);
		info.setSmallId(smallId);
		info.setUnitId(unitId);
		info.setMainTitle(mainTitle);
		List<ItemInfo> infoList = itemInfoService.queryByObject(info);
		if(CollectionUtils.isEmpty(infoList)){ //说明是新的SPU
			return null;
		}else{
			if(null!=infoId){
				Long id = infoList.get(0).getId();
				if(id.equals(infoId)){ //修改的时候
					return null;
				}
			}
			if(infoList.size()>1){
				LOGGER.error("商品名称:{} 在系统中存在多个SPU,请联系管理员检查",mainTitle);
			}else{
				return infoList.get(0);
			}
		}
		return null;
	}


	@Override
	public int checkPrdidSpec(Long itemId,Map<Long, Long> specInfoMap){
		//校验规格是否存在
		//返回1 代表不存在，校验成功，此一组规格可以插入
		//返回2 代表已经存在(规格组与规格一一匹配) 校验失败，此一组规格不可以插入
		// 返回3 代表规格组数量不匹配，校验失败，此一组规格可以插入
        // 返回4 代表完全匹配上的 
		if(specInfoMap.containsKey(0l)){
			specInfoMap.remove(0l);
		}
		ItemDetail detail = new ItemDetail();
		detail.setItemId(itemId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		params.put("itemId", itemId);
		List<ItemDetail> detailList = itemDetailDao.queryByParam(params);
		List<Long> detailIds = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(detailList)){
			for(ItemDetail ItemDetail : detailList){
				detailIds.add(ItemDetail.getId());
			}
			params.remove("itemId");
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
			List<ItemDetailSpec> detailSpecList = itemDetailSpecDao.queryByParam(params);
			if(CollectionUtils.isNotEmpty(detailSpecList)){
				//规格分组
				Map<Long,List<Long>> prdidSpecMap = new HashMap<Long,List<Long>>();
				Map<Long,List<ItemDetailSpec>> prdidAllSameSpecMap = new HashMap<Long,List<ItemDetailSpec>>();
				for(ItemDetailSpec detailSpec:detailSpecList){
					if(!prdidAllSameSpecMap.containsKey(detailSpec.getDetailId())){
						List<ItemDetailSpec> specDOs = new ArrayList<ItemDetailSpec>();
						specDOs.add(detailSpec);
						prdidAllSameSpecMap.put(detailSpec.getDetailId(), specDOs);
					}else{
						prdidAllSameSpecMap.get(detailSpec.getDetailId()).add(detailSpec);
					}
					if(!prdidSpecMap.containsKey(detailSpec.getSpecGroupId())){
						List<Long> specIds = new ArrayList<Long>();
						specIds.add(detailSpec.getSpecId());
						prdidSpecMap.put(detailSpec.getSpecGroupId(), specIds);
					}else{
						prdidSpecMap.get(detailSpec.getSpecGroupId()).add(detailSpec.getSpecId());
					}
					
				}
				if(MapUtils.isNotEmpty(specInfoMap)){
					if(specInfoMap.size()!= prdidSpecMap.size()){
						return 3;
					}
					int checkSize = 0; 
					Set<Long> specKeySet = prdidAllSameSpecMap.keySet();
					for (Iterator<Long> specIt = specKeySet.iterator(); specIt.hasNext();) {
						Long detailId = (Long) specIt.next();
						List<ItemDetailSpec> specIds = prdidAllSameSpecMap.get(detailId);
						Map <Long,Long> map = new HashMap<Long,Long>();
						for(ItemDetailSpec detailSpec : specIds){
							Long specGroupId = detailSpec.getSpecGroupId();
							Long specId = detailSpec.getSpecId();
							map.put(specGroupId, specId);
						}
						// key一样 值不一样的才能为1
						if(mapKeyEquals(map,specInfoMap)){
							if(mapEquals(map,specInfoMap)){
								return 4;
							}else{
								checkSize ++;
							}
						}
					}
					if(checkSize==specKeySet.size()){
						return 1;
					}else if(checkSize<specKeySet.size()&& checkSize>0){
						return 2;
					}else {
						return 3;
					}
					
				}else{ //已经存在规格,即使是均码也不可以再作新增
					Set<Long> keySet = prdidAllSameSpecMap.keySet();
					if(keySet.size()==1){
						for (Iterator<Long> it = keySet.iterator(); it.hasNext();) {
							Long detailId = (Long) it.next();
							List<ItemDetailSpec> specIds = prdidAllSameSpecMap.get(detailId);
							if(CollectionUtils.isNotEmpty(specIds)){
								if(specIds.get(0).getSpecGroupId().longValue()==ItemConstant.FREE_SIZE_ID.longValue()
										&&specIds.get(0).getSpecId().longValue()==ItemConstant.FREE_SIZE_ID.longValue()){
									return 4;
								}
							}
						}
					}
					return 2;
				}
			}else{
				return 1;
			}
			
		}
		return 2;
	}
	
	private static boolean mapEquals(Map<Long,Long> src, Map<Long,Long> target){
		Set<Long> srcSet = src.keySet();
		int flag = 0 ; 
		for (Iterator<Long> it = srcSet.iterator(); it.hasNext();) {
			Long key = it.next();
			if(target.containsKey(key)&&src.get(key).equals(target.get(key))){
				flag ++;
			}
		}
		return (src.size() == target.size())&&(flag ==src.size());
	}
	
	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param src
	 * @param target
	 * @return
	 */
	private static boolean mapKeyEquals(Map<Long,Long> src, Map<Long,Long> target){
		Set<Long> srcSet = src.keySet();
		int flag = 0 ; 
		for (Iterator<Long> it = srcSet.iterator(); it.hasNext();) {
			Long key = it.next();
			if(target.containsKey(key)){
				flag ++;
			}
		}
		return (src.size() == target.size())&&(flag ==src.size());
	}


	@Override
	public List<ItemCopyDto> getItemCopyByDetailId(Long detailId){
		if(null==detailId){
			return null;
		}
		ItemDetail ItemDetail = itemDetailDao.queryById(detailId);
		Long itemId = ItemDetail.getItemId();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("itemId", itemId);
		List<ItemDetail> detailList = itemDetailDao.queryByParam(params);
		if(CollectionUtils.isEmpty(detailList)){
			return null;
		}
		ItemDetail srcDetail = null;
		List<Long> detailIds = new ArrayList<Long>();
		for(ItemDetail de : detailList){
			if(de.getId().equals(detailId)){
				srcDetail = de;
			}else{
				detailIds.add(de.getId());
			}
		}
		detailList.remove(srcDetail);
		//查询图片与详情列表
		List<ItemPictures> picList = itemPicturesDao.queryByParam(params);
		List<ItemDesc> descList =  itemDescDao.queryByParam(params);
		List<ItemDescMobile> descMobileList =  itemDescMobileDao.queryByParam(params);
		//拼装结果
		List<ItemCopyDto> itemCopyDtoList = new ArrayList<ItemCopyDto>();
		for(ItemDetail de : detailList){
			ItemCopyDto itemCopy = new ItemCopyDto();
			itemCopy.setItemDetail(de);
			List<ItemPictures> listPic = new ArrayList<ItemPictures> ();
			ItemDesc desc = null; 
			ItemDescMobile descMobile = null;
			if(CollectionUtils.isNotEmpty(picList)){
				for(ItemPictures pic : picList){
					if(pic.getDetailId().equals(de.getId())){
						listPic.add(pic);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(descList)){
				for(ItemDesc detailDesc : descList){
					if(detailDesc.getDetailId().equals(de.getId())){
						desc = detailDesc;
						break;
					}
				}
			}
			if(CollectionUtils.isNotEmpty(descMobileList)){
				for(ItemDescMobile detailDescMobile : descMobileList){
					if(detailDescMobile.getDetailId().equals(de.getId())){
						descMobile = detailDescMobile;
						break;
					}
				}
			}
			itemCopy.setItemPicturesList(listPic);
			itemCopy.setItemDesc(desc);
			itemCopy.setItemDescMobile(descMobile);
			itemCopyDtoList.add(itemCopy);
		}
		return itemCopyDtoList;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void copyPrdPicAndDesc(Long itemId,List<Long> detailIds, List<String> picList,String desc, String descMobile,String createUser){
		Date now = new Date();
		if(CollectionUtils.isEmpty(detailIds)){//需要讨论
			return ;
		}
		for(Long detailId : detailIds){
			if(CollectionUtils.isNotEmpty(picList)){
				int i = 0 ;
				for(String picture : picList){
					ItemPictures pic = new ItemPictures();
					pic.setCreateTime(now);
					pic.setDetailId(detailId);
					pic.setItemId(itemId);
					pic.setPicture(picture);
					pic.setCreateUser(createUser);
					pic.setMain(i==0?Constant.DEFAULTED.YES:Constant.DEFAULTED.NO);
					i++;
					itemPicturesDao.insert(pic);
				}
				
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			params.put("detailId", detailId);
			if(StringUtils.isNotBlank(desc)){
				List<ItemDesc> descList = itemDescDao.queryByParam(params);
				if(CollectionUtils.isNotEmpty(descList)){
					itemDescDao.deleteById(descList.get(0).getId());
				}
				ItemDesc descDO = new ItemDesc();
				descDO.setCreateTime(now);
				descDO.setDescription(desc);
				descDO.setDetailId(detailId);
				descDO.setItemId(itemId);
				descDO.setCreateUser(createUser);
				descDO.setUpdateUser(createUser);
				itemDescDao.insert(descDO);
			}
			if(StringUtils.isNotBlank(descMobile)){
				List<ItemDescMobile> descMobileList = itemDescMobileDao.queryByParam(params);
				if(CollectionUtils.isNotEmpty(descMobileList)){
					itemDescMobileDao.deleteById(descMobileList.get(0).getId());
				}
				ItemDescMobile descMobileDO = new ItemDescMobile();
				descMobileDO.setCreateTime(now);
				descMobileDO.setDescription(desc);
				descMobileDO.setDetailId(detailId);
				descMobileDO.setItemId(itemId);
				descMobileDO.setCreateUser(createUser);
				descMobileDO.setUpdateTime(now);
				descMobileDO.setUpdateUser(createUser);
				itemDescMobileDao.insert(descMobileDO);
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 	 推送消息给promotion
	 * </pre>
	 *
	 * @param detailId
	 * @throws DAOException 
	 * @throws MqClientException 
	 */
	private void send2PromotionMessage(Long detailId,ItemDetail detail){
		if(null==detailId){
			return ;
		}
		ItemInfo info = itemInfoDao.queryById(detail.getItemId());
		ItemPictures itemPictures = new ItemPictures();
		itemPictures.setDetailId(detailId);
		itemPictures.setMain(Constant.DEFAULTED.YES);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		params.put("main", Constant.DEFAULTED.YES);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemPictures> picList = itemPicturesDao.queryByParam(params);
		String pic = "";
		if(CollectionUtils.isNotEmpty(picList)){
			pic = picList.get(0).getPicture();
			params.clear();
			params.put("detailId", detailId);
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			List<ItemSku> skuList = itemSkuDao.queryByParam(params);
			if(CollectionUtils.isNotEmpty(skuList)){
				List<PromotionItemMqDto> mqList = new ArrayList<PromotionItemMqDto>();
				for(ItemSku sku : skuList){
					if(StringUtils.isNotBlank(pic)){
						PromotionItemMqDto mq = new PromotionItemMqDto();
						mq.setMainPic(pic);
						mq.setMainTitle(sku.getDetailName());
						mq.setSkuCode(sku.getSku());
						mq.setBasicPrice(sku.getBasicPrice());
						mq.setApplyAgeId(detail.getApplyAgeId());
						mq.setStatus(sku.getStatus());
						mq.setUpdateSkuDate(new Date());
						mq.setBrandId(info.getBrandId());
						mq.setSmallCateId(info.getSmallId());
						mq.setMiddleCateId(info.getMediumId());
						mq.setLargeCateId(info.getLargeId());
						mqList.add(mq);
					}
				}
				
				try {
					LOGGER.info("change item info, send mq message.");
					rabbitMqProducer.sendP2PMessage(ItemConstant.ITEM_PROMOTION_PUB_MSG, mqList);
				} catch (MqClientException e) {
					LOGGER.error("发送消息出错：{}",e);
				}
				
			}
		}
	}


	@Override
	public List<ItemSkuSupplier> getSkuSupplierListBySkuIds(List<Long> skuList){
		if(CollectionUtils.isEmpty(skuList)){
			return null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "sku_id in ("+StringUtil.join(skuList, Constant.SPLIT_SIGN.COMMA)+")");
		return itemSkuSupplierDao.queryByParam(params);
	}


	@Override
	public List<ItemSkuArt> getSkuArtNumberBySkuId(Long skuId){
		if(skuId != null){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("skuId",skuId);
			return itemSkuArtDao.queryByParam(params);
		}else{
			LOGGER.error("获取sku对应信息传入参数为空");
		}
		return new ArrayList<ItemSkuArt>();
	}

	
	@Override
	public Long saveSkuArt(ItemSkuArt itemSkuArt){
		ItemSku itemInfo = itemSkuDao.queryById(itemSkuArt.getSkuId());
		if(itemInfo != null){
			itemSkuArt.setSku(itemInfo.getSku());
			itemSkuArt.setSkuId(itemSkuArt.getSkuId());
			itemSkuArtDao.insert(itemSkuArt);
			return itemSkuArt.getId();
		}
		return null;
	}
	
	@Override
	public Integer deleteSkuArtInfo(Long id) throws Exception {
		Assert.notNull(id);
		return itemSkuArtDao.deleteById(id);
	}


	@Override
	public ItemSkuArt checkSkuArtWithSkuId(ItemSkuArt itemSkuArt){
		Assert.notNull(itemSkuArt);
		Map<String,Object> params = BeanUtil.beanMap(itemSkuArt);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemSkuArt>  list = itemSkuArtDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ResultInfo<List<Option>> changeItemStatus(List<Long> skuIds, int status){
		if(CollectionUtils.isEmpty(skuIds)){
			return new ResultInfo<>(new FailInfo("传入的参数不能为空"));
		}
		
		List<ItemSku> skuList = new ArrayList<ItemSku>();
		List<Long> querySkuIds = new ArrayList<Long>();
		for(Long skuId : skuIds){
			if(null!=skuId){
				querySkuIds.add(skuId);
			}
		}
		Map<String,Object> params = new HashMap<String,Object>();
		if(CollectionUtils.isNotEmpty(querySkuIds)){
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in ("+StringUtil.join(querySkuIds, Constant.SPLIT_SIGN.COMMA)+")");
			skuList = itemSkuDao.queryByParam(params);
		}
		if(CollectionUtils.isEmpty(skuList)){
			return new ResultInfo<>(new FailInfo("传入的sku列表不能在系统中查询到sku信息"));
		}
		List<Long> detailIds = new ArrayList<Long>();
		List<ItemSku> updateSkuList = new ArrayList<ItemSku>();
		for(ItemSku sku : skuList){
			if(!detailIds.contains(sku.getDetailId())){
				detailIds.add(sku.getDetailId());
			}
			ItemSku updateSku = new ItemSku();
			updateSku.setId(sku.getId());
			updateSku.setStatus(status);
			updateSkuList.add(updateSku);
		}
		// 根据status值来更新状态 0-未上架 1-上架 2-作废 
		List<Option> errorMap = new ArrayList<Option>();
		switch(status){
			case 0: 
				itemSkuDao.batchUpdateSkusStatu(updateSkuList);
				if(CollectionUtils.isNotEmpty(detailIds)){
					for(Long detailId: detailIds){
						updateDetailStatus(detailId);
					}
				}
				break;
			case 1:
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in ("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
				List<ItemPictures> picList = new ArrayList<ItemPictures>();
				List<ItemDesc> descList = new ArrayList<ItemDesc>();
				List<ItemDescMobile> descMobileList = new ArrayList<ItemDescMobile>();
				picList = itemPicturesDao.queryByParam(params);
				descList = itemDescDao.queryByParam(params);
				descMobileList = itemDescMobileDao.queryByParam(params);
				Map<Long,Integer> detailPicsMap = new HashMap<Long,Integer>(); //商品图片
				Map<Long,String> detailDescMap = new HashMap<Long,String>();
				Map<Long,String> detailDescMobileMap = new HashMap<Long,String>(); 
				if(CollectionUtils.isNotEmpty(picList)){
					for(ItemPictures pic : picList){
						if(detailPicsMap.containsKey(pic.getDetailId())){
							detailPicsMap.put(pic.getDetailId(), 
									detailPicsMap.get(pic.getDetailId())+1);
						}else{
							detailPicsMap.put(pic.getDetailId(), 1);
						}
					}
				}
				
				if(CollectionUtils.isNotEmpty(descList)){
					for(ItemDesc desc : descList){
						if(!detailDescMap.containsKey(desc.getDetailId())){
							detailDescMap.put(desc.getDetailId(), desc.getDescription());
						}
					}
				}
				
				if(CollectionUtils.isNotEmpty(descMobileList)){
					for(ItemDescMobile desc : descMobileList){
						if(!detailDescMobileMap.containsKey(desc.getDetailId())){
							detailDescMobileMap.put(desc.getDetailId(), desc.getDescription());
						}
					}
				}
				List<ItemSku> skuSuccessList = new ArrayList<ItemSku>();
				//图片，商品详情页，条码，编号，
				for(ItemSku sku : skuList){
					Long detailId = sku.getDetailId();
					Integer picCounts = detailPicsMap.get(detailId);
					String desc = detailDescMap.get(detailId);
					String descMobile = detailDescMobileMap.get(detailId);
					if(null == picCounts || picCounts > 10 || picCounts < 3){
						errorMap.add(new Option(String.valueOf(sku.getId()), "Sku的图片必须为3-10张"));
						continue;
					}else if(StringUtils.isBlank(desc)){
						errorMap.add(new Option(String.valueOf(sku.getId()), "PC模板不能为空"));
						continue;
					}else if(StringUtils.isBlank(descMobile)){
						errorMap.add(new Option(String.valueOf(sku.getId()), "手机模板不能为空"));
						continue;
					}else if(StringUtils.isBlank(sku.getBarcode())){
						errorMap.add(new Option(String.valueOf(sku.getId()), "Sku的条码不能为空"));
						continue;
					}else if(StringUtils.isBlank(sku.getDetailName())){
						errorMap.add(new Option(String.valueOf(sku.getId()), "Sku的名称不能为空"));
						continue;
					}else if(null==sku.getSpId()){
						errorMap.add(new Option(String.valueOf(sku.getId()), "Sku的供应商不能为空"));
						continue;
					}
					else{
						errorMap.add(new Option(String.valueOf(sku.getId()), "更新成功"));
						ItemSku itemSku = new ItemSku();
						itemSku.setId(sku.getId());
						itemSku.setStatus(status);
						skuSuccessList.add(itemSku);
					}
				}
				//批量更新sku状态
				if(CollectionUtils.isNotEmpty(skuSuccessList)){
					itemSkuDao.batchUpdateSkusStatu(skuSuccessList);
				}
				if(CollectionUtils.isNotEmpty(detailIds)){
					for(Long detailId: detailIds){
						updateDetailStatus(detailId);
					}
				}
				break;
			case 2:
				//暂时未实现
				break;
		}
		return new ResultInfo<>(errorMap);
	}
	@Override
	public List<ItemSku> getSkuListByDetailIds(List<Long> detailIds){
		if(CollectionUtils.isEmpty(detailIds)){
			return null;
		}
		List<Long> queryIds = new ArrayList<Long>();
		for(Long detailId : detailIds){
			if(null!= detailId && !queryIds.contains(detailId)){
				queryIds.add(detailId);
			}
		}
		if(CollectionUtils.isNotEmpty(queryIds)){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(DAOConstant.MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in ("+StringUtil.join(queryIds,Constant.SPLIT_SIGN.COMMA)+")");
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			return  itemSkuDao.queryByParam(params);
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Long saveItemPrds(ItemDto item, String createUser){
		// 创建日期应该调用的服务器的时间？？
		ItemInfo info = null;
		info = item.getItemInfo();
		List<ItemDetail> detailList = item.getItemDetailList();
		Long itemId = info.getId();
		try {
			// 修改商品
			if(null!=info.getId()){
				updateItem(item,createUser);
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("id",info.getId());
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			ItemInfo itemInfo = itemInfoDao.queryByParam(params).get(0);
			String spu = itemInfo.getSpu();
			info.setCreateUser(createUser);
			// 获取新增detail列表
			if (CollectionUtils.isNotEmpty(detailList)) {
				for (ItemDetail detail : detailList) {
					if(StringUtils.isNotBlank(detail.getBarcode())){
						
						boolean checkFlag = true;
						if(null == detail.getId()){
							checkFlag  = checkBarcodeExsit(detail.getBarcode(),null,null);
						}else{
							checkFlag  = checkBarcodeExsit(detail.getBarcode(), detail.getId(),null);
						}
						if(!checkFlag){
							LOGGER.error( "商品系统中已经存在此条码");
							throw new ItemServiceException( "商品系统中已经存在此条码");
						}
						
						Long detailId = detail.getId();
						if(detailId!= null){
							ItemDetail ItemDetail = new ItemDetail();
							ItemDetail.setId(detailId);
							ItemDetail.setCategoryCode(info.getSmallCode());
							ItemDetail.setSpuName(info.getMainTitle());
							ItemDetail.setBarcode(detail.getBarcode());
							ItemDetail.setItemTitle(detail.getItemTitle());
							ItemDetail.setUpdateTime(detail.getUpdateTime());
							ItemDetail.setUpdateUser(detail.getUpdateUser());
							ItemDetail.setBrandId(info.getBrandId());
							ItemDetail.setUnitId(info.getUnitId());
							itemDetailDao.updateNotNullById(detail);
						}else{
							detail.setItemId(itemId);
							detail.setSpu(spu);
							detail.setSpuName(info.getMainTitle());
							detail.setItemTitle(detail.getItemTitle());
							String prdId = getPrdidCode(spu);
							detail.setPrdid(prdId);
							detail.setBrandId(info.getBrandId());
							detail.setUnitId(info.getUnitId());
							detail.setStatus(ItemStatusEnum.OFFLINE.getValue());
							detail.setCategoryCode(info.getSmallCode());
							detail.setCreateUser(createUser);
							itemDetailDao.insert(detail);
							detailId = detail.getId();
							String specGroupIds = detail.getSpecGroupIds();
							if(StringUtils.isNotBlank(specGroupIds)){
								String [] str = specGroupIds.split(ItemConstant.DEFAULT_SEPARATOR);
								for(String s : str){
									String [] ids = s.split(ItemConstant.DEFAULT_JOIN);
									ItemDetailSpec detailSpec = new ItemDetailSpec();
									detailSpec.setSort(1);
									detailSpec.setDetailId(detailId);
									detailSpec.setCreateTime(new Date());
									detailSpec.setUpdateTime(new Date());
									detailSpec.setSpecGroupId(Long.parseLong(ids[0]));
									detailSpec.setSpecId(Long.parseLong(ids[1]));
									itemDetailSpecDao.insert(detailSpec);
								}
								
							}else{//均码处理
								ItemDetailSpec detailSpec = new ItemDetailSpec();
								detailSpec.setSort(1);
								detailSpec.setDetailId(detailId);
								detailSpec.setCreateTime(new Date());
								detailSpec.setUpdateTime(new Date());
								detailSpec.setSpecGroupId(ItemConstant.FREE_SIZE_ID);
								detailSpec.setSpecId(ItemConstant.FREE_SIZE_ID);
								itemDetailSpecDao.insert(detailSpec);
							}
							
						}
					}
					
				}
			}
			return itemId;
		} catch (ItemServiceException e) {
			LOGGER.error(e.getMessage(),e);
			throw new ItemServiceException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw new ItemServiceException("保存商品失败了");
		}
	}
	@Override
	public ItemInfo getItemInfoFromMasterDataBase(Long itemId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", itemId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		ItemInfo returnInfo = itemInfoDao.queryByParam(params).get(0);
		return returnInfo;
	}


	@Override
	public ItemInfo getItemInfoFromMasterDataBaseBySpuCode(String spuCode){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("spu", spuCode);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemInfo> returnList = itemInfoDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(returnList)){
			return returnList.get(0);
		}
		return null;
	}
	@Override
	public List<ItemDetail> getGroupDetailIdFromMasterDataBaseWithItemId(Long itemId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("itemId", itemId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemDetail> returnList = itemDetailDao.queryByParam(params);
		return returnList;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Long saveOpenPlantFromItem(ItemOpenSaveDto saveDto){
		if(saveDto != null){
			//1保存detail
			Long detailId = 0l;
			Long skuId= 0l;
			String prdid ="";
			if(saveDto.getItemDetail() != null){
				saveDto.getItemDetail().setId(null);
				prdid = getPrdidCode(saveDto.getItemDetail().getSpu());
				saveDto.getItemDetail().setPrdid(prdid);
				itemDetailDao.insert(saveDto.getItemDetail());
				detailId = saveDto.getItemDetail().getId();
			}
			if( detailId > 0){
				if(saveDto.getItemDesc() != null){
					saveDto.getItemDesc().setDetailId(detailId);
					itemDescDao.insert(saveDto.getItemDesc());
				}
				if(saveDto.getItemDescMobile() != null){
					saveDto.getItemDescMobile().setDetailId(detailId);
					itemDescMobileDao.insert(saveDto.getItemDescMobile());
				}
				if(CollectionUtils.isNotEmpty(saveDto.getListDetailSpec())){
					for(ItemDetailSpec saveDO :saveDto.getListDetailSpec()){
						saveDO.setId(null);
						saveDO.setDetailId(detailId);
						itemDetailSpecDao.insert(saveDO);
					}
				}
				if(CollectionUtils.isNotEmpty(saveDto.getAttributeList())){
					for(ItemAttribute saveDO :saveDto.getAttributeList()){
						saveDO.setId(null);
						saveDO.setDetailId(detailId);
						itemAttributeDao.insert(saveDO);
					}
				}
				if(CollectionUtils.isNotEmpty(saveDto.getListPic())){
					for(ItemPictures pic :saveDto.getListPic()){
						pic.setDetailId(detailId);
						itemPicturesDao.insert(pic);
					}
				}
				//插入sku
				ItemSku  itemSku = saveDto.getItemSku();
				itemSku.setItemId(saveDto.getItemDetail().getItemId());
				itemSku.setBrandId(saveDto.getItemDetail().getBrandId());
				itemSku.setSpu(saveDto.getItemDetail().getSpu());
				itemSku.setDetailName(saveDto.getItemDetail().getMainTitle());
				
				itemSku.setItemType(saveDto.getItemDetail().getItemType());
				itemSku.setPrdid(prdid);
				itemSku.setDetailId(detailId);
				
				itemSku.setBarcode(saveDto.getItemDetail().getBarcode());
				String skuCode = getSkuCode(prdid);
				itemSku.setSku(skuCode);
				itemSku.setStatus(saveDto.getItemDetail().getStatus());
				
				itemSku.setSaleType(ItemSaleTypeEnum.SELLER.getValue());
				itemSku.setSort(100);
				
				itemSku.setSpId(saveDto.getItemSku().getSpId());
				SupplierInfo supplierDO = supplierInfoService.queryById(saveDto.getItemSku().getSpId());
				if(supplierDO != null){
					itemSku.setSpCode(supplierDO.getSupplierCode());
					itemSku.setSpName(supplierDO.getName());
				}
				itemSku.setCreateTime(new Date());
				itemSku.setCreateUser(saveDto.getItemDetail().getCreateUser());
				itemSku.setDataSource(ItemDataSourceTypeEnum.SELLER.getCode());
				LOGGER.info(JSONObject.fromObject(itemSku).toString());
				itemSkuDao.insert(itemSku);
				skuId = itemSku.getId();
			}
			return 	skuId ;
		}
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ItemOpenSaveDto saveOpenPlantForsSpuFromItem(ItemOpenSaveDto saveDto){
		Long itemId=0l;
		if(saveDto != null){
			String spu = getSpuCode(saveDto.getItemInfo().getSmallCode());
			if(saveDto.getItemInfo() !=null){
				//1 spu
				saveDto.getItemInfo().setSpu(spu);
				saveDto.getItemInfo().setId(null);
				itemInfoDao.insert(saveDto.getItemInfo());
				itemId = saveDto.getItemInfo().getId();
				saveDto.getItemInfo().setSpu(spu);
				saveDto.getItemInfo().setId(itemId);
			}
			//2保存detail
			Long detailId = 0l;
			String prdid ="";
			if(saveDto.getItemDetail() != null){
				saveDto.getItemDetail().setId(null);
				prdid = getPrdidCode(spu);
				saveDto.getItemDetail().setPrdid(prdid);
				saveDto.getItemDetail().setItemId(itemId);
				saveDto.getItemDetail().setSpu(saveDto.getItemInfo().getSpu());
				itemDetailDao.insert(saveDto.getItemDetail());
				detailId = saveDto.getItemDetail().getId();
				saveDto.getItemDetail().setPrdid(prdid);
			}
			if( detailId > 0){
				if(saveDto.getItemDesc() != null){
					saveDto.getItemDesc().setDetailId(detailId);
					saveDto.getItemDesc().setItemId(itemId);
					itemDescDao.insert(saveDto.getItemDesc());
				}
				if(saveDto.getItemDescMobile() != null){
					saveDto.getItemDescMobile().setDetailId(detailId);
					saveDto.getItemDescMobile().setItemId(itemId);
					itemDescMobileDao.insert(saveDto.getItemDescMobile());
				}
				if(CollectionUtils.isNotEmpty(saveDto.getListDetailSpec())){
					for(ItemDetailSpec saveDO :saveDto.getListDetailSpec()){
						saveDO.setId(null);
						saveDO.setDetailId(detailId);
						itemDetailSpecDao.insert(saveDO);
					}
				}
				if(CollectionUtils.isNotEmpty(saveDto.getAttributeList())){
					for(ItemAttribute saveDO :saveDto.getAttributeList()){
						saveDO.setId(null);
						saveDO.setDetailId(detailId);
						saveDO.setItemId(itemId);
						itemAttributeDao.insert(saveDO);
					}
				}
				if(CollectionUtils.isNotEmpty(saveDto.getListPic())){
					for(ItemPictures pic :saveDto.getListPic()){
						pic.setDetailId(detailId);
						pic.setItemId(itemId);
						itemPicturesDao.insert(pic);
					}
					
				}
				//插入sku
				ItemSku  itemSku = saveDto.getItemSku();
				itemSku.setItemId(itemId);
				
				itemSku.setBrandId(saveDto.getItemInfo().getBrandId());
				itemSku.setSpu(saveDto.getItemInfo().getSpu());
				itemSku.setDetailName(saveDto.getItemDetail().getMainTitle());
				
				itemSku.setItemType(saveDto.getItemDetail().getItemType());
				itemSku.setPrdid(prdid);
				itemSku.setDetailId(detailId);
				
				itemSku.setBarcode(saveDto.getItemDetail().getBarcode());
				String skuCode = getSkuCode(prdid);
				itemSku.setSku(skuCode);
				itemSku.setStatus(saveDto.getItemDetail().getStatus());
				
				itemSku.setSaleType(ItemSaleTypeEnum.SELLER.getValue());
				itemSku.setSort(100);
				
				itemSku.setSpId(saveDto.getItemSku().getSpId());
				SupplierInfo supplierInfo = supplierInfoService.queryById(saveDto.getItemSku().getSpId());
				if(supplierInfo != null){
					itemSku.setSpCode(supplierInfo.getSupplierCode());
					itemSku.setSpName(supplierInfo.getName());
				}
				itemSku.setCreateTime(new Date());
				itemSku.setCreateUser(saveDto.getItemDetail().getCreateUser());
				itemSku.setUpdateUser(itemSku.getCreateUser());
				itemSku.setDataSource(ItemDataSourceTypeEnum.SELLER.getCode());
				
				LOGGER.info(JSONObject.fromObject(itemSku).toString());
				itemSkuDao.insert(itemSku);
				saveDto.getItemSku().setId(itemSku.getId());
			}
			return 	saveDto ;
		}
		return null;
	}

	@Override
	public Long saveItemDetailWithExistSpu(ItemInfo info, DetailDto detailDto,
			Long spId, String auditType, Long skuId) {
		Long detailId =null;
		int res = 0;
		Long itemId = null;
		//  创建日期应该调用的服务器的时间？
		ItemDetail detail = null;
		detail = detailDto.getItemDetail();
		detailId = detail.getId();

		//给itemId赋值
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		itemId = itemDetailDao.queryByParam(params).get(0).getItemId();
		detail.setUpdateTime(new Date());
		detail.setBrandId(info.getBrandId());
		detail.setUnitId(info.getUnitId());
		detail.setCategoryCode(info.getSmallCode());
		detail.setSpuName(info.getMainTitle());
		detail.setUpdateUser("[供应商]"+spId);
		//获取 是否海淘商品,1-否，2-是，默认1
		if(detail.getWavesSign().intValue()==1){
			detail.setTarrifRate(null);
			detail.setCountryId(null);
			detail.setSendType(null);
		}
		//获取 是否保质期管理:1 是，2-否 默认1
		if(detail.getExpSign().intValue()==2){
			detail.setExpDays(null);
		}
		res =  itemDetailDao.updateNotNullById(detail);

		if(res<=0){
			return null;
		}
		//强制更新
		if(StringUtils.isBlank(detail.getManufacturer())){
			detail.setManufacturer(null);
		}
		if(StringUtils.isBlank(detail.getSpecifications())){
			detail.setSpecifications(null);   
		}
		//保存商品信息
		saveItemDetail(itemId, detailId, detailDto,"[供应商]"+spId);
		
		if(SellerItemAuditTypeEnum.S.getCode().equals(auditType)){
			if(skuId != null){
				ItemSku submitInfo = new ItemSku();
				submitInfo.setAuditStatus(SellerItemAuditTypeEnum.S.getCode());
				submitInfo.setId(skuId);
				submitInfo.setBasicPrice(detail.getBasicPrice());
				itemSkuDao.updateNotNullById(submitInfo);
			}
		}else{
			if(skuId != null){
				ItemSku submitInfo = new ItemSku();
				submitInfo.setId(skuId);
				submitInfo.setBasicPrice(detail.getBasicPrice());
				itemSkuDao.updateNotNullById(submitInfo);
			}
		}
		return detailId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Long saveItemWithExistSpu(ItemDto item, Long spId) {
		// 创建日期应该调用的服务器的时间？？
		ItemInfo info = null;
		info = item.getItemInfo();
		List<ItemDetail>  detailList = item.getItemDetailList();
		Long itemId = null;

		info.setCreateUser("[供应商]"+spId);
		info.setCreateTime(new Date());
		info.setBindLevel(SellerItemBindLevelEnum.PRD.getCode());
		info.setSupplierId(spId);
		itemInfoDao.insert(info);
		itemId = info.getId();
		// 获取新增detail列表
		if (CollectionUtils.isNotEmpty(detailList)) {
			for (ItemDetail detail : detailList) {
				if(StringUtils.isNotBlank(detail.getBarcode())){
					detail.setSupplierId(spId);
					detail.setCreateUser("[供应商]"+spId);
					detail.setBindLevel(SellerItemBindLevelEnum.PRD.getCode());
					detail.setCreateTime(new Date());
					
					detail.setItemId(itemId);
					detail.setSpu(info.getSpu());
					detail.setSpuName(info.getMainTitle());
					//PRDID 为系统生成 商家平台
					detail.setPrdid(null);
					detail.setBrandId(info.getBrandId());
					detail.setUnitId(info.getUnitId());
					
					detail.setStatus(ItemStatusEnum.ONLINE.getValue());
					
					detail.setCategoryCode(info.getSmallCode());
					
					detail.setCreateUser("[供应商]"+spId);
					
					itemDetailDao.insert(detail);
					Long detailId = detail.getId();
					//插入sku (默认添加) 
					ItemSku insertSellerSku = new ItemSku();
					insertSellerSku.setAuditStatus(SellerItemAuditTypeEnum.E.getCode());
					insertSellerSku.setBindLevel(SellerItemBindLevelEnum.PRD.getCode()); 
					insertSellerSku.setSpId(spId);
					insertSellerSku.setDetailName(detail.getMainTitle());
					insertSellerSku.setDetailId(detailId);
					insertSellerSku.setSpu(info.getSpu());
					insertSellerSku.setBarcode(detail.getBarcode());
					insertSellerSku.setStatus(1);
					insertSellerSku.setCreateUser("[供应商]"+spId);
					insertSellerSku.setCreateTime(new Date());
					insertSellerSku.setBrandName(info.getBrandName());
					insertSellerSku.setUnitName(info.getUnitName());
					
					itemSkuDao.insert(insertSellerSku);
					Long skuId =insertSellerSku.getId();
					String specGroupIds = detail.getSpecGroupIds();
					if(StringUtils.isNotBlank(specGroupIds)){
						String [] str = specGroupIds.split(ItemConstant.DEFAULT_SEPARATOR);
						for(String s : str){
							String [] ids = s.split(ItemConstant.DEFAULT_JOIN);
							ItemDetailSpec detailSpec = new ItemDetailSpec();
							detailSpec.setSort(1);
							detailSpec.setDetailId(detailId);
							detailSpec.setCreateTime(new Date());
							detailSpec.setUpdateTime(new Date());
							detailSpec.setSpecGroupId(Long.parseLong(ids[0]));
							detailSpec.setSpecId(Long.parseLong(ids[1]));
							itemDetailSpecDao.insert(detailSpec);
						}
						
					}
				}
			}
		}
		return itemId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Long saveNewSpu(ItemDto item, Long spId) {
		// 创建日期应该调用的服务器的时间？？
		ItemInfo info = null;
		info = item.getItemInfo();
		List<ItemDetail> detailList = item.getItemDetailList();
		Long itemId = null;

		info.setCreateUser("[供应商]"+spId);
		info.setCreateTime(new Date());
		info.setBindLevel(SellerItemBindLevelEnum.SPU.getCode());
		info.setSupplierId(spId);
		itemInfoDao.insert(info);
		itemId = info.getId();
		// 获取新增detail列表
		if (CollectionUtils.isNotEmpty(detailList)) {
			for (ItemDetail detail : detailList) {
				
				if(StringUtils.isNotBlank(detail.getBarcode())){
					detail.setSupplierId(spId);
					detail.setCreateUser("[供应商]"+spId);
					detail.setBindLevel(SellerItemBindLevelEnum.SPU.getCode());
					detail.setCreateTime(new Date());
					
					detail.setItemId(itemId);
					detail.setSpu(info.getSpu());
					detail.setSpuName(info.getMainTitle());
					//PRDID 为系统生成 商家平台
					detail.setPrdid(null);
					detail.setBrandId(info.getBrandId());
					detail.setUnitId(info.getUnitId());
					detail.setStatus(ItemStatusEnum.ONLINE.getValue());
					detail.setCategoryCode(info.getSmallCode());
					detail.setCreateUser("[供应商]"+spId);
					
					itemDetailDao.insert(detail);
					Long detailId = detail.getId();
					//插入sku (默认添加) 
					ItemSku insertSellerSku = new ItemSku();
					insertSellerSku.setItemId(itemId);
					insertSellerSku.setAuditStatus(SellerItemAuditTypeEnum.E.getCode());
					insertSellerSku.setBindLevel(SellerItemBindLevelEnum.SPU.getCode()); 
					insertSellerSku.setSpId(spId);
					insertSellerSku.setDetailName(detail.getMainTitle());
					insertSellerSku.setDetailId(detailId);
					insertSellerSku.setSpu(info.getSpu());
					insertSellerSku.setBarcode(detail.getBarcode());
					insertSellerSku.setStatus(1);
					insertSellerSku.setCreateUser("[供应商]"+spId);
					insertSellerSku.setCreateTime(new Date());
					insertSellerSku.setBrandName(info.getBrandName());
					insertSellerSku.setUnitName(info.getUnitName());
					
					itemSkuDao.insert(insertSellerSku);
					Long skuId = insertSellerSku.getId();
					
					String specGroupIds = detail.getSpecGroupIds();
					if(StringUtils.isNotBlank(specGroupIds)){
						String [] str = specGroupIds.split(ItemConstant.DEFAULT_SEPARATOR);
						for(String s : str){
							String [] ids = s.split(ItemConstant.DEFAULT_JOIN);
							ItemDetailSpec detailSpec = new ItemDetailSpec();
							detailSpec.setSort(1);
							detailSpec.setDetailId(detailId);
							detailSpec.setCreateTime(new Date());
							detailSpec.setUpdateTime(new Date());
							detailSpec.setSpecGroupId(Long.parseLong(ids[0]));
							detailSpec.setSpecId(Long.parseLong(ids[1]));
							itemDetailSpecDao.insert(detailSpec);
						}
						
					}
					else{//均码处理
						ItemDetailSpec detailSpec = new ItemDetailSpec();
						detailSpec.setSort(1);
						detailSpec.setDetailId(detailId);
						detailSpec.setCreateTime(new Date());
						detailSpec.setUpdateTime(new Date());
						detailSpec.setSpecGroupId(ItemConstant.FREE_SIZE_ID);
						detailSpec.setSpecId(ItemConstant.FREE_SIZE_ID);
						itemDetailSpecDao.insert(detailSpec);
					}
				}
				
			}
		}
		return itemId;
	}
}
