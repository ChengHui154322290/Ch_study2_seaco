package com.tp.backend.controller.app;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.ImageUtil;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.app.SkinInfo;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.app.SkinInfoProxy;
import com.tp.util.DateUtil;

import javafx.scene.control.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ldr on 2017/1/5.
 */
@Controller
@RequestMapping("/app/skin")
public class SkinInfoController extends AbstractBaseController {

    @Autowired
    private SkinInfoProxy skinInfoProxy;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        Map<String,Object>  param = new HashMap<>();
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name()," id desc");
        ResultInfo<List<SkinInfo>> resultInfo = skinInfoProxy.queryByParam(param);
        model.addAttribute("result", resultInfo);

        return "app/skin/skinIndex";
    }

    @RequestMapping("/skinDetail")
    public String addSkin(Model model, HttpServletRequest request, Long id) {
        if (id != null) {
            ResultInfo<SkinInfo> resultInfo = skinInfoProxy.queryById(id);
            if (resultInfo.isSuccess() && resultInfo.getData() != null) {
                model.addAttribute("skinInfo", resultInfo.getData());
                if (resultInfo.getData() != null) {
                    SkinInfo i = resultInfo.getData();
                    model.addAttribute("icon_a", ImageUtil.getCMSImgFullUrl(i.getIconA()));
                    model.addAttribute("icon_b", ImageUtil.getCMSImgFullUrl(i.getIconB()));
                    model.addAttribute("icon_c", ImageUtil.getCMSImgFullUrl(i.getIconC()));
                    model.addAttribute("icon_d", ImageUtil.getCMSImgFullUrl(i.getIconD()));
                    model.addAttribute("icon_a_sel", ImageUtil.getCMSImgFullUrl(i.getIconASelected()));
                    model.addAttribute("icon_b_sel", ImageUtil.getCMSImgFullUrl(i.getIconBSelected()));
                    model.addAttribute("icon_c_sel", ImageUtil.getCMSImgFullUrl(i.getIconCSelected()));
                    model.addAttribute("icon_d_sel", ImageUtil.getCMSImgFullUrl(i.getIconDSelected()));
                    model.addAttribute("tap_bar", ImageUtil.getCMSImgFullUrl(i.getTapBar()));

                }


            }
        }
        return "app/skin/skinDetail";
    }

    @RequestMapping(value = "/uploadPic/{controlName}", method = RequestMethod.GET)
    public String uploadPic(@PathVariable String controlName, Model model, HttpServletRequest request) {

        System.out.println(controlName);
        model.addAttribute("controlName", controlName);
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.cmsimg.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.cmsimg.name());

        return "app/skin/skinUploadPic";
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultInfo<SkinInfo> save(SkinInfo skinInfo, String startTime, String endTime) {
        try {
            check(skinInfo, startTime, endTime);

            skinInfo.setStartTime(DateUtil.parse(startTime,DateUtil.NEW_FORMAT));
            skinInfo.setEndTime(DateUtil.parse(endTime,DateUtil.NEW_FORMAT));

            Date cur = new Date();
            UserInfo userInfo = getUserInfo();
            skinInfo.setUpdateTime(cur);
            skinInfo.setUpdateUser(userInfo.getUserName());
            if (skinInfo.getId() != null) {
                skinInfoProxy.updateNotNullById(skinInfo);
                return new ResultInfo<>();
            } else {
                skinInfo.setCreateTime(cur);
                skinInfo.setCreateUser(userInfo.getUserName());
                return skinInfoProxy.insert(skinInfo);
            }
        } catch (ServiceException se) {
            logger.error("SAVE_APP_SKIN_ERROR", se);
            return new ResultInfo<>(new FailInfo(se.getMessage(), se.getErrorCode()));
        }catch (Exception e){
            logger.error("SAVE_APP_SKIN_ERROR", e);
            return new ResultInfo<>(new FailInfo("系统异常"));
        }
    }

    private void check(SkinInfo skinInfo, String startTime, String endTime) {
        AssertUtil.notNull(skinInfo, "参数错误");
        AssertUtil.notNull(skinInfo.getName(), "名称为空");
        AssertUtil.notNull(skinInfo.getIconA(), "ICONA为空");
        AssertUtil.notNull(skinInfo.getIconB(), "ICONB为空");
        AssertUtil.notNull(skinInfo.getIconC(), "ICONC为空");
        AssertUtil.notNull(skinInfo.getIconD(), "ICOND为空");
        AssertUtil.notNull(skinInfo.getIconASelected(), "ICONASEL为空");
        AssertUtil.notNull(skinInfo.getIconBSelected(), "ICONBSEL为空");
        AssertUtil.notNull(skinInfo.getIconCSelected(), "ICONCSEL为空");
        AssertUtil.notNull(skinInfo.getIconDSelected(), "ICONDSEL为空");
        AssertUtil.notNull(skinInfo.getTapBar(), "TAPBAR为空");
        AssertUtil.notNull(startTime,"开始时间为空");
        AssertUtil.notNull(endTime,"结束时间为空");
        AssertUtil.notNull(skinInfo.getUnSelectedColor(),"未选中为空");
        AssertUtil.notNull(skinInfo.getSelectedColor(),"选中颜色为空");
    }

}
