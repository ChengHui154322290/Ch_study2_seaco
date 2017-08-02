package com.tp.service.wms.thirdparty;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.SendOrderInfo;
import com.tp.dto.wms.jdz.JDZPurchaseOrder;
import com.tp.dto.wms.jdz.JDZPurchaseOrderDetail;
import com.tp.exception.ServiceException;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.wms.StockasnDetail;
import com.tp.result.wms.ResultMessage;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.wms.thirdparty.ISendOrderService;
import com.tp.util.DateUtil;
import com.tp.util.HttpClientUtil;

/**
 * Created by ldr on 2016/6/20.
 */
@Service
public class JDZSendOrderService implements ISendOrderService {

    @Autowired
    private JDZRequestService jdzRequestService;

    @Autowired
    private IItemSkuArtService itemSkuArtService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResultInfo<Object> send(SendOrderInfo info) {
        JDZPurchaseOrder orderInfo = getJdzPurchaseOrderInfo(info);

        List<JDZPurchaseOrderDetail> items = getJdzPurchaseOrderDetailInfos(info);

        orderInfo.setItems(items);
        try {
            String jsonString = JSONObject.toJSONString(orderInfo);
            String content = JDZHelper.AESEncrypt.encrytor(jsonString);
            Map<String, String> headers = jdzRequestService.getHeaders();
            logger.info("[JDZ_SEND_ORDER_PARAM:]"+jsonString);
            String response = HttpClientUtil.postData(jdzRequestService.getPurchaseOrderUrl(), jsonString, "application/json", headers);
            logger.info("[JDZ_SEND_ORDER_RESULT:]" + response);
            ResultMessage result = JSONObject.parseObject(response, ResultMessage.class);
            if (result.isSuccess()) {
                return new ResultInfo<>();
            } else {
                return new ResultInfo<>(new FailInfo(result.getError(), -1));
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            logger.error("[JDZ_SEND_ORDER_ERROR]", e);
            logger.error("[JDZ_SEND_ORDER_ERROR,PARAM:{}]", JSONObject.toJSONString(orderInfo));
            return new ResultInfo<>(new FailInfo("系统错误"));
        } catch (Exception e) {
            logger.error("[JDZ_SEND_ORDER_ERROR]", e);
            logger.error("[JDZ_SEND_ORDER_ERROR,PARAM={}]", JSONObject.toJSONString(orderInfo));
            return new ResultInfo<>(new FailInfo("系统错误"));
        }
    }

    private List<JDZPurchaseOrderDetail> getJdzPurchaseOrderDetailInfos(SendOrderInfo info) {

        List<String> skus = new ArrayList<>();
        for(StockasnDetail detail:info.getStockasnDetails()){
            skus.add(detail.getItemSku());
        }

        List<ItemSkuArt> itemSkuArts = itemSkuArtService.queryBySkusAndChannel(skus,info.getWarehouse().getBondedArea());

        List<JDZPurchaseOrderDetail> items = new ArrayList<>();
        for (StockasnDetail detail : info.getStockasnDetails()) {
            JDZPurchaseOrderDetail item = new JDZPurchaseOrderDetail();
            item.setSku(getSkuArt( itemSkuArts,detail.getItemSku()));
            item.setPrice(new BigDecimal(detail.getActualPrice().toString()));
            item.setQty(new BigDecimal(detail.getQuantity().toString()));
            item.setTotalPrice(item.getPrice().multiply(item.getQty()));
            item.setCountryCode(StringUtils.EMPTY);
            items.add(item);
        }
        return items;
    }

    private String getSkuArt(List<ItemSkuArt> itemSkuArts,String sku){
        if(CollectionUtils.isEmpty(itemSkuArts)) throw new ServiceException("SKU:["+sku+"]没有商品备案号（货号）");
        for(ItemSkuArt art: itemSkuArts){
            if(StringUtils.equals(art.getSku(),sku)){
                return art.getArticleNumber();
            }
        }
        throw new ServiceException("SKU:["+sku+"]没有商品备案号（货号）");
    }

    private JDZPurchaseOrder getJdzPurchaseOrderInfo(SendOrderInfo info) {
        JDZPurchaseOrder orderInfo = new JDZPurchaseOrder();
        orderInfo.setOrderCode(info.getStockasn().getOrderCode());
        orderInfo.setContractCode(StringUtils.EMPTY);
        orderInfo.setSupplierName(info.getStockasn().getSupplierName());
        orderInfo.setSupplierCode(jdzRequestService.getSupplierCode());
        orderInfo.setOperatorCode(String.valueOf(info.getStockasn().getCreateUserId()));
        orderInfo.setOperator(info.getStockasn().getCreateUser());
        orderInfo.setOperateTime(DateUtil.format(info.getStockasn().getCreateTime(), DateUtil.NEW_FORMAT));
        orderInfo.setWarehouseCode(info.getStockasn().getWarehouseCode());
        orderInfo.setWarehouseName(info.getStockasn().getWarehouseName());
        orderInfo.setPlanTime(DateUtil.format(info.getStockasn().getPlanStartTime(), DateUtil.NEW_FORMAT));
        orderInfo.setOrderType("2");
        orderInfo.setIsCheck("0");
        orderInfo.setIsPackage("0");
        orderInfo.setExpressCode(StringUtils.EMPTY);
        orderInfo.setSizeDetail(StringUtils.EMPTY);
        orderInfo.setManualId(jdzRequestService.getManualNo());
        orderInfo.setGrossWeight(new BigDecimal(info.getStockasn().getGrossWeight()));
        orderInfo.setNetWeight(new BigDecimal(info.getStockasn().getNetWeight()));
        orderInfo.setAmount(new BigDecimal(info.getStockasn().getAmount()));
        orderInfo.setWrapType("1");// 包装种类
        orderInfo.setCustomsCode("2992");//关区代码
        orderInfo.setPort("2991");//申报关区
        orderInfo.setType("K3");//业务类别 
        orderInfo.setProviderCode(jdzRequestService.getProviderCode());
        orderInfo.setRemark(info.getStockasn().getRemark()==null?StringUtils.EMPTY:info.getStockasn().getRemark());
        orderInfo.setGoodsOwner(jdzRequestService.getGoodsOwner());
        return orderInfo;
    }

    @Override
    public boolean check(SendOrderInfo info) {
        if (info != null && info.getWarehouse() != null && 
        		StorageConstant.StorageType.COMMON_SEA.getValue().equals(info.getWarehouse().getType()) &&
        		WMSCode.JDZ_HZ.code.equals(info.getWarehouse().getWmsCode()))
            return true;
        return false;
    }
}
