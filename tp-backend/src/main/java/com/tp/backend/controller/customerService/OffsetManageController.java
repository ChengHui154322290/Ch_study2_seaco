package com.tp.backend.controller.customerService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.ord.OffsetConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.OffsetInfo;
import com.tp.model.ord.OffsetLog;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.ord.OffsetInfoProxy;
import com.tp.proxy.ord.OrderInfoProxy;
import com.tp.proxy.ord.RejectInfoProxy;
import com.tp.proxy.ord.SubOrderProxy;
import com.tp.query.ord.OffsetQuery;
import com.tp.query.ord.RejectQuery;
import com.tp.util.BeanUtil;

/**
 * 补偿单管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/customerservice/offset")
public class OffsetManageController extends AbstractBaseController {

	private static final Logger LOG = LoggerFactory.getLogger(OffsetManageController.class);
	@Autowired
	private OffsetInfoProxy offsetInfoProxy;
	@Autowired
	private RejectInfoProxy rejectInfoProxy;
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	@Autowired
	private OrderInfoProxy orderInfoProxy;
	@Autowired
	private SubOrderProxy subOrderProxy;
	/**
	 * 查询补偿单列表
	 * @param model
	 * @param offsetQuery
	 */
	@RequestMapping(value="/{auditType:audit|finalaudit}/list")
	public String list(Model model,OffsetQuery offsetQuery, @PathVariable(value="auditType") String auditType){
		UserInfo userInfo = super.getUserInfo();
		PageInfo<OffsetInfo> pageInfo = offsetInfoProxy.queryByOffsetQuery(offsetQuery);

		Double totalAmount = 0d;
		if(pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getRows())){
			for(OffsetInfo offsetInfo : pageInfo.getRows()){
				totalAmount += (offsetInfo.getOffsetAmount() == null ? 0d : offsetInfo.getOffsetAmount());
			}
		}
		model.addAttribute("totalAmount", totalAmount);
		model.addAttribute("offsetInfoPage", pageInfo);
		model.addAttribute("query", offsetQuery);
		model.addAttribute("offsetStatusList", OffsetConstant.OFFSET_STATUS.values());
		model.addAttribute("reasonList", OffsetConstant.OFFSET_REASON.values());
		model.addAttribute("auditType", auditType);
		return "/customerservice/offset/list";
	}
	
	@RequestMapping(value="apply",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> apply(Model model,OffsetInfo offsetInfo){
		if(null==offsetInfo){
			return new ResultInfo<>(new FailInfo("没有参数"));
		}
		if(null==offsetInfo.getOrderCode()){
			return new ResultInfo<>(new FailInfo("订单编号不能为空"));
		}
		if(null==offsetInfo.getOffsetAmount()){
			return new ResultInfo<>(new FailInfo("补偿金额不能为空"));
		}
		if(null==offsetInfo.getOffsetReason()){
			return new ResultInfo<>(new FailInfo("没选择原因"));
		}
		SubOrder subOrder = subOrderProxy.findSubOrderByCode(offsetInfo.getOrderCode());
		if(subOrder == null){
			return new ResultInfo<>(new FailInfo("订单号不存在"));
		}
		offsetInfo.setCreateUser(getUserName());
		offsetInfo.setUpdateUser(getUserName());
		return offsetInfoProxy.insertOffsetInfo(offsetInfo);
	}
	
	@RequestMapping(value="/{auditType:audit|finalaudit}",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> audit(Model model,Boolean success, @PathVariable(value="auditType") String auditType,OffsetInfo offsetInfo){
		return offsetInfoProxy.audit(offsetInfo, success,auditType);
	}
	
	@RequestMapping(value="/{auditType:audit|finalaudit}/{action:show|audit}",method=RequestMethod.GET)
	public String show(Model model,Long offsetId, @PathVariable(value="auditType")String auditType, @PathVariable(value="action")String action){
		model.addAttribute("auditType",auditType);
		if("audit".equals(action))
			model.addAttribute("audit",true);
		show(model,offsetId,null);
		
		return "/customerservice/offset/show";
	}
	@RequestMapping(value="/{auditType:audit|finalaudit}/apply",method=RequestMethod.GET)
	public String show(Model model){
		show(model,null,"apply");
		return "/customerservice/offset/apply";
	}
	
	public void show(Model model, Long offsetId,String apply){
		OffsetInfo offsetInfo = null;
		SubOrder4BackendDTO order = null;
		List<OffsetLog> logList = null;
		MemberInfo memberInfo = null;
		OrderConsignee orderConsignee = null;
		if(offsetId!=null && StringUtils.isBlank(apply)){
			offsetInfo = offsetInfoProxy.queryById(offsetId).getData();
			if(null==offsetInfo){
				LOG.error("offsetId={} not find datainfo!",offsetId);
				model.addAttribute("errorMessage", "查询不到此补偿单");
				return;
			}
			if(null==offsetInfo.getOrderCode() || !offsetInfo.getOrderCode().toString().startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())){
				LOG.error("offsetId={} orderNo={} not is subOrder code,suborder code prefix is 11",new Object[]{offsetId,offsetInfo.getOrderCode()});
				model.addAttribute("errorMessage", "此补偿单关联子订单有误，订单编号："+offsetInfo.getOrderCode());
				return;
			}
			order = orderInfoProxy.queryOrder(offsetInfo.getOrderCode());
			memberInfo = memberInfoProxy.queryById(order.getSubOrder().getMemberId()).getData();
			logList = offsetInfoProxy.queryOffsetLogListById(offsetId);
			if(null!=order){
				orderConsignee = order.getOrderConsignee();
			}
		}
		if("apply".equals(apply)){
			order = new SubOrder4BackendDTO();
			memberInfo = new MemberInfo();
			orderConsignee = new OrderConsignee();
			model.addAttribute("apply", apply);
		}
		if(offsetInfo != null ){
		    RejectInfo rejectInfo = rejectInfoProxy.queryByOrderNo(offsetInfo.getOrderCode());
	        List<RejectItem> rejectItemList = null ;
	        if(rejectInfo != null){
	        	rejectItemList = rejectInfoProxy.queryRejectItemListByRejectNo(rejectInfo.getRejectCode());
	        }
	        model.addAttribute("rejectInfo",rejectInfo);
	        model.addAttribute("listRejectItem",rejectItemList);
		}
		
		
		
		model.addAttribute("offset", offsetInfo);
		model.addAttribute("order", order.getSubOrder());
		model.addAttribute("buyUser", memberInfo);
		model.addAttribute("orderConsignee", orderConsignee);
		model.addAttribute("logList", logList);
		
		model.addAttribute("offsetTypeList", OffsetConstant.OFFSET_TYPE.values());
		model.addAttribute("offsetReasonList", OffsetConstant.OFFSET_REASON.values());
		model.addAttribute("offsetBearList", OffsetConstant.OFFSET_BEAR.values());
		model.addAttribute("paymentModelList", OffsetConstant.PAYMENT_MODEL.values());
		
	}
	
	@RequestMapping(value={"order"})
	public void order(Model model,Long orderCode){
		try {
			if(orderCode==null){
				model.addAttribute("errorMessage", "订单号不能为空");
				return;
			}
			SubOrder4BackendDTO order = orderInfoProxy.queryOrder(orderCode);
			SubOrder subOrder = order.getSubOrder();
			if (order != null
					&& (ORDER_STATUS.FINISH.code.intValue() == subOrder
							.getOrderStatus().intValue() || ORDER_STATUS.RECEIPT.code
							.intValue() == subOrder.getOrderStatus().intValue())) {
				MemberInfo memberInfo = memberInfoProxy.queryById(subOrder.getMemberId()).getData();
				OrderConsignee orderConsignee = order.getOrderConsignee();
				RejectQuery rejectQuery = new RejectQuery();
				rejectQuery.setOrderCode(orderCode);
				PageInfo<RejectInfo> rejectList = rejectInfoProxy.getRejectInfoList(rejectQuery);
				model.addAttribute("order", subOrder);
				model.addAttribute("buyUser", memberInfo);
				model.addAttribute("orderConsignee", orderConsignee);
				if(rejectList!=null && CollectionUtils.isNotEmpty(rejectList.getRows())){
					model.addAttribute("rejectList", rejectList.getRows());
				}
			} else {
				model.addAttribute("errorMessage", "订单当前状态不允许创建补偿单");
			}
		} catch (Exception e) {
			model.addAttribute("errorMessage",e.getMessage());
		}
	}
	
	@RequestMapping("exportfile")
	public void uploadFile(Model model){
		
	}
	@RequestMapping(value="importtemplate",method=RequestMethod.POST,produces="text/plain")
	@ResponseBody
	public String importTemplate(Model model, HttpServletRequest request)
			throws FileNotFoundException, IOException {
		String savePath = request.getSession().getServletContext().getRealPath("");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		// 上传文件名称
		String fileName = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			fileName = mf.getOriginalFilename();
			int li = fileName.lastIndexOf(".");
			if (li >= 0) {
				String fileSuffixName = fileName.substring(li).toLowerCase();
				if (!".xls".equals(fileSuffixName)) {
					model.addAttribute("msg", "请选择csv或xls文件！");
				}
				fileName = UUID.randomUUID().toString() + fileSuffixName;
			}

			File file = new File(fileName);
			mf.transferTo(file);
			try {
				offsetInfoProxy.importOffsetApply(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
				return "fail";
			}
		}
		return "success";
	}
	
	@RequestMapping("/exporttemplate")
	public void exportTemplate(Model model,OffsetQuery offsetQuery,HttpServletRequest request,  
            HttpServletResponse response) throws Exception{
		response.setContentType("application/vnd.ms-excel");  
        response.setCharacterEncoding("UTF-8");  
        response.setHeader("Content-Disposition", "attachment; filename="+new String("补偿单.xls".getBytes("GB2312"), "ISO8859-1"));  
		PageInfo<OffsetInfo> page = offsetInfoProxy.queryPageByParam(BeanUtil.beanMap(offsetQuery), new PageInfo<OffsetInfo>(1,50000)).getData();
		List<OffsetInfo> offsetList = page.getRows();
		try {
			OutputStream ouputStream = response.getOutputStream();
			outPutList(offsetList, ouputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}     
	}
	
	private void outPutList(List<OffsetInfo> offsetList, OutputStream fout){
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("补偿单"+new Random(10000));  
        HSSFRow row = sheet.createRow(0);  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        String[] cellNames = new String[]{"补偿单号","订单编号","收款人","收款银行","银行账号","补偿金额","交易流水号","付款银行","付款银行账号","优惠券"};
        for(int i=0;i<cellNames.length;i++){
        	 HSSFCell cell = row.createCell(i);
        	 cell.setCellValue(cellNames[i]);  
             cell.setCellStyle(style); 
        } 
        HSSFCell cell = row.createCell(10);
        cell.setCellValue("银行选项:招商银行;");  
        cell.setCellStyle(style);
        if(CollectionUtils.isNotEmpty(offsetList)){
	        for (int i = 0; i < offsetList.size(); i++){  
	            row = sheet.createRow(i + 1);  
	            OffsetInfo offsetInfo =offsetList.get(i);
	            row.createCell(0).setCellValue(offsetInfo.getOffsetCode());
	            row.createCell(1).setCellValue(offsetInfo.getOrderCode());  
	            row.createCell(2).setCellValue(offsetInfo.getPayee());  
	            row.createCell(3).setCellValue(offsetInfo.getPayeeBank());  
	            row.createCell(4).setCellValue(offsetInfo.getBankAccount()); 
	            row.createCell(5).setCellValue(offsetInfo.getOffsetAmount().toString());
	            row.createCell(6).setCellValue(offsetInfo.getSerialNo());
	            row.createCell(7).setCellValue(offsetInfo.getPayBank());
	            row.createCell(8).setCellValue(offsetInfo.getPayBankAccount());
	            row.createCell(9).setCellValue(offsetInfo.getCouponCode()); 
	        } 
        }
        try{  
//            FileOutputStream fout = new FileOutputStream("补偿单"+new Random(10000)+".xls");  
            wb.write(fout);  
            fout.flush();
            fout.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        } 
	}
}
