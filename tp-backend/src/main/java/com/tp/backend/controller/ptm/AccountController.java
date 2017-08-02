package com.tp.backend.controller.ptm;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.ptm.ReturnData;
import com.tp.dto.ptm.remote.AccountQO;
import com.tp.model.ptm.PlatformAccount;
import com.tp.proxy.ptm.PlatformAccountProxy;


/**
 * 账户控制器
 * @author hpf
 * @version 2015年5月12日 18：50
 */
@Controller
@RequestMapping("/account") 
public class AccountController extends AbstractBaseController {

	private final static Log log = LogFactory.getLog(AccountController.class);
	
	@Autowired
	private PlatformAccountProxy platformAccountProxy;


	/**
	 * 前往账户列表页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String goAccountListPage(@RequestParam(defaultValue = "1") Integer pageNo, PlatformAccount qo, Model model) {
		qo.setStartPage(pageNo);
		
		model.addAttribute("page", platformAccountProxy.findAccountBackendPage(qo));
		model.addAttribute("query", qo);
		return "account/list";
	}
	
	/**
	 * 前往账户列表页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/supList")
	public String goSupplierListPage(@RequestParam String supplier, Model model) {
		/**
		 * 查询 供应商集合
		 */
		model.addAttribute("supList",platformAccountProxy.findSupplierList(supplier));
		return "account/supList";
	}
	
	
	/**
	 * 前往账户新增也页
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String showAccountAddPage(AccountQO qo, Model model) {
		model.addAttribute("type","add");
		return "account/view";
	}
	
	/**
	 * 前往账户新增也页
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String showAccountEditPage(@RequestParam(defaultValue = "0") String appkey, Model model) {
		model.addAttribute("type","edit");
		/**
		 * 查询账户相关信息
		 */
		model.addAttribute("sub", platformAccountProxy.findAccount4DetailDTOByAppkey(appkey));
		return "account/view";
	}
	
	/**
	 *  操作 - 修改账号
	 * @return
	 */
	@RequestMapping(value = "/opt_edit",method = RequestMethod.POST)
	public String optAccountEdit(@RequestParam String supplier,AccountQO qo, Model model) {
		platformAccountProxy.updateAccount(qo, supplier);
		/**
		 * 修改成功后，自动转入修改界面
		 */
		return "redirect:edit.htm?appkey="+qo.getAppkey();
	}
	
	/**
	 *  操作 - 新增账号
	 * @return
	 */
	@RequestMapping(value = "/opt_add",method = RequestMethod.POST)
	public String optAccountAdd(@RequestParam String supplier,AccountQO qo, Model model) {
		platformAccountProxy.createAccount(qo, supplier);
		/**
		 * 修改成功后，自动转入修改界面
		 */
		return "redirect:add.htm";
	}
	
	/**
	 * 前往账户新增也页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ReturnData optAccountDelete(@RequestParam(defaultValue = "0") String appkey, Model model) {
		try {
			platformAccountProxy.deteleAccount(appkey);
			return new ReturnData(true);
		} catch (Exception e) {
			log.error("后台删除账户失败: ", e);
			return new ReturnData(false, e.getMessage());
		}
	}
}
