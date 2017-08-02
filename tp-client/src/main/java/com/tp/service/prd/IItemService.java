package com.tp.service.prd;

import java.util.Date;
import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.SkuDto;
import com.tp.dto.prd.mq.PromotionItemMqDto;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.query.prd.ItemQuery;
import com.tp.query.prd.SkuInfoQuery;
import com.tp.result.prd.SkuInfoResult;

/**
 * 
 * <pre>
 * 	      商品给外部的接口
 *    商品的基本、库存、价格、属性、商品介绍、图片等信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public interface IItemService {
	
	/**
	 * 
	 * <pre>
	 * 	     检验商品接口
	 * </pre>
	 *
	 * @param skuCode必填
	 * @return
	 */
	ResultInfo<Boolean> checkItem(String skuCode);
	
	/**
	 * 
	 * <pre>
	 *   批量检验商品接口
	 * </pre>
	 *
	 * @param skuCodes必填
	 * @return
	 * @
	 */
	ResultInfo<Boolean> checkItemList(List<String> skuCodes);
	
	/**
	 * 
	 * <pre>
	 * 	            通过sku list 查询相关的商品信息
	 * 		    提供给购物车：列表查询接口
	 *        提供给供应商供：查询
	 * </pre>
	 *
	 * @param skuCodes
	 * @return
	 * @
	 */
	List<ItemResultDto> getSkuList(List<String> skuCodes) ;
	
	/***
	 * 通过skuid 查询商品ID
	 * @return
	 * @throws DAOException
	 */
	InfoDetailDto  selectItemIdBySkuId(String skuCode);
	
	/***
	 * 更具detailId 获取商品详情
	 * @param detailId
	 * @return
	 * @throws DAOException
	 */
	String  selectDetailIdDesc(Long detailId);
	
	/***
	 * <pre>
	 * 
	 * 获取商品图片信息
	 * 
	 * </pre>
	 * @param id
	 * @return
	 * @throws DAOException
	 * @author szy
	 */
	List<ItemPictures> selectItemPictures(InfoDetailDto dto) ;
	
	/**
	 * <pre>
	 * 	提供给供应商：  查询sku信息
	 * </pre> 
	 * @param sku
	 * @return
	 * @
	 */
	SkuInfoResult  selectSkuInfo(SkuInfoQuery sku) ;
	
	/***
	 *  提供活动 获取sku信息
	 * 
	 * @param List<String> skuList 
	 * @return  
	 *  商品主图片(picture)   
     *	商品规格信息(spec)
     *	商品品类Id(category)
     *	条码(barCode)
     *  品牌Id(brandId)
     *	 供应商名称(spName)
	 * @
	 */
	List<SkuDto> querySkuDtoListForPromotion(List<String> skuList);
	
	/***
	 *  提供活动 获取sku信息
	 * 
	 * @param  barcode  条码   spCode 商家编码
	 * @return  
	 *  商品主图片(picture)   
     *	商品规格信息(spec)
     *	商品品类Id(category)
     *	条码(barCode)
     *  品牌Id(brandId)
     *	 供应商名称(spName)
	 * @
	 */
	List<SkuDto> querySkuDtoListForPromotionWithBarCodeAndSpCode(List<SkuDto> dtoList);
	
	/***
	 * 手机APP 接口
	 * @param skuCode
	 * @param topicId
	 * @return
	 * @
	 * @throws DAOException 
	 */
	InfoDetailDto queryItemSkuTopicInfoForAPP(String skuCode,String topicId);
	
	
	
	/**
	 * 
	 * <pre>
	 * 	通过barcode与商品名称mainTitle（支持模糊）查询商品prd列表
	 * </pre>
	 *
	 * @param barcode
	 * @param mainTitle
	 * @return
	 * @
	 */
	List<ItemDetail> queryPrdByBarcodeAndName(String barcode,String mainTitle) ;
	
	/**
	 * 
	 * <pre>
	 * 	通过prd编号列表查询
	 * </pre>
	 *
	 * @param prdList
	 * @return
	 * @
	 */
	List<ItemDetail> queryPrdByCodeList(List<String> prdList) ;
	
	
	/**
	 * 
	 * <pre>
	 * 	通过prd编号查询
	 * </pre>
	 *
	 * @param prd
	 * @return
	 * @
	 */
	ItemDetail  queryPrdByPrdCode(String prd) ;
	
	
	/**
	 * 
	 * <pre>
	 *  获取prdid的编码
	 * </pre>
	 *
	 * @param skuCode
	 * @return
	 */
	 String getPrdidCode(String skuCode) ;
	 
	 
	 /**
		 * 
		 * 
		 * <pre>
		 * 获取spu的编码
		 * </pre>
		 *
		 * @param skuCode
		 * @return
		 */
	 String getSpuCode(String skuCode) ;
	 
 
	/***
	 * 根据spid 和 条形码 集合返回 
	 * @param spId
	 * @param barCodes
	 * @return
	 * @
	 */
	List<ItemResultDto> getSkuListForSupplierWithSpIdAndBarCodes(Long spId,Integer saleType,List<String> barCodes) ;
	
	/**
	 * 根据spid 和 skucode形码 集合返回 
	 * @param spId
	 * @param saleType
	 * @param skuCodes
	 * @return
	 * @
	 */
	List<ItemResultDto> getSkuListForSupplierWithSpIdAndSkuCodes(Long spId,Integer saleType,List<String> skuCodes) ;
	/****
	 *  获取sku 海关备案信息 skuArtDO中 设置 sku,bondedArea 通关渠道
	 * @param checkList
	 * @return
	 * @
	 */
	List<ItemSkuArt> checkBoundedInfoForSales(List<ItemSkuArt> checkList);
	
	
	/***
	 * 手机端海淘接口
	 * @param skuCode
	 * @param topicId
	 * @return
	 * @
	 * @throws DAOException
	 */
	InfoDetailDto queryItemSkuTopicInfoForAPPHaiTao(String skuCode,String topicId);

	SkuInfoResult selectXgSkuInfo(SkuInfoQuery sku);
	
	List<ItemSku>queryByItemQueryNotEmpty(ItemQuery itemquery );
	
	List<ItemDetailSpec> queryByDetailId(Long detailid );

	List<PromotionItemMqDto> queryItemByUpdateTime(Date updateTime);

}