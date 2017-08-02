package com.tp.service.sch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tp.common.dao.BaseDao;
import com.tp.dao.sch.*;
import com.tp.dto.sch.enums.SearchBlacklistType;
import com.tp.dto.sch.enums.SearchRecordStatus;
import com.tp.dto.sch.enums.SearchTimestampCode;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.sch.*;
import com.tp.model.sup.SupplierShop;
import com.tp.service.BaseService;
import com.tp.service.sch.ISearchDataService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 搜索数据采集服务
 * Created by ldr on 2016/2/18.
 */
@Service
public class SearchDataService extends BaseService<Search> implements ISearchDataService {

    @Autowired
    private SearchDao searchDao;

    @Autowired
    private TopicSearchDao topicSearchDao;

    @Autowired
    private TopicItemSearchDao topicItemSearchDao;

    @Autowired
    private InventorySearchDao inventorySearchDao;

    @Autowired
    private BrandSearchDao brandSearchDao;

    @Autowired
    private ItemSearchDao itemSearchDao;

    @Autowired
    private TimestampDao timestampDao;

    @Autowired
    private ClearanceChannelSearchDao clearanceChannelSearchDao;

    @Autowired
    private ItemDetailSearchDao itemDetailSearchDao;

    @Autowired
    private DistrictSearchDao districtSearchDao;

    @Autowired
    private ItemSkuSearchDao itemSkuSearchDao;

    @Autowired
    private SearchBlacklistDao searchBlacklistDao;

    @Autowired
    private SearchShopDao searchShopDao;

    @Autowired
    private SupplierShopSearchDao supplierShopSearchDao;

    @Autowired
    private ItemDetailSalesCountSearchDao itemDetailSalesCountSearchDao;

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Override
    public BaseDao<Search> getDao() {
        return searchDao;
    }


    @Override
    public void process() {
        processItemData();
        processShopData();
    }

    public void processItemDataTotal() {

        searchDao.delAll();

        processItemData();

    }

