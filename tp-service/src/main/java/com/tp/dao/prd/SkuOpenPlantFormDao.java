package com.tp.dao.prd;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tp.common.vo.DAOConstant;
import com.tp.datasource.ContextHolder.DATA_SOURCE_KEY;
import com.tp.dto.prd.InfoOpenDto;
import com.tp.dto.prd.SkuOpenDto;

/***
 * sku 开发平台 dao层 接口
 * @author szy
 *
 */
public interface SkuOpenPlantFormDao {
	
	
	/***
	 *  通过barcode 查询商品sku信息
	 * @param barCode
	 * @return
	 */
	SkuOpenDto	getSkuInfoForSellerValidationWithBarCode(Map<String,Object> dataMap);

	/****
	 *  查询商品detailId级别的信息
	 * @param dataMap
	 * @param key
	 * @return
	 */
	SkuOpenDto	getProductDetailInfoForSellerValidationWithBarCode(Map<String,Object> dataMap);
	
	/***
	 * 商家查询商品spu名称数据
	 * @param spuName
	 * @return
	 */
	List<InfoOpenDto> querySpuByNameForSeller(String spuName);
	
	/***
	 * 校验商家平台barcodeList
	 * @param barcode
	 * @param key
	 * @return
	 */
	List<String> checkSellerBarcodeList(List<String> barcode);

	List<String> checkSellerBarcodeList(List<String> barcode,@Param(DAOConstant.DATA_SOURCE_KEY)DATA_SOURCE_KEY masterSaleOrderDataSource);
}
