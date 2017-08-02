package com.tp.backend.controller.supplier.purchase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.supplier.SupplierBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.OrderStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.sup.WarehouseOrderFact;
import com.tp.model.sup.PurchaseWarehouse;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.sup.PurchaseWarehouseProxy;
import com.tp.proxy.wms.StockasnProxy;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

@Controller
@RequestMapping("/supplier/")
public class WarehouseOrderController extends SupplierBaseController {

    @Autowired
    private PurchaseWarehouseProxy purchaseWarehouseProxy;

    @Autowired
    private StockasnProxy stockasnProxy;

    public void initSubBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "/warehouseorderList")
    public String warehouseorderList(final Model model, PurchaseWarehouse purchaseWarehouse,
                                     Date bookingStartDate, Date bookingEndDate, Date orderStartDate, Date orderEndDate, Integer page, Integer size) {
        Map<String, Object> params = BeanUtil.beanMap(purchaseWarehouse);
        params.remove("warehouseName");
        params.remove("supplierName");
        params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " id desc");
        List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
        if (StringUtil.isNotBlank(purchaseWarehouse.getWarehouseName())) {
            whereList.add(new DAOConstant.WHERE_ENTRY("warehouse_name", MYBATIS_SPECIAL_STRING.LIKE, purchaseWarehouse.getWarehouseName()));
        }
        if (StringUtil.isNotBlank(purchaseWarehouse.getSupplierName())) {
            whereList.add(new DAOConstant.WHERE_ENTRY("supplier_name", MYBATIS_SPECIAL_STRING.LIKE, purchaseWarehouse.getSupplierName()));
        }
        if (bookingStartDate != null) {
            whereList.add(new DAOConstant.WHERE_ENTRY("booking_date", MYBATIS_SPECIAL_STRING.GT, bookingStartDate));
        }
        if (bookingEndDate != null) {
            whereList.add(new DAOConstant.WHERE_ENTRY("booking_date", MYBATIS_SPECIAL_STRING.LT, bookingEndDate));
        }
        if (orderStartDate != null) {
            whereList.add(new DAOConstant.WHERE_ENTRY("purchase_date", MYBATIS_SPECIAL_STRING.GT, orderStartDate));
        }
        if (orderEndDate != null) {
            whereList.add(new DAOConstant.WHERE_ENTRY("purchase_date", MYBATIS_SPECIAL_STRING.LT, orderEndDate));
        }
        params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
        PageInfo<PurchaseWarehouse> resutlInfo = purchaseWarehouseProxy.queryPageByParam(params, new PageInfo<PurchaseWarehouse>(page, size)).getData();

        model.addAttribute("page", resutlInfo);
        model.addAttribute("purchaseWarehouseDO", purchaseWarehouse);
        model.addAttribute("PURCHARSE_TYPE_MAP", SupplierConstant.PURCHARSE_TYPE_MAP);
        model.addAttribute("PURCHARSE_STATUS_MAP", SupplierConstant.PURCHARSE_STATUS_MAP);

        return "supplier/order/warehouseorder_list";
    }

    @RequestMapping(value = "/warehouseorderAdd", method = RequestMethod.GET)
    public String warehouseorderAdd(final ModelMap model, HttpServletRequest request) {
        model.addAttribute("PURCHARSE_TYPE_MAP", SupplierConstant.PURCHARSE_TYPE_MAP);
        return "supplier/order/warehouseorder_add";
    }

    @RequestMapping(value = "/warehouseOrderSave", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<PurchaseWarehouse> warehouseOrderSave(HttpServletRequest request) {
        PurchaseWarehouse purchaseWarehouse = genWarehouseDTO(request, Boolean.TRUE);
        purchaseWarehouse.setAuditStatus(OrderStatus.EDITING.getStatus());
        return purchaseWarehouseProxy.insert(purchaseWarehouse);
    }

    @RequestMapping("/isWarehouseOrderExist")
    @ResponseBody
    public ResultInfo<PurchaseWarehouse> isWarehouseOrderExist(Long orderCode, String orderType) {
        PurchaseWarehouse purchaseWarehouse = purchaseWarehouseProxy.getWarehouseOrderDetail(orderType, orderCode);
        return new ResultInfo<PurchaseWarehouse>(purchaseWarehouse);
    }

    /**
     * {查询单据的详细信息}.
     *
     * @param request
     * @return
     */
    @RequestMapping("/warehouseOrderDetail")
    public String warehouseOrderDetail(Model model, Long spId) {
        PurchaseWarehouse warehouseOrderVO = purchaseWarehouseProxy.queryById(spId).getData();
        model.addAttribute("warehouseOrderVO", warehouseOrderVO);
        model.addAttribute("PURCHARSE_TYPE_MAP", SupplierConstant.PURCHARSE_TYPE_MAP);
        return "supplier/order/warehouseorder_show";
    }

    /**
     * 提交仓库预约单
     *
     * @return
     */
    @RequestMapping("/warehouseOrderSub")
    @ResponseBody
    public ResultInfo<String> subWarehouseOrder(@RequestParam(value = "spId", required = true) Long spId) {
        if (null != spId) {
            ResultInfo<PurchaseWarehouse> resultInfo = purchaseWarehouseProxy.queryById(spId);
            if (!resultInfo.success) {
                return new ResultInfo<>(resultInfo.msg);
            }
            UserInfo userInfo = getUserInfo();
            return stockasnProxy.sendOrderInfo(resultInfo.getData(), userInfo);
            //return purchaseWarehouseProxy.submitWarehouseOrderInfo(resultInfo.getData());
        }
        return new ResultInfo<>(new FailInfo("参数不能为空"));
    }

    /**
     * 提交仓库预约单
     *
     * @return
     */
    @RequestMapping("/warehouseOrderSaveandSub")
    @ResponseBody
    public ResultInfo<String> saveandSubWarehouseOrder(HttpServletRequest request) {
        PurchaseWarehouse purchaseWarehouse = genWarehouseDTO(request, Boolean.TRUE);
        if (purchaseWarehouse.getId() == null) {
            ResultInfo<PurchaseWarehouse> resultInfo = purchaseWarehouseProxy.insert(purchaseWarehouse);
            if (!resultInfo.success) {
                return new ResultInfo<>(resultInfo.msg);
            }
            purchaseWarehouse = resultInfo.getData();
        }
        //return purchaseWarehouseProxy.submitWarehouseOrderInfo(resultInfo.getData());
        ResultInfo<String> result = stockasnProxy.sendOrderInfo(purchaseWarehouse, getUserInfo());
        //如果推送仓库预约单失败,返回预约单Id到前台,防止多次生成预约单

        if (!result.isSuccess()) {
            FailInfo failInfo = result.getMsg();
            result.setData(purchaseWarehouse.getId().toString());
            result.setMsg(failInfo);
        }
        return result;
    }

    @RequestMapping("/warehouseOrderFact.htm")
    public String warehouseOrderFact(Long id,Model model){

        ResultInfo<List<WarehouseOrderFact>> result = purchaseWarehouseProxy.warehouseOrderFact(id);
        model.addAttribute("result",result);

        return "supplier/order/warehouse_order_fact";
    }

    private PurchaseWarehouse genWarehouseDTO(HttpServletRequest request, boolean b) {

        Long id = getLongVal(request, "id");
        if (id != null) {
            ResultInfo<PurchaseWarehouse> result = purchaseWarehouseProxy.queryById(id);
            if (result.isSuccess() && result.getData() != null) return result.getData();
        }

        PurchaseWarehouse warehouseDTO = new PurchaseWarehouse();
        warehouseDTO.setAuditStatus(OrderStatus.EDITING.getStatus());
        warehouseDTO.setBookingDate(getDate(request, "bookingDate", "yyyy-MM-dd HH:mm:ss"));

        warehouseDTO.setPurchaseId(getLongVal(request, "purchaseCode"));
        warehouseDTO.setPurchaseCode(getStringVal(request, "purchaseCode"));

        warehouseDTO.setPurchaseDate(getDate(request, "purchaseDate", "yyyy-MM-dd"));
        warehouseDTO.setPurchaseType(getStringVal(request, "purchaseType"));
        warehouseDTO.setSupplierId(getLongVal(request, "supplierId"));
        warehouseDTO.setSupplierName(getStringVal(request, "supplierName"));
        warehouseDTO.setWarehouseAddr(getStringVal(request, "warehouseAddr"));
        warehouseDTO.setWarehouseId(getLongVal(request, "warehouseId"));
        warehouseDTO.setWarehouseName(getStringVal(request, "warehouseName"));
        warehouseDTO.setWarehouseLinkmanId(getStringVal(request, "warehouseLinkmanId"));
        warehouseDTO.setWarehouseLinkmanName(getStringVal(request, "warehouseLinkmanName"));
        warehouseDTO.setWarehouseLinkmanTel(getStringVal(request, "telephone"));
        warehouseDTO.setOrderExpectDate(getDate(request, "purchaseExpectDate", "yyyy-MM-dd"));
        warehouseDTO.setCreateUser(super.getUserName());
        warehouseDTO.setUpdateUser(super.getUserName());
        return warehouseDTO;
    }
}
