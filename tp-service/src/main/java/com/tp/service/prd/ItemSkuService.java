package com.tp.service.prd;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.MqMessageConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.prd.ItemDetailDao;
import com.tp.dao.prd.ItemDetailSpecDao;
import com.tp.dao.prd.ItemSkuDao;
import com.tp.datasource.ContextHolder;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.SkuDetailInfoDto;
import com.tp.dto.prd.SkuDto;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemSku;
import com.tp.mq.RabbitMqProducer;
import com.tp.query.prd.ItemQuery;
import com.tp.query.prd.SellerSkuQuery;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

@Service
public class ItemSkuService extends BaseService<ItemSku> implements IItemSkuService {

	@Autowired
	private ItemSkuDao itemSkuDao;
	@Autowired
	private ItemDetailDao itemDetailDao;
	@Autowired
	private ItemDetailSpecDao itemDetailSpecDao;
	
	@Autowired
	private RabbitMqProducer rabbitMqProducer;
	
	@Override
	public BaseDao<ItemSku> getDao() {
		return itemSkuDao;
	}
	@Override
	public List<SkuDto> selectProductDeatilsByItemIdAndSpId(ItemSku itemSku){
		return itemSkuDao.selectProductDeatilsByItemIdAndSpId(itemSku);
	}

	@Override
	public List<SkuDto> selectSkuDetailInfo(List<Long> skuIds){
		return itemDetailDao.selectSkuDetailInfo(skuIds); 
	}

	
	@Override
	public List<ItemDetailSpec> selectSkuDetailSpecInfos(Long detailId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		return itemDetailSpecDao.queryByParam(params);
	}

