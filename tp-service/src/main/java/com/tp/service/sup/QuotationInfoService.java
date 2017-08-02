package com.tp.service.sup;

import java.util.*;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.dao.sup.AuditRecordsDao;
import com.tp.dao.sup.QuotationInfoDao;
import com.tp.dao.sup.QuotationPriceLogDao;
import com.tp.dao.sup.QuotationProductDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.sup.enums.QuotationPriceLogType;
import com.tp.model.sup.*;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractService;
import com.tp.service.sup.IQuotationInfoService;
import com.tp.service.sup.IQuotationProductService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.BeanUtil;
import com.tp.util.CodeCreateUtil;

@Service
public class QuotationInfoService extends BaseService<QuotationInfo> implements IQuotationInfoService {

    @Autowired
    private QuotationInfoDao quotationInfoDao;
    @Autowired
    private QuotationProductDao quotationProductDao;
    @Autowired
    private AuditRecordsDao auditRecordsDao;

    @Autowired
    private ISupplierInfoService supplierInfoService;
    @Autowired
    private IContractService contractService;

    @Autowired
    IQuotationProductService quotationProductService;

    @Autowired
    QuotationPriceLogDao quotationPriceLogDao;

    @Override
    public BaseDao<QuotationInfo> getDao() {
        return quotationInfoDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> auditQuotation(final QuotationInfo quotationInfo, final Integer auditStatus, final AuditRecords record) {
        if (null != quotationInfo && null != quotationInfo.getId()) {
            final QuotationInfo oldQuotationInfo = this.queryById(quotationInfo.getId());
            if (null == oldQuotationInfo || !SupplierConstant.O_PREVIOUS_AUDIT_STATUS.get(auditStatus).contains(oldQuotationInfo.getAuditStatus())) {
                return new ResultInfo<>(new FailInfo("报价单审核状态异常！ 审核失败。"));
            }

            final QuotationInfo updateInfo = new QuotationInfo();
            updateInfo.setAuditStatus(auditStatus);
            updateInfo.setUpdateTime(new Date());
            updateInfo.setId(quotationInfo.getId());
            final int num = quotationInfoDao.updateNotNullById(updateInfo);
            if (num < 1) {
                return new ResultInfo<>(new FailInfo("审核异常。"));
            }
            // 更改product状态为审核通过
            quotationProductDao.updateAuditStatus(quotationInfo.getId(), auditStatus, Constant.DEFAULTED.YES, quotationInfo.getUpdateUser());
            auditRecordsDao.insert(record);
        } else {
            return new ResultInfo<>(new FailInfo("审核异常。"));
        }
        return new ResultInfo<>(Boolean.TRUE);
    }

    /**
     * <pre>
     * 校验要保存的信息
     * </pre>
     *
     * @return
     * @throws DAOException
     */
    private ResultInfo<Boolean> checkSaveInfo(final QuotationInfo auotationInfo) {
        final Long supplierId = auotationInfo.getSupplierId();
        final Long contractId = auotationInfo.getContractId();
        if (null == supplierId) {
            return new ResultInfo<>(new FailInfo("供应商id不能为空"));
        }
        if (null == contractId) {
            return new ResultInfo<>(new FailInfo("合同id不能为空"));
        }
        final SupplierInfo supplierInfo = supplierInfoService.queryById(supplierId);
        if (null == supplierInfo) {
            return new ResultInfo<>(new FailInfo("供应商找不到。"));
        }
        final Contract contract = contractService.queryById(contractId);
        if (null == contract) {
            return new ResultInfo<>(new FailInfo("合同找不到。"));
        }
        return new ResultInfo<>(Boolean.TRUE);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<QuotationInfo> saveQuotationInfo(final QuotationInfo quotationInfo) {
        final List<QuotationProduct> productList = quotationInfo.getQuotationProductList();
        if (null == productList || productList.size() == 0) {
            return new ResultInfo<>(new FailInfo("报价单商品不能为空。"));
        }
        final ResultInfo<?> msg = checkSaveInfo(quotationInfo);
        if (!msg.success) {
            return new ResultInfo<>(msg.msg);
        }
        quotationInfo.setQuotationCode(CodeCreateUtil.initCodeValue());
        quotationInfo.setId(null);
        quotationInfoDao.insert(quotationInfo);
        updateProductList( productList,quotationInfo);
        return new ResultInfo<>(quotationInfo);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<QuotationInfo> addQuotationProducts(final QuotationInfo quotationInfo, final List<QuotationProduct> newprdlist) {
        // 增加新products
        if (!newprdlist.isEmpty()) {
            addProductList( newprdlist,quotationInfo);
        }
        return new ResultInfo<>(quotationInfo);
    }

    /**
     * <pre>
     * 保存报价单商品
     * </pre>
     *
     * @param productList
     * @throws DAOException
     */
    private void saveQuotationProduct(final QuotationInfo quotationInfo, final List<QuotationProduct> productList) {
        for (final QuotationProduct quotationProduct : productList) {
            quotationProduct.setQuotationId(quotationInfo.getId());
            quotationProduct.setAuditStatus(quotationInfo.getAuditStatus());
            quotationProduct.setCreateUser(quotationInfo.getUpdateUser());
            quotationProduct.setUpdateUser(quotationInfo.getUpdateUser());
            // by zhs 01151337 赋默认值
            if (quotationProduct.getBoxProp() == null) {
                quotationProduct.setBoxProp("");
            }
            if (quotationProduct.getProductProp() == null) {
                quotationProduct.setProductProp("");
            }
            //////////////
            quotationProductDao.insert(quotationProduct);
        }
    }

    /**
     * <pre>
     * 更新商品信息
     * </pre>
     *
     * @param productsCur
     * @throws DAOException
     */
    private void updateProductList(List<QuotationProduct> productsCur, final QuotationInfo quotationInfo) {
        if (productsCur == null) productsCur = Collections.EMPTY_LIST;
        Date cur = new Date();
        //final String updateUser = productList.get(0).getUpdateUser();
        // quotationProductDao.updateStatus(quotationInfo.getId(), Constant.DEFAULTED.NO, updateUser);

        QuotationProduct query = new QuotationProduct();
        query.setQuotationId(quotationInfo.getId());
        query.setStatus(Constant.DEFAULTED.YES);
        List<QuotationProduct> productsDB = quotationProductDao.queryByParam(BeanUtil.beanMap(query));
        List<String> skusDB = new ArrayList<>();

            for (QuotationProduct quotationProduct : productsDB) {
                skusDB.add(quotationProduct.getSku());
            }

        List<QuotationProduct> newProducts = new ArrayList<>();
        List<QuotationProduct> modifiedProducts = new ArrayList<>();
        List<Long> deletedProducts = new ArrayList<>();
        List<String> skusCur = new ArrayList<>();
        List<QuotationPriceLog> priceLogs = new ArrayList<>();

        processNewAndModifiedProductsWithPriceLog(productsCur, quotationInfo, productsDB, skusDB, newProducts, modifiedProducts, skusCur, priceLogs);

        processDeletedProducts(productsDB, deletedProducts, skusCur);

        doAddProducts(newProducts);
        doModifiedProducts(modifiedProducts);
        doDeletedProducts(quotationInfo, deletedProducts);
        doAddPriceLogs(priceLogs);
    }


    /**
     * <pre>
     * 增加商品信息
     * 与更新不同的是 不需要删除商品信息
     * </pre>
     *
     * @param productsCur
     * @throws DAOException
     */
    private void addProductList(List<QuotationProduct> productsCur, final QuotationInfo quotationInfo) {
        if (productsCur == null) productsCur = Collections.EMPTY_LIST;
        Date cur = new Date();
        //final String updateUser = productList.get(0).getUpdateUser();
        // quotationProductDao.updateStatus(quotationInfo.getId(), Constant.DEFAULTED.NO, updateUser);

        QuotationProduct query = new QuotationProduct();
        query.setQuotationId(quotationInfo.getId());
        query.setStatus(Constant.DEFAULTED.YES);
        List<QuotationProduct> productsDB = quotationProductDao.queryByParam(BeanUtil.beanMap(query));
        List<String> skusDB = new ArrayList<>();

        for (QuotationProduct quotationProduct : productsDB) {
            skusDB.add(quotationProduct.getSku());
        }

        List<QuotationProduct> newProducts = new ArrayList<>();
        List<QuotationProduct> modifiedProducts = new ArrayList<>();
        List<Long> deletedProducts = new ArrayList<>();
        List<String> skusCur = new ArrayList<>();
        List<QuotationPriceLog> priceLogs = new ArrayList<>();

        processNewAndModifiedProductsWithPriceLog(productsCur, quotationInfo, productsDB, skusDB, newProducts, modifiedProducts, skusCur, priceLogs);

        doAddProducts(newProducts);
        doModifiedProducts(modifiedProducts);
        doAddPriceLogs(priceLogs);
    }

    private void doAddPriceLogs(List<QuotationPriceLog> priceLogs) {
        if(!priceLogs.isEmpty()){
            for(QuotationPriceLog log: priceLogs){
                quotationPriceLogDao.insert(log);
            }
        }
    }

    private void doDeletedProducts(QuotationInfo quotationInfo, List<Long> deletedProducts) {
        if(!deletedProducts.isEmpty()){
            Map<String,Object> param = new HashMap<>();
            param.put("ids",deletedProducts);
            param.put("status",0);
            param.put("updateUser",quotationInfo.getUpdateUser());
            param.put("updateTime",quotationInfo.getUpdateTime());
            quotationProductDao.deleteByIds(param);
        }
    }

    private void doModifiedProducts(List<QuotationProduct> modifiedProducts) {
        if (!modifiedProducts.isEmpty()) {
            for (QuotationProduct quotationProduct : modifiedProducts) {
                quotationProductDao.updateNotNullById(quotationProduct);
            }
        }
    }

    private void doAddProducts(List<QuotationProduct> newProducts) {
        if (!newProducts.isEmpty()) {
            for (QuotationProduct quotationProduct : newProducts) {
                quotationProductDao.insert(quotationProduct);
            }
        }
    }

    private void processNewAndModifiedProductsWithPriceLog(List<QuotationProduct> productsCur, final QuotationInfo quotationInfo, final List<QuotationProduct> productsDB, final List<String> skusDB, final List<QuotationProduct> newProducts, final List<QuotationProduct> modifiedProducts, final List<String> skusCur, final List<QuotationPriceLog> priceLogs) {
        productsCur.forEach(new Consumer<QuotationProduct>() {
            @Override
            public void accept(QuotationProduct productCur) {
                productCur.setQuotationId(quotationInfo.getId());
                productCur.setAuditStatus( quotationInfo.getAuditStatus());
                skusCur.add(productCur.getSku());

                if (skusDB.contains(productCur.getSku())) {
                    QuotationProduct productDB = getTarDBPdt(productCur, productsDB);
                    colLog(productDB, productCur, priceLogs);
                    copy(productDB, productCur);
                    modifiedProducts.add(productDB);
                } else {
                    newProducts.add(productCur);
                }

            }
        });
    }

    private void processDeletedProducts(List<QuotationProduct> productsDB, List<Long> deletedProducts, List<String> skusCur) {
        for (QuotationProduct product : productsDB) {
            if (!skusCur.contains(product.getSku())) {
                deletedProducts.add(product.getId());
            }
        }
    }

    private void copy(QuotationProduct db, QuotationProduct cur) {
        db.setSumPrice(cur.getSumPrice());
        db.setTarrifTaxRate(cur.getTarrifTaxRate());
        db.setFreight(cur.getFreight());
        db.setBasePrice(cur.getBasePrice());
        db.setMulTaxRate(cur.getMulTaxRate());
        db.setSupplyPrice(cur.getSupplyPrice());
        db.setUpdateTime(cur.getUpdateTime());
        db.setUpdateUser(cur.getUpdateUser());
        db.setStandardPrice(cur.getStandardPrice());
        db.setCommissionPercent(cur.getCommissionPercent());

        db.setProductName(cur.getProductName());
        db.setBrandName(cur.getBrandName());
        db.setBrandId(cur.getBrandId());
        db.setBigId(cur.getBigId());
        db.setBigName(cur.getBigName());
        db.setMidId(cur.getMidId());
        db.setBigName(cur.getBigName());
        db.setSmallId(cur.getSmallId());
        db.setSmallName(cur.getSmallName());
    }

    private void colLog(QuotationProduct db, QuotationProduct cur, List<QuotationPriceLog> logs) {
        if (db.getBasePrice() != null && !db.getBasePrice().equals(cur.getBasePrice())) {
            QuotationPriceLog log = new QuotationPriceLog();
            log.setCreateUser(cur.getCreateUser());
            log.setStartDate(db.getUpdateTime());
            log.setEndDate(cur.getCreateTime());
            log.setType(QuotationPriceLogType.BASE_PRICE.getCode());
            log.setPrice(db.getBasePrice());
            log.setQuotationProductId(db.getId());
            logs.add(log);
        }
        if (db.getSumPrice() != null && !db.getSumPrice().equals(cur.getSumPrice())) {
            QuotationPriceLog log = new QuotationPriceLog();
            log.setCreateUser(cur.getCreateUser());
            log.setStartDate(db.getUpdateTime());
            log.setEndDate(cur.getCreateTime());
            log.setPrice(db.getSumPrice());
            log.setType(QuotationPriceLogType.SUM_PRICE.getCode());
            log.setQuotationProductId(db.getId());
            logs.add(log);
        }
        if(db.getFreight() != null && !db.getFreight().equals(cur.getFreight())){
            QuotationPriceLog log = new QuotationPriceLog();
            log.setCreateUser(cur.getCreateUser());
            log.setStartDate(db.getUpdateTime());
            log.setEndDate(cur.getCreateTime());
            log.setPrice(db.getFreight());
            log.setType(QuotationPriceLogType.FREIGHT.getCode());
            log.setQuotationProductId(db.getId());
            logs.add(log);
        }

        if(db.getMulTaxRate() != null && !db.getMulTaxRate().equals(cur.getMulTaxRate())){
            QuotationPriceLog log = new QuotationPriceLog();
            log.setCreateUser(cur.getCreateUser());
            log.setStartDate(db.getUpdateTime());
            log.setEndDate(cur.getCreateTime());
            log.setPrice(db.getMulTaxRate());
            log.setType(QuotationPriceLogType.MUL_TAX_RATE.getCode());
            log.setQuotationProductId(db.getId());
            logs.add(log);
        }

        if(db.getTarrifTaxRate() != null && !db.getTarrifTaxRate().equals(cur.getTarrifTaxRate())){
            QuotationPriceLog log = new QuotationPriceLog();
            log.setCreateUser(cur.getCreateUser());
            log.setStartDate(db.getUpdateTime());
            log.setEndDate(cur.getCreateTime());
            log.setPrice(db.getTarrifTaxRate());
            log.setType(QuotationPriceLogType.TARRIF_TAX_TATE.getCode());
            log.setQuotationProductId(db.getId());
            logs.add(log);
        }

    }

    private QuotationProduct getTarDBPdt(QuotationProduct quotationProduct, List<QuotationProduct> pdtDB) {
        for (QuotationProduct product : pdtDB) {
            if (product.getSku().equals(quotationProduct.getSku())) {
                return product;
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> updateQuotationInfo(final QuotationInfo quotationInfo, final Integer auditStatus) {
        if (null == quotationInfo) {
            return new ResultInfo<>(new FailInfo("参数异常，实体为空"));
        }

        final Long id = quotationInfo.getId();
        if (id == null) {
            return new ResultInfo<>(new FailInfo("更新id非空"));
        }

        final QuotationInfo oldQuotation = quotationInfoDao.queryById(id);
        if (null == oldQuotation) {
            return new ResultInfo<>(new FailInfo("更新的报价单找不到"));
        }
        // by zhs 0120 设置setAuditStatus
        if (auditStatus != null) {
            quotationInfo.setAuditStatus(auditStatus);
        }
//        if (null == quotationInfo.getAuditStatus()) {
//        	quotationInfo.setAuditStatus(oldQuotation.getAuditStatus());
//        }
        quotationInfo.setCreateUser(null);
        quotationInfo.setCreateTime(null);
        quotationInfoDao.updateNotNullById(quotationInfo);
        updateProductList(quotationInfo.getQuotationProductList(), quotationInfo);
        return new ResultInfo<>(Boolean.TRUE);
    }
}
