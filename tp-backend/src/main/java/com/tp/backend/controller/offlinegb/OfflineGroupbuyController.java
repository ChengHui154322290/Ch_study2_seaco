package com.tp.backend.controller.offlinegb;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.model.mmp.OlgbHsConfig;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mmp.OlgbHsConfigProxy;
import com.tp.proxy.mmp.TopicItemProxy;
import com.tp.proxy.mmp.TopicProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ldr on 2016/10/19.
 */
@Controller
@RequestMapping("/offlinegb")
public class OfflineGroupbuyController extends AbstractBaseController {
    @Autowired
    private OlgbHsConfigProxy olgbHsConfigProxy;

    @Autowired
    private TopicProxy topicProxy;

    @Autowired
    private TopicItemProxy topicItemProxy;


    @RequestMapping("/home")
    public String home( ) {
        return "offlinegb/offlinegbHome";
    }

    @ResponseBody
    @RequestMapping("/doAddConfig")
    public ResultInfo doAddConfig(  Long topicId, String sku, Integer sort) {
        if (topicId == null || sku == null) return new ResultInfo<>(new FailInfo("参数错误", -1));
        ResultInfo<Topic> resultInfo = topicProxy.queryById(topicId);
        if (!resultInfo.isSuccess()) return resultInfo;
        if (resultInfo.getData() == null) return new ResultInfo(new FailInfo("促销专场不存在", -1));
        if (!resultInfo.getData().getType().equals(TopicType.SUPPLIER_SHOP.ordinal()) || !resultInfo.getData().getStatus().equals(TopicStatus.PASSED.ordinal())) {
            return new ResultInfo(new FailInfo("专场类型不为商家店铺或专场状态不为已通过"));
        }
        TopicItem topicItemQ = new TopicItem();
        topicItemQ.setTopicId(topicId);
        topicItemQ.setSku(sku);
        topicItemQ.setDeletion(DeletionStatus.NORMAL.ordinal());
        ResultInfo<List<TopicItem>> listResultInfo = topicItemProxy.queryByObject(topicItemQ);
        if (!listResultInfo.isSuccess()) return listResultInfo;
        if (CollectionUtils.isEmpty(listResultInfo.getData())) return new ResultInfo(new FailInfo("SKU不存在"));
        OlgbHsConfig configQ = new OlgbHsConfig();
        configQ.setSku(sku);
        configQ.setTopicId(topicId);
        ResultInfo<List<OlgbHsConfig>> queryRes = olgbHsConfigProxy.queryByObject(configQ);
        if(!queryRes.isSuccess() ) return  queryRes;
        if(!CollectionUtils.isEmpty(queryRes.getData())) return new ResultInfo(new FailInfo("已存在",-1));

        ResultInfo<List<OlgbHsConfig>> queryT = olgbHsConfigProxy.queryByObject(new OlgbHsConfig());
        if(!queryT.isSuccess()) return queryT;
        if(queryT.getData()!=null && queryT.getData().size()>6) return  new ResultInfo(new FailInfo("最多配置6个数据"));

        OlgbHsConfig config = new OlgbHsConfig();
        config.setSku(sku);
        config.setTopicId(topicId);
        config.setItemName(listResultInfo.getData().get(0).getName());
        config.setTopicItemId(listResultInfo.getData().get(0).getId());
        config.setTopicName(resultInfo.getData().getName());
        config.setSort(sort == null ? 1 : sort);
        config.setStatus(1);
        Date cue = new Date();
        config.setCreateTime(cue);
        config.setUpdateTime(cue);
        UserInfo user = getUserInfo();
        config.setCreateUser(user.getUserName());
        config.setUpdateUser(user.getUserName());

        ResultInfo result = olgbHsConfigProxy.insert(config);
        return result;

    }


    @RequestMapping("hsConfigList")
    public String config(  Model model) {
        OlgbHsConfig config = new OlgbHsConfig();
        Map<String,Object> param = new HashMap<>();
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(),"sort asc");
        ResultInfo<List<OlgbHsConfig>> resultInfo = olgbHsConfigProxy.queryByParam(param);
        model.addAttribute("list", resultInfo.getData());
        return "offlinegb/offlinegbSsConfigList";
    }

    @RequestMapping("/addConfig")
    public String addConfig( ) {

        return "offlinegb/addConfig";
    }

    @ResponseBody
    @RequestMapping("/delConfig")
    public ResultInfo<Integer> delConfig(  Long id) {

        return  olgbHsConfigProxy.deleteById(id);
    }
}

