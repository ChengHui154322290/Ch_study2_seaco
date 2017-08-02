package com.tp.service.prd.openplantform;


import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
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
import com.tp.dto.prd.ItemOpenSaveDto;
import com.tp.dto.prd.enums.ItemDataSourceTypeEnum;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemCode;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.sup.SupplierInfo;
import com.tp.service.prd.openplantform.IItemPlatService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.StringUtil;

@Service(value="itemPlatService")
public class ItemPlatService implements IItemPlatService{

	
	private static final Logger  LOGGER = LoggerFactory.getLogger(ItemPlatService.class);
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
	@Autowired ItemSkuArtDao itemSkuArtDao;
	
	@Autowired
	private ItemAttributeDao itemAttributeDao;
	
	@Autowired
	private ISupplierInfoService supplierInfoService;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String savePushItemInfoFromPlatSpuLevel(ItemOpenSaveDto saveDto)
			throws ItemServiceException {
		
		
		Long itemId=0l;
		if(saveDto != null)
		{
			
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
			Long skuId= 0l;
			String prdid ="";
			if(saveDto.getItemDetail() != null){
				saveDto.getItemDetail().setId(null);
				prdid = getPrdidCode(spu);
				saveDto.getItemDetail().setPrdid(prdid);
				saveDto.getItemDetail().setItemId(itemId);
				saveDto.getItemDetail().setSpu(saveDto.getItemInfo().getSpu());
				
				itemDetailDao.insert(saveDto.getItemDetail());
				detailId  = saveDto.getItemDetail().getId();
				saveDto.getItemDetail().setPrdid(prdid);
				
			}
			String skuCode = null;
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
				ItemSku  skuDO = saveDto.getItemSku();
				skuDO.setItemId(itemId);
				
				skuDO.setBrandId(saveDto.getItemInfo().getBrandId());
				skuDO.setSpu(saveDto.getItemInfo().getSpu());
				skuDO.setDetailName(saveDto.getItemDetail().getMainTitle());
				
				skuDO.setItemType(saveDto.getItemDetail().getItemType());
				skuDO.setPrdid(prdid);
				skuDO.setDetailId(detailId);
				
				skuDO.setBarcode(saveDto.getItemDetail().getBarcode());
				skuCode = getSkuCode(prdid);
				skuDO.setSku(skuCode);
				skuDO.setStatus(saveDto.getItemDetail().getStatus());
				
				skuDO.setSaleType(ItemSaleTypeEnum.SELLER.getValue());
				skuDO.setSort(100);
				
				skuDO.setSpId(saveDto.getItemSku().getSpId());
				SupplierInfo supplierDO = supplierInfoService.queryById(saveDto.getItemSku().getSpId());
				if(supplierDO != null){
					skuDO.setSpCode(supplierDO.getSupplierCode());
					skuDO.setSpName(supplierDO.getName());
				}
				skuDO.setCreateTime(new Date());
				skuDO.setCreateUser(saveDto.getItemDetail().getCreateUser());
				skuDO.setUpdateUser(saveDto.getItemDesc().getCreateUser());
				skuDO.setUpdateTime(new Date());
				LOGGER.info(JSONObject.toJSONString(skuDO));
				itemSkuDao.insert(skuDO);
				skuId = skuDO.getId();
				saveDto.getItemSku().setId(skuId);
				
				if(saveDto.getItemSkuArt() != null){
					ItemSkuArt art = saveDto.getItemSkuArt();
					art.setCreateTime(new Date());
					art.setSkuId(skuId);
					art.setSku(skuCode);
					itemSkuArtDao.insert(art);
				}
				
			}
		
			return skuCode;
		}
		return  null;
		
	}
		
	
	
		public String getSpuCode(String smallCode) throws ItemServiceException {
			return getUniqueCode(smallCode,1);
		}
		
		public String getPrdidCode(String spuCode) throws ItemServiceException {
			return getUniqueCode(spuCode,2);
		}

		public String getSkuCode(String prdIdCode) throws ItemServiceException {
			return getUniqueCode(prdIdCode,3);
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
		 * @throws ItemServiceException
		 */
		private String getUniqueCode(String code,int type) throws ItemServiceException {
			
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
			
			if(StringUtil.isNullOrEmpty(code)){
				throw new ItemServiceException(errorMsg);
			}
			ItemCode codeDO = new ItemCode();
			codeDO.setCode(code);
			int value = 0;
			try {
				List<ItemCode> list  =itemCodeDao.queryByObject(codeDO);
				if(CollectionUtils.isEmpty(list)){
					codeDO.setValue(ItemConstant.CODE_INIT_VALUE);
					itemCodeDao.insert(codeDO);
				}else{
					itemCodeDao.updateCode(code);
				}
				List<ItemCode> listCode  =itemCodeDao.queryByObject(codeDO);
				if(CollectionUtils.isNotEmpty(listCode)){
					value = listCode.get(0).getValue();
				}else{
					throw new ItemServiceException("获取商品编码失败");
				}
				
				
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
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



		@Override
		@Transactional(propagation=Propagation.REQUIRED)
		public String savePushItemInfoFromPlatPRDLevel(ItemOpenSaveDto saveDto)
				throws ItemServiceException {
			//2保存detail
			Long itemId = saveDto.getItemInfo().getId();
			Long detailId = 0l;
			Long skuId= 0l;
			String prdid ="";
			if(saveDto.getItemDetail() != null){
				saveDto.getItemDetail().setId(null);
				prdid = getPrdidCode(saveDto.getItemInfo().getSpu());
				saveDto.getItemDetail().setPrdid(prdid);
				saveDto.getItemDetail().setItemId(itemId);
				saveDto.getItemDetail().setSpu(saveDto.getItemInfo().getSpu());
				
				itemDetailDao.insert(saveDto.getItemDetail());
				detailId  = saveDto.getItemDetail().getId();
				saveDto.getItemDetail().setPrdid(prdid);
				
			}
			String skuCode = null;
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
				ItemSku  skuDO = saveDto.getItemSku();
				skuDO.setItemId(itemId);
				
				skuDO.setBrandId(saveDto.getItemInfo().getBrandId());
				skuDO.setSpu(saveDto.getItemInfo().getSpu());
				skuDO.setDetailName(saveDto.getItemDetail().getMainTitle());
				
				skuDO.setItemType(saveDto.getItemDetail().getItemType());
				skuDO.setPrdid(prdid);
				skuDO.setDetailId(detailId);
				
				skuDO.setBarcode(saveDto.getItemDetail().getBarcode());
				skuCode = getSkuCode(prdid);
				skuDO.setSku(skuCode);
				skuDO.setStatus(saveDto.getItemDetail().getStatus());
				
				skuDO.setSaleType(ItemSaleTypeEnum.SELLER.getValue());
				skuDO.setSort(100);
				
				
				skuDO.setSpId(saveDto.getItemSku().getSpId());
				SupplierInfo supplierDO = supplierInfoService.queryById(saveDto.getItemSku().getSpId());
				if(supplierDO != null){
					skuDO.setSpCode(supplierDO.getSupplierCode());
					skuDO.setSpName(supplierDO.getName());
				}
				skuDO.setCreateTime(new Date());
				skuDO.setCreateUser(saveDto.getItemDetail().getCreateUser());
				skuDO.setUpdateTime(new Date());
				skuDO.setUpdateUser(saveDto.getItemDetail().getCreateUser());
				skuDO.setDataSource(ItemDataSourceTypeEnum.SELLER.getCode());
				
				LOGGER.info(JSONObject.toJSONString(skuDO));
				itemSkuDao.insert(skuDO);
				skuId = skuDO.getId();
				saveDto.getItemSku().setId(skuId);
			}
			
			
			return 	skuCode ;
		}
}
