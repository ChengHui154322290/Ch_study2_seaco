package com.tp.proxy.sup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.stg.BMLStorageConstant.InputOrderType;
import com.tp.common.vo.supplier.entry.OrderAuditStatus;
import com.tp.common.vo.supplier.entry.OrderStatus;
import com.tp.common.vo.supplier.entry.PurcharseType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InputOrderDetailDto;
import com.tp.dto.stg.InputOrderDto;
import com.tp.dto.stg.OutputOrderDetailDto;
import com.tp.dto.stg.OutputOrderDto;
import com.tp.dto.sup.WarehouseOrderFact;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.PurchaseProduct;
import com.tp.model.sup.PurchaseWarehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.wms.StockasnDetail;
import com.tp.model.wms.StockasnDetailFact;
import com.tp.model.wms.StockasnFact;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.stg.IInputOrderService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchaseInfoService;
import com.tp.service.sup.IPurchaseProductService;
import com.tp.service.sup.IPurchaseWarehouseService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.service.wms.IStockasnDetailFactService;
import com.tp.service.wms.IStockasnFactService;
/**
 * 仓库预约单代理层
 * @author szy
 *
 */
@Service
public class PurchaseWarehouseProxy extends BaseProxy<PurchaseWarehouse>{

	@Autowired
	private IPurchaseWarehouseService purchaseWarehouseService;
	@Autowired
	private IPurchaseProductService purchaseProductService;
	@Autowired
	private IPurchaseInfoService purchaseInfoService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IInputOrderService inputOrderService;
	@Autowired
	private IOutputOrderService outputOrderService;
	
	@Autowired
	private ISupplierInfoService supplierInfoService;

    @Autowired
    private IStockasnFactService  stockasnFactService;

    @Autowired
    private IStockasnDetailFactService stockasnDetailFactService;

	@Override
	public IBaseService<PurchaseWarehouse> getService() {
		return purchaseWarehouseService;
	}

	public PurchaseWarehouse getWarehouseOrderDetail(String orderType,Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
        if (StringUtils.isNotBlank(orderType)) {
        	params.put("purchaseType", orderType);
        }
        params.put("id", orderCode);
        params.put("auditStatus", OrderAuditStatus.THROUGH.getStatus());
        params.put("status", Constant.ENABLED.YES);
        List<PurchaseInfo> selectDynamic = purchaseInfoService.queryByParam(params);
        if (CollectionUtils.isNotEmpty(selectDynamic)) {
            PurchaseWarehouse warehouseOrderVO = new PurchaseWarehouse();
            PurchaseInfo purchaseInfoRes = selectDynamic.get(0);
            BeanUtils.copyProperties(purchaseInfoRes, warehouseOrderVO);
            Long warehouseId = purchaseInfoRes.getWarehouseId();
            Warehouse warehouseDO = warehouseService.queryById(warehouseId);
            if (null != warehouseDO) {
                warehouseOrderVO.setWarehouseAddr(warehouseDO.getAddress());
                warehouseOrderVO.setWarehouseName(warehouseDO.getName());
                warehouseOrderVO.setWarehouseLinkmanName(warehouseDO.getLinkman());
                warehouseOrderVO.setWarehouseLinkmanTel(warehouseDO.getPhone());
            }
            warehouseOrderVO.setOrderExpectDate(purchaseInfoRes.getExpectDate());
            return warehouseOrderVO;
        }

        return null;
    }