    @Transactional
    public void processItemData() {

        List<Long> topicBlacklist = new ArrayList<>();
        List<String> skuBlacklist = new ArrayList<>();
        getBlacklist(topicBlacklist, skuBlacklist);
        LOGGER.info("[SEARCH_BLACKLIST,TOPIC:]" + topicBlacklist);
        LOGGER.info("[SEARCH_BLACKLIST,SKU:]" + skuBlacklist);

        List<Search> baseSearchData = searchDao.getAll();
        Map<String, Object> queryTopicParam = new HashMap<>();
        queryTopicParam.put("topicBlacklist", topicBlacklist);
        List<TopicSearch> topicSearchList = topicSearchDao.getAllAvailableTopics(queryTopicParam);
        List<Long> topicIds = new ArrayList<>();
        for (TopicSearch topicSearch : topicSearchList) {
            topicIds.add(topicSearch.getTopicId());
        }
        List<TopicItemSearch> topicItemSearchList;
        if (topicIds.isEmpty()) {
            topicItemSearchList = new ArrayList<>();
        } else {
            Map<String, Object> queryTopicItemParam = new HashMap<>();
            queryTopicItemParam.put("topicIds", topicIds);
            queryTopicItemParam.put("skuBlacklist", skuBlacklist);
            topicItemSearchList = topicItemSearchDao.getAllAvailableTopicItem(queryTopicItemParam);
        }

        List<InventorySearch> inventorySearchList = getInventorySearches(topicItemSearchList);

        List<Search> searchList = new ArrayList<>();
        List<Long> searchListTopicItemIds = new ArrayList<>();
        Date cur = searchDao.timestamp();
        updateTimestamp(cur, SearchTimestampCode.ITEM.name());
        List<Long> brandIds = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        List<Long> channelIds = new ArrayList<>();
        List<String> skus = new ArrayList<>();
        Set<Long> supplierIds = new HashSet<>();
        for (TopicItemSearch topicItemSearch : topicItemSearchList) {

            Search search = getSearch(topicItemSearch);
            TopicSearch topicSearch = getTopicSearch(topicSearchList, topicItemSearch);
            search.setTopicStart(topicSearch.getTopicStart());
            search.setTopicEnd(topicSearch.getTopicEnd());
            search.setTopicType(topicSearch.getTopicType());
            search.setSalesPattern(topicSearch.getSalesPattern() == null ? -1 : topicSearch.getSalesPattern());
            search.setShopName(topicSearch.getName());
            List<Integer> platI = getPlatform(topicSearch);
            search.setPlatform(JSONArray.toJSONString(platI));
            search.setInventory(getInventory(inventorySearchList, topicItemSearch));

            search.setCreateTime(cur);
            search.setUpdateTime(cur);
            if (topicItemSearch.getBrandId() != null) {
                brandIds.add(topicItemSearch.getBrandId());
            }
            if (topicItemSearch.getChannelId() != null) {
                channelIds.add(topicItemSearch.getChannelId());
            }
            if (topicItemSearch.getSku() != null) {
                skus.add(topicItemSearch.getSku());
            }
            if(topicItemSearch.getSupplierId() != null){
                supplierIds.add(topicItemSearch.getSupplierId());
            }

            searchListTopicItemIds.add(topicItemSearch.getTopicItemId());
            itemIds.add(topicItemSearch.getItemId());

            searchList.add(search);
        }


        List<ClearanceChannelSearch> clearanceChannelSearchList = getClearanceChannelSearches(channelIds);

        List<ItemSearch> itemSearchList = getItemSearches(itemIds);

        List<ItemDetailSearch> itemDetailSearchList = getItemDetailSearches(itemIds);

        List<BrandSearch> brandSearchList = getBrandSearches(itemDetailSearchList);

        List<DistrictSearch> districtSearchList = getDistrictSearches(itemDetailSearchList);

        List<ItemSkuSearch> itemSkuSearchList = getItemSkuSearches(skus);

        List<SupplierShopSearch> supplierShops = getSupplierShopSearches(supplierIds);

        List<Long> baseSearchTopicItemIds = new ArrayList<>();
        for (Search search : baseSearchData) {
            baseSearchTopicItemIds.add(search.getTopicItemId());
        }

        List<ItemDetailSalesCountSearch> itemDetailSalesCountSearchList = getItemDetailSalesCountSearchList(itemDetailSearchList);

        //新增的数据
        List<Search> newData = new ArrayList<>();
        //需要更新的数据
        List<Search> updateData = new ArrayList<>();
        //需要删除的数据
        List<Long> delData = new ArrayList<>();

        for (Search search : searchList) {

            setCountryAndBrandId(itemDetailSearchList, itemDetailSalesCountSearchList, search);

            search.setBrandName(getBrandName(brandSearchList, search.getBrandId()));

            setCountryName(districtSearchList, search);

            setCategoryIds(itemSearchList, search);

            setClearanceChannel(clearanceChannelSearchList, search);

            setItemStatus(itemSkuSearchList, search);

            setSupplierShopName(supplierShops, search);

            if (!baseSearchTopicItemIds.contains(search.getTopicItemId())) {
                newData.add(search);
            } else {
                Search baseSearch = getByTopicItemId(search.getTopicItemId(), baseSearchData);
                if (!search.equals(baseSearch)) {
                    copy(cur, search, baseSearch);
                    updateData.add(baseSearch);
                }

            }
        }

        for (Long baseId : baseSearchTopicItemIds) {
            if (!searchListTopicItemIds.contains(baseId)) {
                delData.add(baseId);
            }
        }

        if (!newData.isEmpty()) {
            searchDao.batchInsert(newData);
        }
        if (!updateData.isEmpty()) {
            for (Search search : updateData) {
                searchDao.updateDataById(search);
            }
        }
        if (!delData.isEmpty()) {
            searchDao.updateStatusToDelByTopicItemIds(cur, delData);
        }
        LOGGER.info("SEARCH_DATA,UPDATE:" + JSON.toJSONString(updateData));
        LOGGER.info("SEARCH_DATA,DEL:" + JSON.toJSONString(delData));

    }

    private void setSupplierShopName(List<SupplierShopSearch> supplierShops, Search search) {
        if(supplierShops.isEmpty()){ search.setShopName(StringUtils.EMPTY); return;}
        for(SupplierShopSearch supplierShopSearch: supplierShops){
            if(supplierShopSearch.getSupplierId().equals(search.getSupplierId())){
                search.setShopName(supplierShopSearch.getShopName());
                return;
            }
        }
        search.setShopName(StringUtils.EMPTY);
    }

