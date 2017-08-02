package com.tp.seller.ao.order;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.prd.ItemSellerExportInfo;
import com.tp.query.ord.SubOrderQO;
import com.tp.seller.ao.base.SellerUploadAO;
import com.tp.seller.constant.DeliveryOrder;
import com.tp.seller.domain.ExcelHeaderDeliveryOrderDTO;
import com.tp.seller.domain.SellerExcelHeader;
import com.tp.seller.domain.SellerOrderDTO;
import com.tp.seller.domain.SellerOrderProductDTO;
import com.tp.seller.domain.SimpleExcelExportTemplate;
import com.tp.seller.util.ExcelUtil;
import com.tp.seller.util.SellerOutConstant;
import com.tp.seller.util.SessionUtils;
import com.tp.service.bse.IExpressCodeInfoService;
import com.tp.service.bse.IExpressInfoService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.prd.IItemSellerExportInfoService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.util.StringUtil;

@Service
public class SellerDeliveryAO extends SellOrderBase {

    @Autowired
    private IExpressCodeInfoService expressCodeInfoService;

    @Autowired
    private ISalesOrderRemoteService salesOrderRemoteService;

    @Autowired
    private IOutputOrderService outputOrderService;

    @Autowired
    private IItemSellerExportInfoService itemSellerExportInfoService;
    @Autowired
    private IExpressInfoService expressInfoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerDeliveryAO.class);

    /**
     * 物流信息
     * 
     * @param allExpress
     * @param response
     * @param request
     */
    public void exportExpress(final List<ExpressInfo> allExpress, final HttpServletResponse response,
        final HttpServletRequest request) {
        final Workbook wb = new HSSFWorkbook();
        final Sheet sheet = wb.createSheet("默认快递列表");
        sheet.setDefaultColumnWidth(20);
        final CellStyle style = wb.createCellStyle();
        final Font font = wb.createFont();
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(font);
        final Row row1 = sheet.createRow(0);// 表头
        final Cell cell1 = row1.createCell(0);
        final Cell cell2 = row1.createCell(1);
        cell1.setCellStyle(style);
        cell2.setCellStyle(style);
        cell1.setCellValue("快递公司号");
        cell2.setCellValue("快递公司名");
        for (final ExpressInfo express : allExpress) {
            final int index = allExpress.indexOf(express) + 1;
            final Row row = sheet.createRow(index);
            final Cell cell21 = row.createCell(0);
            final Cell cell22 = row.createCell(1);
            cell21.setCellValue(express.getCode());
            cell22.setCellValue(express.getName());
        }
        String fileName = "默认快递列表.xls";
        //fileName = SellerUploadAO.encodeFilename(fileName, request);
        ServletOutputStream fileOut = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            fileOut = response.getOutputStream();
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
            response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("utf-8"), "ISO8859-1") + "\"");
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
            wb.write(fileOut);
            fileOut.close();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取所有快递信息
     */
    public List<ExpressInfo> selectAllExpressCode() {
        return expressInfoService.queryByParam(new HashMap<String,Object>());
    }

    /**
     * 查询未发货订单信息
     *
     * @param queryMap
     * @return
     */

    public PageInfo<SellerOrderDTO> queryOrderAllWaitingForDelivery(final HttpServletRequest request) {

        final PageInfo<SellerOrderDTO> page = new PageInfo<SellerOrderDTO>();
        Integer startPageInfo = getIntValue(request, "start");
        Integer pageSize = getIntValue(request, "pageSize");
        final SubOrderQO qo = new SubOrderQO();
        if (null == startPageInfo) {
            startPageInfo = 1;
        }
        if (null == pageSize) {
            pageSize = 5000;
        }
        qo.setOrderStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);// 设置为待发货状态
        qo.setStartPage(startPageInfo);
        qo.setPageSize(pageSize);
        qo.setSupplierId(SessionUtils.getSupplierId(request));
        final PageInfo<SubOrder4BackendDTO> orderPageInfo = salesOrderRemoteService.findSubOrder4BackendPage(qo);
        page.setPage(orderPageInfo.getPage());
        page.setSize(orderPageInfo.getSize());
        page.setRecords(orderPageInfo.getRecords());
        page.setRows(listConvert(orderPageInfo.getRows()));
        return page;
    }

    /**
     * 设置没有选择的字段的index
     * 
     * @param indexMap
     */
    private void setNoFieldDefaultIndex(final Map<String, Integer> indexMap) {
        for (final DeliveryOrder order : DeliveryOrder.values()) {
            if (!indexMap.containsKey(order.getKeyName())) {
                indexMap.put(order.getKeyName(), 1000);
            }
        }
    }

    /**
     * 生成导出的头部信息
     * 
     * @param orderStr
     */
    private String[] generateExportHeader(final String orderStr) {
        String retArray[] = null;
        final String[] headersRec = orderStr.split(",");
        final List<String> headList = new ArrayList<String>();
        for (int i = 0; i < headersRec.length; i++) {
            if (null != headersRec[i] && DeliveryOrder.ORDER_FIELD_MAP.containsKey(headersRec[i].trim())) {
                headList.add(DeliveryOrder.ORDER_FIELD_MAP.get(headersRec[i].trim()));
            }
        }
        headList.add(DeliveryOrder.EXPRESS_NAME.getKeyName());
        headList.add(DeliveryOrder.EXPRESS_CODE.getKeyName());
        headList.add(DeliveryOrder.PACKAGE_NO.getKeyName());
        retArray = new String[headList.size()];
        return headList.toArray(retArray);
    }

    /**
     * 导出订单
     * 
     * @param orderStr
     * @param list
     * @param request
     * @param response
     * @throws Exception
     */
    public void exportOrderExcel(final List<SellerOrderDTO> list, final HttpServletRequest request,
        final HttpServletResponse response) throws Exception {
        final String expressName = getStringValue(request, "expressName");
        final String expressCode = getStringValue(request, "expressCode");
        String fileName = SellerOutConstant.ORDER_FILER_NAME + ORDER_DATE_FORMAT.format(new Date()) + ".xls";
        fileName = SellerUploadAO.encodeFilename(fileName, request);
        /*
         * 1. 查询 2. 组装 3. 导出
         */
        /**
         * 组装头部信息
         */
        final SellerExcelHeader header = new ExcelHeaderDeliveryOrderDTO();
        final List<List<String>> dataList = new ArrayList<List<String>>();

        final ItemSellerExportInfo exportInfo = getExportInfo(request);
        if (null == exportInfo) {
            throw new Exception("未能获取导出列信息");
        }
        final String headers[] = generateExportHeader(exportInfo.getExportCol());
        if (null == headers) {
            throw new Exception("头部信息异常");
        }
        header.initHeader(Arrays.asList(headers));
        final int colLen = header.getExcelKeyValues().size();

        /**
         * 组装字段对应get方法的列表
         */
        final Map<String, Integer> indexMap = genHeaderIndexMap(Arrays.asList(headers));

        for (final SellerOrderDTO sellerOrder : list) {
            final List<SellerOrderProductDTO> productList = sellerOrder.getProductList();
            if (null == productList || productList.size() == 0) {
                continue;
            }
            for (final SellerOrderProductDTO productDTO : productList) {
                final String[] oneLineArr = new String[1001];
                oneLineArr[indexMap.get(DeliveryOrder.ORDER_CODE.getKeyName())] = ""+sellerOrder.getOrderCode();
                oneLineArr[indexMap.get(DeliveryOrder.ORDER_DATE.getKeyName())] = getOrderDateStr(sellerOrder
                    .getCreateTime());
                oneLineArr[indexMap.get(DeliveryOrder.ORDER_STATUS.getKeyName())] = sellerOrder.getZhOrderStatus();
                oneLineArr[indexMap.get(DeliveryOrder.PAY_TIME_STR.getKeyName())] = getOrderDateStr(sellerOrder
                    .getPayTime());
                oneLineArr[indexMap.get(DeliveryOrder.NICK_NAME.getKeyName())] = sellerOrder.getNickName();
                oneLineArr[indexMap.get(DeliveryOrder.PAY_TYPE.getKeyName())] = sellerOrder.getPayTypeStr();
                oneLineArr[indexMap.get(DeliveryOrder.PAY_CODE.getKeyName())] = sellerOrder.getPayCode();
//                oneLineArr[indexMap.get(DeliveryOrder.TOTAL_AMOUNT_STR.getKeyName())] = sellerOrder
//                    .getTotalAmountStr();
//                oneLineArr[indexMap.get(DeliveryOrder.DIS_COUNT.getKeyName())] = sellerOrder.getDiscount() + "";
                oneLineArr[indexMap.get(DeliveryOrder.CUSTOM_CODE.getKeyName())] = sellerOrder.getCustomCode();
                oneLineArr[indexMap.get(DeliveryOrder.CONIGNEE_NAME.getKeyName())] = sellerOrder.getConsigneeName();
                oneLineArr[indexMap.get(DeliveryOrder.MOBILE.getKeyName())] = sellerOrder.getMobile();
                oneLineArr[indexMap.get(DeliveryOrder.POST_CODE.getKeyName())] = sellerOrder.getPostCode();
                oneLineArr[indexMap.get(DeliveryOrder.PROVINCE.getKeyName())] = sellerOrder.getProvince();
                oneLineArr[indexMap.get(DeliveryOrder.CITY.getKeyName())] = sellerOrder.getCity();
                oneLineArr[indexMap.get(DeliveryOrder.COUNTY.getKeyName())] = sellerOrder.getCounty();
                oneLineArr[indexMap.get(DeliveryOrder.ADDRESS.getKeyName())] = sellerOrder.getAddress();
                oneLineArr[indexMap.get(DeliveryOrder.IDENTITY_CODE.getKeyName())] = sellerOrder.getIdentityCode();
                oneLineArr[indexMap.get(DeliveryOrder.REAL_NAME.getKeyName())] = sellerOrder.getRealName();
                oneLineArr[indexMap.get(DeliveryOrder.TITLE_STR.getKeyName())] = sellerOrder.getTitleStr();
                oneLineArr[indexMap.get(DeliveryOrder.SOURCE_STR.getKeyName())] = sellerOrder.getSourceStr();
                oneLineArr[indexMap.get(DeliveryOrder.REMARK.getKeyName())] = sellerOrder.getRemark();
                oneLineArr[indexMap.get(DeliveryOrder.PRODUCT_NAME.getKeyName())] = productDTO.getProductName();
                oneLineArr[indexMap.get(DeliveryOrder.PRODUCT_CODE.getKeyName())] = productDTO.getProductCode();
                oneLineArr[indexMap.get(DeliveryOrder.BAR_CODE.getKeyName())] = productDTO.getBarCode();
                oneLineArr[indexMap.get(DeliveryOrder.DELIVERY_WAY.getKeyName())] = sellerOrder.getDeliveryWay();
                oneLineArr[indexMap.get(DeliveryOrder.UNIT.getKeyName())] = productDTO.getUnit();
                oneLineArr[indexMap.get(DeliveryOrder.QUANTITY.getKeyName())] = productDTO.getQuantity() + "";
                oneLineArr[indexMap.get(DeliveryOrder.PRICE.getKeyName())] = productDTO.getPrice() + "";
                oneLineArr[indexMap.get(DeliveryOrder.WEIGHT.getKeyName())] = productDTO.getWeight() + "";
                oneLineArr[indexMap.get(DeliveryOrder.BRAND_NAME.getKeyName())] = productDTO.getBrandName();
                oneLineArr[indexMap.get(DeliveryOrder.EXPRESS_NAME.getKeyName())] = expressName;
                oneLineArr[indexMap.get(DeliveryOrder.EXPRESS_CODE.getKeyName())] = expressCode;
                oneLineArr[indexMap.get(DeliveryOrder.PACKAGE_NO.getKeyName())] = ""; // 运单号暂时没有
                dataList.add(Arrays.asList(ArrayUtils.subarray(oneLineArr, 0, colLen)));
            }
        }
        /**
         * 导出
         */
        final SimpleExcelExportTemplate simpleExportDTO = new SimpleExcelExportTemplate(header);
        final Workbook workbook = ExcelUtil.generateWorkbook(simpleExportDTO, dataList,
            SellerOutConstant.ORDER_FILER_NAME);
        ExcelUtil.export(response, fileName, workbook);
    }

    /**
     * excel导入发货
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public ResultOrderDeliverDTO excelHelperImport(final File file, final Long userId) throws Exception {
        final FileInputStream fis = new FileInputStream(file);
        final List<List<String>> rows = excelFileConvertToList(fis);
        if (rows.size() < 2) {
            throw new Exception("导入文件订单列表为空，请检查");
        }
        final List<String> headList = rows.get(0);// 获取表头
        final int headerLen = headList.size();

        checkHeaderInfo(headList);
        // 生成头部字段和索引的对应关系
        final Map<String, Integer> orderHeaderIndex = genHeaderIndexMap(headList);
        // 生成订单行 并校验行的信息
        final Map<String, List<List<String>>> orderCodeLineMap = generateOrderCodeLinesMap(rows, orderHeaderIndex,headerLen);

        final List<OrderDelivery> orderDeliverList = new ArrayList<OrderDelivery>();
        final List<OrderDelivery> oldOrderDeliverList = new ArrayList<OrderDelivery>();
        for (final Map.Entry<String, List<List<String>>> orderLineMap : orderCodeLineMap.entrySet()) {
            final List<List<String>> orderLines = orderLineMap.getValue();
            final String orderCode = orderLineMap.getKey();
            final OrderDelivery orderDeliver = new OrderDelivery();
            final StringBuffer sb = new StringBuffer("");
            for (final List<String> oneLine : orderLines) {
            	String tmpPackNo = oneLine.get(orderHeaderIndex.get(DeliveryOrder.PACKAGE_NO.getKeyName()));
            	if(!StringUtil.isNullOrEmpty(tmpPackNo) && !sb.toString().contains(tmpPackNo)){
            		sb.append(tmpPackNo).append(",");
            	}                        
            }
            final String packageNo = sb.toString().trim();
            if (StringUtil.isEmpty(packageNo)) {
            	LOGGER.info("excelHelperImport：导入发货物流编号为空,ordercode:{}", orderCode);
            	continue;
			}
            orderDeliver.setCompanyId(orderLines.get(0).get(
                orderHeaderIndex.get(DeliveryOrder.EXPRESS_CODE.getKeyName())));
            orderDeliver.setCompanyName(orderLines.get(0).get(
                orderHeaderIndex.get(DeliveryOrder.EXPRESS_NAME.getKeyName())));
            orderDeliver.setOrderCode(Long.valueOf(orderCode));
            orderDeliver.setWeight(0d);
            orderDeliver.setPackageNo(packageNo.substring(0, packageNo.length() - 1));
            orderDeliver.setCreateUser("[供应商]"+userId);// 设置处理人
            orderDeliver.setDeliveryTime(new Date());// 设置发货时间
            orderDeliverList.add(orderDeliver);
            
            OrderDelivery oldOrderDeliver = new OrderDelivery();
            oldOrderDeliver.setCompanyId(orderLines.get(0).get(
                    orderHeaderIndex.get(DeliveryOrder.EXPRESS_CODE.getKeyName())));
            oldOrderDeliver.setCompanyName(orderLines.get(0).get(
                    orderHeaderIndex.get(DeliveryOrder.EXPRESS_NAME.getKeyName())));
            oldOrderDeliver.setWeight(0d);
            oldOrderDeliver.setPackageNo(packageNo.substring(0, packageNo.length() - 1));
            oldOrderDeliver.setCreateUser("[供应商]"+userId);// 设置处理人
            oldOrderDeliver.setDeliveryTime(new Date());// 设置发货时间
            oldOrderDeliverList.add(oldOrderDeliver);           
        }
        ResultOrderDeliverDTO resultOrderDeliverDTO = null;
        try {
        	resultOrderDeliverDTO = outputOrderService.batchExWarehouseService(orderDeliverList);// 导入
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception("订单导入异常");
        }
        return resultOrderDeliverDTO;
    }
    
    /**
     * 校验头部信息
     * 
     * @param headList
     * @return
     * @throws Exception
     */
    private void checkHeaderInfo(final List<String> headList) throws Exception {
        if (!headList.contains(DeliveryOrder.ORDER_CODE.getKeyName())) {
            throw new Exception("要导入文件必须包含订单号一列");
        }
        if (!headList.contains(DeliveryOrder.ORDER_CODE.getKeyName())) {
            throw new Exception("要导入文件必须包含订单号一列");
        }
        if (!headList.contains(DeliveryOrder.EXPRESS_NAME.getKeyName())) {
            throw new Exception("要导入文件必须包含快递名称一列");
        }
        if (!headList.contains(DeliveryOrder.EXPRESS_CODE.getKeyName())) {
            throw new Exception("要导入文件必须包含快递编号一列");
        }
        if (!headList.contains(DeliveryOrder.PACKAGE_NO.getKeyName())) {
            throw new Exception("要导入文件必须包含运单号一列");
        }
    }

    /**
     * 生成header字段对应索引
     * 
     * @param headList
     * @return
     */
    private Map<String, Integer> genHeaderIndexMap(final List<String> headList) {
        final Map<String, Integer> indexMap = new HashMap<String, Integer>();
        if (null != headList && headList.size() > 0) {
            for (int i = 0; i < headList.size(); i++) {
                final String title = headList.get(i);
                if (null != title) {
                    indexMap.put(title.trim(), i);
                }
            }
        }
        /**
         * 对没有选择的字段设置一个默认index
         */
        setNoFieldDefaultIndex(indexMap);
        return indexMap;
    }

    /**
     * 生成订单code和excel行的对应关系
     * 
     * @param rows
     * @param orderHeaderIndex
     * @param headerLen
     * @throws Exception
     */
    private Map<String, List<List<String>>> generateOrderCodeLinesMap(final List<List<String>> rows,
        final Map<String, Integer> orderHeaderIndex, final int headerLen){
        final Map<String, List<List<String>>> orderCodeLineMap = new HashMap<String, List<List<String>>>();
        // 封装orderCode和行的关系
        for (int i = 1; i < rows.size(); i++) {
            final List<String> dataLine = rows.get(i);
            if (null == dataLine) {
               continue;
            }
            final int dateLineLen = dataLine.size();
            if (headerLen > dateLineLen) {
                continue;
            }
            // 获取orderCode
            final Integer index = orderHeaderIndex.get(DeliveryOrder.ORDER_CODE.getKeyName());
            final String orderCode = dataLine.get(index);
            if (orderCode==null) {
                continue;
            }
            List<List<String>> orderCodeLines = new ArrayList<List<String>>();
            if (orderCodeLineMap.containsKey(orderCode)) {
                orderCodeLines = orderCodeLineMap.get(orderCode);
            } else {
                orderCodeLines = new ArrayList<List<String>>();
            }
            orderCodeLines.add(dataLine);
            orderCodeLineMap.put(orderCode, orderCodeLines);
        }
        return orderCodeLineMap;
    }

    public File uploadFile(final HttpServletRequest request) throws Exception {
        File file = null;
        if (request instanceof MultipartHttpServletRequest) {
            final MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            /** 页面控件的文件流 **/
            final MultipartFile multipartFile = multipartRequest.getFile("file");

            final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd/HH");
            /** 构建图片保存的目录 **/
            final String logoPathDir = "/upload" + dateformat.format(new Date());
            /** 得到图片保存目录的真实路径 **/
            final String logoRealPathDir = request.getSession().getServletContext().getRealPath(logoPathDir);
            /** 根据真实路径创建目录 **/
            final File logoSaveFile = new File(logoRealPathDir);
            if (!logoSaveFile.exists()) {
                logoSaveFile.mkdirs();
            }

            // /**获取文件的后缀**/
            final String logImageName = multipartFile.getOriginalFilename();
            /** 拼成完整的文件保存路径加文件 **/
            final String fileName = logoRealPathDir + File.separator + logImageName;
            file = new File(fileName);

            try {
                multipartFile.transferTo(file);
            } catch (final IllegalStateException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("request请求类型不对。");
            throw new Exception("文件上传失败");
        }
        return file;
    }

    public Boolean saveExportInfoService(final ItemSellerExportInfo exportItemInfo) {
    	if (exportItemInfo.getId() != null) {
           itemSellerExportInfoService.updateNotNullById(exportItemInfo);
        } else {
           itemSellerExportInfoService.insert(exportItemInfo);
        }
        return Boolean.TRUE;
    }
    public ItemSellerExportInfo getExportInfo(final HttpServletRequest request) {
        final Long supplierId = SessionUtils.getSupplierId(request);
        final ItemSellerExportInfo exportInfo = new ItemSellerExportInfo();
        exportInfo.setStatus(Constant.ENABLED.YES);
        exportInfo.setSupplierId(supplierId);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("status", Constant.ENABLED.YES);
        params.put("supplierId", supplierId);
        return  itemSellerExportInfoService.queryUniqueByParams(params);
    }
}
