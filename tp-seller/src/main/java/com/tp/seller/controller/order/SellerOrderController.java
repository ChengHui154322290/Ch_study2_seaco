package com.tp.seller.controller.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tp.common.vo.Constant;
import com.tp.common.vo.FastConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant.StorageType;
import com.tp.common.vo.ord.LogTypeConstant.LOG_TYPE;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.FastUserInfo;
import com.tp.model.ord.OrderStatusLog;
import com.tp.proxy.dss.FastUserInfoProxy;
import com.tp.proxy.ord.OrderStatusLogProxy;
import com.tp.proxy.ord.SubOrderProxy;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.seller.ao.base.SellerCommonAO;
import com.tp.seller.ao.order.SellerOrderAO;
import com.tp.seller.controller.base.BaseController;
import com.tp.seller.domain.SellerOrderDTO;
import com.tp.seller.util.SellerOutConstant;
import com.tp.seller.util.SessionUtils;

/**
 * 商家订单
 *
 * @author yfxie
 */
@Controller
@RequestMapping("/seller/order/")
public class SellerOrderController extends BaseController {

    @Autowired
    private SellerOrderAO sellerOrderAO;
    
    @Autowired
    private SellerCommonAO sellerCommonAO;
    
    @Autowired
    private SubOrderProxy subOrderProxy;
    @Autowired
    private OrderStatusLogProxy orderStatusLogProxy;
    @Autowired
    private FastUserInfoProxy fastUserInfoProxy;

    /**
     * 订单列表
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/orderList", method = RequestMethod.GET)
    public String toorderlist(final ModelMap model, final HttpServletRequest request) {
    	model.addAttribute("orderStatusMap", SellerOutConstant.ORDER_STATUS_MAP);
        model.addAttribute("deliveryWayStr", sellerCommonAO.getStringDeliveryWayOptionStr());
        Map<String,String> orderTypeMap = new HashMap<String,String>();
        if(SessionUtils.checkIsHaitao(request)){
        	orderTypeMap.put(StorageType.OVERSEASMAIL.getValue()+"",StorageType.OVERSEASMAIL.getName());
        	orderTypeMap.put(StorageType.DOMESTIC.getValue()+"",StorageType.DOMESTIC.getName());
        	orderTypeMap.put(StorageType.BONDEDAREA.getValue()+"",StorageType.BONDEDAREA.getName());
        	model.addAttribute("isHaitao","1");
        } else {
        	orderTypeMap.put(StorageType.PLATFORM.getValue()+"",StorageType.PLATFORM.getName());
        	model.addAttribute("isHaitao","0");
        }
        model.addAttribute("orderTypeMap", orderTypeMap);
        return "seller/order/order_list";
    }
    
    /**
     * 订单查询
     * 
     * @return
     */
    @RequestMapping(value = "/orderQuery", method = RequestMethod.POST)
    public ModelAndView orderQuery(final HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView();
        // Map<String,Object> queryMap = geneateSearchMap(request);
        final PageInfo<SellerOrderDTO> sellerOrderPageInfo = sellerOrderAO.queryOrderByCondition(request);
        mav.addObject("page", sellerOrderPageInfo);
        mav.setViewName("seller/order/subpage/item_list");
        return mav;
    }

    /**
     * 订单详情
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/orderDetails", method = RequestMethod.GET)
    public String toviewdetails(final ModelMap model, final HttpServletRequest request, @RequestParam("oid") final Long orderCode) {
        final SellerOrderDTO sellerOrderDTO = sellerOrderAO.getOrderDetail(orderCode);
        model.addAttribute("orderInfo", sellerOrderDTO);
        if(OrderConstant.FAST_ORDER_TYPE.equals(sellerOrderDTO.getType())
  		  && OrderConstant.ORDER_STATUS.DELIVERY.code.equals(sellerOrderDTO.getOrderStatus())){
  			Map<String,Object> params = new HashMap<String,Object>();
  			params.put("enabled", Constant.ENABLED.YES);
			params.put("userType", FastConstant.USER_TYPE.COURIER.code);
  			params.put("warehouseId", sellerOrderDTO.getWarehouseId());
  			List<FastUserInfo> fastUserInfoList = fastUserInfoProxy.queryByParam(params).getData();
  			model.addAttribute("fastUserInfoList", fastUserInfoList);
      	}
        return "seller/order/order_detail";
    }
    
    /**
     * 导出订单信息
     * 
     * @return
     */
    @RequestMapping("/orderExport")
    public void exportOrder(HttpServletRequest request,HttpServletResponse response){
    	//组装查询条件
    	sellerOrderAO.exportOrder(request,response);
    }
    
    /**
     * 订单物流信息
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "orderDelivery")
    public String toshowdetails(final ModelMap model, final Long orderCode) {
        final List<SubOrderExpressInfoDTO> expressLogs = sellerOrderAO.getExpressInfo(orderCode);
        model.addAttribute("expressLogs", expressLogs);
        return "seller/order/track";
    }

    @RequestMapping(value = "receivingorder")
	@ResponseBody
	public ResultInfo<Boolean> receivingOrder(Long orderCode){
		return subOrderProxy.receivingOrder(orderCode,SessionUtils.getSupplierId(),SessionUtils.getUserName(),LOG_TYPE.SELLER);
	}
	
	@RequestMapping(value = "deliveryorder")
	@ResponseBody
	public ResultInfo<Boolean> deliveryOrder(Long orderCode,Long fastUserId,String content){
		return subOrderProxy.deliveryOrder(orderCode,SessionUtils.getSupplierId(),SessionUtils.getUserName(),LOG_TYPE.SELLER,fastUserId,content);
	}
	
	@RequestMapping(value = "log")
	public void log(@RequestParam String subCode, Model model) {
		List<OrderStatusLog> logList = orderStatusLogProxy.queryByOrderCode(subCode);
		model.addAttribute("logList", logList);
	}
}
