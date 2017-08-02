package com.tp.proxy.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wx.QrCodeDto;
import com.tp.service.wx.IAccountManagerService;

/**
 * 账户管理代理层
 * @author zhuss
 */
@Service
public class AccountManagerProxy {
	
	private static final Logger log = LoggerFactory.getLogger(AccountManagerProxy.class);
	
	@Autowired
	private IAccountManagerService accountManagerService;
	
	public ResultInfo<String> getQrcodeTicket(QrCodeDto qrcode){
		try{
			return new ResultInfo<>(accountManagerService.getQrcodeTicket(qrcode));
		}catch(Exception e){
			log.error("[微信API -获取带场景值的二维码图片 EXCEPTION] = {}",e);
			return new ResultInfo<>(new FailInfo("操作失败"));
		}
	}
}
