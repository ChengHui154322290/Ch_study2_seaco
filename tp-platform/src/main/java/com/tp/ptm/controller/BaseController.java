package com.tp.ptm.controller;



import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.mmp.ReturnData;
import com.tp.exception.PlatformServiceException;
import com.tp.ptm.ao.TokenServiceAO;
import com.tp.util.DateUtil;

/**
 * 基础控制
 * 
 * @author 项硕
 * @version 2014年11月27日
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TokenServiceAO tokenServiceAO;

    private static final String EXCEPTION_PREFIX = "exception_";

    /**
     * 日期类型参数绑定，格式为 yyyy-MM-dd HH:mm:ss
     * 
     * @param request
     * @param binder
     */
    @InitBinder
    public void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.NEW_FORMAT);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 异常处理
     * 
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public ReturnData exception(HttpServletRequest request, Exception ex) {
        LOGGER.error("inner error", ex);

        if (ex instanceof PlatformServiceException) { // 业务异常
            return new ReturnData(false, ((PlatformServiceException) ex).getErrorCode());
        }
        return new ReturnData(false, "系統錯誤");
    }

    

    protected String getRequestContent(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder content = new StringBuilder();
            String line = null;

            do {
                line = reader.readLine();
                if (line != null) {
                    content.append(line);
                }
            } while (line != null);

            return content.toString();
        } catch (Exception e) {
            LOGGER.error("获取请求数据异常", e);
        }
        return null;
    }

    /**
     * 获取异常消息
     * 
     * @param errorCode
     * @param args
     * @return
     */
    protected String getExceptionMessage(int errorCode, Object... args) {
        return messageSource.getMessage(EXCEPTION_PREFIX + errorCode, args, LocaleContextHolder.getLocale());
    }

    // public static void main(String[] args) {
    // ReturnData returnData = new ReturnData(true, 1100, 222);
    // System.out.println(JSONObject.toJSONString(returnData));
    // }

}
