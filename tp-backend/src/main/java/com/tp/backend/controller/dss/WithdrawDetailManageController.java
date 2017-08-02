package com.tp.backend.controller.dss;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.Constant.TF;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.WithdrawDetail;
import com.tp.model.dss.WithdrawLog;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.dss.WithdrawDetailProxy;
import com.tp.proxy.dss.WithdrawLogProxy;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 * 提现管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/withdrawdetail/")
public class WithdrawDetailManageController extends AbstractBaseController {

	@Autowired
	private WithdrawDetailProxy withdrawDetailProxy;
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	@Autowired
	private WithdrawLogProxy withdrawLogProxy;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void index(Model model,WithdrawDetail withdrawDetail){
		model.addAttribute("withdrawDetail", withdrawDetail);
	}
	
	@RequestMapping(value="auditlist",method=RequestMethod.GET)
	public void auditList(Model model,WithdrawDetail withdrawDetail){
		model.addAttribute("withdrawDetail", withdrawDetail);
		model.addAttribute("withdrawStatusList", DssConstant.WITHDRAW_STATUS.values());
	}
	
	@RequestMapping(value="audit",method=RequestMethod.GET)
	public void audit(Model model,Long withdrawDetailId){
		model.addAttribute("withdrawDetailId", withdrawDetailId);
		WithdrawDetail withdrawDetail = withdrawDetailProxy.queryById(withdrawDetailId).getData();
		if(withdrawDetail!=null){
			Integer withdrawStatus = withdrawDetail.getWithdrawStatus(); 
			model.addAttribute("withdrawStatus", withdrawStatus);
			model.addAttribute("withdrawDetailCode", withdrawDetail.getWithdrawDetailCode());
			if(DssConstant.WITHDRAW_STATUS.APPLY.code.equals(withdrawStatus) || DssConstant.WITHDRAW_STATUS.AUDITING.code.equals(withdrawStatus)){
				model.addAttribute("withdrawStatusList", new DssConstant.WITHDRAW_STATUS[]{DssConstant.WITHDRAW_STATUS.PASS,DssConstant.WITHDRAW_STATUS.UNPASS});
			}else if(DssConstant.WITHDRAW_STATUS.PASS.code.equals(withdrawStatus)){
				model.addAttribute("withdrawStatusList", new DssConstant.WITHDRAW_STATUS[]{DssConstant.WITHDRAW_STATUS.PAYED,DssConstant.WITHDRAW_STATUS.UNPAY});
				model.addAttribute("paymentShow", TF.YES);
			}
		}
	}
	
	@RequestMapping(value="loglist")
	@ResponseBody
	public PageInfo<WithdrawLog> logList(Model model,Long withdrawDetailId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("withdrawDetailId", withdrawDetailId);
		return withdrawLogProxy.queryPageByParam(params,new PageInfo<WithdrawLog>(1,100)).getData();
	}
	@RequestMapping(value="list",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<WithdrawDetail> list(Model model,WithdrawDetail withdrawDetail){
		ResultInfo<PageInfo<WithdrawDetail>> resultInfo = withdrawDetailProxy.queryPageByObject(withdrawDetail,
				new PageInfo<WithdrawDetail>(withdrawDetail.getStartPage(),withdrawDetail.getPageSize()));
		if(resultInfo.success && CollectionUtils.isNotEmpty(resultInfo.getData().getRows())){
			List<WithdrawDetail> withdrawDetailList = resultInfo.getData().getRows();
			List<Long> promoterIdList = new ArrayList<Long>();
			for(WithdrawDetail detail:withdrawDetailList){
				promoterIdList.add(detail.getWithdrawor());
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " promoter_id in ("+StringUtil.join(promoterIdList, SPLIT_SIGN.COMMA)+")");
			ResultInfo<List<PromoterInfo>> promoterInfoResult = promoterInfoProxy.queryByParam(params);
			if(promoterInfoResult.success && CollectionUtils.isNotEmpty(promoterInfoResult.getData())){
				for(PromoterInfo promoterInfo:promoterInfoResult.getData()){
					for(WithdrawDetail detail:withdrawDetailList){
						if(promoterInfo.getPromoterId().equals(detail.getWithdrawor())){
							detail.setWithdraworName(promoterInfo.getPromoterName());
							detail.setAlipayAccount(promoterInfo.getAlipay());
							detail.setSurplusAmount(promoterInfo.getSurplusAmount());
						}
					}
				}
			}
		}
		return resultInfo.getData();
	}
	
	@RequestMapping(value="audit",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer> audit(Model model,WithdrawDetail withdrawDetail){
		if(null==withdrawDetail.getWithdrawDetailId()){
			return new ResultInfo<Integer>(new FailInfo("参数有问题"));
		}
		if(null==withdrawDetail.getWithdrawStatus()){
			return new ResultInfo<Integer>(new FailInfo("请选择审核状态"));
		}
		if(DssConstant.WITHDRAW_STATUS.PAYED.code.equals(withdrawDetail.getWithdrawStatus())){
			if(StringUtils.isBlank(withdrawDetail.getPaymentor())){
				return new ResultInfo<Integer>(new FailInfo("请填写打款人"));
			}
			if(null==withdrawDetail.getPayedTime()){
				return new ResultInfo<Integer>(new FailInfo("请填写打款时间"));
			}
		}
		withdrawDetail.setUpdateUser(Constant.AUTHOR_TYPE.USER_OPERATER+super.getUserName());
		return withdrawDetailProxy.audit(withdrawDetail);
	}
	
	@RequestMapping(value="batchaudit",method=RequestMethod.GET)
	public void batchAudit(Model model,String withdrawDetailIds,Boolean payed){
		model.addAttribute("withdrawDetailIdList", withdrawDetailIds.split(SPLIT_SIGN.COMMA));
		if(payed){
			model.addAttribute("withdrawStatusList", new DssConstant.WITHDRAW_STATUS[]{DssConstant.WITHDRAW_STATUS.PAYED,DssConstant.WITHDRAW_STATUS.UNPAY});
			model.addAttribute("paymentShow", TF.YES);
		}else{
			model.addAttribute("withdrawStatusList", new DssConstant.WITHDRAW_STATUS[]{DssConstant.WITHDRAW_STATUS.PASS,DssConstant.WITHDRAW_STATUS.UNPASS});
		}
	}
	
	@RequestMapping(value="batchaudit",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer> batchAudit(Model model,WithdrawDetail withdrawDetail){
		withdrawDetail.setUpdateUser(Constant.AUTHOR_TYPE.USER_OPERATER+super.getUserName());
		return withdrawDetailProxy.batchAudit(withdrawDetail);
	}
	
	@RequestMapping(value="exportlist",method=RequestMethod.POST)
	public void exportList(Model model,WithdrawDetail withdrawDetail, HttpServletResponse response){
		withdrawDetail.setPageSize(10000);
		response.setHeader("Content-disposition", "attachment; filename=withdraw-list.xlsx");
        response.setContentType("application/x-download");
		try {
			PageInfo<WithdrawDetail> page = list(model,withdrawDetail);
			if(page!=null && CollectionUtils.isNotEmpty(page.getRows())){
				String templatePath = "/WEB-INF/classes/template/withdraw-list.xlsx";
				String fileName = "withdraw-list_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("list",page.getRows());
				super.exportXLS(map, templatePath, fileName,response);
			}
		} catch (Exception e) {
			
		}
	}
}
