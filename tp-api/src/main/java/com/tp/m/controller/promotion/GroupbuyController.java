package com.tp.m.controller.promotion;

import com.tp.m.ao.promotion.GroupbuyAO;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.promotion.QueryGroupbuy;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.groupbuy.GroupbuyDetailVO;
import com.tp.m.vo.groupbuy.GroupbuyVO;
import com.tp.m.vo.groupbuy.MyGroupVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ldr on 2016/3/21.
 */
@Controller
@RequestMapping("/groupbuy")
public class GroupbuyController {

    @Autowired
    private GroupbuyAO groupbuyAO;

    @Autowired
    private AuthHelper authHelper;


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public String detail(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryGroupbuy query = (QueryGroupbuy) JsonUtil.getObjectByJsonStr(jsonStr, QueryGroupbuy.class);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_DETAIL,PARAM] = {}", JsonUtil.convertObjToStr(query));
            }
            AssertUtil.notBlank(query.getGbid(), MResultInfo.PARAM_ERROR);
            TokenCacheTO usr = authHelper.authToken(query.getToken());
            query.setUserid(usr.getUid());
            MResultVO<GroupbuyDetailVO> result = groupbuyAO.detail(query);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_DETAIL,RESULT:] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[GROUPBUY_DETAIL,MobileException] = {}", me);
            log.error("[GROUPBUY_DETAIL,RESULT] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        } catch (Exception e) {
            log.error("[GROUPBUY_DETAIL,EXCEPTION] = {}", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.FAILED));
        }
    }
    @ResponseBody
    @RequestMapping(value = "/launch" ,method = RequestMethod.POST)
    public String launch(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryGroupbuy query = (QueryGroupbuy) JsonUtil.getObjectByJsonStr(jsonStr, QueryGroupbuy.class);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_LAUNCH,PARAM] = {}", JsonUtil.convertObjToStr(query));
            }
            AssertUtil.notBlank(query.getGbid(), MResultInfo.PARAM_ERROR);
            TokenCacheTO usr = authHelper.authToken(query.getToken());
            query.setUserid(usr.getUid());
            MResultVO<GroupbuyDetailVO> result = groupbuyAO.launch(query);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_LAUNCH,RESULT:] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[GROUPBUY_LAUNCH  MobileException] = {}", me);
            log.error("[GROUPBUY_LAUNCH  RESULT] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        } catch (Exception e) {
            log.error("[GROUPBUY_LAUNCH ,EXCEPTION] = {}", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.FAILED));
        }
    }
    @ResponseBody
    @RequestMapping(value = "/join" ,method = RequestMethod.POST)
    public String join(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryGroupbuy query = (QueryGroupbuy) JsonUtil.getObjectByJsonStr(jsonStr, QueryGroupbuy.class);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_JOIN,PARAM] = {}", JsonUtil.convertObjToStr(query));
            }
            AssertUtil.notBlank(query.getGbid(), MResultInfo.PARAM_ERROR);
            AssertUtil.notBlank(query.getGid(), MResultInfo.PARAM_ERROR);
            TokenCacheTO usr = authHelper.authToken(query.getToken());
            query.setUserid(usr.getUid());
            MResultVO<GroupbuyDetailVO> result = groupbuyAO.join(query);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_JOIN,RESULT:] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[GROUPBUY_JOIN  MobileException] = {}", me);
            log.error("[GROUPBUY_JOIN  RESULT] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        } catch (Exception e) {
            log.error("[GROUPBUY_JOIN ,EXCEPTION] = {}", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.FAILED));
        }
    }
    @ResponseBody
    @RequestMapping(value = "/my" ,method = RequestMethod.POST)
    public String my(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryGroupbuy query = (QueryGroupbuy) JsonUtil.getObjectByJsonStr(jsonStr, QueryGroupbuy.class);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_MY,PARAM] = {}", JsonUtil.convertObjToStr(query));
            }

            TokenCacheTO usr = authHelper.authToken(query.getToken());
            query.setUserid(usr.getUid());
            MResultVO<MyGroupVO> result = groupbuyAO.my(query);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_MY,RESULT:] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[GROUPBUY_MY  MobileException] = {}", me);
            log.error("[GROUPBUY_MY  RESULT] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        } catch (Exception e) {
            log.error("[GROUPBUY_MY ,EXCEPTION] = {}", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.FAILED));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    public String list(QueryGroupbuy query) {
        try {

            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_LIST,PARAM] = {}", JsonUtil.convertObjToStr(query));
            }


            MResultVO<Page<GroupbuyVO>> result = groupbuyAO.list(query);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_LIST,RESULT:] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[GROUPBUY_LIST  MobileException] = {}", me);
            log.error("[GROUPBUY_LIST  RESULT] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        } catch (Exception e) {
            log.error("[GROUPBUY_LIST ,EXCEPTION] = {}", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.FAILED));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/listforindex" ,method = RequestMethod.GET)
    public String listForIndex(QueryGroupbuy query) {
        try {

            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_LIST,PARAM] = {}", JsonUtil.convertObjToStr(query));
            }

            if(query == null){
                query = new QueryGroupbuy();
            }
            MResultVO<Page<GroupbuyVO>> result = groupbuyAO.listForIndex(query);
            if (log.isInfoEnabled()) {
                log.info("[GROUPBUY_LIST,RESULT:] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[GROUPBUY_LIST  MobileException] = {}", me);
            log.error("[GROUPBUY_LIST  RESULT] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        } catch (Exception e) {
            log.error("[GROUPBUY_LIST ,EXCEPTION] = {}", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.FAILED));
        }
    }


}
