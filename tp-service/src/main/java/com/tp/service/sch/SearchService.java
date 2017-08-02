package com.tp.service.sch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.aliyun.opensearch.CloudsearchSearch;
import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.query.AppTopItemPageQuery;
import com.tp.dto.cms.temple.Products;
import com.tp.dto.cms.temple.Topic;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.sch.enums.NavigationType;
import com.tp.dto.sch.enums.Sort;
import com.tp.dto.sch.enums.SortFormula;
import com.tp.exception.ServiceException;
import com.tp.model.bse.NavigationCategory;
import com.tp.model.bse.NavigationCategoryRange;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponRange;
import com.tp.model.mmp.CouponUser;
import com.tp.model.sch.Element;
import com.tp.model.sch.result.*;
import com.tp.query.sch.SearchQuery;
import com.tp.service.bse.INavigationCategoryRangeService;
import com.tp.service.bse.INavigationCategoryService;
import com.tp.service.cms.ISingleBusTemService;
import com.tp.service.mmp.ICouponRangeService;
import com.tp.service.mmp.ICouponService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.sch.ISearchKeyCensusService;
import com.tp.service.sch.ISearchService;
import com.tp.util.BeanUtil;
import com.tp.util.JsonUtil;

/**
 * 搜索服务
 * by ldrs
 */
@Service
public class SearchService implements ISearchService {

    private static final Integer IGNORE_ERROR_CODE = 2112;

    public static final String OR = " OR ";

    public static final String AND = " AND ";

    @Autowired
    private CloudSearchService cloudSearchService;

    @Autowired
    private INavigationCategoryRangeService navigationCategoryRangeService;

    @Autowired
    private INavigationCategoryService navigationCategoryService;

    @Autowired
    private ISearchKeyCensusService searchKeyCensusService;

    @Autowired
    private ICouponService couponService;

    @Autowired
    private ICouponRangeService couponRangeService;

    @Autowired
    private ICouponUserService couponUserService;

    @Autowired
    private ISingleBusTemService singleBusTemService;

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    /**
     * 搜索和分类导航
     *
     * @param query
     * @return 商品信息
     * @throws IOException
     */
    public ResultInfo<PageInfo<ItemResult>> search(SearchQuery query) throws Exception {

        if (query == null || (StringUtils.isBlank(query.getKey()) && query.getNavCategoryId() == null
                && query.getNavBrandId() == null && query.getSalesPattern() != SalesPartten.OFF_LINE_GROUP_BUY.getValue() && query.getCouponId() == null)) {
            LOGGER.error("SEARCH:PARAM_ERROR:PARAM=", JSON.toJSONString(query));
            return new ResultInfo<>(new PageInfo<>());
        }
        CloudsearchSearch search = cloudSearchService.getCloudsearchSearch();

        assembleSearchQuery(query, search);

        assembleUserSearchQuery(query, search);

        //分类导航
        if (query.getNavCategoryId() != null) {
            List<NavigationCategoryRange> list = getNavigationCategoryRanges(query);
            if (CollectionUtils.isEmpty(list)) {
                LOGGER.warn("NAVIGATION_CATEGORY_SEARCH:CATEGORY_ID_HAS_NO_RANGES,CATEGORY_ID:{}", query.getNavCategoryId());
                return new ResultInfo<>(new PageInfo<>());
            }
            assembleNavSearchFilter(search, list);
        }
        //品牌导航
        if (query.getNavBrandId() != null) {
            search.addFilter("contain(brand_id,\"" + query.getNavBrandId() + "\")");
        }

        //查询优惠券可用商品
        if (query.getCouponId() != null) {
            Long topicId = assembleCouponQuery(query, search);

            if(topicId !=null){
                LOGGER.info("SEARCH_PROCESS_TOPIC_SEARCH_TID"+topicId);
               return new ResultInfo<>(  processTopicSearch(query, topicId));
            }
        }

        if (StringUtils.isNoneBlank(query.getKey())) {
            search.setFormulaName(SortFormula.SEARCH.getFormula());
        } else {
            search.setFormulaName(SortFormula.NAVIGATION.getFormula());
        }

        LOGGER.debug("SEARCH_FILTER:" + search.getFilter());
        int startPage = (query.getStartPage() == null ? 1 : query.getStartPage() < 1 ? 1 : query.getStartPage());
        int hits = query.getPageSize() == null ? 20 : query.getPageSize() < 1 ? 20 : query.getPageSize();
        int startHit = (startPage - 1) * hits;
        search.setHits(hits);
        search.setStartHit(startHit);
        if (startPage == 1) {
            searchKeyCensusService.addSearchKeyCensus(query);
        }
        LOGGER.info("SEARCH_FILTER"+search.getFilter());
        String result = search.search();
        LOGGER.debug("SEARCH_RESULT:" + result);
        ItemSearchResult searchResult = JSON.parseObject(result, ItemSearchResult.class);
        if (!StringUtils.equalsIgnoreCase(searchResult.getStatus(), "OK")) {
            LOGGER.error("SEARCH_ERROR,RESULT:" + result);
            LOGGER.error("SEARCH_ERROR,FILTER:" + search.getFilter());
            boolean ignore = isIgnore(searchResult);
            if (!ignore) {
                return new ResultInfo<>(new FailInfo("系统繁忙,请稍后再试", -1));
            }
            LOGGER.error("SEARCH_ERROR,IGNORE_THIS_ERROR.RESULT:" + result);
        }
        PageInfo<ItemResult> page = new PageInfo();
        List<ItemResult> searchResultItemList = searchResult.getResult().getItems();
        for (ItemResult item : searchResultItemList) {
            item.setItem_img(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item, item.getItem_img()));
        }
        page.setRows(searchResultItemList);
        page.setPage(startPage);
        page.setRecords(searchResult.getResult().getTotal());

