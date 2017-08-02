package com.tp.service.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.dto.wx.QrCodeDto;
import com.tp.result.wx.QrcodeResult;
import com.tp.service.wx.IAccountManagerService;
import com.tp.service.wx.cache.VoucherCache;
import com.tp.service.wx.manager.AccountManager;
import com.tp.util.Base64Util;
import com.tp.util.QRCodeUtil;

/**
 * 账户管理实现类
 * @author zhuss
 */
@Service
public class AccountManagerService implements IAccountManagerService{
	
	@Autowired
	private VoucherCache voucherCache;
	/**logo存放路径*/
	@Value("${xg.logo.image.path}")
    public String  logoImagePath;
	@Override
	public String getQrcodeTicket(QrCodeDto qrcode) {
		QrcodeResult result = AccountManager.createQrcode(voucherCache.getAccessTokenCache(), qrcode);
		return result.getTicket();
	}
    /**
     * 
     * 根据微信二维码url生成带有logo的二维码已二进制Base64形式返回.  
     * @see com.tp.service.wx.IAccountManagerService#getQRCodeWidthLogo(java.lang.String)
     */
	@Override
	public String getQRCodeWidthLogo(String qrcodeUrl) {
		String logoUrl=AccountManagerService.class.getResource(logoImagePath).getPath();  
		byte[] qrcImageBytes=QRCodeUtil.generateQRCode(qrcodeUrl, logoUrl);
		return  Base64Util.encrypt(qrcImageBytes);
	}
	public String getLogoImagePath() {
		return logoImagePath; 
	}
	public void setLogoImagePath(String logoImagePath) {
		this.logoImagePath = logoImagePath;
	}
	
}