    private List<SupplierShopSearch> getSupplierShopSearches(Set<Long> supplierIds) {
        if(CollectionUtils.isEmpty(supplierIds)) return Collections.EMPTY_LIST;
        List<SupplierShopSearch> supplierShops = supplierShopSearchDao.getSupplierShopsBySupplierIds(new ArrayList<>(supplierIds));
        return supplierShops;
    }

    private List<ItemDetailSalesCountSearch> getItemDetailSalesCountSearchList(List<ItemDetailSearch> itemDetailSearchList) {
        if (CollectionUtils.isEmpty(itemDetailSearchList)) return Collections.EMPTY_LIST;
        List<Long> itemDetailIds = new ArrayList<>();
        for (ItemDetailSearch itemDetailSearch : itemDetailSearchList) {
            itemDetailIds.add(itemDetailSearch.getDetailId());
        }

        return itemDetailSalesCountSearchDao.getSalesCountByItemDetailIds(itemDetailIds);
    }

    private void getBlacklist(List<Long> blacklistTopicIds, List<String> blacklistSKUs) {
        SearchBlacklist blacklistQuery = new SearchBlacklist();
        blacklistQuery.setIsDeleted(0);
        List<SearchBlacklist> searchBlacklists = searchBlacklistDao.queryByObject(blacklistQuery);
        if (CollectionUtils.isEmpty(searchBlacklists)) {
            return;
        }
        for (SearchBlacklist searchBlacklist : searchBlacklists) {
            if (searchBlacklist.getType() == SearchBlacklistType.TOPIC.getCode()) {
                if (NumberUtils.isNumber(searchBlacklist.getValue())) {
                    blacklistTopicIds.add(Long.parseLong(searchBlacklist.getValue()));
                }
            } else if (searchBlacklist.getType() == SearchBlacklistType.ITEM.getCode()) {
                blacklistSKUs.add(searchBlacklist.getValue());
            }
        }
    }

    private List<Integer> getPlatform(TopicSearch topicSearch) {
        String plat = topicSearch.getPlatform();
        List<Integer> platI = new ArrayList<>(8);
        if (plat.contains(String.valueOf(PlatformEnum.ALL.getCode()))) {
            platI.addAll(PlatformEnum.getAllCode());
        } else {
            String[] plats = plat.split(",");
            for (String p : plats) {
                if (NumberUtils.isNumber(p)) {
                    platI.add(Integer.parseInt(p));
                }
            }
        }
        return platI;
    }

    private List<ItemSkuSearch> getItemSkuSearches(List<String> skus) {
        List<ItemSkuSearch> itemSkuSearchList;
        if (skus.isEmpty()) {
            itemSkuSearchList = Collections.EMPTY_LIST;
        } else {
            itemSkuSearchList = itemSkuSearchDao.getBySkus(skus);
        }
        return itemSkuSearchList;
    }

    private List<DistrictSearch> getDistrictSearches(List<ItemDetailSearch> itemDetailSearchList) {
        List<DistrictSearch> districtSearchList;
        if (CollectionUtils.isEmpty(itemDetailSearchList)) {
            districtSearchList = Collections.EMPTY_LIST;
        } else {
            List<Long> districtIds = new ArrayList<>();
            for (ItemDetailSearch itemDetailSearch : itemDetailSearchList) {
                if (itemDetailSearch.getCountryId() != null) {
                    districtIds.add(itemDetailSearch.getCountryId());
                }
            }
            if (!districtIds.isEmpty()) {
                districtSearchList = districtSearchDao.getByIds(districtIds);
            } else {
                districtSearchList = Collections.EMPTY_LIST;
            }
        }
        return districtSearchList;
    }

    private List<ItemDetailSearch> getItemDetailSearches(List<Long> itemIds) {
        List<ItemDetailSearch> itemDetailSearchList;
        if (CollectionUtils.isEmpty(itemIds)) {
            itemDetailSearchList = Collections.EMPTY_LIST;
        } else {
            itemDetailSearchList = itemDetailSearchDao.getByItemIds(itemIds);
        }
        return itemDetailSearchList;
    }

