package com.tp.proxy.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wx.ConfigDto;
import com.tp.exception.ServiceException;
import com.tp.service.wx.IOauthService;
import com.tp.util.StringUtil;

/**
 * 微信授权相关
 * @author zhuss
 *
 */
@Service
public class OauthProxy {
	
	private static final Logger log = LoggerFactory.getLogger(OauthProxy.class);
	
	@Autowired
	private IOauthService oauthService;
	
	/**
	 * 通过config接口注入权限验证配置
	 * @param url
	 * @return
	 */
	public ResultInfo<ConfigDto> config(String currtUrl){
		try{
			if(StringUtil.isBlank(currtUrl))throw new ServiceException("接口注入权限签名所需的当前页面为空");
			return new ResultInfo<>(oauthService.config(currtUrl));
		}catch(ServiceException se){
			log.error("[接口注入权限验证配置 ServiceException ] = {}",se.getMessage());
			return new ResultInfo<>(new FailInfo(se.getMessage()));
		}catch(Exception e){
			log.error("[接口注入权限验证配置 EXCEPTION ] = {}",e);
			return new ResultInfo<>(new FailInfo("操作失败"));
		}
	}
}
