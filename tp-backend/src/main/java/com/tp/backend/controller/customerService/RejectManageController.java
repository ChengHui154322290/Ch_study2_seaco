package com.tp.backend.controller.customerService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.common.vo.ord.RejectConstant.REJECT_REASON;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.RejectLog;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.ord.RejectInfoProxy;
import com.tp.proxy.ord.SubOrderProxy;
import com.tp.query.ord.RejectAudit;
import com.tp.query.ord.RejectQuery;
import com.tp.result.ord.RejectAuditDTO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.util.DateUtil;

/**
 * 退货、拒收管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/customerservice/reject/")
public class RejectManageController extends AbstractBaseController {
	
	private static final Logger log = LoggerFactory.getLogger(RejectManageController.class);
	
	@Autowired
	private RejectInfoProxy rejectInfoProxy;
	@Autowired
	private SubOrderProxy subOrderProxy;
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	
	@RequestMapping(value={"list","pagelist"})
	public void list(Model model,RejectQuery rejectQuery){
		
		PageInfo<RejectInfo> rejectPageInfo=null;
		try {
			BeanUtils.describe(rejectQuery);
			rejectPageInfo = rejectInfoProxy.getRejectInfoList(rejectQuery);
		} catch (Exception e) {
			log.error("获取退货单列表异常",e);
		}
		model.addAttribute("rejectPageInfo", rejectPageInfo);
		
		model.addAttribute("rejectStatusList", RejectConstant.REJECT_STATUS.values());
		model.addAttribute("auditStatusList", RejectConstant.REJECT_AUDIT_STATUS.values());
		model.addAttribute("query", rejectQuery);
	}
	
	/**
	 * 退货审核
	 * @param model
	 * @param rejectAudit
	 */
	@RequestMapping("audit")
	@ResponseBody
	public ResultInfo<?> audit(Model model,RejectAudit rejectAudit){
		if(null==rejectAudit){
			return new ResultInfo<>(new FailInfo("没有传入参数"));
		}
		if(null==rejectAudit.getRejectCode()){
			return new ResultInfo<>(new FailInfo("没有传入退货编码"));
		}
		if(null==rejectAudit.getRejectId()){
			return new ResultInfo<>(new FailInfo("没有传入退货ID"));
		}
		if(null==rejectAudit.getRejectItemId()){
			return new ResultInfo<>(new FailInfo("没有传入订单商品ID"));
		}
		if(null==rejectAudit.getSuccess()){
			return new ResultInfo<>(new FailInfo("不知是否成功"));
		}
		rejectAudit.setCreateUser(this.getUserInfo().getLoginName());	
		return  rejectInfoProxy.auditReject(rejectAudit);
		
	}
	
	/**
	 * 查看详情
	 * @param model
	 * @param rejectId
	 */
	@RequestMapping(value={"show","auditshow"})
	public void show(Model model,Long rejectId){
		if(null==rejectId){
			model.addAttribute("errorMessage", "没有传入参数");
			return;
		}
		RejectInfo queryRejectItem = rejectInfoProxy.queryRejectItem(rejectId);
		if(null==queryRejectItem || CollectionUtils.isEmpty(queryRejectItem.getRejectItemList())){
			model.addAttribute("errorMessage", "根据传入参数没有查询到退货信息");
			return ;
		}
		
		SubOrder order=null;
		MemberInfo memberInfo=null;
		List<RejectLog> logList=null;
		try {
			order = subOrderProxy.findSubOrderByCode(queryRejectItem.getOrderCode());
			memberInfo = memberInfoProxy.queryById(order.getMemberId()).getData();
			logList = rejectInfoProxy.queryRejectLog(queryRejectItem.getRejectCode());
		} catch (Exception e) {
			log.error("rejectId="+rejectId+"显示退货单信息出现异常",e);
		}

		Map<String, String> mapRejectReasons = new HashMap<String, String>();
		for(REJECT_REASON entry:REJECT_REASON.values()){
			mapRejectReasons.put(entry.code, entry.cnName);
		}
		
		//收货人信息
		model.addAttribute("rejectreasons", mapRejectReasons);		
		model.addAttribute("rejectItem", queryRejectItem.getRejectItemList().get(0));
		model.addAttribute("rejectInfo", queryRejectItem);
		model.addAttribute("order", order);
		model.addAttribute("customerUser", memberInfo);
		model.addAttribute("userDetail", memberInfo);
		model.addAttribute("logList", logList);
	}
	
	/**
	 * 查看快递信息
	 * @param expressNo
	 */
	@RequestMapping("showDelivery")
	public void showDelivery(Model model,String rejectNo,String expressNo){
		if(null == expressNo){
			model.addAttribute("errorMessage", "expressNo参数为空");
			return;
		}if(null == rejectNo){
			model.addAttribute("errorMessage", "rejectNo参数为空");
			return;
		}
		List<SubOrderExpressInfoDTO> queryExpressInfo = null;
		try {
			queryExpressInfo = rejectInfoProxy.queryExpressInfo(rejectNo, expressNo);
		} catch (Exception e) {
			log.error("rejectNo="+rejectNo+","+"expressNo="+expressNo+"查询物流信息出现异常", e);
		}
		model.addAttribute("queryExpressInfo", queryExpressInfo);
	}
	
	@RequestMapping("/exporttemplate")
	public void exportTemplate(Model model,RejectQuery rejectQuery,HttpServletRequest request,  
            HttpServletResponse response) throws UnsupportedEncodingException{
		response.setContentType("application/vnd.ms-excel");  
        response.setCharacterEncoding("UTF-8");  
        response.setHeader("Content-Disposition", "attachment; filename="+new String("退货.xls".getBytes("GB2312"), "ISO8859-1"));  
		PageInfo<RejectInfo> page = rejectInfoProxy.getRejectInfoList(rejectQuery);
		List<RejectInfo> RejectList = page.getRows();
		try {
			OutputStream ouputStream = response.getOutputStream();
			outPutList(RejectList, ouputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}     
	}
	
	private void outPutList(List<RejectInfo> RejectList, OutputStream fout){
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("退货"+new Random(10000));  
        HSSFRow row = sheet.createRow(0);  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        String[] cellNames = new String[]{"订单编号","退货编号","商品编号","商品名称","退货数量","退款金额","申请时间","退货状态","审核状态"};
        for(int i=0;i<cellNames.length;i++){
        	 HSSFCell cell = row.createCell(i);
        	 cell.setCellValue(cellNames[i]);  
             cell.setCellStyle(style); 
        } 
        if(CollectionUtils.isNotEmpty(RejectList)){
	        for (int i = 0; i < RejectList.size(); i++){  
	            row = sheet.createRow(i + 1);  
	            RejectInfo rejectInfo =RejectList.get(i);
	            row.createCell(0).setCellValue(rejectInfo.getOrderCode());
	            row.createCell(1).setCellValue(rejectInfo.getRejectCode()); 
	            if(CollectionUtils.isNotEmpty(rejectInfo.getRejectItemList())){
	            	RejectItem rejectItem = rejectInfo.getRejectItemList().get(0);
	            	row.createCell(2).setCellValue(rejectItem.getItemSkuCode());  
		            row.createCell(3).setCellValue(rejectItem.getItemName());  
		            row.createCell(4).setCellValue(rejectItem.getItemRefundQuantity()); 
	            }
	            row.createCell(5).setCellValue(rejectInfo.getRefundAmount());
	            row.createCell(6).setCellValue(DateUtil.formatDate(rejectInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));  
	            row.createCell(7).setCellValue(rejectInfo.getZhRejectStatus());  
	            row.createCell(8).setCellValue(rejectInfo.getZhAuditStatus()); 
	        } 
        }
        try{  
            wb.write(fout);  
            fout.flush();
            fout.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        } 
	}
	
	@RequestMapping(value="forceAudit",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> forceAudit(Model model,RejectAuditDTO rejectAudit){
		if(null==rejectAudit){
			return new ResultInfo<>(new FailInfo("没有传入参数"));
		}
		if (null == rejectAudit.getRejectId()) {
			model.addAttribute("errorMessage", "rejectId参数为空");
			return new ResultInfo<>(new FailInfo("rejectId参数为空"));
		}
		rejectAudit.setCreateUser(this.getUserName());
		return rejectInfoProxy.forceAudit(rejectAudit);
	}
	
	
}
