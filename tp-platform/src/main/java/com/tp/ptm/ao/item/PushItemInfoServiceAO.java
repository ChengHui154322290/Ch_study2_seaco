package com.tp.ptm.ao.item;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qiniu.QiniuUpload;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.tp.common.vo.Constant;
import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.dto.prd.ItemOpenSaveDto;
import com.tp.dto.prd.PushItemCostpriceDto;
import com.tp.dto.prd.PushItemDetailDto;
import com.tp.dto.prd.PushItemInfoAndDetailDto;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.prd.enums.ItemTypesEnum;
import com.tp.dto.ptm.ReturnData;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.Spec;
import com.tp.model.bse.TaxRate;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemPushLog;
import com.tp.model.prd.ItemSku;
import com.tp.model.ptm.PlatformAccount;
import com.tp.model.ptm.PlatformSupplierRelation;
import com.tp.model.usr.UserInfo;
import com.tp.result.bse.SpecGroupResult;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.ISpecGroupLinkService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.bse.ITaxRateService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.prd.IItemPushLogService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.prd.openplantform.IItemPlatService;
import com.tp.service.ptm.IPlatformAccountService;
import com.tp.service.ptm.IPlatformSupplierRelationService;
import com.tp.service.usr.IUserInfoService;

@Service
public class PushItemInfoServiceAO {
	
	private static final Logger logger = LoggerFactory.getLogger(PushItemInfoServiceAO.class);
	@Value("${upload.tmp.path}")
	private String uploadTempPath;
	
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	@Autowired
	private IBrandService brandService;
	@Autowired
	private ITaxRateService taxRateService;
	@Autowired
	private IDistrictInfoService districtInfoService;
//	@Autowired
//	private DfsService dfsService;
	@Autowired
	private IItemManageService itemManageService;
	@Autowired
	private ISpecGroupLinkService specGroupLinkService;
	@Autowired
	private ISpecGroupService specGroupService;
	
	@Autowired
	private IItemPlatService itemPlatService;
	@Autowired
	private IPlatformSupplierRelationService platformSupplierRelationService;
	@Autowired
	private IMemberInfoService memberInfoService;
	
	@Autowired
	private IItemSkuService itemSkuService;
	@Autowired
	private IItemPushLogService itemPushLogService;
	
