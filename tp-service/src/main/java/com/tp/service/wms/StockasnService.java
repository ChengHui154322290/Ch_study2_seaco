
package com.tp.service.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.stg.BMLStorageConstant;
import com.tp.dao.wms.StockasnDao;
import com.tp.dao.wms.StockasnDetailDao;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.SendOrderInfo;
import com.tp.exception.ServiceException;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.PurchaseProduct;
import com.tp.model.sup.PurchaseWarehouse;
import com.tp.model.wms.Stockasn;
import com.tp.model.wms.StockasnDetail;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.wms.IStockasnService;
import com.tp.service.wms.thirdparty.ISendOrderHandlerService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StockasnService extends BaseService<Stockasn> implements IStockasnService {

    @Autowired
    private StockasnDao stockasnDao;

    @Autowired
    private StockasnDetailDao stockasnDetailDao;

    @Autowired
    private ISendOrderHandlerService sendOrderHandlerService;

    @Autowired
    private IItemSkuService itemSkuService;

    @Autowired
    private IItemDetailService itemDetailService;

    @Override
    public BaseDao<Stockasn> getDao() {
        return stockasnDao;
    }


    @Transactional
    public ResultInfo<Object> sentWarehouseOrder(SendOrderInfo info) {

        assembleStockAshInfo(info);

        ResultInfo<Object> resultInfo = sendOrderHandlerService.doSend(info);

        if (!resultInfo.isSuccess()) throw new ServiceException(resultInfo.getMsg().getMessage());

        return resultInfo;
    }

    private void assembleStockAshInfo(SendOrderInfo info) {
        Date cur = new Date();
        Stockasn stockasn;
        List<StockasnDetail> stockasnDetails;
        String orderCode = BMLStorageConstant.InputOrderType.FG.getCode() + info.getPurchaseWarehouse().getPurchaseCode();
        Stockasn stockasnQuery = new Stockasn();
        stockasnQuery.setOrderCode(orderCode);
        List<Stockasn> stockasns = getDao().queryByObject(stockasnQuery);
        if (stockasns != null && stockasns.size() > 0) {
            stockasn = stockasns.get(0);
            StockasnDetail stockasnDetailQuery = new StockasnDetail();
            stockasnDetailQuery.setStockasnId(stockasn.getId());
            stockasnDetails = stockasnDetailDao.queryByObject(stockasnDetailQuery);
        } else {
            stockasn = new Stockasn();
            stockasn.setOrderCode(orderCode);
            stockasn.setCreateTime(cur);
            stockasn.setOrderCreateTime(cur);
            stockasn.setWarehouseId(info.getWarehouse().getId());
            stockasn.setWarehouseName(info.getWarehouse().getName());
            stockasn.setWarehouseCode(info.getWarehouse().getCode());
            stockasn.setSupplierName(info.getSupplierInfo().getName());
            stockasn.setSupplierCode(info.getSupplierInfo().getId().toString());
            stockasn.setPlanStartTime(info.getPurchaseWarehouse().getBookingDate());
            stockasn.setPlanOverTime(info.getPurchaseWarehouse().getBookingDate());
            stockasn.setContractCode(null);
            stockasn.setOrderType("2");//产品采购
//            stockasn.setCreateUser(info.getUser().getCreateUser());
//            stockasn.setCreateUserId(info.getUser().getId());

            calWeightAndAmount(info, stockasn);


            stockasnDao.insert(stockasn);
            stockasnDetails = new ArrayList<>();
            for (PurchaseProduct purchaseProduct : info.getPurchaseProducts()) {
                StockasnDetail detail = new StockasnDetail();
                detail.setStockasnId(stockasn.getId());
                detail.setCreateTime(cur);
                detail.setItemBarcode(purchaseProduct.getBarcode());
                detail.setActualPrice(purchaseProduct.getOrderPrice());
                detail.setItemSku(purchaseProduct.getSku());
                detail.setPrice(purchaseProduct.getStandardPrice());
                detail.setQuantity(purchaseProduct.getCount().intValue());
                detail.setInventoryType(info.getWarehouse().getType().toString());
                stockasnDetails.add(detail);
            }
            if (stockasnDetails.isEmpty()) throw new ServiceException("商品信息为空");
            stockasnDetailDao.batchInsert(stockasnDetails);
        }
        info.setStockasn(stockasn);
        info.setStockasnDetails(stockasnDetails);
    }

    private void calWeightAndAmount(SendOrderInfo info, Stockasn stockasn) {
        List<String> skus = new ArrayList<>();
        int amount = 0;
        for (PurchaseProduct p : info.getPurchaseProducts()) {
            skus.add(p.getSku());
            amount += p.getCount().intValue();
        }
        List<ItemSku> itemSkuList = itemSkuService.selectSkuListBySkuCode(skus);

        List<Long> itemDetails = new ArrayList<>();
        for (ItemSku itemSku : itemSkuList) {
            itemDetails.add(itemSku.getDetailId());
        }

        List<ItemDetail> itemDetailList = itemDetailService.selectByDetailIds(itemDetails);

        double grossWeight = 0;
        double netWeight = 0;
        for (PurchaseProduct p : info.getPurchaseProducts()) {
            Long detailId = getItemDetailId(itemSkuList, p.getSku());
            ItemDetail itemDetail = getItemDetail(itemDetailList, detailId);
            if (itemDetail.getWeight() == null || itemDetail.getWeight() <= 0D || itemDetail.getWeightNet() == null || itemDetail.getWeightNet() <= 0D) {
                throw new ServiceException("SKU:" + p.getSku() + "的净重和毛重不能为空或为0");
            }
            grossWeight += itemDetail.getWeight() * p.getCount();
            netWeight += itemDetail.getWeightNet() * p.getCount();

        }
        stockasn.setGrossWeight(grossWeight);
        stockasn.setNetWeight(netWeight);
        stockasn.setAmount(amount);
    }

    private Long getItemDetailId(List<ItemSku> itemSkuList, String sku) {
        if (CollectionUtils.isEmpty(itemSkuList)) throw new ServiceException("根据SKU:" + sku + "获取SKU详情失败");
        for (ItemSku itemSku : itemSkuList) {
            if (itemSku.getSku().equals(sku)) {
                return itemSku.getDetailId();
            }
        }
        throw new ServiceException("根据SKU:" + sku + "获取SKU详情失败");
    }

    private ItemDetail getItemDetail(List<ItemDetail> itemDetailList, Long detailId) {
        if (CollectionUtils.isEmpty(itemDetailList)) throw new ServiceException("根据商品详情id" + detailId + "获取商品详情失败");
        for (ItemDetail itemDetail : itemDetailList) {
            if (itemDetail.getId().equals(detailId)) {
                return itemDetail;
            }
        }
        throw new ServiceException("根据商品详情id" + detailId + "获取商品详情失败");
    }

    @Override
    public Integer updateStatusToSuccess(List<Long> ids) {
        return stockasnDao.updateStatusToSuccess(ids);
    }
}
