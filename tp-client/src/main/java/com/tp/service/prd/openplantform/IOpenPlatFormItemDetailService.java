package com.tp.service.prd.openplantform;


import java.util.List;

import com.tp.dto.prd.InfoOpenDto;
import com.tp.dto.prd.ItemDetailOpenDto;
import com.tp.dto.prd.SpuInfoOpenDto;
import com.tp.exception.ItemServiceException;

/**
 * 
 * <pre>
 *  提供给商家平台查询商品信息的接口
 * </pre>
 *
 * @author admin
 * @version $Id: IOpenPlatFormItemDetailService.java, v 0.1 2015年4月15日 上午10:14:06 admin Exp $
 */
public interface IOpenPlatFormItemDetailService {

	/**
	 * 
	 * <pre>
	 *  根据detailId返回商品详细信息的ItemDetailOpenDto
	 * </pre>
	 *
	 * @param detailId 
	 * @return
	 */	
	ItemDetailOpenDto getItemDetailOpenDtoByDetailId(Long detailId) throws ItemServiceException;
	
	
	/***
	 * 商家平台 更具spu名称查询商品 spu 信息  上限 100条
	 * @param spuName
	 * @return
	 * @throws ItemServiceException
	 */
	List<InfoOpenDto> querySpuByNameForSeller(String spuName) throws ItemServiceException;
	
	
	/***
	 * 更具spucode 获取spu信息
	 * @param spuCode
	 * @return
	 * @throws ItemServiceException
	 */
	ItemDetailOpenDto getSpuInfoBySpuCode(String spuCode)throws ItemServiceException;
	
	/**
	 * 
	 * <pre>
	 * 根据spucode返回商spu下所有prdid的信息
	 * </pre>
	 *
	 * @param spuCode
	 * @return
	 * @throws ItemServiceException
	 */
	SpuInfoOpenDto getSpuInfoOpenDtoBySpuCode (String spuCode)throws ItemServiceException;
	/***
	 * 校验商家平台 条形码的列表
	 * @param barcode
	 * @return
	 * @throws ItemServiceException
	 */
	List<String> checkSellerBarcodeList(List<String> barcode)throws ItemServiceException;
	
	
	/***
	 *  通过skuID查询主库所有信息
	 * @param skuId
	 * @return
	 * @throws ItemServiceException
	 */
	ItemDetailOpenDto getItemDetailOpenDtoBySkuId(Long skuId) throws ItemServiceException;
}
