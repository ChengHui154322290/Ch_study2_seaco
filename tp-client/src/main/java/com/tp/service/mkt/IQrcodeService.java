package com.tp.service.mkt;


/**
 * 二维码
 * @author zhuss
 */
public interface IQrcodeService {

	/**
	 * 创建二维码：通过微信API生成
	 * @param scene：场景值
	 * @param actionName:二维码类型 QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
	 * @return
	 */
	public String createScanByWX(String scene,String actionName);
	
//	public String createScanByWXNew(String scene,String actionName,Integer choise);
	
	public byte[] createScanByJP(String url);
}
