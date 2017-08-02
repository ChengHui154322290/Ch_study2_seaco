package com.tp.shop.controller.mkt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.shop.ao.mkt.QrcodeAO;

/**
 * 二维码控制器
 * @author zhuss
 */
@Controller
@RequestMapping("/qrcode")
public class QrcodeController {
	
	@Autowired
	private QrcodeAO qrcodeAO;

	/**
	 * 生成二维码
	 * @param fileName
	 * @param response
	 */
	@RequestMapping("/channel")  
	@ResponseBody
    public String qrcode(String channel,String type){  
		return qrcodeAO.createQrcode(channel, type);
	}
}
