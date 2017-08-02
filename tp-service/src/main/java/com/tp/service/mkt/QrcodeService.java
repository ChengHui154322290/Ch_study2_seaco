package com.tp.service.mkt;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.wx.RequestUrlConstant;
import com.tp.dto.wx.ActionInfo;
import com.tp.dto.wx.QrCodeDto;
import com.tp.dto.wx.Scene;
import com.tp.result.wx.QrcodeResult;
import com.tp.service.mkt.IQrcodeService;
import com.tp.service.wx.cache.VoucherCache;
import com.tp.service.wx.manager.AccountManager;
import com.tp.util.ErWeiMaUtil;
import com.tp.util.StringUtil;

/**
 * 二维码
 * @author zhuss
 */
@Service
public class QrcodeService implements IQrcodeService{
	
	@Autowired
	private VoucherCache voucherCache;
	
	@Override
	public String createScanByWX(String scene,String actionName) {
		QrcodeResult qr = convertQrcodeDto(scene,actionName);
		if(null != qr && StringUtil.isNotBlank(qr.getTicket()))return RequestUrlConstant.QRCODE_SHOW_URL.replace("TICKET", qr.getTicket());
		return null;
	}
//	@Override
//	public String createScanByWXNew(String scene,String actionName,Integer choise) {
//		QrcodeResult qr = convertQrcodeDtoNew(scene,actionName,choise);
//		if(null != qr && StringUtil.isNotBlank(qr.getTicket()))return RequestUrlConstant.QRCODE_SHOW_URL.replace("TICKET", qr.getTicket());
//		return null;
//	}
	/**
	 * 创建场景二维码链接
	 * @param scene
	 * @param type
	 * @return
	 */
	private QrcodeResult convertQrcodeDto(String scene,String actionName){
		QrCodeDto qrcode = new QrCodeDto();
		qrcode.setAction_name(actionName);
		ActionInfo action_info = new ActionInfo();
		action_info.setScene(new Scene(scene));
		qrcode.setAction_info(action_info);
		return AccountManager.createQrcode(voucherCache.getAccessTokenCache(), qrcode);
	}
//	private QrcodeResult convertQrcodeDtoNew(String scene,String actionName,Integer choise){
//		QrCodeDto qrcode = new QrCodeDto();
//		qrcode.setAction_name(actionName);
//		ActionInfo action_info = new ActionInfo();
//		action_info.setScene(new Scene(scene));
//		qrcode.setAction_info(action_info);
//		return AccountManager.createQrcode(voucherCache.getAccessTokenCacheNew(choise), qrcode);
//	}

	@Override
	public byte[] createScanByJP(String url) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ErWeiMaUtil.encoderQRCode(url, output);
		//String path = uploader.upload(output.toByteArray(), Constant.IMAGE_URL_TYPE.cmsimg.name(), "png");
		return output.toByteArray();
	}
}
