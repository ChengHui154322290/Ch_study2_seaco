package com.tp.service.wx;

import com.tp.dto.wx.QrCodeDto;

/**
 * 账户管理接口类
 * @author zhuss
 */
public interface IAccountManagerService {

	public String getQrcodeTicket(QrCodeDto qrcode);
	/**
	 * 
	 * getQRCodeWidthLogo:(根据微信二维码url生成带有logo的二维码已Base64制形式返回). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
	 *  
	 * @author zhouguofeng  
	 * @param qrcodeUrl
	 * @return  
	 * @sinceJDK 1.8
	 */
    public String  getQRCodeWidthLogo(String qrcodeUrl);
}
