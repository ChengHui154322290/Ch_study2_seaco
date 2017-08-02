package com.tp.backend.controller.cmt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.cmt.BeViewpoint;
import com.tp.model.mem.MemberInfo;
import com.tp.model.prd.ItemDetail;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.cmt.BeViewpointProxy;


/**
 * 
 * <pre>
 * 	西客观点
 * </pre>
 *
 * @author qunxi.shao
 * @version $Id: ViewpointController.java, v 0.1 2015-2-26 下午5:09:23 qunxi.shao Exp $
 */
@Controller
@RequestMapping("/comment/viewpoint")
public class BeViewpointController extends AbstractBaseController{
	
	@Autowired
	private BeViewpointProxy beViewpointProxy;
	
	//step1 通过会员账号获取会员信息
	@RequestMapping("/getMemberInfo")
	@ResponseBody
	public ResultInfo<Map<String,String>> getMemberInfo(Model model,String loginName)
			throws Exception {
		MemberInfo memberInfo = beViewpointProxy.getmemberInfoByLoginName(loginName);
		if(null != memberInfo){
			Map<String, String> map = new HashMap<>();
			map.put("userId", memberInfo.getId()+"");
			map.put("memNickName", memberInfo.getNickName());
			map.put("loginName", memberInfo.getMobile());
			return new ResultInfo<Map<String, String>>(map);
		}
		return new ResultInfo<Map<String, String>>(new FailInfo("查询失败"));
	}
	
	//step2 通过barcode 查询 prdid信息(prdid的id，prdid的商品名称)
	@RequestMapping(value = "getPrdInfo")
	@ResponseBody
	public ResultInfo<Map<String,String>> getPrdInfo(Model model,String barcode)
			throws Exception {
		ItemDetail detail= beViewpointProxy.getPidByBarcode(barcode);
		Map<String,String> map = new  HashMap<String,String>();
		if(null != detail){
			map.put("itemTitle", detail.getMainTitle());
			map.put("detailId", detail.getId()+"");
			map.put("spu", detail.getSpu());
			return new ResultInfo<Map<String,String>>(map);
		}
		return new ResultInfo<Map<String,String>>(new FailInfo("查询失败"));
	}
	
	@RequestMapping(value={"/list"})
	public void list(Model model, BeViewpoint beViewPoint, Integer page, Integer size) throws Exception{
		PageInfo<BeViewpoint> pageInfo = beViewpointProxy.listViewpoints(beViewPoint, new PageInfo<BeViewpoint>(page, size));
		model.addAttribute("page", pageInfo);
		model.addAttribute("query", beViewPoint);
	}
	
	@RequestMapping(value={"/add"})
	public String add() throws Exception{
		return "/comment/viewpoint/add";
	}
	
	@RequestMapping(value={"/edit"})
	public String edit(Model model,Long id) throws Exception{
		BeViewpoint beViewpoint = beViewpointProxy.getViewPointById(id);
		model.addAttribute("view", beViewpoint);
		return "/comment/viewpoint/add";
	}
	
	@RequestMapping(value={"/save"})
	@ResponseBody
	public ResultInfo<Boolean> save(Model model,BeViewpoint viewpoint)
			throws Exception {
		UserInfo userInfo = getUserInfo();
		return beViewpointProxy.save(userInfo, viewpoint);
	}
	
	
	
}