    private List<ItemSearch> getItemSearches(List<Long> itemIds) {
        List<ItemSearch> itemSearchList;
        if (CollectionUtils.isEmpty(itemIds)) {
            itemSearchList = Collections.EMPTY_LIST;
        } else {
            itemSearchList = itemSearchDao.getByItemIds(itemIds);
        }
        return itemSearchList;
    }

    private List<BrandSearch> getBrandSearches(List<ItemDetailSearch> itemDetailSearchList) {
        Set<Long> brandIdsSet = new HashSet<>();
        for (ItemDetailSearch itemDetailSearch : itemDetailSearchList) {
            brandIdsSet.add(itemDetailSearch.getBrandId());
        }
        List<BrandSearch> brandSearchList;
        if (CollectionUtils.isEmpty(brandIdsSet)) {
            brandSearchList = Collections.EMPTY_LIST;
        } else {
            brandSearchList = brandSearchDao.getByBrandIds(new ArrayList<>(brandIdsSet));
        }
        return brandSearchList;
    }

    private List<ClearanceChannelSearch> getClearanceChannelSearches(List<Long> channelIds) {
        List<ClearanceChannelSearch> clearanceChannelSearchList;
        if (CollectionUtils.isEmpty(channelIds)) {
            clearanceChannelSearchList = Collections.EMPTY_LIST;
        } else {
            clearanceChannelSearchList = clearanceChannelSearchDao.getByChannelIds(channelIds);
        }
        return clearanceChannelSearchList;
    }

    private Search getSearch(TopicItemSearch topicItemSearch) {
        Search search = new Search();
        search.setTopicItemId(topicItemSearch.getTopicItemId());
        search.setSku(topicItemSearch.getSku());
        search.setSpu(topicItemSearch.getSpu());
        search.setItemId(topicItemSearch.getItemId());
        search.setItemName(topicItemSearch.getItemName());
        search.setTopicId(topicItemSearch.getTopicId());
        search.setSalePrice(topicItemSearch.getSalePrice());
        search.setTopicPrice(topicItemSearch.getTopicPrice());
        search.setBarCode(topicItemSearch.getBarCode());
        search.setBrandId(topicItemSearch.getBrandId());
        search.setItemImg(topicItemSearch.getItemImage());
        search.setlCategoryId(topicItemSearch.getlCategoryId());
        search.setmCategoryId(topicItemSearch.getmCategoryId());
        search.setsCategoryId(topicItemSearch.getsCategoryId());
        search.setLimitAmount(topicItemSearch.getLimitAmount());
        search.setSpecIds(JSONArray.toJSONString(Arrays.asList()));
        search.setSpecDetails(JSONArray.toJSONString(Arrays.asList()));
        search.setlCategoryName("");
        search.setmCategoryName("");
        search.setsCategoryName("");
        search.setStatus(0);
        search.setSalesCount(0);
        search.setCommentCount(0);
        search.setRating(5F);
        search.setHits(0L);
        search.setItemStatus(topicItemSearch.getLockStatus());
        search.setCountryId(topicItemSearch.getCountryId());
        search.setCountryName(topicItemSearch.getCountryName());
        search.setChannelId(topicItemSearch.getChannelId());
        search.setSupplierId(topicItemSearch.getSupplierId());
        return search;
    }

    private List<InventorySearch> getInventorySearches(List<TopicItemSearch> topicItemSearchList) {
        List<InventorySearch> inventorySearchList;
        if (topicItemSearchList == null || topicItemSearchList.isEmpty()) {
            inventorySearchList = new ArrayList<>();
        } else {
            inventorySearchList = inventorySearchDao.getItemsInventory(topicItemSearchList);
        }
        return inventorySearchList;
    }

    private void setItemStatus(List<ItemSkuSearch> itemSkuSearchList, Search search) {
        for (ItemSkuSearch itemSkuSearch : itemSkuSearchList) {
            if (search.getSku() != null && search.getSku().equals(itemSkuSearch.getSku())) {
                if (search.getItemStatus() == null) {
                    search.setItemStatus(1);
                } else if (search.getItemStatus() == 0 && itemSkuSearch.getStatus() == 1) {
                    search.setItemStatus(1);
                } else {
                    search.setItemStatus(0);
                }
            }
        }
    }

