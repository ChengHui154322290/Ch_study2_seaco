package com.tp.proxy.sup;

import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.QuotationPriceLog;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.sup.IQuotationPriceLogService;
import com.tp.util.BeanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代理层
 *
 * @author szy
 */
@Service
public class QuotationPriceLogProxy extends BaseProxy<QuotationPriceLog> {

    @Autowired
    private IQuotationPriceLogService quotationPriceLogService;

    @Override
    public IBaseService<QuotationPriceLog> getService() {
        return quotationPriceLogService;
    }


    public ResultInfo<List<QuotationPriceLog>> getLogByQuotationProductId(Long productId) {
        final ResultInfo<List<QuotationPriceLog>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                QuotationPriceLog query = new QuotationPriceLog();
                query.setQuotationProductId(productId);
                List<QuotationPriceLog> logs = quotationPriceLogService.queryByParam(BeanUtil.beanMap(query));
                result.setData(logs);
            }
        });
        return result;
    }
}
