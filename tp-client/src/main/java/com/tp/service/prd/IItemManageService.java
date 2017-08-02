package com.tp.service.prd;

import java.util.List;
import java.util.Map;

import com.tp.dto.common.Option;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.DetailDto;
import com.tp.dto.prd.ItemCopyDto;
import com.tp.dto.prd.ItemDto;
import com.tp.dto.prd.ItemOpenSaveDto;
import com.tp.model.bse.CustomsDistrictInfo;
import com.tp.model.bse.CustomsUnitInfo;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.prd.ItemSkuSupplier;

/**
 * 
 * <pre>
 *    商品的后台的管理服务
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public interface IItemManageService {

	/**
	 * 
	 * <pre>
	 * 保存商品信息
	 * </pre>
	 *
	 * @param item
	 * @param userId
	 * @return
	 * @
	 */
	Long saveItem(ItemDto item,String createUser);
	
	/**
	 * 
	 * <pre>
	 *   通过itemId查询商品信息
	 * </pre>
	 *
	 * @param itemId
	 * @param type:1 只获取INFO,2获取所有的 
	 * @return
	 * @
	 */
	ItemDto getItemByItemId(Long itemId,int type );
	
	/**
	 * 
	 * <pre>
	 * 	保存商品的prdid纬度信息
	 * </pre>
	 * 
	 * @param infoDO
	 * @param itemDto
	 * @param createUserId
	 * @return
	 * @
	 */
	Long  saveItemDetail(ItemInfo infoDO,DetailDto itemDto,String createUser);
	
	/**
	 * 
	 * <pre>
	 *   查询商品detail信息
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	ItemDetail getItemDetailByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 * 	获取商品的www网站模板
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	ItemDesc  getDescByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 * 	获取商品的手机模板
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	ItemDescMobile geteDescMobilByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 * 	获取商品的图片
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	List<ItemPictures> getPicsByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 * 	获取商品sku的列表
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	List<ItemSku> getSkuListByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 * 	获取小类对应最新的spu编码
	 *  smallCode 为小类编号
	 * </pre>
	 *
	 * @param smallId
	 * @return
	 * @
	 */
	String getSpuCode(String smallCode);
	
	/**
	 * 
	 * <pre>
	 *   获取prdid的编码
	 * </pre>
	 *
	 * @param spuCode
	 * @return
	 * @
	 */
	String getPrdidCode(String spuCode);
	
	/**
	 * 
	 * <pre>
	 * 	获取sku的编码
	 * </pre>
	 *
	 * @param prdIdCode
	 * @return
	 * @
	 */
	String getSkuCode(String prdIdCode);
	
	/**
	 * 
	 * <pre>
	 *    通过detailId 获取sku信息
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	List<ItemSku> getDetailSkuListByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 * 	通过skuId 获取sku对应 供应商信息 只是在自销的模式
	 * </pre>
	 *
	 * @param skuId
	 * @return
	 * @
	 */
	List<ItemSkuSupplier> getSkuSupplierListBySkuId(Long skuId);
	
	/**
	 * 
	 * <pre>
	 * 		通过details的集合查询
	 * </pre>
	 *
	 * @param detailIds
	 * @return
	 * @
	 */
	List<ItemSku> getSkuDetailListByDetailIds(List<Long> detailIds);

	/**
	 * 
	 * <pre>
	 * 		通过detailid 查询商品属性集合
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	List<ItemAttribute> getAttributeByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @
	 */
	//void saveImportLog(SkuImportLogDto skuImprtLogDto);
	
	/**
	 * 
	 * <pre>
	 * 	 barcode 在detail中唯一
	 *   返回true 代表不存在
	 *   返回false 代表已经存在
	 * </pre>
	 *
	 * @param barcode
	 * @param detailId
	 * @param itemId
	 * @return boolean
	 * @
	 */
	boolean checkBarcodeExsit(String barcode,Long detailId,Long itemId);
	
	/**
	 * 
	 * <pre>
	 * 	 barcode 在detail中唯一
	 *   返回true 代表不存在
	 *   返回false 代表已经存在
	 * </pre>
	 *
	 * @param barcode
	 * @param detailId
	 * @param supplierId
	 * @param saleType
	 * @return boolean
	 * @
	 */
	boolean checkBarcodeExsitInSku(Long detailId,String barcode,Long  skuId ,Long supplierId,int saleType);
	
	/**
	 * 
	 * <pre>
	 *  复制商品
	 * </pre>
	 *
	 * @param detailIds
	 * @param userId
	 * @
	 */
	void copyItem(String detailIds,String createUser);
	
	
	/**
	 * 
	 * <pre>
	 * 	查询商品销售规格
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 * @
	 */
	List<ItemDetailSpec> getDetailSpecListByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 *  作废sku
	 * </pre>
	 *
	 * @param skuId
	 * @param userId
	 * @return
	 * @
	 */
	Integer cancelSku(Long skuId,String userName);
	
	
	/**
	 * 
	 * <pre>
	 *   维护sku与供应商的关系
	 * </pre>
	 *
	 * @param skuId
	 * @param skuSupplierList
	 * @return
	 * @
	 */
	Integer addSkuSupplier(Long skuId,String skuSupplierList);
	
	/**
	 * 
	 * <pre>
	 *  删除sku与供应商的关系
	 * </pre>
	 *
	 * @param skuSupplierId
	 * @return
	 * @
	 */
	Integer deleteSkuSupplier(Long skuSupplierId);
	
	/**
	 * 
	 * <pre>
	 * 	查询info列表信息
	 * </pre>
	 *
	 * @param itemIds
	 * @return
	 * @
	 */
	List<ItemInfo> getInfoListByIds(List<Long> itemIds);
	
	
	/**
	 * 
	 * <pre>
	 * 	查询detail列表信息
	 * </pre>
	 *
	 * @param itemIds
	 * @return
	 * @
	 */
	List<ItemDetail> selectByDetailIds(List<Long> detailIds);
	
	/**
	 * 
	 * <pre>
	 * 	校验商品spu是否存在
	 *  不存在返回null
	 *  存在返回info
	 * </pre>
	 *
	 * @param smallId
	 * @param brandId
	 * @param unitId
	 * @param spuName
	 * @param infoId
	 * @return
	 * @
	 */
	ItemInfo checkSpuExsit(Long smallId,Long brandId,Long unitId,String spuName,Long infoId);
	
	/**
	 * <pre>
	 *   此接口需要依赖接口checkSpuExsit
	 * 	  校验新增的规格是否存在
	 *   返回1 代表规格组一样，但是对应规格都没有存在系统中，校验成功，此一组规格可以插入
	 *   返回2 代表已经存在(规格组与规格一一匹配) 校验失败，此一组规格不可以插入
	 *   返回3 代表规格组数量不匹配，校验失败，此一组规格可以插入
	 *   返回4 代表完全匹配上的 
	 * </pre>
	 *
	 * @param itemId
	 * @param specInfoMap
	 * @return
	 * @
	 */
	int checkPrdidSpec(Long itemId,Map<Long,Long> specInfoMap);
	
	/**
	 * 
	 * <pre>
	 * 	获取同级别下的prdid维度下的信息
	 * </pre>
	 *
	 * @param detailId
	 * @return
	 */
	List<ItemCopyDto> getItemCopyByDetailId(Long detailId);
	
	/**
	 * 
	 * <pre>
	 * 	复制prdid 
	 * </pre>
	 *
	 * @param itemId
	 * @param detailIds
	 * @param picList
	 * @param desc
	 * @param descMobile
	 * @param userId
	 * @
	 */
	void copyPrdPicAndDesc(Long itemId,List<Long> detailIds,List<String> picList,String desc,String descMobile,String userId);
	
	/**
	 * 
	 * <pre>
	 * 	sku ids 查询供应商列表
	 * </pre>
	 *
	 * @param skuList
	 * @return
	 * @
	 */
	List<ItemSkuSupplier> getSkuSupplierListBySkuIds(List<Long> skuList);
	
	/***
	 * 获取sku对应的货号信息
	 * @param skuList
	 * @return
	 * @
	 */
	List<ItemSkuArt>  getSkuArtNumberBySkuId(Long skuId); 
	/***
	 * 保存sku 对应货号信息
	 * @param skuArtDO
	 * @param skuId
	 * @return
	 * @
	 */
	Long saveSkuArt(ItemSkuArt skuArtDO);
	
	/***
	 * 删除skuart信息
	 * @param id
	 * @return
	 * @
	 */
	Integer deleteSkuArtInfo(Long id) throws Exception;
	
	/***
	 * 校验sku是否存在的对应的海关数据
	 * @param skuArtDO
	 * @return
	 * @throws Exception
	 */
	ItemSkuArt checkSkuArtWithSkuId(ItemSkuArt skuArt) throws Exception;
	
	/**
	 * 更新sku状态
	 * @param skuIds
	 * @param status : 0-未上架 1-上架 2-作废 
	 * @
	 */
	ResultInfo<List<Option>> changeItemStatus (List<Long> skuIds,int status);
	
	/**
	 * 查询sku列表（barcode，detailName，sku，skuId）
	 * @param detailIds
	 * @return
	 * @
	 */
	List<ItemSku> getSkuListByDetailIds(List<Long> detailIds);
	
	
	/**
	 * 
	 * <pre>
	 * 保存商品信息,prdid信息
	 * </pre>
	 *
	 * @param item
	 * @param userId
	 * @return
	 * @
	 */
	Long saveItemPrds(ItemDto item,String createUser);
	
	/***
	 * 强制从主库获取商品主信息
	 * @param itemId
	 * @return
	 * @
	 */
	ItemInfo getItemInfoFromMasterDataBase(Long itemId);
	
	/***
	 * 强制从主库 通过spu 查询商品信息
	 * @param spuCode
	 * @return
	 * @
	 */
	ItemInfo getItemInfoFromMasterDataBaseBySpuCode(String spuCode);
	/***
	 *  强制从主库获取SPU下PRD信息列表
	 * @param itemId
	 * @return
	 * @
	 */
	List<ItemDetail> getGroupDetailIdFromMasterDataBaseWithItemId(Long itemId);
	
	
	/***
	 * 保存开放平台商品数据
	 * @param saveDto
	 * @return
	 * @
	 */
	Long saveOpenPlantFromItem(ItemOpenSaveDto saveDto);
	
	/***
	 *  保存商家开放平台 数据
	 * @param saveDto
	 * @return
	 * @
	 * @throws DAOException
	 */
	ItemOpenSaveDto saveOpenPlantForsSpuFromItem(ItemOpenSaveDto saveDto);

	Long saveItemDetailWithExistSpu(ItemInfo info, DetailDto detailDto,Long spId, String auditType, Long skuId);

	Long saveItemWithExistSpu(ItemDto item, Long spId);

	Long saveNewSpu(ItemDto item, Long spId);

}
