package com.tp.proxy.mmp.callBack;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ldr on 2016/1/4.
 */
public class AbstractProxy {

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public <T> void execute(ResultInfo<T> result, Callback callback) {
        try {
            callback.process();
        } catch (ServiceException se) {
            LOGGER.error("ServiceException", se);
            result.setMsg(new FailInfo(se.getMessage(), se.getErrorCode()));
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            result.setMsg(new FailInfo("系统异常", -2));
        }
    }
}
