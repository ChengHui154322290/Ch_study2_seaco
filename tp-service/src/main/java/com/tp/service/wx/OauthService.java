package com.tp.service.wx;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.wx.ConfigDto;
import com.tp.service.wx.IOauthService;
import com.tp.service.wx.cache.VoucherCache;
import com.tp.service.wx.manager.SignManager;
import com.tp.util.DateUtil;
import com.tp.util.RandomUtil;


/**
 * 授权
 * @author zhuss
 *
 */
@Service
public class OauthService implements IOauthService{
	
	@Autowired
	private VoucherCache voucherCache;
	
	@Autowired
	Properties settings;

	@Override
	public ConfigDto config(String url) {
		ConfigDto vo  = new ConfigDto();
		vo.setAppid(settings.getProperty("WX_APPID"));
		vo.setNonceStr(RandomUtil.createRadom(16, 2));
		vo.setTimestamp(DateUtil.getTimestamp()/1000);
		String ticket = voucherCache.getTicketCache();
		vo.setSignature(SignManager.signature2Jsapi(ticket, vo.getNonceStr(), vo.getTimestamp(), url));
		return vo;
	}

}
