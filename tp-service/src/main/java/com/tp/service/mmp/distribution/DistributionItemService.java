package com.tp.service.mmp.distribution;

import com.alibaba.fastjson.JSON;
import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dao.mmp.TopicDao;
import com.tp.dao.mmp.TopicItemDao;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.distribution.DistributionItem;
import com.tp.dto.mmp.distribution.DistributionItemQuery;
import com.tp.dto.mmp.enums.ProgressStatus;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.distribution.DistributionItemSort;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.model.prd.ItemSku;
import com.tp.service.mmp.distribution.IDistributionItemService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.BeanUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by ldr on 2016/4/19.
 */
@Service
public class DistributionItemService implements IDistributionItemService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicItemDao topicItemDao;

    @Autowired
    private IItemSkuService itemSkuService;

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public PageInfo<DistributionItem> getDistributionItems(DistributionItemQuery query) {
        Topic topic = new Topic();
        topic.setSalesPartten(SalesPartten.DISTRIBUTION.getValue());
        topic.setProgress(ProgressStatus.DOING.ordinal());
        topic.setStatus(TopicStatus.PASSED.ordinal());

        List<Topic> topicList = topicDao.queryByParam(BeanUtil.beanMap(topic));
        if (CollectionUtils.isEmpty(topicList)) return new PageInfo<>();

        List<Long> topicIds = new ArrayList<>();
        topicList.forEach(new Consumer<Topic>() {
                              @Override
                              public void accept(Topic topic) {
                                  topicIds.add(topic.getId());
                              }
                          }
        );

        if (query.getPageSize() < 1) query.setPageSize(10);
        if (query.getStartPage() < 1) query.setStartPage(1);
        Map<String, Object> param = new HashMap<>();
        param.put("ids", topicIds);
        param.put("start", query.getStart());
        param.put("size", query.getPageSize());
        param.put("sort", DistributionItemSort.getValueByCode(query.getSort()));
        List<TopicItem> topicItemList = topicItemDao.getTopicItemByIdsWithPage(param);
        Integer count = topicItemDao.getTopicItemByIdsWithPageCount(param);
        PageInfo<DistributionItem> page = new PageInfo<>();

        List<ItemSku> itemSkuList = getItemSkus(topicItemList);

        List<DistributionItem> distributionItemList = getDistributionItems(topicItemList, itemSkuList);
        page.setRows(distributionItemList);
        page.setRecords(count);
        page.setSize(query.getPageSize());
        page.setPage(query.getStartPage());
        page.setTotal(count % query.getPageSize() > 0 ? count / query.getPageSize() + 1 : count / query.getPageSize());
        return page;


    }

    private List<ItemSku> getItemSkus(List<TopicItem> topicItemList) {
        List<String> skus = new ArrayList<>();
        topicItemList.forEach(new Consumer<TopicItem>() {
            @Override
            public void accept(TopicItem topicItem) {
                skus.add(topicItem.getSku());
            }
        });

        List<ItemSku> itemSkuList;
        if (skus.isEmpty()) {
            itemSkuList = Collections.emptyList();
        } else {
            ResultInfo<List<ItemSku>> resultInfo = itemSkuService.queryItemListBySkus(skus);
            if (resultInfo.isSuccess())
                itemSkuList = resultInfo.getData();
            else {
                itemSkuList = Collections.emptyList();
                logger.error("[DISTRIBUTION_ITEM_GET_SKU_INFO_ERROR.RESULT=]" + JSON.toJSONString(resultInfo));
            }

        }
        return itemSkuList;
    }

    private List<DistributionItem> getDistributionItems(List<TopicItem> topicItemList, List<ItemSku> skus) {
        List<DistributionItem> distributionItemList = new ArrayList<>();
        for (TopicItem topicItem : topicItemList) {
            DistributionItem distributionItem = new DistributionItem();
            distributionItem.setTopicId(topicItem.getTopicId());
            distributionItem.setTopicItemId(topicItem.getId());
            distributionItem.setSku(topicItem.getSku());
            distributionItem.setItemId(topicItem.getItemId());
            distributionItem.setName(topicItem.getName());
            distributionItem.setSalesPrice(topicItem.getSalePrice());
            distributionItem.setTopicPirce(topicItem.getTopicPrice());
            distributionItem.setPic(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item, topicItem.getTopicImage()));
            distributionItem.setLockStatus(topicItem.getLockStatus());

            setSkuInfo(skus, topicItem, distributionItem);

            distributionItemList.add(distributionItem);
        }
        return distributionItemList;
    }

    private void setSkuInfo(List<ItemSku> itemSkuList, TopicItem topicItem, DistributionItem distributionItem) {
        for (ItemSku itemSku : itemSkuList) {
            if (topicItem.getSku().equals(itemSku.getSku())) {
                distributionItem.setItemStatus(itemSku.getStatus());
                distributionItem.setDisRate(itemSku.getCommisionRate());
                distributionItem.setDisAmount(itemSku.getCommisionRate() == null ? 0 : new BigDecimal(itemSku.getCommisionRate().toString()).multiply(new BigDecimal(topicItem.getTopicPrice().toString())).doubleValue());
                return;
            }
        }
        logger.error("[DISTRIBUTION_ITEM_GET_SKU_INFO_ERROR.SKU_INFO_NOT_EXIST.TOPIC_ITEM:]" + JSON.toJSONString(topicItem));
    }
}
