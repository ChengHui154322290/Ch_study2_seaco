package com.tp.service.wms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.dao.BaseDao;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.stg.BMLStorageConstant;
import com.tp.common.vo.supplier.entry.OrderStatus;
import com.tp.common.vo.wms.StockasnConstant;
import com.tp.dao.wms.StockasnDetailFactDao;
import com.tp.dao.wms.StockasnFactDao;
import com.tp.dto.wms.StockasnFactWithDetail;
import com.tp.dto.wms.excel.StockinImportDetailDto;
import com.tp.dto.wms.jdz.JDZPurchaseFactOrder;
import com.tp.dto.wms.jdz.JDZPurchaseFactOrderDetail;
import com.tp.exception.ServiceException;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.PurchaseWarehouse;
import com.tp.model.wms.Stockasn;
import com.tp.model.wms.StockasnDetailFact;
import com.tp.model.wms.StockasnFact;
import com.tp.model.wms.StockinImportDetail;
import com.tp.model.wms.StockinImportLog;
import com.tp.model.wms.jdz.JdzRequestUser;
import com.tp.mq.RabbitMqProducer;
import com.tp.result.wms.ResultMessage;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchaseWarehouseService;
import com.tp.service.wms.IStockasnDetailFactService;
import com.tp.service.wms.IStockasnFactService;
import com.tp.service.wms.IStockasnService;
import com.tp.service.wms.IStockinImportDetailService;
import com.tp.service.wms.IStockinImportLogService;
import com.tp.service.wms.thirdparty.JDZRequestService;
import com.tp.util.DateUtil;
import com.tp.util.JsonUtil;

@Service
public class StockasnFactService extends BaseService<StockasnFact> implements IStockasnFactService {

    @Autowired
    private StockasnFactDao stockasnFactDao;

    @Autowired
    private StockasnDetailFactDao stockasnDetailFactDao;

    @Autowired
    private JDZRequestService jdzRequestService;

    @Autowired
    private IStockasnService stockasnService;

    @Autowired
    private IPurchaseWarehouseService purchaseWarehouseService;

    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private IItemSkuArtService itemSkuArtService;

    @Autowired
    RabbitMqProducer rabbitMqProducer;

    @Autowired
	private IStockinImportDetailService stockinImportDetailService;

	@Autowired
	private IStockasnDetailFactService stockasnDetailFactService;
	
	@Autowired
	private IStockinImportLogService stockinImportLogService;
	
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public BaseDao<StockasnFact> getDao() {
        return stockasnFactDao;
    }

