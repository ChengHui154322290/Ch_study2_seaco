package com.tp.m.geetest;

import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.geetest.Geetest;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.geetest.GeetestResult;
import com.tp.redis.util.JedisCacheUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ldr on 8/1/2016.
 */

@Service
public class GeetestProcess {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JedisCacheUtil jedisCacheUtil;
    @Autowired
    private GeetestConfig geetestConfig;

    public MResultVO<GeetestResult> preProcess(String userId) {
        AssertUtil.notNull(userId, MResultInfo.PARAM_ERROR);
        GeetestLib gtSdk = new GeetestLib(geetestConfig.getCaptcha_id(), geetestConfig.getPrivate_key());
        int gtServerStatus = gtSdk.preProcess(userId.toString());
        jedisCacheUtil.setCache(gtSdk.gtServerStatusSessionKey, gtServerStatus);

        String resStr = gtSdk.getResponseStr();
        logger.info("GEETEST_PRE_PROCESS:SERVER_STATUS={}",gtServerStatus);
        logger.info("GEETEST_PRE_PROCESS:RESULT={}",resStr);
        GeetestResult result = JsonUtil.parse(resStr, GeetestResult.class);

        return new MResultVO<>(MResultInfo.SUCCESS, result);
    }

    public boolean doProcess(Geetest geetest) {
        GeetestLib gtSdk = new GeetestLib(geetestConfig.getCaptcha_id(), geetestConfig.getPrivate_key());

        int stServerStatusCode = getStatusCode(gtSdk, geetest.getRandid());

        logger.info("GEETEST_ENHANCED_VALIDATION_SERVER_STATUS={}",stServerStatusCode);

        int gtResult;

        if (stServerStatusCode == 1) {
            gtResult = gtSdk.enhencedValidateRequest(geetest.getGeetest_challenge(), geetest.getGeetest_validate(),geetest.getGeetest_seccode(),geetest.getRandid());
            logger.info("GEETEST_ENHANCED_VALIDATION_RESULT={}",gtResult);
        } else {
            logger.warn("GEETEST_FAIL_BACK:USE_OWN_SERVER_CAPTCHA_VALIDATE");
            gtResult = gtSdk.failbackValidateRequest(geetest.getGeetest_challenge(), geetest.getGeetest_validate(),geetest.getGeetest_seccode());
            logger.info("GEETEST_FAIL_BACK_VALIDATION_RESULT={}",gtResult);
        }


        if (gtResult == 1) {
            return true;
        } else {
            return false;
        }

    }

    private int getStatusCode(GeetestLib gtSdk, String userId) {
        Integer i = (Integer) jedisCacheUtil.getCache(gtSdk.gtServerStatusSessionKey);
        if (i == null) {
            int gtServerStatus = gtSdk.preProcess(userId);
            jedisCacheUtil.setCache(gtSdk.gtServerStatusSessionKey, gtServerStatus);
            return i;
        }
        return i;

    }

}
