package com.tp.seller.controller.order;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.prd.ItemSellerExportInfo;
import com.tp.seller.ao.base.SellerUploadAO;
import com.tp.seller.ao.order.SellerDeliveryAO;
import com.tp.seller.constant.DeliveryOrder;
import com.tp.seller.controller.base.BaseController;
import com.tp.seller.domain.SellerOrderDTO;
import com.tp.seller.util.SessionUtils;

/**
 * 商家发货
 *
 * @author szy
 */
@Controller
@RequestMapping("/seller/delivery/")
public class SellerDeliveryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerDeliveryController.class);
    
    @Autowired
    private SellerDeliveryAO sellerDeliveryAO;
    @Autowired
    private SellerUploadAO sellerUploadAO;

    @RequestMapping(value = "deliveryIndex", method = RequestMethod.GET)
    public void toexcelexport(final Model model, final HttpServletRequest request) {
        final Long supplierId = SessionUtils.getSupplierId(request);
        final ItemSellerExportInfo exportInfo = sellerDeliveryAO.getExportInfo(request);
        if (null != exportInfo) {
            final String exportInfoStr = exportInfo.getExportCol();
            final Long exportInfoId = exportInfo.getId();
            model.addAttribute("exportInfoId", exportInfoId);
            if (null != exportInfoStr && exportInfoStr.length() > 0) {
                model.addAttribute("exportColKeyList", Arrays.asList(exportInfoStr.split(",")));
                model.addAttribute("orderFieldMap", DeliveryOrder.ORDER_FIELD_MAP);
            }
        }
        final PageInfo<SellerOrderDTO> page = sellerDeliveryAO.queryOrderAllWaitingForDelivery(request);
        final Integer totalCount = page.getRecords();
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("supplierId", supplierId);

        final List<ExpressInfo> allExpress = sellerDeliveryAO.selectAllExpressCode();
        model.addAttribute("allExpress", allExpress);
    }

    @RequestMapping(value = "exportOrder", method = RequestMethod.POST)
    public void exportOrder(final HttpServletRequest request, final HttpServletResponse response) {

        // 订单list
        final PageInfo<SellerOrderDTO> page = sellerDeliveryAO.queryOrderAllWaitingForDelivery(request);
        final List<SellerOrderDTO> list = page.getRows();
        try {
            sellerDeliveryAO.exportOrderExcel(list, request, response);// 导出xls
        } catch (final Exception e) {
            LOGGER.info("订单导出异常-----");
            LOGGER.info(e.getMessage(), e);
        }
    }

    /**
     * 上传并导入CSV文件
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ModelAndView upload(final Model model, final HttpServletRequest request) throws Exception {
        final ModelAndView mav = new ModelAndView();
        final File file = sellerDeliveryAO.uploadFile(request);
        ResultOrderDeliverDTO resultOrderDeliverDTO = null;
        try {
            resultOrderDeliverDTO = sellerDeliveryAO.excelHelperImport(file, SessionUtils.getUserId(request));// 导入xls
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.info("导入批量发货异常：" + e.getMessage());
            model.addAttribute("message", e.getMessage());
        } finally {
            if (file.exists()) {// 删除临时文件
                file.delete();
            }
        }
        if (resultOrderDeliverDTO != null) {
            final int size = resultOrderDeliverDTO.getErrorSize();
            if (size > 0) {
                model.addAttribute("orderOperatorErrorList", resultOrderDeliverDTO.getOrderOperatorErrorList());
                model.addAttribute("size", size);
            }
        }
        mav.setViewName("seller/delivery/handle_records");
        return mav;
    }

    /**
     * 导出默认快递公司
     *
     * @param request * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "exportExpress", method = RequestMethod.POST)
    public void exportExpresss(final HttpServletRequest request, final HttpServletResponse response) {
        final List<ExpressInfo> allExpress = sellerDeliveryAO.selectAllExpressCode();
        sellerDeliveryAO.exportExpress(allExpress, response, request);
    }

    @RequestMapping(value = "editCol", method = RequestMethod.POST)
    public ModelAndView editCols(@RequestParam(value = "exportInfoId") final Long exportInfoId, final Model model,
        final HttpServletRequest request, final HttpServletResponse response) {
        final ModelAndView mav = new ModelAndView();
        final Long supplierId = SessionUtils.getSupplierId(request);
        final ItemSellerExportInfo exportInfo = sellerDeliveryAO.getExportInfo(request);
        List<String> exportColNameList = null;
        if (null != exportInfo) {
            final String exportInfoStr = exportInfo.getExportCol();
            if (null != exportInfoStr && exportInfoStr.length() > 0) {
                exportColNameList = Arrays.asList(exportInfoStr.split(","));
            }
        }

        final List<DeliveryOrder> colEnums = new ArrayList<DeliveryOrder>();
        for (final DeliveryOrder value : DeliveryOrder.values()) {
            final String key = value.getKey();
            if (key.equals("expressName") || key.equals("expressCode") || key.equals("packageNo")) {
                continue;
            }
            colEnums.add(value);
        }
        model.addAttribute("exportColNameList", exportColNameList);
        model.addAttribute("colEnums", colEnums);
        model.addAttribute("supplierId", supplierId);
        mav.setViewName("seller/delivery/editCols");
        return mav;
    }

    @RequestMapping(value = "saveCol")
    @ResponseBody
    public ResultInfo<Boolean> saveCol(final String exportCol,final Long exportInfoId, final HttpServletRequest request,
        final HttpServletResponse response) {
        final Long supplierId = SessionUtils.getSupplierId(request);
        final ItemSellerExportInfo exportItemInfo = new ItemSellerExportInfo();
        exportItemInfo.setStatus(Constant.ENABLED.YES);
        exportItemInfo.setSupplierId(supplierId);
        exportItemInfo.setExportCol(exportCol);
        exportItemInfo.setId(exportInfoId);
        exportItemInfo.setCreateUser(supplierId.toString());
        exportItemInfo.setUpdateUser(supplierId.toString());
        final boolean flag = sellerDeliveryAO.saveExportInfoService(exportItemInfo);
        return new ResultInfo<Boolean>(flag);
    }
}
