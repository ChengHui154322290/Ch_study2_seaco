/**  
 * Project Name:xg-service  
 * File Name:AccountManagerServiceTest.java  
 * Package Name:com.tp.test.wx  
 * Date:2016年8月10日下午5:15:55  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/
package com.tp.test.wx;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.service.wx.IAccountManagerService;
import com.tp.util.Base64Util;
import com.tp.util.QRCodeUtil;
import com.tp.test.BaseTest;
import com.tp.test.pay.PayTester;

/**
 * ClassName:AccountManagerServiceTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年8月10日 下午5:15:55 <br/>
 * 
 * @author zhouguofeng
 * @version
 * @since JDK 1.8
 * @see
 */
public class AccountManagerServiceTest extends BaseTest {
	private Logger log = LoggerFactory.getLogger(PayTester.class);
	@Autowired
	IAccountManagerService accountManagerService;

	@Test
	public void testgetQRCodeWidthLogo() {
		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQE58DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0JEcy1pX2psZnc4R2xCSkQzQk5wAAIEL9xjVwMEAAAAAA==";
		String base64String = accountManagerService.getQRCodeWidthLogo(url);
		byte[] imageBytes = Base64Util.decrypt(base64String);
		try {
			BufferedImage bi1 = ImageIO.read(new ByteArrayInputStream(imageBytes));
			QRCodeUtil.saveImage(bi1, "d:/", "hebing.png", "png");
			System.out.println("合并完毕!");

		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}