	public ReturnData pushItemInfo(PushItemInfoAndDetailDto pushItemInfoAndDetailDto, HttpServletRequest request,Long currentUserId, String appKey) {
		
		//数据转换
		ReturnData  converResult = this.convertPushItemInfoAndDetailDto(pushItemInfoAndDetailDto);
		if(converResult.getIsSuccess().booleanValue() ==false){
			return converResult;
		}
		
		//数据格式的初步校验
		ReturnData returnData=this.validateushItemInfoAndDetailDto(pushItemInfoAndDetailDto,currentUserId);
		if(returnData.getIsSuccess().booleanValue() ==false){
			return returnData;
		}
		
//		//图片转存
//		ReturnData downLoadImageResult =this.loadImageFormRometeToLocal(pushItemInfoAndDetailDto,request);
//		if(downLoadImageResult.getIsSuccess().booleanValue() ==false){
//			return  downLoadImageResult;
//		}
		//封装数据插入item库
		ItemInfo infoDO = null;
		try {
			infoDO = itemManageService.checkSpuExsit(pushItemInfoAndDetailDto.getSmallId(), pushItemInfoAndDetailDto.getBrandId(),
					pushItemInfoAndDetailDto.getUnitId(),pushItemInfoAndDetailDto.getSpuName(), null);
		} catch (ItemServiceException e1) {
			e1.printStackTrace();
			logger.error(e1.getMessage());
			return  new ReturnData(Boolean.FALSE, 0,"系统异常联系管理员");
		}
		if(null==infoDO){		
			boolean checkBarcodeExsit = itemManageService.checkBarcodeExsit(pushItemInfoAndDetailDto.getItemDetail().getBarcode(), null, null);
			if(!checkBarcodeExsit){
				return  new ReturnData(Boolean.FALSE, 500500,"条形码在西客系统中已存在");
			}
			//spu级别的赋值
			ItemOpenSaveDto itemOpenSaveDto = new ItemOpenSaveDto();
			this.spuLevelSetParams(itemOpenSaveDto,pushItemInfoAndDetailDto,currentUserId,appKey);				
			try {
				itemOpenSaveDto.getItemDetail().setSupplierId(currentUserId);
				String sku = itemPlatService.savePushItemInfoFromPlatSpuLevel(itemOpenSaveDto);
				if( sku==null){
					return  new ReturnData(Boolean.FALSE, 500501,"插入失败");
				}  else {
					 return new ReturnData(Boolean.TRUE,"{\"sku\": "+"\""+sku+"\""+"}");
				}
				 
			} catch (Exception e) {
				logger.info("新增spu级别的插入失败");
				logger.error(e.getMessage());
				return  new ReturnData(Boolean.FALSE, 500504,"插入失败");
			}

		} else {
			
			boolean checkBarcode = itemManageService.checkBarcodeExsit(pushItemInfoAndDetailDto.getItemDetail().getBarcode(), null,infoDO.getId());
			//prdid规格校验
			int flag = itemManageService.checkPrdidSpec(infoDO.getId(), pushItemInfoAndDetailDto.getSpecInfosMap());
			
			if(checkBarcode){
				if(flag!=1){
					logger.info("已存在的spu级别的插入,barcode不存在,flag不为1,flag的值为:"+flag);
					return  new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code,"插入失败");
				}else{
					//go
					ItemOpenSaveDto itemOpenSaveDto = new ItemOpenSaveDto();
					this.prdidLevelSetParams(itemOpenSaveDto,pushItemInfoAndDetailDto,infoDO,currentUserId,appKey);	
					try {
						String sku = itemPlatService.savePushItemInfoFromPlatPRDLevel(itemOpenSaveDto);
						if(null==sku){
							logger.info("可能规格已经存在，无法插入");
							return  new ReturnData(Boolean.FALSE, 500503,"插入失败");
						}  else {
							 logger.info("插入成功");
							 return new ReturnData(Boolean.TRUE,"{\"sku\": "+"\""+sku+"\""+"}");
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					    logger.info("已存在的spu级别的插入失败");
						return  new ReturnData(Boolean.FALSE, 500504,"已存在的spu级别的插入失败");
					}
				}
			} else {
				  logger.info("插入失败,商品已经存在");
				return  new ReturnData(Boolean.FALSE, 500500,"商品已经存在");
			}
			
			
		}
	}

	

	
	private void prdidLevelSetParams(ItemOpenSaveDto itemOpenSaveDto,PushItemInfoAndDetailDto pushItemInfoAndDetailDto, ItemInfo infoDO,Long currentUserId, String appKey) {
		//商品描述
		ItemDesc  desc= new ItemDesc();
		desc.setDescription(pushItemInfoAndDetailDto.getItemDesc());
		desc.setCreateTime(new Date());
		desc.setCreateUser(currentUserId.toString());
		desc.setUpdateTime(new Date());
		desc.setUpdateUser(currentUserId.toString());
		itemOpenSaveDto.setItemDesc(desc);
		
		//移动描述
		ItemDescMobile descMobileDO = new ItemDescMobile();
		descMobileDO.setCreateTime(new Date());
		descMobileDO.setDescription(pushItemInfoAndDetailDto.getDescMobile());
		descMobileDO.setCreateUser(currentUserId.toString());
		descMobileDO.setUpdateTime(new Date());
		descMobileDO.setUpdateUser(currentUserId.toString());
		itemOpenSaveDto.setItemDescMobile(descMobileDO);
		
		
	    Category cate = categoryService.queryById(pushItemInfoAndDetailDto.getSmallId());
		//detail
		ItemDetail detailDO = new  ItemDetail();
		String[] ignoreString ={"tarrifRate","volumeWidth","volumeLength","volumeHigh"};
		BeanUtils.copyProperties(pushItemInfoAndDetailDto.getItemDetail(), detailDO,ignoreString);
		detailDO.setStatus(ItemStatusEnum.OFFLINE.getValue());
		detailDO.setTarrifRate(pushItemInfoAndDetailDto.getItemDetail().getTarrifRateId());
		//新增
		detailDO.setAddedValueRate(pushItemInfoAndDetailDto.getItemDetail().getAddValueRateId());
		detailDO.setExciseRate(pushItemInfoAndDetailDto.getItemDetail().getExciseRateId());
		detailDO.setCustomsRate(pushItemInfoAndDetailDto.getItemDetail().getCustomsRateId());
		
		detailDO.setCreateTime(new Date());
		detailDO.setUpdateTime(new Date());
		detailDO.setCreateUser(currentUserId.toString());
		detailDO.setUpdateUser(currentUserId.toString());
		detailDO.setUnitId(pushItemInfoAndDetailDto.getUnitId());
		detailDO.setItemTitle(pushItemInfoAndDetailDto.getSpuName());
		detailDO.setSpuName(pushItemInfoAndDetailDto.getSpuName());
		detailDO.setBrandId(pushItemInfoAndDetailDto.getBrandId());
		detailDO.setCategoryCode(cate.getCode());
	 	if(pushItemInfoAndDetailDto.getItemDetail().getVolumeHigh()!=null && pushItemInfoAndDetailDto.getItemDetail().getVolumeHigh().intValue()!=0 ){
	   		detailDO.setVolumeHigh(pushItemInfoAndDetailDto.getItemDetail().getVolumeHigh().intValue());
	   	}
	   	if(pushItemInfoAndDetailDto.getItemDetail().getVolumeLength()!=null && pushItemInfoAndDetailDto.getItemDetail().getVolumeLength().intValue()!=0 ){
	   		detailDO.setVolumeLength(pushItemInfoAndDetailDto.getItemDetail().getVolumeLength().intValue());
	   	}
	   	if(pushItemInfoAndDetailDto.getItemDetail().getVolumeWidth()!=null && pushItemInfoAndDetailDto.getItemDetail().getVolumeWidth().intValue()!=0 ){
	   		detailDO.setVolumeWidth(pushItemInfoAndDetailDto.getItemDetail().getVolumeWidth().intValue());
	   	}
		itemOpenSaveDto.setItemDetail(detailDO);
		
		
		// List<AttributeDO>
		if(MapUtils.isNotEmpty(pushItemInfoAndDetailDto.getItemAttrMap())){
			List<ItemAttribute> attributeList = new ArrayList<ItemAttribute>();
			Iterator<Entry<String, String>> iterator = pushItemInfoAndDetailDto.getItemAttrMap().entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<java.lang.String, java.lang.String> entry = (Map.Entry<java.lang.String, java.lang.String>) iterator.next();
				ItemAttribute attrDo= new ItemAttribute();
				attrDo.setAttrKey(entry.getKey());
				attrDo.setAttrValue(entry.getValue());
				attrDo.setCustom(1);
				attributeList.add(attrDo);
			}
			itemOpenSaveDto.setAttributeList(attributeList);
		}
		