    private void setCountryName(List<DistrictSearch> districtSearchList, Search search) {
        for (DistrictSearch districtSearch : districtSearchList) {
            if (search.getCountryId() != null && search.getCountryId().equals(districtSearch.getId())) {
                search.setCountryName(districtSearch.getName());
                return;
            }
        }
        LOGGER.warn("SEARCH_DATA,COUNTRY_NAME_NOT_FOUND.TOPIC_ID=" + search.getTopicId() + ",SKU:" + search.getSku());
        search.setCountryName("");
    }

    private void setCountryAndBrandId(List<ItemDetailSearch> itemDetailSearchList, List<ItemDetailSalesCountSearch> itemDetailSalesCountSearchList, Search search) {
        for (ItemDetailSearch itemDetailSearch : itemDetailSearchList) {
            if (search.getItemId() != null && search.getItemId().equals(itemDetailSearch.getItemId())) {
                search.setCountryId(itemDetailSearch.getCountryId() == null ? -1 : itemDetailSearch.getCountryId());
                search.setBrandId(itemDetailSearch.getBrandId());
                setSalesCount(itemDetailSalesCountSearchList, search, itemDetailSearch);
                return;
            }
        }
        LOGGER.warn("SEARCH_DATA,COUNTRY_ID_AND_BRAND_ID_NOT_FOUND.TOPIC_ID=" + search.getTopicId() + ",SKU:" + search.getSku());
        search.setCountryId(-1L);
    }

    private void setSalesCount(List<ItemDetailSalesCountSearch> itemDetailSalesCountSearchList, Search search, ItemDetailSearch itemDetailSearch) {
        Integer salesCount = 0;
        for (ItemDetailSalesCountSearch itemDetailSalesCountSearch : itemDetailSalesCountSearchList) {
            if (itemDetailSalesCountSearch.getDetailId().equals(itemDetailSearch.getDetailId())) {
                if (itemDetailSalesCountSearch.getRelSalesCount() != null) {
                    salesCount = salesCount + itemDetailSalesCountSearch.getRelSalesCount().intValue();
                }
                if (itemDetailSalesCountSearch.getDefSalesCount() != null) {
                    salesCount = salesCount + itemDetailSalesCountSearch.getDefSalesCount().intValue();
                }
                break;
            }
        }
        search.setSalesCount(salesCount);

    }

    private void setClearanceChannel(List<ClearanceChannelSearch> clearanceChannelSearchList, Search search) {
        for (ClearanceChannelSearch clearanceChannelSearch : clearanceChannelSearchList) {
            if (search.getChannelId() != null && search.getChannelId().equals(clearanceChannelSearch.getId())) {
                search.setChannelName(clearanceChannelSearch.getName());
                return;
            }
        }
        LOGGER.warn("SEARCH_DATA,CLEARANCE_CHANNEL_NOT_FOUND.TOPIC_ID=" + search.getTopicId() + ",SKU:" + search.getSku());
        search.setChannelName("国内直发");
    }

    private void updateTimestamp(Date cur, String code) {
        Date timestamp = timestampDao.getTimestamp(code);
        if (timestamp == null) {
            timestampDao.insert(cur, code);
        } else {
            timestampDao.update(cur, code);
        }
    }

    void setCategoryIds(List<ItemSearch> itemSearchList, Search search) {
        for (ItemSearch itemSearch : itemSearchList) {
            if (itemSearch.getItemId().equals(search.getItemId())) {
                search.setlCategoryId(itemSearch.getLargeId());
                search.setmCategoryId(itemSearch.getMediumId());
                search.setsCategoryId(itemSearch.getSmallId());
                return;
            }
        }
        LOGGER.warn("SEARCH_DATA,ITEM_CATEGORY_ID_NOT_FOUND.ITEM_ID:" + search.getItemId());
    }

