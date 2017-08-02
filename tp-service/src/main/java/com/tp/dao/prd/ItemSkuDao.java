package com.tp.dao.prd;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.OpenPlantFormItemDto;
import com.tp.dto.prd.SkuDetailInfoDto;
import com.tp.dto.prd.SkuDto;
import com.tp.model.prd.ItemSku;
import com.tp.query.prd.ItemQuery;
import com.tp.query.prd.OpenPlantFormItemQuery;

public interface ItemSkuDao extends BaseDao<ItemSku> {

	Integer batchUpdateByDetailId(ItemSku sku);

	Integer batchUpdateByItemId(ItemSku sku);

	void batchUpdateSkusStatu(List<ItemSku> skuSuccessList);

	List<OpenPlantFormItemDto> selectListOfByOpenPlantFormItemQuery(OpenPlantFormItemQuery openPlantFormItemQuery);

	Long selectCountByLikeOfopenPlantFormItemQuery(OpenPlantFormItemQuery openPlantFormItemQuery);

	List<SkuDto> selectProductDeatilsByItemIdAndSpId(ItemSku itemSku);

	InfoDetailDto selectItemIdBySkuId(String skuCode);

	List<SkuDto> querySkuDtoListForPromotion(List<String> listSku);

	List<SkuDto> querySkuDtoListForPromotionWithBarCodeAndSpCodeList(List<SkuDto> dtoList);

	List<ItemResultDto> queryPageByQuery(ItemQuery query);

	Integer queryCountByQuery(ItemQuery query);

	Integer updateSkuInfoWithItemSpuInfo(ItemSku itemSku);

	List<ItemQuery> checkSellerSkuWithAuditStatusAndLevel(ItemQuery searchQuery);

	/**
	 * @param skuList
	 * @return
	 */
	List<SkuDetailInfoDto> selectSkuDetailInfoByListSkus(List<String> skuList);
	

	List<SkuDetailInfoDto> selectCommisionRateByListSkus(List<String> skuList);

	/**
	 * 根据折扣批量更新促销价格
	 * @param idList
	 * @param discount
	 * @param userName
	 * @return
	 */
	Integer updateBatchPrice(@Param("skuList")List<String> skuList, @Param("discount")Float discount, @Param("userName")String userName);


	List<ItemSku> queryByUpdateTime(@Param("updateTime") Date updateTime);
	
}