		//listDetailSpec
		List<ItemDetailSpec> listDetailSpec = new ArrayList<ItemDetailSpec>();
		if(MapUtils.isNotEmpty(pushItemInfoAndDetailDto.getSpecInfosMap())){
			Iterator<Entry<Long, Long>> iterator = pushItemInfoAndDetailDto.getSpecInfosMap().entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<java.lang.Long, java.lang.Long> entry = (Map.Entry<java.lang.Long, java.lang.Long>) iterator.next();
				ItemDetailSpec detailSpecDO = new ItemDetailSpec();
				detailSpecDO.setSpecGroupId(entry.getKey());	
				detailSpecDO.setSpecId(entry.getValue());
				detailSpecDO.setSort(1);
				detailSpecDO.setCreateTime(new Date());
				detailSpecDO.setUpdateTime(new Date());
				listDetailSpec.add(detailSpecDO);		
	       }
		} else {
			ItemDetailSpec detailSpecDO = new ItemDetailSpec();
			detailSpecDO.setSpecGroupId(-1l);
			detailSpecDO.setSpecId(-1l);
			detailSpecDO.setSort(1);
			detailSpecDO.setCreateTime(new Date());
			detailSpecDO.setUpdateTime(new Date());
			listDetailSpec.add(detailSpecDO);
		}
		itemOpenSaveDto.setListDetailSpec(listDetailSpec);
		
		//info
		itemOpenSaveDto.setItemInfo(infoDO);
		
		//picture
		List<String> picListPath = pushItemInfoAndDetailDto.getPicListPath();
		if(CollectionUtils.isNotEmpty(picListPath)){
			List<ItemPictures> picturesDOs = new ArrayList<ItemPictures>();
			int i=0;
			for (String picStr : picListPath) {
				ItemPictures picDo = new ItemPictures();
				if(i==0){
					picDo.setMain(Constant.DEFAULTED.YES);
				} else {
					picDo.setMain(Constant.DEFAULTED.NO);
				}
				i++;
				picDo.setPicture(picStr);
				picDo.setCreateTime(new Date());
				picDo.setCreateUser(currentUserId.toString());
				picturesDOs.add(picDo);
			}
			itemOpenSaveDto.setListPic(picturesDOs);
		}
		
	
			
		//sku
		ItemSku skuDo = new ItemSku();
		skuDo.setSpId(1L);
		skuDo.setStatus(0);
		skuDo.setUnitId(pushItemInfoAndDetailDto.getUnitId());
		skuDo.setCategoryCode(cate.getCode());
		skuDo.setCategoryId(pushItemInfoAndDetailDto.getSmallId());
		skuDo.setItemType(ItemTypesEnum.NORMAL.getValue());
		skuDo.setBasicPrice(pushItemInfoAndDetailDto.getItemDetail().getBasicPrice());
		skuDo.setWavesSign(pushItemInfoAndDetailDto.getItemDetail().getWavesSign());
		skuDo.setSpuName(pushItemInfoAndDetailDto.getSpuName());
		skuDo.setCreateUser(currentUserId.toString());
		skuDo.setSpId(this.getSupplierId(appKey));
		
		skuDo.setGoodsCode(pushItemInfoAndDetailDto.getGoodsCode());
		skuDo.setArticleCode(pushItemInfoAndDetailDto.getArticleCode());
		
		itemOpenSaveDto.setItemSku(skuDo);
	}




	private void spuLevelSetParams(ItemOpenSaveDto itemOpenSaveDto,PushItemInfoAndDetailDto pushItemInfoAndDetailDto, Long currentUserId, String appKey) {
		//商品描述
		ItemDesc  desc= new ItemDesc();
		desc.setDescription(pushItemInfoAndDetailDto.getItemDesc());
		desc.setCreateTime(new Date());
		desc.setCreateUser(currentUserId.toString());
		desc.setUpdateTime(new Date());
		desc.setUpdateUser(currentUserId.toString());
		itemOpenSaveDto.setItemDesc(desc);
		
		//移动描述
		ItemDescMobile descMobileDO = new ItemDescMobile();
		descMobileDO.setCreateTime(new Date());
		descMobileDO.setDescription(pushItemInfoAndDetailDto.getDescMobile());
		descMobileDO.setCreateUser(currentUserId.toString());
		descMobileDO.setUpdateTime(new Date());
		descMobileDO.setUpdateUser(currentUserId.toString());
		itemOpenSaveDto.setItemDescMobile(descMobileDO);
		
		
		
		//类别
		List<Category> parentCategoryList = categoryService.getParentCategoryList(pushItemInfoAndDetailDto.getSmallId());
		if(CollectionUtils.isEmpty(parentCategoryList) || parentCategoryList.size() != 3){
			logger.error("根据小类id未获取到3级类目list");
		}
		//detail
		ItemDetail detailDO = new ItemDetail();
		String[] ignoreString ={"tarrifRate","volumeWidth","volumeLength","volumeHigh"};
		BeanUtils.copyProperties(pushItemInfoAndDetailDto.getItemDetail(), detailDO,ignoreString);
		detailDO.setTarrifRate(pushItemInfoAndDetailDto.getItemDetail().getTarrifRateId());
		//新增税率
		detailDO.setAddedValueRate(pushItemInfoAndDetailDto.getItemDetail().getAddValueRateId());
		detailDO.setExciseRate(pushItemInfoAndDetailDto.getItemDetail().getExciseRateId());
		detailDO.setCustomsRate(pushItemInfoAndDetailDto.getItemDetail().getCustomsRateId());
		
		detailDO.setStatus(ItemStatusEnum.OFFLINE.getValue());
		detailDO.setCreateTime(new Date());
		detailDO.setUpdateTime(new Date());
		detailDO.setCreateUser(currentUserId.toString());
		detailDO.setUpdateUser(currentUserId.toString());
		detailDO.setUnitId(pushItemInfoAndDetailDto.getUnitId());
		detailDO.setItemTitle(pushItemInfoAndDetailDto.getSpuName());
		detailDO.setSpuName(pushItemInfoAndDetailDto.getSpuName());
		detailDO.setBrandId(pushItemInfoAndDetailDto.getBrandId());
		detailDO.setCategoryCode(parentCategoryList.get(2).getCode());
	   	if(pushItemInfoAndDetailDto.getItemDetail().getVolumeHigh()!=null && pushItemInfoAndDetailDto.getItemDetail().getVolumeHigh().intValue()!=0 ){
	   		detailDO.setVolumeHigh(pushItemInfoAndDetailDto.getItemDetail().getVolumeHigh().intValue());
	   	}
	   	if(pushItemInfoAndDetailDto.getItemDetail().getVolumeLength()!=null && pushItemInfoAndDetailDto.getItemDetail().getVolumeLength().intValue()!=0 ){
	   		detailDO.setVolumeLength(pushItemInfoAndDetailDto.getItemDetail().getVolumeLength().intValue());
	   	}
	   	if(pushItemInfoAndDetailDto.getItemDetail().getVolumeWidth()!=null && pushItemInfoAndDetailDto.getItemDetail().getVolumeWidth().intValue()!=0 ){
	   		detailDO.setVolumeWidth(pushItemInfoAndDetailDto.getItemDetail().getVolumeWidth().intValue());
	   	}
		itemOpenSaveDto.setItemDetail(detailDO);
		
		
		// List<AttributeDO>
		if(MapUtils.isNotEmpty(pushItemInfoAndDetailDto.getItemAttrMap())){
			List<ItemAttribute> attributeList = new ArrayList<ItemAttribute>();
			Iterator<Entry<String, String>> iterator = pushItemInfoAndDetailDto.getItemAttrMap().entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<java.lang.String, java.lang.String> entry = (Map.Entry<java.lang.String, java.lang.String>) iterator.next();
				ItemAttribute attrDo= new ItemAttribute();
				attrDo.setAttrKey(entry.getKey());
				attrDo.setAttrValue(entry.getValue());
				attrDo.setCustom(1);
				attributeList.add(attrDo);
			}
			itemOpenSaveDto.setAttributeList(attributeList);
		}
		
		//listDetailSpec
		List<ItemDetailSpec> listDetailSpec = new ArrayList<ItemDetailSpec>();
		if(MapUtils.isNotEmpty(pushItemInfoAndDetailDto.getSpecInfosMap())){
			int i=0;
			Iterator<Entry<Long, Long>> iterator = pushItemInfoAndDetailDto.getSpecInfosMap().entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Long, java.lang.Long> entry = (Map.Entry<java.lang.Long, java.lang.Long>) iterator.next();
				ItemDetailSpec detailSpecDO = new ItemDetailSpec();
				detailSpecDO.setSpecGroupId(entry.getKey());	
				detailSpecDO.setSpecId(entry.getValue());
				detailSpecDO.setCreateTime(new Date());
				detailSpecDO.setSort(i++);
				listDetailSpec.add(detailSpecDO);		
	       }
		} else {
			ItemDetailSpec detailSpecDO = new ItemDetailSpec();
			detailSpecDO.setSpecGroupId(-1l);
			detailSpecDO.setSpecId(-1l);
			detailSpecDO.setSort(1);
			detailSpecDO.setCreateTime(new Date());
			detailSpecDO.setUpdateTime(new Date());
			listDetailSpec.add(detailSpecDO);
		}
		itemOpenSaveDto.setListDetailSpec(listDetailSpec);

		//info
		ItemInfo  infoDO = new ItemInfo();
		infoDO.setLargeId(parentCategoryList.get(0).getId());
		infoDO.setMediumId(parentCategoryList.get(1).getId());
		infoDO.setSmallId(pushItemInfoAndDetailDto.getSmallId());
		infoDO.setBrandId(pushItemInfoAndDetailDto.getBrandId());
		infoDO.setMainTitle(pushItemInfoAndDetailDto.getSpuName());
		infoDO.setSmallCode(parentCategoryList.get(2).getCode());
		infoDO.setRemark(pushItemInfoAndDetailDto.getRemark());
		infoDO.setUnitId(pushItemInfoAndDetailDto.getUnitId());
		infoDO.setCreateTime(new Date());
		infoDO.setCreateUser(currentUserId.toString());
		infoDO.setUpdateTime(new Date());
		infoDO.setUpdateUser(currentUserId.toString());
		itemOpenSaveDto.setItemInfo(infoDO);
		
		//picture
		List<String> picListPath = pushItemInfoAndDetailDto.getPicListPath();
		if(CollectionUtils.isNotEmpty(picListPath)){
			List<ItemPictures> picturesDOs = new ArrayList<ItemPictures>();
			int i=0;
			for (String picStr : picListPath) {
				ItemPictures picDo = new ItemPictures();
				if(i==0){
					picDo.setMain(Constant.DEFAULTED.YES);
				} else {
					picDo.setMain(Constant.DEFAULTED.NO);
				}
				i++;
				picDo.setPicture(picStr);
				picDo.setCreateTime(new Date());
				picDo.setCreateUser(currentUserId.toString());
				picturesDOs.add(picDo);
			}
			itemOpenSaveDto.setListPic(picturesDOs);
		}
		
		
		//sku
		ItemSku skuDo = new ItemSku();
		skuDo.setSpId(1L);
		skuDo.setStatus(0);
		skuDo.setUnitId(pushItemInfoAndDetailDto.getUnitId());
		skuDo.setCategoryCode(parentCategoryList.get(2).getCode());
		skuDo.setCategoryId(pushItemInfoAndDetailDto.getSmallId());
		skuDo.setItemType(ItemTypesEnum.NORMAL.getValue());
		skuDo.setBasicPrice(pushItemInfoAndDetailDto.getItemDetail().getBasicPrice());
		skuDo.setWavesSign(pushItemInfoAndDetailDto.getItemDetail().getWavesSign());
		skuDo.setSpuName(pushItemInfoAndDetailDto.getSpuName());
		skuDo.setCreateUser(currentUserId.toString());
		skuDo.setSpId(this.getSupplierId(appKey));
		skuDo.setSupplierStock(pushItemInfoAndDetailDto.getItemDetail().getInitStock());
		
		skuDo.setGoodsCode(pushItemInfoAndDetailDto.getGoodsCode());
		skuDo.setArticleCode(pushItemInfoAndDetailDto.getArticleCode());
		
		itemOpenSaveDto.setItemSku(skuDo);
		
		if(pushItemInfoAndDetailDto.getCustomsInfo() != null 
				&& StringUtils.isNotEmpty(pushItemInfoAndDetailDto.getCustomsInfo().getArticleNumber())
				&& StringUtils.isNotEmpty(pushItemInfoAndDetailDto.getCustomsInfo().getHsCode())
				&& StringUtils.isNotEmpty(pushItemInfoAndDetailDto.getCustomsInfo().getItemDeclareName())
				&& null != pushItemInfoAndDetailDto.getCustomsInfo().getBondedArea()) {
			itemOpenSaveDto.setItemSkuArt(pushItemInfoAndDetailDto.getCustomsInfo());
		}
		
	}




	private ReturnData loadImageFormRometeToLocal(PushItemInfoAndDetailDto pushItemInfoAndDetailDto, HttpServletRequest request) {
		String savePath = request.getSession().getServletContext().getRealPath(uploadTempPath);
    	List<String>  dfsPicAddr= new ArrayList<String>();
		if(StringUtils.isBlank(savePath)){
			logger.error("图片上传路径配置错误");
			return new ReturnData(Boolean.FALSE, 500200,"图片上传路径配置错误");	
		}
		List<String> picListPath = pushItemInfoAndDetailDto.getPicListPath();
        List<String> dic = new ArrayList<String>(Arrays.asList(new String[picListPath.size()]));  
        Collections.copy(dic, picListPath);  
        if(CollectionUtils.isNotEmpty(dic)){
        	for (String str : dic) {
				if(null == str){
					continue;
				}
		  String fileName = str.substring(str.lastIndexOf("/")+1);	
		  if(fileName.contains("?")){  
			  fileName=fileName.substring(0, fileName.indexOf("?"));
		  }
		      HttpURLConnection connection=null;
		      DataInputStream in= null;
		      DataOutputStream out =null;
		    try {
	             URL url = new URL(str);
	             connection = (HttpURLConnection) url.openConnection();
	             in = new DataInputStream(connection.getInputStream());    
	             File localFile = new File(savePath);
	             if(!localFile.exists()){
	 				Boolean b = localFile.mkdirs();
	 				if(!b) {
	 					logger.error("创建文件夹失败"+localFile);
	 					return new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code,"图片下载失败");	
	 				}
	 			}
	             out = new DataOutputStream(new FileOutputStream(savePath+fileName));  
	             byte[] buffer = new byte[4096];
	             int count = 0;
	              while ((count = in.read(buffer)) > 0)  {
	                out.write(buffer, 0, count);
	              }
	            out.close();
	            in.close();
	            connection.disconnect();
	            File localPic = new File(savePath+fileName);
	            if(!localPic.exists()){
	            	return new ReturnData(Boolean.FALSE, 500201,"图片下载失败");	
	            }
	            String uploadPic = this.uploadPic(localPic);
	            if(StringUtils.isBlank(uploadPic)){
	            	logger.error("图片上传dfs失败");
	            	return new ReturnData(Boolean.FALSE, 500202,"图片上传dfs失败");	
	            }
	            dfsPicAddr.add(uploadPic);
	        } catch (Exception e) {
				return new ReturnData(Boolean.FALSE, 500201,"图片下载失败");	
	        } finally {
	        	if(out !=null){
	        		try {
						out.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
	        	}
	        	if(in !=null){
	        		try {
						in.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
	        	}
	        }
				
			}
        }
        pushItemInfoAndDetailDto.setPicListPath(dfsPicAddr);
        return new ReturnData(Boolean.TRUE);
	}

	/**
	 * 
	 * <pre>
	 *  上传图片到dfs
	 * </pre>
	 *
	 * @param picture
	 * @return String
	 */
	private String uploadPic(File file) throws QiniuException {
		String format = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		String targetName = UUID.randomUUID().toString().replaceAll("-", "");
		Response response= uploader.uploadFile(file.getAbsolutePath(), targetName+"."+format,Constant.IMAGE_URL_TYPE.item.name());
		if(response.isOK()){
			return targetName+"."+format;
		}
		return  null;
	}
	@Autowired
	private QiniuUpload uploader;

	/**
	 *  数据的格式的初步校验
	 * @param pushItemInfoAndDetailDto
	 * @return
	 */
	
	private ReturnData validateushItemInfoAndDetailDto(PushItemInfoAndDetailDto pushItemInfoAndDetailDto,Long currentUserId) {
		
		//小类校验
		Long smallId = pushItemInfoAndDetailDto.getSmallId();
		if(null ==smallId){
			return new ReturnData(Boolean.FALSE, 500103,"小类不能为空");
		}
		
		Category cateDo = categoryService.queryById(smallId);
		if(null == cateDo ||  cateDo.getLevel() !=3 || Constant.ENABLED.NO.equals(cateDo.getStatus())){
			return new ReturnData(Boolean.FALSE, 500104,"不是小类id,或者为无效小类");	
		}
		
		//spu名称校验
		String spuName = pushItemInfoAndDetailDto.getSpuName();
		if(StringUtils.isBlank(spuName)){
			return new ReturnData(Boolean.FALSE, 500105,"spuName不能为空");		
		}
		
		//品牌校验
		Long brandId = pushItemInfoAndDetailDto.getBrandId();
		if(null ==brandId){
			return new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code,"品牌id不能为空");
		}
		Brand brand = brandService.queryById(brandId);
		if(brand == null){
			return new ReturnData(Boolean.FALSE, 500106,"品牌信息不存在");
		}
//		
		//品牌校验
//		String brandName = pushItemInfoAndDetailDto.getBrandName();
//		if(StringUtils.isBlank(brandName)){
//			return new ReturnData(Boolean.FALSE, 500106,"品牌名称不能为空");
//		}
//		Brand query = new Brand();
//		Date date= new Date();
//		query.setName(brandName);
//		query.setStatus(Constant.ENABLED.YES);
//		query.setCreateTime(date);
//		query.setModifyTime(date);
//		query.setCountryId(196);
//	    Long brandId = brandService.selectIdByName(brandName.trim(), currentUserId);
//	    pushItemInfoAndDetailDto.setBrandId(brandId);
		//单位校验
		Long unitId = pushItemInfoAndDetailDto.getUnitId();
		if(null ==unitId){
			return new ReturnData(Boolean.FALSE, 500107,"单位id不能为空");
		}
           
		DictionaryInfo unitInfo = dictionaryInfoService.queryById(unitId);
		if(null ==unitInfo){
			return new ReturnData(Boolean.FALSE, 500108,"单位不存在");
		}
		
		
		
		
		//手机端富文本框校验
		String descMobile = pushItemInfoAndDetailDto.getDescMobile();
		if(StringUtils.isBlank(descMobile)){
			return new ReturnData(Boolean.FALSE, 500109,"商品的富文本描述(手机端)不能为空");		
		}
		//富文本框校验
		String itemDesc = pushItemInfoAndDetailDto.getItemDesc();
		if(StringUtils.isBlank(itemDesc)){
			pushItemInfoAndDetailDto.setItemDesc(descMobile);		
		}	
		//校验规格组
		Map<Long, Long> specInfosMap = pushItemInfoAndDetailDto.getSpecInfosMap();
		if(MapUtils.isNotEmpty(specInfosMap)){	 
			List<SpecGroupResult>  specGroupList = specGroupService.getSpecGroupResultByCategoryId(pushItemInfoAndDetailDto.getSmallId());
			Map<Long,ArrayList<Long>> specIds = new HashMap<Long,ArrayList<Long>>();
			if(CollectionUtils.isNotEmpty(specGroupList)){
				for(SpecGroupResult specGroupResult : specGroupList){
					ArrayList<Long> ids = new ArrayList<Long>();
					if(CollectionUtils.isNotEmpty(specGroupResult.getSpecDoList())){
						for(Spec specDO : specGroupResult.getSpecDoList()){
							ids.add(specDO.getId());
						}
					}
					specIds.put(specGroupResult.getSpecGroup().getId(),ids);
				}
			}
			Iterator<Entry<Long, Long>> iterator = specInfosMap.entrySet().iterator();
			 while (iterator.hasNext()) {
				Map.Entry<java.lang.Long, java.lang.Long> entry = (Map.Entry<java.lang.Long, java.lang.Long>) iterator.next();	
				boolean checkSpecInCategory = this.checkSpecInCategory(entry.getKey(), entry.getValue(), specIds);
				if(!checkSpecInCategory){
					return new ReturnData(Boolean.FALSE, 500110,"规格,规格组不存在关联的小类中");
				}
			 }
		}
			
		
		//校验商品detail 
		PushItemDetailDto pushItemDetailDto = pushItemInfoAndDetailDto.getItemDetail();
		if(null ==pushItemDetailDto){
			return new ReturnData(Boolean.FALSE, 500111,"商品详情不能为空");
		}
		
		//前台展示名称
		String mainTitle = pushItemDetailDto.getMainTitle();		
		if(StringUtils.isBlank(mainTitle) ){
			return new ReturnData(Boolean.FALSE, 500112,"前台展示名不能为空");
		}
		
		
		
		//商品前台展示副标题
		String subTitle = pushItemDetailDto.getSubTitle();	
		if(StringUtils.isBlank(subTitle)){
			return new ReturnData(Boolean.FALSE, 500113,"商品前台展示副标题不能为空");
		}else if(subTitle.length()>100){
			return new ReturnData(Boolean.FALSE,500113,"副标题大于100个字符");
		}

		//商品存储在仓库中的名称	
		String storageTitle = pushItemDetailDto.getStorageTitle();
		if(StringUtils.isBlank(storageTitle)){
			return new ReturnData(Boolean.FALSE, 500114,"在仓库中的名称不能为空");
		}
		//条形码
		String barcode = pushItemDetailDto.getBarcode();
		if(StringUtils.isBlank(barcode) ){
			return new ReturnData(Boolean.FALSE, 500115,"不能为空,长度不大于30");
		}
		
		//生产厂家
		String manufacturer = pushItemDetailDto.getManufacturer();
		if(StringUtils.isNotBlank(manufacturer) && manufacturer.trim().length()>255){
			return new ReturnData(Boolean.FALSE, 500116,"生产厂家名字段最多255个字");
		}
		
		//市场价格
		Double basicPrice = pushItemDetailDto.getBasicPrice();
	    if(null == basicPrice){
	    	return new ReturnData(Boolean.FALSE, 500117,"市场价格不能为空");
	    }
	    
	    //海淘校验
	    Integer wavesSign = pushItemDetailDto.getWavesSign();
	    if(null ==wavesSign || (wavesSign.intValue() != 1 && wavesSign.intValue() !=2) ){
	    	return new ReturnData(Boolean.FALSE, 500118,"海淘标志位不能为空,且只能为1或2");
	    }
	    if(wavesSign.intValue() ==2){
	    	//行邮税
	    	 Double tarrifRate = pushItemDetailDto.getTarrifRate();
	    	if(null == tarrifRate){
	    		return new ReturnData(Boolean.FALSE, 500119,"海淘商品行邮税不能为空");
	    	}
	    	if(tarrifRate.doubleValue()>1 || tarrifRate.doubleValue()<0){
	    		return new ReturnData(Boolean.FALSE, 500120,"税率值在0-1之间");
	    	}
	    	 Long taxRateId = taxRateService.selectIdByTaxRate(tarrifRate*100, currentUserId);
	    	 pushItemDetailDto.setTarrifRateId(taxRateId);
	    	 
	    	//海淘产地
	    	Long countryId = pushItemDetailDto.getCountryId();
	    	if(null == countryId){
	    		return new ReturnData(Boolean.FALSE, 500121,"海淘商品产地不能为空");
	    	}
	    	
	    	DistrictInfo districtInfo = districtInfoService.queryById(countryId);
	    	if(null == districtInfo || districtInfo.getType() !=2){
	    		return new ReturnData(Boolean.FALSE, 500122,"海淘产地不存在或者对应地区不为国家");
	    	}
	    	
	    	//增值税
	    	Long addedValueRateId = pushItemDetailDto.getAddValueRateId();
	    	if(null == addedValueRateId){
	    		return new ReturnData(Boolean.FALSE, 500119,"海淘商品增值税不能为空");
	    	}
	    	TaxRate addedTaxRate = taxRateService.queryById(addedValueRateId);
	    	if(addedTaxRate == null){
	    		return new ReturnData(Boolean.FALSE, 500119,"海淘商品增值税错误");
	    	}
	    	//消费税
	    	if(null == pushItemDetailDto.getExciseRateId()){
	    		return new ReturnData(Boolean.FALSE, 500119,"海淘商品消费税不能为空");
	    	}
	    	TaxRate exciseRate = taxRateService.queryById(pushItemDetailDto.getExciseRateId());
	    	if(exciseRate == null){
	    		return new ReturnData(Boolean.FALSE, 500119,"海淘商品消费税错误");
	    	}
	    	//关税
	    	if(null == pushItemDetailDto.getCustomsRateId()){
	    		return new ReturnData(Boolean.FALSE, 500119,"海淘商品关税不能为空");
	    	}
	    	TaxRate customsRate = taxRateService.queryById(pushItemDetailDto.getCustomsRateId());
	    	if(customsRate == null){
	    		return new ReturnData(Boolean.FALSE, 500119,"海淘商品关税错误");
	    	}
	    }
	    
	    //进项税率
	    Long purRate = pushItemDetailDto.getPurRate();
	    if(null ==purRate || purRate.intValue() !=5 ){
	    	pushItemDetailDto.setPurRate(5l);
	    }
	    
	    //销项税率
	    Long saleRate = pushItemDetailDto.getSaleRate();
	    if(null ==saleRate || saleRate.intValue() !=6 ){
	    	pushItemDetailDto.setSaleRate(6l);
	    }
	    //无理由退货天数
	    Integer returnDays = pushItemDetailDto.getReturnDays();
	    if(null == returnDays){
	    	return new ReturnData(Boolean.FALSE, 500123,"无理由退货天数不能为空");
	    }
	    
	    //有效期标志位
	    Integer expSign = pushItemDetailDto.getExpSign();
	    if(null == expSign ||(expSign.intValue() != 1 && expSign.intValue() != 2)){
	    	return new ReturnData(Boolean.FALSE, 500124,"是否有效期管理标志位不能为空,且只能为1或2");
	    }
	    //有效期月数
	    if( expSign ==1){
	    	Integer expDays = pushItemDetailDto.getExpDays();
	    	if(null == expDays){
	    		return new ReturnData(Boolean.FALSE, 500125,"有效期月数不能为空");
	    	}
	    }
	   //是否优质商品 
	    Integer qualityGoods = pushItemDetailDto.getQualityGoods();
	    if(null == qualityGoods  ||(qualityGoods.intValue() != 1 && qualityGoods.intValue() != 2)){
	    	return new ReturnData(Boolean.FALSE, 500126,"是否优质商品标志位必填,且只能为1或2");
	    }
	    
	    //毛重
	    Double weight = pushItemDetailDto.getWeight();
	    if(null == weight || (weight.doubleValue() < 0) ){
	    	return new ReturnData(Boolean.FALSE, 500127,"毛重必填,且必须大于等于0");
	    }
	    //如果传过来的毛重为0，改为默认1.0
	    if(weight.doubleValue()==0){
	    	pushItemDetailDto.setWeight(1.0);
	    }
	  //如果传过来的毛重为0，改为默认null
	    if(pushItemDetailDto.getWeightNet() != null && pushItemDetailDto.getWeightNet().doubleValue()==0){
	    	pushItemDetailDto.setWeightNet(null);
	    }
//	    pushItemInfoAndDetailDto.setSpuName(pushItemInfoAndDetailDto.getSpuName()+barcode);
	    return new ReturnData(Boolean.TRUE);
	}

	
	
	
	
	
	
   /**
    * 转换pushItemInfoAndDetailDto
    * @param pushItemInfoAndDetailDto
    * @return
    */
	private ReturnData convertPushItemInfoAndDetailDto(PushItemInfoAndDetailDto pushItemInfoAndDetailDto) {
				
        
		/*-----------------------------spec-------------------------------------------*/
		List<Map<String, Long>> specInfos = pushItemInfoAndDetailDto.getSpecInfos();
		
		if (CollectionUtils.isNotEmpty(specInfos)) {
			Map<Long, Long> specInfosMap = new HashMap<Long, Long>();
			for (int i = 0; i < specInfos.size(); i++) {
				Map<String, Long> specObj = specInfos.get(i);
				if (null != specObj) {
					Long specGroupId = (Long) specObj.get("specGroupId");
					Long specId = (Long) specObj.get("specId");
					if (null != specGroupId && null != specId) {
						specInfosMap.put(specGroupId, specId);
					}
				}
				if (specInfosMap.size() > 3) {
					return new ReturnData(Boolean.FALSE, 500101, "规格组多于3组");
				}
				if (specInfosMap.size() != 0) {
					pushItemInfoAndDetailDto.setSpecInfosMap(specInfosMap);
				}
			}
		}
		
		/*-----------------------------attr-------------------------------------------*/
		List<Map<String,String>> itemAttrs = pushItemInfoAndDetailDto.getItemAttr();
	
		if (CollectionUtils.isNotEmpty(itemAttrs)) {
			
			
			if (itemAttrs.size() > 0) {
				Map<String, String> attrInfosMap = new HashMap<String, String>();
				for (int i = 0; i < itemAttrs.size(); i++) {
					Map<String, String> specObj = (Map<String,String>) itemAttrs.get(i);
					if (null != specObj) {
						String attrKey = (String) specObj.get("attKey");
						String attrValue = (String) specObj.get("attValue");
						if (StringUtils.isNotBlank(attrKey)&& StringUtils.isNotBlank(attrValue)) {
							attrInfosMap.put(attrKey, attrValue);
						}
					}
				}
				if (attrInfosMap.size() != 0) {
					pushItemInfoAndDetailDto.setItemAttrMap(attrInfosMap);
				}
			}
		}
		/*-----------------------------pic-------------------------------------------*/
		 List<String> itemPictures = pushItemInfoAndDetailDto.getItemPictures();   
        
		 if (CollectionUtils.isNotEmpty(itemPictures)) {
			 
			 if(itemPictures.size()>2 && itemPictures.size()<11){
				 pushItemInfoAndDetailDto.setPicListPath(itemPictures);
			 }  else {
					return new ReturnData(Boolean.FALSE, 500102,"图片须上传3-10张");
			 }
		 } else {
				return new ReturnData(Boolean.FALSE, 500102,"图片须上传3-10张");
		 }
		 
		return new ReturnData(Boolean.TRUE);
	}

	/**
	 * 
	 * <pre>
	 *  校验规格是否在小类中
	 *  存在为true
	 *  不存在为false
	 * </pre>
	 *
	 * @param specGroupId
	 * @param specId
	 * @param specIds
	 * @return boolean
	 */
	private boolean checkSpecInCategory(Long specGroupId,Long specId,Map<Long,ArrayList<Long>> specGroupIds){
		if(null==specGroupId && null == specId){
			return true;
		}
		if(MapUtils.isNotEmpty(specGroupIds)){
			Set<Long> specGroupIdKey = specGroupIds.keySet();
			for (Iterator<Long> it = specGroupIdKey.iterator(); it.hasNext();) {
				Long groupId  = it.next();
				List<Long> specIdList = specGroupIds.get(groupId);
				if(null!=specGroupId  && specGroupId.equals(groupId)){
					if(null!=specId&&CollectionUtils.isNotEmpty(specIdList)){
						if(specIdList.indexOf(specId)!=-1){
							return true;
						}
					}
				}
			}
		}
		return false;
	}



	@Autowired
	IUserInfoService userInfoService;
	public Long getCurrentUserIdByAppKey(String appKey) {
		return getSupplierId(appKey);
	}	
	@Autowired
	IPlatformAccountService platformAccountService;
	public Long getSupplierId(String appKey){
		List<PlatformSupplierRelation> list = platformSupplierRelationService.selectListByAppkey(appKey);
		if(CollectionUtils.isNotEmpty(list)){
			Long supplierId = list.get(0).getSupplierId();
			return supplierId;
		}
		return null;
	}
	
	public ReturnData setItemCostprice(PushItemCostpriceDto pushItemCostpriceDto, HttpServletRequest request,Long currentUserId, String appKey) {
		if(pushItemCostpriceDto.getSku()==null || "".equals(pushItemCostpriceDto.getSku())){
			return  new ReturnData(Boolean.FALSE, 600002,"sku存在缺失");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sku", pushItemCostpriceDto.getSku());
		//保存数据
		ItemSku itemSku = itemSkuService.queryUniqueByParams(params);
		itemSku.setCostPrice(pushItemCostpriceDto.getCostPrice());
		itemSku.setUpdateUser(currentUserId.toString());
		itemSku.setUpdateTime(new Date());
		itemSkuService.updateById(itemSku);
		
		ItemPushLog itemPushLog = new ItemPushLog();
		itemPushLog.setSku(pushItemCostpriceDto.getSku());
		itemPushLog.setType(2);//type 1-设置库存接口 ， 2-设置成本价接口'
		itemPushLog.setCostPrice(pushItemCostpriceDto.getCostPrice());
		itemPushLog.setCreateUser(String.valueOf(currentUserId));
		itemPushLog.setCreateTime(new Date());
		itemPushLogService.insert(itemPushLog);
		return new ReturnData(Boolean.TRUE);
	}
	
}