    void copy(Date cur, Search search, Search baseSearch) {
        baseSearch.setTopicId(search.getTopicId());
        baseSearch.setSku(search.getSku());
        baseSearch.setItemName(search.getItemName());
        baseSearch.setSpu(search.getSpu());
        baseSearch.setBarCode(search.getBarCode());
        baseSearch.setPlatform(search.getPlatform());
        baseSearch.setStatus(search.getStatus());
        baseSearch.setTopicPrice(search.getTopicPrice());
        baseSearch.setSalePrice(search.getSalePrice());
        baseSearch.setSalesCount(search.getSalesCount());
        baseSearch.setInventory(search.getInventory());
        baseSearch.setItemImg(search.getItemImg());
        baseSearch.setBrandId(search.getBrandId());
        baseSearch.setBrandName(search.getBrandName());
        baseSearch.setlCategoryId(search.getlCategoryId());
        baseSearch.setlCategoryName(search.getlCategoryName());
        baseSearch.setmCategoryId(search.getmCategoryId());
        baseSearch.setmCategoryName(search.getmCategoryName());
        baseSearch.setsCategoryId(search.getsCategoryId());
        baseSearch.setsCategoryName(search.getsCategoryName());
        baseSearch.setLimitAmount(search.getLimitAmount());
        baseSearch.setSupplierId(search.getSupplierId());
        baseSearch.setTopicStart(search.getTopicStart());
        baseSearch.setTopicEnd(search.getTopicEnd());
        baseSearch.setCommentCount(search.getCommentCount());
        baseSearch.setRating(search.getRating());
        baseSearch.setHits(search.getHits());
        baseSearch.setChannelId(search.getChannelId());
        baseSearch.setChannelName(search.getChannelName());
        baseSearch.setCountryId(search.getCountryId());
        baseSearch.setCountryName(search.getCountryName());
        baseSearch.setItemStatus(search.getItemStatus());
        baseSearch.setUpdateTime(cur);
        baseSearch.setTopicType(search.getTopicType());
        baseSearch.setShopName(search.getShopName());
        baseSearch.setSalesPattern(search.getSalesPattern());
    }

    private Search getByTopicItemId(Long topicItemId, List<Search> searchList) {
        if (CollectionUtils.isEmpty(searchList)) return null;
        for (Search search : searchList) {
            if (search.getTopicItemId().equals(topicItemId))
                return search;
        }
        return null;
    }


    private String getBrandName(List<BrandSearch> brandSearchList, Long brandId) {
        if (brandId == null) return StringUtils.EMPTY;
        if (CollectionUtils.isEmpty(brandSearchList)) return StringUtils.EMPTY;
        for (BrandSearch brandSearch : brandSearchList) {
            if (brandSearch != null && brandSearch.getBrandId() != null && brandSearch.getBrandId().equals(brandId)) {
                return brandSearch.getBrandName();
            }
        }
        return StringUtils.EMPTY;
    }

    private TopicSearch getTopicSearch(List<TopicSearch> topicSearchList, TopicItemSearch topicItemSearch) {
        for (TopicSearch ts : topicSearchList) {
            if (ts.getTopicId().equals(topicItemSearch.getTopicId()))
                return ts;
        }
        return null;
    }

    private Integer getInventory(List<InventorySearch> inventorySearchList, TopicItemSearch topicItemSearch) {
        if (inventorySearchList == null || inventorySearchList.isEmpty()) return 0;

        for (InventorySearch inventorySearch : inventorySearchList) {
            if (inventorySearch.getBizId().equals(topicItemSearch.getTopicId().toString()) && inventorySearch.getSku().equals(topicItemSearch.getSku()))
                return inventorySearch.getInventory();
        }
        return 0;
    }

