package com.tp.proxy.mmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.stg.InventoryDto;
import com.tp.exception.ServiceException;
import com.tp.model.bse.Category;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.Spec;
import com.tp.model.mmp.TopicItem;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.stg.Warehouse;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.ISpecService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.ITopicManagementService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemDetailSpecService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchasingManagementService;

/**
 * 促销商品代理层
 *
 * @author szy
 */
@Service
public class TopicItemProxy extends BaseProxy<TopicItem> {

    @Autowired
    private ITopicItemService topicItemService;

    @Override
    public IBaseService<TopicItem> getService() {
        return topicItemService;
    }

    @Autowired
    private IItemSkuService skuService;

    @Autowired
    private IItemDetailService itemDetailService;

    @Autowired
    private IDistrictInfoService districtInfoService;

    @Autowired
    private IInventoryQueryService inventoryQueryService;

    @Autowired
    private IInventoryOperService inventoryOperService;

    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private ISpecService specService;

    @Autowired
    private IItemDetailSpecService itemDetailSpecService;

    @Autowired
    private IPurchasingManagementService purchasingManagementService;

    @Autowired
    private IItemPicturesService itemPicturesService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ITopicManagementService topicManagementService;

    public void insertTopicItemToRedis(TopicItem tItemDO) {
        topicItemService.insertTopicItemToRedis(tItemDO);
    }

    public List<TopicItem> getTopicItemDOByTopicId(Long tId) {
        return topicItemService.getTopicItemDOByTopicId(tId);
    }

    /**
     * 锁定活动商品
     *
     * @param itemId
     * @return
     */
    public ResultInfo lockItem(final Long itemId,UserInfo user ) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = topicItemService.lockTopicItem(itemId,user);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 解锁活动商品
     *
     * @param itemId
     * @return
     */
    public ResultInfo releaseItem(Long itemId,UserInfo user) {
        ResultInfo result = topicItemService.releaseTopicItem(itemId,user);
        return result;
    }

    /**
     * 获取最大排序序号
     *
     * @return
     */
    public Integer getMaxTopicItemSortIndex(Long topicId) {

        return topicItemService.getMaxTopicItemSortIndex(topicId);
    }

    /**
     * 根据sku或者barcode获得SKU信息
     *
     * @param sku
     * @param barcode
     */
    public List<ItemSku> getSKUInfosBySkuOrBarcodeWithBidSpu(String sku, String barcode, Long brandId, String spu, Long supplierId) {
        ItemSku ItemSku = new ItemSku();
        if (!StringUtils.isBlank(barcode)) {
            barcode = barcode.replaceAll("__", "/");
            ItemSku.setBarcode(barcode.trim());
        }
        if (!StringUtils.isBlank(sku)) {
            ItemSku.setSku(sku.trim());
        }
        if (null != brandId && 0 != brandId) {
            ItemSku.setBrandId(brandId);
        }
        if (null != spu && !"0".equals(spu)) {
            ItemSku.setSpu(spu.trim());
        }
        if (null != supplierId && -1 != supplierId) {
            ItemSku.setSpId(supplierId);
        }
        List<ItemSku> itemSkuList = skuService.queryByObject(ItemSku);
        return itemSkuList;
    }
    
    