	public ResultInfo<String> submitWarehouseOrderInfo(PurchaseWarehouse purchaseWarehouse) {

        PurchaseProduct purchaseProduct = new PurchaseProduct();
        Long purchaseId = purchaseWarehouse.getPurchaseId();
        PurchaseInfo purchaseInfo = purchaseInfoService.queryById(purchaseId);
        purchaseProduct.setPurchaseId(purchaseId);
        purchaseProduct.setStatus(Constant.ENABLED.YES);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("purchaseId", purchaseId);
        params.put("status", Constant.ENABLED.YES);
        List<PurchaseProduct> purchaseProducts = purchaseProductService.queryByParam(params);
        Long supplierId = purchaseWarehouse.getSupplierId();
        SupplierInfo supplierInfo = supplierInfoService.queryById(supplierId);
        Warehouse warehouse = warehouseService.queryById(purchaseWarehouse.getWarehouseId());
        if (null == supplierInfo || null == warehouse) {
			return new ResultInfo<>(new FailInfo("供应商信息或者仓库信息不存在"));
		}
        ResultInfo<String> resultInfo = null;
        if (PurcharseType.PURCHARSE.getValue().equals(purchaseWarehouse.getPurchaseType())
            || PurcharseType.SELL.getValue().equals(purchaseWarehouse.getPurchaseType())) {
            InputOrderDto inputOrderDto = new InputOrderDto();
            inputOrderDto.setBz(purchaseWarehouse.getPurchaseDesc());
            inputOrderDto.setDhrq(purchaseWarehouse.getOrderExpectDate());
            inputOrderDto.setOrderCode(purchaseWarehouse.getPurchaseCode());
            List<InputOrderDetailDto> products = new ArrayList<InputOrderDetailDto>();
            if (purchaseProducts != null && purchaseProducts.size() > 0) {
                for (PurchaseProduct productDO : purchaseProducts) {
                    InputOrderDetailDto detailDto = new InputOrderDetailDto();
                    detailDto.setItemCount(productDO.getCount().intValue());
                    detailDto.setItemName(productDO.getProductName());
                    detailDto.setSku(productDO.getSku());
                    detailDto.setBarcode(productDO.getBarcode());
                    logger.info("purchaseProducts detailDto:{}", ToStringBuilder.reflectionToString(detailDto));
                    products.add(detailDto);
                }
                PurchaseProduct purchaseProduct2 = purchaseProducts.get(0);
                if (purchaseProduct2 != null) {
                    inputOrderDto.setZdr(purchaseProduct2.getBatchNumber());
                }
            }

            inputOrderDto.setProducts(products);

            inputOrderDto.setType(InputOrderType.FG.getCode());
            inputOrderDto.setWarehouseCode(purchaseWarehouse.getWarehouseId().toString());
            inputOrderDto.setWarehouseId(purchaseWarehouse.getWarehouseId());
            inputOrderDto.setZdrq(purchaseWarehouse.getPurchaseDate());
            logger.info("submitWarehouseOrderInfo inputOrderDto:{}", ToStringBuilder.reflectionToString(inputOrderDto));
            try {
				resultInfo = inputOrderService.sendInputOrder(inputOrderDto);
			} catch (Exception exception) {
				FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,inputOrderDto);
				return new ResultInfo<String>(new FailInfo(failInfo.getDetailMessage()));
			}
            logger.info("submitWarehouseOrderInfo messageOrder:{}", resultInfo);
        } else if (PurcharseType.PURCHARSE_RETURN.getValue().equals(purchaseWarehouse.getPurchaseType())
            || PurcharseType.SELL_RETURN.getValue().equals(purchaseWarehouse.getPurchaseType())) {
            OutputOrderDto oupputOrderDto = new OutputOrderDto();
            oupputOrderDto.setOrderCode(purchaseId + "");
            oupputOrderDto.setShipping("0");
            oupputOrderDto.setProv("none");
            oupputOrderDto.setCity("none");
            oupputOrderDto.setDistrict("none");
            oupputOrderDto.setAddress(supplierInfo.getAddress());
            oupputOrderDto.setServiceCharge(0D);
            oupputOrderDto.setFreight(0D);
            oupputOrderDto.setPostCode("none");

            oupputOrderDto.setName(supplierInfo.getName());
            oupputOrderDto.setMobile(supplierInfo.getPhone());
            oupputOrderDto.setPhone(supplierInfo.getPhone());
            if (null == purchaseInfo.getTotalMoney()) {
                oupputOrderDto.setItemsValue(0D);
            } else {
                oupputOrderDto.setItemsValue(Double.parseDouble(purchaseInfo.getTotalMoney() + ""));
            }

            List<OutputOrderDetailDto> orderDetailDtoList = new ArrayList<OutputOrderDetailDto>();
            for (PurchaseProduct productDO : purchaseProducts) {
                OutputOrderDetailDto detailDto = new OutputOrderDetailDto();
                detailDto.setItemCount(productDO.getCount().intValue());
                detailDto.setItemName(productDO.getProductName());
                if (null == productDO.getSubtotal()) {
                    detailDto.setItemValue(0D);
                } else {
                    detailDto.setItemValue(Double.parseDouble(productDO.getSubtotal() + ""));
                }
                detailDto.setSku(productDO.getSku());
                detailDto.setApp(App.PURCHASE);
                detailDto.setBatchNo(productDO.getBatchNumber());
                detailDto.setBarcode(productDO.getBarcode());
                detailDto.setBizId("none");
                logger.info("purchaseProducts detailDto:{}", ToStringBuilder.reflectionToString(detailDto));
                orderDetailDtoList.add(detailDto);
            }
            oupputOrderDto.setOrderDetailDtoList(orderDetailDtoList);
            logger.info("submitWarehouseOrderInfo oupputOrderDto:{}",
                ToStringBuilder.reflectionToString(oupputOrderDto));
            try {
				ResultInfo<String> returnResult = outputOrderService.returnOutputOrder(oupputOrderDto);
				if (returnResult.isSuccess()) {
					resultInfo = new ResultInfo<>();
				}else{
					resultInfo = new ResultInfo<>(returnResult.getMsg());
				}
			} catch (Exception exception) {
				FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,oupputOrderDto);
				return new ResultInfo<>(failInfo);
			}
            logger.info("submitWarehouseOrderInfo returnOutputOrder:{}", resultInfo);
        }
        if (resultInfo != null && resultInfo.success) {
            PurchaseWarehouse purchaseWarehoused = new PurchaseWarehouse();
            purchaseWarehoused.setAuditStatus(OrderStatus.SUCCESS.getStatus());
            purchaseWarehoused.setId(purchaseWarehouse.getId());
            purchaseWarehouseService.updateNotNullById(purchaseWarehouse);
        } 
        return resultInfo;

    }

	public Long saveAndSubWarehouseOrderInfo(PurchaseWarehouse purchaseWarehouse) {
        purchaseWarehouseService.insert(purchaseWarehouse);
        return purchaseWarehouse.getId();
    }

    public ResultInfo<List<WarehouseOrderFact>> warehouseOrderFact(Long id){
        ResultInfo<List<WarehouseOrderFact>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                PurchaseWarehouse purchaseWarehouse = purchaseWarehouseService.queryById(id);
                PurchaseInfo purchaseInfo = purchaseInfoService.queryById(purchaseWarehouse.getPurchaseId());
                PurchaseProduct productQuery = new PurchaseProduct();
                productQuery.setPurchaseId(purchaseInfo.getId());
                List<PurchaseProduct> productList = purchaseProductService.queryByObject(productQuery);

                String orderId = InputOrderType.FG.getCode()+ purchaseInfo.getId();

                StockasnFact factQuery = new StockasnFact();
                factQuery.setOrderCode(orderId);
                List<StockasnFact> stockasnFactList = stockasnFactService.queryByObject(factQuery);
                StockasnFact fact = stockasnFactList.get(0);

                StockasnDetailFact detailFactQuery = new StockasnDetailFact();
                detailFactQuery.setStockasnFactId(fact.getId());
                List<StockasnDetailFact> detailFactList = stockasnDetailFactService.queryByObject(detailFactQuery);



                List<WarehouseOrderFact> warehouseOrderFactList = new ArrayList<WarehouseOrderFact>();
                for(PurchaseProduct purchaseProduct :productList){
                    WarehouseOrderFact warehouseOrderFact = new WarehouseOrderFact();
                    warehouseOrderFact.setItemName(purchaseProduct.getProductName());
                    warehouseOrderFact.setSku(purchaseProduct.getSku());
                    warehouseOrderFact.setPlanAmount(purchaseProduct.getCount().intValue());
                    warehouseOrderFact.setFactAmount(getFactAmount(detailFactList, purchaseProduct));
                    warehouseOrderFactList.add(warehouseOrderFact);
                }
                result.setData(warehouseOrderFactList);
            }
        });
        return result;
    }

    private Integer getFactAmount(List<StockasnDetailFact> detailFactList, PurchaseProduct purchaseProduct) {
        for(StockasnDetailFact stockasnDetailFact :detailFactList){
            if(stockasnDetailFact.getSku().equals(purchaseProduct.getSku())){
                return stockasnDetailFact.getQuantity();
            }
        }
        return null;
    }
}
