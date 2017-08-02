/**
 *
 */
package com.tp.proxy.mmp;

import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.BseConstant;
import com.tp.common.vo.mmp.TopicItemConstant;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dfsutils.util.DfsDomainUtil;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.enums.TIPurchaseMethod;
import com.tp.dto.prd.SkuDto;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.InventoryDtoQuery;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.exception.ServiceException;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.Spec;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.stg.Warehouse;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.ISpecService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.ITopicManagementService;
import com.tp.service.prd.*;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchasingManagementService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Service(value = "topicItemBatchServiceAO")
public class TopicItemBatchProxy extends AbstractProxy {
    
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private IInventoryQueryService inventoryQueryService;

    @Autowired
    private IInventoryOperService inventoryOperService;

    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private ISpecService specService;

    @Autowired
    private IItemDetailService itemDetailService;

    @Autowired
    private IDistrictInfoService districtInfoService;

    @Autowired
    private IItemDetailSpecService itemDetailSpecService;

    @Autowired
    private IPurchasingManagementService purchasingManagementService;

    @Autowired
    private IItemService itemService;

    @Autowired
    private IItemPicturesService pictureService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ITopicManagementService topicManagementService;

   // @Autowired
    private DfsDomainUtil dfsDomainUtil;

    @Autowired
    private ITopicItemService topicItemService;
    
    @Autowired
    private IItemSkuService itemSkuService;