    /**
     * 根据skuID查询商品具体信息
     *
     * @param skuId
     * @return
     */
    public ResultInfo<TopicItemInfoDTO> getTopicItemInfoBySkuId(final Long skuId) {
        final ResultInfo<TopicItemInfoDTO> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                TopicItemInfoDTO topicItem = new TopicItemInfoDTO();
                // 设置Sku中的信息
                ItemSku sku = skuService.queryById(skuId);
                if (null == sku) {
                    throw new ServiceException("所选商品信息不存在");
                }
                topicItem.setSKUInfo(sku);
                topicItem.setItemStatus(sku.getStatus());
                if (null != sku.getDetailId() && 0 != sku.getDetailId()) {
                    // 获取商品规格
                    getItemSpec(topicItem, sku);
                    // 获取产地国家
                    getDistrictInfoInfo(topicItem, sku);
                    // 获取商品主要图片
                    if (null != sku.getItemId() && 0 != sku.getItemId()) {
                        getPictureInfo(topicItem, sku);
                    }
                }
                // 获取商品Category信息
                if (!StringUtils.isBlank(sku.getCategoryCode())) {
                    getCategoryInfo(topicItem, sku);
                }
                // 获取活动价格
                topicItem.setTopicPrice(sku.getTopicPrice());
               // BigDecimal topicPrice = purchasingManagementService.getProductSalesPrice(sku.getSku(), sku.getSpId());
                if (null == sku.getTopicPrice() || sku.getTopicPrice().equals(0)) {
                    topicItem.setTopicPrice(sku.getBasicPrice());
                } 
                result.setData( topicItem);
            }
        });
        return result;
    }
    
    /**
     * 根据skuID查询商品具体信息
     *
     * @param skuId
     * @return
     */
    public ResultInfo<PageInfo<TopicItemInfoDTO>> getTopicItemInfoBySkus(final Map<String,Object> params,final Integer pageNo,final Integer pageSize) {
        final ResultInfo<PageInfo<TopicItemInfoDTO>> result = new ResultInfo<PageInfo<TopicItemInfoDTO>>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
            	// 返回结果
            	PageInfo<TopicItemInfoDTO> resultPage = new PageInfo<TopicItemInfoDTO>();
            	List<TopicItemInfoDTO> resultList = new ArrayList<TopicItemInfoDTO>();
            	// 查询ItemSku
            	PageInfo<ItemSku> info = new PageInfo<ItemSku>(pageNo,pageSize);
				PageInfo<ItemSku> skuModelList = skuService.queryPageByParamNotEmpty(params, info);
				List<ItemSku> rows = skuModelList.getRows();
				// 组装数据
				if(rows!=null){
	            	for (ItemSku sku : rows) {
	            		TopicItemInfoDTO topicItem = new TopicItemInfoDTO();
	                    // 设置Sku中的信息
	                    if (null == sku) {
	                        throw new ServiceException("所选商品信息不存在");
	                    }
	                    topicItem.setSKUInfo(sku);
	                    topicItem.setItemStatus(sku.getStatus());
	                    if (null != sku.getDetailId() && 0 != sku.getDetailId()) {
	                        // 获取商品规格
	                        getItemSpec(topicItem, sku);
	                        // 获取产地国家
	                        getDistrictInfoInfo(topicItem, sku);
	                        // 获取商品主要图片
	                        if (null != sku.getItemId() && 0 != sku.getItemId()) {
	                            getPictureInfo(topicItem, sku);
	                        }
	                    }
	                    // 获取商品Category信息
	                    if (!StringUtils.isBlank(sku.getCategoryCode())) {
	                        getCategoryInfo(topicItem, sku);
	                    }
	                    // 获取活动价格
	                    topicItem.setTopicPrice(sku.getTopicPrice());
	                   // BigDecimal topicPrice = purchasingManagementService.getProductSalesPrice(sku.getSku(), sku.getSpId());
	                    if (null == sku.getTopicPrice() || sku.getTopicPrice().equals(0)) {
	                        topicItem.setTopicPrice(sku.getBasicPrice());
	                    } 
	                    // 获取仓库列表
	                    Long spId = sku.getSpId();
	                    Warehouse warehouse = new Warehouse();
	                    warehouse.setSpId(spId);
						List<Warehouse> warehouses = warehouseService.queryByObject(warehouse);
						topicItem.setWarehouses(warehouses);
						
						// 图片
						String file = topicItem.getTopicImage();
		                if (!StringUtils.isBlank(file)) {
		                	topicItem.setImageFullPath(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,file));
		                }
						
	                    resultList.add(topicItem);
					}
				}
				resultPage.setPage(skuModelList.getPage());
				resultPage.setRecords(skuModelList.getRecords());
				resultPage.setSize(skuModelList.getSize());
				resultPage.setTotal(skuModelList.getTotal());
				resultPage.setRows(resultList);
				result.setData(resultPage);
            }
        });
        return result;
    }

    /**
     * 获取指定的sku和供应商id获得库存数据
     *
     * @param sku
     * @param supplierId
     * @return
     */
    public List<Warehouse> getWarehouseList(Long supplierId) {
        Warehouse warehouse = new Warehouse();
        warehouse.setSpId(supplierId);
        List<Warehouse> warehouses = warehouseService.queryByObject(warehouse);
        return warehouses;
    }

    /**
     * 传入sku和仓库id查询库存信息
     *
     * @param whId
     * @param sku
     * @return
     */
    public ResultInfo<InventoryDto> getInventory(final Long whId, final String sku) {
        final ResultInfo<InventoryDto> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
            	 List<InventoryDto> inventoryDtoList = inventoryQueryService.queryAvailableInventory(sku, whId);
               // List<InventoryDto> inventoryDtoList = inventoryQueryService.selectAvailableForSaleBySkuAndWhId(sku, whId);
                if (null == inventoryDtoList || inventoryDtoList.size() == 0) {
                    return;
                }
                if (inventoryDtoList.size() > 1) {
                    throw new ServiceException("商品[%s]有多个库存信息");
                }
                result.setData(inventoryDtoList.get(0));
            }
        });
        return result;
    }

    /**
     * 编辑用锁定库存
     *
     * @param topicItemId
     * @param amount
     * @return
     */
    public ResultInfo requestAddStock(final Long topicItemId, final int amount, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = topicManagementService.requestAddStock(topicItemId, amount, true, userId, userName);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;

    }

    /**
     * 编辑用锁定库存
     *
     * @param topicItemId
     * @param amount
     * @return
     */
    public ResultInfo requestBackStock(final Long topicItemId, final int amount, final Long userId, final String userName) {

        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = topicManagementService.backStock(
                        topicItemId, amount, userId, userName);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 根据TopicItemId获得商品信息
     *
     * @param topicItemId
     * @return
     */
    public TopicItem getTopicItemByItemId(Long topicItemId) {
        return topicItemService.queryById(topicItemId);
    }

    /**
     * 获得活动剩余库存
     *
     * @param TopicItem
     * @return
     */
    public int getTopicItemRemainStock(TopicItem TopicItem) {
        if (TopicItem == null || TopicItem.getTopicId() == null
                || StringUtils.isBlank(TopicItem.getSku())) {
            return 0;
        }
        return inventoryQueryService.querySalableInventory(App.PROMOTION, TopicItem.getTopicId().toString(), 
        		TopicItem.getSku(), TopicItem.getStockLocationId(), DEFAULTED.YES.equals(TopicItem.getReserveInventoryFlag()));
        //return inventoryQueryService.selectInvetory(StorageConstant.App.PROMOTION, String.valueOf(TopicItem.getTopicId()), TopicItem.getSku());
    }

    /**
     * 获得活动可占用库存
     *
     * @param topicItem
     * @return
     */
    public ResultInfo<TopicItem> getTopicItemCurrentStock(final TopicItem topicItem) {
        final ResultInfo<TopicItem> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
            	List<InventoryDto> dtos = inventoryQueryService.queryAvailableInventory(topicItem.getSku(), topicItem.getStockLocationId());

//                List<InventoryDto> dtos = inventoryQueryService.selectAvailableForSaleBySkuAndWhId(topicItem.getSku(), topicItem.getStockLocationId());
                if (CollectionUtils.isEmpty(dtos)) {
                    return;
                }
                InventoryDto iDto = dtos.get(0);
                topicItem.setStockAmount(iDto.getInventory());
                topicItem.setSaledAmount(iDto.getOccupy());
                result.setData(topicItem);
            }
        });
        return result;
    }

    /**
     * 获取商品规格
     *
     * @param topicItem
     * @param sku
     */
    private void getItemSpec(TopicItemInfoDTO topicItem, ItemSku sku) {
        List<Spec> specDOs = getSpecInfo(sku.getDetailId());
        // 设置规格参数
        topicItem.setItemSpecInfo(specDOs);
    }

    /**
     * 获取商品规格对象
     *
     * @param detailId
     * @return
     */
    private List<Spec> getSpecInfo(Long detailId) {
        ItemDetailSpec Spec = new ItemDetailSpec();
        Spec.setDetailId(detailId);
        List<ItemDetailSpec> detailSpecs = itemDetailSpecService.queryByObject(Spec);
        List<Spec> specDOs = null;
        if (null != detailSpecs && detailSpecs.size() > 0) {
            List<Long> specIds = new ArrayList<Long>();
            for (ItemDetailSpec spec : detailSpecs) {
                specIds.add(spec.getSpecId());
            }
            // 根据Id查询商品规格参数
            specDOs = specService.selectListSpec(specIds, ItemConstant.ALL_DATAS);
        }
        return specDOs;
    }

    /**
     * 获取商品Category信息
     *
     * @param topicItem
     * @param sku
     */
    private void getCategoryInfo(TopicItemInfoDTO topicItem, ItemSku sku) {
        if (null != sku) {
            Category category = new Category();
            category.setCode(sku.getCategoryCode());
            category.setStatus(1);
            List<Category> categories = categoryService.queryByObject(category);
            if (null != categories && categories.size() == 1) {
                topicItem.setCategoryId(categories.get(0).getId());
            }
        }
    }

    /**
     * 获取商品主要图片
     *
     * @param topicItem
     * @param sku
     */
    private void getPictureInfo(TopicItemInfoDTO topicItem, ItemSku sku) {
        ItemPictures pictureDO = new ItemPictures();
        pictureDO.setItemId(sku.getItemId());
        pictureDO.setDetailId(sku.getDetailId());
        pictureDO.setMain(1);
        List<ItemPictures> pictures = itemPicturesService.queryByObject(pictureDO);
        if (null != pictures && pictures.size() > 0) {
            topicItem.setTopicImage(pictures.get(0).getPicture());
        }
    }

    /**
     * 获取商品地区信息
     *
     * @param topicItem
     * @param sku
     */
    private void getDistrictInfoInfo(TopicItemInfoDTO topicItem, ItemSku sku) {
        ItemDetail detail = itemDetailService.queryById(sku.getDetailId());
        if (detail == null) {
            logger.error("无商品详细信息");
            throw new ServiceException("无商品详细信息");
        }
        if (detail.getWavesSign() == null) {
            logger.error("商品详细信息获取失败");
            throw new ServiceException("商品详细信息获取失败");
        }
        // TODO:等待商品调整
        if (detail.getWavesSign() != ItemConstant.HAI_TAO) {
            return;
        }
        DistrictInfo info = districtInfoService.queryById(detail
                .getCountryId());
        if (info == null) {
            logger.error("海淘商品没有对应的国家产地");
            throw new ServiceException("海淘商品没有对应的国家产地");
        }
        topicItem.setCountryId(info.getId());
        topicItem.setCountryName(info.getName());
    }
    public ResultInfo<List<TopicItem>> getValidTopicItemBySku(String sku) {
    	List<TopicItem> data = topicItemService.getValidTopicItemBySku(sku);
    	ResultInfo<List<TopicItem>> result = new ResultInfo<>();
    	result.setData(data);
		return result;
	}
}