        return new ResultInfo<>(page);
    }

    private PageInfo<ItemResult> processTopicSearch(SearchQuery query, Long topicId) throws Exception {
        PageInfo<ItemResult> pageInfo = new PageInfo<>();
        AppTopItemPageQuery itemQuery = new AppTopItemPageQuery();
        itemQuery.setSpecialid(topicId);
        itemQuery.setCurpage((query.getStartPage() == null ? 1 : query.getStartPage() < 1 ? 1 : query.getStartPage()));
        itemQuery.setPageSize( query.getPageSize() == null ? 20 : query.getPageSize() < 1 ? 20 : query.getPageSize());
        Topic t= singleBusTemService.loadTopiInfocHtmlApp( itemQuery);
        List<ItemResult> itemResults = new ArrayList<>();

        pageInfo.setPage(query.getStartPage() == null ? 1 : query.getStartPage() < 1 ? 1 : query.getStartPage());
        pageInfo.setSize(query.getPageSize() == null ? 20 : query.getPageSize() < 1 ? 20 : query.getPageSize());
        if(t !=null && t.getProductsList()!=null && !t.getProductsList().isEmpty()){
            for(Products products: t.getProductsList()){
                ItemResult itemResult = new ItemResult();
                itemResult.setSku(products.getSku());
                itemResult.setTopic_id(Long.valueOf(products.getTopicid()));
                itemResult.setSale_price(products.getLastValue());
                itemResult.setTopic_price(products.getNowValue());
                itemResult.setItem_name(products.getName());
                itemResult.setItem_img( products.getImgsrc());
                itemResult.setItem_status(1);
                itemResult.setInventory(1);
                itemResults.add(itemResult);
            }
            pageInfo.setTotal(t.getAllPages());
            pageInfo.setRecords((t.getAllPages() ==null ?0:t.getAllPages()*(query.getPageSize() == null ? 20 : query.getPageSize() < 1 ? 20 : query.getPageSize())));
        }
        pageInfo.setRows(itemResults);
        return  pageInfo;
    }

    private Long assembleCouponQuery(SearchQuery query, CloudsearchSearch search) {
        CouponUser couponUser = couponUserService.queryById(query.getCouponId());
        if(couponUser ==null){
            LOGGER.error("SEARCH_COUPON_SEARCH_COUPON_USER_NOT_EXIST,CID="+query.getCouponId());
            return null;
        }

        Coupon coupon = couponService.queryById(couponUser.getBatchId());
        if(coupon == null){
            LOGGER.error("SEARCH_COUPON_SEARCH_ERROR_COUPON_NOT_EXIST,ID="+query.getCouponId());
            return null;
        }
        CouponRange couponRangeQuery = new CouponRange();
        couponRangeQuery.setCouponId(coupon.getId());
        List<CouponRange> couponRanges = couponRangeService.queryByObject(couponRangeQuery);
        if(CollectionUtils.isEmpty(couponRanges)){
            LOGGER.warn("SEARCH_COUPON_SEARCH_WARN_COUPON_IS_EMPTY,ID="+query.getCouponId());
            return null;
        }

        List<CouponRange> whiteList = new ArrayList<>();
        List<CouponRange> blackList = new ArrayList<>();

        couponRanges.forEach(couponRange -> {
            if (couponRange.getRangeType() != null && couponRange.getRangeType() == 0) {
                whiteList.add(couponRange);
            } else if (couponRange.getRangeType() != null && couponRange.getRangeType() == 1) {
                blackList.add(couponRange);
            }
        });

        List<String> inSKUs = new ArrayList<>();
        List<String> inSmallCates = new ArrayList<>();
        List<String> inMiddleCates = new ArrayList<>();
        List<String> inLargeCates = new ArrayList<>();
        List<String> inBrands = new ArrayList<>();
        List<String> inTopics = new ArrayList<>();
        List<String> noSKUs = new ArrayList<>();
        List<String> noSmallCates = new ArrayList<>();
        List<String> noMiddleCates = new ArrayList<>();
        List<String> noLargeCates = new ArrayList<>();
        List<String> noBrands = new ArrayList<>();
        List<String> noTopics = new ArrayList<>();

        separateRage(whiteList, inSKUs, inSmallCates, inMiddleCates, inLargeCates, inBrands, inTopics);
        if(!inTopics.isEmpty()){
            return Long.valueOf(inTopics.get(0));
        }

        separateRage(blackList, noSKUs, noSmallCates, noMiddleCates, noLargeCates, noBrands, noTopics);

        StringBuilder inBuilder = new StringBuilder();

        if (!inTopics.isEmpty()) {
            String topics = toFilterContainString(inTopics);
            toFilterContainAddAND(inBuilder);
            inBuilder.append("contain(topic_id,\"" + topics + "\")");
        }

        if (!inBrands.isEmpty()) {
            String brands = toFilterContainString(inBrands);
            toFilterContainAddAND(inBuilder);
            inBuilder.append("contain(brand_id,\"" + brands + "\")");
        }

        StringBuilder cateBuilder = new StringBuilder();
        if (!inSmallCates.isEmpty()) {
            String smallCates = toFilterContainString(inSmallCates);
            cateBuilder.append("contain(s_category_id,\"" + smallCates + "\")");
        }
        if (!inMiddleCates.isEmpty()) {
            String middleCates = toFilterContainString(inMiddleCates);
            toFilterContainAddOR(cateBuilder);
            cateBuilder.append("contain(m_category_id,\"" + middleCates + "\")");
        }
        if (!inLargeCates.isEmpty()) {
            String largeCates = toFilterContainString(inLargeCates);
            toFilterContainAddOR(cateBuilder);
            cateBuilder.append("contain(l_category_id,\"" + largeCates + "\")");
        }

        if(cateBuilder.length()>0){
            toFilterContainAddAND(inBuilder);
            inBuilder.append("(").append(cateBuilder.toString()).append(")");
        }

        if (!inSKUs.isEmpty()) {
            toFilterContainAddOR(inBuilder);
            String skuFilter = toFilterContainString(inSKUs);
            inBuilder.append("contain(sku,\"" + skuFilter + "\")");
        }

        StringBuilder noBuilder = new StringBuilder();
        if(inBuilder.length()>0){
            noBuilder.append("(").append(inBuilder).append(")");
        }

        if (!noSKUs.isEmpty()) {
            noSKUs.forEach(sku -> {
                toFilterContainAddAND(noBuilder);
                noBuilder.append("sku!=\"" + sku+"\"");
            });
        }
        if (!noTopics.isEmpty()) {
            noTopics.forEach(topic -> {
                toFilterContainAddAND(noBuilder);
                noBuilder.append("topic_id!=" + topic);
            });
        }

        if (!noBrands.isEmpty()) {
            noBrands.forEach(brand -> {
                toFilterContainAddAND(noBuilder);
                noBuilder.append("brand_id!=" + brand);
            });
        }
        if (!noSmallCates.isEmpty()) {
            noSmallCates.forEach(sc -> {
                toFilterContainAddAND(noBuilder);
                noBuilder.append("s_category_id!=" + sc);
            });
        }
        if (!noMiddleCates.isEmpty()) {
            noMiddleCates.forEach(mc -> {
                toFilterContainAddAND(noBuilder);
                noBuilder.append("m_category_id!=" + mc);
            });
        }
        if (!noLargeCates.isEmpty()) {
            noLargeCates.forEach(lc -> {
                toFilterContainAddAND(noBuilder);
                noBuilder.append("l_category_id!=" + lc);
            });
        }

        LOGGER.info("SEARCH_COUPON_SEARCH_FILTER:"+noBuilder);
        search.addFilter(noBuilder.toString());
        return null;
    }

    private void toFilterContainAddOR(StringBuilder couponFilter) {
        if (couponFilter.length() > 0) {
            couponFilter.append(OR);
        }
    }

    private void toFilterContainAddAND(StringBuilder couponFilter) {
        if (couponFilter.length() > 0) {
            couponFilter.append(AND);
        }
    }

    private String toFilterContainString(List<String> inSKUs) {
        StringBuilder skuBuilder = new StringBuilder();
        inSKUs.forEach(sku -> skuBuilder.append(sku).append("|"));
        String skuFilter = skuBuilder.toString();
        skuFilter = skuFilter.substring(0, skuFilter.lastIndexOf("|"));
        return skuFilter;
    }

    private void separateRage(List<CouponRange> whiteList, List<String> inSKUs, List<String> inSmallCates, List<String> inMiddleCates, List<String> inLargeCates, List<String> inBrands, List<String> inTopics) {
        whiteList.forEach(couponRange -> {
            if (couponRange.getType() != null && couponRange.getType().equals("0") && StringUtils.isNotBlank(couponRange.getCode())) {
                inSKUs.add(couponRange.getCode());
            } else if (couponRange.getType() != null && couponRange.getType().equals("2") && StringUtils.isNotBlank(couponRange.getCode())) {
                inTopics.add(couponRange.getCode());
            } else if (couponRange.getBrandId() != null) {
                inBrands.add(couponRange.getBrandId().toString());
            } else if (couponRange.getCategorySmallId() != null) {
                inSmallCates.add(couponRange.getCategorySmallId().toString());
            } else if (couponRange.getCategoryMiddleId() != null) {
                inMiddleCates.add(couponRange.getCategoryMiddleId().toString());
            } else if (couponRange.getCategoryId() != null) {
                inLargeCates.add(couponRange.getCategoryId().toString());
            } else {
                LOGGER.error("SEARCH_UN_CLEAR_COUPON_RANGE:" + JsonUtil.convertObjToStr(couponRange));
            }
        });
    }


    private boolean isIgnore(ItemSearchResult searchResult) {
        List<SearchError> searchErrorList = searchResult.getErrors();
        boolean ignore = false;
        if (!CollectionUtils.isEmpty(searchErrorList)) {
            for (SearchError searchError : searchErrorList) {
                if (IGNORE_ERROR_CODE.equals(searchError.getCode())) {
                    ignore = true;
                }
            }
        }
        return ignore;
    }

    /**
     * 搜索和分类导航
     *
     * @param query
     * @return 统计信息
     * @throws IOException
     */
    @Override
    public ResultInfo<List<Aggregate>> aggregate(SearchQuery query) throws IOException {
        if (query == null || (StringUtils.isBlank(query.getKey()) && query.getNavCategoryId() == null && query.getNavBrandId() == null)) {
            LOGGER.error("AGGREGATE_SEARCH:PARAM_ERROR:PARAM=", JSON.toJSONString(query));
            return new ResultInfo<>(Collections.EMPTY_LIST);
        }
        CloudsearchSearch search = cloudSearchService.getCloudsearchSearch();
        assembleSearchQuery(query, search);
        if (query.getNavCategoryId() != null) {
            List<NavigationCategoryRange> list = getNavigationCategoryRanges(query);
            if (CollectionUtils.isEmpty(list)) {
                LOGGER.warn("AGGREGATE_SEARCH:CATEGORY_ID_HAS_NO_RANGES,CATEGORY_ID:{}", query.getNavCategoryId());
                return new ResultInfo<>();
            }
            assembleNavSearchFilter(search, list);
        }
        //品牌导航
        if (query.getNavBrandId() != null) {
            search.addFilter("contain(brand_id,\"" + query.getNavBrandId() + "\")");
        }
        search.setFormulaName(SortFormula.DEFAULT.getFormula());

        search.addFetchFields(Arrays.asList("brand_id", "brand_name", "country_id", "country_name"));
        search.addDistinct("brand_id", 1, 1, "false");
        //  search.addDistinct("country_id",1,1,"false");
        search.setStartHit(0);
        search.setHits(30);
        String result = search.search();
        LOGGER.debug("AGGREGATE_SEARCH:RESULT:" + result);
        ItemSearchResult searchResult = JSON.parseObject(result, ItemSearchResult.class);
        if (searchResult == null || !StringUtils.equalsIgnoreCase(searchResult.getStatus(), "OK")) {
            LOGGER.error("AGGREGATE_SEARCH_ERROR,RESULT:" + result);
            boolean ignore = isIgnore(searchResult);
            if (!ignore) {
                return new ResultInfo<>(new FailInfo("系统繁忙,请稍后再试", -1));
            }
            LOGGER.error("SEARCH_ERROR,IGNORE_THIS_ERROR.RESULT:" + result);
        }
        List<ItemResult> itemList = searchResult.getResult().getItems();
        LinkedHashSet<Element> brandInfo = new LinkedHashSet<>();
        LinkedHashSet<Element> countryInfo = new LinkedHashSet<>();

        for (ItemResult item : itemList) {
            brandInfo.add(new Element(String.valueOf(item.getBrand_id()), item.getBrand_name()));
            countryInfo.add(new Element(String.valueOf(item.getCountry_id()), item.getCountry_name()));
        }
        List<Aggregate> aggregateList = new ArrayList<>();
        if (!brandInfo.isEmpty()) {
            aggregateList.add(new Aggregate("brand_id", "品牌", new ArrayList<>(brandInfo)));
        }
        if (!countryInfo.isEmpty()) {
            aggregateList.add(new Aggregate("country_id", "产地", new ArrayList<>(countryInfo)));
        }

        return new ResultInfo(aggregateList);
    }

    @Override
    public ResultInfo<ShopResult> searchShop(SearchQuery query) throws IOException {
        if (query == null || StringUtils.isBlank(query.getKey())) {
            LOGGER.error("SEARCH_SHOP:KEY_IS_EMPTY.RETURN.:PARAM=", JSON.toJSONString(query));
            return new ResultInfo<>();
        }
        CloudsearchSearch search = cloudSearchService.getCloudsearchSearch(cloudSearchService.getShopAppName());

        String tags[] = query.getKey().trim().split(" |,|，");
        List<String> filteredTags = new ArrayList<>();
        for (String t : tags) {
            if (StringUtils.isBlank(t)) continue;
            filteredTags.add(t);
        }
        if (filteredTags.isEmpty()) return new ResultInfo<>();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < filteredTags.size(); i++) {
            builder.append("shop_tag:").append("'").append(filteredTags.get(i)).append("'");
            if (i < filteredTags.size() - 1) builder.append(OR);
        }