    @Transactional
    @Override
    public ResultMessage purchaseFactOrder(String serviceId, String content) throws Exception {

        logger.info("[WMS_STOCK_ASN_FACT_ORDER.PARAM;serviceId={},content={}]",serviceId,content);
        JdzRequestUser own = jdzRequestService.getRequestUser();
        JdzRequestUser jdz = JSONObject.parseObject(serviceId, JdzRequestUser.class);
        if (!own.equals(jdz)) {
            logger.error("WMS STOCK_ASN_FACT_ERROR:SIGNATURE_ERROR.SERVICE_ID={}", serviceId);
            return new ResultMessage(false, "SIGNATURE_ERROR");
        }
        //String jsonData = JDZHelper.AESEncrypt.decryptor(content);
        String jsonData = (content);

        JDZPurchaseFactOrder order = JsonUtil.parseObject(jsonData, JDZPurchaseFactOrder.class);

        if (order == null) {
            logger.error("WMS STOCK_ASN_FACT_ERROR:ORDER_IS_EMPTY.JSON_DATE={}", jsonData);
            throw new ServiceException("订单信息为空");
        }

        if (order.getOrderInDetails() == null || order.getOrderInDetails().isEmpty()) {
            logger.error("WMS STOCK_ASN_FACT_ERROR:ORDER_ITEM_IS_EMPTY.JSON_DATE={}", jsonData);
            throw new ServiceException("订单商品信息为空");
        }

        AssertUtil.notEmpty(order.getWarehouseCode(), "仓库编码为空");
        AssertUtil.notEmpty(order.getOrderCode(), "采购单号为空");
        AssertUtil.notEmpty(order.getGoodsOwner(), "货主名为空");
        AssertUtil.notEmpty(order.getProviderCode(), "电商编码为空");
        for (JDZPurchaseFactOrderDetail detail : order.getOrderInDetails()) {
            AssertUtil.notEmpty(detail.getSku(), "SKU为空");
            AssertUtil.notNull(detail.getQty(), "实际数量为空");

        }
        if (!order.getOrderCode().startsWith(BMLStorageConstant.InputOrderType.FG.getCode())) {
            throw new ServiceException("订单号错误");
        }
        Long purchaesId = Long.parseLong(order.getOrderCode().substring(2));
        Stockasn stockasnQuery = new Stockasn();
        stockasnQuery.setOrderCode(order.getOrderCode());
        List<Stockasn> stockasnList = stockasnService.queryByObject(stockasnQuery);
        if (CollectionUtils.isEmpty(stockasnList)) {
            throw new ServiceException("入库订单不存在");
        }

        PurchaseWarehouse purchaseWarehouseQuery = new PurchaseWarehouse();
        purchaseWarehouseQuery.setPurchaseId(purchaesId);
        List<PurchaseWarehouse> purchaseWarehouseList = purchaseWarehouseService.queryByObject(purchaseWarehouseQuery);

        if (CollectionUtils.isEmpty(purchaseWarehouseList)) {
            throw new ServiceException("仓库预约单不存在");
        }


        StockasnFactWithDetail stockasnFactWithDetail = insertFact(order, stockasnList);

        List<Long> ids = new ArrayList<>(stockasnList.size());
        for (Stockasn stockasn : stockasnList) {
            ids.add(stockasn.getId());
        }

        Integer count = stockasnService.updateStatusToSuccess(ids);

        if (count == null || count == 0) {
            logger.warn("STOCK_ASN_FACT_WARN:UPDATE_STOCK_ASN_STATUS_HAS_0_RESULT.IGNORE_THIS_REQUEST.PARAM:" + JsonUtil.convertObjToStr(order));
            return new ResultMessage(true);
        }
        try {
            rabbitMqProducer.sendP2PMessage(StockasnConstant.WMS_STOCK_ASN_NOTICE_INVENTORY_MSG, stockasnFactWithDetail);
        } catch (Exception e) {
            logger.error("STOCK_ASN_FACT_ERROR:SEND_MQ_ERROR:", e);
            logger.error("STOCK_ASN_FACT_ERROR:SEND_MQ_ERROR:param={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
            return new ResultMessage(false, "SYSTEM-ERROR");
        }


        List<Long> purchaseIds = new ArrayList<>();
        purchaseWarehouseList.forEach(new Consumer<PurchaseWarehouse>() {
            @Override
            public void accept(PurchaseWarehouse purchaseWarehouse) {
                purchaseIds.add(purchaseWarehouse.getId());
            }
        });
        Map<String,Object> param = new HashMap<>();
        param.put("auditStatus",OrderStatus.DONE.getStatus());
        param.put("ids",purchaseIds);
        purchaseWarehouseService.updateAuditStatusByIds(param);

        logger.info("[WMS_STOCK_ASN_FACT_ORDER_PROCESS_SUCCESS;serviceId={},content={}]",serviceId,content);

        return new ResultMessage(true);
    }

    private StockasnFactWithDetail insertFact(JDZPurchaseFactOrder order, List<Stockasn> stockasnList) throws ParseException {
        Date cur = new Date();

        List<Warehouse> warehouseList = warehouseService.queryByCodes(Arrays.asList(order.getWarehouseCode()));

        if (CollectionUtils.isEmpty(warehouseList)) {
            throw new ServiceException("仓库信息不存在,仓库code:" + order.getWarehouseCode());
        }

        Warehouse warehouse = warehouseList.get(0);

        //不重复写入实际入库信息
        StockasnFact stockasnFactQuery = new StockasnFact();
        stockasnFactQuery.setOrderCode(order.getOrderCode());
        List<StockasnFact> stockasnFactListDB = stockasnFactDao.queryByObject(stockasnFactQuery);
        if (stockasnFactListDB != null && !stockasnFactListDB.isEmpty()) {
            StockasnDetailFact query = new StockasnDetailFact();
            query.setStockasnFactId(stockasnFactListDB.get(0).getId());
            List<StockasnDetailFact> stockasnDetailFactList = stockasnDetailFactDao.queryByObject(query);

            return new StockasnFactWithDetail(stockasnFactListDB.get(0), stockasnDetailFactList);
        }

        StockasnFact stockasnFact = new StockasnFact();
        stockasnFact.setOrderCode(order.getOrderCode());
        stockasnFact.setWarehouseCode(order.getWarehouseCode());
        stockasnFact.setAuditor(order.getAuditor());
        stockasnFact.setAuditTime(getTime(order.getAuditTime()));
        stockasnFact.setCreateTime(cur);
        stockasnFact.setGoodsOwner(order.getGoodsOwner());
        stockasnFact.setProviderCode(order.getProviderCode());
        stockasnFact.setStockasnId(stockasnList.get(0).getId());
        stockasnFact.setWarehouseId(warehouse.getId());
        stockasnFact.setRemark(order.getRemark());
        stockasnFactDao.insert(stockasnFact);

        List<StockasnDetailFact> stockasnDetailFactList = new ArrayList<>();

        List<String> artNumbers = new ArrayList<>();
        for (JDZPurchaseFactOrderDetail detail : order.getOrderInDetails()) {
            artNumbers.add(detail.getSku());
        }
        List<ItemSkuArt> arts = itemSkuArtService.queryByArticleNumbersAndChannel(artNumbers, warehouse.getBondedArea());


        for (JDZPurchaseFactOrderDetail detail : order.getOrderInDetails()) {
            StockasnDetailFact stockasnDetailFact = new StockasnDetailFact();
            stockasnDetailFact.setCreateTime(cur);
            stockasnDetailFact.setQuantity(detail.getQty());
            stockasnDetailFact.setSku(getSkuByArtNumber(arts, detail.getSku()));
            stockasnDetailFact.setSkuTp(detail.getSku());
            stockasnDetailFact.setStockasnFactId(stockasnFact.getId());
            stockasnDetailFactList.add(stockasnDetailFact);
        }

        stockasnDetailFactDao.batchInsert(stockasnDetailFactList);

        return new StockasnFactWithDetail(stockasnFact, stockasnDetailFactList);
    }

    private String getSkuByArtNumber(List<ItemSkuArt> itemSkuArts, String art) {
        if (CollectionUtils.isEmpty(itemSkuArts)) {
            logger.error("GET_SKU_BY_ART_NUMBER_FAILED.ART_NUMBER=" + art);
            throw new ServiceException("根据商品备案号（货号）查询SKU失败.货号:" + art);
        }
        for (ItemSkuArt itemSkuArt : itemSkuArts) {
            if (StringUtils.equals(itemSkuArt.getArticleNumber(), art)) {
                return itemSkuArt.getSku();
            }
        }
        throw new ServiceException("根据商品备案号（货号）查询SKU失败.货号:" + art);
    }


    private Date getTime(String time) throws ParseException {
        try {
            return DateUtil.parseDate(time, DateUtil.NEW_FORMAT);
        } catch (Exception e) {
            logger.error("STOCK_ASN_FACT_ERROR:DATE_FORMAT_ERROR:" + time);
            return null;
        }
    }
    
    /**
	 * 导入数据保存到importDetail表、log表、fact表、detailFact表中
	 */
    @Transactional
    @Override
	public void saveImportDetailAndImportLog(String newName, String userName,List<StockinImportDetailDto> excelList,
			Long stockasnId,String realFileName,String secretKey,String token) {
		List<StockinImportDetail> importList = new ArrayList<StockinImportDetail>();
		//封装入库单导入日志对象
		StockinImportLog importLog = new StockinImportLog();
		importLog.setRealFileName(realFileName);
		importLog.setFileKey(secretKey);
		importLog.setFileName(newName);
		importLog.setCreateTime(new Date());
		importLog.setCreateUser(userName);
		importLog.setTotalAmount(excelList.size());
		importLog.setSuccessCount(0);
		importLog.setFailCount(0);
		importLog.setUploadToken(token);
		importLog = stockinImportLogService.insert(importLog);
		//封装入库单导入对象集合
		Long i=1l;
		for (StockinImportDetailDto importDetailDto : excelList) {
			StockinImportDetail importDetail = new StockinImportDetail();
			importDetail.setLogId(importLog.getId());
			importDetail.setUploadToken(token);
			importDetail.setPurchaseCode(importDetailDto.getPurchaseCode());
			importDetail.setWarehouseCode(importDetailDto.getWarehouseCode());
			importDetail.setWarehouseId(Long.valueOf(importDetailDto.getWarehouseId()));
			importDetail.setBarcode("");
			importDetail.setSkuCode(importDetailDto.getSkuCode());
			importDetail.setArticleNumber(importDetailDto.getArticleNumber());
			importDetail.setFactAmount(Long.valueOf(importDetailDto.getFactAmount()));
			importDetail.setPlanAmount(Long.valueOf(importDetailDto.getExcelIndex()));
			if(StringUtils.isNotEmpty(importDetailDto.getStockinTime())){
				Date stockinTime = new Date(importDetailDto.getStockinTime());
				importDetail.setStockinTime(stockinTime);
			}else{
				importDetail.setStockinTime(new Date());
			}
			importDetail.setStatus(1);
			importDetail.setOperator(userName);
			importDetail.setOperateTime(new Date());
			importDetail.setExcelIndex(i);
			importList.add(importDetail);
			i++;
		}
		//保存导入对象和导入日志
		stockinImportDetailService.batchInsert(importList);
		
		//封装入库单反馈对象
		StockasnFact stockasnFact = new StockasnFact();
		stockasnFact.setStockasnId(stockasnId);
		stockasnFact.setOrderCode(excelList.get(0).getPurchaseCode());
		stockasnFact.setWarehouseCode(excelList.get(0).getWarehouseCode());
		stockasnFact.setWarehouseId(Long.valueOf(excelList.get(0).getWarehouseId()));
//		StockasnFact.setAuditor(auditor);
//		StockasnFact.setAuditTime(auditTime);
//		StockasnFact.setProviderCode(providerCode);
//		StockasnFact.setGoodsOwner(goodsOwner);
		stockasnFact.setRemark("excel导入");
		stockasnFact.setCreateTime(new Date());
		stockasnFact = insert(stockasnFact);
		
		//封装入库单反馈明细对象集合
		List<StockasnDetailFact> detailFactList = new ArrayList<StockasnDetailFact>();
		for (StockinImportDetailDto importDetailDto : excelList) {
			StockasnDetailFact stockasnDetailFact = new StockasnDetailFact();
			stockasnDetailFact.setStockasnFactId(stockasnFact.getId());
			stockasnDetailFact.setSku(importDetailDto.getSkuCode());
			stockasnDetailFact.setSkuTp(importDetailDto.getArticleNumber());
			stockasnDetailFact.setQuantity(Integer.valueOf(importDetailDto.getFactAmount()));
			stockasnDetailFact.setCreateTime(new Date());
			detailFactList.add(stockasnDetailFact);
		}
		stockasnDetailFactService.batchInsert(detailFactList);
		
		//发消息
		StockasnFactWithDetail stockasnFactWithDetail = new StockasnFactWithDetail(stockasnFact, detailFactList);
		try {
			logger.info("入库单导入功能：发消息。。。。。");
            rabbitMqProducer.sendP2PMessage(StockasnConstant.WMS_STOCK_ASN_NOTICE_INVENTORY_MSG, stockasnFactWithDetail);
            Stockasn updateStockasn = new Stockasn();
            updateStockasn.setId(stockasnId);
            updateStockasn.setStatus(1);
            stockasnService.updateNotNullById(updateStockasn);
		} catch (Exception e) {
            logger.error("STOCK_ASN_FACT_ERROR:SEND_MQ_ERROR:", e);
            logger.error("STOCK_ASN_FACT_ERROR:SEND_MQ_ERROR:param={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
//            return new ResultMessage(false, "SYSTEM-ERROR");
            Stockasn updateStockasn = new Stockasn();
            updateStockasn.setId(stockasnId);
            updateStockasn.setStatus(0);
            stockasnService.updateNotNullById(updateStockasn);
		}
	}
}
