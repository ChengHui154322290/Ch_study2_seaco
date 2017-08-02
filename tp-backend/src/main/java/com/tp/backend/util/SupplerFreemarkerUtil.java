package com.tp.backend.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class SupplerFreemarkerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplerFreemarkerUtil.class);

    private static Configuration freemarkerConfig;

    @Value("${static.domain}")
    private static String staticPath;

    static {
        if (null == freemarkerConfig) {
            freemarkerConfig = new Configuration();
        }
    }

    /**
     * 生成静态文件.
     *
     * @param templateFilePath 模板路径
     * @param templateFileName 模板文件名,相对htmlskin路径,例如"/tpxw/view.ftl"
     * @param propMap 用于处理模板的属性Object映射
     * @param request
     * @param htmlFilePath 要生成的静态文件的路径,相对设置中的根路径,例如 "/tpxw/1/2005/4/"
     * @param htmlFileName 要生成的文件名,例如 "1.htm"
     * @return boolean true代表生成文件成功
     */
    public String geneHtmlFile(final String templateFilePath, final String templateFileName,
        Map<String, Object> propMap, final HttpServletRequest request) {
        String retStr = "";
        try {
            final String ctPath = request.getSession().getServletContext().getRealPath("/WEB-INF") + "/view/";
            final Template t = this.getFreeMarkerCFG(ctPath + templateFilePath).getTemplate(templateFileName);
            final StringWriter writer = new StringWriter();
            if (null == propMap) {
                propMap = new HashMap<String, Object>();
            }
            propMap.put("domain", WebUtil.getCtx(request));
            t.process(propMap, writer);
            retStr = writer.toString();
        } catch (final TemplateException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return retStr;
    }

    /**
     * 获取freemarker的配置. freemarker本身支持classpath,目录和从ServletContext获取.
     *
     * @param templateFilePath 获取模板路径
     * @return Configuration 返回freemaker的配置属性
     * @throws Exception
     */
    private synchronized Configuration getFreeMarkerCFG(final String templateFilePath) throws Exception {
        try {
            freemarkerConfig.setDirectoryForTemplateLoading(ResourceUtils.getFile(templateFilePath));
            freemarkerConfig.setNumberFormat("0.##########");
            freemarkerConfig.setDefaultEncoding("UTF-8");
        } catch (final Exception ex) {
            throw ex;
        }
        return freemarkerConfig;
    }
}
