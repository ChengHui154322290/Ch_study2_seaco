package com.tp.proxy.wms;

import com.tp.common.vo.supplier.entry.OrderStatus;
import com.tp.exception.ServiceException;
import com.tp.model.sup.PurchaseWarehouse;
import com.tp.model.wms.StockasnFact;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.result.wms.ResultMessage;
import com.tp.service.IBaseService;
import com.tp.service.sup.IPurchaseWarehouseService;
import com.tp.service.wms.IStockasnFactService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 代理层
 *
 * @author szy
 */
@Service
public class StockasnFactProxy extends BaseProxy<StockasnFact> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IStockasnFactService stockasnFactService;

    @Override
    public IBaseService<StockasnFact> getService() {
        return stockasnFactService;
    }

    public ResultMessage purchaseFactOrder(String serviceId, String content) {
        try {
            ResultMessage rm =  stockasnFactService.purchaseFactOrder(serviceId, content);


            return rm;
        } catch (ServiceException se) {
			logger.error("[STOCK_ASC_FACT_ORDER_ERROR.]",se);
			logger.error("[STOCK_ASC_FACT_ORDER_ERROR.PARAM:SERVICE_ID={},CONTENT={}]",serviceId,content);
            return new ResultMessage(false,se.getMessage());
        }catch (Exception e){
            logger.error("[STOCK_ASC_FACT_ORDER_ERROR.]",e);
            logger.error("[STOCK_ASC_FACT_ORDER_ERROR.PARAM:SERVICE_ID={},CONTENT={}]",serviceId,content);
            return new ResultMessage(false,"SYSTEM_ERROR");
        }
    }
}
