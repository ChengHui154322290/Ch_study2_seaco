package com.tp.world.ao.verify;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.kaptcha.Producer;
import com.tp.m.exception.MobileException;
import com.tp.world.helper.cache.CaptchaCacheHelper;

@Service
public class CaptchaAO {
	private static final Logger log = LoggerFactory.getLogger(CaptchaAO.class);
	
	@Autowired
	private Producer captchaProducer;
	
	@Autowired
	private CaptchaCacheHelper captchaCacheHelper;

	/**
	 * 得到验证码
	 * @param key 验证码存放在redis中的key
	 * @throws Exception
	 */
	public void getSecurityCode(HttpServletResponse response,String key){
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");

		String capText = captchaProducer.createText();
		log.info("[获取图形验证码] = {}",capText);
		captchaCacheHelper.getKaptchaCache(key,capText);
		BufferedImage bi = null;
		ServletOutputStream out = null;
		try {
			bi = captchaProducer.createImage(capText);
			out = response.getOutputStream();
			ImageIO.write(bi, "jpg", out);
			out.flush();
		} catch(Exception e){
			throw new MobileException("图形验证码转换图片出错");
		}finally {
			try{if(null != out) out.close();}catch(Exception e){
				throw new MobileException("图形验证码释放资源出错");
			}
		}
	}
}
