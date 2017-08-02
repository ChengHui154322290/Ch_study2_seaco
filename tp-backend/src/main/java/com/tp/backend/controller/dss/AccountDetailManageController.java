package com.tp.backend.controller.dss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.model.dss.AccountDetail;
import com.tp.proxy.dss.AccountDetailProxy;

/**
 * 推广、分销人员流水帐查询
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/accountdetail/")
public class AccountDetailManageController extends AbstractBaseController {
	
	@Autowired
	private AccountDetailProxy accountDetailProxy;
	
	/**
	 * 查询列表
	 * @param model
	 * @param accountDetail
	 */
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void index(Model model,AccountDetail accountDetail){
		model.addAttribute("accountDetail",accountDetail);
	}
	
	@RequestMapping(value="list",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<AccountDetail> list(Model model,AccountDetail accountDetail){
		return accountDetailProxy.queryPageByObject(accountDetail, 
				new PageInfo<AccountDetail>(accountDetail.getStartPage(),accountDetail.getPageSize())).getData();
	}
}
