package com.tp.backend.controller.mem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.mem.UriConstant;
import com.tp.dto.mem.BackendUserDto;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.util.StringUtil;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(UriConstant.USER.SPACE)
public class UserController {
	
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	
	
	@RequestMapping(value="/toShow")
	public String show(@RequestParam(value="username",required=false) String username,
			Model model) throws Exception {
		if(StringUtil.isBlank(username)) {
			model.addAttribute("isExist", true);
			return "user/user";
		}	
		BackendUserDto backend = memberInfoProxy.getBackendInfoByLoginName(username);
		model.addAttribute("backendObj", backend);
		model.addAttribute("username", username);
		return "user/user";
	}

	@RequestMapping(value="/querymemberinfobylike")
	@ResponseBody
	public List<MemberInfo> queryMemberInfoByLike(Model model,String likeName){
		return memberInfoProxy.queryMemberInfoByLike(likeName).getData();
	}
}
