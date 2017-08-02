package com.tp.seller.ao.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.seller.ao.base.SellerUploadAO;
import com.tp.seller.constant.ExcelHeaderOrderEnum;
import com.tp.seller.domain.ExcelHeaderOrderDTO;
import com.tp.seller.domain.SellerExcelHeader;
import com.tp.seller.domain.SellerOrderDTO;
import com.tp.seller.domain.SellerOrderProductDTO;
import com.tp.seller.domain.SimpleExcelExportTemplate;
import com.tp.seller.util.ExcelUtil;
import com.tp.seller.util.SellerOutConstant;
import com.tp.seller.util.SessionUtils;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.util.DateUtil;

/**
 * 商家订单
 *
 */
@Service
public class SellerOrderAO extends SellOrderBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerOrderAO.class);

    @Autowired
    private ISalesOrderRemoteService salesOrderRemoteService;

    /**
     * 根据条件查询订单信息	
     *
     * @param queryMap
     * @return
     */

    public PageInfo<SellerOrderDTO> queryOrderByCondition(final HttpServletRequest request) {

        final PageInfo<SellerOrderDTO> page = new PageInfo<SellerOrderDTO>();
        final SubOrderQO qo = new SubOrderQO();

        generateSearchCondition(request, qo);
        final PageInfo<SubOrder4BackendDTO> orderPageInfo = salesOrderRemoteService.findSubOrder4BackendPage(qo);

        page.setPage(orderPageInfo.getPage());
        page.setSize(qo.getPageSize());
        page.setRecords(orderPageInfo.getRecords());
        page.setRows(listConvert(orderPageInfo.getRows()));
        return page;

    }

    /**
     * 生成查询条件
     *
     * @param request
     * @param qo
     * @return
     */
    private void generateSearchCondition(final HttpServletRequest request, final SubOrderQO qo) {
        final Long supplierId = SessionUtils.getSupplierId(request);
        final Long orderCode = getLongValue(request, "orderCode");
        final Date startTime = getDate(request, "startTime", null);
        final Date endTime = getDate(request, "endTime", null);
        final Integer orderStatus = getIntValue(request, "orderStatus");
        final Integer orderType = getIntValue(request, "orderType");
        final Long deliveryWay = getLongValue(request, "deliveryWay");

        Integer startPage = getIntValue(request, "start");
        Integer pageSize = getIntValue(request, "pageSize");

        if (null == startPage) {
        	startPage = 1;
        }
        if (null == pageSize) {
            pageSize = 20;
        }

        qo.setSeaChannel(deliveryWay);
        qo.setType(orderType);
        qo.setSupplierId(supplierId);
        qo.setOrderCode(orderCode);
        qo.setStartTime(startTime);
        qo.setEndTime(endTime);
        qo.setOrderStatus(orderStatus);
        qo.setStartPage(startPage);
        qo.setPageSize(pageSize);
    }

    /**
     * 获取订单详情
     *
     * @param orderCode
     */
    public SellerOrderDTO getOrderDetail(final Long orderCode) {
        SubOrder4BackendDTO subOrderDTO = null;

        try {
            subOrderDTO = salesOrderRemoteService.findSubOrder4BackendByCode(orderCode);
        } catch (final Exception e) {
            LOGGER.info("调用订单详情接口报错，方法：findSubOrder4BackendByCode 参数：{}", orderCode);
            LOGGER.error(e.getMessage(), e);
        }

        return convertOrderInfo(subOrderDTO);
    }

    /**
     * 查询物流信息
     *
     * @param orderCode
     */
    public List<SubOrderExpressInfoDTO> getExpressInfo(final Long orderCode) {

        List<SubOrderExpressInfoDTO> retInfo = null;

        try {
            retInfo = salesOrderRemoteService.queryExpressLogInfo(orderCode, null);
        } catch (final Exception e) {
            LOGGER.info("获取物流异常，方法：queryExpressLogInfo 参数：{}", orderCode);
            LOGGER.error(e.getMessage(), e);
        }

        return retInfo;

    }

    /**
     * 导出订单信息
     *
     * @param request
     * @param response
     */
    public void exportOrder(final HttpServletRequest request, final HttpServletResponse response) {
        final Long st = System.currentTimeMillis();

        String fileName = SellerOutConstant.ORDER_LIST_FILE_NAME + ORDER_DATE_FORMAT.format(new Date()) + ".xls";
        fileName = SellerUploadAO.encodeFilename(fileName, request);

        final SubOrderQO qo = new SubOrderQO();
        // 生成查询条件
        generateSearchCondition(request, qo);
        // 设置时间范围
        setTimeRange(qo);

        final long et1 = System.currentTimeMillis();
        LOGGER.info("seller order excel condition：{}ms", (et1 - st));

        // 查询
        qo.setStartPage(1);
        qo.setPageSize(30000);
        final PageInfo<SubOrder4BackendDTO> orderPageInfo = salesOrderRemoteService.findSubOrder4BackendPage(qo);

        LOGGER.info("seller order excel salesOrderRemoteService.findSubOrder4BackendPageInfo：{}ms", (System.currentTimeMillis() - et1));

        final long et2 = System.currentTimeMillis();
        // 导出
        execExportOrder(listConvert(orderPageInfo.getRows()), fileName, response);

        final Long et = System.currentTimeMillis();
        LOGGER.info("seller order excel export：{}ms", (et - et2));

        LOGGER.info("seller order excel total：{}ms", (et - st));

    }

    private void execExportOrder(final List<SellerOrderDTO> orderList, final String fileName, final HttpServletResponse response) {

        /*
         * 1. 查询 2. 组装 3. 导出
         */
        final SellerExcelHeader header = new ExcelHeaderOrderDTO();
        /**
         * 组装头部信息
         */
        final List<List<String>> dataList = new ArrayList<List<String>>();
        final int colLen = header.getExcelKeyValues().size();

        final Map<String, Integer> indexMap = header.getHeaderIndex();

        for (final SellerOrderDTO sellerOrder : orderList) {
            final List<SellerOrderProductDTO> productList = sellerOrder.getProductList();
            if (null == productList || productList.size() == 0) {
                continue;
            }
            for (final SellerOrderProductDTO productDTO : productList) {
                final String[] oneLineArr = new String[1001];
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.ORDER_CODE.getKeyName())] = ""+sellerOrder.getOrderCode();                
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.ORDER_DATE.getKeyName())] = getOrderDateStr(sellerOrder.getCreateTime());
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.ORDER_STATUS.getKeyName())] = sellerOrder.getZhOrderStatus();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.PAY_TIME_STR.getKeyName())] = getOrderDateStr(sellerOrder.getPayTime());
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.NICK_NAME.getKeyName())] = sellerOrder.getNickName();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.PAY_TYPE.getKeyName())] = sellerOrder.getPayTypeStr();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.PAY_CODE.getKeyName())] = sellerOrder.getPayCode();

