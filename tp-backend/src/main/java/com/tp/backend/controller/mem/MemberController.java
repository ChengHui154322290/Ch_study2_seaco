package com.tp.backend.controller.mem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.mem.MemberInfoProxy;

/**
 * 会员管理
 * @author zhuss
 * @2016年4月14日 上午10:57:34
 */
@Controller
@RequestMapping("/mem")
public class MemberController extends AbstractBaseController{

	private  Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	
	/**
	 * 根据手机号查询用户是否存在
	 * @return
	 */
	@RequestMapping(value="/isExist",method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Boolean> isExist(String mobile){
		try{
			Boolean result = memberInfoProxy.checkMobileExist(mobile);
			logger.info("会员管理 - 根据手机查询用户是否存在 ",result);
			return new ResultInfo<>(result);
		}catch(UserServiceException us){
			logger.error("会员管理 - 根据手机查询用户是否存在 UserServiceException",us);
			return new ResultInfo<>(new FailInfo(us.getMessage()));
		}catch(Exception e){
			logger.error("会员管理 - 根据手机查询用户是否存在 Exception",e);
			return new ResultInfo<>(new FailInfo(e.getMessage()));
		}
	}
	/**
	 * 
	 * updateMemberInfoStatus:(更新会员状态). <br/>  
	 * @author zhouguofeng  
	 * @param mobile  用户手机号码
	 * @param status  状态 1：可用 0：冻结
	 * @return  
	 * @sinceJDK 1.8
	 */
	@RequestMapping(value="/updateMemberInfoStatus",method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer>   updateMemberInfoStatus(String loginName,String status){
		MemberInfo  memberInfo=memberInfoProxy.getMemberInfoService().getMemberInfoByMobile(loginName);//根据手机号码查询会员信息
		if("1".equals(status)){//正常
			memberInfo.setState(Boolean.TRUE);
		}else{//异常
			memberInfo.setState(Boolean.FALSE);
		}
		return memberInfoProxy.updateById(memberInfo);
		
	}
}
