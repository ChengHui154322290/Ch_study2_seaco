package com.tp.backend.controller.app;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ImageInfo;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.bse.ImageInfoProxy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by ldr on 2016/12/10.
 */
@Controller
@RequestMapping("/app")
public class ImageManageController extends AbstractBaseController {

    @Autowired
    private ImageInfoProxy imageInfoProxy;


    @RequestMapping("/imageManage")
    public String imageManage(HttpServletRequest request, Model model){
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
        return "app/dc/imageManage";
    }

    @ResponseBody
    @RequestMapping("/imageSave")
    public ResultInfo imageSave(HttpServletRequest request,String image){
        if(StringUtils.isBlank(image)) return new ResultInfo(new FailInfo("image is null"));
        UserInfo userInfo = getUserInfo();
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setCode("");
        imageInfo.setBucket(Constant.IMAGE_URL_TYPE.basedata.name());
        imageInfo.setCreateUser(userInfo.getUserName());
        imageInfo.setCreateTime(new Date());
        imageInfo.setImage(image);
        imageInfo.setName("");
        return  imageInfoProxy.insert(imageInfo);
    }

    @ResponseBody
    @RequestMapping("/imageLoad")
    public  List<ImageInfo> imageLoad(HttpServletRequest request, Long id){

        return  imageInfoProxy.queryListWithId(id);


    }
}
