package com.tp.backend.shiro.freemarker;
import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;

/**
 * 
 * <pre>
 * 自定义一个ShiroTagFreeMarkerConfigurer继承Spring本身提供的FreeMarkerConfigurer,目的是在FreeMarker的Configuration中添加shiro的配置
 * </pre>
 *
 * @author yuwenjie
 * @version $Id: ShiroTagFreeMarkerConfigurer.java, v 0.1 2014年12月31日 下午3:20:26 yuwenjie Exp $
 */
public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer{
	
	@Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    }
}