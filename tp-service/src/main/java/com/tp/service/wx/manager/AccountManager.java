package com.tp.service.wx.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.RequestUtil;
import com.tp.common.vo.wx.RequestUrlConstant;
import com.tp.dto.wx.QrCodeDto;
import com.tp.result.wx.QrcodeResult;
import com.tp.util.JsonUtil;
import com.tp.util.StringUtil;

public class AccountManager extends BaseManager{

	private static final Logger log = LoggerFactory.getLogger(AccountManager.class);
	
	/**
	 * 创建二维码
	 * @param token
	 * @param qrcode
	 * @return
	 */
	public static QrcodeResult createQrcode(String token,QrCodeDto qrcode){
		log.info("[微信API - 创建二维码   入参 token = {},qrcode = {}]",token,JsonUtil.convertObjToStr(qrcode));
		if(null == qrcode || StringUtil.isBlank(token)) return null;
		String url = RequestUrlConstant.QRCODE_CREATE_URL.replace("ACCESS_TOKEN",token).trim();
		String jsonQrcode = JsonUtil.convertObjToStr(qrcode);
		JSONObject jsonObject = RequestUtil.httpsRequest(url.trim(), "POST", jsonQrcode);
		handleError(jsonObject,"创建推广二维码");
		return JSONObject.parseObject(jsonObject.toJSONString(), QrcodeResult.class);
	}
}