//                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.TOTAL_AMOUNT_STR.getKeyName())] = sellerOrder.getTotalAmountStr();
//                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.DIS_COUNT.getKeyName())] = sellerOrder.getDiscount() + "";
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.DELIVERY_WAY.getKeyName())] = sellerOrder.getDeliveryWay();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.PRODUCT_NAME.getKeyName())] = productDTO.getProductName();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.PRODUCT_CODE.getKeyName())] = productDTO.getProductCode();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.BAR_CODE.getKeyName())] = productDTO.getBarCode();

                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.CUSTOM_CODE.getKeyName())] = sellerOrder.getCustomCode();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.UNIT.getKeyName())] = productDTO.getUnit();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.QUANTITY.getKeyName())] = productDTO.getQuantity() + "";
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.PRICE.getKeyName())] = productDTO.getPrice() + "";
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.CONIGNEE_NAME.getKeyName())] = sellerOrder.getConsigneeName();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.MOBILE.getKeyName())] = sellerOrder.getMobile();

                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.POST_CODE.getKeyName())] = sellerOrder.getPostCode();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.PROVINCE.getKeyName())] = sellerOrder.getProvince();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.CITY.getKeyName())] = sellerOrder.getCity();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.COUNTY.getKeyName())] = sellerOrder.getCounty();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.ADDRESS.getKeyName())] = sellerOrder.getAddress();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.IDENTITY_CODE.getKeyName())] = sellerOrder.getIdentityCode();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.REAL_NAME.getKeyName())] = sellerOrder.getRealName();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.EXPRESS_NAME.getKeyName())] = sellerOrder.getExpressName();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.EXPRESS_CODE.getKeyName())] = sellerOrder.getExpressCode();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.PACKAGE_NO.getKeyName())] = sellerOrder.getPackageNo(); // 运单号暂时没有
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.IDENTITY_FRONT_IMG.getKeyName())] =sellerOrder.getIdentifyImageFront();
                oneLineArr[indexMap.get(ExcelHeaderOrderEnum.IDENTITY_BACK_IMG.getKeyName())] = sellerOrder.getIdenfifyImageBack();
                dataList.add(Arrays.asList(ArrayUtils.subarray(oneLineArr, 0, colLen)));
            }
        }
        /**
         * 导出
         */
        final Workbook workbook = ExcelUtil.generateWorkbook(new SimpleExcelExportTemplate(header), dataList, "订单信息");
        ExcelUtil.export(response, fileName, workbook);
    }

    /**
     * 设置时间范围
     *
     * @param qo
     */
    private void setTimeRange(final SubOrderQO qo) {

        final Date startTime = qo.getStartTime();
        final Date endTime = qo.getEndTime();
        Calendar calendar = Calendar.getInstance();
        Date nowTime = calendar.getTime();
        Date thirtyDaysAgo =DateUtil.addDays(nowTime, -31);
        if (null == endTime || endTime.after(nowTime)) {
            qo.setEndTime(nowTime);
        }
        if(qo.getEndTime()!=null){
            thirtyDaysAgo =DateUtil.addDays(qo.getEndTime(), -31);
        }

        if (null == startTime || startTime.before(thirtyDaysAgo) ) {
            qo.setStartTime(thirtyDaysAgo);
        }

    }

}
