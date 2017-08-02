package com.tp.proxy.mmp;

import com.tp.common.vo.DAOConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.model.mmp.OlgbHsConfig;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.model.sup.SupplierShop;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.proxy.prd.ItemDetailSalesCountProxy;
import com.tp.proxy.sup.SupplierShopProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IOlgbHsConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 代理层
 *
 * @author szy
 */
@Service
public class OlgbHsConfigProxy extends BaseProxy<OlgbHsConfig> {

    @Autowired
    private IOlgbHsConfigService olgbHsConfigService;

    @Autowired
    private TopicProxy topicProxy;

    @Autowired
    private TopicItemProxy topicItemProxy;

    @Autowired
    private ItemDetailSalesCountProxy itemDetailSalesCountProxy;

    @Autowired
    private SupplierShopProxy supplierShopProxy;

    @Override
    public IBaseService<OlgbHsConfig> getService() {
        return olgbHsConfigService;
    }

    public ResultInfo<List<TopicItem>> getConfig() {
        final ResultInfo<List<TopicItem>> result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Map<String, Object> param = new HashMap();
                param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "sort asc");
                param.put("status", 1);
                List<OlgbHsConfig> list = olgbHsConfigService.queryByParam(param);

                if (CollectionUtils.isEmpty(list)) {
                    result.setData(Collections.EMPTY_LIST);
                    return;
                }

                List<Long> ids = new ArrayList<>();
                List<String> skuCodeList = new ArrayList<>();
                List<TopicItem> topicItemList = new ArrayList<>();
                List<Long> supplierIds = new ArrayList<Long>();
                Iterator<OlgbHsConfig> iterator = list.iterator();
                while (iterator.hasNext()) {
                    OlgbHsConfig config = iterator.next();
                    ResultInfo<Topic> topicResult = topicProxy.queryById(config.getTopicId());

                    if (topicResult.isSuccess() && (topicResult.getData() == null || !topicResult.getData().getStatus().equals(TopicStatus.PASSED.ordinal()))) {
                        ids.add(config.getId());

                        iterator.remove();
                        continue;
                    }
                    ResultInfo<TopicItem> itemResultInfo = topicItemProxy.queryById(config.getTopicItemId());
                    if (itemResultInfo.isSuccess() && (itemResultInfo.getData() == null || itemResultInfo.getData().getDeletion().equals(DeletionStatus.DELETED.ordinal()))) {
                        ids.add(config.getId());
                        iterator.remove();
                    }
                    SupplierShop supplierShopResult =  supplierShopProxy.getSupplierShopInfo(topicResult.getData().getSupplierId());
                    if(supplierShopResult != null){
                        topicResult.getData().setName(supplierShopResult.getShopName());
                    }

                    TopicItem item = itemResultInfo.getData();
                    item.setSortIndex(config.getSort());
                    item.setTopic(topicResult.getData());
                    topicItemList.add(item);
                    supplierIds.add(topicResult.getData().getSupplierId());
                    skuCodeList.add(config.getSku());
                }
                if (!skuCodeList.isEmpty()) {
                    Map<String, Integer> salesCountMap = itemDetailSalesCountProxy.getSalesCountBySkuList(skuCodeList);
                    for (TopicItem topicItem : topicItemList) {
                        Integer salesCount = salesCountMap.get(topicItem.getSku());
                        if (salesCount == null) salesCount = 0;
                        topicItem.setSaledAmount(salesCount);
                    }
                }



                if (!ids.isEmpty()) {
                    olgbHsConfigService.updateStatusByIds(ids);
                }
                result.setData(topicItemList);
            }
        });
        return result;
    }
}
