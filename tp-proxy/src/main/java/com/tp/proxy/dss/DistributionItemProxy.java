package com.tp.proxy.dss;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.distribution.DistributionItem;
import com.tp.dto.mmp.distribution.DistributionItemQuery;
import com.tp.model.dss.GlobalCommision;
import com.tp.model.dss.PromoterInfo;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.mmp.distribution.IDistributionItemService;
import com.tp.util.BigDecimalUtil;

/**
 * Created by ldr on 2016/4/19.
 */
@Service
public class DistributionItemProxy extends AbstractProxy {
    @Autowired
    private IDistributionItemService distributionItemService;
    @Autowired
    private GlobalCommisionProxy globalCommisionProxy;
    @Autowired
    private PromoterInfoProxy promoterInfoProxy;

    public ResultInfo<PageInfo<DistributionItem>> getDistributionItems(final DistributionItemQuery query) {
    	PromoterInfo promoterInfo = promoterInfoProxy.queryById(query.getShopPromoterId()).getData();
        final ResultInfo<PageInfo<DistributionItem>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                PageInfo<DistributionItem> page = distributionItemService.getDistributionItems(query);
                result.setData(page);
            }
        });
        return initRate(result,promoterInfo);
    }
    
    public ResultInfo<PageInfo<DistributionItem>> initRate(ResultInfo<PageInfo<DistributionItem>> result,PromoterInfo promoterInfo){
    	if(result.success && promoterInfo!=null){
    		GlobalCommision globalCommision = globalCommisionProxy.queryLastGlobalCommision().getData();
    		if(null!=globalCommision){
        		result.getData().getRows().forEach(new Consumer<DistributionItem>(){
    				public void accept(DistributionItem distributionItem) {
    					distributionItem.setDisRate(globalCommision.getCurrentCommisionRate(promoterInfo, distributionItem.getDisRate()));
    					distributionItem.setDisAmount(BigDecimalUtil.toPrice(BigDecimalUtil.multiply(distributionItem.getTopicPirce(), distributionItem.getDisRate())));
    				}
        		});
    		}
    	}
    	return result;
    }
}