    /**
     * 根据传入文本型活动商品信息，生成活动商品对象
     *
     * @param batchInput
     * @param sortIndex
     * @return
     */
    public ResultInfo<List<TopicItemInfoDTO>> addBatchItems(final String batchInput, final int sortIndex, int type) {
        final ResultInfo<List<TopicItemInfoDTO>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                String[] inputData;
                AssertUtil.notNull(batchInput, "参数错误");
                if (batchInput.indexOf(TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_LINE_SPILIT_SYMBOL_WIN) > -1) {
                    inputData = batchInput.split(TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_LINE_SPILIT_SYMBOL_WIN);
                } else {
                    inputData = batchInput.split(TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_LINE_SPILIT_SYMBOL_UNIX);
                }
                AssertUtil.notEmpty(inputData, "录入数据结构不正确");
                if (inputData.length > 200) {
                    throw new ServiceException("录入数据超过200行!");
                }
                List<String[]> topicItems = analyzeDataLine(inputData);
                List<TopicItemInfoDTO> topicItemInfoDTOList = getTopicItemInfo(topicItems, sortIndex);
                result.setData(topicItemInfoDTOList);
            }
        });
        return result;
    }

    public ResultInfo batchLockItem(final Long topicId, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = topicItemService.batchLockTopicItem(topicId, userId, userName);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 分析转换数据成Map结构
     *
     * @param inputData
     * @return
     */
    private List<String[]> analyzeDataLine(String[] inputData) {
        List<String[]> dataList = new ArrayList<String[]>();
        for (String lineData : inputData) {
            if (lineData
                    .endsWith(TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_SPILIT_SYMBOL)) {
                lineData += TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_KEY_SPILIT;
            }
            String[] cellData = lineData .split(TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_SPILIT_SYMBOL);
            // 防止数据异常，必须为9列数据
            if (null == cellData || cellData.length != 10) {
                throw new ServiceException("数据录入格式不正确!");
            }
            // 加工数据，去除多余空格
            // 条码
            if (!StringUtils.isBlank(this.getBarCode(cellData))) {
                cellData[0] = this.getBarCode(cellData).trim();
            }
            // 供应商编号
            if (!StringUtils.isBlank(this.getSpCode(cellData))) {
                cellData[1] = this.getSpCode(cellData).trim();
            }
            // sku
            if (!StringUtils.isBlank(this.getSku(cellData))) {
                cellData[2] = this.getSku(cellData).trim();
            }
            // 活动价格
            if (!StringUtils.isBlank(this.getTopicPrice(cellData))) {
                cellData[3] = this.getTopicPrice(cellData).trim();
            }
            // 限购数量
            if (!StringUtils.isBlank(this.getLimitAmount(cellData))) {
                cellData[4] = this.getLimitAmount(cellData).trim();
            }
            // 限购总量
            if (!StringUtils.isBlank(this.getLimitTotal(cellData))) {
                cellData[5] = this.getLimitTotal(cellData).trim();
            }
            // 仓库编号
            if (!StringUtils.isBlank(this.getWHCode(cellData))) {
                cellData[6] = this.getWHCode(cellData).trim();
            }
            // 数据有效性校验
            if ((StringUtils.isBlank(cellData[0]) || StringUtils
                    .isBlank(cellData[1])) && StringUtils.isBlank(cellData[2])) {
                throw new ServiceException("sku或者商家编号+条形码，必须输入一组!");
            }
            String dataKey = "";
            if ((StringUtils.isBlank(cellData[0]) || StringUtils
                    .isBlank(cellData[1]))) {
                dataKey = cellData[0] + "|" + cellData[1];
            } else {
                dataKey = cellData[2];
            }
            if (StringUtils.isBlank(this.getTopicPrice(cellData))
                    || !NumberUtils.isNumber(this.getTopicPrice(cellData))) {
                throw new ServiceException(String.format(
                        "数据[%s]输入的活动价格格式不正确!", dataKey));
            }
            if (StringUtils.isBlank(this.getLimitAmount(cellData))
                    || !NumberUtils.isDigits(this.getLimitAmount(cellData))) {
                throw new ServiceException(String.format(
                        "数据[%s]输入的限购数量格式不正确!", dataKey));
            }
            if (NumberUtils.toInt(this.getLimitAmount(cellData)) > 99) {
                throw new ServiceException(String.format(
                        "限购数量不能大于99!", dataKey));
            }
            if (StringUtils.isBlank(this.getLimitTotal(cellData))
                    || !NumberUtils.isDigits(this.getLimitTotal(cellData))) {
                throw new ServiceException(String.format(
                        "数据[%s]输入的限购总数量格式不正确!", dataKey));
            }
            if (StringUtils.isBlank(this.getWHCode(cellData))) {
                throw new ServiceException(String.format(
                        "数据[%s]输入的仓库编号必须填写!", dataKey));
            }
            if (!StringUtils.isBlank(this.getRemark(cellData))
                    && TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_KEY_SPILIT
                    .equalsIgnoreCase(this.getRemark(cellData))) {
                cellData[8] = "";
            }
            dataList.add(cellData);
            // if (!StringUtils.isBlank(cellData[2])) {
            // dataList.add(cellData);
            // } else if (!StringUtils.isBlank(cellData[0])
            // && !StringUtils.isBlank(cellData[1])) {
            // dataMap.put(cellData[0]
            // + TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_KEY_SPILIT
            // + cellData[1], cellData);
            // }
        }
        return dataList;
    }

    /**
     * 根据录入数据，生成活动商品集合
     *
     * @param topicItemData
     * @param sortIndex
     * @return
     */
    private List<TopicItemInfoDTO> getTopicItemInfo( List<String[]> topicItemDatas, int sortIndex) {
        List<String> whCodes = new ArrayList<String>();
        for (String[] itemData : topicItemDatas) {
            if (itemData[6] != null) {
                whCodes.add(itemData[6]);
            }
        }
        // 获取仓库信息
        Map<String, Warehouse> whMappings = this.getWhIdMappingByWhCodes(whCodes);
        List<TopicItemInfoDTO> infoDtos = this.getItemWHInfo(topicItemDatas, whMappings);
        if (null == infoDtos || infoDtos.size() == 0) {
            throw new ServiceException("无法查询到相关的库存和仓库信息");
        }
        
        List<String> skuQuery = new ArrayList<>();
        for(TopicItemInfoDTO dto : infoDtos) {
        	skuQuery.add(dto.getSku());
        }
        List<ItemSku> skus = itemSkuService.selectSkuListBySkuCode(skuQuery);
        String skuErrorResultString = "";
        for(ItemSku sku : skus) {
        	if(!sku.getStatus().equals(ItemStatusEnum.ONLINE.getValue())){
        		skuErrorResultString += "SKU:" + sku.getSku()+"已下架或作废，不能添加;";
        	}
        }
        if(StringUtils.isNotEmpty(skuErrorResultString)) {
        	 throw new ServiceException(skuErrorResultString);
        }

        List<TopicItemInfoDTO> items = new ArrayList<TopicItemInfoDTO>();
        DistrictInfo selectInfoDo = new DistrictInfo();
        selectInfoDo.setType(BseConstant.DISTRICT_LEVEL.CONTRY.ordinal());
        selectInfoDo.setIsDelete(1);
        List<DistrictInfo> disInfoDOs = districtInfoService.queryByObject(selectInfoDo);
        Map<Long, String> countryMap = new HashMap<>();
        for (DistrictInfo DistrictInfo : disInfoDOs) {
            countryMap.put(DistrictInfo.getId(), DistrictInfo.getName());
        }
        List<Long> detailIds = new ArrayList<>();
        for (TopicItemInfoDTO infoDto : infoDtos) {
            detailIds.add(infoDto.getDetailId());
        }
        List<ItemDetail> details = itemDetailService.selectByDetailIds(detailIds);
        Map<Long, ItemDetail> detailMap = new HashMap<>();
        for (ItemDetail ItemDetail : details) {
            detailMap.put(ItemDetail.getId(), ItemDetail);
        }
        for (TopicItemInfoDTO infoDto : infoDtos) {
            if (!detailMap.containsKey(infoDto.getDetailId())) {
                throw new ServiceException("获取商品详细信息失败:SKU"+String.valueOf(infoDto.getSku())+",detailId:"+String.valueOf(infoDto.getDetailId()));
            }
            ItemDetail ItemDetail = detailMap.get(infoDto.getDetailId());
            this.setTopicItemInfo(infoDto, ItemDetail, countryMap);
            String file = infoDto.getTopicImage();
            if (!StringUtils.isBlank(file)) {
                //infoDto.setImageFullPath(dfsDomainUtil.getFileFullUrl(file));
            }
            items.add(infoDto);
        }
        return items;
    }

    /**
     * 获取仓库及库存相关信息
     *
     * @param topicItemData
     * @return
     */
    private List<TopicItemInfoDTO> getItemWHInfo(List<String[]> topicItemDatas, Map<String, Warehouse> whMapping) {
        Map<String, SkuDto> skuInfos = this.getItemSkuInfo(topicItemDatas);
        if (null == skuInfos || skuInfos.size() == 0) {
            throw new ServiceException("无法查询到相关的SKU!");
        }
        List<TopicItemInfoDTO> topicItemList = new ArrayList<TopicItemInfoDTO>();

        // List<String> whCodes = new ArrayList<String>();
        // while (itemKeys.hasNext()) {
        // String itemKey = itemKeys.next();
        // // 获取仓库编号
        // whCodes.add(topicItemData.get(itemKey)[6]);
        // }
        for (String[] itemData : topicItemDatas) {
            String whCode = this.getWHCode(itemData);
            ItemInputKey key = this.getKey(itemData);
            TopicItemInfoDTO topicItem = new TopicItemInfoDTO();
            setTopicItemInfo(topicItem, itemData);
            if (skuInfos.containsKey(key.getKey())) {
                SkuDto sku = skuInfos.get(key.getKey());
                if (null == sku) {
                    throw new ServiceException("商品信息异常");
                }
                setTopicItemInfo(topicItem, sku);
            }
            if (whMapping.containsKey(whCode)) {
                Warehouse warehouse = whMapping.get(whCode);
                if (warehouse == null) {
                 
                    throw new ServiceException("仓库信息异常");
                }
                setTopicItemInfo(topicItem, warehouse);

            } else {
                throw new ServiceException(String.format( "录入商品[%s],没有有效仓库", key.getKey()));
            }
            topicItemList.add(topicItem);
        }
        return this.getItemWHInfoLink(topicItemList);
    }

    /**
     * 根据SKU对象和仓库Id获取库存信息
     *
     * @return
     */
    private List<TopicItemInfoDTO> getItemWHInfoLink(
            List<TopicItemInfoDTO> itemInfos) {
        // Map<SkuDto, InventoryDto> skuWhInfos = new HashMap<SkuDto,
        // InventoryDto>();

        List<InventoryQuery> queries = new ArrayList<InventoryQuery>();
        for (TopicItemInfoDTO itemDto : itemInfos) {
            if (itemDto != null && itemDto.getSupplierId() != null&& itemDto.getSupplierId() != 0) {
                continue;
            }
            InventoryQuery inventoryQuery = new InventoryQuery();
            inventoryQuery.setSku(itemDto.getSku());
            inventoryQuery.setWarehouseId(itemDto.getStockLocationId());
            queries.add(inventoryQuery);
        }
        List<InventoryDto> inventoryDtos = inventoryQueryService.queryAvailableInventory(queries);
//        List<InventoryDto> inventoryDtos = inventoryQueryService .selectAvailableForSaleBySkuAndWhIdList(queries);
        // 匹配库存信息与Sku信息
        for (TopicItemInfoDTO itemDto : itemInfos) {
            boolean hasInventroy = false;
            if (itemDto.getSupplierId() != null && itemDto.getSupplierId() != 0) {
                InventoryDto inventoryDto = new InventoryDto();
                inventoryDto.setWarehouseId(itemDto.getStockLocationId());
                inventoryDto.setWarehouseName(itemDto.getStockLocation());
                inventoryDto.setInventory(0);
                this.setTopicItemInfo(itemDto, inventoryDto);
                hasInventroy = true;
                continue;
            }
            if (inventoryDtos != null && inventoryDtos.size() > 0) {
                for (InventoryDto inventoryDto : inventoryDtos) {
                    if (null == inventoryDto || null == itemDto.getSku()|| itemDto.getStockLocationId() == null) {
                        throw new ServiceException("库存信息或者商品信息异常!");
                    }
                    // sku和仓库id匹配，退出循环
                    if (null != itemDto.getSku()
                            && itemDto.getSku().equalsIgnoreCase(
                            inventoryDto.getSku())
                            && itemDto.getStockLocationId() == inventoryDto
                            .getWarehouseId()) {
                        this.setTopicItemInfo(itemDto, inventoryDto);
                        hasInventroy = true;
                        break;
                    }
                }
            }
            if (!hasInventroy) {
              
                throw new ServiceException(String.format( "商品[%s]没有发现相关库存!", itemDto.getSku()));
            }
        }
        return itemInfos;
    }

   
    private void setTopicItemInfo(TopicItemInfoDTO topicItem, ItemDetail detail,
                                  Map<Long, String> countryMap) {
        if (detail.getWavesSign() == ItemConstant.HAI_TAO) {
            if (detail.getCountryId() != null && detail.getCountryId() != 0) {
                topicItem.setCountryId(detail.getCountryId());
                topicItem.setCountryName(countryMap.get(detail.getCountryId()));
            } else {
                throw new ServiceException(String.format(
                        "导入海淘商品sku[%s]没有产地国家信息", topicItem.getSku()));
            }
        }
    }

    private void setTopicItemInfo(TopicItemInfoDTO topicItem, SkuDto sku) {
        // sku及基础数据
        if (null != sku) {
            topicItem.setSku(sku.getSku());
            topicItem.setSpu(sku.getSpu());
            topicItem.setItemId(sku.getItemId());
            topicItem.setBarCode(sku.getBarCode());
            topicItem.setBrandId(sku.getBrandId());
            topicItem.setCategoryId(sku.getCategoryId());
            topicItem.setName(sku.getMainTitle());
            topicItem
                    .setPictureSize(TopicItemConstant.TOPIC_ITEM_PIC_SIZE_1_M_1);
            topicItem.setSupplierId(sku.getSpId());
            topicItem.setSupplierName(sku.getSpName());
            topicItem.setSalePrice(sku.getBasicPrice());
            topicItem.setStock(1);
            topicItem.setDetailId(sku.getDetailId());
            topicItem.setTopicImage("");
            if (null != sku.getItemPicturesList()
                    && sku.getItemPicturesList().size() > 0) {
                ItemPictures picureDto = sku.getItemPicturesList().get(0);
                if (null != picureDto && !StringUtils.isBlank(picureDto.getPicture())) {
                    topicItem.setTopicImage(picureDto.getPicture());
                }
            }
            // sku无法提供商品spec,只能循环获取
            String specValue = getSpecValue(sku.getDetailId());
            topicItem.setItemSpec(specValue);
        }
    }

    private void setTopicItemInfo(TopicItemInfoDTO topicItem, String[] itemInfo) {
        // 录入活动商品信息
        if (null != itemInfo && itemInfo.length == 10) {
            if (NumberUtils.isNumber(this.getTopicPrice(itemInfo))) {
                topicItem.setTopicPrice(Double.valueOf(this
                        .getTopicPrice(itemInfo)));
            }
            if (NumberUtils.isNumber(this.getLimitAmount(itemInfo))) {
                topicItem.setLimitAmount(Integer.valueOf(this
                        .getLimitAmount(itemInfo)));
            }
            if (NumberUtils.isNumber(this.getLimitTotal(itemInfo))) {
                topicItem.setLimitTotal(Integer.valueOf(this
                        .getLimitTotal(itemInfo)));
            }
            topicItem.setPurchaseMethod(this.getPurchaseBuy(itemInfo));
            topicItem.setRemark(this.getRemark(itemInfo));
            topicItem.setSortIndex(this.getSortIndex(itemInfo));
        }
    }

    private void setTopicItemInfo(TopicItemInfoDTO topicItem,
                                  Warehouse warehouse) {
        if (warehouse != null) {
        	if(warehouse.getSpId() != topicItem.getSupplierId())
        		throw new ServiceException("仓库编号"+warehouse.getCode()+"与商家名"+topicItem.getSupplierName()+" 或者sku"+topicItem.getSku()+"不对应");
            topicItem.setStockLocationId(warehouse.getId());
            topicItem.setStockLocation(warehouse.getName());
            topicItem.setPutSign(warehouse.getPutSign());
            topicItem.setBondedArea(warehouse.getBondedArea());
            topicItem.setWhType(warehouse.getType());
        } else {
           
            throw new ServiceException("无法查询到相关的仓库信息");
        }
    }

    private void setTopicItemInfo(TopicItemInfoDTO topicItem, InventoryDto inventory) {
        // 仓库信息
        if (null != inventory) {
            topicItem.setStockAmout(inventory.getInventory());
        }
    }

    /**
     * 根据sku或条形码+商家编码获取商品信息
     *
     * @param topicItemData
     * @param bySku
     * @return
     */
    private Map<String, SkuDto> getItemSkuInfo(List<String[]> topicItemDatas) {
        Map<String, SkuDto> skuInfos = new HashMap<String, SkuDto>();
        List<SkuDto> selectSkuDtoBySpBC = null;
        List<String> selectSkuDtoBySkus = null;
      
        for (String[] rowData : topicItemDatas) {
            ItemInputKey key = this.getKey(rowData);
          
            if (!key.isSku()) {
                // 获取SKU为Key的数据结果
                String[] keys = key.getKey().split(
                        TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_KEY_SPILIT);
                if (null == selectSkuDtoBySpBC) {
                    selectSkuDtoBySpBC = new ArrayList<SkuDto>();
                }
                SkuDto selectSku = new SkuDto();
                selectSku.setBarCode(keys[0]);
                // 商家编号
                selectSku.setSpCode(keys[1]);
                selectSkuDtoBySpBC.add(selectSku);
            } else {
                if (null == selectSkuDtoBySkus) {
                    selectSkuDtoBySkus = new ArrayList<String>();
                }
                // 获取BarCode和SpId为Key的数据结果
                selectSkuDtoBySkus.add(key.getKey());
            }
        }
        List<SkuDto> skuDtoBySkus = null;
        List<SkuDto> skuDtoBySpBC = null;
        Map<String, SkuDto> skuDtoMap = new HashMap<String, SkuDto>();
        // 根据 sku 获取Sku对象
        if (null != selectSkuDtoBySkus && selectSkuDtoBySkus.size() > 0) {
            skuDtoBySkus = itemService.querySkuDtoListForPromotion(selectSkuDtoBySkus);
            if (null == skuDtoBySkus || skuDtoBySkus.size() == 0) {
              
                throw new ServiceException( String.format("根据SKU无法查询到相关商品信息!"));
            }
        }
        if (skuDtoBySkus != null && skuDtoBySkus.size() > 0) {
            for (SkuDto skuDto : skuDtoBySkus) {
                if (skuDto == null) {
                    
                    continue;
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.info("get skuinfo from item...sku:" + skuDto.getSku());
                    LOGGER.info("get skuinfo from item...spu:" + skuDto.getSpu());
                    LOGGER.info("save skuinfo from item...key:" + skuDto.getSku());
                }
                skuDtoMap.put(skuDto.getSku(), skuDto);
            }
        }
        // 根据 条形码 和 商家编号 获取Sku对象
        if (null != selectSkuDtoBySpBC && selectSkuDtoBySpBC.size() > 0) {
            skuDtoBySpBC = itemService
                    .querySkuDtoListForPromotionWithBarCodeAndSpCode(selectSkuDtoBySpBC);
            if (null == skuDtoBySpBC || skuDtoBySpBC.size() == 0) {
                LOGGER.error("get skuinfo from item...is null");
                throw new ServiceException( String.format("根据条码和商家编号无法查询到相关商品信息!"));
            }
        }
        if (null != skuDtoBySpBC && skuDtoBySpBC.size() > 0) {
            for (SkuDto skuDto : skuDtoBySpBC) {
                if (skuDto == null) {
                    LOGGER.error("get skuinfo from item...is null");
                    continue;
                }
                LOGGER.info("get skuinfo from item...sku:" + skuDto.getSku());
                LOGGER.info("get skuinfo from item...spu:" + skuDto.getSpu());
                LOGGER.info("save skuinfo from item...key:" + skuDto.getBarCode()
                        + TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_KEY_SPILIT
                        + skuDto.getSpCode());
                skuDtoMap.put(skuDto.getBarCode()
                        + TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_KEY_SPILIT
                        + skuDto.getSpCode(), skuDto);
            }
        }
        for (String[] itemData : topicItemDatas) {
            ItemInputKey key = this.getKey(itemData);
            if (skuDtoMap.containsKey(key.getKey())) {
                skuInfos.put(key.getKey(), skuDtoMap.get(key.getKey()));
            }
        }
        LOGGER.info("end get item info .. ");
        return skuInfos;
    }

    /**
     * 获取商品规格内容
     *
     * @param detailId
     * @return
     */
    private String getSpecValue(Long detailId) {
        List<Spec> specDOs = getSpecInfo(detailId);
        List<String> specList = new ArrayList<String>();
        if (null != specDOs && specDOs.size() > 0) {
            for (Spec specDo : specDOs) {
                specList.add(specDo.getSpec());
            }
            return StringUtils.join(specList, "<br/>");
        }
        return "";
    }

    /**
     * 根据仓库编码集合，获得仓库Id集合
     *
     * @param whCodes 仓库编码列表
     * @return
     */
    private Map<String, Warehouse> getWhIdMappingByWhCodes(
            List<String> whCodes) {
        Map<String, Warehouse> warehouseIds = new HashMap<String, Warehouse>();
        List<Warehouse> warehouses = warehouseService.queryByCodes(whCodes);
        if (null == warehouses || warehouses.size() == 0) {
            throw new ServiceException("无法查询到相关的仓库信息!");
        }
        for (Warehouse whDO : warehouses) {
            warehouseIds.put(whDO.getCode(), whDO);
        }
        return warehouseIds;
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
        List<ItemDetailSpec> detailSpecs = itemDetailSpecService
                .queryByObject(Spec);
        List<Spec> specDOs = null;
        if (null != detailSpecs && detailSpecs.size() > 0) {
            List<Long> specIds = new ArrayList<Long>();
            for (ItemDetailSpec spec : detailSpecs) {
                specIds.add(spec.getSpecId());
            }
            // 根据Id查询商品规格参数
            specDOs = specService.selectListSpec(specIds,
                    ItemConstant.ALL_DATAS);
        }
        return specDOs;
    }

    private String getBarCode(String[] rowData) {
        if (rowData != null && rowData.length > 0) {
            return rowData[0];
        }
        return "";
    }

    private String getSpCode(String[] rowData) {
        if (rowData != null && rowData.length > 1) {
            return rowData[1];
        }
        return "";
    }

    private String getSku(String[] rowData) {
        if (rowData != null && rowData.length > 2) {
            return rowData[2];
        }
        return "";
    }

    private String getTopicPrice(String[] rowData) {
        if (rowData != null && rowData.length > 3) {
            return rowData[3];
        }
        return "";
    }

    private String getLimitAmount(String[] rowData) {
        if (rowData != null && rowData.length > 4) {
            return rowData[4];
        }
        return "";
    }

    private String getLimitTotal(String[] rowData) {
        if (rowData != null && rowData.length > 5) {
            return rowData[5];
        }
        return "";
    }

    private String getWHCode(String[] rowData) {
        if (rowData != null && rowData.length > 6) {
            return rowData[6];
        }
        return "";
    }

    private Integer getPurchaseBuy(String[] rowData) {
        if (rowData != null && rowData.length > 7) {
            if (!StringUtils.isBlank(rowData[7])
                    && rowData[7]
                    .equalsIgnoreCase(String
                            .valueOf(TIPurchaseMethod.AT_ONCE_PURCHASE
                                    .getKey()))) {
                return TIPurchaseMethod.AT_ONCE_PURCHASE.getKey();
            }
        }
        return TIPurchaseMethod.NORMAL.getKey();
    }

    private String getRemark(String[] rowData) {
        if (rowData != null && rowData.length > 8) {
            return rowData[8];
        }
        return "";
    }
    private Integer getSortIndex(String[] rowData) {
        if (rowData != null && rowData.length > 9) {
        	if(StringUtils.isNotEmpty(rowData[9])) {
				try {
					return Integer.valueOf(rowData[9].trim());
				} catch (Exception e) {
					return 1;
				}
			}
        }
        return 0;
    }

    private ItemInputKey getKey(String[] rowData) {
        ItemInputKey key = new ItemInputKey();
        if (StringUtils.isBlank(this.getSku(rowData))) {
            key.setKey(this.getBarCode(rowData)
                    + TopicItemConstant.TOPIC_ITEM_BATCH_INPUT_KEY_SPILIT
                    + this.getSpCode(rowData));
            key.setSku(false);
            return key;
        }
        key.setKey(this.getSku(rowData));
        return key;
    }
}

class ItemInputKey {

    private String key;

    private boolean isSku = true;

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the isSku
     */
    public boolean isSku() {
        return isSku;
    }

    /**
     * @param isSku the isSku to set
     */
    public void setSku(boolean isSku) {
        this.isSku = isSku;
    }

}