//        builder.append(OR).append("shop_name:").append("'").append(query.getKey().trim()).append("'");
        LOGGER.info("SEARCH_SHOP.QUERY_STRING:" + builder.toString());
        search.setQueryString(builder.toString());
        search.setHits(10);
        search.setStartHit(0);
        String result = search.search();
        LOGGER.debug("SEARCH_SHOP_RESULT:" + result);
        ShopSearchResult searchResult = JSON.parseObject(result, ShopSearchResult.class);
        if (searchResult == null || !StringUtils.equalsIgnoreCase(searchResult.getStatus(), "OK")) {
            LOGGER.error("SEARCH_SHOP_ERROR,RESULT:" + result);
            return new ResultInfo<>(new FailInfo("系统繁忙,请稍后再试", -1));
        }
        ShopResult shopResult = null;
        if (searchResult.getResult() != null && !CollectionUtils.isEmpty(searchResult.getResult().getItems())) {
            shopResult = searchResult.getResult().getItems().get(0);
        }

        return new ResultInfo<>(shopResult);
    }

    private void assembleUserSearchQuery(SearchQuery query, CloudsearchSearch search) {
        if (query.getHasInventoryOnly() != null && query.getHasInventoryOnly() == 1) {
            search.addFilter("inventory>0");
        }
        if (query.getBrandId() != null) {
            search.addFilter("brand_id=" + query.getBrandId().toString());
        }
        if (query.getSort() != null && Sort.getByCode(query.getSort()) != null) {
            Sort sort = Sort.getByCode(query.getSort());
            search.addSort(sort.getField(), sort.getType());
        }
        if (query.getCountryId() != null) {
            search.addFilter("country_id=" + query.getCountryId().toString());
        }
        if (query.getSalesPattern() != null && query.getSalesPattern().equals(SalesPartten.OFF_LINE_GROUP_BUY.getValue())) {
            search.addFilter("sales_pattern=" + query.getSalesPattern().toString());
        } else {
            //platform=3 AND (contain(l_category_id,"5|4"))
            search.addFilter("contain(sales_pattern,\"1|2|3|4|5|6|8\")");
        }
    }

    private List<NavigationCategoryRange> getNavigationCategoryRanges(SearchQuery query) {
        NavigationCategoryRange range = new NavigationCategoryRange();
        range.setCategoryId(query.getNavCategoryId());
        return navigationCategoryRangeService.queryByParam(BeanUtil.beanMap(range));
    }

    /**
     * 组装分类导航filter
     *
     * @param search
     * @param list
     */
    private void assembleNavSearchFilter(CloudsearchSearch search, List<NavigationCategoryRange> list) {
        List<NavigationCategoryRange> brands = new ArrayList<>();
        List<NavigationCategoryRange> categories = new ArrayList<>();
        StringBuilder brandFilter = new StringBuilder();
        for (NavigationCategoryRange temp : list) {
            if (temp.getType() == NavigationType.BRAND.getCode()) {
                brands.add(temp);
                brandFilter.append(temp.getContent()).append("|");
            } else if (temp.getType() == NavigationType.CATEGORY.getCode()) {
                categories.add(temp);
            }
        }
        String brand = brandFilter.toString();
        if (brand.endsWith("|")) {
            brand = brand.substring(0, brand.lastIndexOf("|"));
            search.addFilter("contain(brand_id,\"" + brand + "\")");
        }

        if (categories.isEmpty()) {
            return;
        }

        StringBuilder lcate = new StringBuilder();
        StringBuilder mcate = new StringBuilder();
        StringBuilder scate = new StringBuilder();
        for (NavigationCategoryRange temp : categories) {
            String categoryStr = temp.getContent();
            if (StringUtils.isBlank(categoryStr)) {
                LOGGER.warn("NAVIGATION_CATEGORY_SEARCH:RANGE_DATA_ERROR:" + JSON.toJSONString(temp));
                continue;
            }
            String[] catArray = categoryStr.split(",");
            if (catArray == null || catArray.length == 0) {
                LOGGER.warn("NAVIGATION_CATEGORY_SEARCH:RANGE_DATA_ERROR:" + JSON.toJSONString(temp));
                continue;
            }
            if (catArray.length == 3 && StringUtils.isNotBlank(catArray[2])) {
                scate.append(catArray[2]).append("|");
                continue;
            } else if (catArray.length == 2 && StringUtils.isNotBlank(catArray[1])) {
                mcate.append(catArray[1]).append("|");
                continue;
            } else if (catArray.length == 1 && StringUtils.isNotBlank(catArray[0])) {
                lcate.append(catArray[0]).append("|");
                continue;
            } else {
                LOGGER.warn("NAVIGATION_CATEGORY_SEARCH:RANGE_DATA_ERROR:" + JSON.toJSONString(temp));
            }
        }

        String lcateQuery = lcate.toString();
        String mcateQuery = mcate.toString();
        String scateQuery = scate.toString();
        if (lcateQuery.endsWith("|")) {
            lcateQuery = lcateQuery.substring(0, lcateQuery.lastIndexOf("|"));
        }
        if (mcateQuery.endsWith("|")) {
            mcateQuery = mcateQuery.substring(0, mcateQuery.lastIndexOf("|"));
        }
        if (scateQuery.endsWith("|")) {
            scateQuery = scateQuery.substring(0, scateQuery.lastIndexOf("|"));
        }
        if (lcateQuery.length() > 0 || mcateQuery.length() > 0 || scateQuery.length() > 0) {
            StringBuilder cateQueryBuilder = new StringBuilder();
            cateQueryBuilder.append("(");
            if (lcateQuery.length() > 0) {
                cateQueryBuilder.append("contain(l_category_id,\"" + lcateQuery + "\")");
            }
            if (mcateQuery.length() > 0) {
                if (lcateQuery.length() > 0) {
                    cateQueryBuilder.append(OR);
                }
                cateQueryBuilder.append("contain(m_category_id,\"" + mcateQuery + "\")");
            }
            if (scateQuery.length() > 0) {
                if (mcateQuery.length() > 0 || lcateQuery.length() > 0) {
                    cateQueryBuilder.append(OR);
                }
                cateQueryBuilder.append("contain(s_category_id,\"" + scateQuery + "\")");
            }
            cateQueryBuilder.append(")");
            search.addFilter(cateQueryBuilder.toString());
        }
    }


    void assembleSearchQuery(SearchQuery query, CloudsearchSearch search) {
        StringBuilder queryString = new StringBuilder();
        if (StringUtils.isNotBlank(query.getKey())) {
            if (query.getKey().contains("'")) {
                query.setKey(query.getKey().replaceAll("'", " "));
            }
            query.setKey(query.getKey().trim());
            queryString.append("item_name:'").append(query.getKey()).append("'");
            queryString.append(OR);
            queryString.append("brand_name:'").append(query.getKey()).append("'");
            queryString.append(OR);
            queryString.append("country_name:'").append(query.getKey()).append("'");
            processCategories(query, queryString);
            if (queryString.length() > 0) {
                search.setQueryString(queryString.toString());
            }
        }
        if (query.getPlatform() != null) {
            search.addFilter("platform=" + query.getPlatform().getCode());
        }

        LOGGER.debug("SEARCH:SEARCH_QUERY_STRING:" + search.getQuery());
    }

    private void processCategories(SearchQuery query, StringBuilder queryString) {
        //添加 分类
        NavigationCategory categoryQuery = new NavigationCategory();
        categoryQuery.setName(query.getKey());
        List<NavigationCategory> navigationCategories = navigationCategoryService.queryByObject(categoryQuery);
        if (CollectionUtils.isEmpty(navigationCategories)) {
            return;
        }
        NavigationCategoryRange rangeQuery = new NavigationCategoryRange();
        rangeQuery.setCategoryId(navigationCategories.get(0).getId());
        List<NavigationCategoryRange> navigationCategoryRanges = navigationCategoryRangeService.queryByParam(BeanUtil.beanMap(rangeQuery));
        if (CollectionUtils.isEmpty(navigationCategoryRanges)) {
            LOGGER.error("SEARCH:NAVIGATION_CATEGORY_HAS_NO_RANGES.ID:" + navigationCategories.get(0).getId());
            return;
        }
        StringBuilder brandBuilder = new StringBuilder();
        StringBuilder L_categoryBuilder = new StringBuilder();
        StringBuilder M_categoryBuilder = new StringBuilder();
        StringBuilder S_categoryBuilder = new StringBuilder();

        for (NavigationCategoryRange range : navigationCategoryRanges) {
            if (range.getType().equals(NavigationType.BRAND.getCode())) {
                brandBuilder.append("brand_id:'").append(range.getContent()).append("'");
                brandBuilder.append(OR);
            } else if (range.getType().equals(NavigationType.CATEGORY.getCode())) {
                String categoryStr = range.getContent();
                if (StringUtils.isBlank(categoryStr)) {
                    LOGGER.warn("NAVIGATION_CATEGORY_SEARCH:RANGE_DATA_ERROR:" + JSON.toJSONString(range));
                    continue;
                }
                String[] catArray = categoryStr.split(",");
                if (catArray == null || catArray.length == 0) {
                    LOGGER.warn("NAVIGATION_CATEGORY_SEARCH:RANGE_DATA_ERROR:" + JSON.toJSONString(range));
                    continue;
                }
                if (catArray.length == 3 && StringUtils.isNotBlank(catArray[2])) {
                    S_categoryBuilder.append("s_category_id:'").append(catArray[2]).append("'").append(OR);
                } else if (catArray.length == 2 && StringUtils.isNotBlank(catArray[1])) {
                    M_categoryBuilder.append("m_category_id:'").append(catArray[1]).append("'").append(OR);
                } else if (catArray.length == 1 && StringUtils.isNotBlank(catArray[0])) {
                    L_categoryBuilder.append("l_category_id:'").append(catArray[0]).append("'").append(OR);
                } else {
                    LOGGER.warn("NAVIGATION_CATEGORY_SEARCH:RANGE_DATA_ERROR:" + JSON.toJSONString(range));
                }

            }
        }
        String brandQuery = brandBuilder.toString();
        if (brandQuery.endsWith(OR)) brandQuery = brandQuery.substring(0, brandQuery.lastIndexOf(OR));

        String categoriesQuery = L_categoryBuilder.append(M_categoryBuilder).append(S_categoryBuilder).toString();
        if (categoriesQuery.endsWith(OR))
            categoriesQuery = categoriesQuery.substring(0, categoriesQuery.lastIndexOf(OR));

        if (brandQuery.isEmpty() && categoriesQuery.isEmpty()) return;

        StringBuilder FinalCategoriesBuilder = new StringBuilder();
        FinalCategoriesBuilder.append("(");
        if (brandQuery.length() > 0) {
            FinalCategoriesBuilder.append("(");
            FinalCategoriesBuilder.append(brandQuery);
            FinalCategoriesBuilder.append(")");
            if (categoriesQuery.length() > 0) {
                FinalCategoriesBuilder.append(" AND ");
            }
        }
        if (categoriesQuery.length() > 0) {
            FinalCategoriesBuilder.append("(");
            FinalCategoriesBuilder.append(categoriesQuery);
            FinalCategoriesBuilder.append(")");
        }
        FinalCategoriesBuilder.append(")");

        queryString.append(OR);
        queryString.append(FinalCategoriesBuilder.toString());

    }
}
