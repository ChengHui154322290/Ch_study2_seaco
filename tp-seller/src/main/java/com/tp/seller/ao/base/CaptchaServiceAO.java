package com.tp.seller.ao.base;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.kaptcha.Producer;
import com.tp.seller.constant.SellerConstant;

@Service
public class CaptchaServiceAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceAO.class);

    @Autowired
    private Producer captchaProducer;

    /**
     * <pre>
     * 得到验证码
     * </pre>
     *
     * @param key 验证码存放在session中的key
     * @param count 验证码的个数
     * @throws Exception
     */
    public void getSecurityCode(final HttpSession session, final HttpServletResponse response, final String key, final Integer count) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        final String capText = captchaProducer.createText();
        LOGGER.info("放入session的验证码:key:{} value:{}", key, capText);
        session.setAttribute(key, capText);
        LOGGER.info("放入session的验证码end:key:{} value:{}", key, session.getAttribute(key));
        final BufferedImage bi = captchaProducer.createImage(capText);
        final ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    /**
     * 校验验证码
     *
     * @return
     */
    public boolean checkAuthorCode(final String code, final HttpSession session) {
        final Object obj = session.getAttribute(SellerConstant.SECURITE_CODE);
        LOGGER.info("sessionCode:key:{} value:{}", SellerConstant.SECURITE_CODE, obj);
        if (null != code && code.equalsIgnoreCase((String) obj)) {
            return true;
        }
        return false;
    }

}