	@Override
	public List<ItemDetailSpec> selectSpecByDetailIds(List<Long> detailIdList){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+StringUtil.join(detailIdList, Constant.SPLIT_SIGN.COMMA)+")");
		return itemDetailSpecDao.queryByParam(params);
	}
	@Override
	public List<ItemSku> selectSkuListByBarcode(String barcode) {
		if(StringUtils.isBlank(barcode)){
			return null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("barcode", barcode);
		List<ItemSku> skuList = itemSkuDao.queryByParam(params);
		return skuList;
	}

	@Override
	public List<ItemSku> selectSkuListBySkuCode(List<String> skuList) {
		if(CollectionUtils.isEmpty(skuList)){
			return null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sku in ("+StringUtil.join(skuList, Constant.SPLIT_SIGN.COMMA) + ")");
		return itemSkuDao.queryByParam(params);
	}

	@Override
	public ItemSku selectSkuInfoBySkuCode(String sku) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sku", sku);
		List<ItemSku> skuList = itemSkuDao.queryByParam(params);
		if(CollectionUtils.isEmpty(skuList)){
			return null;
		}
		return skuList.get(0);
	}

	@Override
	public ItemSku checQniqueSkuWithDetailIdAndSpId(ItemSku itemSku) {
		if(itemSku != null){
			Map<String,Object> params = BeanUtil.beanMap(itemSku);
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			List<ItemSku> skuList = itemSkuDao.queryByParam(params);
			if(CollectionUtils.isNotEmpty(skuList)){
				return skuList.get(0);
			}
		}
		return null;
	}

	@Override
	public ItemSku selectByIdFromMaster(Long id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		List<ItemSku> itemSkuList = itemSkuDao.queryByParam(params);
		if(CollectionUtils.isNotEmpty(itemSkuList)){
			return itemSkuList.get(0);
		}
		return null;
	}
	@Override
	public PageInfo<ItemResultDto> searchItemByQuery(ItemQuery query) {
		List<ItemResultDto> itemList = itemSkuDao.queryPageByQuery(query);
		if(CollectionUtils.isEmpty(itemList)){
			return new PageInfo<ItemResultDto>();
		}
		Integer count = itemSkuDao.queryCountByQuery(query);
		count = (count==null?0:count);
		PageInfo<ItemResultDto> page = new PageInfo<ItemResultDto>();
		page.setRows(itemList);
		page.setPage(query.getStartPage());
		page.setSize(query.getPageSize());
		page.setRecords(count);
		return page;
	}
	@Override
	public Integer updateSkuInfoWithItemSpuInfo(ItemSku updateLast) {
		return itemSkuDao.updateSkuInfoWithItemSpuInfo(updateLast);
	}
	@Override
	public PageInfo<ItemSku> queryAllLikedSkuByBySellerSkuQueryPage(
			SellerSkuQuery sellerSkuQuery, PageInfo<ItemSku> pageInfo) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("auditStatus", "S");
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		if(StringUtil.isNotBlank(sellerSkuQuery.getDetailName())){
			whereList.add(new DAOConstant.WHERE_ENTRY("detail_name",MYBATIS_SPECIAL_STRING.LIKE,sellerSkuQuery.getDetailName()));
		}
		if(StringUtil.isNotBlank(sellerSkuQuery.getUnitName())){
			whereList.add(new DAOConstant.WHERE_ENTRY("unit_name",MYBATIS_SPECIAL_STRING.LIKE,sellerSkuQuery.getUnitName()));
		}
		if(StringUtil.isNotBlank(sellerSkuQuery.getBrandName())){
			whereList.add(new DAOConstant.WHERE_ENTRY("brand_name",MYBATIS_SPECIAL_STRING.LIKE,sellerSkuQuery.getBrandName()));
		}
		if(CollectionUtils.isNotEmpty(sellerSkuQuery.getSupplierIds())){
			whereList.add(new DAOConstant.WHERE_ENTRY("sp_id",MYBATIS_SPECIAL_STRING.INLIST,sellerSkuQuery.getSupplierIds()));
		}
		if(null!=sellerSkuQuery.getCreateBeginTime()){//create_time
			whereList.add(new DAOConstant.WHERE_ENTRY("create_time",MYBATIS_SPECIAL_STRING.GT,sellerSkuQuery.getCreateBeginTime()));
		}
		if(null!=sellerSkuQuery.getCreateEndTime()){//create_time
			whereList.add(new DAOConstant.WHERE_ENTRY("create_time",MYBATIS_SPECIAL_STRING.LT,sellerSkuQuery.getCreateEndTime()));
		}
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		return super.queryPageByParam(params, pageInfo);
	}
	@Override
	public List<ItemQuery> checkSellerSkuWithAuditStatusAndLevel(ItemQuery searchQuery) {
		return itemSkuDao.checkSellerSkuWithAuditStatusAndLevel(searchQuery);
	}
	
	public ResultInfo<List<ItemSku>> queryItemListBySkus(List<String> skus){
		try{
			List<ItemSku> l = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(skus)){
				Map<String, Object> params = new HashMap<>();
				List<DAOConstant.WHERE_ENTRY> wehreList = new ArrayList<DAOConstant.WHERE_ENTRY>();
				wehreList.add(new DAOConstant.WHERE_ENTRY("sku", MYBATIS_SPECIAL_STRING.INLIST, skus));
				params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), wehreList);
				l = itemSkuDao.queryByParam(params);
			}
			return new ResultInfo<>(l);
		}catch(Exception e){
			return new ResultInfo<>(new FailInfo(e.getMessage()));
		}
	}
	
	
	public List<SkuDetailInfoDto> selectCommisionRateByListSkus(List<String> skuList){
		return itemSkuDao.selectCommisionRateByListSkus(skuList);
	}
	
	@Override
	public Integer updateTopicPrice(ItemSku itemSku) {
		logger.info("更新促销价格:{},{},{},{}",itemSku.getId(),itemSku.getSku(),itemSku.getTopicPrice(),itemSku.getUpdateUser());
		SkuDto skuDto = new SkuDto();
		ItemSku sku = itemSkuDao.queryById(itemSku.getId());
		skuDto.setSku(sku.getSku());
		skuDto.setId(itemSku.getId());
		skuDto.setXgPrice(itemSku.getTopicPrice());
		skuDto.setSendType(itemSku.getUpdateUser());
		List<SkuDto> skuDtoList = new ArrayList<SkuDto>();
		skuDtoList.add(skuDto);
		Integer result = itemSkuDao.updateNotNullById(itemSku);
		try{
			rabbitMqProducer.sendP2PMessage(MqMessageConstant.ITEM_SKU_MODIFY_TOPIC_PRICE, skuDtoList);
		}catch(Exception e){
			logger.error("修改促销价格发送消息失败\r\n{}", e);
		}
		return result;
	}
	
	@Override
	public Integer updateBatchPrice(List<String> skuList, Float discount, String userName) {
		logger.info("批量更新促销价格:{},{},{}",skuList,discount,userName);
		Integer result = itemSkuDao.updateBatchPrice(skuList,discount,userName);
		List<SkuDto> skuDtoList = itemSkuDao.querySkuDtoListForPromotion(skuList);
		for(SkuDto skuDto:skuDtoList){
			skuDto.setSendType(userName);
		}
		try{
			rabbitMqProducer.sendP2PMessage(MqMessageConstant.ITEM_SKU_MODIFY_TOPIC_PRICE, skuDtoList);
		}catch(Exception e){
			logger.error("修改促销价格发送消息失败\r\n{}", e);
		}
		return result;
	}

	@Override
	public List<ItemSku> queryByUpdateTime(Date updateTime) {
		return itemSkuDao.queryByUpdateTime(updateTime);
	}
}
