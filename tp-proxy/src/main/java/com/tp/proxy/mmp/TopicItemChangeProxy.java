package com.tp.proxy.mmp;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.model.mmp.TopicItem;
import com.tp.model.mmp.TopicItemChange;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITopicItemChangeService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.ITopicManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销商品代理层
 *
 * @author szy
 */
@Service
public class TopicItemChangeProxy extends BaseProxy<TopicItemChange> {

    @Autowired
    private ITopicItemChangeService topicItemChangeService;

    @Autowired
    private ITopicManagementService topicManagementService;

    @Autowired
    private ITopicItemService topicItemService;

    @Override
    public IBaseService<TopicItemChange> getService() {
        return topicItemChangeService;
    }


    public TopicItemChange getTopicItemChangeDOById(Long topicItemChangeId) {
        return topicItemChangeService.queryById(topicItemChangeId);
    }

    /**
     * 获取原单据spu和suppilerId
     *
     * @param topicId
     * @return
     */
    public Map<String, String> getFilterInfo(Long topicId) {
        TopicItem selectDO = new TopicItem();
        selectDO.setDeletion(DeletionStatus.NORMAL.ordinal());
        selectDO.setTopicId(topicId);
        List<TopicItem> topicItems = topicItemService.queryByObject(selectDO);
        Map<String, String> topicItemInfo = new HashMap<String, String>();
        if (topicItems == null || topicItems.size() == 0) {
//            topicItemInfo.put("spu", "0");
//            topicItemInfo.put("supplierId", "-1");
        	return null;
        } else {
            topicItemInfo.put("spu", topicItems.get(0).getSpu());
            topicItemInfo.put("supplierId",
                    String.valueOf(topicItems.get(0).getSupplierId()));
        }
        return topicItemInfo;
    }

    /**
     * 新增锁定库存
     *
     * @param topicItemChangeId
     * @param amount
     * @return
     */
    public ResultInfo<String> requestAddStock(final Long topicItemChangeId, final int amount,
                                              final Long userId, final String userName) {
        final ResultInfo<String> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo returnResult = topicManagementService.requestAddStockForChangeOrder(topicItemChangeId, amount, userId, userName);
                if (returnResult.isSuccess()) {
                    result.setData(String.valueOf(amount));
                }
            }
        });

        return result;
    }

    /**
     * 退还锁定库存
     *
     * @param topicItemChangeId
     * @param amount
     * @return
     */
    public ResultInfo requestBackStock(final Long topicItemChangeId, final int amount, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                    ResultInfo returnResult = topicManagementService.backStockForChangeOrder(topicItemChangeId, amount, userId, userName);
                    if (returnResult.isSuccess()) {
                        result.setData(String.valueOf(amount));
                    }
            }
        });

        return result;
    }


}