    @Override
    public void processShopData() {
        Date cur = searchDao.timestamp();
        List<SearchShop> searchShopListBase = searchShopDao.getAll();
        if (searchShopListBase == null) searchShopListBase = Collections.EMPTY_LIST;

        List<Long> shopBaseIds = new ArrayList<>();
        searchShopListBase.forEach(searchShop -> shopBaseIds.add(searchShop.getShopId()));

        List<SupplierShopSearch> supplierShopSearchList = supplierShopSearchDao.getAllSupplierShops();
        List<SearchShop> searchShopListCur = getSearchShopListCur(cur, supplierShopSearchList);

        List<SearchShop> searchShopListNew = new ArrayList<>();
        List<SearchShop> searchShopListUpdate = new ArrayList<>();
        List<Long> searchShopIdsForDelete = new ArrayList<>();
        List<Long> shopCurIds = new ArrayList<>();
        for (SearchShop searchShop : searchShopListCur) {
            shopCurIds.add(searchShop.getShopId());
            if (!shopBaseIds.contains(searchShop.getShopId())) {
                searchShop.setRecordStatus(SearchRecordStatus.NEW.ordinal());
                searchShopListNew.add(searchShop);
                continue;
            }
            SearchShop searchShopBase = getSearchShopBaseByShopId(searchShopListBase, searchShop.getShopId());
            if (searchShop.equals(searchShopBase)) {
                continue;
            }
            searchShopBase.setShopName(searchShop.getShopName());
            searchShopBase.setShopTag(searchShop.getShopTag());
            searchShopBase.setShopBanner(searchShop.getShopBanner());
            searchShopBase.setShopLogo(searchShop.getShopLogo());
            searchShopBase.setShopIntro(searchShop.getShopIntro());
            searchShopBase.setSupplierId(searchShop.getSupplierId());
            searchShopBase.setRecordStatus(SearchRecordStatus.TO_UPDATE.ordinal());
            searchShopBase.setHits(searchShop.getHits());
            searchShopBase.setUpdateTime(cur);
            searchShopListUpdate.add(searchShopBase);
        }

        for (Long id : shopBaseIds) {
            if (shopCurIds.contains(id)) continue;
            searchShopIdsForDelete.add(id);
        }
        if (!searchShopIdsForDelete.isEmpty()) {
            searchShopDao.updateRecordStatusToDel(searchShopIdsForDelete, cur);
        }
        if (!searchShopListUpdate.isEmpty()) {
            for (SearchShop searchShop : searchShopListUpdate) {
                searchShopDao.updateNotNullById(searchShop);
            }
        }
        if (!searchShopListNew.isEmpty()) {
            searchShopDao.batchInsert(searchShopListNew);
        }

        updateTimestamp(cur, SearchTimestampCode.SHOP.name());

    }

    @Override
    public void processShopDataTotal() {
        searchShopDao.deleteTotal();
        processShopData();
    }

    private List<SearchShop> getSearchShopListCur(Date cur, List<SupplierShopSearch> supplierShopSearchList) {
        if (CollectionUtils.isEmpty(supplierShopSearchList)) return Collections.emptyList();
        List<SearchShop> searchShopListCur = new ArrayList<>();
        for (SupplierShopSearch supplierShopSearch : supplierShopSearchList) {
            SearchShop searchShop = new SearchShop();
            searchShop.setShopId(supplierShopSearch.getId() == null ? -1 : supplierShopSearch.getId());
            searchShop.setShopName(String.valueOf(supplierShopSearch.getShopName()));
            searchShop.setShopBanner(String.valueOf(supplierShopSearch.getShopImagePath()));
            searchShop.setShopLogo(String.valueOf(supplierShopSearch.getLogoPath()));
            searchShop.setShopIntro("");// 目前先不收集店铺介绍
            searchShop.setSupplierId(supplierShopSearch.getSupplierId() == null ? -1 : supplierShopSearch.getSupplierId());
            List<String> tags = new ArrayList<>(4);
            addTag(supplierShopSearch.getSearchTitle1(), tags);
            addTag(supplierShopSearch.getSearchTitle2(), tags);
            addTag(supplierShopSearch.getSearchTitle3(), tags);
            addTag(supplierShopSearch.getSearchTitle4(), tags);
            searchShop.setShopTag(JSONArray.toJSONString(tags));
            searchShop.setCreateTime(cur);
            searchShop.setUpdateTime(cur);
            searchShop.setShopStatus(1);
            searchShop.setHits(0);
            searchShop.setRecordStatus(SearchRecordStatus.NORMAL.ordinal());
            searchShopListCur.add(searchShop);
        }
        return searchShopListCur;
    }

    private void addTag(String tag, List<String> tags) {
        if (StringUtils.isNotBlank(tag)) {
            tags.add(tag.trim());
        }
    }

    private SearchShop getSearchShopBaseByShopId(List<SearchShop> searchShopListBase, Long shopId) {
        for (SearchShop searchShopBase : searchShopListBase) {
            if (searchShopBase.getShopId().equals(shopId)) return searchShopBase;
        }
        return null;
    }
}
